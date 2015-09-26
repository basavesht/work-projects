package com.tcs.ebw.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.tcs.ebw.common.util.ByteUtil;
import com.tcs.ebw.common.util.FileUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;



/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class TestSecurity {

    public static void main(String args[]){
        try{
        
            EBWSecurity security = new EBWSecurity();
            String pwd = "retlowner";//Ÿi7qÊÄ7Y\ßB•éõ
            char pwdarr[] = {'U','S','E','R','1'};
            byte[] inputdata = new String(pwd).getBytes();
            String encPwd  = new String(security.computeHash(new String(pwdarr).getBytes(),"SHA1"));
            System.out.println("Encrypted Pwd :"+encPwd);
            
            /**Test for Symmetric Crypto **/
            //System.out.println("Data for Encryption :"+new String(inputdata));
//          FileUtil.writeObjectToFile("D:\\Arun\\ebw.key",(Object)security.generateKeyForSymmetric(),false);
            //security.setKeyForSymmetric((Key)FileUtil.getObjectFromFile("d:\\Arun\\ebw.key"));
            System.out.println("Encrypted String is :"+new String(security.encryptSymmetric(inputdata)));
            //System.out.println("Decrypted Data is   :"+new String(security.decryptSymmetric(security.encryptSymmetric(inputdata))));
            //System.out.println("Decrypted Data is   :"+new String(security.decryptSymmetric("˜>FRìën¨2——@q6q".getBytes())));
            
            /** Test for Asymmetric Crypto **/
            //System.out.println("Data Encrypted :"+new String(security.encryptAsymetric(inputdata)));
            
            /** List of providers 
            Provider providers[] = Security.getProviders();
            for(int i=0;i<providers.length;i++){
             System.out.println("Provider Name :"+providers[i].getName());
             System.out.println("Provider Info : "+providers[i].getInfo());
            }**/
            
            //c3 71 bc fc 90 3f 36 43 e5 be 50 97 35 83 4f d9 d0 a0 04 58
           // security.computeMac("D:\\Arun\\Date1.js");
            
            
            
            
        }
        catch(BadPaddingException bpe){
            bpe.printStackTrace();
        }
        catch(IllegalBlockSizeException ibse){
            ibse.printStackTrace();
        }
        catch(InvalidKeyException ipe){
            ipe.printStackTrace();
        }
        catch(NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
        }
        catch(NoSuchPaddingException nspe){
            nspe.printStackTrace();
        }/*
        catch(IOException io){
            io.printStackTrace();
        }
        catch(ClassNotFoundException cnde){
            cnde.printStackTrace();
        }
        catch(NoSuchProviderException nspe){
         nspe.printStackTrace();   
        }*/ catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
}
