/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 * 
 * This Factory class is used to return the appropriate Connection Handler
 * class which will be used by Service Factory to get the connection object
 * for the service.
 */
package com.tcs.ebw.serverside.factory;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.connections.DatabaseConnection;
import com.tcs.ebw.serverside.connections.EJBConnection;
import com.tcs.ebw.serverside.connections.FileConnection;
import com.tcs.ebw.serverside.connections.MQConnection;
import com.tcs.ebw.serverside.connections.SystemConstants;


public class EBWConnectionFactory {
	
    /**
	 * This method is used to return the appropriate Connection class 
	 * to get connection used by Services.
	 * 
	 * @system_type - In this method system_type means the system name.
	 *                This method will be used by all the service classes 
	 * 				  to get connection object.
	 */
	public static IEBWConnection create(String system_type) {
	    EBWLogger.logDebug("com.tcs.ebw.serverside.factory.EBWConnectionFactory","Starting create method..");
	    EBWLogger.logDebug("************ system type is ********",system_type);
	    
		IEBWConnection connectionObj=null;
			if(system_type.equalsIgnoreCase(SystemConstants.SYSTEM_TYPE_FILE))
				connectionObj = new FileConnection();
			else if(system_type.equalsIgnoreCase(SystemConstants.SYSTEM_TYPE_DATABASE))
				connectionObj = new DatabaseConnection();
			else if(system_type.equalsIgnoreCase(SystemConstants.SYSTEM_TYPE_MQ))
				connectionObj = new MQConnection();
			else if(system_type.equalsIgnoreCase(SystemConstants.SYSTEM_TYPE_EJB))
				connectionObj = new EJBConnection();
			/*else
			    throw new ConnectionHandlerNotFoundException(system_type);
			    */
			
		EBWLogger.logDebug("com.tcs.ebw.serverside.factory.EBWConnectionFactory","Finished create method");
		
		return connectionObj;
	}
}
