/*
 * Created on Mon Apr 20 10:43:41 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;
import org.apache.commons.beanutils.BeanUtils;
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
public class InternalPreConfirmDelegate extends EbwBusinessDelegate {

	InternalPreConfirmForm objInternalPreConfirmForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of InternalPreConfirmDelegate class. 
	 */ 
	public InternalPreConfirmDelegate (InternalPreConfirmForm objInternalPreConfirmForm, HashMap objUserSessionObject) {
		this.objInternalPreConfirmForm = objInternalPreConfirmForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objInternalPreConfirmForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of InternalPreConfirmDelegate class. 
	 */ 
	public InternalPreConfirmDelegate (InternalPreConfirmForm objInternalPreConfirmForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objInternalPreConfirmForm = objInternalPreConfirmForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objInternalPreConfirmForm.getRetainData(), "~"); 
	}

	/**
	 * Method for InternalPreConfirm_INIT State and confirmbut Event. 
	 */
	public InternalConfirmForm internalPreConfirm_INIT_confirmbut() throws Exception 
	{
		EBWLogger.trace(this, "Starting InternalPreConfirm_INITconfirmbut()"); 
		EBWLogger.trace(this, "Service name       : setInternalPaymentDetails"); 

		InternalConfirmForm objInternalConfirmForm = new InternalConfirmForm();
		BeanUtils.copyProperties(objInternalConfirmForm, objInternalPreConfirmForm);

		Object objOutput = null;
		String strStatement = "";

		//Mapping payment attributes...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
		ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
		MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();

		Object objTOParam[] = {objPaymentDetails,objFromMSAcc_Details,objToMSAcc_Details,objMSUserDetails};

		objUserSessionObject.put("isDiffScreen","false");

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

		//Pre Delegate Hook population...
		com.tcs.ebw.payments.businessdelegate.InternalPreConfirmDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.InternalPreConfirmDelegateHook();
		objBusinessDelegateHook.preInternalPreConfirmInternalPreConfirm_INITconfirmbut(objInternalPreConfirmForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("setInternalPaymentDetails");
		objOutput = objService.execute(clsParamTypes, objParams);
		objInternalConfirmForm.setState("InternalConfirm_INIT");

		//Post delegate hook population...
		objBusinessDelegateHook.postInternalPreConfirmInternalPreConfirm_INITconfirmbut(objInternalPreConfirmForm, objInternalConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 


		EBWLogger.trace(this, "Return bean object : " + objInternalConfirmForm);
		EBWLogger.trace(this, "Finished InternalPreConfirm_INITconfirmbut()"); 
		return objInternalConfirmForm;
	}

	/**
	 * Method for InternalPreConfirm_Edit State and confirmbut Event. 
	 */
	public InternalConfirmForm internalPreConfirm_Edit_confirmbut() throws Exception 
	{
		EBWLogger.trace(this, "Starting InternalPreConfirm_Editconfirmbut()"); 
		EBWLogger.trace(this, "Service name       : updateInternalPaymentDetails"); 

		InternalConfirmForm objInternalConfirmForm = new InternalConfirmForm();
		BeanUtils.copyProperties(objInternalConfirmForm, objInternalPreConfirmForm);

		Object objOutput = null;
		String strStatement = "";

		//Mapping payment attributes...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
		ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
		MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();

		Object objTOParam[] = {objPaymentDetails,objFromMSAcc_Details,objToMSAcc_Details,objMSUserDetails};

		objUserSessionObject.put("isDiffScreen","false");

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

		//Pre delegate hook population...
		com.tcs.ebw.payments.businessdelegate.InternalPreConfirmDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.InternalPreConfirmDelegateHook();
		objBusinessDelegateHook.preInternalPreConfirmInternalPreConfirm_Editconfirmbut(objInternalPreConfirmForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("updateInternalPaymentDetails");
		objOutput = objService.execute(clsParamTypes, objParams);
		objInternalConfirmForm.setState("InternalConfirm_INIT");

		//Post business delegate hook population...
		objBusinessDelegateHook.postInternalPreConfirmInternalPreConfirm_Editconfirmbut(objInternalPreConfirmForm, objInternalConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objInternalConfirmForm);
		EBWLogger.trace(this, "Finished InternalPreConfirm_Editconfirmbut()"); 
		return objInternalConfirmForm;
	}

}
