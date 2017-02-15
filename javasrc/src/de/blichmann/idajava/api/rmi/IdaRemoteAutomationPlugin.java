/*
 * IDAJava version 0.3
 * Copyright (c)2007-2017 Christian Blichmann
 *
 * IdaRemoteAutomationPlugin Class
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

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.UUID;

import de.blichmann.framework.util.rmi.SetupRegistry;
import de.blichmann.idajava.api.IdaConsole;
import de.blichmann.idajava.api.plugin.IdaPlugin;
import de.blichmann.idajava.natives.IdaJava;
import de.blichmann.idajava.natives.SWIGTYPE_p_linput_t;

/**
 * The {@code IDARemoteAutomationPlugin} class extends the plugin framework
 * provided by {@link IdaPlugin} with RMI connectivity. It has two
 * major modes of operation:
 * <ol>
 * <li>A standalone mode in which the plugin creates its own local RMI registry
 *     on the default port and bind itself to that registy using a well-known
 *     object name.
 * <li>A pooled mode in which the plugin registers itself with a pool server
 *     which is bound to an existing RMI registry (possibly on another physical
 *     host).
 * </ol>
 * In either mode, clients can accesss the plugin's functionality using the
 * {@link IdaRemoteAutomation} interface.
 * @author Christian Blichmann
 * @since 0.1
 */
public class IdaRemoteAutomationPlugin extends IdaPlugin
		implements IdaRemoteAutomation {
	
	@Override
	public String getDisplayName() {
		return "IdaJava Remote Automation Plugin";
	}

	@Override
	public String getHotkey() {
		return null;
	}

	/** Name of the plugin in the RMI registry */
	private String boundName;
	
	private long lastInputFileId = 0;
	private final Hashtable<Long, SWIGTYPE_p_linput_t> inputFiles;
	
	/**
	 * Pool server with which the plugin is registered. If set to {@code null},
	 * the plugin operates in standalone mode.
	 */
	private IdaJavaInstancePool poolServer;
	
	/**
	 * A {@code boolean} specifying whether or not the terminate()-method has
	 * been called.
	 */
	private boolean terminated;
	
	/**
	 * Constructs a new {@code IDARemoteAutomationPlugin} object. Loads the
	 * the native IDAJava plugin library and initializes private fields.
	 */
	public IdaRemoteAutomationPlugin() {
		boundName = null;
		inputFiles = new Hashtable<Long, SWIGTYPE_p_linput_t>();
		poolServer = null;
		terminated = false;
	}
	
	/**
	 * Exports the plugin object and binds it to the given object name.
	 * @param name an object name in URL format
	 * @return {@code true} if export was successful, {@code false} otherwise.
	 */
	private boolean startRMI(String name) {
		// TODO: Provide a way to specify policy files so that loading
		//       of stubs works.
		/*if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}//*/
		try {
			UnicastRemoteObject.exportObject(this);
			Naming.rebind(name, this);
			boundName = name;
			
			return true;
		} catch (MalformedURLException e) {
			IdaConsole.out.println(e.getMessage());
		} catch (AccessException e) {
			IdaConsole.out.println(e.getMessage());
		} catch (RemoteException e) {
			IdaConsole.out.println(e.getMessage());
		}
		return false;
	}
	
	/**
	 * Unbinds the plugin, effectively stopping RMI connectivity.
	 */
	private void stopRMI() {
		try {
			Naming.unbind(boundName);
		} catch (MalformedURLException e) {
			// Should not happen, since at this stage the plugin should already
			// be bound to a registry
		} catch (AccessException e) {
			// Maybe the security policy changed in the meantime?
			IdaConsole.out.println(e.getMessage());
		} catch (RemoteException e) {
			// Genereal communication error
			IdaConsole.out.println(e.getMessage());
		} catch (NotBoundException e) {
			// Should also not happen, like MalformedURLException
		}
	}
	
	/**
	 * Initializes the plugin and starts RMI connectivity. If RMI could not be
	 * started, returns {@code PLUGIN_SKIP} to prevent further usage of the
	 * plugin.
	 * @return {@code PLUGIN_OK} to keep the plugin loaded, {@code PLUGIN_SKIP}
	 *     on error
	 */
	@Override
	public int initialize() {
		String rmiRegistry = getParameter("RmiRegistry");
		if (rmiRegistry == null || rmiRegistry.isEmpty())
			rmiRegistry = "//";
		
		final String poolServerObjName = getParameter(
				"RmiPoolServerObjectName");
		
		final String objName;
		final boolean standalone = poolServerObjName == null || 
				poolServerObjName.isEmpty();
		if (standalone) {
			objName = rmiRegistry + IdaRemoteAutomation.DEFAULT_NAME;
			try {
				// Create a local registry if none could be contacted
				if (!SetupRegistry.haveRegistry(rmiRegistry))
					LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
				
				if (!startRMI(objName))
					return PLUGIN_SKIP;
				return PLUGIN_KEEP;
			} catch (MalformedURLException e) {
				IdaConsole.out.println(e.getMessage());
			} catch (RemoteException e) {
				IdaConsole.out.println(e.getMessage());
			}
			return PLUGIN_SKIP;
		}
		
		// Generate a unique name
		objName = IdaRemoteAutomation.NAMING_PREFIX + UUID.randomUUID();
		
		if (!startRMI(objName))
			return PLUGIN_SKIP;
		
		try {
			poolServer = (IdaJavaInstancePool) Naming.lookup(rmiRegistry +
					poolServerObjName);
			poolServer.registerInstance(boundName, IdaJava.getIdbPath());
			IdaConsole.out.println("Registered with pool server.");
			return PLUGIN_KEEP;
		} catch (MalformedURLException e) {
			IdaConsole.out.println(e.getMessage());
		} catch (RemoteException e) {
			IdaConsole.out.println(e.getMessage());
		} catch (NotBoundException e) {
			IdaConsole.out.println(e.getMessage());
		}
		return PLUGIN_SKIP;
	}

	@Override
	public void run(int arg) {
		IdaConsole.out.println("Java in run method");
	}

	@Override
	public void terminate() {
		if (terminated)
			return;
		terminated = true;
		
		IdaConsole.out.println("Java in terminate method");
		if (poolServer != null) {
			try {
				poolServer.unregisterInstance(boundName);
			} catch (RemoteException e) {
				IdaConsole.out.println(e.getMessage());
			}
			poolServer = null;
		}
		stopRMI();
	}
	
	// IDARemoteAutomation Interface
	public void msg(String message) throws RemoteException {
		IdaJava.msg(message);
	}
	
	public long loadFile(String fileName) throws RemoteException {
		final SWIGTYPE_p_linput_t file = IdaJava.open_linput(fileName, false);
		if (file == null)
			return -1;
		
		lastInputFileId++;
		inputFiles.put(lastInputFileId, file);
		return lastInputFileId;
	}
	
	public void closeFile(long fileId) throws RemoteException {
		final SWIGTYPE_p_linput_t file = inputFiles.get(fileId);
		if (file == null)
			return;
		
		IdaJava.close_linput(file);
		inputFiles.remove(fileId);
	}
	
	public boolean runScript(String code) throws RemoteException {
		// UNTESTED
		return IdaJava.execute(code);
	}
	
	public boolean runScriptFile(String fileName) throws RemoteException {
		// UNTESTED
		String errbuf = "";
		return IdaJava.ExecuteFile(fileName, "main", 0, null, null, errbuf, 1024);
	}
	
	public boolean runPlugin(String name, int arg) throws RemoteException {
		// Mimic the behavior of the RunPlugin() IDC function
		return false;//IdaJava.load_and_run_plugin(name, arg);
	}

	public void exit(final int code) throws RemoteException {
		// Exit asynchronously to let RMI calls properly unmarshal. Otherwise
		// the caller gets a java.rmi.UnmarshalException.
		new Thread(new Runnable() {
			@Override
			public void run() {
				IdaConsole.out.println("Exit thread");
				
				// Make sure terminate gets called
				terminate();
				
//				// Properly close the database
//				IdaJava.closeDatabase();
				
				// Exit IDA
				IdaConsole.out.println("Calling qexit");
				IdaJava.qexit(code);
			}
		}).start();
	}
	
	public int analyzeInstr(long ea) throws RemoteException {
		return IdaJava.decode_insn(ea);
	}
	
	public short getSingleByte(long ea) throws RemoteException {
		return IdaJava.get_byte(ea);
	}
	
	public short[] getManyBytes(long ea, int size)
			throws RemoteException {
		short[] result = new short[size];
		for (int i = 0; i < size; i++) {
			result[i] = IdaJava.get_byte(ea + i);
			if (result[i] == -1)
				return null;
		}
		//IDAConsole.out.println("DEBUG: getManyBytes() returned true");
		return result;
	}
}
