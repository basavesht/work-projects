package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class GetTransactionStatus extends EBWTransferObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2692346764459376164L;
	private String txn_status = null;
	private String acnt_status = null;

	public String getTxn_status() {
		return txn_status;
	}
	public void setTxn_status(String txn_status) {
		this.txn_status = txn_status;
	}
	public String getAcnt_status() {
		return acnt_status;
	}
	public void setAcnt_status(String acnt_status) {
		this.acnt_status = acnt_status;
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

		retValue = "GetTransactionStatus ( "
			+ super.toString() + TAB
			+ "txn_status = " + this.txn_status + TAB
			+ " )";

		return retValue;
	}
}
