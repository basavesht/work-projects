package com.bosch.tmp.integration.util;

public class ConfigXMLParsingException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6710948230188687094L;
	public ConfigXMLParsingException(String string) {
		super(string);
	}

	public ConfigXMLParsingException(String message, Exception e) {
		super(message, e);
	}

	public ConfigXMLParsingException(Exception e) {
		super(e);
	}
}
