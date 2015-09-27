/*
 * Created on Tue Mar 24 16:17:22 IST 2009
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
public class InternalConfirmDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for InternalConfirm Screen, InternalConfirm_INIT State and INIT Event. 
	 */
	public void preInternalConfirmInternalConfirm_INITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject)
	{
		EBWLogger.trace(this, "Starting preInternalConfirmInternalConfirm_INITINIT"); 
		InternalConfirmForm objInternalConfirm=(InternalConfirmForm) objSourceForm;
		EBWLogger.trace(this, "Finished preInternalConfirmInternalConfirm_INITINIT"); 
	}

	/**
	 * Method for setting the Confirmation screen attributes...
	 */
	public void postInternalConfirmInternalConfirm_INITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postInternalConfirmInternalConfirm_INITINIT");
		InternalConfirmForm objInternalConfirm=(InternalConfirmForm) objSourceForm;
		EBWLogger.trace(this, "Finished postInternalConfirmInternalConfirm_INITINIT"); 
	}
}