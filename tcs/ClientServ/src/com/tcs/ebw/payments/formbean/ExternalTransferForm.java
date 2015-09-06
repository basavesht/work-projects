/*
 * Created on Sat Apr 11 14:33:01 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.formbean;

import org.apache.struts.action.ActionMapping;

import com.tcs.ebw.mvc.validator.EbwForm;

import java.util.HashMap;

import java.util.ArrayList;

import java.util.Collection;
import com.tcs.ebw.codegen.beans.ComboboxData;
import com.tcs.ebw.exception.EbwException;
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
public class ExternalTransferForm extends EbwForm implements java.io.Serializable {

	/**
	 * Form Bean Constructor. 
	 */ 
	public ExternalTransferForm () {
		try {
			setState("ExternalTransfer_INIT");

			loadExternaltransfercreditConfiguration();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void populateCollections() throws Exception{
		if(this.fromAccountCollection==null)
			FetchFromAccountCollection();
		if(this.toAccountCollection==null)
			FetchToAccountCollection();
		if(this.frequencycomboCollection==null)
			FetchFrequencycomboCollection();
		if(this.PAYPAYEEACCNUMCollection==null)
			FetchPAYPAYEEACCNUMCollection();
	}

	public void copyCollections(ExternalTransferForm srcForm) {
		this.fromAccountCollection=srcForm.getFromAccountCollection();
		this.toAccountCollection=srcForm.getToAccountCollection();
		this.frequencycomboCollection=srcForm.getFrequencycomboCollection();
		this.PAYPAYEEACCNUMCollection=srcForm.getPAYPAYEEACCNUMCollection();
	}

	// Instance Variables
	private String fromAccount = null;
	private Collection fromAccountCollection;
	private String fromAccEdit = null;
	private String cashAvailable = null;
	private String marginAvailable = null;
	private String delayedDebitCd = null;
	private String spendingPower = null;
	private String toAccount = null;
	private Collection toAccountCollection;
	private String toAccEdit = null;
	private String paymentamount = null;
	private String initiationDate = null;
	private String initiationDateHidden = null;
	private String frequencyradio = null;
	private String frequencycombo = null;
	private Collection frequencycomboCollection;
	private String durationradio = null;
	private String durationenddate = null;
	private String durationenddateHidden = null;
	private String durationdollarlimit = null;
	private String durationNoOfTransfers = null;
	private String[] PAYPAYEEACCNUM = null;
	private Collection PAYPAYEEACCNUMCollection;
	private String[] PAYDEBITAMT = null;
	private String[] PAYCURRCODE = null;
	private String[] PAYTYPECODE = null;
	private String[] PAYCONSOLIDATE = null;
	private String[] PAYPAYEENAME1 = null;
	private String[] PAYPAYEEBRANCHCODE = null;
	private String[] PAYBATCHREF = null;
	private String[] PAYPAYEEID = null;
	private String[] PAYPAYEEBANKCODE = null;
	private String servicetypecode = null;
	private DataInterface externaltransfercreditCollection;
	private String externaltransfercreditFilHide;
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
	private String infoRTAB = null;
	private String businessDate = null;
	private String estArrivalDate = null;
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
	private String totalTransfer = null;
	private String totalDollarlimit = null;
	private String checkExternalAccStatus = null;
	private String editfreqComboVal=null;
	private String editApprInitiationDate=null;
	private String prevTransactionAmnt=null;
	private String prevTransactionDate=null;
	private String firstLastInitiationDate=null;
	private String cutOffTime =null;
	private String achDays  =null;
	private String fromAccNo_br_fa = null;
	private String toAccNo_br_fa = null;
	private String from_NickName = null;
	private String to_NickName = null;
	private String from_FriendlyName = null;
	private String to_FriendlyName = null;
	private String transactionType = null;
	private String rsaReviewFlag = null; //RSA Review to be mapped during edit transactions ...


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
	 * Set collection for the fromAccount combobox.
	 * @param fromAccountCollection
	 */
	public void setFromAccountCollection(Collection fromAccountCollection) {
		this.fromAccountCollection=fromAccountCollection;
	}
	/**
	 * Get collection for the fromAccount combobox.
	 * @return fromAccountCollection
	 */
	public Collection getFromAccountCollection() {
		return this.fromAccountCollection;
	}

	public void FetchFromAccountCollection() throws Exception{
		try{
			if (this.fromAccountCollection == null) {
				ComboboxData cboData = new ComboboxData();
				this.fromAccountCollection = cboData.getComboBoxData("" , "", "","");
			}
		}catch (EbwException exc) {
			exc.printStackTrace();
			throw exc;
		}
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
	 * Set collection for the toAccount combobox.
	 * @param toAccountCollection
	 */
	public void setToAccountCollection(Collection toAccountCollection) {
		this.toAccountCollection=toAccountCollection;
	}
	/**
	 * Get collection for the toAccount combobox.
	 * @return toAccountCollection
	 */
	public Collection getToAccountCollection() {
		return this.toAccountCollection;
	}

	public void FetchToAccountCollection() throws Exception{
		try{
			if (this.toAccountCollection == null) {
				ComboboxData cboData = new ComboboxData();
				this.toAccountCollection = cboData.getComboBoxData("" , "", "","");
			}
		}catch (EbwException exc) {
			exc.printStackTrace();
			throw exc;
		}
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
	 * Set the initiationDateHidden String.
	 * @param initiationDateHidden
	 */
	public void setInitiationDateHidden(String initiationDateHidden) {
		this.initiationDateHidden=initiationDateHidden;
	}
	/**
	 * Get the initiationDateHidden String.
	 * @return initiationDateHidden
	 */
	public String getInitiationDateHidden() {
		return this.initiationDateHidden;
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
	 * Set collection for the frequencycombo combobox.
	 * @param frequencycomboCollection
	 */
	public void setFrequencycomboCollection(Collection frequencycomboCollection) {
		this.frequencycomboCollection=frequencycomboCollection;
	}
	/**
	 * Get collection for the frequencycombo combobox.
	 * @return frequencycomboCollection
	 */
	public Collection getFrequencycomboCollection() {
		return this.frequencycomboCollection;
	}

	public void FetchFrequencycomboCollection() throws Exception{
		try{
			if (this.frequencycomboCollection == null) {
				ComboboxData cboData = new ComboboxData();
				this.frequencycomboCollection = cboData.getComboBoxData("getPaymentFrequency" , "", "","","Select Frequency");
			}
		}catch (EbwException exc) {
			exc.printStackTrace();
			throw exc;
		}
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
	 * Set the durationenddateHidden String.
	 * @param durationenddateHidden
	 */
	public void setDurationenddateHidden(String durationenddateHidden) {
		this.durationenddateHidden=durationenddateHidden;
	}
	/**
	 * Get the durationenddateHidden String.
	 * @return durationenddateHidden
	 */
	public String getDurationenddateHidden() {
		return this.durationenddateHidden;
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
	 * Set collection for the PAYPAYEEACCNUM combobox.
	 * @param PAYPAYEEACCNUMCollection
	 */
	public void setPAYPAYEEACCNUMCollection(Collection PAYPAYEEACCNUMCollection) {
		this.PAYPAYEEACCNUMCollection=PAYPAYEEACCNUMCollection;
	}
	/**
	 * Get collection for the PAYPAYEEACCNUM combobox.
	 * @return PAYPAYEEACCNUMCollection
	 */
	public Collection getPAYPAYEEACCNUMCollection() {
		return this.PAYPAYEEACCNUMCollection;
	}

	public void FetchPAYPAYEEACCNUMCollection() throws Exception{
		try{
			if (this.PAYPAYEEACCNUMCollection == null) {
				ComboboxData cboData = new ComboboxData();
				this.PAYPAYEEACCNUMCollection = cboData.getComboBoxData("" , "", "","");
			}
		}catch (EbwException exc) {
			exc.printStackTrace();
			throw exc;
		}
	}

	/**
	 * Set the PAYPAYEEACCNUM String[].
	 * @param PAYPAYEEACCNUM
	 */
	public void setPAYPAYEEACCNUM(String[] PAYPAYEEACCNUM) {
		this.PAYPAYEEACCNUM=PAYPAYEEACCNUM;
	}
	/**
	 * Get the PAYPAYEEACCNUM String[].
	 * @return PAYPAYEEACCNUM
	 */
	public String[] getPAYPAYEEACCNUM() {
		return this.PAYPAYEEACCNUM;
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
	 * Set the PAYCONSOLIDATE String[].
	 * @param PAYCONSOLIDATE
	 */
	public void setPAYCONSOLIDATE(String[] PAYCONSOLIDATE) {
		this.PAYCONSOLIDATE=PAYCONSOLIDATE;
	}
	/**
	 * Get the PAYCONSOLIDATE String[].
	 * @return PAYCONSOLIDATE
	 */
	public String[] getPAYCONSOLIDATE() {
		return this.PAYCONSOLIDATE;
	}

	/**
	 * Set the PAYPAYEENAME1 String[].
	 * @param PAYPAYEENAME1
	 */
	public void setPAYPAYEENAME1(String[] PAYPAYEENAME1) {
		this.PAYPAYEENAME1=PAYPAYEENAME1;
	}
	/**
	 * Get the PAYPAYEENAME1 String[].
	 * @return PAYPAYEENAME1
	 */
	public String[] getPAYPAYEENAME1() {
		return this.PAYPAYEENAME1;
	}

	/**
	 * Set the PAYPAYEEBRANCHCODE String[].
	 * @param PAYPAYEEBRANCHCODE
	 */
	public void setPAYPAYEEBRANCHCODE(String[] PAYPAYEEBRANCHCODE) {
		this.PAYPAYEEBRANCHCODE=PAYPAYEEBRANCHCODE;
	}
	/**
	 * Get the PAYPAYEEBRANCHCODE String[].
	 * @return PAYPAYEEBRANCHCODE
	 */
	public String[] getPAYPAYEEBRANCHCODE() {
		return this.PAYPAYEEBRANCHCODE;
	}

	/**
	 * Set the PAYBATCHREF String[].
	 * @param PAYBATCHREF
	 */
	public void setPAYBATCHREF(String[] PAYBATCHREF) {
		this.PAYBATCHREF=PAYBATCHREF;
	}
	/**
	 * Get the PAYBATCHREF String[].
	 * @return PAYBATCHREF
	 */
	public String[] getPAYBATCHREF() {
		return this.PAYBATCHREF;
	}

	/**
	 * Set the PAYPAYEEID String[].
	 * @param PAYPAYEEID
	 */
	public void setPAYPAYEEID(String[] PAYPAYEEID) {
		this.PAYPAYEEID=PAYPAYEEID;
	}
	/**
	 * Get the PAYPAYEEID String[].
	 * @return PAYPAYEEID
	 */
	public String[] getPAYPAYEEID() {
		return this.PAYPAYEEID;
	}

	/**
	 * Set the PAYPAYEEBANKCODE String[].
	 * @param PAYPAYEEBANKCODE
	 */
	public void setPAYPAYEEBANKCODE(String[] PAYPAYEEBANKCODE) {
		this.PAYPAYEEBANKCODE=PAYPAYEEBANKCODE;
	}
	/**
	 * Get the PAYPAYEEBANKCODE String[].
	 * @return PAYPAYEEBANKCODE
	 */
	public String[] getPAYPAYEEBANKCODE() {
		return this.PAYPAYEEBANKCODE;
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
	 * Load DataInterface for the externaltransfercredit
	 */
	public void loadExternaltransfercreditConfiguration() {
		EbwTableHelper helper= new EbwTableHelper("");
		ArrayList arrTableCols=new ArrayList();
		TableColAttrObj tableColAttr = null;
		helper.setTableName("externaltransfercredit");
		helper.setTableTitle("");

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYPAYEEACCNUM");
		tableColAttr.setComponentType("Combobox");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalTransfer.externaltransfercredit.PAYPAYEEACCNUM\" />");
		tableColAttr.setFieldAttribute("class=\"BeneTableName\"");
		tableColAttr.setDefaultValue("");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<select name=\"PAYPAYEEACCNUM\" id=\"PAYPAYEEACCNUM\" ExternalTransferForm></select>");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYDEBITAMT");
		tableColAttr.setComponentType("TextField");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalTransfer.externaltransfercredit.PAYDEBITAMT\" />");
		tableColAttr.setFieldAttribute("class=\"BeneTableAmount\" onblur=\"javascript:fxRate();if(document.CreatePaymentsForm.validationfailed.value=='false'){return(false);}\"");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"text\" name=\"PAYDEBITAMT\" id=\"PAYDEBITAMT\" class=\"BeneTableAmount\" onblur=\"javascript:fxRate();if(document.CreatePaymentsForm.validationfailed.value=='false'){return(false);}\">");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYCURRCODE");
		tableColAttr.setComponentType("TextField");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalTransfer.externaltransfercredit.PAYCURRCODE\" />");
		tableColAttr.setFieldAttribute("class=\"BeneTableCurr\" readonly=\"true\"");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"text\" name=\"PAYCURRCODE\" id=\"PAYCURRCODE\" class=\"BeneTableCurr\" readonly=\"true\">");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYTYPECODE");
		tableColAttr.setComponentType("TextField");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalTransfer.externaltransfercredit.PAYTYPECODE\" />");
		tableColAttr.setFieldAttribute("class=\"BeneTableTotAmt\"");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"text\" name=\"PAYTYPECODE\" id=\"PAYTYPECODE\" class=\"BeneTableTotAmt\">");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYCONSOLIDATE");
		tableColAttr.setComponentType("TextField");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalTransfer.externaltransfercredit.PAYCONSOLIDATE\" />");
		tableColAttr.setFieldAttribute("class=\"hidden\"");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"text\" name=\"PAYCONSOLIDATE\" id=\"PAYCONSOLIDATE\" class=\"hidden\">");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYPAYEENAME1");
		tableColAttr.setComponentType("TextField");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalTransfer.externaltransfercredit.PAYPAYEENAME1\" />");
		tableColAttr.setFieldAttribute("class=\"hidden\"");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"text\" name=\"PAYPAYEENAME1\" id=\"PAYPAYEENAME1\" class=\"hidden\">");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYPAYEEBRANCHCODE");
		tableColAttr.setComponentType("TextField");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalTransfer.externaltransfercredit.PAYPAYEEBRANCHCODE\" />");
		tableColAttr.setFieldAttribute("class=\"hidden\"");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"text\" name=\"PAYPAYEEBRANCHCODE\" id=\"PAYPAYEEBRANCHCODE\" class=\"hidden\">");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYBATCHREF");
		tableColAttr.setComponentType("TextField");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalTransfer.externaltransfercredit.PAYBATCHREF\" />");
		tableColAttr.setFieldAttribute("class=\"hidden\"");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"text\" name=\"PAYBATCHREF\" id=\"PAYBATCHREF\" class=\"hidden\">");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYPAYEEID");
		tableColAttr.setComponentType("TextField");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalTransfer.externaltransfercredit.PAYPAYEEID\" />");
		tableColAttr.setFieldAttribute("class=\"hidden\"");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"text\" name=\"PAYPAYEEID\" id=\"PAYPAYEEID\" class=\"hidden\">");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYPAYEEBANKCODE");
		tableColAttr.setComponentType("TextField");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalTransfer.externaltransfercredit.PAYPAYEEBANKCODE\" />");
		tableColAttr.setFieldAttribute("class=\"hidden\"");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"text\" name=\"PAYPAYEEBANKCODE\" id=\"PAYPAYEEBANKCODE\" class=\"hidden\">");
		arrTableCols.add(tableColAttr);
		helper.setColAttrObjs(arrTableCols);
		this.externaltransfercreditCollection = helper;
	}

	/**
	 * Set DataInterface for the externaltransfercredit Table.
	 * @param externaltransfercreditCollection
	 */
	public void setExternaltransfercreditCollection(DataInterface externaltransfercreditCollection) {
		this.externaltransfercreditCollection=externaltransfercreditCollection;
	}
	/**
	 * Get DataInterface for the externaltransfercredit Table.
	 * @return externaltransfercreditCollection
	 */
	public DataInterface getExternaltransfercreditCollection() {
		return this.externaltransfercreditCollection;
	}

	/**
	 * Set Table Data as an arraylist for the externaltransfercredit Table.
	 * @param externaltransfercredit ArrayList
	 */
	public void setExternaltransfercreditTableData(Object lstExternaltransfercredit) {
		this.externaltransfercreditCollection.setData(lstExternaltransfercredit);
	}
	/**
	 * Set the externaltransfercreditFilHide String.
	 * @param externaltransfercreditFilHide
	 */
	public void setExternaltransfercreditFilHide(String externaltransfercreditFilHide) {
		this.externaltransfercreditFilHide=externaltransfercreditFilHide;
	}
	/**
	 * Get the externaltransfercreditFilHide String.
	 * @return externaltransfercreditFilHide
	 */
	public String getExternaltransfercreditFilHide() {
		return this.externaltransfercreditFilHide;
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
	 * Set the infoRTAB String.
	 * @param infoRTAB
	 */
	public void setInfoRTAB(String infoRTAB) {
		this.infoRTAB=infoRTAB;
	}
	/**
	 * Get the infoRTAB String.
	 * @return infoRTAB
	 */
	public String getInfoRTAB() {
		return this.infoRTAB;
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

	public String getTotalTransfer() {
		return totalTransfer;
	}

	public void setTotalTransfer(String totalTransfer) {
		this.totalTransfer = totalTransfer;
	}

	public String getTotalDollarlimit() {
		return totalDollarlimit;
	}

	public void setTotalDollarlimit(String totalDollarlimit) {
		this.totalDollarlimit = totalDollarlimit;
	}

	public String getCheckExternalAccStatus() {
		return checkExternalAccStatus;
	}

	public void setCheckExternalAccStatus(String checkExternalAccStatus) {
		this.checkExternalAccStatus = checkExternalAccStatus;
	}

	public String getEditfreqComboVal() {
		return editfreqComboVal;
	}

	public void setEditfreqComboVal(String editfreqComboVal) {
		this.editfreqComboVal = editfreqComboVal;
	}

	public String getEditApprInitiationDate() {
		return editApprInitiationDate;
	}

	public void setEditApprInitiationDate(String editApprInitiationDate) {
		this.editApprInitiationDate = editApprInitiationDate;
	}

	public String getPrevTransactionAmnt() {
		return prevTransactionAmnt;
	}

	public void setPrevTransactionAmnt(String prevTransactionAmnt) {
		this.prevTransactionAmnt = prevTransactionAmnt;
	}

	public String getFirstLastInitiationDate() {
		return firstLastInitiationDate;
	}

	public void setFirstLastInitiationDate(String firstLastInitiationDate) {
		this.firstLastInitiationDate = firstLastInitiationDate;
	}

	public String getCutOffTime() {
		return cutOffTime;
	}

	public void setCutOffTime(String cutOffTime) {
		this.cutOffTime = cutOffTime;
	}

	public String getPrevTransactionDate() {
		return prevTransactionDate;
	}

	public void setPrevTransactionDate(String prevTransactionDate) {
		this.prevTransactionDate = prevTransactionDate;
	}

	public String getAchDays() {
		return achDays;
	}

	public void setAchDays(String achDays) {
		this.achDays = achDays;
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
		this.cashAvailable=null;
		this.marginAvailable=null;
		this.delayedDebitCd=null;
		this.spendingPower=null;
		this.toAccount=null;
		this.toAccEdit=null;
		this.paymentamount=null;
		this.initiationDate=null;
		this.initiationDateHidden=null;
		this.frequencyradio=null;
		this.frequencycombo=null;
		this.durationradio=null;
		this.durationenddate=null;
		this.durationenddateHidden=null;
		this.durationdollarlimit=null;
		this.durationNoOfTransfers=null;
		this.PAYPAYEEACCNUM=null;
		this.PAYDEBITAMT=null;
		this.PAYCURRCODE=null;
		this.PAYTYPECODE=null;
		this.PAYCONSOLIDATE=null;
		this.PAYPAYEENAME1=null;
		this.PAYPAYEEBRANCHCODE=null;
		this.PAYBATCHREF=null;
		this.PAYPAYEEID=null;
		this.PAYPAYEEBANKCODE=null;
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
		this.infoRTAB=null;
		this.businessDate=null;
		this.estArrivalDate=null;
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
		strB.append ("cashAvailable=" + cashAvailable + "\r\n");
		strB.append ("marginAvailable=" + marginAvailable + "\r\n");
		strB.append ("delayedDebitCd=" + delayedDebitCd + "\r\n");
		strB.append ("spendingPower=" + spendingPower + "\r\n");
		strB.append ("toAccount=" + toAccount + "\r\n");
		strB.append ("toAccEdit=" + toAccEdit + "\r\n");
		strB.append ("paymentamount=" + paymentamount + "\r\n");
		strB.append ("initiationDate=" + initiationDate + "\r\n");
		strB.append ("initiationDateHidden=" + initiationDateHidden + "\r\n");
		strB.append ("frequencyradio=" + frequencyradio + "\r\n");
		strB.append ("frequencycombo=" + frequencycombo + "\r\n");
		strB.append ("durationradio=" + durationradio + "\r\n");
		strB.append ("durationenddate=" + durationenddate + "\r\n");
		strB.append ("durationenddateHidden=" + durationenddateHidden + "\r\n");
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
		strB.append ("infoRTAB=" + infoRTAB + "\r\n");
		strB.append ("businessDate=" + businessDate + "\r\n");
		strB.append ("estArrivalDate=" + estArrivalDate + "\r\n");
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
		Object[][] objArr = {{"ExternalTransfer", "ScreenName"}};
		reportInfo.addElement(objArr);
		objArr = null;
		Object[][] objArr1 = {{"screentitle", "GROUP"},{"externaltransfercredit","Table"}  };
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
		Object[][] objArr = {{"ExternalTransfer", "ScreenName"}};
		chartInfo.addElement(objArr);
		objArr = null;
		return chartInfo;
	}

	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request) {
		ActionErrors errors=new ActionErrors();
		if(!getAction().equals("INIT")){
			errors=super.validate(mapping,request);
		}
		return errors;
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

	public String getRsaReviewFlag() {
		return rsaReviewFlag;
	}

	public void setRsaReviewFlag(String rsaReviewFlag) {
		this.rsaReviewFlag = rsaReviewFlag;
	}

}