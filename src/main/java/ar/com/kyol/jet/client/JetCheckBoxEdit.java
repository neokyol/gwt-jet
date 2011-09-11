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
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.impl.ImageResourcePrototype;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * A check box that opens a popup to update more values. It has a little pen to edit the 
 * additional values without unchecking it.
 * 
 * @author klarsk
 *
 */
public abstract class JetCheckBoxEdit extends Composite implements HasEnabled, HasValueChangeHandlers<Boolean> {

	private HorizontalPanel checkEditPanel = new HorizontalPanel();
	private HorizontalPanel editImagePanel = new HorizontalPanel();
	private CheckBox checkBox = new CheckBox();
	private Image editImage = new Image();
	private ImageResource imgSrc;
	private OkCancelPopup popup;
	private boolean calledByEdit = false;
	
	public JetCheckBoxEdit() {
		//edit button
		imgSrc = new ImageResourcePrototype("editar", "img/editar.gif",  //TODO get images from jet.jar
				0, 0, 16, 16, false, false);
		editImage.setResource(imgSrc);
		editImage.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(isEnabled()) {
					openPopup(true);
				}
			}
		});
		//edit button end
		
		checkEditPanel.add(checkBox);
		editImagePanel.add(editImage);
		checkEditPanel.add(editImagePanel);
		editImagePanel.setVisible(false);
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				
				if(event.getValue()) {
					editImagePanel.setVisible(true);
					openPopup(false);
				}else{
					editImagePanel.setVisible(false);
					getCurrentPopUp().clean();
				}
			}


		});
		
		initWidget(checkEditPanel);
	}

	private void openPopup(final boolean calledByEdit) {
		this.calledByEdit = calledByEdit;
		getCurrentPopUp().show();
	}

	private OkCancelPopup getCurrentPopUp(){
		if(this.popup==null){
			this.popup = getPopUp();
			popup.addCancelClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent arg0) {
					if(!calledByEdit) {
						checkBox.setValue(false, true);
					}
				}
			});
		}
		
		return popup;
	}
	
	protected abstract OkCancelPopup getPopUp();
	
	@Override
	public boolean isEnabled() {
		return checkBox.isEnabled();
	}
	
	public boolean getValue() {
		return checkBox.getValue();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		checkBox.setEnabled(enabled);
	}
	
	public void clearValue() {
		checkBox.setValue(false);
		editImagePanel.setVisible(false);
		getCurrentPopUp().clean();
	}
	
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return checkBox.addValueChangeHandler(handler);
	}
	
	public Boolean getCheckBoxValue() {
		return checkBox.getValue();
	}
	
	public void setCheckBoxValue(Boolean value) {
		checkBox.setValue(value);
		editImagePanel.setVisible(value);
	}
	
	@Override
	public void fireEvent(GwtEvent<?> event) {
		checkBox.fireEvent(event);
	}
	
}
