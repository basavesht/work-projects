package com.tcs.bancs.ui.helpers.xml;

import org.apache.log4j.Logger;


public class ContextDataParsingException extends Exception {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ContextDataParsingException.class);

	public ContextDataParsingException(String string) {
		super(string);
	}

	public ContextDataParsingException(String msg, Throwable cause) {
		super(msg,cause);
	}

}
