package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;

import com.tcs.ebw.serverside.services.channelPaymentServices.AccountPlating;

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

	//Statement linked account attributes...
	private String accountNumber =null;
	private String officeNumber =null;
	private String faNumber =null;
	private String keyAccount =null;
	private String spendingPower =null;
	private boolean isPrimaryAccount;
	private boolean isAnchorAccount;

	//MM Context to populate the Account Owner attributes Account view service...
	private String[] keyClientId =null;
	private String account_owner_name =null;
	private String ssn_number =null;
	private boolean isClientPrimary;

	//MMContext to populate the following attributes from the Account View service for block display call..
	private String accountName =null; //Corporate Name..
	private String status =null;
	private String accountClass =null;
	private String subClass =null;
	private String novusSubProduct =null;
	private String divPay =null;
	private String clientCategory =null;
	private String choiceFundCode =null;
	private String iraCode =null;
	private String tradeControl =null;
	private String collateralAcctInd =null;
	private String accountCategory =null;
	private String account_tier =null;

	//Stores the acnt plating response...
	private AccountPlating accountPlating =null; 

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
	 * @return the spendingPower
	 */
	public String getSpendingPower() {
		return spendingPower;
	}
	/**
	 * @param spendingPower the spendingPower to set
	 */
	public void setSpendingPower(String spendingPower) {
		this.spendingPower = spendingPower;
	}
	/**
	 * @return the isPrimaryAccount
	 */
	public boolean isPrimaryAccount() {
		return isPrimaryAccount;
	}
	/**
	 * @param isPrimaryAccount the isPrimaryAccount to set
	 */
	public void setPrimaryAccount(boolean isPrimaryAccount) {
		this.isPrimaryAccount = isPrimaryAccount;
	}
	/**
	 * @return the isAnchorAccount
	 */
	public boolean isAnchorAccount() {
		return isAnchorAccount;
	}
	/**
	 * @param isAnchorAccount the isAnchorAccount to set
	 */
	public void setAnchorAccount(boolean isAnchorAccount) {
		this.isAnchorAccount = isAnchorAccount;
	}
	/**
	 * @return the keyClientId
	 */
	public String[] getKeyClientId() {
		return keyClientId;
	}
	/**
	 * @param keyClientId the keyClientId to set
	 */
	public void setKeyClientId(String[] keyClientId) {
		this.keyClientId = keyClientId;
	}
	/**
	 * @return the account_owner_name
	 */
	public String getAccount_owner_name() {
		return account_owner_name;
	}
	/**
	 * @param account_owner_name the account_owner_name to set
	 */
	public void setAccount_owner_name(String account_owner_name) {
		this.account_owner_name = account_owner_name;
	}
	/**
	 * @return the ssn_number
	 */
	public String getSsn_number() {
		return ssn_number;
	}
	/**
	 * @param ssn_number the ssn_number to set
	 */
	public void setSsn_number(String ssn_number) {
		this.ssn_number = ssn_number;
	}
	/**
	 * @return the isClientPrimary
	 */
	public boolean isClientPrimary() {
		return isClientPrimary;
	}
	/**
	 * @param isClientPrimary the isClientPrimary to set
	 */
	public void setClientPrimary(boolean isClientPrimary) {
		this.isClientPrimary = isClientPrimary;
	}
	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}
	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
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
	 * @return the subClass
	 */
	public String getSubClass() {
		return subClass;
	}
	/**
	 * @param subClass the subClass to set
	 */
	public void setSubClass(String subClass) {
		this.subClass = subClass;
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
	 * @return the account_tier
	 */
	public String getAccount_tier() {
		return account_tier;
	}
	/**
	 * @param account_tier the account_tier to set
	 */
	public void setAccount_tier(String account_tier) {
		this.account_tier = account_tier;
	}
	/**
	 * @return the accountPlating
	 */
	public AccountPlating getAccountPlating() {
		return accountPlating;
	}
	/**
	 * @param accountPlating the accountPlating to set
	 */
	public void setAccountPlating(AccountPlating accountPlating) {
		this.accountPlating = accountPlating;
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
			+ "accountNumber = " + this.accountNumber + TAB
			+ "officeNumber = " + this.officeNumber + TAB
			+ "faNumber = " + this.faNumber + TAB
			+ "keyAccount = " + this.keyAccount + TAB
			+ "spendingPower = " + this.spendingPower + TAB
			+ "isPrimaryAccount = " + this.isPrimaryAccount + TAB
			+ "isAnchorAccount = " + this.isAnchorAccount + TAB
			+ "keyClientId = " + this.keyClientId + TAB
			+ "account_owner_name = " + this.account_owner_name + TAB
			+ "ssn_number = " + this.ssn_number + TAB
			+ "isClientPrimary = " + this.isClientPrimary + TAB
			+ "accountName = " + this.accountName + TAB
			+ "status = " + this.status + TAB
			+ "accountClass = " + this.accountClass + TAB
			+ "subClass = " + this.subClass + TAB
			+ "novusSubProduct = " + this.novusSubProduct + TAB
			+ "divPay = " + this.divPay + TAB
			+ "clientCategory = " + this.clientCategory + TAB
			+ "choiceFundCode = " + this.choiceFundCode + TAB
			+ "iraCode = " + this.iraCode + TAB
			+ "tradeControl = " + this.tradeControl + TAB
			+ "collateralAcctInd = " + this.collateralAcctInd + TAB
			+ "accountCategory = " + this.accountCategory + TAB
			+ "account_tier = " + this.account_tier + TAB
			+ "accountPlating = " + this.accountPlating + TAB
			+ " )";

		return retValue;
	}
}