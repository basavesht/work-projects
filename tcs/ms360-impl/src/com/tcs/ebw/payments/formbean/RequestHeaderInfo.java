/**
 * 
 */
package com.tcs.ebw.payments.formbean;

import com.tcs.ebw.mvc.validator.EbwForm;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class RequestHeaderInfo extends EbwForm implements java.io.Serializable 
{	
	private String HTTP_ACCEPT;
	private String HTTP_ACCEPT_CHARS;
	private String HTTP_ACCEPT_ENCODING;
	private String HTTP_ACCEPT_LANG;
	private String HTTP_REFERRER;
	private String USER_AGENT;
	private String IP_ADDRESS;

	public String getHTTP_ACCEPT() {
		return HTTP_ACCEPT;
	}
	public void setHTTP_ACCEPT(String http_accept) {
		HTTP_ACCEPT = http_accept;
	}
	public String getHTTP_ACCEPT_CHARS() {
		return HTTP_ACCEPT_CHARS;
	}
	public void setHTTP_ACCEPT_CHARS(String http_accept_chars) {
		HTTP_ACCEPT_CHARS = http_accept_chars;
	}
	public String getHTTP_ACCEPT_ENCODING() {
		return HTTP_ACCEPT_ENCODING;
	}
	public void setHTTP_ACCEPT_ENCODING(String http_accept_encoding) {
		HTTP_ACCEPT_ENCODING = http_accept_encoding;
	}
	public String getHTTP_ACCEPT_LANG() {
		return HTTP_ACCEPT_LANG;
	}
	public void setHTTP_ACCEPT_LANG(String http_accept_lang) {
		HTTP_ACCEPT_LANG = http_accept_lang;
	}
	public String getHTTP_REFERRER() {
		return HTTP_REFERRER;
	}
	public void setHTTP_REFERRER(String http_referrer) {
		HTTP_REFERRER = http_referrer;
	}
	public String getUSER_AGENT() {
		return USER_AGENT;
	}
	public void setUSER_AGENT(String user_agent) {
		USER_AGENT = user_agent;
	}
	public String getIP_ADDRESS() {
		return IP_ADDRESS;
	}
	public void setIP_ADDRESS(String ip_address) {
		IP_ADDRESS = ip_address;
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
		retValue = "RequestHeaderInfo ( "
			+ super.toString() + TAB
			+ "HTTP_ACCEPT = " + this.HTTP_ACCEPT + TAB
			+ "HTTP_ACCEPT_CHARS = " + this.HTTP_ACCEPT_CHARS + TAB
			+ "HTTP_ACCEPT_ENCODING = " + this.HTTP_ACCEPT_ENCODING + TAB
			+ "HTTP_ACCEPT_LANG = " + this.HTTP_ACCEPT_LANG + TAB
			+ "HTTP_REFERRER = " + this.HTTP_REFERRER + TAB
			+ "USER_AGENT = " + this.USER_AGENT + TAB
			+ "IP_ADDRESS = " + this.IP_ADDRESS + TAB
			+ " )";

		return retValue;
	}


}
