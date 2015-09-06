/*
 * Created on Tue Jul 28 15:23:46 IST 2009
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
public class ExtAccsInformationForm extends EbwForm implements java.io.Serializable {

	/**
	 * Form Bean Constructor. 
	 */ 
	public ExtAccsInformationForm () {
		try {
			setState("ExtAccsInformation_INIT");

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void populateCollections() throws Exception{
	}

	public void copyCollections(ExtAccsInformationForm srcForm) {
	}

	// Instance Variables
	private String externalAccStatusKey = null;
	private String brErrorMessages = null;

	/**
	 * Set the brErrorMessages String.
	 * @param brErrorMessages
	 */
	public void setBrErrorMessages(String brErrorMessages) {
		this.brErrorMessages=brErrorMessages;
	}
	/**
	 * Get the brErrorMessages String.
	 * @return brErrorMessages
	 */
	public String getBrErrorMessages() {
		return this.brErrorMessages;
	}

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.brErrorMessages=null;
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
		strB.append ("brErrorMessages=" + brErrorMessages + "\r\n");
		return strB.toString();
	}

	/**
	 * Returns Vector object for export/report option.
	 * @return Vector object
	 */
	public Vector getReportInformation(){
		Vector reportInfo = new Vector(); 
		Object[][] objArr = {{"ExtAccsInformation", "ScreenName"}};
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
		Object[][] objArr = {{"ExtAccsInformation", "ScreenName"}};
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

	public String getExternalAccStatusKey() {
		return externalAccStatusKey;
	}

	public void setExternalAccStatusKey(String externalAccStatusKey) {
		this.externalAccStatusKey = externalAccStatusKey;
	}

}