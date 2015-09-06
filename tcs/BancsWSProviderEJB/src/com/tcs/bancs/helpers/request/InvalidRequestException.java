package com.tcs.bancs.helpers.request;

public class InvalidRequestException extends Exception 
{
	private static final long serialVersionUID = 2902538224671634019L;

	public InvalidRequestException(String string) {
		super(string);
	}

	public InvalidRequestException(String message, Exception e) {
		super(message,e);
	}

	public InvalidRequestException(Exception e) {
		super(e);
	}
}
