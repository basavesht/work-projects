package com.tcs.bancs.channels.integration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tcs.bancs.ui.helpers.ObservableConfiguration;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.ui.helpers.xml.XMLUtils;

public class SAMLPostHookConfig extends ObservableConfiguration{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ErrorHandlerConfig.class);


	private Map<String,String> pageNameMapping;
	private String ejbProvider;
	private String ejbProviderUrl;
	private String ejbJndi;

	public void parseImpl(String configFileName) throws FileNotFoundException, ConfigXMLParsingException
	{
		InputStream is = null;
		try
		{
			pageNameMapping = new HashMap<String, String>();

			is = new FileInputStream(configFileName);

			Document configDoc = XMLUtils.parse(is);

			NodeList nodes = configDoc.getElementsByTagName("POST_SAML").item(0).getChildNodes();
			int nodes_length = nodes.getLength();
			for(int i = 0 ; i < nodes_length; i++)
			{
				Node child = nodes.item(i);

				if ( child.getNodeName().equals("URI_TO_PAGE"))
				{
					NodeList pageMapNodes =  child.getChildNodes();
					int pageMapNodes_length = pageMapNodes.getLength();
					for(int j = 0 ; j < pageMapNodes_length; j++)
					{
						Node pageMap = pageMapNodes.item(j);
						if(pageMap.getNodeName().equals("PAGE_MAP"))
						{
							String uri = XMLUtils.getAttribute(pageMap,"URI");
							assertConfigForAttribute(uri==null,"PAGE_MAP", "URI");
							String page = XMLUtils.getAttribute(pageMap,"Page");
							assertConfigForAttribute(uri==null,"PAGE_MAP", "pAGE");
							pageNameMapping.put(uri, page);
						}
					}
				}else if(child.getNodeName().equals("EJB_CONNECTION")){
					ejbProvider = XMLUtils.getAttribute(child, "PROVIDER");
					assertConfigForAttribute(ejbProvider == null, "EJB_CONNECTION", "PROVIDER");
					ejbProviderUrl = XMLUtils.getAttribute(child, "URL");
					assertConfigForAttribute(ejbProviderUrl == null, "EJB_CONNECTION", "URL");
					ejbJndi = XMLUtils.getAttribute(child, "JNDI");
					assertConfigForAttribute(ejbJndi == null, "EJB_CONNECTION", "JNDI");
				}
			}

			validateConfig();
		} catch (IOException e) {
			logger.error("SAMLPostHookConfig(String)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException("IOException received.", e);		
		}
		finally
		{
			try {
				if ( is !=null ) is.close();
			} catch (IOException e) {
				logger.warn("SAMLPostHookConfig(String) - exception ignored", e); //$NON-NLS-1$
			}
		}
	}
	private void validateConfig() throws ConfigXMLParsingException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("validateConfig() - start"); //$NON-NLS-1$
		}

        if(ejbProvider == null || ejbProviderUrl == null || ejbJndi == null)
            throw new ConfigXMLParsingException("XML tag 'EJB_CONNECTION' must be specified in config XML");


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


	private static SAMLPostHookConfig instance  = null;
	public static SAMLPostHookConfig getInstance() 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String) - start"); //$NON-NLS-1$
		}

		if ( instance == null){
			instance = new SAMLPostHookConfig();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String) - end"); //$NON-NLS-1$
		}
		return instance;
	}
	public Map<String, String> getPageNameMapping() {
		return pageNameMapping;
	}
	public void setPageNameMapping(Map<String, String> pageNameMapping) {
		this.pageNameMapping = pageNameMapping;
	}

	public String getEjbJndi()
	{
		return ejbJndi;
	}

	public String getEjbProvider()
	{
		return ejbProvider;
	}

	public String getEjbProviderUrl()
	{
		return ejbProviderUrl;
	}

}
