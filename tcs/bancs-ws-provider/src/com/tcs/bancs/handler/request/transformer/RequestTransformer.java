package com.tcs.bancs.handler.request.transformer;

import java.util.Map;

public interface RequestTransformer {
	public Map<String, Object> transformRequest(Map<String, Object> request,String operationId) throws RequestTransformationException ;
}
