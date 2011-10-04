package ar.com.kyol.jet.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

public interface JetBundle extends ClientBundle {
	
	public static final JetBundle INSTANCE =  GWT.create(JetBundle.class);
	
	@NotStrict
	@Source("Jet.css")
	public CssResource css();

}
