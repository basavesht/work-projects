package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_RTAB_INPUT implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5750223091872267330L;
	private String USER_ID = null;
	private String OFFICE_ID = null;
	private String ACCOUNT_NUM = null;
	private String FA_ID = null;
	private String EXT_STR = null;
	private Double EXT_NUM;
	private String APPL_ID = null;

	/**
	 * @return the uSER_ID
	 */
	public String getUSER_ID() {
		return USER_ID;
	}
	/**
	 * @param user_id the uSER_ID to set
	 */
	public void setUSER_ID(String user_id) {
		USER_ID = user_id;
	}
	/**
	 * @return the oFFICE_ID
	 */
	public String getOFFICE_ID() {
		return OFFICE_ID;
	}
	/**
	 * @param office_id the oFFICE_ID to set
	 */
	public void setOFFICE_ID(String office_id) {
		OFFICE_ID = office_id;
	}
	/**
	 * @return the aCCOUNT_NUM
	 */
	public String getACCOUNT_NUM() {
		return ACCOUNT_NUM;
	}
	/**
	 * @param account_num the aCCOUNT_NUM to set
	 */
	public void setACCOUNT_NUM(String account_num) {
		ACCOUNT_NUM = account_num;
	}
	/**
	 * @return the fA_ID
	 */
	public String getFA_ID() {
		return FA_ID;
	}
	/**
	 * @param fa_id the fA_ID to set
	 */
	public void setFA_ID(String fa_id) {
		FA_ID = fa_id;
	}
	/**
	 * @return the eXT_STR
	 */
	public String getEXT_STR() {
		return EXT_STR;
	}
	/**
	 * @param ext_str the eXT_STR to set
	 */
	public void setEXT_STR(String ext_str) {
		EXT_STR = ext_str;
	}
	/**
	 * @return the eXT_NUM
	 */
	public Double getEXT_NUM() {
		return EXT_NUM;
	}
	/**
	 * @param ext_num the eXT_NUM to set
	 */
	public void setEXT_NUM(Double ext_num) {
		EXT_NUM = ext_num;
	}
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

		retValue = "MS_RTAB_INPUT ( "
			+ super.toString() + TAB
			+ "USER_ID = " + this.USER_ID + TAB
			+ "OFFICE_ID = " + this.OFFICE_ID + TAB
			+ "ACCOUNT_NUM = " + this.ACCOUNT_NUM + TAB
			+ "FA_ID = " + this.FA_ID + TAB
			+ "EXT_STR = " + this.EXT_STR + TAB
			+ "EXT_NUM = " + this.EXT_NUM + TAB
			+ "APPL_ID = " + this.APPL_ID + TAB
			+ " )";

		return retValue;
	}

}