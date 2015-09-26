package com.tcs.ebw.serverside.jaas.customLoginModules;

import com.tcs.ebw.exception.EbwException;

/**
 * @author 231259
 * @year 2008
 */
public class CustomLoginFactory {

	public static CustomLoginModule getCustomLoginImpl(String loginImpl) throws EbwException {
		CustomLoginModule customLogin = null;
		try {
			customLogin = (CustomLoginModule) Class.forName(loginImpl).newInstance();
		} catch (InstantiationException ise) {
			ise.printStackTrace();
			throw new EbwException(CustomLoginFactory.class, ise.getMessage());
		} catch(ClassNotFoundException cne) {
			cne.printStackTrace();
			throw new EbwException(CustomLoginFactory.class, cne.getMessage());
		} catch(IllegalAccessException iae) {
			iae.printStackTrace();
			throw new EbwException(CustomLoginFactory.class, iae.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			throw new EbwException(CustomLoginFactory.class, e.getMessage());
		}
		return customLogin;
	}
}
