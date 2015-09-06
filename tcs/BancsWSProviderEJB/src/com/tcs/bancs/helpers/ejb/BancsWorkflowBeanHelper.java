package com.tcs.bancs.helpers.ejb;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.tcs.bancs.config.executor.WorkflowExecutorConfig;
import com.tcs.bancs.config.executor.attributes.ServiceHandlerAttributes;
import com.tcs.bancs.config.executor.attributes.SrvcMethod;
import com.tcs.bancs.helpers.service.ServiceNotFoundException;
import com.tcs.ebw.serverside.factory.IEBWService;

public class BancsWorkflowBeanHelper 
{
	private static final Logger logger = Logger.getLogger(BancsWorkflowBeanHelper.class);

	private transient Connection connection = null;
	private ServiceHandlerAttributes serviceHandler = null;
	private Map<String,Object> returnMap = new HashMap<String,Object>(); 
	private Object returnObject = null; 

	public Map<String,Object> executeWorkflow(WorkflowExecutorConfig config, Map<String,Object> requestParams) throws BancsWorkflowRouterException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("executeWorkflow(WorkflowExecutorConfig, Map<String,Object>) - start"); 
		}

		try {
			serviceHandler = BancsWorkflowRouterBeanUtil.getServiceHandler(config, requestParams);
		} catch (ServiceNotFoundException e) {
			logger.error("executeWorkflow(WorkflowExecutorConfig, Map<String,Object>)", e); 
			throw new BancsWorkflowRouterException("Service Handler could not be found..",e);
		}

		//Creating an Request Validation class instance...
		String serviceImplClass = serviceHandler.getService_class();
		SrvcMethod serviceMethod = serviceHandler.getService_method();
		Class[] methodParams = serviceMethod.getMethodParams();
		Object[] serviceParams = (Object[]) requestParams.get("SERVICE_PARAMS");
		try 
		{
			//Instantiate service class...
			IEBWService srvcClass = (IEBWService)Class.forName(serviceImplClass).newInstance();
			srvcClass.setConnection(connection);

			//Obtain & Invoke service method...
			Method srvcMethod = Class.forName(serviceImplClass).getDeclaredMethod(serviceMethod.getName(),methodParams);
			returnObject = srvcMethod.invoke(srvcClass, serviceParams);	

			if(returnObject!=null){
				returnMap.put("BusinessResponse", returnObject);
			}
		} 
		catch (InstantiationException e) {
			throw new BancsWorkflowRouterException("InstantiationException",e);
		} catch (IllegalAccessException e) {
			throw new BancsWorkflowRouterException("IllegalAccessException",e);
		} catch (ClassNotFoundException e) {
			throw new BancsWorkflowRouterException("ClassNotFoundException",e);
		} catch (SecurityException e) {
			throw new BancsWorkflowRouterException("SecurityException",e);
		} catch (NoSuchMethodException e) {
			throw new BancsWorkflowRouterException("NoSuchMethodException",e);
		} catch (IllegalArgumentException e) {
			throw new BancsWorkflowRouterException("IllegalArgumentException",e);
		} catch (InvocationTargetException e) {
			throw new BancsWorkflowRouterException("InvocationTargetException",e);
		} catch (Exception e) {
			throw new BancsWorkflowRouterException("Exception",e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("executeWorkflow(WorkflowExecutorConfig, Map<String,Object>) - end"); 
		}
		return returnMap;
	}

	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
