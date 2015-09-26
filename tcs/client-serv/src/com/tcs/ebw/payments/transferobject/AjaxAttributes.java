/**
 * 
 */
package com.tcs.ebw.payments.transferobject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class AjaxAttributes implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4042202202276734238L;
	private String accountRefID = null;
	private String office_account_fa = null;
	private String transferType = null;
	private String accountType = null;
	private String jsonAccBalMapStr = null;
	private String jsonAcntPlateMapStr = null;
	private String jsonAcntPayeeMapStr = null;
	private Object acntDetails = null;

	/**
	 * @return the acntDetails
	 */
	public Object getAcntDetails() {
		return acntDetails;
	}
	/**
	 * @param acntDetails the acntDetails to set
	 */
	public void setAcntDetails(Object acntDetails) {
		this.acntDetails = acntDetails;
	}
	/**
	 * @return the accountRefID
	 */
	public String getAccountRefID() {
		return accountRefID;
	}
	/**
	 * @param accountRefID the accountRefID to set
	 */
	public void setAccountRefID(String accountRefID) {
		this.accountRefID = accountRefID;
	}
	/**
	 * @return the office_account_fa
	 */
	public String getOffice_account_fa() {
		return office_account_fa;
	}
	/**
	 * @param office_account_fa the office_account_fa to set
	 */
	public void setOffice_account_fa(String office_account_fa) {
		this.office_account_fa = office_account_fa;
	}
	/**
	 * @return the transferType
	 */
	public String getTransferType() {
		return transferType;
	}
	/**
	 * @param transferType the transferType to set
	 */
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}
	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	/**
	 * @return the jsonAccBalMapStr
	 */
	public String getJsonAccBalMapStr() {
		return jsonAccBalMapStr;
	}
	/**
	 * @param jsonAccBalMapStr the jsonAccBalMapStr to set
	 */
	public void setJsonAccBalMapStr(String jsonAccBalMapStr) {
		this.jsonAccBalMapStr = jsonAccBalMapStr;
	}
	/**
	 * @return the jsonAcntPlateMapStr
	 */
	public String getJsonAcntPlateMapStr() {
		return jsonAcntPlateMapStr;
	}
	/**
	 * @param jsonAcntPlateMapStr the jsonAcntPlateMapStr to set
	 */
	public void setJsonAcntPlateMapStr(String jsonAcntPlateMapStr) {
		this.jsonAcntPlateMapStr = jsonAcntPlateMapStr;
	}
	/**
	 * @return the jsonAcntPayeeMapStr
	 */
	public String getJsonAcntPayeeMapStr() {
		return jsonAcntPayeeMapStr;
	}
	/**
	 * @param jsonAcntPayeeMapStr the jsonAcntPayeeMapStr to set
	 */
	public void setJsonAcntPayeeMapStr(String jsonAcntPayeeMapStr) {
		this.jsonAcntPayeeMapStr = jsonAcntPayeeMapStr;
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

		retValue = "AjaxAttributes ( "
			+ super.toString() + TAB
			+ "accountRefID = " + this.accountRefID + TAB
			+ "office_account_fa = " + this.office_account_fa + TAB
			+ "transferType = " + this.transferType + TAB
			+ "accountType = " + this.accountType + TAB
			+ "jsonAccBalMapStr = " + this.jsonAccBalMapStr + TAB
			+ "jsonAcntPlateMapStr = " + this.jsonAcntPlateMapStr + TAB
			+ "jsonAcntPayeeMapStr = " + this.jsonAcntPayeeMapStr + TAB
			+ "acntDetails = " + this.acntDetails + TAB
			+ " )";

		return retValue;
	}
}