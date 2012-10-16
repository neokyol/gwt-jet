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
import ar.com.kyol.jet.client.wrappers.LongBoxWrapper;
import ar.com.kyol.jet.client.wrappers.SqlDateBoxWrapper;
import ar.com.kyol.jet.client.wrappers.TextBoxWrapper;
import ar.com.kyol.jet.client.wrappers.TimestampBoxWrapper;
import ar.com.kyol.jet.client.wrappers.Wrapper;
import ar.com.kyol.jet.client.wrappers.WrapperGenerator;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.datepicker.client.DateBox;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.NotFoundException;
import com.gwtent.reflection.client.ReflectionRequiredException;
import com.gwtent.reflection.client.TypeOracle;

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
	 * @param property - the property to be wrapped (you could specify multiple levels separating properties with a dot
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
		} else if (objSetter.isOfType(String.class)) {
			wrapper = new TextBoxWrapper(objSetter);
		} else if (objSetter.isOfType(Date.class)) {
			wrapper = new DateBoxWrapper((Date) objSetter.getValue(), objSetter);
		} else if (objSetter.isOfType(java.sql.Date.class)) {
			wrapper = new SqlDateBoxWrapper((java.sql.Date) objSetter.getValue(), objSetter);
		} else if (objSetter.isOfType(java.sql.Timestamp.class)) {
			wrapper = new TimestampBoxWrapper(objSetter);
		} else if (objSetter.isOfType(Boolean.class) || objSetter.isOfType("boolean")) {
			wrapper = new CheckBoxWrapper(objSetter);
		} else if (objSetter.isOfType(Integer.class) || objSetter.isOfType("int")) {
			wrapper = new IntegerBoxWrapper(objSetter);
		} else if (objSetter.isOfType(Float.class)  || objSetter.isOfType("float")) {
			wrapper = new FloatBoxWrapper(objSetter, 2, false);
		} else if (objSetter.isOfType(Long.class)  || objSetter.isOfType("long")) {
			wrapper = new LongBoxWrapper(objSetter);
		} else if (objSetter.isOfType(Double.class)  || objSetter.isOfType("double")) {
			wrapper = new DoubleBoxWrapper(objSetter, 2, false);
		} else if (objSetter.getValue() == null) {
			wrapper = new TextBoxWrapper(objSetter);
		} else
			wrapper = new TextBoxWrapper(objSetter);
		
		wrapper.setColumn(0); //backward compatibility
		wrapper.setRow(0);
		
		wrapper.initWrapper(objSetter);

		if(wrapper.getWrappedWidget() instanceof ValueBoxBase<?>) {
			ValueBoxBase<?> wrappedWidget = (ValueBoxBase<?>)wrapper.getWrappedWidget();
			wrappedWidget.setReadOnly(readonly.isReadOnly(wrappedWidget));
		}
		
		if(wrapper.getWrappedWidget() instanceof CheckBox) {
			CheckBox wrappedWidget = (CheckBox)wrapper.getWrappedWidget();
			wrappedWidget.setEnabled(!readonly.isReadOnly(wrappedWidget));
		}
		
		if(wrapper.getWrappedWidget() instanceof DateBox) {
			DateBox wrappedWidget = (DateBox)wrapper.getWrappedWidget();
			wrappedWidget.setEnabled(!readonly.isReadOnly(wrappedWidget));
		}
		
		if(wrapper.getWrappedWidget() instanceof ListBox){
			ListBox wrappedWidget = (ListBox)wrapper.getWrappedWidget();
			wrappedWidget.setEnabled(!readonly.isReadOnly(wrappedWidget.getValue(wrappedWidget.getSelectedIndex())));
		}
		
		return wrapper;
	}

}
