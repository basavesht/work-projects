/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.serverside.factory;

import com.tcs.ebw.common.context.EBWAppContext;
import java.util.LinkedHashMap;

public interface IEBWService {
    
    /**
	 * To execute any service, we need the service informations like
	 * Implementation classname, method name etc. All these service related
	 * informations are stored and set in a Service Implementation class using
	 * this method.
	 * 
	 * @param serviceInfo - List of key value pairs having information about
	 * 						the service.
	 */
	public void setServiceInfo(LinkedHashMap serviceInfo) ;
	
	
	/**
	 * Service needs connection for execution. Hence this method
	 * is used to set the connection object created using the 
	 * ConnetionFactory. Note that connection has to be created
	 * and set before calling any service.
	 * 
	 * @param connectionObj - Connection object used by the service. It
	 *    					  can be a Database connection, HttpConnection etc
	 * 						  based on the type of the service call. These 
	 * 						  configurations are taken from the service configuration
	 *  					  table.
	 * @return
	 */
	public Object setConnection(Object connectionObj);
	
	
	/**
	 *   Execute method is used to call the service method
	 * given in the implementation class. Please refer EBWAbstractServiceHandler
	 * on usage of this execute method. 
	 * 
	 * @param paramType - Array representing the class type of individual parameters.
	 * @param params	- Array containing the parameter objects to be passed to service
	 * 					  method.
	 * @return			- Return value is based on the implementation and requirement 
	 *                    of the service, hence it is declared as Object. Class which is
	 * 					  calling the service should know the return type and hence should
	 * 					  type cast the returned object to required class type.  
	 */
	public Object execute(Class paramType[],Object params[]) throws Exception;
	
	/**
	 * Sets the application Context having application related information   
	 */
	public void setAppContext(EBWAppContext appContext);
	
	/**
	 * Returns the application Context.  
	 */
	public EBWAppContext getAppContext();
	
	
	//public LinkedHashMap getBusinessServiceInfo(String serviceId) throws Exception;
	
	//public LinkedHashMap getBusinessSystemInfo(String serviceId) throws Exception;
	
	/**
	 * This method is used to close all the open connections after executing
	 * one or more service(s).
	 * @return - Returns success or failure code based on closing operation.
	 */
	public Object close() throws Exception;
	
	/**
	 * Sets the cash denominations information.
	 */
	public void setCashDenominationHandler(Object cashDenominationHandler);
	
	/**
	 * Returns the cash denominations information.
	 */
	public Object getCashDenominationHandler();
	
	/**
	 * Sets the error override flag information.
	 */
	public void setErrorOverride(boolean errorOverride);
	
	/**
	 * Returns the error override flag information.
	 */
	public boolean isErrorOverride();
		
}
