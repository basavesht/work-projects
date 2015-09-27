package com.tcs.bancs.channels.integration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.w3c.dom.Document;

import com.tcs.bancs.ui.filters.security.saml.ContextDataExtractor;
import com.tcs.bancs.ui.filters.security.saml.SAMLAuthorizer;
import com.tcs.bancs.ui.helpers.Base64Util;
import com.tcs.bancs.ui.helpers.security.saml.SAMLValidationException;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.ui.helpers.xml.ContextDataParsingException;
import com.tcs.bancs.ui.helpers.xml.XMLUtils;


public class ExtractContextData implements ContextDataExtractor 
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SAMLAuthorizer.class);

	private ExtractorConfig config = null;
	public Object extract(List<AttributeStatement> asList ) throws SAMLValidationException {
		long start=0,end=0;
		if (logger.isDebugEnabled()) {
			logger.debug("extract(List<AttributeStatement>, String) - start"); //$NON-NLS-1$
		}
		try {
			config.checkNew();
		} catch (FileNotFoundException e) {
			throw SAMLValidationException.createInstance(e);
		} catch (ConfigXMLParsingException e) {
			throw SAMLValidationException.createInstance(e);
		}

		String base64EncodedConetxtData = null;

		// Extract context data
		boolean hasContextAttribute = false;

		if (asList == null || asList.size() == 0) {
			throw  SAMLValidationException.createInstance("No attribute statements present");
		} else {
			for (Iterator<AttributeStatement> it = asList.iterator(); it.hasNext();) {
				AttributeStatement as = it.next();
				List<Attribute> attList = as.getAttributes();
				if (attList == null || attList.size() == 0) {
					throw SAMLValidationException.createInstance("No attribute present");
				} else {
					for (Iterator<Attribute> it2 = attList.iterator(); it2.hasNext();) {
						Attribute att = it2.next();
						if (!att.getName().equals(config.getContextAttributeName())) {
							continue;
						} else {
							hasContextAttribute = true;
							base64EncodedConetxtData = att.getDOM().getFirstChild().getTextContent();
						}
					}
				}
			}
			if (!hasContextAttribute) {
				throw SAMLValidationException.createInstance(
						"Must have attribute '" + config.getContextAttributeName() + "'");
			}
		}

		byte[] contextData = null;
				
		Throwable ctxDatDecodeExp = null;
		try {
			contextData = Base64Util.decode(base64EncodedConetxtData);
		} catch (IOException e) {
			logger.error("extract(List<AttributeStatement>, String)", e); //$NON-NLS-1$

			contextData = null;
			ctxDatDecodeExp = e;
		}
		if(contextData == null)
		{
			throw  SAMLValidationException.createInstance("Base64 decoding of context data failed",ctxDatDecodeExp);
		}
		if( logger.isInfoEnabled()){
			logger.info(String.format("===== Context XML Received ===== %s %s" , System.getProperty("line.separator"), new String(contextData)));
		}
		Document contextDocument = null;
		try {
			contextDocument = XMLUtils.parse(contextData,config.getContextXSDFile());
		} catch (ContextDataParsingException e) {
			logger.error("extract(List<AttributeStatement>, String)", e); //$NON-NLS-1$
			throw  SAMLValidationException.createInstance("Context XML Parsing failed" , new String(contextData), e);
		}
		
		MMContext context = null;
		try {
			
			start = System.nanoTime();
			context = new MMContext(contextDocument);
			end = System.nanoTime();
            System.out.println("Extract Context Data - EPR:getAppRoles WS call time : %d ns" + Long.valueOf(end - start));

	
		} catch (ContextHasNoAccounts e) {
			logger.error("extract(List<AttributeStatement>, String)", e); //$NON-NLS-1$

			throw  SAMLValidationException.createInstance("Context XML has no account", new String(contextData), e);
		} catch (ContextDataParsingException e) {
			logger.error("extract(List<AttributeStatement>, String)", e); //$NON-NLS-1$

			throw  SAMLValidationException.createInstance("Context XML Parsing failed", new String(contextData),e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("extract(List<AttributeStatement>, String) - end"); //$NON-NLS-1$
		}
		return context;
	}

	public void init(String configFileName)
			throws ConfigXMLParsingException {
		if (logger.isDebugEnabled()) {
			logger.debug("init(String) - start"); //$NON-NLS-1$
		}
		config = ExtractorConfig.getInstance();

		try {
			config.parse(configFileName);
		} catch (FileNotFoundException e) {
			logger.error("init(String)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException(e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init(String) - end"); //$NON-NLS-1$
		}
	}

}
