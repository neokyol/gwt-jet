package ar.com.kyol.jet.client.wrappers;

import ar.com.kyol.jet.client.ObjectSetter;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.TextArea;

public class TextAreaWrapper extends Wrapper {

	private TextArea textArea ;
	
	public TextAreaWrapper(ObjectSetter objSetter) {
		this(objSetter, new TextArea());
	}
	
	public TextAreaWrapper(ObjectSetter objSetter, TextArea textArea) {
		super(false);
		this.objSetter = objSetter;
		this.textArea = textArea;
		if(objSetter.getValue() == null) {
			textArea.setText("");
		} else {
			textArea.setText(formatValue(objSetter.getValue().toString()));
		}
		textArea.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent arg0) {
				setProperty(TextAreaWrapper.this.textArea.getText());
			}
		});
		initWidget(textArea);	
	}
	@Override
	protected String getValueAsString() {
		return null;
	}

	public TextArea getTextArea() {
		return textArea;
	}

}
