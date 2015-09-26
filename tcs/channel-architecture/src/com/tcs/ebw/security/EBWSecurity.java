package com.tcs.ebw.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.tcs.ebw.common.util.ByteUtil;
import com.tcs.ebw.common.util.PropertyFileReader;

/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class EBWSecurity {
    
    /**** Variable declarations for  Symmetric Cryptography ******/
    /** Used for generating key when using Symmetric Cryptography **/
    private KeyGenerator keygen;
    
    /** Used store generated key from Symmetric Cryptography **/
    private Key symmetricKey;
    
    /** Cipher used for Symmetric Crypto for encryption/decryption **/
    private Cipher cipherSymmetric;
    
    
    /***** Variable declarations for Asymmetric Cryptography Usage *****/
    /** Cipher used for Asymmetric Crypto for encryption/decryption **/
    private Cipher cipherAsymmetric;
    
    /** Variable for storing private key generated from Asymmetric Crytography **/
    private PrivateKey priKey;
    
    /** Variable for storing public key generated from Asymmetric Cryptography **/
    private PublicKey pubKey;
    
    /** Used to store keypair generated by Asymmetric Cryptography **/
    private KeyPair kp;
    
    /** Variable to store KeyPair generator used by Asymmetric Cryptography **/
    private KeyPairGenerator kpg = null;
    
    /** Used to store secret key for storing generated key used in MAC generation **/
    private SecretKey secKey =null;
     
    public EBWSecurity() throws Exception{
    	try{
    	EBWConstants.populateConstants();
    	}catch(Exception e)
    	{
    		throw e;
    	}
    }
    
    public static void main(String args[]){
    	
    	try {
    	
    	EBWSecurity sec=new EBWSecurity();
    	String testData2="Pramodh B Somashekara 231259 Channels !@#$%^&*()";
    	if(args.length > 0) testData2 = (String)args[0];
    		if(testData2==null){
    		System.out.println("No Arguements in command line...Continuing with 'ELECTUSR'");
    			testData2="ELECTUSR";
    		}
    		byte[] enc;
    		 System.out.println("----------------ENCRYPTION-------------");		
    		 System.out.println("Original String:"+testData2);
    		 /** Set IMRS Key here **/
             sec.setSecretKey(sec.getSecretKey());
             testData2=(new String(Base64.encodeBase64(sec.encryptSymmetric(testData2.getBytes()))));
             System.out.println("After Base64 Encode:"+testData2);
    		testData2=java.net.URLEncoder.encode(testData2, "utf-8");
			System.out.println("Encrypted[Base64+EncryptAlgo+URLEncoded]["+testData2+"]");
			
			System.out.println("---------------DECRYPTION--------------");
			System.out.println("Encrypted[Base64+EncryptAlgo+URLEncoded]"+testData2);
			testData2=java.net.URLDecoder.decode(testData2, "utf-8");
			testData2=new String(sec.decryptSymmetric(Base64.decodeBase64(java.net.URLDecoder.decode(testData2, "utf-8").getBytes())));
			System.out.println("Decrypted[Base64+EncryptAlgo]["+testData2+"]");
			   
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    
    
    /**
     * This method is used to generate keypairs for using with Asymmetric cryptography.
     * This method generates and stores private and public keys in their respective 
     * objects. Later these keys can be used in appropriate places in Encyption 
     * and Decryption using PublicKeyCryptography or Asymmetric Cryptography methods.
     * 
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws NoSuchProviderException
     */
    private void generateKeyPairsForAsymmetric() 
     throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException {
        
        /** KeyPair Generation required for Asymmetric Crypto **/
        if(kpg==null){
           // //EBWLogger.logDebug(this,"Provider added successully");
	        kpg = KeyPairGenerator.getInstance(EBWConstants.ENCRYPTION_ASYMMETRIC_ALGORITHM);
	        ////EBWLogger.logDebug(this,"Keypair generated successfully..");
	        kpg.initialize(1024);
	        kp = kpg.generateKeyPair();
	        priKey = kp.getPrivate();
	        pubKey = kp.getPublic();
	        //if(priKey!=null && pubKey!=null)
	        ////EBWLogger.logDebug(this,"Private / Public keys generated successfully..");
        }
        
        /** Initialize cipher used for Asymmetric Crypto **/
        ////EBWLogger.logDebug(this,"Getting Cipher instance.");
        
        cipherAsymmetric = Cipher.getInstance(EBWConstants.ENCRYPTION_ASYMMETRIC_ALGORITHM);
        
    }
    
    /**
     * This method is used to return the private key used by Assymmetric Cryptography.
     * 
     * @return Returns PrivateKey object if it already exists or generates a new key 
     *         and returns it.  
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws NoSuchProviderException
     */
    public PrivateKey getPrivateKey() 
      throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException{
        if(priKey==null)
            generateKeyPairsForAsymmetric();
        return priKey;
    }
    
    /**
     * This method is used for returning the public key used for Asymmetric Cryptography.
     * @return Returns PublicKey object if it already exists or generates one and returns it.
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws NoSuchProviderException
     */
    public PublicKey getPublicKey() 
     throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException{
        if(pubKey==null)
            generateKeyPairsForAsymmetric();
        return pubKey;
    }
    
    /**
     * This method is used to Encrypt the data byte array using privateKey (Asymmetric). 
     * For Encryption, it uses algorithm as defined in EBWConstants.ENCRYPTION_ASYMMETRIC_ALGORITHM 
     * and provider as defined in    EBWConstants.ENCRYPTION_ASYMMETRIC_PROVIDER.
     * 
     * @param inputdata - Data can be anything from a String to a binary object. See converting
     *                    @see String.getBytes() for converting String to  byte array.
     * @return - Returns an Encrypted byte array. 
     *            
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[]  encryptAsymetric(byte[] inputdata) 
    	throws InvalidKeyException,IllegalBlockSizeException,BadPaddingException
    	, NoSuchPaddingException, NoSuchProviderException, NoSuchAlgorithmException{
        //EBWLogger.trace(this,"Starting method public byte[]  encryptAsymetric(byte[] inputdata)");
        //EBWLogger.trace(this," data :"+new String(inputdata));
        generateKeyPairsForAsymmetric();
        /**Initialize the Cipher Object and get it ready for
        Encrypting/Decrypting etc. **/
        cipherAsymmetric.init(Cipher.ENCRYPT_MODE,priKey);
        
        
        /**Create a byte data for encrypting.
        All inputs given to encryption are bytes. **/
        byte[] encResult = cipherAsymmetric.doFinal(inputdata);
        //EBWLogger.trace(this,"Returning from method public static String  encrypt(String data)");
        return encResult;
    }
    
/**
 * This method is used to decrypt the data in byte array. This data should be an encrypted data using
 * the private key corresponding to the public key used to encrypt the data. 
 * @param encdata
 * @return
 * @throws InvalidKeyException
 * @throws IllegalBlockSizeException
 * @throws BadPaddingException
 */    
    public byte[]  decryptAsymmetric(byte[] encdata) 
     throws InvalidKeyException,IllegalBlockSizeException,BadPaddingException, NoSuchProviderException,
     NoSuchAlgorithmException, NoSuchPaddingException{
        //EBWLogger.trace(this,"Starting method public String  decrypt(String encdata)");
        //EBWLogger.trace(this," data :"+new String(encdata));
        
        cipherAsymmetric.init(Cipher.DECRYPT_MODE,getPublicKey());
        byte[] decResult = cipherAsymmetric.doFinal(encdata);
        
        //EBWLogger.trace(this,"Returning from method public String  decrypt(String encdata)");
        return decResult;
    }
    
    /**
     * This method returns a key generated for using with Symmetric Crypto 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public Key generateKeyForSymmetric() 
    throws NoSuchAlgorithmException, NoSuchPaddingException{
        /**Create a keygenerator for Symmetric Crypto **/
    	
        if(symmetricKey==null){
            keygen = KeyGenerator.getInstance(EBWConstants.ENCRYPTION_KEYGEN_ALGORITHM);
            symmetricKey = keygen.generateKey();
            
       try {
//    	   ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("d:/UBS/AES.key")));
//    	   oos.writeObject(symmetricKey);
//    	   oos.close();
//    	   System.out.println("Key File written");
    	   ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(EBWConstants.SYMMETRIC_KEY_FILENAME)));
    	   Key symmetricKeyNew =(Key) ois.readObject();
	       //oos.close(); 
	    	//System.out.println("Key File read");
	    	symmetricKey=symmetricKeyNew;
		/*ObjectWriter bw=new ObjectWriter(new FileWriter("d:/UBS/AES.key"));
		bw.write(symmetricKey.toString());
		bw.close();
		*/
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        }else{
            //EBWLogger.logDebug(this,"Key Already set");
        }
        return symmetricKey;
    }
    
    
    public byte[]  encryptSymmetric(byte[] inputdata) 
        throws InvalidKeyException,IllegalBlockSizeException,BadPaddingException, NoSuchPaddingException, 
         NoSuchAlgorithmException{
        //EBWLogger.trace(this,"Starting method public String  encrypt(String data)");
        //EBWLogger.trace(this," data :"+new String(inputdata));

        /** Initialize cipher used for Symmetric Crypto **/
//        cipherSymmetric = Cipher.getInstance(EBWConstants.ENCRYPTION_KEYGEN_ALGORITHM);
//        cipherSymmetric.init(Cipher.ENCRYPT_MODE,generateKeyForSymmetric());
                
        try {
//        	cipherSymmetric = Cipher.getInstance(EBWConstants.ENCRYPTION_KEYGEN_ALGORITHM+"/CFB/NoPadding");
        	System.out.println("["+EBWConstants.ENCRYPTION_KEYGEN_ALGORITHM+"/"+EBWConstants.ENCRYPTION_MODE+"/"+EBWConstants.ENCRYPTION_PADDING+"]");
        	cipherSymmetric = Cipher.getInstance(EBWConstants.ENCRYPTION_KEYGEN_ALGORITHM+"/"+EBWConstants.ENCRYPTION_MODE+"/"+EBWConstants.ENCRYPTION_PADDING);
        	IvParameterSpec ivps = new IvParameterSpec(getSecretVector());
        	cipherSymmetric.init(Cipher.ENCRYPT_MODE,generateKeyForSymmetric(),ivps);
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   
        /**Create a byte data for encrypting.
        All inputs given to encryption are bytes.**/
        byte[] encResult = cipherSymmetric.doFinal(inputdata);
        //EBWLogger.trace(this,"Returning from method public static String  encrypt(String data)");
        
        return encResult;
    }
    
    
    public byte[]  decryptSymmetric(byte[] encdata) 
    	throws InvalidKeyException,IllegalBlockSizeException,BadPaddingException, NoSuchAlgorithmException
    	, NoSuchPaddingException,InvalidAlgorithmParameterException,Exception{
        //EBWLogger.trace(this,"Starting method public String  decrypt(String encdata)");
        //EBWLogger.trace(this," Encrypted data :"+new String(encdata));
        /** Initialize cipher used for Symmetric Crypto **/
        cipherSymmetric = Cipher.getInstance(EBWConstants.ENCRYPTION_KEYGEN_ALGORITHM+"/"+EBWConstants.ENCRYPTION_MODE+"/"+EBWConstants.ENCRYPTION_PADDING);
        IvParameterSpec ivps = new IvParameterSpec(getSecretVector());
    	
        cipherSymmetric.init(Cipher.DECRYPT_MODE,generateKeyForSymmetric(),ivps);
        
        byte[] decResult = cipherSymmetric.doFinal(encdata);
        
        
        //EBWLogger.trace(this,"Returning from method public String  decrypt(String encdata)");
        return decResult;
    }
   
    public void setSecretKey(Key symmetricKey){
        this.symmetricKey = symmetricKey;
    }
    
    
    public void computeMac(String fileName) throws NoSuchAlgorithmException, InvalidKeyException
    ,FileNotFoundException, IOException,NoSuchPaddingException{
        Mac mac = Mac.getInstance(EBWConstants.ENCRYPTION_MAC_ALGORITHM);
        mac.init(generateKeyForSymmetric());
        
        FileInputStream fis = new FileInputStream(fileName);
        byte[] dataBytes = new byte[1024];
        int nread = fis.read(dataBytes);
        while (nread > 0) {
          mac.update(dataBytes, 0, nread);
          nread = fis.read(dataBytes);
        };
        byte[] macbytes = mac.doFinal();
        System.out.println("MAC(in hex):: " + ByteUtil.byteArrayToHex(macbytes));
        //3e 17 56 a8 e7 19 4e cc da 87 69 ad 91 a0 b2 1a 83 3d 93 a4

    }
    
    public byte[] computeHash(byte[] data,String algorithm) throws NoSuchAlgorithmException{
        MessageDigest md =null;
        byte[] digest= null; 
        
        md = MessageDigest.getInstance(algorithm);
        
        digest = md.digest(data);
        return digest;
    }
    public SecretKeySpec getSecretKey() throws Exception{
        File imrsFile = new File(PropertyFileReader.getProperty("SYMMETRIC_KEY_FILENAME"));
        System.out.println("Created File Object for IMRS Key ");
        FileInputStream fis = new FileInputStream(imrsFile);
        System.out.println("File Inputstream created-------");
        byte[] imrsBytes = new byte[Integer.parseInt(EBWConstants.SYMMETRIC_KEY_LENGTH)];
        int readResult = fis.read(imrsBytes);
        System.out.println("IMRS Key read and stored in Byte Array");

        SecretKeySpec imrsKey = new SecretKeySpec(imrsBytes,EBWConstants.ENCRYPTION_KEYGEN_ALGORITHM);
        System.out.println("Created IMRS Key successfully ");
        return imrsKey;
      }

    
    public byte[]  getSecretVector() throws Exception{
        File VectFile = new File(EBWConstants.VECTOR_FILE_PATH);
        System.out.println("Created File Object for IMRS Vector");
        FileInputStream fis = new FileInputStream(VectFile);        
        byte[] VectBytes = new byte[Integer.parseInt(EBWConstants.VECTOR_FILE_LENGTH)];
        int readResult = fis.read(VectBytes);
        System.out.println("Secret Key Vector  read and stored in Byte Array");

        return VectBytes;
    }
}