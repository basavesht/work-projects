package com.tcs.ebw.payments.transferobject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MerlinOutResponse implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5890287584074976476L;
	public String accountNumber;
	public String keyAccNumber;
	public String catTier;
	public String acntCategory = null;

	public String getAcntCategory() {
		return acntCategory;
	}
	public void setAcntCategory(String acntCategory) {
		this.acntCategory = acntCategory;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getCatTier() {
		return catTier;
	}
	public void setCatTier(String catTier) {
		this.catTier = catTier;
	}
	public String getKeyAccNumber() {
		return keyAccNumber;
	}
	public void setKeyAccNumber(String keyAccNumber) {
		this.keyAccNumber = keyAccNumber;
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

		retValue = "MerlinOutResponse ( "
			+ super.toString() + TAB
			+ "accountNumber = " + this.accountNumber + TAB
			+ "keyAccNumber = " + this.keyAccNumber + TAB
			+ "catTier = " + this.catTier + TAB
			+ "acntCategory = " + this.acntCategory + TAB
			+ " )";

		return retValue;
	}
}
