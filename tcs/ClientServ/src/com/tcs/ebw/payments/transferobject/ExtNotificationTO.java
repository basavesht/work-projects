/*
 * Created on Sun Apr 26 12:30:28 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ExtNotificationTO extends EBWTransferObject implements Serializable {

	private String cpypayeeid = null;
	private String extaccnum = null;
	private String lifeuserid = null;
	
	/**
	 * @param cpypayeeid the cpypayeeid to set.
	 */
	public void setCpypayeeid(String cpypayeeid) {
		this.cpypayeeid=cpypayeeid;
	}
	/**
	 * @return Returns the cpypayeeid.
	 */
	public String getCpypayeeid() {
		return this.cpypayeeid;
	}

	public String getExtaccnum() {
		return extaccnum;
	}
	public void setExtaccnum(String extaccnum) {
		this.extaccnum = extaccnum;
	}
	/**
	 * Returns TransferObject data as a String.
	 *
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("=====================================================\r\n");
		strB.append ("Data for ExtNotificationTO \r\n");
		strB.append ("cpypayeeid = " + cpypayeeid + "\r\n");
		strB.append ("extaccnum = " + extaccnum + "\r\n");
		return strB.toString();
	}
	public String getLifeuserid() {
		return lifeuserid;
	}
	public void setLifeuserid(String lifeuserid) {
		this.lifeuserid = lifeuserid;
	}
}