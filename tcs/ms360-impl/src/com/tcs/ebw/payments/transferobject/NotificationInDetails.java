/**
 * 
 */
package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *   224703			30-05-2011		P3				 3rd Party ACH - Alerts
 * **********************************************************
 */
public class NotificationInDetails extends EBWTransferObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3117567599837335693L;
	private String EVENT_DESC = null;
	private String EVENT_ID = null;
	private String paypayref = null;
	private String cpypayeeid = null;
	private boolean isExternalAccount = false;
	private boolean isDsntKeyAcntNumber = false;
	private boolean isFrequency = false;
	private boolean isUnitlValue = false;
	private boolean isSkippedExecDate = false;
	private boolean isNextTransferDate = false;
	private boolean isAccountInitDate = false;
	private boolean isAccountSuspendDate = false;
	private boolean isTxnSuspendDate = false;
	private boolean isReturnCodeDesc = false;
	private boolean isAccountOwnerMSSB = false;
	private boolean isPrimaryAccount = false;
	private boolean isBankName = false;

	public String getEVENT_DESC() {
		return EVENT_DESC;
	}
	public void setEVENT_DESC(String event_desc) {
		EVENT_DESC = event_desc;
	}
	public String getEVENT_ID() {
		return EVENT_ID;
	}
	public void setEVENT_ID(String event_id) {
		EVENT_ID = event_id;
	}
	public String getPaypayref() {
		return paypayref;
	}
	public void setPaypayref(String paypayref) {
		this.paypayref = paypayref;
	}
	public String getCpypayeeid() {
		return cpypayeeid;
	}
	public void setCpypayeeid(String cpypayeeid) {
		this.cpypayeeid = cpypayeeid;
	}
	public boolean isExternalAccount() {
		return isExternalAccount;
	}
	public void setExternalAccount(boolean isExternalAccount) {
		this.isExternalAccount = isExternalAccount;
	}
	public boolean isDsntKeyAcntNumber() {
		return isDsntKeyAcntNumber;
	}
	public void setDsntKeyAcntNumber(boolean isDsntKeyAcntNumber) {
		this.isDsntKeyAcntNumber = isDsntKeyAcntNumber;
	}
	public boolean isFrequency() {
		return isFrequency;
	}
	public void setFrequency(boolean isFrequency) {
		this.isFrequency = isFrequency;
	}
	public boolean isUnitlValue() {
		return isUnitlValue;
	}
	public void setUnitlValue(boolean isUnitlValue) {
		this.isUnitlValue = isUnitlValue;
	}
	public boolean isSkippedExecDate() {
		return isSkippedExecDate;
	}
	public void setSkippedExecDate(boolean isSkippedExecDate) {
		this.isSkippedExecDate = isSkippedExecDate;
	}
	public boolean isNextTransferDate() {
		return isNextTransferDate;
	}
	public void setNextTransferDate(boolean isNextTransferDate) {
		this.isNextTransferDate = isNextTransferDate;
	}
	public boolean isAccountInitDate() {
		return isAccountInitDate;
	}
	public void setAccountInitDate(boolean isAccountInitDate) {
		this.isAccountInitDate = isAccountInitDate;
	}
	public boolean isAccountSuspendDate() {
		return isAccountSuspendDate;
	}
	public void setAccountSuspendDate(boolean isAccountSuspendDate) {
		this.isAccountSuspendDate = isAccountSuspendDate;
	}
	public boolean isTxnSuspendDate() {
		return isTxnSuspendDate;
	}
	public void setTxnSuspendDate(boolean isTxnSuspendDate) {
		this.isTxnSuspendDate = isTxnSuspendDate;
	}
	public boolean isReturnCodeDesc() {
		return isReturnCodeDesc;
	}
	public void setReturnCodeDesc(boolean isReturnCodeDesc) {
		this.isReturnCodeDesc = isReturnCodeDesc;
	}
	public boolean isAccountOwnerMSSB() {
		return isAccountOwnerMSSB;
	}
	public void setAccountOwnerMSSB(boolean isAccountOwnerMSSB) {
		this.isAccountOwnerMSSB = isAccountOwnerMSSB;
	}
	public boolean isPrimaryAccount() {
		return isPrimaryAccount;
	}
	public void setPrimaryAccount(boolean isPrimaryAccount) {
		this.isPrimaryAccount = isPrimaryAccount;
	}
	public boolean isBankName() {
		return isBankName;
	}
	public void setBankName(boolean isBankName) {
		this.isBankName = isBankName;
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

		retValue = "NotificationInDetails ( "
			+ super.toString() + TAB
			+ "EVENT_DESC = " + this.EVENT_DESC + TAB
			+ "EVENT_ID = " + this.EVENT_ID + TAB
			+ "paypayref = " + this.paypayref + TAB
			+ "cpypayeeid = " + this.cpypayeeid + TAB
			+ "isExternalAccount = " + this.isExternalAccount + TAB
			+ "isDsntKeyAcntNumber = " + this.isDsntKeyAcntNumber + TAB
			+ "isFrequency = " + this.isFrequency + TAB
			+ "isUnitlValue = " + this.isUnitlValue + TAB
			+ "isSkippedExecDate = " + this.isSkippedExecDate + TAB
			+ "isNextTransferDate = " + this.isNextTransferDate + TAB
			+ "isAccountInitDate = " + this.isAccountInitDate + TAB
			+ "isAccountSuspendDate = " + this.isAccountSuspendDate + TAB
			+ "isTxnSuspendDate = " + this.isTxnSuspendDate + TAB
			+ "isReturnCodeDesc = " + this.isReturnCodeDesc + TAB
			+ "isAccountOwnerMSSB = " + this.isAccountOwnerMSSB + TAB
			+ "isPrimaryAccount = " + this.isPrimaryAccount + TAB
			+ "isBankName = " + this.isBankName + TAB
			+ " )";

		return retValue;
	}

}