/*
 * Created on Fri Jun 02 09:38:10 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.common.formbean;

import org.apache.struts.action.ActionMapping;

import com.tcs.ebw.mvc.validator.EbwForm;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 * TransactionPwd.jsp is using this FormBean. 
 */
public class TransactionPwdForm extends EbwForm implements java.io.Serializable {

	// Instance Variables
	private String txnPwd = null;
	private String forwardTo = null;
	private String forwardFrom = null;


	private String delegateMethod= null;
	private String delegatePackage = null;

	/**
	 * Set the txnPwd String.
	 * @param txnPwd
	 */
	public void setTxnPwd(String txnPwd) {
		this.txnPwd=txnPwd;
	}
	/**
	 * Get the txnPwd String.
	 * @return txnPwd
	 */
	public String getTxnPwd() {
		return this.txnPwd;
	}



	/**
	 * Set the delegateMethod String.
	 * @param delegateMethod
	 */
	public void setDelegateMethod(String delegateMethod) {
		this.delegateMethod=delegateMethod;
	}
	/**
	 * Get the delegateMethod String.
	 * @return delegateMethod
	 */
	public String getDelegateMethod() {
		return this.delegateMethod;
	}

	/**
	 * Set the delegatePackage String.
	 * @param delegatePackage
	 */
	public void setDelegatePackage(String delegatePackage) {
		this.delegatePackage=delegatePackage;
	}
	/**
	 * Get the delegatePackage String.
	 * @return delegatePackage
	 */
	public String getDelegatePackage() {
		return this.delegatePackage;
	}

	/**
	 * Reset all properties to their default values.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.txnPwd=null;
	}

	/**
	 * Returns All Form data as a String.
	 * @return String
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("=====================================================\r\n");
		strB.append ("Data for TransactionPwdForm \r\n");
		strB.append ("action = " + getAction() + "\r\n");
		strB.append ("txnPwd = " + txnPwd + "\r\n");
		return strB.toString();
	}

	/**
	 * Returns Vector object for export/report option.
	 * @return Vector object
	 */
	public Vector getReportInformation(){
		Vector reportInfo = new Vector(); 
		Object[][] objArr = {{"TransactionPwd", "ScreenName"}};
		reportInfo.addElement(objArr);
		objArr = null;
		return reportInfo;
	}

	 /**
     * @return Returns the forwardFrom.
     */
    public String getForwardFrom() {

        return forwardFrom;
    }
    /**
     * @param forwardTo The forwardFrom to set.
     */
    public void setForwardFrom(String forwardFrom) {
		
       this.forwardFrom = forwardFrom;
    }


    /**
     * @return Returns the forwardTo.
     */
    public String getForwardTo() {
        return forwardTo;
    }
    
	/**
     * @param forwardTo The forwardTo to set.
     */
    public void setForwardTo(String forwardTo) {
        this.forwardTo = forwardTo;
    }


	
}