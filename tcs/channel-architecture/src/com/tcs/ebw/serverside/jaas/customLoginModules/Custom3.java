package com.tcs.ebw.serverside.jaas.customLoginModules;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;

/**
 * @author 231259
 * @year 2008
 */
public class Custom3 implements CustomLoginModule {

	public String login(CallbackHandler ebwch) {
		try {
			Callback callbacks[] = new Callback[3];
			CallbackHandler callbackHandler = ebwch;
			callbackHandler.handle(callbacks);
			String username = new String(((NameCallback) callbacks[0]).getName());
			String password = new String(((PasswordCallback) callbacks[1]).getPassword());
			
			System.out.println("username:"+username);
			System.out.println("password:"+password);
			
			if(username.equalsIgnoreCase(password))
				return "success_login";
			else 
				return "failure_login";
			
		} catch (IOException io) {
			io.printStackTrace();
			return null;
		} catch (UnsupportedCallbackException uncall) {
			uncall.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
