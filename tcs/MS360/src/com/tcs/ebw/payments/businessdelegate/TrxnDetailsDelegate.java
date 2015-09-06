/*
 * Created on Tue May 26 17:25:43 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;

import com.tcs.ebw.common.util.*;
import com.tcs.ebw.businessdelegate.EbwBusinessDelegate;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class TrxnDetailsDelegate extends EbwBusinessDelegate {

	TrxnDetailsForm objTrxnDetailsForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of TrxnDetailsDelegate class. 
	 */ 
	public TrxnDetailsDelegate (TrxnDetailsForm objTrxnDetailsForm, HashMap objUserSessionObject) {
		this.objTrxnDetailsForm = objTrxnDetailsForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objTrxnDetailsForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of TrxnDetailsDelegate class. 
	 */ 
	public TrxnDetailsDelegate (TrxnDetailsForm objTrxnDetailsForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objTrxnDetailsForm = objTrxnDetailsForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objTrxnDetailsForm.getRetainData(), "~"); 
	}

	/**
	 * Method for TrxnDetails_INIT State and INIT Event. 
	 */
	public TrxnDetailsForm trxnDetails_INIT_iNIT() throws Exception 
	{
		EBWLogger.trace(this, "Starting TrxnDetails_INITINIT()"); 
		EBWLogger.trace(this, "Service name       : getTransactionViewDetails"); 

		Object objOutput = null;
		String strStatement = "";

		//Mapping payment attributes...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();

		Object objTOParam[] = {objPaymentDetails,objMSUserDetails};

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

		//Pre Business delegate hook population...
		com.tcs.ebw.payments.businessdelegate.TrxnDetailsDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.TrxnDetailsDelegateHook();
		objBusinessDelegateHook.preTrxnDetailsTrxnDetails_INITINIT(objTrxnDetailsForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		IEBWService objService = EBWServiceFactory.create("getTransactionViewDetails");
		objOutput = objService.execute(clsParamTypes, objParams);
		objTrxnDetailsForm.setState("TrxnDetails_INIT");

		//Post Business delegate hook population...
		objBusinessDelegateHook.postTrxnDetailsTrxnDetails_INITINIT(objTrxnDetailsForm, objTrxnDetailsForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objTrxnDetailsForm);
		EBWLogger.trace(this, "Finished TrxnDetails_INITINIT()"); 
		return objTrxnDetailsForm;
	}

	/**
	 * Method for TrxnDetails_View State and INIT Event. 
	 */
	public TrxnDetailsForm trxnDetails_View_iNIT() throws Exception 
	{
		EBWLogger.trace(this, "Starting TrxnDetails_ViewINIT()"); 
		EBWLogger.trace(this, "Service name       : getTransactionViewDetails"); 

		Object objOutput = null;
		String strStatement = "";

		//Mapping payment attributes...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();

		Object objTOParam[] = {objPaymentDetails,objMSUserDetails};

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

		//Pre Business delegate hook population...
		com.tcs.ebw.payments.businessdelegate.TrxnDetailsDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.TrxnDetailsDelegateHook();
		objBusinessDelegateHook.preTrxnDetailsTrxnDetails_ViewINIT(objTrxnDetailsForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		IEBWService objService = EBWServiceFactory.create("getTransactionViewDetails");
		objOutput = objService.execute(clsParamTypes, objParams);
		objTrxnDetailsForm.setState("TrxnDetails_View");

		//Post business delegate hook population...
		objBusinessDelegateHook.postTrxnDetailsTrxnDetails_ViewINIT(objTrxnDetailsForm, objTrxnDetailsForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objTrxnDetailsForm);
		EBWLogger.trace(this, "Finished TrxnDetails_ViewINIT()"); 
		return objTrxnDetailsForm;
	}

}
