package com.tcs.bancs.helpers.ejb;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.tcs.bancs.gateway.iiop.ejb.BancsWorkflowRouter;

public class BancsWorkflowRouterBeanUtil 
{
	private static Object lookup(java.util.Hashtable environment,String jndiName) throws NamingException {
		Object objRef = null;
		InitialContext initialContext = new InitialContext(environment);
		try {
			objRef = initialContext.lookup(jndiName);
		} finally {
			initialContext.close();
		}
		return objRef;
	}

	public static BancsWorkflowRouter getBeanInstance(Hashtable environment, String jndiName) throws NamingException {
		BancsWorkflowRouter returnBancsWorkflowRouter = (BancsWorkflowRouter) lookup(environment, jndiName);
		return returnBancsWorkflowRouter;
	}
}