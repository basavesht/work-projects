package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_DAP_ROLE_OUTPUT implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3611296144522304217L;
	private String RoleResult=null;
	private String[] ROLE = new String[100];

	public String[] getROLE() {
		return ROLE;
	}
	public void setROLE(String[] role) {
		ROLE = role;
	}
	public String getRoleResult() {
		return RoleResult;
	}
	public void setRoleResult(String roleResult) {
		RoleResult = roleResult;
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
	    
	    retValue = "MS_DAP_ROLE_OUTPUT ( "
	        + super.toString() + TAB
	        + "RoleResult = " + this.RoleResult + TAB
	        + "ROLE = " + this.ROLE + TAB
	        + " )";
	
	    return retValue;
	}
	
}
