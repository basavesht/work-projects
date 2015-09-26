package com.tcs.bancs.handler.response;

public class ResponseHandlerException extends Exception 
{
	private static final long serialVersionUID = 4413740565434183043L;

	public ResponseHandlerException(String string) {
		super(string);
	}

	public ResponseHandlerException(String message, Exception e) {
		super(message,e);
	}

	public ResponseHandlerException(Exception e) {
		super(e);
	}
}
