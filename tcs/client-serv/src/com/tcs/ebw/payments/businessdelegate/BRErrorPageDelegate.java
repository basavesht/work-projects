/*
 * Created on Sun Jun 07 11:20:30 IST 2009
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
public class BRErrorPageDelegate extends EbwBusinessDelegate {

	BRErrorPageForm objBRErrorPageForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of BRErrorPageDelegate class. 
	 */ 
	public BRErrorPageDelegate (BRErrorPageForm objBRErrorPageForm, HashMap objUserSessionObject) {
		this.objBRErrorPageForm = objBRErrorPageForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objBRErrorPageForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of BRErrorPageDelegate class. 
	 */ 
	public BRErrorPageDelegate (BRErrorPageForm objBRErrorPageForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objBRErrorPageForm = objBRErrorPageForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objBRErrorPageForm.getRetainData(), "~"); 
	}

	/**
	 * Method for BRErrorPage_INIT State and INIT Event. 
	 */
	public BRErrorPageForm bRErrorPage_INIT_iNIT() throws Exception {
		EBWLogger.trace(this, "Starting BRErrorPage_INITINIT()"); 
		Object objOutput = null;

		Object objParams[]=null;
		Class clsParamTypes[]=null;

		//Busisness Delegate hook object used for Pre and post population.
		com.tcs.ebw.payments.businessdelegate.BRErrorPageDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.BRErrorPageDelegateHook();

		// Prepopulate function call.
		objBusinessDelegateHook.preBRErrorPageBRErrorPage_INITINIT(objBRErrorPageForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		objBRErrorPageForm.setState("BRErrorPage_INIT");

		// Postpopulate function call.
		objBusinessDelegateHook.postBRErrorPageBRErrorPage_INITINIT(objBRErrorPageForm, objBRErrorPageForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 


		EBWLogger.trace(this, "Return bean object : " + objBRErrorPageForm);
		EBWLogger.trace(this, "Finished BRErrorPage_INITINIT()"); 
		return objBRErrorPageForm;
	}

}
