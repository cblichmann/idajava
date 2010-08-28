/*
 * IdaJava version 0.3
 * Copyright (c)2007-2010 by Christian Blichmann
 *
 * Plugin Class
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

// Include class definition
#include "idajava_pluginclass.h"

using namespace std;

HMODULE GetCurrentModule()
{
	// Note: Although this function does a little trickery, it is the
	//       recommended way to obtain a handle to the current module. We use
	//       VirtualQuery to obtain MEMORY_BASIC_INFORMATION structure for a
	//       variable in this module's address space. Since the AllocationBase
	//       member of this structure is always the base address after any
	//       relocations (and is guaranteed to be unique throughout the
	//       system, it can be used safely as the handle to the current
	//       module.

	MEMORY_BASIC_INFORMATION mbi;
	static int addressSpaceAnchor;
	VirtualQuery(&addressSpaceAnchor, &mbi, sizeof(mbi));

	return reinterpret_cast<HMODULE>(mbi.AllocationBase);
}

static int idaapi ui_callback(void *user_data, int notification_code,
		va_list va)
{
	if (notification_code == ui_ready_to_run)
		IdaJavaPlugin::getInstance()->notifyGuiInitialized();
	return 0;
}

// Initialize static member
IdaJavaPlugin *IdaJavaPlugin::m_instance = 0;

bool IdaJavaPlugin::readRegConfig(HKEY rootkey, LPCSTR subkey)
{
	HKEY key;
	char buf[MAX_PATH];

	if (RegOpenKeyEx(rootkey, subkey, 0, KEY_READ | KEY_WOW64_32KEY,
			&key) != ERROR_SUCCESS)
		return false;

	// TODO: Allow to pass arbitrary configuration options to Java

	if (RegQueryStringValue(key, CONFIG_NAME_JVMCLASSPATH,
			static_cast<char *>(buf), MAX_PATH))
		m_jvmClassPath = buf;
	
	if (RegQueryStringValue(key, CONFIG_NAME_JVMWORKINGDIRECTORY,
			static_cast<char *>(buf), MAX_PATH))
		m_jvmWorkingDirectory = buf;

	if (RegQueryStringValue(key, CONFIG_NAME_PLUGINJAVACLASS,
			static_cast<char *>(buf), MAX_PATH))
		m_pluginJavaClassName = buf;
	
	if (RegQueryStringValue(key, CONFIG_NAME_EMBEDDEDFRAMECLASS,
			static_cast<char *>(buf), MAX_PATH))
		m_params[CONFIG_NAME_EMBEDDEDFRAMECLASS] = buf;

	{
	DWORD loglevel(0);
	if (RegQueryUInt32Value(key, CONFIG_NAME_LOGLEVEL, loglevel)) {
		m_logLevel = loglevel;
		m_params[CONFIG_NAME_LOGLEVEL] = string("" + m_logLevel);
	}
	}
	
	if (RegQueryStringValue(key, CONFIG_NAME_RMIREGISTRY,
			static_cast<char *>(buf), MAX_PATH))
		m_params[CONFIG_NAME_RMIREGISTRY] = buf;
	
	if (RegQueryStringValue(key, CONFIG_NAME_RMIPOOLSERVEROBJECTNAME,
			static_cast<char *>(buf), MAX_PATH))
		m_params[CONFIG_NAME_RMIPOOLSERVEROBJECTNAME] = buf;
	
	RegCloseKey(key);
	return true;
}

bool IdaJavaPlugin::readConfig()
{
	// Set defaults
	m_jvmClassPath = CONFIG_VALUE_JVMCLASSPATH;
	m_jvmWorkingDirectory = CONFIG_VALUE_JVMWORKINGDIRECTORY;
	m_pluginJavaClassName = CONFIG_VALUE_PLUGINJAVACLASS;
	m_params[CONFIG_NAME_EMBEDDEDFRAMECLASS] =
		CONFIG_VALUE_EMBEDDEDFRAMECLASS;
	m_params[CONFIG_NAME_RMIREGISTRY] = CONFIG_VALUE_RMIREGISTRY;
	m_params[CONFIG_NAME_RMIPOOLSERVEROBJECTNAME] =
		CONFIG_VALUE_RMIPOOLSERVEROBJECTNAME;

	// Read from HKCU first, then from HKLM
	bool haveConfig(readRegConfig(HKEY_CURRENT_USER,
		REGKEY_HKCU_PLUGIN_ROOT));
	haveConfig = haveConfig || readRegConfig(HKEY_LOCAL_MACHINE,
		REGKEY_HKLM_PLUGIN_ROOT);

	// TODO: Implement plugin options
	const char *options(get_plugin_options("idajava"));
	if (options != 0)
		msg("Info: Ignored plugin command line options: \"%s\"\n", options);
	
	return haveConfig;
}

IdaJavaPlugin *IdaJavaPlugin::getInstance()
{
	if (m_instance == 0)
		m_instance = new IdaJavaPlugin();
	return m_instance;
}

void IdaJavaPlugin::destroyInstance()
{
	if (m_instance == 0)
		return;
	delete m_instance;
	m_instance = 0;
}

int IdaJavaPlugin::initialize()
{
 	// Only initialize once
	if (m_initDone)
		return PLUGIN_KEEP;
	m_initDone = true;

	// Display version and copyright information
	msg("%s\n", APP_BANNER);

	// Read configuration
	if (!readConfig()) {
		msg("Error: Cannot read plugin configuration, skipping "
			"plugin\n");
		return PLUGIN_SKIP;
	}

	char buf[MAX_PATH];
	if (GetModuleFileName(GetCurrentModule(), buf, MAX_PATH) == 0) {
		msg("Error: Cannot determine module path, skipping plugin\n");
		return PLUGIN_SKIP;
	}
	m_moduleFileName = buf;

	if (m_jvmWorkingDirectory.empty())
		// Extract the directory part of the module file name
		// FIXME: Determine platform-specific path separator and use it
		m_jvmWorkingDirectory = string(m_moduleFileName.begin(),
			m_moduleFileName.begin() + m_moduleFileName.rfind('\\'));

	if (!SetCurrentDirectory(m_jvmWorkingDirectory.c_str()))
		msg("Warning: Failed to change working directory to \"%s\"\n",
			m_jvmWorkingDirectory.c_str());

	// Find and create a suitable Java VM
	msg("Initializing in-process Java VM...");
	JavaVMCreator jvc;
	jvc.addVMOption(("-Djava.class.path=" + m_jvmClassPath).c_str());
	jvc.addVMOption("-Xcheck:jni");
	if (!jvc.findJRE(MIN_JVM_VERSION)) {
		msg("\nError: Cannot find a suitable Java VM, skipping plugin\n");
		return PLUGIN_SKIP;
	}
	if (!jvc.createVM(&m_jvm, &m_env)) {
		msg("\nError: Cannot create Java VM, skipping plugin\n");
		return PLUGIN_SKIP;
	}
	msg("OK\n");

	// Register to receive IDA UI events
	if (!hook_to_notification_point(HT_UI, ui_callback, 
			reinterpret_cast<void *>(HOOKDATA_IDAJAVA_COOKIE))) {
		msg("Error: Cannot register to receive UI messages, skipping plugin\n");
		return PLUGIN_SKIP;
	}

	return PLUGIN_KEEP;
}

void IdaJavaPlugin::notifyGuiInitialized(void) {
	m_lateInitDone = true;

	// Call Java plugins late in the init process;
	callJavaPluginInitialize();
}

bool IdaJavaPlugin::createJavaPlugin()
{
	jmethodID mid;
	jstring jstr;

	m_javaPluginClass = m_env->FindClass(m_pluginJavaClassName.c_str());
	if (m_javaPluginClass == 0) {
		msg("\nError: Plugin Java class %s not found, skipping plugin\n",
			m_pluginJavaClassName.c_str());
		return false;
	}

	mid = m_env->GetMethodID(m_javaPluginClass, "<init>",
		"(Ljava/lang/String;)V");
	if (mid == 0) {
		msg("\nError: Plugin class has no constructor with signature " \
			"(Ljava/lang/String;)V, skipping plugin\n");
		return false;
	}

	jstr = m_env->NewStringUTF(m_moduleFileName.c_str());
	if (jstr == 0) {
		msg("\nError: Cannot create UTF string, skipping plugin\n");
		return false;
	}

	m_javaPluginObj = m_env->NewObject(m_javaPluginClass, mid, jstr);
	if (m_javaPluginObj == 0) {
		msg("\nError: Cannot create Java object, skipping plugin\n");
		return false;
	}

	msg("OK\n");
	return true;
}

bool IdaJavaPlugin::checkHandleJavaException()
{
//	AttachCurrentThread attach(&m_jvm, &m_env);

	// Exit early if there is no pending exception
	if (m_env != 0 && !m_env->ExceptionCheck())
		return false;

	// From this point on we know for sure an exception happened. Now we try
	// to provide at least some of the information a normal Java program
	// would output when encountering an exception.
	jthrowable e = m_env->ExceptionOccurred();
	
	// Save some method IDs for future use
	static jmethodID throwableGetClass(0);
	static jmethodID throwableGetMessage(0);
	static jmethodID classGetName(0);
	if (throwableGetClass == 0) {
		// The calls to FindClass and GetMethodID should never fail, since
		// we're asking for core Java classes here.
		jclass cls;

		cls = m_env->FindClass("java/lang/Throwable");
		assert(cls != 0);
		throwableGetClass = m_env->GetMethodID(cls, "getClass",
			"()Ljava/lang/Class;");
		assert(throwableGetClass != 0);
		throwableGetMessage = m_env->GetMethodID(cls, "getMessage",
			"()Ljava/lang/String;");
		assert(throwableGetMessage);
		m_env->DeleteLocalRef(cls);

		cls = m_env->FindClass("java/lang/Class");
		classGetName = m_env->GetMethodID(cls, "getName",
			"()Ljava/lang/String;");
		assert(classGetName != 0);
		m_env->DeleteLocalRef(cls);
	}

	// Get exception class
	jobject c = m_env->CallObjectMethod(e, throwableGetClass);
	jstring className_jstr = (jstring) m_env->CallObjectMethod(c, classGetName);
	const char *className = m_env->GetStringUTFChars(className_jstr, JNI_FALSE);

	// Get exception message
	jstring message_jstr = (jstring) m_env->CallObjectMethod(e,
		throwableGetMessage);
	const char *message = m_env->GetStringUTFChars(message_jstr, JNI_FALSE);
	
	msg("Java Exception %s: %s\n", className, message);

	// Free JNI allocated strings
	m_env->ReleaseStringUTFChars(message_jstr, message);
	m_env->ReleaseStringUTFChars(className_jstr, className);

	// Clear the exception, so JNI is available again
	m_env->ExceptionClear();
	return true;
}

/*#include <sstream>
#include "va_pass.h"
static callui_t (idaapi *g_savedCallui)(ui_notification_t what, ...) = 0;
callui_t idaapi spy_callui(ui_notification_t what, ...)
{
	callui_t result;
	va_list args;

	va_start(args, what);
	ostringstream os;
	os << what << endl;
	OutputDebugString(os.str().c_str());
	result = g_savedCallui(what, va_pass(args));
	va_end(args);

	return result;
}*/

int IdaJavaPlugin::callJavaPluginInitialize()
{
/*	g_savedCallui = callui;
	callui = spy_callui;//*/

	AttachCurrentThread attach(&m_jvm, &m_env);
	msg("Invoking Java stub plugin...");
	if (!createJavaPlugin())
		return PLUGIN_SKIP;

	jmethodID mid = m_env->GetMethodID(m_javaPluginClass, "initialize", "()I");

	// If the initialize()-method was not found, just issue a warning and
	// assume the plugin needs no initialization and return PLUGIN_KEEP
	if (mid == 0) {
		msg("Info: No initialize()-method found for Java plugin, assuming " \
			"it does not need initialization\n");
		return PLUGIN_KEEP;
	}

	// Call the plugin's initialize()-method
	long result = m_env->CallIntMethod(m_javaPluginObj, mid);

	// Skip plugin if there was an exception
	if (checkHandleJavaException())
		return PLUGIN_SKIP;

	return PLUGIN_KEEP;//(int) result;
}

void IdaJavaPlugin::callJavaPluginRun(int arg)
{
//	AttachCurrentThread attach(&m_jvm, &m_env);
	jmethodID mid = m_env->GetMethodID(m_javaPluginClass, "run", "(I)V");
	if (mid == 0) {
		msg("Warning: Java plugin has no run()-method, nothing to be done\n");
		return;
	}

	// Call the plugin's run()-method
	m_env->CallVoidMethod(m_javaPluginObj, mid, (jint) arg);
	checkHandleJavaException();
}

void IdaJavaPlugin::callJavaPluginTerminate(void)
{
	AttachCurrentThread attach(&m_jvm, &m_env);
	jmethodID mid = m_env->GetMethodID(m_javaPluginClass, "terminate", "()V");
	if (mid == 0) {
		msg("Info: No terminate()-method found for Java plugin\n");
		return;
	}

	// Call the plugin's terminate()-method
	m_env->CallVoidMethod(m_javaPluginObj, mid);
	checkHandleJavaException();
}

void IdaJavaPlugin::run(int arg)
{
	if (!m_lateInitDone)
		return;

	if (m_env == 0 || m_javaPluginClass == 0) {
		msg("Error: Java not initialized and plugin invocation requested\n");
		return;
	}
	callJavaPluginRun(arg);
}

void IdaJavaPlugin::terminate()
{
	// Call terminate()-method if Java was ever initialized
	if (m_env == 0 || m_javaPluginClass == 0)
		callJavaPluginTerminate();

	// Unregister UI event notification (placed here in case terminate()
	// needed it)
	unhook_from_notification_point(HT_UI, ui_callback,
		reinterpret_cast<void *>(HOOKDATA_IDAJAVA_COOKIE));

	if (m_env == 0)
		return;

	{
//	AttachCurrentThread attach(&m_jvm, &m_env);
	// Call System.exit(0) to ensure there are no Java threads. Ignore all
	// errors and exceptions here. FindClass and GetStaticMethodID shouldn't
	// fail anyway, since java.lang.System is a core Java class.
m_jvm->AttachCurrentThreadAsDaemon((void**)&m_env, 0);
	jclass cls = m_env->FindClass("java/lang/System");
	jmethodID mid = m_env->GetStaticMethodID(cls, "exit", "(I)V");
	m_env->CallVoidMethod(m_javaPluginObj, mid, 0);
	// TODO: Use the mutex from the initialize()-method to determine whether
	//       we're exiting IDA for good or just open another database
	msg("Unloading Java...");
	}
//	m_jvm->DetachCurrentThread();
	m_jvm->DestroyJavaVM();
	msg("OK\n");
}

const char *IdaJavaPlugin::getParameter(const char *name)
{
	map<string, string>::iterator i = m_params.find(name);
	return (i != m_params.end()) ? i->second.c_str() : 0;
}

/**
 * @brief Callback for the custom window procedure for embedded Ida forms.
 *
 * @param[in] handle  The handle to the current window
 * @param[in] wParam  The first message parameter
 * @param[in] lParam  The second message parameter
 *
 * @return The value returned by call to
 *     @a IdaJavaPlugin::embeddedWindowProc().
 * 
 */
static LRESULT CALLBACK embeddedWindowProcCallback(HWND handle, UINT uMsg,
		WPARAM wParam, LPARAM lParam)
{ 
	return IdaJavaPlugin::getInstance()->embeddedWindowProc(handle, uMsg,
			wParam, lParam);
}

LRESULT IdaJavaPlugin::embeddedWindowProc(HWND handle, UINT uMsg,
		WPARAM wParam, LPARAM lParam)
{
	if (uMsg == WM_ERASEBKGND)
		return 1;
	if (uMsg == WM_SIZING)
	{
		msg(".");
	}

	WNDPROC prevWndProc(reinterpret_cast<WNDPROC>(GetWindowLong(handle,
			GWL_USERDATA)));
	if (prevWndProc == 0)
		return DefWindowProc(handle, uMsg, wParam, lParam);
	return CallWindowProc(prevWndProc, handle, uMsg, wParam, lParam);
}

bool IdaJavaPlugin::initIdaEmbeddedWindow(TForm *form)
{
	HWND handle = get_tform_handle(form);
	RECT rect;
	while (GetClientRect(handle, &rect)) {
		if (rect.right < 10000)
			break;
		handle = GetParent(handle);
	}

	WNDPROC prevWndProc = reinterpret_cast<WNDPROC>(GetWindowLongPtr(handle,
			GWL_WNDPROC));
	if (prevWndProc == 0) {
		msg("Error: Could not get previous window procedure\n");
		return false;
	}
	SetLastError(0);
	SetWindowLongPtr(handle, GWL_USERDATA,
			reinterpret_cast<LONG_PTR>(prevWndProc));
	if (GetLastError() != 0)
	{
		msg("Error: Could not set user data on window\n");
		return false;
	}
	SetLastError(0);
	SetWindowLongPtr(handle, GWL_WNDPROC, reinterpret_cast<LONG_PTR>(
			embeddedWindowProcCallback));
	if (GetLastError() != 0)
	{
		msg("Error: Could not set window procedure\n");
		return false;
	}
	return true;
}
