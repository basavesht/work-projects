package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_DAP_ACCESS_OUTPUT  implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1322669381460390738L;
	private boolean superUser;
	public boolean isSuperUser(){
		return superUser;
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
	    
	    retValue = "MS_DAP_ACCESS_OUTPUT ( "
	        + super.toString() + TAB
	        + "superUser = " + this.superUser + TAB
	        + " )";
	
	    return retValue;
	} 
	
}
