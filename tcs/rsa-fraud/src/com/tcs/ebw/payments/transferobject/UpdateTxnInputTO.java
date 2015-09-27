/*
 * Created on Fri May 29 20:05:59 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
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
public class UpdateTxnInputTO extends EBWTransferObject implements java.io.Serializable {

	private static final long serialVersionUID = -3134749943762808484L;
	private String paypayref = null;
	private String statusCode = null;

	/**
	 * @param paypayref the paypayref to set.
	 */
	public void setPaypayref(String paypayref) {
		this.paypayref=paypayref;
	}
	/**
	 * @return Returns the paypayref.
	 */
	public String getPaypayref() {
		return this.paypayref;
	}

	/**
	 * @param statusCode the statusCode to set.
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode=statusCode;
	}
	/**
	 * @return Returns the statusCode.
	 */
	public String getStatusCode() {
		return this.statusCode;
	}


	/**
	 * Returns TransferObject data as a String.
	 *
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("=====================================================\r\n");
		strB.append ("Data for UpdateTxnInputTO \r\n");
		strB.append ("paypayref = " + paypayref + "\r\n");
		strB.append ("statusCode = " + statusCode + "\r\n");
		return strB.toString();
	}
}