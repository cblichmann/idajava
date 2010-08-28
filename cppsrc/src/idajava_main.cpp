/*
 * IdaJava version 0.3
 * Copyright (c)2007-2010 by Christian Blichmann
 *
 * Plugin Main
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
 *
 * This is for IDA Pro 4.9 or higher
 * Compile with Visual C++ 2008
 * Documentation generated with Doxygen
 */

#define USE_STANDARD_FILE_FUNCTIONS

// Plugin headers
#include "idajava_main.h"
#include "idajava_consts.h"
#include "idajava_pluginclass.h"
#include "idajava_natives.h"

int idaapi IdaJava_init()
{
	return IdaJavaPlugin::getInstance()->initialize();
}

void idaapi IdaJava_term()
{
	IdaJavaPlugin::getInstance()->terminate();
}

void idaapi IdaJava_run(int arg)
{
	IdaJavaPlugin::getInstance()->run(arg);
}
