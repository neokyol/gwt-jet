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
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.kyol.jet.client.wrappers.GenericWrapper;
import ar.com.kyol.jet.client.wrappers.Wrapper;
import ar.com.kyol.jet.client.wrappers.WrapperGenerator;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * A FlexTable that automatically wraps every field to auto-populate the values.
 * 
 * @author klarsk
 *
 * @param <E>
 */
public abstract class JetTable<E extends Reflection> extends FlexTable implements IsJetTable<E> {
	
	protected static final int HEADER_ROW_INDEX = 0;
	
	private static final Logger log = Logger.getLogger(""); //root logger
	
	static {
		log.setLevel(Level.SEVERE);
	}
	
	protected List<JetColumn<E>> jetColumns = new ArrayList<JetColumn<E>>();
	protected int rowIndex = 1;
	
	protected List<E> values;
	
	//public abstract void initJetTable();
	
	public List<Widget> getRow(int row){
		List<Widget> rowContent = new ArrayList<Widget>(); 
		int columnCount = this.getCellCount(row);
		for(int column=0;column<columnCount;column++) {
			rowContent.add(this.getWidget(row, column));
		}
		return rowContent;
	}
	
	//TODO what if they want to add without a previous set?
	public void addValues(Collection<E> values) {
		this.values.addAll(values);
		addRows(values);
		applyDataRowStyles();
	}
	
	public void addValue(E value) {
		this.values.add(value);
		addRow(value);
		applyDataRowStyles();
	}
	
	public void addValueButDoNotShowIt(E value) {
		this.values.add(value);
	}
	
	protected void addRows(Collection<? extends Object> objs) {

		//TODO optimize this, every row is equal and reflection is costly
		for (Object obj : objs) {
			int cell = 0;
			for (JetColumn<E> jetColumn : jetColumns) {
				Wrapper wrapper = createWrapperWidget(getProperty(obj, jetColumn.getColumnName()), jetColumn, cell, rowIndex);
				this.getContent().setWidget(rowIndex, cell, wrapper);
				this.getContent().getCellFormatter().addStyleName(rowIndex, cell,
						"JetTable-Cell");
				cell++;
			}
			rowIndex++;
		}
	}
	
	protected void addRow(Object obj) {
		int cell = 0;
		for (JetColumn<E> jetColumn : jetColumns) {
			Wrapper wrapper = createWrapperWidget(getProperty(obj, jetColumn.getColumnName()), jetColumn, cell, rowIndex);
			this.getContent().setWidget(rowIndex, cell, wrapper);
			this.getContent().getCellFormatter().addStyleName(rowIndex, cell,
					"JetTable-Cell");
			cell++;
		}
		rowIndex++;
	}
	
	public void addNewRow(List<Widget> widgets) {
		int cell = 0;
		for (Widget widget : widgets) {
			this.getContent().setWidget(rowIndex, cell, widget);
			this.getContent().getCellFormatter().addStyleName(rowIndex, cell,
				"JetTable-Cell");
			cell++;
		}
		applyDataRowStyles();
		rowIndex++;
	}
	
	protected abstract FlexTable getContent();
	protected abstract FlexTable getHeader();
	public abstract void setValues(Collection<E> values);
	protected abstract int getFirstRowNumber();
	
	public void refresh() {
		this.setValues(getValues());
	}
	
	public void setRowVisible(int rowNumber, boolean visible) {
		getContent().getRowFormatter().setVisible(rowNumber, false);
	}
	
	private void applyDataRowStyles() {
		HTMLTable.RowFormatter rf = this.getContent().getRowFormatter();

		for (int row = getFirstRowNumber(); row < this.getContent().getRowCount(); ++row) {
			if ((row % 2) != 0) {
				rf.addStyleName(row, "JetTable-OddRow");
			} else {
				rf.addStyleName(row, "JetTable-EvenRow");
			}
		}
	}
	
	protected ObjectSetter getProperty(Object obj, String prop) {
		return JetWrapper.getProperty(obj, prop);
	}
	
	protected Wrapper createWrapperWidget(final ObjectSetter objSetter, JetColumn<E> jetColumn, int column, int row) {
		if(objSetter!=null){
			if(objSetter.getReadOnlyCondition() == null) {
				objSetter.setReadOnlyCondition(jetColumn.getReadonly());
			} //else we respect the condition set on getProperty
		}
		Wrapper wrapper = jetColumn.getWrapper(objSetter);
		
		if(wrapper != null) {
			//continue
		} else if (objSetter.getValue() instanceof Widget) { //TODO this seems to have no sense at all, remove it
			wrapper = new GenericWrapper() { 
				
				@Override
				public Widget getGenericWidget() {
					return (Widget) objSetter.getValue();
				}
			};
		} else {
			wrapper = JetWrapper.createWrapper(objSetter);
		}

		JetWrapper.formatWrapper(objSetter, wrapper, jetColumn.getReadonly(), column, row-getFirstRowNumber());
		
		if(jetColumn.getContentStyle() != null && !jetColumn.getContentStyle().equals("")) {
			wrapper.addStyleName(jetColumn.getContentStyle());
		}
		
		if(jetColumn.getColumnWidth() != null) {
			wrapper.setWidth(jetColumn.getColumnWidth());
		}
		
		return wrapper;
	}
	
	
	
	public List<E> getValues() {
		return this.values;
	}
	
	protected void addColumns() {
		for (JetColumn<E> jetColumn : jetColumns) {
			addColumn(jetColumn.getTitle());
		}
	}
	
	protected void addColumn(Object columnHeading) {
		Widget widget = createCellWidget(columnHeading);
		int cell = this.getHeader().getCellCount(HEADER_ROW_INDEX);

		widget.setWidth("100%");
		widget.addStyleName("JetTable-ColumnLabel");

		this.getHeader().setWidget(HEADER_ROW_INDEX, cell, widget);

		this.getHeader().getCellFormatter().addStyleName(HEADER_ROW_INDEX, cell,
				"JetTable-ColumnLabelCell");
	}
	
	private Widget createCellWidget(Object cellObject) {
		Widget widget = null;

		if (cellObject instanceof Widget)
			widget = (Widget) cellObject;
		else
			widget = new Label(cellObject.toString());

		return widget;
	}
	
	public void addColumn(String columnName, String title) {
		addColumn(columnName, title, null, null, null, ReadOnlyCondition.NEVER);
	}
	
	public void addColumn(String columnName, String title, WrapperGenerator widget) {
		addColumn(columnName, title, null, widget, null, ReadOnlyCondition.NEVER);
	}
	
	public void addColumn(String columnName, Widget title, WrapperGenerator widget) {
		addColumn(columnName, title, null, widget, null, ReadOnlyCondition.NEVER);
	}
	
	public void addColumn(String columnName, Widget title, String contentStyle, WrapperGenerator widget) {
		addColumn(columnName, title, contentStyle, widget, null, ReadOnlyCondition.NEVER);
	}
	
	public void addColumn(String columnName, Widget title, Integer columnWidth, WrapperGenerator widget) {
		addColumn(columnName, title, null, widget, columnWidth, ReadOnlyCondition.NEVER);
	}
	
	public void addColumn(String columnName, String title, Integer columnWidth) {
		addColumn(columnName, title, null, null, columnWidth, ReadOnlyCondition.NEVER);
	}
	
	public void addColumn(String columnName, String title, Integer columnWidth, ReadOnlyCondition readonly) {
		addColumn(columnName, title, null, null, columnWidth, readonly);
	}
	
	public void addColumn(String columnName, String title, Integer columnWidth, WrapperGenerator widget) {
		addColumn(columnName, title, null, widget, columnWidth, ReadOnlyCondition.NEVER);
	}
	
	public void addColumn(String columnName, String title, Integer columnWidth, WrapperGenerator widget, ReadOnlyCondition readonly) {
		addColumn(columnName, title, null, widget, columnWidth, readonly);
	}
	
	public void addColumn(String columnName, String title, String contentStyle) {
		addColumn(columnName, title, contentStyle, null, null, ReadOnlyCondition.NEVER);
	}

	public void addColumn(String columnName, String title, String contentStyle,
			WrapperGenerator widget) {
		addColumn(columnName, title, contentStyle, widget, null, ReadOnlyCondition.NEVER);
	}
	
	public void addColumn(String columnName, String title, String contentStyle,
			Integer columnWidth, ReadOnlyCondition readonly) {
		addColumn(columnName, title, contentStyle, null, columnWidth, readonly);
	}
	
	public void addColumn(String columnName, String title, String contentStyle,
			WrapperGenerator widget, Integer columnWidth, ReadOnlyCondition readonly) {
		addColumn(columnName, new HTML(title), contentStyle, widget, columnWidth, readonly);
	}
	
	public void addColumn(String columnName, Widget title, String contentStyle,
			WrapperGenerator widget, Integer columnWidth, ReadOnlyCondition readonly) {
		JetColumn<E> jetCol = new JetColumn<E>(columnName, title, contentStyle, widget, columnWidth, readonly);
		jetCol.setJetTableParent(this);
		jetColumns.add(jetCol);
	}
	
	public Widget getCellContent(int row, int column) {
		Widget widget = getContent().getWidget(row, column);
		if(widget instanceof Wrapper) {
			return ((Wrapper)widget).getWrappedWidget();
		}
		return widget; 
	}
	
	public JetColumn<E> getColumn(int col) {
		return jetColumns.get(col);
	}
	
	public void setEnabledRow(int row, boolean enabled) {
		List<Widget> wRow = getRow(row);
		for (Widget widget : wRow) {
			if(widget instanceof HasEnabled) {
				((HasEnabled)widget).setEnabled(enabled);
			}
		}
	}
	
	@Override
	public JetTable<E> asJetTable() {
		return this;
	}
}
