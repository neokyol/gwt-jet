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
package ar.com.kyol.jet.client;

import java.util.Date;

import ar.com.kyol.jet.client.wrappers.HTMLWrapper;
import ar.com.kyol.jet.client.wrappers.NullWrapper;
import ar.com.kyol.jet.client.wrappers.Wrapper;

import com.google.gwt.user.client.ui.HTML;

public class JetViewTableHelper {

	@SuppressWarnings("rawtypes")
	public Wrapper createWrapperWidget(ObjectSetter objSetter,
			 JetColumn jetColumn, int column, int row, int rowoffset) {
		if(objSetter!=null){
			objSetter.setReadOnlyCondition(ReadOnlyCondition.ALWAYS);
		}
		
		Wrapper wrapper = jetColumn.getWrapper(objSetter);
		
		if(objSetter == null && wrapper == null) { //for jetColumns trying to show inner attributes from null objects
			return new NullWrapper(true);
		}
		
		HTML html = new HTML();
		if(jetColumn.getClickHandler() != null) {
			html.addClickHandler(jetColumn.getClickHandler());
		}
		
		//TODO change everything labelWrapper	wrapper = new LabelWrapper("");
		if(wrapper != null) {
			//continue
		} else if (objSetter.isOfType(Date.class)) {
			wrapper = new HTMLWrapper(objSetter, html);
		} else if (objSetter.isOfType(java.sql.Date.class)) {
			wrapper = new HTMLWrapper(objSetter, html);
		} else if (objSetter.isOfType(Boolean.class)) {
			String value = Jet.constants.n();
			if(objSetter.getValue() != null && (Boolean)objSetter.getValue())
				value = Jet.constants.y();
			objSetter.setValue(value);
			wrapper = new HTMLWrapper(objSetter, html);
		} else if (objSetter.isOfType(String.class)) {
			wrapper = new HTMLWrapper(objSetter, html); 
		} else if (objSetter.isOfType(Integer.class)) {
			wrapper = new HTMLWrapper(objSetter, html); 
		} else if (objSetter.isOfType(Float.class)) {
			wrapper = new HTMLWrapper(objSetter, html);
		} else if (objSetter.getValue() == null) {
			wrapper = new HTMLWrapper(objSetter, html); 
		} else
			wrapper = new HTMLWrapper(objSetter, html);
		
		wrapper.setColumn(new Integer(column));
		wrapper.setRow(new Integer(row-rowoffset));
		
		wrapper.initWrapper(objSetter);

		if(jetColumn.getContentStyle() != null && !jetColumn.getContentStyle().equals("")) {
			wrapper.addStyleName(jetColumn.getContentStyle());
		}
		
		if(jetColumn.getColumnWidth() != null) {
			wrapper.setWidth(jetColumn.getColumnWidth());
		}
		
		return wrapper;
	}

}
