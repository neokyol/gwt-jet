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

import com.google.gwt.user.client.ui.FlexTable;
import com.gwtent.reflection.client.Reflection;

/**
 * A single implementation of a JetTable
 * 
 * @author fpugnali
 *
 * @param <E>
 */
public class JetSingleTable<E extends Reflection> extends JetTable<E> {
	
	public JetSingleTable() {
		this.setCellSpacing(0);
		this.addStyleName("JetTable");
	}
	
	private void generarEncabezado(){
		this.insertRow(HeaderRowIndex);
		this.getRowFormatter().addStyleName(HeaderRowIndex, "JetTable-Header");
		addColumns();
	}

	@Override
	public void setValues(List<E> values) {
		this.removeAllRows();
		rowIndex = getFirstRowNumber();
		this.values = new ArrayList<E>(); 
		generarEncabezado();
		addValues(values);
	}
	
	@Override
	protected FlexTable getContenido() {
		return this;
	}
	
	@Override
	protected FlexTable getEncabezado() {
		return this;
	}

	@Override
	protected int getFirstRowNumber() {
		return 1;
	}

}
