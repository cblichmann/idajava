/*
 * IdaJava version 0.3
 * Copyright (c)2007-2017 by Christian Blichmann
 *
 * Plugin Main header
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
#define USE_DANGEROUS_FUNCTIONS
#define USE_STANDARD_FILE_FUNCTIONS
#pragma warning(push)
#pragma warning(disable: 4267) // netnode.hpp: Conversion nodeidx_t <-> size_t
#pragma warning(disable: 4996) // pro.h: unsafe use of ctime()
#include <idp.hpp>
#include <loader.hpp>
#pragma warning(pop)

/**
 * Initialize the plugin.
 * @return PLUGIN_KEEP to keep it in memory
 */
int idaapi idajava_init(void);

/**
 * Gets called before unloading the plugin.
 */
void idaapi idajava_term(void);

/**
 * Plugin main function. Gets called when the user selects the plugin.
 * @param arg the input argument, can be specified in plugins.cfg file.
 *            Defaults to zero.
 */
void idaapi idajava_run(int arg);

// Plugin Description Block
char comment[]       = "IdaJava";
char help[]          = "IdaJava Plugin\n";
char wanted_name[]   = "IdaJava";
char wanted_hotkey[] = "ctrl+8";

extern "C" {

plugin_t PLUGIN =
{
	IDP_INTERFACE_VERSION,
	PLUGIN_FIX,				// Load plugin when IDA starts and keep it in
                            // memory (needed, because we can only load the
							// JVM once per process)

	idajava_init,			// Initialize the plugin
	idajava_term,			// Terminate, this pointer may be NULL
	idajava_run,			// Invoke plugin

	comment,				// Long comment about the plugin,
							// it could appear in the status line
							// or as a hint

	help,					// Multiline help about the plugin

	wanted_name,			// Preferred short name of the plugin
	wanted_hotkey			// Preferred hotkey to run the plugin
};

}
