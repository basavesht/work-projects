package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class TxnVersionDetails extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7153346477091191415L;
	private Double versionnum=null;
	private Double batversionnum = null;
	private Double partxnversionnum = null;
	
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
	 * @return the partxnversionnum
	 */
	public Double getPartxnversionnum() {
		return partxnversionnum;
	}
	/**
	 * @param partxnversionnum the partxnversionnum to set
	 */
	public void setPartxnversionnum(Double partxnversionnum) {
		this.partxnversionnum = partxnversionnum;
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
	    
	    retValue = "TxnVersionDetails ( "
	        + super.toString() + TAB
	        + "versionnum = " + this.versionnum + TAB
	        + "batversionnum = " + this.batversionnum + TAB
	        + "partxnversionnum = " + this.partxnversionnum + TAB
	        + " )";
	
	    return retValue;
	}
	
	
}
