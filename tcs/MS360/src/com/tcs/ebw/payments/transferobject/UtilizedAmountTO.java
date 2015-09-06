package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class UtilizedAmountTO extends EBWTransferObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -311260902876444803L;
	private String transfer_type =null;
	private String from_account = null;
	private String status =null;
	private Timestamp startDate =null;
	private Timestamp endDate =null;
	private String same_name_flg =null;
	private String auth_mode =null;
	private BigDecimal utilized_amount = null;

	/**
	 * @return the transfer_type
	 */
	public String getTransfer_type() {
		return transfer_type;
	}
	/**
	 * @param transfer_type the transfer_type to set
	 */
	public void setTransfer_type(String transfer_type) {
		this.transfer_type = transfer_type;
	}
	/**
	 * @return the from_account
	 */
	public String getFrom_account() {
		return from_account;
	}
	/**
	 * @param from_account the from_account to set
	 */
	public void setFrom_account(String from_account) {
		this.from_account = from_account;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the startDate
	 */
	public Timestamp getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Timestamp getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the same_name_flg
	 */
	public String getSame_name_flg() {
		return same_name_flg;
	}
	/**
	 * @param same_name_flg the same_name_flg to set
	 */
	public void setSame_name_flg(String same_name_flg) {
		this.same_name_flg = same_name_flg;
	}
	/**
	 * @return the auth_mode
	 */
	public String getAuth_mode() {
		return auth_mode;
	}
	/**
	 * @param auth_mode the auth_mode to set
	 */
	public void setAuth_mode(String auth_mode) {
		this.auth_mode = auth_mode;
	}
	/**
	 * @return the utilized_amount
	 */
	public BigDecimal getUtilized_amount() {
		return utilized_amount;
	}
	/**
	 * @param utilized_amount the utilized_amount to set
	 */
	public void setUtilized_amount(BigDecimal utilized_amount) {
		this.utilized_amount = utilized_amount;
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
		final String TAB = "    ";

		String retValue = "";

		retValue = "UtilizedAmountTO ( "
			+ super.toString() + TAB
			+ "transfer_type = " + this.transfer_type + TAB
			+ "from_account = " + this.from_account + TAB
			+ "status = " + this.status + TAB
			+ "startDate = " + this.startDate + TAB
			+ "endDate = " + this.endDate + TAB
			+ "same_name_flg = " + this.same_name_flg + TAB
			+ "auth_mode = " + this.auth_mode + TAB
			+ "utilized_amount = " + this.utilized_amount + TAB
			+ " )";

		return retValue;
	}

}