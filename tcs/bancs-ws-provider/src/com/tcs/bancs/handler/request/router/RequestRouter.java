package com.tcs.bancs.handler.request.router;

import java.util.Map;

import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;

public interface RequestRouter {
	public void init(String configFileName) throws ConfigXMLParsingException ;
	public Map<String,Object> routeRequest(Map<String,Object> request) throws ConfigXMLParsingException,RequestRouterException ;
}
