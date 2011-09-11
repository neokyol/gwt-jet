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

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.TextBox;

public class TextBoxWrapper extends Wrapper {
	
	private TextBox textBox;
	
	/**
	 * Instantiates a new text box wrapper.
	 *
	 * @param objSetter the obj setter
	 */
	public TextBoxWrapper(ObjectSetter objSetter) {
		this(objSetter, new TextBox());
	}
	
	/**
	 * Instantiates a new text box wrapper.
	 *
	 * @param objSetter the obj setter
	 * @param textBox the text box
	 */
	public TextBoxWrapper(ObjectSetter objSetter, TextBox textBox) {
		super(false);
		this.objSetter = objSetter;
		this.textBox = textBox;
		if(objSetter.getValue() == null) {
			textBox.setText("");
		} else {
			textBox.setText(formatValue(objSetter.getValue().toString()));
		}
		
		textBox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent arg0) {
				setProperty(TextBoxWrapper.this.textBox.getText());
			}
		});
		
		initWidget(textBox);
	}
	
	public TextBox getTextBox() {
		return this.textBox;
	}

	@Override
	protected String getValueAsString() {
		return null; //it will be never called
	}

}
