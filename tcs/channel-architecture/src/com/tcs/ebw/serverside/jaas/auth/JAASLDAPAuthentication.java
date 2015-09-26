/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.serverside.jaas.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
/**
 * 
 * This Class is used for implementing LDAP Authentication using the 'Auth' interface.
 *
 */
public class JAASLDAPAuthentication extends JAASAbstractAuth implements Auth{
	/**
     * Logging output for this plug in instance.
     */
    protected Log log = LogFactory.getLog(this.getClass());
    
    /**
     * 
     * @param username - Login credential passed from Client interaction class(LogonForm).
     * @param password - Login credential passed from Client interaction class(LogonForm).
     */
    protected String username,password;
    /**
     * Class Constructor
     * @param username - The user id requesting Authentication.
     * @param password - The Password requesting Authentication.
     */
	public JAASLDAPAuthentication(String username,String password){
		super(username,password);
	}
	
	/**
	 * @see com.tcs.ebw.jaas.Auth#authenticate()
	 */
	public boolean authenticate() {
		try {
		         lc = new LoginContext("EBWLoginModules",new EBWCallbackHandler(username, password));
		     	 lc.login();
		    } catch (LoginException le) {
		    		le.printStackTrace();
		    		//log.error(le.getCause());
		            return false;
		    }
		    catch(Exception e){
		    	e.printStackTrace();
		    }
		  return true;
	}
	
	
	}
