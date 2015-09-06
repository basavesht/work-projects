package com.tcs.Payments.EAITO;

import java.sql.Timestamp;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class BR_VALIDATION_LOG implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2290139796739170669L;
	private String BO_KEY = null;
	private long SQNC_NUM;
	private long RULE_SET_ID;
	private String RULE_TYPE = null;
	private long RULE_ID;
	private String RULE_NAME = null;
	private long RULE_PRIORITY; 
	private long ACTION_ID;
	private String APPROVER_ROLE = null;
	private Integer APRROVER_PRIORITY;
	private long APPRVD_FLG;
	private Timestamp DATE_OF_INSERTION;
	private long IS_VALID;
	private String ERR_LVL = null;
	private String USR_GRP = null;
	private String ERRMESG = null;
	private int CANCEL_TXN_FLG;
	private String ACC_IN_ELIGIBLE = null;
	private String RSA_REVIEW_FLAG = null;
	private int ERRORID;
	private String APPRVD_BY = null;
	private int RECENT_RUN;
	private String AUTH_MODE = null;
	private String DONT_SPAWN_FLAG = null;
	private String AUTH_REQ_FLAG = null;
	private String DOCUMENTS_REQ = null;
	private String OFAC_CASE_ID = null;

	/**
	 * @return the oFAC_CASE_ID
	 */
	public String getOFAC_CASE_ID() {
		return OFAC_CASE_ID;
	}
	/**
	 * @param ofac_case_id the oFAC_CASE_ID to set
	 */
	public void setOFAC_CASE_ID(String ofac_case_id) {
		OFAC_CASE_ID = ofac_case_id;
	}
	/**
	 * @return the bO_KEY
	 */
	public String getBO_KEY() {
		return BO_KEY;
	}
	/**
	 * @param bo_key the bO_KEY to set
	 */
	public void setBO_KEY(String bo_key) {
		BO_KEY = bo_key;
	}
	/**
	 * @return the sQNC_NUM
	 */
	public long getSQNC_NUM() {
		return SQNC_NUM;
	}
	/**
	 * @param sqnc_num the sQNC_NUM to set
	 */
	public void setSQNC_NUM(long sqnc_num) {
		SQNC_NUM = sqnc_num;
	}
	/**
	 * @return the rULE_SET_ID
	 */
	public long getRULE_SET_ID() {
		return RULE_SET_ID;
	}
	/**
	 * @param rule_set_id the rULE_SET_ID to set
	 */
	public void setRULE_SET_ID(long rule_set_id) {
		RULE_SET_ID = rule_set_id;
	}
	/**
	 * @return the rULE_TYPE
	 */
	public String getRULE_TYPE() {
		return RULE_TYPE;
	}
	/**
	 * @param rule_type the rULE_TYPE to set
	 */
	public void setRULE_TYPE(String rule_type) {
		RULE_TYPE = rule_type;
	}
	/**
	 * @return the rULE_ID
	 */
	public long getRULE_ID() {
		return RULE_ID;
	}
	/**
	 * @param rule_id the rULE_ID to set
	 */
	public void setRULE_ID(long rule_id) {
		RULE_ID = rule_id;
	}
	/**
	 * @return the rULE_NAME
	 */
	public String getRULE_NAME() {
		return RULE_NAME;
	}
	/**
	 * @param rule_name the rULE_NAME to set
	 */
	public void setRULE_NAME(String rule_name) {
		RULE_NAME = rule_name;
	}
	/**
	 * @return the rULE_PRIORITY
	 */
	public long getRULE_PRIORITY() {
		return RULE_PRIORITY;
	}
	/**
	 * @param rule_priority the rULE_PRIORITY to set
	 */
	public void setRULE_PRIORITY(long rule_priority) {
		RULE_PRIORITY = rule_priority;
	}
	/**
	 * @return the aCTION_ID
	 */
	public long getACTION_ID() {
		return ACTION_ID;
	}
	/**
	 * @param action_id the aCTION_ID to set
	 */
	public void setACTION_ID(long action_id) {
		ACTION_ID = action_id;
	}
	/**
	 * @return the aPPROVER_ROLE
	 */
	public String getAPPROVER_ROLE() {
		return APPROVER_ROLE;
	}
	/**
	 * @param approver_role the aPPROVER_ROLE to set
	 */
	public void setAPPROVER_ROLE(String approver_role) {
		APPROVER_ROLE = approver_role;
	}
	/**
	 * @return the aPRROVER_PRIORITY
	 */
	public Integer getAPRROVER_PRIORITY() {
		return APRROVER_PRIORITY;
	}
	/**
	 * @param aprrover_priority the aPRROVER_PRIORITY to set
	 */
	public void setAPRROVER_PRIORITY(Integer aprrover_priority) {
		APRROVER_PRIORITY = aprrover_priority;
	}
	/**
	 * @return the aPPRVD_FLG
	 */
	public long getAPPRVD_FLG() {
		return APPRVD_FLG;
	}
	/**
	 * @param apprvd_flg the aPPRVD_FLG to set
	 */
	public void setAPPRVD_FLG(long apprvd_flg) {
		APPRVD_FLG = apprvd_flg;
	}
	/**
	 * @return the dATE_OF_INSERTION
	 */
	public Timestamp getDATE_OF_INSERTION() {
		return DATE_OF_INSERTION;
	}
	/**
	 * @param date_of_insertion the dATE_OF_INSERTION to set
	 */
	public void setDATE_OF_INSERTION(Timestamp date_of_insertion) {
		DATE_OF_INSERTION = date_of_insertion;
	}
	/**
	 * @return the iS_VALID
	 */
	public long getIS_VALID() {
		return IS_VALID;
	}
	/**
	 * @param is_valid the iS_VALID to set
	 */
	public void setIS_VALID(long is_valid) {
		IS_VALID = is_valid;
	}
	/**
	 * @return the eRR_LVL
	 */
	public String getERR_LVL() {
		return ERR_LVL;
	}
	/**
	 * @param err_lvl the eRR_LVL to set
	 */
	public void setERR_LVL(String err_lvl) {
		ERR_LVL = err_lvl;
	}
	/**
	 * @return the uSR_GRP
	 */
	public String getUSR_GRP() {
		return USR_GRP;
	}
	/**
	 * @param usr_grp the uSR_GRP to set
	 */
	public void setUSR_GRP(String usr_grp) {
		USR_GRP = usr_grp;
	}
	/**
	 * @return the eRRMESG
	 */
	public String getERRMESG() {
		return ERRMESG;
	}
	/**
	 * @param errmesg the eRRMESG to set
	 */
	public void setERRMESG(String errmesg) {
		ERRMESG = errmesg;
	}
	/**
	 * @return the cANCEL_TXN_FLG
	 */
	public int getCANCEL_TXN_FLG() {
		return CANCEL_TXN_FLG;
	}
	/**
	 * @param cancel_txn_flg the cANCEL_TXN_FLG to set
	 */
	public void setCANCEL_TXN_FLG(int cancel_txn_flg) {
		CANCEL_TXN_FLG = cancel_txn_flg;
	}
	/**
	 * @return the aCC_IN_ELIGIBLE
	 */
	public String getACC_IN_ELIGIBLE() {
		return ACC_IN_ELIGIBLE;
	}
	/**
	 * @param acc_in_eligible the aCC_IN_ELIGIBLE to set
	 */
	public void setACC_IN_ELIGIBLE(String acc_in_eligible) {
		ACC_IN_ELIGIBLE = acc_in_eligible;
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
	 * @return the eRRORID
	 */
	public int getERRORID() {
		return ERRORID;
	}
	/**
	 * @param errorid the eRRORID to set
	 */
	public void setERRORID(int errorid) {
		ERRORID = errorid;
	}
	/**
	 * @return the aPPRVD_BY
	 */
	public String getAPPRVD_BY() {
		return APPRVD_BY;
	}
	/**
	 * @param apprvd_by the aPPRVD_BY to set
	 */
	public void setAPPRVD_BY(String apprvd_by) {
		APPRVD_BY = apprvd_by;
	}
	/**
	 * @return the rECENT_RUN
	 */
	public int getRECENT_RUN() {
		return RECENT_RUN;
	}
	/**
	 * @param recent_run the rECENT_RUN to set
	 */
	public void setRECENT_RUN(int recent_run) {
		RECENT_RUN = recent_run;
	}
	/**
	 * @return the aUTH_MODE
	 */
	public String getAUTH_MODE() {
		return AUTH_MODE;
	}
	/**
	 * @param auth_mode the aUTH_MODE to set
	 */
	public void setAUTH_MODE(String auth_mode) {
		AUTH_MODE = auth_mode;
	}
	/**
	 * @return the dONT_SPAWN_FLAG
	 */
	public String getDONT_SPAWN_FLAG() {
		return DONT_SPAWN_FLAG;
	}
	/**
	 * @param dont_spawn_flag the dONT_SPAWN_FLAG to set
	 */
	public void setDONT_SPAWN_FLAG(String dont_spawn_flag) {
		DONT_SPAWN_FLAG = dont_spawn_flag;
	}
	/**
	 * @return the aUTH_REQ_FLAG
	 */
	public String getAUTH_REQ_FLAG() {
		return AUTH_REQ_FLAG;
	}
	/**
	 * @param auth_req_flag the aUTH_REQ_FLAG to set
	 */
	public void setAUTH_REQ_FLAG(String auth_req_flag) {
		AUTH_REQ_FLAG = auth_req_flag;
	}
	/**
	 * @return the dOCUMENTS_REQ
	 */
	public String getDOCUMENTS_REQ() {
		return DOCUMENTS_REQ;
	}
	/**
	 * @param documents_req the dOCUMENTS_REQ to set
	 */
	public void setDOCUMENTS_REQ(String documents_req) {
		DOCUMENTS_REQ = documents_req;
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

		retValue = "BR_VALIDATION_LOG ( "
			+ super.toString() + TAB
			+ "BO_KEY = " + this.BO_KEY + TAB
			+ "SQNC_NUM = " + this.SQNC_NUM + TAB
			+ "RULE_SET_ID = " + this.RULE_SET_ID + TAB
			+ "RULE_TYPE = " + this.RULE_TYPE + TAB
			+ "RULE_ID = " + this.RULE_ID + TAB
			+ "RULE_NAME = " + this.RULE_NAME + TAB
			+ "RULE_PRIORITY = " + this.RULE_PRIORITY + TAB
			+ "ACTION_ID = " + this.ACTION_ID + TAB
			+ "APPROVER_ROLE = " + this.APPROVER_ROLE + TAB
			+ "APRROVER_PRIORITY = " + this.APRROVER_PRIORITY + TAB
			+ "APPRVD_FLG = " + this.APPRVD_FLG + TAB
			+ "DATE_OF_INSERTION = " + this.DATE_OF_INSERTION + TAB
			+ "IS_VALID = " + this.IS_VALID + TAB
			+ "ERR_LVL = " + this.ERR_LVL + TAB
			+ "USR_GRP = " + this.USR_GRP + TAB
			+ "ERRMESG = " + this.ERRMESG + TAB
			+ "CANCEL_TXN_FLG = " + this.CANCEL_TXN_FLG + TAB
			+ "ACC_IN_ELIGIBLE = " + this.ACC_IN_ELIGIBLE + TAB
			+ "RSA_REVIEW_FLAG = " + this.RSA_REVIEW_FLAG + TAB
			+ "ERRORID = " + this.ERRORID + TAB
			+ "APPRVD_BY = " + this.APPRVD_BY + TAB
			+ "RECENT_RUN = " + this.RECENT_RUN + TAB
			+ "AUTH_MODE = " + this.AUTH_MODE + TAB
			+ "DONT_SPAWN_FLAG = " + this.DONT_SPAWN_FLAG + TAB
			+ "AUTH_REQ_FLAG = " + this.AUTH_REQ_FLAG + TAB
			+ "DOCUMENTS_REQ = " + this.DOCUMENTS_REQ + TAB
			+ "OFAC_CASE_ID = " + this.OFAC_CASE_ID + TAB
			+ " )";

		return retValue;
	}
}
