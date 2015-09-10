package com.bosch.pat.exception;

public class UserNotAuthorizedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -671286861308243445L;

	public UserNotAuthorizedException(String string) {
		super(string);
	}

	public UserNotAuthorizedException(String message, Exception e) {
		super(message,e);
	}

	public UserNotAuthorizedException(Exception e) {
		super(e);
	}
}
