/*
 * Created on Mon Jun 08 21:45:24 IST 2009
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
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ConfirmPopupRejectForm extends EbwForm implements java.io.Serializable {

	/**
	 * Form Bean Constructor. 
	 */ 
	public ConfirmPopupRejectForm () {
		try {
			setState("ConfirmPopupReject_INIT");

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void populateCollections() throws Exception{
	}

	public void copyCollections(ConfirmPopupRejectForm srcForm) {
	}

	// Instance Variables
	private String comments = null;
	private String confirmationNoHidden = null;
	private String confirmationStatusHidden = null;
	private String frequencyValue = null;
	private String paymentstatus = null;
	private String txnBatchRefNumber = null;
	private String txnPayPayRefNumber = null;
	private String businessDate = null;
	private boolean brErrors;
	private String payStatusCode = null;
	private String transferType = null;

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
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.comments=null;
		this.confirmationNoHidden=null;
		this.confirmationStatusHidden=null;
		this.frequencyValue=null;
		this.paymentstatus=null;
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
		strB.append ("paymentstatus=" + paymentstatus + "\r\n");
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
		Object[][] objArr = {{"ConfirmPopupReject", "ScreenName"}};
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
		Object[][] objArr = {{"ConfirmPopupReject", "ScreenName"}};
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

	public String getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
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
}