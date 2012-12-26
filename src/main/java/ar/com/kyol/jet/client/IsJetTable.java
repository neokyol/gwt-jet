package ar.com.kyol.jet.client;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * It can be seen as a JetTable
 * 
 * @author fpugnali
 * @author smuzzopappa
 *
 * @param <E>
 */
public interface IsJetTable<E extends Reflection> extends IsWidget {
	
	/**
	 * Returns the JetTable aspect of the object
	 * 
	 * @return
	 */
	public JetTable<E> asJetTable();

}
