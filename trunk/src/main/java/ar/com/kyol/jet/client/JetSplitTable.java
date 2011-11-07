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

import ar.com.kyol.jet.client.wrappers.Wrapper;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtent.reflection.client.Reflection;

/**
 * A JetTable implementation that has a fixed header. It does not use the extended FlexTable but rather
 * use two FlexTables, one for the header and one for the content.
 *  
 * @author fpugnali
 *
 * @param <E>
 */
public class JetSplitTable<E extends Reflection> extends JetTable<E> {
	
	private SimplePanel contentPanel = new SimplePanel();
	private FlexTable encabezado = new FlexTable();
	private FlexTable contenido = new FlexTable();
	
	public JetSplitTable() {
		this.setCellSpacing(0);
		this.encabezado.addStyleName("JetTable");
		this.contenido.addStyleName("JetTable");
	}
	
	private void generarEncabezado(){
		this.encabezado.insertRow(HeaderRowIndex);
		this.encabezado.getRowFormatter().addStyleName(HeaderRowIndex, "JetTable-Header");
		addColumns();
		this.insertRow(HeaderRowIndex);
		this.setWidget(0, 0, encabezado);
	}

	@Override
	public void setValues(List<E> values) {
		setValues(values, true);
	}
	
	public void setValues(List<E> values, boolean withScroll) {
		setValues(values,withScroll,null);
	}
	public void setValues(List<E> values, boolean withScroll, String jetTableContentPanelClass) {
		encabezado.removeAllRows();
		contenido.removeAllRows();
		this.removeAllRows();
		rowIndex = getFirstRowNumber();
		this.values = new ArrayList<E>(); 
		generarEncabezado();
		addValues(values);
		this.insertRow(1);
		contentPanel.clear();
		contentPanel.add(contenido);
		if(jetTableContentPanelClass==null){
			contentPanel.setStyleName("JetTable-contentPanel");
		}else{
			contentPanel.setStyleName(jetTableContentPanelClass);
		}
		if(withScroll)
			contentPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
		
		this.setWidget(1,0, contentPanel);
	}
	
	public void addWithScroll() {
		contentPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
	}

	@Override
	public void setWidth(String width) {
		super.setWidth(width);
		contenido.setWidth(width);
		encabezado.setWidth(width);
	}
	
	@Override
	public List<Widget> getRow(int row) {
		List<Widget> rowContent = new ArrayList<Widget>(); 
		int columnCount = contenido.getCellCount(row);
		for(int column=0;column<columnCount;column++) {
			rowContent.add(((Wrapper)contenido.getWidget(row, column)).getWrappedWidget());
		}
		return rowContent;
	}

	public FlexTable getEncabezado() {
		return encabezado;
	}

	public FlexTable getContenido() {
		return contenido;
	}
	
	@Override
	public ColumnFormatter getColumnFormatter() {
		return new TableColumnFormatter();
	}
	
	private class TableColumnFormatter extends ColumnFormatter {
		@Override
		public void setStyleName(int column, String styleName) {
			encabezado.getColumnFormatter().setStyleName(column, styleName);
			contenido.getColumnFormatter().setStyleName(column, styleName);
		}
		@Override
		public void addStyleName(int col, String styleName) {
			encabezado.getColumnFormatter().addStyleName(col, styleName);
			contenido.getColumnFormatter().addStyleName(col, styleName);
		}
		@Override
		public void setWidth(int column, String width) {
			encabezado.getColumnFormatter().setWidth(column, width);
			contenido.getColumnFormatter().setWidth(column, width);
		}
		@Override
		public String getStylePrimaryName(int column) {
			return encabezado.getColumnFormatter().getStylePrimaryName(column);
		}
		@Override
		public void removeStyleName(int column, String styleName) {
			encabezado.getColumnFormatter().removeStyleName(column, styleName);
			contenido.getColumnFormatter().removeStyleName(column, styleName);
		}
	}

	@Override
	protected int getFirstRowNumber() {
		return 0;
	}

}
