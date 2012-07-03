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

import com.google.gwt.dom.client.Document;
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
	private DateBox dateBox;
	private HoursBox hoursBox;
	private MinutesBox minutesBox;
	private Timestamp originalTimestamp;
	
	/**
	 * Instantiates a new timestamp box wrapper.
	 *
	 * @param timestamp the timestamp
	 */
	public TimestampBoxWrapper(Timestamp timestamp) {
		this(timestamp, new DateBox(), new HoursBox(), new MinutesBox());
	}
	
	/**
	 * Instantiates a new timestamp box wrapper.
	 *
	 * @param timestamp the timestamp
	 * @param dateBox the date box
	 * @param hoursBox the hours box
	 * @param minutesBox the minutes box
	 */
	public TimestampBoxWrapper(Timestamp timestamp, DateBox dateBox, HoursBox hoursBox, MinutesBox minutesBox) {
		this(timestamp, dateBox, hoursBox, minutesBox, false);
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
	public TimestampBoxWrapper(Timestamp timestamp, DateBox dateBox, HoursBox hoursBox, MinutesBox minutesBox, boolean useValueAsString) {
		super(useValueAsString);
		this.dateBox = dateBox;
		this.hoursBox = hoursBox;
		this.minutesBox = minutesBox;
		this.originalTimestamp = timestamp;
		if(timestamp!=null){
			this.timestamp = new Timestamp(timestamp.getTime());
		} else {
			this.timestamp = new Timestamp(1);
		}
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
				TimestampBoxWrapper.this.timestamp.setHours(Integer.parseInt(hoursBox.getValue(hoursBox.getSelectedIndex())));
				setProperty(TimestampBoxWrapper.this.timestamp);
			}

		});
		
		minutesBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent arg0) {
				TimestampBoxWrapper.this.timestamp.setMinutes(Integer.parseInt(minutesBox.getValue(minutesBox.getSelectedIndex())));
				setProperty(TimestampBoxWrapper.this.timestamp);
			}

			
		});
		
		ChangeEvent.fireNativeEvent(Document.get().createChangeEvent(), hoursBox);
		ChangeEvent.fireNativeEvent(Document.get().createChangeEvent(), minutesBox);
		
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
				Date inputDate = TimestampBoxWrapper.this.dateBox.getValue();
				Date wrappedDate = new Date(TimestampBoxWrapper.this.timestamp.getTime());
				wrappedDate.setYear(inputDate.getYear());
				wrappedDate.setMonth(inputDate.getMonth());
				wrappedDate.setDate(inputDate.getDate());
				wrappedDate.setHours(0);
				wrappedDate.setMinutes(0);
				wrappedDate.setSeconds(0);
				TimestampBoxWrapper.this.timestamp.setTime(wrappedDate.getTime());
				setProperty(TimestampBoxWrapper.this.timestamp);
			}
		});
		dateBox.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				if(arg0.getValue().equals("")) {
					cleanDate();
				}
			}

		});
		initWidget(dateBox);
	}

	private void cleanDate() {
		// TODO Auto-generated method stub
		dateBox.setValue(null);
		setProperty(null);
	}
	
	@Override
	protected String getValueAsString() {
		return this.dateBox.getValue().toString();
	}

}
