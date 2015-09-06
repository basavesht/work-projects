/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.serverside.jaas.auth;

import java.security.Permission;
/**
 * 
 * This class  
 *
 */
public class URLPermission extends Permission{
	/**
	 * This method is used to check if the arguement is of type 'URLPermission' or not. It also evaluates
	 * the permission name whether it is valid or not.
	 * @param permission An object of java.Security.Permission class
	 * @return   Boolean value that indicates the validation status of the Permission object passed to this method
	 */
	public boolean implies(Permission permission) {
	    System.out.println("permission instance :"+(permission instanceof URLPermission));
		if(!(permission instanceof URLPermission))
            return false;        
        String thisName = this.getName();
        String permName = permission.getName();
        System.out.println("permission name is :"+permName);
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
	 * This method compares whether the arguement is of type 'URLPermission' or not and also checks whether the name attribute
	 * in the object is same as the name attribute of this Class.
	 * @param obj An object of class 'Object'
	 */
	public boolean equals(Object obj) {
		if((obj instanceof URLPermission) && ((URLPermission)obj).getName().equals(this.getName()))
            return true;
        else 
            return false;

	}
/**
 * Returns a String
 */
	public String getActions() {
		return "";
	}
	/**
	 * This method returns the name attribute of 'Permission' class in form of 'hashCode' object.
	 */
	public int hashCode() {
		return this.getName().hashCode(); 
	}
	
	/**
	 * Class Constructor
	 * @param arg0 The URL requesting authorization 
	 */
	public URLPermission(String arg0) {
		super(arg0);
	}
	
	public static void main(String args[]){
		System.out.println(new URLPermission("Test").hashCode());
		
	}
	}
