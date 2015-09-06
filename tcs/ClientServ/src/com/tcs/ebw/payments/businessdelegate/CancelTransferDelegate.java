/*
 * Created on Thu Apr 16 11:49:46 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;

import org.apache.commons.beanutils.BeanUtils;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.ebw.common.context.EBWAppContext;
import com.tcs.ebw.common.util.*;
import com.tcs.ebw.businessdelegate.EbwBusinessDelegate;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.DsConfigDetailsTO;
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
public class CancelTransferDelegate extends EbwBusinessDelegate {

	CancelTransferForm objCancelTransferForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of CancelTransferDelegate class. 
	 */ 
	public CancelTransferDelegate (CancelTransferForm objCancelTransferForm, HashMap objUserSessionObject) {
		this.objCancelTransferForm = objCancelTransferForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objCancelTransferForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of CancelTransferDelegate class. 
	 */ 
	public CancelTransferDelegate (CancelTransferForm objCancelTransferForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objCancelTransferForm = objCancelTransferForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objCancelTransferForm.getRetainData(), "~"); 
	}
	/**
	 * Method for CancelTransfer_INIT State and INIT Event. 
	 */
	public CancelTransferForm cancelTransfer_INIT_iNIT() throws Exception 
	{
		EBWLogger.trace(this, "Starting CancelTransfer_INITINIT()"); 
		EBWLogger.trace(this, "Service name       : getOnLoadCancelPayDetails"); 

		Object objOutput = null;

		com.tcs.ebw.payments.transferobject.DsConfigDetailsTO objDsConfigDetails = new com.tcs.ebw.payments.transferobject.DsConfigDetailsTO();

		//Input Mappings for the getting transaction details...
		com.tcs.ebw.payments.transferobject.DsGetCancelPayInTO dsgetCancelPayInTO = new com.tcs.ebw.payments.transferobject.DsGetCancelPayInTO();
		dsgetCancelPayInTO.setPaypayref(ConvertionUtil.convertToString(objCancelTransferForm.getTxnPayPayRefNumber()));
		dsgetCancelPayInTO.setPar_txn_no(ConvertionUtil.convertToDouble(objCancelTransferForm.getParentTxnNumber()));

		//Input Mappings for the Status Consistency check (ServerSide validations)...
		com.tcs.ebw.payments.transferobject.DsStatusConsistency dsStatusConsistency = new com.tcs.ebw.payments.transferobject.DsStatusConsistency();
		dsStatusConsistency.setApplication_id(PropertyFileReader.getProperty("APPL_ID"));
		dsStatusConsistency.setEvent_name("Cancel_Txn");

		String strStatement[]={"getCurrentBusinessDate","getCancelPayDetails","getValidTxnStatus"};
		Object objTOParam[] = {objDsConfigDetails,dsgetCancelPayInTO,dsStatusConsistency};

		objUserSessionObject.put("isDiffScreen","false");

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String[].class,Object[].class,Boolean.class};

		//Pre Business Delegate hook population...
		com.tcs.ebw.payments.businessdelegate.CancelTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.CancelTransferDelegateHook();
		objBusinessDelegateHook.preCancelTransferCancelTransfer_INITINIT(objCancelTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		EBWAppContext appContext=new EBWAppContext();
		appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

		IEBWService objService = EBWServiceFactory.create("getOnLoadCancelPayDetails",appContext);
		objOutput = objService.execute(clsParamTypes, objParams);
		objCancelTransferForm.setState("CancelTransfer_INIT");

		//Post Business Delegate Hook population..
		objBusinessDelegateHook.postCancelTransferCancelTransfer_INITINIT(objCancelTransferForm, objCancelTransferForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		com.tcs.ebw.payments.transferobject.DsConfigDetailsOut dsConfigDetailsOut = ((com.tcs.ebw.payments.transferobject.DsConfigDetailsOut)((Object []) objOutput)[0]);
		objCancelTransferForm.setBusinessDate(ConvertionUtil.convertToString(dsConfigDetailsOut.getBusiness_date()));

		com.tcs.ebw.payments.transferobject.DsGetCancelPayOutTO dsgetCancelPayOutTO = ((com.tcs.ebw.payments.transferobject.DsGetCancelPayOutTO)((Object[])objOutput)[1]);
		objCancelTransferForm.setPaymentstatus (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayccsstatuscode()));
		objCancelTransferForm.setTxnBatchRefNumber (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPaybatchref()));
		objCancelTransferForm.setParentTxnNumber(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPar_txn_no()));
		objCancelTransferForm.setFromKeyAccNumber (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getKeyaccountnumber_from()));
		objCancelTransferForm.setToKeyAccNumber (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getKeyaccountnumber_to()));
		objCancelTransferForm.setPayeeId(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPaypayeeid()));
		objCancelTransferForm.setCancelTransAmount (MSCommonUtils.formatTxnAmount(dsgetCancelPayOutTO.getPaydebitamt()));
		objCancelTransferForm.setCancelTransTransferDate (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getRequested_exe_dt()));
		objCancelTransferForm.setVersionnum(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getVersionnum()));
		objCancelTransferForm.setBatchVersionnum(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getBatversionnum()));
		objCancelTransferForm.setParTxnVersionnum(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPartxnversionnum()));

		EBWLogger.trace(this, "Return bean object : " + objCancelTransferForm);
		EBWLogger.trace(this, "Finished CancelTransfer_INITINIT()"); 
		return objCancelTransferForm;
	}

	/**
	 * Method for CancelTransfer_INIT State and cancelBtn Event. 
	 */
	public CancelTransferConfirmForm cancelTransfer_INIT_cancelBtn() throws Exception 
	{
		EBWLogger.trace(this, "Starting CancelTransfer_INITcancelBtn()"); 
		EBWLogger.trace(this, "Service name       : cancelPaymentDetails"); 

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

		//Pre Business Delegate Hook...
		com.tcs.ebw.payments.businessdelegate.CancelTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.CancelTransferDelegateHook();
		objBusinessDelegateHook.preCancelTransferCancelTransfer_INITcancelBtn(objCancelTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		EBWAppContext appContext=new EBWAppContext();
		appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

		IEBWService objService = EBWServiceFactory.create("cancelPaymentDetails",appContext);
		objOutput = objService.execute(clsParamTypes, objParams);
		objCancelTransferConfirmForm.setState("CancelTransferConfirm_INIT");

		//Post Business Delegate hook...
		objBusinessDelegateHook.postCancelTransferCancelTransfer_INITcancelBtn(objCancelTransferForm, objCancelTransferConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objCancelTransferConfirmForm);
		EBWLogger.trace(this, "Finished CancelTransfer_INITcancelBtn()"); 
		return objCancelTransferConfirmForm;
	}

	/**
	 * Method for CancelTransfer_Recurring State and INIT Event. 
	 */
	public CancelTransferForm cancelTransfer_Recurring_iNIT() throws Exception 
	{
		EBWLogger.trace(this, "Starting CancelTransfer_RecurringINIT()"); 
		EBWLogger.trace(this, "Service name       : getOnLoadCancelRecDetails"); 

		Object objOutput = null;

		DsConfigDetailsTO objDsConfigDetails = new DsConfigDetailsTO();

		//Input Mappings for the transaction details...
		com.tcs.ebw.payments.transferobject.DsGetCancelPayInTO dsgetCancelPayInTO = new com.tcs.ebw.payments.transferobject.DsGetCancelPayInTO();
		dsgetCancelPayInTO.setPaypayref(ConvertionUtil.convertToString(objCancelTransferForm.getTxnPayPayRefNumber()));
		dsgetCancelPayInTO.setPar_txn_no(ConvertionUtil.convertToDouble(objCancelTransferForm.getParentTxnNumber()));

		//Input Mappings for the Status Consistency check (ServerSide validations)...
		com.tcs.ebw.payments.transferobject.DsStatusConsistency dsStatusConsistency = new com.tcs.ebw.payments.transferobject.DsStatusConsistency();
		dsStatusConsistency.setApplication_id(PropertyFileReader.getProperty("APPL_ID"));
		dsStatusConsistency.setEvent_name("Cancel_Txn");

		String strStatement[]={"getCurrentBusinessDate","getCancelPayDetails","getValidTxnStatus"};
		Object objTOParam[] = {objDsConfigDetails,dsgetCancelPayInTO,dsStatusConsistency};

		objUserSessionObject.put("isDiffScreen","false");

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String[].class,Object[].class,Boolean.class};

		//Pre Business Delegate Hook population...
		com.tcs.ebw.payments.businessdelegate.CancelTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.CancelTransferDelegateHook();
		objBusinessDelegateHook.preCancelTransferCancelTransfer_RecurringINIT(objCancelTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		EBWAppContext appContext=new EBWAppContext();
		appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

		IEBWService objService = EBWServiceFactory.create("getOnLoadCancelRecDetails",appContext);
		objOutput = objService.execute(clsParamTypes, objParams);
		objCancelTransferForm.setState("CancelTransfer_Recurring");

		//Post business delegate hook population...
		objBusinessDelegateHook.postCancelTransferCancelTransfer_RecurringINIT(objCancelTransferForm, objCancelTransferForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		com.tcs.ebw.payments.transferobject.DsConfigDetailsOut dsConfigDetailsOut = ((com.tcs.ebw.payments.transferobject.DsConfigDetailsOut)((Object []) objOutput)[0]);
		objCancelTransferForm.setBusinessDate(ConvertionUtil.convertToString(dsConfigDetailsOut.getBusiness_date()));

		com.tcs.ebw.payments.transferobject.DsGetCancelPayOutTO dsgetCancelPayOutTO = ((com.tcs.ebw.payments.transferobject.DsGetCancelPayOutTO)((Object[])objOutput)[1]);
		objCancelTransferForm.setPaymentstatus (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayccsstatuscode()));
		objCancelTransferForm.setRecurringFreq (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayfrequency()));
		objCancelTransferForm.setDurationValue (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getDuration()));
		objCancelTransferForm.setTxnBatchRefNumber (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPaybatchref()));
		objCancelTransferForm.setParentTxnNumber(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPar_txn_no()));
		objCancelTransferForm.setFromKeyAccNumber (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getKeyaccountnumber_from()));
		objCancelTransferForm.setToKeyAccNumber (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getKeyaccountnumber_to()));
		objCancelTransferForm.setPayeeId(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPaypayeeid()));
		objCancelTransferForm.setCancelTransAmount (MSCommonUtils.formatTxnAmount(dsgetCancelPayOutTO.getPaydebitamt()));
		objCancelTransferForm.setCancelTransTransferDate (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getRequested_exe_dt()));
		objCancelTransferForm.setCancelTransDollarsTransfered (MSCommonUtils.formatTxnAmount(dsgetCancelPayOutTO.getAccumulatedAmt()));
		objCancelTransferForm.setCancelTransNoOfTransfers (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getAccumulatedTransfers()));
		objCancelTransferForm.setPrefered_previous_txn_dt(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPrefered_previous_txn_dt()));
		objCancelTransferForm.setVersionnum(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getVersionnum()));
		objCancelTransferForm.setBatchVersionnum(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getBatversionnum()));
		objCancelTransferForm.setParTxnVersionnum(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPartxnversionnum()));
		objCancelTransferForm.setTransactiontype(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getTxn_type()));
		objCancelTransferForm.setParentTxnDate(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPar_txn_request_dt()));

		EBWLogger.trace(this, "Return bean object : " + objCancelTransferForm);
		EBWLogger.trace(this, "Finished CancelTransfer_RecurringINIT()"); 
		return objCancelTransferForm;
	}

	/**
	 * Method for CancelTransfer_Recurring State and cancelBtn Event. 
	 */
	public CancelTransferConfirmForm cancelTransfer_Recurring_cancelBtn() throws Exception 
	{
		EBWLogger.trace(this, "Starting CancelTransfer_RecurringcancelBtn()"); 
		EBWLogger.trace(this, "Service name       : cancelRecurringPayment"); 

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

		objUserSessionObject.put("isDiffScreen","false");

		//Pre Business Delegate population...
		com.tcs.ebw.payments.businessdelegate.CancelTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.CancelTransferDelegateHook();
		objBusinessDelegateHook.preCancelTransferCancelTransfer_RecurringcancelBtn(objCancelTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		EBWAppContext appContext=new EBWAppContext();
		appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

		IEBWService objService = EBWServiceFactory.create("cancelRecurringPayment",appContext);
		objOutput = objService.execute(clsParamTypes, objParams);
		objCancelTransferConfirmForm.setState("CancelTransferConfirm_INIT");

		//Post Business Delegate population...
		objBusinessDelegateHook.postCancelTransferCancelTransfer_RecurringcancelBtn(objCancelTransferForm, objCancelTransferConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objCancelTransferConfirmForm);
		EBWLogger.trace(this, "Finished CancelTransfer_RecurringcancelBtn()"); 
		return objCancelTransferConfirmForm;
	}

	/**
	 * Method for CancelTransfer_Recurring State and skipBtn Event. 
	 */
	public SkipTransferConfirmForm cancelTransfer_Recurring_skipBtn() throws Exception 
	{
		EBWLogger.trace(this, "Starting CancelTransfer_RecurringskipBtn()"); 
		SkipTransferConfirmForm objSkipTransferConfirmForm = new SkipTransferConfirmForm();

		BeanUtils.copyProperties(objSkipTransferConfirmForm, objCancelTransferForm);

		Object objOutput = null;

		Object objParams[]=null;
		Class clsParamTypes[]=null;

		//Pre Business Delegate population..
		com.tcs.ebw.payments.businessdelegate.CancelTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.CancelTransferDelegateHook();
		objBusinessDelegateHook.preCancelTransferCancelTransfer_RecurringskipBtn(objCancelTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		objSkipTransferConfirmForm.setState("SkipTransferConfirm_INIT");

		//Post Business Delegate population...
		objBusinessDelegateHook.postCancelTransferCancelTransfer_RecurringskipBtn(objCancelTransferForm, objSkipTransferConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objSkipTransferConfirmForm);
		EBWLogger.trace(this, "Finished CancelTransfer_RecurringskipBtn()"); 
		return objSkipTransferConfirmForm;
	}


}
