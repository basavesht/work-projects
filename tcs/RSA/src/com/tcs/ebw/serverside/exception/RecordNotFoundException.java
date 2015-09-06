package com.tcs.ebw.serverside.exception;

import com.tcs.ebw.exception.EbwException;

/**
 * @author 231259
 * @year 2008
 */
public class RecordNotFoundException extends EbwException {
	static final String version = "RecordNotFoundException V1.0";

	/**
	 * Constructor for RecordNotFoundException.
	 */
	public RecordNotFoundException() {
		super("TELLER0004");
	}

	/**
	 * Constructor for RecordNotFoundException.
	 * 
	 * @param s
	 */
	public RecordNotFoundException(String s) {
		super("TELLER0004",s);
	}

}
