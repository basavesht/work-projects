package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_ACNT_PLATING_INP implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4737522080586019552L;
	private String APPL_ID = null;
	private String SERVER_IP = null;
	private String USER_ID =null;
	private String ACCOUNT_NUMBER = null;
	private String FA = null;
	private String OFFICE =null;

	/**
	 * @return the aPPL_ID
	 */
	public String getAPPL_ID() {
		return APPL_ID;
	}
	/**
	 * @param appl_id the aPPL_ID to set
	 */
	public void setAPPL_ID(String appl_id) {
		APPL_ID = appl_id;
	}
	/**
	 * @return the sERVER_IP
	 */
	public String getSERVER_IP() {
		return SERVER_IP;
	}
	/**
	 * @param server_ip the sERVER_IP to set
	 */
	public void setSERVER_IP(String server_ip) {
		SERVER_IP = server_ip;
	}
	/**
	 * @return the uSERD_ID
	 */
	public String getUSERD_ID() {
		return USER_ID;
	}
	/**
	 * @param userd_id the uSERD_ID to set
	 */
	public void setUSERD_ID(String userd_id) {
		USER_ID = userd_id;
	}
	/**
	 * @return the aCCOUNT_NUMBER
	 */
	public String getACCOUNT_NUMBER() {
		return ACCOUNT_NUMBER;
	}
	/**
	 * @param account_number the aCCOUNT_NUMBER to set
	 */
	public void setACCOUNT_NUMBER(String account_number) {
		ACCOUNT_NUMBER = account_number;
	}
	/**
	 * @return the fA
	 */
	public String getFA() {
		return FA;
	}
	/**
	 * @param fa the fA to set
	 */
	public void setFA(String fa) {
		FA = fa;
	}
	/**
	 * @return the oFFICE
	 */
	public String getOFFICE() {
		return OFFICE;
	}
	/**
	 * @param office the oFFICE to set
	 */
	public void setOFFICE(String office) {
		OFFICE = office;
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
	    
	    retValue = "MS_ACCPLATING_INP ( "
	        + super.toString() + TAB
	        + "APPL_ID = " + this.APPL_ID + TAB
	        + "SERVER_IP = " + this.SERVER_IP + TAB
	        + "USERD_ID = " + this.USER_ID + TAB
	        + "ACCOUNT_NUMBER = " + this.ACCOUNT_NUMBER + TAB
	        + "FA = " + this.FA + TAB
	        + "OFFICE = " + this.OFFICE + TAB
	        + " )";
	
	    return retValue;
	}

}
