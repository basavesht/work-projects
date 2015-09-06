/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.serverside.jaas.auth;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import com.tcs.ebw.common.util.EBWLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * An abstract class that imlements class 'Auth'for JAAS Authentication.
 *
 */



public class JAASAbstractAuth implements Auth {
	/* Store username got from the User keyed in at runtime*/
	protected String username;
	
	/* Store password got from the User keyed in at runtime */
	protected String password;
	
	/* Login context object which doest the actual login process */
	protected LoginContext lc;
	/*
	 * Store username and password got @runtime into the local variables
	 * @param username - username received at runtime from the user.
	 * @param password - password recieved at runtime from the user.
	 */
	
	/**
     * Logging output for this plug in instance.
     */
    protected Log log = LogFactory.getLog(this.getClass());
    

    /**
     *	Class Constructor 
     * @param username - Login credential passed from Client interaction class(LogonForm).
     * @param password - Login credential passed from Client interaction class(LogonForm).
     */
	public JAASAbstractAuth(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	
	
    /* (non-Javadoc)
	 * @see com.tcs.ebw.jaas.Auth#authenticate()
	 */
	public boolean authenticate() throws Exception{
		//try {
				EBWLogger.trace(this,"into authenticate method whith username and password "+username + " "+password);
				//System.setProperty("java.security.auth.login.config", "C:\\Docume~1\\152699\\ebw_jaas.config");
				//System.setProperty("java.security.auth.policy", "C:\\Docume~1\\152699\\ebw_jaas.policy");
				
				javax.security.auth.callback.CallbackHandler ebwch = new EBWCallbackHandler(username,password);
				
				 lc = new LoginContext("EBWLoginModules",ebwch);
		         EBWLogger.trace(this,"Login context created.."+lc.getClass()+" lcnull"+(lc==null));
		     	 lc.login();
		     	 
		    /*} catch (LoginException le) {
		    		le.printStackTrace();
		    		System.out.println("Error message 1:"+le.getMessage());
		    		//System.out.println("Cause :"+le.getCause());
		    		//log.error(le.getCause());
		            return false;
		    }catch(Exception e){
		    	e.printStackTrace();
		    	System.out.println("Error message 2:"+e.getMessage());
	    		//System.out.println("Cause :"+e.getCause());
		    }*/
		  return true;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.tcs.ebw.jaas.Auth#getSubject()
	 */
	/**
	 * @see com.tcs.ebw.jaas.Auth#getSubject()
	 */
	
	public Subject getSubject() {
		EBWLogger.trace(this,"into get subject");
		if (lc == null) {
		     throw new IllegalStateException("either login failed or the authenticate method hasn't been called.");
		     } else {
		      return lc.getSubject();
		}
	}
	
	/**
	 * 
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
	
	public static void main(String args[]){
		JAASAbstractAuth ja = new JAASAbstractAuth("wpsadmin","wpsadmin");
		//ja.authenticate();
		
	}
	
}
