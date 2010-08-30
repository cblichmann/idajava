/*
 * IdaJava version 0.3
 * Copyright (c)2007-2010 by Christian Blichmann
 *
 * Windows utility functions
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

#include "idajava_windows.h"

/**
 * @brief Utility function that returns a handle to the current module.
 * @return the handle
 */
HMODULE
GetCurrentModule()
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

bool
RegQueryStringValue(HKEY key, const char * name, char * buf, int bufsize)
{
	DWORD type, size;
	
	// Do we have a value of that name?
	if (RegQueryValueEx(key, name, 0, &type, 0, &size) != ERROR_SUCCESS)
		return false;
	
	// Is it the right type and not too large?
	if (type != REG_SZ || (size >= (unsigned int)bufsize))
		return false;

	// Finally read the string and return status
	return RegQueryValueEx(key, name, 0, 0, reinterpret_cast<LPBYTE>(buf),
		&size) == ERROR_SUCCESS;
}

bool
RegQueryUInt32Value(HKEY key, const char * name, DWORD & value)
{
	DWORD type, size;
	
	// Do we have a value of that name?
	if (RegQueryValueEx(key, name, 0, &type, 0, &size) != ERROR_SUCCESS)
		return false;
	
	// Is it the right type and not too large?
	if (type != REG_DWORD || size > sizeof(DWORD))
		return false;

	// Finally read the string and return status
	return RegQueryValueEx(key, name, 0, 0, reinterpret_cast<LPBYTE>(&value),
		&size) == ERROR_SUCCESS;
}
