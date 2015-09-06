package com.tcs.ebw.serverside.query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.StringUtil;
/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class UpdateQueryCreator extends SQLCreatorHelper implements ISQLCreator{

    /*
     *  (non-Javadoc)
     * @see com.tcs.ebw.serverside.query.ISQLCreator#getQuery(java.lang.String, java.lang.Object)
     */
    public String getQuery(String tableName,Object params) throws SQLException,Exception{
        boolean softdelete = false; 
        loadTableMetaData(tableName);
        qryString.delete(0,qryString.toString().length());
        EBWLogger.logDebug(this,"Started getQuery"+rsmd+" table is null :"+(rsmd==null));
        
        if(params!=null)
            if(((LinkedHashMap)params).containsKey("softdelete"))
                softdelete= ((Boolean)((LinkedHashMap)params).get("softdelete")).booleanValue();
        
        if(!softdelete)
            qryString.append("update"+StringUtil.initCaps(tableName)+".Query=UPDATE "+tableName);
        else
            qryString.append("delete"+StringUtil.initCaps(tableName)+".Query=UPDATE "+tableName);
        
        //Add set clause
        qryString.append(" SET ");
        for(int i=1;i<=rsmd.getColumnCount();i++){
            if(softdelete){
                if(rsmd.getColumnName(i).toUpperCase().startsWith("VERSION") ||
                        rsmd.getColumnName(i).toUpperCase().startsWith("DELETEFLAG")){
                    addColumnAndValue(i);
                    qryString.append("#,");
                }
            }
            else{
                addColumnAndValue(i);
                qryString.append("#,");
            }
            
            
        }

        qryString.delete(qryString.toString().length()-2, qryString.length());
        ArrayList primaryCols = getPrimaryKeyCols(tableName);
        EBWLogger.logDebug(this,"primaryCols "+primaryCols);
        
        //Add where clause
        qryString.append("\r\n\t WHERE ");
        for(int i=1;i<=rsmd.getColumnCount();i++){
            EBWLogger.logDebug(this,"Checking col :"+rsmd.getColumnName(i));
            if(primaryCols.contains(rsmd.getColumnName(i))){
	            addColumnAndValue(i);
	    		qryString.append("# AND ");
            }
        }
        qryString.delete(qryString.length()- ("# AND ".length()-1),qryString.toString().length());
		return qryString.toString();
    }
}
