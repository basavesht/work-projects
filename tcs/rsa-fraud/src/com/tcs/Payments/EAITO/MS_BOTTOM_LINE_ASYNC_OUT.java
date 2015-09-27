package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_BOTTOM_LINE_ASYNC_OUT implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -78673104014185776L;
	private String RETURN_CODE = null;
	private String ERR_DESC = null;

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
	 * @return the eRR_DESC
	 */
	public String getERR_DESC() {
		return ERR_DESC;
	}
	/**
	 * @param err_desc the eRR_DESC to set
	 */
	public void setERR_DESC(String err_desc) {
		ERR_DESC = err_desc;
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

		retValue = "MS_BOTTOM_LINE_ASYNC_OUT ( "
			+ super.toString() + TAB
			+ "RETURN_CODE = " + this.RETURN_CODE + TAB
			+ "ERR_DESC = " + this.ERR_DESC + TAB
			+ " )";

		return retValue;
	}
}