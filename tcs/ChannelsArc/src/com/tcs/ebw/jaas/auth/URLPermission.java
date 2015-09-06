/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.auth;

import java.security.Permission;

/**
 * After successful authentication, a user Principal can be associated with
 * a particular Subject to augment that Subject with an additional identity.
 * Authorization decisions can then be based upon the Principals that are associated with a Subject.
 *  
 * @author TCS
 * @version 1.0
 *
 */
public class URLPermission extends Permission{
	
	/**
	 * verify if this permission implies another URLPermission.
	 * @param permission
	 * @return  true if implies, false otherwise
	 */
	public boolean implies(Permission permission) {
		if(!(permission instanceof URLPermission))
            return false;        
        String thisName = this.getName();
        String permName = permission.getName();
        if(this.getName().equals("*")) 
            return true;
        if(thisName.endsWith("*") && permName.startsWith(thisName.substring(0, thisName.lastIndexOf("*")))) {
            return true;
        }        
        if(thisName.equals(permName))
            return true;
        return false;  
	}
	
	/**
	 *
	 * @param obj The object to compare this permission against.  
	 * @return boolean  true if they are equal; false otherwise.
	 */
	
	public boolean equals(Object obj) {
		if((obj instanceof URLPermission) && ((URLPermission)obj).getName().equals(this.getName()))
            return true;
        else 
            return false;

	}
	
	/**
	 * @return permitted actions.
	 */
	public String getActions() {
		return "";
	}
	
	/**
	 * methode used to accelerate the comparation process: useful when hashcode return different int.
	 * @return hashcode.
	 */
	public int hashCode() {
		return this.getName().hashCode(); 
	}
		
	/**
	 * Creates a new instance of URLPermission.
	 * @param arg0  
	 */
	public URLPermission(String arg0) {
		super(arg0);
	}
	
	}
