/*
 * IdaJava version 0.3
 * Copyright (c)2007-2017 by Christian Blichmann
 *
 * jvm_builder header file
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

/** Function pointer for runtime dynamic linking */
typedef jint (JNICALL * CreateJavaVM_t)(JavaVM **, void **, void *);

typedef jint (JNICALL * GetCreatedJavaVMs_t)(JavaVM **, jsize, jsize *);

/**
 * @brief Finds and creates a Java VM. Tries to locate a suitable Java
 * Runtime Environment and loads the corresponding library.
 */
class jvm_builder
{
private:
	/** Path to the Java runtime library */
	std::string jvm_lib_path_;

	/** Java VM startup argument struct */
	JavaVMInitArgs jvm_args_;

	/** Temporary vector holding Java VM options */
	std::vector<JavaVMOption> jvm_options_;

	jint map_jni_version(const double version);
public:
	/**
	 * @brief  Tries to locate an installed Java VM with a specified minimum
	 * required version.
	 * @param minRequiredVersion minimum required version, defaults to 1.2
	 * @return true, if a suitable Java VM was found; false otherwise
	 */
	virtual bool find_jre(const double min_required_version = 1.2);

	/**
	 * @brief Adds a Java VM option to the arguments struct. Refer to the Java
	 * Invocation API for available options.
	 * @param optionString the option value
	 * @param extraInfo optional pointer to additional option data
	 */
	virtual void add_vmoption(const char* option_string, void *extraInfo = 0);

	/**
	 * @brief Creates a previously located Java VM. Dynamically loads the Java
	 * runtime library into the current process. After a successful call to this
	 * method, use jvm to shutdown the Java VM and env to invoke Java code.
	 * Refer to the Java Invocation API for information on how to instatiate
	 * a Java class and call its methods.
	 * @param jvm a receiving JavaVM object
	 * @param env a receiving JNIenv object
	 */
	virtual bool create_jvm(JavaVM ** jvm, JNIEnv ** env);
};

class jvm_thread_autoattach
{
private:
	JavaVM * jvm_;
public:
	jvm_thread_autoattach(JavaVM ** jvm, JNIEnv ** env_)
		: jvm_(* jvm)
	{
/*		jint existingJvmReturned;
		jint res;
		res = JavaVMCreator::pfnGetCreatedJavaVMs(jvm, 1, &existingJvmReturned);
		if (*jvm != 0 && res == 0 && existingJvmReturned > 0) // success
		{
			// Jvm already existed, get the env
			res = (*jvm)->GetEnv((void**)env_, JNI_VERSION_1_6);
		} else
			throw std::string("Could not obtain JVM");
 
		if (res == JNI_EDETACHED)*/
			jvm_->AttachCurrentThreadAsDaemon((void**)env_, 0);
	}

	~jvm_thread_autoattach()
	{
//		jvm_->DetachCurrentThread();
	}
};
