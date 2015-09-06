package com.tcs.bancs.handler.request;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

import com.tcs.bancs.config.handler.attributes.RequestHandlerAttributes;
import com.tcs.bancs.config.handler.attributes.WSOperation;
import com.tcs.bancs.handler.request.router.ejb.RequestEJBRouterImpl;
import com.tcs.bancs.handler.request.router.RequestRouter;
import com.tcs.bancs.handler.request.router.RequestRouterException;
import com.tcs.bancs.handler.request.transformer.RequestTransformationException;
import com.tcs.bancs.handler.request.transformer.RequestTransformer;
import com.tcs.bancs.handler.request.validator.RequestValidationException;
import com.tcs.bancs.handler.request.validator.RequestValidator;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;

public class RequestHandlerImpl implements RequestHandler
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RequestHandlerImpl.class);

	private boolean isRequestValid = true;
	private Map<String,String> validator = null;
	private Map<String,String> transformer = null;
	private RequestRouter router = null;
	private Map<String,Object> businessRequest = null;
	private Map<String,Object> businessResponse = null;
	@SuppressWarnings("unused")
	private String operationName = null;
	private String operationId = null;

	public Map<String, Object> processRequest(Map<String, Object> request) throws RequestHandlerException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("processRequest(Map<String,Object>) - start");
		}

		try {
			validateRequest(request);
			if(isRequestValid) {
				transformRequest(request);
				if(businessRequest!=null && !businessRequest.isEmpty()) {
					routeRequest(businessRequest);
				}
			}
		} catch (ConfigXMLParsingException e) {
			e.printStackTrace();
			throw new RequestHandlerException("Error with the Request Handler configuration",e);
		} catch (RequestValidationException e) {
			e.printStackTrace();
			throw new RequestHandlerException("Web service request validation failed",e);
		} catch (RequestTransformationException e) {
			e.printStackTrace();
			throw new RequestHandlerException("Failed during transforming web service request into business request",e);
		} catch (RequestRouterException e) {
			e.printStackTrace();
			throw new RequestHandlerException("Request could not be routed to the corresonding business method",e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("processRequest(Map<String,Object>) - end");
		}
		return businessResponse;
	}

	/**
	 * Validating the Input request...
	 * @param request
	 * @throws RequestValidationException
	 * @throws ConfigXMLParsingException
	 */
	public void validateRequest(Map<String, Object> request) throws RequestValidationException, ConfigXMLParsingException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("validateRequest(Map<String,Object>) - start"); 
		}

		RequestValidator requestValidator = null;
		assertHandler(validator==null,"REQUEST_VALIDATOR");

		String isValidatorRequired = (String)validator.get("REQUIRED");
		if(isValidatorRequired.equalsIgnoreCase("true"))
		{
			//Creating an Request Validation class instance...
			String validatorImplClass = validator.get("VALIDATOR_IMPL_CLASS");
			try {
				Class classObj = Class.forName(validatorImplClass);
				requestValidator = (RequestValidator)classObj.newInstance();
			} catch (ClassNotFoundException e) {
				throw new RequestValidationException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'REQUEST_VALIDATOR'", e);
			} catch (IllegalAccessException e) {
				throw new RequestValidationException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'REQUEST_VALIDATOR'", e);
			} catch (InstantiationException e) {
				throw new RequestValidationException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'REQUEST_VALIDATOR'", e);
			}

			//Validating the Request
			try {
				isRequestValid = requestValidator.validate(request);
			} 
			catch (Exception e) {
				logger.error("validateRequest(Map<String,Object>)", e); 

				throw new RequestValidationException("Exception occured during Request Validation ..", e);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("validateRequest(Map<String,Object>) - end"); 
		}
	}

	/**
	 * Transforming the input request to the business input...
	 * @param request
	 * @throws RequestTransformationException
	 * @throws ConfigXMLParsingException
	 */
	public void transformRequest(Map<String, Object> request) throws RequestTransformationException, ConfigXMLParsingException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("transformRequest(Map<String,Object>) - start"); 
		}

		RequestTransformer requestTransformer = null;
		assertHandler(transformer==null,"REQUEST_TRANSFORMER");

		String isTransformerRequired = (String)transformer.get("REQUIRED");
		if(isTransformerRequired.equalsIgnoreCase("true"))
		{
			//Creating an Request Transformer class instance...
			String transformerImplClass = transformer.get("TRANSFORMER_IMPL_CLASS");
			try {
				Class classObj = Class.forName(transformerImplClass);
				requestTransformer = (RequestTransformer)classObj.newInstance();
			} catch (ClassNotFoundException e) {
				throw new RequestTransformationException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'REQUEST_TRANSFORMER'", e);
			} catch (IllegalAccessException e) {
				throw new RequestTransformationException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'REQUEST_TRANSFORMER'", e);
			} catch (InstantiationException e) {
				throw new RequestTransformationException("Invalid class name defined under 'IMPL_CLASS' attribute specified in 'REQUEST_TRANSFORMER'", e);
			}

			//Transforming web service request into business request..
			try {
				businessRequest = requestTransformer.transformRequest(request,operationId);
			} catch (Exception e) {
				logger.error("transformRequest(Map<String,Object>)", e); 

				throw new RequestTransformationException("Exception occured while transforming request into business request ..", e);
			}
		}
		else {
			businessRequest = request;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("transformRequest(Map<String,Object>) - end"); 
		}
	}

	/**
	 * Routing the web service request for business processing logic..
	 * @param request
	 * @throws RequestRouterException
	 * @throws ConfigXMLParsingException
	 */
	public void routeRequest(Map<String, Object> request) throws RequestRouterException,ConfigXMLParsingException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("routeRequest(Map<String,Object>) - start"); 
		}

		assertHandler(router==null,"REQUEST_ROUTER");
		try {
			businessResponse = router.routeRequest(request);
		} 
		catch (Exception e) {
			throw new RequestRouterException("Exception occured during Request Routing to business ..", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("routeRequest(Map<String,Object>) - end"); 
		}
	}

	public void setValidator(RequestHandlerAttributes requestHandlerProps) {
		this.validator = requestHandlerProps.getRequestValidator();
	}

	public void setTransformer(RequestHandlerAttributes requestHandlerProps) {
		this.transformer = requestHandlerProps.getRequestTransformer();
	}

	public void setRouter(RequestHandlerAttributes requestHandlerProps,List<RequestRouter> routers) throws ConfigXMLParsingException {
		String isDispatcherRequired = requestHandlerProps.getRequestRouter().get("REQUIRED");
		String router_mode = requestHandlerProps.getRequestRouter().get("ROUTER_MODE");
		if(isDispatcherRequired.equalsIgnoreCase("true")) {
			if("EJB".equalsIgnoreCase(router_mode)) {
				for(RequestRouter businessRouter : routers){
					if(businessRouter instanceof RequestEJBRouterImpl){
						router = businessRouter;
					}
				}
			}
		}
	}

	public void setOperationName(WSOperation operation) {
		this.operationName = operation.getOperation_name();
	}

	public void setOperationId(WSOperation operation) {
		this.operationId = operation.getOperation_id();
	}

	private static void assertHandler(boolean raiseError,String handlerName) throws ConfigXMLParsingException {
		if(raiseError) {
			throw new ConfigXMLParsingException("Handler for" + handlerName + "not found");
		}
	}
}
