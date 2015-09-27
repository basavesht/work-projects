package com.tcs.bancs.ui.filters.helpers;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HttpServletRequestWrapperImpl extends HttpServletRequestWrapper {

	private Map parameters = new  Hashtable();
	public HttpServletRequestWrapperImpl(HttpServletRequest request) {
		super(request);
		
		for( Object key:request.getParameterMap().keySet()) {
			
			parameters.put(key,request.getParameterMap().get(key));
			
		}
		
	}

	@Override
	public Map getParameterMap()
	{
		return parameters;
	}

	@Override
	public String getParameter(String key) {
		String[] values = getParameterValues(key);
		return (values != null && values.length > 0) ? values[0] : null;
	}

	@Override
	public Enumeration getParameterNames() {
		return Collections.enumeration(parameters.keySet());
	}

	@Override
	public String[] getParameterValues(String key) {
		return (String[])parameters.get(key);
	}


}
