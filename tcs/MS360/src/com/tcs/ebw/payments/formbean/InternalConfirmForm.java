package com.tcs.ebw.payments.formbean;

import org.apache.struts.action.ActionMapping;

import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.payments.transferobject.TrxnDetailsOutputTO;

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
public class InternalConfirmForm extends EbwForm implements java.io.Serializable {

	/**
	 * Form Bean Constructor. 
	 */ 
	public InternalConfirmForm () 
	{
		try 
		{
			setState("InternalConfirm_INIT");

			loadReasonCodesConfiguration();
			loadActionDetailsConfiguration();
			loadAuthDetailsConfiguration();
			loadSignedAuthDetailsConfiguration();

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void populateCollections() throws Exception{
	}

	public void copyCollections(InternalConfirmForm srcForm) {
	}

	// Instance Variables
	private String confirmationNo = null;
	private String trxnType = null;
	private String trxnAmount = null;
	private String frequencyType = null;
	private String confirmationNoHidden = null;
	private String txnStatusDesc = null;
	private String accTierFromAcc = null;
	private String accTierToAcc = null;
	private String accNoFromAcc = null;
	private String accNoToAcc = null;
	private String routingNoFromAcc = null;
	private String routingNoToAcc = null;
	private String ownerFromAcc = null;
	private String ownerToAcc = null;
	private String accHeldAtFromAcc = null;
	private String accHeldAtToAcc = null;
	private String accClassFromAcc = null;
	private String accClassToAcc = null;
	private String accSubClassFromAcc = null;
	private String accSubClassToAcc = null;
	private String accNameFromAcc = null;
	private String accNameToAcc = null;
	private String accDtlsFromAcc = null;
	private String accDtlsToAcc = null;
	private String printMode = null;
	private String[] reasonCodesSelectId = null;
	private String[] REASONCODEDATE = null;
	private String[] REASONCODETIME = null;
	private String[] REASONCODEDESC = null;
	private DataInterface reasonCodesCollection;
	private String reasonCodesFilHide;
	private String[] actionDetailsSelectId = null;
	private String[] DEPT = null;
	private String[] ACTION = null;
	private String[] LOGINID = null;
	private String[] USERNAME = null;
	private String[] ACTIONDATE = null;
	private String[] STATUS = null;
	private String[] COMMENTS = null;
	private DataInterface actionDetailsCollection;
	private String actionDetailsFilHide;
	private String batchRefNumber;
	private String payStatusCode; // For status consistency check ...
	private String txnTypeCode; // TransactionType code...
	private String owner = null;
	private String entryDate = null;
	private String transDate = null;
	private String businessRuleMessages = null;
	private boolean isBrErrors=false;
	private boolean isBrWarnings=false;
	private TrxnDetailsOutputTO txnDetails = null;
	private String[] authDetailsSelectId = null;
	private String[] signedAuthDetailsSelectId = null;
	private String[] INFORECEIVER = null;
	private String[] SPOKETO = null;
	private String[] AUTHDATE = null;
	private String[] AUTHTIME = null;
	private String[] CLIENTVERIFICATION = null;
	private String[] COMFIRMEDBY = null;
	private String[] AUTHUSERNAME = null;
	private DataInterface authDetailsCollection;
	private String authDetailsFilHide;
	private DataInterface signedAuthDetailsCollection;
	private String signedAuthDetailsFilHide;
	private String auth_mode=null;
	private String recurringFlag=null;
	private String screenNavigationFlag = null;
	private String untilDollarLimit = null;

	/**
	 * @return the txnDetails
	 */
	public TrxnDetailsOutputTO getTxnDetails() {
		return txnDetails;
	}

	/**
	 * @param txnDetails the txnDetails to set
	 */
	public void setTxnDetails(TrxnDetailsOutputTO txnDetails) {
		this.txnDetails = txnDetails;
	}

	/**
	 * @return the txnTypeCode
	 */
	public String getTxnTypeCode() {
		return txnTypeCode;
	}

	/**
	 * @param txnTypeCode the txnTypeCode to set
	 */
	public void setTxnTypeCode(String txnTypeCode) {
		this.txnTypeCode = txnTypeCode;
	}

	/**
	 * Set the confirmationNo String.
	 * @param confirmationNo
	 */
	public void setConfirmationNo(String confirmationNo) {
		this.confirmationNo=confirmationNo;
	}
	/**
	 * Get the confirmationNo String.
	 * @return confirmationNo
	 */
	public String getConfirmationNo() {
		return this.confirmationNo;
	}

	/**
	 * Set the trxnType String.
	 * @param trxnType
	 */
	public void setTrxnType(String trxnType) {
		this.trxnType=trxnType;
	}
	/**
	 * Get the trxnType String.
	 * @return trxnType
	 */
	public String getTrxnType() {
		return this.trxnType;
	}

	/**
	 * Set the trxnAmount String.
	 * @param trxnAmount
	 */
	public void setTrxnAmount(String trxnAmount) {
		this.trxnAmount=trxnAmount;
	}
	/**
	 * Get the trxnAmount String.
	 * @return trxnAmount
	 */
	public String getTrxnAmount() {
		return this.trxnAmount;
	}

	/**
	 * Set the frequencyType String.
	 * @param frequencyType
	 */
	public void setFrequencyType(String frequencyType) {
		this.frequencyType=frequencyType;
	}
	/**
	 * Get the frequencyType String.
	 * @return frequencyType
	 */
	public String getFrequencyType() {
		return this.frequencyType;
	}

	/**
	 * Set the confirmationNoHidden String.
	 * @param confirmationNoHidden
	 */
	public void setConfirmationNoHidden(String confirmationNoHidden) {
		this.confirmationNoHidden=confirmationNoHidden;
	}
	/**
	 * Get the confirmationNoHidden String.
	 * @return confirmationNoHidden
	 */
	public String getConfirmationNoHidden() {
		return this.confirmationNoHidden;
	}

	/**
	 * Set the accTierFromAcc String.
	 * @param accTierFromAcc
	 */
	public void setAccTierFromAcc(String accTierFromAcc) {
		this.accTierFromAcc=accTierFromAcc;
	}
	/**
	 * Get the accTierFromAcc String.
	 * @return accTierFromAcc
	 */
	public String getAccTierFromAcc() {
		return this.accTierFromAcc;
	}

	/**
	 * Set the accTierToAcc String.
	 * @param accTierToAcc
	 */
	public void setAccTierToAcc(String accTierToAcc) {
		this.accTierToAcc=accTierToAcc;
	}
	/**
	 * Get the accTierToAcc String.
	 * @return accTierToAcc
	 */
	public String getAccTierToAcc() {
		return this.accTierToAcc;
	}

	/**
	 * Set the accNoFromAcc String.
	 * @param accNoFromAcc
	 */
	public void setAccNoFromAcc(String accNoFromAcc) {
		this.accNoFromAcc=accNoFromAcc;
	}
	/**
	 * Get the accNoFromAcc String.
	 * @return accNoFromAcc
	 */
	public String getAccNoFromAcc() {
		return this.accNoFromAcc;
	}

	/**
	 * Set the accNoToAcc String.
	 * @param accNoToAcc
	 */
	public void setAccNoToAcc(String accNoToAcc) {
		this.accNoToAcc=accNoToAcc;
	}
	/**
	 * Get the accNoToAcc String.
	 * @return accNoToAcc
	 */
	public String getAccNoToAcc() {
		return this.accNoToAcc;
	}

	/**
	 * Set the routingNoFromAcc String.
	 * @param routingNoFromAcc
	 */
	public void setRoutingNoFromAcc(String routingNoFromAcc) {
		this.routingNoFromAcc=routingNoFromAcc;
	}
	/**
	 * Get the routingNoFromAcc String.
	 * @return routingNoFromAcc
	 */
	public String getRoutingNoFromAcc() {
		return this.routingNoFromAcc;
	}

	/**
	 * Set the routingNoToAcc String.
	 * @param routingNoToAcc
	 */
	public void setRoutingNoToAcc(String routingNoToAcc) {
		this.routingNoToAcc=routingNoToAcc;
	}
	/**
	 * Get the routingNoToAcc String.
	 * @return routingNoToAcc
	 */
	public String getRoutingNoToAcc() {
		return this.routingNoToAcc;
	}

	/**
	 * Set the accHeldAtFromAcc String.
	 * @param accHeldAtFromAcc
	 */
	public void setAccHeldAtFromAcc(String accHeldAtFromAcc) {
		this.accHeldAtFromAcc=accHeldAtFromAcc;
	}
	/**
	 * Get the accHeldAtFromAcc String.
	 * @return accHeldAtFromAcc
	 */
	public String getAccHeldAtFromAcc() {
		return this.accHeldAtFromAcc;
	}

	/**
	 * Set the accHeldAtToAcc String.
	 * @param accHeldAtToAcc
	 */
	public void setAccHeldAtToAcc(String accHeldAtToAcc) {
		this.accHeldAtToAcc=accHeldAtToAcc;
	}
	/**
	 * Get the accHeldAtToAcc String.
	 * @return accHeldAtToAcc
	 */
	public String getAccHeldAtToAcc() {
		return this.accHeldAtToAcc;
	}

	/**
	 * Set the accClassFromAcc String.
	 * @param accClassFromAcc
	 */
	public void setAccClassFromAcc(String accClassFromAcc) {
		this.accClassFromAcc=accClassFromAcc;
	}
	/**
	 * Get the accClassFromAcc String.
	 * @return accClassFromAcc
	 */
	public String getAccClassFromAcc() {
		return this.accClassFromAcc;
	}

	/**
	 * Set the accClassToAcc String.
	 * @param accClassToAcc
	 */
	public void setAccClassToAcc(String accClassToAcc) {
		this.accClassToAcc=accClassToAcc;
	}
	/**
	 * Get the accClassToAcc String.
	 * @return accClassToAcc
	 */
	public String getAccClassToAcc() {
		return this.accClassToAcc;
	}

	/**
	 * Set the accSubClassFromAcc String.
	 * @param accSubClassFromAcc
	 */
	public void setAccSubClassFromAcc(String accSubClassFromAcc) {
		this.accSubClassFromAcc=accSubClassFromAcc;
	}
	/**
	 * Get the accSubClassFromAcc String.
	 * @return accSubClassFromAcc
	 */
	public String getAccSubClassFromAcc() {
		return this.accSubClassFromAcc;
	}

	/**
	 * Set the accSubClassToAcc String.
	 * @param accSubClassToAcc
	 */
	public void setAccSubClassToAcc(String accSubClassToAcc) {
		this.accSubClassToAcc=accSubClassToAcc;
	}
	/**
	 * Get the accSubClassToAcc String.
	 * @return accSubClassToAcc
	 */
	public String getAccSubClassToAcc() {
		return this.accSubClassToAcc;
	}

	/**
	 * Set the accNameFromAcc String.
	 * @param accNameFromAcc
	 */
	public void setAccNameFromAcc(String accNameFromAcc) {
		this.accNameFromAcc=accNameFromAcc;
	}
	/**
	 * Get the accNameFromAcc String.
	 * @return accNameFromAcc
	 */
	public String getAccNameFromAcc() {
		return this.accNameFromAcc;
	}

	/**
	 * Set the accNameToAcc String.
	 * @param accNameToAcc
	 */
	public void setAccNameToAcc(String accNameToAcc) {
		this.accNameToAcc=accNameToAcc;
	}
	/**
	 * Get the accNameToAcc String.
	 * @return accNameToAcc
	 */
	public String getAccNameToAcc() {
		return this.accNameToAcc;
	}

	/**
	 * Set the reasonCodesSelectId String[].
	 * @param reasonCodesSelectId
	 */
	public void setReasonCodesSelectId(String[] reasonCodesSelectId) {
		this.reasonCodesSelectId=reasonCodesSelectId;
	}
	/**
	 * Get the reasonCodesSelectId String[].
	 * @return reasonCodesSelectId
	 */
	public String[] getReasonCodesSelectId() {
		return this.reasonCodesSelectId;
	}

	/**
	 * Set the REASONCODEDATE String[].
	 * @param REASONCODEDATE
	 */
	public void setREASONCODEDATE(String[] REASONCODEDATE) {
		this.REASONCODEDATE=REASONCODEDATE;
	}
	/**
	 * Get the REASONCODEDATE String[].
	 * @return REASONCODEDATE
	 */
	public String[] getREASONCODEDATE() {
		return this.REASONCODEDATE;
	}

	/**
	 * Set the REASONCODETIME String[].
	 * @param REASONCODETIME
	 */
	public void setREASONCODETIME(String[] REASONCODETIME) {
		this.REASONCODETIME=REASONCODETIME;
	}
	/**
	 * Get the REASONCODETIME String[].
	 * @return REASONCODETIME
	 */
	public String[] getREASONCODETIME() {
		return this.REASONCODETIME;
	}

	/**
	 * Set the REASONCODEDESC String[].
	 * @param REASONCODEDESC
	 */
	public void setREASONCODEDESC(String[] REASONCODEDESC) {
		this.REASONCODEDESC=REASONCODEDESC;
	}
	/**
	 * Get the REASONCODEDESC String[].
	 * @return REASONCODEDESC
	 */
	public String[] getREASONCODEDESC() {
		return this.REASONCODEDESC;
	}

	/**
	 * Load DataInterface for the reasonCodes
	 */
	public void loadReasonCodesConfiguration() {
		EbwTableHelper helper= new EbwTableHelper("");
		ArrayList arrTableCols=new ArrayList();
		TableColAttrObj tableColAttr = null;
		helper.setTableName("reasonCodes");
		helper.setTableTitle("");

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("reasonCodesSelectId");
		tableColAttr.setComponentType("RadioButton");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.reasonCodes.reasonCodesSelectId.Select\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"radio\" name=\"reasonCodesSelectId\" id=\"reasonCodesSelectId~VARIABLE\" value= \"~VARIABLE\" ><label for=\"reasonCodesSelectId\" ><ebwbean:message key=\"InternalConfirm.reasonCodes.reasonCodesSelectId.Select\" /></label>");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("REASONCODEDATE");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.reasonCodes.REASONCODEDATE\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("REASONCODETIME");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.reasonCodes.REASONCODETIME\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("REASONCODEVAL");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.reasonCodes.REASONCODEVAL\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("REASONCODESEVERITY");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.reasonCodes.REASONCODESEVERITY\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("REASONCODEDESC");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.reasonCodes.REASONCODEDESC\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);
		helper.setColAttrObjs(arrTableCols);
		this.reasonCodesCollection = helper;
	}

	/**
	 * Set DataInterface for the reasonCodes Table.
	 * @param reasonCodesCollection
	 */
	public void setReasonCodesCollection(DataInterface reasonCodesCollection) {
		this.reasonCodesCollection=reasonCodesCollection;
	}
	/**
	 * Get DataInterface for the reasonCodes Table.
	 * @return reasonCodesCollection
	 */
	public DataInterface getReasonCodesCollection() {
		return this.reasonCodesCollection;
	}

	/**
	 * Set Table Data as an arraylist for the reasonCodes Table.
	 * @param reasonCodes ArrayList
	 */
	public void setReasonCodesTableData(Object lstReasonCodes) {
		this.reasonCodesCollection.setData(lstReasonCodes);
	}
	/**
	 * Set the reasonCodesFilHide String.
	 * @param reasonCodesFilHide
	 */
	public void setReasonCodesFilHide(String reasonCodesFilHide) {
		this.reasonCodesFilHide=reasonCodesFilHide;
	}
	/**
	 * Get the reasonCodesFilHide String.
	 * @return reasonCodesFilHide
	 */
	public String getReasonCodesFilHide() {
		return this.reasonCodesFilHide;
	}

	/**
	 * Set the actionDetailsSelectId String[].
	 * @param actionDetailsSelectId
	 */
	public void setActionDetailsSelectId(String[] actionDetailsSelectId) {
		this.actionDetailsSelectId=actionDetailsSelectId;
	}
	/**
	 * Get the actionDetailsSelectId String[].
	 * @return actionDetailsSelectId
	 */
	public String[] getActionDetailsSelectId() {
		return this.actionDetailsSelectId;
	}

	/**
	 * Set the DEPT String[].
	 * @param DEPT
	 */
	public void setDEPT(String[] DEPT) {
		this.DEPT=DEPT;
	}
	/**
	 * Get the DEPT String[].
	 * @return DEPT
	 */
	public String[] getDEPT() {
		return this.DEPT;
	}

	/**
	 * Set the ACTION String[].
	 * @param ACTION
	 */
	public void setACTION(String[] ACTION) {
		this.ACTION=ACTION;
	}
	/**
	 * Get the ACTION String[].
	 * @return ACTION
	 */
	public String[] getACTION() {
		return this.ACTION;
	}

	/**
	 * Set the LOGINID String[].
	 * @param LOGINID
	 */
	public void setLOGINID(String[] LOGINID) {
		this.LOGINID=LOGINID;
	}
	/**
	 * Get the LOGINID String[].
	 * @return LOGINID
	 */
	public String[] getLOGINID() {
		return this.LOGINID;
	}

	/**
	 * Set the USERNAME String[].
	 * @param USERNAME
	 */
	public void setUSERNAME(String[] USERNAME) {
		this.USERNAME=USERNAME;
	}
	/**
	 * Get the USERNAME String[].
	 * @return USERNAME
	 */
	public String[] getUSERNAME() {
		return this.USERNAME;
	}

	/**
	 * Set the ACTIONDATE String[].
	 * @param ACTIONDATE
	 */
	public void setACTIONDATE(String[] ACTIONDATE) {
		this.ACTIONDATE=ACTIONDATE;
	}
	/**
	 * Get the ACTIONDATE String[].
	 * @return ACTIONDATE
	 */
	public String[] getACTIONDATE() {
		return this.ACTIONDATE;
	}

	/**
	 * @return the sTATUS
	 */
	public String[] getSTATUS() {
		return STATUS;
	}

	/**
	 * @param status the sTATUS to set
	 */
	public void setSTATUS(String[] status) {
		STATUS = status;
	}

	/**
	 * Set the COMMENTS String[].
	 * @param COMMENTS
	 */
	public void setCOMMENTS(String[] COMMENTS) {
		this.COMMENTS=COMMENTS;
	}
	/**
	 * Get the COMMENTS String[].
	 * @return COMMENTS
	 */
	public String[] getCOMMENTS() {
		return this.COMMENTS;
	}

	/**
	 * Load DataInterface for the actionDetails
	 */
	public void loadActionDetailsConfiguration() {
		EbwTableHelper helper= new EbwTableHelper("");
		ArrayList arrTableCols=new ArrayList();
		TableColAttrObj tableColAttr = null;
		helper.setTableName("actionDetails");
		helper.setTableTitle("");

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("actionDetailsSelectId");
		tableColAttr.setComponentType("RadioButton");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.actionDetails.actionDetailsSelectId.Select\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"radio\" name=\"actionDetailsSelectId\" id=\"actionDetailsSelectId~VARIABLE\" value= \"~VARIABLE\" ><label for=\"actionDetailsSelectId\" ><ebwbean:message key=\"InternalConfirm.actionDetails.actionDetailsSelectId.Select\" /></label>");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("DEPT");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.actionDetails.DEPT\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("ACTION");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.actionDetails.ACTION\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("LOGINID");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.actionDetails.LOGINID\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("USERNAME");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.actionDetails.USERNAME\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("ACTIONDATE");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.actionDetails.ACTIONDATE\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("STATUS");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.actionDetails.STATUS\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("COMMENTS");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.actionDetails.COMMENTS\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);
		helper.setColAttrObjs(arrTableCols);
		this.actionDetailsCollection = helper;
	}

	/**
	 * Set DataInterface for the actionDetails Table.
	 * @param actionDetailsCollection
	 */
	public void setActionDetailsCollection(DataInterface actionDetailsCollection) {
		this.actionDetailsCollection=actionDetailsCollection;
	}
	/**
	 * Get DataInterface for the actionDetails Table.
	 * @return actionDetailsCollection
	 */
	public DataInterface getActionDetailsCollection() {
		return this.actionDetailsCollection;
	}

	/**
	 * Set Table Data as an arraylist for the actionDetails Table.
	 * @param actionDetails ArrayList
	 */
	public void setActionDetailsTableData(Object lstActionDetails) {
		this.actionDetailsCollection.setData(lstActionDetails);
	}
	/**
	 * Set the actionDetailsFilHide String.
	 * @param actionDetailsFilHide
	 */
	public void setActionDetailsFilHide(String actionDetailsFilHide) {
		this.actionDetailsFilHide=actionDetailsFilHide;
	}
	/**
	 * Get the actionDetailsFilHide String.
	 * @return actionDetailsFilHide
	 */
	public String getActionDetailsFilHide() {
		return this.actionDetailsFilHide;
	}

	/**
	 * Set the authDetailsSelectId String[].
	 * @param authDetailsSelectId
	 */
	public void setAuthDetailsSelectId(String[] authDetailsSelectId) {
		this.authDetailsSelectId=authDetailsSelectId;
	}
	/**
	 * Get the authDetailsSelectId String[].
	 * @return authDetailsSelectId
	 */
	public String[] getAuthDetailsSelectId() {
		return this.authDetailsSelectId;
	}
	/**
	 * @return the iNFORECEIVER
	 */
	public String[] getINFORECEIVER() {
		return INFORECEIVER;
	}
	/**
	 * @param inforeceiver the iNFORECEIVER to set
	 */
	public void setINFORECEIVER(String[] inforeceiver) {
		INFORECEIVER = inforeceiver;
	}
	/**
	 * @return the sPOKETO
	 */
	public String[] getSPOKETO() {
		return SPOKETO;
	}
	/**
	 * @param spoketo the sPOKETO to set
	 */
	public void setSPOKETO(String[] spoketo) {
		SPOKETO = spoketo;
	}
	/**
	 * @return the aUTHDATE
	 */
	public String[] getAUTHDATE() {
		return AUTHDATE;
	}
	/**
	 * @param authdate the aUTHDATE to set
	 */
	public void setAUTHDATE(String[] authdate) {
		AUTHDATE = authdate;
	}
	/**
	 * @return the aUTHTIME
	 */
	public String[] getAUTHTIME() {
		return AUTHTIME;
	}
	/**
	 * @param authtime the aUTHTIME to set
	 */
	public void setAUTHTIME(String[] authtime) {
		AUTHTIME = authtime;
	}
	/**
	 * @return the cLIENTVERIFICATION
	 */
	public String[] getCLIENTVERIFICATION() {
		return CLIENTVERIFICATION;
	}
	/**
	 * @param clientverification the cLIENTVERIFICATION to set
	 */
	public void setCLIENTVERIFICATION(String[] clientverification) {
		CLIENTVERIFICATION = clientverification;
	}
	/**
	 * @return the cOMFIRMEDBY
	 */
	public String[] getCOMFIRMEDBY() {
		return COMFIRMEDBY;
	}
	/**
	 * @param comfirmedby the cOMFIRMEDBY to set
	 */
	public void setCOMFIRMEDBY(String[] comfirmedby) {
		COMFIRMEDBY = comfirmedby;
	}

	/**
	 * @return the aUTHUSERNAME
	 */
	public String[] getAUTHUSERNAME() {
		return AUTHUSERNAME;
	}
	/**
	 * @param authusername the aUTHUSERNAME to set
	 */
	public void setAUTHUSERNAME(String[] authusername) {
		AUTHUSERNAME = authusername;
	}

	/**
	 * Load DataInterface for the authDetails
	 */
	public void loadAuthDetailsConfiguration() 
	{
		EbwTableHelper helper= new EbwTableHelper("");
		ArrayList arrTableCols=new ArrayList();
		TableColAttrObj tableColAttr = null;
		helper.setTableName("authDetails");
		helper.setTableTitle("");

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("authDetailsSelectId");
		tableColAttr.setComponentType("RadioButton");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.authDetails.authDetailsSelectId.Select\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"radio\" name=\"authDetailsSelectId\" id=\"authDetailsSelectId~VARIABLE\" value= \"~VARIABLE\" ><label for=\"authDetailsSelectId\" ><ebwbean:message key=\"InternalConfirm.authDetails.authDetailsSelectId.Select\" /></label>");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("INFORECEIVER");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.authDetails.INFORECEIVER\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("SPOKETO");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.authDetails.SPOKETO\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("AUTHDATE");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.authDetails.AUTHDATE\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("AUTHTIME");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.authDetails.AUTHTIME\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("CLIENTVERIFICATION");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.authDetails.CLIENTVERIFICATION\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("COMFIRMEDBY");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.authDetails.COMFIRMEDBY\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("AUTHUSERNAME");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.authDetails.AUTHUSERNAME\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		helper.setColAttrObjs(arrTableCols);
		this.authDetailsCollection = helper;
	}

	/**
	 * Set DataInterface for the authDetails Table.
	 * @param authDetailsCollection
	 */
	public void setAuthDetailsCollection(DataInterface authDetailsCollection) {
		this.authDetailsCollection=authDetailsCollection;
	}
	/**
	 * Get DataInterface for the authDetails Table.
	 * @return authDetailsCollection
	 */
	public DataInterface getAuthDetailsCollection() {
		return this.authDetailsCollection;
	}
	/**
	 * Set Table Data as an arraylist for the authDetails Table.
	 * @param authDetails ArrayList
	 */
	public void setAuthDetailsTableData(Object lstAuthDetails) {
		this.authDetailsCollection.setData(lstAuthDetails);
	}
	/**
	 * Set the authDetailsFilHide String.
	 * @param authDetailsFilHide
	 */
	public void setAuthDetailsFilHide(String authDetailsFilHide) {
		this.authDetailsFilHide=authDetailsFilHide;
	}
	/**
	 * Get the authDetailsFilHide String.
	 * @return authDetailsFilHide
	 */
	public String getAuthDetailsFilHide() {
		return this.authDetailsFilHide;
	}

	/**
	 * Load DataInterface for the signedAuthDetails
	 */
	public void loadSignedAuthDetailsConfiguration() 
	{
		EbwTableHelper helper= new EbwTableHelper("");
		ArrayList arrTableCols=new ArrayList();
		TableColAttrObj tableColAttr = null;
		helper.setTableName("signedAuthDetails");
		helper.setTableTitle("");

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("signedAuthDetailsSelectId");
		tableColAttr.setComponentType("RadioButton");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.signedAuthDetails.signedAuthDetailsSelectId.Select\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"radio\" name=\"signedAuthDetailsSelectId\" id=\"signedAuthDetailsSelectId~VARIABLE\" value= \"~VARIABLE\" ><label for=\"signedAuthDetailsSelectId\" ><ebwbean:message key=\"InternalConfirm.signedAuthDetails.signedAuthDetailsSelectId.Select\" /></label>");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("SPOKETO");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.signedAuthDetails.SPOKETO\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("COMFIRMEDBY");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.signedAuthDetails.COMFIRMEDBY\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("AUTHUSERNAME");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"InternalConfirm.signedAuthDetails.AUTHUSERNAME\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("~VARIABLE");
		arrTableCols.add(tableColAttr);

		helper.setColAttrObjs(arrTableCols);
		this.signedAuthDetailsCollection = helper;
	}

	/**
	 * Set DataInterface for the signedAuthDetails Table.
	 * @param signedAuthDetailsCollection
	 */
	public void setSignedAuthDetailsCollection(DataInterface signedAuthDetailsCollection) {
		this.signedAuthDetailsCollection=signedAuthDetailsCollection;
	}

	/**
	 * Get DataInterface for the signedAuthDetails Table.
	 * @return signedAuthDetailsCollection
	 */
	public DataInterface getSignedAuthDetailsCollection() {
		return this.signedAuthDetailsCollection;
	}

	/**
	 * Set Table Data as an arraylist for the signedAuthDetails Table.
	 * @param signedAuthDetails ArrayList
	 */
	public void setSignedAuthDetailsTableData(Object lstSignedAuthDetails) {
		this.signedAuthDetailsCollection.setData(lstSignedAuthDetails);
	}

	/**
	 * Set the signedAuthDetailsFilHide String.
	 * @param signedAuthDetailsFilHide
	 */
	public void setSignedAuthDetailsFilHide(String signedAuthDetailsFilHide) {
		this.signedAuthDetailsFilHide=signedAuthDetailsFilHide;
	}

	/**
	 * Get the signedAuthDetailsFilHide String.
	 * @return signedAuthDetailsFilHide
	 */
	public String getSignedAuthDetailsFilHide() {
		return this.signedAuthDetailsFilHide;
	}

	public String getBatchRefNumber() {
		return batchRefNumber;
	}

	public void setBatchRefNumber(String batchRefNumber) {
		this.batchRefNumber = batchRefNumber;
	}

	public String getPayStatusCode() {
		return payStatusCode;
	}

	public void setPayStatusCode(String payStatusCode) {
		this.payStatusCode = payStatusCode;
	}
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the entryDate
	 */
	public String getEntryDate() {
		return entryDate;
	}
	/**
	 * @param entryDate the entryDate to set
	 */
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	/**
	 * @return the transDate
	 */
	public String getTransDate() {
		return transDate;
	}
	/**
	 * @param transDate the transDate to set
	 */
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	/**
	 * @return the businessRuleMessages
	 */
	public String getBusinessRuleMessages() {
		return businessRuleMessages;
	}
	/**
	 * @param businessRuleMessages the businessRuleMessages to set
	 */
	public void setBusinessRuleMessages(String businessRuleMessages) {
		this.businessRuleMessages = businessRuleMessages;
	}
	/**
	 * @return the isBrErrors
	 */
	public boolean isBrErrors() {
		return isBrErrors;
	}
	/**
	 * @param isBrErrors the isBrErrors to set
	 */
	public void setBrErrors(boolean isBrErrors) {
		this.isBrErrors = isBrErrors;
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
	 * @return the ownerFromAcc
	 */
	public String getOwnerFromAcc() {
		return ownerFromAcc;
	}
	/**
	 * @param ownerFromAcc the ownerFromAcc to set
	 */
	public void setOwnerFromAcc(String ownerFromAcc) {
		this.ownerFromAcc = ownerFromAcc;
	}
	/**
	 * @return the ownerToAcc
	 */
	public String getOwnerToAcc() {
		return ownerToAcc;
	}
	/**
	 * @return the txnStatusDesc
	 */
	public String getTxnStatusDesc() {
		return txnStatusDesc;
	}
	/**
	 * @param txnStatusDesc the txnStatusDesc to set
	 */
	public void setTxnStatusDesc(String txnStatusDesc) {
		this.txnStatusDesc = txnStatusDesc;
	}
	/**
	 * @param ownerToAcc the ownerToAcc to set
	 */
	public void setOwnerToAcc(String ownerToAcc) {
		this.ownerToAcc = ownerToAcc;
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
	 * @return the recurringFlag
	 */
	public String getRecurringFlag() {
		return recurringFlag;
	}
	/**
	 * @param recurringFlag the recurringFlag to set
	 */
	public void setRecurringFlag(String recurringFlag) {
		this.recurringFlag = recurringFlag;
	}
	/**
	 * @return the screenNavigationFlag
	 */
	public String getScreenNavigationFlag() {
		return screenNavigationFlag;
	}
	/**
	 * @param screenNavigationFlag the screenNavigationFlag to set
	 */
	public void setScreenNavigationFlag(String screenNavigationFlag) {
		this.screenNavigationFlag = screenNavigationFlag;
	}
	/**
	 * @return the untilDollarLimit
	 */
	public String getUntilDollarLimit() {
		return untilDollarLimit;
	}
	/**
	 * @param untilDollarLimit the untilDollarLimit to set
	 */
	public void setUntilDollarLimit(String untilDollarLimit) {
		this.untilDollarLimit = untilDollarLimit;
	}
	/**
	 * @return the accDtlsFromAcc
	 */
	public String getAccDtlsFromAcc() {
		return accDtlsFromAcc;
	}
	/**
	 * @param accDtlsFromAcc the accDtlsFromAcc to set
	 */
	public void setAccDtlsFromAcc(String accDtlsFromAcc) {
		this.accDtlsFromAcc = accDtlsFromAcc;
	}
	/**
	 * @return the accDtlsToAcc
	 */
	public String getAccDtlsToAcc() {
		return accDtlsToAcc;
	}
	/**
	 * @param accDtlsToAcc the accDtlsToAcc to set
	 */
	public void setAccDtlsToAcc(String accDtlsToAcc) {
		this.accDtlsToAcc = accDtlsToAcc;
	}
	/**
	 * @return the printMode
	 */
	public String getPrintMode() {
		return printMode;
	}
	/**
	 * @param printMode the printMode to set
	 */
	public void setPrintMode(String printMode) {
		this.printMode = printMode;
	}
	/**
	 * @return the signedAuthDetailsSelectId
	 */
	public String[] getSignedAuthDetailsSelectId() {
		return signedAuthDetailsSelectId;
	}
	/**
	 * @param signedAuthDetailsSelectId the signedAuthDetailsSelectId to set
	 */
	public void setSignedAuthDetailsSelectId(String[] signedAuthDetailsSelectId) {
		this.signedAuthDetailsSelectId = signedAuthDetailsSelectId;
	}

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.confirmationNo=null;
		this.trxnType=null;
		this.trxnAmount=null;
		this.frequencyType=null;
		this.confirmationNoHidden=null;
		this.txnStatusDesc=null;
		this.accTierFromAcc=null;
		this.accTierToAcc=null;
		this.accNoFromAcc=null;
		this.accNoToAcc=null;
		this.routingNoFromAcc=null;
		this.routingNoToAcc=null;
		this.accHeldAtFromAcc=null;
		this.accHeldAtToAcc=null;
		this.accClassFromAcc=null;
		this.accClassToAcc=null;
		this.accSubClassFromAcc=null;
		this.accSubClassToAcc=null;
		this.accNameFromAcc=null;
		this.accNameToAcc=null;
		this.reasonCodesSelectId=null;
		this.REASONCODEDATE=null;
		this.REASONCODETIME=null;
		this.REASONCODEDESC=null;
		this.actionDetailsSelectId=null;
		this.DEPT=null;
		this.ACTION=null;
		this.LOGINID=null;
		this.USERNAME=null;
		this.ACTIONDATE=null;
		this.COMMENTS=null;
		this.authDetailsSelectId=null;
		this.INFORECEIVER = null;
		this.SPOKETO = null;
		this.AUTHDATE = null;
		this.AUTHTIME = null;
		this.CLIENTVERIFICATION = null;
		this.COMFIRMEDBY = null;
		this.AUTHUSERNAME = null;
	}

	/**
	 * Returns Vector object for export/report option.
	 * @return Vector object
	 */
	public Vector getReportInformation(){
		Vector reportInfo = new Vector(); 
		Object[][] objArr = {{"InternalConfirm", "ScreenName"}};
		reportInfo.addElement(objArr);
		objArr = null;
		Object[][] objArr1 = {{"screentitle", "GROUP"},{"reasonCodes","Table"} ,{"actionDetails","Table"},{"authDetails","Table"}};
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
		Object[][] objArr = {{"InternalConfirm", "ScreenName"}};
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

		retValue = "InternalConfirmForm ( "
			+ super.toString() + TAB
			+ "confirmationNo = " + this.confirmationNo + TAB
			+ "trxnType = " + this.trxnType + TAB
			+ "trxnAmount = " + this.trxnAmount + TAB
			+ "frequencyType = " + this.frequencyType + TAB
			+ "confirmationNoHidden = " + this.confirmationNoHidden + TAB
			+ "statusHidden = " + this.txnStatusDesc + TAB
			+ "accTierFromAcc = " + this.accTierFromAcc + TAB
			+ "accTierToAcc = " + this.accTierToAcc + TAB
			+ "accNoFromAcc = " + this.accNoFromAcc + TAB
			+ "accNoToAcc = " + this.accNoToAcc + TAB
			+ "routingNoFromAcc = " + this.routingNoFromAcc + TAB
			+ "routingNoToAcc = " + this.routingNoToAcc + TAB
			+ "ownerFromAcc = " + this.ownerFromAcc + TAB
			+ "ownerToAcc = " + this.ownerToAcc + TAB
			+ "accHeldAtFromAcc = " + this.accHeldAtFromAcc + TAB
			+ "accHeldAtToAcc = " + this.accHeldAtToAcc + TAB
			+ "accClassFromAcc = " + this.accClassFromAcc + TAB
			+ "accClassToAcc = " + this.accClassToAcc + TAB
			+ "accSubClassFromAcc = " + this.accSubClassFromAcc + TAB
			+ "accSubClassToAcc = " + this.accSubClassToAcc + TAB
			+ "accNameFromAcc = " + this.accNameFromAcc + TAB
			+ "accNameToAcc = " + this.accNameToAcc + TAB
			+ "reasonCodesSelectId = " + this.reasonCodesSelectId + TAB
			+ "REASONCODEDATE = " + this.REASONCODEDATE + TAB
			+ "REASONCODETIME = " + this.REASONCODETIME + TAB
			+ "REASONCODEDESC = " + this.REASONCODEDESC + TAB
			+ "reasonCodesCollection = " + this.reasonCodesCollection + TAB
			+ "reasonCodesFilHide = " + this.reasonCodesFilHide + TAB
			+ "actionDetailsSelectId = " + this.actionDetailsSelectId + TAB
			+ "DEPT = " + this.DEPT + TAB
			+ "ACTION = " + this.ACTION + TAB
			+ "LOGINID = " + this.LOGINID + TAB
			+ "USERNAME = " + this.USERNAME + TAB
			+ "ACTIONDATE = " + this.ACTIONDATE + TAB
			+ "COMMENTS = " + this.COMMENTS + TAB
			+ "actionDetailsCollection = " + this.actionDetailsCollection + TAB
			+ "actionDetailsFilHide = " + this.actionDetailsFilHide + TAB
			+ "batchRefNumber = " + this.batchRefNumber + TAB
			+ "payStatusCode = " + this.payStatusCode + TAB
			+ "txnTypeCode = " + this.txnTypeCode + TAB
			+ "owner = " + this.owner + TAB
			+ "entryDate = " + this.entryDate + TAB
			+ "transDate = " + this.transDate + TAB
			+ "businessRuleMessages = " + this.businessRuleMessages + TAB
			+ "isBrErrors = " + this.isBrErrors + TAB
			+ "isBrWarnings = " + this.isBrWarnings + TAB
			+ "txnDetails = " + this.txnDetails + TAB
			+ "authDetailsSelectId = " + this.authDetailsSelectId + TAB
			+ "INFORECEIVER = " + this.INFORECEIVER + TAB
			+ "SPOKETO = " + this.SPOKETO + TAB
			+ "AUTHDATE = " + this.AUTHDATE + TAB
			+ "AUTHTIME = " + this.AUTHTIME + TAB
			+ "CLIENTVERIFICATION = " + this.CLIENTVERIFICATION + TAB
			+ "COMFIRMEDBY = " + this.COMFIRMEDBY + TAB
			+ "AUTHUSERNAME = " + this.AUTHUSERNAME + TAB
			+ "authDetailsCollection = " + this.authDetailsCollection + TAB
			+ "authDetailsFilHide = " + this.authDetailsFilHide + TAB
			+ " )";

		return retValue;
	}
}