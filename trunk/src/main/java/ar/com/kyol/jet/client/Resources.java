package ar.com.kyol.jet.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
	
	public static final Resources INSTANCE =  GWT.create(Resources.class);

	@Source("images/editar.gif")
	ImageResource editar();
	
	@Source("images/gwt-jet-loader.gif")
	ImageResource loader();
	
}
