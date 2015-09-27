package com.tcs.bancs.ui.filters.security.saml;

import java.util.ArrayList;
import java.util.Collection;

public class SAML2ProfileConfiguration {
	private long assertionLifetime;
	private Collection<String> assertionAudiences = new ArrayList<String>();
	private Collection<String> proxyAudiences = new ArrayList<String>();
	private int proxyCount;
;
	private String nameIdValue;
	private boolean signAssertion;
	public long getAssertionLifetime() {
		// TODO Auto-generated method stub
		return assertionLifetime;
	}

	public Collection<String> getAssertionAudiences() {
		// TODO Auto-generated method stub
		return assertionAudiences;
	}

	/**
	 * @param assertionAudiences the assertionAudiences to set
	 */
	public void setAssertionAudiences(Collection<String> assertionAudiences) {
		this.assertionAudiences = assertionAudiences;
	}

	/**
	 * @param assertionLifetime the assertionLifetime to set
	 */
	public void setAssertionLifetime(long assertionLifetime) {
		this.assertionLifetime = assertionLifetime;
	}

	/**
	 * @param nameIdValue the nameIdValue to set
	 */
	public void setNameIdValue(String nameIdValue) {
		this.nameIdValue = nameIdValue;
	}

	/**
	 * @param proxyAudiences the proxyAudiences to set
	 */
	public void setProxyAudiences(Collection<String> proxyAudiences) {
		this.proxyAudiences = proxyAudiences;
	}

	/**
	 * @param proxyCount the proxyCount to set
	 */
	public void setProxyCount(int proxyCount) {
		this.proxyCount = proxyCount;
	}

	public Collection<String> getProxyAudiences() {
		// TODO Auto-generated method stub
		return proxyAudiences;
	}

	public int getProxyCount() {
		// TODO Auto-generated method stub
		return proxyCount;
	}

	public boolean signAssertion() {
		// TODO Auto-generated method stub
		return signAssertion;
	}

	public String getNameIdValue() {
		// TODO Auto-generated method stub
		return nameIdValue;
	}

	/**
	 * @return the signAssertion
	 */
	public boolean isSignAssertion() {
		return signAssertion;
	}

	/**
	 * @param signAssertion the signAssertion to set
	 */
	public void setSignAssertion(boolean signAssertion) {
		this.signAssertion = signAssertion;
	}
}
