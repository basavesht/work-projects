/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.auth;

/**
 *
 * This class is used to handle Authentication of users against information
 * stored in any DBMS or RDBMS. Information on type of Database, connection
 * information etc can be given in an external property file named ebw.properties.
 * The property file should be located in the same folder as this class file. 
 * 
 * @author  TCS
 * @version 1.0
 * 
 */
public class JAASDBMSAuthentication extends JAASAbstractAuth {
	
	/**
	 * Constructor of JAASDBMSAuthentication
	 * 
	 * @param username Username of the User
	 * @param password Password of the User
	 */
	public JAASDBMSAuthentication(String username, String password){
		super(username,password);
	}
}
