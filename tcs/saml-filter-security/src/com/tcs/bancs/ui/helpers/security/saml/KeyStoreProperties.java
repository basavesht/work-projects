package com.tcs.bancs.ui.helpers.security.saml;

import java.net.URL;

import org.apache.log4j.Logger;

import com.tcs.bancs.ui.helpers.security.PasswordEncryptionDecryptionException;
import com.tcs.bancs.ui.helpers.security.PasswordEncryptorDecryptor;

public class KeyStoreProperties 
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(KeyStoreProperties.class);

	private URL keystore;
	private String password;
	private String type;
	private String alias;
	private String keyPassword;
	private PasswordEncryptorDecryptor ped;
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getKeyPassword() {
		return keyPassword;
	}
	public void setKeyPassword(String keyPassword) throws PasswordEncryptionDecryptionException {
		if (logger.isDebugEnabled()) {
			logger.debug("setKeyPassword(String) - start"); //$NON-NLS-1$
		}

		if ( ped != null)
		{
			this.keyPassword = ped.decrypt(keyPassword);
		}
		else
		{
			this.keyPassword = keyPassword;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setKeyPassword(String) - end"); //$NON-NLS-1$
		}
	}
	public URL getKeystore() {
		return keystore;
	}
	public void setKeystore(URL keystore) {
		this.keystore = keystore;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) throws PasswordEncryptionDecryptionException {
		if (logger.isDebugEnabled()) {
			logger.debug("setPassword(String) - start"); //$NON-NLS-1$
		}

		if ( ped != null)
		{
			this.password = ped.decrypt(password);
		}
		else
		{
			this.password = password;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("setPassword(String) - end"); //$NON-NLS-1$
		}
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setPed(String decryptorClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		if (logger.isDebugEnabled()) {
			logger.debug("setPed(String) - start"); //$NON-NLS-1$
		}

		Class clazz = Class.forName(decryptorClass);
		ped = (PasswordEncryptorDecryptor)clazz.newInstance();

		if (logger.isDebugEnabled()) {
			logger.debug("setPed(String) - end"); //$NON-NLS-1$
		}
	}
	
	
}
