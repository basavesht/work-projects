package com.tcs.ebw.security;

import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.tcs.ebw.common.util.FileUtil;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.common.util.StringUtil;

/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class DBUtil {
    
    public static void main(String args[]){
        try{
            System.out.println("args length :"+args.length);
            if(args.length==0 || args.length<3){
                printUsage();
            }
            
            
            String userid = args[0];
            String password = args[1];
            String type=args[2];            System.out.println("Entered Userid is :"+userid);
            System.out.println("Entered Password is :"+password);
            Class.forName(PropertyFileReader.getProperty("QueryExecutor_drivername"));
            EBWSecurity security  = new EBWSecurity();
            Connection con = DriverManager.getConnection(PropertyFileReader.getProperty("QueryExecutor_url"),(String)PropertyFileReader.getProperty("QueryExecutor_user"),(String)PropertyFileReader.getProperty("QueryExecutor_password"));
            Statement stat = con.createStatement();
            int result=0; 
            
            if(type.equalsIgnoreCase("login")){
                System.out.println("Executing query :"+"update cwbowner.ds_user set usrpwd ='"+new String(security.computeHash(password.getBytes(),"MD5"))+"' where usruserid='"+userid+"'");
                result = stat.executeUpdate("update cwbowner.ds_user set usrpwd ='"+new String(security.computeHash(password.getBytes(),"MD5"))+"' where usruserid='"+userid+"'");
            }
            else if(type.equalsIgnoreCase("transaction")){
                System.out.println("Executing query :"+"update cwbowner.ds_user set usrtranspwd ='"+new String(security.computeHash(password.getBytes(),"SHA1"))+"' where usruserid='"+userid+"'");
                result = stat.executeUpdate("update cwbowner.ds_user set usrtranspwd ='"+new String(security.computeHash(password.getBytes(),"SHA1"))+"' where usruserid='"+userid+"'");
            }
            else
                printUsage();
                
            System.out.println(result +" rows affected");
            
            if(result>0)
                System.out.println(StringUtil.initCaps(args[2])+" password Encrypted and stored Successfully for Userid: "+userid);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void updatePassword(String userid, String password,String passwordType) throws Exception{
        Class.forName(PropertyFileReader.getProperty("QueryExecutor_drivername"));
        EBWSecurity security  = new EBWSecurity();
        Connection con = DriverManager.getConnection(PropertyFileReader.getProperty("QueryExecutor_url"),(String)PropertyFileReader.getProperty("QueryExecutor_user"),(String)PropertyFileReader.getProperty("QueryExecutor_password"));
        Statement stat = con.createStatement();
        int result=0; 
        
        if(passwordType.equalsIgnoreCase("login")){
            System.out.println("Executing query :"+"update cwbowner.ds_user set usrpwd ='"+new String(security.computeHash(password.getBytes(),"MD5"))+"' where usruserid='"+userid+"'");
            result = stat.executeUpdate("update cwbowner.ds_user set usrpwd ='"+new String(security.computeHash(password.getBytes(),"MD5"))+"' where usruserid='"+userid+"'");
        }
        else if(passwordType.equalsIgnoreCase("transaction")){
            System.out.println("Executing query :"+"update cwbowner.ds_user set usrtranspwd ='"+new String(security.computeHash(password.getBytes(),"SHA1"))+"' where usruserid='"+userid+"'");
            result = stat.executeUpdate("update cwbowner.ds_user set usrtranspwd ='"+new String(security.computeHash(password.getBytes(),"SHA1"))+"' where usruserid='"+userid+"'");
        }
        
        System.out.println(result +" rows affected");
        
        if(result>0)
            System.out.println(StringUtil.initCaps(passwordType)+" password Encrypted and stored Successfully for Userid: "+userid);
        
    }

    private static void printUsage(){
        System.out.println("Usage: DBUtil <userid> <password to be encrypted> <transaction (or) login>");
        System.out.println("Eg For Login password: DBUtil TESTUSER TESTUSER login");
        System.out.println("Eg For Transaction password: DBUtil PWD123 PWD123 transaction");
        System.exit(0);
    }
}
