package com.tcs.bancs.ui.helpers.security.saml;

import org.apache.log4j.Logger;

public class KeyProperties 
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(KeyProperties.class);

	private KeyStoreProperties keystore;
	public KeyStoreProperties getKeystore() {
		return keystore;
	}
	public void setKeystore(KeyStoreProperties keystore) {
		this.keystore = keystore;
	}
}
