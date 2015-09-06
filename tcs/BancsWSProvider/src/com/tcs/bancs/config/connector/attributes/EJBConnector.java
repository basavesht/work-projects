package com.tcs.bancs.config.connector.attributes;

public class EJBConnector implements IWorkflowConnector
{
	private String ejbProvider = null;
	private String ejbProviderUrl = null;
	private String ejbJndi = null;

	public String getEjbProvider() {
		return ejbProvider;
	}
	public void setEjbProvider(String ejbProvider) {
		this.ejbProvider = ejbProvider;
	}
	public String getEjbProviderUrl() {
		return ejbProviderUrl;
	}
	public void setEjbProviderUrl(String ejbProviderUrl) {
		this.ejbProviderUrl = ejbProviderUrl;
	}
	public String getEjbJndi() {
		return ejbJndi;
	}
	public void setEjbJndi(String ejbJndi) {
		this.ejbJndi = ejbJndi;
	}
}
