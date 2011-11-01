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

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtent.reflection.client.Reflection;

/**
 * Abstract wrapper to make a JetTable use a pagination behaviour.
 * 
 * @author klarsk
 *
 * @param <E>
 */
public abstract class JetPaginatedTable<E extends Reflection> extends Composite {
	
	protected JetTable<E> jetTable;
	protected int from;
	protected int qty;
	protected int qtyRetrieved;
	protected int total;
	protected int page = 1;
	protected Panel mainPanel;
	protected Panel navigationPanel;
	
	public JetPaginatedTable() {
		Panel totalPanel = new VerticalPanel();
		navigationPanel = new HorizontalPanel();
		mainPanel = new VerticalPanel();
		totalPanel.add(mainPanel);
		this.jetTable = getJetTable();
		mainPanel.add(jetTable);
		this.qty = getPageSize();
		refresh();
		this.initWidget(totalPanel);
	}
	
	private void addNavigationLinks() {
		//FIXME internationalization!
		//gmail like pagination:
		int to = from+qtyRetrieved-1;
		if(qtyRetrieved == 0) from = 0; //for empty lists
		if(from > 0) {
			if(from+1 > (qty + 2)) {
				Hyperlink first = new Hyperlink("<< Primero", this.getPlainToken()+"/p1");
				first.getElement().getStyle().setMarginRight(5, Unit.PX);
				navigationPanel.add(first);
			}
			Hyperlink previous = new Hyperlink("< Anterior", this.getPlainToken()+"/p"+(page-1));
			previous.getElement().getStyle().setMarginRight(5, Unit.PX);
			navigationPanel.add(previous);
		}
		//FIXME de internat
		Label label = new Label((from+1)+" - "+(to+1)+" de "+total);
		//label.setWidth("130px");
		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		navigationPanel.add(label);
		if(from+qtyRetrieved < total && total != 0) {
			Hyperlink next = new Hyperlink("Siguiente >", this.getPlainToken()+"/p"+(page+1));
			next.getElement().getStyle().setMarginLeft(5, Unit.PX);
			navigationPanel.add(next);
			if(from+qtyRetrieved < (total - qty)) {
				Hyperlink last = new Hyperlink("Último >>", this.getPlainToken()+"/p"+(int)Math.ceil(Float.valueOf(total) / qty));
				last.getElement().getStyle().setMarginLeft(5, Unit.PX);
				navigationPanel.add(last);
			}
		}
		//
		//-------------------------------------------------------------------------------------------
		//OLD another pagination:
//		int to = from+qtyRetrieved-1;
//		if(qtyRetrieved == 0) from = 0; //for empty lists
//		if(from > 1) {
////			if(from > (qty + 2)) {
////				navigationPanel.add(new Hyperlink("<< Newest", this.getPlainToken()+"/p1"));
////			}
//			navigationPanel.add(new Hyperlink("previous", this.getPlainToken()+"/p"+(page-1)));
//		}
//		int mostrados = 0;
//		for (int i = page-5; i < page; i++) {
//			if(i > 0) {
//				navigationPanel.add(new Hyperlink(Integer.toString(i), this.getPlainToken()+"/p"+(i)));
//				mostrados++;
//			}
//		}
//		if(mostrados > 0 || total > qty) {
//			navigationPanel.add(new Hyperlink("<b>"+Integer.toString(page)+"</b>",true, this.getPlainToken()+"/p"+(page)));
//		}
//		for (int i = page+1; i < ((int)Math.ceil(Float.valueOf(total) / qty))+1 && i < page+10-mostrados; i++) {
//			navigationPanel.add(new Hyperlink(Integer.toString(i), this.getPlainToken()+"/p"+(i)));
//		}
//		if(from+qtyRetrieved < total && total != 0) {
//			navigationPanel.add(new Hyperlink("next", this.getPlainToken()+"/p"+(page+1)));
////			if(from-1+qtyRetrieved < (total - qty)) {
////				navigationPanel.add(new Hyperlink("Oldest >>", this.getPlainToken()+"/p"+(int)Math.ceil(Float.valueOf(total) / qty)));
////			}
//		}
//		Label label = new Label(" (Showing "+from+" to "+to+" out of "+total+" results)");
//		label.setWidth("250px");
//		label.addStyleName("showingResults");
//		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
//		navigationPanel.add(label);
		//
		//-------------------------------------------------------------------------------------------
	}
	
	/**
	 * Check the requested page from the History token and returns a value for FROM.
	 * 
	 * @return from
	 */
	protected int getRequestedPageFrom() {
		String token = History.getToken(); 
		int pagePos = token.indexOf("/p")+2;
		if(pagePos > 1 && !token.equals("")) {
			String sPage = token.substring(pagePos);
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<sPage.length();i++) {
				if(!Character.isDigit(sPage.charAt(i))) break;
				sb.append(sPage.substring(i, i+1));
			}
			if(sb.length()>0) {
				page = Integer.parseInt(sb.toString());
				return (page * qty) - qty;
			}
		} else {
			page = 1;
		}
		return 0;
	}
	
	/**
	 * Returns the token without page parameters.
	 * 
	 * @return
	 */
	protected String getPlainToken() {
		int pos = History.getToken().indexOf("/p");
		if(pos != -1) {
			return History.getToken().substring(0, pos);
		}
		return History.getToken();
	}
	
	public void refresh() {
		//TODO loading message Crawler.getErrorLabel().setText("Loading...");
		//this.mainPanel.clear();
		this.from = this.getRequestedPageFrom();
		getTotalRows(new AsyncCallback<Integer>() {
			
			@Override
			public void onSuccess(Integer arg0) {
				total = arg0;
				getValues(from, qty, new AsyncCallback<List<E>>() {
					
					@Override
					public void onSuccess(List<E> arg0) {
						callbackRefresh(arg0);
					}
					
					@Override
					public void onFailure(Throwable arg0) {
						onAnyError(arg0);
					}
				});
			}
			
			@Override
			public void onFailure(Throwable arg0) {
				onAnyError(arg0);
			}
		});
	}
	
	private void callbackRefresh(List<E> values) {
		
		int retSize;
		if(values != null) retSize = values.size();
		else retSize = 0;
		
		this.qtyRetrieved = retSize;
		
		this.jetTable.setValues(values);
		
		addNavigationLinks();
		
		Panel verticalPanel = new VerticalPanel();
		verticalPanel.add(navigationPanel);
		verticalPanel.add(jetTable);
		
		Panel listPanel = new DecoratorPanel();
		listPanel.add(verticalPanel);
		
		mainPanel.clear();
		mainPanel.add(listPanel);
	}
	
	protected abstract void onAnyError(Throwable t);

	protected abstract void getValues(int initialRow, int qty, AsyncCallback<List<E>> callback);
	
	protected abstract void getTotalRows(AsyncCallback<Integer> callback);
	
	protected abstract int getPageSize();
	
	protected abstract JetTable<E> getJetTable();

}
