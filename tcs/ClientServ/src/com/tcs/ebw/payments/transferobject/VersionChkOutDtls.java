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
	private static final long serialVersionUID = -5024217118058748406L;
	private Double versionnum = null;
	private Double batversionnum = null;
	private Double txnversionnum = null;

	public Double getVersionnum() {
		return versionnum;
	}
	public void setVersionnum(Double versionnum) {
		this.versionnum = versionnum;
	}
    public Double getBatversionnum() {
		return batversionnum;
	}
	public void setBatversionnum(Double batversionnum) {
		this.batversionnum = batversionnum;
	}
	public Double getTxnversionnum() {
		return txnversionnum;
	}
	public void setTxnversionnum(Double txnversionnum) {
		this.txnversionnum = txnversionnum;
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
	        + " )";
	
	    return retValue;
	}

}
