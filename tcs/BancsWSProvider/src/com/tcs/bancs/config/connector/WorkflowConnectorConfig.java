package com.tcs.bancs.config.connector;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.tcs.bancs.config.connector.attributes.EJBConnector;
import com.tcs.bancs.config.connector.attributes.IWorkflowConnector;
import com.tcs.bancs.helpers.ObservableConfiguration;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.helpers.xml.XMLUtils;

public class WorkflowConnectorConfig extends ObservableConfiguration  
{
	private static final Logger logger = Logger.getLogger(WorkflowConnectorConfig.class);
	private List<IWorkflowConnector> connectors =  new ArrayList<IWorkflowConnector>();
	private static WorkflowConnectorConfig instance  = null;

	//Singleton class..
	private WorkflowConnectorConfig(){

	}

	//Single Instance creation..
	public static WorkflowConnectorConfig getInstance() 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String) - start");
		}

		if ( instance == null){
			instance = new WorkflowConnectorConfig();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String) - end");
		}
		return instance;
	}

	@Override
	public void parseImpl(String configFileName) throws FileNotFoundException,ConfigXMLParsingException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("parseImpl(String) - start");
		}

		InputStream is = null;
		try
		{
			is = new FileInputStream(configFileName);
			Document configDoc = XMLUtils.parse(is);
			NodeList nodes = configDoc.getElementsByTagName("WORKFLOW_CONNECTOR_CONFIG").item(0).getChildNodes();
			int nodes_length = nodes.getLength();
			for(int i = 0 ; i < nodes_length; i++)
			{
				Node child = nodes.item(i);
				if(child.getNodeName().equals("EJB_CONNECTOR"))
				{
					EJBConnector ejbConnectorProps = new EJBConnector();

					//EJB Provider ..
					String ejbProvider = XMLUtils.getAttribute(child, "PROVIDER");
					assertConfigForAttribute(ejbProvider == null, "EJB_CONNECTOR", "PROVIDER");
					ejbConnectorProps.setEjbProvider(ejbProvider);

					//EJB Provider URL ..
					String ejbProviderUrl = XMLUtils.getAttribute(child, "URL");
					assertConfigForAttribute(ejbProviderUrl == null, "EJB_CONNECTOR", "URL");
					ejbConnectorProps.setEjbProviderUrl(ejbProviderUrl);

					//EJB JNDI ...
					String ejbJndi = XMLUtils.getAttribute(child, "JNDI");
					assertConfigForAttribute(ejbJndi == null, "EJB_CONNECTOR", "JNDI");
					ejbConnectorProps.setEjbJndi(ejbJndi);

					connectors.add((IWorkflowConnector)ejbConnectorProps);
				}
			}
		} catch (IOException e) {
			throw new ConfigXMLParsingException("IOException received.", e);		
		}
		finally
		{
			try {
				if ( is !=null ) is.close();
			} catch (IOException e) {
				logger.warn("WorkflowConnectorConfig(String) - exception ignored", e);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("parseImpl(String) - end"); 
		}
	}

	private void assertConfigForAttribute(boolean raiseError, String tagName, String attributeName) throws ConfigXMLParsingException{
		if(raiseError) {
			throw new ConfigXMLParsingException("XML attribute '" + attributeName + "' must be specified for tag '" + tagName +"' in config XML");
		}
	}

	public List<IWorkflowConnector> getConnectors() {
		return connectors;
	}

	public void setConnectors(List<IWorkflowConnector> connectors) {
		this.connectors = connectors;
	}

	public static void main(String args[]) throws FileNotFoundException, ConfigXMLParsingException{
		WorkflowConnectorConfig config = WorkflowConnectorConfig.getInstance();
		config.parseImpl("./WEB-INF/workflow-connector.xml");
	}
}

