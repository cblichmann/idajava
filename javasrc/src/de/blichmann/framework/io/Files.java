/*
 * blichmann.de Application Framework for Java version 0.1
 * Copyright (c)2004-2017 Christian Blichmann
 *
 * Files Class
 */

package de.blichmann.framework.io;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.*;

public final class Files {
    /**
     * Private constructor to disable public construction.
     */
	private Files() {	
	}
	
	public static boolean extensionEquals(String fileName, String extension) {
		return fileName.substring(fileName.length() - extension.length())
				.equalsIgnoreCase(extension);
	}
	
	public static int wildcardMatches(String fileName, String wildcard) {
		return internalWildcardMatches(fileName, wildcard, false);
	}
	
	public static int wildcardMatchesIgnoreCase(String fileName,
			String wildcard) {
		return internalWildcardMatches(fileName, wildcard, true);
	}
	
	private static int internalWildcardMatches(String fileName, String wildcard,
			boolean ignoreCase) {
		int result = 0;
		
		// We do our own case-folding 
		if (ignoreCase) {
			fileName = fileName.toLowerCase();
			wildcard = wildcard.toLowerCase();
		}

		// Split the wildcard string at ';' characters
		// Note: This means file names cannot contain semicolon characters 
		final StringTokenizer st = new StringTokenizer(wildcard, ";");
		String mask;
		while (st.hasMoreTokens()) {
			result++;
			// Convert wildcard to regular expression by escaping characters
			mask = st.nextToken()
					.replace("\\", "\\\\")
					.replace("^", "\\^").replace("$", "\\$")
					.replace("[", "\\[").replace("]", "\\]")
					.replace("(", "\\(").replace(")", "\\)")
					.replace(".", "\\.").replace("|", "\\|")
					.replace("*", ".*")
					.replace('?', '.')
					+ "$";
			
			if (Pattern.matches(mask, fileName))
				return result;
		}
		return 0;
	}
	
	public static String makeRelativeTo(String fileName, String newParent) {
		return makeRelativeTo(new File(fileName), new File(newParent))
				.getPath();
	}

	public static File makeRelativeTo(File file, File newParent) {
		return getCanonicalFile(newParent.getPath() + File.separator +
				file.getName()); 
	}

	/**
	 * Converts a path specification for Unix/Windows to a path representation
	 * for the current platform. Calls {@code getCanonicalFile()} to do the
	 * conversion and so does <em>not</em> throw {@code IOException}.
	 * @param fileName a system dependent path specification
	 * @return a path specification for the current platform.
     * @throws SecurityException If a required system property value cannot be
     *     accessed.
	 */
	public static String getCanonicalPath(String fileName) {
		return getCanonicalFile(fileName).getPath();
	}

	/**
	 * Converts a path specification for Unix/Windows to an abstract path name
	 * for the current platform. Slashes and backslashes are replaced by
	 * <tt>File.separatorChar</tt>. Double separators are replaced by a single
	 * one. UNC names are not supported.
	 * <p>Does <em>not</em> throw {@code IOException}.
	 * @param fileName a system dependent path specification
	 * @see <a href="http://msdn.microsoft.com/library/en-us/off2000/html/defUNC.asp">
	 *     MSDN glossary entry: UNC names</a>
	 * @return an abstract path name for the current platform.
     * @throws SecurityException If a required system property value cannot be
     *     accessed, or if a security manager exists and its <code>{@link
     *     java.lang.SecurityManager#checkRead}</code> method denies read access
	 */
	public static File getCanonicalFile(String fileName) {
		final String fn = fileName
				.replace('/', File.separatorChar)
				.replace('\\', File.separatorChar)
				.replace(File.separator + File.separator, File.separator)
				.replace(File.separator + "." + File.separator, File.separator);
		try {
			return new File(fn).getCanonicalFile();
		} catch (IOException e) {
			return new File(fn).getAbsoluteFile();
		}
	}
}
