package com.tcs.Payments.EAITO;

import java.math.BigDecimal;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_CHK_NBR_IN implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -219275913981305986L;
	private String CALLER_PGMID = null;
	private String CALLER_APPL = null;
	private String MMS_TRANS_ID = null;
	private Integer BRANCH_NUMBER = null;
	private String MAILING_ADDR_LINE1 = null;
	private String MAILING_ADDR_LINE2 = null;
	private String MAILING_ADDR_LINE3 = null;
	private String MAILING_ADDR_LINE4 = null;
	private String MAILING_ADDR_LINE5 = null;
	private BigDecimal AMOUNT = null;
	private String CHECK_TYPE = null;
	private String LS_EXPAND = null;

	/**
	 * @return the cALLER_PGMID
	 */
	public String getCALLER_PGMID() {
		return CALLER_PGMID;
	}
	/**
	 * @param caller_pgmid the cALLER_PGMID to set
	 */
	public void setCALLER_PGMID(String caller_pgmid) {
		CALLER_PGMID = caller_pgmid;
	}
	/**
	 * @return the cALLER_APPL
	 */
	public String getCALLER_APPL() {
		return CALLER_APPL;
	}
	/**
	 * @param caller_appl the cALLER_APPL to set
	 */
	public void setCALLER_APPL(String caller_appl) {
		CALLER_APPL = caller_appl;
	}
	/**
	 * @return the mMS_TRANS_ID
	 */
	public String getMMS_TRANS_ID() {
		return MMS_TRANS_ID;
	}
	/**
	 * @param mms_trans_id the mMS_TRANS_ID to set
	 */
	public void setMMS_TRANS_ID(String mms_trans_id) {
		MMS_TRANS_ID = mms_trans_id;
	}
	/**
	 * @return the bRANCH_NUMBER
	 */
	public Integer getBRANCH_NUMBER() {
		return BRANCH_NUMBER;
	}
	/**
	 * @param branch_number the bRANCH_NUMBER to set
	 */
	public void setBRANCH_NUMBER(Integer branch_number) {
		BRANCH_NUMBER = branch_number;
	}
	/**
	 * @return the mAILING_ADDR_LINE1
	 */
	public String getMAILING_ADDR_LINE1() {
		return MAILING_ADDR_LINE1;
	}
	/**
	 * @param mailing_addr_line1 the mAILING_ADDR_LINE1 to set
	 */
	public void setMAILING_ADDR_LINE1(String mailing_addr_line1) {
		MAILING_ADDR_LINE1 = mailing_addr_line1;
	}
	/**
	 * @return the mAILING_ADDR_LINE2
	 */
	public String getMAILING_ADDR_LINE2() {
		return MAILING_ADDR_LINE2;
	}
	/**
	 * @param mailing_addr_line2 the mAILING_ADDR_LINE2 to set
	 */
	public void setMAILING_ADDR_LINE2(String mailing_addr_line2) {
		MAILING_ADDR_LINE2 = mailing_addr_line2;
	}
	/**
	 * @return the mAILING_ADDR_LINE3
	 */
	public String getMAILING_ADDR_LINE3() {
		return MAILING_ADDR_LINE3;
	}
	/**
	 * @param mailing_addr_line3 the mAILING_ADDR_LINE3 to set
	 */
	public void setMAILING_ADDR_LINE3(String mailing_addr_line3) {
		MAILING_ADDR_LINE3 = mailing_addr_line3;
	}
	/**
	 * @return the mAILING_ADDR_LINE4
	 */
	public String getMAILING_ADDR_LINE4() {
		return MAILING_ADDR_LINE4;
	}
	/**
	 * @param mailing_addr_line4 the mAILING_ADDR_LINE4 to set
	 */
	public void setMAILING_ADDR_LINE4(String mailing_addr_line4) {
		MAILING_ADDR_LINE4 = mailing_addr_line4;
	}
	/**
	 * @return the mAILING_ADDR_LINE5
	 */
	public String getMAILING_ADDR_LINE5() {
		return MAILING_ADDR_LINE5;
	}
	/**
	 * @param mailing_addr_line5 the mAILING_ADDR_LINE5 to set
	 */
	public void setMAILING_ADDR_LINE5(String mailing_addr_line5) {
		MAILING_ADDR_LINE5 = mailing_addr_line5;
	}
	/**
	 * @return the aMOUNT
	 */
	public BigDecimal getAMOUNT() {
		return AMOUNT;
	}
	/**
	 * @param amount the aMOUNT to set
	 */
	public void setAMOUNT(BigDecimal amount) {
		AMOUNT = amount;
	}
	/**
	 * @return the cHECK_TYPE
	 */
	public String getCHECK_TYPE() {
		return CHECK_TYPE;
	}
	/**
	 * @param check_type the cHECK_TYPE to set
	 */
	public void setCHECK_TYPE(String check_type) {
		CHECK_TYPE = check_type;
	}
	/**
	 * @return the lS_EXPAND
	 */
	public String getLS_EXPAND() {
		return LS_EXPAND;
	}
	/**
	 * @param ls_expand the lS_EXPAND to set
	 */
	public void setLS_EXPAND(String ls_expand) {
		LS_EXPAND = ls_expand;
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

		retValue = "MS_CHK_NBR_IN ( "
			+ super.toString() + TAB
			+ "CALLER_PGMID = " + this.CALLER_PGMID + TAB
			+ "CALLER_APPL = " + this.CALLER_APPL + TAB
			+ "MMS_TRANS_ID = " + this.MMS_TRANS_ID + TAB
			+ "BRANCH_NUMBER = " + this.BRANCH_NUMBER + TAB
			+ "MAILING_ADDR_LINE1 = " + this.MAILING_ADDR_LINE1 + TAB
			+ "MAILING_ADDR_LINE2 = " + this.MAILING_ADDR_LINE2 + TAB
			+ "MAILING_ADDR_LINE3 = " + this.MAILING_ADDR_LINE3 + TAB
			+ "MAILING_ADDR_LINE4 = " + this.MAILING_ADDR_LINE4 + TAB
			+ "MAILING_ADDR_LINE5 = " + this.MAILING_ADDR_LINE5 + TAB
			+ "AMOUNT = " + this.AMOUNT + TAB
			+ "CHECK_TYPE = " + this.CHECK_TYPE + TAB
			+ "LS_EXPAND = " + this.LS_EXPAND + TAB
			+ " )";

		return retValue;
	}


}
