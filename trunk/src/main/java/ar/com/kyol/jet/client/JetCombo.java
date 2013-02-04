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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.ListBox;
import com.gwtent.reflection.client.ClassType;
import com.gwtent.reflection.client.TypeOracle;

/**
 * A very cool ListBox wrapper.
 * 
 * @author smuzzopappa & fpugnali
 *
 * @param <E>
 */
public class JetCombo<E> extends Composite implements HasEnabled, HasChangeHandlers, Focusable {
	
	private List<E> list = new ArrayList<E>();
	private ListBox listBox = new ListBox();
	private String getter;
	@SuppressWarnings("rawtypes")
	private ClassType cType;
	private CreateNewHandler createNewHandler = null;
	private boolean cargando = false;
	private boolean isCreateNew = false;
	private String createNewDescription;
	private boolean hasSeleccioneItem = true;
	private boolean isEnabled = true;
	private Integer pendingSelectedIndex = null;
	private E pendingSelectedItem = null;
	
	private static String CREATE_NEW = "CREATE_NEW";
	
	/**
	 * A disabled JetCombo with a "Cargando" unique item. It will be enabled again when add or addAll is called.
	 * @param 
	 * @param descriptor - the property to display in the combo box
	 */
	public JetCombo(String descriptor, boolean hasSelectedItem) {
		this.hasSeleccioneItem=hasSelectedItem;
		this.cargando = true;
		initGetter(descriptor);
		listBox.addItem("Cargando...", "");
		listBox.setEnabled(false);
		
		registerChangeHandler();
		
		initWidget(listBox);
	}
	public JetCombo(String descriptor) {
		this(descriptor,true);
	}
	
	public int getItemCount(){
		return listBox.getItemCount();
	}
	
	public E getValue(int position){
		return list.get(position);
	}
	
	/**
	 * A JetCombo with a "Seleccione" first item and a list of objects to choose from.
	 * 
	 * @param list - the backup list, it will be internally copied
	 * @param descriptor - the property to display in the combo box
	 */
	public JetCombo(List<E> list, String descriptor, boolean hasSeleccioneItem) {
		this.list.addAll(list);
		this.hasSeleccioneItem = hasSeleccioneItem;
		if(hasSeleccioneItem){
			listBox.addItem("Seleccione", "");
		}
		initGetter(descriptor);
		if(list != null && !list.isEmpty()) {
			saveType(list.get(0));
			int i = 0;
			for (E e : list) {
				String description = (String)cType.invoke(e, getter, (Object[]) null);
				listBox.addItem(description, Integer.toString(i));
				i++;
			} 
		}
		
		registerChangeHandler();
		
		initWidget(listBox);
	}
	
	public JetCombo(List<E> list, String descriptor) {
		this(list,descriptor,true);
	}

	private void saveType(Object obj) {
		cType = TypeOracle.Instance.getClassType(obj.getClass()); //use first one to extract the class
	}

	private void initGetter(String descriptor) {
		if(descriptor == null || descriptor.equals("") || descriptor.equals("toString")) { //very ugly solution for not being able to access E class, this way we allow List<String> to be Jetcomboed (or anything toStringable)
			getter = "toString";
		} else {
			getter = "get"+descriptor.substring(0, 1).toUpperCase() + descriptor.substring(1, descriptor.length()); //TODO support depth with dot char
		}
	}
	
	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addDomHandler(handler, ChangeEvent.getType());
	}

	private void registerChangeHandler() {
		listBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent arg0) {
				String value = listBox.getValue(listBox.getSelectedIndex());
				if(CREATE_NEW.equals(value)) {
					JetCombo.this.createNewHandler.onCreateNew();
				}
			}
		});
	}
	
	/**
	 * Returns the selected item.
	 * 
	 * @return
	 */
	public E getSelectedItem() {
		String value = listBox.getValue(listBox.getSelectedIndex());
		E e = null;
		if(!value.equals("") && !value.equals(CREATE_NEW)) {
			e = list.get(Integer.parseInt(value));
		}
		return e;
	}
	
	public void setSelectedIndex(int index) {
		if(cargando) {
			pendingSelectedIndex = index;
		} else {
			listBox.setSelectedIndex(index);
		}
	}
	
	public void setSelectedItem(E item) {
		setSelectedItem(item, false);
	}
	
	public void setSelectedItem(E item, boolean fireEvent) {
		if(cargando) {
			pendingSelectedItem = item;
		} else {
			int index = list.indexOf(item);
			if(index > -1) {
				if(hasSeleccioneItem){
					index+=1;
				}
				listBox.setSelectedIndex(index);
				if(fireEvent) {
					DomEvent.fireNativeEvent(Document.get().createChangeEvent(), listBox);
				}
			}
		}
	}
	
	public boolean contains(E item){
		return list.contains(item);
	}
	/**
	 * Adds an item.
	 * 
	 * @param e
	 */
	public void add(E e) {
		checkLoading();
		addElement(e);
		checkCreateNew();
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}

	private void addElement(E e) {
		list.add(e);
		if(containsCreateNew()) {
			reconstructList();
		} else {
			addElementToListBox(e);
		}
	}

	private void addElementToListBox(E e) {
		if(cType == null) {
			saveType(e);
		}
		String description = (String)cType.invoke(e, getter, (Object[]) null);
		int i = listBox.getItemCount();
		if(hasSeleccioneItem) {
			i--;
		}
		listBox.addItem(description, Integer.toString(i));
	}
	
	private void reconstructList() {
		resetList();
		for (E e : list) {
			addElementToListBox(e);
		}
		checkCreateNew();
	}
	
	public void add(int index, E e) {
		checkLoading();
		list.add(index, e);
		reconstructList();
	}
	
	/**
	 * Adds each item from the list
	 * 
	 * @param list
	 */
	public void addAll(List<E> list) {
		checkLoading();
		for (E e : list) {
			addElement(e);
		}
		checkCreateNew();
	}
	
	public void set(List<E> list) {
		cargando = false;
		this.list.clear();
		this.list.addAll(list);
		reconstructList();
	}
	
	private void checkLoading() {
		if(cargando) {
			cargando = false;
			resetList();
		}
	}

	private void resetList() {
		listBox.clear();
		listBox.setEnabled(isEnabled);
		if(hasSeleccioneItem){
			listBox.addItem("Seleccione", "");
		}
	}
	
	

	private void checkCreateNew() {
		if(isCreateNew && !containsCreateNew()) {
			listBox.addItem(createNewDescription, CREATE_NEW);
		}
		if(pendingSelectedIndex != null) {
			setSelectedIndex(pendingSelectedIndex);
			pendingSelectedIndex = null;
		} else if(pendingSelectedItem != null) {
			setSelectedItem(pendingSelectedItem);
			pendingSelectedItem = null;
		}
	}
	
	/**
	 * Adds a last element that will call back the handler when selected.
	 * It is mainly used for creating new elements but could contain and do anything.
	 * 
	 * @param description - the displayed name for the element
	 * @param createNewHandler - the handler to be called when the element is selected
	 */
	public void addCreateNewElement(String description, CreateNewHandler createNewHandler) {
		if(!containsCreateNew()) {
			this.createNewHandler = createNewHandler;
			this.isCreateNew = true;
			this.createNewDescription = description;
			listBox.addItem(description, CREATE_NEW);
		}
	}
	
	private boolean containsCreateNew() {
		if(listBox.getItemCount() > 0) {
			String ultimo = listBox.getValue(listBox.getItemCount()-1);
			if(CREATE_NEW.equals(ultimo)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isEnabled(){
		return listBox.isEnabled();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
		listBox.setEnabled(enabled);
	}
	
	public void clean() {
		this.set(new ArrayList<E>());
	}
	
	public void setFocus(boolean focused) {
		this.listBox.setFocus(focused);
	}
	@Override
	public int getTabIndex() {
		return this.listBox.getTabIndex();
	}
	@Override
	public void setAccessKey(char key) {
		this.listBox.setAccessKey(key);
	}
	@Override
	public void setTabIndex(int index) {
		this.listBox.setTabIndex(index);
	}

}
