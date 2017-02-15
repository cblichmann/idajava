/*
 * IDAJava version 0.3
 * Copyright (c)2007-2017 Christian Blichmann
 *
 * EmbeddedFrameCreationException Class
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

package de.blichmann.idajava.api.ui;

/**
 * Thrown on error in {@link IdaEmbeddedFrameFactory}.
 * @author Christian Blichmann
 * @since 0.2
 */
public class EmbeddedFrameCreationException extends Exception {
	private static final long serialVersionUID = 543946868130872544L;

	public EmbeddedFrameCreationException(String message) {
		super(message);
	}

    public EmbeddedFrameCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
