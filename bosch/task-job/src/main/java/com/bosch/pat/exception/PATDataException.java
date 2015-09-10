package com.bosch.pat.exception;

import org.springframework.dao.DataAccessException;

public class PATDataException extends DataAccessException{

	public PATDataException() {
		super("Pat Data Exception");
	}
	
	public PATDataException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	
}
