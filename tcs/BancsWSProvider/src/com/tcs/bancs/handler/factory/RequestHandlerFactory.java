package com.tcs.bancs.handler.factory;

import java.util.List;

import org.apache.log4j.Logger;

import com.tcs.bancs.config.handler.WorkflowHandlerConfig;
import com.tcs.bancs.config.handler.attributes.RequestHandlerAttributes;
import com.tcs.bancs.config.handler.attributes.WSOperation;
import com.tcs.bancs.handler.request.RequestHandler;
import com.tcs.bancs.handler.request.router.RequestRouter;
import com.tcs.bancs.helpers.ws.HandlerNotFoundException;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;

public class RequestHandlerFactory
{
	private static final Logger logger = Logger.getLogger(RequestHandlerFactory.class);

	public static RequestHandler getHandler(WorkflowHandlerConfig config,String operationName) throws ConfigXMLParsingException,HandlerNotFoundException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getHandler(WSHandlerConfig, String) - start");
		}

		assertConfig(config==null);
		RequestHandler requestHandler = null;
		List<RequestRouter> routers = config.getRouters();
		try 
		{
			for(WSOperation operation : config.getOperations())
			{
				if(operation.getOperation_name()!=null){
					if(operation.getOperation_name().equalsIgnoreCase(operationName))
					{
						RequestHandlerAttributes handlerProps = operation.getRequestHandler();
						String requestHandlerClass = handlerProps.getHandlerImplClass();
						try 
						{
							//Creating an Request Handler class instance...
							Class classObj = Class.forName(requestHandlerClass);
							requestHandler = (RequestHandler)classObj.newInstance();

							//Setting the request handler properties ..
							requestHandler.setValidator(handlerProps);
							requestHandler.setTransformer(handlerProps);
							requestHandler.setRouter(handlerProps,routers);
							requestHandler.setOperationId(operation);
							requestHandler.setOperationName(operation);
							break;
						} 
						catch (ClassNotFoundException e) {
							throw new HandlerNotFoundException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'REQUEST_HANDLER'", e);
						} catch (IllegalAccessException e) {
							throw new HandlerNotFoundException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'REQUEST_HANDLER'", e);
						} catch (InstantiationException e) {
							throw new HandlerNotFoundException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'REQUEST_HANDLER'", e);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new HandlerNotFoundException("Request Handler Exception :", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getHandler(WSHandlerConfig, String) - end");
		}
		return requestHandler;
	}

	private static void assertConfig(boolean raiseError) throws ConfigXMLParsingException {
		if(raiseError) {
			throw new ConfigXMLParsingException("WSConfiguration is not initialized");
		}
	}
}
