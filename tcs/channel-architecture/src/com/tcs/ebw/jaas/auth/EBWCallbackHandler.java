/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.auth;

import java.io.IOException;

import javax.security.auth.callback.*;

public class EBWCallbackHandler implements CallbackHandler{

	private String username;
	private String password;
	
	/**
	 * Constructor of EBWCallbackHandler
	 * @param username UserName of a user
	 * @param password Password of a user
	 */
	public EBWCallbackHandler(String username,String password){
		this.username=username;
		this.password = password;
	}
	
	/**
	 * This method is used for authenticating a user by providing 
	 * callback array(username callback,password callback)
	 *  
	 * @param callbacks Callback[] is passed as input parameter
	 */

	
	public void handle(Callback[] callbacks) throws IOException,UnsupportedCallbackException {
		System.out.println("Into callback handle "+callbacks.length);
		
		callbacks[0] = new NameCallback("Username");
		((NameCallback)callbacks[0]).setName(username);
		callbacks[1] = new NameCallback("Password");
		((NameCallback)callbacks[1]).setName(password);
		System.out.println("instace of calback Username "+((NameCallback)callbacks[0]).getName());
		System.out.println("instace of calback Password "+((NameCallback)callbacks[1]).getName());

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
