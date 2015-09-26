package com.tcs.ebw.serverside.query;


//import com.tcs.ebw.trade.transferobject.EbRdcBuysellReqTO;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.GregorianCalendar;


/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class TestQueryExecutor {

    
    public static void main(String args[]){
        try{
	    /*EbRdcBuysellReqTO buysellto = new EbRdcBuysellReqTO();
	    buysellto.setRefid("MFB000000000721");
	    
	    QueryExecutor query = new QueryExecutor();
	    EBWLogger.logDebug("com.tcs.","Test");*/
        
        //query.setConnection()
        
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@172.19.24.206:1521:EBW","cwbowner","cwbowner");
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        st.setMaxRows(5);
        st.setFetchSize(5);
        
        ResultSet rs = st.executeQuery("Select * from ds_domains");
        //st.setFetchSize(100);
        
        GregorianCalendar dt = new GregorianCalendar();
        
        
        while(rs.next())
            System.out.println(rs.getString(1));
        
        rs = st.executeQuery("Select * from ds_domains");
        rs.absolute(5);
        
        while(rs.next())
            System.out.println(rs.getString(1));
        
	        System.out.println("Default St Fetch size"+st.getFetchSize());
	        System.out.println("Default rs Fetch size"+rs.getFetchSize());
	        rs.close();
	        con.close();
        
        }catch(Exception e){
            e.printStackTrace();
        }
	    
	}
}
