/*
 * Created on Tue May 26 15:46:15 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.ebw.common.util.*;
import com.tcs.ebw.businessdelegate.EbwBusinessDelegate;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.DsConfigDetailsOut;
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
	 * Method for CancelTransfer_INIT State and skipBtn Event. (Direct skip from the Transaction View Details screen)..
	 */
	public SkipTransferConfirmForm skipTransferConfirm_INIT_iNIT() throws Exception 
	{
		EBWLogger.trace(this, "Starting skipTransferConfirm_INIT_iNIT()"); 
		EBWLogger.trace(this, "Service name       : getOnLoadCancelRecDetails"); 

		Object objOutput = null;

		DsConfigDetailsTO objDsConfigDetails = new DsConfigDetailsTO();

		//Input Mappings for the transaction details...
		com.tcs.ebw.payments.transferobject.DsGetCancelPayInTO dsgetCancelPayInTO = new com.tcs.ebw.payments.transferobject.DsGetCancelPayInTO();
		dsgetCancelPayInTO.setPaypayref(ConvertionUtil.convertToString(objSkipTransferConfirmForm.getTxnPayPayRefNumber()));
		dsgetCancelPayInTO.setPar_txn_no(ConvertionUtil.convertToDouble(objSkipTransferConfirmForm.getParentTxnNumber()));

		//Input Mappings for the Status Consistency check (ServerSide validations)...
		com.tcs.ebw.payments.transferobject.DsStatusConsistency dsStatusConsistency = new com.tcs.ebw.payments.transferobject.DsStatusConsistency();
		dsStatusConsistency.setApplication_id(PropertyFileReader.getProperty("APPL_ID"));
		dsStatusConsistency.setEvent_name("Cancel_Txn");

		String strStatement[]={"getCurrentBusinessDate","getCancelPayDetails","getValidTxnStatus"};
		Object objTOParam[] = {objDsConfigDetails,dsgetCancelPayInTO,dsStatusConsistency};

		objUserSessionObject.put("isDiffScreen","false");

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String[].class,Object[].class,Boolean.class};

		//Pre business delegate hook population...
		com.tcs.ebw.payments.businessdelegate.SkipTransferConfirmDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.SkipTransferConfirmDelegateHook();
		objBusinessDelegateHook.preSkipTransferConfirmSkipTransferConfirm_INITINIT(objSkipTransferConfirmForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("getOnLoadCancelRecDetails");
		objOutput = objService.execute(clsParamTypes, objParams);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		//Post Business Delegate hook population...
		objBusinessDelegateHook.postSkipTransferConfirmSkipTransferConfirm_INITINIT(objSkipTransferConfirmForm, objSkipTransferConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		DsConfigDetailsOut dsConfigDetailsOut = ((DsConfigDetailsOut)((Object []) objOutput)[0]);
		objSkipTransferConfirmForm.setBusinessDate(ConvertionUtil.convertToAppDateStr(dsConfigDetailsOut.getBusiness_date()));

		com.tcs.ebw.payments.transferobject.DsGetCancelPayOutTO dsgetCancelPayOutTO = ((com.tcs.ebw.payments.transferobject.DsGetCancelPayOutTO)((Object[])objOutput)[1]);
		objSkipTransferConfirmForm.setPaymentstatus (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayccsstatuscode()));
		objSkipTransferConfirmForm.setRecurringFreq (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayfrequency()));
		objSkipTransferConfirmForm.setDurationValue (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getDuration()));
		objSkipTransferConfirmForm.setTxnBatchRefNumber (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPaybatchref()));
		objSkipTransferConfirmForm.setParentTxnNumber(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPar_txn_no()));
		objSkipTransferConfirmForm.setFromKeyAccNumber (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getKeyaccountnumber_from()));
		objSkipTransferConfirmForm.setToKeyAccNumber (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getKeyaccountnumber_to()));
		objSkipTransferConfirmForm.setPayeeId(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPaypayeeid()));
		objSkipTransferConfirmForm.setCancelTransAmount (MSCommonUtils.formatTxnAmount(dsgetCancelPayOutTO.getPaydebitamt()));
		objSkipTransferConfirmForm.setCancelTransTransferDate (ConvertionUtil.convertToAppDateStr(dsgetCancelPayOutTO.getRequested_exe_dt()));
		objSkipTransferConfirmForm.setCancelTransDollarsTransfered (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getAccumulatedAmt()));
		objSkipTransferConfirmForm.setCancelTransNoOfTransfers (ConvertionUtil.convertToString(dsgetCancelPayOutTO.getAccumulatedTransfers()));
		objSkipTransferConfirmForm.setCancelTransEntryDate(ConvertionUtil.convertToAppDateStr(dsgetCancelPayOutTO.getCreated_date()));
		objSkipTransferConfirmForm.setPrefered_previous_txn_dt(ConvertionUtil.convertToAppDateStr(dsgetCancelPayOutTO.getPrefered_previous_txn_dt()));
		objSkipTransferConfirmForm.setVersionnum(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getVersionnum()));
		objSkipTransferConfirmForm.setBatchVersionnum(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getBatversionnum()));
		objSkipTransferConfirmForm.setParTxnVersionnum(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPartxnversionnum()));
		objSkipTransferConfirmForm.setTransactiontype(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getTxn_type()));
		objSkipTransferConfirmForm.setThreasholdAmount(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayfreqlimit()));

		objSkipTransferConfirmForm.setState("SkipTransferConfirm_INIT");

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objSkipTransferConfirmForm);
		EBWLogger.trace(this, "Finished skipTransferConfirm_INIT_iNIT()"); 
		return objSkipTransferConfirmForm;
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
		IEBWService objService = EBWServiceFactory.create("skipNextTransfer");
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
