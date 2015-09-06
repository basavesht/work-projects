/**
 * 
 */
package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class BrValidationLogTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7952891558491875282L;
	private String bOKey;
	private String approved_by;

	/**
	 * @return the bOKey
	 */
	public String getBOKey() {
		return bOKey;
	}
	/**
	 * @param key the bOKey to set
	 */
	public void setBOKey(String key) {
		bOKey = key;
	}
	/**
	 * @return the approved_by
	 */
	public String getApproved_by() {
		return approved_by;
	}
	/**
	 * @param approved_by the approved_by to set
	 */
	public void setApproved_by(String approved_by) {
		this.approved_by = approved_by;
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

		retValue = "BrValidationLogTO ( "
			+ super.toString() + TAB
			+ "bOKey = " + this.bOKey + TAB
			+ "approved_by = " + this.approved_by + TAB
			+ " )";

		return retValue;
	}

}
