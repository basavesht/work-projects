package com.tcs.bancs.channels.integration;


import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.tcs.bancs.channels.saml.bean.PostSAMLAccountRegister;
import com.tcs.bancs.channels.saml.bean.PostSAMLAccountRegisterBeanUtil;
import com.tcs.bancs.channels.saml.bean.PostSAMLAccountRegisterHome;
import com.tcs.bancs.ui.filters.security.saml.PostCallInterface;
import com.tcs.bancs.ui.filters.security.saml.SAMLConfiguration;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

public class SAMLPostHook implements PostCallInterface {

	private static final Logger logger = Logger.getLogger(MMSessionUtils.class);

	SAMLPostHookConfig hookConfig = null;
	public boolean call(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object context, SAMLConfiguration samlConfig) throws ConfigXMLParsingException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("call(HttpServletRequest, HttpServletResponse, Object) - start"); //$NON-NLS-1$
		}

		try {
			hookConfig.checkNew();
		} catch (FileNotFoundException e) {
			throw new ConfigXMLParsingException(e);
		} catch (ConfigXMLParsingException e) {
			throw new ConfigXMLParsingException(e);
		}

		if ( context != null && context instanceof MMContext )
		{
			MMContext mmcontext = (MMContext)context;
			HttpSession session = httpRequest.getSession();
			String base64EncodedSAMLToken = httpRequest.getParameter(samlConfig.getPostAttrSAML());
			registerAccountDetails(session, base64EncodedSAMLToken);
			String username = mmcontext.getLoginId();
			Subject subject = new Subject();
			UserPrincipal userPrincipal = new UserPrincipal(username);
			userPrincipal.setContextData(mmcontext);
			System.out.println("generated session id is"+session.getId());
			userPrincipal.setSessionId(session.getId());
			System.out.println("generated secure id is"+session.getAttribute("MMSecureId").toString());
			userPrincipal.setSecurityTokenId(session.getAttribute("MMSecureId").toString());
			System.out.println("generated uuid is"+mmcontext.getUuid());
			userPrincipal.setUuid(mmcontext.getUuid());
			session.setAttribute("MMUUID", mmcontext.getUuid());
			System.out.println("UserPrincipal details.......");
			System.out.println(userPrincipal.toString());
			subject.getPrincipals().add(userPrincipal);
			session.setAttribute("subject", subject);
		}

		if( httpRequest.getParameter("action") == null)	{

			String getParameters = getStateValuesFromSession(httpRequest);

			if( getParameters != null ) {
				setParameters(httpRequest, getParameters);
			}	
		}
		if (logger.isDebugEnabled()) {
			logger.debug("call(HttpServletRequest, HttpServletResponse, Object) - end"); //$NON-NLS-1$
		}
		return true;
	}

	private void registerAccountDetails(HttpSession MMSession, String base64EncodedSAMLToken)
	throws ConfigXMLParsingException
	{
		
		String providerFactory = hookConfig.getEjbProvider();
		String providerURL = hookConfig.getEjbProviderUrl();
		String ejbJndi = hookConfig.getEjbJndi();
		Hashtable systemEnvt = new Hashtable();
		
		systemEnvt.put("java.naming.factory.initial", providerFactory);
		systemEnvt.put("java.naming.provider.url", providerURL);
		System.out.println("systemEnvt dtls..providerFactory...providerURL"+providerFactory+"..."+providerURL);
		try
		{
			System.out.println("Home Details..ejbJndi.."+ejbJndi);
			PostSAMLAccountRegisterHome home = PostSAMLAccountRegisterBeanUtil.getHome(systemEnvt, ejbJndi);
			PostSAMLAccountRegister bean = home.create();
			String MMSECUREID = bean.createAccountMapping(MMSession.getId(), base64EncodedSAMLToken);
			MMSession.setAttribute("MMSecureId", MMSECUREID); // TODO: This MMSecure Id should be used in subsequent Business EJB calls
			
			
		}
		catch(NamingException e)
		{
			throw new ConfigXMLParsingException(e);
		}
		catch(RemoteException e)
		{
			throw new ConfigXMLParsingException(e);
		}
		catch(CreateException e)
		{
			throw new ConfigXMLParsingException(e);
		}
	}


	private void setParameters(HttpServletRequest httpRequest, String getParameters) 
	{
		String decodedParams = null;
		try {
			decodedParams = URLDecoder.decode(getParameters, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Could not set parameter", e);
		}
		if ( decodedParams != null ){
			String params[] = decodedParams.split("&");
			for(String param : params)
			{
				String nameValuePair[] = param.split("=");
				if ( nameValuePair.length == 2 )
				{
					setParameter(httpRequest, nameValuePair[0], nameValuePair[1]);
				}
			}
		}

	}

	private void setParameter(HttpServletRequest httpRequest, String name, String value) {
		httpRequest.getParameterMap().put(name, new String[]{value});		
	}


	private String getStateValuesFromSession(HttpServletRequest httpRequest)
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getStateValuesFromSession(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String page = getPageName(httpRequest);
		if ( page != null )
		{
			HttpSession session = httpRequest.getSession(false);
			if ( session != null )
			{
				String returnString = MMSessionUtils.getParams(page, session);
				if (logger.isDebugEnabled()) {
					logger.debug("getStateValuesFromSession(HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
			else
			{
				String returnString = MMSessionUtils.getDefaultParams(page);
				if (logger.isDebugEnabled()) {
					logger.debug("getStateValuesFromSession(HttpServletRequest) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
		}
		else
		{
			if (logger.isDebugEnabled()) {
				logger.debug("getStateValuesFromSession(HttpServletRequest) - end"); //$NON-NLS-1$
			}
			return null;
		}
	}

	private String getPageName(HttpServletRequest httpRequest) 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getPageName(HttpServletRequest) - start"); //$NON-NLS-1$
		}

		String uri = httpRequest.getRequestURI();

		if (logger.isDebugEnabled()) {
			logger.debug("URI : " + uri); //$NON-NLS-1$
		}

		String page = hookConfig.getPageNameMapping().get(uri);

		if (logger.isDebugEnabled()) {
			logger.debug("PAGE : " + page); //$NON-NLS-1$
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getPageName(HttpServletRequest) - end"); //$NON-NLS-1$
		}
		return page;
	}

	public void init(String configXML) throws ConfigXMLParsingException {
		if (logger.isDebugEnabled()) {
			logger.debug("init(String) - start"); //$NON-NLS-1$
		}

		hookConfig = SAMLPostHookConfig.getInstance();

		try {
			hookConfig.parse(configXML);
		} catch (FileNotFoundException e) {
			logger.error("init(String)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException(e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init(String) - end"); //$NON-NLS-1$
		}


	}
}
