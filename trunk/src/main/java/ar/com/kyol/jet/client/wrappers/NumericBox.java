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

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

public class NumericBox extends TextBox {
	
	/**
	 * Instantiates a new numeric box.
	 */
	public NumericBox() {
		sinkEvents(Event.ONPASTE);
		//addKeyPressHandler(new KeyPressHandler() {
		addKeyDownHandler(new KeyDownHandler() {

			@Override
			//public void onKeyPress(KeyPressEvent event) { //GWT bug: Issue 5558: 	KeyPressEvent.getCharCode returns zero for special keys like enter, escape, tab
			public void onKeyDown(KeyDownEvent event) {
			
				switch(event.getNativeKeyCode()) {
				case KeyCodes.KEY_LEFT:
				case KeyCodes.KEY_DOWN:
				case KeyCodes.KEY_RIGHT:
				case KeyCodes.KEY_UP:
				case KeyCodes.KEY_BACKSPACE:
				case KeyCodes.KEY_DELETE:
				case KeyCodes.KEY_TAB:
				case KeyCodes.KEY_HOME:
				case KeyCodes.KEY_END:
					return;
				}
				
				if((event.getNativeKeyCode() >= 96 && event.getNativeKeyCode() <= 106)) { //numeric pad!
					return;
				}
				
				//FIXME shift!!!	
				if(event.getNativeKeyCode() < 48 || event.getNativeKeyCode() > 57 || event.isAnyModifierKeyDown() || event.isAltKeyDown()) { //numeric keys
					((TextBoxBase)event.getSource()).cancelKey();
				}
				
			}
		});
	}
	
	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent( event );  
		        switch ( event.getTypeInt() )  
		        {  
		            case Event.ONPASTE :  
		            {  
		                event.stopPropagation();  
		                event.preventDefault();  
		                break;  
		            }  
		        }  
	}
	
	public Integer getNumber() {
		return Integer.parseInt(this.getText());
	}

}
