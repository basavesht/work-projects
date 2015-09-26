/*
 * Created on Mon Apr 20 10:41:51 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.formbean;

import org.apache.struts.action.ActionMapping;

import com.tcs.ebw.mvc.validator.EbwForm;

import java.util.HashMap;

import java.util.ArrayList;

import java.util.Vector;
import com.tcs.ebw.taglib.DataInterface;
import com.tcs.ebw.taglib.TableColAttrObj;
import com.tcs.ebw.taglib.EbwTableHelper;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class InternalPreConfirmForm extends EbwForm implements java.io.Serializable {

	/**
	 * @return the isBrWarnings
	 */
	public boolean isBrWarnings() {
		return isBrWarnings;
	}

	/**
	 * @param isBrWarnings the isBrWarnings to set
	 */
	public void setBrWarnings(boolean isBrWarnings) {
		this.isBrWarnings = isBrWarnings;
	}

	/**
	 * Form Bean Constructor. 
	 */ 
	public InternalPreConfirmForm () {
		try {
			setState("InternalPreConfirm_INIT");

			loadInternaltransfercreditConfiguration();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void populateCollections() throws Exception{
	}

	public void copyCollections(InternalPreConfirmForm srcForm) {
	}

	// Instance Variables
	private String fromAccount = null;
	private String fromAccEdit = null;
	private String toAccount = null;
	private String toAccEdit = null;
	private String paymentamount = null;
	private String initiationDate = null;
	private String estArrivalDate = null;
	private String frequencyradio = null;
	private String frequencycombo = null;
	private String error = null;
	private String[] PAYDEBITACCNUM = null;
	private String[] PAYCURRCODE = null;
	private String[] PAYDEBITAMT = null;
	private String[] PAYFXCUSTOMERFXRATE = null;
	private String[] PAYTYPECODE = null;
	private String cashAvailable = null;
	private DataInterface internaltransfercreditCollection;
	private String internaltransfercreditFilHide;
	private String marginAvailable = null;
	private String delayedDebitCd = null;
	private String spendingPower = null;
	private String transferTime = null;
	private String estArrivalDateLabel = null;
	private String durationradio = null;
	private String durationenddate = null;
	private String durationdollarlimit = null;
	private String durationNoOfTransfers = null;
	private String servicetypecode = null;
	private String currencyCode = null;
	private String payeeName = null;
	private String bankCode = null;
	private String branchCode = null;
	private String fxRate = null;
	private String payeeId = null;
	private String totalPayAmount = null;
	private String durationValue = null;
	private String frequencyValue = null;
	private String batchId = null;
	private String validationfailed = null;
	private String customerId = null;
	private String groupId = null;
	private String userId = null;
	private String businessDate = null;
	private String payeeRefId = null;
	private String payeeAdd1 = null;
	private String payeeAdd2 = null;
	private String payeeAdd3 = null;
	private String payeeAccType = null;
	private String txnBatchRefNumber = null;
	private String txnPayPayRefNumber = null;
	private String parentTxnNumber = null;
	private String fromKeyAccNumber=null;
	private String toKeyAccNumber=null;
	private String prevPaystatus=null;
	private String brWarnings=null;
	private String brInformations=null;
	private String firstLastInitiationDate = null;
	private String businessRuleMessages = null;
	private boolean isBrErrors=false;
	private String prevTransactionAmnt=null;
	private String loginUserName = null;
	private String fromAccNo_br_fa = null;
	private String toAccNo_br_fa = null;
	private String from_NickName = null;
	private String to_NickName = null;
	private String from_FriendlyName = null;
	private String to_FriendlyName = null;
	private String transactionType = null;
	private boolean isBrWarnings=false;
	private String loanAcntNo = null;

	/**
	 * Set the fromAccount String.
	 * @param fromAccount
	 */
	public void setFromAccount(String fromAccount) {
		this.fromAccount=fromAccount;
	}
	/**
	 * Get the fromAccount String.
	 * @return fromAccount
	 */
	public String getFromAccount() {
		return this.fromAccount;
	}

	/**
	 * Set the fromAccEdit String.
	 * @param fromAccEdit
	 */
	public void setFromAccEdit(String fromAccEdit) {
		this.fromAccEdit=fromAccEdit;
	}
	/**
	 * Get the fromAccEdit String.
	 * @return fromAccEdit
	 */
	public String getFromAccEdit() {
		return this.fromAccEdit;
	}

	/**
	 * Set the toAccount String.
	 * @param toAccount
	 */
	public void setToAccount(String toAccount) {
		this.toAccount=toAccount;
	}
	/**
	 * Get the toAccount String.
	 * @return toAccount
	 */
	public String getToAccount() {
		return this.toAccount;
	}

	/**
	 * Set the toAccEdit String.
	 * @param toAccEdit
	 */
	public void setToAccEdit(String toAccEdit) {
		this.toAccEdit=toAccEdit;
	}
	/**
	 * Get the toAccEdit String.
	 * @return toAccEdit
	 */
	public String getToAccEdit() {
		return this.toAccEdit;
	}

	/**
	 * Set the paymentamount String.
	 * @param paymentamount
	 */
	public void setPaymentamount(String paymentamount) {
		this.paymentamount=paymentamount;
	}
	/**
	 * Get the paymentamount String.
	 * @return paymentamount
	 */
	public String getPaymentamount() {
		return this.paymentamount;
	}

	/**
	 * Set the initiationDate String.
	 * @param initiationDate
	 */
	public void setInitiationDate(String initiationDate) {
		this.initiationDate=initiationDate;
	}
	/**
	 * Get the initiationDate String.
	 * @return initiationDate
	 */
	public String getInitiationDate() {
		return this.initiationDate;
	}

	/**
	 * Set the estArrivalDate String.
	 * @param estArrivalDate
	 */
	public void setEstArrivalDate(String estArrivalDate) {
		this.estArrivalDate=estArrivalDate;
	}
	/**
	 * Get the estArrivalDate String.
	 * @return estArrivalDate
	 */
	public String getEstArrivalDate() {
		return this.estArrivalDate;
	}

	/**
	 * Set the frequencyradio String.
	 * @param frequencyradio
	 */
	public void setFrequencyradio(String frequencyradio) {
		this.frequencyradio=frequencyradio;
	}
	/**
	 * Get the frequencyradio String.
	 * @return frequencyradio
	 */
	public String getFrequencyradio() {
		return this.frequencyradio;
	}

	/**
	 * Set the frequencycombo String.
	 * @param frequencycombo
	 */
	public void setFrequencycombo(String frequencycombo) {
		this.frequencycombo=frequencycombo;
	}
	/**
	 * Get the frequencycombo String.
	 * @return frequencycombo
	 */
	public String getFrequencycombo() {
		return this.frequencycombo;
	}

	/**
	 * Set the error String.
	 * @param error
	 */
	public void setError(String error) {
		this.error=error;
	}
	/**
	 * Get the error String.
	 * @return error
	 */
	public String getError() {
		return this.error;
	}

	/**
	 * Set the PAYDEBITACCNUM String[].
	 * @param PAYDEBITACCNUM
	 */
	public void setPAYDEBITACCNUM(String[] PAYDEBITACCNUM) {
		this.PAYDEBITACCNUM=PAYDEBITACCNUM;
	}
	/**
	 * Get the PAYDEBITACCNUM String[].
	 * @return PAYDEBITACCNUM
	 */
	public String[] getPAYDEBITACCNUM() {
		return this.PAYDEBITACCNUM;
	}

	/**
	 * Set the PAYCURRCODE String[].
	 * @param PAYCURRCODE
	 */
	public void setPAYCURRCODE(String[] PAYCURRCODE) {
		this.PAYCURRCODE=PAYCURRCODE;
	}
	/**
	 * Get the PAYCURRCODE String[].
	 * @return PAYCURRCODE
	 */
	public String[] getPAYCURRCODE() {
		return this.PAYCURRCODE;
	}

	/**
	 * Set the PAYDEBITAMT String[].
	 * @param PAYDEBITAMT
	 */
	public void setPAYDEBITAMT(String[] PAYDEBITAMT) {
		this.PAYDEBITAMT=PAYDEBITAMT;
	}
	/**
	 * Get the PAYDEBITAMT String[].
	 * @return PAYDEBITAMT
	 */
	public String[] getPAYDEBITAMT() {
		return this.PAYDEBITAMT;
	}

	/**
	 * Set the PAYFXCUSTOMERFXRATE String[].
	 * @param PAYFXCUSTOMERFXRATE
	 */
	public void setPAYFXCUSTOMERFXRATE(String[] PAYFXCUSTOMERFXRATE) {
		this.PAYFXCUSTOMERFXRATE=PAYFXCUSTOMERFXRATE;
	}
	/**
	 * Get the PAYFXCUSTOMERFXRATE String[].
	 * @return PAYFXCUSTOMERFXRATE
	 */
	public String[] getPAYFXCUSTOMERFXRATE() {
		return this.PAYFXCUSTOMERFXRATE;
	}

	/**
	 * Set the PAYTYPECODE String[].
	 * @param PAYTYPECODE
	 */
	public void setPAYTYPECODE(String[] PAYTYPECODE) {
		this.PAYTYPECODE=PAYTYPECODE;
	}
	/**
	 * Get the PAYTYPECODE String[].
	 * @return PAYTYPECODE
	 */
	public String[] getPAYTYPECODE() {
		return this.PAYTYPECODE;
	}

	/**
	 * Set the cashAvailable String.
	 * @param cashAvailable
	 */
	public void setCashAvailable(String cashAvailable) {
		this.cashAvailable=cashAvailable;
	}
	/**
	 * Get the cashAvailable String.
	 * @return cashAvailable
	 */
	public String getCashAvailable() {
		return this.cashAvailable;
	}

	/**
	 * Load DataInterface for the internaltransfercredit
	 */
	public void loadInternaltransfercreditConfiguration() {
		EbwTableHelper helper= new EbwTableHelper("");
		ArrayList arrTableCols=new ArrayList();
		TableColAttrObj tableColAttr = null;
		helper.setTableName("internaltransfercredit");
		helper.setTableTitle("");

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYDEBITACCNUM");
		tableColAttr.setComponentType("Hidden");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYCURRCODE");
		tableColAttr.setComponentType("Hidden");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYDEBITAMT");
		tableColAttr.setComponentType("Hidden");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYFXCUSTOMERFXRATE");
		tableColAttr.setComponentType("Hidden");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYTYPECODE");
		tableColAttr.setComponentType("Hidden");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("");
		arrTableCols.add(tableColAttr);
		helper.setColAttrObjs(arrTableCols);
		this.internaltransfercreditCollection = helper;
	}

	/**
	 * Set DataInterface for the internaltransfercredit Table.
	 * @param internaltransfercreditCollection
	 */
	public void setInternaltransfercreditCollection(DataInterface internaltransfercreditCollection) {
		this.internaltransfercreditCollection=internaltransfercreditCollection;
	}
	/**
	 * Get DataInterface for the internaltransfercredit Table.
	 * @return internaltransfercreditCollection
	 */
	public DataInterface getInternaltransfercreditCollection() {
		return this.internaltransfercreditCollection;
	}

	/**
	 * Set Table Data as an arraylist for the internaltransfercredit Table.
	 * @param internaltransfercredit ArrayList
	 */
	public void setInternaltransfercreditTableData(Object lstInternaltransfercredit) {
		this.internaltransfercreditCollection.setData(lstInternaltransfercredit);
	}
	/**
	 * Set the internaltransfercreditFilHide String.
	 * @param internaltransfercreditFilHide
	 */
	public void setInternaltransfercreditFilHide(String internaltransfercreditFilHide) {
		this.internaltransfercreditFilHide=internaltransfercreditFilHide;
	}
	/**
	 * Get the internaltransfercreditFilHide String.
	 * @return internaltransfercreditFilHide
	 */
	public String getInternaltransfercreditFilHide() {
		return this.internaltransfercreditFilHide;
	}

	/**
	 * Set the marginAvailable String.
	 * @param marginAvailable
	 */
	public void setMarginAvailable(String marginAvailable) {
		this.marginAvailable=marginAvailable;
	}
	/**
	 * Get the marginAvailable String.
	 * @return marginAvailable
	 */
	public String getMarginAvailable() {
		return this.marginAvailable;
	}

	/**
	 * Set the delayedDebitCd String.
	 * @param delayedDebitCd
	 */
	public void setDelayedDebitCd(String delayedDebitCd) {
		this.delayedDebitCd=delayedDebitCd;
	}
	/**
	 * Get the delayedDebitCd String.
	 * @return delayedDebitCd
	 */
	public String getDelayedDebitCd() {
		return this.delayedDebitCd;
	}

	/**
	 * Set the spendingPower String.
	 * @param spendingPower
	 */
	public void setSpendingPower(String spendingPower) {
		this.spendingPower=spendingPower;
	}
	/**
	 * Get the spendingPower String.
	 * @return spendingPower
	 */
	public String getSpendingPower() {
		return this.spendingPower;
	}

	/**
	 * Set the transferTime String.
	 * @param transferTime
	 */
	public void setTransferTime(String transferTime) {
		this.transferTime=transferTime;
	}
	/**
	 * Get the transferTime String.
	 * @return transferTime
	 */
	public String getTransferTime() {
		return this.transferTime;
	}

	/**
	 * Set the estArrivalDateLabel String.
	 * @param estArrivalDateLabel
	 */
	public void setEstArrivalDateLabel(String estArrivalDateLabel) {
		this.estArrivalDateLabel=estArrivalDateLabel;
	}
	/**
	 * Get the estArrivalDateLabel String.
	 * @return estArrivalDateLabel
	 */
	public String getEstArrivalDateLabel() {
		return this.estArrivalDateLabel;
	}

	/**
	 * Set the durationradio String.
	 * @param durationradio
	 */
	public void setDurationradio(String durationradio) {
		this.durationradio=durationradio;
	}
	/**
	 * Get the durationradio String.
	 * @return durationradio
	 */
	public String getDurationradio() {
		return this.durationradio;
	}

	/**
	 * Set the durationenddate String.
	 * @param durationenddate
	 */
	public void setDurationenddate(String durationenddate) {
		this.durationenddate=durationenddate;
	}
	/**
	 * Get the durationenddate String.
	 * @return durationenddate
	 */
	public String getDurationenddate() {
		return this.durationenddate;
	}

	/**
	 * Set the durationdollarlimit String.
	 * @param durationdollarlimit
	 */
	public void setDurationdollarlimit(String durationdollarlimit) {
		this.durationdollarlimit=durationdollarlimit;
	}
	/**
	 * Get the durationdollarlimit String.
	 * @return durationdollarlimit
	 */
	public String getDurationdollarlimit() {
		return this.durationdollarlimit;
	}

	/**
	 * Set the durationNoOfTransfers String.
	 * @param durationNoOfTransfers
	 */
	public void setDurationNoOfTransfers(String durationNoOfTransfers) {
		this.durationNoOfTransfers=durationNoOfTransfers;
	}
	/**
	 * Get the durationNoOfTransfers String.
	 * @return durationNoOfTransfers
	 */
	public String getDurationNoOfTransfers() {
		return this.durationNoOfTransfers;
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
	 * Set the currencyCode String.
	 * @param currencyCode
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode=currencyCode;
	}
	/**
	 * Get the currencyCode String.
	 * @return currencyCode
	 */
	public String getCurrencyCode() {
		return this.currencyCode;
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
	 * Set the branchCode String.
	 * @param branchCode
	 */
	public void setBranchCode(String branchCode) {
		this.branchCode=branchCode;
	}
	/**
	 * Get the branchCode String.
	 * @return branchCode
	 */
	public String getBranchCode() {
		return this.branchCode;
	}

	/**
	 * Set the fxRate String.
	 * @param fxRate
	 */
	public void setFxRate(String fxRate) {
		this.fxRate=fxRate;
	}
	/**
	 * Get the fxRate String.
	 * @return fxRate
	 */
	public String getFxRate() {
		return this.fxRate;
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
	 * Set the totalPayAmount String.
	 * @param totalPayAmount
	 */
	public void setTotalPayAmount(String totalPayAmount) {
		this.totalPayAmount=totalPayAmount;
	}
	/**
	 * Get the totalPayAmount String.
	 * @return totalPayAmount
	 */
	public String getTotalPayAmount() {
		return this.totalPayAmount;
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
	 * Set the batchId String.
	 * @param batchId
	 */
	public void setBatchId(String batchId) {
		this.batchId=batchId;
	}
	/**
	 * Get the batchId String.
	 * @return batchId
	 */
	public String getBatchId() {
		return this.batchId;
	}

	/**
	 * Set the validationfailed String.
	 * @param validationfailed
	 */
	public void setValidationfailed(String validationfailed) {
		this.validationfailed=validationfailed;
	}
	/**
	 * Get the validationfailed String.
	 * @return validationfailed
	 */
	public String getValidationfailed() {
		return this.validationfailed;
	}

	/**
	 * Set the customerId String.
	 * @param customerId
	 */
	public void setCustomerId(String customerId) {
		this.customerId=customerId;
	}
	/**
	 * Get the customerId String.
	 * @return customerId
	 */
	public String getCustomerId() {
		return this.customerId;
	}

	/**
	 * Set the groupId String.
	 * @param groupId
	 */
	public void setGroupId(String groupId) {
		this.groupId=groupId;
	}
	/**
	 * Get the groupId String.
	 * @return groupId
	 */
	public String getGroupId() {
		return this.groupId;
	}

	/**
	 * Set the userId String.
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId=userId;
	}
	/**
	 * Get the userId String.
	 * @return userId
	 */
	public String getUserId() {
		return this.userId;
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
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.fromAccount=null;
		this.fromAccEdit=null;
		this.toAccount=null;
		this.toAccEdit=null;
		this.paymentamount=null;
		this.initiationDate=null;
		this.estArrivalDate=null;
		this.frequencyradio=null;
		this.frequencycombo=null;
		this.error=null;
		this.PAYDEBITACCNUM=null;
		this.PAYCURRCODE=null;
		this.PAYDEBITAMT=null;
		this.PAYFXCUSTOMERFXRATE=null;
		this.PAYTYPECODE=null;
		this.cashAvailable=null;
		this.marginAvailable=null;
		this.delayedDebitCd=null;
		this.spendingPower=null;
		this.transferTime=null;
		this.estArrivalDateLabel=null;
		this.durationradio=null;
		this.durationenddate=null;
		this.durationdollarlimit=null;
		this.durationNoOfTransfers=null;
		this.servicetypecode=null;
		this.currencyCode=null;
		this.payeeName=null;
		this.bankCode=null;
		this.branchCode=null;
		this.fxRate=null;
		this.payeeId=null;
		this.totalPayAmount=null;
		this.durationValue=null;
		this.frequencyValue=null;
		this.batchId=null;
		this.validationfailed=null;
		this.customerId=null;
		this.groupId=null;
		this.userId=null;
		this.businessDate=null;
		this.payeeRefId=null;
		this.payeeAdd1=null;
		this.payeeAdd2=null;
		this.payeeAdd3=null;
		this.payeeAccType=null;
		this.txnBatchRefNumber=null;
		this.txnPayPayRefNumber=null;
		this.parentTxnNumber=null;
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
		strB.append ("fromAccount=" + fromAccount + "\r\n");
		strB.append ("fromAccEdit=" + fromAccEdit + "\r\n");
		strB.append ("toAccount=" + toAccount + "\r\n");
		strB.append ("toAccEdit=" + toAccEdit + "\r\n");
		strB.append ("paymentamount=" + paymentamount + "\r\n");
		strB.append ("initiationDate=" + initiationDate + "\r\n");
		strB.append ("estArrivalDate=" + estArrivalDate + "\r\n");
		strB.append ("frequencyradio=" + frequencyradio + "\r\n");
		strB.append ("frequencycombo=" + frequencycombo + "\r\n");
		strB.append ("error=" + error + "\r\n");
		strB.append ("cashAvailable=" + cashAvailable + "\r\n");
		strB.append ("marginAvailable=" + marginAvailable + "\r\n");
		strB.append ("delayedDebitCd=" + delayedDebitCd + "\r\n");
		strB.append ("spendingPower=" + spendingPower + "\r\n");
		strB.append ("transferTime=" + transferTime + "\r\n");
		strB.append ("estArrivalDateLabel=" + estArrivalDateLabel + "\r\n");
		strB.append ("durationradio=" + durationradio + "\r\n");
		strB.append ("durationenddate=" + durationenddate + "\r\n");
		strB.append ("durationdollarlimit=" + durationdollarlimit + "\r\n");
		strB.append ("durationNoOfTransfers=" + durationNoOfTransfers + "\r\n");
		strB.append ("servicetypecode=" + servicetypecode + "\r\n");
		strB.append ("currencyCode=" + currencyCode + "\r\n");
		strB.append ("payeeName=" + payeeName + "\r\n");
		strB.append ("bankCode=" + bankCode + "\r\n");
		strB.append ("branchCode=" + branchCode + "\r\n");
		strB.append ("fxRate=" + fxRate + "\r\n");
		strB.append ("payeeId=" + payeeId + "\r\n");
		strB.append ("totalPayAmount=" + totalPayAmount + "\r\n");
		strB.append ("durationValue=" + durationValue + "\r\n");
		strB.append ("frequencyValue=" + frequencyValue + "\r\n");
		strB.append ("batchId=" + batchId + "\r\n");
		strB.append ("validationfailed=" + validationfailed + "\r\n");
		strB.append ("customerId=" + customerId + "\r\n");
		strB.append ("groupId=" + groupId + "\r\n");
		strB.append ("userId=" + userId + "\r\n");
		strB.append ("businessDate=" + businessDate + "\r\n");
		strB.append ("payeeRefId=" + payeeRefId + "\r\n");
		strB.append ("payeeAdd1=" + payeeAdd1 + "\r\n");
		strB.append ("payeeAdd2=" + payeeAdd2 + "\r\n");
		strB.append ("payeeAdd3=" + payeeAdd3 + "\r\n");
		strB.append ("payeeAccType=" + payeeAccType + "\r\n");
		strB.append ("txnBatchRefNumber=" + txnBatchRefNumber + "\r\n");
		strB.append ("txnPayPayRefNumber=" + txnPayPayRefNumber + "\r\n");
		strB.append ("parentTxnNumber=" + parentTxnNumber + "\r\n");
		return strB.toString();
	}

	/**
	 * Returns Vector object for export/report option.
	 * @return Vector object
	 */
	public Vector getReportInformation(){
		Vector reportInfo = new Vector(); 
		Object[][] objArr = {{"InternalPreConfirm", "ScreenName"}};
		reportInfo.addElement(objArr);
		objArr = null;
		Object[][] objArr1 = {{"screentitle", "GROUP"},{"internaltransfercredit","Table"}  };
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
		Object[][] objArr = {{"InternalPreConfirm", "ScreenName"}};
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

	public String getFromKeyAccNumber() {
		return fromKeyAccNumber;
	}

	public void setFromKeyAccNumber(String fromKeyAccNumber) {
		this.fromKeyAccNumber = fromKeyAccNumber;
	}

	public String getToKeyAccNumber() {
		return toKeyAccNumber;
	}

	public void setToKeyAccNumber(String toKeyAccNumber) {
		this.toKeyAccNumber = toKeyAccNumber;
	}

	public String getPrevPaystatus() {
		return prevPaystatus;
	}

	public void setPrevPaystatus(String prevPaystatus) {
		this.prevPaystatus = prevPaystatus;
	}

	public String getBrWarnings() {
		return brWarnings;
	}

	public void setBrWarnings(String brWarnings) {
		this.brWarnings = brWarnings;
	}

	public String getBrInformations() {
		return brInformations;
	}

	public void setBrInformations(String brInformations) {
		this.brInformations = brInformations;
	}

	public String getFirstLastInitiationDate() {
		return firstLastInitiationDate;
	}

	public void setFirstLastInitiationDate(String firstLastInitiationDate) {
		this.firstLastInitiationDate = firstLastInitiationDate;
	}

	public String getBusinessRuleMessages() {
		return businessRuleMessages;
	}

	public void setBusinessRuleMessages(String businessRuleMessages) {
		this.businessRuleMessages = businessRuleMessages;
	}

	public boolean isBrErrors() {
		return isBrErrors;
	}

	public void setBrErrors(boolean isBrErrors) {
		this.isBrErrors = isBrErrors;
	}

	public String getPrevTransactionAmnt() {
		return prevTransactionAmnt;
	}

	public void setPrevTransactionAmnt(String prevTransactionAmnt) {
		this.prevTransactionAmnt = prevTransactionAmnt;
	}

	public String getLoginUserName() {
		return loginUserName;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
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

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the loanAcntNo
	 */
	public String getLoanAcntNo() {
		return loanAcntNo;
	}

	/**
	 * @param loanAcntNo the loanAcntNo to set
	 */
	public void setLoanAcntNo(String loanAcntNo) {
		this.loanAcntNo = loanAcntNo;
	}
}