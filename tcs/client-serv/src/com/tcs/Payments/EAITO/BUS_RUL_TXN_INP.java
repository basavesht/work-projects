package com.tcs.Payments.EAITO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class BUS_RUL_TXN_INP implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6173885273737248791L;
	private String TYPE;
	private BigDecimal AMT;
	private String STATE;
	private Date EXE_DATE;
	private Date BUS_DATE;
	private String ACTION;
	private String RULE_TYPE;
	private String CONFIRM_NUM;
	private String TXN_TYPE=null;
	private String TIER;
	private String DEBIT_ACCOUNT;
	private int CREATE_FLG;
	private int UPDATE_FLG;
	private int DELETE_FLG;
	private BigDecimal ORGNL_TXN_AMT;
	private BigDecimal NEW_TXN_AMT;
	private Date ORGNL_TXN_DATE;
	private Date NEW_TXN_DATE;
	private String EXT_STR;
	private int EXT_NUM;
	private String CURRENT_APPRVER;
	private String MODE;
	private String CHECK_PICKUP_ADDR_STAT;
	private String MS360_SAME_NAME;
	private String AUTH_MODE;
	private String CHARGE_TO;
	private BigDecimal UTILIZED_AMNT;
	private BigDecimal FEE;
	private String PAGE_TYPE ;
	private String TXN_SUB_TYPE ;
	private String FREQUENCY ;
	private String PICKUP_OPTION ;
	private String CERTIFIED ;
	private String DFLT_CHK_PCKP_ADDR ;
	private String PRINT_AT_BRANCH ;
	private String IRA_TXN_IND ;
	private BigDecimal UN_EDITED_AMT ;
	private String REPEAT;
	private String UNTIL_END_DATE;
	private BigDecimal UNTIL_DOLLAR_AMT;
	private Integer UNTIL_NO_OF_TXNS;
	private Integer TRANSFERS_DONE_TILL_DT;
	private BigDecimal AMT_TRNSFRD_TILL_DT;
	private String IRA_OPTION;
	private BigDecimal UN_EDITED_FEE;
	private String UN_EDITED_FEE_CHARGED_TO;
	private String PAYEE_NAME;
	private String TIRD_PARTY_REASON;
	private String EST_PICKUP_TIME;
	private String PRINT_ADDR_ON_CHECK;
	private String ADDR_LINE1;
	private String ADDR_LINE2;
	private String ADDR_LINE3;
	private String ADDR_LINE4;
	private String MEMO_LINE1;
	private String MEMO_LINE2;
	private String PRINT_MEMO_ON_CHECK;
	private String PRINT_MEMO_ON_STUB;
	private String QUALIFIER;
	private String REVERSE_QUALIFIER;
	private String SPOKE_TO_TYPE;
	private String SPOKE_TO_NAME;

	/**
	 * @return the tYPE
	 */
	public String getTYPE() {
		return TYPE;
	}

	/**
	 * @param type the tYPE to set
	 */
	public void setTYPE(String type) {
		TYPE = type;
	}

	/**
	 * @return the aMT
	 */
	public BigDecimal getAMT() {
		return AMT;
	}

	/**
	 * @param amt the aMT to set
	 */
	public void setAMT(BigDecimal amt) {
		AMT = amt;
	}

	/**
	 * @return the sTATE
	 */
	public String getSTATE() {
		return STATE;
	}

	/**
	 * @param state the sTATE to set
	 */
	public void setSTATE(String state) {
		STATE = state;
	}

	/**
	 * @return the eXE_DATE
	 */
	public Date getEXE_DATE() {
		return EXE_DATE;
	}

	/**
	 * @param exe_date the eXE_DATE to set
	 */
	public void setEXE_DATE(Date exe_date) {
		EXE_DATE = exe_date;
	}

	/**
	 * @return the bUS_DATE
	 */
	public Date getBUS_DATE() {
		return BUS_DATE;
	}

	/**
	 * @param bus_date the bUS_DATE to set
	 */
	public void setBUS_DATE(Date bus_date) {
		BUS_DATE = bus_date;
	}

	/**
	 * @return the aCTION
	 */
	public String getACTION() {
		return ACTION;
	}

	/**
	 * @param action the aCTION to set
	 */
	public void setACTION(String action) {
		ACTION = action;
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
	 * @return the cONFIRM_NUM
	 */
	public String getCONFIRM_NUM() {
		return CONFIRM_NUM;
	}

	/**
	 * @param confirm_num the cONFIRM_NUM to set
	 */
	public void setCONFIRM_NUM(String confirm_num) {
		CONFIRM_NUM = confirm_num;
	}

	/**
	 * @return the tXN_TYPE
	 */
	public String getTXN_TYPE() {
		return TXN_TYPE;
	}

	/**
	 * @param txn_type the tXN_TYPE to set
	 */
	public void setTXN_TYPE(String txn_type) {
		TXN_TYPE = txn_type;
	}

	/**
	 * @return the tIER
	 */
	public String getTIER() {
		return TIER;
	}

	/**
	 * @param tier the tIER to set
	 */
	public void setTIER(String tier) {
		TIER = tier;
	}

	/**
	 * @return the dEBIT_ACCOUNT
	 */
	public String getDEBIT_ACCOUNT() {
		return DEBIT_ACCOUNT;
	}

	/**
	 * @param debit_account the dEBIT_ACCOUNT to set
	 */
	public void setDEBIT_ACCOUNT(String debit_account) {
		DEBIT_ACCOUNT = debit_account;
	}

	/**
	 * @return the cREATE_FLG
	 */
	public int getCREATE_FLG() {
		return CREATE_FLG;
	}

	/**
	 * @param create_flg the cREATE_FLG to set
	 */
	public void setCREATE_FLG(int create_flg) {
		CREATE_FLG = create_flg;
	}

	/**
	 * @return the uPDATE_FLG
	 */
	public int getUPDATE_FLG() {
		return UPDATE_FLG;
	}

	/**
	 * @param update_flg the uPDATE_FLG to set
	 */
	public void setUPDATE_FLG(int update_flg) {
		UPDATE_FLG = update_flg;
	}

	/**
	 * @return the dELETE_FLG
	 */
	public int getDELETE_FLG() {
		return DELETE_FLG;
	}

	/**
	 * @param delete_flg the dELETE_FLG to set
	 */
	public void setDELETE_FLG(int delete_flg) {
		DELETE_FLG = delete_flg;
	}

	/**
	 * @return the oRGNL_TXN_AMT
	 */
	public BigDecimal getORGNL_TXN_AMT() {
		return ORGNL_TXN_AMT;
	}

	/**
	 * @param orgnl_txn_amt the oRGNL_TXN_AMT to set
	 */
	public void setORGNL_TXN_AMT(BigDecimal orgnl_txn_amt) {
		ORGNL_TXN_AMT = orgnl_txn_amt;
	}

	/**
	 * @return the nEW_TXN_AMT
	 */
	public BigDecimal getNEW_TXN_AMT() {
		return NEW_TXN_AMT;
	}

	/**
	 * @param new_txn_amt the nEW_TXN_AMT to set
	 */
	public void setNEW_TXN_AMT(BigDecimal new_txn_amt) {
		NEW_TXN_AMT = new_txn_amt;
	}

	/**
	 * @return the oRGNL_TXN_DATE
	 */
	public Date getORGNL_TXN_DATE() {
		return ORGNL_TXN_DATE;
	}

	/**
	 * @param orgnl_txn_date the oRGNL_TXN_DATE to set
	 */
	public void setORGNL_TXN_DATE(Date orgnl_txn_date) {
		ORGNL_TXN_DATE = orgnl_txn_date;
	}

	/**
	 * @return the nEW_TXN_DATE
	 */
	public Date getNEW_TXN_DATE() {
		return NEW_TXN_DATE;
	}

	/**
	 * @param new_txn_date the nEW_TXN_DATE to set
	 */
	public void setNEW_TXN_DATE(Date new_txn_date) {
		NEW_TXN_DATE = new_txn_date;
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
	 * @return the cURRENT_APPRVER
	 */
	public String getCURRENT_APPRVER() {
		return CURRENT_APPRVER;
	}

	/**
	 * @param current_apprver the cURRENT_APPRVER to set
	 */
	public void setCURRENT_APPRVER(String current_apprver) {
		CURRENT_APPRVER = current_apprver;
	}

	/**
	 * @return the mODE
	 */
	public String getMODE() {
		return MODE;
	}

	/**
	 * @param mode the mODE to set
	 */
	public void setMODE(String mode) {
		MODE = mode;
	}

	/**
	 * @return the cHECK_PICKUP_ADDR_STAT
	 */
	public String getCHECK_PICKUP_ADDR_STAT() {
		return CHECK_PICKUP_ADDR_STAT;
	}

	/**
	 * @param check_pickup_addr_stat the cHECK_PICKUP_ADDR_STAT to set
	 */
	public void setCHECK_PICKUP_ADDR_STAT(String check_pickup_addr_stat) {
		CHECK_PICKUP_ADDR_STAT = check_pickup_addr_stat;
	}

	/**
	 * @return the mS360_SAME_NAME
	 */
	public String getMS360_SAME_NAME() {
		return MS360_SAME_NAME;
	}

	/**
	 * @param ms360_same_name the mS360_SAME_NAME to set
	 */
	public void setMS360_SAME_NAME(String ms360_same_name) {
		MS360_SAME_NAME = ms360_same_name;
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
	 * @return the cHARGE_TO
	 */
	public String getCHARGE_TO() {
		return CHARGE_TO;
	}

	/**
	 * @param charge_to the cHARGE_TO to set
	 */
	public void setCHARGE_TO(String charge_to) {
		CHARGE_TO = charge_to;
	}

	/**
	 * @return the uTILIZED_AMNT
	 */
	public BigDecimal getUTILIZED_AMNT() {
		return UTILIZED_AMNT;
	}

	/**
	 * @param utilized_amnt the uTILIZED_AMNT to set
	 */
	public void setUTILIZED_AMNT(BigDecimal utilized_amnt) {
		UTILIZED_AMNT = utilized_amnt;
	}

	/**
	 * @return the fEE
	 */
	public BigDecimal getFEE() {
		return FEE;
	}

	/**
	 * @param fee the fEE to set
	 */
	public void setFEE(BigDecimal fee) {
		FEE = fee;
	}

	/**
	 * @return the pAGE_TYPE
	 */
	public String getPAGE_TYPE() {
		return PAGE_TYPE;
	}

	/**
	 * @param page_type the pAGE_TYPE to set
	 */
	public void setPAGE_TYPE(String page_type) {
		PAGE_TYPE = page_type;
	}

	/**
	 * @return the tXN_SUB_TYPE
	 */
	public String getTXN_SUB_TYPE() {
		return TXN_SUB_TYPE;
	}

	/**
	 * @param txn_sub_type the tXN_SUB_TYPE to set
	 */
	public void setTXN_SUB_TYPE(String txn_sub_type) {
		TXN_SUB_TYPE = txn_sub_type;
	}

	/**
	 * @return the fREQUENCY
	 */
	public String getFREQUENCY() {
		return FREQUENCY;
	}

	/**
	 * @param frequency the fREQUENCY to set
	 */
	public void setFREQUENCY(String frequency) {
		FREQUENCY = frequency;
	}

	/**
	 * @return the pICKUP_OPTION
	 */
	public String getPICKUP_OPTION() {
		return PICKUP_OPTION;
	}

	/**
	 * @param pickup_option the pICKUP_OPTION to set
	 */
	public void setPICKUP_OPTION(String pickup_option) {
		PICKUP_OPTION = pickup_option;
	}

	/**
	 * @return the cERTIFIED
	 */
	public String getCERTIFIED() {
		return CERTIFIED;
	}

	/**
	 * @param certified the cERTIFIED to set
	 */
	public void setCERTIFIED(String certified) {
		CERTIFIED = certified;
	}

	/**
	 * @return the dFLT_CHK_PCKP_ADDR
	 */
	public String getDFLT_CHK_PCKP_ADDR() {
		return DFLT_CHK_PCKP_ADDR;
	}

	/**
	 * @param dflt_chk_pckp_addr the dFLT_CHK_PCKP_ADDR to set
	 */
	public void setDFLT_CHK_PCKP_ADDR(String dflt_chk_pckp_addr) {
		DFLT_CHK_PCKP_ADDR = dflt_chk_pckp_addr;
	}

	/**
	 * @return the pRINT_AT_BRANCH
	 */
	public String getPRINT_AT_BRANCH() {
		return PRINT_AT_BRANCH;
	}

	/**
	 * @param print_at_branch the pRINT_AT_BRANCH to set
	 */
	public void setPRINT_AT_BRANCH(String print_at_branch) {
		PRINT_AT_BRANCH = print_at_branch;
	}

	/**
	 * @return the iRA_TXN_IND
	 */
	public String getIRA_TXN_IND() {
		return IRA_TXN_IND;
	}

	/**
	 * @param ira_txn_ind the iRA_TXN_IND to set
	 */
	public void setIRA_TXN_IND(String ira_txn_ind) {
		IRA_TXN_IND = ira_txn_ind;
	}

	/**
	 * @return the uN_EDITED_AMT
	 */
	public BigDecimal getUN_EDITED_AMT() {
		return UN_EDITED_AMT;
	}

	/**
	 * @param un_edited_amt the uN_EDITED_AMT to set
	 */
	public void setUN_EDITED_AMT(BigDecimal un_edited_amt) {
		UN_EDITED_AMT = un_edited_amt;
	}

	/**
	 * @return the rEPEAT
	 */
	public String getREPEAT() {
		return REPEAT;
	}

	/**
	 * @param repeat the rEPEAT to set
	 */
	public void setREPEAT(String repeat) {
		REPEAT = repeat;
	}

	/**
	 * @return the uNTIL_END_DATE
	 */
	public String getUNTIL_END_DATE() {
		return UNTIL_END_DATE;
	}

	/**
	 * @param until_end_date the uNTIL_END_DATE to set
	 */
	public void setUNTIL_END_DATE(String until_end_date) {
		UNTIL_END_DATE = until_end_date;
	}

	/**
	 * @return the uNTIL_DOLLAR_AMT
	 */
	public BigDecimal getUNTIL_DOLLAR_AMT() {
		return UNTIL_DOLLAR_AMT;
	}

	/**
	 * @param until_dollar_amt the uNTIL_DOLLAR_AMT to set
	 */
	public void setUNTIL_DOLLAR_AMT(BigDecimal until_dollar_amt) {
		UNTIL_DOLLAR_AMT = until_dollar_amt;
	}

	/**
	 * @return the uNTIL_NO_OF_TXNS
	 */
	public Integer getUNTIL_NO_OF_TXNS() {
		return UNTIL_NO_OF_TXNS;
	}

	/**
	 * @param until_no_of_txns the uNTIL_NO_OF_TXNS to set
	 */
	public void setUNTIL_NO_OF_TXNS(Integer until_no_of_txns) {
		UNTIL_NO_OF_TXNS = until_no_of_txns;
	}

	/**
	 * @return the tRANSFERS_DONE_TILL_DT
	 */
	public Integer getTRANSFERS_DONE_TILL_DT() {
		return TRANSFERS_DONE_TILL_DT;
	}

	/**
	 * @param transfers_done_till_dt the tRANSFERS_DONE_TILL_DT to set
	 */
	public void setTRANSFERS_DONE_TILL_DT(Integer transfers_done_till_dt) {
		TRANSFERS_DONE_TILL_DT = transfers_done_till_dt;
	}

	/**
	 * @return the aMT_TRNSFRD_TILL_DT
	 */
	public BigDecimal getAMT_TRNSFRD_TILL_DT() {
		return AMT_TRNSFRD_TILL_DT;
	}

	/**
	 * @param amt_trnsfrd_till_dt the aMT_TRNSFRD_TILL_DT to set
	 */
	public void setAMT_TRNSFRD_TILL_DT(BigDecimal amt_trnsfrd_till_dt) {
		AMT_TRNSFRD_TILL_DT = amt_trnsfrd_till_dt;
	}

	/**
	 * @return the iRA_OPTION
	 */
	public String getIRA_OPTION() {
		return IRA_OPTION;
	}

	/**
	 * @param ira_option the iRA_OPTION to set
	 */
	public void setIRA_OPTION(String ira_option) {
		IRA_OPTION = ira_option;
	}

	/**
	 * @return the uN_EDITED_FEE
	 */
	public BigDecimal getUN_EDITED_FEE() {
		return UN_EDITED_FEE;
	}

	/**
	 * @param un_edited_fee the uN_EDITED_FEE to set
	 */
	public void setUN_EDITED_FEE(BigDecimal un_edited_fee) {
		UN_EDITED_FEE = un_edited_fee;
	}

	/**
	 * @return the uN_EDITED_FEE_CHARGED_TO
	 */
	public String getUN_EDITED_FEE_CHARGED_TO() {
		return UN_EDITED_FEE_CHARGED_TO;
	}

	/**
	 * @param un_edited_fee_charged_to the uN_EDITED_FEE_CHARGED_TO to set
	 */
	public void setUN_EDITED_FEE_CHARGED_TO(String un_edited_fee_charged_to) {
		UN_EDITED_FEE_CHARGED_TO = un_edited_fee_charged_to;
	}

	/**
	 * @return the pAYEE_NAME
	 */
	public String getPAYEE_NAME() {
		return PAYEE_NAME;
	}

	/**
	 * @param payee_name the pAYEE_NAME to set
	 */
	public void setPAYEE_NAME(String payee_name) {
		PAYEE_NAME = payee_name;
	}

	/**
	 * @return the tIRD_PARTY_REASON
	 */
	public String getTIRD_PARTY_REASON() {
		return TIRD_PARTY_REASON;
	}

	/**
	 * @param tird_party_reason the tIRD_PARTY_REASON to set
	 */
	public void setTIRD_PARTY_REASON(String tird_party_reason) {
		TIRD_PARTY_REASON = tird_party_reason;
	}

	/**
	 * @return the eST_PICKUP_TIME
	 */
	public String getEST_PICKUP_TIME() {
		return EST_PICKUP_TIME;
	}

	/**
	 * @param est_pickup_time the eST_PICKUP_TIME to set
	 */
	public void setEST_PICKUP_TIME(String est_pickup_time) {
		EST_PICKUP_TIME = est_pickup_time;
	}

	/**
	 * @return the pRINT_ADDR_ON_CHECK
	 */
	public String getPRINT_ADDR_ON_CHECK() {
		return PRINT_ADDR_ON_CHECK;
	}

	/**
	 * @param print_addr_on_check the pRINT_ADDR_ON_CHECK to set
	 */
	public void setPRINT_ADDR_ON_CHECK(String print_addr_on_check) {
		PRINT_ADDR_ON_CHECK = print_addr_on_check;
	}

	/**
	 * @return the aDDR_LINE1
	 */
	public String getADDR_LINE1() {
		return ADDR_LINE1;
	}

	/**
	 * @param addr_line1 the aDDR_LINE1 to set
	 */
	public void setADDR_LINE1(String addr_line1) {
		ADDR_LINE1 = addr_line1;
	}

	/**
	 * @return the aDDR_LINE2
	 */
	public String getADDR_LINE2() {
		return ADDR_LINE2;
	}

	/**
	 * @param addr_line2 the aDDR_LINE2 to set
	 */
	public void setADDR_LINE2(String addr_line2) {
		ADDR_LINE2 = addr_line2;
	}

	/**
	 * @return the aDDR_LINE3
	 */
	public String getADDR_LINE3() {
		return ADDR_LINE3;
	}

	/**
	 * @param addr_line3 the aDDR_LINE3 to set
	 */
	public void setADDR_LINE3(String addr_line3) {
		ADDR_LINE3 = addr_line3;
	}

	/**
	 * @return the aDDR_LINE4
	 */
	public String getADDR_LINE4() {
		return ADDR_LINE4;
	}

	/**
	 * @param addr_line4 the aDDR_LINE4 to set
	 */
	public void setADDR_LINE4(String addr_line4) {
		ADDR_LINE4 = addr_line4;
	}

	/**
	 * @return the mEMO_LINE1
	 */
	public String getMEMO_LINE1() {
		return MEMO_LINE1;
	}

	/**
	 * @param memo_line1 the mEMO_LINE1 to set
	 */
	public void setMEMO_LINE1(String memo_line1) {
		MEMO_LINE1 = memo_line1;
	}

	/**
	 * @return the mEMO_LINE2
	 */
	public String getMEMO_LINE2() {
		return MEMO_LINE2;
	}

	/**
	 * @param memo_line2 the mEMO_LINE2 to set
	 */
	public void setMEMO_LINE2(String memo_line2) {
		MEMO_LINE2 = memo_line2;
	}

	/**
	 * @return the pRINT_MEMO_ON_CHECK
	 */
	public String getPRINT_MEMO_ON_CHECK() {
		return PRINT_MEMO_ON_CHECK;
	}

	/**
	 * @param print_memo_on_check the pRINT_MEMO_ON_CHECK to set
	 */
	public void setPRINT_MEMO_ON_CHECK(String print_memo_on_check) {
		PRINT_MEMO_ON_CHECK = print_memo_on_check;
	}

	/**
	 * @return the pRINT_MEMO_ON_STUB
	 */
	public String getPRINT_MEMO_ON_STUB() {
		return PRINT_MEMO_ON_STUB;
	}

	/**
	 * @param print_memo_on_stub the pRINT_MEMO_ON_STUB to set
	 */
	public void setPRINT_MEMO_ON_STUB(String print_memo_on_stub) {
		PRINT_MEMO_ON_STUB = print_memo_on_stub;
	}

	/**
	 * @return the qUALIFIER
	 */
	public String getQUALIFIER() {
		return QUALIFIER;
	}

	/**
	 * @param qualifier the qUALIFIER to set
	 */
	public void setQUALIFIER(String qualifier) {
		QUALIFIER = qualifier;
	}

	/**
	 * @return the rEVERSE_QUALIFIER
	 */
	public String getREVERSE_QUALIFIER() {
		return REVERSE_QUALIFIER;
	}

	/**
	 * @param reverse_qualifier the rEVERSE_QUALIFIER to set
	 */
	public void setREVERSE_QUALIFIER(String reverse_qualifier) {
		REVERSE_QUALIFIER = reverse_qualifier;
	}

	public String getSPOKE_TO_TYPE() {
		return SPOKE_TO_TYPE;
	}

	public void setSPOKE_TO_TYPE(String spoke_to_type) {
		SPOKE_TO_TYPE = spoke_to_type;
	}

	public String getSPOKE_TO_NAME() {
		return SPOKE_TO_NAME;
	}

	public void setSPOKE_TO_NAME(String spoke_to_name) {
		SPOKE_TO_NAME = spoke_to_name;
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

		retValue = "BUS_RUL_TXN_INP ( "
			+ super.toString() + TAB
			+ "TYPE = " + this.TYPE + TAB
			+ "AMT = " + this.AMT + TAB
			+ "STATE = " + this.STATE + TAB
			+ "EXE_DATE = " + this.EXE_DATE + TAB
			+ "BUS_DATE = " + this.BUS_DATE + TAB
			+ "ACTION = " + this.ACTION + TAB
			+ "RULE_TYPE = " + this.RULE_TYPE + TAB
			+ "CONFIRM_NUM = " + this.CONFIRM_NUM + TAB
			+ "TXN_TYPE = " + this.TXN_TYPE + TAB
			+ "TIER = " + this.TIER + TAB
			+ "DEBIT_ACCOUNT = " + this.DEBIT_ACCOUNT + TAB
			+ "CREATE_FLG = " + this.CREATE_FLG + TAB
			+ "UPDATE_FLG = " + this.UPDATE_FLG + TAB
			+ "DELETE_FLG = " + this.DELETE_FLG + TAB
			+ "ORGNL_TXN_AMT = " + this.ORGNL_TXN_AMT + TAB
			+ "NEW_TXN_AMT = " + this.NEW_TXN_AMT + TAB
			+ "ORGNL_TXN_DATE = " + this.ORGNL_TXN_DATE + TAB
			+ "NEW_TXN_DATE = " + this.NEW_TXN_DATE + TAB
			+ "EXT_STR = " + this.EXT_STR + TAB
			+ "EXT_NUM = " + this.EXT_NUM + TAB
			+ "CURRENT_APPRVER = " + this.CURRENT_APPRVER + TAB
			+ "MODE = " + this.MODE + TAB
			+ "CHECK_PICKUP_ADDR_STAT = " + this.CHECK_PICKUP_ADDR_STAT + TAB
			+ "MS360_SAME_NAME = " + this.MS360_SAME_NAME + TAB
			+ "AUTH_MODE = " + this.AUTH_MODE + TAB
			+ "CHARGE_TO = " + this.CHARGE_TO + TAB
			+ "UTILIZED_AMNT = " + this.UTILIZED_AMNT + TAB
			+ "FEE = " + this.FEE + TAB
			+ "PAGE_TYPE = " + this.PAGE_TYPE + TAB
			+ "TXN_SUB_TYPE = " + this.TXN_SUB_TYPE + TAB
			+ "FREQUENCY = " + this.FREQUENCY + TAB
			+ "PICKUP_OPTION = " + this.PICKUP_OPTION + TAB
			+ "CERTIFIED = " + this.CERTIFIED + TAB
			+ "DFLT_CHK_PCKP_ADDR = " + this.DFLT_CHK_PCKP_ADDR + TAB
			+ "PRINT_AT_BRANCH = " + this.PRINT_AT_BRANCH + TAB
			+ "IRA_TXN_IND = " + this.IRA_TXN_IND + TAB
			+ "UN_EDITED_AMT = " + this.UN_EDITED_AMT + TAB
			+ "REPEAT = " + this.REPEAT + TAB
			+ "UNTIL_END_DATE = " + this.UNTIL_END_DATE + TAB
			+ "UNTIL_DOLLAR_AMT = " + this.UNTIL_DOLLAR_AMT + TAB
			+ "UNTIL_NO_OF_TXNS = " + this.UNTIL_NO_OF_TXNS + TAB
			+ "TRANSFERS_DONE_TILL_DT = " + this.TRANSFERS_DONE_TILL_DT + TAB
			+ "AMT_TRNSFRD_TILL_DT = " + this.AMT_TRNSFRD_TILL_DT + TAB
			+ "IRA_OPTION = " + this.IRA_OPTION + TAB
			+ "UN_EDITED_FEE = " + this.UN_EDITED_FEE + TAB
			+ "UN_EDITED_FEE_CHARGED_TO = " + this.UN_EDITED_FEE_CHARGED_TO + TAB
			+ "PAYEE_NAME = " + this.PAYEE_NAME + TAB
			+ "TIRD_PARTY_REASON = " + this.TIRD_PARTY_REASON + TAB
			+ "EST_PICKUP_TIME = " + this.EST_PICKUP_TIME + TAB
			+ "PRINT_ADDR_ON_CHECK = " + this.PRINT_ADDR_ON_CHECK + TAB
			+ "ADDR_LINE1 = " + this.ADDR_LINE1 + TAB
			+ "ADDR_LINE2 = " + this.ADDR_LINE2 + TAB
			+ "ADDR_LINE3 = " + this.ADDR_LINE3 + TAB
			+ "ADDR_LINE4 = " + this.ADDR_LINE4 + TAB
			+ "MEMO_LINE1 = " + this.MEMO_LINE1 + TAB
			+ "MEMO_LINE2 = " + this.MEMO_LINE2 + TAB
			+ "PRINT_MEMO_ON_CHECK = " + this.PRINT_MEMO_ON_CHECK + TAB
			+ "PRINT_MEMO_ON_STUB = " + this.PRINT_MEMO_ON_STUB + TAB
			+ "QUALIFIER = " + this.QUALIFIER + TAB
			+ "REVERSE_QUALIFIER = " + this.REVERSE_QUALIFIER + TAB
			+ "SPOKE_TO_TYPE = " + this.SPOKE_TO_TYPE + TAB
			+ "SPOKE_TO_NAME = " + this.SPOKE_TO_NAME + TAB
			+ " )";

		return retValue;
	}


}