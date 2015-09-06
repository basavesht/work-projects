package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_DAP_ROLE_INPUT  implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1856780404097647022L;
	private String RACFID = null;
	private String EPRGROUP = null;
	private String BRANCHID =null;
	private String FAID = null;

	public String getEPRGROUP() {
		return EPRGROUP;
	}
	public void setEPRGROUP(String eprgroup) {
		EPRGROUP = eprgroup;
	}
	public String getRACFID() {
		return RACFID;
	}
	public void setRACFID(String racfid) {
		RACFID = racfid;
	}
	public String getBRANCHID() {
		return BRANCHID;
	}
	public void setBRANCHID(String branchid) {
		BRANCHID = branchid;
	}
	public String getFAID() {
		return FAID;
	}
	public void setFAID(String faid) {
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
	    
	    retValue = "MS_DAP_ROLE_INPUT ( "
	        + super.toString() + TAB
	        + "RACFID = " + this.RACFID + TAB
	        + "EPRGROUP = " + this.EPRGROUP + TAB
	        + "BRANCHID = " + this.BRANCHID + TAB
	        + "FAID = " + this.FAID + TAB
	        + " )";
	
	    return retValue;
	}

}
