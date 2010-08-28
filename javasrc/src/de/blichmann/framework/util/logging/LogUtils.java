/*
 * blichmann.de Application Framework for Java version 0.1
 * Copyright (c)2004-2009 Christian Blichmann
 *
 * LogUtils Class
 *
 * ***********************************************
 * THIS IS PRELIMINARY BETA CODE SUBJECT TO CHANGE
 * ***********************************************
 */

package de.blichmann.framework.util.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * This class provides a namespace for static convenience methods related to the
 * Log4j logging environment. 
 * @author Christian Blichmann
 */
public final class LogUtils {
	
	/** A boolean keeping track of the initialization state */
	private static boolean configured = false; 
	
	/**
	 * Private constructor to disable public construction.
	 */
	private LogUtils() {
	}
	
	/**
	 * Returns whether the logging environment was initialized.
	 * @return {@code true} if logging was configured, {@code false} otherwise.
	 */
	public static boolean isConfigured() {
		return configured;
	}
	
	/**
	 * Configures the logging environment. If {@code conditional} is set to
	 * {@code true}, configures only if the logging environment was not
	 * previously configured. Calls {@link LogUtils#configureAndWatch(boolean,
	 * String, long)} with no delay.
	 * @param conditional specifies whether to configure the logging environment
	 *     regardless of any existing configuration
	 * @param configFilename the name of a configuration file in XML format
	 * @throws FileNotFoundException if the configuration file specified was not
	 *     found
	 * @see DOMConfigurator#configureAndWatch
	 */
	public static void configure(boolean conditional, String configFilename)
			throws FileNotFoundException {
		configureAndWatch(conditional, configFilename, 0);
	}
	
	/**
	 * Configures the logging environment. If {@code conditional} is set to
	 * {@code true}, configures only if the logging environment was not
	 * previously configured. If {@code delay} is greater zero, starts an XML
	 * watchdog thread that checks periodically for changes to the specified
	 * configuration file.
	 * @param conditional specifies whether to configure the logging environment
	 *     regardless of any existing configuration
	 * @param configFilename the name of a configuration file in XML format
	 * @param delay the delay in milliseconds to wait between each check for
	 *     changes to the configuration file. If zero, logging is only
	 *     configured once.
	 * @throws FileNotFoundException if the configuration file specified was not
	 *     found
	 * @see DOMConfigurator#configureAndWatch
	 * @see XMLWatchdog#doOnChange
	 */
	public static void configureAndWatch(boolean conditional,
			String configFilename, long delay) throws FileNotFoundException {
		
		if (conditional && configured)
			return;
		
		// Deal with platform specific path separators
		final String canonicalFilename = configFilename
				.replace("/", File.separator).replace("\\", File.separator);
		
		// Try filesystem first
		if (new File(canonicalFilename).isFile())
			if (delay == 0)
				DOMConfigurator.configure(canonicalFilename);
			else
				DOMConfigurator.configureAndWatch(canonicalFilename, delay);
		else {
			final InputStream is = LogUtils.class.getResourceAsStream(
					configFilename);
			if (is == null)
				throw new FileNotFoundException("Error: Cannot find logging " +
						"configuration file \"" + configFilename + "\"");
			
			new DOMConfigurator().doConfigure(is,
					LogManager.getLoggerRepository());
		}
		
		configured = true;
	}
}
