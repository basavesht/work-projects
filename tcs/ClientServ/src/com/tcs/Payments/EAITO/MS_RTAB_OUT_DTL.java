package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_RTAB_OUT_DTL implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7552226237439238475L;
	private String CONTEXT = null;
	private String REAL_TIME_BALANCE_DTLS = null;
	private String EXT_STR = null;
	private Double EXT_NUM;

	public String getCONTEXT() {
		return CONTEXT;
	}
	public void setCONTEXT(String context) {
		CONTEXT = context;
	}
	public Double getEXT_NUM() {
		return EXT_NUM;
	}
	public void setEXT_NUM(Double ext_num) {
		EXT_NUM = ext_num;
	}
	public String getEXT_STR() {
		return EXT_STR;
	}
	public void setEXT_STR(String ext_str) {
		EXT_STR = ext_str;
	}
	public String getREAL_TIME_BALANCE_DTLS() {
		return REAL_TIME_BALANCE_DTLS;
	}
	public void setREAL_TIME_BALANCE_DTLS(String real_time_balance_dtls) {
		REAL_TIME_BALANCE_DTLS = real_time_balance_dtls;
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

		retValue = "MS_RTAB_OUT_DTL ( "
			+ super.toString() + TAB
			+ "CONTEXT = " + this.CONTEXT + TAB
			+ "REAL_TIME_BALANCE_DTLS = " + this.REAL_TIME_BALANCE_DTLS + TAB
			+ "EXT_STR = " + this.EXT_STR + TAB
			+ "EXT_NUM = " + this.EXT_NUM + TAB
			+ " )";

		return retValue;
	}
}
