package com.tcs.ebw.serverside.jaas.utils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.tcs.ebw.common.util.EBWLogger;



/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
public class EBWSessionListener implements HttpSessionListener {
    
    public void sessionCreated(HttpSessionEvent arg0) {
        	EBWLogger.trace(this,"New User Session with "+arg0.getSession().getId()+" Created.");
        	HttpSession session = (HttpSession)arg0;
        	
    }
    public void sessionDestroyed(HttpSessionEvent arg0) {
        EBWLogger.trace(this,"User with Session id "+arg0.getSession().getId()+" has logged out or Session Timed Out.");
    }
}
