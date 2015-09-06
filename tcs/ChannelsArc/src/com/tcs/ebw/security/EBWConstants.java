package com.tcs.ebw.security;
import com.tcs.ebw.common.util.PropertyFileReader;
import java.util.MissingResourceException;
import com.tcs.ebw.common.util.EBWLogger;
/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class EBWConstants {
//   public static final String ENCRYPTION_KEYGEN_ALGORITHM = "DES";
//   public static final String ENCRYPTION_ASYMMETRIC_ALGORITHM = "DSA";
//   public static final String ENCRYPTION_ASYMMETRIC_PROVIDER ="BC"; 
//   public static final String ENCRYPTION_MAC_ALGORITHM = "HmacSHA1";
//   public static final String ENCRYPTION_SECRETKEY_FILENAME = "D:\\Arun\\ebw.key";

	   public static String ENCRYPTION_KEYGEN_ALGORITHM=null;
	   public static String ENCRYPTION_ASYMMETRIC_ALGORITHM =null;
	   public static String ENCRYPTION_ASYMMETRIC_PROVIDER =null; 
	   public static String ENCRYPTION_MAC_ALGORITHM = null;
	   public static String ENCRYPTION_SECRETKEY_FILENAME = null;
	   public static String SYMMETRIC_KEY_FILENAME=null;
	   public static String VECTOR_FILE_PATH=null;
	   public static String ENCRYPTION_MODE=null;
	   public static String ENCRYPTION_PADDING=null;
	   public static String SYMMETRIC_KEY_LENGTH=null;
	   public static String VECTOR_FILE_LENGTH=null;
	   
 public static void populateConstants() throws Exception{
	try{
	   ENCRYPTION_KEYGEN_ALGORITHM =PropertyFileReader.getProperty("ENCRYPTION_KEYGEN_ALGORITHM");
	   ENCRYPTION_ASYMMETRIC_ALGORITHM =PropertyFileReader.getProperty("ENCRYPTION_ASYMMETRIC_ALGORITHM");
	   ENCRYPTION_ASYMMETRIC_PROVIDER =PropertyFileReader.getProperty("ENCRYPTION_ASYMMETRIC_PROVIDER"); 
	   ENCRYPTION_MAC_ALGORITHM = PropertyFileReader.getProperty("ENCRYPTION_MAC_ALGORITHM");
	   ENCRYPTION_SECRETKEY_FILENAME = PropertyFileReader.getProperty("ENCRYPTION_SECRETKEY_FILENAME");
	   SYMMETRIC_KEY_FILENAME=PropertyFileReader.getProperty("SYMMETRIC_KEY_FILENAME");
	   VECTOR_FILE_PATH=PropertyFileReader.getProperty("VECTOR_FILE_PATH");
	   ENCRYPTION_MODE=PropertyFileReader.getProperty("ENCRYPTION_MODE");
	   ENCRYPTION_PADDING=PropertyFileReader.getProperty("ENCRYPTION_PADDING");
	   SYMMETRIC_KEY_LENGTH=PropertyFileReader.getProperty("SYMMETRIC_KEY_LENGTH");
	   VECTOR_FILE_LENGTH=PropertyFileReader.getProperty("VECTOR_FILE_LENGTH");
		}catch(MissingResourceException mre){
			EBWLogger.logError("EBWConstants", "##ERROR:"+mre.getMessage());
			throw mre;
		}	catch(Exception e){
		EBWLogger.logError("EBWConstants", "##ERROR:"+e.getMessage());
		throw e;
		}
 }
 
}
