package com.tcs.bancs.config.handler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tcs.bancs.config.handler.attributes.RequestHandlerAttributes;
import com.tcs.bancs.config.handler.attributes.ResponseHandlerAttributes;
import com.tcs.bancs.config.handler.attributes.WSOperation;
import com.tcs.bancs.handler.request.router.RequestRouter;
import com.tcs.bancs.helpers.ObservableConfiguration;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.helpers.xml.XMLUtils;

public class WorkflowHandlerConfig extends ObservableConfiguration 
{
	private static final Logger logger = Logger.getLogger(WorkflowHandlerConfig.class);
	private List<WSOperation> operations = new ArrayList<WSOperation>();
	private List<RequestRouter> routers = new ArrayList<RequestRouter>();
	private String requiredValue = null;
	private boolean required = false;
	private static WorkflowHandlerConfig instance  = null;

	//Singleton class..
	private WorkflowHandlerConfig(){

	}

	//Single Instance creation..
	public static WorkflowHandlerConfig getInstance()
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String, boolean) - start"); 
		}

		if (instance == null){
			instance = new WorkflowHandlerConfig();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getInstance(String, boolean) - end"); 
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
			NodeList nodes = configDoc.getElementsByTagName("WORKFLOW_HANDLER_CONFIG").item(0).getChildNodes();
			int nodes_length_0 = nodes.getLength();
			for(int m = 0 ; m < nodes_length_0; m++)
			{
				Node child_0 = nodes.item(m);
				if (child_0.getNodeName().equals("OPERATIONS"))
				{
					NodeList nodes_0 = child_0.getChildNodes();
					int nodes_length = nodes_0.getLength();
					for(int i = 0 ; i < nodes_length; i++)
					{
						Node child = nodes_0.item(i);
						if (child.getNodeName().equals("OPERATION"))
						{
							WSOperation operation = new WSOperation();
							operation.setOperation_name(XMLUtils.getAttribute(child,"NAME"));
							operation.setOperation_id(XMLUtils.getAttribute(child,"ID"));

							NodeList nodes_1 = child.getChildNodes();
							int nodes_length_1 = nodes_1.getLength();
							for(int j = 0 ; j < nodes_length_1; j++)
							{
								Node child_1 = nodes_1.item(j);
								if (child_1.getNodeName().equals("REQUEST_HANDLER"))
								{
									RequestHandlerAttributes requestHandler = new RequestHandlerAttributes();
									requestHandler.setHandlerImplClass(XMLUtils.getAttribute(child_1,"IMPL_CLASS"));

									NodeList nodes_2 = child_1.getChildNodes();
									int nodes_length_2 = nodes_2.getLength();
									for(int k = 0 ; k < nodes_length_2; k++)
									{
										Node child_2 = nodes_2.item(k);
										if (child_2.getNodeName().equals("REQUEST_VALIDATOR"))
										{
											Map<String,String> requestValidator = new HashMap<String,String>();
											requiredValue = XMLUtils.getAttribute(child_2,"REQUIRED");
											required = requiredValue.equalsIgnoreCase("true");
											requestValidator.put("REQUIRED", requiredValue);
											if(required){
												String validator_class = XMLUtils.getAttribute(child_2,"VALIDATOR_IMPL_CLASS");
												requestValidator.put("VALIDATOR_IMPL_CLASS", validator_class);
											}
											requestHandler.setRequestValidator(requestValidator);
										}
										else if (child_2.getNodeName().equals("REQUEST_TRANSFORMER"))
										{
											Map<String,String> requestTransformer = new HashMap<String,String>();
											requiredValue = XMLUtils.getAttribute(child_2,"REQUIRED");
											required = requiredValue.equalsIgnoreCase("true");
											requestTransformer.put("REQUIRED", requiredValue);
											if(required){
												String transformer_class = XMLUtils.getAttribute(child_2,"TRANSFORMER_IMPL_CLASS");
												requestTransformer.put("TRANSFORMER_IMPL_CLASS", transformer_class);
											}
											requestHandler.setRequestTransformer(requestTransformer);
										}
										else if (child_2.getNodeName().equals("REQUEST_ROUTER"))
										{
											Map<String,String> requestRouter = new HashMap<String,String>();
											requiredValue = XMLUtils.getAttribute(child_2,"REQUIRED");
											required = requiredValue.equalsIgnoreCase("true");
											requestRouter.put("REQUIRED", requiredValue);
											if(required){
												String router_mode = XMLUtils.getAttribute(child_2,"ROUTER_MODE");
												requestRouter.put("ROUTER_MODE", router_mode);
											}
											requestHandler.setRequestRouter(requestRouter);
										}
									}
									operation.setRequestHandler(requestHandler);
								}
								else if (child_1.getNodeName().equals("RESPONSE_HANDLER"))
								{
									ResponseHandlerAttributes responseHandler = new ResponseHandlerAttributes();
									responseHandler.setHandlerImplClass(XMLUtils.getAttribute(child_1,"IMPL_CLASS"));

									NodeList nodes_2 = child_1.getChildNodes();
									int nodes_length_2 = nodes_2.getLength();
									for(int k = 0 ; k < nodes_length_2; k++)
									{
										Node child_2 = nodes_2.item(k);
										if (child_2.getNodeName().equals("RESPONSE_TRANSFORMER"))
										{
											Map<String,String> responseTransformer = new HashMap<String,String>();
											requiredValue = XMLUtils.getAttribute(child_2,"REQUIRED");
											required = requiredValue.equalsIgnoreCase("true");
											responseTransformer.put("REQUIRED", requiredValue);
											if(required){
												String transformer_class = XMLUtils.getAttribute(child_2,"TRANSFORMER_IMPL_CLASS");
												responseTransformer.put("TRANSFORMER_IMPL_CLASS", transformer_class);
											}
											responseHandler.setResponseTransformer(responseTransformer);
										}
									}
									operation.setResponseHandler(responseHandler);
								}
							}
							validateOperationsConfig(operation);
							operations.add(operation);
						}
					}
				}
				else if(child_0.getNodeName().equals("ROUTERS"))
				{
					String connectorConfig = XMLUtils.getAttribute(child_0,"INIT");
					NodeList nodes_0 = child_0.getChildNodes();
					int nodes_length = nodes_0.getLength();
					for(int i = 0 ; i < nodes_length; i++)
					{
						Node child = nodes_0.item(i);
						if (child.getNodeName().equals("ROUTER"))
						{
							RequestRouter router = null;
							try {
								Class classObj = Class.forName(child.getFirstChild().getNodeValue());
								router = (RequestRouter)classObj.newInstance();
							} catch (ClassNotFoundException e) {
								throw new ConfigXMLParsingException("Invalid class name defined in 'ROUTER' Tag", e);
							} catch (IllegalAccessException e) {
								throw new ConfigXMLParsingException("Invalid class name defined in 'ROUTER' Tag", e);
							} catch (InstantiationException e) {
								throw new ConfigXMLParsingException("Invalid class name defined in 'ROUTER' Tag", e);
							}

							if(router!=null){
								router.init(connectorConfig);
							}
							validateRouterConfig(router);
							routers.add(router);
						}
					}
				}
			}
		} 
		catch (IOException e) {
			logger.error("WorkflowHandlerConfig(String)", e);
			throw new ConfigXMLParsingException("IOException received", e);		
		}
		finally
		{
			try {
				if (is !=null) 
					is.close();
			} 
			catch (IOException e) {
				logger.warn("WorkflowHandlerConfig(String)- exception ignored", e); 
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("parseImpl(String) - end");
		}
	}

	private void validateOperationsConfig(WSOperation operation) throws ConfigXMLParsingException {
		assertConfigForTag(operation==null,"OPERATION");
		assertConfigForAttribute(operation.getOperation_name()==null,"OPERATION","NAME");
		assertConfigForAttribute(operation.getOperation_id()==null,"OPERATION","ID");

		validateRequestHandlerConfig(operation.getRequestHandler());
		validateResponseHandlerConfig(operation.getResponseHandler());
	}

	private void validateRouterConfig(RequestRouter requestRouter) throws ConfigXMLParsingException {
		assertConfigForTag(requestRouter==null,"ROUTERS");
	}

	private void validateRequestHandlerConfig(RequestHandlerAttributes requestHandler) throws  ConfigXMLParsingException {
		assertConfigForTag(requestHandler==null,"REQUEST_HANDLER");
		assertConfigForAttribute(requestHandler.getHandlerImplClass()==null,"REQUEST_HANDLER","IMPL_CLASS");

		assertConfigForTag(requestHandler.getRequestValidator()==null,"REQUEST_VALIDATOR");
		if(!requestHandler.getRequestValidator().isEmpty()){
			Map<String,String> requestValidator = requestHandler.getRequestValidator();
			assertConfigForAttribute(requestValidator.get("REQUIRED")==null,"REQUEST_VALIDATOR","REQUIRED");
			if(requestValidator.containsKey("REQUIRED") && requestValidator.get("REQUIRED").equalsIgnoreCase("true")){
				assertConfigForAttribute(requestValidator.get("VALIDATOR_IMPL_CLASS")==null,"REQUEST_VALIDATOR","VALIDATOR_IMPL_CLASS");
			}
		}
		else {
			assertConfigForTag(true,"REQUEST_VALIDATOR");
		}

		assertConfigForTag(requestHandler.getRequestTransformer()==null,"REQUEST_TRANSFORMER");
		if(!requestHandler.getRequestTransformer().isEmpty()){
			Map<String,String> requestTransformer = requestHandler.getRequestTransformer();
			assertConfigForAttribute(requestTransformer.get("REQUIRED")==null,"REQUEST_TRANSFORMER","REQUIRED");
			if(requestTransformer.containsKey("REQUIRED") && requestTransformer.get("REQUIRED").equalsIgnoreCase("true")){
				assertConfigForAttribute(requestTransformer.get("TRANSFORMER_IMPL_CLASS")==null,"REQUEST_TRANSFORMER","TRANSFORMER_IMPL_CLASS");
			}
		}
		else {
			assertConfigForTag(true,"REQUEST_TRANSFORMER");
		}

		assertConfigForTag(requestHandler.getRequestRouter()==null,"REQUEST_ROUTER");
		if(!requestHandler.getRequestRouter().isEmpty()){
			Map<String,String> requestRouter = requestHandler.getRequestRouter();
			assertConfigForAttribute(requestRouter.get("REQUIRED")==null,"REQUEST_ROUTER","REQUIRED");
			if(requestRouter.containsKey("REQUIRED") && requestRouter.get("REQUIRED").equalsIgnoreCase("true")){
				assertConfigForAttribute(requestRouter.get("ROUTER_MODE")==null,"REQUEST_ROUTER","ROUTER_MODE");
			}
		}
		else {
			assertConfigForTag(true,"REQUEST_ROUTER");
		}
	}

	private void validateResponseHandlerConfig(ResponseHandlerAttributes responseHandler) throws ConfigXMLParsingException {	
		assertConfigForTag(responseHandler==null,"RESPONSE_HANDLER");
		assertConfigForAttribute(responseHandler.getHandlerImplClass()==null,"RESPONSE_HANDLER","IMPL_CLASS");

		assertConfigForTag(responseHandler.getResponseTransformer()==null,"RESPONSE_TRANSFORMER");
		if(!responseHandler.getResponseTransformer().isEmpty()){
			Map<String,String> responseTransformer = responseHandler.getResponseTransformer();
			assertConfigForAttribute(responseTransformer.get("REQUIRED")==null,"RESPONSE_TRANSFORMER","REQUIRED");
			if(responseTransformer.containsKey("REQUIRED") && responseTransformer.get("REQUIRED").equalsIgnoreCase("true")){
				assertConfigForAttribute(responseTransformer.get("TRANSFORMER_IMPL_CLASS")==null,"RESPONSE_TRANSFORMER","TRANSFORMER_IMPL_CLASS");
			}
		}
		else {
			assertConfigForTag(true,"RESPONSE_TRANSFORMER");
		}
	}

	private void assertConfigForTag(boolean raiseError, String tagName) throws ConfigXMLParsingException {
		if(raiseError) {
			throw new ConfigXMLParsingException("XML tag '" + tagName + "' must be specified in config XML");
		}
	}

	private void assertConfigForAttribute(boolean raiseError, String tagName, String attributeName) throws ConfigXMLParsingException {		
		if(raiseError) {
			throw new ConfigXMLParsingException("XML attribute '" + attributeName + "' must be specified for tag '" + tagName +"' in config XML");
		}
	}

	public List<WSOperation> getOperations() {
		return operations;
	}
	public void setOperations(List<WSOperation> operations) {
		this.operations = operations;
	}
	public List<RequestRouter> getRouters() {
		return routers;
	}
	public void setRouters(List<RequestRouter> routers) {
		this.routers = routers;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
		final String TAB = "\r\n";

		String retValue = "";

		retValue = "WorkflowHandlerConfig ( "
				+ super.toString() + TAB
				+ "operations = " + this.operations + TAB
				+ "routers = " + this.routers + TAB
				+ " )";

		return retValue;
	}

}
