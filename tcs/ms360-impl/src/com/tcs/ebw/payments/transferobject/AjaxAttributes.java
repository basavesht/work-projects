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
	private String debitCreditInd = null;
	private Object acntDetails = null;

	public String getAccountRefID() {
		return accountRefID;
	}
	public void setAccountRefID(String accountRefID) {
		this.accountRefID = accountRefID;
	}
	public String getOffice_account_fa() {
		return office_account_fa;
	}
	public void setOffice_account_fa(String office_account_fa) {
		this.office_account_fa = office_account_fa;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getJsonAccBalMapStr() {
		return jsonAccBalMapStr;
	}
	public void setJsonAccBalMapStr(String jsonAccBalMapStr) {
		this.jsonAccBalMapStr = jsonAccBalMapStr;
	}
	public String getJsonAcntPlateMapStr() {
		return jsonAcntPlateMapStr;
	}
	public void setJsonAcntPlateMapStr(String jsonAcntPlateMapStr) {
		this.jsonAcntPlateMapStr = jsonAcntPlateMapStr;
	}
	public String getJsonAcntPayeeMapStr() {
		return jsonAcntPayeeMapStr;
	}
	public void setJsonAcntPayeeMapStr(String jsonAcntPayeeMapStr) {
		this.jsonAcntPayeeMapStr = jsonAcntPayeeMapStr;
	}
	public String getDebitCreditInd() {
		return debitCreditInd;
	}
	public void setDebitCreditInd(String debitCreditInd) {
		this.debitCreditInd = debitCreditInd;
	}
	public Object getAcntDetails() {
		return acntDetails;
	}
	public void setAcntDetails(Object acntDetails) {
		this.acntDetails = acntDetails;
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
			+ "debitCreditInd = " + this.debitCreditInd + TAB
			+ "acntDetails = " + this.acntDetails + TAB
			+ " )";

		return retValue;
	}
}