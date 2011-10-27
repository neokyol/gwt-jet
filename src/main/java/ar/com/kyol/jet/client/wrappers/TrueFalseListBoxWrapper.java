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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;

public class TrueFalseListBoxWrapper extends Wrapper {
	
	private ListBox listBox;
	public final static String NULL = "---";
	public final static String TRUE = "sí"; //TODO internationalize this!
	public final static String FALSE = "no";
	
	/**
	 * Instantiates a new true false list box wrapper.
	 *
	 * @param objSetter the obj setter
	 */
	public TrueFalseListBoxWrapper(ObjectSetter objSetter) {
		this(objSetter, false);
	}
	
	/**
	 * Instantiates a new true false list box wrapper.
	 *
	 * @param objSetter the obj setter
	 * @param useValueAsString the use value as string
	 */
	public TrueFalseListBoxWrapper(ObjectSetter objSetter, boolean useValueAsString) {
		this(objSetter, new ListBox(), useValueAsString);
	}
	
	/**
	 * Instantiates a new true false list box wrapper.
	 *
	 * @param objSetter the obj setter
	 * @param listBox the list box
	 */
	public TrueFalseListBoxWrapper(ObjectSetter objSetter, ListBox listBox) {
		this(objSetter, listBox, false);
	}
	
	/**
	 * Instantiates a new true false list box wrapper.
	 *
	 * @param objSetter the obj setter
	 * @param listbox the listbox
	 * @param useValueAsString the use value as string
	 */
	public TrueFalseListBoxWrapper(ObjectSetter objSetter, ListBox listbox, boolean useValueAsString) {
		super(useValueAsString);
		this.objSetter = objSetter;
		listBox = listbox;
		listbox.addItem(NULL, "");
		listBox.addItem(TRUE);
		listBox.addItem(FALSE);
		if(objSetter.getValue()!=null){
			if(useValueAsString){
				listBox.setSelectedIndex(stringToBoolean((String)objSetter.getValue())?1:2);
			} else {
				listBox.setSelectedIndex((Boolean)objSetter.getValue()?1:2);
			}
		} else {
				listBox.setSelectedIndex(0);
		}
	
		listBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent arg0) {
				String value = listBox.getValue(listBox.getSelectedIndex());
				if(value.equals(NULL)) {
					setProperty(null);
				} else if(value.equals(TRUE)) {
					setProperty(true);
				} else if(value.equals(FALSE)) {
					setProperty(false);
				}
			}
		});
		
		initWidget(listBox);
	}
	
	public boolean stringToBoolean(String objSetterValue){
		if(objSetterValue.equalsIgnoreCase("true") || objSetterValue.equalsIgnoreCase("sí") || objSetterValue.equalsIgnoreCase("si")){
			return true;
		}else{
			return false;
		}
	}
	
	public ListBox getListBox() {
		return this.listBox;
	}

	@Override
	protected String getValueAsString() {
		String value = listBox.getValue(listBox.getSelectedIndex());
		String retorno = null;
		if(!value.equals(NULL)) {
			retorno = value.toString();
		} 
		return retorno;
	}

}
