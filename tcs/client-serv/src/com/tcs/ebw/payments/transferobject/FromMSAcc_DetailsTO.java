package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class FromMSAcc_DetailsTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -327757875915906873L;

	private boolean isAcntViewCallReq = true; //Indicator to bypass account view interface call for the account .. ( E.g Ajax call on account selection )

	private String accountNumber =null;
	private String officeNumber =null;
	private String faNumber =null;
	private String keyAccount =null;
	private String nickName =null;
	private String friendlyName =null;
	private boolean viewTransactionFlag;
	private String status =null;
	private String accountClass =null;
	private String novusSubProduct =null;
	private String divPay =null;
	private String clientCategory =null;
	private String choiceFundCode =null;
	private String iraCode =null;
	private String tradeControl =null;
	private String accountCategory =null;
	private String collateralAcctInd =null;

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	/**
	 * @return the officeNumber
	 */
	public String getOfficeNumber() {
		return officeNumber;
	}
	/**
	 * @param officeNumber the officeNumber to set
	 */
	public void setOfficeNumber(String officeNumber) {
		this.officeNumber = officeNumber;
	}
	/**
	 * @return the faNumber
	 */
	public String getFaNumber() {
		return faNumber;
	}
	/**
	 * @param faNumber the faNumber to set
	 */
	public void setFaNumber(String faNumber) {
		this.faNumber = faNumber;
	}
	/**
	 * @return the keyAccount
	 */
	public String getKeyAccount() {
		return keyAccount;
	}
	/**
	 * @param keyAccount the keyAccount to set
	 */
	public void setKeyAccount(String keyAccount) {
		this.keyAccount = keyAccount;
	}
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * @return the friendlyName
	 */
	public String getFriendlyName() {
		return friendlyName;
	}
	/**
	 * @param friendlyName the friendlyName to set
	 */
	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	/**
	 * @return the viewTransactionFlag
	 */
	public boolean isViewTransactionFlag() {
		return viewTransactionFlag;
	}
	/**
	 * @param viewTransactionFlag the viewTransactionFlag to set
	 */
	public void setViewTransactionFlag(boolean viewTransactionFlag) {
		this.viewTransactionFlag = viewTransactionFlag;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the accountClass
	 */
	public String getAccountClass() {
		return accountClass;
	}
	/**
	 * @param accountClass the accountClass to set
	 */
	public void setAccountClass(String accountClass) {
		this.accountClass = accountClass;
	}
	/**
	 * @return the novusSubProduct
	 */
	public String getNovusSubProduct() {
		return novusSubProduct;
	}
	/**
	 * @param novusSubProduct the novusSubProduct to set
	 */
	public void setNovusSubProduct(String novusSubProduct) {
		this.novusSubProduct = novusSubProduct;
	}
	/**
	 * @return the divPay
	 */
	public String getDivPay() {
		return divPay;
	}
	/**
	 * @param divPay the divPay to set
	 */
	public void setDivPay(String divPay) {
		this.divPay = divPay;
	}
	/**
	 * @return the clientCategory
	 */
	public String getClientCategory() {
		return clientCategory;
	}
	/**
	 * @param clientCategory the clientCategory to set
	 */
	public void setClientCategory(String clientCategory) {
		this.clientCategory = clientCategory;
	}
	/**
	 * @return the choiceFundCode
	 */
	public String getChoiceFundCode() {
		return choiceFundCode;
	}
	/**
	 * @param choiceFundCode the choiceFundCode to set
	 */
	public void setChoiceFundCode(String choiceFundCode) {
		this.choiceFundCode = choiceFundCode;
	}
	/**
	 * @return the iraCode
	 */
	public String getIraCode() {
		return iraCode;
	}
	/**
	 * @param iraCode the iraCode to set
	 */
	public void setIraCode(String iraCode) {
		this.iraCode = iraCode;
	}
	/**
	 * @return the tradeControl
	 */
	public String getTradeControl() {
		return tradeControl;
	}
	/**
	 * @param tradeControl the tradeControl to set
	 */
	public void setTradeControl(String tradeControl) {
		this.tradeControl = tradeControl;
	}
	/**
	 * @return the accountCategory
	 */
	public String getAccountCategory() {
		return accountCategory;
	}
	/**
	 * @param accountCategory the accountCategory to set
	 */
	public void setAccountCategory(String accountCategory) {
		this.accountCategory = accountCategory;
	}
	/**
	 * @return the collateralAcctInd
	 */
	public String getCollateralAcctInd() {
		return collateralAcctInd;
	}
	/**
	 * @param collateralAcctInd the collateralAcctInd to set
	 */
	public void setCollateralAcctInd(String collateralAcctInd) {
		this.collateralAcctInd = collateralAcctInd;
	}
	/**
	 * @return the isAcntViewCallReq
	 */
	public boolean isAcntViewCallReq() {
		return isAcntViewCallReq;
	}
	/**
	 * @param isAcntViewCallReq the isAcntViewCallReq to set
	 */
	public void setAcntViewCallReq(boolean isAcntViewCallReq) {
		this.isAcntViewCallReq = isAcntViewCallReq;
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
		final String TAB = "\r\n";

		String retValue = "";

		retValue = "FromMSAcc_DetailsTO ( "
			+ super.toString() + TAB
			+ "isAcntViewCallReq = " + this.isAcntViewCallReq + TAB
			+ "accountNumber = " + this.accountNumber + TAB
			+ "officeNumber = " + this.officeNumber + TAB
			+ "faNumber = " + this.faNumber + TAB
			+ "keyAccount = " + this.keyAccount + TAB
			+ "nickName = " + this.nickName + TAB
			+ "friendlyName = " + this.friendlyName + TAB
			+ "viewTransactionFlag = " + this.viewTransactionFlag + TAB
			+ "status = " + this.status + TAB
			+ "accountClass = " + this.accountClass + TAB
			+ "novusSubProduct = " + this.novusSubProduct + TAB
			+ "divPay = " + this.divPay + TAB
			+ "clientCategory = " + this.clientCategory + TAB
			+ "choiceFundCode = " + this.choiceFundCode + TAB
			+ "iraCode = " + this.iraCode + TAB
			+ "tradeControl = " + this.tradeControl + TAB
			+ "accountCategory = " + this.accountCategory + TAB
			+ "collateralAcctInd = " + this.collateralAcctInd + TAB
			+ " )";

		return retValue;
	}
}
