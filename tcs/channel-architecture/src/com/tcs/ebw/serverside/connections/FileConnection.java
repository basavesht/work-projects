/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.serverside.connections;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.serverside.factory.IEBWConnection;


public class FileConnection implements IEBWConnection {

    /* (non-Javadoc)
     * @see com.tcs.ebw.serverside.factory.IEBWConnection#disconnect()
     */
    public Object disconnect() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
	private Connection serviceConnection;
	private DataSource ds=null;
	
	public Object connect(LinkedHashMap params) throws Exception{
			EBWLogger.trace(this,"Starting ConfigConnectionHandler.getConnect().");
			String serviceId = (String)params.get("serviceId");
			
			Hashtable properties = new Hashtable();
			properties.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
			properties.put(Context.PROVIDER_URL,PropertyFileReader.getProperty("baseProtocol")+"://"+PropertyFileReader.getProperty("ip_address")+":"+PropertyFileReader.getProperty("port_no"));
			properties.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			
			EBWLogger.logDebug(this,PropertyFileReader.getProperty("baseProtocol")+"://"+PropertyFileReader.getProperty("ip_address")+":"+PropertyFileReader.getProperty("port_no"));
			InitialContext ctx = new InitialContext(properties);
			ds = (DataSource) ctx.lookup(PropertyFileReader.getProperty(serviceId));
			serviceConnection =ds.getConnection(); 
			EBWLogger.trace(this,"Finished ConfigConnectionHandler.getConnect()....");
		
		return serviceConnection;
	}


}
