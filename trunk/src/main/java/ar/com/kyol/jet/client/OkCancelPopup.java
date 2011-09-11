/*
 * gwt-jet 
 * 
 * Widgets wrapping objects with reflection autopopulation for fast coding
 * 
 * The gwt-jet library provides a fast, flexible and easy way to wrap business 
 * objects that you want to show at the front-end. The jet classes automatically 
 * create the corresponding widget and automagically populate the user modified 
 * values into the original object.
 * 
 * gwt-jet was created by
 * Silvana Muzzopappa & Federico Pugnali
 * (c)2011 - Apache 2.0 license
 * 
 */
package ar.com.kyol.jet.client;

import com.google.gwt.event.dom.client.ClickHandler;

public interface OkCancelPopup {

	/**
	 * Show your popup
	 */
	public abstract void show();
	
	/**
	 * Clean your wrapped object or fields
	 * 
	 */
	public abstract void clean();
	
	/**
	 * Attach this handler to your cancel button.
	 * 
	 * @param handler
	 */
	public abstract void addCancelClickHandler(ClickHandler handler);

}
