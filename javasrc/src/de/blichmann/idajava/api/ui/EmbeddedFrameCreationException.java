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
