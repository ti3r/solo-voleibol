package org.blanco.solovolei.fragments;

/**
 * Interface to send signals between components that
 * Accept or Cancel commands have been executed.
 * 
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 *
 */
public interface AcceptCancelHandler extends ActionBarCommandsHandler {

	/**
	 * The Accept signal has been triggered in the executor
	 * and the receiver should handle the call.
	 */
	public void triggerAccept();
	/**
	 * The Cancel signal has been triggered in the executor
	 * and the receiver should handle the call.
	 */
	public void triggerCancel();
	
}
