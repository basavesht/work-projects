package com.tcs.Payments.EAITO;

import java.math.BigDecimal;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MS_RTAB_ACNT_DTLS implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5057718626665126895L;
	private String OFFICE_ACCOUNT = null;
	private String ACCOUNT = null;
	private String FA_ID = null;
	private BigDecimal AVAILABLE_SPENDING_POWER = null;
	private BigDecimal CASH_AVAILABLE = null;
	private BigDecimal CASH_AVAILABLE_PENDING_TXN = null;
	private BigDecimal MARGIN_AVAILABLE = null;
	private BigDecimal MARGIN_PENDING_TXN = null;
	private BigDecimal DELAYED_DEBIT_CARD_TXN = null;
	private String ERROR_CODE = null;
	private String ERROR_DESCRIPTION = null;
	private String EXT_STR = null;
	private Double EXT_NUM;

	/**
	 * @return the oFFICE_ACCOUNT
	 */
	public String getOFFICE_ACCOUNT() {
		return OFFICE_ACCOUNT;
	}

	/**
	 * @param office_account the oFFICE_ACCOUNT to set
	 */
	public void setOFFICE_ACCOUNT(String office_account) {
		OFFICE_ACCOUNT = office_account;
	}

	/**
	 * @return the aCCOUNT
	 */
	public String getACCOUNT() {
		return ACCOUNT;
	}

	/**
	 * @param account the aCCOUNT to set
	 */
	public void setACCOUNT(String account) {
		ACCOUNT = account;
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
	 * @return the aVAILABLE_SPENDING_POWER
	 */
	public BigDecimal getAVAILABLE_SPENDING_POWER() {
		return AVAILABLE_SPENDING_POWER;
	}

	/**
	 * @param available_spending_power the aVAILABLE_SPENDING_POWER to set
	 */
	public void setAVAILABLE_SPENDING_POWER(BigDecimal available_spending_power) {
		AVAILABLE_SPENDING_POWER = available_spending_power;
	}

	/**
	 * @return the cASH_AVAILABLE
	 */
	public BigDecimal getCASH_AVAILABLE() {
		return CASH_AVAILABLE;
	}

	/**
	 * @param cash_available the cASH_AVAILABLE to set
	 */
	public void setCASH_AVAILABLE(BigDecimal cash_available) {
		CASH_AVAILABLE = cash_available;
	}

	/**
	 * @return the cASH_AVAILABLE_PENDING_TXN
	 */
	public BigDecimal getCASH_AVAILABLE_PENDING_TXN() {
		return CASH_AVAILABLE_PENDING_TXN;
	}

	/**
	 * @param cash_available_pending_txn the cASH_AVAILABLE_PENDING_TXN to set
	 */
	public void setCASH_AVAILABLE_PENDING_TXN(BigDecimal cash_available_pending_txn) {
		CASH_AVAILABLE_PENDING_TXN = cash_available_pending_txn;
	}

	/**
	 * @return the mARGIN_AVAILABLE
	 */
	public BigDecimal getMARGIN_AVAILABLE() {
		return MARGIN_AVAILABLE;
	}

	/**
	 * @param margin_available the mARGIN_AVAILABLE to set
	 */
	public void setMARGIN_AVAILABLE(BigDecimal margin_available) {
		MARGIN_AVAILABLE = margin_available;
	}

	/**
	 * @return the mARGIN_PENDING_TXN
	 */
	public BigDecimal getMARGIN_PENDING_TXN() {
		return MARGIN_PENDING_TXN;
	}

	/**
	 * @param margin_pending_txn the mARGIN_PENDING_TXN to set
	 */
	public void setMARGIN_PENDING_TXN(BigDecimal margin_pending_txn) {
		MARGIN_PENDING_TXN = margin_pending_txn;
	}

	/**
	 * @return the dELAYED_DEBIT_CARD_TXN
	 */
	public BigDecimal getDELAYED_DEBIT_CARD_TXN() {
		return DELAYED_DEBIT_CARD_TXN;
	}

	/**
	 * @param delayed_debit_card_txn the dELAYED_DEBIT_CARD_TXN to set
	 */
	public void setDELAYED_DEBIT_CARD_TXN(BigDecimal delayed_debit_card_txn) {
		DELAYED_DEBIT_CARD_TXN = delayed_debit_card_txn;
	}

	/**
	 * @return the eRROR_CODE
	 */
	public String getERROR_CODE() {
		return ERROR_CODE;
	}

	/**
	 * @param error_code the eRROR_CODE to set
	 */
	public void setERROR_CODE(String error_code) {
		ERROR_CODE = error_code;
	}

	/**
	 * @return the eRROR_DESCRIPTION
	 */
	public String getERROR_DESCRIPTION() {
		return ERROR_DESCRIPTION;
	}

	/**
	 * @param error_description the eRROR_DESCRIPTION to set
	 */
	public void setERROR_DESCRIPTION(String error_description) {
		ERROR_DESCRIPTION = error_description;
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

		retValue = "MS_RTAB_ACNT_DTLS ( "
			+ super.toString() + TAB
			+ "OFFICE_ACCOUNT = " + this.OFFICE_ACCOUNT + TAB
			+ "ACCOUNT = " + this.ACCOUNT + TAB
			+ "FA_ID = " + this.FA_ID + TAB
			+ "AVAILABLE_SPENDING_POWER = " + this.AVAILABLE_SPENDING_POWER + TAB
			+ "CASH_AVAILABLE = " + this.CASH_AVAILABLE + TAB
			+ "CASH_AVAILABLE_PENDING_TXN = " + this.CASH_AVAILABLE_PENDING_TXN + TAB
			+ "MARGIN_AVAILABLE = " + this.MARGIN_AVAILABLE + TAB
			+ "MARGIN_PENDING_TXN = " + this.MARGIN_PENDING_TXN + TAB
			+ "DELAYED_DEBIT_CARD_TXN = " + this.DELAYED_DEBIT_CARD_TXN + TAB
			+ "ERROR_CODE = " + this.ERROR_CODE + TAB
			+ "ERROR_DESCRIPTION = " + this.ERROR_DESCRIPTION + TAB
			+ "EXT_STR = " + this.EXT_STR + TAB
			+ "EXT_NUM = " + this.EXT_NUM + TAB
			+ " )";

		return retValue;
	}


}
