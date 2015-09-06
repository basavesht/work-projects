package com.tcs.bancs.handler.request.transformer;

public class RequestTransformationException extends Exception
{
	private static final long serialVersionUID = -6781817497477752052L;

	public RequestTransformationException(String string) {
		super(string);
	}

	public RequestTransformationException(String message, Exception e) {
		super(message,e);
	}

	public RequestTransformationException(Exception e) {
		super(e);
	}
}
