/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.auth;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class JAASAbstractAuth implements Auth{
	/**
	 *  Store username got from the User keyed in at runtime
	 */
	protected String username;
	
	/**
	 *  Store password got from the User keyed in at runtime 
	 */
	protected String password;
	
	/**
	 *  Login context object which doest the actual login process 
	 */
	protected LoginContext lc;

	
	/**
     * Logging output for this plug in instance.
     */
    protected Log log = LogFactory.getLog(this.getClass());
    

    /**
     * 
     * @param username - Login credential passed from Client interaction class(LogonForm).
     * @param password - Login credential passed from Client interaction class(LogonForm).
     */
	public JAASAbstractAuth(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	
	/** 
	 * Method to authenticate the user by passing Login Module to 
	 * LoginContext and calling login method of login context.
	 * 
	 * @return - true if the user is authenticated and false otherwise.
	 */
	public boolean authenticate() {
		try {
				System.out.println("into authenticate method whith username and password "+username + " "+password);
				javax.security.auth.callback.CallbackHandler ebwch = new EBWCallbackHandler(username,password);			
				 lc = new LoginContext("EBWLoginModules",ebwch);
		         System.out.println("Login context created.."+lc.getClass()+" lcnull"+(lc==null));
		     	 lc.login();
		    } catch (LoginException le) {
		    		le.printStackTrace();
		    		System.out.println("Error message 1:"+le.getMessage());		    	
		            return false;
		    }catch(Exception e){
		    	e.printStackTrace();
		    	System.out.println("Error message 2:"+e.getMessage());	    	
		    }
		  return true;
	}
	
	
	
	/**
	 * Returns the subject which has the user information who has
	 * logged in and to whom various permissions are given @ runtime.
	 * 
	 * @return - Returns the subject which is loaded  after the user 
	 * logged into  the application.
	 */
	public Subject getSubject() {
		System.out.println("into get subject");
		if (lc == null) {
		     throw new IllegalStateException("either login failed or the authenticate method hasn't been called.");
		     } else {
		      return lc.getSubject();
		}
	}
	
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
}
