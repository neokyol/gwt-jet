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

import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.kyol.jet.client.ObjectSetter;
import ar.com.kyol.jet.client.ReadOnlyCondition;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.TypeOracle;

/**
 * The property wrapper for JetTables
 * 
 * @author klarsk
 *
 */
public abstract class Wrapper extends Composite {
	private Integer row;
	private Integer column;
	protected ObjectSetter objSetter;
	private boolean useValueAsString;
	protected static final Logger log = Logger.getLogger(Wrapper.class.toString()); //TODO check this logger name
	
	public Wrapper(boolean useValueAsString) {
		this.useValueAsString = useValueAsString;
	}
	
	public Widget getWrappedWidget() {
		return this.getWidget();
	}
	
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public Integer getColumn() {
		return column;
	}
	public void setColumn(Integer column) {
		this.column = column;
	} 
	public void setObjSetter(ObjectSetter objSetter) {
		this.objSetter = objSetter;
	}
	public ObjectSetter getObjSetter() {
		return objSetter;
	}
	
	public void setProperty(Object obj) {
		if(useValueAsString) {
			obj = getValueAsString();
		}
		if(objSetter!=null && objSetter.getReadOnlyCondition()!=null){
			if(objSetter.getReadOnlyCondition().equals(ReadOnlyCondition.ALWAYS) || 
					(objSetter.getReadOnlyCondition().equals(ReadOnlyCondition.WHEN_EMPTY) && objSetter.getValue() == null) ||
					(objSetter.getReadOnlyCondition().equals(ReadOnlyCondition.WHEN_NOT_EMPTY) && objSetter.getValue() != null)){
			}else {
				@SuppressWarnings("rawtypes")
				ClassType cType = TypeOracle.Instance.getClassType(objSetter.getObj().getClass());
				cType.invoke(objSetter.getObj(), objSetter.getSetter(), new Object[]{obj});
			}
		}
	}
	
	public void initWrapper(ObjectSetter objSetter) {
		this.objSetter = objSetter;
	}
	
	/**
	 * Formats a float value with the associated objSetter.getFormat
	 * 
	 * @param value
	 * @return value formatted or not, and "formatError!" if value can't be parsed to float 
	 */
	protected String formatValue(String value) {
		String returnValue = value;
		if(objSetter.getFormat() != null) {
			try {
				NumberFormat formatter = NumberFormat.getFormat(objSetter.getFormat());
				returnValue = formatter.format(Float.parseFloat(value));
			} catch(Exception e) {
				log.log(Level.SEVERE, "formatter error in setter "+objSetter.getSetter()+" with format "+objSetter.getFormat());
				returnValue = "formatError!";
			}
		}
		return returnValue;
	}
	
	protected abstract String getValueAsString();

}
