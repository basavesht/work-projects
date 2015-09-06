/*
 * Created on Fri Nov 28 15:31:15 IST 2008
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.common.formbean;

import org.apache.struts.action.ActionMapping;

import com.tcs.ebw.mvc.validator.EbwForm;

import java.util.HashMap;

import java.util.ArrayList;

import java.util.Collection;
import com.tcs.ebw.codegen.beans.ComboboxData;
import com.tcs.ebw.exception.EbwException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 * TemplateList.jsp is using this FormBean. 
 */
public class TemplateListForm extends EbwForm implements java.io.Serializable {

	/**
	 * Form Bean Constructor. 
	 */ 
	public TemplateListForm () {
		try {
			setState("TemplateList_INIT");

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void populateCollections() throws Exception{
		if(this.templateNamesCollection==null)
			FetchTemplateNamesCollection();
	}

	public void copyCollections(TemplateListForm srcForm) {
		this.templateNamesCollection=srcForm.getTemplateNamesCollection();
	}

	// Instance Variables
	private String templateNames = null;
	private Collection templateNamesCollection;
	private Vector vectemplateNamesDescJS;
	private String usruserid = null;
	private String comboval = null;

	/**
	 * Set the templateNames String.
	 * @param templateNames
	 */
	public void setTemplateNames(String templateNames) {
		this.templateNames=templateNames;
	}
	/**
	 * Get the templateNames String.
	 * @return templateNames
	 */
	public String getTemplateNames() {
		return this.templateNames;
	}

	/**
	 * Set collection for the templateNames combobox.
	 * @param templateNamesCollection
	 */
	public void setTemplateNamesCollection(Collection templateNamesCollection) {
		this.templateNamesCollection=templateNamesCollection;
	}
	/**
	 * Get collection for the templateNames combobox.
	 * @return templateNamesCollection
	 */
	public Collection getTemplateNamesCollection() {
		return this.templateNamesCollection;
	}

	public void FetchTemplateNamesCollection() throws Exception{
	try{
			if (this.templateNamesCollection == null) {
				ComboboxData cboData = new ComboboxData();
				this.templateNamesCollection = cboData.getComboBoxData("getTemplateNames" , "USERID="+ getUsruserid()+",SCREENNAME="+ getScreenName(),"VAL=1,DISP=1,REF=2,REFFIELD=comboval","");
				this.vectemplateNamesDescJS = (Vector) cboData.getComboBoxDesc();
			}
		}catch (EbwException exc) {
			exc.printStackTrace();
			throw exc;
		}
	}

	public String getTemplateNamesDescJS() {
		String strTemplateNamesDescJS="";
			strTemplateNamesDescJS = getComboDesc("templateNames", vectemplateNamesDescJS).toString();
		return strTemplateNamesDescJS;
	}
	/**
	 * Set the usruserid String.
	 * @param usruserid
	 */
	public void setUsruserid(String usruserid) {
		this.usruserid=usruserid;
	}
	/**
	 * Get the usruserid String.
	 * @return usruserid
	 */
	public String getUsruserid() {
		return this.usruserid;
	}

	/**
	 * Set the comboval String.
	 * @param comboval
	 */
	public void setComboval(String comboval) {
		this.comboval=comboval;
	}
	/**
	 * Get the comboval String.
	 * @return comboval
	 */
	public String getComboval() {
		return this.comboval;
	}

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.templateNames=null;
		this.usruserid=null;
		this.comboval=null;
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
		strB.append ("templateNames=" + templateNames + "\r\n");
		strB.append ("usruserid=" + usruserid + "\r\n");
		strB.append ("comboval=" + comboval + "\r\n");
		return strB.toString();
	}

	/**
	 * Returns Vector object for export/report option.
	 * @return Vector object
	 */
	public Vector getReportInformation(){
		Vector reportInfo = new Vector(); 
		Object[][] objArr = {{"TemplateList", "ScreenName"}};
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
		Object[][] objArr = {{"TemplateList", "ScreenName"}};
		chartInfo.addElement(objArr);
		objArr = null;
		return chartInfo;
	}
	
}