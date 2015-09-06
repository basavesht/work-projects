package com.tcs.ebw.payments.transferobject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MSUser_DetailsTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3742173250866470131L;
	private String loginId =null;
	private String uuid =null;
	private String firstName =null;
	private String middleName =null;
	private String lastName =null;
	private boolean FA = false;
	private String devicePrint =null;
	private String deviceCookie =null;
	private String deviceFSO =null;
	private String lastAccountOpenDate =null;
	private String lastOnlineServicePasswordChangeDate =null;
	private String onlineServiceEnrollDate =null;
	private String lastAddressChangeDate =null;
	private String lastPhoneChangeDate =null;
	private String lastEmailChangeDate =null;
	private String sessionId =null;
	private String clientIPAddress =null;
	private String clientIdentifier =null;
	private String maskedClientIdentifier =null;
	private ArrayList contextAccounts = new ArrayList();
	
	/**
	 * @return the clientIPAddress
	 */
	public String getClientIPAddress() {
		return clientIPAddress;
	}
	/**
	 * @param clientIPAddress the clientIPAddress to set
	 */
	public void setClientIPAddress(String clientIPAddress) {
		this.clientIPAddress = clientIPAddress;
	}
	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}
	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the fA
	 */
	public boolean isFA() {
		return FA;
	}
	/**
	 * @param fa the fA to set
	 */
	public void setFA(boolean fa) {
		FA = fa;
	}
	/**
	 * @return the devicePrint
	 */
	public String getDevicePrint() {
		return devicePrint;
	}
	/**
	 * @param devicePrint the devicePrint to set
	 */
	public void setDevicePrint(String devicePrint) {
		this.devicePrint = devicePrint;
	}
	/**
	 * @return the deviceCookie
	 */
	public String getDeviceCookie() {
		return deviceCookie;
	}
	/**
	 * @param deviceCookie the deviceCookie to set
	 */
	public void setDeviceCookie(String deviceCookie) {
		this.deviceCookie = deviceCookie;
	}
	/**
	 * @return the deviceFSO
	 */
	public String getDeviceFSO() {
		return deviceFSO;
	}
	/**
	 * @param deviceFSO the deviceFSO to set
	 */
	public void setDeviceFSO(String deviceFSO) {
		this.deviceFSO = deviceFSO;
	}
	/**
	 * @return the lastAccountOpenDate
	 */
	public String getLastAccountOpenDate() {
		return lastAccountOpenDate;
	}
	/**
	 * @param lastAccountOpenDate the lastAccountOpenDate to set
	 */
	public void setLastAccountOpenDate(String lastAccountOpenDate) {
		this.lastAccountOpenDate = lastAccountOpenDate;
	}
	/**
	 * @return the lastOnlineServicePasswordChangeDate
	 */
	public String getLastOnlineServicePasswordChangeDate() {
		return lastOnlineServicePasswordChangeDate;
	}
	/**
	 * @param lastOnlineServicePasswordChangeDate the lastOnlineServicePasswordChangeDate to set
	 */
	public void setLastOnlineServicePasswordChangeDate(
			String lastOnlineServicePasswordChangeDate) {
		this.lastOnlineServicePasswordChangeDate = lastOnlineServicePasswordChangeDate;
	}
	/**
	 * @return the onlineServiceEnrollDate
	 */
	public String getOnlineServiceEnrollDate() {
		return onlineServiceEnrollDate;
	}
	/**
	 * @param onlineServiceEnrollDate the onlineServiceEnrollDate to set
	 */
	public void setOnlineServiceEnrollDate(String onlineServiceEnrollDate) {
		this.onlineServiceEnrollDate = onlineServiceEnrollDate;
	}
	/**
	 * @return the lastAddressChangeDate
	 */
	public String getLastAddressChangeDate() {
		return lastAddressChangeDate;
	}
	/**
	 * @param lastAddressChangeDate the lastAddressChangeDate to set
	 */
	public void setLastAddressChangeDate(String lastAddressChangeDate) {
		this.lastAddressChangeDate = lastAddressChangeDate;
	}
	/**
	 * @return the lastPhoneChangeDate
	 */
	public String getLastPhoneChangeDate() {
		return lastPhoneChangeDate;
	}
	/**
	 * @param lastPhoneChangeDate the lastPhoneChangeDate to set
	 */
	public void setLastPhoneChangeDate(String lastPhoneChangeDate) {
		this.lastPhoneChangeDate = lastPhoneChangeDate;
	}
	/**
	 * @return the lastEmailChangeDate
	 */
	public String getLastEmailChangeDate() {
		return lastEmailChangeDate;
	}
	/**
	 * @param lastEmailChangeDate the lastEmailChangeDate to set
	 */
	public void setLastEmailChangeDate(String lastEmailChangeDate) {
		this.lastEmailChangeDate = lastEmailChangeDate;
	}
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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
	    
	    retValue = "MSUser_DetailsTO ( "
	        + super.toString() + TAB
	        + "loginId = " + this.loginId + TAB
	        + "uuid = " + this.uuid + TAB
	        + "firstName = " + this.firstName + TAB
	        + "middleName = " + this.middleName + TAB
	        + "lastName = " + this.lastName + TAB
	        + "FA = " + this.FA + TAB
	        + "devicePrint = " + this.devicePrint + TAB
	        + "deviceCookie = " + this.deviceCookie + TAB
	        + "deviceFSO = " + this.deviceFSO + TAB
	        + "lastAccountOpenDate = " + this.lastAccountOpenDate + TAB
	        + "lastOnlineServicePasswordChangeDate = " + this.lastOnlineServicePasswordChangeDate + TAB
	        + "onlineServiceEnrollDate = " + this.onlineServiceEnrollDate + TAB
	        + "lastAddressChangeDate = " + this.lastAddressChangeDate + TAB
	        + "lastPhoneChangeDate = " + this.lastPhoneChangeDate + TAB
	        + "lastEmailChangeDate = " + this.lastEmailChangeDate + TAB
	        + "sessionId = " + this.sessionId + TAB
	        + "clientIPAddress = " + this.clientIPAddress + TAB
	        + " )";
	
	    return retValue;
	}
	/**
	 * @return the clientIdentifier
	 */
	public String getClientIdentifier() {
		return clientIdentifier;
	}
	/**
	 * @param clientIdentifier the clientIdentifier to set
	 */
	public void setClientIdentifier(String clientIdentifier) {
		this.clientIdentifier = clientIdentifier;
	}
	/**
	 * @return the maskedClientIdentifier
	 */
	public String getMaskedClientIdentifier() {
		return maskedClientIdentifier;
	}
	/**
	 * @param maskedClientIdentifier the maskedClientIdentifier to set
	 */
	public void setMaskedClientIdentifier(String maskedClientIdentifier) {
		this.maskedClientIdentifier = maskedClientIdentifier;
	}
	/**
	 * @return the contextAccounts
	 */
	public ArrayList getContextAccounts() {
		return contextAccounts;
	}
	/**
	 * @param contextAccounts the contextAccounts to set
	 */
	public void setContextAccounts(ArrayList contextAccounts) {
		this.contextAccounts = contextAccounts;
	}

}
