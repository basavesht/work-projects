// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 4/9/2009 10:55:33 AM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   QueryExecutor.java

package com.tcs.ebw.serverside.query;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import oracle.sql.CLOB;
import oracle.sql.DATE;
import oracle.sql.TIMESTAMP;

import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.FileUtil;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.common.util.StringUtil;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.transferobject.EBWTransferObject;

// Referenced classes of package com.tcs.ebw.serverside.query:
//            EBWStatement, Prepopulator, NullObj

public class QueryExecutor
{
    public QueryExecutor()
    {
        cls = null;
        queryRB = null;
        values = null;
        strStmtType = null;
        STMT_PREP = "PreparedStatement";
        STMT_ONLY = "Statement";
        parameterCount = 0;
        filePath = null;
        fileLocation = null;
        ExternalisedStatement = "false";
    }

    private void init(String strStmtName)
        throws EbwException
    {
        //EBWLogger.trace("QueryExecutor :", (new StringBuilder("Starting method init(String ")).append(strStmtName).append(")").toString());
        try
        {
          
            if(PropertyFileReader.getProperty("isExternalisedStatements") == null)
            {
                ExternalisedStatement = "false";
                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("ExternalisedStatement by dafault from QueryExcutor is ")).append(ExternalisedStatement).toString());
            } else
            {
                ExternalisedStatement = PropertyFileReader.getProperty("isExternalisedStatements");
                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("ExternalisedStatement from ebw propertes file is :")).append(ExternalisedStatement).toString());
            }
            strStmtType = getStmtType((new StringBuilder(String.valueOf(strStmtName))).append(".StmtType").toString());
            if(strStmtType == null)
                strStmtType = STMT_ONLY;
            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Statement Type is ")).append(strStmtType).append(" and Statement name is ").append(strStmtName).toString());
        }
        catch(MissingResourceException mre)
        {
            strStmtType = STMT_ONLY;
        }
        catch(SQLException sqlEx)
        {
            throw new EbwException(this, sqlEx);
        }
        catch(Exception e)
        {
            strStmtType = STMT_ONLY;
        }
        //EBWLogger.trace("QueryExecutor :", "Exiting from init");
    }

    private String getQueryValue(String key, boolean isMandatory)
        throws EbwException
    {
        String sqlStmt = null;
        try
        {
            //EBWLogger.trace("QueryExecutor :", (new StringBuilder("Starting Method getQueryValue(")).append(key).append(")").toString());
            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Externalised stmt ")).append(ExternalisedStatement).toString());
            if(ExternalisedStatement.equalsIgnoreCase("true"))
            {
                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query ")).append(queryRB.getString(key)).toString());
                sqlStmt = queryRB.getString(key).trim();
                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Value of the sqlStmt from ResouceBundle is :")).append(sqlStmt).toString());
            } else
            {
                sqlStmt = linkedHash.get(key).toString().trim();
            }
           // EBWLogger.trace("QueryExecutor :", "Exit from Method getQueryValue");
        }
        catch(MissingResourceException e)
        {
            if(isMandatory)
            {
                //EBWLogger.logDebug("QueryExecutor :", e.getMessage());
                throw new EbwException(this, e);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new EbwException(this, e);
        }
        return sqlStmt;
    }

    public String getStmtType(String key)
        throws MissingResourceException
    {
        String strStmtType = "";
        try
        {
            if(ExternalisedStatement.equalsIgnoreCase("true"))
            {
                queryRB = ResourceBundle.getBundle("Statement");
                strStmtType = queryRB.getString(key);
            } else
            {
                strStmtType = linkedHash.get(key).toString().trim();
            }
        }
        catch(MissingResourceException mse)
        {
            strStmtType = STMT_ONLY;
        }
        catch(Exception e)
        {
            strStmtType = STMT_ONLY;
        }
        return strStmtType;
    }

    public Object executeQuery(String stmtName, Object obj)
        throws Exception
    {
        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Starting Method executeQuery(")).append(stmtName).append(",").append(obj).append(")").toString());
        Object opObj = null;
        init(stmtName);
        String key1 = (new StringBuilder(String.valueOf(stmtName))).append(".OutputType").toString();
        String sqlStmt = "";
        try
        {
            sqlStmt = getQueryValue((new StringBuilder(String.valueOf(stmtName))).append(".Query").toString(), true);
            //sqlStmt=getVersionNumQuery(sqlStmt);
            String query = getQueryString(sqlStmt, obj);
            if(EBWLogger.isDebugEnabled()){
            EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("The Latest Query Generated is :-")).append(query).toString());
            }
            ResultSet resultSet = null;
            Statement stmt = null;
            PreparedStatement preStmt = null;
            boolean issqlj = isSQLJ(stmtName);
            if(!issqlj)
            {
                strStmtType = strStmtType.trim();
                if(strStmtType.equalsIgnoreCase(STMT_ONLY))
                {
                    stmt = con.createStatement();
                    resultSet = stmt.executeQuery(query);
                } else
                if(strStmtType.equalsIgnoreCase(STMT_PREP))
                {
                    preStmt = con.prepareStatement(query);
                    SetPreparedStmtValues(parameterCount, preStmt);
                    resultSet = preStmt.executeQuery();
                }
                if(EBWLogger.isDebugEnabled()){
                EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query ")).append(stmtName).append(" has been executed successfully through ").append(strStmtType).toString());
                }
            } else
            {
                String fileName = StringUtil.initCaps(stmtName);
                //createSQLJFile(fileName, query);
              //  translateSQLJFile();
                Class cls = Class.forName((new StringBuilder(String.valueOf(getClass().getPackage().getName()))).append(".").append(fileName).toString());
                Object ob = cls.newInstance();
                Class clss[] = {
                		java.sql.Connection.class
                };
                Method method = cls.getMethod("executeQuery", clss);
                Object objArr[] = {
                    con
                };
                resultSet = (ResultSet)method.invoke(ob, objArr);
            }
            opObj = getOutputObject(stmtName, resultSet, key1);
            closeStatement(stmt, resultSet);
            closePreparedStatement(preStmt, resultSet);
        }
        catch(MissingResourceException mre)
        {
            throw new EbwException(this, mre);
        }
        catch(SQLException sqlE)
        {
            sqlE.printStackTrace();
            throw new EbwException(this, sqlE);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
       
        return opObj;
    }

    public Object executeQuery(String stmtName, Object obj1, HashMap paginationMap)
        throws Exception
    {
        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Starting Method executeQuery(")).append(stmtName).append(",").append(obj1).append(")").toString());
        Object opObj = null;
        init(stmtName);
        String key1 = null;
        String sqlStmt = "";
        Object obj = obj1;
        rowsPerPage = null;
        String PaginationIndex = null;
        String ConditionKey = null;
        HashMap toGetQueryStrMap = new HashMap();
        if(paginationMap != null)
        {
            Set set = new HashSet();
            set = paginationMap.keySet();
            String array[] = (String[])set.toArray(new String[set.size()]);
            for(int i = 0; i < array.length; i++)
            {
                if(array[i].equalsIgnoreCase("RowsPerPage"))
                    rowsPerPage = (String)paginationMap.get("RowsPerPage");
                if(array[i].equalsIgnoreCase("PaginationIndex"))
                    PaginationIndex = (String)paginationMap.get("PaginationIndex");
                if(array[i].equalsIgnoreCase("restartlogic"))
                {
                    String conditionKey = (String)paginationMap.get("restartlogic");
                    String restartlogicInQuery = null;
                    restartlogicInQuery = conditionKey;
                    toGetQueryStrMap = populatePgnMap(restartlogicInQuery);
                }
            }

        }
        try
        {
            if(!paginationMap.containsKey("export") && rowsPerPage != null && rowsPerPage.length() > 0)
                stmtName = (new StringBuilder(String.valueOf(stmtName))).append("_page").toString();
            HashMap exportMap = new HashMap();
            boolean exportFlag = false;
            if(paginationMap.containsKey("export"))
            {
                exportMap = (HashMap)(HashMap)paginationMap.get("export");
                if(exportMap.containsKey("export") && ((String)exportMap.get("export")).equalsIgnoreCase("y"))
                {
                    String conditionKey = (String)exportMap.get("restartlogic");
                    String restartlogicInQuery = null;
                    restartlogicInQuery = conditionKey;
                    toGetQueryStrMap = populatePgnMap(restartlogicInQuery);
                    exportFlag = true;
                    stmtName = (new StringBuilder(String.valueOf(stmtName))).append("_page").toString();
                }
            }
            key1 = (new StringBuilder(String.valueOf(stmtName))).append(".OutputType").toString();
            sqlStmt = getQueryValue((new StringBuilder(String.valueOf(stmtName))).append(".Query").toString(), true);
            //sqlStmt=getVersionNumQuery(sqlStmt);
            String query = getQueryString(sqlStmt, obj);
            query = getInquery(query);
            if(query != null && query.indexOf("CONDITIONLOGIC:VARCHAR") > -1)
            {
                String conditionlogic = "CONDITIONLOGIC:VARCHAR";
                query = query.replaceAll(conditionlogic, conditionlogic.toLowerCase());
            }
            if(query != null && query.indexOf("SORTLOGIC:VARCHAR") > -1)
            {
                String sortlogic = "SORTLOGIC:VARCHAR";
                query = query.replaceAll(sortlogic, sortlogic.toLowerCase());
            }
            if(exportFlag)
            {
            	//query=query.toLowerCase();
            	if(query.toLowerCase().contains("fetch")){
                	query=rowsinpage(query);
                }else{
                query = query.substring(0, query.lastIndexOf("+"));
                query = (new StringBuilder(String.valueOf(query))).append(")").toString();
                }
            	
            }
            if(rowsPerPage != null && rowsPerPage.length() > 0 || exportFlag)
                query = getQueryString(query, toGetQueryStrMap);
           // for(; query != null && query.indexOf("   ") > -1; query = query.replaceAll("   ", " "));
            if(query != null && query.toLowerCase().indexOf("where order by") != -1){
            	query = query.replaceAll("WHERE order by", "order by");
                query = query.replaceAll("where order by", "order by");
            }
            //modified by krishna on 10-04-2009 specific to db2 pagination
            try{
            	String ANDFETCH="AND fetch";
            	/*String SPACEQUERY[]=query.split(" ");
            	String query1="";
            	for(int l=0;l<SPACEQUERY.length;l++){
            		query1=query1+" "+SPACEQUERY[l].trim();
            		//System.out.println("query1 is......."+query1);
            	}
            	query=query1.trim();*/
            	                                 
				if(query!= null & query.indexOf("escape'+' escape'+' escape'+' escape'+' escape'+'")!=-1){
                	query = query.replace("escape'+' escape'+' escape'+' escape'+' escape'+'", "");
                } 
            	if(query!= null & query.indexOf("escape'+' escape'+' escape'+' escape'+'")!=-1){
            		query = query.replace("escape'+' escape'+' escape'+' escape'+'", "");
            		
            	}
            	if(query!= null & query.indexOf("escape'+'  escape'+' escape'+' escape'+'  escape'+'")!=-1){
                	query = query.replace("escape'+'  escape'+' escape'+' escape'+'  escape'+'", "");
                } 

            	query=query.replaceAll("where  fetch","fetch");
            	query=query.replaceAll("WHERE  FETCH"," fetch ");
            	query=query.replaceAll("WHERE  fetch"," fetch ");
            	query=query.replaceAll("where  FETCH"," fetch ");
            	query=query.replaceAll("WHERE  order by","order by");
            	query=query.replaceAll("where  order by","order by");
            	query=query.replaceAll("AND  fetch","fetch");
            	query=query.replaceAll("AND  FETCH"," fetch ");
            	query=query.replaceAll("and  FETCH"," fetch ");
            	query=query.replaceAll("and  fetch"," fetch ");
            	query = query.replaceAll("WHERE  AND order by", "order by");
            	query = query.replaceAll("WHERE  and order by", "order by");
            	query = query.replaceAll("AND  order by", "order by");
            	query = query.replaceAll("and  order by", "order by");
            	
            	  query = query.replaceAll("\\( AND\\)", "");
            	  query = query.replaceAll("\\(AND\\)", "");
                  query = query.replaceAll("AND  AND", "AND ");
                  query = query.replaceAll("AND \\)", ")");
                  query = query.replaceAll("AND  \\)", ")");
            	query=query.replaceAll("AND \\( OR \\)"," ");
            	query=query.replaceAll("AND \\( OR\\)"," "); 
            	query=query.replaceAll("AND \\(   \\)","");
            	query=query.replaceAll("AND \\(  \\)","");
            	query=query.replaceAll("AND \\( \\)","");
            	query=query.replaceAll("AND \\( \\)","");
            	query=query.replaceAll(" ANDAND "," AND ");
            	query=query.replaceAll("AND AND"," AND ");
            	
            	

            	query=query.replaceAll("where fetch","fetch");
            	query=query.replaceAll("WHERE FETCH"," fetch ");
            	query=query.replaceAll("WHERE fetch"," fetch ");
            	query=query.replaceAll("where FETCH"," fetch ");
            	query=query.replaceAll("AND fetch","fetch");
            	query=query.replaceAll("AND FETCH"," fetch ");
            	query=query.replaceAll("and FETCH"," fetch ");
            	query=query.replaceAll("and fetch"," fetch ");   
            	
            	 if(query != null && query.indexOf("WHERE AND order by") != -1)
                     query = query.replaceAll("WHERE AND order by", "order by");
                 
                 if(query != null && query.indexOf("WHERE and order by") != -1)
                     query = query.replaceAll("WHERE and order by", "order by");
                 
                 if(query != null && query.indexOf("AND order by") != -1)
                     query = query.replaceAll("AND order by", "order by");
                 
                 if(query != null && query.indexOf("and order by") != -1)
                     query = query.replaceAll("and order by", "order by");
                 
                 query = query.replaceAll("WHERE  AND ", "WHERE ");
                
                 
               
                 
                 
               /*  if(exportFlag)
                 {
                	 query = query.toUpperCase();
                 }*/
                 
                 
            }catch(Exception e){
            	e.printStackTrace();
            }
            EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("The Latest Query Generated for pagination is :-")).append(query).toString());
            ResultSet resultSet = null;
            Statement stmt = null;
            PreparedStatement preStmt = null;
            boolean issqlj = isSQLJ(stmtName);
            if(!issqlj)
            {
                strStmtType = strStmtType.trim();
                if(strStmtType.equalsIgnoreCase(STMT_ONLY))
                {
                    stmt = con.createStatement();
                    resultSet = stmt.executeQuery(query);
                } else
                if(strStmtType.equalsIgnoreCase(STMT_PREP))
                {
                    preStmt = con.prepareStatement(query);
                    SetPreparedStmtValues(parameterCount, preStmt);
                    resultSet = preStmt.executeQuery();
                }
                if(EBWLogger.isDebugEnabled()){
                EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query ")).append(stmtName).append(" has been executed successfully through ").append(strStmtType).toString());
                }
            } else
            {
                String fileName = StringUtil.initCaps(stmtName);
                createSQLJFile(fileName, query);
                translateSQLJFile();
                Class cls = Class.forName((new StringBuilder(String.valueOf(getClass().getPackage().getName()))).append(".").append(fileName).toString());
                Object ob = cls.newInstance();
                Class clss[] = {
                		java.sql.Connection.class
                };
                Method method = cls.getMethod("executeQuery", clss);
                Object objArr[] = {
                    con
                };
                resultSet = (ResultSet)method.invoke(ob, objArr);
            }
            opObj = getOutputObject(stmtName, resultSet, key1);
            closeStatement(stmt, resultSet);
        }
        catch(MissingResourceException mre)
        {
            throw new EbwException(this, mre);
        }
        catch(SQLException sqlEx)
        {
            sqlEx.printStackTrace();
            throw new EbwException("SQLException :", sqlEx);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        EBWLogger.trace("QueryExecutor :", "Exiting from executeQuery ");
        return opObj;
    }

    private String getInquery(String query) {
		// TODO Auto-generated method stub
    	String OrigQuery = query;
    	String ModQuery = "";
    	try{
    		
    	if(query.indexOf(" in ('%')")!=-1 ||query.indexOf(" IN ('%')")!=-1 ||
    			query.indexOf(" in('%')")!=-1 ||query.indexOf(" IN('%')")!=-1 ){
    		query = query.replaceAll("in \\('%'\\)","IN('%')");
        	query = query.replaceAll("in\\('%'\\)","IN('%')");
        	query = query.replaceAll("IN \\('%'\\)","IN('%')");
    		
        	String[] inClauseSplit = query.split("IN\\('%'\\)");
    		for (int i = 0; i < inClauseSplit.length; i++) {
    			String TempToken = inClauseSplit[i].trim();
    			if(i < inClauseSplit.length-1){
    				//ModQuery = ModQuery+ TempToken;
    				ModQuery = ModQuery+ TempToken.substring(0,TempToken.lastIndexOf(" "));
    			}else{
    			
    			ModQuery = ModQuery+ TempToken;
    			}
				
			}
    		query = ModQuery;
    		
    	}
    	}catch(Exception e){
    		query= OrigQuery;
    	}
		return query;
	}

	private String rowsinpage(String query) {
		// TODO Auto-generated method stub
    	
    	int fetchindex=query.toLowerCase().indexOf(" fetch first ")+13;
    	String rowsperpage=query.substring(fetchindex,query.toLowerCase().indexOf(" rows only")).trim();
    	int rows=Integer.parseInt(rowsperpage.trim());
    	rows=rows-1;
    	query=query.substring(0,fetchindex)+rows+" rows only";   	
    	
		return query;
	}

/*	public Object executeQuery(String stmtName, Object obj1, HashMap paginationMap, String twotable)        throws Exception
    {
        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Starting Method executeQuery(")).append(stmtName).append(",").append(obj1).append(")").toString());
        Object opObj = null;
        init(stmtName);
        String sqlStmt = "";
        Object obj = obj1;
        String rowsPerPage = null;
        String PaginationIndex = null;
        String paginationFlag = null;
        String ConditionKey = null;
        HashMap toGetQueryStrMap = new HashMap();
        boolean exportFlag = false;
        String key1 = null;
        if(paginationMap != null)
        {
            HashMap temp = new HashMap();
            temp = (HashMap)(HashMap)paginationMap.get(stmtName);
            Set set = new HashSet();
            set = temp.keySet();
            String array[] = (String[])set.toArray(new String[set.size()]);
            //for(int z = 0; z < array.length; z++)
                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder(" In queryexecutor array ")).append(z).append(":").append(array[z]).toString());

            if(temp != null)
            {
                for(int i = 0; i < array.length; i++)
                {
                    if(array[i].equalsIgnoreCase("RowsPerPage"))
                        rowsPerPage = (String)temp.get("RowsPerPage");
                    if(array[i].equalsIgnoreCase("PaginationFlag"))
                        paginationFlag = (String)temp.get("PaginationFlag");
                    if(array[i].equalsIgnoreCase("PaginationIndex"))
                        PaginationIndex = (String)temp.get("PaginationIndex");
                    if(array[i].equalsIgnoreCase("restartlogic"))
                    {
                        String conditionKey = (String)temp.get("restartlogic");
                        String restartlogicInQuery = null;
                        restartlogicInQuery = conditionKey;
                        toGetQueryStrMap = populatePgnMap(restartlogicInQuery);
                    }
                }

            }
            if(!temp.containsKey("export") && rowsPerPage != null && rowsPerPage.length() > 0)
                stmtName = (new StringBuilder(String.valueOf(stmtName))).append("_page").toString();
            HashMap exportMap = new HashMap();
            if(temp.containsKey("export"))
            {
                exportMap = (HashMap)(HashMap)temp.get("export");
                if(exportMap.containsKey("export") && ((String)exportMap.get("export")).equalsIgnoreCase("y"))
                {
                    String conditionKey = (String)exportMap.get("restartlogic");
                    String restartlogicInQuery = null;
                    restartlogicInQuery = conditionKey;
                    toGetQueryStrMap = populatePgnMap(restartlogicInQuery);
                    exportFlag = true;
                    stmtName = (new StringBuilder(String.valueOf(stmtName))).append("_page").toString();
                }
            }
        }
        key1 = (new StringBuilder(String.valueOf(stmtName))).append(".OutputType").toString();
        try
        {
            sqlStmt = getQueryValue((new StringBuilder(String.valueOf(stmtName))).append(".Query").toString(), true);
            String query = getQueryString(sqlStmt, obj);
            if(exportFlag)
            {
                query = query.substring(0, query.lastIndexOf("+"));
                query = (new StringBuilder(String.valueOf(query))).append(")").toString();
            }
            if(rowsPerPage != null && rowsPerPage.length() > 0 || exportFlag)
                query = getQueryString(query, toGetQueryStrMap);
            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query to be executed for pagination is :")).append(query).toString());
            ResultSet resultSet = null;
            Statement stmt = null;
            PreparedStatement preStmt = null;
            boolean issqlj = isSQLJ(stmtName);
            if(!issqlj)
            {
                strStmtType = strStmtType.trim();
                if(strStmtType.equalsIgnoreCase(STMT_ONLY))
                {
                    stmt = con.createStatement();
                    resultSet = stmt.executeQuery(query);
                } else
                if(strStmtType.equalsIgnoreCase(STMT_PREP))
                {
                    preStmt = con.prepareStatement(query);
                    SetPreparedStmtValues(parameterCount, preStmt);
                    resultSet = preStmt.executeQuery();
                }
                EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query ")).append(stmtName).append(" has been executed successfully through ").append(strStmtType).toString());
            } else
            {
                String fileName = StringUtil.initCaps(stmtName);
                createSQLJFile(fileName, query);
                translateSQLJFile();
                Class cls = Class.forName((new StringBuilder(String.valueOf(getClass().getPackage().getName()))).append(".").append(fileName).toString());
                Object ob = cls.newInstance();
                Class clss[] = {
                		java.sql.Connection.class
                };
                Method method = cls.getMethod("executeQuery", clss);
                Object objArr[] = {
                    con
                };
                resultSet = (ResultSet)method.invoke(ob, objArr);
            }
            opObj = getOutputObject(stmtName, resultSet, key1);
            closeStatement(stmt, resultSet);
        }
        catch(MissingResourceException mre)
        {
            throw new EbwException(this, mre);
        }
        catch(SQLException sqlEx)
        {
            sqlEx.printStackTrace();
            throw new EbwException(this, sqlEx);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        //EBWLogger.trace("QueryExecutor :", "Exiting from executeQuery(String stmtName, Object obj1, HashMap paginationMap, String twotable) ");
        return opObj;
    }*/

    public HashMap populatePgnMap(String restartlogicInQuery)
    {
        HashMap toGetQueryStrMap = new HashMap();
        String where = null;
        String order = null;
        if(restartlogicInQuery.indexOf("order by") != -1)
        {
            where = restartlogicInQuery.substring(0, restartlogicInQuery.indexOf("order by"));
            order = restartlogicInQuery.substring(restartlogicInQuery.indexOf("order by"), restartlogicInQuery.length());
        }
        //String whereTemp = null;
        if(where != null && where.length() > 0 && where.length() > 2)
        {
            if(where.toLowerCase().startsWith(" and "))
            {
                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Inside where alteration - before where:")).append(where).toString());
                //where = (new StringBuilder(String.valueOf(where.substring(where.indexOf(" and") + " and".length() + 1, where.length())))).append(" AND ").toString();
                where = (new StringBuilder(String.valueOf(where.substring(" and".length() + 1, where.length())))).append(" AND ").toString();
            } else
            {
                where = (new StringBuilder(String.valueOf(where))).append(" AND ").toString();
            }
        } else
        {
            where = "";
        }
        toGetQueryStrMap.put("sortlogic", order);
        toGetQueryStrMap.put("conditionlogic", where);
        //EBWLogger.trace("QueryExecutor :", (new StringBuilder("sort and condition logic  in populatePgnMap fn() :")).append(toGetQueryStrMap).toString());
        return toGetQueryStrMap;
    }

    public String callRemoveRestartlogicFn(String query)
    {
        //EBWLogger.trace("QueryExecutor :", (new StringBuilder("Starting Method callRemoveRestartlogicFn having parameter :")).append(query).toString());
        String resultQuery = null;
        if(query.indexOf("#restartlogic:varchar#") > -1)
        {
            String query1 = query.substring(0, query.indexOf("#restartlogic:varchar#"));
            String query2 = query.substring(query.indexOf("#restartlogic:varchar#") + "#restartlogic:varchar#".length(), query.length());
            resultQuery = (new StringBuilder(String.valueOf(query1))).append("  ").append(query2).toString();
        }
        //EBWLogger.trace("QueryExecutor :", (new StringBuilder("Exit from  Method callRemoveRestartlogicFn with output :")).append(resultQuery).toString());
        return resultQuery;
    }

    /*private String getQueryStringWorkingFine(String queryString, HashMap obj)
        throws Exception
    {
       // //EBWLogger.trace(this, (new StringBuilder("Starting Method getQueryString  having queryString :")).append(queryString).append(" and HashMap :").append(obj).toString());
        parameterCount = 0;
        values = new Object[100];
       // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Getting queryString for ")).append(queryString).toString());
        String query = null;
        if(queryString.indexOf("#") != -1)
        {
            String clause[] = queryString.split("#");
           // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Number of Hash parameters are ")).append(clause.length).toString());
            query = new String();
            int index = -1;
            for(int i = 0; i < clause.length; i++)
            {
                if(clause[i].indexOf("(") == -1 && clause[i].indexOf(",") == -1 && clause[i].indexOf("=") == -1 && clause[i].indexOf(")") == -1 && clause[i].indexOf(" like ") == -1 && clause[i].indexOf("between") == -1 && clause[i].indexOf(" and ") == -1 && clause[i].trim().length() > 0)
                {
                    String condition[] = (String[])null;
                    EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Value within Hash for replacement before instance check is :")).append(clause[i]).toString());
                    if(obj instanceof HashMap)
                    {
                        condition = clause[i].split(":");
                        HashMap data = obj;
                        Object value = data.get(condition[0].trim());
                        if(value == null && queryString.toLowerCase().indexOf("select") > -1)
                        {
                            clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                '\''
                            })))).append("%").append(new String(new char[] {
                                '\''
                            })).toString();
                        } else
                        {
                            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Value Send for the where clause or thecolum of Select or Update query is null ")).append(String.valueOf(value == null)).toString());
                            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Setting value :")).append(value).append(" for column :").append(condition[0].trim()).toString());
                            values[parameterCount++] = value;
                            if(strStmtType.equals(STMT_PREP))
                            {
                                clause[i] = "?";
                            } else
                            {
                                EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Datatype of ")).append(value).append(" is ").append(condition[1]).toString());
                                if(condition[1].equalsIgnoreCase("numeric"))
                                    clause[i] = ConvertionUtil.convertToString(value);
                                else
                                if(condition[1].equalsIgnoreCase("date") && value != null)
                                    clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                        '\''
                                    })))).append(ConvertionUtil.convertToString(value)).append(new String(new char[] {
                                        '\''
                                    })).toString();
                                else
                                if(condition[1].equalsIgnoreCase("varchar") && value != null)
                                    clause[i] = (String)value;
                                else
                                if(value == null)
                                    clause[i] = null;
                            }
                        }
                    } else
                    {
                        EBWLogger.logDebug("QueryExecutor :", "Supporting Object instance sent forexecution of query is not of typeEBWTransferObject,Object[],HashMap");
                    }
                }
                query = (new StringBuilder(String.valueOf(query))).append(clause[i]).toString();
            }

        } else
        {
            query = new String(queryString);
        }
        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Future:Query before modification :")).append(query).toString());
        if(obj != null && obj.containsKey("restartlogic") && obj.get("restartlogic") != null)
        {
            String restartlogicInQuery = null;
            restartlogicInQuery = (String)obj.get("restartlogic");
            String where = null;
            String order = null;
            if(restartlogicInQuery.indexOf("order by") != -1)
            {
                where = restartlogicInQuery.substring(0, restartlogicInQuery.indexOf("order by"));
                order = restartlogicInQuery.substring(restartlogicInQuery.indexOf("order by"), restartlogicInQuery.length());
            }
            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Where  :")).append(where).toString());
           // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Order  :")).append(order).toString());
           // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("RestartLogic :")).append(restartlogicInQuery).toString());
            String queryTEmpRemoved = query.substring(query.lastIndexOf(restartlogicInQuery), query.length());
           // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("QueryTEmpRemoved :")).append(queryTEmpRemoved).toString());
            String queryTobeAdded = queryTEmpRemoved.substring(queryTEmpRemoved.indexOf("rownum"), queryTEmpRemoved.length());
           // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query to be added :")).append(queryTobeAdded).toString());
            String originalQry = query.substring(0, query.lastIndexOf(restartlogicInQuery));
            String query1 = null;
            String query2 = null;
            if(query.toLowerCase().indexOf(" union ") > -1)
            {
                if(where != null && where.length() > 0)
                {
                    String whereTemp = null;
                    if(where.toLowerCase().indexOf(" where ") <= -1 && where.toLowerCase().startsWith(" and "))
                    {
                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Inside where alteration - before where:")).append(where).toString());
                        whereTemp = (new StringBuilder(" where ")).append(where.substring(where.indexOf(" and") + " and".length() + 1, where.length())).toString();
                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Inside where alteration - after where:")).append(whereTemp).toString());
                        query1 = query.substring(0, query.lastIndexOf(where));
                        query2 = query.substring(query.indexOf(where) + where.length(), query.length());
                        query = (new StringBuilder(String.valueOf(query1))).append(" ").append(whereTemp).append(" ").append(query2).toString();
                    }
                    //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("where temp :")).append(whereTemp).toString());
                }
            } else
            if(query.indexOf("group by") != -1 || query.indexOf("GROUP BY") != -1)
            {
                String queryWithWhere = null;
                String finalQuery = null;
                finalQuery = originalQry;
                if(where != null && where.length() > 0)
                {
                    if(originalQry.indexOf("group by") != -1)
                        queryWithWhere = originalQry.replaceFirst("group by ", (new StringBuilder(" ")).append(where).append(" group by ").toString());
                    else
                    if(originalQry.indexOf("GROUP BY") != -1)
                        queryWithWhere = originalQry.replaceFirst("GROUP BY ", (new StringBuilder(" ")).append(where).append(" group by ").toString());
                    finalQuery = queryWithWhere;
                }
                if(order != null && order.length() > 0)
                    if(originalQry.lastIndexOf("order") > -1)
                    {
                        if(originalQry.lastIndexOf(" by ") > -1)
                        {
                            finalQuery = finalQuery.substring(0, finalQuery.lastIndexOf(" order"));
                            finalQuery = (new StringBuilder(String.valueOf(finalQuery))).append(" ").append(order).append("  ").toString();
                        }
                    } else
                    {
                        finalQuery = (new StringBuilder(String.valueOf(finalQuery))).append(" ").append(order).append(" ").toString();
                    }
                query = (new StringBuilder(String.valueOf(finalQuery))).append(" ) where ").append(queryTobeAdded).toString();
            } else
            if(order != null && order.length() > 0 && (originalQry.indexOf("order ") != -1 || originalQry.indexOf("ORDER ") != -1) && (originalQry.lastIndexOf("by ") != -1 || originalQry.lastIndexOf("BY ") != -1))
            {
                String originalQrywithoutOrder = null;
                if(originalQry.lastIndexOf("ORDER") > -1)
                    originalQrywithoutOrder = originalQry.substring(0, originalQry.lastIndexOf("ORDER"));
                else
                if(originalQry.lastIndexOf("order") > -1)
                    originalQrywithoutOrder = originalQry.substring(0, originalQry.lastIndexOf("order"));
                if(originalQrywithoutOrder != null && originalQrywithoutOrder.length() > 0)
                {
                    if(where != null && where.length() > 0)
                        query = (new StringBuilder(String.valueOf(originalQrywithoutOrder))).append(" ").append(where).append(" ").toString();
                    else
                        query = originalQrywithoutOrder;
                    //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("order:")).append(order).toString());
                    query = (new StringBuilder(String.valueOf(query))).append(" ").append(order).toString();
                    if(queryTobeAdded != null)
                        query = (new StringBuilder(String.valueOf(query))).append(" ) where   ").append(queryTobeAdded).toString();
                }
            }
        }
        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("final query:")).append(query).toString());
        //EBWLogger.trace("", (new StringBuilder("Exit from  Method getQueryString(String queryString,HashMap obj) with output :")).append(query).toString());
        return query;
    }*/

    private String getQueryString(String queryString, HashMap obj)
        throws Exception
    {
    	if(EBWLogger.isDebugEnabled()){
        EBWLogger.logDebug(this, (new StringBuilder("Starting Method getQueryString  having queryString :")).append(queryString).append(" and HashMap :").append(obj).toString());
    	}
        // parameterCount = 0;
       // values = new Object[100];
       // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Getting queryString for ")).append(queryString).toString());
        String query = null;
        if(queryString.indexOf("#") != -1)
        {
            String clause[] = queryString.split("#");
            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Number of Hash parameters are ")).append(clause.length).toString());
            query = new String();
           // int index = -1;
            for(int i = 0; i < clause.length; i++)
            {
                if(clause[i].indexOf("(") == -1 && clause[i].indexOf(",") == -1 && clause[i].indexOf("=") == -1 && clause[i].indexOf(")") == -1 && clause[i].indexOf(" like ") == -1 && clause[i].indexOf("between") == -1 && clause[i].indexOf(" and ") == -1 && clause[i].toUpperCase().indexOf("ROWS ONLY") == -1 && clause[i].toUpperCase().indexOf("FETCH FIRST") == -1 && clause[i].trim().length() > 0)
                {
                    String condition[] = (String[])null;
                    //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Value within Hash for replacement before instance check is :")).append(clause[i]).toString());
                    if(obj instanceof HashMap)
                    {
                        condition = clause[i].split(":");
                        HashMap data = obj;
                        Object value = data.get(condition[0].trim());
                        if(value == null && queryString.toLowerCase().indexOf("select") > -1)
                        {
                            clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                '\''
                            })))).append("%").append(new String(new char[] {
                                '\''
                            })).toString();
                        } else
                        {
                            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Value Send for the where clause or thecolum of Select or Update query is null ")).append(String.valueOf(value == null)).toString());
                            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Setting value :")).append(value).append(" for column :").append(condition[0].trim()).toString());
                           /* values[parameterCount++] = value;
                            if(strStmtType.equals(STMT_PREP))
                            {
                                clause[i] = "?";
                            } else
                            {*/
                                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Datatype of ")).append(value).append(" is ").append(condition[1]).toString());
                                if(condition[1].equalsIgnoreCase("numeric"))
                                    clause[i] = ConvertionUtil.convertToString(value);
                                else
                                if(condition[1].equalsIgnoreCase("date") && value != null)
                                    clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                        '\''
                                    })))).append(ConvertionUtil.convertToString(value)).append(new String(new char[] {
                                        '\''
                                    })).toString();
                                else
                                if(condition[1].equalsIgnoreCase("varchar") && value != null)
                                    clause[i] = (String)value;
                                else
                                if(value == null)
                                    clause[i] = null;
                           // }
                        }
                    } else
                    {
                    	if(EBWLogger.isDebugEnabled()){
                        EBWLogger.logDebug("QueryExecutor :", "Supporting Object instance sent forexecution of query is not of typeEBWTransferObject,Object[],HashMap");
                    }
                    }
                }
                query = (new StringBuilder(String.valueOf(query))).append(clause[i]).toString();
            }

        } else
        {
            query = new String(queryString);
        }
        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query in getQueryString(hashmap query :")).append(query).toString());
        if(query != null && query.length() > 0)
        {
           // query = query.replaceAll("  ", " ");
          //  query = query.replaceAll("   ", " "); 
            for(query = query.replaceAll("    ", " "); query.indexOf("like '%'") > -1;)
            {
                query = tempQuery(query);
                if(query.indexOf("WHERE   AND") > -1)
                {
                    query = query.replaceAll("WHERE   AND", "WHERE");
                    //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("after :")).append(query).toString());
                }
                if(query.indexOf("WHERE AND") > -1)
                {
                    query = query.replaceAll("WHERE AND", "WHERE");
                    //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("after :")).append(query).toString());
                }
                if(query.indexOf("WHERE  AND") > -1)
                {
                    query = query.replaceAll("WHERE  AND", "WHERE");
                    //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("after :")).append(query).toString());
                }
            }

        }
        
        if(query.toUpperCase().indexOf("AND ( )")!= -1){        	
        	query = query.replaceAll("and \\( \\)","AND ( )");
        	query = query.replaceAll("AND \\( \\)","");        
        }
        
        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("final query:")).append(query).toString());
        //EBWLogger.trace(this, (new StringBuilder("Exit from  Method getQueryString(String queryString,HashMap obj) with output :")).append(query).toString());
        return query;
    }

    public String tempQuery(String query)
    {
        String result = null;
        if(query.indexOf("like '%'") > -1)
        {
            String queryArr[] = query.split("like '%'");
            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("No of likes :")).append(queryArr.length).toString());
            if(queryArr != null && queryArr.length > 0)
            {
                for(int i = 0; i < queryArr.length; i++)
                    if(i == 0)
                    {
                        String subQuery = queryArr[i];
                        subQuery = (new StringBuilder(String.valueOf(subQuery))).append("like '%'").toString();
                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("subQuery   :")).append(subQuery).toString());
                        String subQryArr[] = subQuery.split(" ");
                        String OpenBrace = subQryArr[subQryArr.length - 3];
                        
                         if(OpenBrace.startsWith("(") || OpenBrace.startsWith("UPPER")|| OpenBrace.startsWith("LOWER")){
                        	StringBuffer stBuff = new StringBuffer(query);
                        	String qryToBeRemoved = (new StringBuilder(String.valueOf(subQryArr[subQryArr.length - 3]))).append(" ").append(subQryArr[subQryArr.length - 2]).append(" ").append(subQryArr[subQryArr.length - 1]).toString();
                        	qryToBeRemoved = qryToBeRemoved.substring(0,qryToBeRemoved.length());
                            //EBWLogger.logDebug("QueryExecutor :", "final qryToBeRemoved for testing:"+qryToBeRemoved);
                            if(query.indexOf(qryToBeRemoved) > -1)
                            {
                                int indx1 = stBuff.indexOf(qryToBeRemoved);
                                int indx2 = indx1 + qryToBeRemoved.length();
                                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("indx1   :")).append(indx1).append("  index2 : ").append(indx2).toString());
                                stBuff.replace(indx1, indx2, "");
                               // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("final String for testing as StringBuffer with WHERE Clause:")).append(stBuff.toString()).toString());
                            }
                            query = stBuff.toString();
                        }else{
                        //String temp = (new StringBuilder(String.valueOf(subQryArr[subQryArr.length - 3]))).append(" ").append(subQryArr[subQryArr.length - 2]).append(" ").append(subQryArr[subQryArr.length - 1]).toString();
                        StringBuffer stBuff = new StringBuffer(query);
                        if(subQryArr[subQryArr.length - 4].toUpperCase().equalsIgnoreCase("WHERE"))
                        {
                            String qryToBeRemoved = (new StringBuilder(String.valueOf(subQryArr[subQryArr.length - 3]))).append(" ").append(subQryArr[subQryArr.length - 2]).append(" ").append(subQryArr[subQryArr.length - 1]).append(" AND").toString();
                           // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("final qryToBeRemoved for testing:")).append(qryToBeRemoved).toString());
                            String qryToBeRemoved1 = (new StringBuilder(String.valueOf(subQryArr[subQryArr.length - 3]))).append(" ").append(subQryArr[subQryArr.length - 2]).append(" ").append(subQryArr[subQryArr.length - 1]).toString();
                          //  EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("final qryToBeRemoved1 for testing:")).append(qryToBeRemoved1).toString());
                            if(query.indexOf(qryToBeRemoved) > -1)
                            {
                                int indx1 = stBuff.indexOf(qryToBeRemoved);
                                int indx2 = indx1 + qryToBeRemoved.length();
                             //   EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("indx1   :")).append(indx1).append("  index2 : ").append(indx2).toString());
                                stBuff.replace(indx1, indx2, "");
                             //   EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("final String for testing as StringBuffer with WHERE Clause:")).append(stBuff.toString()).toString());
                            } else
                            if(query.indexOf(qryToBeRemoved1) > -1)
                            {
                                int indx1 = stBuff.indexOf(qryToBeRemoved1);
                                int indx2 = indx1 + qryToBeRemoved1.length();
                                stBuff.replace(indx1, indx2, "");
                               // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("final String for testing as StringBuffer with WHERE clause:")).append(stBuff.toString()).toString());
                            }
                        } else
                        if(subQryArr[subQryArr.length - 4].toUpperCase().equalsIgnoreCase("AND") || subQryArr[subQryArr.length - 4].toUpperCase().equalsIgnoreCase("OR"))
                        {
                            String qryToBeRemoved = (new StringBuilder(String.valueOf(subQryArr[subQryArr.length - 4]))).append(" ").append(subQryArr[subQryArr.length - 3]).append(" ").append(subQryArr[subQryArr.length - 2]).append(" ").append(subQryArr[subQryArr.length - 1]).toString();
                           // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("qryToBeRemoved  no where :")).append(qryToBeRemoved).toString());
                            if(query.indexOf(qryToBeRemoved) > -1)
                            {
                                int indx1 = stBuff.indexOf(qryToBeRemoved);
                                int indx2 = indx1 + qryToBeRemoved.length();
                                stBuff.replace(indx1, indx2, "");
                               // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("final String for testing as StringBuffer with AND clause:")).append(stBuff.toString()).toString());
                            }
                        }else if(subQryArr[subQryArr.length - 3].toUpperCase().startsWith("COALESCE(UPPER(")){
                        	String qryToBeRemoved = (new StringBuilder(String.valueOf(subQryArr[subQryArr.length - 3]))).append(" ").append(subQryArr[subQryArr.length - 2]).append(" ").append(subQryArr[subQryArr.length - 1]).toString();
                        	if(query.indexOf(qryToBeRemoved) > -1)
                            {
                                int indx1 = stBuff.indexOf(qryToBeRemoved);
                                int indx2 = indx1 + qryToBeRemoved.length();
                                stBuff.replace(indx1, indx2, "");
                               // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("final String for testing as StringBuffer with AND clause:")).append(stBuff.toString()).toString());
                            }
                        }
                        query = stBuff.toString();
                        if(query.toUpperCase().indexOf("WHERE   AND") > -1)
                            query.replaceAll("WHERE   AND", "WHERE");
                        
                        
                        query = query.replaceAll("  ", " ");
                        query = query.replaceAll("   ", " ");
                        query = query.replaceAll("    ", " ");
                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query after  like removed :")).append(query).toString());
                    }
                    }

            }
        }
        
        if(query != null && query.toUpperCase().indexOf("AND ( )") != -1)
            query = query.replaceAll("AND \\( \\)", "");
        query = query.replaceAll("AND  \\) AND", "AND");   
        query = query.replaceAll("AND \\)", ")");
        query = query.replaceAll("AND  AND","AND");
        query = query.replaceAll("AND   AND","AND");
        
        result = query;
        return result;
    }

    private boolean isSQLJ(String stmtName)
        throws EbwException
    {
        return false;
    }

    public Object execute(Object stmtId, Object obj)
        throws Exception
    {
        EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Starting Method execute(")).append(stmtId).append(",").append(obj).append(")").toString());
        Object result = "0";
        try
        {
            if(stmtId instanceof String)
            {
                String stmtName = (String)stmtId;
                if(!isSQLJ(stmtName))
                {
                    init(stmtName);
                    if(obj instanceof EBWTransferObject[])
                        result = executeBatch(stmtName, (Object[])obj);
                    else
                    if(obj instanceof HashMap[])
                        result = executeBatch(stmtName, (HashMap[])obj);
                    else
                        result = executeUpdate(stmtName, obj);
                } else
                {
                    String sqlStmt = "";
                    sqlStmt = getQueryValue((new StringBuilder(String.valueOf(stmtName))).append(".Query").toString(), true);
                    String fileName = StringUtil.initCaps(stmtName);
                    String query = getQueryString(sqlStmt, obj);
                    createSQLJFile(fileName, query);
                    translateSQLJFile();
                    Class cls = Class.forName((new StringBuilder(String.valueOf(getClass().getPackage().getName()))).append(".").append(fileName).toString());
                    Object ob = cls.newInstance();
                    Class clss[] = {
                    		java.sql.Connection.class
                    };
                    Method method = cls.getMethod("execute", clss);
                    Object objArr[] = {
                        getConnection()
                    };
                    method.invoke(ob, objArr);
                }
            } else
            if(stmtId instanceof String[])
                result = executeBatch((String[])stmtId, (Object[])obj);
        }
        catch(MissingResourceException mre)
        {
            mre.printStackTrace();
            throw new EbwException(this, mre);
        }
        catch(SQLException sqlEx)
        {
            sqlEx.printStackTrace();
            throw new EbwException(this, sqlEx);
        }
        catch(Exception ebwEx)
        {
            ebwEx.printStackTrace();
            throw ebwEx;
        }
        EBWLogger.trace("QueryExecutor :", "Exiting from execute");
        return result;
    }

    public void setConnection(Connection con)
    {
        this.con = con;
    }

    private String getQueryString(String sqlStmt, Object obj)
        throws Exception
    {
        EBWLogger.logDebug("QueryExecutor: ", (new StringBuilder("Starting Method  getQueryString(")).append(sqlStmt).append(",").append(obj).append(")").toString());
        int rowcount=0;
        parameterCount = 0;
        values = new Object[100];
        String query = null;
        String urclause="With ur";
        String colNull = "";
        String colNullResult = "";
        String nullResult = "";
        String colNameINNull = "";
        String colNameINNullInt = "";
        //String blankOutput = "";
        String nullCheck = "";
        String resultPrepopulator[] = (String[])null;
        String colValNullIN[] = (String[])null;
        Integer colValIntIN[] = (Integer[])null;
        boolean param_incr = false;
        int countQuery = 0;
        int countEqualQuery = 0;
        int countINQuery = 0;
        String queryStrArr[] = new String[80];
        String queryEqualStrArr[] = new String[80];
        String queryINStrArr[] = new String[80];
        ArrayList attrTO = null;
        byte byteData[] = (byte[])null;
       
        try
        {
            if(sqlStmt.indexOf("#") != -1)
            {
                String clause[] = sqlStmt.split("#");
                query = new String();
                int index = -1;
                if(obj instanceof EBWTransferObject)
                    try
                    {
                        attrTO = Prepopulator.LoadTODefValues((EBWTransferObject)obj, con);
                       // EBWLogger.logDebug("QueryExecutor :", "Value of the attrTO"+attrTO);
                        if(attrTO == null || attrTO != null && attrTO.isEmpty())
                        {
                            EBWLogger.logDebug("QueryExecutor :", "Value of the arrayList is empty");
                        } else
                        {
                            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Value of the arrayList is")).append(attrTO.toString()).toString());
                            Prepopulator.fillTransferObject((EBWTransferObject)obj, attrTO, con);
                           // EBWLogger.logDebug("QueryExecutor :", "fill transfer object"+obj);
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                for(int i = 0; i < clause.length; i++)
                {
                    if(clause[i].indexOf("(") == -1 && clause[i].indexOf(",") == -1 && clause[i].indexOf("=") == -1 && clause[i].indexOf(")") == -1 && clause[i].indexOf(" like ") == -1 && clause[i].indexOf("between") == -1 && clause[i].indexOf(" and ") == -1 && clause[i].toUpperCase().indexOf("ROWS ONLY") == -1&& clause[i].toUpperCase().indexOf("FETCH FIRST") == -1 && clause[i].trim().length() > 0)
                    {
                        String condition[] = (String[])null;
                        if(obj instanceof EBWTransferObject)
                        {
                            cls = Class.forName(obj.getClass().getName());
                            String value = null;
                          
                            if(clause[i].indexOf(":") > -1)
                            {
                                condition = clause[i].split(":");
                          
                                char fieldChar[] = {
                                    condition[0].trim().charAt(0)
                                };
                                String methodName = (new StringBuilder("get")).append((new String(fieldChar)).toUpperCase()).append(condition[0].trim().substring(1)).toString();
                                try
                                {
                                    Method method = cls.getMethod(methodName, null);
                                    Object colValue = method.invoke(obj, null);
                                   //modified by krishna on 10-04-2009 
                                    if(condition[0].equalsIgnoreCase("rowsinapage")){
                                    
                                    	if(colValue == null){
                                    		colValue = Double.valueOf(rowsPerPage);
                                        	Double d = Double.valueOf(((Double)colValue).doubleValue() + 1.0D);
                                            colValue = d;
                                    	}else{
                                    	colValue = method.invoke(obj, null);
                                    	Double d = Double.valueOf(((Double)colValue).doubleValue() + 1.0D);
                                        colValue = d;
                                    	}

                                    }
                                    if(EBWLogger.isDebugEnabled()){
                                    EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("ColValue is :")).append(colValue).append(" For method ").append(methodName).toString());
                                    }
                                    if(colValue == null)
                                    {
                                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Enter into ColValue ")).append(colValue).toString());
                                       // methodName = (new StringBuilder("get")).append((new String(fieldChar)).toUpperCase()).append(condition[0].trim().substring(1)).toString();
                                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Method name to be invoked ")).append(methodName).toString());
                                       /* method = cls.getMethod(methodName, null);
                                        colValue = method.invoke(obj, null);*/
                                       // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("colValue Blank after method ")).append(methodName).append(" Invoke is ").append(colValue).toString());
                                        if(sqlStmt.toLowerCase().startsWith("select"))
                                        {
                                            //EBWLogger.logDebug("QueryExecutor :", "***Select Query with ValueObject variable null ***");
                                            clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                                '\''
                                            })))).append("%").append(new String(new char[] {
                                                '\''
                                            })).toString();
                                        }
                                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("clause[i] :")).append(clause[i]).toString());
                                        if(colValue instanceof String[])
                                        {
                                            resultPrepopulator = (String[])colValue;
                                        } else
                                        if(colValue instanceof String)
                                            nullCheck = (String)colValue;
                                        nullResult = clause[i];
                                        colNull = clause[i - 1];
                                        ArrayList likeList = new ArrayList();
                                        if(colNull.toLowerCase().indexOf("like") != -1)
                                            likeList.add(colNull);
                                        for(Iterator queryIter = likeList.iterator(); queryIter.hasNext();)
                                        {
                                            queryStrArr[countQuery] = queryIter.next().toString();
                                            countQuery++;
                                        } 

                                        if(sqlStmt.toUpperCase().startsWith("INSERT") || query.toLowerCase().startsWith("update"))
                                            if(strStmtType.equals(STMT_PREP))
                                            {
                                                
                                                param_incr = true;
                                                if(condition[1].equalsIgnoreCase("numeric"))
                                                    values[parameterCount++] = new NullObj(2);
                                                else
    											if(condition[1].equalsIgnoreCase("BigDecimal"))
                                                    values[parameterCount++] = new NullObj(6);
												else
												if(condition[1].equalsIgnoreCase("timestamp"))
                                                    values[parameterCount++] = new NullObj(3);
                                                else
                                                if(condition[1].equalsIgnoreCase("date"))
                                                    values[parameterCount++] = new NullObj(91);
                                                else
                                                if(condition[1].equalsIgnoreCase("varchar"))
                                                    values[parameterCount++] = new NullObj(12);
                                                else
                                                if(condition[1].equalsIgnoreCase("double"))
                                                    values[parameterCount++] = new NullObj(8);
                                                else
                                                    param_incr = false;
                                                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("NULL VALUE FOUND; Updated Query is ")).append(query).append(clause[i]).toString());
                                                clause[i] = "?";
                                            } else
                                            if(strStmtType.equals(STMT_ONLY))
                                            {
                                                clause[i] = null;
                                                
                                            }
                                    } else
                                    {
                                        if(strStmtType.equals(STMT_PREP) && condition[0].equalsIgnoreCase("rowsinapage")){
                                    		clause[i] = colValue.toString().substring(0,colValue.toString().length()-2);
                                    	}else{
										values[parameterCount++] = colValue;
                                        if(strStmtType.equals(STMT_PREP))
                                        {
                                            if(condition[1].equalsIgnoreCase("numeric"))
                                            {
                                               
                                                
                                                /*value = ConvertionUtil.convertToString((Double)colValue);
                                                clause[i] = "?";*/
                                            	if(colValue instanceof Integer[])
                                                {
                                                    colValIntIN = (Integer[])colValue;
                                                    if(colValIntIN != null)
                                                    {
                                                        for(int k = 0; k < colValIntIN.length; k++)
                                                       	 colNameINNullInt = clause[i - 1];
                                                    }
                                                    value = ConvIntergerArrToInteger((Integer[])colValue);
                                                    clause[i] = "?";  
                                                }else if(colValue instanceof Double[])
                                                {
                                               	 //EBWLogger.logDebug("QueryExecutor :", "inside double array method");
                                               	 Double[] arr=(Double[])colValue;
                                               	 String DOUBLE_ARRAY="";
                                               	 clause[i]="";
                                               	 for(int l=0;l<arr.length;l++){
                                               		 
	                                               		Double dObj1 = new Double(arr[l]);
	                                               		//EBWLogger.logDebug("QueryExecutor :", "inside double array method"+dObj1);
	                                               		DOUBLE_ARRAY=DOUBLE_ARRAY+dObj1.toString()+",";
	                                               		clause[i] = clause[i]+"?"+",";
                                               	 }
                                               	 clause[i]=clause[i].substring(0,clause[i].length()-1).trim();
                                               	 //clause[i] = "?";*/
                                              	
                                              	} else if(colValue instanceof Integer)
                                                {
                                              		value = ConvertionUtil.convertToString(colValue);
                                              		clause[i] = "?";  
                                                } else{
                                           		value = ConvertionUtil.convertToString((Double)colValue);
                                           		clause[i] = "?";  
                                                }          
                                            
                                            
                                            } else
                                            if(condition[1].equalsIgnoreCase("java.lang.Double"))
                                            {
                                                value = ConvertionUtil.convertToString((Double)colValue);
                                                clause[i] = "?";
                                            } else
                                            if(condition[1].equalsIgnoreCase("blob"))
                                            {
                                                byteData = (byte[])colValue;
                                                clause[i] = new String("?");
                                            } else if(condition[1].equalsIgnoreCase(CLOBDATA))
                                            {
                                            	value=colValue.toString();
                                            	clause[i] = "?";
                                            }else if(condition[1].equalsIgnoreCase(BigDecimal))
                                            {
                                            	//value =((Double) colValue).toString();
                                            	value =(colValue).toString();
                                                clause[i] = "?";
                                            }else
                                            {
                                                if(condition[1].equalsIgnoreCase("date")){
                                                	value = ConvertionUtil.convertToDBDateStr(ConvertionUtil.convertToString(colValue)); 
                                                	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                                                	Date dd = df.parse(value);
                                                	values[parameterCount-1] = dd;
                                                    clause[i] = "?";
                                                }
                                                else
                                                    if(condition[1].equalsIgnoreCase("timestamp"))
                                                    {
                                                        //Timestamp ts = ConvertionUtil.convertToTimestamp((Date)colValue);
                                                        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
                                                        value = dtFormat.format(colValue);
                                                        clause[i] = "?";
                                                    }
												else
                                                if(condition[1].equalsIgnoreCase("varchar"))
                                                    if(colValue instanceof String[])
                                                    {
                                                        /*colValNullIN = (String[])colValue;
                                                        if(colValNullIN != null)
                                                        {
                                                            for(int k = 0; k < colValNullIN.length; k++)
                                                                colNameINNull = clause[i - 1];

                                                        }
                                                        value = ConvStringArrToStr((String[])colValue);*/
                                                    	String[] arr=(String[])colValue;
                                                        
                                                     	 clause[i]="";
                                                     	 for(int l=0;l<arr.length;l++){
                                                     		 
     	                                               		
     	                                               		clause[i] = clause[i]+"?"+",";
                                                     	 }
                                                     	 clause[i]=clause[i].substring(0,clause[i].length()-1).trim();
                                                     	
                                                    } else
                                                    {
                                                        value = (String)colValue;
                                                        clause[i] = "?";
                                                    }
                                                //clause[i] = "?";
                                                //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Clause[i] for PrepareStatement  in getQueryString(String sqlStmt, Object obj) is:")).append(clause[i]).toString());
                                            }
                                        } else
                                        if(condition[1].equalsIgnoreCase("numeric"))
                                        {/*
                                            value = ConvertionUtil.convertToString((Double)colValue);
                                            clause[i] = value;
                                        */
                                          	 if(colValue instanceof Integer[])
                                               {
                                                   colValIntIN = (Integer[])colValue;
                                                   if(colValIntIN != null)
                                                   {
                                                       for(int k = 0; k < colValIntIN.length; k++){
                                                      	 colNameINNullInt = clause[i - 1];
                                                           //EBWLogger.logDebug("QueryExecutor","Value of the colNameINNullInt is :"+colNameINNullInt);
                                                       }
                                                   }
                                                   value = ConvIntergerArrToInteger((Integer[])colValue);
                                                   clause[i] = value;  
                                               }else if ((colValue.getClass().getName()).equals("java.lang.Integer")){                                            	  
                                                   clause[i] = colValue.toString();
                                               }else if((colValue.getClass().getName()).equals("java.math.BigDecimal")){
                                            	   clause[i] = colValue.toString();
                                         	     }else{
                                               //	EBWLogger.logDebug("QueryExecutor","Value of the colValue is :"+colValue.getClass().getName());
                                               	  if(colValue.getClass().getName().equalsIgnoreCase("[Ljava.lang.Double;")){
                                               		//  EBWLogger.logDebug("QueryExecutor","Value of the colValue is :"+colValue.getClass().getName());
                                                     	Double[] arr=(Double[])colValue;
                                                     	//double d =arr[0];
                                                     	//colValue=d;
                                                     	//EBWLogger.logDebug("QueryExecutor","Value of the colValue is :"+colValue.getClass().getName());
                                                     //	EBWLogger.logDebug("QueryExecutor","Value of the colValue is :"+colValue);
                                                     	//value = ConvertionUtil.convertToString((Double)colValue);
                                                  		//clause[i] = value;  
                                                     	String DOUBLE_ARRAY="";
                                                     	for(int l=0;l<arr.length;l++){
                                                     		Double dObj1 = new Double(arr[l]);
                                                     		DOUBLE_ARRAY=DOUBLE_ARRAY+dObj1.toString()+",";
                                                     	}
                                                     	clause[i]=DOUBLE_ARRAY.substring(0,DOUBLE_ARRAY.length()-1);
                                               	  }else{
                                               		  
                                               	   if(!(colValue.getClass().getName()).equals("java.lang.Double")){
                                               		 colValue=Double.parseDouble((String)colValue);
                                               	     } else {
                                               	    	 colValue=(Double)colValue;
                                               	     }
                                          		value = ConvertionUtil.convertToString((Double)colValue);
                                          		clause[i] = value;  
                                               	  }
                                               }
                                        }else if(condition[1].equalsIgnoreCase("BigDecimal")){
                                        	value =(colValue).toString();
                                        	clause[i] = value;
                                        } else
                                        {
                                            if(condition[1].equalsIgnoreCase("date"))
                                                value = ConvertionUtil.convertToDBDateStr(ConvertionUtil.convertToString(colValue));
                                            else
                                            if(condition[1].equalsIgnoreCase("timestamp"))
                                            {
                                                //Timestamp ts = ConvertionUtil.convertToTimestamp((Date)colValue);
                                                SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
                                                value = dtFormat.format(colValue);
                                            } else
                                            if(condition[1].equalsIgnoreCase("blob"))
                                            {
                                                byteData = (byte[])colValue;
                                                clause[i] = new String(byteData);
                                            } else
                                            if(condition[1].equalsIgnoreCase("varchar"))
                                                if(colValue instanceof String[])
                                                {
                                                    colValNullIN = (String[])colValue;
                                                    if(colValNullIN != null)
                                                    {
                                                        for(int k = 0; k < colValNullIN.length; k++)
                                                            colNameINNull = clause[i - 1];

                                                    }
                                                    value = ConvStringArrToStr((String[])colValue);
                                                } else
                                                if(colValue instanceof Double)
                                                {
                                                    value = ConvertionUtil.convertToString((Double)colValue);
                                                    clause[i] = value;
                                                } else
                                                {
                                                    value = (String)colValue;
                                                }
                                            clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                                '\''
                                            })))).append(value).append(new String(new char[] {
                                                '\''
                                            })).toString();
                                            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Clause[i] for Statement  in getQueryString(String sqlStmt, Object obj) is: :")).append(clause[i]).toString());
                                        
                                        }
                                        }
                                    }
                                }
                                catch(Exception e)
                                {
                                    EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Method get")).append(StringUtil.initCaps(methodName)).append(" not found in TO ").append(obj).toString());
                                    clause[i] = (new StringBuilder("#")).append(clause[i]).append("#").toString();
                                }
                            } else
                            if(clause[i].trim().length() > 0 && !clause[i].trim().equalsIgnoreCase(urclause))
                            {
                                char fieldChar[] = {
                                    clause[i].charAt(0)
                                };
                                String methodName = (new StringBuilder("get")).append((new String(fieldChar)).toUpperCase()).append(clause[i].substring(1)).toString();
                                //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("NOCOLON scenario; Method name to be invoked ")).append(methodName).toString());
                                Method method = cls.getMethod(methodName, null);
                                Object colValue = method.invoke(obj, null);
                               // EBWLogger.logDebug("QueryExecutor", (new StringBuilder("colValue after method ")).append(methodName).append(" Invoke is ").append(colValue).toString());
                                if(colValue == null && sqlStmt.toLowerCase().indexOf("select") > -1)
                                {
                                    clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                        '\''
                                    })))).append("%").append(new String(new char[] {
                                        '\''
                                    })).toString();
                                } else
                                {
                                    Field field = cls.getDeclaredField(clause[i]);
                                    if(field.getType().getName().equals("java.lang.Double"))
                                    {
                                        value = ConvertionUtil.convertToString((Double)colValue);
                                        clause[i] = value;
                                    } else
                                    {
                                        if(field.getType().getName().equals("java.lang.String"))
                                            value = (String)colValue;
                                        else
                                        if(field.getType().getName().equals("date"))
                                            value = ConvertionUtil.convertToString((Date)colValue);
                                        else
                                        if(field.getType().getName().equals("java.lang.String[]"))
                                        {
                                            value = ConvertionUtil.convertToCSV((String[])colValue);
                                            //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("StringArray convertion is :")).append(value).toString());
                                        }
                                        clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                            '\''
                                        })))).append(value).append(new String(new char[] {
                                            '\''
                                        })).toString();
                                    }
                                }
                            } else {
                            	if(clause[i].trim().equalsIgnoreCase(urclause)){
                            		clause[i] = " "+urclause;
                            	}
                            }
                        } else
                        if(obj instanceof Object[])
                        {
                            //EBWLogger.logDebug("QueryExecutor :", "Obj is an object Array");
                            condition = clause[i].split(":");
                            index++;
                            Object data[] = (Object[])obj;
                            if(strStmtType.equals(STMT_PREP))
                                clause[i] = "?";
                            else
                            if(condition[1].equalsIgnoreCase("numeric"))
                                clause[i] = ConvertionUtil.convertToString((Double)data[index]);
                            else
                            if(condition[1].equalsIgnoreCase("date"))
                                clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                    '\''
                                })))).append(((Date)data[index]).toString()).append(new String(new char[] {
                                    '\''
                                })).toString();
                            else
                            if(condition[1].equalsIgnoreCase("varchar"))
                                clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                    '\''
                                })))).append(data[index]).append(new String(new char[] {
                                    '\''
                                })).toString();
                        } else
                        if(obj instanceof HashMap)
                        {
                            //EBWLogger.logDebug("QueryExecutor :", "Obj is a Hashmap");
                            condition = clause[i].split(":");
                            HashMap data = (HashMap)obj;
                            Object value = data.get(condition[0].trim());
                            if(value == null && sqlStmt.toLowerCase().indexOf("select") > -1)
                            {
                                clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                    '\''
                                })))).append("%").append(new String(new char[] {
                                    '\''
                                })).toString();
                            } else
                            {
                                values[parameterCount++] = value;
                                if(strStmtType.equals(STMT_PREP))
                                {
                                    clause[i] = "?";
                                } else
                                {
                                  
                                    if(condition[1].equalsIgnoreCase("numeric"))
                                        clause[i] = ConvertionUtil.convertToString(value);
                                    else
                                    if(condition[1].equalsIgnoreCase("date") && value != null)
                                        clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                            '\''
                                        })))).append(ConvertionUtil.convertToString(value)).append(new String(new char[] {
                                            '\''
                                        })).toString();
                                    else
                                    if(condition[1].equalsIgnoreCase("varchar") && value != null)
                                        clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                            '\''
                                        })))).append((String)value).append(new String(new char[] {
                                            '\''
                                        })).toString();
                                    else
                                    if(value == null)
                                        clause[i] = null;
                                }
                            }
                        }
                    }
                    query = (new StringBuilder(String.valueOf(query))).append(clause[i]).toString();
                    //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query RESULT IS :")).append(query).toString());
                    boolean whereFlag = false;
                    if(colNull.indexOf("WHERE") != -1)
                    {
                       // EBWLogger.logDebug("QueryExecutor :", "Entered insdie where ");
                      //  EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("colNull is :")).append(colNull).toString());
                        int indexData = colNull.indexOf("WHERE");
                        colNullResult = colNull.substring(indexData + 5, colNull.length());
                        String nullStr = null;
                        String str[] = colNull.split(" ");
                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("str last :")).append(str[str.length - 1]).toString());
                        if(str[str.length - 1].equalsIgnoreCase("like"))
                        {
                            //EBWLogger.logDebug("QueryExecutor :", "girish");
                            if(!str[str.length - 3].equalsIgnoreCase("where"))
                            {
                                whereFlag = true;
                                nullStr = (new StringBuilder(String.valueOf(str[str.length - 3]))).append(" ").append(str[str.length - 2]).append(" ").append(str[str.length - 1]).toString();
                                nullStr = nullStr.trim();
                                if(nullStr.startsWith("(")){
                                	nullStr = nullStr.substring(1,nullStr.length());
                                }
                            } else
                            if(str[str.length - 3].equalsIgnoreCase("where"))
                            {
                                //EBWLogger.logDebug("QueryExecutor :", "Inside string to be removed starts with WHERE");
                                String toBeRemoved = (new StringBuilder(String.valueOf(str[str.length - 2]))).append(" ").append(str[str.length - 1]).append("like '%' AND").toString();
                                if(query.indexOf(toBeRemoved) > -1)
                                {
                                    nullStr = toBeRemoved;
                                    nullResult = "";
                                }
                            }
                            colNullResult = nullStr;
                        }
                        if(query.toUpperCase().startsWith("INSERT"))
                        {
                            if(strStmtType.equals(STMT_PREP) && param_incr)
                            {
                                String result = (new StringBuilder(String.valueOf(colNullResult))).append(nullResult).toString();
                                int d = indexData + 5;
                                int res = d + result.length() + 1;
                                StringBuffer b = new StringBuffer(query);
                                b = b.replace(d, res, "");
                                query = b.toString();
                                parameterCount--;
                                param_incr = false;
                            }
                        } else
                        if(colNull.toUpperCase().indexOf("SELECT") == -1)
                        {
                            if(query.indexOf((new StringBuilder(String.valueOf(colNullResult))).append(" ").append(nullResult).toString()) > -1)
                                query = query.replaceAll((new StringBuilder(String.valueOf(colNullResult))).append(" ").append(nullResult).toString(), "");
                           // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("query after removing like :")).append(query).toString());
                        } else
                        if(whereFlag)
                        {
                            query = query.replaceAll((new StringBuilder(String.valueOf(colNullResult))).append(" ").append(nullResult).toString(), "");
                        } else
                        {
                            if(colNull.indexOf("like") != -1)
                            {
                                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Clause[i] is :")).append(clause[i]).toString());
                                if(clause[i].indexOf(":") != -1)
                                {
                                    int colNullLast = query.lastIndexOf(colNull);
                                    int likeIndex = colNull.indexOf("like");
                                    int total = colNullLast + likeIndex + 5;
                                    int last = total + nullResult.length();
                                    StringBuffer b = new StringBuffer(query);
                                    b = b.replace(total, last, " '%'");
                                    query = b.toString();
                                    //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query for like Clause is :")).append(b.toString()).toString());
                                }
                            }
                            if(query.indexOf(":") != -1 && query.indexOf("=") != -1)
                                query = query.replaceAll((new StringBuilder(String.valueOf(colNullResult))).append(nullResult).toString(), "");
                        }
                        //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Updated Query in WHERE clause of executeQuery(String stmtName, Object obj) method is:")).append(query).toString());
                    } else
                    if(nullCheck == null)
                    {
                        if(resultPrepopulator != null)
                        {
                            for(int z = 0; z < resultPrepopulator.length; z++)
                                if(String.valueOf(resultPrepopulator[z]).equals("blank"))
                                {
                                    colNull = colNull.replace('(', '%');
                                    colNull = colNull.replaceAll("%", "");
                                    query = query.replaceAll(colNull, "");
                                    query = query.replaceAll(nullResult, "");
                                    if(query.indexOf("()") != -1)
                                    {
                                        StringBuffer b = new StringBuffer(query);
                                        int indexblank = b.indexOf("()");
                                        b = b.replace(indexblank, indexblank + 2, "");
                                        query = b.toString();
                                    }
                                }

                        }
                        if(colNameINNull.indexOf(" IN ") != -1)
                        {
                            if(query.indexOf(" IN ('blank')") != -1)
                            {
                                colNameINNull = colNameINNull.replace('(', '%');
                                colNameINNull = colNameINNull.replaceAll("%", "");
                                query = query.replaceAll(colNameINNull, "");
                                query = query.replaceAll("'blank'", "");
                                if(query.indexOf("()") != -1)
                                {
                                    StringBuffer b = new StringBuffer(query);
                                    int indexblank = b.indexOf("()");
                                    b = b.replace(indexblank, indexblank + 2, "");
                                    query = b.toString();
                                }
                            }
                            if(query.indexOf(" IN (?)") != -1)
                            {
                                colNameINNull = colNameINNull.replace('(', '%');
                                colNameINNull = colNameINNull.replaceAll("%", "");
                                query = query.replaceAll(colNameINNull, "");
                                query = query.replace('?', '@');
                                query = query.replaceAll("@", "");
                                if(query.indexOf("()") != -1)
                                {
                                    StringBuffer b = new StringBuffer(query);
                                    int indexblank = b.indexOf("()");
                                    b = b.replace(indexblank, indexblank + 2, "");
                                    query = b.toString();
                                }
                            }
                        }
                        if(colNull.indexOf(") AND") != -1 || colNull.indexOf(" AND") != -1 || colNull.indexOf(" like ") != -1)
                        {
                            colNull = colNull.replace(')', '%');
                            colNull = colNull.replaceAll("%", "");
                            if(colNull.toUpperCase().indexOf("SELECT") == -1)
                                query = query.replaceAll((new StringBuilder(String.valueOf(colNull))).append(nullResult).toString(), (new StringBuilder(String.valueOf(colNull))).append("'%'").toString());
                        } else
                        if(colNull.toUpperCase().indexOf("SELECT") == -1)
                            query = query.replaceAll((new StringBuilder(String.valueOf(colNull))).append(nullResult).toString(), "");
                    } else
                    if(query.indexOf(" IN ('blank')") != -1)
                    {
                        colNameINNull = colNameINNull.replace('(', '%');
                        colNameINNull = colNameINNull.replaceAll("%", "");
                        StringBuffer b1 = new StringBuffer(query);
                        if(colNameINNull.indexOf("WHERE") != -1)
                        {
                            int indx = query.indexOf(colNameINNull);
                            int whereIndex = colNameINNull.indexOf("WHERE");
                            int nextIndex = indx + whereIndex + 5;
                            int lastbeforeIndex = colNameINNull.indexOf("IN");
                            int lastIndex = indx + lastbeforeIndex + 2;
                            b1 = b1.replace(nextIndex, lastIndex, "");
                            query = b1.toString();
                        } else
                        {
                            if(colNameINNull.startsWith(")"))
                                colNameINNull = colNameINNull.substring(colNameINNull.indexOf(")") + 1, colNameINNull.length());
                            int length = colNameINNull.length();
                            int indx = query.indexOf(colNameINNull);
                            int total = indx + length;
                            b1 = b1.replace(indx, total, "");
                            query = b1.toString();
                        }
                        query = query.replaceAll("'blank'", "");
                        if(query.indexOf("()") != -1)
                        {
                            StringBuffer b = new StringBuffer(query);
                            int indexblank = b.indexOf("()");
                            b = b.replace(indexblank, indexblank + 2, "");
                            query = b.toString();
                        }
                    }
                }

                for(int queryVal = 0; queryVal < countQuery; queryVal++)
                {
                    //EBWLogger.logDebug("QueryExecutor", "INSIDE removing like Clause");
                    String result = (new StringBuilder(String.valueOf(queryStrArr[queryVal]))).append("'%'").toString();
                    //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("result :")).append(result).toString());
                    //String toBeREmoved = null;
                    StringBuffer b = new StringBuffer(query);
                    if(result.toLowerCase().indexOf("like '%'") != -1)
                    {
                        if(result.indexOf("(") != -1 || result.indexOf(")") != -1)
                        {
                            StringBuffer b1 = new StringBuffer(result);
                            b1 = b1.replace(0, 1, "");
                            result = b1.toString();
                        }
                        String resultArr[] = result.split(" ");
                        if(resultArr.length > 3 && (new StringBuilder(String.valueOf(resultArr[resultArr.length - 2]))).append(" ").append(resultArr[resultArr.length - 1]).toString().equalsIgnoreCase("like '%'"))
                            if(!resultArr[resultArr.length - 4].toUpperCase().equalsIgnoreCase("WHERE")){
                                result = (new StringBuilder(String.valueOf(resultArr[resultArr.length - 4]))).append(" ").append(resultArr[resultArr.length - 3]).append(" ").append(resultArr[resultArr.length - 2]).append(" ").append(resultArr[resultArr.length - 1]).toString();
                                if(result.startsWith("AND (")){
                                	result = result.substring(5,result.length());
                                } else if( result.startsWith("(") && result.indexOf("UPPER" )> -1) {
                                	result = result.substring(1,result.length());
                                	}else if( result.startsWith("(") ) {
                                	result = result.substring(1,result.length());
                                	}
                                }
                            else
                            if(resultArr[resultArr.length - 4].toUpperCase().equalsIgnoreCase("WHERE"))
                            {
                                String result1 = (new StringBuilder(String.valueOf(resultArr[resultArr.length - 3]))).append(" ").append(resultArr[resultArr.length - 2]).append(" ").append(resultArr[resultArr.length - 1]).toString();
                                if(query.indexOf((new StringBuilder(String.valueOf(result1))).append(" AND").toString()) > -1)
                                    result = (new StringBuilder(String.valueOf(result1))).append(" AND").toString();
                                else
                                if(query.indexOf(result1) > -1)
                                    result = result1;
                            }
                        int Index = query.indexOf(result);
                        int total = result.length();
                        if(Index > -1)
                            b = b.replace(Index, Index + total, "");
                        query = b.toString();
                    }
//                  clause to remove or upper part from the query with like '%'
                    else if(result.startsWith(" OR UPPER(") && result.indexOf("like  '%'") >-1){
                    	 result = result.replace("like  '%'", "like '%'");
                    	
                    	if( (result.indexOf("like") >-1) && (result.indexOf("'%'") >-1) ){
                    		
                    		int Index = b.indexOf(result);
                            int total = result.length();
                            if(Index > -1)
                                b = b.replace(Index, Index + total, "");
                            query = b.toString();
                    	}
                    } 
                    for(; query != null && query.indexOf("  ") > -1; query = query.replaceAll("  ", " "));
                    for(; query != null && query.indexOf("   ") > -1; query = query.replaceAll("   ", " "));
                    for(; query != null && query.indexOf("    ") > -1; query = query.replaceAll("    ", " "));
                   // EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Query after removing like Clause is :")).append(b.toString()).toString());
                }
                
                

                /*if(query.indexOf("FNA_FA_ID='%'") > -1 || query.indexOf("fna_fa_id='%'") > -1)
                {
                    //EBWLogger.logDebug(this, "Enter into CARTS..");
                } else
                {*/
                    for(int queryVal = 0; queryVal < countEqualQuery; queryVal++)
                    {
                        //EBWLogger.logDebug("QueryExecutor", "INSIDE removing = Clause");
                        for(; query != null && query.indexOf("= '%'") > -1; query = query.replaceAll("= '%'", "='%'"));
                        String result = (new StringBuilder(String.valueOf(queryEqualStrArr[queryVal]))).append("'%'").toString();
                       // EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Value of the result is :")).append(result).toString());
                        for(; result != null && result.indexOf("= '%'") > -1; result = result.replaceAll("= '%'", "='%'"));
                        if(result.toLowerCase().indexOf("select") > -1)
                        {
                           // EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Value of the result is ")).append(result).toString());
                            if(result.indexOf("='%'") > -1)
                            {
                                if(result.indexOf("AND") > -1)
                                {
                                    int StartIndex = result.toUpperCase().indexOf("AND");
                                    int lastIndex = result.lastIndexOf("='%'");
                                    StringBuffer b = new StringBuffer(result);
                                    b.replace(StartIndex, lastIndex + 4, "");
                                    result = b.toString();
                                    query = result;
                                }
                                //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Result in ='%' clause from the query :")).append(query).toString());
                            }
                        } else
                        if(result.toLowerCase().indexOf(",") > -1)
                        {
                            if(result.toLowerCase().indexOf("where") > -1 && result.toLowerCase().indexOf("='%'") > -1 || result.toLowerCase().indexOf("= '%'") > -1)
                            {
                                if(result.toLowerCase().indexOf("= '%'") > -1)
                                    result = result.replaceAll("= '%'", "='%'");
                                int setStartIndex = result.toUpperCase().indexOf("WHERE");
                                int setEndIndex = result.lastIndexOf("='%'");
                                String updatesetString = result.substring(setStartIndex + 5, setEndIndex + 4);
                                query = query.replaceAll(updatesetString, "");
                            } //else
                           // if(result.toLowerCase().indexOf("coalesce( '%'") > -1)
                                //EBWLogger.logDebug("QueryExecutor", "Skip removing coalesce( '%' function");
                            else
                            if(result.toLowerCase().indexOf(")) and") > -1)
                            {
                                if(result.toLowerCase().indexOf("and") > -1 && result.toLowerCase().trim().indexOf("='%'") > -1 || result.toLowerCase().trim().indexOf("= '%'") > -1)
                                {
                                    if(result.toLowerCase().indexOf("= '%'") > -1)
                                        result = result.replaceAll("= '%'", "='%'");
                                    int lastIndex = result.toUpperCase().lastIndexOf("AND");
                                    StringBuffer b = new StringBuffer(result);
                                    if(lastIndex > 0)
                                        b.replace(0, lastIndex - 1, "");
                                    result = b.toString();
                                    query = query.replaceAll(result, "");
                                }
                                //EBWLogger.logDebug("QueryExecutor", "Skip removing from the query");
                            } else
                            {
                                query = query.replaceAll(result, "");
                            }
                        } else
                        if(result.toLowerCase().indexOf("update") > -1 && result.toLowerCase().indexOf("set") > -1 && result.toLowerCase().indexOf("='%'") > -1)
                        {
                            int setStartIndex = result.toUpperCase().indexOf("SET");
                            int setEndIndex = result.lastIndexOf("='%'");
                            String updatesetString = result.substring(setStartIndex + 4, setEndIndex + 4);
                            query = query.replaceAll(updatesetString, "");
                        } else
                        {
                            if(result.toLowerCase().indexOf("and") > -1 && result.toLowerCase().trim().indexOf("='%'") > -1)
                            {
                                int lastIndex = result.toUpperCase().lastIndexOf("AND");
                                StringBuffer b = new StringBuffer(result);
                                if(lastIndex > 0)
                                    b.replace(0, lastIndex - 1, "");
                                result = b.toString();
                                query = query.replaceAll(result, "");
                            }
                            //EBWLogger.logDebug("QueryExecutor", "INSIDE removing ='%' from else Clause");
                        }
                    }

                    for(int queryVal = 0; queryVal < countINQuery; queryVal++)
                    {
                        //EBWLogger.logDebug("QueryExecutor", "INSIDE removing IN ('%') Clause");
                        StringBuffer b;
                        for(; query != null && query.indexOf("IN ('%')") > -1; query = b.toString())
                        {
                            b = new StringBuffer(query);
                            int INindex = query.toUpperCase().indexOf("IN ('%')");
                            b.replace(INindex + 2, INindex + 3, "");
                        }

                        String result = (new StringBuilder(String.valueOf(queryINStrArr[queryVal]))).append("'%'").toString();
                        //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Value of the result in IN ('%') is :")).append(result).toString());
                        for(; result != null && result.indexOf("IN (") > -1; result = b.toString())
                        {
                            b = new StringBuffer(result);
                            int INindex = result.toUpperCase().indexOf("IN (");
                            b.replace(INindex + 2, INindex + 3, "");
                        }

                        if(result.toLowerCase().indexOf("select") > -1)
                        {
                            //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Value of the result is ")).append(result).toString());
                            if(result.toUpperCase().indexOf("IN('%'") > -1)
                            {
                                int StartIndex = result.toUpperCase().indexOf("WHERE");
                                int lastIndex = result.toUpperCase().lastIndexOf("IN('%'");
                                result = result.substring(StartIndex + 5, lastIndex + 6);
                                b = new StringBuffer(query);
                                b.replace(query.indexOf(result), query.indexOf(result) + result.length() + 1, "");
                                query = b.toString();
                                //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Result in IN('%') clause from the query :")).append(query).toString());
                            }
                        } else
                        if(result.toLowerCase().indexOf(",") > -1)
                        {
                            if(result.toLowerCase().indexOf("where") > -1 && result.toUpperCase().indexOf("IN('%'") > -1 || result.toLowerCase().indexOf("IN ('%')") > -1)
                            {
                                if(result.toLowerCase().indexOf("IN ('%')") > -1)
                                    result = result.replaceAll("IN ", "IN");
                                int setStartIndex = result.toUpperCase().indexOf("WHERE");
                                int setEndIndex = result.lastIndexOf("IN('%')");
                                String updatesetString = result.substring(setStartIndex + 5, setEndIndex + 4);
                                StringBuffer b1 = new StringBuffer(query);
                                b1.replace(query.indexOf(updatesetString), query.indexOf(updatesetString) + updatesetString.length() + 1, "");
                                query = b1.toString();
                            } //else
                            //if(result.toLowerCase().indexOf("coalesce( '%'") > -1)
                                //EBWLogger.logDebug("QueryExecutor", "Skip removing coalesce( '%' function");
                            else
                            if(result.toLowerCase().indexOf(")) and") > -1)
                            {
                                if(result.toLowerCase().indexOf("and") > -1 && result.toUpperCase().indexOf("IN('%'") > -1 || result.toLowerCase().trim().indexOf("IN ('%'") > -1)
                                {
                                    if(result.toLowerCase().indexOf("IN ('%')") > -1)
                                        result = result.replaceAll("IN ", "IN");
                                    int lastIndex = result.toUpperCase().lastIndexOf("AND");
                                    b = new StringBuffer(result);
                                    if(lastIndex > 0)
                                        b.replace(0, lastIndex - 1, "");
                                    result = b.toString();
                                    StringBuffer b1 = new StringBuffer(query);
                                    b1.replace(query.indexOf(result), query.indexOf(result) + result.length() + 1, "");
                                    query = b1.toString();
                                }
                                //EBWLogger.logDebug("QueryExecutor", "Skip removing from the query");
                            } else
                            {
                                StringBuffer b1 = new StringBuffer(query);
                                b1.replace(query.indexOf(result), query.indexOf(result) + result.length() + 1, "");
                                query = b1.toString();
                            }
                        } else
                        if(result.toLowerCase().indexOf("update") > -1 && result.toLowerCase().indexOf("set") > -1 && result.toLowerCase().indexOf("IN('%'") > -1)
                        {
                            int setStartIndex = result.toUpperCase().indexOf("SET");
                            int setEndIndex = result.lastIndexOf("IN('%')");
                            String updatesetString = result.substring(setStartIndex + 4, setEndIndex + 4);
                            StringBuffer b1 = new StringBuffer(query);
                            b1.replace(query.indexOf(updatesetString), query.indexOf(updatesetString) + updatesetString.length() + 1, "");
                            query = b1.toString();
                        } else
                        {
                            if(result.toLowerCase().indexOf("and") > -1 && result.toUpperCase().indexOf("IN('%'") > -1)
                            {
                                int lastIndex = result.toUpperCase().lastIndexOf("AND");
                                b = new StringBuffer(result);
                                if(lastIndex > 0)
                                    b.replace(0, lastIndex - 1, "");
                                result = b.toString();
                                StringBuffer b1 = new StringBuffer(query);
                                b1.replace(query.indexOf(result), query.indexOf(result) + result.length() + 1, "");
                                query = b1.toString();
                            }
                            //EBWLogger.logDebug("QueryExecutor", "INSIDE removing IN('%') from else Clause");
                        }
                    }

                    if(query.toLowerCase().indexOf("coalesce( '%'") > -1)
                    {
                        int count = 0;
                        for(int i = 0; (i = query.indexOf("coalesce( '%'", i)) >= 0; i++)
                            count++;

                        for(int x = 0; x < count; x++)
                        {
                            StringBuffer b = new StringBuffer(query);
                            int indx = query.indexOf("coalesce( '%'");
                            b.replace(indx, indx + 13, "coalesce(NULL");
                            query = b.toString();
                        }

                    }
                    if(query.toUpperCase().substring(query.length() - 4).equals("AND "))
                    {
                        StringBuffer b = new StringBuffer(query);
                        b.replace(query.length() - 4, query.length(), "");
                        query = b.toString();
                    }
                    if(query.toUpperCase().indexOf("WHERE ") > -1 && query.indexOf("='%'") > -1 || query.indexOf("= '%'") > -1)
                    {
                        if(query.indexOf("= '%'") > -1)
                            query = query.replaceAll("= '%'", "='%'");
                       
                        	
        					
        					if(query.indexOf("'%'")!=-1){
        					String whereToken="";
        					String whereQuery="";
        					query = query.replaceAll(" WHERE ", " where ");
        					query.replaceAll(" AND ", " and ");
        					String whereList[]=query.split(" where");
        					for(int j=0;j<whereList.length;j++){
        						whereToken=whereList[j];
        						if(whereToken.indexOf(" and ")!= -1){
        							
        							String INNERQUERY="";
                					String token="";
                					
                					//query=query.replaceAll(" and ", " AND ");
                					String aList[]=whereToken.split(" and ");
                					
                					for(int i=0;i<aList.length;i++){
                						  token=aList[i];

                						  if(token.indexOf("'%'")!=-1 && token.indexOf("#")==-1 && token.indexOf("'%')")==-1){
                							  token="";
                							  
                						  }else if(token.indexOf("'%')")!=-1){
                							  int tokenindex = token.indexOf("'%')");
                							  token=token.substring(tokenindex+3, token.length());
                							  token = token.trim();
                							  
                							 
                						  }
                						  if(token.indexOf("#")!=-1){
                							  String hashToken;
                							  int HashIdx = token.indexOf("#");                							  
                							  hashToken = token.substring(0,HashIdx);
                							  token = token.substring(HashIdx,token.length());                							  
                							  if(hashToken.indexOf("'%'")!=-1)
                							  hashToken = "";
                							  
                							  token=hashToken+token;
                						  }
                						  
                						  INNERQUERY=INNERQUERY+" and "+token;
                						 
                					  }
                					if(INNERQUERY.startsWith(" and")){
                						INNERQUERY=INNERQUERY.substring(4, INNERQUERY.length());
                					}
                					whereToken=INNERQUERY;
                					
        							
        						}else{
        							if(whereToken.indexOf("'%'")!=-1){
        								whereToken =whereToken.substring(whereToken.indexOf("'%'")+3, whereToken.length());
        							}
        						}
        						whereQuery=whereQuery+" where "+whereToken;
        						whereQuery=whereQuery.trim();
        						
        						 
        						
        						if(whereQuery.startsWith("where")){
            						whereQuery=whereQuery.substring(6, whereQuery.length());
            					}
        				
        					}
        					
        					if(whereQuery != null && whereQuery.indexOf(" and )") != -1)
   							 whereQuery = whereQuery.replaceAll(" and \\)", "\\)");
        					
        					query=whereQuery;
        					query = query.replaceAll(" where ", " WHERE ");
        					query = query.replaceAll(" and ", " AND ");
        					
        					
        					
        				}    
        				
        					
                        
                    }
                    if(query.toUpperCase().substring(query.length() - 6).equals("WHERE "))
                    {
                        StringBuffer b1 = new StringBuffer(query);
                        b1.replace(query.length() - 6, query.length(), "");
                        query = b1.toString();
                    }
                    
                    if(query.toUpperCase().indexOf("SET  ,") > -1)
                    {
                        StringBuffer b = new StringBuffer(query);
                        int setIndex = query.toUpperCase().indexOf("SET ,");
                        b.replace(setIndex + 4, setIndex + 5, "");
                        query = b.toString();
                    }
                
                
                if(query.toUpperCase().indexOf("WHERE  AND") != -1)
                    query = query.toUpperCase().replaceAll("WHERE  AND", "WHERE AND");
                if(query.toUpperCase().indexOf("WHERE      AND") != -1){
                    query = query.toUpperCase().replaceAll("WHERE      AND", "WHERE ");
                }
                if(query.toUpperCase().indexOf("WHERE   AND") != -1)
                    query = query.replaceAll("WHERE   AND", "WHERE");
               
                if(query.toUpperCase().indexOf("WHERE AND") != -1)
                {
                    query = query.toUpperCase().replaceAll("WHERE AND", "WHERE");
                   
                    //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("WHERE AND Scenario -- Updated Query is")).append(query).toString());
                } else
                if(query.toUpperCase().indexOf("WHERE OR") != -1)
                {
                    query = query.toUpperCase().replaceAll("WHERE OR", "WHERE");
                    //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("WHERE OR Scenario -- Updated Query is")).append(query).toString());
                }
                if(query.toUpperCase().indexOf(" AND ") == -1 && query.toUpperCase().indexOf(" OR ") == -1 && query.toUpperCase().indexOf(" NOT ") == -1)
                    if(query.toLowerCase().indexOf("update") != -1 || query.toLowerCase().indexOf("delete") != -1 || query.toLowerCase().indexOf("insert") != -1 || query.toLowerCase().indexOf("select") != -1)
                    {
                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Updated Query without replacement is :")).append(query).toString());
                    } else
                    {
                        query = query.replaceAll("  WHERE ", "");
                        query = query.replaceAll("  where ", "");
                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Replaced WHERE -- Updated Query is ")).append(query.toLowerCase()).toString());
                    }
                if(query.toUpperCase().indexOf("ORDER BY '") > -1 || query.toUpperCase().indexOf("ORDER BY  '") > -1)
                {
                    for(; query != null && query.indexOf("ORDER BY  '") > -1; query = query.replaceAll("ORDER BY  '", "ORDER BY '"));
                    StringBuffer b = new StringBuffer(query);
                    String orderbyLen = "ORDER BY '";
                    int setIndex = query.toUpperCase().indexOf("ORDER BY '");
                    b.replace((setIndex + orderbyLen.length()) - 1, setIndex + orderbyLen.length(), "");
                    query = b.toString();
                    if(query.lastIndexOf("'") == query.length() - 1)
                    {
                        StringBuffer b1 = new StringBuffer(query);
                        b1.replace(query.length() - 1, query.length(), "");
                        query = b1.toString();
                    }
                }
                
                             
            
             
              while (query.indexOf("  ")!=-1){
            	  query = query.replaceAll("  ", " ");
              }
              
              while (query.indexOf("AND AND")!=-1){
            	  query = query.replaceAll("AND AND", "AND");
              }
                
                if(query.toUpperCase().indexOf("WHERE AND WHERE ") != -1)
                {
                    query = query.toUpperCase().replaceAll("WHERE AND WHERE ", "WHERE");
                }
                if(query != null && query.toUpperCase().indexOf("WHERE AND") != -1)
                    query = query.replaceAll("WHERE AND", "WHERE ");
                
                if(query != null && query.toUpperCase().indexOf("OR OR") != -1)
                    query = query.toUpperCase().replaceAll("OR OR", "OR");
               
                
                
            } else
            {
                query = new String(sqlStmt);
            }
        }
        catch(MissingResourceException mre)
        {
            throw new EbwException(this, mre);
        }
        catch(ClassNotFoundException cstEx)
        {
            throw new EbwException(this, cstEx);
        }
        catch(Exception e)
        {
            throw e;
        }
        EBWLogger.trace("QueryExecutor :", "Exiting from getQueryString");
        return query;
    }

    private Object executeUpdate(String stmtName, Object obj)
        throws Exception
    {
        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Starting Method executeUpdate(")).append(stmtName).append(",").append(obj).append(")").toString());
        String sqlStmt = "";
        Object result = "";
        try
        {
            sqlStmt = getQueryValue((new StringBuilder(String.valueOf(stmtName))).append(".Query").toString(), true);
            init(stmtName);
            String query = null;
           // String query_audit = null;
          
           /* if(obj instanceof EBWTransferObject)
            {
                EBWTransferObject objEBWTransferObject = (EBWTransferObject)obj;
              
                query = getInsertQueryString(sqlStmt, objEBWTransferObject);
                
                
               
                EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("The Latest Query Generated is :-")).append(query).toString());
            } else
            {
               
                query = getQueryString(sqlStmt, obj);
                
                EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("The Latest Query Generated is :-")).append(query).toString());
            }*/
                
            query = getInsertQueryString(sqlStmt, obj);
            Statement stmt = null;
            PreparedStatement preStmt = null;
          
            if(strStmtType.equalsIgnoreCase(STMT_ONLY))
            {
                stmt = con.createStatement();
                result = new Integer(stmt.executeUpdate(query));
               
            } else
            if(strStmtType.equalsIgnoreCase(STMT_PREP))
            {
                preStmt = con.prepareStatement(query);
                SetPreparedStmtValues(parameterCount, preStmt);
                result = new Integer(preStmt.executeUpdate());
            }
            closeStatement(stmt, null);
        }
        catch(SQLException sqlEx)
        {
            throw new EbwException(this, sqlEx);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        EBWLogger.trace("QueryExecutor :", "Exit from Method executeUpdate  ");
        return result;
    }

	private String getInsertQueryString(String sqlStmt, Object obj)  throws Exception {

        EBWLogger.logDebug("QueryExecutor: ", (new StringBuilder("Starting Method  getQueryString(")).append(sqlStmt).append(",").append(obj).append(")").toString());
        int rowcount=0;
        parameterCount = 0;
        values = new Object[100];
        String query = null;
        String urclause="With ur";
        String colNull = "";
        String colNullResult = "";
        String nullResult = "";
        String colNameINNull = "";
        String colNameINNullInt = "";
        String nullCheck = "";
        String resultPrepopulator[] = (String[])null;
        String colValNullIN[] = (String[])null;
        Integer colValIntIN[] = (Integer[])null;
        boolean param_incr = false;
        
        ArrayList attrTO = null;
       
        try
        {
            if(sqlStmt.indexOf("#") != -1)
            {
                String clause[] = sqlStmt.split("#");
                query = new String();
                int index = -1;
                if(obj instanceof EBWTransferObject)
                    try
                    {
                        attrTO = Prepopulator.LoadTODefValues((EBWTransferObject)obj, con);
                      
                        if(attrTO == null || attrTO != null && attrTO.isEmpty())
                        {
                            EBWLogger.logDebug("QueryExecutor :", "Value of the arrayList is empty");
                        } else
                        {
                            Prepopulator.fillTransferObject((EBWTransferObject)obj, attrTO, con);
                           
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                for(int i = 0; i < clause.length; i++)
                {
                    if(clause[i].indexOf("(") == -1 && clause[i].indexOf(",") == -1 && clause[i].indexOf(" like ") == -1 && clause[i].indexOf("=") == -1 && clause[i].indexOf(")") == -1 &&  clause[i].trim().length() > 0)
                    {
                        String condition[] = (String[])null;
                        if(obj instanceof EBWTransferObject)
                        {
                            cls = Class.forName(obj.getClass().getName());
                            String value = null;
                           if(clause[i].indexOf(":") > -1)
                            {
                                condition = clause[i].split(":");
                              
                                char fieldChar[] = {
                                    condition[0].trim().charAt(0)
                                };
                                String methodName = (new StringBuilder("get")).append((new String(fieldChar)).toUpperCase()).append(condition[0].trim().substring(1)).toString();
                                try
                                {
                                    Method method = cls.getMethod(methodName, null);
                                    Object colValue = method.invoke(obj, null);
                                 
                                 
                                    
                                    EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("ColValue is :")).append(colValue).append(" For method ").append(methodName).toString());
                                    if(colValue == null)
                                    {
                                    	if(strStmtType.equals(STMT_PREP))
                                        {
                                            
                                            param_incr = true;
                                            if(condition[1].equalsIgnoreCase("numeric"))
                                                values[parameterCount++] = new NullObj(2);
                                            else
											if(condition[1].equalsIgnoreCase("BigDecimal"))
                                                  values[parameterCount++] = new NullObj(6);
											else
											if(condition[1].equalsIgnoreCase("timestamp"))
                                                values[parameterCount++] = new NullObj(3);
                                            else
                                            if(condition[1].equalsIgnoreCase("date"))
                                                values[parameterCount++] = new NullObj(91);
                                            else
                                            if(condition[1].equalsIgnoreCase("varchar"))
                                                values[parameterCount++] = new NullObj(12);
                                            else
                                            if(condition[1].equalsIgnoreCase("double"))
                                                values[parameterCount++] = new NullObj(8);
                                            else
                                                param_incr = false;
                                           
                                            clause[i] = "?";
                                        } else
                                        if(strStmtType.equals(STMT_ONLY))
                                        {
                                            clause[i] = null;
                                           
                                        }
                                    } else
                                    {
                                        values[parameterCount++] = colValue;
                                        if(strStmtType.equals(STMT_PREP))
                                        {
                                            if(condition[1].equalsIgnoreCase("numeric"))
                                            {
                                              
                                            	if(colValue instanceof Integer[])
                                                {
                                                    colValIntIN = (Integer[])colValue;
                                                    if(colValIntIN != null)
                                                    {
                                                        for(int k = 0; k < colValIntIN.length; k++)
                                                       	 colNameINNullInt = clause[i - 1];
                                                    }
                                                    value = ConvIntergerArrToInteger((Integer[])colValue);
                                                    clause[i] = "?";  
                                                }else if(colValue instanceof Double[])
                                                {
                                               
                                               	 Double[] arr=(Double[])colValue;
                                               	 String DOUBLE_ARRAY="";
                                               	 clause[i]="";
                                               	 for(int l=0;l<arr.length;l++){
                                               		 
	                                               		Double dObj1 = new Double(arr[l]);
	                                               		DOUBLE_ARRAY=DOUBLE_ARRAY+dObj1.toString()+",";
	                                               		clause[i] = clause[i]+"?"+",";
                                               	 }
                                               	 clause[i]=clause[i].substring(0,clause[i].length()-1).trim();
                                               	 
                                              	
                                              	} else if(colValue instanceof Integer)
                                                {
                                              		value = ConvertionUtil.convertToString(colValue);
                                              		clause[i] = "?";  
                                                } else{
                                           		value = ConvertionUtil.convertToString((Double)colValue);
                                           		clause[i] = "?";  
                                                }          
                                            
                                            } else
                                            if(condition[1].equalsIgnoreCase("java.lang.Double"))
                                            {
                                                value = ConvertionUtil.convertToString((Double)colValue);
                                                clause[i] = "?";
                                            } else if(condition[1].equalsIgnoreCase(BigDecimal))
                                            {
                                            
                                            	value =(colValue).toString();
                                                clause[i] = "?";
                                            }else
                                            {
                                                if(condition[1].equalsIgnoreCase("date")){
                                                	value = ConvertionUtil.convertToDBDateStr(ConvertionUtil.convertToString(colValue)); 
                                                	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                                                	Date dd = df.parse(value);
                                                	values[parameterCount-1] = dd;
                                                    clause[i] = "?";
                                                }
                                                else
                                                    if(condition[1].equalsIgnoreCase("timestamp"))
                                                    {
                                                        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
                                                        value = dtFormat.format(colValue);
                                                        clause[i] = "?";
                                                    }
												else
                                                if(condition[1].equalsIgnoreCase("varchar"))
                                                    if(colValue instanceof String[])
                                                    {
                                                       
                                                    	String[] arr=(String[])colValue;
                                                        
                                                     	 clause[i]="";
                                                     	 for(int l=0;l<arr.length;l++){
                                                     		 clause[i] = clause[i]+"?"+",";
                                                     	 }
                                                     	 clause[i]=clause[i].substring(0,clause[i].length()-1).trim();
                                                     	
                                                    } else
                                                    {
                                                        value = (String)colValue;
                                                        clause[i] = "?";
                                                    }
                                                }
                                        } else
                                        if(condition[1].equalsIgnoreCase("numeric"))
                                        {
                                          	 if(colValue instanceof Integer[])
                                               {
                                                   colValIntIN = (Integer[])colValue;
                                                   if(colValIntIN != null)
                                                   {
                                                       for(int k = 0; k < colValIntIN.length; k++){
                                                      	 colNameINNullInt = clause[i - 1];
                                                         
                                                       }
                                                   }
                                                   value = ConvIntergerArrToInteger((Integer[])colValue);
                                                   clause[i] = value;  
                                               }else if ((colValue.getClass().getName()).equals("java.lang.Integer") ||(colValue.getClass().getName()).equals("java.math.BigDecimal") ){                                            	  
                                                   clause[i] = colValue.toString();
                                               }else{
                                             
                                               	  if(colValue.getClass().getName().equalsIgnoreCase("[Ljava.lang.Double;")){
                                               	
                                                     	Double[] arr=(Double[])colValue;
                                                     	String DOUBLE_ARRAY="";
                                                     	for(int l=0;l<arr.length;l++){
                                                     		Double dObj1 = new Double(arr[l]);
                                                     		DOUBLE_ARRAY=DOUBLE_ARRAY+dObj1.toString()+",";
                                                     	}
                                                     	clause[i]=DOUBLE_ARRAY.substring(0,DOUBLE_ARRAY.length()-1);
                                               	  }else{
                                               		  
                                               	   if(!(colValue.getClass().getName()).equals("java.lang.Double")){
                                               		 colValue=Double.parseDouble((String)colValue);
                                               	     } else {
                                               	    	 colValue=(Double)colValue;
                                               	     }
                                          		value = ConvertionUtil.convertToString((Double)colValue);
                                          		clause[i] = value;  
                                               	  }
                                               }
                                        }else if(condition[1].equalsIgnoreCase("BigDecimal")){
                                        	value =(colValue).toString();
                                        	clause[i] = value;
                                        } else
                                        {
                                            if(condition[1].equalsIgnoreCase("date"))
                                                value = ConvertionUtil.convertToDBDateStr(ConvertionUtil.convertToString(colValue));
                                            else
                                            if(condition[1].equalsIgnoreCase("timestamp"))
                                            {
                                              
                                                SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
                                                value = dtFormat.format(colValue);
                                            }/* else
                                            if(condition[1].equalsIgnoreCase("blob"))
                                            {
                                                byteData = (byte[])colValue;
                                                clause[i] = new String(byteData);
                                            }*/ else
                                            if(condition[1].equalsIgnoreCase("varchar"))
                                                if(colValue instanceof String[])
                                                {
                                                    colValNullIN = (String[])colValue;
                                                    if(colValNullIN != null)
                                                    {
                                                        for(int k = 0; k < colValNullIN.length; k++)
                                                            colNameINNull = clause[i - 1];

                                                    }
                                                    value = ConvStringArrToStr((String[])colValue);
                                                } else
                                                if(colValue instanceof Double)
                                                {
                                                    value = ConvertionUtil.convertToString((Double)colValue);
                                                    clause[i] = value;
                                                } else
                                                {
                                                    value = (String)colValue;
                                                }
                                            clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                                '\''
                                            })))).append(value).append(new String(new char[] {
                                                '\''
                                            })).toString();
                                          
                                        
                                        }
                                        
                                    }
                                }
                                catch(Exception e)
                                {
                                    EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Method get")).append(StringUtil.initCaps(methodName)).append(" not found in TO ").append(obj).toString());
                                    clause[i] = (new StringBuilder("#")).append(clause[i]).append("#").toString();
                                }
                            } else
                            if(clause[i].trim().length() > 0 && !clause[i].trim().equalsIgnoreCase(urclause))
                            {
                                char fieldChar[] = {
                                    clause[i].charAt(0)
                                };
                                String methodName = (new StringBuilder("get")).append((new String(fieldChar)).toUpperCase()).append(condition[0].trim().substring(1)).toString();
                                Method method = cls.getMethod(methodName, null);
                                Object colValue = method.invoke(obj, null);
                              
                                 Field field = cls.getDeclaredField(clause[i]);
                                    if(field.getType().getName().equals("java.lang.Double"))
                                    {
                                        value = ConvertionUtil.convertToString((Double)colValue);
                                        clause[i] = value;
                                    } else
                                    {
                                        if(field.getType().getName().equals("java.lang.String"))
                                            value = (String)colValue;
                                        else
                                        if(field.getType().getName().equals("date"))
                                            value = ConvertionUtil.convertToString((Date)colValue);
                                        else
                                        if(field.getType().getName().equals("java.lang.String[]"))
                                        {
                                            value = ConvertionUtil.convertToCSV((String[])colValue);
                                           
                                        }
                                        clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                            '\''
                                        })))).append(value).append(new String(new char[] {
                                            '\''
                                        })).toString();
                                    }
                                
                            } else {
                            	if(clause[i].trim().equalsIgnoreCase(urclause)){
                            		clause[i] = " "+urclause;
                            	}
                            }
                        } else
                        if(obj instanceof Object[])
                        {
                          
                            condition = clause[i].split(":");
                            index++;
                            Object data[] = (Object[])obj;
                            if(strStmtType.equals(STMT_PREP))
                                clause[i] = "?";
                            else
                            if(condition[1].equalsIgnoreCase("numeric"))
                                clause[i] = ConvertionUtil.convertToString((Double)data[index]);
                            else
                            if(condition[1].equalsIgnoreCase("date"))
                                clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                    '\''
                                })))).append(((Date)data[index]).toString()).append(new String(new char[] {
                                    '\''
                                })).toString();
                            else
                            if(condition[1].equalsIgnoreCase("varchar"))
                                clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                    '\''
                                })))).append(data[index]).append(new String(new char[] {
                                    '\''
                                })).toString();
                        } else
                        if(obj instanceof HashMap)
                        {
                          
                            condition = clause[i].split(":");
                            HashMap data = (HashMap)obj;
                            Object value = data.get(condition[0].trim());
                            if(value == null && sqlStmt.toLowerCase().indexOf("select") > -1)
                            {
                                clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                    '\''
                                })))).append("%").append(new String(new char[] {
                                    '\''
                                })).toString();
                            } else
                            {
                                values[parameterCount++] = value;
                                if(strStmtType.equals(STMT_PREP))
                                {
                                    clause[i] = "?";
                                } else
                                {
                                  
                                    if(condition[1].equalsIgnoreCase("numeric"))
                                        clause[i] = ConvertionUtil.convertToString(value);
                                    else
                                    if(condition[1].equalsIgnoreCase("date") && value != null)
                                        clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                            '\''
                                        })))).append(ConvertionUtil.convertToString(value)).append(new String(new char[] {
                                            '\''
                                        })).toString();
                                    else
                                    if(condition[1].equalsIgnoreCase("varchar") && value != null)
                                        clause[i] = (new StringBuilder(String.valueOf(new String(new char[] {
                                            '\''
                                        })))).append((String)value).append(new String(new char[] {
                                            '\''
                                        })).toString();
                                    else
                                    if(value == null)
                                        clause[i] = null;
                                }
                            }
                        } 
                    }
                    query = (new StringBuilder(String.valueOf(query))).append(clause[i]).toString();
                   
                    boolean whereFlag = false;
                    if(colNull.indexOf("WHERE") != -1)
                    {
                         int indexData = colNull.indexOf("WHERE");
                        colNullResult = colNull.substring(indexData + 5, colNull.length());
                        String nullStr = null;
                        String str[] = colNull.split(" ");
                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("str last :")).append(str[str.length - 1]).toString());
                        if(str[str.length - 1].equalsIgnoreCase("like"))
                        {
                           
                            if(!str[str.length - 3].equalsIgnoreCase("where"))
                            {
                                whereFlag = true;
                                nullStr = (new StringBuilder(String.valueOf(str[str.length - 3]))).append(" ").append(str[str.length - 2]).append(" ").append(str[str.length - 1]).toString();
                                nullStr = nullStr.trim();
                                if(nullStr.startsWith("(")){
                                	nullStr = nullStr.substring(1,nullStr.length());
                                }
                            } else
                            if(str[str.length - 3].equalsIgnoreCase("where"))
                            {
                                
                                String toBeRemoved = (new StringBuilder(String.valueOf(str[str.length - 2]))).append(" ").append(str[str.length - 1]).append("like '%' AND").toString();
                                if(query.indexOf(toBeRemoved) > -1)
                                {
                                    nullStr = toBeRemoved;
                                    nullResult = "";
                                }
                            }
                            colNullResult = nullStr;
                        }
                        if(query.toUpperCase().startsWith("INSERT"))
                        {
                            if(strStmtType.equals(STMT_PREP) && param_incr)
                            {
                                String result = (new StringBuilder(String.valueOf(colNullResult))).append(nullResult).toString();
                                int d = indexData + 5;
                                int res = d + result.length() + 1;
                                StringBuffer b = new StringBuffer(query);
                                b = b.replace(d, res, "");
                                query = b.toString();
                                parameterCount--;
                                param_incr = false;
                            }
                        } else
                        if(colNull.toUpperCase().indexOf("SELECT") == -1)
                        {
                            if(query.indexOf((new StringBuilder(String.valueOf(colNullResult))).append(" ").append(nullResult).toString()) > -1)
                                query = query.replaceAll((new StringBuilder(String.valueOf(colNullResult))).append(" ").append(nullResult).toString(), "");
                           // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("query after removing like :")).append(query).toString());
                        } else
                        if(whereFlag)
                        {
                            query = query.replaceAll((new StringBuilder(String.valueOf(colNullResult))).append(" ").append(nullResult).toString(), "");
                        } else
                        {
                            if(colNull.indexOf("like") != -1)
                            {
                                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Clause[i] is :")).append(clause[i]).toString());
                                if(clause[i].indexOf(":") != -1)
                                {
                                    int colNullLast = query.lastIndexOf(colNull);
                                    int likeIndex = colNull.indexOf("like");
                                    int total = colNullLast + likeIndex + 5;
                                    int last = total + nullResult.length();
                                    StringBuffer b = new StringBuffer(query);
                                    b = b.replace(total, last, " '%'");
                                    query = b.toString();
                                    //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query for like Clause is :")).append(b.toString()).toString());
                                }
                            }
                            if(query.indexOf(":") != -1 && query.indexOf("=") != -1)
                                query = query.replaceAll((new StringBuilder(String.valueOf(colNullResult))).append(nullResult).toString(), "");
                        }
                        //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Updated Query in WHERE clause of executeQuery(String stmtName, Object obj) method is:")).append(query).toString());
                    } else
                    if(nullCheck == null)
                    {
                        if(resultPrepopulator != null)
                        {
                            for(int z = 0; z < resultPrepopulator.length; z++)
                                if(String.valueOf(resultPrepopulator[z]).equals("blank"))
                                {
                                    colNull = colNull.replace('(', '%');
                                    colNull = colNull.replaceAll("%", "");
                                    query = query.replaceAll(colNull, "");
                                    query = query.replaceAll(nullResult, "");
                                    if(query.indexOf("()") != -1)
                                    {
                                        StringBuffer b = new StringBuffer(query);
                                        int indexblank = b.indexOf("()");
                                        b = b.replace(indexblank, indexblank + 2, "");
                                        query = b.toString();
                                    }
                                }

                        }
                        
                        if(colNull.indexOf(") AND") != -1 || colNull.indexOf(" AND") != -1 || colNull.indexOf(" like ") != -1)
                        {
                            colNull = colNull.replace(')', '%');
                            colNull = colNull.replaceAll("%", "");
                       }   } 
                }

              /*  for(int queryVal = 0; queryVal < countQuery; queryVal++)
                {
                    //EBWLogger.logDebug("QueryExecutor", "INSIDE removing like Clause");
                    String result = (new StringBuilder(String.valueOf(queryStrArr[queryVal]))).append("'%'").toString();
                    //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("result :")).append(result).toString());
                    //String toBeREmoved = null;
                    StringBuffer b = new StringBuffer(query);
                    if(result.toLowerCase().indexOf("like '%'") != -1)
                    {
                        if(result.indexOf("(") != -1 || result.indexOf(")") != -1)
                        {
                            StringBuffer b1 = new StringBuffer(result);
                            b1 = b1.replace(0, 1, "");
                            result = b1.toString();
                        }
                        String resultArr[] = result.split(" ");
                        if(resultArr.length > 3 && (new StringBuilder(String.valueOf(resultArr[resultArr.length - 2]))).append(" ").append(resultArr[resultArr.length - 1]).toString().equalsIgnoreCase("like '%'"))
                            if(!resultArr[resultArr.length - 4].toUpperCase().equalsIgnoreCase("WHERE")){
                                result = (new StringBuilder(String.valueOf(resultArr[resultArr.length - 4]))).append(" ").append(resultArr[resultArr.length - 3]).append(" ").append(resultArr[resultArr.length - 2]).append(" ").append(resultArr[resultArr.length - 1]).toString();
                                if(result.startsWith("AND (")){
                                	result = result.substring(5,result.length());
                                } else if( result.startsWith("(") && result.indexOf("UPPER" )> -1) {
                                	result = result.substring(1,result.length());
                                	}else if( result.startsWith("(") ) {
                                	result = result.substring(1,result.length());
                                	}
                                }
                            else
                            if(resultArr[resultArr.length - 4].toUpperCase().equalsIgnoreCase("WHERE"))
                            {
                                String result1 = (new StringBuilder(String.valueOf(resultArr[resultArr.length - 3]))).append(" ").append(resultArr[resultArr.length - 2]).append(" ").append(resultArr[resultArr.length - 1]).toString();
                                if(query.indexOf((new StringBuilder(String.valueOf(result1))).append(" AND").toString()) > -1)
                                    result = (new StringBuilder(String.valueOf(result1))).append(" AND").toString();
                                else
                                if(query.indexOf(result1) > -1)
                                    result = result1;
                            }
                        int Index = query.indexOf(result);
                        int total = result.length();
                        if(Index > -1)
                            b = b.replace(Index, Index + total, "");
                        query = b.toString();
                    }
//                  clause to remove or upper part from the query with like '%'
                    else if(result.startsWith(" OR UPPER(") && result.indexOf("like  '%'") >-1){
                    	 result = result.replace("like  '%'", "like '%'");
                    	
                    	if( (result.indexOf("like") >-1) && (result.indexOf("'%'") >-1) ){
                    		
                    		int Index = b.indexOf(result);
                            int total = result.length();
                            if(Index > -1)
                                b = b.replace(Index, Index + total, "");
                            query = b.toString();
                    	}
                    } 
                    for(; query != null && query.indexOf("  ") > -1; query = query.replaceAll("  ", " "));
                    for(; query != null && query.indexOf("   ") > -1; query = query.replaceAll("   ", " "));
                    for(; query != null && query.indexOf("    ") > -1; query = query.replaceAll("    ", " "));
                   // EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Query after removing like Clause is :")).append(b.toString()).toString());
                }*/
                
                
                   /* for(int queryVal = 0; queryVal < countEqualQuery; queryVal++)
                    {
                        //EBWLogger.logDebug("QueryExecutor", "INSIDE removing = Clause");
                        for(; query != null && query.indexOf("= '%'") > -1; query = query.replaceAll("= '%'", "='%'"));
                        String result = (new StringBuilder(String.valueOf(queryEqualStrArr[queryVal]))).append("'%'").toString();
                       // EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Value of the result is :")).append(result).toString());
                        for(; result != null && result.indexOf("= '%'") > -1; result = result.replaceAll("= '%'", "='%'"));
                        if(result.toLowerCase().indexOf("select") > -1)
                        {
                           // EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Value of the result is ")).append(result).toString());
                            if(result.indexOf("='%'") > -1)
                            {
                                if(result.indexOf("AND") > -1)
                                {
                                    int StartIndex = result.toUpperCase().indexOf("AND");
                                    int lastIndex = result.lastIndexOf("='%'");
                                    StringBuffer b = new StringBuffer(result);
                                    b.replace(StartIndex, lastIndex + 4, "");
                                    result = b.toString();
                                    query = result;
                                }
                                //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Result in ='%' clause from the query :")).append(query).toString());
                            }
                        } else
                        if(result.toLowerCase().indexOf(",") > -1)
                        {
                            if(result.toLowerCase().indexOf("where") > -1 && result.toLowerCase().indexOf("='%'") > -1 || result.toLowerCase().indexOf("= '%'") > -1)
                            {
                                if(result.toLowerCase().indexOf("= '%'") > -1)
                                    result = result.replaceAll("= '%'", "='%'");
                                int setStartIndex = result.toUpperCase().indexOf("WHERE");
                                int setEndIndex = result.lastIndexOf("='%'");
                                String updatesetString = result.substring(setStartIndex + 5, setEndIndex + 4);
                                query = query.replaceAll(updatesetString, "");
                            } //else
                           // if(result.toLowerCase().indexOf("coalesce( '%'") > -1)
                                //EBWLogger.logDebug("QueryExecutor", "Skip removing coalesce( '%' function");
                            else
                            if(result.toLowerCase().indexOf(")) and") > -1)
                            {
                                if(result.toLowerCase().indexOf("and") > -1 && result.toLowerCase().trim().indexOf("='%'") > -1 || result.toLowerCase().trim().indexOf("= '%'") > -1)
                                {
                                    if(result.toLowerCase().indexOf("= '%'") > -1)
                                        result = result.replaceAll("= '%'", "='%'");
                                    int lastIndex = result.toUpperCase().lastIndexOf("AND");
                                    StringBuffer b = new StringBuffer(result);
                                    if(lastIndex > 0)
                                        b.replace(0, lastIndex - 1, "");
                                    result = b.toString();
                                    query = query.replaceAll(result, "");
                                }
                                //EBWLogger.logDebug("QueryExecutor", "Skip removing from the query");
                            } else
                            {
                                query = query.replaceAll(result, "");
                            }
                        } else
                        if(result.toLowerCase().indexOf("update") > -1 && result.toLowerCase().indexOf("set") > -1 && result.toLowerCase().indexOf("='%'") > -1)
                        {
                            int setStartIndex = result.toUpperCase().indexOf("SET");
                            int setEndIndex = result.lastIndexOf("='%'");
                            String updatesetString = result.substring(setStartIndex + 4, setEndIndex + 4);
                            query = query.replaceAll(updatesetString, "");
                        } else
                        {
                            if(result.toLowerCase().indexOf("and") > -1 && result.toLowerCase().trim().indexOf("='%'") > -1)
                            {
                                int lastIndex = result.toUpperCase().lastIndexOf("AND");
                                StringBuffer b = new StringBuffer(result);
                                if(lastIndex > 0)
                                    b.replace(0, lastIndex - 1, "");
                                result = b.toString();
                                query = query.replaceAll(result, "");
                            }
                            //EBWLogger.logDebug("QueryExecutor", "INSIDE removing ='%' from else Clause");
                        }
                    }*/

                  /*  for(int queryVal = 0; queryVal < countINQuery; queryVal++)
                    {
                        //EBWLogger.logDebug("QueryExecutor", "INSIDE removing IN ('%') Clause");
                        StringBuffer b;
                        for(; query != null && query.indexOf("IN ('%')") > -1; query = b.toString())
                        {
                            b = new StringBuffer(query);
                            int INindex = query.toUpperCase().indexOf("IN ('%')");
                            b.replace(INindex + 2, INindex + 3, "");
                        }

                        String result = (new StringBuilder(String.valueOf(queryINStrArr[queryVal]))).append("'%'").toString();
                        //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Value of the result in IN ('%') is :")).append(result).toString());
                        for(; result != null && result.indexOf("IN (") > -1; result = b.toString())
                        {
                            b = new StringBuffer(result);
                            int INindex = result.toUpperCase().indexOf("IN (");
                            b.replace(INindex + 2, INindex + 3, "");
                        }

                        if(result.toLowerCase().indexOf("select") > -1)
                        {
                            //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Value of the result is ")).append(result).toString());
                            if(result.toUpperCase().indexOf("IN('%'") > -1)
                            {
                                int StartIndex = result.toUpperCase().indexOf("WHERE");
                                int lastIndex = result.toUpperCase().lastIndexOf("IN('%'");
                                result = result.substring(StartIndex + 5, lastIndex + 6);
                                b = new StringBuffer(query);
                                b.replace(query.indexOf(result), query.indexOf(result) + result.length() + 1, "");
                                query = b.toString();
                                //EBWLogger.logDebug("QueryExecutor", (new StringBuilder("Result in IN('%') clause from the query :")).append(query).toString());
                            }
                        } else
                        if(result.toLowerCase().indexOf(",") > -1)
                        {
                            if(result.toLowerCase().indexOf("where") > -1 && result.toUpperCase().indexOf("IN('%'") > -1 || result.toLowerCase().indexOf("IN ('%')") > -1)
                            {
                                if(result.toLowerCase().indexOf("IN ('%')") > -1)
                                    result = result.replaceAll("IN ", "IN");
                                int setStartIndex = result.toUpperCase().indexOf("WHERE");
                                int setEndIndex = result.lastIndexOf("IN('%')");
                                String updatesetString = result.substring(setStartIndex + 5, setEndIndex + 4);
                                StringBuffer b1 = new StringBuffer(query);
                                b1.replace(query.indexOf(updatesetString), query.indexOf(updatesetString) + updatesetString.length() + 1, "");
                                query = b1.toString();
                            } //else
                            //if(result.toLowerCase().indexOf("coalesce( '%'") > -1)
                                //EBWLogger.logDebug("QueryExecutor", "Skip removing coalesce( '%' function");
                            else
                            if(result.toLowerCase().indexOf(")) and") > -1)
                            {
                                if(result.toLowerCase().indexOf("and") > -1 && result.toUpperCase().indexOf("IN('%'") > -1 || result.toLowerCase().trim().indexOf("IN ('%'") > -1)
                                {
                                    if(result.toLowerCase().indexOf("IN ('%')") > -1)
                                        result = result.replaceAll("IN ", "IN");
                                    int lastIndex = result.toUpperCase().lastIndexOf("AND");
                                    b = new StringBuffer(result);
                                    if(lastIndex > 0)
                                        b.replace(0, lastIndex - 1, "");
                                    result = b.toString();
                                    StringBuffer b1 = new StringBuffer(query);
                                    b1.replace(query.indexOf(result), query.indexOf(result) + result.length() + 1, "");
                                    query = b1.toString();
                                }
                                //EBWLogger.logDebug("QueryExecutor", "Skip removing from the query");
                            } else
                            {
                                StringBuffer b1 = new StringBuffer(query);
                                b1.replace(query.indexOf(result), query.indexOf(result) + result.length() + 1, "");
                                query = b1.toString();
                            }
                        } else
                        if(result.toLowerCase().indexOf("update") > -1 && result.toLowerCase().indexOf("set") > -1 && result.toLowerCase().indexOf("IN('%'") > -1)
                        {
                            int setStartIndex = result.toUpperCase().indexOf("SET");
                            int setEndIndex = result.lastIndexOf("IN('%')");
                            String updatesetString = result.substring(setStartIndex + 4, setEndIndex + 4);
                            StringBuffer b1 = new StringBuffer(query);
                            b1.replace(query.indexOf(updatesetString), query.indexOf(updatesetString) + updatesetString.length() + 1, "");
                            query = b1.toString();
                        } else
                        {
                            if(result.toLowerCase().indexOf("and") > -1 && result.toUpperCase().indexOf("IN('%'") > -1)
                            {
                                int lastIndex = result.toUpperCase().lastIndexOf("AND");
                                b = new StringBuffer(result);
                                if(lastIndex > 0)
                                    b.replace(0, lastIndex - 1, "");
                                result = b.toString();
                                StringBuffer b1 = new StringBuffer(query);
                                b1.replace(query.indexOf(result), query.indexOf(result) + result.length() + 1, "");
                                query = b1.toString();
                            }
                            //EBWLogger.logDebug("QueryExecutor", "INSIDE removing IN('%') from else Clause");
                        }
                    }*/

                    /*if(query.toLowerCase().indexOf("coalesce( '%'") > -1)
                    {
                        int count = 0;
                        for(int i = 0; (i = query.indexOf("coalesce( '%'", i)) >= 0; i++)
                            count++;

                        for(int x = 0; x < count; x++)
                        {
                            StringBuffer b = new StringBuffer(query);
                            int indx = query.indexOf("coalesce( '%'");
                            b.replace(indx, indx + 13, "coalesce(NULL");
                            query = b.toString();
                        }

                    }*/
                   /* if(query.toUpperCase().substring(query.length() - 4).equals("AND "))
                    {
                        StringBuffer b = new StringBuffer(query);
                        b.replace(query.length() - 4, query.length(), "");
                        query = b.toString();
                    }*/
                    /*if(query.toUpperCase().indexOf("WHERE ") > -1 && query.indexOf("='%'") > -1 || query.indexOf("= '%'") > -1)
                    {
                        if(query.toLowerCase().indexOf("= '%'") > -1)
                            query = query.replaceAll("= '%'", "='%'");
                        if(query.toLowerCase().indexOf("coalesce(") > -1)
                        {
                            //EBWLogger.logDebug("QueryExecutor", "Skip removing coalesce( '%' function");
                        } else
                        {
                        	
                            int StartIndex = query.toUpperCase().indexOf("WHERE");
                            int lastIndex = query.lastIndexOf("='%'");
                            StringBuffer b = new StringBuffer(query);
                            b.replace(StartIndex + 6, lastIndex + 4, "");
                            query = b.toString();
                        	
        					
        					if(query.indexOf("'%'")!=-1){
        					String whereToken="";
        					String whereQuery="";
        					query = query.replaceAll(" WHERE ", " where ");
        					query.replaceAll(" AND ", " and ");
        					String whereList[]=query.split(" where");
        					for(int j=0;j<whereList.length;j++){
        						whereToken=whereList[j];
        						if(whereToken.indexOf(" and ")!= -1){
        							
        							String INNERQUERY="";
                					String token="";
                					
                					//query=query.replaceAll(" and ", " AND ");
                					String aList[]=whereToken.split(" and ");
                					
                					for(int i=0;i<aList.length;i++){
                						  token=aList[i];

                						  if(token.indexOf("'%'")!=-1 && token.indexOf("#")==-1 && token.indexOf("'%')")==-1){
                							  token="";
                							  
                						  }else if(token.indexOf("'%')")!=-1){
                							  int tokenindex = token.indexOf("'%')");
                							  token=token.substring(tokenindex+3, token.length());
                							  token = token.trim();
                							  
                							 
                						  }
                						  if(token.indexOf("#")!=-1){
                							  String hashToken;
                							  int HashIdx = token.indexOf("#");                							  
                							  hashToken = token.substring(0,HashIdx);
                							  token = token.substring(HashIdx,token.length());                							  
                							  if(hashToken.indexOf("'%'")!=-1)
                							  hashToken = "";
                							  
                							  token=hashToken+token;
                						  }
                						  
                						  INNERQUERY=INNERQUERY+" and "+token;
                						 
                					  }
                					if(INNERQUERY.startsWith(" and")){
                						INNERQUERY=INNERQUERY.substring(4, INNERQUERY.length());
                					}
                					whereToken=INNERQUERY;
                					
        							
        						}else{
        							if(whereToken.indexOf("'%'")!=-1){
        								whereToken =whereToken.substring(whereToken.indexOf("'%'")+3, whereToken.length());
        							}
        						}
        						whereQuery=whereQuery+" where "+whereToken;
        						whereQuery=whereQuery.trim();
        						
        						 
        						
        						if(whereQuery.startsWith("where")){
            						whereQuery=whereQuery.substring(6, whereQuery.length());
            					}
        				
        					}
        					
        					if(whereQuery != null && whereQuery.indexOf(" and )") != -1)
   							 whereQuery = whereQuery.replaceAll(" and \\)", "\\)");
        					
        					query=whereQuery;
        					query = query.replaceAll(" where ", " WHERE ");
        					query = query.replaceAll(" and ", " AND ");
        					
        					
        					
        				}    
        				
        					
                        }
                    }*/
                    /*if(query.toUpperCase().substring(query.length() - 6).equals("WHERE "))
                    {
                        StringBuffer b1 = new StringBuffer(query);
                        b1.replace(query.length() - 6, query.length(), "");
                        query = b1.toString();
                    }*/
                    
                if(query.indexOf("  ")!=-1){
                	query.replace("  "," ");
                }
                if(query.indexOf("  ")!=-1){
                	query.replace("  "," ");
                }
                if(query.indexOf("  ")!=-1){
                	query.replace("  "," ");
                }
                
               
                if(query.toUpperCase().indexOf("WHERE AND") != -1)
                {
                    query = query.toUpperCase().replaceAll("WHERE AND", "WHERE");
                }
                
            
              while (query.indexOf("AND AND")!=-1){
            	  query = query.replaceAll("AND AND", "AND");
              }
               
            } else
            {
                query = new String(sqlStmt);
            }
        }
        catch(MissingResourceException mre)
        {
            throw new EbwException(this, mre);
        }
        catch(ClassNotFoundException cstEx)
        {
            throw new EbwException(this, cstEx);
        }
        catch(Exception e)
        {
            throw e;
        }
        EBWLogger.trace("QueryExecutor :", "Exiting from getQueryString");
        return query;
    
	}

	/*private String getAuditQuery(String query_audit) {
		// TODO Auto-generated method stub
    	String query="";
    	
    	query=query.trim();
    	if(query.startsWith("insert")||query.startsWith("INSERT")){
    		query.replaceAll("actiOn", "insert");
    	}else if(query.startsWith("update")||query.startsWith("UPDATE")){
    		query.replaceAll("actiOn", "update");
    	}
    	
		return query;
	}*/

/*    private String getVersionNumQuery(String sqlStmt) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
      	 String TABLENAME="";
      	String versionnum="";
      	 boolean VERIFICATION=false;
      	 try{
           	
           	sqlStmt=sqlStmt.trim();
           	
               String INITTABLE1="";
               String INITTABLE2="";
               String CAPTABLE1="";
               String CAPTABLE2="";
               String CAPTABLE3="";
               String CAPTABLE4="";
               
               if((sqlStmt.toUpperCase().indexOf(CAPTABLE1)!=-1  ||sqlStmt.toUpperCase().indexOf(CAPTABLE2)!=-1)&&(sqlStmt.toUpperCase().indexOf(CAPTABLE3)==-1 && sqlStmt.toUpperCase().indexOf(CAPTABLE4)==-1)){
               	EBWLogger.logDebug("QueryExecutor :", "Entry to the versionnum building...");
               	VERIFICATION=true;
              	               	            	 
              	 if(VERIFICATION==true){            	 
              	 sqlStmt = sqlStmt.trim();
   	             if(sqlStmt.toUpperCase().equals("UPDATE")){
   	            	 sqlStmt=sqlStmt.replaceAll("UPDATE","UPDATE");
   	            	sqlStmt=sqlStmt.replaceAll("SET","set");
   	             	int UPDATEINDEX=sqlStmt.indexOf("update");
   	             	int SETINDEX=sqlStmt.indexOf("set");
   	             	TABLENAME=sqlStmt.substring(UPDATEINDEX+6,SETINDEX);
   	             	if(TABLENAME.contains(".")){
   	             		TABLENAME=TABLENAME.substring(TABLENAME.indexOf(".")+1,TABLENAME.length()).trim();
   	             		
   	             		
   	                 if(TABLENAME.equalsIgnoreCase(INITTABLE1)||TABLENAME.equalsIgnoreCase(INITTABLE2)||sqlStmt.indexOf(CAPTABLE1)!=-1 ||sqlStmt.indexOf(CAPTABLE2)!=-1){
   	                 	String set[]=sqlStmt.split("set");
   	                 	sqlStmt=set[0]+" set "+" versionnum=versionnum+1,modifieddate=CURRENT_TIMESTAMP,"+set[1];	
   	                 	
   	                 	if(sqlStmt.contains("where")){
   	                 	set=sqlStmt.split("where");
   	                 	sqlStmt=set[0]+" where "+" versionnum=#versionnum:numeric# and "+set[1];
   	                 	}
   	                 }
   	             	}else{
   	             		if(TABLENAME.equalsIgnoreCase(INITTABLE1)||TABLENAME.equalsIgnoreCase(INITTABLE2)||sqlStmt.indexOf(CAPTABLE1)!=-1 ||sqlStmt.indexOf(CAPTABLE2)!=-1){
   	                     	String set[]=sqlStmt.split("set");
   	                     	sqlStmt=set[0]+" set "+" versionnum=versionnum+1,modifieddate=CURRENT_TIMESTAMP,"+set[1];	
   	                     	
   	                     	if(sqlStmt.contains("where")){
   	                     	set=sqlStmt.split("where");
   	                     	sqlStmt=set[0]+" where "+" versionnum=#versionnum:numeric# and "+set[1];
   	                     	}
   	                     }
   	             	}
   	             }else if(sqlStmt.startsWith("insert")||sqlStmt.startsWith("INSERT")||sqlStmt.startsWith("Insert")){
   	             	int INSERTINDEX=sqlStmt.indexOf(" into ");
   	             	int BRACEINDEX=sqlStmt.indexOf("(");
   	             	TABLENAME=sqlStmt.substring(INSERTINDEX+6,BRACEINDEX).trim();
   	             	
   	             	if(TABLENAME.contains(".")){
   	             		TABLENAME=TABLENAME.substring(TABLENAME.indexOf(".")+1,TABLENAME.length()).trim();
   	             	if(TABLENAME.equalsIgnoreCase(INITTABLE1)||TABLENAME.equalsIgnoreCase(INITTABLE2)||TABLENAME.equalsIgnoreCase(CAPTABLE1)||TABLENAME.equalsIgnoreCase(CAPTABLE2)){
   	                 	
   	                 	int startindex=sqlStmt.indexOf("(");
   	                 	String tempstartquery=sqlStmt.substring(0, startindex);
   	                 	String tempendquery=sqlStmt.substring(startindex,sqlStmt.length());
   	                 	                        	
   	                 	tempendquery="(versionnum,modifieddate,"+tempendquery.substring(1,tempendquery.length());
   	                 	
   	                 	sqlStmt=tempstartquery+tempendquery;
   	                 	int tempvaluesindex=sqlStmt.indexOf("values");
   	                 	String tempmidstartquery=sqlStmt.substring(0,tempvaluesindex);
   	                 	String tempmidquery=sqlStmt.substring(tempvaluesindex,sqlStmt.length());
   	                 	startindex=tempmidquery.indexOf("(");
   	                 	String subtempstartquery=tempmidquery.substring(0,startindex);
   	                 	String subtempendquery=tempmidquery.substring(startindex,tempmidquery.length());
   	                 	subtempendquery="(1,CURRENT_TIMESTAMP,"+subtempendquery.substring(1,subtempendquery.length());
   	                 	
   	                 	sqlStmt=tempmidstartquery+subtempstartquery+subtempendquery;
   	                 	 //EBWLogger.logDebug("QueryExecutor :","sqlstmt for payments is"+sqlStmt);
   	                 }
   	             }else{
   	             	if(TABLENAME.equalsIgnoreCase(INITTABLE1)||TABLENAME.equalsIgnoreCase(INITTABLE2)||TABLENAME.equalsIgnoreCase(CAPTABLE1)||TABLENAME.equalsIgnoreCase(CAPTABLE2)){
   	                 	
   	                 	int startindex=sqlStmt.indexOf("(");
   	                 	String tempstartquery=sqlStmt.substring(0, startindex);
   	                 	String tempendquery=sqlStmt.substring(startindex,sqlStmt.length());
   	                 	                        	
   	                 	tempendquery="(versionnum,modifieddate,"+tempendquery.substring(1,tempendquery.length());
   	                 	
   	                 	sqlStmt=tempstartquery+tempendquery;
   	                 	int tempvaluesindex=sqlStmt.indexOf("values");
   	                 	String tempmidstartquery=sqlStmt.substring(0,tempvaluesindex);
   	                 	String tempmidquery=sqlStmt.substring(tempvaluesindex,sqlStmt.length());
   	                 	startindex=tempmidquery.indexOf("(");
   	                 	String subtempstartquery=tempmidquery.substring(0,startindex);
   	                 	String subtempendquery=tempmidquery.substring(startindex,tempmidquery.length());
   	                 	subtempendquery="(1,CURRENT_TIMESTAMP,"+subtempendquery.substring(1,subtempendquery.length());
   	                 	
   	                 	sqlStmt=tempmidstartquery+subtempstartquery+subtempendquery;
   	                 	 //EBWLogger.logDebug("QueryExecutor :","sqlstmt for payments is"+sqlStmt);
   	                 }
   	             }
   	             }else if(sqlStmt.toUpperCase().startsWith("SELECT")){
   	            	EBWLogger.logDebug("QueryExecutor :", "inside versionnum selection");
   	            	
   	            	 String FROMQUERY="";
   	            	 String fromList[]=sqlStmt.split(" from ");
   	            	EBWLogger.logDebug("QueryExecutor :", "from size"+fromList.length);
   	            	if(fromList.length <=2){
   	            		if(sqlStmt.contains("COUNT(*)")||sqlStmt.contains("count(*)")){
   	            			FROMQUERY=sqlStmt;
   	            		}else{
   	            			if((sqlStmt.toUpperCase().contains(CAPTABLE1))&&(sqlStmt.toUpperCase().contains(CAPTABLE2))){
  	            		 FROMQUERY=sqlStmt;
   	            				versionnum = PropertyFileReader.getProperty("versionnum");
   	            				sqlStmt="select "+versionnum+".versionnum,"+FROMQUERY.substring(7,FROMQUERY.length());
   	            			}else{
  	            		 FROMQUERY=sqlStmt;
  	            		FROMQUERY=FROMQUERY.trim();
  	            		sqlStmt="select "+"versionnum,"+FROMQUERY.substring(7,FROMQUERY.length());
   	            			}
   	            		}
  	            	 }else{
   	            	 for(int i=0;i<fromList.length;i++){
   	            		 String FROMTOKEN=fromList[i];
   	            		 
   	            		//EBWLogger.logDebug("QueryExecutor :", "inside i selection"+i+" is "+fromList[i]);
   	            		 if(FROMTOKEN.contains(INITTABLE1)||FROMTOKEN.contains(INITTABLE2)||FROMTOKEN.contains(CAPTABLE1)||FROMTOKEN.contains(CAPTABLE2)){
   	            			//EBWLogger.logDebug("QueryExecutor :", "inside versionnum selection"+fromList[i]);
   	            			 versionnum = PropertyFileReader.getProperty("versionnum");
   	            			 FROMQUERY=FROMQUERY+","+versionnum+".versionnum as versionnum";
   	            		 }
   	            		 FROMQUERY=FROMQUERY+" from "+FROMTOKEN;
   	            		 FROMQUERY=FROMQUERY.trim();
   	            		EBWLogger.logDebug("QueryExecutor :", "versionnum query is "+FROMQUERY);
   	            		 }
   	            	 if(FROMQUERY.endsWith("from")){
   	            		 FROMQUERY=FROMQUERY.substring(0,FROMQUERY.length()-4);
   	            	 }
   	            	if(FROMQUERY.startsWith("from")){
  	            		 FROMQUERY=FROMQUERY.substring(4,FROMQUERY.length());
  	            	 }
   	            	 FROMQUERY=FROMQUERY.trim();
   	            	sqlStmt="select "+"versionnum,"+FROMQUERY.substring(7,FROMQUERY.length());
  	            	 }
              	 
               }else if(sqlStmt.startsWith("delete")){
              	 if(sqlStmt.contains("where")){
                    	String[] set=sqlStmt.split("where");
                    	sqlStmt=set[0]+" where "+" versionnum=#versionnum:numeric# and "+set[1];
                    	}else{
                    		sqlStmt=sqlStmt+" where "+" versionnum=#versionnum:numeric#";
                    	}
               }
               }
               }else{
               	EBWLogger.logDebug("QueryExecutor :", "Else condition with out versionnum building :-" + sqlStmt);
               }
           }catch(Exception e){
           	EBWLogger.logDebug("QueryExecutor :", "Exception during the versionnum building :-" + sqlStmt);
           	e.printStackTrace();
           }
           
   		return sqlStmt;
   	
	}*/

	private Object executeBatch(String stmtName[], Object obj[])
        throws Exception
    {
        Statement stmt;
        PreparedStatement preStmt;
        PreparedStatement preStmtAudit;
        String sqlStmt_Audit;
        String Audit_Query;
        Object result[];
        //EBWLogger.trace("QueryExecutor :", "Enter Into executeBatch(String stmtName[], Object obj[]) ");
        printTOArrayValues(obj, " Object is :");
        printStringArrayValues(stmtName, "Statement is : ");
        stmt = null;
        preStmt = null;
        preStmtAudit = null;
        sqlStmt_Audit = null;
        Audit_Query = "";
        String query = null;
        String query_audit = null;
        result = new Object[obj.length];
        int stmtResult[];
        int preStmtResult[];
        try{
        for(int i = 0; i < stmtName.length; i++)
        {
            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Executing Statement ")).append(stmtName[i]).append(" in batch").toString());
            String sqlStmt = "";
            init(stmtName[i]);
           // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("IsExternalStatement is :")).append(ExternalisedStatement).toString());
            try
            {
                sqlStmt = getQueryValue((new StringBuilder(String.valueOf(stmtName[i]))).append(".Query").toString(), true);
                if(PropertyFileReader.getProperty("audit_logging").equalsIgnoreCase("true"))
                    sqlStmt_Audit = getQueryValue((new StringBuilder(String.valueOf(stmtName[i]))).append(".Query").append("_Audit").toString(), true);
                if(sqlStmt_Audit != null)
                    sqlStmt_Audit = getQueryValue((new StringBuilder()).append(stmtName).append(".Query").append("_Audit").toString(), false).toString().trim();
            }
            catch(MissingResourceException mre)
            {
                throw new EbwException(this, mre);
            }
           
            if(obj[i] instanceof EBWTransferObject)
            {
                EBWTransferObject objEBWTransferObject = (EBWTransferObject)obj[i];
               // EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("TO Class name before fillTO :")).append(objEBWTransferObject.getClass().getName()).toString());
                query = getQueryString(sqlStmt, objEBWTransferObject);
                if(sqlStmt_Audit != null)
                {
                   query_audit = getQueryString(sqlStmt_Audit, objEBWTransferObject);
                    Audit_Query = query_audit.toUpperCase();
                }
            } else
            {
                query = getQueryString(sqlStmt, ((Object) (obj)));
                /*if(sqlStmt_Audit != null)
                {
                    query_audit = getQueryString(sqlStmt_Audit, ((Object) (obj)));
                    Audit_Query = query_audit.toUpperCase();
                }*/
            }
            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("QueryExecutor Batch statement : ")).append(stmtName[i]).append(":").append(strStmtType).append(":").append(query).toString());
            if(strStmtType.equalsIgnoreCase(STMT_ONLY))
            {
                if(stmt == null)
                    stmt = con.createStatement();
                stmt.addBatch(query);
                if(sqlStmt_Audit != null)
                    stmt.addBatch(Audit_Query);
            } else
            if(strStmtType.equalsIgnoreCase(STMT_PREP))
            {
                preStmt = con.prepareStatement(query);
                SetPreparedStmtValues(parameterCount, preStmt);
            }
            EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query to be executed in executeUpdate Pre ")).append(query).toString());
        }

        stmtResult = (int[])null;
        preStmtResult = (int[])null;
        if(stmt != null)
        {
            stmtResult = stmt.executeBatch();
            for(int k = 0; k < stmtResult.length; k++)
                EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Number of rows affected in Statement-")).append(k).append(" of Batch is: ").append(stmtResult[k]).toString());

            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("PropertyFileReader.getProperty('audit_logging'): ")).append(PropertyFileReader.getProperty("audit_logging")).toString());
            if(PropertyFileReader.getProperty("audit_logging").equalsIgnoreCase("true"))
            {
                EBWLogger.logDebug("QueryExecutor :", "Prepared Statement -  Into Auditing");
                if(Audit_Query.startsWith("INSERT"))
                {
                    Audit_Query = Audit_Query.replaceAll("EB_RDC", "EB_RDC_AUD");
                    preStmtAudit = con.prepareStatement(Audit_Query);
                    SetPreparedStmtValues(parameterCount, preStmt);
                }
                if(preStmtAudit != null)
                {
                    int preStmtAuditResult[] = preStmtAudit.executeBatch();
                   // for(int k = 0; k < preStmtAuditResult.length; k++)
                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Number of rows affected in PreparedStatement-")).append(k).append(" of Batch is: ").append(preStmtAuditResult[k]).toString());

                }
            }
            closeStatement(stmt, null);
        }
        if(preStmt != null)
        {
            preStmtResult = preStmt.executeBatch();
            for(int k = 0; k < preStmtResult.length; k++)
                //EBWLogger.trace("QueryExecutor :", (new StringBuilder("Number of rows affected in PreparedStatement-")).append(k).append(" of Batch is: ").append(preStmtResult[k]).toString());

            closePreparedStatement(preStmt, null);
        }
        //EBWLogger.trace("QueryExecutor :", "Exiting from executeBatch(String stmtName[], Object obj[]) ");
        if(obj[0] instanceof EBWTransferObject){
        	EBWLogger.logDebug("QueryExecutor :", "Result ((Object) (result)) from ExecuteBatch String (stmtName, Object obj[]) is:"+((Object) (result)));
        		return ((Object) (result));
            } 
            else if(stmtResult!=null){
            	EBWLogger.logDebug("QueryExecutor :", "Result stmtResult from ExecuteBatch String(stmtName, Object obj[]) is:"+((Object) (result)));
            		return stmtResult;
	        }else {
	        	EBWLogger.logDebug("QueryExecutor :", "Result stmtResult from ExecuteBatch String(stmtName, Object obj[]) is:"+((Object) (result)));
	    		return preStmtResult;
	        } 
    }
    catch(MissingResourceException mre)
    {      mre.printStackTrace();      
            throw new EbwException(this, mre);
    }    
    catch(SQLException sqlEx)
    {       sqlEx.printStackTrace();
            throw new EbwException(this, sqlEx);                      
    }catch(Exception ebwEx)
    {
        ebwEx.printStackTrace();
        throw ebwEx;
    }
    
}


    private Object executeBatch(String stmtName, Object obj[])
        throws Exception
    {
        //EBWLogger.trace("QueryExecutor :", (new StringBuilder("Starting method executeBatch with Statement is:")).append(stmtName).append(",and obj[] length is :").append(obj.length).append(") ").toString());
        printTOArrayValues(obj, "Object is :");
        Statement stmt = null;
        PreparedStatement preStmt = null;
        String query = null;
        init(stmtName);
        Object result[] = new Object[obj.length];
        String sqlStmt = "";
        String query_audit = null;
        String sqlStmt_Audit = null;
        String Audit_Query = "";
        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("IsExternalStatement is :")).append(ExternalisedStatement).toString());
        sqlStmt = getQueryValue((new StringBuilder(String.valueOf(stmtName))).append(".Query").toString(), true);
        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Param obj length is ")).append(obj.length).toString());
        if(PropertyFileReader.getProperty("audit_logging").equalsIgnoreCase("true"))
            sqlStmt_Audit = getQueryValue((new StringBuilder(String.valueOf(stmtName))).append(".Query").append("_Audit").toString(), true);
        for(int i = 0; i < obj.length; i++)
        {
            if(obj[i] instanceof EBWTransferObject)
            {
                EBWTransferObject objEBWTransferObject = (EBWTransferObject)obj[i];
                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("TO Class name before fillTO :")).append(objEBWTransferObject.getClass().getName()).toString());
                query = getQueryString(sqlStmt, objEBWTransferObject);
               /* if(sqlStmt_Audit != null)
                {
                    query_audit = getQueryString(sqlStmt_Audit, objEBWTransferObject);
                    query_audit = getAuditQuery(query_audit);
					Audit_Query = query_audit.toUpperCase();
                }*/
            } else
            if(obj[i] instanceof HashMap)
            {
                HashMap objHashMap = (HashMap)obj[i];
                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("HashMap content before getting query :")).append(objHashMap).toString());
                query = getQueryString(sqlStmt, objHashMap);
                /*if(sqlStmt_Audit != null)
                {
                    query_audit = getQueryString(sqlStmt_Audit, objHashMap);
                    Audit_Query = query_audit.toUpperCase();
                }*/
            } else
            {
                query = getQueryString(sqlStmt, ((Object) (obj)));
               /* if(sqlStmt_Audit != null)
                {
                    query_audit = getQueryString(sqlStmt_Audit, ((Object) (obj)));
                    Audit_Query = query_audit.toUpperCase();
                }*/
            }
            if(strStmtType.equalsIgnoreCase(STMT_ONLY))
            {
                if(stmt == null)
                    stmt = con.createStatement();
                //EBWLogger.trace(this, (new StringBuilder("Query added in Batchn is :")).append(query).toString());
            } else
            if(strStmtType.equalsIgnoreCase(STMT_PREP))
            {
                //EBWLogger.trace(this, "Statement type in Batch execution is  PreparedStatement");
                //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("parameter Count is ")).append(parameterCount).toString());
                preStmt = con.prepareStatement(query);
                SetPreparedStmtValues(parameterCount, preStmt);
            }
            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query to be executed in executeUpdate Pre ")).append(query).toString());
            if(strStmtType.equalsIgnoreCase(STMT_ONLY))
            {
                stmt.addBatch(query);
                EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query executed successfully in addBatch Pre for the Query :")).append(query).toString());
                if(sqlStmt_Audit != null)
                {
                    stmt.addBatch(Audit_Query);
                    EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query executed successfully in addBatch Pre for the Audit_Query :")).append(Audit_Query).toString());
                }
            } else
            if(strStmtType.equalsIgnoreCase(STMT_PREP))
            {
                if(preStmt == null)
                    EBWLogger.logDebug("QueryExecutor :", "Prepared Statement is null in executeUpdate()");
                else
                    preStmt.addBatch();
                EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Query executed successfully in addBatch Pre for the Query using PreapredStatement : ")).append(query).toString());
            }
        }

        for(int i = 0; i < obj.length - 1; i++)
            if(preStmt != null)
                preStmt.addBatch();

        int stmtResult[] = (int[])null;
        int preStmtResult[] = (int[])null;
        int stmtAuditResult[] = (int[])null;
        if(stmt != null)
        {
            stmtResult = stmt.executeBatch();
            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Statement preStmtResult is :")).append(stmtResult.length).toString());
            if(PropertyFileReader.getProperty("audit_logging").equalsIgnoreCase("true"))
            {
                //EBWLogger.logDebug("QueryExecutor :", "Statement -  Into Auditing");
                if(Audit_Query.startsWith("INSERT"))
                {
                    stmtAuditResult = stmt.executeBatch();
                    EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Statement stmtAuditResult is :")).append(stmtAuditResult.length).toString());
                }
            }
            for(int k = 0; k < stmtResult.length; k++)
                if(strStmtType.equalsIgnoreCase(STMT_ONLY))
                    EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Number of rows affected in Statement-")).append(k).append(" of Batch is: ").append(stmtResult[k]).toString());

            closeStatement(stmt, null);
        } else
        if(preStmt != null)
        {
            preStmtResult = preStmt.executeBatch();
            EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("PreparedStatement preStmtResult is :")).append(preStmtResult.length).toString());
            for(int k = 0; k < preStmtResult.length; k++)
                if(strStmtType.equalsIgnoreCase(STMT_PREP))
                    EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Number of rows affected in PreparedStatement-")).append(k).append(" of Batch is: ").append(preStmtResult[k]).toString());

            closePreparedStatement(preStmt, null);
        }
        //EBWLogger.trace("QueryExecutor :", "Exiting from executeBatch(String stmtName, Object obj[]) ");
        if(obj[0] instanceof EBWTransferObject)
        {
            EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Result ((Object) (result)) from ExecuteBatch String (stmtName, Object obj[]) is:")).append(((Object) (result))).toString());
            return ((Object) (result));
        }
        if(stmtResult != null)
        {
            EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Result stmtResult from ExecuteBatch String(stmtName, Object obj[]) is:")).append(((Object) (result))).toString());
            return stmtResult;
        } else
        {
            EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Result stmtResult from ExecuteBatch String(stmtName, Object obj[]) is:")).append(((Object) (result))).toString());
            return preStmtResult;
        }
    }

    private void executeUpdate(String stmtName, Object obj, boolean isBatch)
        throws Exception
    {
        //EBWLogger.trace("QueryExecutor :", (new StringBuilder("Starting method executeUpdate with Statement :")).append(stmtName).append(",").append(" Object is").append(obj).append(")").toString());
        String sqlStmt = "";
        try
        {
            sqlStmt = getQueryValue((new StringBuilder(String.valueOf(stmtName))).append(".Query").toString(), true);
            init(stmtName);
            String query = null;
            /*if(obj instanceof EBWTransferObject)
            {
                EBWTransferObject objEBWTransferObject = (EBWTransferObject)obj;
                query = getQueryString(sqlStmt, objEBWTransferObject);
            } else
            {
                query = getQueryString(sqlStmt, obj);
            }*/
            query = getQueryString(sqlStmt, obj);
            Statement stmt = null;
            PreparedStatement preStmt = null;
            if(strStmtType.equalsIgnoreCase(STMT_ONLY))
            {
                stmt = con.createStatement();
                stmt.executeUpdate(query);
            } else
            if(strStmtType.equalsIgnoreCase(STMT_PREP))
            {
                preStmt = con.prepareStatement(query);
                SetPreparedStmtValues(parameterCount, preStmt);
                preStmt.executeUpdate();
            }
            closeStatement(stmt, null);
        }
        catch(SQLException sqlEx)
        {
            throw new EbwException(this, sqlEx);
        }
        catch(Exception ebwEx)
        {
            ebwEx.printStackTrace();
            throw ebwEx;
        }
        //EBWLogger.trace("QueryExecutor :", "Exiting from executeUpdate(String stmtName, Object obj, boolean isBatch) ");
    }

    private String[] getColumnMapping(String mappingKey)
        throws EbwException
    {
        //EBWLogger.trace("QueryExecutor", (new StringBuilder("Starting method getColumnMapping( for key ")).append(mappingKey).append(")").toString());
        String mappings = null;
        mappings = getQueryValue(mappingKey, false);
        String mapString = mappings.substring(mappings.indexOf("{") + 1, mappings.indexOf("}"));
        String mapping[] = mapString.trim().split(",");
        //EBWLogger.trace("QueryExecutor", (new StringBuilder("Exiting from getColumnMapping with ")).append(mapping).toString());
        return mapping;
    }

    private Object getOutputObject(String stmtName, ResultSet resultSet, String key1)
        throws Exception
    {
        //EBWLogger.trace("QueryExecutor", (new StringBuilder("Starting method getOutputObject( statement -- ")).append(stmtName).append(", resultset -- ").append(resultSet).append(",Key is -").append(key1).append(")").toString());
        Object opObj = null;
        try
        {
            String outputType = null;
            if(ExternalisedStatement.equalsIgnoreCase("true"))
                outputType = queryRB.getString((new StringBuilder(String.valueOf(stmtName))).append(".OutputType").toString());
            else
                outputType = linkedHash.get(key1).toString().trim();
            EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Output type of statement ")).append(stmtName).append(".OutputType").append(" is ").append(outputType).toString());
            int rowcount = 0;
            if(outputType.indexOf("transferobject") > -1)
            {
                String mapping[] = getColumnMapping((new StringBuilder(String.valueOf(stmtName))).append(".TOColumnMap").toString());
                Class opClass = null;
                Class paramType[] = (Class[])null;
                Object transferObj = null;
                Object transObject = null;
                Vector vectTransferObj = null;
                int index = -1;
                while(resultSet.next()) 
                {
                    Object colValue = null;
                    index++;
                    for(int j = 0; j < mapping.length; j++)
                    {
                        //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Value of mappings in getOutputObject are :")).append(mapping[j].trim()).toString());
                        String colMapping[] = mapping[j].trim().split("=");
                        Object paramValue[] = (Object[])null;
                        String methodName = (new StringBuilder("set")).append((new String(new char[] {
                            colMapping[1].trim().charAt(0)
                        })).toUpperCase()).append(new String(colMapping[1].trim().substring(1))).toString();
                        if(outputType.indexOf("[]") == -1)
                        {
                            //EBWLogger.logDebug("QueryExecutor :", "Outputtype is TransferObject");
                            if(opClass == null)
                            {
                                opClass = Class.forName(outputType.trim());
                                transferObj = opClass.newInstance();
                            }
                            Field field = opClass.getDeclaredField(colMapping[1].trim());
                            paramType = (new Class[] {
                                field.getType()
                            });
                            ResultSetMetaData rsMetaData = resultSet.getMetaData();
                            //EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("colMapping[0].trim():")).append(colMapping[1].trim()).toString());
                            colValue = resultSet.getObject(colMapping[1].trim());
                            if(colValue != null)
                            {
                                //EBWLogger.logDebug("QueryExecutor :", colValue.getClass().getName());
                                String columnType = colValue.getClass().getName();
                                if(columnType.equalsIgnoreCase("oracle.sql.BLOB"))
                                    colValue = getBLOB(resultSet, colMapping[1].trim());
                                else
                                if(columnType.equalsIgnoreCase("oracle.sql.CLOB"))
                                {
                                    String line = "";
                                    java.io.InputStream is = resultSet.getBinaryStream(colMapping[1].trim());
                                    BufferedReader in = new BufferedReader(new InputStreamReader(is));
                                    StringBuffer buffer = new StringBuffer();
                                    while((line = in.readLine()) != null) 
                                        buffer.append(line);
                                    colValue = buffer.toString();
                                } else
                                if(colValue.getClass().getName().equals("java.math.BigDecimal"))
                                    colValue = (BigDecimal)colValue;
                                else
                                if(colValue.getClass().getName().equals("java.math.BigInteger"))
                                    colValue = new Double(((BigInteger)colValue).doubleValue());
                                else
                                if(colValue.getClass().getName().equals("java.lang.Integer"))
                                    colValue = new Double(((Integer)colValue).doubleValue());
                                else
                                    if(colValue.getClass().getName().equals("java.lang.Long"))
                                        colValue = new Double(((Long)colValue).doubleValue());
                            }
                            paramValue = (new Object[] {
                                colValue
                            });
                            Method method = opClass.getMethod(methodName, paramType);
                            method.invoke(transferObj, paramValue);
                        } else
                        {
                            EBWLogger.logDebug("QueryExecutor :", "Output Type is TransferObjectArray");
                            if(opClass == null)
                            {
                                opClass = Class.forName(outputType.trim().substring(0, outputType.indexOf("[")));
                                vectTransferObj = new Vector();
                            }
                            if(transObject == null)
                                transObject = opClass.newInstance();
                            Field field = opClass.getDeclaredField(colMapping[1].trim());
                            paramType = (new Class[] {
                                field.getType()
                            });
                            Method method = opClass.getMethod(methodName, paramType);
                            colValue = resultSet.getObject(colMapping[1].trim());
                            EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Column Value is ")).append(colValue).toString());
                            if(colValue != null)
                            {
                                String columnType = colValue.getClass().getName();
                                if(columnType.equalsIgnoreCase("oracle.sql.BLOB"))
                                    colValue = getBLOB(resultSet, colMapping[1].trim());
                                else
                                if(columnType.equalsIgnoreCase("oracle.sql.CLOB"))
                                {
                                    String line = "";
                                    java.io.InputStream is = resultSet.getBinaryStream(colMapping[1].trim());
                                    BufferedReader in = new BufferedReader(new InputStreamReader(is));
                                    StringBuffer buffer = new StringBuffer();
                                    while((line = in.readLine()) != null) 
                                        buffer.append(line);
                                    colValue = buffer.toString();
                                } else
                                if((colValue instanceof TIMESTAMP) || (colValue instanceof java.sql.Date) || (colValue instanceof DATE))
                                {
                                    Timestamp ts = resultSet.getTimestamp(colMapping[0].trim());
                                    colValue = ts;
                                } else
                                if(colValue.getClass().getName().equals("java.math.BigDecimal"))
                                    colValue = (BigDecimal)colValue;
                                else
                                if(colValue.getClass().getName().equals("java.math.BigInteger"))
                                    colValue = new Double(((BigInteger)colValue).doubleValue());
                                else
                                if(colValue.getClass().getName().equals("java.lang.Integer"))
                                    colValue = new Double(((Integer)colValue).doubleValue());
                                else
                                    if(colValue.getClass().getName().equals("java.lang.Long"))
                                        colValue = new Double(((Long)colValue).doubleValue());
                            }
                            paramValue = (new Object[] {
                                colValue
                            });
                            method.invoke(transObject, paramValue);
                        }
                    }

                    if(transObject != null)
                        vectTransferObj.add(transObject);
                    transObject = null;
                }
                if(vectTransferObj != null)
                {
                    transferObj = Array.newInstance(opClass, index + 1);
                    for(int indx = 0; indx < index + 1; indx++)
                        Array.set(transferObj, indx, vectTransferObj.get(indx));

                }
                opObj = transferObj;
            } else
            if(outputType.indexOf("Object") > -1)
            {
                String mapping[] = getColumnMapping((new StringBuilder(String.valueOf(stmtName))).append(".ArrayColumnMap").toString());
                String colValue = null;
                Object data[] = new Object[rowcount];
                int index = -1;
                while(resultSet.next()) 
                {
                    index++;
                    for(int j = 0; j < mapping.length; j++)
                    {
                        String colMapping[] = mapping[j].trim().split("=");
                        if(colValue == null)
                            colValue = resultSet.getString(colMapping[0].trim());
                        else
                            colValue = (new StringBuilder(String.valueOf(colValue))).append(",").append(resultSet.getString(colMapping[0].trim())).toString();
                    }

                    data[index] = colValue;
                    colValue = null;
                }
                opObj = ((Object) (data));
            } else
            if(outputType.indexOf("util") > -1)
            {
                //EBWLogger.logDebug("QueryExecutor :", "Into java.util Outputtype condition handler");
                Class collectionCls = Class.forName(outputType.trim());
                Object collectionObj = null;
                if(outputType.indexOf("ArrayList") > -1)
                {
		  
					EBWLogger.logDebug("QueryExecutor :","Into ArrayList outputtype handler");
					collectionObj = (ArrayList) collectionCls.newInstance();
					String mapping[] = getColumnMapping(stmtName+ TO_COL_MAPPING);
					ArrayList data = null;
					ResultSetMetaData rsMetaData = resultSet.getMetaData();
					int colCount = rsMetaData.getColumnCount();
					for (int index = 1; index <= colCount; index++) {
						if (data == null)
							data = new ArrayList();
						data.add(rsMetaData.getColumnName(index));
					}
					((ArrayList) collectionObj).add(data);
					for (data = null; resultSet.next(); data = null) {
						if (data == null)
							data = new ArrayList();
						for (int j = 0; j < mapping.length; j++) {
							String colMapping[] = mapping[j].trim().split(EQUALTO);
							//EBWLogger.logDebug("QueryExecutor :", "Getting value of Column "+ colMapping[0].trim() + " from REsultset");
							if (resultSet.getObject(colMapping[0].trim()) == null) {
								data.add(resultSet.getObject(colMapping[0].trim()));
							} else {
								Object rsData = resultSet.getObject(colMapping[0].trim());	
								
								//Conditions for CLOB data Start
								if (rsData instanceof java.sql.Clob){
									String line="";;
									InputStream is = resultSet.getBinaryStream(colMapping[0].trim());        
									BufferedReader in = new BufferedReader(new InputStreamReader(is));            	
									StringBuffer buffer = new StringBuffer();
					            	  while ((line = in.readLine()) != null) {
					            	    buffer.append(line);
					            	  }						            	 
					            	data.add(ConvertionUtil.convertToString(buffer.toString()));
								}//End of Clob
								if (rsData.getClass().getName().equals(BIGDECIMAL)){
									rsData =(BigDecimal) rsData;								
								}
								else if (rsData.getClass().getName().equals(BIGINTEGER)){
									rsData = new Double(((BigInteger) rsData).doubleValue());
								}
								//Checking for TimeStamp
								EBWLogger.logDebug("QueryExecutor :","rsData type is"+rsData.getClass().getName());
								try{
									if(rsData instanceof java.lang.String){
										data.add(ConvertionUtil.convertToString(rsData));
									}else if(rsData instanceof  java.lang.Double){
	                                	data.add(ConvertionUtil.convertToString(rsData));
									}else if(rsData instanceof java.lang.Long){
										data.add(ConvertionUtil.convertToString(rsData));
									}else if(rsData instanceof java.lang.Integer){
										//data.add( new Double(((Integer)rsData).doubleValue()));
										//data.add(rsData);
										data.add(ConvertionUtil.convertToString(rsData));
									}else if(rsData.getClass().getName().equals("java.math.BigDecimal")){
	                                    rsData = (BigDecimal)rsData;
	                                    data.add(rsData.toString());
	                                }else if(rsData.getClass().getName().equals("java.math.BigInteger")){
	                                    rsData = new Double(((BigInteger)rsData).doubleValue());
	                                    data.add(rsData.toString());
	                                }else if( rsData instanceof java.sql.Date ){
										//java.sql.Timestamp ts=resultSet.getTimestamp(colMapping[0].trim());
	                                	java.sql.Date ts=resultSet.getDate(colMapping[0].trim());
										data.add(ConvertionUtil.convertToString(ts));
									}else if(rsData instanceof java.sql.Timestamp){
										java.sql.Timestamp ts=resultSet.getTimestamp(colMapping[0].trim());
										//data.add(ts.toString());										
										data.add(ConvertionUtil.convertToString(ts));
									}else if (rsData instanceof java.sql.Clob){
										EBWLogger.logDebug("QueryExecutor :","Checking for Clob Data");
										data.add(rsData);	
									}else if (rsData instanceof java.sql.Blob){
										EBWLogger.logDebug("QueryExecutor :","Checking for Blob Data");
										data.add(rsData);	
									}else{									
											data.add(ConvertionUtil.convertToString(rsData));
										}
							}catch(Exception e){
								EBWLogger.logDebug("QueryExecutor :","Error is:"+e);
								e.printStackTrace();
							}finally{
								EBWLogger.logDebug("QueryExecutor :","rsData details");
							}
								
							}
							if (resultSet.getObject(colMapping[0].trim()) != null)
								if (data.get(data.size() - 1) instanceof java.sql.Timestamp)
									EBWLogger.logDebug("QueryExecutor :","*** Value is data"+ ((java.sql.Timestamp) data.get(data.size() - 1))+ " DataType :"+ resultSet.getObject(colMapping[0].trim()).getClass().getName());
						}
						((ArrayList) collectionObj).add(data);
					}
				
                    } else
                if(outputType.indexOf("Vector") > -1)
                {
                    EBWLogger.logDebug("QueryExecutor :", "Into Vector output type Handler");
                    collectionObj = (Vector)(Vector)collectionCls.newInstance();
                    Collection rows = new Vector();
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    int cols = rsmd.getColumnCount();
                    EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Number of columns in output: ")).append(cols).toString());
                    EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Output result is Null ")).append(resultSet == null).toString());
                    for(; resultSet.next(); ((Vector)collectionObj).add(rows))
                    {
                        rows = new Vector();
                        for(int i = 0; i < cols; i++)
                            rows.add(resultSet.getString(i + 1));

                    }

                } else
                if(outputType.indexOf("HashMap") > -1)
                {
                    collectionObj = (HashMap)(HashMap)collectionCls.newInstance();
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    int cols = rsmd.getColumnCount();
                    if(outputType.indexOf("Linked") == -1)
                        collectionObj = new HashMap();
                    else
                        collectionObj = new LinkedHashMap();
                    while(resultSet.next()) 
                    {
                        String key = resultSet.getString(1);
                        String value = resultSet.getString(2);
                        if(((Map)collectionObj).containsKey(key))
                        {
                            value = (new StringBuilder(String.valueOf(value))).append("~").append((String)((HashMap)collectionObj).get(key)).toString();
                            ((Map)collectionObj).put(key, value);
                        } else
                        {
                            ((Map)collectionObj).put(key, value);
                        }
                    }
                    opObj = collectionObj;
                } else
                if(outputType.equalsIgnoreCase("java.sql.ResultSet"))
                    opObj = resultSet;
                opObj = collectionObj;
            }
        }
        catch(MissingResourceException mre)
        {
            throw new EbwException(this, mre);
        }
        catch(SQLException sqlEx)
        {
            sqlEx.printStackTrace();
            throw new EbwException(this, sqlEx);
        }
        catch(ClassNotFoundException clntFndEx)
        {
            throw new EbwException(this, clntFndEx);
        }
        catch(InstantiationException insEx)
        {
            throw new EbwException(this, insEx);
        }
        catch(IllegalAccessException e)
        {
            throw new EbwException(this, e);
        }
        catch(SecurityException SecutrityEx)
        {
            throw new EbwException(this, SecutrityEx);
        }
        catch(NoSuchFieldException NoShFieldEx)
        {
            throw new EbwException(this, NoShFieldEx);
        }
        catch(NoSuchMethodException NoShMethodEx)
        {
            throw new EbwException(this, NoShMethodEx);
        }
        catch(IllegalArgumentException IllegalAgEx)
        {
            throw new EbwException(this, IllegalAgEx);
        }
        catch(InvocationTargetException invokeEx)
        {
            throw new EbwException(this, invokeEx);
        }
        catch(IOException ioEx)
        {
            throw new EbwException(this, ioEx);
        }
        catch(Exception ebwEx)
        {
            ebwEx.printStackTrace();
            throw ebwEx;
        }
        //EBWLogger.trace("QueryExecutor :", (new StringBuilder("Exiting from getOutputObject with return value :")).append(opObj).toString());
        return opObj;
    }

    private void createSQLJFile(String fileName, String query)
        throws Exception
    {
        EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Starting Method createSQLJFile having input file is:")).append(fileName).append(", String is:").append(query).append(")").toString());
        StringBuffer content = new StringBuffer();
        content.append("package com.tcs.ebw.serverside.query;\n");
        content.append("\n");
        content.append("import java.sql.ResultSet;\n");
        content.append("import java.sql.Connection;\n");
        content.append("import java.sql.SQLException;\n");
        content.append("import oracle.sqlj.runtime.Oracle;\n");
        content.append("import sqlj.runtime.ResultSetIterator;\n");
        content.append("import sqlj.runtime.ref.DefaultContext;\n");
        content.append("\n");
        if(query.startsWith("Select") || query.startsWith("select"))
        {
            content.append("#sql context ConnectionCtx;\n");
            content.append("\n");
        }
        content.append((new StringBuilder("public class ")).append(fileName).append("{\n").toString());
        if(query.startsWith("Select") || query.startsWith("select"))
        {
            content.append("private ConnectionCtx ctx = null;\n");
            content.append("\n");
            content.append("public ResultSetIterator rsi = null;\n");
            content.append("public ResultSet rs= null;\n");
            content.append("public ResultSet executeQuery(Connection con)throws Exception {\n");
            content.append("try {\n");
            content.append("ctx = new ConnectionCtx(con);\n");
            content.append((new StringBuilder("#sql [ctx] rsi  ={")).append(query).append("};\n").toString());
            content.append("rs = rsi.getResultSet();\n");
            content.append("} catch (Exception e ) {\n");
            content.append("} finally {\n");
            content.append("try {\n");
            content.append("Oracle.close();\n");
            content.append("} catch ( SQLException e ) {");
            content.append("System.err.println(\"SQLException\"+e);\n");
            content.append("}\n");
            content.append("}\n");
            content.append("return rs;\n");
            content.append("}\n");
        } else
        {
            content.append("public void execute(Connection con)throws Exception {\n");
            content.append("try {\n");
            content.append("DefaultContext ctx = new DefaultContext(con);\n");
            content.append((new StringBuilder("#sql [ctx] {")).append(query).append("};\n").toString());
            content.append("} catch (Exception e ) {\n");
            content.append("} finally {\n");
            content.append("try {\n");
            content.append("Oracle.close();\n");
            content.append("} catch ( SQLException e ) {");
            content.append("System.err.println(\"SQLException\"+e);\n");
            content.append("}\n");
            content.append("}\n");
            content.append("}\n");
        }
        content.append("}\n");
        fileLocation = (new StringBuilder(String.valueOf(HOME))).append("/").append((new String(getClass().getPackage().getName())).replace('.', '/')).append("/").toString();
        filePath = (new StringBuilder(String.valueOf(fileLocation))).append(fileName).append(".sqlj").toString();
        FileUtil.writeToFile(filePath, content.toString());
        //EBWLogger.trace("QueryExecutor", "Exiting from createSQLJFile(String fileName, String query)");
    }

    private void translateSQLJFile()
        throws Exception
    {
        //EBWLogger.trace("QueryExecutor :", "Starting Method of translateSQLJFile()");
        String file = filePath.replace('/', '\\');
        Runtime runTime = Runtime.getRuntime();
        runTime.exec((new StringBuilder("java sqlj.tools.Sqlj -codegen=iso ")).append(file).toString());
        EBWLogger.logDebug("QueryExecutor :", file);
        EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("File Name ")).append(file.replaceAll("sqlj", "java")).toString());
        runTime.exec((new StringBuilder("javac ")).append(file.replaceAll("sqlj", "java")).toString());
        //EBWLogger.trace("QueryExecutor :", "Exiting from translateSQLJFile");
    }

    public Connection getConnection() 
    {
        //EBWLogger.trace("QueryExecutor :", "Starting method getConnection for getting connection");
        try
        {
            /*if(con == null)
            {
                String driverName = PropertyFileReader.getProperty("QueryExecutor_drivername");
                String url = PropertyFileReader.getProperty("QueryExecutor_url");
                String user = PropertyFileReader.getProperty("QueryExecutor_user");
                String password = PropertyFileReader.getProperty("QueryExecutor_password");
                //Class.forName(driverName);
                //con = DriverManager.getConnection(url, user, password);
            }*/
        }
        catch(Exception e)
        {
            e.printStackTrace();
            //throw new EbwException(this, e);
        }
        EBWLogger.trace("QueryExecutor :", (new StringBuilder("Exiting from getConnection with Connection :")).append(con).toString());
        return con;
    }

    private void closeStatement(Statement stmt, ResultSet resultset)
        throws Exception
    {
       // EBWLogger.trace("QueryExecutor :", "Closing Statements and ResultSet in closeStatement ");
        if(stmt != null)
        {
            stmt.close();
            stmt = null;
        }
        if(resultset != null)
        {
            resultset.close();
            resultset = null;
        }
    }

    private void closePreparedStatement(PreparedStatement preStmt, ResultSet resultset)
        throws Exception
    {
        //EBWLogger.trace("QueryExecutor :", "Closing PreparedStatements and ResultSet in closeStatement ");
        if(preStmt != null)
        {
            preStmt.close();
            preStmt = null;
        }
        if(resultset != null)
        {
            resultset.close();
            resultset = null;
        }
    }

    public void closeConnections()
        throws SQLException, EbwException
    {
        EBWLogger.trace("QueryExecutor :", "Closing the Connection");
        try
        {
            con.close();
            con = null;
        }
        catch(SQLException sqlEx)
        {
            throw new EbwException(this, sqlEx);
        }
    }

    private void printStringArrayValues(String statementids[], String methodName)
    {
        if(statementids != null)
        {
           // for(int i = 0; i < statementids.length; i++)
                //EBWLogger.trace("QueryExecutor :", (new StringBuilder("statementids[")).append(i).append("] :").append(statementids[i]).toString());

        } else
        {
            EBWLogger.logError(this, (new StringBuilder("Value of String Array in method ")).append(methodName).append(" is NULL").toString());
        }
    }

    private void printTOArrayValues(Object tos[], String str)
    {
        if(tos != null)
        {
            //for(int i = 0; i < tos.length; i++)
                //EBWLogger.trace("QueryExecutor :", (new StringBuilder(String.valueOf(str))).append("[").append(i).append("] is:").append(tos[i].getClass().getName()).toString());

        } else
        {
            EBWLogger.logError(this, (new StringBuilder("Value of TO Array for ")).append(str).append(" is NULL").toString());
        }
    }

    public byte[] getBLOB(ResultSet resultSet, String columnName)
        throws Exception
    {
        Blob blob = resultSet.getBlob(columnName);
        return blob.getBytes(1L, (int)blob.length());
    }

    private Object getBLOBObject(ResultSet resultSet, String columnName)
        throws Exception
    {
        //EBWLogger.trace("QueryExecutor :", (new StringBuilder("Starting method getBLOBObject with ResultSet")).append(resultSet).append(",ColumnName").append(columnName).append(")").toString());
        java.io.InputStream is = resultSet.getBinaryStream(columnName);
        ObjectInputStream objis = new ObjectInputStream(is);
        EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Blob Object from the InputStream is:")).append(objis.readObject()).toString());
        //EBWLogger.trace("QueryExector :", (new StringBuilder("Exiting from getBLOBObject with Output")).append(objis.readObject()).toString());
        return objis.readObject();
    }

    public String ConvStringArrToStr(String strArr[])
    {
        //EBWLogger.trace("QueryExecutor :", "Starting Method ConvStringArrToStr ");
        String result = "";
        try
        {
            String s = ConvertionUtil.convertToCSV(strArr);
            String cls[] = s.split(",");
            for(int y = 0; y < cls.length; y++)
            {
                result = (new StringBuilder(String.valueOf(result))).append(cls[y]).toString();
                result = (new StringBuilder(String.valueOf(new String(new char[] {
                    '\''
                })))).append(result).append(new String(new char[] {
                    '\''
                })).toString();
                result = result.concat(",");
                if(cls.length > 1)
                    result = result.concat("'");
            }

            StringBuffer b = new StringBuffer(result);
            b = b.replace(result.length() - 1, result.length(), "");
            for(int p = 0; p < cls.length - 1; p++)
                b = b.replace(0, 1, "");

            b = b.replace(0, 1, "");
            b = b.replace(b.length() - 1, b.length(), "");
            if(cls.length > 1)
                b = b.replace(b.length() - 1, b.length(), "");
            result = b.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //EBWLogger.trace("QueryExecutor :", (new StringBuilder("Exit from ConvStringArrToStr with value :")).append(result).toString());
        return result;
    }

    public void SetPreparedStmtValues(int parameterCount, PreparedStatement preStmt)
        throws Exception
    {
    	int j = 0 ;
        EBWLogger.trace("QueryExecutor :", (new StringBuilder("Starting Method  SetPreparedStmtValues having parameterCount is -- ")).append(parameterCount).append(" and PreparedStatement is-- ").append(preStmt).toString());
        for(int i = 0; i < parameterCount; i++)
        {
            EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Setting Value for col")).append(i + 1).append(" as :").append(values[i]).toString());
            String s;
            if(values[i] instanceof Double){
                preStmt.setDouble(j + 1, ((Double)values[i]).doubleValue());
                j = j + 1 ;
            }
            else
			if(values[i] instanceof Timestamp){
                preStmt.setTimestamp(j + 1, ConvertionUtil.convertToTimestamp((Date)values[i]));
                j = j + 1 ;
			}
            else
            if(values[i] instanceof Date){
                preStmt.setDate(j + 1, ConvertionUtil.convertToDBDate((Date)values[i]));
                j = j + 1 ;
            }
            else
            if(values[i] instanceof EbwForm)
            {
                EBWLogger.logDebug("QueryExecutor :", (new StringBuilder("Value is of type EBWForm and the Class name is ")).append(values[i].getClass().getName()).toString());
                ByteArrayOutputStream byteos = new ByteArrayOutputStream();
                ObjectOutputStream objos = new ObjectOutputStream(byteos);
                objos.writeObject((EbwForm)values[i]);
                java.io.InputStream instream = new ByteArrayInputStream(byteos.toByteArray());
                int instreamlength = byteos.toByteArray().length;
                preStmt.setBinaryStream(j + 1, instream, instreamlength);
                j = j + 1 ;
            } else
            if(values[i] instanceof byte[]){
                preStmt.setBytes(j + 1, (byte[])values[i]);
                j = j + 1 ;
            }
            else
            if(values[i] instanceof CLOB){
                preStmt.setClob(j + 1, (CLOB)values[i]);
                j = j + 1 ;
            }
            else
            if(values[i] instanceof String){
                preStmt.setString(j + 1, (String)values[i]);
                j = j + 1 ;
            }
            else
                if(values[i] instanceof Integer){
                    preStmt.setInt(j + 1, (Integer)values[i]);
                    j = j + 1 ;
                }
            else
            if(values[i] instanceof NullObj){
                preStmt.setNull(j + 1, ((NullObj)values[i]).getValue());
                j = j + 1 ;
            }
            else
            if(values[i] instanceof String[]){
               // s = ConvStringArrToStr((String[])values[i]);
            	String[] arr=(String[])values[i];
                
              	 for(int l=0;l<arr.length;l++){
                 		String dObj1 = arr[l];
                 		preStmt.setString(j+1, dObj1);
                 		j = j + 1 ;
              	 }
            }
			else
            if(values[i] instanceof BigDecimal){
            		
                    //preStmt.setString(j + 1, values[i].toString());
            	 preStmt.setBigDecimal(j + 1, (BigDecimal)values[i]);
                    j = j + 1 ;
            }
            else{
                preStmt.setString(j + 1, null);
                j = j + 1 ;
            }
        }

        EBWLogger.trace("QueryExecutor :", "Exit from SetPreparedStmtValues");
    }

    public String ConvIntergerArrToInteger(Integer strArr[])
    {
        EBWLogger.trace("QueryExecutor :", "Starting Method ConvStringArrToStr ");
        String result = "";
        try
        {
        	if(strArr.length == 1 && strArr[0].equals(""))
                return null;
            StringBuffer stringbuffer = new StringBuffer();
            for(int i = 0; i < strArr.length; i++)
            {
                EBWLogger.logDebug("ConversionUtil :","Values inside foorloop is :" + strArr[i]);
                stringbuffer.append(strArr[i]);
                stringbuffer.append(",");
            }      
            stringbuffer.deleteCharAt(stringbuffer.toString().length() - 1);   
            //EBWLogger.trace("ConversionUtil :", "Exit from Method convertToCSV with result :"+stringbuffer.toString());
            return stringbuffer.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //EBWLogger.trace("QueryExecutor :", "Exit from ConvStringArrToStr with value :" + result);
        return strArr.toString();
    }       

    protected Connection con;
    protected final String QUERY = ".Query";
    private final String OP_TYPE = ".OutputType";
    private final String RESULTSET = "java.sql.ResultSet";
    private final String TO_COL_MAPPING = ".TOColumnMap";
    private final String STMT_TYPE = ".StmtType";
    private final String ARRAY_COL_MAPPING = ".ArrayColumnMap";
    private final String NUMERIC = "numeric";
    private final String VARCHAR = "varchar";
    private final String DATE = "date";
    private final String TIMESTAMP = "timestamp";
    //private final String SET = "set";
    //private final String GET = "get";
    //private final String OBJECT = "Object";
    //private final String TO = "transferobject";
    //private final String UTIL = "util";
    //private final String HASHMAP = "HashMap";
    //private final String LINKED = "Linked";
    //private final String ARRAYLIST = "ArrayList";
    //private final String VECTOR = "Vector";
    //private static final String SELECT = "SELECT";
    //private final String INSERT = "INSERT";
    //private final String BETWEEN = "between";
    //private final String SQRBRACE = "[]";
    private final String BIGDECIMAL = "java.math.BigDecimal";
    //private final String STRING = "java.lang.String";
    //private final String STRINGARRAY = "java.lang.String[]";
    private final String BIGINTEGER = "java.math.BigInteger";
    //private final String INTEGER = "java.lang.Integer";
    //private final String HASH = "#";
    //private final String DOUBLE = "java.lang.Double";
    //private final String _DATE = "java.util.Date";
    //private final String COLON = ":";
    //private final String COMMA = ",";
    //private final String OPENCURLY = "{";
    //private final String CLOSECURLY = "}";
    //private final String AND = " and ";
    //private final String OPENPAR = "(";
    //private final String CLOSEPAR = ")";
    //private final String OPENSQR = "[";
    //private final String CLOSESQR = "]";
    private final String EQUALTO = "=";
    //private final String LIKE = " like ";
    //private final String PERCENTAGE = "%";
    //private final char SINGLEQUOTE = '\'';
    //private final String DB = "ebw";
    //private static final String QUERYEXECUTOR_PROPERTYFLE = "Statement";
    //private final String DRIVERNAME = "QueryExecutor_drivername";
    //private final String USER = "QueryExecutor_user";
    //private final String PASSWORD = "QueryExecutor_password";
    //private final String URL = "QueryExecutor_url";
    private Class cls;
    //private final String BYTEARRAY = "blob";
    //private final String CLOB = "oracle.sql.CLOB";
    private final String CLOBDATA = "clob";
    private final String BigDecimal = "BigDecimal";
    protected ResourceBundle queryRB;
    private final String HOME = PropertyFileReader.getPropertyKeyValue("DB", "EBWHOME");
    private Object values[];
    private String strStmtType;
    //private final String ORDER = " order";
    private String STMT_PREP;
    private String STMT_ONLY;
    private int parameterCount;
    //private final String ISSQLJ = ".IsSqlj";
    //private final String BRACKET = "()";
    //private final String FALSE = "false";
    //private final String TRUE = "true";
    private String filePath;
    private String fileLocation;
    //private final String BLOB = "oracle.sql.BLOB";
    //private final String IsExternalisedStatements = "isExternalisedStatements";
    protected final String AUDIT = "_Audit";
    //private final String BLANK = "blank";
    //private final String AUDIT_LOGGING = "audit_logging";
    //private final String TIELD = "~";
    //private final String YES = "y";
    //private final String PAGINATIONFLAG = "PaginationFlag";
    //private final String PAGINATIONINDEX = "PaginationIndex";
    //private final String ROWNUM = "rownum";
    //private final String ROWSPERPAGE = "RowsPerPage";
    //private final String UNION = " union ";
    //private final String RESTARTLOGIC_VARCHAR = "#restartlogic:varchar#";
    //private final String ORDERBY = "order by";
    //private final String RESTARTLOGIC = "restartlogic";
    //private final String PAGE = "_page";
    //private final String EXPORT = "export";
    //private final String BY = " by ";
    //private final String GroupBy = "group by";
    //private final String GROUPBY = "GROUP BY";
    protected String ExternalisedStatement;
    //private final String DOT = ".";
    //private final String PLUS = "+";
    private static HashMap linkedHash = new HashMap();
    public  String rowsPerPage;

    static 
    {
        linkedHash = EBWStatement.getMap();
    }
}