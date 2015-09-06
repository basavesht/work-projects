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
public class MS_ACCOUNT_OUT_DTL implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8447283069027295446L;
	private String OFFICE;
	private String ACCOUNT_NO;
	private String FA;
	private String KEY_ACCOUNT_NO;
	private String HOUSE_ACNT_IND;
	private String RED_FLAG_IND;
	private String SHELL_IND;
	private String PENDING_ACNT_IND;
	private String UNDEL_STMNT_IND;
	private String STATUS;
	private String SW_STATUS;
	private String ACNT_CLS;
	private String SUB_CLASS;
	private String TEFRA;
	private boolean FIX_ADRS_IND;
	private String PRNTCHLDIND;
	private String NVS_SUB_PROD;
	private String PRNTOFFNUM;
	private String PRNTACCNUM;
	private String CLNT_AGREE_CAT;
	private String FEDCALL;
	private boolean ISMARGIN;
	private String ACNTCODE;
	private String IRACODE;
	private String OPNDATE;
	private String LSTSTMNTDATE;
	private String CATTIER;
	private String TRADCNTRL;
	private String DIVPAY;
	private String CHOICE_FUND_CODE;
	private String CLNT_CAT;
	private boolean ISMNGED;
	private String PLA_LOAN_EXISTS;
	private BigDecimal PLA_OUTSTNDNG_AMT = null;
	private BigDecimal SPNDG_PWR_FROM_RTAB = null;
	private BigDecimal AVAILABLE_SPNDG_PWR = null;
	private BigDecimal AVAILABLE_CASH_BALNCE = null;
	private BigDecimal AVAILABLE_MARGIN_BALNCE = null;
	private BigDecimal CASH_PENDNG_DEBIT = null;
	private BigDecimal MARGIN_PENDNG_DEBIT = null;
	private String DR_CR_IND=null;
	private String ACNT_CLS_DESC;  //Attribute for account class description...
	private String SUB_CLASS_DESC; //Attribute for account sub-class description...
	private String SHORT_POSITION_IND;
	private String COLLATERAL_ACC_IND;
	private String PAR_ACC_NOVUS_CODE;
	private String PAR_NOVUS_SUB_PRODUCT;
	private String TRUST_SUB_TYPE;
	private String ACC_CATEGORY;
	private String FOREIGN_ACCT_IND;
	private String REVOCABLE;
	private String MS_BUSINESS_ACC;
	private String RESIDES;
	private String EXT_ACCT_TYPE;
	private String EXT_SUB_ACCT_TYPE;
	private String SHELL_ACC_IND;
	private String ROUTING_CODE;
	private String EXT_ACCT_NUMBER;
	private String EXT_ACCT_LINK;
	private String EXT_ACC_CREATED_BY;
	private String ACNT_NOT_FND_IND;
	private String EXT_ACC_OWNER_ID;
	private String EXT_ACCT_SGN;
	private String EXT_OWNER;
	private String THRD_PRTY_IND;
	private String NAME_ON_EXT;
	private String RCVR_ADDR_1;
	private String RCVR_ADDR_2;
	private String RCVR_ADDR_3;
	private String RCVR_ADDR_4;
	private String BANK_NAME;
	private String BANK_ADDR_1;
	private String BANK_ADDR_2;
	private String BANK_ADDR_3;
	private String BANK_ADDR_4;
	private ACCOUNT_OUT_MSG_INFO[] MS_ACCOUNT_OUT_MSG_INFO = new ACCOUNT_OUT_MSG_INFO[20];
	private ACCOUNT_OUT_CLIENT[] MS_ACCOUNT_OUT_CLIENT = new ACCOUNT_OUT_CLIENT[20];
	private ACCOUNT_OUT_DOCUMENT[] MS_ACCOUNT_OUT_DOCUMENT = new ACCOUNT_OUT_DOCUMENT[20];
	private ACCOUNT_OUT_ADDRESS[] MS_ACCOUNT_OUT_ADDRESS = new ACCOUNT_OUT_ADDRESS[20];
	private ACCOUNT_PLATING_ADDRESS[] MS_ACCOUNT_PLATING_ADDRESS = new ACCOUNT_PLATING_ADDRESS[20];

	public String getACNT_CLS() {
		return ACNT_CLS;
	}
	public void setACNT_CLS(String acnt_cls) {
		ACNT_CLS = acnt_cls;
	}
	public String getFOREIGN_ACCT_IND() {
		return FOREIGN_ACCT_IND;
	}
	public void setFOREIGN_ACCT_IND(String foreign_acct_ind) {
		FOREIGN_ACCT_IND = foreign_acct_ind;
	}
	public String getACNTCODE() {
		return ACNTCODE;
	}
	public void setACNTCODE(String acntcode) {
		ACNTCODE = acntcode;
	}
	public String getCATTIER() {
		return CATTIER;
	}
	public void setCATTIER(String cattier) {
		CATTIER = cattier;
	}
	public String getCHOICE_FUND_CODE() {
		return CHOICE_FUND_CODE;
	}
	public void setCHOICE_FUND_CODE(String choice_fund_code) {
		CHOICE_FUND_CODE = choice_fund_code;
	}
	public String getCLNT_AGREE_CAT() {
		return CLNT_AGREE_CAT;
	}
	public void setCLNT_AGREE_CAT(String clnt_agree_cat) {
		CLNT_AGREE_CAT = clnt_agree_cat;
	}
	public String getCLNT_CAT() {
		return CLNT_CAT;
	}
	public void setCLNT_CAT(String clnt_cat) {
		CLNT_CAT = clnt_cat;
	}
	public String getDIVPAY() {
		return DIVPAY;
	}
	public void setDIVPAY(String divpay) {
		DIVPAY = divpay;
	}
	public String getFEDCALL() {
		return FEDCALL;
	}
	public void setFEDCALL(String fedcall) {
		FEDCALL = fedcall;
	}
	public boolean isFIX_ADRS_IND() {
		return FIX_ADRS_IND;
	}
	public void setFIX_ADRS_IND(boolean fix_adrs_ind) {
		FIX_ADRS_IND = fix_adrs_ind;
	}
	public String getHOUSE_ACNT_IND() {
		return HOUSE_ACNT_IND;
	}
	public void setHOUSE_ACNT_IND(String house_acnt_ind) {
		HOUSE_ACNT_IND = house_acnt_ind;
	}
	public String getIRACODE() {
		return IRACODE;
	}
	public void setIRACODE(String iracode) {
		IRACODE = iracode;
	}
	public boolean isISMARGIN() {
		return ISMARGIN;
	}
	public void setISMARGIN(boolean ismargin) {
		ISMARGIN = ismargin;
	}
	public boolean isISMNGED() {
		return ISMNGED;
	}
	public void setISMNGED(boolean ismnged) {
		ISMNGED = ismnged;
	}

	public String getNVS_SUB_PROD() {
		return NVS_SUB_PROD;
	}
	public void setNVS_SUB_PROD(String nvs_sub_prod) {
		NVS_SUB_PROD = nvs_sub_prod;
	}

	public String getLSTSTMNTDATE() {
		return LSTSTMNTDATE;
	}
	public void setLSTSTMNTDATE(String lststmntdate) {
		LSTSTMNTDATE = lststmntdate;
	}
	public String getOPNDATE() {
		return OPNDATE;
	}
	public void setOPNDATE(String opndate) {
		OPNDATE = opndate;
	}
	public String getPENDING_ACNT_IND() {
		return PENDING_ACNT_IND;
	}
	public void setPENDING_ACNT_IND(String pending_acnt_ind) {
		PENDING_ACNT_IND = pending_acnt_ind;
	}
	public String getPRNTACCNUM() {
		return PRNTACCNUM;
	}
	public void setPRNTACCNUM(String prntaccnum) {
		PRNTACCNUM = prntaccnum;
	}
	public String getPRNTCHLDIND() {
		return PRNTCHLDIND;
	}
	public void setPRNTCHLDIND(String prntchldind) {
		PRNTCHLDIND = prntchldind;
	}
	public String getPRNTOFFNUM() {
		return PRNTOFFNUM;
	}
	public void setPRNTOFFNUM(String prntoffnum) {
		PRNTOFFNUM = prntoffnum;
	}
	public String getRED_FLAG_IND() {
		return RED_FLAG_IND;
	}
	public void setRED_FLAG_IND(String red_flag_ind) {
		RED_FLAG_IND = red_flag_ind;
	}
	public String getSHELL_IND() {
		return SHELL_IND;
	}
	public void setSHELL_IND(String shell_ind) {
		SHELL_IND = shell_ind;
	}
	public String getSUB_CLASS() {
		return SUB_CLASS;
	}
	public void setSUB_CLASS(String sub_class) {
		SUB_CLASS = sub_class;
	}
	public String getTEFRA() {
		return TEFRA;
	}
	public void setTEFRA(String tefra) {
		TEFRA = tefra;
	}
	public String getTRADCNTRL() {
		return TRADCNTRL;
	}
	public void setTRADCNTRL(String tradcntrl) {
		TRADCNTRL = tradcntrl;
	}
	public String getUNDEL_STMNT_IND() {
		return UNDEL_STMNT_IND;
	}
	public void setUNDEL_STMNT_IND(String undel_stmnt_ind) {
		UNDEL_STMNT_IND = undel_stmnt_ind;
	}

	public class ACCOUNT_OUT_MSG_INFO implements java.io.Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 358452821000220959L;
		String OFFICE;
		String ACCOUNT_NO;
		String FA;
		String KEY_ACCOUNT_NO;
		boolean HOUSE_DEL_IND;
		boolean IS_GUARANTD;
		boolean IS_GUARANTOR;
		boolean IS_CRS_GRNTD;
		String EXT_STR;
		Double EXT_NUM;
		public String getACCOUNT_NO() {
			return ACCOUNT_NO;
		}
		public void setACCOUNT_NO(String account_no) {
			ACCOUNT_NO = account_no;
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
		public String getFA() {
			return FA;
		}
		public void setFA(String fa) {
			FA = fa;
		}
		public boolean isHOUSE_DEL_IND() {
			return HOUSE_DEL_IND;
		}
		public void setHOUSE_DEL_IND(boolean house_del_ind) {
			HOUSE_DEL_IND = house_del_ind;
		}
		public boolean isIS_CRS_GRNTD() {
			return IS_CRS_GRNTD;
		}
		public void setIS_CRS_GRNTD(boolean is_crs_grntd) {
			IS_CRS_GRNTD = is_crs_grntd;
		}
		public boolean isIS_GUARANTD() {
			return IS_GUARANTD;
		}
		public void setIS_GUARANTD(boolean is_guarantd) {
			IS_GUARANTD = is_guarantd;
		}
		public boolean isIS_GUARANTOR() {
			return IS_GUARANTOR;
		}
		public void setIS_GUARANTOR(boolean is_guarantor) {
			IS_GUARANTOR = is_guarantor;
		}
		public String getKEY_ACCOUNT_NO() {
			return KEY_ACCOUNT_NO;
		}
		public void setKEY_ACCOUNT_NO(String key_account_no) {
			KEY_ACCOUNT_NO = key_account_no;
		}
		public String getOFFICE() {
			return OFFICE;
		}
		public void setOFFICE(String office) {
			OFFICE = office;
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

			retValue = "ACCOUNT_OUT_MSG_INFO ( "
				+ super.toString() + TAB
				+ "OFFICE = " + this.OFFICE + TAB
				+ "ACCOUNT_NO = " + this.ACCOUNT_NO + TAB
				+ "FA = " + this.FA + TAB
				+ "KEY_ACCOUNT_NO = " + this.KEY_ACCOUNT_NO + TAB
				+ "HOUSE_DEL_IND = " + this.HOUSE_DEL_IND + TAB
				+ "IS_GUARANTD = " + this.IS_GUARANTD + TAB
				+ "IS_GUARANTOR = " + this.IS_GUARANTOR + TAB
				+ "IS_CRS_GRNTD = " + this.IS_CRS_GRNTD + TAB
				+ "EXT_STR = " + this.EXT_STR + TAB
				+ "EXT_NUM = " + this.EXT_NUM + TAB
				+ " )";

			return retValue;
		}

	}

	public class ACCOUNT_OUT_CLIENT implements java.io.Serializable 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6504372633105566028L;
		String OFFICE;
		String ACCOUNT_NO;
		String FA;
		String KEY_ACCOUNT_NO;
		String EMP_CODE;
		String SSN;
		String SSN_TXID_CLASS;
		String FRSTNM;
		String LSTNAME;
		String KEYCLNT;
		String CORPORATENAME;
		String KEYLINK;
		String RELATIONSHIP;
		String RESIDENT_COUNTRY;
		Date DOB;
		String EXT_STR;
		Double EXT_NUM;

		public String getOFFICE() {
			return OFFICE;
		}
		public void setOFFICE(String office) {
			OFFICE = office;
		}
		public String getACCOUNT_NO() {
			return ACCOUNT_NO;
		}
		public void setACCOUNT_NO(String account_no) {
			ACCOUNT_NO = account_no;
		}
		public String getFA() {
			return FA;
		}
		public void setFA(String fa) {
			FA = fa;
		}
		public String getKEY_ACCOUNT_NO() {
			return KEY_ACCOUNT_NO;
		}
		public void setKEY_ACCOUNT_NO(String key_account_no) {
			KEY_ACCOUNT_NO = key_account_no;
		}
		public String getEMP_CODE() {
			return EMP_CODE;
		}
		public void setEMP_CODE(String emp_code) {
			EMP_CODE = emp_code;
		}
		public String getSSN() {
			return SSN;
		}
		public void setSSN(String ssn) {
			SSN = ssn;
		}
		public String getSSN_TXID_CLASS() {
			return SSN_TXID_CLASS;
		}
		public void setSSN_TXID_CLASS(String ssn_txid_class) {
			SSN_TXID_CLASS = ssn_txid_class;
		}
		public String getFRSTNM() {
			return FRSTNM;
		}
		public void setFRSTNM(String frstnm) {
			FRSTNM = frstnm;
		}
		public String getLSTNAME() {
			return LSTNAME;
		}
		public void setLSTNAME(String lstname) {
			LSTNAME = lstname;
		}
		public String getKEYCLNT() {
			return KEYCLNT;
		}
		public void setKEYCLNT(String keyclnt) {
			KEYCLNT = keyclnt;
		}
		public String getCORPORATENAME() {
			return CORPORATENAME;
		}
		public void setCORPORATENAME(String corporatename) {
			CORPORATENAME = corporatename;
		}
		public String getKEYLINK() {
			return KEYLINK;
		}
		public void setKEYLINK(String keylink) {
			KEYLINK = keylink;
		}
		public String getRELATIONSHIP() {
			return RELATIONSHIP;
		}
		public void setRELATIONSHIP(String relationship) {
			RELATIONSHIP = relationship;
		}
		public String getRESIDENT_COUNTRY() {
			return RESIDENT_COUNTRY;
		}
		public void setRESIDENT_COUNTRY(String resident_country) {
			RESIDENT_COUNTRY = resident_country;
		}
		public Date getDOB() {
			return DOB;
		}
		public void setDOB(Date dob) {
			DOB = dob;
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

			retValue = "ACCOUNT_OUT_CLIENT ( "
				+ super.toString() + TAB
				+ "OFFICE = " + this.OFFICE + TAB
				+ "ACCOUNT_NO = " + this.ACCOUNT_NO + TAB
				+ "FA = " + this.FA + TAB
				+ "KEY_ACCOUNT_NO = " + this.KEY_ACCOUNT_NO + TAB
				+ "EMP_CODE = " + this.EMP_CODE + TAB
				+ "SSN = " + this.SSN + TAB
				+ "SSN_TXID_CLASS = " + this.SSN_TXID_CLASS + TAB
				+ "FRSTNM = " + this.FRSTNM + TAB
				+ "LSTNAME = " + this.LSTNAME + TAB
				+ "KEYCLNT = " + this.KEYCLNT + TAB
				+ "CORPORATENAME = " + this.CORPORATENAME + TAB
				+ "KEYLINK = " + this.KEYLINK + TAB
				+ "RELATIONSHIP = " + this.RELATIONSHIP + TAB
				+ "RESIDENT_COUNTRY = " + this.RESIDENT_COUNTRY + TAB
				+ "DOB = " + this.DOB + TAB
				+ "EXT_STR = " + this.EXT_STR + TAB
				+ "EXT_NUM = " + this.EXT_NUM + TAB
				+ " )";

			return retValue;
		}

	}

	public class ACCOUNT_OUT_DOCUMENT implements java.io.Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7693580738258458374L;
		String OFFICE;
		String ACCOUNT_NO;
		String FA;
		String KEY_ACCOUNT_NO;
		String CTGRY;
		String KEY;
		String STATUS;
		String EXT_STR;
		Double EXT_NUM;
		public String getACCOUNT_NO() {
			return ACCOUNT_NO;
		}
		public void setACCOUNT_NO(String account_no) {
			ACCOUNT_NO = account_no;
		}
		public String getCTGRY() {
			return CTGRY;
		}
		public void setCTGRY(String ctgry) {
			CTGRY = ctgry;
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
		public String getFA() {
			return FA;
		}
		public void setFA(String fa) {
			FA = fa;
		}
		public String getKEY() {
			return KEY;
		}
		public void setKEY(String key) {
			KEY = key;
		}
		public String getKEY_ACCOUNT_NO() {
			return KEY_ACCOUNT_NO;
		}
		public void setKEY_ACCOUNT_NO(String key_account_no) {
			KEY_ACCOUNT_NO = key_account_no;
		}
		public String getOFFICE() {
			return OFFICE;
		}
		public void setOFFICE(String office) {
			OFFICE = office;
		}
		public String getSTATUS() {
			return STATUS;
		}
		public void setSTATUS(String status) {
			STATUS = status;
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

			retValue = "ACCOUNT_OUT_DOCUMENT ( "
				+ super.toString() + TAB
				+ "OFFICE = " + this.OFFICE + TAB
				+ "ACCOUNT_NO = " + this.ACCOUNT_NO + TAB
				+ "FA = " + this.FA + TAB
				+ "KEY_ACCOUNT_NO = " + this.KEY_ACCOUNT_NO + TAB
				+ "CTGRY = " + this.CTGRY + TAB
				+ "KEY = " + this.KEY + TAB
				+ "STATUS = " + this.STATUS + TAB
				+ "EXT_STR = " + this.EXT_STR + TAB
				+ "EXT_NUM = " + this.EXT_NUM + TAB
				+ " )";

			return retValue;
		}
	}

	public class ACCOUNT_OUT_ADDRESS implements java.io.Serializable 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2681165884303280292L;
		String OFFICE;
		String ACCOUNT_NO;
		String FA;
		String KEY_ACCOUNT_NO;
		Date UPDDATE;
		String KEY_ADDRESS;
		String CNTRY_CODE;
		String EXT_STR;
		Double EXT_NUM;
		String KEY_CLIENT;
		String KEY_LINK;
		String EXTENDED_ADDR;
		String COUNTRY_TEXT;
		String CATEGORY;
		String FOREIGN_STATUS;
		String EFFECTIVE_DATE;
		String LINE1;
		String LINE2;
		String LINE3;
		String CITY;
		String STATE;
		String ZIP5;
		String ZIP4;
		String POSTAL;
		String PROVINCE;
		String NOTES;
		String OPID;
		String TERMINATION_DATE;
		String ADVISOR_CATEGORY;
		String ATTN_LINE1;
		String ATTN_LINE2;

		public String getACCOUNT_NO() {
			return ACCOUNT_NO;
		}
		public void setACCOUNT_NO(String account_no) {
			ACCOUNT_NO = account_no;
		}
		public String getCNTRY_CODE() {
			return CNTRY_CODE;
		}
		public void setCNTRY_CODE(String cntry_code) {
			CNTRY_CODE = cntry_code;
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
		public String getFA() {
			return FA;
		}
		public void setFA(String fa) {
			FA = fa;
		}
		public String getKEY_ACCOUNT_NO() {
			return KEY_ACCOUNT_NO;
		}
		public void setKEY_ACCOUNT_NO(String key_account_no) {
			KEY_ACCOUNT_NO = key_account_no;
		}
		public String getKEY_ADDRESS() {
			return KEY_ADDRESS;
		}
		public void setKEY_ADDRESS(String key_address) {
			KEY_ADDRESS = key_address;
		}
		public String getOFFICE() {
			return OFFICE;
		}
		public void setOFFICE(String office) {
			OFFICE = office;
		}
		public Date getUPDDATE() {
			return UPDDATE;
		}
		public void setUPDDATE(Date upddate) {
			UPDDATE = upddate;
		}
		public String getKEY_CLIENT() {
			return KEY_CLIENT;
		}
		public void setKEY_CLIENT(String key_client) {
			KEY_CLIENT = key_client;
		}
		public String getKEY_LINK() {
			return KEY_LINK;
		}
		public void setKEY_LINK(String key_link) {
			KEY_LINK = key_link;
		}
		public String getEXTENDED_ADDR() {
			return EXTENDED_ADDR;
		}
		public void setEXTENDED_ADDR(String extended_addr) {
			EXTENDED_ADDR = extended_addr;
		}
		public String getCOUNTRY_TEXT() {
			return COUNTRY_TEXT;
		}
		public void setCOUNTRY_TEXT(String country_text) {
			COUNTRY_TEXT = country_text;
		}
		public String getCATEGORY() {
			return CATEGORY;
		}
		public void setCATEGORY(String category) {
			CATEGORY = category;
		}
		public String getFOREIGN_STATUS() {
			return FOREIGN_STATUS;
		}
		public void setFOREIGN_STATUS(String foreign_status) {
			FOREIGN_STATUS = foreign_status;
		}
		public String getEFFECTIVE_DATE() {
			return EFFECTIVE_DATE;
		}
		public void setEFFECTIVE_DATE(String effective_date) {
			EFFECTIVE_DATE = effective_date;
		}
		public String getLINE1() {
			return LINE1;
		}
		public void setLINE1(String line1) {
			LINE1 = line1;
		}
		public String getLINE2() {
			return LINE2;
		}
		public void setLINE2(String line2) {
			LINE2 = line2;
		}
		public String getLINE3() {
			return LINE3;
		}
		public void setLINE3(String line3) {
			LINE3 = line3;
		}
		public String getCITY() {
			return CITY;
		}
		public void setCITY(String city) {
			CITY = city;
		}
		public String getSTATE() {
			return STATE;
		}
		public void setSTATE(String state) {
			STATE = state;
		}
		public String getZIP5() {
			return ZIP5;
		}
		public void setZIP5(String zip5) {
			ZIP5 = zip5;
		}
		public String getZIP4() {
			return ZIP4;
		}
		public void setZIP4(String zip4) {
			ZIP4 = zip4;
		}
		public String getPOSTAL() {
			return POSTAL;
		}
		public void setPOSTAL(String postal) {
			POSTAL = postal;
		}
		public String getPROVINCE() {
			return PROVINCE;
		}
		public void setPROVINCE(String province) {
			PROVINCE = province;
		}
		public String getNOTES() {
			return NOTES;
		}
		public void setNOTES(String notes) {
			NOTES = notes;
		}
		public String getOPID() {
			return OPID;
		}
		public void setOPID(String opid) {
			OPID = opid;
		}
		public String getTERMINATION_DATE() {
			return TERMINATION_DATE;
		}
		public void setTERMINATION_DATE(String termination_date) {
			TERMINATION_DATE = termination_date;
		}
		public String getADVISOR_CATEGORY() {
			return ADVISOR_CATEGORY;
		}
		public void setADVISOR_CATEGORY(String advisor_category) {
			ADVISOR_CATEGORY = advisor_category;
		}
		public String getATTN_LINE1() {
			return ATTN_LINE1;
		}
		public void setATTN_LINE1(String attn_line1) {
			ATTN_LINE1 = attn_line1;
		}
		public String getATTN_LINE2() {
			return ATTN_LINE2;
		}
		public void setATTN_LINE2(String attn_line2) {
			ATTN_LINE2 = attn_line2;
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

			retValue = "ACCOUNT_OUT_ADDRESS ( "
				+ super.toString() + TAB
				+ "OFFICE = " + this.OFFICE + TAB
				+ "ACCOUNT_NO = " + this.ACCOUNT_NO + TAB
				+ "FA = " + this.FA + TAB
				+ "KEY_ACCOUNT_NO = " + this.KEY_ACCOUNT_NO + TAB
				+ "UPDDATE = " + this.UPDDATE + TAB
				+ "KEY_ADDRESS = " + this.KEY_ADDRESS + TAB
				+ "CNTRY_CODE = " + this.CNTRY_CODE + TAB
				+ "EXT_STR = " + this.EXT_STR + TAB
				+ "EXT_NUM = " + this.EXT_NUM + TAB
				+ "KEY_CLIENT = " + this.KEY_CLIENT + TAB
				+ "KEY_LINK = " + this.KEY_LINK + TAB
				+ "EXTENDED_ADDR = " + this.EXTENDED_ADDR + TAB
				+ "COUNTRY_TEXT = " + this.COUNTRY_TEXT + TAB
				+ "CATEGORY = " + this.CATEGORY + TAB
				+ "FOREIGN_STATUS = " + this.FOREIGN_STATUS + TAB
				+ "EFFECTIVE_DATE = " + this.EFFECTIVE_DATE + TAB
				+ "LINE1 = " + this.LINE1 + TAB
				+ "LINE2 = " + this.LINE2 + TAB
				+ "LINE3 = " + this.LINE3 + TAB
				+ "CITY = " + this.CITY + TAB
				+ "STATE = " + this.STATE + TAB
				+ "ZIP5 = " + this.ZIP5 + TAB
				+ "ZIP4 = " + this.ZIP4 + TAB
				+ "POSTAL = " + this.POSTAL + TAB
				+ "PROVINCE = " + this.PROVINCE + TAB
				+ "NOTES = " + this.NOTES + TAB
				+ "OPID = " + this.OPID + TAB
				+ "TERMINATION_DATE = " + this.TERMINATION_DATE + TAB
				+ "ADVISOR_CATEGORY = " + this.ADVISOR_CATEGORY + TAB
				+ "ATTN_LINE1 = " + this.ATTN_LINE1 + TAB
				+ "ATTN_LINE2 = " + this.ATTN_LINE2 + TAB
				+ " )";

			return retValue;
		}
	}

	public class ACCOUNT_PLATING_ADDRESS implements java.io.Serializable 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1064031622461609959L;
		String ADDRESS_CATEGORY = null;
		String CATEGORY_LINE = null;
		String LAST_UPDATE_DATE = null;
		String ADDRESS_LINE_INDEX = null;
		String ADDRESS_LINE = null;
		String ADDRESS_CATEGORY1 = null;
		String CATEGORY_LINE1 = null;

		public String getADDRESS_CATEGORY() {
			return ADDRESS_CATEGORY;
		}
		public void setADDRESS_CATEGORY(String address_category) {
			ADDRESS_CATEGORY = address_category;
		}
		public String getCATEGORY_LINE() {
			return CATEGORY_LINE;
		}
		public void setCATEGORY_LINE(String category_line) {
			CATEGORY_LINE = category_line;
		}
		public String getLAST_UPDATE_DATE() {
			return LAST_UPDATE_DATE;
		}
		public void setLAST_UPDATE_DATE(String last_update_date) {
			LAST_UPDATE_DATE = last_update_date;
		}
		public String getADDRESS_LINE_INDEX() {
			return ADDRESS_LINE_INDEX;
		}
		public void setADDRESS_LINE_INDEX(String address_line_index) {
			ADDRESS_LINE_INDEX = address_line_index;
		}
		public String getADDRESS_LINE() {
			return ADDRESS_LINE;
		}
		public void setADDRESS_LINE(String address_line) {
			ADDRESS_LINE = address_line;
		}
		public String getADDRESS_CATEGORY1() {
			return ADDRESS_CATEGORY1;
		}
		public void setADDRESS_CATEGORY1(String address_category1) {
			ADDRESS_CATEGORY1 = address_category1;
		}
		public String getCATEGORY_LINE1() {
			return CATEGORY_LINE1;
		}
		public void setCATEGORY_LINE1(String category_line1) {
			CATEGORY_LINE1 = category_line1;
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

			retValue = "ACCOUNT_PLATING_ADDRESS ( "
				+ super.toString() + TAB
				+ "ADDRESS_CATEGORY = " + this.ADDRESS_CATEGORY + TAB
				+ "CATEGORY_LINE = " + this.CATEGORY_LINE + TAB
				+ "LAST_UPDATE_DATE = " + this.LAST_UPDATE_DATE + TAB
				+ "ADDRESS_LINE_INDEX = " + this.ADDRESS_LINE_INDEX + TAB
				+ "ADDRESS_LINE = " + this.ADDRESS_LINE + TAB
				+ "ADDRESS_CATEGORY1 = " + this.ADDRESS_CATEGORY1 + TAB
				+ "CATEGORY_LINE1 = " + this.CATEGORY_LINE1 + TAB
				+ " )";

			return retValue;
		}
	}

	String EXT_STR;
	Double EXT_NUM;
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
	public String getACCOUNT_NO() {
		return ACCOUNT_NO;
	}
	public void setACCOUNT_NO(String account_no) {
		ACCOUNT_NO = account_no;
	}

	public String getFA() {
		return FA;
	}
	public void setFA(String fa) {
		FA = fa;
	}
	public String getKEY_ACCOUNT_NO() {
		return KEY_ACCOUNT_NO;
	}
	public void setKEY_ACCOUNT_NO(String key_account_no) {
		KEY_ACCOUNT_NO = key_account_no;
	}
	public String getOFFICE() {
		return OFFICE;
	}
	public void setOFFICE(String office) {
		OFFICE = office;
	}
	public BigDecimal getAVAILABLE_SPNDG_PWR() {
		return AVAILABLE_SPNDG_PWR;
	}
	public void setAVAILABLE_SPNDG_PWR(BigDecimal available_spndg_pwr) {
		AVAILABLE_SPNDG_PWR = available_spndg_pwr;
	}
	public BigDecimal getAVAILABLE_CASH_BALNCE() {
		return AVAILABLE_CASH_BALNCE;
	}
	public void setAVAILABLE_CASH_BALNCE(BigDecimal available_cash_balnce) {
		AVAILABLE_CASH_BALNCE = available_cash_balnce;
	}
	public BigDecimal getAVAILABLE_MARGIN_BALNCE() {
		return AVAILABLE_MARGIN_BALNCE;
	}
	public void setAVAILABLE_MARGIN_BALNCE(BigDecimal available_margin_balnce) {
		AVAILABLE_MARGIN_BALNCE = available_margin_balnce;
	}
	public BigDecimal getCASH_PENDNG_DEBIT() {
		return CASH_PENDNG_DEBIT;
	}
	public void setCASH_PENDNG_DEBIT(BigDecimal cash_pendng_debit) {
		CASH_PENDNG_DEBIT = cash_pendng_debit;
	}
	public BigDecimal getMARGIN_PENDNG_DEBIT() {
		return MARGIN_PENDNG_DEBIT;
	}
	public void setMARGIN_PENDNG_DEBIT(BigDecimal margin_pendng_debit) {
		MARGIN_PENDNG_DEBIT = margin_pendng_debit;
	}
	public String getDR_CR_IND() {
		return DR_CR_IND;
	}
	public void setDR_CR_IND(String dr_cr_ind) {
		DR_CR_IND = dr_cr_ind;
	}
	public ACCOUNT_OUT_MSG_INFO[] getMS_ACCOUNT_OUT_MSG_INFO() {
		return MS_ACCOUNT_OUT_MSG_INFO;
	}
	public void setMS_ACCOUNT_OUT_MSG_INFO(
			ACCOUNT_OUT_MSG_INFO[] ms_account_out_msg_info) {
		MS_ACCOUNT_OUT_MSG_INFO = ms_account_out_msg_info;
	}
	public ACCOUNT_OUT_CLIENT[] getMS_ACCOUNT_OUT_CLIENT() {
		return MS_ACCOUNT_OUT_CLIENT;
	}
	public void setMS_ACCOUNT_OUT_CLIENT(ACCOUNT_OUT_CLIENT[] ms_account_out_client) {
		MS_ACCOUNT_OUT_CLIENT = ms_account_out_client;
	}
	public ACCOUNT_OUT_DOCUMENT[] getMS_ACCOUNT_OUT_DOCUMENT() {
		return MS_ACCOUNT_OUT_DOCUMENT;
	}
	public void setMS_ACCOUNT_OUT_DOCUMENT(
			ACCOUNT_OUT_DOCUMENT[] ms_account_out_document) {
		MS_ACCOUNT_OUT_DOCUMENT = ms_account_out_document;
	}
	public ACCOUNT_OUT_ADDRESS[] getMS_ACCOUNT_OUT_ADDRESS() {
		return MS_ACCOUNT_OUT_ADDRESS;
	}
	public void setMS_ACCOUNT_OUT_ADDRESS(
			ACCOUNT_OUT_ADDRESS[] ms_account_out_address) {
		MS_ACCOUNT_OUT_ADDRESS = ms_account_out_address;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String status) {
		STATUS = status;
	}
	public String getSW_STATUS() {
		return SW_STATUS;
	}
	public void setSW_STATUS(String sw_status) {
		SW_STATUS = sw_status;
	}
	public String getACNT_CLS_DESC() {
		return ACNT_CLS_DESC;
	}
	public void setACNT_CLS_DESC(String acnt_cls_desc) {
		ACNT_CLS_DESC = acnt_cls_desc;
	}
	public String getSUB_CLASS_DESC() {
		return SUB_CLASS_DESC;
	}
	public void setSUB_CLASS_DESC(String sub_class_desc) {
		SUB_CLASS_DESC = sub_class_desc;
	}
	public String getSHORT_POSITION_IND() {
		return SHORT_POSITION_IND;
	}
	public void setSHORT_POSITION_IND(String short_position_ind) {
		SHORT_POSITION_IND = short_position_ind;
	}
	public String getCOLLATERAL_ACC_IND() {
		return COLLATERAL_ACC_IND;
	}
	public void setCOLLATERAL_ACC_IND(String collateral_acc_ind) {
		COLLATERAL_ACC_IND = collateral_acc_ind;
	}
	public String getPAR_ACC_NOVUS_CODE() {
		return PAR_ACC_NOVUS_CODE;
	}
	public void setPAR_ACC_NOVUS_CODE(String par_acc_novus_code) {
		PAR_ACC_NOVUS_CODE = par_acc_novus_code;
	}
	public String getPAR_NOVUS_SUB_PRODUCT() {
		return PAR_NOVUS_SUB_PRODUCT;
	}
	public void setPAR_NOVUS_SUB_PRODUCT(String par_novus_sub_product) {
		PAR_NOVUS_SUB_PRODUCT = par_novus_sub_product;
	}
	public String getTRUST_SUB_TYPE() {
		return TRUST_SUB_TYPE;
	}
	public void setTRUST_SUB_TYPE(String trust_sub_type) {
		TRUST_SUB_TYPE = trust_sub_type;
	}
	public String getACC_CATEGORY() {
		return ACC_CATEGORY;
	}
	public void setACC_CATEGORY(String acc_category) {
		ACC_CATEGORY = acc_category;
	}
	public String getREVOCABLE() {
		return REVOCABLE;
	}
	public void setREVOCABLE(String revocable) {
		REVOCABLE = revocable;
	}
	/**
	 * @return the mS_BUSINESS_ACC
	 */
	public String getMS_BUSINESS_ACC() {
		return MS_BUSINESS_ACC;
	}
	/**
	 * @param ms_business_acc the mS_BUSINESS_ACC to set
	 */
	public void setMS_BUSINESS_ACC(String ms_business_acc) {
		MS_BUSINESS_ACC = ms_business_acc;
	}
	/**
	 * @return the rESIDES
	 */
	public String getRESIDES() {
		return RESIDES;
	}
	/**
	 * @param resides the rESIDES to set
	 */
	public void setRESIDES(String resides) {
		RESIDES = resides;
	}
	/**
	 * @return the eXT_ACCT_TYPE
	 */
	public String getEXT_ACCT_TYPE() {
		return EXT_ACCT_TYPE;
	}
	/**
	 * @param ext_acct_type the eXT_ACCT_TYPE to set
	 */
	public void setEXT_ACCT_TYPE(String ext_acct_type) {
		EXT_ACCT_TYPE = ext_acct_type;
	}
	/**
	 * @return the eXT_SUB_ACCT_TYPE
	 */
	public String getEXT_SUB_ACCT_TYPE() {
		return EXT_SUB_ACCT_TYPE;
	}
	/**
	 * @param ext_sub_acct_type the eXT_SUB_ACCT_TYPE to set
	 */
	public void setEXT_SUB_ACCT_TYPE(String ext_sub_acct_type) {
		EXT_SUB_ACCT_TYPE = ext_sub_acct_type;
	}
	/**
	 * @return the sHELL_ACC_IND
	 */
	public String getSHELL_ACC_IND() {
		return SHELL_ACC_IND;
	}
	/**
	 * @param shell_acc_ind the sHELL_ACC_IND to set
	 */
	public void setSHELL_ACC_IND(String shell_acc_ind) {
		SHELL_ACC_IND = shell_acc_ind;
	}
	/**
	 * @return the rOUTING_CODE
	 */
	public String getROUTING_CODE() {
		return ROUTING_CODE;
	}
	/**
	 * @param routing_code the rOUTING_CODE to set
	 */
	public void setROUTING_CODE(String routing_code) {
		ROUTING_CODE = routing_code;
	}
	/**
	 * @return the eXT_ACCT_NUMBER
	 */
	public String getEXT_ACCT_NUMBER() {
		return EXT_ACCT_NUMBER;
	}
	/**
	 * @param ext_acct_number the eXT_ACCT_NUMBER to set
	 */
	public void setEXT_ACCT_NUMBER(String ext_acct_number) {
		EXT_ACCT_NUMBER = ext_acct_number;
	}
	/**
	 * @return the eXT_ACCT_LINK
	 */
	public String getEXT_ACCT_LINK() {
		return EXT_ACCT_LINK;
	}
	/**
	 * @param ext_acct_link the eXT_ACCT_LINK to set
	 */
	public void setEXT_ACCT_LINK(String ext_acct_link) {
		EXT_ACCT_LINK = ext_acct_link;
	}
	/**
	 * @return the eXT_ACC_CREATED_BY
	 */
	public String getEXT_ACC_CREATED_BY() {
		return EXT_ACC_CREATED_BY;
	}
	/**
	 * @param ext_acc_created_by the eXT_ACC_CREATED_BY to set
	 */
	public void setEXT_ACC_CREATED_BY(String ext_acc_created_by) {
		EXT_ACC_CREATED_BY = ext_acc_created_by;
	}
	/**
	 * @return the sPNDG_PWR_FROM_RTAB
	 */
	public BigDecimal getSPNDG_PWR_FROM_RTAB() {
		return SPNDG_PWR_FROM_RTAB;
	}
	/**
	 * @param spndg_pwr_from_rtab the sPNDG_PWR_FROM_RTAB to set
	 */
	public void setSPNDG_PWR_FROM_RTAB(BigDecimal spndg_pwr_from_rtab) {
		SPNDG_PWR_FROM_RTAB = spndg_pwr_from_rtab;
	}
	/**
	 * @return the aCNT_NOT_FND_IND
	 */
	public String getACNT_NOT_FND_IND() {
		return ACNT_NOT_FND_IND;
	}
	/**
	 * @param acnt_not_fnd_ind the aCNT_NOT_FND_IND to set
	 */
	public void setACNT_NOT_FND_IND(String acnt_not_fnd_ind) {
		ACNT_NOT_FND_IND = acnt_not_fnd_ind;
	}
	/**
	 * @return the eXT_ACC_OWNER_ID
	 */
	public String getEXT_ACC_OWNER_ID() {
		return EXT_ACC_OWNER_ID;
	}
	/**
	 * @param ext_acc_owner_id the eXT_ACC_OWNER_ID to set
	 */
	public void setEXT_ACC_OWNER_ID(String ext_acc_owner_id) {
		EXT_ACC_OWNER_ID = ext_acc_owner_id;
	}

	public String getEXT_ACCT_SGN() {
		return EXT_ACCT_SGN;
	}
	public void setEXT_ACCT_SGN(String ext_acct_sgn) {
		EXT_ACCT_SGN = ext_acct_sgn;
	}
	public ACCOUNT_PLATING_ADDRESS[] getMS_ACCOUNT_PLATING_ADDRESS() {
		return MS_ACCOUNT_PLATING_ADDRESS;
	}
	public void setMS_ACCOUNT_PLATING_ADDRESS(
			ACCOUNT_PLATING_ADDRESS[] ms_account_plating_address) {
		MS_ACCOUNT_PLATING_ADDRESS = ms_account_plating_address;
	}

	/**
	 * @return the eXT_OWNER
	 */
	public String getEXT_OWNER() {
		return EXT_OWNER;
	}
	/**
	 * @param ext_owner the eXT_OWNER to set
	 */
	public void setEXT_OWNER(String ext_owner) {
		EXT_OWNER = ext_owner;
	}
	/**
	 * @return the tHRD_PRTY_IND
	 */
	public String getTHRD_PRTY_IND() {
		return THRD_PRTY_IND;
	}
	/**
	 * @param thrd_prty_ind the tHRD_PRTY_IND to set
	 */
	public void setTHRD_PRTY_IND(String thrd_prty_ind) {
		THRD_PRTY_IND = thrd_prty_ind;
	}
	/**
	 * @return the nAME_ON_EXT
	 */
	public String getNAME_ON_EXT() {
		return NAME_ON_EXT;
	}
	/**
	 * @param name_on_ext the nAME_ON_EXT to set
	 */
	public void setNAME_ON_EXT(String name_on_ext) {
		NAME_ON_EXT = name_on_ext;
	}
	/**
	 * @return the rCVR_ADDR_1
	 */
	public String getRCVR_ADDR_1() {
		return RCVR_ADDR_1;
	}
	/**
	 * @param rcvr_addr_1 the rCVR_ADDR_1 to set
	 */
	public void setRCVR_ADDR_1(String rcvr_addr_1) {
		RCVR_ADDR_1 = rcvr_addr_1;
	}
	/**
	 * @return the rCVR_ADDR_2
	 */
	public String getRCVR_ADDR_2() {
		return RCVR_ADDR_2;
	}
	/**
	 * @param rcvr_addr_2 the rCVR_ADDR_2 to set
	 */
	public void setRCVR_ADDR_2(String rcvr_addr_2) {
		RCVR_ADDR_2 = rcvr_addr_2;
	}
	/**
	 * @return the rCVR_ADDR_3
	 */
	public String getRCVR_ADDR_3() {
		return RCVR_ADDR_3;
	}
	/**
	 * @param rcvr_addr_3 the rCVR_ADDR_3 to set
	 */
	public void setRCVR_ADDR_3(String rcvr_addr_3) {
		RCVR_ADDR_3 = rcvr_addr_3;
	}
	/**
	 * @return the rCVR_ADDR_4
	 */
	public String getRCVR_ADDR_4() {
		return RCVR_ADDR_4;
	}
	/**
	 * @param rcvr_addr_4 the rCVR_ADDR_4 to set
	 */
	public void setRCVR_ADDR_4(String rcvr_addr_4) {
		RCVR_ADDR_4 = rcvr_addr_4;
	}
	/**
	 * @return the bANK_NAME
	 */
	public String getBANK_NAME() {
		return BANK_NAME;
	}
	/**
	 * @param bank_name the bANK_NAME to set
	 */
	public void setBANK_NAME(String bank_name) {
		BANK_NAME = bank_name;
	}
	/**
	 * @return the bANK_ADDR_1
	 */
	public String getBANK_ADDR_1() {
		return BANK_ADDR_1;
	}
	/**
	 * @param bank_addr_1 the bANK_ADDR_1 to set
	 */
	public void setBANK_ADDR_1(String bank_addr_1) {
		BANK_ADDR_1 = bank_addr_1;
	}
	/**
	 * @return the bANK_ADDR_2
	 */
	public String getBANK_ADDR_2() {
		return BANK_ADDR_2;
	}
	/**
	 * @param bank_addr_2 the bANK_ADDR_2 to set
	 */
	public void setBANK_ADDR_2(String bank_addr_2) {
		BANK_ADDR_2 = bank_addr_2;
	}
	/**
	 * @return the bANK_ADDR_3
	 */
	public String getBANK_ADDR_3() {
		return BANK_ADDR_3;
	}
	/**
	 * @param bank_addr_3 the bANK_ADDR_3 to set
	 */
	public void setBANK_ADDR_3(String bank_addr_3) {
		BANK_ADDR_3 = bank_addr_3;
	}
	/**
	 * @return the bANK_ADDR_4
	 */
	public String getBANK_ADDR_4() {
		return BANK_ADDR_4;
	}
	/**
	 * @param bank_addr_4 the bANK_ADDR_4 to set
	 */
	public void setBANK_ADDR_4(String bank_addr_4) {
		BANK_ADDR_4 = bank_addr_4;
	}

	/**
	 * @return the pLA_LOAN_EXISTS
	 */
	public String getPLA_LOAN_EXISTS() {
		return PLA_LOAN_EXISTS;
	}
	/**
	 * @param pla_loan_exists the pLA_LOAN_EXISTS to set
	 */
	public void setPLA_LOAN_EXISTS(String pla_loan_exists) {
		PLA_LOAN_EXISTS = pla_loan_exists;
	}
	/**
	 * @return the pLA_OUTSTNDNG_AMT
	 */
	public BigDecimal getPLA_OUTSTNDNG_AMT() {
		return PLA_OUTSTNDNG_AMT;
	}
	/**
	 * @param pla_outstndng_amt the pLA_OUTSTNDNG_AMT to set
	 */
	public void setPLA_OUTSTNDNG_AMT(BigDecimal pla_outstndng_amt) {
		PLA_OUTSTNDNG_AMT = pla_outstndng_amt;
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

		retValue = "MS_ACCOUNT_OUT_DTL ( "
			+ super.toString() + TAB
			+ "OFFICE = " + this.OFFICE + TAB
			+ "ACCOUNT_NO = " + this.ACCOUNT_NO + TAB
			+ "FA = " + this.FA + TAB
			+ "KEY_ACCOUNT_NO = " + this.KEY_ACCOUNT_NO + TAB
			+ "HOUSE_ACNT_IND = " + this.HOUSE_ACNT_IND + TAB
			+ "RED_FLAG_IND = " + this.RED_FLAG_IND + TAB
			+ "SHELL_IND = " + this.SHELL_IND + TAB
			+ "PENDING_ACNT_IND = " + this.PENDING_ACNT_IND + TAB
			+ "UNDEL_STMNT_IND = " + this.UNDEL_STMNT_IND + TAB
			+ "STATUS = " + this.STATUS + TAB
			+ "SW_STATUS = " + this.SW_STATUS + TAB
			+ "ACNT_CLS = " + this.ACNT_CLS + TAB
			+ "SUB_CLASS = " + this.SUB_CLASS + TAB
			+ "TEFRA = " + this.TEFRA + TAB
			+ "FIX_ADRS_IND = " + this.FIX_ADRS_IND + TAB
			+ "PRNTCHLDIND = " + this.PRNTCHLDIND + TAB
			+ "NVS_SUB_PROD = " + this.NVS_SUB_PROD + TAB
			+ "PRNTOFFNUM = " + this.PRNTOFFNUM + TAB
			+ "PRNTACCNUM = " + this.PRNTACCNUM + TAB
			+ "CLNT_AGREE_CAT = " + this.CLNT_AGREE_CAT + TAB
			+ "FEDCALL = " + this.FEDCALL + TAB
			+ "ISMARGIN = " + this.ISMARGIN + TAB
			+ "ACNTCODE = " + this.ACNTCODE + TAB
			+ "IRACODE = " + this.IRACODE + TAB
			+ "OPNDATE = " + this.OPNDATE + TAB
			+ "LSTSTMNTDATE = " + this.LSTSTMNTDATE + TAB
			+ "CATTIER = " + this.CATTIER + TAB
			+ "TRADCNTRL = " + this.TRADCNTRL + TAB
			+ "DIVPAY = " + this.DIVPAY + TAB
			+ "CHOICE_FUND_CODE = " + this.CHOICE_FUND_CODE + TAB
			+ "CLNT_CAT = " + this.CLNT_CAT + TAB
			+ "ISMNGED = " + this.ISMNGED + TAB
			+ "PLA_LOAN_EXISTS = " + this.PLA_LOAN_EXISTS + TAB
			+ "PLA_OUTSTNDNG_AMT = " + this.PLA_OUTSTNDNG_AMT + TAB
			+ "SPNDG_PWR_FROM_RTAB = " + this.SPNDG_PWR_FROM_RTAB + TAB
			+ "AVAILABLE_SPNDG_PWR = " + this.AVAILABLE_SPNDG_PWR + TAB
			+ "AVAILABLE_CASH_BALNCE = " + this.AVAILABLE_CASH_BALNCE + TAB
			+ "AVAILABLE_MARGIN_BALNCE = " + this.AVAILABLE_MARGIN_BALNCE + TAB
			+ "CASH_PENDNG_DEBIT = " + this.CASH_PENDNG_DEBIT + TAB
			+ "MARGIN_PENDNG_DEBIT = " + this.MARGIN_PENDNG_DEBIT + TAB
			+ "DR_CR_IND = " + this.DR_CR_IND + TAB
			+ "ACNT_CLS_DESC = " + this.ACNT_CLS_DESC + TAB
			+ "SUB_CLASS_DESC = " + this.SUB_CLASS_DESC + TAB
			+ "SHORT_POSITION_IND = " + this.SHORT_POSITION_IND + TAB
			+ "COLLATERAL_ACC_IND = " + this.COLLATERAL_ACC_IND + TAB
			+ "PAR_ACC_NOVUS_CODE = " + this.PAR_ACC_NOVUS_CODE + TAB
			+ "PAR_NOVUS_SUB_PRODUCT = " + this.PAR_NOVUS_SUB_PRODUCT + TAB
			+ "TRUST_SUB_TYPE = " + this.TRUST_SUB_TYPE + TAB
			+ "ACC_CATEGORY = " + this.ACC_CATEGORY + TAB
			+ "FOREIGN_ACCT_IND = " + this.FOREIGN_ACCT_IND + TAB
			+ "REVOCABLE = " + this.REVOCABLE + TAB
			+ "MS_BUSINESS_ACC = " + this.MS_BUSINESS_ACC + TAB
			+ "RESIDES = " + this.RESIDES + TAB
			+ "EXT_ACCT_TYPE = " + this.EXT_ACCT_TYPE + TAB
			+ "EXT_SUB_ACCT_TYPE = " + this.EXT_SUB_ACCT_TYPE + TAB
			+ "SHELL_ACC_IND = " + this.SHELL_ACC_IND + TAB
			+ "ROUTING_CODE = " + this.ROUTING_CODE + TAB
			+ "EXT_ACCT_NUMBER = " + this.EXT_ACCT_NUMBER + TAB
			+ "EXT_ACCT_LINK = " + this.EXT_ACCT_LINK + TAB
			+ "EXT_ACC_CREATED_BY = " + this.EXT_ACC_CREATED_BY + TAB
			+ "ACNT_NOT_FND_IND = " + this.ACNT_NOT_FND_IND + TAB
			+ "EXT_ACC_OWNER_ID = " + this.EXT_ACC_OWNER_ID + TAB
			+ "EXT_ACCT_SGN = " + this.EXT_ACCT_SGN + TAB
			+ "EXT_OWNER = " + this.EXT_OWNER + TAB
			+ "THRD_PRTY_IND = " + this.THRD_PRTY_IND + TAB
			+ "NAME_ON_EXT = " + this.NAME_ON_EXT + TAB
			+ "RCVR_ADDR_1 = " + this.RCVR_ADDR_1 + TAB
			+ "RCVR_ADDR_2 = " + this.RCVR_ADDR_2 + TAB
			+ "RCVR_ADDR_3 = " + this.RCVR_ADDR_3 + TAB
			+ "RCVR_ADDR_4 = " + this.RCVR_ADDR_4 + TAB
			+ "BANK_NAME = " + this.BANK_NAME + TAB
			+ "BANK_ADDR_1 = " + this.BANK_ADDR_1 + TAB
			+ "BANK_ADDR_2 = " + this.BANK_ADDR_2 + TAB
			+ "BANK_ADDR_3 = " + this.BANK_ADDR_3 + TAB
			+ "BANK_ADDR_4 = " + this.BANK_ADDR_4 + TAB
			+ "MS_ACCOUNT_OUT_MSG_INFO = " + this.MS_ACCOUNT_OUT_MSG_INFO + TAB
			+ "MS_ACCOUNT_OUT_CLIENT = " + this.MS_ACCOUNT_OUT_CLIENT + TAB
			+ "MS_ACCOUNT_OUT_DOCUMENT = " + this.MS_ACCOUNT_OUT_DOCUMENT + TAB
			+ "MS_ACCOUNT_OUT_ADDRESS = " + this.MS_ACCOUNT_OUT_ADDRESS + TAB
			+ "MS_ACCOUNT_PLATING_ADDRESS = " + this.MS_ACCOUNT_PLATING_ADDRESS + TAB
			+ "EXT_STR = " + this.EXT_STR + TAB
			+ "EXT_NUM = " + this.EXT_NUM + TAB
			+ " )";

		return retValue;
	}

}
