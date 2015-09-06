/*
 * Created on Tue Jul 28 15:26:49 IST 2009
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
public class ExtAccsInformationDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for ExtAccsInformation Screen, ExtAccsInformation_INIT State and INIT Event. 
	 * @throws Exception 
	 */
	public void preExtAccsInformationExtAccsInformation_INITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception{
		EBWLogger.trace(this, "Starting preExtAccsInformationExtAccsInformation_INITINIT"); 
		ExtAccsInformationForm objExtAccsInformation=(ExtAccsInformationForm) objSourceForm;
		EBWLogger.trace(this, "Finished preExtAccsInformationExtAccsInformation_INITINIT"); 
	}
	public void postExtAccsInformationExtAccsInformation_INITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject){
		EBWLogger.trace(this, "Starting postExtAccsInformationExtAccsInformation_INITINIT");
		ExtAccsInformationForm objExtAccsInformation=(ExtAccsInformationForm) objSourceForm;
		EBWLogger.trace(this, "Finished postExtAccsInformationExtAccsInformation_INITINIT"); 
	}

}