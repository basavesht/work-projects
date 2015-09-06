/**
 * 
 */
package com.tcs.ebw.payments.transferobject;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *  224703			30-05-2011		P3				 3rd Party ACH - Alerts
 * **********************************************************
 */
public class NotificationOutDetails extends EBWTransferObject implements java.io.Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2446798578187487633L;

	// For Transaction Notifications Input..
	private String paypayeeaccnum = null;
	private String paydebitaccnum = null;
	private String currency = null;
	private BigDecimal amount = null;
	private Timestamp executionDate = null;
	private String frequency = null;
	private String untilValue = null;
	private String confirmationNumber = null;
	private String txnType = null;
	private String srcKeyAcntNumber = null;
	private String dstnKeyAcntNumber = null;
	private String lifeTimeUserId = null;
	private String trial_depo = null;
	private String externalAccNickName = null;
	private Timestamp untilEndDate = null;
	private Double untilNoOfTransfers = null;
	private BigDecimal untilAmountLimit = null;
	private Timestamp skippedExecutionDate = null;
	private Timestamp nextExecutionDate = null;
	private String tobr_acct_no_fa = null;
	private String frombr_acct_no_fa = null;

	// For External Accounts ...
	private String externalAccount = null;
	private Timestamp accountInitDate = null;
	private String primaryAccount = null;
	private String accountOwnerMSSB = null;
	private String bankName = null;

	public String getPaypayeeaccnum() {
		return paypayeeaccnum;
	}
	public void setPaypayeeaccnum(String paypayeeaccnum) {
		this.paypayeeaccnum = paypayeeaccnum;
	}
	public String getPaydebitaccnum() {
		return paydebitaccnum;
	}
	public void setPaydebitaccnum(String paydebitaccnum) {
		this.paydebitaccnum = paydebitaccnum;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Timestamp getExecutionDate() {
		return executionDate;
	}
	public void setExecutionDate(Timestamp executionDate) {
		this.executionDate = executionDate;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getUntilValue() {
		return untilValue;
	}
	public void setUntilValue(String untilValue) {
		this.untilValue = untilValue;
	}
	public String getConfirmationNumber() {
		return confirmationNumber;
	}
	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getSrcKeyAcntNumber() {
		return srcKeyAcntNumber;
	}
	public void setSrcKeyAcntNumber(String srcKeyAcntNumber) {
		this.srcKeyAcntNumber = srcKeyAcntNumber;
	}
	public String getDstnKeyAcntNumber() {
		return dstnKeyAcntNumber;
	}
	public void setDstnKeyAcntNumber(String dstnKeyAcntNumber) {
		this.dstnKeyAcntNumber = dstnKeyAcntNumber;
	}
	public String getLifeTimeUserId() {
		return lifeTimeUserId;
	}
	public void setLifeTimeUserId(String lifeTimeUserId) {
		this.lifeTimeUserId = lifeTimeUserId;
	}
	public String getTrial_depo() {
		return trial_depo;
	}
	public void setTrial_depo(String trial_depo) {
		this.trial_depo = trial_depo;
	}
	public String getExternalAccNickName() {
		return externalAccNickName;
	}
	public void setExternalAccNickName(String externalAccNickName) {
		this.externalAccNickName = externalAccNickName;
	}
	public Timestamp getUntilEndDate() {
		return untilEndDate;
	}
	public void setUntilEndDate(Timestamp untilEndDate) {
		this.untilEndDate = untilEndDate;
	}
	public Double getUntilNoOfTransfers() {
		return untilNoOfTransfers;
	}
	public void setUntilNoOfTransfers(Double untilNoOfTransfers) {
		this.untilNoOfTransfers = untilNoOfTransfers;
	}
	public BigDecimal getUntilAmountLimit() {
		return untilAmountLimit;
	}
	public void setUntilAmountLimit(BigDecimal untilAmountLimit) {
		this.untilAmountLimit = untilAmountLimit;
	}
	public Timestamp getSkippedExecutionDate() {
		return skippedExecutionDate;
	}
	public void setSkippedExecutionDate(Timestamp skippedExecutionDate) {
		this.skippedExecutionDate = skippedExecutionDate;
	}
	public Timestamp getNextExecutionDate() {
		return nextExecutionDate;
	}
	public void setNextExecutionDate(Timestamp nextExecutionDate) {
		this.nextExecutionDate = nextExecutionDate;
	}
	public String getTobr_acct_no_fa() {
		return tobr_acct_no_fa;
	}
	public void setTobr_acct_no_fa(String tobr_acct_no_fa) {
		this.tobr_acct_no_fa = tobr_acct_no_fa;
	}
	public String getFrombr_acct_no_fa() {
		return frombr_acct_no_fa;
	}
	public void setFrombr_acct_no_fa(String frombr_acct_no_fa) {
		this.frombr_acct_no_fa = frombr_acct_no_fa;
	}
	public String getExternalAccount() {
		return externalAccount;
	}
	public void setExternalAccount(String externalAccount) {
		this.externalAccount = externalAccount;
	}
	public Timestamp getAccountInitDate() {
		return accountInitDate;
	}
	public void setAccountInitDate(Timestamp accountInitDate) {
		this.accountInitDate = accountInitDate;
	}
	public String getPrimaryAccount() {
		return primaryAccount;
	}
	public void setPrimaryAccount(String primaryAccount) {
		this.primaryAccount = primaryAccount;
	}
	public String getAccountOwnerMSSB() {
		return accountOwnerMSSB;
	}
	public void setAccountOwnerMSSB(String accountOwnerMSSB) {
		this.accountOwnerMSSB = accountOwnerMSSB;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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

		retValue = "NotificationOutDetails ( "
			+ super.toString() + TAB
			+ "paypayeeaccnum = " + this.paypayeeaccnum + TAB
			+ "paydebitaccnum = " + this.paydebitaccnum + TAB
			+ "currency = " + this.currency + TAB
			+ "amount = " + this.amount + TAB
			+ "executionDate = " + this.executionDate + TAB
			+ "frequency = " + this.frequency + TAB
			+ "untilValue = " + this.untilValue + TAB
			+ "confirmationNumber = " + this.confirmationNumber + TAB
			+ "txnType = " + this.txnType + TAB
			+ "srcKeyAcntNumber = " + this.srcKeyAcntNumber + TAB
			+ "dstnKeyAcntNumber = " + this.dstnKeyAcntNumber + TAB
			+ "lifeTimeUserId = " + this.lifeTimeUserId + TAB
			+ "trial_depo = " + this.trial_depo + TAB
			+ "externalAccNickName = " + this.externalAccNickName + TAB
			+ "untilEndDate = " + this.untilEndDate + TAB
			+ "untilNoOfTransfers = " + this.untilNoOfTransfers + TAB
			+ "untilAmountLimit = " + this.untilAmountLimit + TAB
			+ "skippedExecutionDate = " + this.skippedExecutionDate + TAB
			+ "nextExecutionDate = " + this.nextExecutionDate + TAB
			+ "tobr_acct_no_fa = " + this.tobr_acct_no_fa + TAB
			+ "frombr_acct_no_fa = " + this.frombr_acct_no_fa + TAB
			+ "externalAccount = " + this.externalAccount + TAB
			+ "accountInitDate = " + this.accountInitDate + TAB
			+ "primaryAccount = " + this.primaryAccount + TAB
			+ "accountOwnerMSSB = " + this.accountOwnerMSSB + TAB
			+ "bankName = " + this.bankName + TAB
			+ " )";

		return retValue;
	}

}