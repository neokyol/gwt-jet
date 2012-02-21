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

import ar.com.kyol.jet.client.ObjectSetter;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HTML;

public class HTMLWrapper extends Wrapper {
	
	private HTML html;
	
	/**
	 * Instantiates a new hTML wrapper.
	 *
	 * @param objSetter the obj setter
	 */
	public HTMLWrapper(ObjectSetter objSetter) {
		this(objSetter, new HTML());
	}
	
	/**
	 * Instantiates a new hTML wrapper.
	 *
	 * @param objSetter the obj setter
	 * @param html the html
	 */
	public HTMLWrapper(ObjectSetter objSetter, HTML html) {
		super(false);
		this.objSetter = objSetter;
		this.html = html;
		if(objSetter.getValue() == null) {
			html.setText("");
		} else {
			NumberFormat.setForcedLatinDigits(true);
			if (objSetter.getValue() instanceof Float){
				NumberFormat nf = NumberFormat.getFormat(getFormat("0.00"));
				Float valor = (Float)objSetter.getValue();
				html.setText(nf.format(valor));
			}else if(objSetter.getValue() instanceof Long){
				NumberFormat nf = NumberFormat.getFormat(getFormat("0"));
				Long valor = (Long)objSetter.getValue();
				html.setText(nf.format(valor));
			}else if(objSetter.getValue() instanceof Integer){
				NumberFormat nf = NumberFormat.getFormat(getFormat("0"));
				Integer valor = (Integer)objSetter.getValue();
				html.setText(nf.format(valor));
			}else if(objSetter.getValue() instanceof java.util.Date){
				java.util.Date utilDate = (java.util.Date)objSetter.getValue();
				html.setText(DateTimeFormat.getFormat(getFormat("dd/MM/yyyy")).format(utilDate));
			} else if(objSetter.getValue() instanceof java.sql.Date){
				java.sql.Date sqlDate = (java.sql.Date)objSetter.getValue();
				html.setText(DateTimeFormat.getFormat(getFormat("dd/MM/yyyy")).format(sqlDate));
			} else if(objSetter.getValue() instanceof Timestamp){
				Timestamp ts = (Timestamp)objSetter.getValue();
				html.setText(DateTimeFormat.getFormat(getFormat("dd/MM/yyyy")).format(ts));
			} else {
				html.setText(objSetter.getValue().toString());
			}
		}
		
		initWidget(html);
	}
	
	private String getFormat(String defaultFormat) { //TODO try to unify a default format somewhere
		String format = defaultFormat;
		if(objSetter.getFormat() != null) {
			format = objSetter.getFormat(); 
		}
		return format;
	}
	
	public HTML getHTML() {
		return this.html;
	}

	@Override
	protected String getValueAsString() {
		return null;
	}

}
