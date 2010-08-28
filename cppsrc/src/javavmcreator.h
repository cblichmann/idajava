/*
 * IdaJava version 0.3
 * Copyright (c)2007-2010 by Christian Blichmann
 *
 * JavaVMCreator header file
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
#include <vector>

// Windows specific includes, omit rarely used APIs
#define WIN32_LEAN_AND_MEAN
#include <windows.h>

// Java Native Interface
#include <jni.h>

/** The JRE's registry key under HKEY_LOCAL_MACHINE */
#define REGKEY_HKLM_JRE_ROOT "SOFTWARE\\JavaSoft\\Java Runtime Environment"

/** Function pointer for runtime dynamic linking */
typedef jint (JNICALL *CreateJavaVM_t)(JavaVM **, void **, void *);

typedef jint (JNICALL *GetCreatedJavaVMs_t)(JavaVM **, jsize, jsize *);

/**
 * @brief Finds and creates a Java VM. Tries to locate a suitable Java
 * Runtime Environment and loads the corresponding library.
 */
class JavaVMCreator {
	/** Path to the Java runtime library */
	std::string m_jvmLibPath;

	/** Java VM startup argument struct */
	JavaVMInitArgs m_jvmArgs;

	/** Temporary vector holding Java VM options */
	std::vector<JavaVMOption> m_jvmOptions;
protected:
	jint mapJNIVersion(const double version);
public:
	/**
	 * @brief  Tries to locate an installed Java VM with a specified minimum
	 * required version.
	 * @param minRequiredVersion minimum required version, defaults to 1.2
	 * @return true, if a suitable Java VM was found; false otherwise
	 */
	virtual bool findJRE(const double minRequiredVersion = 1.2);

	/**
	 * @brief Adds a Java VM option to the arguments struct. Refer to the Java
	 * Invocation API for available options.
	 * @param optionString the option value
	 * @param extraInfo optional pointer to additional option data
	 */
	virtual void addVMOption(const char* optionString,
							 void *extraInfo = 0);

	/**
	 * @brief Creates a previously located Java VM. Dynamically loads the Java
	 * runtime library into the current process. After a successful call to this
	 * method, use jvm to shutdown the Java VM and env to invoke Java code.
	 * Refer to the Java Invocation API for information on how to instatiate
	 * a Java class and call its methods.
	 * @param jvm a receiving JavaVM object
	 * @param env a receiving JNIenv object
	 */
	virtual bool createVM(JavaVM **jvm, JNIEnv **env);
};

/**
 * @brief Utility function that reads a string value from a registry key.
 * @param key registry key containing the desired value, must already be open
 * @param name name of the value to read
 * @param buf pointer to a buffer that receives the value
 * @param bufsize size of the receiving buffer
 * @return true on success, false otherwise
 */
bool RegQueryStringValue(HKEY key, const char *name, char *buf, int bufsize);

/**
 * @brief Utility function that reads a 32-bit unsigned integer value from a
 * registry key.
 * @param key registry key containing the desired value, must already be open
 * @param name name of the value to read
 * @param value reference to a variable that receives the value
 * @return true on success, false otherwise
 */
bool RegQueryUInt32Value(HKEY key, const char *name, DWORD &value);
