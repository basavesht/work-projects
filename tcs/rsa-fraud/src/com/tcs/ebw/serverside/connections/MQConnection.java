/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.serverside.connections;

import java.util.LinkedHashMap;

import com.tcs.ebw.serverside.factory.IEBWConnection;

public class MQConnection implements IEBWConnection{
	

    /* (non-Javadoc)
     * @see com.tcs.ebw.serverside.factory.IEBWConnection#disconnect()
     */
    public Object disconnect() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
	public Object connect(LinkedHashMap serviceId) throws Exception{
		return null;
	}
	
}
