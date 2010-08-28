/*
 * blichmann.de Application Framework for Java version 0.1
 * Copyright (c)2004-2009 Christian Blichmann
 *
 * CollectionsEx Class
 *
 * ***********************************************
 * THIS IS PRELIMINARY BETA CODE SUBJECT TO CHANGE
 * ***********************************************
 */

package de.blichmann.framework.util;

import java.util.ArrayList;
import java.util.Comparator;

public class CollectionsEx {
	
    /**
     * Private constructor to disable public construction.
     */
    private CollectionsEx() {
    }

	static class QuickSort {
		private static <T> void quickSort(ArrayList<T> list, Comparator<T> comp,
				int low0, int high0) {
			int low = low0, high = high0;
			if (low >= high)
				return;
			if (low == high - 1) {
				if (comp.compare(list.get(low), list.get(high)) > 0) {
					final T temp = list.get(low);
					list.set(low, list.get(high));
					list.set(high, temp);
				}
				return;
			}
			
			final T pivot = list.get((low + high) / 2);
			list.set((low + high) / 2, list.get(high));
			list.set(high, pivot);
			
			while (low < high) {
				while (comp.compare(list.get(low), pivot) <= 0 && low < high)
					low++;
				while (comp.compare(list.get(high), pivot) >= 0 && low < high)
					high--;
				if (low < high) {
					final T temp = list.get(low);
					list.set(low, list.get(high));
					list.set(high, temp);
				}
			}
			list.set(high0, list.get(high));
			list.set(high, pivot);
			quickSort(list, comp, low0, low - 1);
			quickSort(list, comp, high + 1, high0);
		}
		
		public static <T> void sort(ArrayList<T> list, Comparator<T> comp) {
			quickSort(list, comp, 0, list.size() - 1);
		}
	}
}
