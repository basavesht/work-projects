package com.bosch.pat.exception;

public class PatientNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5219715428307772075L;

	public PatientNotFoundException(String string) {
		super(string);
	}

	public PatientNotFoundException(String message, Exception e) {
		super(message,e);
	}

	public PatientNotFoundException(Exception e) {
		super(e);
	}
}
