/*
 * blichmann.de Application Framework for Java version 0.1
 * Copyright (c)2004-2017 Christian Blichmann
 *
 * SetupRegistry Class
 */

package de.blichmann.framework.util.rmi;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 * @author Christian Blichmann
 */
public final class SetupRegistry {
    /**
     * Private constructor to disable public construction.
     */
    private SetupRegistry() {
    }

	/**
	 * Determines if there is a usable RMI registry. Tries to connect to an RMI
	 * registry given in the {@code name} parameter.
     * @param name a name in URL format (without the scheme component) 
	 * @return {@code true} if successfully connected to an RMI registry,
	 *     {@code false} otherwise
	 * @exception MalformedURLException if the given name was malformed 
	 */
	public static boolean haveRegistry(String name)
			throws MalformedURLException {
		try {
			// This call does not make an actual connection to the remote
			// registry. It merely returns a local reference.
			final Registry registry = (Registry) Naming.lookup(name);
			
			try {
				// Force connection to remote registry. 
				registry.lookup(""); // Empty string
			} catch (NotBoundException e) {
				// An empty name is never bound so it should always throw a
				// NotBoundException implying that we found a valid Registry.
				return true;
			} catch (ConnectException e) {
				// Connection was actively refused, can also mean that the
				// current security policy forbids a connection
			}
		} catch (RemoteException e) {
			// General communication error
		} catch (NotBoundException e) {
			// Registry was not bound, this is unlikely
		}
		return false;
	}

	public static ParsedNamingURL parseURL(String str)
			throws MalformedURLException {
		// Simple handling of special Naming URLs
		final String LOCAL_REGISTRY = "rmi://localhost:1099/";
		if (str.startsWith("///"))
			str = LOCAL_REGISTRY + str.substring(3);
		else if (str.startsWith("//:/"))
			str = LOCAL_REGISTRY + str.substring(4);
		else if (str.startsWith("rmi:///"))
			str = LOCAL_REGISTRY + str.substring(7);
		else if (str.startsWith("rmi://:/"))
			str = LOCAL_REGISTRY + str.substring(8);
		
		// If all went well, URI should be able to parse the string
		final URI uri;
		try {
			uri = new URI(str);
		} catch (URISyntaxException e) {
			throw (MalformedURLException) (new MalformedURLException(
					"invalid URL String: " + str)).initCause(e);
		}
		
		// Host and port
		final String host = uri.getHost();
		final int port = uri.getPort() > 0 ? uri.getPort() :
			Registry.REGISTRY_PORT;
		
		// Object name
		String name = uri.getPath();
		if (name != null) {
			if (name.isEmpty())
				name = null;
			else if (name.startsWith("/"))
		    	name = name.substring(1);
		}
		
		return new ParsedNamingURL(host, port, name);
	}
		
	public static Registry getRegistry(ParsedNamingURL parsed)
			throws MalformedURLException {
		try {
			// This call does not make an actual connection to the remote
			// registry. It merely returns a local reference.
			final Registry registry = LocateRegistry.getRegistry(
					parsed.getHost(), parsed.getPort());
			
			try {
				// Force connection to remote registry. 
				registry.lookup(""); // Empty string
			} catch (NotBoundException e) {
				// An empty name is never bound so it should always throw a
				// NotBoundException implying that we found a valid Registry.
				return registry;
			} catch (ConnectException e) {
				// Connection refused
			}
		} catch (RemoteException e) {
			// Reference could not be created
		}
		return null;
	}
}
