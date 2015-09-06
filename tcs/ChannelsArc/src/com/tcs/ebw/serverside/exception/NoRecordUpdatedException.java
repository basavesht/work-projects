package com.tcs.ebw.serverside.exception;

import com.tcs.ebw.exception.EbwException;

/**
 * @author 231259
 * @year 2008
 */
public class NoRecordUpdatedException extends EbwException {
	static final String version = "NoRecordUpdatedException V1.0";

	/**
	 * Constructor for NoRecordUpdatedException.
	 */
	public NoRecordUpdatedException() {
		super("TELLER0003");
	}

	/**
	 * Constructor for NoRecordUpdatedException.
	 * 
	 * @param s
	 */
	public NoRecordUpdatedException(String s) {
		super("TELLER0003",s);
	}

}
