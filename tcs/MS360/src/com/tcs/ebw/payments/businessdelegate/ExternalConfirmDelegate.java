/*
 * Created on Tue Mar 24 11:50:57 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;

import com.tcs.ebw.common.util.*;
import com.tcs.ebw.businessdelegate.EbwBusinessDelegate;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

import com.tcs.ebw.payments.formbean.*;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ExternalConfirmDelegate extends EbwBusinessDelegate {

	ExternalConfirmForm objExternalConfirmForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of ExternalConfirmDelegate class. 
	 */ 
	public ExternalConfirmDelegate (ExternalConfirmForm objExternalConfirmForm, HashMap objUserSessionObject) {
		this.objExternalConfirmForm = objExternalConfirmForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objExternalConfirmForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of ExternalConfirmDelegate class. 
	 */ 
	public ExternalConfirmDelegate (ExternalConfirmForm objExternalConfirmForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objExternalConfirmForm = objExternalConfirmForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objExternalConfirmForm.getRetainData(), "~"); 
	}

	/**
	 * Method for ExternalConfirm_INIT State and INIT Event. 
	 */
	public ExternalConfirmForm externalConfirm_INIT_iNIT() throws Exception {
		EBWLogger.trace(this, "Starting ExternalConfirm_INITINIT()"); 
		Object objOutput = null;

		Object objParams[]=null;
		Class clsParamTypes[]=null;

		//Busisness Delegate hook object used for Pre and post population.
		com.tcs.ebw.payments.businessdelegate.ExternalConfirmDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ExternalConfirmDelegateHook();

		// Prepopulate function call.
		objBusinessDelegateHook.preExternalConfirmExternalConfirm_INITINIT(objExternalConfirmForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		objExternalConfirmForm.setState("ExternalConfirm_INIT");

		// Postpopulate function call.
		objBusinessDelegateHook.postExternalConfirmExternalConfirm_INITINIT(objExternalConfirmForm, objExternalConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 


		EBWLogger.trace(this, "Return bean object : " + objExternalConfirmForm);
		EBWLogger.trace(this, "Finished ExternalConfirm_INITINIT()"); 
		return objExternalConfirmForm;
	}

}
