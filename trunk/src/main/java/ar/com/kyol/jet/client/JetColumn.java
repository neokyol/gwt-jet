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

import ar.com.kyol.jet.client.wrappers.Wrapper;
import ar.com.kyol.jet.client.wrappers.WrapperGenerator;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import ar.com.kyol.jet.client.Reflection;

public class JetColumn<E extends Reflection> {
	
	private String columnName;
	private Widget title;
	private String contentStyle;
	private WrapperGenerator widgetWrapper;
	private JetTable<E> jetTableParent;
	private Integer columnWidth;
	private ReadOnlyCondition readonly;
	private ClickHandler clickHandler;
	
	/**
	 * Instantiates a new jet column.
	 *
	 * @param columnName the column name
	 * @param title the title
	 */
	public JetColumn(String columnName, String title) {
		this(columnName, title, null, null, null, ReadOnlyCondition.NEVER);
	}
	
	/**
	 * Instantiates a new jet column.
	 *
	 * @param columnName the column name
	 * @param title the title
	 * @param widget the widget
	 */
	public JetColumn(String columnName, String title, WrapperGenerator widget) {
		this(columnName, title, null, widget, null, ReadOnlyCondition.NEVER);
	}
	
	/**
	 * Instantiates a new jet column.
	 *
	 * @param columnName the column name
	 * @param title the title
	 * @param columnWidth the column width
	 */
	public JetColumn(String columnName, String title, Integer columnWidth) {
		this(columnName, title, null, null, columnWidth, ReadOnlyCondition.NEVER);
	}
	
	/**
	 * Instantiates a new jet column.
	 *
	 * @param columnName the column name
	 * @param title the title
	 * @param columnWidth the column width
	 * @param readonly the readonly
	 */
	public JetColumn(String columnName, String title, Integer columnWidth, ReadOnlyCondition readonly) {
		this(columnName, title, null, null, columnWidth, readonly);
	}
	
	/**
	 * Instantiates a new jet column.
	 *
	 * @param columnName the column name
	 * @param title the title
	 * @param columnWidth the column width
	 * @param widget the widget
	 */
	public JetColumn(String columnName, String title, Integer columnWidth, WrapperGenerator widget) {
		this(columnName, title, null, widget, columnWidth, ReadOnlyCondition.NEVER);
	}
	
	/**
	 * Instantiates a new jet column.
	 *
	 * @param columnName the column name
	 * @param title the title
	 * @param columnWidth the column width
	 * @param widget the widget
	 * @param readonly the readonly
	 */
	public JetColumn(String columnName, String title, Integer columnWidth, WrapperGenerator widget, ReadOnlyCondition readonly) {
		this(columnName, title, null, widget, columnWidth, readonly);
	}
	
	/**
	 * Instantiates a new jet column.
	 *
	 * @param columnName the column name
	 * @param title the title
	 * @param contentStyle the content style
	 */
	public JetColumn(String columnName, String title, String contentStyle) {
		this(columnName, title, contentStyle, null);
	}

	/**
	 * Instantiates a new jet column.
	 *
	 * @param columnName the column name
	 * @param title the title
	 * @param contentStyle the content style
	 * @param widget the widget
	 */
	public JetColumn(String columnName, String title, String contentStyle,
			WrapperGenerator widget) {
		this(columnName, title, contentStyle, widget, null, ReadOnlyCondition.NEVER);
	}
	
	/**
	 * Instantiates a new jet column.
	 *
	 * @param columnName the column name
	 * @param title the title
	 * @param contentStyle the content style
	 * @param widget the widget
	 * @param columnWidth the column width
	 * @param readonly the readonly
	 */
	public JetColumn(String columnName, String title, String contentStyle,
			WrapperGenerator widget, Integer columnWidth, ReadOnlyCondition readonly) {
		this(columnName, new HTML(title), contentStyle, widget, columnWidth, readonly);
	}
	
	/**
	 * Instantiates a new jet column.
	 *
	 * @param columnName the column name
	 * @param title the title
	 * @param contentStyle the content style
	 * @param widget the widget
	 * @param columnWidth the column width
	 * @param readonly the readonly
	 */
	public JetColumn(String columnName, Widget title, String contentStyle,
			WrapperGenerator widget, Integer columnWidth, ReadOnlyCondition readonly) {
		super();
		this.columnName = columnName;
		this.title = title;
		this.setContentStyle(contentStyle);
		this.widgetWrapper = widget;
		this.columnWidth = columnWidth;
		this.readonly = readonly;
	}

	/**
	 * Gets the column name.
	 *
	 * @return the column name
	 */
	public String getColumnName() {
		return columnName;
	}
	
	/**
	 * Sets the column name.
	 *
	 * @param columnName the new column name
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public Widget getTitle() {
		return title;
	}
	
	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(Widget title) {
		this.title = title;
	}

	/**
	 * Sets the content style.
	 *
	 * @param contentStyle the new content style
	 */
	public void setContentStyle(String contentStyle) {
		this.contentStyle = contentStyle;
	}

	/**
	 * Gets the content style.
	 *
	 * @return the content style
	 */
	public String getContentStyle() {
		return contentStyle;
	}

	/**
	 * Sets the widget wrapper.
	 *
	 * @param widgetWrapper the new widget wrapper
	 */
	public void setWidgetWrapper(WrapperGenerator widgetWrapper) {
		this.widgetWrapper = widgetWrapper;
	}

	/**
	 * Gets the wrapper.
	 *
	 * @param objSetter the obj setter
	 * @return the wrapper
	 */
	public Wrapper getWrapper(ObjectSetter objSetter) {
		if(widgetWrapper != null) {
			return widgetWrapper.generateWrapper(objSetter);
		}
		return null;
	}

	/**
	 * Sets the jet table parent.
	 *
	 * @param jetTableParent the new jet table parent
	 */
	public void setJetTableParent(JetTable<E> jetTableParent) {
		this.jetTableParent = jetTableParent;
	}

	/**
	 * Gets the jet table parent.
	 *
	 * @return the jet table parent
	 */
	public JetTable<E> getJetTableParent() {
		return jetTableParent;
	}

	/**
	 * Sets the column width.
	 *
	 * @param columnWidth the new column width
	 */
	public void setColumnWidth(Integer columnWidth) {
		this.columnWidth = columnWidth;
	}

	/**
	 * Gets the column width.
	 *
	 * @return the column width
	 */
	public String getColumnWidth() {
		if(columnWidth == null) return null;
		return columnWidth+"px";
	}

	/**
	 * Gets the readonly.
	 *
	 * @return the readonly
	 */
	public ReadOnlyCondition getReadonly() {
		return readonly;
	}
	
	/**
	 * Checks if it's read only.
	 *
	 * @param obj the obj
	 * @return true, if it's read only
	 */
	public boolean isReadOnly(ValueBoxBase<?> obj){
		Object value = obj.getValue();
		boolean isReadOnly = isReadOnly(value);
		
		return isReadOnly;
	}

	/**
	 * Checks if it's read only.
	 *
	 * @param obj the obj
	 * @return true, if it's read only
	 */
	public boolean isReadOnly(ListBox obj){
		Object value = obj.getValue(obj.getSelectedIndex());
		boolean isReadOnly = isReadOnly(value);
		
		return isReadOnly;
	}

	/**
	 * Checks if it's read only.
	 *
	 * @param obj the obj
	 * @return true, if it's read only
	 */
	public boolean isReadOnly(CheckBox obj){
		Object value = obj.getValue();
		boolean isReadOnly = isReadOnly(value);

		return isReadOnly;
	}

	/**
	 * Checks if it's read only.
	 *
	 * @param obj the obj
	 * @return true, if it's read only
	 */
	public boolean isReadOnly(DateBox obj){
		Object value = obj.getValue();
		boolean isReadOnly = isReadOnly(value);
		
		return isReadOnly;
	}

	/**
	 * Checks if it's readonly.
	 *
	 * @param value the value
	 * @return true, if it's read only
	 */
	private boolean isReadOnly(Object value) {
		return readonly.isReadOnly(value);
	}

	/**
	 * Sets the click handler.
	 *
	 * @param clickHandler the new click handler
	 */
	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

	/**
	 * Gets the click handler.
	 *
	 * @return the click handler
	 */
	public ClickHandler getClickHandler() {
		return clickHandler;
	}
}
