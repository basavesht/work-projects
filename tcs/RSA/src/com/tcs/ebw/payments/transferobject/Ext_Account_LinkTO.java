/**
 * 
 */
package com.tcs.ebw.payments.transferobject;

import java.sql.Timestamp;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class Ext_Account_LinkTO  extends EBWTransferObject implements java.io.Serializable {

	private static final long serialVersionUID = 6966468940741215149L;
	private String ext_reference_id = null;
	private String int_key_account_no = null;
	private String int_office = null;
	private String int_fa = null;
	private String int_account = null;
	private String approver_name = null;
	private String approver_id = null;
	private Timestamp confirmation_date = null;

	/**
	 * @return the ext_reference_id
	 */
	public String getExt_reference_id() {
		return ext_reference_id;
	}
	/**
	 * @param ext_reference_id the ext_reference_id to set
	 */
	public void setExt_reference_id(String ext_reference_id) {
		this.ext_reference_id = ext_reference_id;
	}
	/**
	 * @return the int_key_account_no
	 */
	public String getInt_key_account_no() {
		return int_key_account_no;
	}
	/**
	 * @param int_key_account_no the int_key_account_no to set
	 */
	public void setInt_key_account_no(String int_key_account_no) {
		this.int_key_account_no = int_key_account_no;
	}
	/**
	 * @return the int_office
	 */
	public String getInt_office() {
		return int_office;
	}
	/**
	 * @param int_office the int_office to set
	 */
	public void setInt_office(String int_office) {
		this.int_office = int_office;
	}
	/**
	 * @return the int_fa
	 */
	public String getInt_fa() {
		return int_fa;
	}
	/**
	 * @param int_fa the int_fa to set
	 */
	public void setInt_fa(String int_fa) {
		this.int_fa = int_fa;
	}
	/**
	 * @return the int_account
	 */
	public String getInt_account() {
		return int_account;
	}
	/**
	 * @param int_account the int_account to set
	 */
	public void setInt_account(String int_account) {
		this.int_account = int_account;
	}
	/**
	 * @return the approver_name
	 */
	public String getApprover_name() {
		return approver_name;
	}
	/**
	 * @param approver_name the approver_name to set
	 */
	public void setApprover_name(String approver_name) {
		this.approver_name = approver_name;
	}
	/**
	 * @return the approver_id
	 */
	public String getApprover_id() {
		return approver_id;
	}
	/**
	 * @param approver_id the approver_id to set
	 */
	public void setApprover_id(String approver_id) {
		this.approver_id = approver_id;
	}
	/**
	 * @return the confirmation_date
	 */
	public Timestamp getConfirmation_date() {
		return confirmation_date;
	}
	/**
	 * @param confirmation_date the confirmation_date to set
	 */
	public void setConfirmation_date(Timestamp confirmation_date) {
		this.confirmation_date = confirmation_date;
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

		retValue = "Ext_Account_Link ( "
			+ super.toString() + TAB
			+ "ext_reference_id = " + this.ext_reference_id + TAB
			+ "int_key_account_no = " + this.int_key_account_no + TAB
			+ "int_office = " + this.int_office + TAB
			+ "int_fa = " + this.int_fa + TAB
			+ "int_account = " + this.int_account + TAB
			+ "approver_name = " + this.approver_name + TAB
			+ "approver_id = " + this.approver_id + TAB
			+ "confirmation_date = " + this.confirmation_date + TAB
			+ " )";

		return retValue;
	}

}
