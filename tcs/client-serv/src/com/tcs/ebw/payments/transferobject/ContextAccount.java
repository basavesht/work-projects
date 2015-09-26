/**
 * 
 */
package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ContextAccount implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1420873494148935107L;
	private String office = null;
	private String fa = null;
	private String account = null;
	private String keyAccount = null;

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

		retValue = "ContextAccount ( "
			+ super.toString() + TAB
			+ "office = " + this.office + TAB
			+ "fa = " + this.fa + TAB
			+ "account = " + this.account + TAB
			+ "keyAccount = " + this.keyAccount + TAB
			+ " )";

		return retValue;
	}

}
