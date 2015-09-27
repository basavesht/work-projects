/*
 * Created on Mon Apr 20 10:43:41 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.HashMap;

import org.apache.commons.beanutils.BeanUtils;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.ebw.common.util.*;
import com.tcs.ebw.businessdelegate.EbwBusinessDelegate;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.DsConfigDetailsTO;
import com.tcs.ebw.payments.transferobject.DsGetEditTransferInTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       23-09-2011        P3-B            PLA  
 * **********************************************************
 */
public class InternalTransferDelegate extends EbwBusinessDelegate {

	InternalTransferForm objInternalTransferForm =  null;
	UserPrincipal objUserPrincipal =  null;
	HashMap objUserSessionObject =  null;
	HashMap objRetainDataMap = null;

	/**
	 * Constructor of InternalTransferDelegate class. 
	 */ 
	public InternalTransferDelegate (InternalTransferForm objInternalTransferForm, HashMap objUserSessionObject) {
		this.objInternalTransferForm = objInternalTransferForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objInternalTransferForm.getRetainData(), "~"); 
	}

	/**
	 * Constructor of InternalTransferDelegate class. 
	 */ 
	public InternalTransferDelegate (InternalTransferForm objInternalTransferForm, HashMap objUserSessionObject, UserPrincipal objUserPrincipal) {
		this.objInternalTransferForm = objInternalTransferForm; 
		this.objUserSessionObject = objUserSessionObject; 
		this.objUserPrincipal = objUserPrincipal; 
		this.objRetainDataMap = ConvertionUtil.convertToMap(this.objInternalTransferForm.getRetainData(), "~"); 
	}

	/**
	 * Method for InternalTransfer_INIT State and INIT Event. 
	 */
	public InternalTransferForm internalTransfer_INIT_iNIT() throws Exception
	{
		EBWLogger.trace(this, "Starting InternalTransfer_INITINIT()"); 
		EBWLogger.trace(this, "Service name       : getOnLoadInternalINITDtl"); 
		Object objOutput = null;

		DsConfigDetailsTO objDsConfigDetails = new DsConfigDetailsTO();

		String strStatement[]={"getCurrentBusinessDate"};
		Object objTOParam[] = {objDsConfigDetails};

		objUserSessionObject.put("isDiffScreen","false");

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String[].class,Object[].class,Boolean.class};

		//Busisness Delegate hook object used for Pre and post population.
		com.tcs.ebw.payments.businessdelegate.InternalTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.InternalTransferDelegateHook();

		// Postpopulate function call.
		objBusinessDelegateHook.preInternalTransferInternalTransfer_INITINIT(objInternalTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("getOnLoadInternalINITDtl");
		objOutput = objService.execute(clsParamTypes, objParams);
		objInternalTransferForm.setState("InternalTransfer_INIT");

		com.tcs.ebw.payments.transferobject.DsConfigDetailsOut dsConfigDetailsOut = ((com.tcs.ebw.payments.transferobject.DsConfigDetailsOut)((Object[]) objOutput)[0]);
		objInternalTransferForm.setBusinessDate(ConvertionUtil.convertToAppDateStr(dsConfigDetailsOut.getBusiness_date()));
		objInternalTransferForm.setInitiationDate(ConvertionUtil.convertToAppDateStr(dsConfigDetailsOut.getBusiness_date()));

		// Postpopulate function call.
		objBusinessDelegateHook.postInternalTransferInternalTransfer_INITINIT(objInternalTransferForm, objInternalTransferForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "Return bean object : " + objInternalTransferForm);
		EBWLogger.trace(this, "Finished InternalTransfer_INITINIT()"); 
		return objInternalTransferForm;
	}

	/**
	 * Method for InternalTransfer_INIT State and Submit Event. 
	 */
	public InternalPreConfirmForm internalTransfer_INIT_submitbut() throws Exception 
	{
		EBWLogger.trace(this, "Starting InternalTransfer_INIT_submitbut()"); 
		EBWLogger.trace(this, "Service name       : checkPaymentDetails"); 

		InternalPreConfirmForm objInternalPreConfirmForm = new InternalPreConfirmForm();
		BeanUtils.copyProperties(objInternalPreConfirmForm, objInternalTransferForm);

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

		//Busisness Delegate hook object used for Pre and post population.
		com.tcs.ebw.payments.businessdelegate.InternalTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.InternalTransferDelegateHook();

		// Prepopulate function call.
		objBusinessDelegateHook.preInternalTransferInternalTransfer_INITsubmitbut(objInternalTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("checkPaymentDetails");
		objOutput = objService.execute(clsParamTypes, objParams);
		objInternalPreConfirmForm.setState("InternalPreConfirm_INIT");

		// Postpopulate function call.
		objBusinessDelegateHook.postInternalTransferInternalTransfer_INITsubmitbut(objInternalTransferForm, objInternalPreConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "Return bean object : " + objInternalPreConfirmForm);
		EBWLogger.trace(this, "Finished InternalTransfer_INIT_submitbut()"); 
		return objInternalPreConfirmForm;
	}

	/**
	 * Method for InternalTransfer_Edit State and INIT Event. 
	 */
	public InternalTransferForm internalTransfer_Edit_iNIT() throws Exception {
		EBWLogger.trace(this, "Starting InternalTransfer_EditINIT()"); 
		EBWLogger.trace(this, "Service name       : getOnLoadInternalEditDtl"); 

		Object objOutput = null;

		DsConfigDetailsTO objDsConfigDetails = new DsConfigDetailsTO();

		DsGetEditTransferInTO dsGetEditTransferInTO = new DsGetEditTransferInTO();
		dsGetEditTransferInTO.setPaypayref(ConvertionUtil.convertToString(objInternalTransferForm.getTxnPayPayRefNumber()));
		dsGetEditTransferInTO.setPaybatchref(ConvertionUtil.convertToString(objInternalTransferForm.getTxnBatchRefNumber()));

		//Input Mappings for the Status Consistency check (ServerSide validations)...
		com.tcs.ebw.payments.transferobject.DsStatusConsistency dsStatusConsistency = new com.tcs.ebw.payments.transferobject.DsStatusConsistency();
		dsStatusConsistency.setApplication_id(PropertyFileReader.getProperty("APPL_ID"));
		dsStatusConsistency.setEvent_name("Edit_Txn");

		String strStatement[]={"getCurrentBusinessDate","getEditTransferDetails","getValidTxnStatus"};
		Object objTOParam[] = {objDsConfigDetails,dsGetEditTransferInTO,dsStatusConsistency};

		objUserSessionObject.put("isDiffScreen","false");

		Object objParams[]={strStatement,objTOParam,new Boolean(false)};
		Class clsParamTypes[]={String[].class,Object[].class,Boolean.class};

		//Busisness Delegate hook object used for Pre and post population.
		com.tcs.ebw.payments.businessdelegate.InternalTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.InternalTransferDelegateHook();

		// Prepopulate function call.
		objBusinessDelegateHook.preInternalTransferInternalTransfer_EditINIT(objInternalTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("getOnLoadInternalEditDtl");
		objOutput = objService.execute(clsParamTypes, objParams);
		objInternalTransferForm.setState("InternalTransfer_Edit");

		com.tcs.ebw.payments.transferobject.DsConfigDetailsOut dsConfigDetailsOut = ((com.tcs.ebw.payments.transferobject.DsConfigDetailsOut)((Object []) objOutput)[0]);
		objInternalTransferForm.setBusinessDate(ConvertionUtil.convertToAppDateStr(dsConfigDetailsOut.getBusiness_date()));

		com.tcs.ebw.payments.transferobject.DsGetEditTransferOutTO dsGetEditTransferOutTO = ((com.tcs.ebw.payments.transferobject.DsGetEditTransferOutTO)((Object []) objOutput)[1]);
		objInternalTransferForm.setPaymentamount(MSCommonUtils.formatTxnAmount(dsGetEditTransferOutTO.getPaydebitamt()));
		objInternalTransferForm.setInitiationDate(ConvertionUtil.convertToAppDateStr(dsGetEditTransferOutTO.getInitiationDate()));
		objInternalTransferForm.setDurationNoOfTransfers(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPayfreqpaymentcount()));
		objInternalTransferForm.setDurationdollarlimit(MSCommonUtils.formatTxnAmount(dsGetEditTransferOutTO.getPayfreqlimit()));
		objInternalTransferForm.setDurationValue(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getDuration()));
		objInternalTransferForm.setFrequencycombo(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPayfrequencycombo()));
		objInternalTransferForm.setParentTxnNumber(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPar_txn_no()));
		objInternalTransferForm.setFromKeyAccNumber(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getKeyaccountnumber_from()));
		objInternalTransferForm.setToKeyAccNumber(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getKeyaccountnumber_to()));
		objInternalTransferForm.setPayeeId(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPaypayeeid()));
		objInternalTransferForm.setTotalDollarlimit(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getAccumulatedamt()));
		objInternalTransferForm.setTotalTransfer(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getAccumulatedtransfers()));
		objInternalTransferForm.setEditfreqComboVal(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPayfrequencycombo()));
		objInternalTransferForm.setVersionnum(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getVersionnum()));
		objInternalTransferForm.setBatchVersionnum(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getBatversionnum()));
		objInternalTransferForm.setParTxnVersionnum(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPartxnversionnum()));
		objInternalTransferForm.setPrevPaystatus(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getPayccsstatuscode()));
		objInternalTransferForm.setFromAccNo_br_fa(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getFrombr_acct_no_fa()));
		objInternalTransferForm.setToAccNo_br_fa(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getTobr_acct_no_fa()));
		objInternalTransferForm.setTransactionType(ConvertionUtil.convertToString(dsGetEditTransferOutTO.getTransactionType()));

		//Setting the LoanAcnt in case of PLA transactions..
		if(dsGetEditTransferOutTO.getTransactionType()!=null && dsGetEditTransferOutTO.getTransactionType().equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
			objInternalTransferForm.setLoanAcntNo(dsGetEditTransferOutTO.getLoanAcnt()); //Loan Account in case of PLA Txns..
		}

		// Postpopulate function call.
		objBusinessDelegateHook.postInternalTransferInternalTransfer_EditINIT(objInternalTransferForm, objInternalTransferForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objInternalTransferForm);
		EBWLogger.trace(this, "Finished InternalTransfer_EditINIT()"); 
		return objInternalTransferForm;
	}

	/**
	 * Method for InternalTransfer_Edit State and submitbut Event. 
	 */
	public InternalPreConfirmForm internalTransfer_Edit_submitbut() throws Exception {
		EBWLogger.trace(this, "Starting InternalTransfer_Editsubmitbut()");
		EBWLogger.trace(this, "Service name       : checkPaymentDetails"); 

		InternalPreConfirmForm objInternalPreConfirmForm = new InternalPreConfirmForm();
		BeanUtils.copyProperties(objInternalPreConfirmForm, objInternalTransferForm);

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

		//Busisness Delegate hook object used for Pre and post population.
		com.tcs.ebw.payments.businessdelegate.InternalTransferDelegateHook objBusinessDelegateHook = new com.tcs.ebw.payments.businessdelegate.InternalTransferDelegateHook();

		// Prepopulate function call.
		objBusinessDelegateHook.preInternalTransferInternalTransfer_Editsubmitbut(objInternalTransferForm, objParams, clsParamTypes, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After Prepopulate Service Parameters : " + objParams); 
		EBWLogger.trace(this, "After Prepopulate Service Param Type : " + clsParamTypes); 

		//Service Call.....
		IEBWService objService = EBWServiceFactory.create("checkPaymentDetails");
		objOutput = objService.execute(clsParamTypes, objParams);
		objInternalPreConfirmForm.setState("InternalPreConfirm_Edit");

		// Postpopulate function call.
		objBusinessDelegateHook.postInternalTransferInternalTransfer_Editsubmitbut(objInternalTransferForm, objInternalPreConfirmForm, objOutput, objParams, objUserPrincipal, objRetainDataMap, objUserSessionObject);

		EBWLogger.trace(this, "After post populate Return Value : " + objOutput); 
		EBWLogger.trace(this, "After post populate Param  Value : " + objParams); 

		EBWLogger.trace(this, "Return bean object : " + objInternalPreConfirmForm);
		EBWLogger.trace(this, "Finished InternalTransfer_Editsubmitbut()"); 
		return objInternalPreConfirmForm;
	}
}
