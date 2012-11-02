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

import java.sql.Timestamp;
import java.util.Date;

import ar.com.kyol.jet.client.ObjectSetter;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

@SuppressWarnings("deprecation") //TODO Calendar in gwt not mature enough (see http://code.google.com/p/google-web-toolkit/issues/detail?id=603)
public class TimestampBoxWrapper extends Wrapper {
	
	private Timestamp timestamp;
	private Timestamp originalTimestamp;
	private DateBox dateBox;
	private HoursBox hoursBox;
	private MinutesBox minutesBox;
	
	/**
	 * Instantiates a new timestamp box wrapper.
	 *
	 * @param timestamp the timestamp
	 */
	public TimestampBoxWrapper(ObjectSetter objSetter) {
		this(objSetter, new DateBox(), new HoursBox(), new MinutesBox());
	}
	
	/**
	 * Instantiates a new timestamp box wrapper.
	 *
	 * @param timestamp the timestamp
	 * @param dateBox the date box
	 * @param hoursBox the hours box
	 * @param minutesBox the minutes box
	 */
	public TimestampBoxWrapper(ObjectSetter objSetter, DateBox dateBox, HoursBox hoursBox, MinutesBox minutesBox) {
		this(objSetter, dateBox, hoursBox, minutesBox, false);
	}
	
	/**
	 * Instantiates a new timestamp box wrapper.
	 *
	 * @param timestamp the timestamp
	 * @param dateBox the date box
	 * @param hoursBox the hours box
	 * @param minutesBox the minutes box
	 * @param useValueAsString the use value as string
	 */
	public TimestampBoxWrapper(ObjectSetter objSetter, DateBox dateBox, HoursBox hoursBox, MinutesBox minutesBox, boolean useValueAsString) {
		super(useValueAsString);
		this.dateBox = dateBox;
		this.hoursBox = hoursBox;
		this.minutesBox = minutesBox;
		this.objSetter = objSetter;
		refreshTimestamp();
	}
	
	@Override
	public void initWrapper(ObjectSetter objSetter) {
		super.initWrapper(objSetter);
		
		if(objSetter.getFormat() != null && objSetter.getFormat().toLowerCase().startsWith("h")) {
			initTimeWidget();
		} else {
			initDateWidget();
		}
		
	}
	
	private void initTimeWidget() {
		int hour = this.timestamp.getHours();
		int minute = this.timestamp.getMinutes();
		
		hoursBox.setSelectedIndex(hour);
		minutesBox.setSelectedIndex(minute);
		
		hoursBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent arg0) {
				refreshTimestamp();
				TimestampBoxWrapper.this.timestamp.setHours(Integer.parseInt(hoursBox.getValue(hoursBox.getSelectedIndex())));
				setProperty(TimestampBoxWrapper.this.timestamp);
			}

		});
		
		minutesBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent arg0) {
				refreshTimestamp();
				TimestampBoxWrapper.this.timestamp.setMinutes(Integer.parseInt(minutesBox.getValue(minutesBox.getSelectedIndex())));
				setProperty(TimestampBoxWrapper.this.timestamp);
			}

			
		});
		
		//ChangeEvent.fireNativeEvent(Document.get().createChangeEvent(), hoursBox);
		//ChangeEvent.fireNativeEvent(Document.get().createChangeEvent(), minutesBox);
		
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(hoursBox);
		panel.add(minutesBox);
		
		initWidget(panel);
	}
	
	private void initDateWidget() {
		if(this.originalTimestamp != null) {
			dateBox.setValue(new Date(this.timestamp.getTime()));
		}
		String format = "dd/MM/yyyy";
		if(objSetter.getFormat() != null && !objSetter.getFormat().equals("")) {
			format = objSetter.getFormat();
		}
		dateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(format)));
		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> arg0) {
				refreshTimestamp();
				Date inputDate = TimestampBoxWrapper.this.dateBox.getValue();
				TimestampBoxWrapper.this.timestamp.setYear(inputDate.getYear());
				TimestampBoxWrapper.this.timestamp.setMonth(inputDate.getMonth());
				TimestampBoxWrapper.this.timestamp.setDate(inputDate.getDate());
				setProperty(TimestampBoxWrapper.this.timestamp);
			}
		});
		dateBox.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				if("".equals(arg0.getValue())) {
					cleanDate();
				}
			}

		});
		initWidget(dateBox);
	}

	private void cleanDate() {
		dateBox.setValue(null);
		setProperty(null);
	}
	
	@Override
	protected String getValueAsString() {
		return this.dateBox.getValue().toString();
	}
	
	protected void refreshTimestamp() {
		originalTimestamp = (Timestamp)this.getProperty();
		if(originalTimestamp!=null){
			this.timestamp = new Timestamp(originalTimestamp.getTime());
		} else {
			this.timestamp = new Timestamp(1);
			this.timestamp.setYear(70);
			this.timestamp.setMonth(0);
			this.timestamp.setDate(1);
			this.timestamp.setHours(0);
			this.timestamp.setMinutes(0);
			this.timestamp.setSeconds(0);
			this.timestamp.setNanos(0);
		}
	}

}
