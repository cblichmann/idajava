/*
 * IDAJava version 0.3
 * Copyright (c)2007-2017 Christian Blichmann
 *
 * IDAJavaInstancePool Interface
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

package de.blichmann.idajava.api.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IdaJavaInstancePool extends Remote {
	/** Default name to use when binding to registry */
	public static final String DEFAULT_NAME =
		IdaRemoteAutomation.NAMING_PREFIX + "PoolServer";
	
	IdaRemoteAutomation createInstance(String databaseFilename, int timeout,
			String ... addtnlCmdOpts) throws RemoteException;
	
	boolean registerInstance(String boundName, String databaseFilename)
			throws RemoteException;
	void unregisterInstance(String boundName) throws RemoteException;
}
