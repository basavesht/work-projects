package com.tcs.ebw.serverside.jaas.struts.actions;

import javax.servlet.http.HttpServletRequest;

import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

public interface EbwActionHookInterface 
{
	public boolean postLogon(HttpServletRequest request, UserPrincipal principal);
	public boolean postLogout(HttpServletRequest request, UserPrincipal principal);
}