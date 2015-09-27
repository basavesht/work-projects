/*
 * Created on Mon Jun 08 18:21:46 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;
import com.tcs.ebw.common.util.*;
import com.tcs.ebw.businessdelegate.EbwBusinessDelegate;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.DsConfigDetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ConfirmPopupRejectDelegate extends EbwBusinessDelegate {

	ConfirmPopupRejectForm objConfirmPopupRejectForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of ConfirmPopupRejectDelegate class. 
	 */ 
	public ConfirmPopupRejectDelegate (ConfirmPopupRejectForm objConfirmPopupRejectForm, HashMap objUserSessionObject) {
		this.objConfirmPopupRejectForm = objConfirmPopupRejectForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objConfirmPopupRejectForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of ConfirmPopupRejectDelegate class. 
	 */ 
	public ConfirmPopupRejectDelegate (ConfirmPopupRejectForm objConfirmPopupRejectForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objConfirmPopupRejectForm = objConfirmPopupRejectForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objConfirmPopupRejectForm.getRetainData(), "~"); 
	}

	/**
	 * Method for ConfirmPopupReject_INIT State and INIT Event. 
	 */
	public ConfirmPopupRejectForm confirmPopupReject_INIT_iNIT() throws Exception 
	{
		EBWLogger.trace(this, "Starting ConfirmPopupReject_INITINIT()"); 
		EBWLogger.trace(this, "Service name       : getCOERejectPayDetails"); 

		Object objOutput = null;
		String[] strStatement = {"getCurrentBusinessDate","getValidTxnStatus"};

		DsConfigDetailsTO objDsConfigDetails = new DsConfigDetailsTO();

		//Note : StatementId's for the below server side validations are hard wired in the service class..
		//Input Mappings for the Status Consistency check (ServerSide validations)...
		com.tcs.ebw.payments.transferobject.DsStatusConsistency dsStatusConsistency = new com.tcs.ebw.payments.transferobject.DsStatusConsistency();
		dsStatusConsistency.setApplication_id(PropertyFileReader.getProperty("APPL_ID"));
		dsStatusConsistency.setEvent_name("Reject_Txn");

		Object objTOParam[] = {objDsConfigDetails,dsStatusConsistency};

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String[].class,Object[].class,Boolean.class};

		//Pre business delegate population...
		com.tcs.ebw.payments.businessdelegate.ConfirmPopupRejectDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ConfirmPopupRejectDelegateHook();
		objBusinessDelegateHook.preConfirmPopupRejectConfirmPopupReject_INITINIT(objConfirmPopupRejectForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("getCOERejectPayDetails");
		objOutput = objService.execute(clsParamTypes, objParams);
		objConfirmPopupRejectForm.setState("ConfirmPopupReject_INIT");

		//Post business delegate hook population...
		objBusinessDelegateHook.postConfirmPopupRejectConfirmPopupReject_INITINIT(objConfirmPopupRejectForm, objConfirmPopupRejectForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objConfirmPopupRejectForm);
		EBWLogger.trace(this, "Finished ConfirmPopupReject_INITINIT()"); 
		return objConfirmPopupRejectForm;
	}

	/**
	 * Method for ConfirmPopupReject_INIT State and confirmBtn Event. 
	 */
	public PostRejectConfirmForm confirmPopupReject_INIT_confirmBtn() throws Exception 
	{
		EBWLogger.trace(this, "Starting ConfirmPopupReject_INITconfirmBtn()"); 
		PostRejectConfirmForm objPostRejectConfirmForm = new PostRejectConfirmForm();
		EBWLogger.trace(this, "Service name       : confirmReject"); 

		Object objOutput = null;
		String strStatement = "";

		//Mapping payment attributes...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();

		Object objTOParam[] = {objPaymentDetails,objMSUserDetails};

		objUserSessionObject.put("isDiffScreen","false");
		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String.class,Object[].class,Boolean.class};

		//Pre Business Delegate hook population...
		com.tcs.ebw.payments.businessdelegate.ConfirmPopupRejectDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ConfirmPopupRejectDelegateHook();
		objBusinessDelegateHook.preConfirmPopupRejectConfirmPopupReject_INITconfirmBtn(objConfirmPopupRejectForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("confirmReject");
		objOutput = objService.execute(clsParamTypes, objParams);
		objPostRejectConfirmForm.setState("PostRejectConfirm_INIT");

		//Post business delegate hook population...
		objBusinessDelegateHook.postConfirmPopupRejectConfirmPopupReject_INITconfirmBtn(objConfirmPopupRejectForm, objConfirmPopupRejectForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objPostRejectConfirmForm);
		EBWLogger.trace(this, "Finished ConfirmPopupReject_INITconfirmBtn()"); 
		return objPostRejectConfirmForm;
	}
}
