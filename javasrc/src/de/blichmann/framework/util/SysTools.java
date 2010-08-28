/*
 * blichmann.de Application Framework for Java version 0.1
 * Copyright (c)2004-2009 Christian Blichmann
 *
 * SysTools Class
 *
 * ***********************************************
 * THIS IS PRELIMINARY BETA CODE SUBJECT TO CHANGE
 * ***********************************************
 */

package de.blichmann.framework.util;

public class SysTools {
	
	/**
	 * Returns the version number of the current Java VM. For version
	 * "1.6.0_04" returns {@code 1.6004}.
	 * @return the version number
	 */
	public static double getVMVersion() {
		final String[] versionSplit = System.getProperty("java.version")
			.replaceAll("_", "").split("\\.");
		
		StringBuffer v;
		double vmVersion = 0;
		try {
			if (versionSplit.length > 0) {
				v = new StringBuffer(versionSplit[0] +
						(versionSplit.length > 1 ? "." : ""));
				for (int i = 1; i < versionSplit.length; i++)
					v.append(versionSplit[i]);
				vmVersion = Double.parseDouble(v.toString());
			}
		} catch (Exception e) { /* Eat */ }
		
		return vmVersion;
	}
}
