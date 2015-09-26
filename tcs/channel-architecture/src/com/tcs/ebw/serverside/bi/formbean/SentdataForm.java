/*
 * Created on Wed Jul 12 15:41:42 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.serverside.bi.formbean;

import org.apache.struts.action.ActionMapping;

import com.tcs.ebw.mvc.validator.EbwForm;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 * Sentdata.jsp is using this FormBean. 
 */
public class SentdataForm extends EbwForm implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Instance Variables
	private String det = null;
	private Object biOutput=null;
	//added for bulkimport statistics
	private String fileRefNo=null;
	private int totalRec=0;
	private int currRec=0;
	private String status[];
	private String accId[];
	private String remarks[];
	//end
	/**
	 * Set the det String.
	 * @param det
	 */
	//ADDED
	public String[] getStatus()
	{
		return this.status;
	}
	public void setStatus(String[] status)
	{
		this.status=status;
	}
	public String[] getAccId()
	{
		return this.accId;
	}
	public void setAccId(String[] accId)
	{
		this.accId=accId;
	}

	public String[] getRemarks()
	{
		return this.remarks;
	}
	public void setRemarks(String[] remarks)
	{
		this.remarks=remarks;
	}


	public void setTotalRec(int totalRec) {
		this.totalRec=totalRec;
	}
	/**
	 * Get the det String.
	 * @return det
	 */
	public int getTotalRec() {
		return this.totalRec;
	}
	
	public void setCurrRec(int currRec) {
		this.currRec=currRec;
	}
	public int getCurrRec() {
		return this.currRec;
	}
	//END
	public void setDet(String det) {
		this.det=det;
	}
	public String getDet() {
		return this.det;
	}
	
	public String getfileRefNo() {
		return this.fileRefNo;
	}
	public void setfileRefNo(String fileRefNo) {
		this.fileRefNo=fileRefNo;
	}
	/**
	 * Get the fileref String.
	 * @return fileref
	 */

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.det=null;
		this.fileRefNo=null;
	}

	/**
	 * Returns All Form data as a String.
	 * @return String
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("=====================================================\r\n");
		strB.append ("Data for SentdataForm \r\n");
		strB.append ("action = " + getAction() + "\r\n");
		strB.append ("det = " + det + "\r\n");
		strB.append ("fileRefNo = " + fileRefNo+ "\r\n");
		return strB.toString();
	}

	/**
	 * Returns Vector object for export/report option.
	 * @return Vector object
	 */
	public Vector getReportInformation(){
		Vector reportInfo = new Vector(); 
		Object[][] objArr = {{"Sentdata", "ScreenName"}};
		reportInfo.addElement(objArr);
		objArr = null;
		return reportInfo;
	}
    public Object getBiOutput() {
        return biOutput;
    }
    public void setBiOutput(Object biOutput) {
        this.biOutput = biOutput;
    }
}