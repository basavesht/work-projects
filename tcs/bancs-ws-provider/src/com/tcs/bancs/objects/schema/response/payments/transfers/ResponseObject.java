/**
 * 
 */
package com.tcs.bancs.objects.schema.response.payments.transfers;

import java.io.Serializable;

/**
 * @author 259245
 *
 */
public class ResponseObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 513402212820213230L;
	private Integer returnCode = null;
	private String description = null;
	
	/**
	 * @return the returnCode
	 */
	public Integer getReturnCode() {
		return returnCode;
	}
	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(Integer returnCode) {
		this.returnCode = returnCode;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
		final String TAB = "    ";

		String retValue = "";

		retValue = "ResponseObjectTO ( "
				+ super.toString() + TAB
				+ "returnCode = " + this.returnCode + TAB
				+ "description = " + this.description + TAB
				+ " )";

		return retValue;
	}


}
