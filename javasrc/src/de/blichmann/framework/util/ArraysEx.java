/*
 * blichmann.de Application Framework for Java version 0.1
 * Copyright (c)2004-2009 Christian Blichmann
 *
 * ArraysEx Class
 *
 * ***********************************************
 * THIS IS PRELIMINARY BETA CODE SUBJECT TO CHANGE
 * ***********************************************
 */

package de.blichmann.framework.util;

import java.util.Iterator;

public final class ArraysEx {
	
    /**
     * Private constructor to disable public construction.
     */
    private ArraysEx() {
    }

	public static <T> Iterator<T> iterator(final T[] a) {
		return iterator(null, a);
	}

	public static <T> Iterator<T> iterator(final T def, final T[] a) {
		return new Iterator<T>() {
			/* Index of element to be returned by subsequent call to next */
			int cursor = 0;
			
			@Override
			public boolean hasNext() {
				return cursor < a.length;
			}

			@Override
			public T next() {
			    try {
			    	T next = a[cursor++];
					return next;
			    } catch (IndexOutOfBoundsException e) {
			    	return def;
			    }
			}

			@Override
			public void remove() {
				// TODO: Implement
				throw new UnsupportedOperationException();
			}
		};
    }
}
