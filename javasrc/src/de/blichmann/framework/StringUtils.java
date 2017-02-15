/*
 * blichmann.de Application Framework for Java version 0.1
 * Copyright (c)2004-2017 Christian Blichmann
 *
 * StringUtils Class
 */

package de.blichmann.framework;

public final class StringUtils {
    /**
     * Private constructor to disable public construction.
     */
	private StringUtils() {	
	}
	
	/**
	 * Converts an array of bytes into a hexadecimal representation. Not
	 * particularly efficient.
	 * @param data
	 * @return
	 */
	public static String toHexString(final byte[] data) {
		// TODO: Write own code for conversion
    	final StringBuilder result = new StringBuilder();
		for (int i = 0; i < data.length; ++i)
			result.append(Integer.toHexString((data[i] & 0xFF) | 0x100)
					.substring(1, 3));

        return result.toString();
	}

	/**
	 * Computes the Hamming distance of two strings.
	 * @param first the first string
	 * @param second the second string
	 * @return the Hamming distance
	 * @throws IllegalArgumentException if the strings do not have the same
	 *     length.
	 */
	public static int getHammingDistance(final String first,
			final String second) {
		// TODO: Submit a version to Apache Commons Lang
		if (first.length() != second.length())
			throw new IllegalArgumentException("Strings must have same length");
		
		int result = 0;
		for (int i = 0; i < first.length(); i++)
			if (first.charAt(i) != second.charAt(i))
				result++;
		
		return result;
	}
}
