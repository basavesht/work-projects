/*
 * Created on Thu Apr 16 09:49:11 IST 2009
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
public class CancelTransferConfirmForm extends EbwForm implements java.io.Serializable {

	/**
	 * Form Bean Constructor. 
	 */ 
	public CancelTransferConfirmForm () {
		try {
			setState("CancelTransferConfirm_INIT");

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void populateCollections() throws Exception{
	}

	public void copyCollections(CancelTransferConfirmForm srcForm) {
	}

	// Instance Variables
	private String cancelConfirmationNo = null;
	private boolean isBrErrors=false;

	/**
	 * Set the cancelConfirmationNo String.
	 * @param cancelConfirmationNo
	 */
	public void setCancelConfirmationNo(String cancelConfirmationNo) {
		this.cancelConfirmationNo=cancelConfirmationNo;
	}
	/**
	 * Get the cancelConfirmationNo String.
	 * @return cancelConfirmationNo
	 */
	public String getCancelConfirmationNo() {
		return this.cancelConfirmationNo;
	}

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.cancelConfirmationNo=null;
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
		strB.append ("cancelConfirmationNo=" + cancelConfirmationNo + "\r\n");
		return strB.toString();
	}

	/**
	 * Returns Vector object for export/report option.
	 * @return Vector object
	 */
	public Vector getReportInformation(){
		Vector reportInfo = new Vector(); 
		Object[][] objArr = {{"CancelTransferConfirm", "ScreenName"}};
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
		Object[][] objArr = {{"CancelTransferConfirm", "ScreenName"}};
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

	public boolean isBrErrors() {
		return isBrErrors;
	}

	public void setBrErrors(boolean isBrErrors) {
		this.isBrErrors = isBrErrors;
	}
}