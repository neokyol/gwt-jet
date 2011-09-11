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

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;

public class CheckBoxWrapper extends Wrapper {
	
	private CheckBox checkBox;
	
	public CheckBoxWrapper(ObjectSetter objSetter) {
		this(objSetter, false);
	}
	
	public CheckBoxWrapper(ObjectSetter objSetter, boolean useValueAsString) {
		this(objSetter, new CheckBox(), useValueAsString);
	}
	
	public CheckBoxWrapper(ObjectSetter objSetter, CheckBox checkBox) {
		this(objSetter, checkBox, false);
	}
	
	public CheckBoxWrapper(ObjectSetter objSetter, CheckBox checkBox, boolean useValueAsString) {
		super(useValueAsString);
		this.objSetter = objSetter;
		this.checkBox = checkBox;
		if(useValueAsString){
			if(objSetter.getValue()!=null){
				checkBox.setValue(new Boolean((String)objSetter.getValue()));
			}
		}else{
			checkBox.setValue((Boolean)objSetter.getValue());
		}
	
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> arg0) {
				setProperty(arg0.getValue());
			}
		});
		
		initWidget(checkBox);
	}
	
	public CheckBox getCheckBox() {
		return this.checkBox;
	}

	@Override
	protected String getValueAsString() {
		return this.checkBox.getValue().toString();
	}

}
