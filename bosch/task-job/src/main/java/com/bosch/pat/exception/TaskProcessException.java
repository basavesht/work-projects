package com.bosch.pat.exception;

public class TaskProcessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4176989391730044131L;

	public TaskProcessException(String string) {
		super(string);
	}

	public TaskProcessException(String message, Exception e) {
		super(message,e);
	}

	public TaskProcessException(Exception e) {
		super(e);
	}
}
