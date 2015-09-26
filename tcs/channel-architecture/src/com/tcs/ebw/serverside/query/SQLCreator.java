package com.tcs.ebw.serverside.query;

import java.util.LinkedHashMap;

import com.tcs.ebw.common.util.FileUtil;

/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class SQLCreator {
    	
    public String createSQLs(String tableName) throws Exception{
        StringBuffer queries = new StringBuffer();
        String selectQry="",insertQry="",deleteQry="",updateQry="";
	    LinkedHashMap params = new LinkedHashMap();
	    
	    if(tableName!=null){
		    selectQry = EBWQueryFactory.create(EBWQueryFactory.QUERY_TYPE_SELECT).getQuery(tableName,null);
		    insertQry = EBWQueryFactory.create(EBWQueryFactory.QUERY_TYPE_INSERT).getQuery(tableName,null);
		    
		    params.put("softdelete",new Boolean(true));
		    deleteQry = EBWQueryFactory.create(EBWQueryFactory.QUERY_TYPE_DELETE).getQuery(tableName,params);
		    updateQry = EBWQueryFactory.create(EBWQueryFactory.QUERY_TYPE_UPDATE).getQuery(tableName,null);
		    
		    selectQry+="\r\n\r\n";
	        FileUtil.writeToFileAppend("statement1.properties",selectQry,false);
	        insertQry+="\r\n\r\n";
	        FileUtil.writeToFileAppend("statement1.properties",insertQry,true);
	        deleteQry+="\r\n\r\n";
	        FileUtil.writeToFileAppend("statement1.properties",deleteQry,true);
		    FileUtil.writeToFileAppend("statement1.properties",updateQry,true);
	        
		    queries.append(selectQry);
		    queries.append(insertQry);
		    queries.append(deleteQry);
		    queries.append(updateQry);
	    }
        return queries.toString();
    }
}
