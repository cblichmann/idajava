/*
 * IdaJava version 0.3
 * Copyright (c)2007-2017 by Christian Blichmann
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

// IDA SDK includes
#define USE_DANGEROUS_FUNCTIONS
#define USE_STANDARD_FILE_FUNCTIONS
#pragma warning(push)
#pragma warning(disable: 4229) // loader.h: modifier on data
#pragma warning(disable: 4996) // pro.h: unsafe use of ctime()
#include <fpro.h>
#include <loader.hpp>
#pragma warning(pop)

#include "idajava_natives.h"
#include "idajava_pluginclass.h"

const char *
getParameter(const char * key)
{
	return idajava_plugin::instance()->parameter(key);
}

int
getIdaWindowHandle()
{
	return reinterpret_cast<int>(callui(ui_get_hwnd).vptr);
}

int
getInternalHandle(int handle)
{
	return reinterpret_cast<int>(GetParent(GetParent(reinterpret_cast<HWND>(handle))));
}

bool
initIdaEmbeddedWindow(TForm *form)
{
	return idajava_plugin::instance()->init_ida_embedded_window(form);
}

const char *
getIdbPath()
{
	return database_idb;
}

static bool
idaapi wrap_add_menu_item_callback(void * ud)
{
	reinterpret_cast<IdaMenuItemListener *>(ud)->actionPerformed();
	return true;
}

bool
addMenuItem(const char * menupath, const char * name, const char * hotkey,
		int flags, IdaMenuItemListener * listener)
{
	return add_menu_item(menupath, name, hotkey, flags,
		wrap_add_menu_item_callback, reinterpret_cast<void *>(listener));
}
