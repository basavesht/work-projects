/*
 * Created on Sat Apr 11 14:33:02 IST 2009
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

import com.tcs.ebw.serverside.services.channelPaymentServices.AccountPlating;
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
public class ExternalPreConfirmForm extends EbwForm implements java.io.Serializable {

	/**
	 * Form Bean Constructor. 
	 */ 
	public ExternalPreConfirmForm () {
		try {
			setState("ExternalPreConfirm_INIT");
			loadExternaltransfercreditConfiguration();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void populateCollections() throws Exception{
		if(this.spokeToCollection==null)
			FetchSpokeToCollection();
		if(this.verbalAuthHourCollection==null)
			FetcheVerbalAuthHourCollection();
		if(this.verbalAuthMinCollection==null)
			FetchVerbalAuthMinCollection();
		if(this.verbalAuthAMorPMCollection==null)
			FetchVerbalAuthAMorPMCollection();
		if(this.clientVerficationCollection==null)
			FetchClientVerficationCollection();
	}

	public void copyCollections(ExternalPreConfirmForm srcForm) {
		this.spokeToCollection=srcForm.getSpokeToCollection();
		this.verbalAuthHourCollection=srcForm.getVerbalAuthHourCollection();
		this.verbalAuthMinCollection=srcForm.getVerbalAuthMinCollection();
		this.verbalAuthAMorPMCollection=srcForm.getVerbalAuthAMorPMCollection();	
		this.clientVerficationCollection=srcForm.getClientVerficationCollection();
	}

	// Instance Variables
	private String error = null;
	private String fromAccount = null;
	private String fromAccEdit = null;
	private String toAccount = null;
	private String toAccEdit = null;
	private String paymentamount = null;
	private String initiationDate = null;
	private String estArrivalDate = null;
	private String frequencyradio = null;
	private String frequencycombo = null;
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
	private String cashAvailable = null;
	private DataInterface externaltransfercreditCollection;
	private String externaltransfercreditFilHide;
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
	private String txnBatchRefNumber = null;
	private String txnPayPayRefNumber = null;
	private String parentTxnNumber = null;
	private String fromKeyAccNumber=null;
	private String toKeyAccNumber=null;
	private String prevPaystatus=null;
	private String brWarnings=null;
	private String brInformations=null;
	private String firstLastInitiationDate=null;
	private String businessRuleMessages = null;
	private boolean isBrErrors=false;
	private String prevTransactionAmnt=null;
	private String prevTransactionDate=null;
	private String fromAccNo_br_fa = null;
	private String toAccNo_br_fa = null;
	private String transactionType = null;
	private Object requestHeaderInfo = null; // FTM related change to add the Request header info..
	private String rsaReviewFlag = null; //RSA Review to be mapped during edit transactions ...
	private boolean isBrWarnings=false;
	private String auth_mode = null;
	private String informationRecvdBy = null;
	private String spokeTo = null;
	private Collection spokeToCollection;
	private String verbalAuthDate = null;
	private String verbalAuthHour = null;
	private Collection verbalAuthHourCollection;
	private String verbalAuthMin = null;
	private Collection verbalAuthMinCollection;
	private String verbalAuthAMorPM = null;
	private Collection verbalAuthAMorPMCollection;
	private String clientVerfication = null;
	private Collection clientVerficationCollection;
	private String authDocumentsReq =null;
	private String userComments = null;
	private String transferTypeIRA = null;
	private String txnTypeIraDisp = null;
	private boolean debitAcntDataInSession = false;
	private boolean creditAcntDataInSession = false;
	private AccountPlating debitAcntPlatingInfo = null;
	private AccountPlating creditAcntPlatingInfo = null;
	private String maxVerbalAuthPastDays = null;
	private String externalKeyClient = null;
	private String otherAcntOwner = null;

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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalPreConfirm.externaltransfercredit.PAYPAYEEACCNUM\" />");
		tableColAttr.setFieldAttribute("class=\"BeneTableName\"");
		tableColAttr.setDefaultValue("");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<select name=\"PAYPAYEEACCNUM\" id=\"PAYPAYEEACCNUM\" ExternalPreConfirmForm></select>");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("PAYDEBITAMT");
		tableColAttr.setComponentType("TextField");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalPreConfirm.externaltransfercredit.PAYDEBITAMT\" />");
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalPreConfirm.externaltransfercredit.PAYCURRCODE\" />");
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalPreConfirm.externaltransfercredit.PAYTYPECODE\" />");
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalPreConfirm.externaltransfercredit.PAYCONSOLIDATE\" />");
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalPreConfirm.externaltransfercredit.PAYPAYEENAME1\" />");
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalPreConfirm.externaltransfercredit.PAYPAYEEBRANCHCODE\" />");
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalPreConfirm.externaltransfercredit.PAYBATCHREF\" />");
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalPreConfirm.externaltransfercredit.PAYPAYEEID\" />");
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ExternalPreConfirm.externaltransfercredit.PAYPAYEEBANKCODE\" />");
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

	public String getPrevTransactionDate() {
		return prevTransactionDate;
	}

	public void setPrevTransactionDate(String prevTransactionDate) {
		this.prevTransactionDate = prevTransactionDate;
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

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Object getRequestHeaderInfo() {
		return requestHeaderInfo;
	}

	public void setRequestHeaderInfo(Object requestHeaderInfo) {
		this.requestHeaderInfo = requestHeaderInfo;
	}

	public String getRsaReviewFlag() {
		return rsaReviewFlag;
	}

	public void setRsaReviewFlag(String rsaReviewFlag) {
		this.rsaReviewFlag = rsaReviewFlag;
	}

	public String getInformationRecvdBy() {
		return informationRecvdBy;
	}

	public void setInformationRecvdBy(String informationRecvdBy) {
		this.informationRecvdBy = informationRecvdBy;
	}	

	public String getSpokeTo() {
		return spokeTo;
	}

	public void setSpokeTo(String spokeTo) {
		this.spokeTo = spokeTo;
	}

	public Collection getSpokeToCollection() {
		return spokeToCollection;
	}

	public void setSpokeToCollection(Collection spokeToCollection) {
		this.spokeToCollection = spokeToCollection;
	}

	public String getVerbalAuthDate() {
		return verbalAuthDate;
	}

	public void setVerbalAuthDate(String verbalAuthDate) {
		this.verbalAuthDate = verbalAuthDate;
	}

	public String getVerbalAuthHour() {
		return verbalAuthHour;
	}

	public void setVerbalAuthHour(String verbalAuthHour) {
		this.verbalAuthHour = verbalAuthHour;
	}

	public Collection getVerbalAuthHourCollection() {
		return verbalAuthHourCollection;
	}

	public void setVerbalAuthHourCollection(Collection verbalAuthHourCollection) {
		this.verbalAuthHourCollection = verbalAuthHourCollection;
	}

	public String getVerbalAuthMin() {
		return verbalAuthMin;
	}

	public void setVerbalAuthMin(String verbalAuthMin) {
		this.verbalAuthMin = verbalAuthMin;
	}

	public Collection getVerbalAuthMinCollection() {
		return verbalAuthMinCollection;
	}

	public void setVerbalAuthMinCollection(Collection verbalAuthMinCollection) {
		this.verbalAuthMinCollection = verbalAuthMinCollection;
	}

	public String getVerbalAuthAMorPM() {
		return verbalAuthAMorPM;
	}

	public void setVerbalAuthAMorPM(String verbalAuthAMorPM) {
		this.verbalAuthAMorPM = verbalAuthAMorPM;
	}

	public Collection getVerbalAuthAMorPMCollection() {
		return verbalAuthAMorPMCollection;
	}

	public void setVerbalAuthAMorPMCollection(Collection verbalAuthAMorPMCollection) {
		this.verbalAuthAMorPMCollection = verbalAuthAMorPMCollection;
	}

	public String getClientVerfication() {
		return clientVerfication;
	}

	public void setClientVerfication(String clientVerfication) {
		this.clientVerfication = clientVerfication;
	}

	public Collection getClientVerficationCollection() {
		return clientVerficationCollection;
	}

	public void setClientVerficationCollection(
			Collection clientVerficationCollection) {
		this.clientVerficationCollection = clientVerficationCollection;
	}

	/**
	 * @return the authDocumentsReq
	 */
	public String getAuthDocumentsReq() {
		return authDocumentsReq;
	}
	/**
	 * @param authDocumentsReq the authDocumentsReq to set
	 */
	public void setAuthDocumentsReq(String authDocumentsReq) {
		this.authDocumentsReq = authDocumentsReq;
	}
	/**
	 * @return the userComments
	 */
	public String getUserComments() {
		return userComments;
	}
	/**
	 * @param userComments the userComments to set
	 */
	public void setUserComments(String userComments) {
		this.userComments = userComments;
	}
	/**
	 * @return the transferTypeIRA
	 */
	public String getTransferTypeIRA() {
		return transferTypeIRA;
	}
	/**
	 * @param transferTypeIRA the transferTypeIRA to set
	 */
	public void setTransferTypeIRA(String transferTypeIRA) {
		this.transferTypeIRA = transferTypeIRA;
	}
	/**
	 * @return the debitAcntDataInSession
	 */
	public boolean isDebitAcntDataInSession() {
		return debitAcntDataInSession;
	}
	/**
	 * @param debitAcntDataInSession the debitAcntDataInSession to set
	 */
	public void setDebitAcntDataInSession(boolean debitAcntDataInSession) {
		this.debitAcntDataInSession = debitAcntDataInSession;
	}
	/**
	 * @return the creditAcntDataInSession
	 */
	public boolean isCreditAcntDataInSession() {
		return creditAcntDataInSession;
	}
	/**
	 * @param creditAcntDataInSession the creditAcntDataInSession to set
	 */
	public void setCreditAcntDataInSession(boolean creditAcntDataInSession) {
		this.creditAcntDataInSession = creditAcntDataInSession;
	}
	/**
	 * @return the debitAcntPlatingInfo
	 */
	public AccountPlating getDebitAcntPlatingInfo() {
		return debitAcntPlatingInfo;
	}
	/**
	 * @param debitAcntPlatingInfo the debitAcntPlatingInfo to set
	 */
	public void setDebitAcntPlatingInfo(AccountPlating debitAcntPlatingInfo) {
		this.debitAcntPlatingInfo = debitAcntPlatingInfo;
	}
	/**
	 * @return the creditAcntPlatingInfo
	 */
	public AccountPlating getCreditAcntPlatingInfo() {
		return creditAcntPlatingInfo;
	}
	/**
	 * @param creditAcntPlatingInfo the creditAcntPlatingInfo to set
	 */
	public void setCreditAcntPlatingInfo(AccountPlating creditAcntPlatingInfo) {
		this.creditAcntPlatingInfo = creditAcntPlatingInfo;
	}
	/**
	 * @return the maxVerbalAuthPastDays
	 */
	public String getMaxVerbalAuthPastDays() {
		return maxVerbalAuthPastDays;
	}
	/**
	 * @param maxVerbalAuthPastDays the maxVerbalAuthPastDays to set
	 */
	public void setMaxVerbalAuthPastDays(String maxVerbalAuthPastDays) {
		this.maxVerbalAuthPastDays = maxVerbalAuthPastDays;
	}
	/**
	 * @return the txnTypeIraDisp
	 */
	public String getTxnTypeIraDisp() {
		return txnTypeIraDisp;
	}
	/**
	 * @param txnTypeIraDisp the txnTypeIraDisp to set
	 */
	public void setTxnTypeIraDisp(String txnTypeIraDisp) {
		this.txnTypeIraDisp = txnTypeIraDisp;
	}

	/**
	 * @return the externalKeyClient
	 */
	public String getExternalKeyClient() {
		return externalKeyClient;
	}
	/**
	 * @param externalKeyClient the externalKeyClient to set
	 */
	public void setExternalKeyClient(String externalKeyClient) {
		this.externalKeyClient = externalKeyClient;
	}
	public void FetchSpokeToCollection() throws Exception
	{
		try
		{
			if (this.spokeToCollection == null) {
				ComboboxData cboData = new ComboboxData();
				this.spokeToCollection = cboData.getComboBoxData("","","","","");
			}
		}catch (EbwException exc) {
			exc.printStackTrace();
			throw exc;
		}
	}

	public void FetcheVerbalAuthHourCollection() throws Exception
	{
		try{
			if (this.verbalAuthHourCollection == null) {
				ComboboxData cboData = new ComboboxData();
				this.verbalAuthHourCollection = cboData.getComboBoxData("getHourValues","","","","");					
			}
		}catch (EbwException exc) {
			exc.printStackTrace();
			throw exc;
		}
	}

	public void FetchVerbalAuthMinCollection() throws Exception
	{
		try{		
			if (this.verbalAuthMinCollection == null) {
				ComboboxData cboData = new ComboboxData();
				this.verbalAuthMinCollection = cboData.getComboBoxData("getMinuteValues","","","","");
			}
		}catch (EbwException exc) {
			exc.printStackTrace();
			throw exc;
		}
	}

	public void FetchVerbalAuthAMorPMCollection() throws Exception
	{
		try{
			if (this.verbalAuthAMorPMCollection == null) {
				ComboboxData cboData = new ComboboxData();
				this.verbalAuthAMorPMCollection = cboData.getComboBoxData("getTimeFormat","","","","");
			}
		}catch (EbwException exc) {
			exc.printStackTrace();
			throw exc;
		}
	}

	public void FetchClientVerficationCollection() throws Exception
	{
		try{
			if (this.clientVerficationCollection == null) {
				ComboboxData cboData = new ComboboxData();
				this.clientVerficationCollection = cboData.getComboBoxData("getClientVerificationDesc" ,"","","","Please Select...");
			}
		}catch (EbwException exc) {
			exc.printStackTrace();
			throw exc;
		}
	}

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.error=null;
		this.fromAccount=null;
		this.fromAccEdit=null;
		this.toAccount=null;
		this.toAccEdit=null;
		this.paymentamount=null;
		this.initiationDate=null;
		this.estArrivalDate=null;
		this.frequencyradio=null;
		this.frequencycombo=null;
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
		this.txnBatchRefNumber=null;
		this.txnPayPayRefNumber=null;
		this.parentTxnNumber=null;
		this.informationRecvdBy=null;
		this.spokeTo=null;
		this.verbalAuthDate=null;
		this.verbalAuthHour=null;
		this.verbalAuthMin=null;
		this.verbalAuthAMorPM=null;
		this.clientVerfication=null;
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
		strB.append ("error=" + error + "\r\n");
		strB.append ("fromAccount=" + fromAccount + "\r\n");
		strB.append ("fromAccEdit=" + fromAccEdit + "\r\n");
		strB.append ("toAccount=" + toAccount + "\r\n");
		strB.append ("toAccEdit=" + toAccEdit + "\r\n");
		strB.append ("paymentamount=" + paymentamount + "\r\n");
		strB.append ("initiationDate=" + initiationDate + "\r\n");
		strB.append ("estArrivalDate=" + estArrivalDate + "\r\n");
		strB.append ("frequencyradio=" + frequencyradio + "\r\n");
		strB.append ("frequencycombo=" + frequencycombo + "\r\n");
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
		strB.append ("txnBatchRefNumber=" + txnBatchRefNumber + "\r\n");
		strB.append ("txnPayPayRefNumber=" + txnPayPayRefNumber + "\r\n");
		strB.append ("parentTxnNumber=" + parentTxnNumber + "\r\n");
		strB.append ("informationRecvdBy=" + informationRecvdBy + "\r\n");
		strB.append ("spokeTo=" + spokeTo + "\r\n");
		strB.append ("verbalAuthDate=" + verbalAuthDate + "\r\n");
		strB.append ("verbalAuthHour=" + verbalAuthHour + "\r\n");
		strB.append ("verbalAuthMin=" + verbalAuthMin + "\r\n");
		strB.append ("verbalAuthAMorPM=" + verbalAuthAMorPM + "\r\n");
		strB.append ("clientVerfication=" + clientVerfication + "\r\n");
		return strB.toString();
	}

	/**
	 * Returns Vector object for export/report option.
	 * @return Vector object
	 */
	public Vector getReportInformation(){
		Vector reportInfo = new Vector(); 
		Object[][] objArr = {{"ExternalPreConfirm", "ScreenName"}};
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
		Object[][] objArr = {{"ExternalPreConfirm", "ScreenName"}};
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

	public String getOtherAcntOwner() {
		return otherAcntOwner;
	}

	public void setOtherAcntOwner(String otherAcntOwner) {
		this.otherAcntOwner = otherAcntOwner;
	}
}