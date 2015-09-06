package com.tcs.bancs.channels.saml.bean;

import java.util.Hashtable;

import javax.naming.NamingException;

public class PostSAMLAccountRegisterBeanUtil {

	private static Object lookupHome(java.util.Hashtable environment,
			String jndiName, Class narrowTo)
			throws javax.naming.NamingException {
		// Obtain initial context
		javax.naming.InitialContext initialContext = new javax.naming.InitialContext(
				environment);
		try {
			Object objRef = initialContext.lookup(jndiName);
			// only narrow if necessary
			if (java.rmi.Remote.class.isAssignableFrom(narrowTo))
				return javax.rmi.PortableRemoteObject.narrow(objRef, narrowTo);
			else
				return objRef;
		} finally {
			initialContext.close();
		}
	}

	public static PostSAMLAccountRegisterHome getHome(Hashtable environment, String jndiName)
	throws NamingException
	{
		return (PostSAMLAccountRegisterHome)lookupHome(environment, jndiName, PostSAMLAccountRegisterHome.class);
	}


}