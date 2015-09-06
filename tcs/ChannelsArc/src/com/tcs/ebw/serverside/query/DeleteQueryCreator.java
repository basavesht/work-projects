package com.tcs.ebw.serverside.query;

import java.sql.SQLException;
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
public class DeleteQueryCreator extends SQLCreatorHelper implements ISQLCreator{
    
    /*
     *  (non-Javadoc)
     * @see com.tcs.ebw.serverside.query.ISQLCreator#getQuery(java.lang.String, java.lang.Object)
     */
    public String getQuery(String tableName,Object params) throws SQLException, Exception{
        LinkedHashMap param=null;
        boolean softdelete =false;
        if(params!=null){
	        param= (LinkedHashMap)params;
	        softdelete = ((Boolean)param.get("softdelete")).booleanValue();
        }
        
        loadTableMetaData(tableName);
        qryString.delete(0,qryString.toString().length());
        
        EBWLogger.logDebug(this,"Started getQuery"+rsmd+" table is null :"+(rsmd==null));
        
        if(!softdelete){
            qryString.append("delete"+StringUtil.initCaps(tableName)+".Query=DELETE FROM "+tableName+"\r\n");
            qryString.append("\t WHERE \r\n");
        }
        
        for(int i=1;i<=rsmd.getColumnCount();i++)
            if(!softdelete){
                addColumnAndValue(i);
                qryString.append("# AND ");
            }
            else{
                qryString.delete(0,qryString.toString().length());
                return EBWQueryFactory.create(EBWQueryFactory.QUERY_TYPE_UPDATE).getQuery(tableName,param);
            }
        
    
        if(qryString.toString().indexOf("AND")!=-1)
            qryString.delete(qryString.length()-("# AND ".length()),qryString.toString().length());
        
        return qryString.toString();
    }
}
