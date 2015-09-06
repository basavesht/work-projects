package com.tcs.bancs.channels.integration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tcs.bancs.ui.helpers.ObservableConfiguration;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.ui.helpers.xml.XMLUtils;

public class ErrorHandlerConfig extends ObservableConfiguration{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ErrorHandlerConfig.class);


	private boolean showTrace = true;
	private String postAttrToken;
	private String defaultTokenId;
	private String invalidSessionHandlerConfigFile;

	public void parseImpl(String configFileName) throws FileNotFoundException, ConfigXMLParsingException
	{
		InputStream is = null;
		try
		{
			is = new FileInputStream(configFileName);

			Document configDoc = XMLUtils.parse(is);

			NodeList nodes = configDoc.getElementsByTagName("ERROR_HANDLER").item(0).getChildNodes();
			int nodes_length = nodes.getLength();
			for(int i = 0 ; i < nodes_length; i++)
			{
				Node child = nodes.item(i);

				if ( child.getNodeName().equals("SHOW_ERROR_TRACE_ON_ERROR"))
				{
					showTrace = Boolean.valueOf(child.getTextContent());
				}				
				else if ( child.getNodeName().equals("TOKEN_POST_ATTRIBUTE_NAME"))
				{
					postAttrToken = child.getTextContent();
				}
				else if ( child.getNodeName().equals("DEFAULT_TOKEN_ID"))
				{
					defaultTokenId = child.getTextContent();
				}
				else if ( child.getNodeName().equals("INVALID_SESSION_HANDLER_CONFIG"))
				{
					invalidSessionHandlerConfigFile = child.getTextContent();
				}

			}

			validateConfig();
		} catch (IOException e) {
			logger.error("ErrorHandlerConfig(String)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException("IOException received.", e);		
		}
		finally
		{
			try {
				if ( is !=null ) is.close();
			} catch (IOException e) {
				logger.warn("ErrorHandlerConfig(String) - exception ignored", e); //$NON-NLS-1$
			}
		}
	}
	private void validateConfig() throws ConfigXMLParsingException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("validateConfig() - start"); //$NON-NLS-1$
		}

		assertConfigForTag(postAttrToken==null,"TOKEN_POST_ATTRIBUTE_NAME");
		assertConfigForTag(defaultTokenId==null,"DEFAULT_TOKEN_ID");
		assertConfigForTag(invalidSessionHandlerConfigFile==null,"INVALID_SESSION_HANDLER_CONFIG");

		if (logger.isDebugEnabled()) {
			logger.debug("validateConfig() - end"); //$NON-NLS-1$
		}
	}
	private void assertConfigForTag(boolean raiseError, String tagName) throws ConfigXMLParsingException {
		if (logger.isDebugEnabled()) {
			logger.debug("assertConfigForTag(boolean, String) - start"); //$NON-NLS-1$
		}

		if(raiseError) throw new ConfigXMLParsingException("XML tag '" + tagName + "' must be specified in config XML");

		if (logger.isDebugEnabled()) {
			logger.debug("assertConfigForTag(boolean, String) - end"); //$NON-NLS-1$
		}
	}


	private static ErrorHandlerConfig instance  = null;
	public static ErrorHandlerConfig getInstance() 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String) - start"); //$NON-NLS-1$
		}

		if ( instance == null){
			instance = new ErrorHandlerConfig();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String) - end"); //$NON-NLS-1$
		}
		return instance;
	}
	public String getPostAttrToken() {
		return postAttrToken;
	}
	public boolean isShowTrace() {
		return showTrace;
	}
	public String getDefaultTokenId() {
		return defaultTokenId;
	}
	public String getInvalidSessionHandlerConfigFile() {
		return invalidSessionHandlerConfigFile;
	}
}
