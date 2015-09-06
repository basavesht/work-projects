package com.tcs.bancs.ui.filters.security.saml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;

public interface PostCallInterface {

	public void init(String configFileName) throws ConfigXMLParsingException; ;
	boolean call(HttpServletRequest httpRequest,  HttpServletResponse httpResponse, Object context, SAMLConfiguration samlConfig) throws ConfigXMLParsingException;

}
