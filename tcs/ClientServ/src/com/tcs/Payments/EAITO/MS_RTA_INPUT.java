package com.tcs.Payments.EAITO;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_RTA_INPUT implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7273459065246718956L;
	private String REFERENCE_KEY;
	private String USER_ID;
	private Double IN_STATUS;
	private Double ACTION_KEY;
	private Timestamp ENTRY_DATE;
	private Timestamp TRADE_DATE;
	private Timestamp PROCESS_DATE;
	private Timestamp SETTLE_DATE;
	private Timestamp EXECUTION_TIME;
	private BigDecimal NET_AMT;
	private Double TRAN_CODE;
	private Double TRAN_ALPHA;
	private Double TRAN_ALPHA_SUB;
	private String JRNL_DESC;
	private String SRC_APPL_REF;
	private String FROM_OFFICE;
	private String FROM_ACCT;
	private String FROM_FA_NBR;
	private Double FROM_ACCT_TYPE;
	private String TO_OFFICE;
	private String TO_ACCT;
	private String TO_FA_NBR;
	private Double TO_ACCT_TYPE;
	private String FROM_ACCT_TRLR_1;
	private String FROM_ACCT_TRLR_2;
	private String TO_ACCT_TRLR_1;
	private String TO_ACCT_TRLR_2;
	private Double TXN_TYP;
	private String EXT_STR;
	private String EXT_STR2;
	private Double EXT_NUM;
	/**
	 * @return the rEFERENCE_KEY
	 */
	public String getREFERENCE_KEY() {
		return REFERENCE_KEY;
	}
	/**
	 * @param reference_key the rEFERENCE_KEY to set
	 */
	public void setREFERENCE_KEY(String reference_key) {
		REFERENCE_KEY = reference_key;
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
	 * @return the iN_STATUS
	 */
	public Double getIN_STATUS() {
		return IN_STATUS;
	}
	/**
	 * @param in_status the iN_STATUS to set
	 */
	public void setIN_STATUS(Double in_status) {
		IN_STATUS = in_status;
	}
	/**
	 * @return the aCTION_KEY
	 */
	public Double getACTION_KEY() {
		return ACTION_KEY;
	}
	/**
	 * @param action_key the aCTION_KEY to set
	 */
	public void setACTION_KEY(Double action_key) {
		ACTION_KEY = action_key;
	}
	/**
	 * @return the eNTRY_DATE
	 */
	public Timestamp getENTRY_DATE() {
		return ENTRY_DATE;
	}
	/**
	 * @param entry_date the eNTRY_DATE to set
	 */
	public void setENTRY_DATE(Timestamp entry_date) {
		ENTRY_DATE = entry_date;
	}
	/**
	 * @return the tRADE_DATE
	 */
	public Timestamp getTRADE_DATE() {
		return TRADE_DATE;
	}
	/**
	 * @param trade_date the tRADE_DATE to set
	 */
	public void setTRADE_DATE(Timestamp trade_date) {
		TRADE_DATE = trade_date;
	}
	/**
	 * @return the pROCESS_DATE
	 */
	public Timestamp getPROCESS_DATE() {
		return PROCESS_DATE;
	}
	/**
	 * @param process_date the pROCESS_DATE to set
	 */
	public void setPROCESS_DATE(Timestamp process_date) {
		PROCESS_DATE = process_date;
	}
	/**
	 * @return the sETTLE_DATE
	 */
	public Timestamp getSETTLE_DATE() {
		return SETTLE_DATE;
	}
	/**
	 * @param settle_date the sETTLE_DATE to set
	 */
	public void setSETTLE_DATE(Timestamp settle_date) {
		SETTLE_DATE = settle_date;
	}
	/**
	 * @return the eXECUTION_TIME
	 */
	public Timestamp getEXECUTION_TIME() {
		return EXECUTION_TIME;
	}
	/**
	 * @param execution_time the eXECUTION_TIME to set
	 */
	public void setEXECUTION_TIME(Timestamp execution_time) {
		EXECUTION_TIME = execution_time;
	}
	/**
	 * @return the nET_AMT
	 */
	public BigDecimal getNET_AMT() {
		return NET_AMT;
	}
	/**
	 * @param net_amt the nET_AMT to set
	 */
	public void setNET_AMT(BigDecimal net_amt) {
		NET_AMT = net_amt;
	}
	/**
	 * @return the tRAN_CODE
	 */
	public Double getTRAN_CODE() {
		return TRAN_CODE;
	}
	/**
	 * @param tran_code the tRAN_CODE to set
	 */
	public void setTRAN_CODE(Double tran_code) {
		TRAN_CODE = tran_code;
	}
	/**
	 * @return the tRAN_ALPHA
	 */
	public Double getTRAN_ALPHA() {
		return TRAN_ALPHA;
	}
	/**
	 * @param tran_alpha the tRAN_ALPHA to set
	 */
	public void setTRAN_ALPHA(Double tran_alpha) {
		TRAN_ALPHA = tran_alpha;
	}
	/**
	 * @return the tRAN_ALPHA_SUB
	 */
	public Double getTRAN_ALPHA_SUB() {
		return TRAN_ALPHA_SUB;
	}
	/**
	 * @param tran_alpha_sub the tRAN_ALPHA_SUB to set
	 */
	public void setTRAN_ALPHA_SUB(Double tran_alpha_sub) {
		TRAN_ALPHA_SUB = tran_alpha_sub;
	}
	/**
	 * @return the jRNL_DESC
	 */
	public String getJRNL_DESC() {
		return JRNL_DESC;
	}
	/**
	 * @param jrnl_desc the jRNL_DESC to set
	 */
	public void setJRNL_DESC(String jrnl_desc) {
		JRNL_DESC = jrnl_desc;
	}
	/**
	 * @return the sRC_APPL_REF
	 */
	public String getSRC_APPL_REF() {
		return SRC_APPL_REF;
	}
	/**
	 * @param src_appl_ref the sRC_APPL_REF to set
	 */
	public void setSRC_APPL_REF(String src_appl_ref) {
		SRC_APPL_REF = src_appl_ref;
	}
	/**
	 * @return the fROM_OFFICE
	 */
	public String getFROM_OFFICE() {
		return FROM_OFFICE;
	}
	/**
	 * @param from_office the fROM_OFFICE to set
	 */
	public void setFROM_OFFICE(String from_office) {
		FROM_OFFICE = from_office;
	}
	/**
	 * @return the fROM_ACCT
	 */
	public String getFROM_ACCT() {
		return FROM_ACCT;
	}
	/**
	 * @param from_acct the fROM_ACCT to set
	 */
	public void setFROM_ACCT(String from_acct) {
		FROM_ACCT = from_acct;
	}
	/**
	 * @return the fROM_FA_NBR
	 */
	public String getFROM_FA_NBR() {
		return FROM_FA_NBR;
	}
	/**
	 * @param from_fa_nbr the fROM_FA_NBR to set
	 */
	public void setFROM_FA_NBR(String from_fa_nbr) {
		FROM_FA_NBR = from_fa_nbr;
	}
	/**
	 * @return the fROM_ACCT_TYPE
	 */
	public Double getFROM_ACCT_TYPE() {
		return FROM_ACCT_TYPE;
	}
	/**
	 * @param from_acct_type the fROM_ACCT_TYPE to set
	 */
	public void setFROM_ACCT_TYPE(Double from_acct_type) {
		FROM_ACCT_TYPE = from_acct_type;
	}
	/**
	 * @return the tO_OFFICE
	 */
	public String getTO_OFFICE() {
		return TO_OFFICE;
	}
	/**
	 * @param to_office the tO_OFFICE to set
	 */
	public void setTO_OFFICE(String to_office) {
		TO_OFFICE = to_office;
	}
	/**
	 * @return the tO_ACCT
	 */
	public String getTO_ACCT() {
		return TO_ACCT;
	}
	/**
	 * @param to_acct the tO_ACCT to set
	 */
	public void setTO_ACCT(String to_acct) {
		TO_ACCT = to_acct;
	}
	/**
	 * @return the tO_FA_NBR
	 */
	public String getTO_FA_NBR() {
		return TO_FA_NBR;
	}
	/**
	 * @param to_fa_nbr the tO_FA_NBR to set
	 */
	public void setTO_FA_NBR(String to_fa_nbr) {
		TO_FA_NBR = to_fa_nbr;
	}
	/**
	 * @return the tO_ACCT_TYPE
	 */
	public Double getTO_ACCT_TYPE() {
		return TO_ACCT_TYPE;
	}
	/**
	 * @param to_acct_type the tO_ACCT_TYPE to set
	 */
	public void setTO_ACCT_TYPE(Double to_acct_type) {
		TO_ACCT_TYPE = to_acct_type;
	}
	/**
	 * @return the fROM_ACCT_TRLR_1
	 */
	public String getFROM_ACCT_TRLR_1() {
		return FROM_ACCT_TRLR_1;
	}
	/**
	 * @param from_acct_trlr_1 the fROM_ACCT_TRLR_1 to set
	 */
	public void setFROM_ACCT_TRLR_1(String from_acct_trlr_1) {
		FROM_ACCT_TRLR_1 = from_acct_trlr_1;
	}
	/**
	 * @return the fROM_ACCT_TRLR_2
	 */
	public String getFROM_ACCT_TRLR_2() {
		return FROM_ACCT_TRLR_2;
	}
	/**
	 * @param from_acct_trlr_2 the fROM_ACCT_TRLR_2 to set
	 */
	public void setFROM_ACCT_TRLR_2(String from_acct_trlr_2) {
		FROM_ACCT_TRLR_2 = from_acct_trlr_2;
	}
	/**
	 * @return the tO_ACCT_TRLR_1
	 */
	public String getTO_ACCT_TRLR_1() {
		return TO_ACCT_TRLR_1;
	}
	/**
	 * @param to_acct_trlr_1 the tO_ACCT_TRLR_1 to set
	 */
	public void setTO_ACCT_TRLR_1(String to_acct_trlr_1) {
		TO_ACCT_TRLR_1 = to_acct_trlr_1;
	}
	/**
	 * @return the tO_ACCT_TRLR_2
	 */
	public String getTO_ACCT_TRLR_2() {
		return TO_ACCT_TRLR_2;
	}
	/**
	 * @param to_acct_trlr_2 the tO_ACCT_TRLR_2 to set
	 */
	public void setTO_ACCT_TRLR_2(String to_acct_trlr_2) {
		TO_ACCT_TRLR_2 = to_acct_trlr_2;
	}
	/**
	 * @return the tXN_TYP
	 */
	public Double getTXN_TYP() {
		return TXN_TYP;
	}
	/**
	 * @param txn_typ the tXN_TYP to set
	 */
	public void setTXN_TYP(Double txn_typ) {
		TXN_TYP = txn_typ;
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
	 * @return the eXT_STR2
	 */
	public String getEXT_STR2() {
		return EXT_STR2;
	}
	/**
	 * @param ext_str2 the eXT_STR2 to set
	 */
	public void setEXT_STR2(String ext_str2) {
		EXT_STR2 = ext_str2;
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

		retValue = "MS_RTA_INPUT ( "
			+ super.toString() + TAB
			+ "REFERENCE_KEY = " + this.REFERENCE_KEY + TAB
			+ "USER_ID = " + this.USER_ID + TAB
			+ "IN_STATUS = " + this.IN_STATUS + TAB
			+ "ACTION_KEY = " + this.ACTION_KEY + TAB
			+ "ENTRY_DATE = " + this.ENTRY_DATE + TAB
			+ "TRADE_DATE = " + this.TRADE_DATE + TAB
			+ "PROCESS_DATE = " + this.PROCESS_DATE + TAB
			+ "SETTLE_DATE = " + this.SETTLE_DATE + TAB
			+ "EXECUTION_TIME = " + this.EXECUTION_TIME + TAB
			+ "NET_AMT = " + this.NET_AMT + TAB
			+ "TRAN_CODE = " + this.TRAN_CODE + TAB
			+ "TRAN_ALPHA = " + this.TRAN_ALPHA + TAB
			+ "TRAN_ALPHA_SUB = " + this.TRAN_ALPHA_SUB + TAB
			+ "JRNL_DESC = " + this.JRNL_DESC + TAB
			+ "SRC_APPL_REF = " + this.SRC_APPL_REF + TAB
			+ "FROM_OFFICE = " + this.FROM_OFFICE + TAB
			+ "FROM_ACCT = " + this.FROM_ACCT + TAB
			+ "FROM_FA_NBR = " + this.FROM_FA_NBR + TAB
			+ "FROM_ACCT_TYPE = " + this.FROM_ACCT_TYPE + TAB
			+ "TO_OFFICE = " + this.TO_OFFICE + TAB
			+ "TO_ACCT = " + this.TO_ACCT + TAB
			+ "TO_FA_NBR = " + this.TO_FA_NBR + TAB
			+ "TO_ACCT_TYPE = " + this.TO_ACCT_TYPE + TAB
			+ "FROM_ACCT_TRLR_1 = " + this.FROM_ACCT_TRLR_1 + TAB
			+ "FROM_ACCT_TRLR_2 = " + this.FROM_ACCT_TRLR_2 + TAB
			+ "TO_ACCT_TRLR_1 = " + this.TO_ACCT_TRLR_1 + TAB
			+ "TO_ACCT_TRLR_2 = " + this.TO_ACCT_TRLR_2 + TAB
			+ "TXN_TYP = " + this.TXN_TYP + TAB
			+ "EXT_STR = " + this.EXT_STR + TAB
			+ "EXT_STR2 = " + this.EXT_STR2 + TAB
			+ "EXT_NUM = " + this.EXT_NUM + TAB
			+ " )";

		return retValue;
	}

}