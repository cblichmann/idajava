/*
 * blichmann.de Application Framework for Java version 0.1
 * Copyright (c)2004-2017 Christian Blichmann
 *
 * ParsedNamingURL Class
 */

package de.blichmann.framework.util.rmi;

public class ParsedNamingURL {
	private final String host;
	private final int port;
	private final String name;
	
	public ParsedNamingURL(String host, int port, String name) {
	    this.host = host;
	    this.port = port;
	    this.name = name;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getName() {
		return name;
	}
}
