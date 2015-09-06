/*
 * Created on Wed May 26 13:58:46 IST 2010
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ConfirmPrintSuccessInputTO extends EBWTransferObject implements java.io.Serializable {

	private static final long serialVersionUID = 7491064417109708319L;
	private String BATBATCHREF = null;
	private String confirmationNo = null;
	private String newStatus = null;
	private String checkNo = null;
	private String voidReason = null;



	/**
	 * @param confirmationNo the confirmationNo to set.
	 */
	public void setConfirmationNo(String confirmationNo) {
		this.confirmationNo=confirmationNo;
	}
	/**
	 * @return Returns the confirmationNo.
	 */
	public String getConfirmationNo() {
		return this.confirmationNo;
	}


	/**
	 * Returns TransferObject data as a String.
	 *
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("=====================================================\r\n");
		strB.append ("Data for ConfirmPrintSuccessInputTO \r\n");
		strB.append ("BATBATCHREF = " + BATBATCHREF + "\r\n");
		strB.append ("confirmationNo = " + confirmationNo + "\r\n");
		strB.append ("newStatus = " + newStatus + "\r\n");
		strB.append ("checkNo = " + checkNo + "\r\n");
		strB.append ("voidReason = " + voidReason + "\r\n");

		return strB.toString();
	}
	/**
	 * @return the bATBATCHREF
	 */
	public String getBATBATCHREF() {
		return BATBATCHREF;
	}
	/**
	 * @param batbatchref the bATBATCHREF to set
	 */
	public void setBATBATCHREF(String batbatchref) {
		BATBATCHREF = batbatchref;
	}
	/**
	 * @return the newStatus
	 */
	public String getNewStatus() {
		return newStatus;
	}
	/**
	 * @param newStatus the newStatus to set
	 */
	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}
	/**
	 * @return the checkNo
	 */
	public String getCheckNo() {
		return checkNo;
	}
	/**
	 * @param checkNo the checkNo to set
	 */
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	/**
	 * @return the voidReason
	 */
	public String getVoidReason() {
		return voidReason;
	}
	/**
	 * @param voidReason the voidReason to set
	 */
	public void setVoidReason(String voidReason) {
		this.voidReason = voidReason;
	}
}