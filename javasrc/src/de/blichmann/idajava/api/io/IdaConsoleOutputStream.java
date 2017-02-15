/*
 * IDAJava version 0.3
 * Copyright (c)2007-2017 Christian Blichmann
 *
 * IdaConsoleOutputStream Class
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

package de.blichmann.idajava.api.io;

import java.io.IOException;
import java.io.OutputStream;

import de.blichmann.idajava.api.rmi.IdaRemoteAutomation;
import de.blichmann.idajava.natives.IdaJava;

public class IdaConsoleOutputStream extends OutputStream {
	private final StringBuffer buf = new StringBuffer();
	private final IdaRemoteAutomation remotePlugin;
	
	public IdaConsoleOutputStream() {
		remotePlugin = null;
	}
	
	public IdaConsoleOutputStream(IdaRemoteAutomation remotePlugin) {
		this.remotePlugin = remotePlugin;
	}

	@Override
	public void write(int b) throws IOException {
		final char c = Character.toChars(b)[0];
		switch (c) {
		case '\r':
			// Ignore carriage-return, IDA only uses line-feed for EOL
			break;
		case '\t':
			buf.append("        "); // exactly 8 spaces
			break;
		default:
			buf.append(c);
		}
	}
	
	@Override
    public void flush() throws IOException {
		if (remotePlugin == null)
			IdaJava.msg(buf.toString());
		else
			remotePlugin.msg(buf.toString());
		buf.delete(0, buf.length());
    }
}
