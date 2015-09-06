package com.tcs.bancs.channels.integration;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class MMAccount implements Serializable
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MMAccount.class);

	private String accountNumber;
	private String officeNumber;
	private String faNumber;
	private String keyAccount;
	private String nickName;
	private String friendlyName;
	private boolean viewTransactionFlag;
	private String status;
	private String accountClass;
	private String novusSubProduct;
	private String divPay;
	private String clientCategory;
	private String choiceFundCode;
	private String iraCode;
	private String tradeControl;
	private String accountCategory;
	private String collateralAcctInd;
	
	public String getCollateralAcctInd() {
		return collateralAcctInd;
	}

	public void setCollateralAcctInd(String collateralAcctInd) {
		this.collateralAcctInd = collateralAcctInd;
	}

	public boolean isAccountRestricted()
	{
		return ( divPay != null && divPay.equals("4") && status != null && status.equals("0"));
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getChoiceFundCode() {
		return choiceFundCode;
	}
	public void setChoiceFundCode(String choiceFundCode) {
		this.choiceFundCode = choiceFundCode;
	}
	public String getClientCategory() {
		return clientCategory;
	}
	public void setClientCategory(String clientCategory) {
		this.clientCategory = clientCategory;
	}
	public String getDivPay() {
		return divPay;
	}
	public void setDivPay(String divPay) {
		this.divPay = divPay;
	}
	public String getFriendlyName() {
		return friendlyName;
	}
	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	public String getIraCode() {
		return iraCode;
	}
	public void setIraCode(String iraCode) {
		this.iraCode = iraCode;
	}
	public String getKeyAccount() {
		return keyAccount;
	}
	public void setKeyAccount(String keyAccount) {
		this.keyAccount = keyAccount;
	}
	public String getNickName() {
		
		return (nickName!=null)?nickName:friendlyName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getNovusSubProduct() {
		return novusSubProduct;
	}
	public void setNovusSubProduct(String novusSubProduct) {
		this.novusSubProduct = novusSubProduct;
	}
	public String getAccountClass() {
		return accountClass;
	}
	public void setAccountClass(String accountClass) {
		this.accountClass = accountClass;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFaNumber() {
		return faNumber;
	}
	public void setFaNumber(String faNumber) {
		this.faNumber = faNumber;
	}
	public String getOfficeNumber() {
		return officeNumber;
	}
	public void setOfficeNumber(String officeNumber) {
		this.officeNumber = officeNumber;
	}
	public boolean isViewTransactionFlag() {
		return viewTransactionFlag;
	}
	public void setViewTransactionFlag(boolean viewTransactionFlag) {
		this.viewTransactionFlag = viewTransactionFlag;
	}

	public String getAccountCategory() {
		return accountCategory;
	}

	public void setAccountCategory(String accountCategory) {
		this.accountCategory = accountCategory;
	}

	public String getTradeControl() {
		return tradeControl;
	}

	public void setTradeControl(String tradeControl) {
		this.tradeControl = tradeControl;
	}
}
