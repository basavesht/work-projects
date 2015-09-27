package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_BOTTOM_LINE_PRINT_OUT implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1619838232350798011L;
	private String RESPONSE = null;
	private String FAULT_CODE = null;
	private String FAULT_STRING = null;
	private String PRINTER_STATUS_CODE = null;
	/**
	 * @return the rESPONSE
	 */
	public String getRESPONSE() {
		return RESPONSE;
	}
	/**
	 * @param response the rESPONSE to set
	 */
	public void setRESPONSE(String response) {
		RESPONSE = response;
	}
	/**
	 * @return the fAULT_CODE
	 */
	public String getFAULT_CODE() {
		return FAULT_CODE;
	}
	/**
	 * @param fault_code the fAULT_CODE to set
	 */
	public void setFAULT_CODE(String fault_code) {
		FAULT_CODE = fault_code;
	}
	/**
	 * @return the fAULT_STRING
	 */
	public String getFAULT_STRING() {
		return FAULT_STRING;
	}
	/**
	 * @param fault_string the fAULT_STRING to set
	 */
	public void setFAULT_STRING(String fault_string) {
		FAULT_STRING = fault_string;
	}
	/**
	 * @return the pRINTER_STATUS_CODE
	 */
	public String getPRINTER_STATUS_CODE() {
		return PRINTER_STATUS_CODE;
	}
	/**
	 * @param printer_status_code the pRINTER_STATUS_CODE to set
	 */
	public void setPRINTER_STATUS_CODE(String printer_status_code) {
		PRINTER_STATUS_CODE = printer_status_code;
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

		retValue = "MS_BOTTOM_LINE_PRINT_OUT ( "
			+ super.toString() + TAB
			+ "RESPONSE = " + this.RESPONSE + TAB
			+ "FAULT_CODE = " + this.FAULT_CODE + TAB
			+ "FAULT_STRING = " + this.FAULT_STRING + TAB
			+ "PRINTER_STATUS_CODE = " + this.PRINTER_STATUS_CODE + TAB
			+ " )";

		return retValue;
	}

}
