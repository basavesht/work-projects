package com.tcs.bancs.handler.response.transformer;

import java.util.Map;

public interface ResponseTransformer {
	public Map<String, Object> transformResponse(Map<String, Object> response) throws ResponseTransformationException ;
}
