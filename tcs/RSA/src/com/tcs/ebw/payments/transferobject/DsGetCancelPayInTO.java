/*
 * Created on Sun Apr 26 12:30:28 IST 2009
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
public class DsGetCancelPayInTO extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1198885800772538742L;
	private String paypayref = null;
	private Double par_txn_no = null;
	
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
	 * @return the par_txn_no
	 */
	public Double getPar_txn_no() {
		return par_txn_no;
	}
	/**
	 * @param par_txn_no the par_txn_no to set
	 */
	public void setPar_txn_no(Double par_txn_no) {
		this.par_txn_no = par_txn_no;
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
	    
	    retValue = "DsGetCancelPayInTO ( "
	        + super.toString() + TAB
	        + "paypayref = " + this.paypayref + TAB
	        + "par_txn_no = " + this.par_txn_no + TAB
	        + " )";
	
	    return retValue;
	}

}