/*
 * IdaJava version 0.3
 * Copyright (c)2007-2010 by Christian Blichmann
 *
 * Plugin-specific exports for Java
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

#include "idajava_natives.h"
#include "idajava_pluginclass.h"
#include "va_pass.h"

const char *getParameter(const char *key)
{
	return IdaJavaPlugin::getInstance()->getParameter(key);
}

int getIdaWindowHandle()
{
	return reinterpret_cast<int>(callui(ui_get_hwnd).vptr);
}

int getInternalHandle(int handle)
{
	return reinterpret_cast<int>(GetParent(GetParent(reinterpret_cast<HWND>(handle))));
}

bool initIdaEmbeddedWindow(TForm *form)
{
	return IdaJavaPlugin::getInstance()->initIdaEmbeddedWindow(form);
}

void sayHello()
{
	msg("Hello IDA from Java!");
}

const char *getIdbPath()
{
	return database_idb;
}

callui_t (idaapi *g_savedCallui)(ui_notification_t what, ...) = 0;
char *g_openDatabaseFilename = 0;

callui_t idaapi openDatabaseCallui(ui_notification_t what, ...)
{
	callui_t result;
	va_list args;

	if (what == ui_askfile)
		result.cptr = g_openDatabaseFilename;
	else {
		va_start(args, what);
		result = g_savedCallui(what, va_pass(args));
		va_end(args);
	}
	return result;
}

bool openDatabase(char *fileName)
{
	// Close any existing database
	closeDatabase();

	// Assign a temporary UI callback
	g_savedCallui = callui;
	callui = openDatabaseCallui;

	g_openDatabaseFilename = fileName;
	char *argv[] = { fileName };
	int newfile = 0;
	int retVal = init_database(1, argv, &newfile);

	callui = g_savedCallui;

	msg("Debug: init_database: %d %d\n", retVal, newfile);

	return retVal == 0;
}

void closeDatabase()
{
	term_database();
	g_openDatabaseFilename = 0;
}

static bool idaapi wrap_add_menu_item_callback(void *ud)
{
	reinterpret_cast<IdaMenuItemListener *>(ud)->actionPerformed();
	return true;
}

bool addMenuItem(const char *menupath, const char *name,
	const char *hotkey, int flags, IdaMenuItemListener *listener)
{
	return add_menu_item(menupath, name, hotkey, flags,
		wrap_add_menu_item_callback, reinterpret_cast<void *>(listener));
}
