package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class IRATransferTypeTO  extends EBWTransferObject implements Serializable {

	private static final long serialVersionUID = 3374488799888358486L;
	private String from_plan_code = null;
	private String to_plan_code = null;
	private String txn_type = null;
	private String retirement_mnemonic = null;
	private String retirement_mnemonic_desc = null;
	private String qualifier = null;
	private String reverse_qualifier = null;
	private String active_flag = null;
	private String retirement_types = null;
	private String txnConfirmation_No = null;
	
	/**
	 * @return the from_plan_code
	 */
	public String getFrom_plan_code() {
		return from_plan_code;
	}
	/**
	 * @param from_plan_code the from_plan_code to set
	 */
	public void setFrom_plan_code(String from_plan_code) {
		this.from_plan_code = from_plan_code;
	}
	/**
	 * @return the to_plan_code
	 */
	public String getTo_plan_code() {
		return to_plan_code;
	}
	/**
	 * @param to_plan_code the to_plan_code to set
	 */
	public void setTo_plan_code(String to_plan_code) {
		this.to_plan_code = to_plan_code;
	}
	/**
	 * @return the txn_type
	 */
	public String getTxn_type() {
		return txn_type;
	}
	/**
	 * @param txn_type the txn_type to set
	 */
	public void setTxn_type(String txn_type) {
		this.txn_type = txn_type;
	}
	/**
	 * @return the retirement_mnemonic
	 */
	public String getRetirement_mnemonic() {
		return retirement_mnemonic;
	}
	/**
	 * @param retirement_mnemonic the retirement_mnemonic to set
	 */
	public void setRetirement_mnemonic(String retirement_mnemonic) {
		this.retirement_mnemonic = retirement_mnemonic;
	}
	/**
	 * @return the retirement_mnemonic_desc
	 */
	public String getRetirement_mnemonic_desc() {
		return retirement_mnemonic_desc;
	}
	/**
	 * @param retirement_mnemonic_desc the retirement_mnemonic_desc to set
	 */
	public void setRetirement_mnemonic_desc(String retirement_mnemonic_desc) {
		this.retirement_mnemonic_desc = retirement_mnemonic_desc;
	}
	/**
	 * @return the qualifier
	 */
	public String getQualifier() {
		return qualifier;
	}
	/**
	 * @param qualifier the qualifier to set
	 */
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	/**
	 * @return the reverse_qualifier
	 */
	public String getReverse_qualifier() {
		return reverse_qualifier;
	}
	/**
	 * @param reverse_qualifier the reverse_qualifier to set
	 */
	public void setReverse_qualifier(String reverse_qualifier) {
		this.reverse_qualifier = reverse_qualifier;
	}
	/**
	 * @return the active_flag
	 */
	public String getActive_flag() {
		return active_flag;
	}
	/**
	 * @param active_flag the active_flag to set
	 */
	public void setActive_flag(String active_flag) {
		this.active_flag = active_flag;
	}
	/**
	 * @return the retirement_types
	 */
	public String getRetirement_types() {
		return retirement_types;
	}
	/**
	 * @param retirement_types the retirement_types to set
	 */
	public void setRetirement_types(String retirement_types) {
		this.retirement_types = retirement_types;
	}
	/**
	 * @return the txnConfirmation_No
	 */
	public String getTxnConfirmation_No() {
		return txnConfirmation_No;
	}
	/**
	 * @param txnConfirmation_No the txnConfirmation_No to set
	 */
	public void setTxnConfirmation_No(String txnConfirmation_No) {
		this.txnConfirmation_No = txnConfirmation_No;
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

		retValue = "IRATransferTypeTO ( "
			+ super.toString() + TAB
			+ "from_plan_code = " + this.from_plan_code + TAB
			+ "to_plan_code = " + this.to_plan_code + TAB
			+ "txn_type = " + this.txn_type + TAB
			+ "retirement_mnemonic = " + this.retirement_mnemonic + TAB
			+ "retirement_mnemonic_desc = " + this.retirement_mnemonic_desc + TAB
			+ "qualifier = " + this.qualifier + TAB
			+ "reverse_qualifier = " + this.reverse_qualifier + TAB
			+ "active_flag = " + this.active_flag + TAB
			+ "retirement_types = " + this.retirement_types + TAB
			+ "txnConfirmation_No = " + this.txnConfirmation_No + TAB
			+ " )";

		return retValue;
	}

}