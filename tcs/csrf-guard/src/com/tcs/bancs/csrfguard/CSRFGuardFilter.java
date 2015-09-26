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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class CSRFGuardFilter implements Filter
{
	private final static String PARAM_CONFIG_FILE = "config";
	
	private ServletContext context = null;
	
	private CSRFGuard guard = null;
	
	public void init(FilterConfig config)
	{
		this.context = config.getServletContext();
		
		String configFile = config.getInitParameter(PARAM_CONFIG_FILE);
		configFile = context.getRealPath(configFile);
		
		this.guard = new CSRFGuard(context, configFile);
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		if(request instanceof HttpServletRequest && response instanceof HttpServletResponse)
		{
			HttpServletRequest hRequest = (HttpServletRequest)request;
			HttpServletResponse hResponse = (HttpServletResponse)response;
			
			context.log("Verifying for CSRF Attack on request");
			guard.doRequestEnforce(hRequest, hResponse);
			
			chain.doFilter(hRequest, hResponse);
		}
	}
	
	public void destroy()
	{
		
	}
}
