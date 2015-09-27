package com.tcs.Payments.EAITO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class BUS_RULE_INP_CRTL implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1382269308237539540L;
	private String UUID;
	private String GROUP;
	private String ROLE;
	private int ELIGIBLE_ACC_CHECK;
	private String RSA_REQUEST_FLAG; 
	private String RSA_REVIEW_FLAG;
	private String EXT_STR;
	private int EXT_NUM;
	private String LIMIT_CHECK_REQ;
	private String APPL_ID;
	private String SERVER_IP;
	private String USER_ID;
	private String TXN_USER_ID;
	private String CLIENT_IDENTFIER;

	/**
	 * @return the uUID
	 */
	public String getUUID() {
		return UUID;
	}
	/**
	 * @param uuid the uUID to set
	 */
	public void setUUID(String uuid) {
		UUID = uuid;
	}
	/**
	 * @return the gROUP
	 */
	public String getGROUP() {
		return GROUP;
	}
	/**
	 * @param group the gROUP to set
	 */
	public void setGROUP(String group) {
		GROUP = group;
	}
	/**
	 * @return the rOLE
	 */
	public String getROLE() {
		return ROLE;
	}
	/**
	 * @param role the rOLE to set
	 */
	public void setROLE(String role) {
		ROLE = role;
	}
	/**
	 * @return the eLIGIBLE_ACC_CHECK
	 */
	public int getELIGIBLE_ACC_CHECK() {
		return ELIGIBLE_ACC_CHECK;
	}
	/**
	 * @param eligible_acc_check the eLIGIBLE_ACC_CHECK to set
	 */
	public void setELIGIBLE_ACC_CHECK(int eligible_acc_check) {
		ELIGIBLE_ACC_CHECK = eligible_acc_check;
	}
	/**
	 * @return the rSA_REQUEST_FLAG
	 */
	public String getRSA_REQUEST_FLAG() {
		return RSA_REQUEST_FLAG;
	}
	/**
	 * @param rsa_request_flag the rSA_REQUEST_FLAG to set
	 */
	public void setRSA_REQUEST_FLAG(String rsa_request_flag) {
		RSA_REQUEST_FLAG = rsa_request_flag;
	}
	/**
	 * @return the rSA_REVIEW_FLAG
	 */
	public String getRSA_REVIEW_FLAG() {
		return RSA_REVIEW_FLAG;
	}
	/**
	 * @param rsa_review_flag the rSA_REVIEW_FLAG to set
	 */
	public void setRSA_REVIEW_FLAG(String rsa_review_flag) {
		RSA_REVIEW_FLAG = rsa_review_flag;
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
	public int getEXT_NUM() {
		return EXT_NUM;
	}
	/**
	 * @param ext_num the eXT_NUM to set
	 */
	public void setEXT_NUM(int ext_num) {
		EXT_NUM = ext_num;
	}
	/**
	 * @return the lIMIT_CHECK_REQ
	 */
	public String getLIMIT_CHECK_REQ() {
		return LIMIT_CHECK_REQ;
	}
	/**
	 * @param limit_check_req the lIMIT_CHECK_REQ to set
	 */
	public void setLIMIT_CHECK_REQ(String limit_check_req) {
		LIMIT_CHECK_REQ = limit_check_req;
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
	 * @return the sERVER_IP
	 */
	public String getSERVER_IP() {
		return SERVER_IP;
	}
	/**
	 * @param server_ip the sERVER_IP to set
	 */
	public void setSERVER_IP(String server_ip) {
		SERVER_IP = server_ip;
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
	 * @return the tXN_USER_ID
	 */
	public String getTXN_USER_ID() {
		return TXN_USER_ID;
	}
	/**
	 * @param txn_user_id the tXN_USER_ID to set
	 */
	public void setTXN_USER_ID(String txn_user_id) {
		TXN_USER_ID = txn_user_id;
	}
	/**
	 * @return the cLIENT_IDENTFIER
	 */
	public String getCLIENT_IDENTFIER() {
		return CLIENT_IDENTFIER;
	}
	/**
	 * @param client_identfier the cLIENT_IDENTFIER to set
	 */
	public void setCLIENT_IDENTFIER(String client_identfier) {
		CLIENT_IDENTFIER = client_identfier;
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

		retValue = "BUS_RULE_INP_CRTL ( "
			+ super.toString() + TAB
			+ "UUID = " + this.UUID + TAB
			+ "GROUP = " + this.GROUP + TAB
			+ "ROLE = " + this.ROLE + TAB
			+ "ELIGIBLE_ACC_CHECK = " + this.ELIGIBLE_ACC_CHECK + TAB
			+ "RSA_REQUEST_FLAG = " + this.RSA_REQUEST_FLAG + TAB
			+ "RSA_REVIEW_FLAG = " + this.RSA_REVIEW_FLAG + TAB
			+ "EXT_STR = " + this.EXT_STR + TAB
			+ "EXT_NUM = " + this.EXT_NUM + TAB
			+ "LIMIT_CHECK_REQ = " + this.LIMIT_CHECK_REQ + TAB
			+ "APPL_ID = " + this.APPL_ID + TAB
			+ "SERVER_IP = " + this.SERVER_IP + TAB
			+ "USER_ID = " + this.USER_ID + TAB
			+ "TXN_USER_ID = " + this.TXN_USER_ID + TAB
			+ "CLIENT_IDENTFIER = " + this.CLIENT_IDENTFIER + TAB
			+ " )";

		return retValue;
	}

}