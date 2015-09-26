package com.tcs.bancs.config.executor.attributes;

public class SrvcMethod {

	private String name = null;
	private Class[] methodParams = null;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class[] getMethodParams() {
		return methodParams;
	}
	public void setMethodParams(Class[] methodParams) {
		this.methodParams = methodParams;
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
		retValue = "MethodParams ( "
				+ super.toString() + TAB
				+ "name = " + this.name + TAB
				+ "methodParams = " + this.methodParams + TAB
				+ " )";

		return retValue;
	}
}
