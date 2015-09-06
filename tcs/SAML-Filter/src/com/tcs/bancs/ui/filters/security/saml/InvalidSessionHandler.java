package com.tcs.bancs.ui.filters.security.saml;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;

public abstract class InvalidSessionHandler 
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(InvalidSessionHandler.class);

	public void handle(HttpServletRequest httpRequest,HttpServletResponse httpResponse,String idp_url) throws IOException, ConfigXMLParsingException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("handle(HttpServletRequest, HttpServletResponse, String) - start"); //$NON-NLS-1$
		}

		httpResponse.setStatus(202);

		String html = getHTML(httpRequest,idp_url);
		httpResponse.getWriter().print(html);
		httpResponse.getWriter().flush();
		

		if (logger.isDebugEnabled()) {
			logger.debug("handle(HttpServletRequest, HttpServletResponse, String) - end"); //$NON-NLS-1$
		}
	}

	public abstract String getHTML(HttpServletRequest httpRequest,String idp_url) throws ConfigXMLParsingException;

	public abstract void init(String invalidSessionHandlerInitParam) throws ConfigXMLParsingException; 

}
