/**
 * 
 */
package com.tcs.ebw.payments.transferobject;

import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
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

	public String getEVENT_DESC() {
		return EVENT_DESC;
	}
	public void setEVENT_DESC(String event_desc) {
		EVENT_DESC = event_desc;
	}

	public boolean isExternalAccount() {
		return isExternalAccount;
	}
	public void setExternalAccount(boolean isExternalAccount) {
		this.isExternalAccount = isExternalAccount;
	}
	public String getPaypayref() {
		return paypayref;
	}
	public void setPaypayref(String paypayref) {
		this.paypayref = paypayref;
	}
	public String getEVENT_ID() {
		return EVENT_ID;
	}
	public void setEVENT_ID(String event_id) {
		EVENT_ID = event_id;
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
	public String getCpypayeeid() {
		return cpypayeeid;
	}
	public void setCpypayeeid(String cpypayeeid) {
		this.cpypayeeid = cpypayeeid;
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
	        + " )";
	
	    return retValue;
	}

}
