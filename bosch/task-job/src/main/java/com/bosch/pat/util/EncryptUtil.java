package com.bosch.pat.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * Utility method to encrypt the sensitive data.
 * @author TEB1PAL
 *
 */
public class EncryptUtil {
	
	public static void main(String... args) {
		 StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		 encryptor.setPassword("bosch"); // Encryption Key
		 encryptor.setProvider(new BouncyCastleProvider());
		 encryptor.setAlgorithm("PBEWITHSHA256AND128BITAES-CBC-BC");
		 System.out.println(encryptor.encrypt("hhncares")); // Data to be encrypted.
	}

}
