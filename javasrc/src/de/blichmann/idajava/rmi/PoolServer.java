/*
 * IDAJava version 0.3
 * Copyright (c)2007-2010 Christian Blichmann
 *
 * PoolServer Class
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

package de.blichmann.idajava.rmi;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;

import de.blichmann.framework.io.Files;
import de.blichmann.framework.util.rmi.SetupRegistry;

import de.blichmann.idajava.api.rmi.IdaJavaInstancePool;
import de.blichmann.idajava.api.rmi.IdaRemoteAutomation;

/**
 * @author Christian Blichmann
 */
public class PoolServer implements IdaJavaInstancePool {
	
	public static final int DEFAULT_TIMEOUT = 30;
	
	/**
	 * Simple class to store instance-related data.
	 */
	class IDAJavaInstance {
		/**
		 * A {@code Thread} that makes sure the {@code IDAJavaInstance}s are
		 * removed from the Hashtable when the corresponding IDA process exits.
		 */
		class CleanupThread extends Thread {
			@Override
			public void run() {
				try {
					// Wait for the enclosing instance's OS process
					final int code = IDAJavaInstance.this.process.waitFor();
					logger.info("Process ended with exit code " + code);
				} catch (InterruptedException e) {
					logger.warn("Interrupted while waiting for process " +
							"to finish");
				}
				
				final Iterator<IDAJavaInstance> i =
						instances.values().iterator();
				while (i.hasNext())
					if (IDAJavaInstance.this.equals(i.next().process)) {
						i.remove();
						break;
					}
			}
		}
		
		/**
		 * A {@code Date} holding the time the instance was created.
		 * Not currently used.
		 */
		final Date timestamp;
		
		/** Local reference to remote automation object */
		final IdaRemoteAutomation remote;
		
		/** IDA database associated with this instance */
		final String idbFilename;
		
		/** Operating system process corresponding to this instance */
		Process process;
		
		/** Cleanup Thread */
		Thread cleaner;
		
		/**
		 * Constructs a new instance of this class and initializes its field.
		 * @param remote a local reference to a remote IDAJava plugin
		 * @param idbFilename IDA database associcated with the remote plugin
		 */
		public IDAJavaInstance(IdaRemoteAutomation remote, String idbFilename) {
			this.timestamp = new Date();
			this.remote = remote;
			this.idbFilename = idbFilename;
			this.process = null;
			this.cleaner = null;
		}
	}
	
	class ShutdownHookThread extends Thread {
		@Override
		public void run() {
			PoolServer.this.stop();
		}
	}
	
	/** Log4j logger */
	private static Logger logger;
	
	/**
	 * Holds the currently active instances keyed by their names with which
	 * they are bound to the RMI registry
	 */
	private final Hashtable<String, IDAJavaInstance> instances;
	
	/**
	 * A {@code Hashtable} holding synchronization objects that correspond to
	 * {@code Instance}s that have IDA started, but that do not yet have
	 * registered with the pool. The Hashtable is keyed by the IDB filenames in
	 * canonicalized form.
	 */
	private final Hashtable<String, Object> pending;
	
	private boolean stopped;
	private boolean bound;
	
	private String idaCommand;
	
	private Thread shutdownHook;
	
	private int timeout;
	
	public PoolServer(String idaCommand) {
		logger = Logger.getLogger(this.getClass());
		instances =  new Hashtable<String, IDAJavaInstance>();
		stopped = false;
		bound = false;
		pending = new Hashtable<String, Object>();
		timeout = DEFAULT_TIMEOUT;
		
		this.idaCommand = idaCommand;
	}

	public void start() {
		try {
			// Try to locate an existing RMI registry or create a new one
			if (!SetupRegistry.haveRegistry("///"))
				LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			
			// Bind to registry
			UnicastRemoteObject.exportObject(this);
			Naming.rebind(IdaJavaInstancePool.DEFAULT_NAME, this); 
			
			shutdownHook = new ShutdownHookThread();
			Runtime.getRuntime().addShutdownHook(shutdownHook);
			
			stopped = false;
			bound = true;
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e); // Shouldn't happen
		} catch (RemoteException e) {
			logger.error("Error: RMI error", e);
		}
	}
	
	public void stop() {
		// Do nothing if already stopped so that this method may be called
		// unconditionally in shutdown hooks
		if (stopped)
			return;
		stopped = true;
		
		// Also return if the instance pool was not bound to the registry
		if (!bound)
			return;
		
		try {
			Naming.unbind(IdaJavaInstancePool.DEFAULT_NAME);
			bound = false;
		} catch (RemoteException e) {
			logger.warn(e.getMessage(), e);
		} catch (MalformedURLException e) {
			logger.warn(e.getMessage(), e);
		} catch (NotBoundException e) {
			logger.warn(e.getMessage(), e);
		}
		
		// Interrupt any threads waiting for instances
		for (IDAJavaInstance inst : instances.values())
			if (inst.cleaner != null)
				inst.cleaner.interrupt();
	}
	
	private IDAJavaInstance getInstanceByIDB(String idbFilename) {
		for (IDAJavaInstance inst : instances.values())
			if (inst.idbFilename != null &&
					inst.idbFilename.equals(idbFilename))
				return inst;
		return null;
	}
	
	private IDAJavaInstance getInstanceByRemote(IdaRemoteAutomation ira) {
		for (IDAJavaInstance inst : instances.values())
			if (inst.remote != null && inst.remote.equals(ira))
				return inst;
		return null;
	}
	
	public void ensureInstanceDestruction(IdaRemoteAutomation ira) {
		final IDAJavaInstance inst = getInstanceByRemote(ira);
		if (inst == null)
			return;

		try {
			// If the process has not ended, the exception
			// is triggered
			inst.process.exitValue();
		} catch (IllegalThreadStateException e) {
			inst.process.destroy();
		}
	}
	
	private void removeTempFiles(String idbFilename) {
		final String basename = idbFilename.toLowerCase().endsWith(".idb") ?
			idbFilename.substring(0, idbFilename.length() - 4) :
				idbFilename;
		
		try {
			new File(basename + ".id0").delete();
			new File(basename + ".id1").delete();
			new File(basename + ".nam").delete();
			new File(basename + ".til").delete();
		} catch (SecurityException e) {
			logger.warn(e.getMessage());
		}
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public IdaRemoteAutomation createInstance(String idbFilename)
			throws RemoteException {
		return createInstance(idbFilename, timeout);
	}
	
	public IdaRemoteAutomation createInstance(String idbFilename,
			String ... addtnlCmdOpts) throws RemoteException {
		return createInstance(idbFilename, timeout, addtnlCmdOpts);
	}
	
	// InstancePool Interface
	@Override
	public IdaRemoteAutomation createInstance(String idbFilename, int timeout,
			String ... addtnlCmdOpts) throws RemoteException {
		final File idbFile = Files.getCanonicalFile(idbFilename);

		// Check if file does not exist or is not readable
		if (!idbFile.canRead()) {
			logger.warn("Cannot read `'" + idbFile + "\"");
			return null;
		}
		
		idbFilename = idbFile.getPath();
		
		// Exit if there is already a pending request for an instance of this
		// database or if the database is already open
		if (pending.containsKey(idbFilename) || getInstanceByIDB(idbFilename)
				!= null)
			return null;
		
		// Create a new synchronization object 
		final Object sync = new Object();
		pending.put(idbFilename, sync);
		
		// Remove leftover temporary files to keep IDA from asking to "restore
		// unpacked base"
		removeTempFiles(idbFilename);
		
		// Start the actual process
		final String[] command = new String[addtnlCmdOpts.length + 3];
		command[0] = idaCommand;
		command[1] = "-A";
		System.arraycopy(addtnlCmdOpts, 0, command, 2, addtnlCmdOpts.length);
		command[command.length - 1] = idbFilename;
		final ProcessBuilder pb = new ProcessBuilder(command);
		final Process ida;
		try {
			ida = pb.start();
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
			return null;
		} catch (SecurityException e) {
			logger.warn(e.getMessage(), e);
			return null;
		}
			
		IDAJavaInstance found = null;
		int timeoutSecs = 0;
		synchronized (sync) {
			while (found == null) {
				try {
					sync.wait(1000);
					timeoutSecs++;
					if (timeout > 0 && timeoutSecs > timeout)
						break;
				} catch (InterruptedException e) {
					logger.warn(e.getMessage(), e);
					break;
				}
				found = getInstanceByIDB(idbFilename);
			}
		}
		
		if (found == null) {
			logger.warn("Timeout exceeded");
			return null;
		}
		
		found.process = ida;
		
		// Start clean up
		found.cleaner = found.new CleanupThread();
		found.cleaner.start();
		
		return found.remote;
	}

	@Override
	public boolean registerInstance(String boundName, String idbFilename)
			throws RemoteException {
		try {
			final IdaRemoteAutomation ra = (IdaRemoteAutomation) Naming.lookup(
					boundName);
			
			final Object sync = pending.remove(idbFilename);
			if (sync != null) {
				synchronized (sync) {
					instances.put(boundName, new IDAJavaInstance(ra,
							idbFilename));
					sync.notify();
				}
				logger.debug("Registered IDAJava instance " + boundName +
						" with database \"" + idbFilename + "\"");
				return true;
			}
		} catch (MalformedURLException e) {
			logger.warn(e.getMessage());
		} catch (NotBoundException e) {
			logger.warn(e.getMessage());
		}
		return false;
	}

	@Override
	public void unregisterInstance(String boundName) throws RemoteException {
		final IDAJavaInstance inst = instances.remove(boundName);
		if (inst == null)
			return;
		
		logger.debug("Unregistered IDAJava instance " + boundName);
/*		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (Thread.currentThread()) {
					Thread.currentThread().wait(5000);
				}*/
				inst.process.destroy();
				removeTempFiles(inst.idbFilename);
/*			}
		});*/
	}
}
