/*
 * IDAJava version 0.2
 * Copyright (c)2007-2009 Christian Blichmann
 *
 * Plugin RMI interface
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

package de.blichmann.idajava.api.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.blichmann.idajava.api.rmi.IdaRemoteAutomation;

public interface IdaRemoteAutomation extends Remote {
	/** Default RMI naming prefix */
	static final String NAMING_PREFIX = "IDAJava/";
	
	/** Default name to use when binding to registry */
	static final String DEFAULT_NAME = NAMING_PREFIX + "Standalone";
	
	// Java Plugin
//	/** Not implemented */
//	public void reloadPluginClass(boolean graceful) throws RemoteException;
	
	// Mesage Window
	void msg(String message) throws RemoteException;
	
	// Disassembly databases
	/**
	 * Programmatically opens an existing IDA database. Does not properly
	 * update the IDA user interface. The plugin list is also not properly
	 * updated.
	 * <p>Use with caution: At times, it does not seem to work at all.
	 * @throws RemoteException
	 */
	boolean openDatabase(String fileName) throws RemoteException;
	
	/**
	 * Closes the currently active database. Simply calls
	 * {@code IDAJava.term_database()}.
	 * @throws RemoteException
	 */
	void closeDatabase() throws RemoteException; 
	
	// Additional file management
	/**
	 * Wrapper for IDA's open_linput().
	 * @param fileName the name of the file to load
	 * @return a numeric id for the opened file or 0 if the call failed 
	 */
	long loadFile(String fileName) throws RemoteException; 
	void closeFile(long fileId) throws RemoteException; 
	
	// IDC-scripts
	boolean runScript(String code) throws RemoteException; 
	boolean runScriptFile(String fileName) throws RemoteException;
	
	// Plugins
	boolean runPlugin(String name, int arg) throws RemoteException;
	
	// Misc.
	public void exit(int code) throws RemoteException;
	
	//////////////////////////////////////////////////////////////////////////
	int analyzeInstr(long ea) throws RemoteException;
	
	short getSingleByte(long ea) throws RemoteException;

	/**
	 * @param ea
	 * @param size
	 * @return the array on success, null on failure
	 * @throws RemoteException
	 */
	short[] getManyBytes(long ea, int size) throws RemoteException;
}
