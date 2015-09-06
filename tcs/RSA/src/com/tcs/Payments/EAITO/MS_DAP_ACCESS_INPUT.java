package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_DAP_ACCESS_INPUT  implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2145594430347536512L;
	private String	 USERID =null;
	private String	APPLICATIONNAME = null;
	private String	VERB = null;

	public String getAPPLICATIONNAME() {
		return APPLICATIONNAME;
	}
	public void setAPPLICATIONNAME(String applicationname) {
		APPLICATIONNAME = applicationname;
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

		retValue = "MS_DAP_ACCESS_INPUT ( "
			+ super.toString() + TAB
			+ "USERID = " + this.USERID + TAB
			+ "APPLICATIONNAME = " + this.APPLICATIONNAME + TAB
			+ "VERB = " + this.VERB + TAB
			+ " )";

		return retValue;
	}

}
