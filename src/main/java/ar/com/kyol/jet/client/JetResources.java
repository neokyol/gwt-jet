package ar.com.kyol.jet.client;

import com.google.gwt.core.client.EntryPoint;

public class JetResources implements EntryPoint {

	@Override
	public void onModuleLoad() {
		JetBundle.INSTANCE.css().ensureInjected();
	}

}
