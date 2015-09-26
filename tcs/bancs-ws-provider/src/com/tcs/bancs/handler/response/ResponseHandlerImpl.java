package com.tcs.bancs.handler.response;

import org.apache.log4j.Logger;

import java.util.Map;

import com.tcs.bancs.config.handler.attributes.ResponseHandlerAttributes;
import com.tcs.bancs.handler.response.transformer.ResponseTransformationException;
import com.tcs.bancs.handler.response.transformer.ResponseTransformer;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;

public class ResponseHandlerImpl implements ResponseHandler 
{
	private static final Logger logger = Logger.getLogger(ResponseHandlerImpl.class);
	private Map<String,String> transformerProps = null;
	private Object serviceResponse = null;

	public Object processResponse(Map<String, Object> businessResponse) throws ResponseHandlerException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("processResponse(Map<String,Object>) - start"); 
		}

		try {
			transformRequest(businessResponse);
		} catch (ConfigXMLParsingException e) {
			throw new ResponseHandlerException("Error with the Response Handler configuration",e);
		} catch (ResponseTransformationException e) {
			throw new ResponseHandlerException("Web service request transformation failed",e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("processResponse(Map<String,Object>) - end"); 
		}
		return serviceResponse;
	}

	/**
	 * Transforming business response into web service response..
	 * @param response
	 * @throws ResponseTransformationException
	 * @throws ConfigXMLParsingException
	 */
	public void transformRequest(Map<String, Object> businessResponse) throws ResponseTransformationException, ConfigXMLParsingException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("transformRequest(Map<String,Object>) - start"); 
		}

		ResponseTransformer responseTransformer = null;
		assertHandler(transformerProps==null,"RESPONSE_TRANSFORMER");

		String isTransformerRequired = (String)transformerProps.get("REQUIRED");
		if(isTransformerRequired.equalsIgnoreCase("true"))
		{
			//Creating an Request Transformer class instance...
			String transformerImplClass = transformerProps.get("TRANSFORMER_IMPL_CLASS");
			try {
				Class classObj = Class.forName(transformerImplClass);
				responseTransformer = (ResponseTransformer)classObj.newInstance();
			} catch (ClassNotFoundException e) {
				throw new ResponseTransformationException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'REQUEST_TRANSFORMER'", e);
			} catch (IllegalAccessException e) {
				throw new ResponseTransformationException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'REQUEST_TRANSFORMER'", e);
			} catch (InstantiationException e) {
				throw new ResponseTransformationException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'REQUEST_TRANSFORMER'", e);
			}

			//Transforming businessResponse into web service response..
			try {
				serviceResponse = responseTransformer.transformResponse(businessResponse);
			} catch (Exception e) {
				throw new ResponseTransformationException("Exception occured while wrapping/transforming request into business request ..", e);
			}
		}
		else {
			serviceResponse = businessResponse;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("transformRequest(Map<String,Object>) - end"); 
		}
	}

	public void setTransformer(ResponseHandlerAttributes responseHandlerProps) {
		transformerProps = responseHandlerProps.getResponseTransformer();
	}

	private static void assertHandler(boolean raiseError,String handlerName) throws ConfigXMLParsingException {
		if(raiseError) {
			throw new ConfigXMLParsingException("Handler for" + handlerName + "not found");
		}
	}
}
