/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.auth;

import java.security.Permission;

/**
 * This class gives the permission factory which is a singleton class
 * 
 * @author TCS
 * @version 1.0
 *
 */
public class PermissionFactory {

	public static PermissionFactory getInstance(){
		return new PermissionFactory();
	}
	
	/**
	 * This method is to give Permission to a user.
	 * @param url URL passed as input
	 * @return Permission
	 */
	public Permission getPermission(String url){
		if(url.startsWith("/") || url.startsWith("\\"))
			return new URLPermission(url);
		else return null;
	}
}
