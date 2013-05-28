package ar.com.kyol.jet.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class JetDoubleListBox<E> extends Composite implements HasEnabled {

	private JetCombo<E> leftList;
	private JetCombo<E> rightList;
	private Button addButton;
	private Button removeButton;
	
	public JetDoubleListBox(String descriptor) {
		leftList = new JetCombo<E>(descriptor, false, true);
		rightList = new JetCombo<E>(descriptor, false, true);
		rightList.set(new ArrayList<E>());
		setBoxesSize("200px", "300px");
		addButton = new Button(">");
		removeButton = new Button("<");
		
		addButton.addClickHandler(addButtonClickHandler());
		removeButton.addClickHandler(removeButtonClickHandler());
		
		VerticalPanel buttonsPanel = new VerticalPanel();
		buttonsPanel.add(addButton);
		buttonsPanel.add(removeButton);
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.add(leftList);
		mainPanel.add(buttonsPanel);
		mainPanel.add(rightList);
		mainPanel.setCellVerticalAlignment(buttonsPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.setStyleName("doubleListBox");
		initWidget(mainPanel);
	}

	public JetDoubleListBox(String descriptor, List<E> initialList) {
		this(descriptor);
		leftList.set(initialList);
	}
	
	public void setBoxesSize(String width, String height) {
		leftList.setSize(width, height);
		rightList.setSize(width, height);
	}
	
	public void setBoxesWidth(String width) {
		leftList.setWidth(width);
		rightList.setWidth(width);
	}
	
	public void setBoxesHeight(String height) {
		leftList.setHeight(height);
		rightList.setHeight(height);
	}
	
	private ClickHandler addButtonClickHandler() {
		return new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				List<E> selectedItems = getSelectedList(leftList);
				if(!selectedItems.isEmpty()) {
					rightList.addAll(selectedItems);
					removeListFromCombo(selectedItems, leftList);
				}
			}
		};
	}
	
	private ClickHandler removeButtonClickHandler() {
		
		return new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				List<E> selectedItems = getSelectedList(rightList);
				if(!selectedItems.isEmpty()) {
					leftList.addAll(selectedItems);
					removeListFromCombo(selectedItems, rightList);
				}
			}
		};
	}

	private void removeListFromCombo(List<E> selectedItems, JetCombo<E> jetCombo) {
		for (E e : selectedItems) {
			jetCombo.remove(e);
		}
	}
	
	private List<E> getSelectedList(JetCombo<E> jetCombo) {
		List<E> returnList = new ArrayList<E>();
		int count = jetCombo.getItemCount();
		for(int i = 0; i < count; i++) {
			if(jetCombo.isSelectedItem(i)) {
				E selected = jetCombo.getValue(i);
				returnList.add(selected);
			}
		}
		return returnList;
	}


	public void setInitialList(List<E> initialList) {
		leftList.set(initialList);
	}

	@Override
	public boolean isEnabled() {
		return rightList.isEnabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		leftList.setEnabled(enabled);
		rightList.setEnabled(enabled);
		addButton.setEnabled(enabled);
		removeButton.setEnabled(enabled);
	}
	
	private List<E> getComboList(JetCombo<E> jetCombo) {
		List<E> returnList = new ArrayList<E>();
		int count = jetCombo.getItemCount();
		for(int i = 0; i < count; i++){
			returnList.add(jetCombo.getValue(i));
		}
		return returnList;
		
	}
	
	public List<E> getRightList() {
		return getComboList(rightList);
	}
	
	public List<E> getLeftList() {
		return getComboList(leftList);
	}
	
	public void addClickHandlerToAddButton(ClickHandler clickHandler) {
		addButton.addClickHandler(clickHandler);
	}

	public void addClickHandlerToRemoveButton(ClickHandler clickHandler) {
		removeButton.addClickHandler(clickHandler);
	}

}
