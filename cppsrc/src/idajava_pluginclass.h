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

// Standard headers
#include <cstdarg>
#include <string>
#include <map>

// Java Native Interface
#include <jni.h>

#include "idajava_consts.h"
#include "idajava_jvm.h"

/** Minimum JVM version required */
#define MIN_JVM_VERSION 1.5

/**
 * @brief Plugin class, handles the IDA plugin callback functions.
 */
class idajava_plugin {
	/** Reference to the single instance */
	static idajava_plugin * instance_;

	/** Flag to determine whether the plugin was already initialized */
	bool init_done_;
	bool late_init_done_;

	/** Variable holding the instanciated Java VM */
	JavaVM * jvm_;

	/** The JVM environment class */
	JNIEnv * env_;

	/** Class path for the internal Java VM */
	std::string jvm_class_path_;

	/** Working directory for the internal Java VM */
	std::string jvm_working_directory_;

	/** Amount of logging information that is printed to IDA console */
	unsigned int log_level_;

	/** Absolute filename of the plugin shared library */
	std::string module_filename_;

	/** Name of the Java class to instantiate */
	std::string java_plugin_class_name_;

	/**
	 * Holds configuration parameters for Java plugins, similiar to parameters
	 * in applets.
	 */
	std::map<std::string, std::string> params_;

	/** Reference to the Java plugin's class */
	jclass java_plugin_class_;

	/** The instantiated Java plugin */
	jobject java_plugin_instance;

	/** @brief Construct an instance of @a idajava_plugin. */
	idajava_plugin()
		: init_done_(false)
		, late_init_done_(false)
		, jvm_(0)
		, env_(0)
		, log_level_(0)
		, java_plugin_class_(0)
		, java_plugin_instance(0) {}

	/**
	 * @brief Read the plugin configuration from the specified Windows registry
	 * hive and subkey. If rootKey is not one of the predefined registry
	 * key constants, it must be a handle to an already open key.
	 * @param rootkey the registry root key where the configuration is located
	 * @param subkey location of the configuration data relative to rootKey
	 * @return true of the specified rootkey exists and configuration data was
	 *     read, false otherwise.
	 */
	bool read_reg_config(HKEY rootkey, LPCSTR subkey);

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
	bool read_config(void);
	
	/**
	 * Creates the the java plugin by calling its constructor.
	 * @return true on success, false otherwise
	 */
	bool create_java_plugin(void);

	/**
	 * @brief Checks if there is a pending Java exception and if so, outputs the
	 * exception message and clears it.
	 * @return true if an exception was handled and cleared, false if there was
	 *     no pending exception.
	 */
	bool check_handle_java_exception(void);

	int call_java_plugin_initialize(void);
	void call_java_plugin_run(int arg);
	void call_java_plugin_terminate(void);

public:
	/**
	 * @brief Return an instance of @a idajava_plugin.
	 * @return the instance
	 */
	static idajava_plugin * instance(void);

	/**
	 * @brief Destroy the instance of @a idajava_plugin.
	 */
	static void destroy_instance(void);

	/**
	 * @brief Initialize the plugin.
	 * @return @a PLUGIN_KEEP on success to keep it in memory, @a PLUGIN_SKIP on
	 *     error
	 */
	int initialize(void);

	/**
	 * @brief Notifies the plugin that IDA's GUI is fully initialized.
	 */
	void notify_late_init_done(void);

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
	const char * parameter(const char * name);
	bool init_ida_embedded_window(TForm * form);
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
	LRESULT embedded_window_proc(HWND handle, UINT uMsg, WPARAM wParam,
		LPARAM lParam);
};
