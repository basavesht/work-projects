/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.serverside.jaas.auth;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import com.tcs.ebw.common.util.EBWLogger;
/**
 * An application implements a CallbackHandler and passes it to underlying security services so that 
 * they may interact with the application to retrieve specific authentication data, such as usernames 
 * and passwords, or to display certain information, such as error and warning messages. 
 */
public class EBWCallbackHandler implements CallbackHandler{
/**
 * @param username is the userid used in Login Module for Authentication
 */
	private String username;
/**
 * @param password is the password used in Login Module for Authentication
 */
	private String password;
	/**
	 * @param miscinfo is any information other than username & passwordthat can be passed used in Login Module for Authentication
	 */	
	
	
	private String 	miscinfo;
	/**
	 *The constructor for this class 
	 */
	
	public EBWCallbackHandler(String username,String password){
		this.username=username;
		this.password = password;
		this.miscinfo=null;
	}
	public EBWCallbackHandler(String username,String password,String miscinfo){
		this.username=username;
		this.password = password;
		this.miscinfo=miscinfo;
	}
	

	/**
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
	 */
	
	/**
	 * This method implementation checks the instance(s) of the Callback object(s) passed in to retrieve or display the requested information.
	 */
	public void handle(Callback[] callbacks) throws IOException,UnsupportedCallbackException {
		System.out.println("Into callback handle "+callbacks.length);
		
		callbacks[0] = new NameCallback("Username");
		((NameCallback)callbacks[0]).setName(username);
		callbacks[1] = new PasswordCallback("Password",false);
		((PasswordCallback)callbacks[1]).setPassword(password.toCharArray());
		System.out.println("instace of calback Username "+((NameCallback)callbacks[0]).getName());
		System.out.println("instace of calback Password "+new String(((PasswordCallback)callbacks[1]).getPassword()));
		try{
		if (miscinfo!=null || miscinfo!=""){
			callbacks[2] = new NameCallback("Miscinfo");
			((NameCallback)callbacks[2]).setName(miscinfo);
			System.out.println("instace of calback Miscinfo"+((NameCallback)callbacks[2]).getName());
		}
		}catch(Exception e){
		EBWLogger.logDebug(this,"WARNING: miscinfo is null -"+e.getMessage());	
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
	/**
	 * @return Returns the username.
	 */
	public String getMiscinfo() {
		return miscinfo;
	}
	/**
	 * @param username The username to set.
	 */
	public void setMiscinfo(String miscinfo) {
		this.miscinfo= miscinfo;
	}

}
