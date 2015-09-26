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

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tcs.bancs.csrfguard.utils.TokenGenerator;

public final class CSRFGuard
{
	private ServletContext context = null;

	private CSRFConfig config = null;

	private CSRFGuard()
	{

	}

	public CSRFGuard(ServletContext context, String configFile)
	{
		this();

		this.context = context;
		this.config = new CSRFConfig(context, configFile);
	}

	public void doRequestEnforce(HttpServletRequest request, HttpServletResponse response)
	{

		HttpSession session = request.getSession(false);
		if ( session == null ){
			context.log("There is no session: not checking request");
			return;
		}

		String requestedURI = request.getRequestURI();

		context.log("Requested URI :" +  requestedURI);

		List<SecuredURI> securedURIs = getMatch(config.getSecuredURIs(),requestedURI);

		boolean check = false;
		if ( config.getStateParameter() != null ){
			context.log("State Parameter : " +  config.getStateParameter());

			String currentState = request.getParameter(config.getStateParameter());

			context.log("Requested page state : " + currentState);

			for(SecuredURI securedURI:securedURIs){
				context.log(String.format("   > Matching '%s' with '%s'",securedURI.getStateParameter(),currentState));
				if ( securedURI.getStateParameter()!=null && currentState!= null && securedURI.getStateParameter().equals(currentState)){
					context.log("requested URI is one of the secured URI configured and page state mathed: Checking for possible CSRF Attack");
					check = true;
					break;
				}
			}
			if (!check){
				context.log("None of the configured state matched with current page state : NOT Checking for possible CSRF Attack");
			}
		}
		else {
			if ( securedURIs.size() > 0 ){
				context.log("StateParameter not spceified in config && requested URI is one of the zsecured URI configured : Checking for possible CSRF Attack");
				check = true;
			}else{
				context.log("StateParameter not spceified in config && requested URI is NOT one of the zsecured URI configured : NOT Checking for possible CSRF Attack");
				check = false;
			}
		}
		if ( check )
		{
			String reqToken = request.getParameter(config.getTokenName());
			context.log("Token from request : " + reqToken);
			String sesToken = (String)session.getAttribute(config.getTokenName());
			context.log("Token from session : " + sesToken);

			if ( reqToken == null || sesToken == null || !reqToken.equals(sesToken)){
				if ( reqToken == null){
					context.log("CSRF Token is null from request");				
				}
				if ( sesToken == null){
					context.log("CSRF Token is null from session");				
				}
				context.log("CSRF Token verification failed : could be " + 
						"\n\t 1. possible CSRF Attack from " + request.getRemoteAddr() +
						"\n\t 2. JSP code dose not have CSRF tag to generate token in form for '" + requestedURI + "', if request token is null" +			
				"\n\t 3. User submitted twice and this is 2nd call, if session token is null" );			
				session.invalidate();
			}
			else
			{
				context.log("Token verified successfully");
				session.removeAttribute(config.getTokenName());
				context.log("Token removed from session");

				String reqSessionId = request.getParameter(config.getSessionIdName());
				context.log("Session ID from request : " + reqSessionId);
				String sessionId = session.getId();
				context.log("Current Session ID: " + sessionId);

				if ( reqSessionId == null){
					context.log("Session ID is null from request");
					context.log("Session ID verification failed : could be " + 
							"\n\t 1. possible CSRF Attack from " + request.getRemoteAddr() +
							"\n\t 2. JSP code dose not have CSRF tag to generate session id in form for '" + requestedURI + "'");			
					session.invalidate();
				}else if ( !reqSessionId.equals(sessionId)){
					context.log("Session ID from request does not match with current session ID");
					context.log("Session ID verification failed : could be " + 
							"\n\t 1. possible CSRF Attack from " + request.getRemoteAddr());
					session.invalidate();

				}else{
					context.log("Session ID verified successfully");
				}
			}
		}
	}

	private List<SecuredURI> getMatch(List<SecuredURI> securedURIs,
			String requestedURI) {
		List<SecuredURI> list = new ArrayList<SecuredURI>();
		for ( SecuredURI uri:securedURIs){
			context.log("\t Matching " + uri.getURI() + " with " + requestedURI);
			if ( requestedURI.equals(uri.getURI())){
				list.add(uri);
			}
		}
		return list;
	}


	public String getFormField(String token, String sessionID) {
		String html = "<input type='hidden' name='" + config.getTokenName() + "' value='" + token + "'>";
		html += "<input type='hidden' id='" + config.getSessionIdName() + "' name='" + config.getSessionIdName() + "' value='" + "dummyvalue"  + "'>";
		String cookie_name = config.getSessionCookieName(); //"JSESSIONID";
		String prefix_length = config.getSessionValuePrefixLength(); //"4";
		html += "<script language='JavaScript'>var cm=document.cookie.match(/" + cookie_name + "=[^;]+/);var c=''; if (cm) c=cm[0];var c_start="+ prefix_length + " + " + cookie_name.length() + "+1;var c_end=c.indexOf(':');if (c_end==-1) c_end=c.length;var cv = unescape(c.substring(c_start,c_end));document.getElementById('" + config.getSessionIdName() + "').value=cv;</script>";
		context.log("Generated HTML: " + html);
		return html;
	}

	public String generateToken() throws NoSuchAlgorithmException {
		String token = TokenGenerator.generateCSRFToken(config.getPRNG(), config.getTokenLength());
		context.log("Generated token: " + token);
		return token;
	}

	public void setToken(HttpSession session, String token) {
		if ( session != null){
			session.setAttribute(config.getTokenName(), token);
			context.log("Session set : " + config.getTokenName() + " : " + token); 
		}
	}
}
