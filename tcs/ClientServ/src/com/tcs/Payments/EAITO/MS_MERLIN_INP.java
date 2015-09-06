package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_MERLIN_INP implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5321396600602757427L;
	private String RMRKS_L01;
	private String OFFICE_ACNT;
	private String ACNT_NUM;
	private String KEY_ACNT;
	private String FANO;
	private String EXT_STR;
	private Double EXT_NUM;
	private String USER_ID;
	private String APPL_ID;
	private String AUTH_APPL_ID;
	private String AUTH_VERB;
	private String WEB_SERVER_ID;
	
	/**
	 * @return the rMRKS_L01
	 */
	public String getRMRKS_L01() {
		return RMRKS_L01;
	}
	/**
	 * @param rmrks_l01 the rMRKS_L01 to set
	 */
	public void setRMRKS_L01(String rmrks_l01) {
		RMRKS_L01 = rmrks_l01;
	}
	/**
	 * @return the oFFICE_ACNT
	 */
	public String getOFFICE_ACNT() {
		return OFFICE_ACNT;
	}
	/**
	 * @param office_acnt the oFFICE_ACNT to set
	 */
	public void setOFFICE_ACNT(String office_acnt) {
		OFFICE_ACNT = office_acnt;
	}
	/**
	 * @return the aCNT_NUM
	 */
	public String getACNT_NUM() {
		return ACNT_NUM;
	}
	/**
	 * @param acnt_num the aCNT_NUM to set
	 */
	public void setACNT_NUM(String acnt_num) {
		ACNT_NUM = acnt_num;
	}
	/**
	 * @return the kEY_ACNT
	 */
	public String getKEY_ACNT() {
		return KEY_ACNT;
	}
	/**
	 * @param key_acnt the kEY_ACNT to set
	 */
	public void setKEY_ACNT(String key_acnt) {
		KEY_ACNT = key_acnt;
	}
	/**
	 * @return the fANO
	 */
	public String getFANO() {
		return FANO;
	}
	/**
	 * @param fano the fANO to set
	 */
	public void setFANO(String fano) {
		FANO = fano;
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
	 * @return the aUTH_APPL_ID
	 */
	public String getAUTH_APPL_ID() {
		return AUTH_APPL_ID;
	}
	/**
	 * @param auth_appl_id the aUTH_APPL_ID to set
	 */
	public void setAUTH_APPL_ID(String auth_appl_id) {
		AUTH_APPL_ID = auth_appl_id;
	}
	/**
	 * @return the aUTH_VERB
	 */
	public String getAUTH_VERB() {
		return AUTH_VERB;
	}
	/**
	 * @param auth_verb the aUTH_VERB to set
	 */
	public void setAUTH_VERB(String auth_verb) {
		AUTH_VERB = auth_verb;
	}
	/**
	 * @return the wEB_SERVER_ID
	 */
	public String getWEB_SERVER_ID() {
		return WEB_SERVER_ID;
	}
	/**
	 * @param web_server_id the wEB_SERVER_ID to set
	 */
	public void setWEB_SERVER_ID(String web_server_id) {
		WEB_SERVER_ID = web_server_id;
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
	    
	    retValue = "MS_MERLIN_INP ( "
	        + super.toString() + TAB
	        + "RMRKS_L01 = " + this.RMRKS_L01 + TAB
	        + "OFFICE_ACNT = " + this.OFFICE_ACNT + TAB
	        + "ACNT_NUM = " + this.ACNT_NUM + TAB
	        + "KEY_ACNT = " + this.KEY_ACNT + TAB
	        + "FANO = " + this.FANO + TAB
	        + "EXT_STR = " + this.EXT_STR + TAB
	        + "EXT_NUM = " + this.EXT_NUM + TAB
	        + "USER_ID = " + this.USER_ID + TAB
	        + "APPL_ID = " + this.APPL_ID + TAB
	        + "AUTH_APPL_ID = " + this.AUTH_APPL_ID + TAB
	        + "AUTH_VERB = " + this.AUTH_VERB + TAB
	        + "WEB_SERVER_ID = " + this.WEB_SERVER_ID + TAB
	        + " )";
	
	    return retValue;
	}
	
}