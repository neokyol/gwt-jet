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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * A little pen to edit something with a popup. 
 * 
 * @author klarsk
 *
 */
public abstract class JetEdit extends Composite implements HasEnabled, ReadOnlyCondition.HasValueForReadOnlyCondition {

	private HorizontalPanel editImagePanel = new HorizontalPanel();
	private Image editImage = new Image();
	private OkCancelPopup popup;
	private boolean enabled = true;
	
	public JetEdit() {
		//edit button
		editImage.setResource(Resources.INSTANCE.editar());
		editImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(isEnabled()) {
					openPopup();
				}
			}
		});
		//edit button end
		
		editImagePanel.add(editImage);
		editImagePanel.setVisible(true);
		
		initWidget(editImagePanel);
	}

	private void openPopup() {
		getCurrentPopUp().show();
	}

	private OkCancelPopup getCurrentPopUp(){
		if(this.popup==null){
			this.popup = getPopUp();
		}
		
		return popup;
	}
	
	protected abstract OkCancelPopup getPopUp();
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if(enabled) {
			editImage.removeStyleName("Jet-ImageDisabled");
		} else {
			editImage.addStyleName("Jet-ImageDisabled");
		}
	}
	
	@Override
	public Object getValueForReadOnlyCondition() {
		return true;
	}
	
}
