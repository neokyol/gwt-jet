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
package ar.com.kyol.jet.client.wrappers;

import ar.com.kyol.jet.client.ObjectSetter;

import com.google.gwt.user.client.ui.Widget;

public abstract class GenericWrapper extends Wrapper {
	
	
	public abstract Widget getGenericWidget();
	
	/**
	 * Instantiates a new generic wrapper.
	 */
	public GenericWrapper() {
		super(false);
	}
	
	@Override
	public void setProperty(Object obj) {
		if(objSetter == null) {
			System.out.println("!!!");
			System.out.println("!!! objSetter is null - Did you forget to call initWrapper?");
			System.out.println("!!!");
		}
		super.setProperty(obj);
	}
	
	@Override
	public void initWrapper(ObjectSetter objSetter) {
		super.initWrapper(objSetter);
		initWidget(getGenericWidget());
	}
	
	@Override
	protected String getValueAsString() {
		return null;
	}
	
}
