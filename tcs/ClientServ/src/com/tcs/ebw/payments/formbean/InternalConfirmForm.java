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
public class InternalConfirmForm extends EbwForm implements java.io.Serializable {

	/**
	 * Form Bean Constructor. 
	 */ 
	public InternalConfirmForm () {
		try {
			setState("InternalConfirm_INIT");

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void populateCollections() throws Exception{
	}

	public void copyCollections(InternalConfirmForm srcForm) {
	}

	// Instance Variables
	private String confirmNo = null;
	private String frmacc = null;
	private String toacc = null;
	private String payamnt = null;
	private String date = null;
	private String status = null;
	private String frequency = null;
	private String duration = null;
	private String businessRuleMessages = null;
	private boolean isBrErrors=false;
	private String frequencyValue = null;
	
	/**
	 * Set the confirmNo String.
	 * @param confirmNo
	 */
	public void setConfirmNo(String confirmNo) {
		this.confirmNo=confirmNo;
	}
	/**
	 * Get the confirmNo String.
	 * @return confirmNo
	 */
	public String getConfirmNo() {
		return this.confirmNo;
	}

	/**
	 * Set the frmacc String.
	 * @param frmacc
	 */
	public void setFrmacc(String frmacc) {
		this.frmacc=frmacc;
	}
	/**
	 * Get the frmacc String.
	 * @return frmacc
	 */
	public String getFrmacc() {
		return this.frmacc;
	}

	/**
	 * Set the toacc String.
	 * @param toacc
	 */
	public void setToacc(String toacc) {
		this.toacc=toacc;
	}
	/**
	 * Get the toacc String.
	 * @return toacc
	 */
	public String getToacc() {
		return this.toacc;
	}

	/**
	 * Set the payamnt String.
	 * @param payamnt
	 */
	public void setPayamnt(String payamnt) {
		this.payamnt=payamnt;
	}
	/**
	 * Get the payamnt String.
	 * @return payamnt
	 */
	public String getPayamnt() {
		return this.payamnt;
	}

	/**
	 * Set the date String.
	 * @param date
	 */
	public void setDate(String date) {
		this.date=date;
	}
	/**
	 * Get the date String.
	 * @return date
	 */
	public String getDate() {
		return this.date;
	}
   /**
	 * Set the frequency String.
	 * @param frequency
	 */
	public void setFrequency(String frequency) {
		this.frequency=frequency;
	}
	/**
	 * Get the frequency String.
	 * @return frequency
	 */
	public String getFrequency() {
		return this.frequency;
	}

	/**
	 * Set the duration String.
	 * @param duration
	 */
	public void setDuration(String duration) {
		this.duration=duration;
	}
	/**
	 * Get the duration String.
	 * @return duration
	 */
	public String getDuration() {
		return this.duration;
	}

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.confirmNo=null;
		this.frmacc=null;
		this.toacc=null;
		this.payamnt=null;
		this.date=null;
		this.status=null;
		this.frequency=null;
		this.duration=null;
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
		strB.append ("confirmNo=" + confirmNo + "\r\n");
		strB.append ("frmacc=" + frmacc + "\r\n");
		strB.append ("toacc=" + toacc + "\r\n");
		strB.append ("payamnt=" + payamnt + "\r\n");
		strB.append ("date=" + date + "\r\n");
		strB.append ("estArrdate=" + status + "\r\n");
		strB.append ("frequency=" + frequency + "\r\n");
		strB.append ("duration=" + duration + "\r\n");
		return strB.toString();
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	/**
	 * @return the frequencyValue
	 */
	public String getFrequencyValue() {
		return frequencyValue;
	}

	/**
	 * @param frequencyValue the frequencyValue to set
	 */
	public void setFrequencyValue(String frequencyValue) {
		this.frequencyValue = frequencyValue;
	}
}