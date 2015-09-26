package com.tcs.bancs.handler.request.router.ejb;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import com.tcs.bancs.config.connector.WorkflowConnectorConfig;
import com.tcs.bancs.config.connector.attributes.EJBConnector;
import com.tcs.bancs.config.connector.attributes.IWorkflowConnector;
import com.tcs.bancs.gateway.iiop.ejb.BancsWorkflowRouter;
import com.tcs.bancs.handler.request.router.RequestRouter;
import com.tcs.bancs.handler.request.router.RequestRouterException;
import com.tcs.bancs.helpers.ejb.BancsWorkflowRouterBeanUtil;
import com.tcs.bancs.helpers.ejb.BancsWorkflowRouterException;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;

public class RequestEJBRouterImpl implements RequestRouter
{
	private static final Logger logger = Logger.getLogger(RequestEJBRouterImpl.class);
	private WorkflowConnectorConfig connectorConfig = null;
	private Map<String,Object> responseMap = null; 

	public Map<String, Object> routeRequest(Map<String, Object> request) throws RequestRouterException, ConfigXMLParsingException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("routeRequest(Map<String,Object>) - start"); 
		}

		try {
			connectorConfig.checkNew();
		} catch (FileNotFoundException e) {
			throw new ConfigXMLParsingException(e);
		} catch (ConfigXMLParsingException e) {
			throw new ConfigXMLParsingException(e);
		}

		List<IWorkflowConnector> connectors = connectorConfig.getConnectors();
		for(IWorkflowConnector connector : connectors)
		{
			if(connector instanceof EJBConnector) 
			{
				//EJB Connection Properties...
				String providerFactory = ((EJBConnector) connector).getEjbProvider();
				String providerURL = ((EJBConnector) connector).getEjbProviderUrl();
				String ejbJndi =((EJBConnector) connector).getEjbJndi();

				//EJB environment properties...
				Hashtable systemEnvt = new Hashtable();
				systemEnvt.put("java.naming.factory.initial", providerFactory);
				systemEnvt.put("java.naming.provider.url", providerURL);

				try {
					BancsWorkflowRouter bancsRouter = BancsWorkflowRouterBeanUtil.getBeanInstance(systemEnvt, ejbJndi);
					responseMap = bancsRouter.process(request);
				}
				catch(NamingException e){
					throw new RequestRouterException("JNDI Name : " + ejbJndi + "not found",e);
				}
				catch(BancsWorkflowRouterException e){
					throw new RequestRouterException("Exception while executing the bancs workflow",e);
				}
				catch(Exception e){
					throw new RequestRouterException("Internal EJB invocation exception",e);
				}
				break;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("routeRequest(Map<String,Object>) - end"); 
		}
		return responseMap;
	}	

	public void init(String configFileName) throws ConfigXMLParsingException  {
		connectorConfig = WorkflowConnectorConfig.getInstance();
		try {
			connectorConfig.parse(configFileName);
		} catch (FileNotFoundException e) {
			throw new ConfigXMLParsingException(e);
		}
	}
}
