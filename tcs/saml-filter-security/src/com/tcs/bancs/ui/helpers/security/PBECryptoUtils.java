/**
 * 
 */
package com.tcs.bancs.ui.helpers.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.tcs.bancs.ui.helpers.Base64Util;

/**
 * @author tuhin.sengupta
 *
 */
public class PBECryptoUtils implements
PasswordEncryptorDecryptor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(PBECryptoUtils.class);

	Cipher cipher;
	SecretKey key;
	IvParameterSpec ips;
	public PBECryptoUtils(String algo, byte[] keyBytes, byte[] init) throws NoSuchAlgorithmException, NoSuchPaddingException
	{
		ips = new IvParameterSpec(init);
		cipher = Cipher.getInstance(algo);
	    key = new SecretKeySpec(keyBytes, algo);
	}
	public PBECryptoUtils(String algo, String mode, String padding, byte[] keyBytes, byte[] init) throws NoSuchAlgorithmException, NoSuchPaddingException
	{
		ips = new IvParameterSpec(init);
		cipher = Cipher.getInstance(algo + "/" + mode + "/" + padding);
	    key = new SecretKeySpec(keyBytes, algo);
	}
	public String decrypt(String cipherText) throws PasswordEncryptionDecryptionException {
		if (logger.isDebugEnabled()) {
			logger.debug("decrypt(String) - start"); //$NON-NLS-1$
		}

		try {
			String returnString = new String(decrypt(Base64Util.decode(cipherText)));
			if (logger.isDebugEnabled()) {
				logger.debug("decrypt(String) - end"); //$NON-NLS-1$
			}
			return returnString;
		} catch (Throwable e) {
			logger.error("decrypt(String)", e); //$NON-NLS-1$

			throw new PasswordEncryptionDecryptionException(e);
		} 
	}

	/* (non-Javadoc)
	 * @see com.tcs.bancs.channels.helpers.security.PasswordEncryptorDecryptor#encrypt(java.lang.String)
	 */
	public String encrypt(String plainText) throws PasswordEncryptionDecryptionException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("encrypt(String) - start"); //$NON-NLS-1$
		}
		
		try {
			String returnString = Base64Util.encode(encrypt(plainText.getBytes()));
			if (logger.isDebugEnabled()) {
				logger.debug("encrypt(String) - end"); //$NON-NLS-1$
			}
			return returnString;
		} catch (Throwable e) {
			logger.error("encrypt(String)", e); //$NON-NLS-1$

			throw new PasswordEncryptionDecryptionException(e);
		} 
	}


	public byte[] encrypt(byte[] inpBytes) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("encrypt(byte[]) - start"); //$NON-NLS-1$
		}

		cipher.init(Cipher.ENCRYPT_MODE, key, ips);
		byte[] returnbyteArray = cipher.doFinal(inpBytes);
		if (logger.isDebugEnabled()) {
			logger.debug("encrypt(byte[]) - end"); //$NON-NLS-1$
		}
		return returnbyteArray;
	}

	public byte[] decrypt(byte[] inpBytes) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("decrypt(byte[]) - start"); //$NON-NLS-1$
		}

		cipher.init(Cipher.DECRYPT_MODE, key, ips);
		byte[] returnbyteArray = cipher.doFinal(inpBytes);
		if (logger.isDebugEnabled()) {
			logger.debug("decrypt(byte[]) - end"); //$NON-NLS-1$
		}
		return returnbyteArray;
	}

}


