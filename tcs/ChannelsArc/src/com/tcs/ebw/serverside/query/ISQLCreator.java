package com.tcs.ebw.serverside.query;

import java.sql.SQLException;
/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public interface ISQLCreator {
    	/**
    	 * This method will form and return the queries corresponding
    	 * to the type of the Statement (INSERT,DELETE,UPDATE,SELECT).
    	 * 
    	 * @param tableName
    	 * @param params - It can have user input or parameters like 
    	 * 				   whether it is a softdelete or not. Handling
    	 * 				   of this param object is independent of the
    	 * 				   class which is implementing this interface.
    	 * @return       - Returns the formed query.
    	 * @throws SQLException
    	 * @throws Exception
    	 */
    	public String getQuery(String tableName,Object params) throws SQLException,Exception;
}
