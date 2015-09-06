package com.tcs.bancs.config.handler.attributes;

public class WSOperation
{
	private String operation_name = null;
	private String operation_id = null;
	private RequestHandlerAttributes  requestHandler = null;
	private ResponseHandlerAttributes responseHandler = null;

	public String getOperation_name() {
		return operation_name;
	}
	public void setOperation_name(String operation_name) {
		this.operation_name = operation_name;
	}
	public String getOperation_id() {
		return operation_id;
	}
	public void setOperation_id(String operation_id) {
		this.operation_id = operation_id;
	}
	public RequestHandlerAttributes getRequestHandler() {
		return requestHandler;
	}
	public void setRequestHandler(RequestHandlerAttributes requestHandler) {
		this.requestHandler = requestHandler;
	}
	public ResponseHandlerAttributes getResponseHandler() {
		return responseHandler;
	}
	public void setResponseHandler(ResponseHandlerAttributes responseHandler) {
		this.responseHandler = responseHandler;
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
		retValue = "WSOperation ( "
				+ super.toString() + TAB
				+ "operation_name = " + this.operation_name + TAB
				+ "operation_id = " + this.operation_id + TAB
				+ "requestHandler = " + this.requestHandler + TAB
				+ "responseHandler = " + this.responseHandler + TAB
				+ " )";

		return retValue;
	}
}
