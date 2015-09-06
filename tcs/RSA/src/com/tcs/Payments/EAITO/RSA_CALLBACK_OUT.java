/**
 * 
 */
package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class RSA_CALLBACK_OUT implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -400224370678033377L;
	private String RETURN_CODE  = null;
	private String ERROR_DESC = null;

	/**
	 * @return the rETURN_CODE
	 */
	public String getRETURN_CODE() {
		return RETURN_CODE;
	}
	/**
	 * @param return_code the rETURN_CODE to set
	 */
	public void setRETURN_CODE(String return_code) {
		RETURN_CODE = return_code;
	}
	/**
	 * @return the eRROR_DESC
	 */
	public String getERROR_DESC() {
		return ERROR_DESC;
	}
	/**
	 * @param error_desc the eRROR_DESC to set
	 */
	public void setERROR_DESC(String error_desc) {
		ERROR_DESC = error_desc;
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

		retValue = "RSA_CALLBACK_OUT ( "
			+ super.toString() + TAB
			+ "RETURN_CODE = " + this.RETURN_CODE + TAB
			+ "ERROR_DESC = " + this.ERROR_DESC + TAB
			+ " )";

		return retValue;
	}
}
