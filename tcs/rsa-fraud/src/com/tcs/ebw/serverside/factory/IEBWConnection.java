/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.serverside.factory;

import java.util.LinkedHashMap;

/**
 * This interface will be used as a basic prototype for all the connections
 * to external or internal systems defined as per the system configuration
 * table. All connection classes specific to individual systems should
 * implement this interface and override these methods to be compliant
 * to eBankworks Serverside Framework.
 */

public interface IEBWConnection {
	
	/**
	 * This method is used to connect with the system which is defined in the 
	 * eBankworks serverside System configuration table read by the connection
	 * established using localConnect method defined above.
	 *  
	 * @return - Will return the connection object obtained by trying to connect
	 *           with the system defined in system configuration table.
	 *           Eg.If the system is defined as Database type, then the connection
	 *           object will return a Database connection object. If its a webservice
	 *           then the connection  object will have the handle to WebService endpoint etc.
	 */
	public Object connect(LinkedHashMap systemInfo) throws Exception;
	public Object disconnect() throws Exception;
	
}
