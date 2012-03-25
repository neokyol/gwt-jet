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

public class LongBoxWrapper extends Wrapper {
	
	private NumericBox numericBox;
	
	/**
	 * Instantiates a new long box wrapper.
	 *
	 * @param objSetter the obj setter
	 */
	public LongBoxWrapper(ObjectSetter objSetter) {
		this(objSetter, false);
	}
	
	/**
	 * Instantiates a new long box wrapper.
	 *
	 * @param objSetter the obj setter
	 * @param useValueAsString the use value as string
	 */
	public LongBoxWrapper(ObjectSetter objSetter, boolean useValueAsString) {
		this(objSetter, new NumericBox(), useValueAsString);
	}
	
	/**
	 * Instantiates a new long box wrapper.
	 *
	 * @param objSetter the obj setter
	 * @param numericBox the numeric box
	 */
	public LongBoxWrapper(ObjectSetter objSetter, NumericBox numericBox) {
		this(objSetter, numericBox, false);
	}
	
	/**
	 * Instantiates a new long box wrapper.
	 *
	 * @param objSetter the obj setter
	 * @param numericBox the numeric box
	 * @param useValueAsString the use value as string
	 */
	public LongBoxWrapper(ObjectSetter objSetter, NumericBox numericBox, boolean useValueAsString) {
		super(useValueAsString);
		this.objSetter = objSetter;
		this.numericBox = numericBox;
		if(objSetter == null) {
			System.out.println("!!! objSetter is null - have you used a null property name?");
		}
		if(objSetter.getValue() == null) {
			numericBox.setText("");
		} else {
			numericBox.setText(formatValue(objSetter.getValue().toString()));
		}
		
		numericBox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent arg0) {
				setProperty(getValor());
			}
		});
		
		initWidget(numericBox);
	}
	
	/**
	 * Gets the text box.
	 *
	 * @return the text box
	 */
	public TextBox getTextBox() {
		return this.numericBox;
	}
	
	/**
	 * Gets the valor.
	 *
	 * @return the valor
	 */
	protected Object getValor() {
		String valorInteger = LongBoxWrapper.this.numericBox.getText();
		if(valorInteger.isEmpty()) {
			return null;
		} else {
			return Long.parseLong(valorInteger);
		}
	}

	@Override
	protected String getValueAsString() {
		return LongBoxWrapper.this.numericBox.getText();
	}

}
