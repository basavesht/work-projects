package com.tcs.ebw.transferobject;

import java.io.Serializable;

import com.tcs.ebw.transferobject.EBWTransferObject;

public class SecurityCheckTO extends EBWTransferObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8607672569223305799L;
	private String uuid = null;
	private String sessionId =null;
	private String securityTokenId =null;
	
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
	 * @return the securityTokenId
	 */
	public String getSecurityTokenId() {
		return securityTokenId;
	}
	/**
	 * @param securityTokenId the securityTokenId to set
	 */
	public void setSecurityTokenId(String securityTokenId) {
		this.securityTokenId = securityTokenId;
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
	    
	    retValue = "SecurityCheckTO ( "
	        + super.toString() + TAB
	        + "uuid = " + this.uuid + TAB
	        + "sessionId = " + this.sessionId + TAB
	        + "securityTokenId = " + this.securityTokenId + TAB
	        + " )";
	
	    return retValue;
	}

}
