package com.tcs.ebw.serverside.services;

import java.sql.ResultSet;
import java.util.MissingResourceException;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.serverside.query.QueryExecutor;
import com.tcs.ebw.serverside.services.DatabaseService;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class AuditLogger extends QueryExecutor{
    
    public void logAudit(String stmtName) throws SQLException{
    EBWLogger.logDebug(this,"Statement -  Into Auditing");
    //Connection con = (Connection)serviceConnection;
    try{    
        String auditlogging = PropertyFileReader.getProperty("audit_logging");
	    if(auditlogging.equalsIgnoreCase("true")){
	        
		    Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
	                ResultSet.CONCUR_UPDATABLE);
		    
		    String Audit_Query = "";
		    PropertyFileReader.getProperty(stmtName+"Query_Audit");
			if(PropertyFileReader.getProperty("audit_logging").equalsIgnoreCase("true"))
			    Audit_Query  = queryRB.getString(stmtName+QUERY+AUDIT).trim();
			
		    EBWLogger.logDebug(this,"Statement - query.toUpperCase().startsWith('INSERT')"+Audit_Query.startsWith("INSERT"));
		    Audit_Query = Audit_Query.toUpperCase();
		        EBWLogger.logDebug(this," Audit_Query :"+Audit_Query);
		        stmt.executeUpdate(Audit_Query);
		}
    }catch(MissingResourceException mre){
        EBWLogger.trace(this,"Auditing is not enabled for this query");
    }
    catch(Exception ex){
        
    }
    }
}
