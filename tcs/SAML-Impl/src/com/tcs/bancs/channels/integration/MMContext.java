package com.tcs.bancs.channels.integration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tcs.bancs.ui.helpers.xml.ContextDataParsingException;

public class MMContext implements Serializable
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MMContext.class);

	private String loginId;
	private String uuid;
	private String keyClient;
	private String keyLink;
	private String firstName;
	private String middleName;
	private String lastName;
	private boolean FA;
	private String devicePrint;
	private String deviceCookie;
	private String deviceFSO;
	private String lastAccountOpenDate;
	private String lastOnlineServicePasswordChangeDate;
	private String onlineServiceEnrollDate;
	private String lastAddressChangeDate;
	
	private String ClientIdentifier;
	private String MaskedClientIdentifier;
	
	private String lastPhoneChangeDate;
	private String lastEmailChangeDate;
	private String CSSessionID;
	private String ClientIPAddress;
	
	ArrayList<MMAccount> accounts = new ArrayList<MMAccount>();
	public MMContext(Document contextDocument) throws ContextHasNoAccounts, ContextDataParsingException 
	{
		NodeList nodes = contextDocument.getElementsByTagName("MMContext").item(0).getChildNodes();
		int nodes_length = nodes.getLength();
		for(int i = 0 ; i < nodes_length; i++)
		{
			Node child = nodes.item(i);
			String value = null;
			if ( child.getChildNodes().getLength() > 0 )
			{
				value = child.getChildNodes().item(0).getTextContent();
			}

			if ( child.getNodeName().equals("LoginId"))
			{
				loginId = value;
			}
			else if ( child.getNodeName().equals("UUID"))
			{
				uuid = value;
			}
			else if ( child.getNodeName().equals("KeyClient"))
			{
				keyClient = value;
			}
			else if ( child.getNodeName().equals("KeyLink"))
			{
				keyLink = value;
			}
			else if ( child.getNodeName().equals("FirstName"))
			{
				firstName = value;
			}
			else if ( child.getNodeName().equals("MiddleInitial"))
			{
				middleName = value;
			}
			else if ( child.getNodeName().equals("LastName"))
			{
				lastName = value;
			}
			else if ( child.getNodeName().equals("FAIndicator"))
			{
				FA = Boolean.parseBoolean(child.getTextContent());
			}
			else if ( child.getNodeName().equals("DevicePrint"))
			{
				devicePrint= value;
			}
			else if ( child.getNodeName().equals("DeviceCookie"))
			{
				deviceCookie=value;
			}
			else if ( child.getNodeName().equals("DeviceFSO"))
			{
				deviceFSO= value;
			}
			else if ( child.getNodeName().equals("LastAccountOpenDate"))
			{
				lastAccountOpenDate= value;
			}
			else if ( child.getNodeName().equals("LastOnlineServicePasswordChangeDate"))
			{
				lastOnlineServicePasswordChangeDate= value;
			}
			else if ( child.getNodeName().equals("OnlineServiceEnrollDate"))
			{
				onlineServiceEnrollDate= value;
			}
			else if ( child.getNodeName().equals("LastAddressChangeDate"))
			{
				lastAddressChangeDate= value;
			}
			else if ( child.getNodeName().equals("LastPhoneChangeDate"))
			{
				lastPhoneChangeDate=value;
			}
			else if ( child.getNodeName().equals("LastEmailChangeDate"))
			{
				lastEmailChangeDate= value;
			}
			else if ( child.getNodeName().equals("CSSessionID"))
			{
				CSSessionID=value;
			}
			else if ( child.getNodeName().equals("ClientIPAddress"))
			{
				ClientIPAddress=value;
			}else if ( child.getNodeName().equals("ClientIdentifier"))
			{
				ClientIdentifier=value;
			}else if ( child.getNodeName().equals("MaskedClientIdentifier"))
			{
				MaskedClientIdentifier=value;
			}
			else if ( child.getNodeName().equals("Accounts"))
			{
				NodeList child_accounts = child.getChildNodes();
				int child_accounts_length = child_accounts.getLength();
				for(int ii = 0 ; ii < child_accounts_length; ii++)
				{
					Node child_account = child_accounts.item(ii);
					if(child_account.getNodeName().equals("Account"))
					{
						MMAccount account = new MMAccount();
						NodeList child_account_attributes = child_account.getChildNodes();
						int child_account_attributes_length = child_account_attributes.getLength();
						for(int iii=0; iii<child_account_attributes_length; iii++)
						{
							Node child_account_attribute = child_account_attributes.item(iii);
							value = null;
							if ( child_account_attribute.getChildNodes().getLength() > 0 )
							{
								value = child_account_attribute.getChildNodes().item(0).getTextContent();
							}
							if ( child_account_attribute.getNodeName().equals("AccountNo"))
							{
								account.setAccountNumber(value);
							}
							else if ( child_account_attribute.getNodeName().equals("OfficeNo"))
							{
								account.setOfficeNumber(value);
							}
							else if ( child_account_attribute.getNodeName().equals("FANo"))
							{
								account.setFaNumber(value);
							}
							else if ( child_account_attribute.getNodeName().equals("KeyAccount"))
							{
								account.setKeyAccount(value);
							}
							else if ( child_account_attribute.getNodeName().equals("NickName"))
							{
								account.setNickName(value);
							}
							else if ( child_account_attribute.getNodeName().equals("FriendlyName"))
							{
								account.setFriendlyName(value);
							}
							else if ( child_account_attribute.getNodeName().equals("ViewTransactionFlag"))
							{
								account.setViewTransactionFlag(Boolean.parseBoolean(value));
							}
							else if ( child_account_attribute.getNodeName().equals("AccountStatus"))
							{
								account.setStatus(value);
							}
							else if ( child_account_attribute.getNodeName().equals("AccountClass"))
							{
								account.setAccountClass(value);
							}
							else if ( child_account_attribute.getNodeName().equals("NovusSubProduct"))
							{
								account.setNovusSubProduct(value);
							}
							else if ( child_account_attribute.getNodeName().equals("DivPay"))
							{
								account.setDivPay(value);
							}
							else if ( child_account_attribute.getNodeName().equals("ClientCategory"))
							{
								account.setClientCategory(value);
							}
							else if ( child_account_attribute.getNodeName().equals("ChoiceFundCode"))
							{
								account.setChoiceFundCode(value);
							}
							else if ( child_account_attribute.getNodeName().equals("IRACode"))
							{
								account.setIraCode(value);
							}
							else if ( child_account_attribute.getNodeName().equals("TradeControl"))
							{
								account.setTradeControl(value);
							}
							else if ( child_account_attribute.getNodeName().equals("AccountCategory"))
							{
								account.setAccountCategory(value);
							}
							else if ( child_account_attribute.getNodeName().equals("CollateralAcctInd"))
							{
								account.setCollateralAcctInd(value);
							}
						}
						accounts.add(account);
					}
				}
			}
		}
		
		validateConfig();
	}
	private void validateConfig() throws ContextHasNoAccounts, ContextDataParsingException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("validateConfig() - start"); //$NON-NLS-1$
		}

		if (accounts.size() == 0)
			throw new ContextHasNoAccounts();
		
		if (logger.isDebugEnabled()) {
			logger.debug("validateConfig() - end"); //$NON-NLS-1$
		}
	}
	public ArrayList<MMAccount> getAccounts() {
		return accounts;
	}
	public void setAccounts(ArrayList<MMAccount> accounts) {
		this.accounts = accounts;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public boolean isFA() {
		return FA;
	}
	public void setFA(boolean fa) {
		this.FA = fa;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getDeviceCookie() {
		return deviceCookie;
	}
	public void setDeviceCookie(String deviceCookie) {
		this.deviceCookie = deviceCookie;
	}
	public String getDeviceFSO() {
		return deviceFSO;
	}
	public void setDeviceFSO(String deviceFSO) {
		this.deviceFSO = deviceFSO;
	}
	public String getDevicePrint() {
		return devicePrint;
	}
	public void setDevicePrint(String devicePrint) {
		this.devicePrint = devicePrint;
	}
	public String getLastAccountOpenDate() {
		return lastAccountOpenDate;
	}
	public void setLastAccountOpenDate(String lastAccountOpenDate) {
		this.lastAccountOpenDate = lastAccountOpenDate;
	}
	public String getLastAddressChangeDate() {
		return lastAddressChangeDate;
	}
	public void setLastAddressChangeDate(String lastAddressChangeDate) {
		this.lastAddressChangeDate = lastAddressChangeDate;
	}
	public String getLastEmailChangeDate() {
		return lastEmailChangeDate;
	}
	public void setLastEmailChangeDate(String lastEmailChangeDate) {
		this.lastEmailChangeDate = lastEmailChangeDate;
	}
	public String getLastOnlineServicePasswordChangeDate() {
		return lastOnlineServicePasswordChangeDate;
	}
	public void setLastOnlineServicePasswordChangeDate(
			String lastOnlineServicePasswordChangeDate) {
		this.lastOnlineServicePasswordChangeDate = lastOnlineServicePasswordChangeDate;
	}
	public String getLastPhoneChangeDate() {
		return lastPhoneChangeDate;
	}
	public void setLastPhoneChangeDate(String lastPhoneChangeDate) {
		this.lastPhoneChangeDate = lastPhoneChangeDate;
	}
	public String getOnlineServiceEnrollDate() {
		return onlineServiceEnrollDate;
	}
	public void setOnlineServiceEnrollDate(String onlineServiceEnrollDate) {
		this.onlineServiceEnrollDate = onlineServiceEnrollDate;
	}
	/*public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}*/
	public String getCSSessionID() {
		return CSSessionID;
	}
	public void setCSSessionID(String sessionID) {
		CSSessionID = sessionID;
	}
	public String getClientIPAddress() {
		return ClientIPAddress;
	}
	public void setClientIPAddress(String clientIPAddress) {
		ClientIPAddress = clientIPAddress;
	}
	public String getKeyClient() {
		return keyClient;
	}
	public void setKeyClient(String keyClient) {
		this.keyClient = keyClient;
	}
	public String getKeyLink() {
		return keyLink;
	}
	public void setKeyLink(String keyLink) {
		this.keyLink = keyLink;
	}
	public String getClientIdentifier() {
		return ClientIdentifier;
	}
	public void setClientIdentifier(String clientIdentifier) {
		ClientIdentifier = clientIdentifier;
	}
	public String getMaskedClientIdentifier() {
		return MaskedClientIdentifier;
	}
	public void setMaskedClientIdentifier(String maskedClientIdentifier) {
		MaskedClientIdentifier = maskedClientIdentifier;
	}
}
