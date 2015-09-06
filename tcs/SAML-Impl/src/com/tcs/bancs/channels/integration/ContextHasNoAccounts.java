package com.tcs.bancs.channels.integration;

import org.apache.log4j.Logger;

public class ContextHasNoAccounts extends Exception {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ContextHasNoAccounts.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContextHasNoAccounts()
	{
		super("Context has no accounts.");
	}
}
