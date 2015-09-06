package com.tcs.bancs.handler.response.transformer;

public class ResponseTransformationException extends Exception 
{
	private static final long serialVersionUID = 4564367532250403509L;

	public ResponseTransformationException(String string) {
		super(string);
	}

	public ResponseTransformationException(String message, Exception e) {
		super(message,e);
	}

	public ResponseTransformationException(Exception e) {
		super(e);
	}
}
