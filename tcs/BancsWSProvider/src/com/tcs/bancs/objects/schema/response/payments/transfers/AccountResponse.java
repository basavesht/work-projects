/**
 * 
 */
package com.tcs.bancs.objects.schema.response.payments.transfers;

import java.io.Serializable;

import com.tcs.bancs.objects.schema.request.payments.transfers.AcntType;

/**
 * @author 259245
 *
 */
public class AccountResponse implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6782659242224649957L;
	private AcntType acntType = null;
	private String office = null;
	private String fa = null;
	private String account = null;
	private String keyAccount = null;
	private String displayName = null;
	private String externalAcntRefId = null;
	private AcntBalance acntBalance = null;

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
	 * @return the acntBalance
	 */
	public AcntBalance getAcntBalance() {
		return acntBalance;
	}
	/**
	 * @param acntBalance the acntBalance to set
	 */
	public void setAcntBalance(AcntBalance acntBalance) {
		this.acntBalance = acntBalance;
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

		retValue = "AccountResponse ( "
				+ super.toString() + TAB
				+ "acntType = " + this.acntType + TAB
				+ "office = " + this.office + TAB
				+ "fa = " + this.fa + TAB
				+ "account = " + this.account + TAB
				+ "keyAccount = " + this.keyAccount + TAB
				+ "displayName = " + this.displayName + TAB
				+ "externalAcntRefId = " + this.externalAcntRefId + TAB
				+ "acntBalance = " + this.acntBalance + TAB
				+ " )";

		return retValue;
	}

}
