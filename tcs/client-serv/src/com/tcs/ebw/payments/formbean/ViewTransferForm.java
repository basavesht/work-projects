/*
 * Created on Fri Apr 10 11:21:25 IST 2009
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
public class ViewTransferForm extends EbwForm implements java.io.Serializable {

	/**
	 * Form Bean Constructor. 
	 */ 
	public ViewTransferForm () {
		try {
			setState("ViewTransfer_INIT");

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void populateCollections() throws Exception{
	}

	public void copyCollections(ViewTransferForm srcForm) {
	}

	// Instance Variables
	private String viewTransFromAcc = null;
	private String viewTransToAcc = null;
	private String viewTransAmount = null;
	private String viewTransInitDate = null;
	private String viewTransLastTransDate = null;
	private String viewTransConfNo = null;
	private String viewTransStatus = null;
	private String viewTransFrequency = null;
	private String viewTransRepeat = null;
	private String viewTransDollarsTransfered = null;
	private String viewTransNoOfTransfers = null;
	private String transferType = null;
	private String durationEndDate = null;
	private String durationNoOfTransfers = null;
	private String durationValue = null;
	private String frequencyCombo = null;
	private String txnPayPayRefNumber = null;
	private String frequencyValue = null;
	private String txnBatchRefNumber=null;
	/**
	 * Set the viewTransFromAcc String.
	 * @param viewTransFromAcc
	 */
	public void setViewTransFromAcc(String viewTransFromAcc) {
		this.viewTransFromAcc=viewTransFromAcc;
	}
	/**
	 * Get the viewTransFromAcc String.
	 * @return viewTransFromAcc
	 */
	public String getViewTransFromAcc() {
		return this.viewTransFromAcc;
	}

	/**
	 * Set the viewTransToAcc String.
	 * @param viewTransToAcc
	 */
	public void setViewTransToAcc(String viewTransToAcc) {
		this.viewTransToAcc=viewTransToAcc;
	}
	/**
	 * Get the viewTransToAcc String.
	 * @return viewTransToAcc
	 */
	public String getViewTransToAcc() {
		return this.viewTransToAcc;
	}

	/**
	 * Set the viewTransAmount String.
	 * @param viewTransAmount
	 */
	public void setViewTransAmount(String viewTransAmount) {
		this.viewTransAmount=viewTransAmount;
	}
	/**
	 * Get the viewTransAmount String.
	 * @return viewTransAmount
	 */
	public String getViewTransAmount() {
		return this.viewTransAmount;
	}

	/**
	 * Set the viewTransInitDate String.
	 * @param viewTransInitDate
	 */
	public void setViewTransInitDate(String viewTransInitDate) {
		this.viewTransInitDate=viewTransInitDate;
	}
	/**
	 * Get the viewTransInitDate String.
	 * @return viewTransInitDate
	 */
	public String getViewTransInitDate() {
		return this.viewTransInitDate;
	}

	/**
	 * Set the viewTransLastTransDate String.
	 * @param viewTransLastTransDate
	 */
	public void setViewTransLastTransDate(String viewTransLastTransDate) {
		this.viewTransLastTransDate=viewTransLastTransDate;
	}
	/**
	 * Get the viewTransLastTransDate String.
	 * @return viewTransLastTransDate
	 */
	public String getViewTransLastTransDate() {
		return this.viewTransLastTransDate;
	}

	/**
	 * Set the viewTransConfNo String.
	 * @param viewTransConfNo
	 */
	public void setViewTransConfNo(String viewTransConfNo) {
		this.viewTransConfNo=viewTransConfNo;
	}
	/**
	 * Get the viewTransConfNo String.
	 * @return viewTransConfNo
	 */
	public String getViewTransConfNo() {
		return this.viewTransConfNo;
	}

	/**
	 * Set the viewTransStatus String.
	 * @param viewTransStatus
	 */
	public void setViewTransStatus(String viewTransStatus) {
		this.viewTransStatus=viewTransStatus;
	}
	/**
	 * Get the viewTransStatus String.
	 * @return viewTransStatus
	 */
	public String getViewTransStatus() {
		return this.viewTransStatus;
	}

	/**
	 * Set the viewTransFrequency String.
	 * @param viewTransFrequency
	 */
	public void setViewTransFrequency(String viewTransFrequency) {
		this.viewTransFrequency=viewTransFrequency;
	}
	/**
	 * Get the viewTransFrequency String.
	 * @return viewTransFrequency
	 */
	public String getViewTransFrequency() {
		return this.viewTransFrequency;
	}

	/**
	 * Set the viewTransRepeat String.
	 * @param viewTransRepeat
	 */
	public void setViewTransRepeat(String viewTransRepeat) {
		this.viewTransRepeat=viewTransRepeat;
	}
	/**
	 * Get the viewTransRepeat String.
	 * @return viewTransRepeat
	 */
	public String getViewTransRepeat() {
		return this.viewTransRepeat;
	}

	/**
	 * Set the viewTransDollarsTransfered String.
	 * @param viewTransDollarsTransfered
	 */
	public void setViewTransDollarsTransfered(String viewTransDollarsTransfered) {
		this.viewTransDollarsTransfered=viewTransDollarsTransfered;
	}
	/**
	 * Get the viewTransDollarsTransfered String.
	 * @return viewTransDollarsTransfered
	 */
	public String getViewTransDollarsTransfered() {
		return this.viewTransDollarsTransfered;
	}

	/**
	 * Set the viewTransNoOfTransfers String.
	 * @param viewTransNoOfTransfers
	 */
	public void setViewTransNoOfTransfers(String viewTransNoOfTransfers) {
		this.viewTransNoOfTransfers=viewTransNoOfTransfers;
	}
	/**
	 * Get the viewTransNoOfTransfers String.
	 * @return viewTransNoOfTransfers
	 */
	public String getViewTransNoOfTransfers() {
		return this.viewTransNoOfTransfers;
	}

	/**
	 * Set the transferType String.
	 * @param transferType
	 */
	public void setTransferType(String transferType) {
		this.transferType=transferType;
	}
	/**
	 * Get the transferType String.
	 * @return transferType
	 */
	public String getTransferType() {
		return this.transferType;
	}

	/**
	 * Set the durationEndDate String.
	 * @param durationEndDate
	 */
	public void setDurationEndDate(String durationEndDate) {
		this.durationEndDate=durationEndDate;
	}
	/**
	 * Get the durationEndDate String.
	 * @return durationEndDate
	 */
	public String getDurationEndDate() {
		return this.durationEndDate;
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
	 * Set the frequencyCombo String.
	 * @param frequencyCombo
	 */
	public void setFrequencyCombo(String frequencyCombo) {
		this.frequencyCombo=frequencyCombo;
	}
	/**
	 * Get the frequencyCombo String.
	 * @return frequencyCombo
	 */
	public String getFrequencyCombo() {
		return this.frequencyCombo;
	}

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.viewTransFromAcc=null;
		this.viewTransToAcc=null;
		this.viewTransAmount=null;
		this.viewTransInitDate=null;
		this.viewTransLastTransDate=null;
		this.viewTransConfNo=null;
		this.viewTransStatus=null;
		this.viewTransFrequency=null;
		this.viewTransRepeat=null;
		this.viewTransDollarsTransfered=null;
		this.viewTransNoOfTransfers=null;
		this.transferType=null;
		this.durationEndDate=null;
		this.durationNoOfTransfers=null;
		this.durationValue=null;
		this.frequencyCombo=null;
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
		strB.append ("viewTransFromAcc=" + viewTransFromAcc + "\r\n");
		strB.append ("viewTransToAcc=" + viewTransToAcc + "\r\n");
		strB.append ("viewTransAmount=" + viewTransAmount + "\r\n");
		strB.append ("viewTransInitDate=" + viewTransInitDate + "\r\n");
		strB.append ("viewTransLastTransDate=" + viewTransLastTransDate + "\r\n");
		strB.append ("viewTransConfNo=" + viewTransConfNo + "\r\n");
		strB.append ("viewTransStatus=" + viewTransStatus + "\r\n");
		strB.append ("viewTransFrequency=" + viewTransFrequency + "\r\n");
		strB.append ("viewTransRepeat=" + viewTransRepeat + "\r\n");
		strB.append ("viewTransDollarsTransfered=" + viewTransDollarsTransfered + "\r\n");
		strB.append ("viewTransNoOfTransfers=" + viewTransNoOfTransfers + "\r\n");
		strB.append ("transferType=" + transferType + "\r\n");
		strB.append ("durationEndDate=" + durationEndDate + "\r\n");
		strB.append ("durationNoOfTransfers=" + durationNoOfTransfers + "\r\n");
		strB.append ("durationValue=" + durationValue + "\r\n");
		strB.append ("frequencyCombo=" + frequencyCombo + "\r\n");
		return strB.toString();
	}

	/**
	 * Returns Vector object for export/report option.
	 * @return Vector object
	 */
	public Vector getReportInformation(){
		Vector reportInfo = new Vector(); 
		Object[][] objArr = {{"ViewTransfer", "ScreenName"}};
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
		Object[][] objArr = {{"ViewTransfer", "ScreenName"}};
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

	public String getTxnPayPayRefNumber() {
		return txnPayPayRefNumber;
	}

	public void setTxnPayPayRefNumber(String txnPayPayRefNumber) {
		this.txnPayPayRefNumber = txnPayPayRefNumber;
	}

	public String getFrequencyValue() {
		return frequencyValue;
	}

	public void setFrequencyValue(String frequencyValue) {
		this.frequencyValue = frequencyValue;
	}

	public String getTxnBatchRefNumber() {
		return txnBatchRefNumber;
	}

	public void setTxnBatchRefNumber(String txnBatchRefNumber) {
		this.txnBatchRefNumber = txnBatchRefNumber;
	}
}