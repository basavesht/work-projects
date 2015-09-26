package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class BUSINESS_RULE_OUT_APPR implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4895635732576029571L;
	private String 	ROLE =null;
	private Integer PRIORITY=null;
	
	public Integer getPRIORITY() {
		return PRIORITY;
	}
	public void setPRIORITY(Integer priority) {
		PRIORITY = priority;
	}
	public String getROLE() {
		return ROLE;
	}
	public void setROLE(String role) {
		ROLE = role;
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
	    
	    retValue = "BUSINESS_RULE_OUT_APPR ( "
	        + super.toString() + TAB
	        + "ROLE = " + this.ROLE + TAB
	        + "PRIORITY = " + this.PRIORITY + TAB
	        + " )";
	
	    return retValue;
	}

}
