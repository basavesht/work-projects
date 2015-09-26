package com.tcs.bancs.handler.request.router;

public class RequestRouterException extends Exception 
{
	private static final long serialVersionUID = 4181749356905627669L;

	public RequestRouterException(String string) {
		super(string);
	}

	public RequestRouterException(String message, Exception e) {
		super(message,e);
	}

	public RequestRouterException(Exception e) {
		super(e);
	}
}
