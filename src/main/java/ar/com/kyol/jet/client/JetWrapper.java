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
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.kyol.jet.client.wrappers.CheckBoxWrapper;
import ar.com.kyol.jet.client.wrappers.DateBoxWrapper;
import ar.com.kyol.jet.client.wrappers.DoubleBoxWrapper;
import ar.com.kyol.jet.client.wrappers.FloatBoxWrapper;
import ar.com.kyol.jet.client.wrappers.IntegerBoxWrapper;
import ar.com.kyol.jet.client.wrappers.SqlDateBoxWrapper;
import ar.com.kyol.jet.client.wrappers.TextBoxWrapper;
import ar.com.kyol.jet.client.wrappers.TimestampBoxWrapper;
import ar.com.kyol.jet.client.wrappers.Wrapper;
import ar.com.kyol.jet.client.wrappers.WrapperGenerator;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.NotFoundException;
import com.gwtent.reflection.client.ReflectionRequiredException;
import com.gwtent.reflection.client.TypeOracle;
import com.gwtent.reflection.client.impl.PrimitiveTypeImpl;

/**
 * The infamous Jet Wrapper
 *  
 * @author klarsk
 *
 */
public class JetWrapper {
	private static final Logger log = Logger.getLogger("");
	/**
	 * Creates a Wrapper for the provided object's property, automatically updating it when the associated Widget changes.
	 * 
	 * @param obj - the object containing the property to be wrapped
	 * @param property - the property to be wrapped (you could specify multiple levels separating properties with a dot. 
	 * You can add a format using the $ sign separator after the property name.
	 * For Integer/Long fields, use <i>-</i> to allow the minus sign in the text box.
	 * For Date fields, put the date format patter if you don't want the default format.
	 * For Float/Double fields, the all the possibles number formats are allowed.
	 * @param readonly - the readonly condition
	 * @return
	 */
	public static Wrapper createWrapper(Object obj, String property, ReadOnlyCondition readonly) {
		return createWrapperWidget(getProperty(obj, property), readonly, null);
	}
	
	/**
	 * Creates a Wrapper for the provided object's property, automatically updating it when the associated Widget changes.
	 * 
	 * @param obj - the object containing the property to be wrapped
	 * @param property - the property to be wrapped (you could specify multiple levels separating properties with a dot
	 * @param readonly - the readonly condition
	 * @param wrapperGenerator - the WrapperGenerator to create the widget
	 * @return
	 */
	public static Wrapper createWrapper(Object obj, String property, ReadOnlyCondition readonly, WrapperGenerator wrapperGenerator) {
		return createWrapperWidget(getProperty(obj, property), readonly, wrapperGenerator);
	}
	
	public static ObjectSetter getProperty(Object obj, String prop) {
		if(prop == null || prop.equals("")) {
			return null;
		}
		ObjectSetter objSetter = new ObjectSetter(null, "", obj, null);
		
		String format = null;
		String propNF; //prop with no format tag
		
		//extract format (first split after $, ignore the rest)
		if(prop.contains("$")) {
			String[] formatSplit = prop.split("\\$");
			format = formatSplit[1];
			propNF = formatSplit[0];
		} else {
			propNF = prop;
		}
		
		String[] split;
		if(propNF.contains(".")){
			split = propNF.split("\\.");
		}else{
			split = new String[]{propNF};
		}
		
		String getter = "getNULL";
		for (int i = 0; i < split.length; i++) {
			objSetter = new ObjectSetter(objSetter.getValue(), "", null, null);
			getter = "get" + split[i].substring(0, 1).toUpperCase() + split[i].substring(1, split[i].length());
//			log.severe("obj para extraer clase :"+objSetter.getObj());
			if(objSetter.getObj() == null) {
				throw new NullPointerException("La propiedad "+prop+" resuelve a null"); 
			}
			@SuppressWarnings("rawtypes")
			ClassType cType = TypeOracle.Instance.getClassType(objSetter.getObj().getClass());
			
			try {
				objSetter.setValue(cType.invoke(objSetter.getObj(), getter, (Object[]) null));
				if(i == split.length-1) {
					try {
						objSetter.setType(cType.findMethod(getter, (String[]) null).getReturnType());
						objSetter.setSetter("s"+getter.substring(1));
						objSetter.setGetter(getter);
					} catch (ReflectionRequiredException r) {
						log.log(Level.SEVERE, "UNKNOWN TYPE. TRYING TO JETWRAP A BOUNDED TYPE PARAMETER (<T extends SomeClass>)?");
						throw r;
					}
				}
			} catch(NotFoundException e) {
				objSetter.setReadOnlyCondition(ReadOnlyCondition.ALWAYS); //for both cases, force READ ONLY
				try {
					objSetter.setValue(cType.invoke(objSetter.getObj(), split[i], (Object[]) null)); //if getter not found, try directly for a method
	
					if(i == split.length-1) {
						try {
							objSetter.setType(cType.findMethod(split[i], (String[]) null).getReturnType());
						} catch (ReflectionRequiredException r) {
							log.log(Level.SEVERE, "UNKNOWN TYPE. TRYING TO JETWRAP A BOUNDED TYPE PARAMETER (<T extends SomeClass>)?");
							throw r;
						}
					}

				} catch(NotFoundException f) {
					objSetter.setValue(cType.getFieldValue(objSetter.getObj(), split[i])); //if not, try with the property
					if(i == split.length-1) {
						try {
							objSetter.setType(cType.findField(split[i]).getType());
						} catch (ReflectionRequiredException r) {
							log.log(Level.SEVERE, "UNKNOWN TYPE. TRYING TO JETWRAP A BOUNDED TYPE PARAMETER (<T extends SomeClass>)?");
							throw r;
						}
					}
				}
			}
			
		}
		objSetter.setFormat(format);
		return objSetter;
	}
	
	private static Wrapper createWrapperWidget(final ObjectSetter objSetter, ReadOnlyCondition readonly, WrapperGenerator wrapperGenerator) {
		objSetter.setReadOnlyCondition(readonly);
		Wrapper wrapper = null;
		
		if(wrapperGenerator != null) {
			wrapper = wrapperGenerator.generateWrapper(objSetter);
		} else {
			wrapper = createWrapper(objSetter);
		}
		
		formatWrapper(objSetter, wrapper, readonly);
		
		return wrapper;
	}
	
	/**
	 * Creates a Wrapper based on the type of objSetter, binding objSetter to that wrapper.
	 * 
	 * @param objSetter
	 * @return
	 */
	protected static Wrapper createWrapper(final ObjectSetter objSetter) {
		Wrapper wrapper;
		
		if (objSetter.isOfType(String.class)) {
			wrapper = new TextBoxWrapper(objSetter);
		} else if (objSetter.isOfType(Date.class)) {
			wrapper = new DateBoxWrapper((Date) objSetter.getValue(), objSetter);
		} else if (objSetter.isOfType(java.sql.Date.class)) {
			wrapper = new SqlDateBoxWrapper((java.sql.Date) objSetter.getValue(), objSetter);
		} else if (objSetter.isOfType(java.sql.Timestamp.class)) {
			wrapper = new TimestampBoxWrapper(objSetter);
		} else if (objSetter.isOfType(Boolean.class) || objSetter.isOfType(PrimitiveTypeImpl.BOOLEAN.getQualifiedSourceName())) {
			wrapper = new CheckBoxWrapper(objSetter);
		} else if (objSetter.isOfType(Integer.class) || objSetter.isOfType(PrimitiveTypeImpl.INT.getQualifiedSourceName())) {
			wrapper = new IntegerBoxWrapper(objSetter);
		} else if (objSetter.isOfType(Float.class) || objSetter.isOfType(PrimitiveTypeImpl.FLOAT.getQualifiedSourceName())) {
			wrapper = new FloatBoxWrapper(objSetter, 2, false);
		} else if (objSetter.isOfType(Long.class) || objSetter.isOfType(PrimitiveTypeImpl.LONG.getQualifiedSourceName())) {
			wrapper = new IntegerBoxWrapper(objSetter);
		} else if (objSetter.isOfType(Double.class) || objSetter.isOfType(PrimitiveTypeImpl.DOUBLE.getQualifiedSourceName())) {
			wrapper = new DoubleBoxWrapper(objSetter, 2, false);
		} else if (objSetter.getValue() == null) {
			wrapper = new TextBoxWrapper(objSetter);
		} else {
			wrapper = new TextBoxWrapper(objSetter);
		}
		
		return wrapper;
	}
	
	/**
	 * Sets default column and row, and enable/disable according to readonly condition.
	 * @param wrapper
	 * @param readonly
	 */
	protected static void formatWrapper(ObjectSetter objSetter, Wrapper wrapper, ReadOnlyCondition readonly) {
		formatWrapper(objSetter, wrapper, readonly, 0, 0);
	}
	
	/**
	 * Sets column, row and enable/disable according to readonly condition.
	 * 
	 * @param wrapper
	 * @param readonly
	 * @param column
	 * @param row
	 */
	protected static void formatWrapper(ObjectSetter objSetter, Wrapper wrapper, ReadOnlyCondition readonly, int column, int row) {
		wrapper.setColumn(new Integer(column));
		wrapper.setRow(new Integer(row));
		
		wrapper.initWrapper(objSetter);

		recursiveEnabler(wrapper.getWrappedWidget(), readonly);
	}
	
	private static void recursiveEnabler(Widget widget, ReadOnlyCondition readonly) {
		if(widget instanceof HasEnabled) {
			((HasEnabled)widget).setEnabled(!readonly.isReadOnly(widget));
		} else if(widget instanceof ComplexPanel) {
			for (int i = 0; i < ((ComplexPanel)widget).getWidgetCount(); i++) {
				recursiveEnabler(((ComplexPanel)widget).getWidget(i), readonly);
			}
		} else if(widget instanceof ValueBoxBase<?>) { //why not using HasEnabled instead?
			ValueBoxBase<?> wrappedWidget = (ValueBoxBase<?>)widget;
			wrappedWidget.setReadOnly(readonly.isReadOnly(widget));
		} else if(widget instanceof DateBox) { //DateBox does not implement HasEnabled in GWT 2.2
			DateBox wrappedWidget = (DateBox)widget;
			wrappedWidget.setEnabled(!readonly.isReadOnly(widget));
		}
	}

}
