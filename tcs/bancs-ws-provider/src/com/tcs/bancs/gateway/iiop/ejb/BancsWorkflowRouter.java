package com.tcs.bancs.gateway.iiop.ejb;

import java.util.Map;

import javax.ejb.Remote;

import com.tcs.bancs.helpers.ejb.BancsWorkflowRouterException;

@Remote
public interface BancsWorkflowRouter {
	public Map<String,Object> process(Map<String,Object> requestParams) throws BancsWorkflowRouterException ;
}
