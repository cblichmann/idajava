/*
 * blichmann.de Application Framework for Java version 0.1
 * Copyright (c)2004-2017 Christian Blichmann
 *
 * TypedProperties Class
 */

package de.blichmann.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * The {@code TypedProperties} class supplements the {@link Properties} class
 * with typed versions of the {@code getProperty} and {@code setProperty}
 * methods.
 * <p>Some additional convenience methods for loading and saving XML- and
 * {@code .properties}-files are provided as well. 
 *  
 * <p>Like its ancestor, this class is thread-safe: multiple threads can share
 * a single {@code TypedProperties} object without the need for external
 * synchronization.
 * 
 * @author Christian Blichmann
 */
public class TypedProperties extends Properties {
	private static final long serialVersionUID = -188546156591787486L;

	/**
	 * Reads a property list (key and element pairs) from the specified file.
	 * The file is assumed to use the ISO 8859-1 character encoding.
	 * <p>
	 * The specified file is closed after this method returns.
	 *
	 * @param fileName the file name of the property file
	 * @throws FileNotFoundException if the specified file was not found
	 * @throws IOException if an error occurred when reading from the file
	 * @throws IllegalArgumentException if the file contains a malformed
	 *     Unicode escape sequence.
	 */
	public synchronized void loadFromFile(String fileName)
			throws FileNotFoundException, IOException {
		final BufferedInputStream is = new BufferedInputStream(
				new FileInputStream(fileName));
		try {
			load(is);
		} finally {
			is.close();
		}
	}

	public synchronized void storeToFile(String fileName) throws IOException {
		storeToFile(fileName, null);
	}

	public synchronized void storeToFile(String fileName, String comment)
			throws IOException {
		final BufferedOutputStream os = new BufferedOutputStream(
				new FileOutputStream(fileName));
		try {
			store(os, comment);
		} finally {
			os.close();
		}
	}

	public synchronized void loadFromXML(String fileName)
			throws InvalidPropertiesFormatException, FileNotFoundException,
				IOException {
		loadFromXML(new BufferedInputStream(new FileInputStream(fileName)));
	}

	public synchronized void storeToXML(String fileName) throws IOException {
		storeToXML(fileName, null);
	}

	public synchronized void storeToXML(String fileName, String comment)
			throws IOException {
		storeToXML(new BufferedOutputStream(new FileOutputStream(fileName)),
				comment);
	}

	public String getString(String key) {
		return getProperty(key);
	}

	public String getString(String key, String def) {
		return getProperty(key, def);
	}

	public void setString(String key, String value) {
		if (value != null)
			setProperty(key, value);
	}

	public Integer getInteger(String key) {
		return getInteger(key, null);
	}

	public Integer getInteger(String key, int def) {
		return getInteger(key, new Integer(def));
	}

	public Integer getInteger(String key, Integer def) {
		final String s = getProperty(key);
		
		try {
			if (s != null)
				return Integer.decode(s);
			return def;
		} catch (NumberFormatException e) {
			/* Return default value */
			return def;
		}
	}

	public void setInteger(String key, int value) {
		setProperty(key, Integer.toString(value));
	}

	public Long getLong(String key) {
		return getLong(key, null);
	}

	public Long getLong(String key, int def) {
		return getLong(key, new Long(def));
	}

	public Long getLong(String key, Long def) {
		final String s = getProperty(key);

		try {
			if (s != null)
				return Long.decode(s);
			return def;
		} catch (NumberFormatException e) {
			/* Return default value */
			return def;
		}
	}

	public void setLong(String key, long value) {
		setProperty(key, Long.toString(value));
	}

	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}

	public Boolean getBoolean(String key, boolean def) {
		return getBoolean(key, def); // Auto-boxing
	}

	public Boolean getBoolean(String key, Boolean def) {
		String s = getProperty(key);

		if (s != null) {
			s = s.trim().toLowerCase();
			if (s.equals("true") || s.equals("1") || s.equals("yes")
					|| s.equals("on"))
				return Boolean.TRUE;
			
			if (s.equals("false") || s.equals("0") || s.equals("no")
				|| s.equals("off"))
				return Boolean.FALSE;
		}

		return def;
	}

	public void setBoolean(String key, boolean value) {
		setProperty(key, value ? "true" : "false");
	}
}