/*
 * IdaJava version 0.3
 * Copyright (c)2007-2017 by Christian Blichmann
 *
 * jvm_builder class
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

#include "idajava_jvm.h" // Include class definition
#include "idajava_windows.h"

/** The JRE's registry key under HKEY_LOCAL_MACHINE */
#define REGKEY_HKLM_JRE_ROOT "SOFTWARE\\JavaSoft\\Java Runtime Environment"

jint
jvm_builder::map_jni_version(const double version)
{
	// JNI version does not change with Java 1.7
	if (version >= 1.6)
		return JNI_VERSION_1_6;
	if (version >= 1.4)
		return JNI_VERSION_1_4;
	if (version >= 1.2)
		return JNI_VERSION_1_2;
	if (version >= 1.1)
		return JNI_VERSION_1_1;
	return 0;
}

bool
jvm_builder::find_jre(const double min_required_version)
{
	bool result;
	HKEY key, subkey;
	char buf[MAX_PATH];
	double curVer;

	if (RegOpenKeyEx(HKEY_LOCAL_MACHINE, REGKEY_HKLM_JRE_ROOT, 0, KEY_READ,
			&key) != ERROR_SUCCESS)
		return false;

	result = false;
	if (RegQueryStringValue(key, "CurrentVersion", (char *)buf, MAX_PATH))
	{
		curVer = atof(buf);

		if (min_required_version <= curVer && RegOpenKeyEx(key, buf, 0,
				KEY_READ, &subkey) == ERROR_SUCCESS)
		{
			if (RegQueryStringValue(subkey, "RuntimeLib", (char *)buf,
					MAX_PATH))
			{
				jvm_lib_path_ = buf;
				result = true;
			}
			RegCloseKey(subkey);
		}
	}
	RegCloseKey(key);

	jvm_args_.version = map_jni_version(min_required_version);
	return result;
}

void
jvm_builder::add_vmoption(const char * optionString, void * extraInfo)
{
	const JavaVMOption opt = {const_cast<char *>(optionString), extraInfo};
	jvm_options_.push_back(opt);
}

bool
jvm_builder::create_jvm(JavaVM ** jvm, JNIEnv ** env)
{
	bool result;
	HMODULE hjvm = LoadLibrary(jvm_lib_path_.c_str());
	if (hjvm == NULL)
		return false;

	// Runtime dynamic linking
	CreateJavaVM_t pfnCreateJavaVM = reinterpret_cast<CreateJavaVM_t>(
			GetProcAddress(hjvm, "JNI_CreateJavaVM"));
//	GetCreatedJavaVMs_t pfnGetCreatedJavaVMs =
//		 	reinterpret_cast<GetCreatedJavaVMs_t>(GetProcAddress(hjvm,
//			"JNI_GetCreatedJavaVMs"));
	result = pfnCreateJavaVM != NULL;
	if (result) {
		// Populate VM arguments
		jvm_args_.options = &jvm_options_[0];
		jvm_args_.nOptions = static_cast<jint>(jvm_options_.size());
		jvm_args_.ignoreUnrecognized = JNI_TRUE;
		
		// Create the actual Java VM
		result = pfnCreateJavaVM(jvm, (void **)env, &jvm_args_) >= 0;
	}

	FreeLibrary(hjvm);
	return result;
}
