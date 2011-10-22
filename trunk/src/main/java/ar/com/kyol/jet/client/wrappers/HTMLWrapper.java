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
				String format = "0.00";
				if(objSetter.getFormat() != null) {
					format = objSetter.getFormat(); //TODO unify into wrapper method with default format optional prop
				}
				NumberFormat nf = NumberFormat.getFormat(format);
				Float valor = (Float)objSetter.getValue();
				html.setText(nf.format(valor));
			}else if(objSetter.getValue() instanceof java.util.Date){
				java.util.Date utilDate = (java.util.Date)objSetter.getValue();
				html.setText(DateTimeFormat.getFormat("dd/MM/yyyy").format(utilDate));
			} else if(objSetter.getValue() instanceof java.sql.Date){
				java.sql.Date sqlDate = (java.sql.Date)objSetter.getValue();
				html.setText(DateTimeFormat.getFormat("dd/MM/yyyy").format(sqlDate));
			} else if(objSetter.getValue() instanceof Timestamp){
				Timestamp ts = (Timestamp)objSetter.getValue();
				html.setText(DateTimeFormat.getFormat("dd/MM/yyyy").format(ts));
			} else {
				html.setText(objSetter.getValue().toString());
			}
		}
		
		initWidget(html);
	}
	
	public HTML getHTML() {
		return this.html;
	}

	@Override
	protected String getValueAsString() {
		return null;
	}

}
