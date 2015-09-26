package com.tcs.bancs.handler.response;

import java.util.Map;

import com.tcs.bancs.config.handler.attributes.ResponseHandlerAttributes;

public interface ResponseHandler {
	public void setTransformer(ResponseHandlerAttributes requestHandlerProps);
	public Object processResponse(Map<String,Object> response) throws ResponseHandlerException;
}
