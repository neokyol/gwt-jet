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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;

import ar.com.kyol.jet.client.JetCombo;
import ar.com.kyol.jet.client.ObjectSetter;

public class JetComboWrapper<E> extends Wrapper {
	
	private final JetCombo<E> jetCombo;

	/**
	 * Instantiates a new jet combo wrapper.
	 *
	 * @param objSetter the obj setter
	 * @param jetCombo the jet combo
	 */
	@SuppressWarnings("unchecked")
	public JetComboWrapper(ObjectSetter objSetter, JetCombo<E> jetCombo) {
		super(false);
		this.objSetter = objSetter;
		this.jetCombo = jetCombo;
		if(objSetter.getValue()!=null){
			jetCombo.setSelectedItem((E)objSetter.getValue());
		}
		
		jetCombo.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent arg0) {
				setProperty(JetComboWrapper.this.jetCombo.getSelectedItem());
			}
		});
		
		initWidget(jetCombo);
	}

	@Override
	protected String getValueAsString() {
		return this.jetCombo.getSelectedItem().toString();
	}

}
