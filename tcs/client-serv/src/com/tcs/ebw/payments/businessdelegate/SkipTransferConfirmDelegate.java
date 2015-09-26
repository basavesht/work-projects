/*
 * Created on Tue May 26 15:46:15 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;

import com.tcs.ebw.common.context.EBWAppContext;
import com.tcs.ebw.common.util.*;
import com.tcs.ebw.businessdelegate.EbwBusinessDelegate;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class SkipTransferConfirmDelegate extends EbwBusinessDelegate {

	SkipTransferConfirmForm objSkipTransferConfirmForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of SkipTransferConfirmDelegate class. 
	 */ 
	public SkipTransferConfirmDelegate (SkipTransferConfirmForm objSkipTransferConfirmForm, HashMap objUserSessionObject) {
		this.objSkipTransferConfirmForm = objSkipTransferConfirmForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objSkipTransferConfirmForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of SkipTransferConfirmDelegate class. 
	 */ 
	public SkipTransferConfirmDelegate (SkipTransferConfirmForm objSkipTransferConfirmForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objSkipTransferConfirmForm = objSkipTransferConfirmForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objSkipTransferConfirmForm.getRetainData(), "~"); 
	}

	/**
	 * Method for SkipTransferConfirm_INIT State and confirmBtn Event. 
	 */
	public CancelTransferConfirmForm skipTransferConfirm_INIT_confirmBtn() throws Exception 
	{
		EBWLogger.trace(this, "Starting SkipTransferConfirm_INITconfirmBtn()"); 
		EBWLogger.trace(this, "Service name       : skipNextTransfer"); 

		CancelTransferConfirmForm objCancelTransferConfirmForm = new CancelTransferConfirmForm();

		Object objOutput = null;
		String strStatement="";

		//Mapping payment attributes...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
		ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
		MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();

		Object objTOParam[] = {objPaymentDetails,objFromMSAcc_Details,objToMSAcc_Details,objMSUserDetails};

		objUserSessionObject.put("isDiffScreen","false");

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

		//Pre business delegate hook population...
		com.tcs.ebw.payments.businessdelegate.SkipTransferConfirmDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.SkipTransferConfirmDelegateHook();
		objBusinessDelegateHook.preSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn(objSkipTransferConfirmForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		EBWAppContext appContext=new EBWAppContext();
		appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));
		
		IEBWService objService = EBWServiceFactory.create("skipNextTransfer",appContext);
		objOutput = objService.execute(clsParamTypes, objParams);
		objCancelTransferConfirmForm.setState("CancelTransferConfirm_INIT");

		//Post Business Delegate hook population...
		objBusinessDelegateHook.postSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn(objSkipTransferConfirmForm, objCancelTransferConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 


		EBWLogger.trace(this, "Return bean object : " + objCancelTransferConfirmForm);
		EBWLogger.trace(this, "Finished SkipTransferConfirm_INITconfirmBtn()"); 
		return objCancelTransferConfirmForm;
	}

}
