/**
 * 
 */
package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class OtherMSAccountTO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6258296509168236393L;

	private String otherMSAccName = null; //JSON Account Name as obtained from MS360 Framework..
	private String otherMSKeyAccount = null; //JSON Key account as obtained from MS360 Framework..(3-6-3 format)
	private String otherMSAccNumber = null; //JSON Account number as obtained from MS360 Framework..

	/**
	 * @return the otherMSAccName
	 */
	public String getOtherMSAccName() {
		return otherMSAccName;
	}
	/**
	 * @param otherMSAccName the otherMSAccName to set
	 */
	public void setOtherMSAccName(String otherMSAccName) {
		this.otherMSAccName = otherMSAccName;
	}
	/**
	 * @return the otherMSKeyAccount
	 */
	public String getOtherMSKeyAccount() {
		return otherMSKeyAccount;
	}
	/**
	 * @param otherMSKeyAccount the otherMSKeyAccount to set
	 */
	public void setOtherMSKeyAccount(String otherMSKeyAccount) {
		this.otherMSKeyAccount = otherMSKeyAccount;
	}
	/**
	 * @return the otherMSAccNumber
	 */
	public String getOtherMSAccNumber() {
		return otherMSAccNumber;
	}
	/**
	 * @param otherMSAccNumber the otherMSAccNumber to set
	 */
	public void setOtherMSAccNumber(String otherMSAccNumber) {
		this.otherMSAccNumber = otherMSAccNumber;
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

		retValue = "OtherMSAccountTO ( "
			+ super.toString() + TAB
			+ "otherMSAccName = " + this.otherMSAccName + TAB
			+ "otherMSKeyAccount = " + this.otherMSKeyAccount + TAB
			+ "otherMSAccNumber = " + this.otherMSAccNumber + TAB
			+ " )";

		return retValue;
	}

}
