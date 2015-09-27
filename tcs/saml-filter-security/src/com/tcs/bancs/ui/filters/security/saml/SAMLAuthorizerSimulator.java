
package com.tcs.bancs.ui.filters.security.saml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tcs.bancs.ui.helpers.Base64Util;
import com.tcs.bancs.ui.helpers.security.saml.VerifySignatureType;


public class SAMLAuthorizerSimulator extends SAMLAuthorizer {


	private static final Logger logger = Logger.getLogger(SAMLAuthorizerSimulator.class);

	/* (non-Java-doc)
	 * @see javax.servlet.Filter#initialize(FilterConfig arg0)
	 */
	@Override
	public void initialize(FilterConfig config) throws FilterConfigException {
		if (logger.isDebugEnabled()) {
			logger.debug("initialize(FilterConfig) - start"); //$NON-NLS-1$
		}
		
	

		String contextFileName = config.getInitParameter("contextXML");
		
		
		if ( contextFileName == null)
			throw new FilterConfigException("Init parameter 'contextXML' must be specified for Servlet Filter 'SAMLAuthorizer'.");


		super.initialize(config);
		
		samlConfig.setSimulated(true);
		
		samlConfig.setSimulatedContext(contextFileName);
		
		samlConfig.setVerifySignature(VerifySignatureType.never);
		
		samlConfig.setErrorHandler(new DefaultErrorHandler());


		if (logger.isDebugEnabled()) {
			logger.debug("initialize(FilterConfig) - end"); //$NON-NLS-1$
		}
	}
}