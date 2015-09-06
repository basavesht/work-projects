package com.tcs.bancs.ui.helpers.security;

import org.apache.log4j.Logger;

public class PasswordEncryptionDecryptionException extends Exception {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PasswordEncryptionDecryptionException.class);

	public PasswordEncryptionDecryptionException(Throwable e) {
		super(e);
	}

}
