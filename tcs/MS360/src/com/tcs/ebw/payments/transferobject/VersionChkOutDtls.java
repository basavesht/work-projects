package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class VersionChkOutDtls extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8652396806601953630L;
	private Double versionnum = null;
	private Double batversionnum = null;
	private Double txnversionnum = null;
	private Double chkversionnum = null;

	/**
	 * @return the versionnum
	 */
	public Double getVersionnum() {
		return versionnum;
	}
	/**
	 * @param versionnum the versionnum to set
	 */
	public void setVersionnum(Double versionnum) {
		this.versionnum = versionnum;
	}
	/**
	 * @return the batversionnum
	 */
	public Double getBatversionnum() {
		return batversionnum;
	}
	/**
	 * @param batversionnum the batversionnum to set
	 */
	public void setBatversionnum(Double batversionnum) {
		this.batversionnum = batversionnum;
	}
	/**
	 * @return the txnversionnum
	 */
	public Double getTxnversionnum() {
		return txnversionnum;
	}
	/**
	 * @param txnversionnum the txnversionnum to set
	 */
	public void setTxnversionnum(Double txnversionnum) {
		this.txnversionnum = txnversionnum;
	}
	/**
	 * @return the chkversionnum
	 */
	public Double getChkversionnum() {
		return chkversionnum;
	}
	/**
	 * @param chkversionnum the chkversionnum to set
	 */
	public void setChkversionnum(Double chkversionnum) {
		this.chkversionnum = chkversionnum;
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

		retValue = "VersionChkOutDtls ( "
			+ super.toString() + TAB
			+ "versionnum = " + this.versionnum + TAB
			+ "batversionnum = " + this.batversionnum + TAB
			+ "txnversionnum = " + this.txnversionnum + TAB
			+ "chkversionnum = " + this.chkversionnum + TAB
			+ " )";

		return retValue;
	}

}