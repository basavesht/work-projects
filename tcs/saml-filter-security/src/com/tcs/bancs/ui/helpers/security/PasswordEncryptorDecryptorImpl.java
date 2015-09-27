/**
 * 
 */
package com.tcs.bancs.ui.helpers.security;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.tcs.bancs.ui.helpers.Base64Util;

/**
 * @author tuhin.sengupta
 *
 */
public class PasswordEncryptorDecryptorImpl implements
PasswordEncryptorDecryptor {
	static PBECryptoUtils utils = null;
	public PasswordEncryptorDecryptorImpl() throws PasswordEncryptionDecryptionException
	{
		Security.addProvider(new BouncyCastleProvider());
		try {
			try {
				utils = new PBECryptoUtils("DES",
						new byte[]{87,-23,115,4,69,-60,-32,-3},
						new byte[]{ 0x0a, 0x01, 0x02, 0x03, 0x04, 0x0b, 0x0c, 0x0d });
			} catch (NoSuchAlgorithmException e) {
				throw new PasswordEncryptionDecryptionException(e);
			} catch (NoSuchPaddingException e) {
				throw new PasswordEncryptionDecryptionException(e);
			}
		}
		finally
		{
			
		}
	}
	public String decrypt(String cipherText) throws PasswordEncryptionDecryptionException {
		try {
			String returnString = new String(utils.decrypt(Base64Util.decode(cipherText)));
			return returnString;
		} catch (Throwable e) {
			throw new PasswordEncryptionDecryptionException(e);
		} 
	}

	/* (non-Javadoc)
	 * @see com.tcs.bancs.channels.helpers.security.PasswordEncryptorDecryptor#encrypt(java.lang.String)
	 */
	public String encrypt(String plainText) throws PasswordEncryptionDecryptionException 
	{
		try {
			String returnString = Base64Util.encode(utils.encrypt(plainText.getBytes()));
			return returnString;
		} catch (Throwable e) {
			throw new PasswordEncryptionDecryptionException(e);
		} 
	}

	/**
	 * @param args
	 * @throws PasswordEncryptionDecryptionException 
	 */
	public static void main(String[] args) throws PasswordEncryptionDecryptionException 
	{
	    PasswordEncryptorDecryptorImpl impl = new PasswordEncryptorDecryptorImpl();
		String password = readPassword("Enter password: ");
		System.out.println("The encrypted password is: "+ impl.encrypt(password));
	}
	/**
	 *@param prompt The prompt to display to the user
	 *@return The password as entered by the user
	 */
	public static String readPassword (String prompt) {
		System.out.print(prompt);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String password = "";

		try {
			password = in.readLine();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return password;
	}

}


