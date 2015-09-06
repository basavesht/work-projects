package com.tcs.bancs.helpers.ejb;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.Map;

import com.tcs.bancs.config.executor.WorkflowExecutorConfig;
import com.tcs.bancs.config.executor.attributes.ServiceHandlerAttributes;
import com.tcs.bancs.helpers.request.InvalidRequestException;
import com.tcs.bancs.helpers.service.ServiceNotFoundException;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;

public class BancsWorkflowRouterBeanUtil
{
	private static final Logger logger = Logger.getLogger(BancsWorkflowRouterBeanUtil.class);

	public static WorkflowExecutorConfig init(String configFileName) throws ConfigXMLParsingException  {
		WorkflowExecutorConfig executorConfig = WorkflowExecutorConfig.getInstance();
		try {
			executorConfig.parse(configFileName);
		} catch (FileNotFoundException e) {
			throw new ConfigXMLParsingException("Config File could not be found",e);
		}
		return executorConfig;
	}

	public static ServiceHandlerAttributes getServiceHandler(WorkflowExecutorConfig config, Map<String,Object> request) throws ServiceNotFoundException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getServiceHandler(WorkflowExecutorConfig, Map<String,Object>) - start"); 
		}

		ServiceHandlerAttributes eventHandler = null;
		String serviceId = (String)request.get("SERVICE_ID");
		logger.debug("Service Id :" + serviceId); 
		try {
			for(ServiceHandlerAttributes handler : config.getServiceHandlers()){
				if(handler.getService_id().equalsIgnoreCase(serviceId)){
					eventHandler = handler;
				}
			}
		}
		catch(Exception e) {
			throw new ServiceNotFoundException("Service workflow handler could not be found",e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getServiceHandler(WorkflowExecutorConfig, Map<String,Object>) - end"); 
		}

		return eventHandler;
	}

	public static void validateRequest(Map<String,Object> request) throws InvalidRequestException {		
		assertRequestKey(request==null,"REQUEST_OBJECT");
		assertRequestKey(request.get("SERVICE_ID")==null,"SERVICE_ID");
		assertRequestKey(request.get("SERVICE_PARAMS")==null,"SERVICE_PARAMS");
	}

	public static void checkConfig(WorkflowExecutorConfig config,String configFile) throws ConfigXMLParsingException 
	{
		if(config == null) {
			config = WorkflowExecutorConfig.getInstance();
			try {
				config.parse(configFile);
			} catch (FileNotFoundException e) {
				throw new ConfigXMLParsingException("WebService Executor config file not found ..",e);
			} catch (ConfigXMLParsingException e) {
				throw new ConfigXMLParsingException("WebService Executor config file(XML) parsing exception ..",e);
			} 
		}
		else {
			try {
				config.checkNew();
			} catch (FileNotFoundException e) {
				throw new ConfigXMLParsingException("WebService Executor config file not found ..",e);
			} catch (ConfigXMLParsingException e) {
				throw new ConfigXMLParsingException("WebService Executor config file(XML) parsing exception ..",e);
			} 
		}
	}

	public static void assertRequestKey(boolean raiseError,String key) throws InvalidRequestException {		
		if(raiseError) {
			throw new InvalidRequestException("Request Key " + key + " not found in the input parameter");
		}
	}
}