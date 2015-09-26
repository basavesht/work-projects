/**
 * 
 */
package com.tcs.bancs.objects.schema.request.payments.transfers;

import java.io.Serializable;

/**
 * @author 259245
 *
 */
public class UserDetails implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1935276777588408739L;
	private String loginId = null;
	private String clientUUID = null;
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;
	private String clientIdentifier = null;
	private String maskedClientIdentifier= null;

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
	 * @return the clientUUID
	 */
	public String getClientUUID() {
		return clientUUID;
	}
	/**
	 * @param clientUUID the clientUUID to set
	 */
	public void setClientUUID(String clientUUID) {
		this.clientUUID = clientUUID;
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

		retValue = "UserDetailsTO ( "
				+ super.toString() + TAB
				+ "loginId = " + this.loginId + TAB
				+ "clientUUID = " + this.clientUUID + TAB
				+ "firstName = " + this.firstName + TAB
				+ "middleName = " + this.middleName + TAB
				+ "lastName = " + this.lastName + TAB
				+ "clientIdentifier = " + this.clientIdentifier + TAB
				+ "maskedClientIdentifier = " + this.maskedClientIdentifier + TAB
				+ " )";

		return retValue;
	}

}
