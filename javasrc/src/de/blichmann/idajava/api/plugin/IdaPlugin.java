/*
 * IDAJava version 0.3
 * Copyright (c)2007-2017 Christian Blichmann
 *
 * IDAAutomationPlugin Class
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

package de.blichmann.idajava.api.plugin;

import de.blichmann.idajava.natives.IdaJava;

/**
 * The {@code IDAPlugin} class provides a framework for writing IDA plugins in
 * Java. Its interface resembles vaguely that of the {@code java.applet.Applet}
 * class. 
 * @author Christian Blichmann
 * @since 0.1
 */
public abstract class IdaPlugin {
	
	/**
	 * Initialization constant, specifiying not to load the plugin. Use this
	 * constant to signal an error condition.
	 */
	public static final int PLUGIN_SKIP = 0;
	
	/**
	 * Initialization constant, plugin is to be loaded.
	 */
	public static final int PLUGIN_OK = 1;
	
	/**
	 * Initialization constant, keep plugin loaded in memory. Use this constant
	 * to signal success.
	 * Note: This is mapped to value 1 because IDAJava does not exhibit the
	 *       exact same behavior as IDA does when loading its plugins.
	 */
	public static final int PLUGIN_KEEP = 1;

	public abstract String getDisplayName();
	public abstract String getHotkey();
	
	/**
	 * Initializes the plugin. The default implementation just returns
	 * {@code PLUGIN_OK}, override this to change this behavior.
	 * @return {@code PLUGIN_OK} by default, to keep the plugin loaded
	 */
	public int initialize() {
		/* Do nothing by default */
		return PLUGIN_OK;
	}
	
	/**
	 * Plugin main function, gets called when either the user selects the plugin
	 * via the configured hotkey or it is invoked programmatically from an API
	 * call. The default implementation does nothing, override to provide
	 * specific functionality.
	 * @param arg the input argument
	 * @note The current implementation always invokes plugins with an argument
	 *     of zero.
	 */
	public void run(int arg) {
		/* Do nothing by default */
	}
	
	/**
	 * Gets called before unloading the plugin. The default implementation does
	 * nothing, override to perform custom finalization.
	 */
	public void terminate() {
		/* Do nothing by default */
	}

    /**
     * Returns the value of a named configuration option. The <code>name</code>
     * argument is case sensitive.
     * @param name the name of a configuration option
     * @return the value of the named parameter, or <code>null</code> if not
     *     set.
     */
	public String getParameter(String name) {
		return IdaJava.getParameter(name);
	}
}
