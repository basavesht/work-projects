/**
 * 
 */
package com.tcs.bancs.ui.helpers.security;


/**
 * @author tuhin.sengupta
 *
 */
public interface PasswordEncryptorDecryptor 
{
	public String encrypt(String plainText) throws PasswordEncryptionDecryptionException;
	public String decrypt(String cipherText) throws PasswordEncryptionDecryptionException;
}
