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
 *
 * **********************************************************
 */
public class NotificationOutDetails extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3619050767673290753L;

	// For Transaction Notifications Input..
	private String paypayeeaccnum=null;
	private String paydebitaccnum=null;
	private String currency=null;
	private BigDecimal amount=null;
	private Timestamp executionDate=null;
	private String frequency=null;
	private String untilValue=null;
	private String confirmationNumber=null;
	private String txnType=null;
	private String srcKeyAcntNumber=null;
	private String dstnKeyAcntNumber=null;
	private String lifeTimeUserId=null;
	private String trial_depo=null;
	private String externalAccNickName = null;
	private Timestamp untilEndDate = null;
	private Double untilNoOfTransfers = null;
	private BigDecimal untilAmountLimit = null;
	private Timestamp skippedExecutionDate = null;
	private Timestamp nextExecutionDate = null;
	private String tobr_acct_no_fa = null;
	private String frombr_acct_no_fa = null;

	// For External Accounts ...
	private String externalAccount=null;
	private Timestamp accountInitDate=null;
	
	/**
	 * @return the paypayeeaccnum
	 */
	public String getPaypayeeaccnum() {
		return paypayeeaccnum;
	}

	/**
	 * @param paypayeeaccnum the paypayeeaccnum to set
	 */
	public void setPaypayeeaccnum(String paypayeeaccnum) {
		this.paypayeeaccnum = paypayeeaccnum;
	}

	/**
	 * @return the paydebitaccnum
	 */
	public String getPaydebitaccnum() {
		return paydebitaccnum;
	}

	/**
	 * @param paydebitaccnum the paydebitaccnum to set
	 */
	public void setPaydebitaccnum(String paydebitaccnum) {
		this.paydebitaccnum = paydebitaccnum;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
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
	 * @return the executionDate
	 */
	public Timestamp getExecutionDate() {
		return executionDate;
	}

	/**
	 * @param executionDate the executionDate to set
	 */
	public void setExecutionDate(Timestamp executionDate) {
		this.executionDate = executionDate;
	}

	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	/**
	 * @return the untilValue
	 */
	public String getUntilValue() {
		return untilValue;
	}

	/**
	 * @param untilValue the untilValue to set
	 */
	public void setUntilValue(String untilValue) {
		this.untilValue = untilValue;
	}

	/**
	 * @return the confirmationNumber
	 */
	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	/**
	 * @param confirmationNumber the confirmationNumber to set
	 */
	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	/**
	 * @return the txnType
	 */
	public String getTxnType() {
		return txnType;
	}

	/**
	 * @param txnType the txnType to set
	 */
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	/**
	 * @return the srcKeyAcntNumber
	 */
	public String getSrcKeyAcntNumber() {
		return srcKeyAcntNumber;
	}

	/**
	 * @param srcKeyAcntNumber the srcKeyAcntNumber to set
	 */
	public void setSrcKeyAcntNumber(String srcKeyAcntNumber) {
		this.srcKeyAcntNumber = srcKeyAcntNumber;
	}

	/**
	 * @return the dstnKeyAcntNumber
	 */
	public String getDstnKeyAcntNumber() {
		return dstnKeyAcntNumber;
	}

	/**
	 * @param dstnKeyAcntNumber the dstnKeyAcntNumber to set
	 */
	public void setDstnKeyAcntNumber(String dstnKeyAcntNumber) {
		this.dstnKeyAcntNumber = dstnKeyAcntNumber;
	}

	/**
	 * @return the lifeTimeUserId
	 */
	public String getLifeTimeUserId() {
		return lifeTimeUserId;
	}

	/**
	 * @param lifeTimeUserId the lifeTimeUserId to set
	 */
	public void setLifeTimeUserId(String lifeTimeUserId) {
		this.lifeTimeUserId = lifeTimeUserId;
	}

	/**
	 * @return the trial_depo
	 */
	public String getTrial_depo() {
		return trial_depo;
	}

	/**
	 * @param trial_depo the trial_depo to set
	 */
	public void setTrial_depo(String trial_depo) {
		this.trial_depo = trial_depo;
	}

	/**
	 * @return the externalAccNickName
	 */
	public String getExternalAccNickName() {
		return externalAccNickName;
	}

	/**
	 * @param externalAccNickName the externalAccNickName to set
	 */
	public void setExternalAccNickName(String externalAccNickName) {
		this.externalAccNickName = externalAccNickName;
	}

	/**
	 * @return the untilEndDate
	 */
	public Timestamp getUntilEndDate() {
		return untilEndDate;
	}

	/**
	 * @param untilEndDate the untilEndDate to set
	 */
	public void setUntilEndDate(Timestamp untilEndDate) {
		this.untilEndDate = untilEndDate;
	}

	/**
	 * @return the untilNoOfTransfers
	 */
	public Double getUntilNoOfTransfers() {
		return untilNoOfTransfers;
	}

	/**
	 * @param untilNoOfTransfers the untilNoOfTransfers to set
	 */
	public void setUntilNoOfTransfers(Double untilNoOfTransfers) {
		this.untilNoOfTransfers = untilNoOfTransfers;
	}

	/**
	 * @return the untilAmountLimit
	 */
	public BigDecimal getUntilAmountLimit() {
		return untilAmountLimit;
	}

	/**
	 * @param untilAmountLimit the untilAmountLimit to set
	 */
	public void setUntilAmountLimit(BigDecimal untilAmountLimit) {
		this.untilAmountLimit = untilAmountLimit;
	}

	/**
	 * @return the skippedExecutionDate
	 */
	public Timestamp getSkippedExecutionDate() {
		return skippedExecutionDate;
	}

	/**
	 * @param skippedExecutionDate the skippedExecutionDate to set
	 */
	public void setSkippedExecutionDate(Timestamp skippedExecutionDate) {
		this.skippedExecutionDate = skippedExecutionDate;
	}

	/**
	 * @return the nextExecutionDate
	 */
	public Timestamp getNextExecutionDate() {
		return nextExecutionDate;
	}

	/**
	 * @param nextExecutionDate the nextExecutionDate to set
	 */
	public void setNextExecutionDate(Timestamp nextExecutionDate) {
		this.nextExecutionDate = nextExecutionDate;
	}

	/**
	 * @return the tobr_acct_no_fa
	 */
	public String getTobr_acct_no_fa() {
		return tobr_acct_no_fa;
	}

	/**
	 * @param tobr_acct_no_fa the tobr_acct_no_fa to set
	 */
	public void setTobr_acct_no_fa(String tobr_acct_no_fa) {
		this.tobr_acct_no_fa = tobr_acct_no_fa;
	}

	/**
	 * @return the frombr_acct_no_fa
	 */
	public String getFrombr_acct_no_fa() {
		return frombr_acct_no_fa;
	}

	/**
	 * @param frombr_acct_no_fa the frombr_acct_no_fa to set
	 */
	public void setFrombr_acct_no_fa(String frombr_acct_no_fa) {
		this.frombr_acct_no_fa = frombr_acct_no_fa;
	}

	/**
	 * @return the externalAccount
	 */
	public String getExternalAccount() {
		return externalAccount;
	}

	/**
	 * @param externalAccount the externalAccount to set
	 */
	public void setExternalAccount(String externalAccount) {
		this.externalAccount = externalAccount;
	}

	/**
	 * @return the accountInitDate
	 */
	public Timestamp getAccountInitDate() {
		return accountInitDate;
	}

	/**
	 * @param accountInitDate the accountInitDate to set
	 */
	public void setAccountInitDate(Timestamp accountInitDate) {
		this.accountInitDate = accountInitDate;
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
	        + " )";
	
	    return retValue;
	}
}