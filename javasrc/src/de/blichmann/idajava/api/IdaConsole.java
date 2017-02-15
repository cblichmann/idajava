/*
 * IDAJava version 0.3
 * Copyright (c)2007-2017 Christian Blichmann
 *
 * IDAConsole Class
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.blichmann.idajava.api;

import java.io.PrintStream;

import de.blichmann.idajava.api.io.IdaConsoleOutputStream;


/**
 * The {@code IDAConsole} class provides a more "natural" interface to the IDA
 * console using its public {@code out} field. It can be used much like
 * {@link System#out}:
 * <pre>
 *   // Output some text to the console
 *   IDAConsole.out.println("Hello, IDA!");
 *   
 *   // Print some formatted number as result of a calculation 
 *   IDAConsole.out.printf("This is a number: %d\n", 6 * 7);
 * </pre>
 * The underlying {@code IDAConsoleOutputStream} is flushed automatically, when
 * either a newline is encountered or one of the {@code println()} methods is
 * invoked.
 * @see System#out
 * @see IdaConsoleOutputStream
 * @author Christian Blichmann
 * @since 0.1
 */
public class IdaConsole {
	public static final PrintStream out;
	
	static {
		out = new PrintStream(new IdaConsoleOutputStream(), true);
	}
}
