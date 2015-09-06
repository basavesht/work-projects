package com.tcs.bancs.channels.saml.bean;

/**
 * Home interface for com.tcs.bancs.channels.saml.bean.PostSAMLAccountRegisterBean bean.
 */
public interface PostSAMLAccountRegisterHome extends javax.ejb.EJBHome {

	/* Default create */
	public com.tcs.bancs.channels.saml.bean.PostSAMLAccountRegister create()
			throws java.rmi.RemoteException, javax.ejb.CreateException;

}
