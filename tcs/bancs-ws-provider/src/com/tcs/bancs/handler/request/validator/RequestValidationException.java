package com.tcs.bancs.handler.request.validator;

public class RequestValidationException extends Exception 
{
	private static final long serialVersionUID = -7900506974073478397L;

	public RequestValidationException(String string) {
		super(string);
	}

	public RequestValidationException(String message, Exception e) {
		super(message,e);
	}

	public RequestValidationException(Exception e) {
		super(e);
	}
}
