/*
 * Created on Tue Jun 09 10:07:36 IST 2009
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
public class ConfirmPopupApproveDelegate extends EbwBusinessDelegate {

	ConfirmPopupApproveForm objConfirmPopupApproveForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of ConfirmPopupApproveDelegate class. 
	 */ 
	public ConfirmPopupApproveDelegate (ConfirmPopupApproveForm objConfirmPopupApproveForm, HashMap objUserSessionObject) {
		this.objConfirmPopupApproveForm = objConfirmPopupApproveForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objConfirmPopupApproveForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of ConfirmPopupApproveDelegate class. 
	 */ 
	public ConfirmPopupApproveDelegate (ConfirmPopupApproveForm objConfirmPopupApproveForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objConfirmPopupApproveForm = objConfirmPopupApproveForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objConfirmPopupApproveForm.getRetainData(), "~"); 
	}

	/**
	 * Method for ConfirmPopupApprove_INIT State and INIT Event. 
	 */
	public ConfirmPopupApproveForm confirmPopupApprove_INIT_iNIT() throws Exception 
	{
		EBWLogger.trace(this, "Starting ConfirmPopupApprove_INITINIT()"); 
		EBWLogger.trace(this, "Service name       : submitApprove"); 

		Object objOutput = null;
		String strStatement = "";

		//Mapping payment attributes...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();

		Object objTOParam[] = {objPaymentDetails,objMSUserDetails};

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

		//Pre business delegate hook population...
		com.tcs.ebw.payments.businessdelegate.ConfirmPopupApproveDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ConfirmPopupApproveDelegateHook();
		objBusinessDelegateHook.preConfirmPopupApproveConfirmPopupApprove_INITINIT(objConfirmPopupApproveForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("submitApprove");
		objOutput = objService.execute(clsParamTypes, objParams);
		objConfirmPopupApproveForm.setState("ConfirmPopupApprove_INIT");

		//Post business delegate hook population...
		objBusinessDelegateHook.postConfirmPopupApproveConfirmPopupApprove_INITINIT(objConfirmPopupApproveForm, objConfirmPopupApproveForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objConfirmPopupApproveForm);
		EBWLogger.trace(this, "Finished ConfirmPopupApprove_INITINIT()"); 
		return objConfirmPopupApproveForm;
	}

	/**
	 * Method for ConfirmPopupApprove_INIT State and confirmBtn Event. 
	 */
	public PostApproveConfirmForm confirmPopupApprove_INIT_confirmBtn() throws Exception 
	{
		EBWLogger.trace(this, "Starting ConfirmPopupApprove_INITconfirmBtn()"); 
		EBWLogger.trace(this, "Service name : confirmApprove "); 
		PostApproveConfirmForm objPostApproveConfirmForm = new PostApproveConfirmForm();

		Object objOutput = null;
		String strStatement = "";

		//Mapping payment attributes...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();

		Object objTOParam[] = {objPaymentDetails,objMSUserDetails};

		objUserSessionObject.put("isDiffScreen","false");
		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

		//Pre business delegate hook population...
		com.tcs.ebw.payments.businessdelegate.ConfirmPopupApproveDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ConfirmPopupApproveDelegateHook();
		objBusinessDelegateHook.preConfirmPopupApproveConfirmPopupApprove_INITconfirmBtn(objConfirmPopupApproveForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("confirmApprove");
		objOutput = objService.execute(clsParamTypes, objParams);
		objPostApproveConfirmForm.setState("PostApproveConfirm_INIT");

		//Post business delegate hook population...
		objBusinessDelegateHook.postConfirmPopupApproveConfirmPopupApprove_INITconfirmBtn(objConfirmPopupApproveForm, objConfirmPopupApproveForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objPostApproveConfirmForm);
		EBWLogger.trace(this, "Finished ConfirmPopupApprove_INITconfirmBtn()"); 
		return objPostApproveConfirmForm;
	}

}
