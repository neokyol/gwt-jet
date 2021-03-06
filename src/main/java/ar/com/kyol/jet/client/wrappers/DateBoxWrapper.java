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

import java.util.Date;

import ar.com.kyol.jet.client.ObjectSetter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.TypeOracle;

public class DateBoxWrapper extends Wrapper {
	
	private Date date;
	private DateBox dateBox;
	private ObjectSetter objSetter;

	/**
	 * Instantiates a new date box wrapper.
	 *
	 * @param date the date
	 * @param objSetter the obj setter
	 */
	public DateBoxWrapper(Date date, ObjectSetter objSetter) {
		this(date, objSetter,new DateBox());
	}
	
	/**
	 * Instantiates a new date box wrapper.
	 *
	 * @param date the date
	 * @param objSetter the obj setter
	 * @param dateBox the date box
	 */
	public DateBoxWrapper(Date date, ObjectSetter objSetter, DateBox dateBox) {
		this(date, objSetter, dateBox, false);
	}
	
	/**
	 * Instantiates a new date box wrapper.
	 *
	 * @param date the date
	 * @param objSetter the obj setter
	 * @param dateBox the date box
	 * @param useValueAsString the use value as string
	 */
	public DateBoxWrapper(Date date, ObjectSetter objSetter, final DateBox dateBox, boolean useValueAsString) {
		super(useValueAsString);
		this.dateBox = dateBox;
		this.date = date;
		this.objSetter = objSetter;
		dateBox.setValue(this.date);
		String format = "dd/MM/yyyy";
		if(objSetter.getFormat() != null && !objSetter.getFormat().equals("")) {
			format = objSetter.getFormat();
		}
		dateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(format)));
		
		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			@SuppressWarnings("rawtypes")
			public void onValueChange(ValueChangeEvent<Date> arg0) {
				if(DateBoxWrapper.this.date == null) {
					ClassType cType = TypeOracle.Instance.getClassType(DateBoxWrapper.this.objSetter.getObj().getClass());
					DateBoxWrapper.this.date = new Date();
					cType.invoke(DateBoxWrapper.this.objSetter.getObj(), DateBoxWrapper.this.objSetter.getSetter(), new Object[]{DateBoxWrapper.this.date});
				}

				if(DateBoxWrapper.this.dateBox.getValue() != null) {
					DateBoxWrapper.this.date.setTime(DateBoxWrapper.this.dateBox.getValue().getTime());
				} else {
					DateBoxWrapper.this.date = null;
					ClassType cType = TypeOracle.Instance.getClassType(DateBoxWrapper.this.objSetter.getObj().getClass());
					cType.invoke(DateBoxWrapper.this.objSetter.getObj(), DateBoxWrapper.this.objSetter.getSetter(), new Object[]{DateBoxWrapper.this.date});
				}
			}
		});
		
		//gwt issue 4084 (Fixed in version 2.5)
		dateBox.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if("".equals(event.getValue()) || null == event.getValue()) {
					ValueChangeEvent.fire(dateBox, null);
				}
			}
		});
		initWidget(dateBox);
	}

	@Override
	protected String getValueAsString() {
		return this.dateBox.getValue().toString();
	}

}
