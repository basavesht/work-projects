/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.serverside.jaas.loginmodules;

import javax.security.auth.spi.LoginModule;
import java.util.Map;
import javax.security.auth.*;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.*;
import java.sql.*;

import com.tcs.ebw.serverside.jaas.principal.RolePrincipal;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.common.util.EBWLogger;
import org.apache.commons.logging.*;
/**
 * This is an abstract class that implements 'javax.security.auth.spi.LoginModule'. It is used to 
 * populate the user principal and committing the user Authentication
 * 
 *
 */
public abstract class JAASAbstractLoginModule implements LoginModule{
	/**
	 * Subject is the person or entity which might have 1 or multiple principals.
	 */
    protected Subject subject;
    
    /**
     * States to be shared with other login module. If this is not needed in the
     * user defined login module, then it can be ignored.
     */
    protected Map sharedState;
    
    
    protected Map options;
    /**
     *  configurable option
     */
    protected boolean debug = false;

    /**
     * the authentication status
     */ 
    protected boolean succeeded = false;
    protected boolean commitSucceeded = false;

    /**
     *  username and password
     */
    protected String username;
    protected String password;
    protected String miscinfo;
//  testUser's SamplePrincipal
    protected UserPrincipal userPrincipal;
    protected RolePrincipal rolePrincipal;
    
    protected Connection connection;
    public CallbackHandler callbackHandler;
    //public EBWCallbackHandler callbackHandler;
    
    /**
     * Logging output for this plug in instance.
     */
    protected Log log = LogFactory.getLog(this.getClass());
    
	/**
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	public boolean abort() throws LoginException {
		return false;
	}
	
	protected abstract void initializeConnection() throws Throwable;
	/**
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	public boolean logout() throws LoginException {
		subject.getPrincipals().remove(userPrincipal);
		succeeded = false;
		succeeded = commitSucceeded;
		username = null;
		password = null;
		userPrincipal = null;
		miscinfo=null;
		return true;
	}
    
	/**
     * Initialize this <code>LoginModule</code>.
     *
     * <p>
     *
     * @param subject the <code>Subject</code> to be authenticated. <p>
     *
     * @param sharedState shared <code>LoginModule</code> state. <p>
     *
     * @param options options specified in the login
     *			<code>Configuration</code> for this particular
     *			<code>LoginModule</code>.
     */
   // public void initialize(Subject subject) {
 
 //   	this.subject = subject;
  //  }
    
    
    /**
     * <p> This method is called if the LoginContext's
     * overall authentication succeeded
     * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
     * succeeded).
     *
     * <p> If this LoginModule's own authentication attempt
     * succeeded (checked by retrieving the private state saved by the
     * <code>login</code> method), then this method associates a
     * <code>SamplePrincipal</code>
     * with the <code>Subject</code> located in the
     * <code>LoginModule</code>.  If this LoginModule's own
     * authentication attempted failed, then this method removes
     * any state that was originally saved.
     *
     * <p>
     *
     * @exception LoginException if the commit fails.
     *
     * @return true if this LoginModule's own login and commit
     *		attempts succeeded, or false otherwise.
     */
    public boolean commit() throws LoginException {
    	EBWLogger.logDebug(this,"inside commit");
	if (succeeded == false) {
		EBWLogger.logDebug(this,"Commit returning false");
	    return false;
	} else {
		EBWLogger.logDebug(this,"Login Successfull..in comit.");
	    // add a Principal (authenticated identity)
	    // to the Subject

	    // assume the user we authenticated is the DBMSPrincipal
		
		
	    if (!subject.getPrincipals().contains(userPrincipal)){
	        subject.getPrincipals().add(userPrincipal);
	        EBWLogger.logDebug(this,"Loaded principal of type "+userPrincipal.getClass().getName()+" into Subject");
	        EBWLogger.logDebug(this,"Fanums in userPrincipal of JAASAbstractLoginModule is :"+userPrincipal.getFanums());
	    }
	    // in any case, clean out state
	    username = null;
	    password = null;
	    miscinfo=null;
	    commitSucceeded = true;
	    EBWLogger.logDebug(this,"Returning true from commit..");
	    return true;
	}
    }

}
