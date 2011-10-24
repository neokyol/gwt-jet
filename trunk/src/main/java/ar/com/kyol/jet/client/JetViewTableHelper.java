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
	
	public Wrapper createWrapperWidget(ObjectSetter objSetter,
			@SuppressWarnings("rawtypes") JetColumn jetColumn, int column, int row) {
		if(objSetter!=null){
			objSetter.setReadOnlyCondition(ReadOnlyCondition.ALWAYS);
		}else{
			return new NullWrapper(true);
		}
		Wrapper wrapper = jetColumn.getWrapper(objSetter);
		
		HTML html = new HTML();
		if(jetColumn.getClickHandler() != null) {
			html.addClickHandler(jetColumn.getClickHandler());
		}
		
		if(wrapper != null) {
			//continue
		} else if (objSetter.isOfType(Date.class)) {
			wrapper = new HTMLWrapper(objSetter, html);
		} else if (objSetter.isOfType(java.sql.Date.class)) {
			wrapper = new HTMLWrapper(objSetter, html);
		} else if (objSetter.isOfType(Boolean.class)) {
			String value = "N";
			if(objSetter.getValue() != null && (Boolean)objSetter.getValue())
				value = "S";
			objSetter.setValue(value);
			wrapper = new HTMLWrapper(objSetter, html);
		} else if (objSetter.isOfType(String.class)) {
			wrapper = new HTMLWrapper(objSetter, html); 
		} else if (objSetter.isOfType(Integer.class)) {
			wrapper = new HTMLWrapper(objSetter, html); 
		} else if (objSetter.isOfType(Float.class)) {
			wrapper = new HTMLWrapper(objSetter, html);
		} else if (objSetter.getValue() == null) {
			//FIXME pasar a labelWrapper	wrapper = new LabelWrapper("");
			wrapper = new HTMLWrapper(objSetter, html); 
		} else
			//FIXME cambiar a labelWrapper
			wrapper = new HTMLWrapper(objSetter, html);
			//wrapper = new LabelWrapper(objSetter.getValue().toString());
		
		wrapper.setColumn(new Integer(column));
		wrapper.setRow(new Integer(row));
		
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