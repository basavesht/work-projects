/*
 * Created on Wed May 26 13:58:46 IST 2010
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.transferobject;

import java.math.BigDecimal;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ConfirmPrintSuccessOutputTO extends EBWTransferObject implements java.io.Serializable {

	private static final long serialVersionUID = -1073164667344542294L;
	private String confirmationNo = null;
	private String fromAccnt = null;
	private String payee = null;
	private java.util.Date reqPrntDate = null;
	private BigDecimal amount = null;
	private String deliveryOption = null;
	private String certifiedChk = null;
	private String loa = null;
	private String printingBranch = null;
	private String checkNo = null;
	private String memo1 = null;
	private String memo2 = null;
	private String addrline1 = null;
	private String addrline2 = null;
	private String addrline3 = null;
	private String addrline4 = null;
	private String thirdparty = null;
	private Double versionnum = null;
	private String txnBatchId = null;
	private String sameNameFlag = null;
	private Double pickUpOption = null;	
	private Double batversionnum = null;	

	public Double getBatversionnum() {
		return batversionnum;
	}
	public void setBatversionnum(Double batversionnum) {
		this.batversionnum = batversionnum;
	}
	public Double getPickUpOption() {
		return pickUpOption;
	}
	public void setPickUpOption(Double pickUpOption) {
		this.pickUpOption = pickUpOption;
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
	 * @param fromAccnt the fromAccnt to set.
	 */
	public void setFromAccnt(String fromAccnt) {
		this.fromAccnt=fromAccnt;
	}
	/**
	 * @return Returns the fromAccnt.
	 */
	public String getFromAccnt() {
		return this.fromAccnt;
	}

	/**
	 * @param payee the payee to set.
	 */
	public void setPayee(String payee) {
		this.payee=payee;
	}
	/**
	 * @return Returns the payee.
	 */
	public String getPayee() {
		return this.payee;
	}

	/**
	 * @param reqPrntDate the reqPrntDate to set.
	 */
	public void setReqPrntDate(java.util.Date reqPrntDate) {
		this.reqPrntDate=reqPrntDate;
	}
	/**
	 * @return Returns the reqPrntDate.
	 */
	public java.util.Date getReqPrntDate() {
		return this.reqPrntDate;
	}



	/**
	 * @param deliveryOption the deliveryOption to set.
	 */
	public void setDeliveryOption(String deliveryOption) {
		this.deliveryOption=deliveryOption;
	}
	/**
	 * @return Returns the deliveryOption.
	 */
	public String getDeliveryOption() {
		return this.deliveryOption;
	}

	/**
	 * @param certifiedChk the certifiedChk to set.
	 */
	public void setCertifiedChk(String certifiedChk) {
		this.certifiedChk=certifiedChk;
	}
	/**
	 * @return Returns the certifiedChk.
	 */
	public String getCertifiedChk() {
		return this.certifiedChk;
	}

	/**
	 * @param loa the loa to set.
	 */
	public void setLoa(String loa) {
		this.loa=loa;
	}
	/**
	 * @return Returns the loa.
	 */
	public String getLoa() {
		return this.loa;
	}





	/**
	 * @return the versionnum
	 */
	public Double getVersionnum() {
		return versionnum;
	}
	/**
	 * @param versionnum the versionnum to set
	 */
	public void setVersionnum(Double versionnum) {
		this.versionnum = versionnum;
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
	 * @return the memo1
	 */
	public String getMemo1() {
		return memo1;
	}
	/**
	 * @param memo1 the memo1 to set
	 */
	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}
	/**
	 * @return the memo2
	 */
	public String getMemo2() {
		return memo2;
	}
	/**
	 * @param memo2 the memo2 to set
	 */
	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}
	/**
	 * @return the addrline1
	 */
	public String getAddrline1() {
		return addrline1;
	}
	/**
	 * @param addrline1 the addrline1 to set
	 */
	public void setAddrline1(String addrline1) {
		this.addrline1 = addrline1;
	}
	/**
	 * @return the addrline2
	 */
	public String getAddrline2() {
		return addrline2;
	}
	/**
	 * @param addrline2 the addrline2 to set
	 */
	public void setAddrline2(String addrline2) {
		this.addrline2 = addrline2;
	}
	/**
	 * @return the addrline3
	 */
	public String getAddrline3() {
		return addrline3;
	}
	/**
	 * @param addrline3 the addrline3 to set
	 */
	public void setAddrline3(String addrline3) {
		this.addrline3 = addrline3;
	}
	/**
	 * @return the addrline4
	 */
	public String getAddrline4() {
		return addrline4;
	}
	/**
	 * @param addrline4 the addrline4 to set
	 */
	public void setAddrline4(String addrline4) {
		this.addrline4 = addrline4;
	}
	/**
	 * @return the thirdparty
	 */
	public String getThirdparty() {
		return thirdparty;
	}
	/**
	 * @param thirdparty the thirdparty to set
	 */
	public void setThirdparty(String thirdparty) {
		this.thirdparty = thirdparty;
	}
	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * @return the txnBatchId
	 */
	public String getTxnBatchId() {
		return txnBatchId;
	}
	/**
	 * @param txnBatchId the txnBatchId to set
	 */
	public void setTxnBatchId(String txnBatchId) {
		this.txnBatchId = txnBatchId;
	}
	public String getPrintingBranch() {
		return printingBranch;
	}
	public void setPrintingBranch(String printingBranch) {
		this.printingBranch = printingBranch;
	}
	public String getSameNameFlag() {
		return sameNameFlag;
	}
	public void setSameNameFlag(String sameNameFlag) {
		this.sameNameFlag = sameNameFlag;
	}
	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
		final String TAB = "    ";

		String retValue = "";

		retValue = "ConfirmPrintSuccessOutputTO ( "
			+ super.toString() + TAB
			+ "confirmationNo = " + this.confirmationNo + TAB
			+ "fromAccnt = " + this.fromAccnt + TAB
			+ "payee = " + this.payee + TAB
			+ "reqPrntDate = " + this.reqPrntDate + TAB
			+ "amount = " + this.amount + TAB
			+ "deliveryOption = " + this.deliveryOption + TAB
			+ "certifiedChk = " + this.certifiedChk + TAB
			+ "loa = " + this.loa + TAB
			+ "printingBranch = " + this.printingBranch + TAB
			+ "checkNo = " + this.checkNo + TAB
			+ "memo1 = " + this.memo1 + TAB
			+ "memo2 = " + this.memo2 + TAB
			+ "addrline1 = " + this.addrline1 + TAB
			+ "addrline2 = " + this.addrline2 + TAB
			+ "addrline3 = " + this.addrline3 + TAB
			+ "addrline4 = " + this.addrline4 + TAB
			+ "thirdparty = " + this.thirdparty + TAB
			+ "versionnum = " + this.versionnum + TAB
			+ "txnBatchId = " + this.txnBatchId + TAB
			+ "sameNameFlag = " + this.sameNameFlag + TAB
			+ "pickUpOption = " + this.pickUpOption + TAB
			+ "batversionnum = " + this.batversionnum + TAB
			+ " )";

		return retValue;
	}


}