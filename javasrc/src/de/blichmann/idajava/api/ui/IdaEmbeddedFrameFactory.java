package de.blichmann.idajava.api.ui;

import java.awt.Frame;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.blichmann.idajava.natives.IdaJava;
import de.blichmann.idajava.natives.IdaJavaConstants;
import de.blichmann.idajava.natives.SWIGTYPE_p_Forms__TForm;

public class IdaEmbeddedFrameFactory {
    /** Default form flags when creating an IDA TForm */
	static final int DEFAULT_FORMFLAGS = IdaJavaConstants.FORM_MDI |
			IdaJavaConstants.FORM_TAB | IdaJavaConstants.FORM_MENU |
			IdaJavaConstants.FORM_RESTORE;

	/** Internal flag specifying whether Swing was already initialized */
	static boolean swingInitialized = false;

	/**
	 * Initializes the Swing UI manager and default focus traversal policy.
	 */
	static synchronized void initializeSwing() {
	    if (swingInitialized)
	    	return;
	    swingInitialized = true;
	    try {
	    	// Initialize the default focus traversal policy
	    	Class<?>[] emptyClass = new Class[0];
	    	Object[] emptyObject = new Object[0];
	    	Class<?> clazz = Class.forName("javax.swing.UIManager");
	    	Method method = clazz.getMethod("getDefaults", emptyClass);
	    	if (method != null)
	    		method.invoke(clazz, emptyObject);
		} catch (Throwable e) { /* Eat */ }
	}

	public static Frame createFrame(final String title)
			throws EmbeddedFrameCreationException {
		return createFrame(title, DEFAULT_FORMFLAGS);
	}

	@SuppressWarnings("deprecation")
	public static Frame createFrame(final String title, int formFlags)
			throws EmbeddedFrameCreationException {
		// Note: create_tform() always returns an invalid handle. This is
		//       because IDA uses the VCL to show its forms. VCL TForm's
		//       get their window handle on-demand, i.e. when shown. So we
		//       throw away the window handle here.
		int[] outHandle = new int[1];
		SWIGTYPE_p_Forms__TForm form = IdaJava.create_tform(title, outHandle);
		IdaJava.open_tform(form, formFlags);
		int handle = IdaJava.get_tform_handle(form);//IdaJava.getInternalHandle();

        // Install custom window procedure
		if (!IdaJava.initIdaEmbeddedWindow(form))
			throw new EmbeddedFrameCreationException("Could not initialize " +
					"embedded window (see native error message above)");

		// Some JREs have implemented the embedded frame constructor to take an
		// integer and other JREs take a long.  To handle this binary
		// incompatibility, use reflection to create the embedded frame.
        Class<?> clazz = null;
        try {
        	final String embeddedFrameClass = IdaJava.getParameter(
        			"EmbeddedFrameClass");
        	final String className = embeddedFrameClass != null ?
        			embeddedFrameClass : "sun.awt.windows.WEmbeddedFrame";
        	clazz = Class.forName(className);
        } catch (Throwable e) {
        	// Rethrow as exception
			throw new EmbeddedFrameCreationException(
					"Embedded Frame class not found", e);
        }

        initializeSwing();

        Object value = null;
        Constructor<?> constructor = null;
        try {
        	constructor = clazz.getConstructor(new Class[] { int.class });
        	value = constructor.newInstance(new Object[] { handle });
        } catch (Throwable e1) {
			try {
				constructor = clazz.getConstructor(new Class[] { long.class });
				value = constructor.newInstance(
						new Object[] { new Long(handle) });
			} catch (Throwable e2) {
				throw new EmbeddedFrameCreationException(
						"Failed to pass window handle to constructor on " +
						"embedded frame class, tried parameter types were: " +
						"int, long", e2);
			}
        }
        final Frame frame = (Frame) value;

        // This is necessary to make lightweight components
        // directly added to the frame receive mouse events
        // properly.
        frame.addNotify();

        // TEMPORARY CODE
        // For some reason, the graphics configuration of the embedded
        // frame is not initialized properly. This causes an exception
        // when the depth of the screen is changed.
        try {
			clazz = Class.forName("sun.awt.windows.WComponentPeer");
			Field field = clazz.getDeclaredField("winGraphicsConfig");
			field.setAccessible(true);
			field.set(frame.getPeer(), frame.getGraphicsConfiguration());
        } catch (Throwable e3) {
        }

        return frame;
	}
}
