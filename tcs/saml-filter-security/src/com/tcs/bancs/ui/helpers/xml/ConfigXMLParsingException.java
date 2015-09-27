package com.tcs.bancs.ui.helpers.xml;

import org.apache.log4j.Logger;



public class ConfigXMLParsingException extends Exception {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ConfigXMLParsingException.class);

	public ConfigXMLParsingException(String string) {
		super(string);
	}

	public ConfigXMLParsingException(String msg, Exception e) {
		super(msg,e);
	}

	public ConfigXMLParsingException(Exception e) {
		super(e);
	}

}
