/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.serverside.jaas.auth;

import java.security.Permission;
/**
 * 
 * This Class is used for implementation of Authorization.
 *
 */
public class PermissionFactory {
/**
 * The method creates an instance of PermissionFactory class using its constructor.
 * @return   An object of class 'PermissionFactory'.
 */
	public static PermissionFactory getInstance(){
		return new PermissionFactory();
	}
	/**
	 * This method is used to Authorize the URL used for Logging in.
	 * @param url The URL used for Logging in.
	 * @return    Object of Class java.Security.Permission
	 */
	public Permission getPermission(String url){
	    System.out.println("Into get permission for url "+url);
		if(url.startsWith("/") || url.startsWith("\\"))
			return new URLPermission(url);
		else return null;
	}
}
