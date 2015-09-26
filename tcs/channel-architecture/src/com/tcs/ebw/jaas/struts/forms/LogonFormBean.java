
/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.struts.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form used to load information from user screen to this class.
 * @author  TCS
 * @version 1.0
 * 
 */
public class LogonFormBean extends ActionForm{
	
	/**
	 * variable username of user
	 */
	private String username;
	
	/**
	 * variable password of user
	 */
	private String password;
	
	
	/**
	 * This method reset username & password
	 * @param arg0 ActionMapping
	 * @param arg1 HttpServletRequest
	 */
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		username="";
		password="";
		
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
