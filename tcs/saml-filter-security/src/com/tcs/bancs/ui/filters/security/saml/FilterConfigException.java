package com.tcs.bancs.ui.filters.security.saml;

import org.apache.log4j.Logger;

public class FilterConfigException extends Exception {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FilterConfigException.class);

	public FilterConfigException(String msg, Throwable e) {
		super(msg,e);
	}
	public FilterConfigException(String msg) {
		super(msg);
	}

}
