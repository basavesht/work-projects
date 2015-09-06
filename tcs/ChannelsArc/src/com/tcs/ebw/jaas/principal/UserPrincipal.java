/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.principal;

import java.security.Principal;

/**
 * After successful authentication, a user Principal can be associated with
 * a particular Subject to augment that Subject with an additional identity.
 * Authorization decisions can then be based upon the Principals that are associated with a Subject.
 *  
 * @author TCS
 * @version 1.0
 *
 */
public class UserPrincipal implements Principal, java.io.Serializable{
	 	private String name;
	 	
	 	/**
	 	 * Creates a principal. 
	 	 * @param name The principal's string name.
	 	 * @throws NullPointerException If the name is null.
	 	 */
	 	
	 	public UserPrincipal(String name) {
	 		if (name == null)
	 		    throw new NullPointerException("illegal null input");

	 		this.name = name;
	 	}
	 	 
		/**
		 * @return Returns the name.
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name The name to set.
		 */
		public void setName(String name) {
			this.name = name;
		}
}
