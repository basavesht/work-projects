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

public class ExtractorConfig extends ObservableConfiguration {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ExtractorConfig.class);


		private String contextAttributeName;
		private String contextXSDFile;

		public void parseImpl(String configFileName) throws FileNotFoundException, ConfigXMLParsingException
		{
			InputStream is = null;
			try
			{
				is = new FileInputStream(configFileName);

				Document configDoc = XMLUtils.parse(is);

				NodeList nodes = configDoc.getFirstChild().getChildNodes();
				int nodes_length = nodes.getLength();
				for(int i = 0 ; i < nodes_length; i++)
				{
					Node child = nodes.item(i);
					
					if ( child.getNodeName().equals("CONTEXT_ATTRIBUTE_NAME"))
					{
						contextAttributeName = child.getTextContent();
					}				
					else if ( child.getNodeName().equals("CONTEXT_XSD_FILE"))
					{
						contextXSDFile = child.getTextContent();
					}

				}

				validateConfig();
			} catch (IOException e) {
			logger.error("ExtractorConfig(String)", e); //$NON-NLS-1$

				throw new ConfigXMLParsingException("IOException received.", e);		
			}
			finally
			{
				try {
					if ( is !=null ) is.close();
				} catch (IOException e) {
				logger.warn("ExtractorConfig(String) - exception ignored", e); //$NON-NLS-1$
				}
			}
		}
		private void validateConfig() throws ConfigXMLParsingException 
		{
		if (logger.isDebugEnabled()) {
			logger.debug("validateConfig() - start"); //$NON-NLS-1$
		}

			assertConfigForTag(contextAttributeName==null,"CONTEXT_ATTRIBUTE_NAME");
			assertConfigForTag(contextXSDFile==null,"CONTEXT_XSD_FILE");

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


		private static ExtractorConfig instance  = null;
		public static ExtractorConfig getInstance()
		{
		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String) - start"); //$NON-NLS-1$
		}

		if ( instance == null){
			instance = new ExtractorConfig();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String) - end"); //$NON-NLS-1$
		}
			return instance;
		}
		public String getContextAttributeName() {
			return contextAttributeName;
		}
		public String getContextXSDFile() {
			return contextXSDFile;
		}

}
