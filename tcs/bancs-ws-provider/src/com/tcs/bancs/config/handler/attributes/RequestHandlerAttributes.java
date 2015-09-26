package com.tcs.bancs.config.handler.attributes;

import java.util.Map;

public class RequestHandlerAttributes {

	private String handlerImplClass = null;
	private Map<String,String> requestValidator = null;
	private Map<String,String> requestTransformer = null;
	private Map<String,String> requestRouter = null;

	public String getHandlerImplClass() {
		return handlerImplClass;
	}
	public void setHandlerImplClass(String handlerImplClass) {
		this.handlerImplClass = handlerImplClass;
	}
	public Map<String, String> getRequestValidator() {
		return requestValidator;
	}
	public void setRequestValidator(Map<String, String> requestValidator) {
		this.requestValidator = requestValidator;
	}
	public Map<String, String> getRequestTransformer() {
		return requestTransformer;
	}
	public void setRequestTransformer(Map<String, String> requestTransformer) {
		this.requestTransformer = requestTransformer;
	}
	public Map<String, String> getRequestRouter() {
		return requestRouter;
	}
	public void setRequestRouter(Map<String, String> requestRouter) {
		this.requestRouter = requestRouter;
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
	    
	    retValue = "RequestHandlerAttributes ( "
	        + super.toString() + TAB
	        + "handlerImplClass = " + this.handlerImplClass + TAB
	        + "requestValidator = " + this.requestValidator + TAB
	        + "requestTransformer = " + this.requestTransformer + TAB
	        + "requestRouter = " + this.requestRouter + TAB
	        + " )";
	
	    return retValue;
	}


}
