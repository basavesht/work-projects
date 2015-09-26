package com.tcs.bancs.handler.factory;

import org.apache.log4j.Logger;

import com.tcs.bancs.config.handler.WorkflowHandlerConfig;
import com.tcs.bancs.config.handler.attributes.ResponseHandlerAttributes;
import com.tcs.bancs.config.handler.attributes.WSOperation;
import com.tcs.bancs.handler.response.ResponseHandler;
import com.tcs.bancs.helpers.ws.HandlerNotFoundException;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;

public class ResponseHandlerFactory 
{
	private static final Logger logger = Logger.getLogger(ResponseHandlerFactory.class);

	public static ResponseHandler getHandler(WorkflowHandlerConfig config,String operationName) throws ConfigXMLParsingException, HandlerNotFoundException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getHandler(WSHandlerConfig, String) - start");
		}

		assertConfig(config==null);
		ResponseHandler responseHandler = null;
		try 
		{
			for(WSOperation operation : config.getOperations())
			{
				if(operation.getOperation_name()!=null){
					if(operation.getOperation_name().equalsIgnoreCase(operationName))
					{
						ResponseHandlerAttributes handlerProps = operation.getResponseHandler();
						String responseHandlerClass = handlerProps.getHandlerImplClass();
						try 
						{
							//Creating an Response Handler class instance...
							Class classObj = Class.forName(responseHandlerClass);
							responseHandler = (ResponseHandler)classObj.newInstance();

							//Setting the Response Handler properties...
							responseHandler.setTransformer(handlerProps);
							break;
						} 
						catch (ClassNotFoundException e) {
							throw new HandlerNotFoundException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'RESPONSE_HANDLER'", e);
						} catch (IllegalAccessException e) {
							throw new HandlerNotFoundException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'RESPONSE_HANDLER'", e);
						} catch (InstantiationException e) {
							throw new HandlerNotFoundException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'RESPONSE_HANDLER'", e);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new HandlerNotFoundException("Response Handler Exception :", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getHandler(WSHandlerConfig, String) - end");
		}
		return responseHandler;
	}

	private static void assertConfig(boolean raiseError) throws ConfigXMLParsingException {
		if(raiseError) {
			throw new ConfigXMLParsingException("WSConfiguration is not initialized");
		}
	}
}
