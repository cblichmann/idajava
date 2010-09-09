/*
 * IdaJava version 0.3
 * Copyright (c)2007-2010 by Christian Blichmann
 *
 * Constants
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

// Short version number
#define APP_VERSION_SHORT "0.3"

// Startup banner
#define APP_BANNER "IdaJava plugin  version " APP_VERSION_SHORT \
	" (c)2007-2010 Christian Blichmann"

// Default log levels
enum
{
	LOGLEVEL_INFO = 0,
	LOGLEVEL_VERBOSE,
	LOGLEVEL_DEBUG
};

/**
 * Cookie value to differentiate between IDA forms and IdaJava form in UI
 * notification messages.
 */
const intptr_t HOOKDATA_IDAJAVA_COOKIE(0x4944414A); // "IDAJ"

/** The plugin's registry root under HKEY_CURRENT_USER */
#define REGKEY_HKCU_PLUGIN_ROOT "Software\\blichmann.de\\IdaJava\\" \
	APP_VERSION_SHORT

/** The plugin's registry root under HKEY_LOCAL_MACHINE */
#define REGKEY_HKLM_PLUGIN_ROOT "SOFTWARE\\blichmann.de\\IdaJava\\" \
	APP_VERSION_SHORT

// Configuration constants
/** Default classname of the embedded frame */
#define CONFIG_NAME_EMBEDDEDFRAMECLASS "EmbeddedFrameClass"
#define CONFIG_VALUE_EMBEDDEDFRAMECLASS "sun.awt.windows.WEmbeddedFrame"

/** Default classpath if nothing else was specified */
#define CONFIG_NAME_JVMCLASSPATH  "JvmClassPath"
#define CONFIG_VALUE_JVMCLASSPATH ".;idajava.jar"

/** Enable or disable additional JNI checks */
#define CONFIG_NAME_JNICHECKSENABLE "JniChecksEnable"
#define CONFIG_VALUE_JNICHECKSENABLE "0"

/** Enable or disable JVM debugging */
#define CONFIG_NAME_JVMDEBUGENABLE "JvmDebugEnable"
#define CONFIG_VALUE_JVMEBUGENABLE "0"

/** Set JDWP transport */
#define CONFIG_NAME_JDWPTRANSPORT "JdwpTransport"
#define CONFIG_VALUE_JDWPTRANSPORT "transport=dt_socket,server=y,suspend=n,address=1044"

/**
 * Directory to switch to just before loading any Java classes. Can specify a
 * relative path. If this option is not set, IdaJava sets the working direcotry
 * to the directory containing its shared library file.
 */
#define CONFIG_NAME_JVMWORKINGDIRECTORY  "JvmWorkingDirectory"
#define CONFIG_VALUE_JVMWORKINGDIRECTORY "" // Should be empty

/** Default log level */
#define CONFIG_NAME_LOGLEVEL "LogLevel"
#define CONFIG_VALUE_LOGLEVEL LOGLEVEL_INFO

/** Default plugin Java class to instantiate */
#define CONFIG_NAME_PLUGINJAVACLASS  "PluginJavaClass"
#define CONFIG_VALUE_PLUGINJAVACLASS "de/blichmann/idajava/plugin/IdaJavaPlugin"

/**
 * Java Naming URL pointing to an RMI registry with a bound pool server. The
 * value has the standard URL format with Java Naming enhancements, i.e.:
 *     rmi://localhost:1099/
 *     rmi://:/
 *     //:/
 *     ///
 */
#define CONFIG_NAME_RMIREGISTRY  "RmiRegistry"
#define CONFIG_VALUE_RMIREGISTRY "///" // Shorthand for rmi://localhost:1099/

/**
 * Object name of a pool server that is bound to the RMI registry specified
 * by the RmiRegistry configuration option. If this value is set to a
 * non-empty string, the de.blichmann.idajava.rmi.IDARemoteAutomationPlugin
 * class does not create its own RMI server. Instead, the configuration
 * value is used to register with an already running RMI registry. The value
 * should not start with a "/" as this might interfere with URL parsing.
 */
#define CONFIG_NAME_RMIPOOLSERVEROBJECTNAME  "RmiPoolServerObjectName"
#define CONFIG_VALUE_RMIPOOLSERVEROBJECTNAME "" // Should be empty
