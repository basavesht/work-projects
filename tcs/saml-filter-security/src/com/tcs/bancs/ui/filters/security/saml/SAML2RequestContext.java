package com.tcs.bancs.ui.filters.security.saml;

import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Status;


public class SAML2RequestContext {
	private boolean encryptAssertion;
	private String inboundMessageIssuer;
	private Status failureStatus;
	private String localEntityId;
	private SAML2ProfileConfiguration profileConfiguration;
	private NameID subjectNameIdentifier;

	public boolean encryptAssertion() {
		// TODO Auto-generated method stub
		return encryptAssertion;
	}

	public String getInboundMessageIssuer() {
		// TODO Auto-generated method stub
		return inboundMessageIssuer;
	}

	public void setFailureStatus(Status status) {
		failureStatus = status;
		
	}

	public String getLocalEntityId() {
		// TODO Auto-generated method stub
		return localEntityId;
	}

	public SAML2ProfileConfiguration getProfileConfiguration() {
		// TODO Auto-generated method stub
		return profileConfiguration;
	}

	public void setSubjectNameIdentifier(NameID nameID) {
		subjectNameIdentifier = nameID;
		
	}

	/**
	 * @return the encryptAssertion
	 */
	public boolean isEncryptAssertion() {
		return encryptAssertion;
	}

	/**
	 * @param encryptAssertion the encryptAssertion to set
	 */
	public void setEncryptAssertion(boolean encryptAssertion) {
		this.encryptAssertion = encryptAssertion;
	}

	/**
	 * @return the subjectNameIdentifier
	 */
	public NameID getSubjectNameIdentifier() {
		return subjectNameIdentifier;
	}

	/**
	 * @param inboundMessageIssuer the inboundMessageIssuer to set
	 */
	public void setInboundMessageIssuer(String inboundMessageIssuer) {
		this.inboundMessageIssuer = inboundMessageIssuer;
	}

	/**
	 * @param localEntityId the localEntityId to set
	 */
	public void setLocalEntityId(String localEntityId) {
		this.localEntityId = localEntityId;
	}

	/**
	 * @param profileConfiguration the profileConfiguration to set
	 */
	public void setProfileConfiguration(
			SAML2ProfileConfiguration profileConfiguration) {
		this.profileConfiguration = profileConfiguration;
	}

	public Status getFailureStatus() {
		// TODO Auto-generated method stub
		return failureStatus;
	}

}
