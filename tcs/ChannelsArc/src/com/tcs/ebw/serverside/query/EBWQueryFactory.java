package com.tcs.ebw.serverside.query;

/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class EBWQueryFactory {
    public static final int QUERY_TYPE_SELECT = 0;
    public static final int QUERY_TYPE_INSERT = 1;
    public static final int QUERY_TYPE_UPDATE = 2;
    public static final int QUERY_TYPE_DELETE = 3;
    
    
    public static ISQLCreator create(final int queryType){
        ISQLCreator queryCreator=null;
        switch(queryType){
          case QUERY_TYPE_SELECT:
            queryCreator = new SelectQueryCreator();
            break;
           case QUERY_TYPE_INSERT:
               queryCreator = new InsertQueryCreator();
               break;
           case QUERY_TYPE_UPDATE:
               queryCreator = new UpdateQueryCreator();
               break;
           case QUERY_TYPE_DELETE:
               queryCreator = new DeleteQueryCreator();
               break;
        }
        return queryCreator;
    }
}
