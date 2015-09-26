package com.tcs.ebw.serverside.exception;

import com.tcs.ebw.exception.EbwException;

/**
 * @author 231259
 * @year 2008
 */
public class DuplicateKeyException extends EbwException {
	static final String version = "DuplicateKeyException V1.0";

	/**
	 * Constructor for RecordNotFoundException.
	 */
	public DuplicateKeyException() {
		super("TELLER0001");
	}

	/**
	 * Constructor for RecordNotFoundException.
	 * 
	 * @param s
	 */
	public DuplicateKeyException(String s) {
		super("TELLER0001",s);
	}

}