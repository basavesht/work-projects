/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.auth;

import javax.security.auth.Subject;

/**
 * This interface is used by all the authentication Modules for authenticating a user.
 * Those classes which implement this inteface have to implement all the concrete methodss. 
 * 
 * @author  TCS
 * @version 1.0
 *
 */
public interface Auth {
	/**
	 *  Declaration of the Session attribute name to store the subject 
	 */ 
	public static final String SUBJECT_SESSION_KEY="subject";
	
	/** 
	 * Method to authenticate the user by passing Login Module to 
	 * LoginContext and calling login method of login context.
	 * 
	 * @return - true if the user is authenticated and false otherwise.
	 */
	public boolean authenticate();
	
	/**
	 * Returns the subject which has the user information who has
	 * logged in and to whom various permissions are given @ runtime.
	 * 
	 * @return - Returns the subject which is loaded  after the user 
	 * logged into  the application.
	 */
	public Subject getSubject();
}
