/*
 * blichmann.de Application Framework for Java version 0.1
 * Copyright (c)2004-2009 Christian Blichmann
 *
 * CommandLineException Class
 *
 * ***********************************************
 * THIS IS PRELIMINARY BETA CODE SUBJECT TO CHANGE
 * ***********************************************
 */

package de.blichmann.framework.cli;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Usage:
 * <pre>
 *     void parseCommandLine(String[] args) throws CommandLineException { ... }
 *     ...
 *     try {
 *         parseCommandLine(args);
 *         
 *         // Use arguments
 *         ...
 *     } catch (CommandLineException e) {
 *         e.printHelpMessage();
 *     }
 * </pre>
 * 
 * @author Christian Blichmann
 */
public class CommandLineException extends RuntimeException {
	private static final long serialVersionUID = -5140305905978739409L;

	private String helpMessage; 
	
    /**
     * Constructs a new {@code CommandLineException} with the specified detail
     * message and help message.
     * @param message the detail message 
     */
	public CommandLineException(String message, String helpMessage) {
		super(message);
		this.helpMessage = helpMessage;
	}
	
    /**
     * Constructs a new {@code CommandLineException} with the specified detail
     * message.
     * @param message the detail message 
     */
	public CommandLineException(String message) {
		this(message, null);
	}
	
	/**
     * Creates a help message for this command line exception. Subclasses may
     * override this method in order to produce a specific message.
     * <p>For subclasses that do not override this method, the default
     * implementation returns an empty string.
     * @return The help message of this exception.
     */
	public String getHelpMessage() {
		return helpMessage != null ? helpMessage : "";
	}
	
    /**
     * Creates a localized help message for this command line exception.
     * Subclasses may override this method in order to produce a locale-specific
     * message.
     * <p>For subclasses that do not override this method, the default
     * implementation returns the same result as {@code getHelpMessage()}.
     * @return The localized help message of this exception.
     */
	public String getLocalizedHelpMessage() {
		return getHelpMessage();
	}
	
	/**
	 * Prints the help message and the exception message to the standard output
	 * stream.
	 */
	public void printHelpMessage() {
		printHelpMessage(System.out);
	}
	
	/**
	 * Prints the help message and the exception message to the specified print
	 * stream.
     * @param s {@code PrintStream} to use for output
	 */
	public void printHelpMessage(PrintStream s) {
        synchronized (s) {
			final String message = getLocalizedMessage(); 
			if (message != null)
				s.println(message);
			if (helpMessage != null)
				s.println(getLocalizedHelpMessage());
        }
	}
	
	/**
	 * Prints the help message and the exception message to the specified print
	 * writer.
     * @param s {@code PrintWriter} to use for output
	 */
	public void printHelpMessage(PrintWriter s) {
        synchronized (s) {
			final String message = getLocalizedMessage(); 
			if (message != null)
				s.println(message);
			if (helpMessage != null)
				s.println(getLocalizedHelpMessage());
        }
	}
}
