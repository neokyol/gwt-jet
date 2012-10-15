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

import com.gwtent.reflection.client.Type;

/**
 * It contains all the information to wrap a property.
 * 
 * @author klarsk
 *
 */
public class ObjectSetter {
	
	private Object obj;
	private String getter;
	private String setter;
	private Object value;
	private Type type;
	private String format;
	private ReadOnlyCondition readOnlyCondition;
	
	public ReadOnlyCondition getReadOnlyCondition() {
		return readOnlyCondition;
	}

	public void setReadOnlyCondition(ReadOnlyCondition readOnlyCondition) {
		this.readOnlyCondition = readOnlyCondition;
	}

	public ObjectSetter(Object obj, String setter, Object value, Type type) {
		this.obj = obj;
		this.setter = setter;
		this.value = value;
		this.type = type;
	}

	/**
	 * The object containing the property
	 * 
	 * @return
	 */
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	/**
	 * The setter method to alter the value of the property
	 * 
	 * @return
	 */
	public String getSetter() {
		return setter;
	}

	public void setSetter(String setter) {
		this.setter = setter;
	}

	/**
	 * The property itself
	 * 
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * The type of the property
	 * 
	 * @return
	 */
	public Type getType() {
		return type;
	}
	
	public boolean isOfType(@SuppressWarnings("rawtypes") Class clase) {
		return isOfType(clase.getName());
	}
	
	public boolean isOfType(String type) {
		return getType().getQualifiedSourceName().equals(type);
	}
	
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * The optional format to display the property
	 * 
	 * @return
	 */
	public String getFormat() {
		return format;
	}

	public String getGetter() {
		return getter;
	}

	public void setGetter(String getter) {
		this.getter = getter;
	}

}
