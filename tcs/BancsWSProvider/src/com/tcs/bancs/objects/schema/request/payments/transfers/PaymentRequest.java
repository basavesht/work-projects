/**
 * 
 */
package com.tcs.bancs.objects.schema.request.payments.transfers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author 259245
 *
 */
public class PaymentRequest implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6457917302852527320L;
	private TransferAction transferAction = null;
	private TransferType transferType = null;
	private ArrayList<Account> account = new ArrayList<Account>();
	private BigDecimal amount = null;
	private Date transferDate = null;
	private String comments = null;
	private TransferSchedule transferSchedule = null;
	private RecurringDetails recurringDetails = null;
	private String txnConfirmationNo = null;

	/**
	 * @return the transferAction
	 */
	public TransferAction getTransferAction() {
		return transferAction;
	}
	/**
	 * @param transferAction the transferAction to set
	 */
	public void setTransferAction(TransferAction transferAction) {
		this.transferAction = transferAction;
	}
	/**
	 * @return the transferType
	 */
	public TransferType getTransferType() {
		return transferType;
	}
	/**
	 * @param transferType the transferType to set
	 */
	public void setTransferType(TransferType transferType) {
		this.transferType = transferType;
	}
	/**
	 * @return the account
	 */
	public ArrayList<Account> getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(ArrayList<Account> account) {
		this.account = account;
	}
	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * @return the transferDate
	 */
	public Date getTransferDate() {
		return transferDate;
	}
	/**
	 * @param transferDate the transferDate to set
	 */
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return the transferSchedule
	 */
	public TransferSchedule getTransferSchedule() {
		return transferSchedule;
	}
	/**
	 * @param transferSchedule the transferSchedule to set
	 */
	public void setTransferSchedule(TransferSchedule transferSchedule) {
		this.transferSchedule = transferSchedule;
	}
	/**
	 * @return the recurringDetail
	 */
	public RecurringDetails getRecurringDetails() {
		return recurringDetails;
	}
	/**
	 * @param recurringDetail the recurringDetail to set
	 */
	public void setRecurringDetails(RecurringDetails recurringDetails) {
		this.recurringDetails = recurringDetails;
	}
	/**
	 * @return the txnConfirmationNo
	 */
	public String getTxnConfirmationNo() {
		return txnConfirmationNo;
	}
	/**
	 * @param txnConfirmationNo the txnConfirmationNo to set
	 */
	public void setTxnConfirmationNo(String txnConfirmationNo) {
		this.txnConfirmationNo = txnConfirmationNo;
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

		retValue = "PaymentRequest ( "
				+ super.toString() + TAB
				+ "transferAction = " + this.transferAction + TAB
				+ "transferType = " + this.transferType + TAB
				+ "account = " + this.account + TAB
				+ "amount = " + this.amount + TAB
				+ "transferDate = " + this.transferDate + TAB
				+ "comments = " + this.comments + TAB
				+ "transferSchedule = " + this.transferSchedule + TAB
				+ "recurringDetail = " + this.recurringDetails + TAB
				+ "txnConfirmationNo = " + this.txnConfirmationNo + TAB
				+ " )";

		return retValue;
	}





}
