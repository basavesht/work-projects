package com.tcs.ebw.serverside.query;


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
public class InsertQueryCreator extends SQLCreatorHelper implements ISQLCreator {
    
    /*
     *  (non-Javadoc)
     * @see com.tcs.ebw.serverside.query.ISQLCreator#getQuery(java.lang.String, java.lang.Object)
     */
    public String getQuery(String tableName,Object params) throws Exception{
        loadTableMetaData(tableName);
        qryString.delete(0,qryString.toString().length());
        
        EBWLogger.logDebug(this,"Started getQuery"+rsmd+" table is null :"+(rsmd==null));
        
        qryString.append("set"+StringUtil.initCaps(tableName)+".Query=INSERT INTO "+tableName);
        qryString.append("(\\\r\n");
        
        for(int i=1;i<=rsmd.getColumnCount();i++)
            qryString.append("\t"+ rsmd.getColumnName(i)+",\\\r\n");
        
        qryString.delete(qryString.length() - ",\\\r\n".length(), qryString.length());
        
        qryString.append(") values(\\\r\n");

        //Add .Query property 
        for(int i=1;i<=rsmd.getColumnCount();i++){
            addColumnValues(i);
            qryString.append("#,\\\r\n");
        }
        
        qryString.delete(qryString.toString().length()-4,qryString.toString().length());
        qryString.append("\\");
        qryString.append("\r\n\t )");
        
        closeAll();
        EBWLogger.logDebug(this,"Finishing getQuery");
        return qryString.toString();
    }
}
