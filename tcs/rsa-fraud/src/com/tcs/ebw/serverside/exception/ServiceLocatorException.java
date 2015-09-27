package com.tcs.ebw.serverside.exception;

import com.tcs.ebw.exception.EbwException;

/**
 * @author 231259
 * @year 2008
 */
public class ServiceLocatorException extends EbwException {

	/**
	 * Constructor for ServiceLocatorException.
	 */
	public ServiceLocatorException() {
		super("TELLER0005");
	}

	/**
	 * Constructor for ServiceLocatorException.
	 * 
	 * @param s
	 */
	public ServiceLocatorException(String s) {
		super("TELLER0005",s);
	}
}
