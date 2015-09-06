/*
 * Created on Tue Mar 24 11:05:07 IST 2009
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
public class ExternalConfirmDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for ExternalConfirm Screen, ExternalConfirm_INIT State and INIT Event. 
	 */
	public void preExternalConfirmExternalConfirm_INITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject)
	{
		EBWLogger.trace(this, "Starting preExternalConfirmExternalConfirm_INITINIT"); 
		ExternalConfirmForm objExternalConfirm=(ExternalConfirmForm) objSourceForm;
		EBWLogger.trace(this, "Finished preExternalConfirmExternalConfirm_INITINIT"); 
	}
	
	/**
	 * Method for setting the Confirmation screen attributes...
	 */
	public void postExternalConfirmExternalConfirm_INITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postExternalConfirmExternalConfirm_INITINIT");
		ExternalConfirmForm objExternalConfirm=(ExternalConfirmForm) objSourceForm;
		EBWLogger.trace(this, "Finished postExternalConfirmExternalConfirm_INITINIT"); 
	}

}