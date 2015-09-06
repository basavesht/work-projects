/*
 * Created on Tue May 26 17:25:43 IST 2009
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
public class TrxnDetailsInputTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1110210030950653033L;
	private String paypayref = null;
	private String paybatchref = null;
	private String next_approver_role = null;

	/**
	 * @return the paypayref
	 */
	public String getPaypayref() {
		return paypayref;
	}
	/**
	 * @param paypayref the paypayref to set
	 */
	public void setPaypayref(String paypayref) {
		this.paypayref = paypayref;
	}
	/**
	 * @return the paybatchref
	 */
	public String getPaybatchref() {
		return paybatchref;
	}
	/**
	 * @param paybatchref the paybatchref to set
	 */
	public void setPaybatchref(String paybatchref) {
		this.paybatchref = paybatchref;
	}
	/**
	 * @return the next_approver_role
	 */
	public String getNext_approver_role() {
		return next_approver_role;
	}
	/**
	 * @param next_approver_role the next_approver_role to set
	 */
	public void setNext_approver_role(String next_approver_role) {
		this.next_approver_role = next_approver_role;
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

		retValue = "TrxnDetailsInputTO ( "
			+ super.toString() + TAB
			+ "paypayref = " + this.paypayref + TAB
			+ "paybatchref = " + this.paybatchref + TAB
			+ "next_approver_role = " + this.next_approver_role + TAB
			+ " )";

		return retValue;
	}

}