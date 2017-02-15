/*
 * blichmann.de Application Framework for Java version 0.1
 * Copyright (c)2004-2017 Christian Blichmann
 *
 * SimpleLockFile Class
 */

package de.blichmann.framework.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class SimpleLockFile {
	
	private final File lockFile;
	private boolean locked = false;
	
	public SimpleLockFile(URI path) {
		lockFile = new File(path);
	}
	
	public SimpleLockFile(String fileName) {
		lockFile = new File(fileName);
	}
	
	public boolean tryLock() throws SecurityException {
		try {
			locked = lockFile.createNewFile();
			return locked;
		} catch (IOException e) {
			return false;
		}
	}
	
	public void unlock() throws SecurityException {
		if (!locked)
			return;
		locked = !lockFile.delete();
	}
	
	public boolean haveLock() {
		return locked;
	}
}
