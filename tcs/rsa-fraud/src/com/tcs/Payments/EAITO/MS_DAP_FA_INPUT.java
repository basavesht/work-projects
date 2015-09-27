package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_DAP_FA_INPUT  implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2815479685723808569L;
	private String USERID =null;
	private String APPID =null;
	private String VERB = null;
	
	public String getAPPID() {
		return APPID;
	}
	public void setAPPID(String appid) {
		APPID = appid;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String userid) {
		USERID = userid;
	}
	public String getVERB() {
		return VERB;
	}
	public void setVERB(String verb) {
		VERB = verb;
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
	    
	    retValue = "MS_DAP_FA_INPUT ( "
	        + super.toString() + TAB
	        + "USERID = " + this.USERID + TAB
	        + "APPID = " + this.APPID + TAB
	        + "VERB = " + this.VERB + TAB
	        + " )";
	
	    return retValue;
	}
	

}
