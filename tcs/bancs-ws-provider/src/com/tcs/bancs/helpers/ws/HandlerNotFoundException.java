package com.tcs.bancs.helpers.ws;

public class HandlerNotFoundException extends Exception 
{
	private static final long serialVersionUID = 6108256693225600704L;

	public HandlerNotFoundException(String string) {
		super(string);
	}

	public HandlerNotFoundException(String message, Exception e) {
		super(message,e);
	}

	public HandlerNotFoundException(Exception e) {
		super(e);
	}
}
