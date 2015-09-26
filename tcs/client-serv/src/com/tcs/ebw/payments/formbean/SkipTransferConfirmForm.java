/*
 * Created on Tue May 26 15:43:53 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.formbean;

import org.apache.struts.action.ActionMapping;

import com.tcs.ebw.mvc.validator.EbwForm;

import java.util.HashMap;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class SkipTransferConfirmForm extends EbwForm implements java.io.Serializable {

	/**
	 * Form Bean Constructor. 
	 */ 
	public SkipTransferConfirmForm () {
		try {
			setState("SkipTransferConfirm_INIT");

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void populateCollections() throws Exception{
	}

	public void copyCollections(SkipTransferConfirmForm srcForm) {
	}

	// Instance Variables
	private String cancelTransFromAcc = null;
	private String cancelTransToAcc = null;
	private String cancelTransAmount = null;
	private String cancelTransTransferDate = null;
	private String nextTransferAmount = null;
	private String nextTransferDateVal = null;
	private String businessDate = null;
	private String txnBatchRefNumber = null;
	private String txnPayPayRefNumber = null;
	private String parentTxnNumber = null;
	private String frequencyValue = null;
	private String durationValue = null;
	private String paymentstatus = null;
	private String servicetypecode = null;
	private String transactiontype = null;
	private String paypayeeaccnum = null;
	private String payeeName = null;
	private String bankCode = null;
	private String payeeRefId = null;
	private String payeeAdd1 = null;
	private String payeeAdd2 = null;
	private String payeeAdd3 = null;
	private String payeeAccType = null;
	private String payeeId = null;
	private String recurringFreq = null;
	private String fromKeyAccNumber = null;
	private String toKeyAccNumber = null;
	private String payDebitAcc = null;
	private String cancelTransFrequency = null;
	private String cancelTransRepeat = null;
	private String cancelTransDollarsTransfered = null;
	private String cancelTransNoOfTransfers = null;
	private String currencyCode = null;
	private String prefered_previous_txn_dt = null;
	private String fromAcc_Tier = null;
	private String toAcc_Tier = null;
	private String fromAccNo_br_fa = null;
	private String toAccNo_br_fa = null;
	private String from_NickName = null;
	private String to_NickName = null;
	private String from_FriendlyName = null;
	private String to_FriendlyName = null;
	private String parentTxnDate = null;

	/**
	 * Set the cancelTransFromAcc String.
	 * @param cancelTransFromAcc
	 */
	public void setCancelTransFromAcc(String cancelTransFromAcc) {
		this.cancelTransFromAcc=cancelTransFromAcc;
	}
	/**
	 * Get the cancelTransFromAcc String.
	 * @return cancelTransFromAcc
	 */
	public String getCancelTransFromAcc() {
		return this.cancelTransFromAcc;
	}

	/**
	 * Set the cancelTransToAcc String.
	 * @param cancelTransToAcc
	 */
	public void setCancelTransToAcc(String cancelTransToAcc) {
		this.cancelTransToAcc=cancelTransToAcc;
	}
	/**
	 * Get the cancelTransToAcc String.
	 * @return cancelTransToAcc
	 */
	public String getCancelTransToAcc() {
		return this.cancelTransToAcc;
	}

	/**
	 * Set the cancelTransAmount String.
	 * @param cancelTransAmount
	 */
	public void setCancelTransAmount(String cancelTransAmount) {
		this.cancelTransAmount=cancelTransAmount;
	}
	/**
	 * Get the cancelTransAmount String.
	 * @return cancelTransAmount
	 */
	public String getCancelTransAmount() {
		return this.cancelTransAmount;
	}

	/**
	 * Set the cancelTransTransferDate String.
	 * @param cancelTransTransferDate
	 */
	public void setCancelTransTransferDate(String cancelTransTransferDate) {
		this.cancelTransTransferDate=cancelTransTransferDate;
	}
	/**
	 * Get the cancelTransTransferDate String.
	 * @return cancelTransTransferDate
	 */
	public String getCancelTransTransferDate() {
		return this.cancelTransTransferDate;
	}

	/**
	 * Set the nextTransferAmount String.
	 * @param nextTransferAmount
	 */
	public void setNextTransferAmount(String nextTransferAmount) {
		this.nextTransferAmount=nextTransferAmount;
	}
	/**
	 * Get the nextTransferAmount String.
	 * @return nextTransferAmount
	 */
	public String getNextTransferAmount() {
		return this.nextTransferAmount;
	}

	/**
	 * Set the nextTransferDateVal String.
	 * @param nextTransferDateVal
	 */
	public void setNextTransferDateVal(String nextTransferDateVal) {
		this.nextTransferDateVal=nextTransferDateVal;
	}
	/**
	 * Get the nextTransferDateVal String.
	 * @return nextTransferDateVal
	 */
	public String getNextTransferDateVal() {
		return this.nextTransferDateVal;
	}

	/**
	 * Set the businessDate String.
	 * @param businessDate
	 */
	public void setBusinessDate(String businessDate) {
		this.businessDate=businessDate;
	}
	/**
	 * Get the businessDate String.
	 * @return businessDate
	 */
	public String getBusinessDate() {
		return this.businessDate;
	}

	/**
	 * Set the txnBatchRefNumber String.
	 * @param txnBatchRefNumber
	 */
	public void setTxnBatchRefNumber(String txnBatchRefNumber) {
		this.txnBatchRefNumber=txnBatchRefNumber;
	}
	/**
	 * Get the txnBatchRefNumber String.
	 * @return txnBatchRefNumber
	 */
	public String getTxnBatchRefNumber() {
		return this.txnBatchRefNumber;
	}

	/**
	 * Set the txnPayPayRefNumber String.
	 * @param txnPayPayRefNumber
	 */
	public void setTxnPayPayRefNumber(String txnPayPayRefNumber) {
		this.txnPayPayRefNumber=txnPayPayRefNumber;
	}
	/**
	 * Get the txnPayPayRefNumber String.
	 * @return txnPayPayRefNumber
	 */
	public String getTxnPayPayRefNumber() {
		return this.txnPayPayRefNumber;
	}

	/**
	 * Set the parentTxnNumber String.
	 * @param parentTxnNumber
	 */
	public void setParentTxnNumber(String parentTxnNumber) {
		this.parentTxnNumber=parentTxnNumber;
	}
	/**
	 * Get the parentTxnNumber String.
	 * @return parentTxnNumber
	 */
	public String getParentTxnNumber() {
		return this.parentTxnNumber;
	}

	/**
	 * Set the frequencyValue String.
	 * @param frequencyValue
	 */
	public void setFrequencyValue(String frequencyValue) {
		this.frequencyValue=frequencyValue;
	}
	/**
	 * Get the frequencyValue String.
	 * @return frequencyValue
	 */
	public String getFrequencyValue() {
		return this.frequencyValue;
	}

	/**
	 * Set the durationValue String.
	 * @param durationValue
	 */
	public void setDurationValue(String durationValue) {
		this.durationValue=durationValue;
	}
	/**
	 * Get the durationValue String.
	 * @return durationValue
	 */
	public String getDurationValue() {
		return this.durationValue;
	}

	/**
	 * Set the paymentstatus String.
	 * @param paymentstatus
	 */
	public void setPaymentstatus(String paymentstatus) {
		this.paymentstatus=paymentstatus;
	}
	/**
	 * Get the paymentstatus String.
	 * @return paymentstatus
	 */
	public String getPaymentstatus() {
		return this.paymentstatus;
	}

	/**
	 * Set the servicetypecode String.
	 * @param servicetypecode
	 */
	public void setServicetypecode(String servicetypecode) {
		this.servicetypecode=servicetypecode;
	}
	/**
	 * Get the servicetypecode String.
	 * @return servicetypecode
	 */
	public String getServicetypecode() {
		return this.servicetypecode;
	}

	/**
	 * Set the transactiontype String.
	 * @param transactiontype
	 */
	public void setTransactiontype(String transactiontype) {
		this.transactiontype=transactiontype;
	}
	/**
	 * Get the transactiontype String.
	 * @return transactiontype
	 */
	public String getTransactiontype() {
		return this.transactiontype;
	}

	/**
	 * Set the paypayeeaccnum String.
	 * @param paypayeeaccnum
	 */
	public void setPaypayeeaccnum(String paypayeeaccnum) {
		this.paypayeeaccnum=paypayeeaccnum;
	}
	/**
	 * Get the paypayeeaccnum String.
	 * @return paypayeeaccnum
	 */
	public String getPaypayeeaccnum() {
		return this.paypayeeaccnum;
	}

	/**
	 * Set the payeeName String.
	 * @param payeeName
	 */
	public void setPayeeName(String payeeName) {
		this.payeeName=payeeName;
	}
	/**
	 * Get the payeeName String.
	 * @return payeeName
	 */
	public String getPayeeName() {
		return this.payeeName;
	}

	/**
	 * Set the bankCode String.
	 * @param bankCode
	 */
	public void setBankCode(String bankCode) {
		this.bankCode=bankCode;
	}
	/**
	 * Get the bankCode String.
	 * @return bankCode
	 */
	public String getBankCode() {
		return this.bankCode;
	}

	/**
	 * Set the payeeRefId String.
	 * @param payeeRefId
	 */
	public void setPayeeRefId(String payeeRefId) {
		this.payeeRefId=payeeRefId;
	}
	/**
	 * Get the payeeRefId String.
	 * @return payeeRefId
	 */
	public String getPayeeRefId() {
		return this.payeeRefId;
	}

	/**
	 * Set the payeeAdd1 String.
	 * @param payeeAdd1
	 */
	public void setPayeeAdd1(String payeeAdd1) {
		this.payeeAdd1=payeeAdd1;
	}
	/**
	 * Get the payeeAdd1 String.
	 * @return payeeAdd1
	 */
	public String getPayeeAdd1() {
		return this.payeeAdd1;
	}

	/**
	 * Set the payeeAdd2 String.
	 * @param payeeAdd2
	 */
	public void setPayeeAdd2(String payeeAdd2) {
		this.payeeAdd2=payeeAdd2;
	}
	/**
	 * Get the payeeAdd2 String.
	 * @return payeeAdd2
	 */
	public String getPayeeAdd2() {
		return this.payeeAdd2;
	}

	/**
	 * Set the payeeAdd3 String.
	 * @param payeeAdd3
	 */
	public void setPayeeAdd3(String payeeAdd3) {
		this.payeeAdd3=payeeAdd3;
	}
	/**
	 * Get the payeeAdd3 String.
	 * @return payeeAdd3
	 */
	public String getPayeeAdd3() {
		return this.payeeAdd3;
	}

	/**
	 * Set the payeeAccType String.
	 * @param payeeAccType
	 */
	public void setPayeeAccType(String payeeAccType) {
		this.payeeAccType=payeeAccType;
	}
	/**
	 * Get the payeeAccType String.
	 * @return payeeAccType
	 */
	public String getPayeeAccType() {
		return this.payeeAccType;
	}

	/**
	 * Set the payeeId String.
	 * @param payeeId
	 */
	public void setPayeeId(String payeeId) {
		this.payeeId=payeeId;
	}
	/**
	 * Get the payeeId String.
	 * @return payeeId
	 */
	public String getPayeeId() {
		return this.payeeId;
	}

	/**
	 * Set the recurringFreq String.
	 * @param recurringFreq
	 */
	public void setRecurringFreq(String recurringFreq) {
		this.recurringFreq=recurringFreq;
	}
	/**
	 * Get the recurringFreq String.
	 * @return recurringFreq
	 */
	public String getRecurringFreq() {
		return this.recurringFreq;
	}

	/**
	 * Set the fromKeyAccNumber String.
	 * @param fromKeyAccNumber
	 */
	public void setFromKeyAccNumber(String fromKeyAccNumber) {
		this.fromKeyAccNumber=fromKeyAccNumber;
	}
	/**
	 * Get the fromKeyAccNumber String.
	 * @return fromKeyAccNumber
	 */
	public String getFromKeyAccNumber() {
		return this.fromKeyAccNumber;
	}

	/**
	 * Set the toKeyAccNumber String.
	 * @param toKeyAccNumber
	 */
	public void setToKeyAccNumber(String toKeyAccNumber) {
		this.toKeyAccNumber=toKeyAccNumber;
	}
	/**
	 * Get the toKeyAccNumber String.
	 * @return toKeyAccNumber
	 */
	public String getToKeyAccNumber() {
		return this.toKeyAccNumber;
	}

	/**
	 * Set the payDebitAcc String.
	 * @param payDebitAcc
	 */
	public void setPayDebitAcc(String payDebitAcc) {
		this.payDebitAcc=payDebitAcc;
	}
	/**
	 * Get the payDebitAcc String.
	 * @return payDebitAcc
	 */
	public String getPayDebitAcc() {
		return this.payDebitAcc;
	}

	/**
	 * Set the cancelTransFrequency String.
	 * @param cancelTransFrequency
	 */
	public void setCancelTransFrequency(String cancelTransFrequency) {
		this.cancelTransFrequency=cancelTransFrequency;
	}
	/**
	 * Get the cancelTransFrequency String.
	 * @return cancelTransFrequency
	 */
	public String getCancelTransFrequency() {
		return this.cancelTransFrequency;
	}

	/**
	 * Set the cancelTransRepeat String.
	 * @param cancelTransRepeat
	 */
	public void setCancelTransRepeat(String cancelTransRepeat) {
		this.cancelTransRepeat=cancelTransRepeat;
	}
	/**
	 * Get the cancelTransRepeat String.
	 * @return cancelTransRepeat
	 */
	public String getCancelTransRepeat() {
		return this.cancelTransRepeat;
	}

	/**
	 * Set the cancelTransDollarsTransfered String.
	 * @param cancelTransDollarsTransfered
	 */
	public void setCancelTransDollarsTransfered(String cancelTransDollarsTransfered) {
		this.cancelTransDollarsTransfered=cancelTransDollarsTransfered;
	}
	/**
	 * Get the cancelTransDollarsTransfered String.
	 * @return cancelTransDollarsTransfered
	 */
	public String getCancelTransDollarsTransfered() {
		return this.cancelTransDollarsTransfered;
	}

	/**
	 * Set the cancelTransNoOfTransfers String.
	 * @param cancelTransNoOfTransfers
	 */
	public void setCancelTransNoOfTransfers(String cancelTransNoOfTransfers) {
		this.cancelTransNoOfTransfers=cancelTransNoOfTransfers;
	}
	/**
	 * Get the cancelTransNoOfTransfers String.
	 * @return cancelTransNoOfTransfers
	 */
	public String getCancelTransNoOfTransfers() {
		return this.cancelTransNoOfTransfers;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getPrefered_previous_txn_dt() {
		return prefered_previous_txn_dt;
	}
	public void setPrefered_previous_txn_dt(String prefered_previous_txn_dt) {
		this.prefered_previous_txn_dt = prefered_previous_txn_dt;
	}
	public String getFromAcc_Tier() {
		return fromAcc_Tier;
	}
	public void setFromAcc_Tier(String fromAcc_Tier) {
		this.fromAcc_Tier = fromAcc_Tier;
	}
	public String getToAcc_Tier() {
		return toAcc_Tier;
	}
	public void setToAcc_Tier(String toAcc_Tier) {
		this.toAcc_Tier = toAcc_Tier;
	}
	public String getFromAccNo_br_fa() {
		return fromAccNo_br_fa;
	}
	public void setFromAccNo_br_fa(String fromAccNo_br_fa) {
		this.fromAccNo_br_fa = fromAccNo_br_fa;
	}
	public String getToAccNo_br_fa() {
		return toAccNo_br_fa;
	}
	public void setToAccNo_br_fa(String toAccNo_br_fa) {
		this.toAccNo_br_fa = toAccNo_br_fa;
	}
	public String getFrom_NickName() {
		return from_NickName;
	}
	public void setFrom_NickName(String from_NickName) {
		this.from_NickName = from_NickName;
	}
	public String getTo_NickName() {
		return to_NickName;
	}
	public void setTo_NickName(String to_NickName) {
		this.to_NickName = to_NickName;
	}
	public String getFrom_FriendlyName() {
		return from_FriendlyName;
	}
	public void setFrom_FriendlyName(String from_FriendlyName) {
		this.from_FriendlyName = from_FriendlyName;
	}
	public String getTo_FriendlyName() {
		return to_FriendlyName;
	}
	public void setTo_FriendlyName(String to_FriendlyName) {
		this.to_FriendlyName = to_FriendlyName;
	}
	/**
	 * @return the parentTxnDate
	 */
	public String getParentTxnDate() {
		return parentTxnDate;
	}
	/**
	 * @param parentTxnDate the parentTxnDate to set
	 */
	public void setParentTxnDate(String parentTxnDate) {
		this.parentTxnDate = parentTxnDate;
	}
	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.cancelTransFromAcc=null;
		this.cancelTransToAcc=null;
		this.cancelTransAmount=null;
		this.cancelTransTransferDate=null;
		this.nextTransferAmount=null;
		this.nextTransferDateVal=null;
		this.businessDate=null;
		this.txnBatchRefNumber=null;
		this.txnPayPayRefNumber=null;
		this.parentTxnNumber=null;
		this.frequencyValue=null;
		this.durationValue=null;
		this.paymentstatus=null;
		this.servicetypecode=null;
		this.transactiontype=null;
		this.paypayeeaccnum=null;
		this.payeeName=null;
		this.bankCode=null;
		this.payeeRefId=null;
		this.payeeAdd1=null;
		this.payeeAdd2=null;
		this.payeeAdd3=null;
		this.payeeAccType=null;
		this.payeeId=null;
		this.recurringFreq=null;
		this.fromKeyAccNumber=null;
		this.toKeyAccNumber=null;
		this.payDebitAcc=null;
		this.cancelTransFrequency=null;
		this.cancelTransRepeat=null;
		this.cancelTransDollarsTransfered=null;
		this.cancelTransNoOfTransfers=null;
	}

	/**
	 * Returns All Form data as a String.
	 * @return String
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("action=" + getAction() + "\r\n");
		strB.append ("state=" + getState() + "\r\n");
		strB.append ("exportType=" + getExportType() + "\r\n");
		strB.append ("screenName=" + getScreenName() + "\r\n");
		strB.append ("retainData=" + getRetainData() + "\r\n");
		strB.append ("cancelFlag=" + getCancelFlag() + "\r\n");
		strB.append ("paginationIndex=" + getPaginationIndex() + "\r\n");
		strB.append ("txnPwdValid=" + getTxnPwdValid() + "\r\n");
		strB.append ("chartDetail=" + getChartDetail() + "\r\n");
		strB.append ("cancelTransFromAcc=" + cancelTransFromAcc + "\r\n");
		strB.append ("cancelTransToAcc=" + cancelTransToAcc + "\r\n");
		strB.append ("cancelTransAmount=" + cancelTransAmount + "\r\n");
		strB.append ("cancelTransTransferDate=" + cancelTransTransferDate + "\r\n");
		strB.append ("nextTransferAmount=" + nextTransferAmount + "\r\n");
		strB.append ("nextTransferDateVal=" + nextTransferDateVal + "\r\n");
		strB.append ("businessDate=" + businessDate + "\r\n");
		strB.append ("txnBatchRefNumber=" + txnBatchRefNumber + "\r\n");
		strB.append ("txnPayPayRefNumber=" + txnPayPayRefNumber + "\r\n");
		strB.append ("parentTxnNumber=" + parentTxnNumber + "\r\n");
		strB.append ("frequencyValue=" + frequencyValue + "\r\n");
		strB.append ("durationValue=" + durationValue + "\r\n");
		strB.append ("paymentstatus=" + paymentstatus + "\r\n");
		strB.append ("servicetypecode=" + servicetypecode + "\r\n");
		strB.append ("transactiontype=" + transactiontype + "\r\n");
		strB.append ("paypayeeaccnum=" + paypayeeaccnum + "\r\n");
		strB.append ("payeeName=" + payeeName + "\r\n");
		strB.append ("bankCode=" + bankCode + "\r\n");
		strB.append ("payeeRefId=" + payeeRefId + "\r\n");
		strB.append ("payeeAdd1=" + payeeAdd1 + "\r\n");
		strB.append ("payeeAdd2=" + payeeAdd2 + "\r\n");
		strB.append ("payeeAdd3=" + payeeAdd3 + "\r\n");
		strB.append ("payeeAccType=" + payeeAccType + "\r\n");
		strB.append ("payeeId=" + payeeId + "\r\n");
		strB.append ("recurringFreq=" + recurringFreq + "\r\n");
		strB.append ("fromKeyAccNumber=" + fromKeyAccNumber + "\r\n");
		strB.append ("toKeyAccNumber=" + toKeyAccNumber + "\r\n");
		strB.append ("payDebitAcc=" + payDebitAcc + "\r\n");
		strB.append ("cancelTransFrequency=" + cancelTransFrequency + "\r\n");
		strB.append ("cancelTransRepeat=" + cancelTransRepeat + "\r\n");
		strB.append ("cancelTransDollarsTransfered=" + cancelTransDollarsTransfered + "\r\n");
		strB.append ("cancelTransNoOfTransfers=" + cancelTransNoOfTransfers + "\r\n");
		return strB.toString();
	}

	/**
	 * Returns Vector object for export/report option.
	 * @return Vector object
	 */
	public Vector getReportInformation(){
		Vector reportInfo = new Vector(); 
		Object[][] objArr = {{"SkipTransferConfirm", "ScreenName"}};
		reportInfo.addElement(objArr);
		objArr = null;
		Object[][] objArr1 = {{"screentitle", "GROUP"} };
		reportInfo.addElement(objArr1);
		objArr1 = null;

		return reportInfo;
	}

	/**
	 * Returns Vector object for Chart option.
	 * @return Vector object
	 */
	public Vector getChartInformation(String chartButton){
		Vector chartInfo = new Vector(); 
		HashMap conditionMap = new HashMap();
		Object[][] objArr = {{"SkipTransferConfirm", "ScreenName"}};
		chartInfo.addElement(objArr);
		objArr = null;
		return chartInfo;
	}
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request) {
		ActionErrors errors=new ActionErrors();
		if(!getAction().equals("INIT"))
			errors=super.validate(mapping,request);
		return errors;
	}
}