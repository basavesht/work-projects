/*
 * Created on Tue Apr 14 10:53:15 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;
import org.apache.commons.beanutils.BeanUtils;

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
public class ExternalPreConfirmDelegate extends EbwBusinessDelegate {

	ExternalPreConfirmForm objExternalPreConfirmForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of ExternalPreConfirmDelegate class. 
	 */ 
	public ExternalPreConfirmDelegate (ExternalPreConfirmForm objExternalPreConfirmForm, HashMap objUserSessionObject) {
		this.objExternalPreConfirmForm = objExternalPreConfirmForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objExternalPreConfirmForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of ExternalPreConfirmDelegate class. 
	 */ 
	public ExternalPreConfirmDelegate (ExternalPreConfirmForm objExternalPreConfirmForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objExternalPreConfirmForm = objExternalPreConfirmForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objExternalPreConfirmForm.getRetainData(), "~"); 
	}

	/**
	 * Method for ExternalPreConfirm_INIT State and confirmbut Event. 
	 */
	public ExternalConfirmForm externalPreConfirm_INIT_confirmbut() throws Exception 
	{
		EBWLogger.trace(this, "Starting ExternalPreConfirm_INITconfirmbut()"); 
		EBWLogger.trace(this, "Service name       : setPaymentDetails"); 

		ExternalConfirmForm objExternalConfirmForm = new ExternalConfirmForm();
		BeanUtils.copyProperties(objExternalConfirmForm, objExternalPreConfirmForm);

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

		//Pre business delegate hook population...
		com.tcs.ebw.payments.businessdelegate.ExternalPreConfirmDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ExternalPreConfirmDelegateHook();
		objBusinessDelegateHook.preExternalPreConfirmExternalPreConfirm_INITconfirmbut(objExternalPreConfirmForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		EBWAppContext appContext=new EBWAppContext();
		appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

		IEBWService objService = EBWServiceFactory.create("setPaymentDetails",appContext);
		objOutput = objService.execute(clsParamTypes, objParams);
		objExternalConfirmForm.setState("ExternalConfirm_INIT");

		//Post delegate hook population...
		objBusinessDelegateHook.postExternalPreConfirmExternalPreConfirm_INITconfirmbut(objExternalPreConfirmForm, objExternalConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objExternalConfirmForm);
		EBWLogger.trace(this, "Finished ExternalPreConfirm_INITconfirmbut()"); 
		return objExternalConfirmForm;
	}

	/**
	 * Method for ExternalPreConfirm_Edit State and confirmbut Event. 
	 */
	public ExternalConfirmForm externalPreConfirm_Edit_confirmbut() throws Exception 
	{
		EBWLogger.trace(this, "Starting ExternalPreConfirm_Editconfirmbut()"); 
		EBWLogger.trace(this, "Service name       : updatePaymentDetails"); 

		ExternalConfirmForm objExternalConfirmForm = new ExternalConfirmForm();
		BeanUtils.copyProperties(objExternalConfirmForm, objExternalPreConfirmForm);

		Object objOutput = null;
		String strStatement = "";

		//Mapping payment attributes...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
		ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
		MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();

		Object objTOParam[] = {objPaymentDetails,objFromMSAcc_Details,objToMSAcc_Details,objMSUserDetails};

		objUserSessionObject.put("isDiffScreen","false");
		//handlePagination(objUserSessionObject,objTOParam,objExternalPreConfirmForm,serviceId,StringMode1);

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

		//Pre business delegate hook population...
		com.tcs.ebw.payments.businessdelegate.ExternalPreConfirmDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ExternalPreConfirmDelegateHook();
		objBusinessDelegateHook.preExternalPreConfirmExternalPreConfirm_Editconfirmbut(objExternalPreConfirmForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		EBWAppContext appContext=new EBWAppContext();
		appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

		IEBWService objService = EBWServiceFactory.create("updatePaymentDetails",appContext);
		objOutput = objService.execute(clsParamTypes, objParams);
		objExternalConfirmForm.setState("ExternalConfirm_INIT");

		//Post business delegate hook population...
		objBusinessDelegateHook.postExternalPreConfirmExternalPreConfirm_Editconfirmbut(objExternalPreConfirmForm, objExternalConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objExternalConfirmForm);
		EBWLogger.trace(this, "Finished ExternalPreConfirm_Editconfirmbut()"); 
		return objExternalConfirmForm;
	}

}
