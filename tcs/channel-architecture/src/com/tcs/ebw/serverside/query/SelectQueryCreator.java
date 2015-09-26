package com.tcs.ebw.serverside.query;

import java.sql.SQLException;

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
public class SelectQueryCreator extends SQLCreatorHelper implements ISQLCreator{

    /*
     *  (non-Javadoc)
     * @see com.tcs.ebw.serverside.query.ISQLCreator#getQuery(java.lang.String, java.lang.Object)
     */
    public String getQuery(String tableName,Object params) throws SQLException, Exception {
        loadTableMetaData(tableName);
        qryString.delete(0,qryString.toString().length());
        
        EBWLogger.logDebug(this,"Started getQuery"+rsmd+" table is null :"+(rsmd==null));
        
        qryString.append("get"+StringUtil.initCaps(tableName)+".Query=SELECT \\\r\n");
        
        for(int i=1;i<=rsmd.getColumnCount();i++)
            qryString.append("\t"+ rsmd.getColumnName(i)+",\\\r\n");
        
        qryString.delete(qryString.toString().length()-",\\\r\n".length(),qryString.toString().length());
        qryString.append(" \\");
        qryString.append("\r\n\t FROM "+tableName+" \\\r\n");
        
        qryString.append("\t WHERE \\");
        
        for(int i=1;i<=rsmd.getColumnCount();i++){
            addColumnAndValue(i);
    		qryString.append("# AND \\");
        }
        
        qryString.delete(qryString.length()-("# AND ".length()),qryString.toString().length());
        
        qryString.append("\r\n");
        
        qryString.append("get"+StringUtil.initCaps(tableName)+".TOColumnMap={\\");
        for(int i=1;i<=rsmd.getColumnCount();i++){
            addTOColumnMap(i);
    		qryString.append(",\\");
        }
        qryString.delete(qryString.length()-(",\\".length()),qryString.toString().length());
        
        qryString.append("\\\r\n\t}");
        
        qryString.append("\r\n");
        
        qryString.append("get"+StringUtil.initCaps(tableName)+".OutputType=java.util.ArrayList");
        return qryString.toString();
    }
}
