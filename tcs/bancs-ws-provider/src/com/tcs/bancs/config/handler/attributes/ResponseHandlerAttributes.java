package com.tcs.bancs.config.handler.attributes;

import java.util.Map;

public class ResponseHandlerAttributes {

	private String handlerImplClass = null;
	private Map<String,String> responseTransformer = null;

	public String getHandlerImplClass() {
		return handlerImplClass;
	}
	public void setHandlerImplClass(String handlerImplClass) {
		this.handlerImplClass = handlerImplClass;
	}
	public Map<String, String> getResponseTransformer() {
		return responseTransformer;
	}
	public void setResponseTransformer(Map<String, String> responseTransformer) {
		this.responseTransformer = responseTransformer;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
		final String TAB = "\r\n";

		String retValue = "";

		retValue = "ResponseHandlerAttributes ( "
				+ super.toString() + TAB
				+ "handlerImplClass = " + this.handlerImplClass + TAB
				+ "responseTransformer = " + this.responseTransformer + TAB
				+ " )";

		return retValue;
	}
}
