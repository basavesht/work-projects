package com.tcs.ebw.serverside.jaas.customLoginModules;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.spi.LoginModule;

/**
 * @author 231259
 * @year 2008
 */
public interface CustomLoginModule extends LoginModule {	
	public String login(CallbackHandler ebwch);
}
