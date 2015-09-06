package com.tcs.ebw.serverside.jaas.auth;

/**
 * @author 231259
 * @year 2008
 */
public class JAASSocketLoginModule extends JAASAbstractAuth {
	public JAASSocketLoginModule(String username, String password){
		super(username,password);
	}
}
