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

public class FloatBoxWrapper extends Wrapper {
	
	private FloatBox floatBox;
	
	/**
	 * Instantiates a new float box wrapper.
	 *
	 * @param objSetter the obj setter
	 */
	public FloatBoxWrapper(ObjectSetter objSetter) {
		this(objSetter, false);
	}
	
	/**
	 * Instantiates a new float box wrapper.
	 *
	 * @param objSetter the obj setter
	 * @param useValueAsString the use value as string
	 */
	public FloatBoxWrapper(ObjectSetter objSetter, boolean useValueAsString) {
		this(objSetter, new FloatBox(), useValueAsString);
	}
	
	/**
	 * Instantiates a new float box wrapper.
	 *
	 * @param objSetter the obj setter
	 * @param floatBox the float box
	 */
	public FloatBoxWrapper(ObjectSetter objSetter, FloatBox floatBox) {
		this(objSetter, floatBox, false);
	}
	
	/**
	 * Instantiates a new float box wrapper.
	 *
	 * @param objSetter the obj setter
	 * @param floatBox the float box
	 * @param useValueAsString the use value as string
	 */
	public FloatBoxWrapper(ObjectSetter objSetter, FloatBox floatBox, boolean useValueAsString) {
		super(useValueAsString);
		this.objSetter = objSetter;
		this.floatBox = floatBox;
		if(objSetter == null) {
			System.out.println("!!! objSetter is null - have you used a null property name?");
		}
		if(objSetter.getValue() == null) {
			floatBox.setText("");
		} else {
			floatBox.setText(formatValue(objSetter.getValue().toString()));
		}
		
		floatBox.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent arg0) {
				setProperty(getValor());
			}
		});
		
		initWidget(floatBox);
	}
	
	public TextBox getTextBox() {
		return this.floatBox;
	}
	
	protected Object getValor() {
		String valorFloat = FloatBoxWrapper.this.floatBox.getText();
		if(valorFloat.isEmpty()) {
			return null;
		} else {
			return Float.parseFloat(valorFloat);
		}
	}

	@Override
	protected String getValueAsString() {
		return FloatBoxWrapper.this.floatBox.getText();
	}

}
