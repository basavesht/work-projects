package com.tcs.bancs.channels.saml.bean;

import java.rmi.RemoteException;
import com.tcs.bancs.channels.integration.MMContext;

/**
 * Remote interface for com.tcs.bancs.channels.saml.bean.PostSAMLAccountRegisterBean bean.
 */
public interface PostSAMLAccountRegister extends javax.ejb.EJBObject {
	public String createAccountMapping(String MMSessionId, String base64EncodedSAMLToken) throws RemoteException;
}
