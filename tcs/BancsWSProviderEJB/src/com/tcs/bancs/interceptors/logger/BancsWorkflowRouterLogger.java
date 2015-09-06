package com.tcs.bancs.interceptors.logger;

import java.util.Map;

import org.apache.log4j.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class BancsWorkflowRouterLogger
{
	private static final Logger logger = Logger.getLogger(BancsWorkflowRouterLogger.class);

	@AroundInvoke
	public Object logMethodParams(InvocationContext invocationContext) throws Exception 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("logMethodParams(InvocationContext) - start");
		}
		logger.debug("Entering method: "+ invocationContext.getMethod().getName());

		//Request Logging..
		Object[] inputParams = invocationContext.getParameters(); 
		for(Object input : inputParams){
			if(input instanceof Map<?,?>){
				Map<?,?> params = (Map<?,?>)input;
				for(Object param : params.values()) {
					logger.debug("Input Request Parameter :" + param.toString());
				}
			}
		}

		//Proceed Method execution..
		Object returnObject = invocationContext.proceed();

		//Response Logging..
		if(returnObject instanceof Map<?,?>){
			Map<?,?> params = (Map<?,?>)returnObject;
			for(Object param : params.values()) {
				logger.debug("Output Response Object :" + param.toString());
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("logMethodParams(InvocationContext) - end");
		}
		return returnObject;
	}
}
