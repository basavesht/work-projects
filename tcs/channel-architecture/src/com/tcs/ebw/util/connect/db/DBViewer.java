/*
 * Created on Mar 17, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.util.connect.db;

import java.util.ResourceBundle;
import java.sql.*;

/**
 * @author TCS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DBViewer {
    
    Statement statement;
    int col_len;

    /**
     * 
     */
    public DBViewer() {
        super();
        try {
	        ResourceBundle dbResource = ResourceBundle.getBundle("com.tcs.ebw.util.connect.db.db");
		    String url = dbResource.getString("url");
		    String user = dbResource.getString("user");
		    String pwd = dbResource.getString("password"); 
		    col_len = Integer.parseInt(dbResource.getString("col.len"));
			Class.forName(dbResource.getString("drivername"));
			Connection connection = DriverManager.getConnection(url, user, pwd);
			statement = connection.createStatement();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        
    }
    
    public void execute(String str) {
        try {
	        if (str.toLowerCase().startsWith("select") || str.toLowerCase().startsWith("desc")) {
	            String newLine;
	            if (str.toLowerCase().startsWith("desc")) {
	                System.out.println (str.substring(5));
	                str = "select * from " + str.substring(5) + " where 1=2";
	                newLine = "\r\n";
	            } else {
	                newLine = "";
	            }
	            
	            ResultSet rs = statement.executeQuery(str);
	            ResultSetMetaData rsmd = rs.getMetaData();
	            int cols = rsmd.getColumnCount();
	            
	            for (int i=1; i<=cols; i++) {
	                System.out.print (rpad(rsmd.getColumnName(i),col_len));
	                System.out.print (newLine);
	            }
	            System.out.println ("");
	            while (rs.next()) {
		            for (int i=1; i<=cols; i++) {
		                System.out.print (rpad(rs.getString(i),col_len));
		            }
		            System.out.println ("");
	            }
	        } else {
	            System.out.println ("Executed : " + statement.executeUpdate(str));
	        }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    
    public static String rpad(String str, int len) {
        if (str == null) str="";
        StringBuffer strB = new StringBuffer(str);
        int addSpace = 0;
        if (str.length() < len) {
            addSpace = len - str.length();
            for (int i=0; i < addSpace; i++) {
                strB.append (" ");
            }
        }
        return strB.toString();
    }
    
    public static void main(String[] args) {
        DBViewer db = new DBViewer();
        if (args.length > 0)
            db.execute(args[0]);
        else
            /*db.execute("select  distinct UPGPROGRAMID, upgprogramname " +
 		   " from    CWBOWNER.DS_FAP_PGM_ACTION fap, " +
 		  "    CWBOWNER.DS_USER_FAP_DAP_LINK lnk, " +
 		 "    CWBOWNER.DS_PROGRAMS pgm,  " +
 		" CWBOWNER.DS_DOMAINS domain " +
 		"  where   UFAGRPID = '#RETGRP#' " +
 		"  and     UFDGRPID= UFAGRPID  " +
 		"  and     UFDUSERID = 'RDCSUPER1' " +
 		"  and     UFAFAPID = UFDFAPID " +
 		"  and     UPGPROGRAMID = UFAPGMID " +
 		"  and     lnk.deleteflag='N' " +
 		"  and     fap.deleteflag='N' " +
 		"  and     pgm.deleteflag='N' " +
 		"          and     upgmenuid is not null " +
 		"  AND 	   DMNdomain = 'D_ApplicationArea' " +
 		"  AND 	   dmndomainval = upgprogapplarea");*/
        //db.execute("alter table idgenerator add AttributeType varchar2(20)");
            //db.execute("select CMTLONGNAME1 from ds_cust_master");
        /*db.execute("SELECT DISTINCT udafieldname, udafieldvalue || ' - ' || (select CMTLONGNAME1 from ds_cust_master where CMTQLCUSTID = acctcd.udafieldvalue) as udafieldvalue " +
            " FROM    ds_dap_acct_cd acctcd " +
            " WHERE   UPPER(acctcd.deleteflag) = 'N' " +
            " AND    udafieldname =  'Customer ID' " +
            " AND    udadapid     IN (   SELECT distinct ufddapid " +
              "                         FROM    ds_user_fap_dap_link link " +
               "                        WHERE   UPPER(link.deleteflag) = 'N' " +
                "                       AND     ufdgrpid  = '#RETGRP#' " +
                 "                      AND     ufduserid = 'RDCSUPER1' ) " +
            " AND    udagrpid     =  '#RETGRP#'  ");*/
        
       db.execute("desc DS_PAYEE_REF");
        //db.execute("update ds_programs set UPGPROGAPPLAREA='TRAD' where UPGPROGRAMID LIKE 'SellOrder%'");
        //db.execute("Select * from ds_programs");
        //db.execute("insert into ds_domains (DMNDOMAIN,DMNDOMAINVAL,DMNDOMAINDESC,DMNDOMAINVALDESC) " +
        	//" values ('D_ApplicationArea','SAS','Application Area','System Admin')");
        //db.execute("select * from ds_domains order by 1");
        //db.execute("alter table ds_programs modify UPGPROGRAMID varchar2(20)");
        //db.execute("insert into DS_programs (UPGPROGRAMID,UPGPROGRAMNAME,UPGPROGRAMTYPE,deleteflag) values(" +
            	//	"'SellOrderOptions','Sell Order Options','R','N')");
        //db.execute("insert into ds_fap_pgm_action values(" +
            	//	"'#RETGRP#','RDCFAP','SellOrderEntry','btnPDFReport','CWBUSER',sysdate, 'CWBUSER', sysdate, 'N', 1,201,1)");
        // db.execute("insert into ds_dap_acct_cd (UDADAPID, UDAGRPID, UDAFIELDNAME, UDAFIELDVALUE, DELETEFLAG, VERSIONNUM)" +
          	//	" values('BNKDAPSUP','#RETGRP#','Customer ID','929','N',1)");  
        //db.execute("select tot.*, '' as buy, '' as sell from scott.TOT_EQUITY_HOLD tot");
       // db.execute("select * from ds_dap_acct_cd");
        //db.execute("select * from ds_fap_pgm_action");
            //db.execute("update DS_programs set upgmenuid='M002' where UPGPROGRAMID='SellOrderOptions'");
    }
}
