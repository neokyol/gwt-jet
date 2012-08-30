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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract wrapper to make a JetTable use a pagination behaviour.
 * 
 * If useHyperlinks is enabled, the navigation occurs with /p(+pageNumber) hyperlinks, and
 * it's up to the web application to manage the new history tokens calling this class refresh() method.
 * 
 * @author fpugnali
 * @author smuzzopappa
 *
 * @param <E>
 */
public abstract class JetPaginatedTable<E extends Reflection> extends Composite {
	
	protected JetTable<E> jetTable;
	protected boolean useHyperlinks;
	protected int from;
	protected int qty;
	protected int qtyRetrieved;
	protected int total;
	protected int page = 1;
	protected AbsolutePanel mainPanel;
	protected Panel navigationPanel;
	
	public JetPaginatedTable() {
		this(true);
	}
	
	public JetPaginatedTable(boolean useHyperlinks) {
		this.useHyperlinks = useHyperlinks;
		Panel totalPanel = new VerticalPanel();
		navigationPanel = new HorizontalPanel();
		mainPanel = new AbsolutePanel();
		totalPanel.add(mainPanel);
		this.jetTable = getJetTable();
		mainPanel.add(jetTable);
		this.qty = getPageSize();
		refresh();
		this.initWidget(totalPanel);
	}
	
	private void addNavigationLinks() {
		//gmail like pagination:
		int to = from+qtyRetrieved-1;
		if(qtyRetrieved == 0) from = 0; //for empty lists
		if(from > 0) {
			if(from+1 > (qty + 2)) {
				addOldest();
			}
			addOlder();
		}
		Label label = new Label((from+1)+" - "+(to+1)+" "+Jet.constants.of()+" "+total);
		label.addStyleDependentName("navigator-gwtjet");
		//label.setWidth("130px");
		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		navigationPanel.add(label);
		if(from+qtyRetrieved < total && total != 0) {
			addNewer();
			if(from+qtyRetrieved < (total - qty)) {
				addNewest();
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

	private void addNewest() {
		String label = Jet.constants.newest()+" >>";
		int newest = (int)Math.ceil(Float.valueOf(total) / qty);
		Widget last;
		if(useHyperlinks) {
			last = new Hyperlink(label, this.getPlainToken()+"/p"+newest);
			last.getElement().getStyle().setMarginLeft(5, Unit.PX);
		} else {
			last = createAnchor(label, newest);
		}
		navigationPanel.add(last);
	}

	private void addNewer() {
		String label = Jet.constants.newer()+" >";
		Widget next;
		if(useHyperlinks) {
			next = new Hyperlink(label, this.getPlainToken()+"/p"+(page+1));
			next.getElement().getStyle().setMarginLeft(5, Unit.PX);
		} else {
			next = createAnchor(label, page+1);
		}
		navigationPanel.add(next);
	}

	private void addOlder() {
		String label = "< "+Jet.constants.older();
		Widget previous;
		if(useHyperlinks) {
			previous = new Hyperlink(label, this.getPlainToken()+"/p"+(page-1));
			previous.getElement().getStyle().setMarginRight(5, Unit.PX);
		} else {
			previous = createAnchor(label, page-1);
		}
		navigationPanel.add(previous);
	}
	
	private void addOldest() {
		String label = "<< "+Jet.constants.oldest();
		Widget first;
		if(useHyperlinks) {
			first = new Hyperlink(label, this.getPlainToken()+"/p1");
			first.getElement().getStyle().setMarginRight(5, Unit.PX);
		} else {
			first = createAnchor(label, 1);
		}
		navigationPanel.add(first);
	}
	
	private Anchor createAnchor(String label, final int page) {
		Anchor anchor = new Anchor(label);
		anchor.addStyleDependentName("gwtjet");
		anchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				JetPaginatedTable.this.page = page;
				refresh();
			}
		});
		return anchor;
	}
	
	/**
	 * Check the requested page from the History token and returns a value for FROM.
	 * If useHyperlinks is disabled, return the FROM according to current page.
	 * 
	 * @return from
	 */
	protected int getRequestedPageFrom() {
		if(useHyperlinks) {
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
				} else {
					page = 1;
				}
			} else {
				page = 1;
			}
		}
		return (page * qty) - qty;
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
		if(!useHyperlinks) {
			AbsolutePanel panel = new AbsolutePanel();
			panel.getElement().getStyle().setOpacity(0.3d);
			panel.setHeight(""+jetTable.getOffsetHeight()+"px");
			panel.setWidth(""+jetTable.getOffsetWidth()+"px");
			panel.getElement().getStyle().setBackgroundColor("black");
			Image loader = new Image();
			loader.setResource(Resources.INSTANCE.loader());
			SimplePanel panelLoader = new SimplePanel();
			panelLoader.add(loader);
			this.mainPanel.add(panelLoader, jetTable.getOffsetWidth() / 2, jetTable.getOffsetHeight() / 2);
			this.mainPanel.add(panel, 0, navigationPanel.getOffsetHeight());
		}
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
		
		this.navigationPanel.clear();
		addNavigationLinks();
		
		Panel verticalPanel = new VerticalPanel();
		verticalPanel.add(navigationPanel);
		verticalPanel.add(jetTable);
		
		mainPanel.clear();
		mainPanel.add(verticalPanel);
		
		tableRefreshed();
	}
	
	protected abstract void onAnyError(Throwable t);

	protected abstract void getValues(int initialRow, int qty, AsyncCallback<List<E>> callback);
	
	protected abstract void getTotalRows(AsyncCallback<Integer> callback);
	
	protected abstract int getPageSize();
	
	protected abstract JetTable<E> getJetTable();
	
	protected abstract void tableRefreshed();
	
	@Override
	public void removeStyleName(String style) {
		jetTable.removeStyleName(style);
	}
	
	@Override
	public void removeStyleDependentName(String styleSuffix) {
		jetTable.removeStyleDependentName(styleSuffix);
	}
	
	@Override
	public void addStyleName(String style) {
		jetTable.addStyleName(style);
	}
	
	@Override
	public void addStyleDependentName(String styleSuffix) {
		jetTable.addStyleDependentName(styleSuffix);
	}
	
	@Override
	public String getStyleName() {
		return jetTable.getStyleName();
	}
	
	@Override
	public String getStylePrimaryName() {
		return jetTable.getStylePrimaryName();
	}

}
