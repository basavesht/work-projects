/*
 * Created on Sat Apr 18 12:35:24 IST 2009
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
public class PendingTransferSearchTO extends EBWTransferObject implements java.io.Serializable 
{
	private static final long serialVersionUID = -215926212836872453L;
	private java.util.Date startDate = null;
	private java.util.Date endDate = null;
	private String confirmationNo = null;
	private String accountNo = null;
	private Double minAmount = null;
	private Double maxAmount = null;
	private String frequency = null;
	private String status = null;
	private String lifeUserId = null;
	private String keyAccountNo = null;
	private String[] keyAcctNumbers = null;

	/**
	 * @param startDate the startDate to set.
	 */
	public void setStartDate(java.util.Date startDate) {
		this.startDate=startDate;
	}
	/**
	 * @return Returns the startDate.
	 */
	public java.util.Date getStartDate() {
		return this.startDate;
	}

	/**
	 * @param endDate the endDate to set.
	 */
	public void setEndDate(java.util.Date endDate) {
		this.endDate=endDate;
	}
	/**
	 * @return Returns the endDate.
	 */
	public java.util.Date getEndDate() {
		return this.endDate;
	}

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
	 * @param accountNo the accountNo to set.
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo=accountNo;
	}
	/**
	 * @return Returns the accountNo.
	 */
	public String getAccountNo() {
		return this.accountNo;
	}

	/**
	 * @param minAmount the minAmount to set.
	 */
	public void setMinAmount(Double minAmount) {
		this.minAmount=minAmount;
	}
	/**
	 * @return Returns the minAmount.
	 */
	public Double getMinAmount() {
		return this.minAmount;
	}

	/**
	 * @param maxAmount the maxAmount to set.
	 */
	public void setMaxAmount(Double maxAmount) {
		this.maxAmount=maxAmount;
	}
	/**
	 * @return Returns the maxAmount.
	 */
	public Double getMaxAmount() {
		return this.maxAmount;
	}

	/**
	 * @param frequency the frequency to set.
	 */
	public void setFrequency(String frequency) {
		this.frequency=frequency;
	}
	/**
	 * @return Returns the frequency.
	 */
	public String getFrequency() {
		return this.frequency;
	}

	/**
	 * @param status the status to set.
	 */
	public void setStatus(String status) {
		this.status=status;
	}
	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * @param lifeUserId the lifeUserId to set.
	 */
	public void setLifeUserId(String lifeUserId) {
		this.lifeUserId=lifeUserId;
	}
	/**
	 * @return Returns the lifeUserId.
	 */
	public String getLifeUserId() {
		return this.lifeUserId;
	}

	/**
	 * @param keyAccountNo the keyAccountNo to set.
	 */
	public void setKeyAccountNo(String keyAccountNo) {
		this.keyAccountNo=keyAccountNo;
	}
	/**
	 * @return Returns the keyAccountNo.
	 */
	public String getKeyAccountNo() {
		return this.keyAccountNo;
	}	

	public String[] getKeyAcctNumbers() {
		return keyAcctNumbers;
	}
	
	public void setKeyAcctNumbers(String[] keyAcctNumbers) {
		this.keyAcctNumbers = keyAcctNumbers;
	}
	/**
	 * Returns TransferObject data as a String.
	 *
	 */
	public String toString() {
		StringBuffer strB = new StringBuffer();
		strB.append ("=====================================================\r\n");
		strB.append ("Data for PendingTransferSearchTO \r\n");
		strB.append ("startDate = " + startDate + "\r\n");
		strB.append ("endDate = " + endDate + "\r\n");
		strB.append ("confirmationNo = " + confirmationNo + "\r\n");
		strB.append ("accountNo = " + accountNo + "\r\n");
		strB.append ("minAmount = " + minAmount + "\r\n");
		strB.append ("maxAmount = " + maxAmount + "\r\n");
		strB.append ("frequency = " + frequency + "\r\n");
		strB.append ("status = " + status + "\r\n");
		strB.append ("lifeUserId = " + lifeUserId + "\r\n");
		strB.append ("keyAccountNo = " + keyAccountNo + "\r\n");
		return strB.toString();
	}
}