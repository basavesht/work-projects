package com.tcs.ebw.serverside.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.tcs.ebw.common.util.EBWLogger;
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
public class SQLCreatorHelper {

    /*
     * Stores DB Connection.
     */
    private Connection con;
    
    /**
     * Stores created statement Object used to get table metadata.
     */
    private Statement stmt;
    
    /**
     * Stores ResultSet from query execution. Used to get resultsetmetadata.
     */
    private ResultSet rs;
    
    /**
     * Stores table metadata Object.
     */
    protected ResultSetMetaData rsmd;
    
    /**
     * Fully qualified JDBC Driver class. 
     */
    private String driverClass;
    
    /**
     * Connection URL for connecting with Database.
     */
    private String url;
    
    /** 
     * Username for DB connection.
     */
    private String uid;
    
    /**
     * Password for DB Connection.
     */
    private String pwd;
    
    protected StringBuffer qryString = new StringBuffer();
    
    /**
     * Loads DB properties from Application propery file (ebw) and
     * creates a DB connection.
     * 
     * @throws Exception
     */
    private void connectToDB() throws Exception{
        driverClass = PropertyFileReader.getProperty("Prepopulator_drivername");
        url = PropertyFileReader.getProperty("Prepopulator_url");
        uid= PropertyFileReader.getProperty("Prepopulator_user");
        pwd= PropertyFileReader.getProperty("Prepopulator_password");
        
        if(con==null){
	        Class.forName(driverClass);
	        con = DriverManager.getConnection(url,uid,pwd);
        }
    }
    
    
    /**
     * Gets DB Connection using the connectToDB method and gets
     * the MetaData of the given table. 
     * 
     * @param tableName 	- Name of the table whos metadata is required.
     * @return				- LinkedHashMap in format {columnName = datatype}  
     * @throws SQLException
     * @throws Exception
     */
    public void loadTableMetaData(String tableName) throws SQLException, Exception{
     EBWLogger.logDebug(this,"Starting getTableMetaData with table Name "+tableName);
     connectToDB();
     LinkedHashMap tableInfo = new LinkedHashMap();
     
     if(con!=null){
         // to get 0 rows. Only metadata is required.
         String query = "select "+tableName+".* from "+tableName+" where 1=2";
         stmt = con.createStatement();
         rs = stmt.executeQuery(query);
         rsmd = rs.getMetaData();
         StringBuffer createdQry = new StringBuffer();
     }
     EBWLogger.logDebug(this,"Finished getTableMetaData ");
    }
  
    protected void addColumnAndValue(int i)throws SQLException,Exception{
        qryString.append("\r\n\t"+rsmd.getColumnName(i)+"=");
        
        qryString.append("#"+StringUtil.removeUnderscore(rsmd.getColumnName(i).toLowerCase())+":");
        
        if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR ||
                rsmd.getColumnType(i)==java.sql.Types.CHAR)
        	    qryString.append("varchar");
        else if(rsmd.getColumnType(i)==java.sql.Types.DATE)
            qryString.append("date");
        else if(rsmd.getColumnType(i)==java.sql.Types.DECIMAL ||
                rsmd.getColumnType(i)==java.sql.Types.INTEGER ||
                rsmd.getColumnType(i)==java.sql.Types.NUMERIC )
            qryString.append("numeric");
    }
    
    
    protected void addColumnNames(int i)throws SQLException,Exception{
        qryString.append("\r\n\t"+rsmd.getColumnName(i)+"\r\n");
    }
    
    protected void addColumnValues(int i)throws SQLException,Exception{
        String colname = rsmd.getColumnName(i).toLowerCase();
        if(colname.indexOf("_")>-1)
            colname = StringUtil.removeUnderscore(colname);
        
            
        qryString.append("\t#"+colname+":");
        
        if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR ||
                rsmd.getColumnType(i)==java.sql.Types.CHAR)
        	    qryString.append("varchar");
        else if(rsmd.getColumnType(i)==java.sql.Types.DATE)
            qryString.append("date");
        else if(rsmd.getColumnType(i)==java.sql.Types.DECIMAL ||
                rsmd.getColumnType(i)==java.sql.Types.INTEGER ||
                rsmd.getColumnType(i)==java.sql.Types.NUMERIC )
            qryString.append("numeric");
    }
    
    
    protected ArrayList getPrimaryKeyCols(String tableName) throws SQLException, Exception{
        ArrayList pkKeys = new ArrayList();
        StringBuffer queryBuffer = new StringBuffer();
        queryBuffer.append("select a.column_name from user_cons_columns a, user_constraints b ");
        queryBuffer.append("where a.constraint_name = b.constraint_name and ");
        queryBuffer.append("a.table_name = b.table_name and ");
        queryBuffer.append("b.table_name = '"+tableName.toUpperCase()+"' and b.constraint_type = 'P'");
        
        EBWLogger.logDebug(this,"queryBuffer :"+queryBuffer.toString());
        
        ResultSet newRs =null;
        Statement newStmt = con.createStatement();
        if(stmt!=null)
            newRs = newStmt.executeQuery(queryBuffer.toString());
        
        if(newRs!=null){
            while(newRs.next())
            pkKeys.add(newRs.getString(1));
        }
        return pkKeys;
    }
    
    
    protected void addTOColumnMap(int i) throws SQLException{
        qryString.append("\r\n\t"+rsmd.getColumnName(i)+"=");
        qryString.append(StringUtil.removeUnderscore(rsmd.getColumnName(i).toLowerCase()));
    }
    
    
    protected void closeAll() throws SQLException{
        rs.close();
        rs = null;
        rsmd=null;
        con.close();
        con=null;
    }
}
