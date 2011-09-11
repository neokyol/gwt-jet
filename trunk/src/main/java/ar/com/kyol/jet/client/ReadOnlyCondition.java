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

public enum ReadOnlyCondition {
	
ALWAYS, NEVER, WHEN_EMPTY, WHEN_NOT_EMPTY;
	
	/**
	 * Analiza el tipo de objeto y su valor para saber si debe ser tratado como readonly o no.
	 * 
	 * @param value - el objeto afectado por esta ReadOnlyCondition
	 * @return true si el objeto debe ser tratado como readonly
	 */
	public boolean isReadOnly(Object value) {
		boolean isReadOnly = false;
		if(this.equals(ReadOnlyCondition.ALWAYS)){
			isReadOnly = true;
		} else if(this.equals(ReadOnlyCondition.NEVER)){
			isReadOnly = false;
		} else if(this.equals(ReadOnlyCondition.WHEN_EMPTY)) {
			if(value instanceof String){
				isReadOnly = (value == null || value.equals(""));
			}else{
				isReadOnly = (value == null);
			}
		} else if(this.equals(ReadOnlyCondition.WHEN_NOT_EMPTY)){
			if(value instanceof String){
				isReadOnly = (value != null && !value.equals(""));
			}else{
				isReadOnly = value != null;
			}
		}
		return isReadOnly;
	}
}
