/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.loginmodules;

import javax.security.auth.spi.LoginModule;
import java.util.Map;
import javax.security.auth.*;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.*;
import java.sql.*;
import com.tcs.ebw.jaas.principal.UserPrincipal;
import org.apache.commons.logging.*;

/**
 * This is an abstract class whose partail methods will be implemented by those classes
 * which extend this class.Otherwise those classes will be abstract.
 * 
 * @author  TCS
 * @version 1.0
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
    
    /**
     *   variable configurable option
     */
    protected Map options;
  
    protected boolean debug = false;

    /** the authentication status*/
    protected boolean succeeded = false;
    protected boolean commitSucceeded = false;

    /**
     * variable username
     */ 
    protected String username;
    
    /**
     * varable password
     */
    protected String password;
    
    /**
     *  variable  UserPrincipal reference
     */    
    protected UserPrincipal userPrincipal;
    
    /**
	 * Connection object which has the connection to an LDAP Server.
	 * The parameters for LDAP connection will be taken from the 
	 * Property file.
	 *  
	 */
    protected Connection connection;
    
    /**
     * This variable is  callback handler 
     */
    public CallbackHandler callbackHandler;    
    
    /**
     * Logging output for this plug in instance.
     */
    protected Log log = LogFactory.getLog(this.getClass());
    
	/** 
	 * This method is to abort the login process
	 * @return boolean if true,then abort from login process,else not
	 */
	public boolean abort() throws LoginException {
		return false;
	}
	
	protected abstract void initializeConnection();
	
	/**
	 *  This method is to logout from login process
	 *  @return boolean if true,then logout from login process,else not
	 *  
	 */
	public boolean logout() throws LoginException {
		subject.getPrincipals().remove(userPrincipal);
		succeeded = false;
		succeeded = commitSucceeded;
		username = null;
		password = null;
		userPrincipal = null;
		return true;
	}
    
	/**
     * Initialize this <code>LoginModule</code>.
     *
     * <p>
     *
     * @param subject the <code>Subject</code> to be authenticated. <p>
     *  
     */
    public void initialize(Subject subject) {
 
    	this.subject = subject;
    }
    
    
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
    	System.out.println("inside commit");
	if (succeeded == false) {
		System.out.println("Commit returning false");
	    return false;
	} else {
		System.out.println("Login Successfull..in comit.");
	    // add a Principal (authenticated identity)
	    // to the Subject

	    // assume the user we authenticated is the DBMSPrincipal
		userPrincipal = new UserPrincipal(username);
		
	    if (!subject.getPrincipals().contains(userPrincipal))
		subject.getPrincipals().add(userPrincipal);
	    
	    // in any case, clean out state
	    username = null;
	    password = null;

	    commitSucceeded = true;
	    System.out.println("Returning true from commit..");
	    return true;
	}
    }

}
