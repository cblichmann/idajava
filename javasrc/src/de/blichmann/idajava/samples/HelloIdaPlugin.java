/*
 * IDAJava version 0.3
 * Copyright (c)2007-2010 Christian Blichmann
 *
 * HelloIdaPlugin Class
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

package de.blichmann.idajava.samples;

import de.blichmann.idajava.api.IdaConsole;
import de.blichmann.idajava.api.plugin.IdaPlugin;
import de.blichmann.idajava.natives.IdaJava;
import de.blichmann.idajava.natives.insn_t;

/**
 * Sample plugin that demonstrates how to write IDA plugins in Java.
 * @author Christian Blichmann
 * @since 0.1
 */
public class HelloIdaPlugin extends IdaPlugin {
	
	@Override
	public String getDisplayName() {
		return "IdaJava Hello World Plugin";
	}

	@Override
	public String getHotkey() {
		// No hotkey
		return null;
	}

	/**
	 * Initializes the plugin.
	 * @return {@code PLUGIN_OK} to keep the plugin loaded, {@code PLUGIN_SKIP}
	 *     on error
	 */
	@Override
	public int initialize() {
		IdaConsole.out.println("Java plugin in initialize() method");
		return PLUGIN_OK;
	}

	@Override
	public void run(int arg) {
		IdaConsole.out.println("Java plugin in run() method");
		IdaConsole.out.println("Hello, IDA!");

		// Actually do something
		IdaConsole.out.println("Current address is: " +
				IdaJava.get_screen_ea());

		insn_t inst;
		inst = IdaJava.getCmd();
	}

	@Override
	public void terminate() {
		IdaConsole.out.println("Java plugin in terminate() method");
	}
}
