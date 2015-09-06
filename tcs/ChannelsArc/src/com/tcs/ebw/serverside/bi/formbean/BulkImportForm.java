/*
 * Created on Wed Jul 12 15:41:42 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.serverside.bi.formbean;

import org.apache.struts.action.ActionMapping;

import com.tcs.ebw.codegen.beans.ComboboxData;
import com.tcs.ebw.mvc.validator.EbwForm;

import org.apache.struts.upload.FormFile;

import java.util.Collection;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 * BulkImport.jsp is using this FormBean. 
 */
public class BulkImportForm extends EbwForm implements java.io.Serializable {

	// Instance Variables
	private String dtfield = null;
	private String filename = null;
	private String version = null;
	private String impfiletypes=null;
	private Collection impfiletypesCollection;
	private String description = null;
	public FormFile impfile;
	private String realFilePath=null;

	/**
	 * Set the dtfield String.
	 * @param dtfield
	 */
	public void setDtfield(String dtfield) {
		this.dtfield=dtfield;
	}
	/**
	 * Get the dtfield String.
	 * @return dtfield
	 */
	public String getDtfield() {
		return this.dtfield;
	}

	/**
	 * Set the filename String.
	 * @param filename
	 */
	public void setFilename(String filename) {
		this.filename=filename;
	}
	/**
	 * Get the filename String.
	 * @return filename
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * Set the version String.
	 * @param version
	 */
	public void setVersion(String version) {
		this.version=version;
	}
	/**
	 * Get the version String.
	 * @return version
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * Set FormFile for the impfile 
	 * @param impfile
	 */
	public void setImpfile(FormFile impfile) {
		this.impfile=impfile;
	}
	/**
	 * Get impfile FormFile Object
	 * @return impfile
	 */
	public FormFile getImpfile() {
		return this.impfile;
	}

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.dtfield=null;
		this.filename=null;
		this.version=null;
		this.impfile=null;
		this.impfiletypes = null;
	}

	/**
	 * Returns All Form data as a String.
	 * @return String
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("=====================================================\r\n");
		strB.append ("Data for BulkImportForm \r\n");
		strB.append ("action = " + getAction() + "\r\n");
		strB.append ("dtfield = " + dtfield + "\r\n");
		strB.append ("filename = " + filename + "\r\n");
		strB.append ("version = " + version + "\r\n");
		return strB.toString();
	}

	/**
	 * Returns Vector object for export/report option.
	 * @return Vector object
	 */
	public Vector getReportInformation(){
		Vector reportInfo = new Vector(); 
		Object[][] objArr = {{"BulkImport", "ScreenName"}};
		reportInfo.addElement(objArr);
		objArr = null;
		return reportInfo;
	}
    public Collection getImpfiletypesCollection() throws Exception{
            ComboboxData comboboxdata = new ComboboxData();
            impfiletypesCollection = comboboxdata.getComboBoxData("getImpfiletypesInfo", "", "","");
        return impfiletypesCollection;
    }
    public void setImpfiletypesCollection(Collection impfiletypesCollection) {
        this.impfiletypesCollection = impfiletypesCollection;
    }
    public String getImpfiletypes() {
        return impfiletypes;
    }
    public void setImpfiletypes(String impfiletypes) {
        this.impfiletypes = impfiletypes;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getRealFilePath() {
        return realFilePath;
    }
    public void setRealFilePath(String realFilePath) {
        this.realFilePath = realFilePath;
    }
}