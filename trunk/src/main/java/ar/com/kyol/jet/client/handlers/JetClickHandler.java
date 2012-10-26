package ar.com.kyol.jet.client.handlers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * A ClickHandler wrapper to have row information in JetViewTable.addColumnWithHandler method.
 * 
 * @author Federico Pugnali & Silvana Muzzopappa
 * 
 *
 */
public abstract class JetClickHandler implements ClickHandler {

	private int row;
	
	@Override
	public abstract void onClick(ClickEvent arg0);
	
	/**
	 * Returns true if the cell must have this click handler.
	 * @param obj
	 * @return condition to add the click handler to the cell.
	 */
	public abstract boolean compareTo(Object obj);
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}

}
