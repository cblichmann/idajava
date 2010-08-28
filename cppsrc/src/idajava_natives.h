/*
 * IdaJava version 0.3
 * Copyright (c)2007-2010 by Christian Blichmann
 *
 * Plugin-specific exports for Java headers
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

// IDA SDK includes
#include <ida.hpp>
#include <kernwin.hpp>
#include <diskio.hpp>

// Note: When extending this file with additional functions, watch out for
//       potential name clashes with the IDA API.

/**
 * Returns the value of a named configuration option. The name argument
 * is case sensitive.
 * @param name the name of a configuration option
 * @return the value of the named parameter, or 0 if there was no
 *     configuration value with the specified key.
 */
const char *getParameter(const char *key);

/**
 * @brief Returns a window handle to the IDA main window.
 * TODO: Check for 64-bit compatibility (once IDA is 64-bit itself).
 */
int getIdaWindowHandle();

/**
 * @brief Preform platform-specific initialization of a new embedded window.
 * @param form An IDA TForm that already has a valid window handle
 * @return @a true on success, @a false otherwise.
 */
bool initIdaEmbeddedWindow(TForm *form);
int getInternalHandle(int handle);


/**
 * Adds the text "Hello IDA from Java!" to the IDA message window.
 */
void sayHello(void);

/**
 * @brief Returns the absolute filename of the database that is currently
 * loaded.
 * @return the filename.
 */
const char *getIdbPath(void);

/**
 * @brief Experimental function that opens another IDA database within the
 * same instance of IDA. This function uses some dirty tricks and does not
 * work reliably. DO NOT USE.
 * @param fileName the file name of the database to open
 * @return true on success, false otherwise.
 */
bool openDatabase(char *fileName);

/**
 * @brief Closes the current IDA database.
 */
void closeDatabase(void);

class IdaMenuItemListener {
public:
	virtual ~IdaMenuItemListener() { /* Empty destructor needed for SWIG*/ }
	virtual void actionPerformed()
	{ /* Do nothing by default */ }
};
bool addMenuItem(const char *menupath, const char *name,
	const char *hotkey, int flags, IdaMenuItemListener *listener);
