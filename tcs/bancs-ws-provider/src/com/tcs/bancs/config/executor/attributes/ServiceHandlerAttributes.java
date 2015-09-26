package com.tcs.bancs.config.executor.attributes;

public class ServiceHandlerAttributes
{
	private String service_id = null;
	private String service_class = null;
	private SrvcMethod service_method = null;

	public String getService_id() {
		return service_id;
	}
	public void setService_id(String service_id) {
		this.service_id = service_id;
	}
	public String getService_class() {
		return service_class;
	}
	public void setService_class(String service_class) {
		this.service_class = service_class;
	}
	public SrvcMethod getService_method() {
		return service_method;
	}
	public void setService_method(SrvcMethod service_method) {
		this.service_method = service_method;
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
		retValue = "ServiceHandlerAttributes ( "
				+ super.toString() + TAB
				+ "service_id = " + this.service_id + TAB
				+ "service_class = " + this.service_class + TAB
				+ "service_method = " + this.service_method + TAB
				+ " )";

		return retValue;
	}

}
