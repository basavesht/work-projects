package com.tcs.bancs.csrfguard;

public class SecuredURI {
	
	private String uri = null;
	private String state = null;

	public SecuredURI(String uri, String state) {
		this.uri = uri;
		this.state = state;
	}

	public String getURI() {
		return uri;
	}

	public String getStateParameter() {
		return state;
	}

}
