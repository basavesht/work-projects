/*
 * Created on Tue Jul 28 15:25:53 IST 2009
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
public class ExtAccsInformationDelegate extends EbwBusinessDelegate {

	ExtAccsInformationForm objExtAccsInformationForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of ExtAccsInformationDelegate class. 
	 */ 
	public ExtAccsInformationDelegate (ExtAccsInformationForm objExtAccsInformationForm, HashMap objUserSessionObject) {
		this.objExtAccsInformationForm = objExtAccsInformationForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objExtAccsInformationForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of ExtAccsInformationDelegate class. 
	 */ 
	public ExtAccsInformationDelegate (ExtAccsInformationForm objExtAccsInformationForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objExtAccsInformationForm = objExtAccsInformationForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objExtAccsInformationForm.getRetainData(), "~"); 
	}

	/**
	 * Method for ExtAccsInformation_INIT State and INIT Event. 
	 */
	public ExtAccsInformationForm extAccsInformation_INIT_iNIT() throws Exception {
		EBWLogger.trace(this, "Starting ExtAccsInformation_INITINIT()"); 
		Object objOutput = null;

		Object objParams[]=null;
		Class clsParamTypes[]=null;

		//Busisness Delegate hook object used for Pre and post population.
		com.tcs.ebw.payments.businessdelegate.ExtAccsInformationDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ExtAccsInformationDelegateHook();

		// Prepopulate function call.
		objBusinessDelegateHook.preExtAccsInformationExtAccsInformation_INITINIT(objExtAccsInformationForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		objExtAccsInformationForm.setState("ExtAccsInformation_INIT");

		// Postpopulate function call.
		objBusinessDelegateHook.postExtAccsInformationExtAccsInformation_INITINIT(objExtAccsInformationForm, objExtAccsInformationForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 


		EBWLogger.trace(this, "Return bean object : " + objExtAccsInformationForm);
		EBWLogger.trace(this, "Finished ExtAccsInformation_INITINIT()"); 
		return objExtAccsInformationForm;
	}

}
