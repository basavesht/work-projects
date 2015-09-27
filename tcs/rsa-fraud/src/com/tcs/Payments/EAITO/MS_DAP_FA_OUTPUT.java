package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_DAP_FA_OUTPUT  implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2028597235731097212L;
	private int BRANCHID ;
	private int FAID ;
	
	public int getBRANCHID() {
		return BRANCHID;
	}
	public void setBRANCHID(int branchid) {
		BRANCHID = branchid;
	}
	public int getFAID() {
		return FAID;
	}
	public void setFAID(int faid) {
		FAID = faid;
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
	    
	    retValue = "MS_DAP_FA_OUTPUT ( "
	        + super.toString() + TAB
	        + "BRANCHID = " + this.BRANCHID + TAB
	        + "FAID = " + this.FAID + TAB
	        + " )";
	
	    return retValue;
	}




}
