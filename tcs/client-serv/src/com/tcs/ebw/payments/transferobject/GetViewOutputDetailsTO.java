/*
 * Created on Fri Jun 05 14:13:50 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class GetViewOutputDetailsTO extends EBWTransferObject implements java.io.Serializable 
{

	private static final long serialVersionUID = -7736729717856182340L;

	//Payment Details..
	private BigDecimal paymentAmount = null;
	private Timestamp paymentDate = null;
	private String transferType=null;
	private String transferFrequency = null;
	private String confirmationNo = null;
	private String status = null;

	//Recurring transfer Details...
	private String durationValue = null;
	private Timestamp lastTransactionDate = null;
	private BigDecimal amountTransferred = null;
	private Double noOfTransfers = null;
	private Double payfreqpaymentcount=null;
	private BigDecimal payfreqlimit=null;
	private Timestamp payfreqenddate=null;
	private String payfrequency = null;

	//From Account Details..
	private String frombr_acct_no_fa = null;
	private String keyaccountnumber_from = null;
	private String frmAccNickName = null;
	private String frmAccFriendlyName = null;

	//To Account Details..
	private String tobr_acct_no_fa = null;
	private String keyaccountnumber_to = null;
	private String toAccNickName = null;
	private String toAccFriendlyName = null;

	//External account details..
	private String paypayeename1=null;
	private String paypayeebankcode=null;
	private String paypayee_acct_type=null;
	private String payeeaccnum=null;
	private String paypayeeid=null;
	private String extAccOwner=null;

	//Loan Account ...
	private String loanAcnt = null;

	/**
	 * @return the loanAcnt
	 */
	public String getLoanAcnt() {
		return loanAcnt;
	}
	/**
	 * @param loanAcnt the loanAcnt to set
	 */
	public void setLoanAcnt(String loanAcnt) {
		this.loanAcnt = loanAcnt;
	}
	/**
	 * @return the paymentAmount
	 */
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	/**
	 * @param paymentAmount the paymentAmount to set
	 */
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	/**
	 * @return the paymentDate
	 */
	public Timestamp getPaymentDate() {
		return paymentDate;
	}
	/**
	 * @param paymentDate the paymentDate to set
	 */
	public void setPaymentDate(Timestamp paymentDate) {
		this.paymentDate = paymentDate;
	}
	/**
	 * @return the transferType
	 */
	public String getTransferType() {
		return transferType;
	}
	/**
	 * @param transferType the transferType to set
	 */
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	/**
	 * @return the transferFrequency
	 */
	public String getTransferFrequency() {
		return transferFrequency;
	}
	/**
	 * @param transferFrequency the transferFrequency to set
	 */
	public void setTransferFrequency(String transferFrequency) {
		this.transferFrequency = transferFrequency;
	}
	/**
	 * @return the confirmationNo
	 */
	public String getConfirmationNo() {
		return confirmationNo;
	}
	/**
	 * @param confirmationNo the confirmationNo to set
	 */
	public void setConfirmationNo(String confirmationNo) {
		this.confirmationNo = confirmationNo;
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
	 * @return the durationValue
	 */
	public String getDurationValue() {
		return durationValue;
	}
	/**
	 * @param durationValue the durationValue to set
	 */
	public void setDurationValue(String durationValue) {
		this.durationValue = durationValue;
	}
	/**
	 * @return the lastTransactionDate
	 */
	public Timestamp getLastTransactionDate() {
		return lastTransactionDate;
	}
	/**
	 * @param lastTransactionDate the lastTransactionDate to set
	 */
	public void setLastTransactionDate(Timestamp lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}
	/**
	 * @return the amountTransferred
	 */
	public BigDecimal getAmountTransferred() {
		return amountTransferred;
	}
	/**
	 * @param amountTransferred the amountTransferred to set
	 */
	public void setAmountTransferred(BigDecimal amountTransferred) {
		this.amountTransferred = amountTransferred;
	}
	/**
	 * @return the noOfTransfers
	 */
	public Double getNoOfTransfers() {
		return noOfTransfers;
	}
	/**
	 * @param noOfTransfers the noOfTransfers to set
	 */
	public void setNoOfTransfers(Double noOfTransfers) {
		this.noOfTransfers = noOfTransfers;
	}
	/**
	 * @return the payfreqpaymentcount
	 */
	public Double getPayfreqpaymentcount() {
		return payfreqpaymentcount;
	}
	/**
	 * @param payfreqpaymentcount the payfreqpaymentcount to set
	 */
	public void setPayfreqpaymentcount(Double payfreqpaymentcount) {
		this.payfreqpaymentcount = payfreqpaymentcount;
	}
	/**
	 * @return the payfreqlimit
	 */
	public BigDecimal getPayfreqlimit() {
		return payfreqlimit;
	}
	/**
	 * @param payfreqlimit the payfreqlimit to set
	 */
	public void setPayfreqlimit(BigDecimal payfreqlimit) {
		this.payfreqlimit = payfreqlimit;
	}
	/**
	 * @return the payfreqenddate
	 */
	public Timestamp getPayfreqenddate() {
		return payfreqenddate;
	}
	/**
	 * @param payfreqenddate the payfreqenddate to set
	 */
	public void setPayfreqenddate(Timestamp payfreqenddate) {
		this.payfreqenddate = payfreqenddate;
	}
	/**
	 * @return the payfrequency
	 */
	public String getPayfrequency() {
		return payfrequency;
	}
	/**
	 * @param payfrequency the payfrequency to set
	 */
	public void setPayfrequency(String payfrequency) {
		this.payfrequency = payfrequency;
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
	 * @return the keyaccountnumber_from
	 */
	public String getKeyaccountnumber_from() {
		return keyaccountnumber_from;
	}
	/**
	 * @param keyaccountnumber_from the keyaccountnumber_from to set
	 */
	public void setKeyaccountnumber_from(String keyaccountnumber_from) {
		this.keyaccountnumber_from = keyaccountnumber_from;
	}
	/**
	 * @return the frmAccNickName
	 */
	public String getFrmAccNickName() {
		return frmAccNickName;
	}
	/**
	 * @param frmAccNickName the frmAccNickName to set
	 */
	public void setFrmAccNickName(String frmAccNickName) {
		this.frmAccNickName = frmAccNickName;
	}
	/**
	 * @return the frmAccFriendlyName
	 */
	public String getFrmAccFriendlyName() {
		return frmAccFriendlyName;
	}
	/**
	 * @param frmAccFriendlyName the frmAccFriendlyName to set
	 */
	public void setFrmAccFriendlyName(String frmAccFriendlyName) {
		this.frmAccFriendlyName = frmAccFriendlyName;
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
	 * @return the keyaccountnumber_to
	 */
	public String getKeyaccountnumber_to() {
		return keyaccountnumber_to;
	}
	/**
	 * @param keyaccountnumber_to the keyaccountnumber_to to set
	 */
	public void setKeyaccountnumber_to(String keyaccountnumber_to) {
		this.keyaccountnumber_to = keyaccountnumber_to;
	}
	/**
	 * @return the toAccNickName
	 */
	public String getToAccNickName() {
		return toAccNickName;
	}
	/**
	 * @param toAccNickName the toAccNickName to set
	 */
	public void setToAccNickName(String toAccNickName) {
		this.toAccNickName = toAccNickName;
	}
	/**
	 * @return the toAccFriendlyName
	 */
	public String getToAccFriendlyName() {
		return toAccFriendlyName;
	}
	/**
	 * @param toAccFriendlyName the toAccFriendlyName to set
	 */
	public void setToAccFriendlyName(String toAccFriendlyName) {
		this.toAccFriendlyName = toAccFriendlyName;
	}
	/**
	 * @return the paypayeename1
	 */
	public String getPaypayeename1() {
		return paypayeename1;
	}
	/**
	 * @param paypayeename1 the paypayeename1 to set
	 */
	public void setPaypayeename1(String paypayeename1) {
		this.paypayeename1 = paypayeename1;
	}
	/**
	 * @return the paypayeebankcode
	 */
	public String getPaypayeebankcode() {
		return paypayeebankcode;
	}
	/**
	 * @param paypayeebankcode the paypayeebankcode to set
	 */
	public void setPaypayeebankcode(String paypayeebankcode) {
		this.paypayeebankcode = paypayeebankcode;
	}
	/**
	 * @return the paypayee_acct_type
	 */
	public String getPaypayee_acct_type() {
		return paypayee_acct_type;
	}
	/**
	 * @param paypayee_acct_type the paypayee_acct_type to set
	 */
	public void setPaypayee_acct_type(String paypayee_acct_type) {
		this.paypayee_acct_type = paypayee_acct_type;
	}
	/**
	 * @return the payeeaccnum
	 */
	public String getPayeeaccnum() {
		return payeeaccnum;
	}
	/**
	 * @param payeeaccnum the payeeaccnum to set
	 */
	public void setPayeeaccnum(String payeeaccnum) {
		this.payeeaccnum = payeeaccnum;
	}
	/**
	 * @return the paypayeeid
	 */
	public String getPaypayeeid() {
		return paypayeeid;
	}
	/**
	 * @param paypayeeid the paypayeeid to set
	 */
	public void setPaypayeeid(String paypayeeid) {
		this.paypayeeid = paypayeeid;
	}
	/**
	 * @return the extAccOwner
	 */
	public String getExtAccOwner() {
		return extAccOwner;
	}
	/**
	 * @param extAccOwner the extAccOwner to set
	 */
	public void setExtAccOwner(String extAccOwner) {
		this.extAccOwner = extAccOwner;
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

		retValue = "GetViewOutputDetailsTO ( "
			+ super.toString() + TAB
			+ "paymentAmount = " + this.paymentAmount + TAB
			+ "paymentDate = " + this.paymentDate + TAB
			+ "transferType = " + this.transferType + TAB
			+ "transferFrequency = " + this.transferFrequency + TAB
			+ "confirmationNo = " + this.confirmationNo + TAB
			+ "status = " + this.status + TAB
			+ "durationValue = " + this.durationValue + TAB
			+ "lastTransactionDate = " + this.lastTransactionDate + TAB
			+ "amountTransferred = " + this.amountTransferred + TAB
			+ "noOfTransfers = " + this.noOfTransfers + TAB
			+ "payfreqpaymentcount = " + this.payfreqpaymentcount + TAB
			+ "payfreqlimit = " + this.payfreqlimit + TAB
			+ "payfreqenddate = " + this.payfreqenddate + TAB
			+ "payfrequency = " + this.payfrequency + TAB
			+ "frombr_acct_no_fa = " + this.frombr_acct_no_fa + TAB
			+ "keyaccountnumber_from = " + this.keyaccountnumber_from + TAB
			+ "frmAccNickName = " + this.frmAccNickName + TAB
			+ "frmAccFriendlyName = " + this.frmAccFriendlyName + TAB
			+ "tobr_acct_no_fa = " + this.tobr_acct_no_fa + TAB
			+ "keyaccountnumber_to = " + this.keyaccountnumber_to + TAB
			+ "toAccNickName = " + this.toAccNickName + TAB
			+ "toAccFriendlyName = " + this.toAccFriendlyName + TAB
			+ "paypayeename1 = " + this.paypayeename1 + TAB
			+ "paypayeebankcode = " + this.paypayeebankcode + TAB
			+ "paypayee_acct_type = " + this.paypayee_acct_type + TAB
			+ "payeeaccnum = " + this.payeeaccnum + TAB
			+ "paypayeeid = " + this.paypayeeid + TAB
			+ "extAccOwner = " + this.extAccOwner + TAB
			+ "loanAcnt = " + this.loanAcnt + TAB
			+ " )";

		return retValue;
	}
}