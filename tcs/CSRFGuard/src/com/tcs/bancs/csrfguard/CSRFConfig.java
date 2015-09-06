/**
 * OWASP CSRFGuard
 * 
 * This file is part of the Open Web Application Security Project (OWASP)
 * Copyright (c) 2007 - The OWASP Foundation
 * 
 * The CSRFGuard is published by OWASP under the LGPL. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 * 
 * @author <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created 2007
 */

package com.tcs.bancs.csrfguard;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

public final class CSRFConfig
{
	private ServletContext context = null;
	
	private boolean debug = false;
	
	private String stateParameter = null;
	
	private List<SecuredURI> securedURIs = new ArrayList<SecuredURI>();
	
	private String tokenName = null;
	private String sessionIdName = null;
	
	private String PRNG = null;
	
	private int tokenLength = 0;

	private String sessionCookieName = null;
	
	private String sessionValuePrefixLength = null;
	
	private CSRFConfig()
	{
		
	}
	
	protected CSRFConfig(ServletContext context, String configFile)
	{
		this();
		
		this.context = context;
		
		loadConfig(configFile);
		
		printConfig();
	}
	
	public boolean isDebugMode()
	{
		return debug;
	}
	
	public void isDebugMode(boolean debug)
	{
		this.debug = debug;
	}
	
	
	public void printConfig()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("[CSRFGuard] the following properties were loaded into CSRFGuard\n");
		sb.append("\t Debug:                     " + debug + "\n");
		sb.append("\t StateParameter:            " + stateParameter + "\n");
		sb.append("\t TokenName:                 " + tokenName + "\n");
		sb.append("\t sessionIdName:             " + sessionIdName + "\n");
		sb.append("\t sessionCookieName:         " + sessionCookieName + "\n");
		sb.append("\t sessionValuePrefixLength:  " + sessionValuePrefixLength + "\n");
		sb.append("\t PRNG:                      " + PRNG + "\n");
		sb.append("\t TokenLength:               " + tokenLength + "\n");
		sb.append("\t Secured URI Count:         " + securedURIs.size() + "\n");
		
		for(int i=0; i<securedURIs.size(); i++)
		{
			sb.append("\t\tSecuredURI(" + i + ")\t" + securedURIs.get(i).getURI() + ";" + securedURIs.get(i).getStateParameter() + "\n");
		}


		context.log(sb.toString());
	}
	
	private void loadConfig(String s)
	{
		Properties p = new Properties();
		InputStream is = null;
		
		try
		{
			is = new FileInputStream(s);
			p.load(is);
			
			loadConfig(p);
		}
		catch (IOException ioe)
		{
			context.log(ioe.getMessage(), ioe);
		}
		finally
		{
			if(is != null)
			{
				close(is);
			}
		}
	}
	
	private void close(InputStream is)
	{
		try
		{
			is.close();
		}
		catch (IOException ioe)
		{
			context.log(ioe.getMessage(), ioe);
		}
	}
	
	private void loadConfig(Properties p)
	{
		this.debug = getDebug(p);
		this.stateParameter = p.getProperty("com.tcs.bancs.csrfguard.StateParameter").trim();
		loadSecuredURIs(p);
		this.tokenName = p.getProperty("com.tcs.bancs.csrfguard.TokenName").trim();
		this.sessionIdName = p.getProperty("com.tcs.bancs.csrfguard.SessionIdName").trim();
		this.sessionCookieName = p.getProperty("com.tcs.bancs.csrfguard.SessionCookieName").trim();
		this.sessionValuePrefixLength = p.getProperty("com.tcs.bancs.csrfguard.SessionValuePrefixLength").trim();
		this.PRNG = p.getProperty("com.tcs.bancs.csrfguard.PRNG").trim();
		this.tokenLength = Integer.parseInt(p.getProperty("com.tcs.bancs.csrfguard.TokenLength").trim());
	}
	
	public String getSessionCookieName() {
		return sessionCookieName;
	}

	public String getSessionValuePrefixLength() {
		return sessionValuePrefixLength;
	}

	private void loadSecuredURIs(Properties p) {
		final String prefix = "com.tcs.bancs.csrfguard.SecuredURI.";
		Enumeration<Object> e = p.keys();

		while(e.hasMoreElements())
		{
			String s = (String)e.nextElement();
			if ( s.startsWith(prefix)){
				String securedURIString = p.getProperty(s);
				String[] values = securedURIString.split(";");
				String uri = values[0];
				String state = null;
				if ( values.length>1){
					state=values[1];
				}
				securedURIs.add(new SecuredURI(uri.trim(),state.trim()));
			}
		}
		
	}

	private boolean getDebug(Properties p)
	{
		return Boolean.parseBoolean(p.getProperty("com.tcs.bancs.csrfguard.Debug").trim());
	}
	

	public String getStateParameter() {
		return stateParameter;
	}

	public List<SecuredURI> getSecuredURIs() {
		return securedURIs;
	}

	public String getTokenName() {
		return tokenName;
	}

	public String getSessionIdName() {
		return sessionIdName;
	}

	public String getPRNG() {
		return PRNG;
	}

	public int getTokenLength() {
		return tokenLength;
	}

}
