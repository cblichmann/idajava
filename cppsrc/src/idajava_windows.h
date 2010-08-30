/*
 * IdaJava version 0.3
 * Copyright (c)2007-2010 by Christian Blichmann
 *
 * Windows utility header file
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

// Windows specific includes, omit rarely used APIs
#define WIN32_LEAN_AND_MEAN
#include <windows.h>

HMODULE GetCurrentModule();

/**
 * @brief Utility function that reads a string value from a registry key.
 * @param key registry key containing the desired value, must already be open
 * @param name name of the value to read
 * @param buf pointer to a buffer that receives the value
 * @param bufsize size of the receiving buffer
 * @return true on success, false otherwise
 */
bool RegQueryStringValue(HKEY key, const char * name, char * buf, int bufsize);

/**
 * @brief Utility function that reads a 32-bit unsigned integer value from a
 * registry key.
 * @param key registry key containing the desired value, must already be open
 * @param name name of the value to read
 * @param value reference to a variable that receives the value
 * @return true on success, false otherwise
 */
bool RegQueryUInt32Value(HKEY key, const char *name, DWORD &value);
