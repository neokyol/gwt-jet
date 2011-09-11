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

import com.google.gwt.event.dom.client.ClickHandler;
import com.gwtent.reflection.client.Reflection;

/**
 * A JetSingleTable used for views only. The automatically wrapper widgets used here do not update values
 * and are not modifiable. 
 * 
 * @author klarsk
 *
 * @param <E>
 */
public class JetViewSingleTable<E extends Reflection> extends JetSingleTable<E> {
	
	@Override
	protected Wrapper createWrapperWidget(ObjectSetter objSetter,
			JetColumn<E> jetColumn, int column, int row) {
		return new JetViewTableHelper().createWrapperWidget(objSetter, jetColumn, column, row);
	}
	
	public void addColumnWithHandler(String columnName, String title, String contentStyle,
			Integer columnWidth, ClickHandler clickHandler) {
		JetColumn<E> jetCol = new JetColumn<E>(columnName, title, contentStyle, null, columnWidth, ReadOnlyCondition.ALWAYS);
		jetCol.setJetTableParent(this);
		jetCol.setClickHandler(clickHandler);
		jetColumns.add(jetCol);
	}

	@Override
	protected ObjectSetter getProperty(Object obj, String prop) {
		try {
			return super.getProperty(obj, prop);
		}catch (NullPointerException np){
			return null;
		}
	}
}
