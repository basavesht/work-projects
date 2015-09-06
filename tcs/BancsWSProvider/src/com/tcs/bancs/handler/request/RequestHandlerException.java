package com.tcs.bancs.handler.request;

public class RequestHandlerException extends Exception 
{
	private static final long serialVersionUID = -6926694590226148669L;

	public RequestHandlerException(String string) {
		super(string);
	}

	public RequestHandlerException(String message, Exception e) {
		super(message,e);
	}

	public RequestHandlerException(Exception e) {
		super(e);
	}
}
