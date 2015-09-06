package com.tcs.ebw.serverside.jaas.customLoginModules;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;


/**
 * @author 231259
 * @year 2008
 */
public class Custom4 implements CustomLoginModule {

	public String login(CallbackHandler ebwch) {
		return "success_login";
	}

	public boolean abort() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean commit() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

	public void initialize(Subject arg0, CallbackHandler arg1, Map arg2,
			Map arg3) {
		// TODO Auto-generated method stub

	}

	public boolean login() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean logout() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

}
