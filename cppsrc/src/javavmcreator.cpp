/*
 * IdaJava version 0.3
 * Copyright (c)2007-2010 by Christian Blichmann
 *
 * JavaVMCreator class
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
#include "javavmcreator.h"

bool RegQueryStringValue(HKEY key, const char *name, char *buf,
						 int bufsize)
{
	DWORD type, size;
	
	// Do we have a value of that name?
	if (RegQueryValueEx(key, name, 0, &type, 0, &size) != ERROR_SUCCESS)
		return false;
	
	// Is it the right type and not too large?
	if (type != REG_SZ || (size >= (unsigned int)bufsize))
		return false;

	// Finally read the string and return status
	return RegQueryValueEx(key, name, 0, 0, (unsigned char *)buf,
		&size) == ERROR_SUCCESS;
}

bool RegQueryUInt32Value(HKEY key, const char *name, DWORD &value)
{
	DWORD type, size;
	
	// Do we have a value of that name?
	if (RegQueryValueEx(key, name, 0, &type, 0, &size) != ERROR_SUCCESS)
		return false;
	
	// Is it the right type and not too large?
	if (type != REG_DWORD || size > sizeof(DWORD))
		return false;

	// Finally read the string and return status
	return RegQueryValueEx(key, name, 0, 0, (LPBYTE)&value,
		&size) == ERROR_SUCCESS;
}

jint JavaVMCreator::mapJNIVersion(const double version)
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

bool JavaVMCreator::findJRE(const double minRequiredVersion)
{
	bool result;
	HKEY key, subkey;
	char buf[MAX_PATH];
	double curVer;

	if (RegOpenKeyEx(HKEY_LOCAL_MACHINE, REGKEY_HKLM_JRE_ROOT, 0, KEY_READ,
			&key) != ERROR_SUCCESS)
		return false;

	result = false;
	if (RegQueryStringValue(key, "CurrentVersion", (char *)buf, MAX_PATH)) {
		curVer = atof(buf);

		if (minRequiredVersion <= curVer && RegOpenKeyEx(key, buf, 0, KEY_READ,
				&subkey) == ERROR_SUCCESS) {
			if (RegQueryStringValue(subkey, "RuntimeLib", (char *)buf,
					MAX_PATH)) {
				m_jvmLibPath = buf;
				result = true;
			}
			RegCloseKey(subkey);
		}
	}
	RegCloseKey(key);

	m_jvmArgs.version = mapJNIVersion(minRequiredVersion);
	return result;
}

void JavaVMCreator::addVMOption(const char* optionString,
								void *extraInfo)
{
	const JavaVMOption opt = {const_cast<char *>(optionString), extraInfo};
	m_jvmOptions.push_back(opt);
}

bool JavaVMCreator::createVM(JavaVM **jvm, JNIEnv **env)
{
	bool result;
	HMODULE hjvm = LoadLibrary(m_jvmLibPath.c_str());
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
		m_jvmArgs.options = &m_jvmOptions[0];
		m_jvmArgs.nOptions = (jint)m_jvmOptions.size();
		m_jvmArgs.ignoreUnrecognized = JNI_TRUE;
		
		// Create the actual Java VM
		result = pfnCreateJavaVM(jvm, (void**)env, &m_jvmArgs) >= 0;
	}

	FreeLibrary(hjvm);
	return result;
}
