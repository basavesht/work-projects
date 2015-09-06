/*
 * Created on Mon Apr 20 10:43:41 IST 2009
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
public class InternalConfirmDelegate extends EbwBusinessDelegate {

	InternalConfirmForm objInternalConfirmForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of InternalConfirmDelegate class. 
	 */ 
	public InternalConfirmDelegate (InternalConfirmForm objInternalConfirmForm, HashMap objUserSessionObject) {
		this.objInternalConfirmForm = objInternalConfirmForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objInternalConfirmForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of InternalConfirmDelegate class. 
	 */ 
	public InternalConfirmDelegate (InternalConfirmForm objInternalConfirmForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objInternalConfirmForm = objInternalConfirmForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objInternalConfirmForm.getRetainData(), "~"); 
	}

	/**
	 * Method for InternalConfirm_INIT State and INIT Event. 
	 */
	public InternalConfirmForm internalConfirm_INIT_iNIT() throws Exception {
		EBWLogger.trace(this, "Starting InternalConfirm_INITINIT()"); 
		Object objOutput = null;

		Object objParams[]=null;
		Class clsParamTypes[]=null;

		//Busisness Delegate hook object used for Pre and post population.
		com.tcs.ebw.payments.businessdelegate.InternalConfirmDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.InternalConfirmDelegateHook();

		// Prepopulate function call.
		objBusinessDelegateHook.preInternalConfirmInternalConfirm_INITINIT(objInternalConfirmForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		objInternalConfirmForm.setState("InternalConfirm_INIT");

		// Postpopulate function call.
		objBusinessDelegateHook.postInternalConfirmInternalConfirm_INITINIT(objInternalConfirmForm, objInternalConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 


		EBWLogger.trace(this, "Return bean object : " + objInternalConfirmForm);
		EBWLogger.trace(this, "Finished InternalConfirm_INITINIT()"); 
		return objInternalConfirmForm;
	}

}
