/*
 * IdaJava version 0.3
 * Copyright (c)2007-2010 by Christian Blichmann
 *
 * Plugin Class header
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

#pragma once

// STL headers
#include <string>
#include <map>

// Windows specific includes, omit rarely used APIs
#define WIN32_LEAN_AND_MEAN
#include <windows.h>

// IDA SDK includes
#pragma warning(push)
// Disable warning about conversion between nodeidx_t and size_t in netnode.hpp
#pragma warning(disable: 4267)
// Disable warning about unsafe use of ctime() in pro.h
#pragma warning(disable: 4996)
#include <ida.hpp>
#include <idp.hpp>
#include <expr.hpp>
#include <bytes.hpp>
#include <loader.hpp>
#include <kernwin.hpp>
#pragma warning( pop )

// Java Native Interface
#include <jni.h>

#include "idajava_consts.h"
#include "javavmcreator.h"
#include <process.h>

/** Minimum JVM version required */
#define MIN_JVM_VERSION 1.4

class AttachCurrentThread {
	JavaVM *m_jvm;
public:
	AttachCurrentThread(JavaVM **jvm, JNIEnv **m_env)
		: m_jvm(*jvm)
	{
/*		jint existingJvmReturned;
		jint res;
		res = JavaVMCreator::pfnGetCreatedJavaVMs(jvm, 1, &existingJvmReturned);
		if (*jvm != 0 && res == 0 && existingJvmReturned > 0) // success
		{
			// Jvm already existed, get the env
			res = (*jvm)->GetEnv((void**)m_env, JNI_VERSION_1_6);
		} else
			throw std::string("Could not obtain JVM");
 
		if (res == JNI_EDETACHED)*/
			m_jvm->AttachCurrentThreadAsDaemon((void**)m_env, 0);
	}

	~AttachCurrentThread()
	{
//		m_jvm->DetachCurrentThread();
	}
};

/**
 * @brief Plugin class, handles the IDA plugin callback functions.
 */
class IdaJavaPlugin {
	/** Reference to the single instance */
	static IdaJavaPlugin *m_instance;

	/** Flag to determine whether the plugin was already initialized */
	bool m_initDone;
	bool m_lateInitDone;

	/** Variable holding the instanciated Java VM */
	JavaVM *m_jvm;

	/** The JVM environment class */
	JNIEnv *m_env;

	/** Class path for the internal Java VM */
	std::string m_jvmClassPath;

	/** Working directory for the internal Java VM */
	std::string m_jvmWorkingDirectory;

	/** Amount of logging information that is printed to IDA console */
	unsigned int m_logLevel;

	/** Absolute file name of the plugin shared library */
	std::string m_moduleFileName;

	/** Name of the Java class to instantiate */
	std::string m_pluginJavaClassName;

	/**
	 * Holds configuration parameters for Java plugins, similiar to parameters
	 * in applets.
	 */
	std::map<std::string, std::string> m_params;

	/** Reference to the Java plugin's class */
	jclass m_javaPluginClass;

	/** The instantiated Java plugin */
	jobject m_javaPluginObj;

	/** @brief Construct an instance of @a IdaJavaPlugin. */
	IdaJavaPlugin()
		: m_initDone(false)
		, m_lateInitDone(false)
		, m_jvm(0)
		, m_env(0)
		, m_logLevel(0)
		, m_javaPluginClass(0)
		, m_javaPluginObj(0) {}

	/**
	 * @brief Read the plugin configuration from the specified Windows registry
	 * hive and subkey. If rootKey is not one of the predefined registry
	 * key constants, it must be a handle to an already open key.
	 * @param rootkey the registry root key where the configuration is located
	 * @param subkey location of the configuration data relative to rootKey
	 * @return true of the specified rootkey exists and configuration data was
	 *     read, false otherwise.
	 */
	bool readRegConfig(HKEY rootkey, LPCSTR subkey);

	/**
	 * @brief Read the plugin configuration. Tries to read the configuration
	 * from the following places (in order):
	 *   1. (Windows-only) The registry path under HKEY_CURRENT_USER as
	 *      specified by the REG_PLUGIN_ROOT_HKCU constant.
	 *   2. (Windows-only) The registry path under HKEY_LOCAL_MACHINE as
	 *      specified by the REG_PLUGIN_ROOT_HKLM constant. System settings
	 *      override per-user settings.
	 *   TODO: 3. (Linux-only) The file idajava.conf in the /etc directory.
	 *   TODO: 4. The file idajava.conf in the current directory (i.e., the
	 *      directory in which the plugin shared library resides).
	 *   TODO: 5. The file .idajava.conf in the users' home directory.
	 *   TODO: 6. IDA plugin options specified on the command line with the
	 *      prefix "idajava".
	 * @return true on success, false if the configuration could not be read
	 */
	bool readConfig(void);
	
	/**
	 * Creates the the java plugin by calling its constructor.
	 * @return true on success, false otherwise
	 */
	bool createJavaPlugin(void);

	/**
	 * @brief Checks if there is a pending Java exception and if so, outputs the
	 * exception message and clears it.
	 * @return true if an exception was handled and cleared, false if there was
	 *     no pending exception.
	 */
	bool checkHandleJavaException(void);

	int callJavaPluginInitialize(void);
	void callJavaPluginRun(int arg);
	void callJavaPluginTerminate(void);

public:
	/**
	 * @brief Return an instance of @a IdaJavaPlugin.
	 * @return the instance
	 */
	static IdaJavaPlugin *getInstance(void);

	/**
	 * @brief Destroy the instance of @a IdaJavaPlugin.
	 */
	static void destroyInstance(void);

	/**
	 * @brief Initialize the plugin.
	 * @return @a PLUGIN_KEEP on success to keep it in memory, @a PLUGIN_SKIP on
	 *     error
	 */
	int initialize(void);

	/**
	 * @brief Notifies the plugin that IDA's GUI is fully initialized.
	 */
	void notifyGuiInitialized(void);

	/**
	 * @brief Plugin main function. Gets called when the user selects the plugin.
	 * @param arg the input argument, can be specified in plugins.cfg file,
	 *     defaults to zero.
	 */
	void run(int arg);

	/**
	 * @brief Gets called before unloading the plugin.
	 */
	void terminate(void);

    /**
     * @brief The following functions are documented in idajava_natives.h
     * @{
     */
	const char *getParameter(const char *name);
	bool initIdaEmbeddedWindow(TForm *form);
	/** @} */

	/**
	 * @brief Custom window procedure for embedded Ida forms. Currently only
	 * handles WM_ERASEBKGND directly, all other messages are redirected to the
	 * saved window procedure.
	 *
	 * @param[in] handle  The handle to the current window
	 * @param[in] wParam  The first message parameter
	 * @param[in] lParam  The second message parameter
	 *
	 * @return The value returned by a call to the saved window procedure for
	 *     @a handle, or non-zero if @a uMsg == WM_ERASEBKGND.
	 */
	LRESULT embeddedWindowProc(HWND handle, UINT uMsg, WPARAM wParam,
		LPARAM lParam);
};

/**
 * @brief Utility function that returns a handle to the current module.
 * @return the handle
 */
HMODULE GetCurrentModule();
