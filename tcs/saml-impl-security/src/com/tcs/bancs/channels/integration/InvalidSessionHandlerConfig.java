package com.tcs.bancs.channels.integration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tcs.bancs.ui.helpers.ObservableConfiguration;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.ui.helpers.xml.XMLUtils;

public class InvalidSessionHandlerConfig extends ObservableConfiguration{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(InvalidSessionHandlerConfig.class);


		private HashMap<String, String> pageIdMap;
		private String defaultPageId ;
		private String domain;
		private String postAttrToken;
		private String defaultTokenId;

		public void parseImpl(String configFileName) throws FileNotFoundException, ConfigXMLParsingException
		{
			InputStream is = null;
			try
			{
				is = new FileInputStream(configFileName);

				Document configDoc = XMLUtils.parse(is);

				NodeList nodes = configDoc.getElementsByTagName("INVALID_SESSION_HANDLER").item(0).getChildNodes();
				int nodes_length = nodes.getLength();
				for(int i = 0 ; i < nodes_length; i++)
				{
					Node child = nodes.item(i);
					
					if ( child.getNodeName().equals("CS_TO_MM_PAGEMAP"))
					{
						defaultPageId = XMLUtils.getAttribute(child,"defaultPageId");
						NodeList pageMapNodes =  child.getChildNodes();
						pageIdMap = new HashMap<String, String>();
						int pageMapNodes_length = pageMapNodes.getLength();
						for(int j = 0 ; j < pageMapNodes_length; j++)
						{
							Node pageMap = pageMapNodes.item(j);
							if(pageMap.getNodeName().equals("PAGE_MAP"))
							{
								String mm_page_id = XMLUtils.getAttribute(pageMap,"MMPageId");
								assertConfigForAttribute( mm_page_id == null,"PAGE_MAP", "MMPageId");
								String cs_uri = XMLUtils.getAttribute(pageMap,"ClientServURI");
								assertConfigForAttribute( cs_uri == null,"PAGE_MAP", "ClientServURI");
								pageIdMap.put(cs_uri, mm_page_id);
							}
						}
					}
					else if ( child.getNodeName().equals("DOMAIN"))
					{
						domain = child.getTextContent();
					}
					else if ( child.getNodeName().equals("TOKEN_POST_ATTRIBUTE_NAME"))
					{
						postAttrToken = child.getTextContent();
					}
					else if ( child.getNodeName().equals("DEFAULT_TOKEN_ID"))
					{
						defaultTokenId = child.getTextContent();
					}

				}

				validateConfig();
			} catch (IOException e) {
			logger.error("InvalidSessionHandlerConfig(String)", e); //$NON-NLS-1$

				throw new ConfigXMLParsingException("IOException received.", e);		
			}
			finally
			{
				try {
					if ( is !=null ) is.close();
				} catch (IOException e) {
				logger.warn("InvalidSessionHandlerConfig(String) - exception ignored", e); //$NON-NLS-1$
				}
			}
		}
		/**
		 * @return the pageIdMap
		 */
		public HashMap<String, String> getPageIdMap() {
			return pageIdMap;
		}
		private void validateConfig() throws ConfigXMLParsingException 
		{
		if (logger.isDebugEnabled()) {
			logger.debug("validateConfig() - start"); //$NON-NLS-1$
		}

			assertConfigForAttribute(defaultPageId==null,"CS_TO_MM_PAGEMAP","defaultPageId");
			assertConfigForTag(domain==null,"DOMAIN");
			assertConfigForTag(postAttrToken==null,"TOKEN_POST_ATTRIBUTE_NAME");
			assertConfigForTag(defaultTokenId==null,"DEFAULT_TOKEN_ID");

		if (logger.isDebugEnabled()) {
			logger.debug("validateConfig() - end"); //$NON-NLS-1$
		}
		}
		

		private void assertConfigForAttribute(boolean raiseError, String tagName, String attributeName) throws ConfigXMLParsingException {
		if (logger.isDebugEnabled()) {
			logger.debug("assertConfigForAttribute(boolean, String, String) - start"); //$NON-NLS-1$
		}

			if(raiseError) throw new ConfigXMLParsingException("XML attribute '" + attributeName + "' must be specified for tag '" + tagName +"' in config XML");

			if (logger.isDebugEnabled()) {
			logger.debug("assertConfigForAttribute(boolean, String, String) - end"); //$NON-NLS-1$
		}
		}

		private void assertConfigForTag(boolean raiseError, String tagName) throws ConfigXMLParsingException {
			if (logger.isDebugEnabled()) {
				logger.debug("assertConfigForTag(boolean, String, String) - start"); //$NON-NLS-1$
			}

				if(raiseError) throw new ConfigXMLParsingException("XML tag '" + tagName + "' must be specified.");

				if (logger.isDebugEnabled()) {
				logger.debug("assertConfigForTag(boolean, String, String) - end"); //$NON-NLS-1$
			}
			}

		private static InvalidSessionHandlerConfig instance  = null;
		public static InvalidSessionHandlerConfig getInstance() 
		{
		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String) - start"); //$NON-NLS-1$
		}

		if ( instance == null){
			instance = new InvalidSessionHandlerConfig();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String) - end"); //$NON-NLS-1$
		}
			return instance;
		}
		public String getDefaultPageId() {
			return defaultPageId;
		}
		public String getDomain() {
			return domain;
		}
		public String getDefaultTokenId() {
			return defaultTokenId;
		}
		public String getPostAttrToken() {
			return postAttrToken;
		}
}
