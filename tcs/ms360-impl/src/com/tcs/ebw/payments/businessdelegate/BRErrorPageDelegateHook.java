/*
 * Created on Sun Jun 07 11:21:31 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;

import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.businessdelegate.BusinessDelegateHook;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

import com.tcs.ebw.common.util.EBWLogger;

import com.tcs.ebw.payments.formbean.*;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class BRErrorPageDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for BRErrorPage Screen, BRErrorPage_INIT State and INIT Event. 
	 * @throws Exception 
	 */
	public void preBRErrorPageBRErrorPage_INITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preBRErrorPageBRErrorPage_INITINIT"); 
		try 
		{
			BRErrorPageForm objBRErrorPage=(BRErrorPageForm) objSourceForm;
			String brMessages = (String)objUserPrincipal.getBrErrorMessages();
			if(brMessages!=null && !brMessages.trim().equalsIgnoreCase(""))
			{
				objBRErrorPage.setBrErrorMessages(brMessages);
				objBRErrorPage.setPostNavigationId(objUserPrincipal.getPostNavigationPageId());
			}
			objUserPrincipal.setBrErrorMessages(null); // Remove the same from the session ....
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preBRErrorPageBRErrorPage_INITINIT"); 
	}

	/**
	 * Post delegate method..
	 */
	public void postBRErrorPageBRErrorPage_INITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject)
	{
		EBWLogger.trace(this, "Starting postBRErrorPageBRErrorPage_INITINIT");
		BRErrorPageForm objBRErrorPage=(BRErrorPageForm) objSourceForm;
		EBWLogger.trace(this, "Finished postBRErrorPageBRErrorPage_INITINIT"); 
	}

}