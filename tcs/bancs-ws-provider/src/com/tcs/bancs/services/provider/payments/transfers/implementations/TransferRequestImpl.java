package com.tcs.bancs.services.provider.payments.transfers.implementations;

import java.io.FileNotFoundException;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.tcs.bancs.config.handler.WorkflowHandlerConfig;
import com.tcs.bancs.handler.factory.RequestHandlerFactory;
import com.tcs.bancs.handler.factory.ResponseHandlerFactory;
import com.tcs.bancs.handler.request.RequestHandler;
import com.tcs.bancs.handler.request.RequestHandlerException;
import com.tcs.bancs.handler.response.ResponseHandler;
import com.tcs.bancs.handler.response.ResponseHandlerException;
import com.tcs.bancs.helpers.ws.HandlerNotFoundException;
import com.tcs.bancs.helpers.ws.TransferRequestException;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest;
import com.tcs.bancs.objects.schema.response.payments.transfers.TransferResponse;
import com.tcs.bancs.services.provider.payments.transfers.interfaces.ITransferRequest;
import com.tcs.bancs.utils.TransferRequestUtil;

@HandlerChain(
		file = "handler-chain.xml")
@WebService(
		endpointInterface = "com.tcs.bancs.services.provider.payments.transfers.interfaces.ITransferRequest",
		serviceName = "TransferRequestService",
		portName = "TransferRequestPort",
		wsdlLocation = "WEB-INF/wsdl/TransferRequestService.wsdl")
@BindingType(
		value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)

public class TransferRequestImpl implements ITransferRequest
{
	@Resource (name = "workflow-handler", type = java.lang.String.class)
	private String handlerConfigFile;

	private Map<String,Object> businessResponse = null;
	private TransferResponse serviceResponse = null;
	private WorkflowHandlerConfig handlerConfig = null;
	private String operation = null;

	@PostConstruct
	public void initialize()
	{
		handlerConfig = WorkflowHandlerConfig.getInstance();
		try {
			handlerConfig.parse(handlerConfigFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ConfigXMLParsingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load Transfer processing..
	 */
	public TransferResponse loadTransfer(TransferRequest request) throws TransferRequestException 
	{
		try {
			//Config extraction ..
			TransferRequestUtil.checkHandlerConfig(handlerConfig, handlerConfigFile);
			operation = TransferRequestUtil.getOperation(request);

			//Request Work-flow 
			RequestHandler requestHandler = RequestHandlerFactory.getHandler(handlerConfig,operation);
			businessResponse = requestHandler.processRequest(TransferRequestUtil.createRequestMap(request));

			//Response Work-flow 
			ResponseHandler responseHandler = ResponseHandlerFactory.getHandler(handlerConfig,operation);
			serviceResponse = TransferRequestUtil.createResponseObject((Map<String,Object>)responseHandler.processResponse(TransferRequestUtil.createBusinessResponse(request,businessResponse)));

		} catch (ConfigXMLParsingException e) {
			throw new TransferRequestException("Select Account :", TransferRequestUtil.getFaultBean("XML Config Parsing Exception"),e);
		} catch (HandlerNotFoundException e) {
			throw new TransferRequestException("Select Account :", TransferRequestUtil.getFaultBean("Request/Response hander Not Found"),e);
		} catch (RequestHandlerException e) {
			throw new TransferRequestException("Select Account :", TransferRequestUtil.getFaultBean("Request Handler Exception"),e);
		} catch (ResponseHandlerException e) {
			throw new TransferRequestException("Select Account :", TransferRequestUtil.getFaultBean("Response Handler Exception"),e);
		}
		return serviceResponse;

	}

	/**
	 * Select Account processing..
	 */
	public TransferResponse selectAccount(TransferRequest request)throws TransferRequestException 
	{
		try {
			//Config extraction ..
			TransferRequestUtil.checkHandlerConfig(handlerConfig, handlerConfigFile);
			operation = TransferRequestUtil.getOperation(request);

			//Request Work-flow 
			RequestHandler requestHandler = RequestHandlerFactory.getHandler(handlerConfig,operation);
			businessResponse = requestHandler.processRequest(TransferRequestUtil.createRequestMap(request));

			//Response Work-flow 
			ResponseHandler responseHandler = ResponseHandlerFactory.getHandler(handlerConfig,operation);
			serviceResponse = TransferRequestUtil.createResponseObject((Map<String,Object>)responseHandler.processResponse(TransferRequestUtil.createBusinessResponse(request,businessResponse)));

		} catch (ConfigXMLParsingException e) {
			throw new TransferRequestException("Select Account :", TransferRequestUtil.getFaultBean("XML Config Parsing Exception"),e);
		} catch (HandlerNotFoundException e) {
			throw new TransferRequestException("Select Account :", TransferRequestUtil.getFaultBean("Request/Response hander Not Found"),e);
		} catch (RequestHandlerException e) {
			throw new TransferRequestException("Select Account :", TransferRequestUtil.getFaultBean("Request Handler Exception"),e);
		} catch (ResponseHandlerException e) {
			throw new TransferRequestException("Select Account :", TransferRequestUtil.getFaultBean("Response Handler Exception"),e);
		}
		return serviceResponse;

	}

	/**
	 * Create Transfer processing..
	 */
	public TransferResponse createTransfer(TransferRequest request) throws TransferRequestException
	{
		try {
			//Config extraction ..
			TransferRequestUtil.checkHandlerConfig(handlerConfig, handlerConfigFile);
			operation = TransferRequestUtil.getOperation(request);

			//Request Work-flow 
			RequestHandler requestHandler = RequestHandlerFactory.getHandler(handlerConfig,operation);
			businessResponse = requestHandler.processRequest(TransferRequestUtil.createRequestMap(request));

			//Response Work-flow 
			ResponseHandler responseHandler = ResponseHandlerFactory.getHandler(handlerConfig,operation);
			serviceResponse = TransferRequestUtil.createResponseObject((Map<String,Object>)responseHandler.processResponse(TransferRequestUtil.createBusinessResponse(request,businessResponse)));

		} catch (ConfigXMLParsingException e) {
			throw new TransferRequestException("Create Transfer :", TransferRequestUtil.getFaultBean("XML Config Parsing Exception"),e);
		} catch (HandlerNotFoundException e) {
			throw new TransferRequestException("Create Transfer :", TransferRequestUtil.getFaultBean("Request/Response hander Not Found"),e);
		} catch (RequestHandlerException e) {
			throw new TransferRequestException("Create Transfer :", TransferRequestUtil.getFaultBean("Request Handler Exception"),e);
		} catch (ResponseHandlerException e) {
			throw new TransferRequestException("Create Transfer :", TransferRequestUtil.getFaultBean("Response Handler Exception"),e);
		}
		return serviceResponse;
	}

	@PreDestroy
	public void cleanup() {
		System.out.println("Clean up task called successfully");

	}
}
