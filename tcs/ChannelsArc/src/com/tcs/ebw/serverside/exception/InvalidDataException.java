package com.tcs.ebw.serverside.exception;

import com.tcs.ebw.exception.EbwException;

/**
 * @author 231259
 * @year 2008
 */

public class InvalidDataException extends EbwException {
	
	static final String version = "InvalidDataException V1.0";
	static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for InvalidDataException.
	 */
	public InvalidDataException()
	{
		super("TELLER0006");
	}

	/**
	 * Constructor for InvalidDataException.
	 * @param s
	 */
	public InvalidDataException(String s)
	{
		super("TELLER0006",s);
	}
}
