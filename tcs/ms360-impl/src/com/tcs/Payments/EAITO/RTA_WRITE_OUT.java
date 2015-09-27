package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class RTA_WRITE_OUT implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5129477962463959854L;
	private Double RTRN_CODE;
	private Double ERR_CODE;
	private String ERR_MSG;
	private Double ERR_SQL;
	private String EXT_STR;
	private Double EXT_NUM;

	public Double getRTRN_CODE() {
		return RTRN_CODE;
	}
	public void setRTRN_CODE(Double rtrn_code) {
		RTRN_CODE = rtrn_code;
	}
	public Double getERR_CODE() {
		return ERR_CODE;
	}
	public void setERR_CODE(Double err_code) {
		ERR_CODE = err_code;
	}
	public String getERR_MSG() {
		return ERR_MSG;
	}
	public void setERR_MSG(String err_msg) {
		ERR_MSG = err_msg;
	}
	public Double getERR_SQL() {
		return ERR_SQL;
	}
	public void setERR_SQL(Double err_sql) {
		ERR_SQL = err_sql;
	}
	public String getEXT_STR() {
		return EXT_STR;
	}
	public void setEXT_STR(String ext_str) {
		EXT_STR = ext_str;
	}
	public Double getEXT_NUM() {
		return EXT_NUM;
	}
	public void setEXT_NUM(Double ext_num) {
		EXT_NUM = ext_num;
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

		retValue = "RTA_WRITE_OUT ( "
			+ super.toString() + TAB
			+ "RTRN_CODE = " + this.RTRN_CODE + TAB
			+ "ERR_CODE = " + this.ERR_CODE + TAB
			+ "ERR_MSG = " + this.ERR_MSG + TAB
			+ "ERR_SQL = " + this.ERR_SQL + TAB
			+ "EXT_STR = " + this.EXT_STR + TAB
			+ "EXT_NUM = " + this.EXT_NUM + TAB
			+ " )";

		return retValue;
	}

}
