package com.tcs.ebw.serverside.exception;

import com.tcs.ebw.exception.EbwException;

/**
 * @author 231259
 * @year 2008
 */
public class DBException extends EbwException {

	static final String version = "DBException V1.0";
	static final long serialVersionUID = 1L;

	public DBException() {
		super("TELLER0002");
	}

	public DBException(String s) {
		super("TELLER0002",s);
	}
}