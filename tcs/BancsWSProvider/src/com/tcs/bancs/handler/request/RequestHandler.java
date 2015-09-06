package com.tcs.bancs.handler.request;

import java.util.List;
import java.util.Map;

import com.tcs.bancs.config.handler.attributes.RequestHandlerAttributes;
import com.tcs.bancs.config.handler.attributes.WSOperation;
import com.tcs.bancs.handler.request.router.RequestRouter;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;

public interface RequestHandler {
	public void setOperationName(WSOperation operation);
	public void setOperationId(WSOperation operation);
	public void setValidator(RequestHandlerAttributes requestHandlerProps);
	public void setTransformer(RequestHandlerAttributes requestHandlerProps);
	public void setRouter(RequestHandlerAttributes requestHandlerProps,List<RequestRouter> router) throws ConfigXMLParsingException;
	public Map<String,Object> processRequest(Map<String,Object> request) throws RequestHandlerException ;
}
