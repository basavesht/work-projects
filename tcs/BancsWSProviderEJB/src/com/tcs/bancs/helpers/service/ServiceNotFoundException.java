package com.tcs.bancs.helpers.service;

public class ServiceNotFoundException extends Exception {

	private static final long serialVersionUID = 2787415215129403256L;

	public ServiceNotFoundException(String string) {
		super(string);
	}

	public ServiceNotFoundException(String message, Exception e) {
		super(message,e);
	}

	public ServiceNotFoundException(Exception e) {
		super(e);
	}
}
