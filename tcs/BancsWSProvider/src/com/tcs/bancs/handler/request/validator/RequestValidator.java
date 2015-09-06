package com.tcs.bancs.handler.request.validator;

import java.util.Map;

public interface RequestValidator {
	public boolean validate(Map<String, Object> request) throws RequestValidationException ;
}
