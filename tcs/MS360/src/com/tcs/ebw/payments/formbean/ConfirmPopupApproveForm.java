/*
 * Created on Mon Jun 08 21:45:25 IST 2009
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
public class ConfirmPopupApproveForm extends EbwForm implements java.io.Serializable {

	/**
	 * Form Bean Constructor. 
	 */ 
	public ConfirmPopupApproveForm () {
		try {
			setState("ConfirmPopupApprove_INIT");

			loadReasonCodesConfiguration();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void populateCollections() throws Exception{
	}

	public void copyCollections(ConfirmPopupApproveForm srcForm) {
	}

	// Instance Variables
	private String comments = null;
	private String confirmationNoHidden = null;
	private String confirmationStatusHidden = null;
	private String[] reasonCodesSelectId = null;
	private String[] REASONCODEDATE = null;
	private String[] REASONCODETIME = null;
	private String[] REASONCODEDESC = null;
	private DataInterface reasonCodesCollection;
	private String reasonCodesFilHide;
	private String frequencyValue = null;
	private String trxnType = null;
	private String businessDate = null;
	private String txnBatchRefNumber = null;
	private String txnPayPayRefNumber = null;
	private boolean brErrors;
	private String payStatusCode = null;
	private String next_approver_req_flag = null;
	private String next_approver_role = null;
	private String dont_spawn_flag = "N"; //Defaulted to N..
	private String ofac_case_id = null; 

	/**
	 * @return the next_approver_req_flag
	 */
	public String getNext_approver_req_flag() {
		return next_approver_req_flag;
	}

	/**
	 * @param next_approver_req_flag the next_approver_req_flag to set
	 */
	public void setNext_approver_req_flag(String next_approver_req_flag) {
		this.next_approver_req_flag = next_approver_req_flag;
	}

	/**
	 * @return the next_approver_role
	 */
	public String getNext_approver_role() {
		return next_approver_role;
	}

	/**
	 * @param next_approver_role the next_approver_role to set
	 */
	public void setNext_approver_role(String next_approver_role) {
		this.next_approver_role = next_approver_role;
	}

	/**
	 * Set the comments String.
	 * @param comments
	 */
	public void setComments(String comments) {
		this.comments=comments;
	}
	/**
	 * Get the comments String.
	 * @return comments
	 */
	public String getComments() {
		return this.comments;
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
	 * Set the confirmationStatusHidden String.
	 * @param confirmationStatusHidden
	 */
	public void setConfirmationStatusHidden(String confirmationStatusHidden) {
		this.confirmationStatusHidden=confirmationStatusHidden;
	}
	/**
	 * Get the confirmationStatusHidden String.
	 * @return confirmationStatusHidden
	 */
	public String getConfirmationStatusHidden() {
		return this.confirmationStatusHidden;
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ConfirmPopupApprove.reasonCodes.reasonCodesSelectId.Select\" />");
		tableColAttr.setFieldAttribute("");
		tableColAttr.setDefaultValue("~VARIABLE");
		tableColAttr.setDynamicValue("");
		tableColAttr.setDisplayLength("");
		tableColAttr.setTagContent("<input type=\"radio\" name=\"reasonCodesSelectId\" id=\"reasonCodesSelectId~VARIABLE\" value= \"~VARIABLE\" ><label for=\"reasonCodesSelectId\" ><ebwbean:message key=\"ConfirmPopupApprove.reasonCodes.reasonCodesSelectId.Select\" /></label>");
		arrTableCols.add(tableColAttr);

		tableColAttr = new TableColAttrObj();
		tableColAttr.setColName("REASONCODEDATE");
		tableColAttr.setComponentType("LabelData");
		tableColAttr.setDataType("Varchar");
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ConfirmPopupApprove.reasonCodes.REASONCODEDATE\" />");
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ConfirmPopupApprove.reasonCodes.REASONCODETIME\" />");
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"TrxnDetails.reasonCodes.REASONCODEVAL\" />");
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"TrxnDetails.reasonCodes.REASONCODESEVERITY\" />");
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
		tableColAttr.setDisplayLabel("<ebwbean:message key=\"ConfirmPopupApprove.reasonCodes.REASONCODEDESC\" />");
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
	public void setFrequencyValue(String frequencyValue) {
		this.frequencyValue = frequencyValue;
	}
	public String getTrxnType() {
		return trxnType;
	}
	public void setTrxnType(String trxnType) {
		this.trxnType = trxnType;
	}
	public String getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}
	public String getTxnBatchRefNumber() {
		return txnBatchRefNumber;
	}
	public void setTxnBatchRefNumber(String txnBatchRefNumber) {
		this.txnBatchRefNumber = txnBatchRefNumber;
	}
	public String getTxnPayPayRefNumber() {
		return txnPayPayRefNumber;
	}
	public void setTxnPayPayRefNumber(String txnPayPayRefNumber) {
		this.txnPayPayRefNumber = txnPayPayRefNumber;
	}
	public boolean isBrErrors() {
		return brErrors;
	}
	public void setBrErrors(boolean brErrors) {
		this.brErrors = brErrors;
	}
	public String getPayStatusCode() {
		return payStatusCode;
	}
	public void setPayStatusCode(String payStatusCode) {
		this.payStatusCode = payStatusCode;
	}
	public String getDont_spawn_flag() {
		return dont_spawn_flag;
	}
	public void setDont_spawn_flag(String dont_spawn_flag) {
		this.dont_spawn_flag = dont_spawn_flag;
	}
	public String getOfac_case_id() {
		return ofac_case_id;
	}
	public void setOfac_case_id(String ofac_case_id) {
		this.ofac_case_id = ofac_case_id;
	}

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.comments=null;
		this.confirmationNoHidden=null;
		this.confirmationStatusHidden=null;
		this.reasonCodesSelectId=null;
		this.REASONCODEDATE=null;
		this.REASONCODETIME=null;
		this.REASONCODEDESC=null;
		this.frequencyValue=null;
		this.businessDate=null;
		this.txnBatchRefNumber=null;
		this.txnPayPayRefNumber=null;
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
		strB.append ("comments=" + comments + "\r\n");
		strB.append ("confirmationNoHidden=" + confirmationNoHidden + "\r\n");
		strB.append ("confirmationStatusHidden=" + confirmationStatusHidden + "\r\n");
		strB.append ("frequencyValue=" + frequencyValue + "\r\n");
		strB.append ("businessDate=" + businessDate + "\r\n");
		strB.append ("txnBatchRefNumber=" + txnBatchRefNumber + "\r\n");
		strB.append ("txnPayPayRefNumber=" + txnPayPayRefNumber + "\r\n");

		return strB.toString();
	}

	/**
	 * Returns Vector object for export/report option.
	 * @return Vector object
	 */
	public Vector getReportInformation(){
		Vector reportInfo = new Vector(); 
		Object[][] objArr = {{"ConfirmPopupApprove", "ScreenName"}};
		reportInfo.addElement(objArr);
		objArr = null;
		Object[][] objArr1 = {{"screentitle", "GROUP"},{"reasonCodes","Table"}  };
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
		Object[][] objArr = {{"ConfirmPopupApprove", "ScreenName"}};
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

	public String getFrequencyValue() {
		return frequencyValue;
	}

}