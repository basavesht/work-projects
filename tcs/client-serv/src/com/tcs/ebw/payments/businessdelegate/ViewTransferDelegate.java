/*
 * Created on Fri Jun 05 14:12:50 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.ebw.common.context.EBWAppContext;
import com.tcs.ebw.common.util.*;
import com.tcs.ebw.businessdelegate.EbwBusinessDelegate;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.GetViewInputDetailsTO;
/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ViewTransferDelegate extends EbwBusinessDelegate {

	ViewTransferForm objViewTransferForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of ViewTransferDelegate class. 
	 */ 
	public ViewTransferDelegate (ViewTransferForm objViewTransferForm, HashMap objUserSessionObject) {
		this.objViewTransferForm = objViewTransferForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objViewTransferForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of ViewTransferDelegate class. 
	 */ 
	public ViewTransferDelegate (ViewTransferForm objViewTransferForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objViewTransferForm = objViewTransferForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objViewTransferForm.getRetainData(), "~"); 
	}

	/**
	 * Method for ViewTransfer_INIT State and INIT Event. 
	 */
	public ViewTransferForm viewTransfer_INIT_iNIT() throws Exception {
		EBWLogger.trace(this, "Starting ViewTransfer_INITINIT()"); 
		EBWLogger.trace(this, "Service name       : getTransferViewDetails"); 

		Object objOutput = null;

		GetViewInputDetailsTO objGetViewInputDetailsTO = new GetViewInputDetailsTO();
		objGetViewInputDetailsTO.setPaypayref(ConvertionUtil.convertToString(objViewTransferForm.getTxnPayPayRefNumber()));
		objGetViewInputDetailsTO.setPaybatchref(ConvertionUtil.convertToString(objViewTransferForm.getTxnBatchRefNumber()));

		Object objParams[]={objGetViewInputDetailsTO,new Boolean(false)};
		Class clsParamTypes[]={Object.class,Boolean.class};

		//Busisness Delegate hook object used for Pre and post population.
		com.tcs.ebw.payments.businessdelegate.ViewTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ViewTransferDelegateHook();

		// Prepopulate function call.
		objBusinessDelegateHook.preViewTransferViewTransfer_INITINIT(objViewTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		EBWAppContext appContext=new EBWAppContext();
		appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

		IEBWService objService = EBWServiceFactory.create("getTransferViewDetails",appContext);
		objOutput = objService.execute(clsParamTypes, objParams);
		objViewTransferForm.setState("ViewTransfer_INIT");

		// Postpopulate function call.
		objBusinessDelegateHook.postViewTransferViewTransfer_INITINIT(objViewTransferForm, objViewTransferForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO getViewOutputDetailsTO = (com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO)objOutput;
		objViewTransferForm.setViewTransAmount(MSCommonUtils.formatTxnAmount(getViewOutputDetailsTO.getPaymentAmount()));
		objViewTransferForm.setViewTransInitDate(ConvertionUtil.convertToString(getViewOutputDetailsTO.getPaymentDate()));
		objViewTransferForm.setViewTransLastTransDate(ConvertionUtil.convertToString(getViewOutputDetailsTO.getLastTransactionDate()));
		objViewTransferForm.setViewTransConfNo(ConvertionUtil.convertToString(getViewOutputDetailsTO.getConfirmationNo()));
		objViewTransferForm.setViewTransStatus(ConvertionUtil.convertToString(getViewOutputDetailsTO.getStatus()));
		objViewTransferForm.setTransferType(ConvertionUtil.convertToString(getViewOutputDetailsTO.getTransferType()));

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 


		EBWLogger.trace(this, "Return bean object : " + objViewTransferForm);
		EBWLogger.trace(this, "Finished ViewTransfer_INITINIT()"); 
		return objViewTransferForm;
	}

	/**
	 * Method for ViewTransfer_Recurring State and INIT Event. 
	 */
	public ViewTransferForm viewTransfer_Recurring_iNIT() throws Exception {
		EBWLogger.trace(this, "Starting ViewTransfer_RecurringINIT()"); 
		EBWLogger.trace(this, "Service name       : getTransferViewDetails"); 

		Object objOutput = null;

		GetViewInputDetailsTO objGetViewInputDetailsTO = new GetViewInputDetailsTO();
		objGetViewInputDetailsTO.setPaypayref(ConvertionUtil.convertToString(objViewTransferForm.getTxnPayPayRefNumber()));
		objGetViewInputDetailsTO.setPaybatchref(ConvertionUtil.convertToString(objViewTransferForm.getTxnBatchRefNumber()));

		Object objParams[]={objGetViewInputDetailsTO,new Boolean(false)};
		Class clsParamTypes[]={Object.class,Boolean.class};

		//Busisness Delegate hook object used for Pre and post population.
		com.tcs.ebw.payments.businessdelegate.ViewTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ViewTransferDelegateHook();

		// Prepopulate function call.
		objBusinessDelegateHook.preViewTransferViewTransfer_RecurringINIT(objViewTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		EBWAppContext appContext=new EBWAppContext();
		appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

		IEBWService objService = EBWServiceFactory.create("getTransferViewDetails",appContext);
		objOutput = objService.execute(clsParamTypes, objParams);
		objViewTransferForm.setState("ViewTransfer_Recurring");

		// Postpopulate function call.
		objBusinessDelegateHook.postViewTransferViewTransfer_RecurringINIT(objViewTransferForm, objViewTransferForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO getViewOutputDetailsTO = (com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO)objOutput;
		objViewTransferForm.setViewTransAmount(MSCommonUtils.formatTxnAmount(getViewOutputDetailsTO.getPaymentAmount()));
		objViewTransferForm.setViewTransInitDate(ConvertionUtil.convertToString(getViewOutputDetailsTO.getPaymentDate()));
		objViewTransferForm.setViewTransConfNo(ConvertionUtil.convertToString(getViewOutputDetailsTO.getConfirmationNo()));
		objViewTransferForm.setViewTransStatus(ConvertionUtil.convertToString(getViewOutputDetailsTO.getStatus()));
		objViewTransferForm.setViewTransDollarsTransfered(MSCommonUtils.formatTxnAmount(getViewOutputDetailsTO.getAmountTransferred()));
		objViewTransferForm.setViewTransNoOfTransfers(ConvertionUtil.convertToString(getViewOutputDetailsTO.getNoOfTransfers()));
		objViewTransferForm.setDurationValue(ConvertionUtil.convertToString(getViewOutputDetailsTO.getDurationValue()));
		objViewTransferForm.setTransferType(ConvertionUtil.convertToString(getViewOutputDetailsTO.getTransferType()));

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 


		EBWLogger.trace(this, "Return bean object : " + objViewTransferForm);
		EBWLogger.trace(this, "Finished ViewTransfer_RecurringINIT()"); 
		return objViewTransferForm;
	}

	/**
	 * Method for ViewTransfer_OneTimeHistory State and INIT Event. 
	 */
	public ViewTransferForm viewTransfer_OneTimeHistory_iNIT() throws Exception {
		EBWLogger.trace(this, "Starting ViewTransfer_OneTimeHistoryINIT()"); 
		EBWLogger.trace(this, "Service name       : getTransferViewDetails"); 

		Object objOutput = null;

		GetViewInputDetailsTO objGetViewInputDetailsTO = new GetViewInputDetailsTO();
		objGetViewInputDetailsTO.setPaypayref(ConvertionUtil.convertToString(objViewTransferForm.getTxnPayPayRefNumber()));
		objGetViewInputDetailsTO.setPaybatchref(ConvertionUtil.convertToString(objViewTransferForm.getTxnBatchRefNumber()));

		Object objParams[]={objGetViewInputDetailsTO,new Boolean(false)};
		Class clsParamTypes[]={Object.class,Boolean.class};

		//Busisness Delegate hook object used for Pre and post population.
		com.tcs.ebw.payments.businessdelegate.ViewTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ViewTransferDelegateHook();

		// Prepopulate function call.
		objBusinessDelegateHook.preViewTransferViewTransfer_OneTimeHistoryINIT(objViewTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		EBWAppContext appContext=new EBWAppContext();
		appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

		IEBWService objService = EBWServiceFactory.create("getTransferViewDetails",appContext);
		objOutput = objService.execute(clsParamTypes, objParams);
		objViewTransferForm.setState("ViewTransfer_OneTimeHistory");

		// Postpopulate function call.
		objBusinessDelegateHook.postViewTransferViewTransfer_OneTimeHistoryINIT(objViewTransferForm, objViewTransferForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO getViewOutputDetailsTO = (com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO)objOutput;
		objViewTransferForm.setViewTransAmount(MSCommonUtils.formatTxnAmount(getViewOutputDetailsTO.getPaymentAmount()));
		objViewTransferForm.setViewTransInitDate(ConvertionUtil.convertToString(getViewOutputDetailsTO.getPaymentDate()));
		objViewTransferForm.setViewTransLastTransDate(ConvertionUtil.convertToString(getViewOutputDetailsTO.getLastTransactionDate()));
		objViewTransferForm.setViewTransConfNo(ConvertionUtil.convertToString(getViewOutputDetailsTO.getConfirmationNo()));
		objViewTransferForm.setViewTransStatus(ConvertionUtil.convertToString(getViewOutputDetailsTO.getStatus()));
		objViewTransferForm.setTransferType(ConvertionUtil.convertToString(getViewOutputDetailsTO.getTransferType()));

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 


		EBWLogger.trace(this, "Return bean object : " + objViewTransferForm);
		EBWLogger.trace(this, "Finished ViewTransfer_OneTimeHistoryINIT()"); 
		return objViewTransferForm;
	}

	/**
	 * Method for ViewTransfer_RecurringHistory State and INIT Event. 
	 */
	public ViewTransferForm viewTransfer_RecurringHistory_iNIT() throws Exception {
		EBWLogger.trace(this, "Starting ViewTransfer_RecurringHistoryINIT()"); 
		EBWLogger.trace(this, "Service name       : getTransferViewDetails"); 

		Object objOutput = null;

		GetViewInputDetailsTO objGetViewInputDetailsTO = new GetViewInputDetailsTO();
		objGetViewInputDetailsTO.setPaypayref(ConvertionUtil.convertToString(objViewTransferForm.getTxnPayPayRefNumber()));
		objGetViewInputDetailsTO.setPaybatchref(ConvertionUtil.convertToString(objViewTransferForm.getTxnBatchRefNumber()));

		Object objParams[]={objGetViewInputDetailsTO,new Boolean(false)};
		Class clsParamTypes[]={Object.class,Boolean.class};

		//Busisness Delegate hook object used for Pre and post population.
		com.tcs.ebw.payments.businessdelegate.ViewTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ViewTransferDelegateHook();

		// Prepopulate function call.
		objBusinessDelegateHook.preViewTransferViewTransfer_RecurringHistoryINIT(objViewTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		EBWAppContext appContext=new EBWAppContext();
		appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

		IEBWService objService = EBWServiceFactory.create("getTransferViewDetails",appContext);
		objOutput = objService.execute(clsParamTypes, objParams);
		objViewTransferForm.setState("ViewTransfer_RecurringHistory");

		// Postpopulate function call.
		objBusinessDelegateHook.postViewTransferViewTransfer_RecurringHistoryINIT(objViewTransferForm, objViewTransferForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO getViewOutputDetailsTO = (com.tcs.ebw.payments.transferobject.GetViewOutputDetailsTO)objOutput;
		objViewTransferForm.setViewTransAmount(MSCommonUtils.formatTxnAmount(getViewOutputDetailsTO.getPaymentAmount()));
		objViewTransferForm.setViewTransInitDate(ConvertionUtil.convertToString(getViewOutputDetailsTO.getPaymentDate()));
		objViewTransferForm.setViewTransConfNo(ConvertionUtil.convertToString(getViewOutputDetailsTO.getConfirmationNo()));
		objViewTransferForm.setViewTransStatus(ConvertionUtil.convertToString(getViewOutputDetailsTO.getStatus()));
		objViewTransferForm.setViewTransDollarsTransfered(MSCommonUtils.formatTxnAmount(getViewOutputDetailsTO.getAmountTransferred()));
		objViewTransferForm.setViewTransNoOfTransfers(ConvertionUtil.convertToString(getViewOutputDetailsTO.getNoOfTransfers()));
		objViewTransferForm.setDurationValue(ConvertionUtil.convertToString(getViewOutputDetailsTO.getDurationValue()));
		objViewTransferForm.setTransferType(ConvertionUtil.convertToString(getViewOutputDetailsTO.getTransferType()));

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objViewTransferForm);
		EBWLogger.trace(this, "Finished ViewTransfer_RecurringHistoryINIT()"); 
		return objViewTransferForm;
	}

}
