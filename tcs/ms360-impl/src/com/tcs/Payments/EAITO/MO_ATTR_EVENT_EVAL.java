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
public class MO_ATTR_EVENT_EVAL implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5916885528589613359L;
	private String ATTR_NM = null;
	private String ATTR_VAL = null;

	public String getATTR_NM() {
		return ATTR_NM;
	}
	public void setATTR_NM(String attr_nm) {
		ATTR_NM = attr_nm;
	}
	public String getATTR_VAL() {
		return ATTR_VAL;
	}
	public void setATTR_VAL(String attr_val) {
		ATTR_VAL = attr_val;
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

		retValue = "MO_ATTR_EVENT_EVAL ( "
			+ super.toString() + TAB
			+ "ATTR_NM = " + this.ATTR_NM + TAB
			+ "ATTR_VAL = " + this.ATTR_VAL + TAB
			+ " )";

		return retValue;
	}
}
