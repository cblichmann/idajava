/*
 * IDAJava version 0.2
 * Copyright (c)2007-2009 Christian Blichmann
 *
 * RemoteAutomationTest Class
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

package de.blichmann.idajava.samples;

import java.io.*;
import java.rmi.*;

import de.blichmann.framework.util.logging.LogUtils;
import de.blichmann.idajava.rmi.*;
import de.blichmann.idajava.api.io.IdaConsoleOutputStream;
import de.blichmann.idajava.api.rmi.IdaJavaInstancePool;
import de.blichmann.idajava.api.rmi.IdaRemoteAutomation;

public class RemoteAutomationTest {
	
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.out.println("usage: java " +
					RemoteAutomationTest.class.getCanonicalName() +
					" IDAEXE IDBFILE");
			return;
		}
		
		LogUtils.configure(true, "config/logging.xml");
		
		final PoolServer pool = new PoolServer(args[0]);
		pool.start();
		
		IdaRemoteAutomation ira;
		IdaJavaInstancePool ip = (IdaJavaInstancePool) Naming.lookup(
				"///" + IdaJavaInstancePool.DEFAULT_NAME);
		ira = ip.createInstance(args[1], 0);
		
		if (ira == null) {
			System.out.println("remote failed");
			return;
		}
		
		final PrintStream ps = new PrintStream(new IdaConsoleOutputStream(ira),
				true);
/*		System.out.println("Getting basic block instruction bytes...");
		ps.println("Getting basic block instruction bytes...");
		
		long ea = 0x40bda0;
		int size = ira.analyzeInstr(ea);
		
		short[] buf = ira.getManyBytes(ea, size);
		if (buf != null) {
			System.out.print("got instruction byte: ");
			for (short b : buf)
				System.out.print(b);
			System.out.println();
		}*/
		ps.println("Hello from Java!");
				
//		ira.exit(0);
//		pool.ensureInstanceDestruction(ira);
		
//		pool.stop();
//		System.exit(0);
	}
}
