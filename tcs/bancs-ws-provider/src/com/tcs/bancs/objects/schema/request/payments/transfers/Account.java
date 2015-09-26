/**
 * 
 */
package com.tcs.bancs.objects.schema.request.payments.transfers;

import java.io.Serializable;

/**
 * @author 259245
 *
 */
public class Account implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8478704690570705094L;
	private DrCrIndicator drCrIndicator = null;
	private AcntType acntType =  null;
	private String office = null;
	private String fa = null;
	private String account = null;
	private String keyAccount = null;
	private String externalAcntRefId = null;
	private String accountStatus = null;
	private String accountClass = null;
	private String novusSubProduct = null;
	private String divPay = null;
	private String clientCategory = null;
	private String choiceFundCode = null;
	private String iraCode = null;
	private String tradeControl = null;
	private String accountCategory = null;
	private String collateralAcctInd =  null;
	private String displayName = null;

	/**
	 * @return the drCrIndicator
	 */
	public DrCrIndicator getDrCrIndicator() {
		return drCrIndicator;
	}
	/**
	 * @param drCrIndicator the drCrIndicator to set
	 */
	public void setDrCrIndicator(DrCrIndicator drCrIndicator) {
		this.drCrIndicator = drCrIndicator;
	}
	/**
	 * @return the acntType
	 */
	public AcntType getAcntType() {
		return acntType;
	}
	/**
	 * @param acntType the acntType to set
	 */
	public void setAcntType(AcntType acntType) {
		this.acntType = acntType;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * @return the office
	 */
	public String getOffice() {
		return office;
	}
	/**
	 * @param office the office to set
	 */
	public void setOffice(String office) {
		this.office = office;
	}
	/**
	 * @return the fa
	 */
	public String getFa() {
		return fa;
	}
	/**
	 * @param fa the fa to set
	 */
	public void setFa(String fa) {
		this.fa = fa;
	}
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
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
	 * @return the externalAcntRefId
	 */
	public String getExternalAcntRefId() {
		return externalAcntRefId;
	}
	/**
	 * @param externalAcntRefId the externalAcntRefId to set
	 */
	public void setExternalAcntRefId(String externalAcntRefId) {
		this.externalAcntRefId = externalAcntRefId;
	}
	/**
	 * @return the accountStatus
	 */
	public String getAccountStatus() {
		return accountStatus;
	}
	/**
	 * @param accountStatus the accountStatus to set
	 */
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
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

		retValue = "AccountTO ( "
				+ super.toString() + TAB
				+ "drCrIndicator = " + this.drCrIndicator + TAB
				+ "acntType = " + this.acntType + TAB
				+ "office = " + this.office + TAB
				+ "fa = " + this.fa + TAB
				+ "account = " + this.account + TAB
				+ "keyAccount = " + this.keyAccount + TAB
				+ "externalAcntRefId = " + this.externalAcntRefId + TAB
				+ "accountStatus = " + this.accountStatus + TAB
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
