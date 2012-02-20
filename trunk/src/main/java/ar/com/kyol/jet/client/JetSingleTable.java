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
import com.google.gwt.user.client.ui.Widget;
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
	protected FlexTable getContent() {
		return this;
	}
	
	@Override
	protected FlexTable getHeader() {
		return this;
	}

	@Override
	protected int getFirstRowNumber() {
		return 1;
	}
	
	@Override
	public Widget getCellContent(int row, int column) {
		return super.getCellContent(row+1, column);
	}
	
	@Override
	public void setEnabledRow(int row, boolean enabled) {
		super.setEnabledRow(row+1, enabled);
	}
	
	@Override
	public List<Widget> getRow(int row) {
		return super.getRow(row+1);
	}
	
	@Override
	public void setRowVisible(int rowNumber, boolean visible) {
		super.setRowVisible(rowNumber+1, visible);
	}
}
