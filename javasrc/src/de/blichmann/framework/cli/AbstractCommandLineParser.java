/*
 * blichmann.de Application Framework for Java version 0.1
 * Copyright (c)2004-2009 Christian Blichmann
 *
 * AbstractCommandLineParser Class
 *
 * ***********************************************
 * THIS IS PRELIMINARY BETA CODE SUBJECT TO CHANGE
 * ***********************************************
 */

package de.blichmann.framework.cli;

import java.util.Iterator;

import de.blichmann.framework.util.ArraysEx;

public abstract class AbstractCommandLineParser {
	/**
	 * Parses the command line and sets appropriate configuration options.
	 * @param args array with command line arguments
	 * @throws CommandLineException if help was requested or the command line
	 *     could not be parsed
	 */
	public void parseCommandLine(final String[] args)
			throws CommandLineException {
		final Iterator<String> i = ArraysEx.iterator("", args);
		String arg;
		boolean fSpec = false;
		while (i.hasNext()) {
			arg = i.next();
			
			// Check for option termination symbol
			if (arg.equals("--")) {
				// All following options are files
				fSpec = true;
				continue;
			}
			
			if (!fSpec)
				if (arg.startsWith("--"))
					handleLongOption(arg, i);
				else if (arg.startsWith("-"))
					handleShortOption(arg, i);
				else
					// At this point anything must be a file/directory spec
					// since the handle method advance i for every
					// additional argument
					fSpec = true;
			
			if (fSpec)
				handleFileListSpec(arg);
		}
	}

	/**
	 * Handles a single long command line option. A long command line option
	 * begins with `--'.
	 * @param arg the command line argument
	 * @param i the iterator for the current command line
	 * @throws CommandLineException if an unrecognized option was encountered
	 */
	public void handleLongOption(String arg, Iterator<String> i)
			throws CommandLineException {
		// TODO: Implement do-nothing behavior in an adapter class
		/* Do nothing */
	}
	
	/**
	 * Handles single short command line options. A short command line option
	 * begins with `-'.
	 * @param arg the command line argument
	 * @param i the iterator for the current command line
	 * @throws CommandLineException if an invalid option was encountered
	 */
	public void handleShortOption(String arg, Iterator<String> i)
			throws CommandLineException {
		// TODO: Implement do-nothing behavior in an adapter class
		/* Do nothing */
	}
	
	/**
	 * Handles a single file command line argument.
	 * @param arg the command line argument
	 * @throws CommandLineException if there was an I/O error
	 */
	public void handleFileListSpec(String arg) throws CommandLineException {
		// TODO: Implement do-nothing behavior in an adapter class
		/* Do nothing */
	}
	
	/**
	 * Throws a {@code CommandLineException} with an optional detail message.
	 * @param message an optional detail message
	 * @throws CommandLineException always thrown
	 */
	public void printCommandLineHelp(String message)
			throws CommandLineException {
		// TODO: Implement do-nothing behavior in an adapter class
		/* Do nothing by default */
	}
	
	/**
	 * Throws a {@code CommandLineException} with the specified detail message.
	 * @param message the message
	 * @throws CommandLineException always thrown
	 */
	public void printCommandLineError(String message)
			throws CommandLineException {
		throw new CommandLineException(message, null);
	}
}
