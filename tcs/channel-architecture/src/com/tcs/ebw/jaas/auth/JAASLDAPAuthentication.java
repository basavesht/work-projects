/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * This class is for authenticating an user through JAAS using LDAP Authentication Modules
 * by providing username and password of the user.
 * 
 * @author TCS
 * @version 1.0
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
     * Constructor of JAASLDAPAuthentication
     * @param username Username of the user 
     * @param password Password of the user
     */
	public JAASLDAPAuthentication(String username,String password){
		super(username,password);
	}
	
	/**
	 * This function is for authenticating a user based on username and password
	 * @return boolean 
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
