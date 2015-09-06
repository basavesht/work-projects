/*
 * Created on Tue Apr 14 10:53:15 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;

import org.apache.commons.beanutils.BeanUtils;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.ebw.common.util.*;
import com.tcs.ebw.businessdelegate.EbwBusinessDelegate;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.DsConfigDetailsTO;
import com.tcs.ebw.payments.transferobject.DsGetEditTransferInTO;
import com.tcs.ebw.payments.transferobject.DsOnloadAccDetailsTO;
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

public class ExternalTransferDelegate extends EbwBusinessDelegate {

	ExternalTransferForm objExternalTransferForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of ExternalTransferDelegate class. 
	 */ 
	public ExternalTransferDelegate (ExternalTransferForm objExternalTransferForm, HashMap objUserSessionObject) {
		this.objExternalTransferForm = objExternalTransferForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objExternalTransferForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of ExternalTransferDelegate class. 
	 */ 
	public ExternalTransferDelegate (ExternalTransferForm objExternalTransferForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objExternalTransferForm = objExternalTransferForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objExternalTransferForm.getRetainData(), "~"); 
	}

	/**
	 * Method for ExternalTransfer_INIT State and INIT Event. 
	 */
	public ExternalTransferForm externalTransfer_INIT_iNIT() throws Exception 
	{
		EBWLogger.trace(this, "Starting ExternalTransfer_INITINIT()"); 
		EBWLogger.trace(this, "Service name       : getOnLoadExtINITDtl"); 

		Object objOutput = null;
		DsConfigDetailsTO objDsConfigDetails = new DsConfigDetailsTO();
		DsOnloadAccDetailsTO dsOnloadAccDetailsTO = new DsOnloadAccDetailsTO();

		String[] strStatement={"getCurrentBusinessDate","checkExtAccAvailablity"};
		Object objTOParam[] = {objDsConfigDetails,dsOnloadAccDetailsTO};

		objUserSessionObject.put("isDiffScreen","false");

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String[].class,Object[].class,Boolean.class};

		//Pre delegate hook population...
		com.tcs.ebw.payments.businessdelegate.ExternalTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ExternalTransferDelegateHook();
		objBusinessDelegateHook.preExternalTransferExternalTransfer_IINITINIT(objExternalTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("getOnLoadExtINITDtl");
		objOutput = objService.execute(clsParamTypes, objParams);
		objExternalTransferForm.setState("ExternalTransfer_INIT");

		com.tcs.ebw.payments.transferobject.DsConfigDetailsOut dsConfigDetailsOut = ((com.tcs.ebw.payments.transferobject.DsConfigDetailsOut)((Object []) objOutput)[0]);
		objExternalTransferForm.setBusinessDate(ConvertionUtil.convertToAppDateStr(dsConfigDetailsOut.getBusiness_date()));
		objExternalTransferForm.setInitiationDate(ConvertionUtil.convertToAppDateStr(dsConfigDetailsOut.getBusiness_date()));

		//Post delegate hook population...
		objBusinessDelegateHook.postExternalTransferExternalTransfer_IINITINIT(objExternalTransferForm, objExternalTransferForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objExternalTransferForm);
		EBWLogger.trace(this, "Finished ExternalTransfer_INITINIT()"); 
		return objExternalTransferForm;
	}

	/**
	 * Method for ExternalTransfer_INIT State and submitbut Event. 
	 */
	public ExternalPreConfirmForm externalTransfer_INIT_submitbut() throws Exception 
	{
		EBWLogger.trace(this, "Starting ExternalTransfer_INITsubmitbut()"); 
		EBWLogger.trace(this, "Service name       : checkPaymentDetails"); 

		ExternalPreConfirmForm objExternalPreConfirmForm = new ExternalPreConfirmForm();
		BeanUtils.copyProperties(objExternalPreConfirmForm, objExternalTransferForm);

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
		com.tcs.ebw.payments.businessdelegate.ExternalTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ExternalTransferDelegateHook();
		objBusinessDelegateHook.preExternalTransferExternalTransfer_INITsubmitbut(objExternalTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("checkPaymentDetails");
		objOutput = objService.execute(clsParamTypes, objParams);
		objExternalPreConfirmForm.setState("ExternalPreConfirm_INIT");

		//Post business delegate hook population...
		objBusinessDelegateHook.postExternalTransferExternalTransfer_INITsubmitbut(objExternalTransferForm, objExternalPreConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objExternalPreConfirmForm);
		EBWLogger.trace(this, "Finished ExternalTransfer_INITsubmitbut()"); 
		return objExternalPreConfirmForm;
	}

	/**
	 * Method for ExternalTransfer_Edit State and INIT Event. 
	 */
	public ExternalTransferForm externalTransfer_Edit_iNIT() throws Exception 
	{
		EBWLogger.trace(this, "Starting ExternalTransfer_EditINIT()"); 
		EBWLogger.trace(this, "Service name       : getOnLoadExtEditDtl"); 

		Object objOutput = null;

		DsConfigDetailsTO objDsConfigDetails = new DsConfigDetailsTO();

		//Input Mappings for the transaction details...
		DsGetEditTransferInTO dsGetEditTransferInTO = new DsGetEditTransferInTO();
		dsGetEditTransferInTO.setPaypayref(ConvertionUtil.convertToString(objExternalTransferForm.getTxnPayPayRefNumber()));
		dsGetEditTransferInTO.setPaybatchref(ConvertionUtil.convertToString(objExternalTransferForm.getTxnBatchRefNumber()));

		//Input Mappings for the Status Consistency check (ServerSide validations)...
		com.tcs.ebw.payments.transferobject.DsStatusConsistency dsStatusConsistency = new com.tcs.ebw.payments.transferobject.DsStatusConsistency();
		dsStatusConsistency.setApplication_id(PropertyFileReader.getProperty("APPL_ID"));
		dsStatusConsistency.setEvent_name("Edit_Txn");

		String strStatement[]={"getCurrentBusinessDate","getEditTransferDetails","getValidTxnStatus"};
		Object objTOParam[] = {objDsConfigDetails,dsGetEditTransferInTO,dsStatusConsistency};

		objUserSessionObject.put("isDiffScreen","false");

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String[].class,Object[].class,Boolean.class};

		//Pre business delegate hook population..
		com.tcs.ebw.payments.businessdelegate.ExternalTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ExternalTransferDelegateHook();
		objBusinessDelegateHook.preExternalTransferExternalTransfer_EditINIT(objExternalTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("getOnLoadExtEditDtl");
		objOutput = objService.execute(clsParamTypes, objParams);
		objExternalTransferForm.setState("ExternalTransfer_Edit");

		com.tcs.ebw.payments.transferobject.DsConfigDetailsOut dsConfigDetailsOut = ((com.tcs.ebw.payments.transferobject.DsConfigDetailsOut)((Object []) objOutput)[0]);
		objExternalTransferForm.setBusinessDate(ConvertionUtil.convertToAppDateStr(dsConfigDetailsOut.getBusiness_date()));

		com.tcs.ebw.payments.transferobject.DsGetEditTransferOutTO dsGetEditTransferOutTO = ((com.tcs.ebw.payments.transferobject.DsGetEditTransferOutTO)((Object []) objOutput)[1]);
		objExternalTransferForm.setPaymentamount(MSCommonUtils.formatTxnAmount(dsGetEditTransferOutTO.getPaydebitamt()));
		objExternalTransferForm.setInitiationDate(ConvertionUtil.convertToAppDateStr(dsGetEditTransferOutTO.getInitiationDate()));
		objExternalTransferForm.setDurationNoOfTransfers(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPayfreqpaymentcount()));
		objExternalTransferForm.setDurationdollarlimit(MSCommonUtils.formatTxnAmount(dsGetEditTransferOutTO.getPayfreqlimit()));
		objExternalTransferForm.setDurationValue(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getDuration()));
		objExternalTransferForm.setFrequencycombo(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPayfrequencycombo()));
		objExternalTransferForm.setParentTxnNumber(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPar_txn_no()));
		objExternalTransferForm.setFromKeyAccNumber(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getKeyaccountnumber_from()));
		objExternalTransferForm.setToKeyAccNumber(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getKeyaccountnumber_to()));
		objExternalTransferForm.setPayeeId(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPaypayeeid()));
		objExternalTransferForm.setTotalDollarlimit(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getAccumulatedamt()));
		objExternalTransferForm.setTotalTransfer(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getAccumulatedtransfers()));
		objExternalTransferForm.setEditfreqComboVal(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPayfrequencycombo()));
		objExternalTransferForm.setVersionnum(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getVersionnum()));
		objExternalTransferForm.setBatchVersionnum(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getBatversionnum()));
		objExternalTransferForm.setParTxnVersionnum(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPartxnversionnum()));
		objExternalTransferForm.setPrevPaystatus(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPayccsstatuscode()));
		objExternalTransferForm.setTransactionType(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getTransactionType()));
		objExternalTransferForm.setFromAccNo_br_fa(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getFrombr_acct_no_fa()));
		objExternalTransferForm.setToAccNo_br_fa(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getTobr_acct_no_fa()));
		objExternalTransferForm.setTransferTypeIRAEdit(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getRetirement_txn_type_desc()));

		//Post business delegate hook population..
		objBusinessDelegateHook.postExternalTransferExternalTransfer_EditINIT(objExternalTransferForm, objExternalTransferForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objExternalTransferForm);
		EBWLogger.trace(this, "Finished ExternalTransfer_EditINIT()"); 
		return objExternalTransferForm;
	}

	/**
	 * Method for ExternalTransfer_Edit State and submitbut Event. 
	 */
	public ExternalPreConfirmForm externalTransfer_Edit_submitbut() throws Exception 
	{
		EBWLogger.trace(this, "Starting ExternalTransfer_Editsubmitbut()"); 
		EBWLogger.trace(this, "Service name       : checkPaymentDetails"); 

		ExternalPreConfirmForm objExternalPreConfirmForm = new ExternalPreConfirmForm();
		BeanUtils.copyProperties(objExternalPreConfirmForm, objExternalTransferForm);

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

		//Pre business delegate hook..
		com.tcs.ebw.payments.businessdelegate.ExternalTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.ExternalTransferDelegateHook();
		objBusinessDelegateHook.preExternalTransferExternalTransfer_Editsubmitbut(objExternalTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("checkPaymentDetails");
		objOutput = objService.execute(clsParamTypes, objParams);
		objExternalPreConfirmForm.setState("ExternalPreConfirm_Edit");

		//Post business delegate hook...
		objBusinessDelegateHook.postExternalTransferExternalTransfer_Editsubmitbut(objExternalTransferForm, objExternalPreConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objExternalPreConfirmForm);
		EBWLogger.trace(this, "Finished ExternalTransfer_Editsubmitbut()"); 
		return objExternalPreConfirmForm;
	}
}
