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

// IDA SDK includes
#define USE_DANGEROUS_FUNCTIONS
#define USE_STANDARD_FILE_FUNCTIONS
#pragma warning(push)
#pragma warning(disable: 4267) // netnode.hpp: Conversion nodeidx_t <-> size_t
#pragma warning(disable: 4996) // pro.h: unsafe use of ctime()
#include <ida.hpp>
#include <idp.hpp>
#include <expr.hpp>
#include <bytes.hpp>
#include <loader.hpp>
#include <kernwin.hpp>
#pragma warning(pop)

#include "idajava_pluginclass.h" // Include class definition
#include "idajava_windows.h"

using namespace std;

static int idaapi ui_callback(void *user_data, int notification_code,
		va_list va)
{
	if (notification_code == ui_ready_to_run)
		idajava_plugin::instance()->notify_late_init_done();
	return 0;
}

// Initialize static member
idajava_plugin *idajava_plugin::instance_ = 0;

bool idajava_plugin::read_reg_config(HKEY rootkey, LPCSTR subkey)
{
	HKEY key;
	char buf[MAX_PATH];

	if (RegOpenKeyEx(rootkey, subkey, 0, KEY_READ | KEY_WOW64_32KEY,
			&key) != ERROR_SUCCESS)
		return false;

	if (RegQueryStringValue(key, CONFIG_NAME_JVMCLASSPATH,
			static_cast<char *>(buf), MAX_PATH))
		jvm_class_path_ = buf;
	
	if (RegQueryStringValue(key, CONFIG_NAME_JVMWORKINGDIRECTORY,
			static_cast<char *>(buf), MAX_PATH))
		jvm_working_directory_ = buf;

	if (RegQueryStringValue(key, CONFIG_NAME_PLUGINJAVACLASS,
			static_cast<char *>(buf), MAX_PATH))
		java_plugin_class_name_ = buf;
	
	if (RegQueryStringValue(key, CONFIG_NAME_EMBEDDEDFRAMECLASS,
			static_cast<char *>(buf), MAX_PATH))
		params_[CONFIG_NAME_EMBEDDEDFRAMECLASS] = buf;

	{
	DWORD loglevel(0);
	if (RegQueryUInt32Value(key, CONFIG_NAME_LOGLEVEL, loglevel)) {
		log_level_ = loglevel;
		params_[CONFIG_NAME_LOGLEVEL] = string("" + log_level_);
	}
	}
	
	if (RegQueryStringValue(key, CONFIG_NAME_RMIREGISTRY,
			static_cast<char *>(buf), MAX_PATH))
		params_[CONFIG_NAME_RMIREGISTRY] = buf;
	
	if (RegQueryStringValue(key, CONFIG_NAME_RMIPOOLSERVEROBJECTNAME,
			static_cast<char *>(buf), MAX_PATH))
		params_[CONFIG_NAME_RMIPOOLSERVEROBJECTNAME] = buf;
	
	RegCloseKey(key);
	return true;
}

bool idajava_plugin::read_config()
{
	// Set defaults
	jvm_class_path_ = CONFIG_VALUE_JVMCLASSPATH;
	jvm_working_directory_ = CONFIG_VALUE_JVMWORKINGDIRECTORY;
	java_plugin_class_name_ = CONFIG_VALUE_PLUGINJAVACLASS;
	params_[CONFIG_NAME_EMBEDDEDFRAMECLASS] =
		CONFIG_VALUE_EMBEDDEDFRAMECLASS;
	params_[CONFIG_NAME_RMIREGISTRY] = CONFIG_VALUE_RMIREGISTRY;
	params_[CONFIG_NAME_RMIPOOLSERVEROBJECTNAME] =
		CONFIG_VALUE_RMIPOOLSERVEROBJECTNAME;

	// Read from HKCU first, then from HKLM
	bool haveConfig(read_reg_config(HKEY_CURRENT_USER,
		REGKEY_HKCU_PLUGIN_ROOT));
	haveConfig = haveConfig || read_reg_config(HKEY_LOCAL_MACHINE,
		REGKEY_HKLM_PLUGIN_ROOT);

	// TODO: Implement plugin options
	const char *options(get_plugin_options("idajava"));
	if (options != 0)
		msg("Info: Ignored plugin command line options: \"%s\"\n", options);
	
	return haveConfig;
}

idajava_plugin *idajava_plugin::instance()
{
	if (instance_ == 0)
		instance_ = new idajava_plugin();
	return instance_;
}

void idajava_plugin::destroy_instance()
{
	if (instance_ == 0)
		return;
	delete instance_;
	instance_ = 0;
}

int idajava_plugin::initialize()
{
 	// Only initialize once
	if (init_done_)
		return PLUGIN_KEEP;
	init_done_ = true;

	// Display version and copyright information
	msg("%s\n", APP_BANNER);

	// Read configuration
	if (!read_config())
	{
		msg("Error: Cannot read plugin configuration, skipping "
			"plugin\n");
		return PLUGIN_SKIP;
	}

	char buf[MAX_PATH];
	if (GetModuleFileName(GetCurrentModule(), buf, MAX_PATH) == 0)
	{
		msg("Error: Cannot determine module path, skipping plugin\n");
		return PLUGIN_SKIP;
	}
	module_filename_ = buf;

	if (jvm_working_directory_.empty())
		// Extract the directory part of the module file name
		// FIXME: Determine platform-specific path separator and use it
		jvm_working_directory_ = string(module_filename_.begin(),
			module_filename_.begin() + module_filename_.rfind('\\'));

	if (!SetCurrentDirectory(jvm_working_directory_.c_str()))
		msg("Warning: Failed to change working directory to \"%s\"\n",
			jvm_working_directory_.c_str());

	// Find and create a suitable Java VM
	msg("Initializing in-process Java VM...");
	jvm_builder jvc;
	jvc.add_vmoption(("-Djava.class.path=" + jvm_class_path_).c_str());
	jvc.add_vmoption("-Xcheck:jni");
	if (!jvc.find_jre(MIN_JVM_VERSION))
	{
		msg("\nError: Cannot find a suitable Java VM, skipping plugin\n");
		return PLUGIN_SKIP;
	}
	if (!jvc.create_jvm(&jvm_, &env_))
	{
		msg("\nError: Cannot create Java VM, skipping plugin\n");
		return PLUGIN_SKIP;
	}
	msg("OK\n");

	// Register to receive IDA UI events
	if (!hook_to_notification_point(HT_UI, ui_callback, 
			reinterpret_cast<void *>(HOOKDATA_IDAJAVA_COOKIE)))
	{
		msg("Error: Cannot register to receive UI messages, skipping plugin\n");
		return PLUGIN_SKIP;
	}

	return PLUGIN_KEEP;
}

void idajava_plugin::notify_late_init_done(void) {
	late_init_done_ = true;

	// Call Java plugins late in the init process;
	call_java_plugin_initialize();
}

bool idajava_plugin::create_java_plugin()
{
	jmethodID mid;
	jstring jstr;

	java_plugin_class_ = env_->FindClass(java_plugin_class_name_.c_str());
	if (java_plugin_class_ == 0)
	{
		msg("\nError: Plugin Java class %s not found, skipping plugin\n",
			java_plugin_class_name_.c_str());
		return false;
	}

	mid = env_->GetMethodID(java_plugin_class_, "<init>",
		"(Ljava/lang/String;)V");
	if (mid == 0)
	{
		msg("\nError: Plugin class has no constructor with signature " \
			"(Ljava/lang/String;)V, skipping plugin\n");
		return false;
	}

	jstr = env_->NewStringUTF(module_filename_.c_str());
	if (jstr == 0)
	{
		msg("\nError: Cannot create UTF string, skipping plugin\n");
		return false;
	}

	java_plugin_instance = env_->NewObject(java_plugin_class_, mid, jstr);
	if (java_plugin_instance == 0)
	{
		msg("\nError: Cannot create Java object, skipping plugin\n");
		return false;
	}

	msg("OK\n");
	return true;
}

bool idajava_plugin::check_handle_java_exception()
{
//	AttachCurrentThread attach(&jvm_, &env_);

	// Exit early if there is no pending exception
	if (env_ != 0 && !env_->ExceptionCheck())
		return false;

	// From this point on we know for sure an exception happened. Now we try
	// to provide at least some of the information a normal Java program
	// would output when encountering an exception.
	jthrowable e = env_->ExceptionOccurred();
	
	// Save some method IDs for future use
	static jmethodID throwableGetClass(0);
	static jmethodID throwableGetMessage(0);
	static jmethodID classGetName(0);
	if (throwableGetClass == 0) {
		// The calls to FindClass and GetMethodID should never fail, since
		// we're asking for core Java classes here.
		jclass cls;

		cls = env_->FindClass("java/lang/Throwable");
		assert(cls != 0);
		throwableGetClass = env_->GetMethodID(cls, "getClass",
			"()Ljava/lang/Class;");
		assert(throwableGetClass != 0);
		throwableGetMessage = env_->GetMethodID(cls, "getMessage",
			"()Ljava/lang/String;");
		assert(throwableGetMessage);
		env_->DeleteLocalRef(cls);

		cls = env_->FindClass("java/lang/Class");
		classGetName = env_->GetMethodID(cls, "getName",
			"()Ljava/lang/String;");
		assert(classGetName != 0);
		env_->DeleteLocalRef(cls);
	}

	// Get exception class
	jobject c = env_->CallObjectMethod(e, throwableGetClass);
	jstring className_jstr = (jstring) env_->CallObjectMethod(c, classGetName);
	const char *className = env_->GetStringUTFChars(className_jstr, JNI_FALSE);

	// Get exception message
	jstring message_jstr = (jstring) env_->CallObjectMethod(e,
		throwableGetMessage);
	const char *message = env_->GetStringUTFChars(message_jstr, JNI_FALSE);
	
	msg("Java Exception %s: %s\n", className, message);

	// Free JNI allocated strings
	env_->ReleaseStringUTFChars(message_jstr, message);
	env_->ReleaseStringUTFChars(className_jstr, className);

	// Clear the exception, so JNI is available again
	env_->ExceptionClear();
	return true;
}

int idajava_plugin::call_java_plugin_initialize()
{
	jvm_thread_autoattach attach(&jvm_, &env_);
	msg("Invoking Java stub plugin...");
	if (!create_java_plugin())
		return PLUGIN_SKIP;

	jmethodID mid = env_->GetMethodID(java_plugin_class_, "initialize", "()I");

	// If the initialize()-method was not found, just issue a warning and
	// assume the plugin needs no initialization and return PLUGIN_KEEP
	if (mid == 0)
	{
		msg("Info: No initialize()-method found for Java plugin, assuming " \
			"it does not need initialization\n");
		return PLUGIN_KEEP;
	}

	// Call the plugin's initialize()-method
	long result = env_->CallIntMethod(java_plugin_instance, mid);

	// Skip plugin if there was an exception
	if (check_handle_java_exception())
		return PLUGIN_SKIP;

	return PLUGIN_KEEP;//(int) result;
}

void idajava_plugin::call_java_plugin_run(int arg)
{
//	jvm_thread_autoattach attach(&jvm_, &env_);
	jmethodID mid = env_->GetMethodID(java_plugin_class_, "run", "(I)V");
	if (mid == 0)
	{
		msg("Warning: Java plugin has no run()-method, nothing to be done\n");
		return;
	}

	// Call the plugin's run()-method
	env_->CallVoidMethod(java_plugin_instance, mid, (jint) arg);
	check_handle_java_exception();
}

void idajava_plugin::call_java_plugin_terminate(void)
{
	jvm_thread_autoattach attach(&jvm_, &env_);
	jmethodID mid = env_->GetMethodID(java_plugin_class_, "terminate", "()V");
	if (mid == 0)
	{
		msg("Info: No terminate()-method found for Java plugin\n");
		return;
	}

	// Call the plugin's terminate()-method
	env_->CallVoidMethod(java_plugin_instance, mid);
	check_handle_java_exception();
}

void idajava_plugin::run(int arg)
{
	if (!late_init_done_)
		return;

	if (env_ == 0 || java_plugin_class_ == 0)
	{
		msg("Error: Java not initialized and plugin invocation requested\n");
		return;
	}
	call_java_plugin_run(arg);
}

void idajava_plugin::terminate()
{
	// Call terminate()-method if Java was ever initialized
	if (env_ == 0 || java_plugin_class_ == 0)
		call_java_plugin_terminate();

	// Unregister UI event notification (placed here in case terminate()
	// needed it)
	unhook_from_notification_point(HT_UI, ui_callback,
		reinterpret_cast<void *>(HOOKDATA_IDAJAVA_COOKIE));

	if (env_ == 0)
		return;

	{
//	AttachCurrentThread attach(&jvm_, &env_);
	// Call System.exit(0) to ensure there are no Java threads. Ignore all
	// errors and exceptions here. FindClass and GetStaticMethodID shouldn't
	// fail anyway, since java.lang.System is a core Java class.
jvm_->AttachCurrentThreadAsDaemon((void **)&env_, 0);
	jclass cls = env_->FindClass("java/lang/System");
	jmethodID mid = env_->GetStaticMethodID(cls, "exit", "(I)V");
	env_->CallVoidMethod(java_plugin_instance, mid, 0);
	// TODO: Use the mutex from the initialize()-method to determine whether
	//       we're exiting IDA for good or just open another database
	msg("Unloading Java...");
	}
//	jvm_->DetachCurrentThread();
	jvm_->DestroyJavaVM();
	msg("OK\n");
}

const char *
idajava_plugin::parameter(const char * name)
{
	map<string, string>::iterator i = params_.find(name);
	return (i != params_.end()) ? i->second.c_str() : 0;
}

/**
 * @brief Callback for the custom window procedure for embedded Ida forms.
 *
 * @param[in] handle  The handle to the current window
 * @param[in] wParam  The first message parameter
 * @param[in] lParam  The second message parameter
 *
 * @return The value returned by call to
 *     @a idajava_plugin::embedded_window_proc().
 * 
 */
static LRESULT CALLBACK
embeddedWindowProcCallback(HWND handle, UINT uMsg, WPARAM wParam, LPARAM lParam)
{ 
	return idajava_plugin::instance()->embedded_window_proc(handle, uMsg,
			wParam, lParam);
}

LRESULT
idajava_plugin::embedded_window_proc(HWND handle, UINT uMsg, WPARAM wParam,
		LPARAM lParam)
{
	if (uMsg == WM_ERASEBKGND)
		return 1;
	if (uMsg == WM_SIZING)
	{
//		msg(".");
	}

	WNDPROC prevWndProc(reinterpret_cast<WNDPROC>(GetWindowLong(handle,
			GWL_USERDATA)));
	if (prevWndProc == 0)
		return DefWindowProc(handle, uMsg, wParam, lParam);
	return CallWindowProc(prevWndProc, handle, uMsg, wParam, lParam);
}

bool
idajava_plugin::init_ida_embedded_window(TForm * form)
{
	// Try to find deeply nested child window
	HWND handle = get_tform_handle(form);
	RECT rect;
	while (GetClientRect(handle, &rect))
	{
		if (rect.right < 10000)
			break;
		handle = GetParent(handle);
	}

	WNDPROC prevWndProc = reinterpret_cast<WNDPROC>(GetWindowLongPtr(handle,
			GWL_WNDPROC));
	if (prevWndProc == 0)
	{
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
