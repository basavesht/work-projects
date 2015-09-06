/*
 * Created on Thu Apr 16 09:51:26 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.FormatAccount;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.serverValidations.StatusConsistencyChk;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.channels.integration.MMAccount;
import com.tcs.bancs.channels.integration.MMContext;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.businessdelegate.BusinessDelegateHook;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.*;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 * **********************************************************
 */
public class CancelTransferDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for CancelTransfer Screen, CancelTransfer_INIT State and INIT Event. 
	 * @throws Exception 
	 */
	public void preCancelTransferCancelTransfer_INITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preCancelTransferCancelTransfer_INITINIT"); 
		try 
		{	
			Object[] cancelTransferTO = (Object[])objParam[1];

			//Updating the qz_date TO...
			String qzBranchCode = PropertyFileReader.getProperty("OU_ID"); 
			DsConfigDetailsTO objDsConfigDetails = (DsConfigDetailsTO)cancelTransferTO[0];
			objDsConfigDetails.setBranch_code(qzBranchCode);

			//Initialization code...
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();

			//Context sharing for the Input Mappings 
			MMContext objMMContextData = objUserPrincipal.getContextData();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);

			//Updating the input TO...
			DsGetCancelPayInTO objDsGetCancelPayInTO = (DsGetCancelPayInTO)cancelTransferTO[1];
			objDsGetCancelPayInTO.setLifeUserId(objMSUserDetails.getUuid());
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preCancelTransferCancelTransfer_INITINIT"); 
	}

	public void postCancelTransferCancelTransfer_INITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postCancelTransferCancelTransfer_INITINIT");
		try 
		{
			CancelTransferForm objCancelTransfer=(CancelTransferForm) objSourceForm;
			com.tcs.ebw.payments.transferobject.DsGetCancelPayOutTO dsgetCancelPayOutTO = ((com.tcs.ebw.payments.transferobject.DsGetCancelPayOutTO)((Object []) objReturn)[1]); 
			HashMap txnDetails = new HashMap();

			//Required Payment Details Mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setFrequency_Type("1");
			objPaymentDetails.setTransfer_Type(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getTxn_type()));

			//From account mappings..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(dsgetCancelPayOutTO.getFrombr_acct_no_fa()!=null){
				objFromMSAcc_Details.setAccountNumber(dsgetCancelPayOutTO.getFrombr_acct_no_fa().substring(3,9));
				objFromMSAcc_Details.setOfficeNumber(dsgetCancelPayOutTO.getFrombr_acct_no_fa().substring(0,3));
				objFromMSAcc_Details.setFaNumber(dsgetCancelPayOutTO.getFrombr_acct_no_fa().substring(9));
				objFromMSAcc_Details.setNickName(dsgetCancelPayOutTO.getFrmAccNickName());
				objFromMSAcc_Details.setFriendlyName(dsgetCancelPayOutTO.getFrmAccFriendlyName());
			}

			//To account mappings..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(dsgetCancelPayOutTO.getTobr_acct_no_fa()!=null){
				objToMSAcc_Details.setAccountNumber(dsgetCancelPayOutTO.getTobr_acct_no_fa().substring(3,9));
				objToMSAcc_Details.setOfficeNumber(dsgetCancelPayOutTO.getTobr_acct_no_fa().substring(0,3));
				objToMSAcc_Details.setFaNumber(dsgetCancelPayOutTO.getTobr_acct_no_fa().substring(9));
				objToMSAcc_Details.setNickName(dsgetCancelPayOutTO.getToAccNickName());
				objToMSAcc_Details.setFriendlyName(dsgetCancelPayOutTO.getToAccFriendlyName());
			}

			//External account mappings..
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO = new DsExtPayeeDetailsOutTO();
			objDsExtPayeeDetailsOutTO.setNick_name(dsgetCancelPayOutTO.getPaypayeename1());
			objDsExtPayeeDetailsOutTO.setCpyaccnum(dsgetCancelPayOutTO.getPayeeaccnum());

			//Loan Account mappings..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			objLoanAcntDetails.setLoanAccount(dsgetCancelPayOutTO.getLoanAcnt());

			txnDetails.put("PaymentDetails",objPaymentDetails);
			txnDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			txnDetails.put("MSToAccDetails",objToMSAcc_Details);
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);
			txnDetails.put("LoanAccountDetails",objLoanAcntDetails);

			//Setting the Frequency Value.....
			objCancelTransfer.setFrequencyValue("1");
			objCancelTransfer.setCancelTransFrequency(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));

			//Setting the local currency by default..
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			objCancelTransfer.setCurrencyCode(currencyCode);

			//Checking the status consistency ...
			StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
			ArrayList event_valid_statuses = ((ArrayList)((Object []) objReturn)[2]); 
			String txn_Paystatus = dsgetCancelPayOutTO.getPayccsstatuscode();
			ServiceContext contextData = (ServiceContext)statusConsistency.verifyStatusConsistency(event_valid_statuses,txn_Paystatus);

			//Setting the From account and To account Details...
			objCancelTransfer.setCancelTransFromAcc (FormatAccount.getDebitAccountDisp(txnDetails));
			objCancelTransfer.setCancelTransToAcc (FormatAccount.getCreditAccountDisp(txnDetails));

			//Response...
			MSCommonUtils.logEventResponse(contextData);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}

		} catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished postCancelTransferCancelTransfer_INITINIT"); 
	}

	/**
	 * Method for CancelTransfer Screen, CancelTransfer_INIT State and cancelBtn Event. 
	 * @throws Exception 
	 */
	public void preCancelTransferCancelTransfer_INITcancelBtn(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preCancelTransferCancelTransfer_INITcancelBtn"); 
		try 
		{
			CancelTransferForm objCancelTransfer=(CancelTransferForm) objSourceForm;
			Object cancelTransferToObj[] = (Object[])objParam[1];

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			ServiceContext serviceContext = new ServiceContext();
			String fromKeyAccount =objCancelTransfer.getFromKeyAccNumber(); // Value may be MS Key account or Reference Id for the external account...
			String toKeyAccount =objCancelTransfer.getToKeyAccNumber(); // Value may be MS Key account or Reference Id for the external account...
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)cancelTransferToObj[0];

			//Context sharing for the Input Mappings 
			MMContext objMMContextData = objUserPrincipal.getContextData();
			boolean isFA= objMMContextData.isFA();
			if(isFA){
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.ACCESS_ERROR);
				String errorMessage = MSCommonUtils.extractContextErrMessage(serviceContext);
				throw new Exception(errorMessage);
			}

			ArrayList objMMContextAcc = objMMContextData.getAccounts();
			for(int i=0;i<objMMContextData.getAccounts().size();i++)
			{
				MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(i);
				objMMAccount = MSCommonUtils.getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number(3-6-3 format)...
				if(objMMAccount.getKeyAccount().equalsIgnoreCase(fromKeyAccount))
				{	
					//Setting From account details...
					objPaymentDetails.setFrom_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setFrom_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setFrmAcc_InContext(true);

					FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
					objFromMSAcc_Details = objMSCommonUtils.setFromMSAccDetailsTO(objMMAccount);
					cancelTransferToObj[1] = objFromMSAcc_Details;
				}
				else if(objMMAccount.getKeyAccount().equalsIgnoreCase(toKeyAccount))
				{
					//Setting To account details...
					objPaymentDetails.setTo_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setTo_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setToAcc_InContext(true);

					ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
					objToMSAcc_Details = objMSCommonUtils.setToMSAccDetailsTO(objMMAccount);
					cancelTransferToObj[2]= objToMSAcc_Details;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			cancelTransferToObj[3] = objMSUserDetails;

			objPaymentDetails.setBusiness_Date(objCancelTransfer.getBusinessDate());
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Cancel);
			objPaymentDetails.setTxnCancelled(true);
			objPaymentDetails.setPrevAction("Cancel");
			objPaymentDetails.setTransactionId(objCancelTransfer.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objCancelTransfer.getVersionnum());
			objPaymentDetails.setTxnBatchId(objCancelTransfer.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objCancelTransfer.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnId(objCancelTransfer.getParentTxnNumber());
			objPaymentDetails.setRecParentTxnVersion(objCancelTransfer.getParTxnVersionnum());
			objPaymentDetails.setExtAccount_RefId(objCancelTransfer.getPayeeId());
			objPaymentDetails.setTxnCurrentStatusCode(objCancelTransfer.getPaymentstatus());
			objPaymentDetails.setTxnPrevStatusCode(objCancelTransfer.getPaymentstatus());

			EBWLogger.trace(this, "Finished preCancelTransferCancelTransfer_INITcancelBtn");

		} catch (Exception exception) {
			throw exception;
		}
	}

	public void postCancelTransferCancelTransfer_INITcancelBtn(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postCancelTransferCancelTransfer_INITcancelBtn");
		try 
		{
			CancelTransferConfirmForm objCancelTransferConfirm=(CancelTransferConfirmForm) objTargetForm;
			objUserPrincipal.setBrErrorMessages(null);

			//Output extraction...
			ArrayList cancelPayOut = (ArrayList)objReturn;
			ServiceContext contextData = (ServiceContext)cancelPayOut.get(0);

			MSCommonUtils.logEventResponse(contextData);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}
			else 
			{
				if(contextData.getMaxSeverity()==MessageType.SEVERE)
				{
					//Extraction....
					JSONArray jsonError = MSCommonUtils.extractContextSevereErrMsg(contextData,true);

					//JSON Mapping....
					HashMap busniessErrorMessages = new HashMap();
					busniessErrorMessages.put("BusinessErrors", jsonError);
					JSONObject jsonErrorMess = JSONObject.fromObject(busniessErrorMessages);

					//Settings..
					objCancelTransferConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.PENDING_TRANSFERS);
				}
				else if(contextData.getMaxSeverity()==MessageType.ERROR)
				{
					//Extraction....
					JSONArray jsonError = MSCommonUtils.extractContextErrMsg(contextData,true);

					//JSON Mapping....
					HashMap busniessErrorMessages = new HashMap();
					busniessErrorMessages.put("BusinessErrors", jsonError);
					JSONObject jsonErrorMess = JSONObject.fromObject(busniessErrorMessages);

					//Settings..
					objCancelTransferConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.PENDING_TRANSFERS);
				}
				else if(cancelPayOut.get(1)!=null)
				{
					HashMap txnOutDetails = (HashMap)cancelPayOut.get(1);
					PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
					if(txnOutDetails.containsKey("PaymentDetails")){
						objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
					}

					objCancelTransferConfirm.setCancelConfirmationNo(objPaymentDetails.getTransactionId());
				}
			}
			EBWLogger.trace(this, "Finished postCancelTransferCancelTransfer_INITcancelBtn");
		} catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Method for CancelTransfer Screen, CancelTransfer_Recurring State and INIT Event. 
	 * @throws Exception 
	 */
	public void preCancelTransferCancelTransfer_RecurringINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preCancelTransferCancelTransfer_RecurringINIT"); 
		try 
		{
			Object[] cancelTransferTO = (Object[])objParam[1];

			//Updating the qz_date TO...
			String qzBranchCode = PropertyFileReader.getProperty("OU_ID"); 
			DsConfigDetailsTO objDsConfigDetails = (DsConfigDetailsTO)cancelTransferTO[0];
			objDsConfigDetails.setBranch_code(qzBranchCode);

			//Initialization code...
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();

			//Context sharing for the Input Mappings 
			MMContext objMMContextData = objUserPrincipal.getContextData();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);

			//Updating the input TO...
			DsGetCancelPayInTO objDsGetCancelPayInTO = (DsGetCancelPayInTO)cancelTransferTO[1];
			objDsGetCancelPayInTO.setLifeUserId(objMSUserDetails.getUuid());
		} 
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preCancelTransferCancelTransfer_RecurringINIT"); 
	}

	/**
	 * Post cancel transfers Recurring method...
	 */
	public void postCancelTransferCancelTransfer_RecurringINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postCancelTransferCancelTransfer_RecurringINIT");
		try 
		{
			CancelTransferForm objCancelTransfer=(CancelTransferForm) objSourceForm;
			ServiceContext context = new ServiceContext();
			com.tcs.ebw.payments.transferobject.DsGetCancelPayOutTO dsgetCancelPayOutTO = ((com.tcs.ebw.payments.transferobject.DsGetCancelPayOutTO)((Object []) objReturn)[1]);
			HashMap txnDetails = new HashMap();

			//Required Payment Details Mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setFrequency_Type("2");
			objPaymentDetails.setDuration_AmountLimit(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayfreqlimit()));
			objPaymentDetails.setDuration_EndDate(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayfreqenddate()));
			objPaymentDetails.setDuration_NoOfTransfers(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayfreqpaymentcount()));
			objPaymentDetails.setFrequency_DurationValue(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getDuration()));
			objPaymentDetails.setFrequency_DurationDesc(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayfrequency()));
			objPaymentDetails.setTransfer_Type(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getTxn_type()));

			//From account mappings..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(dsgetCancelPayOutTO.getFrombr_acct_no_fa()!=null){
				objFromMSAcc_Details.setAccountNumber(dsgetCancelPayOutTO.getFrombr_acct_no_fa().substring(3,9));
				objFromMSAcc_Details.setOfficeNumber(dsgetCancelPayOutTO.getFrombr_acct_no_fa().substring(0,3));
				objFromMSAcc_Details.setFaNumber(dsgetCancelPayOutTO.getFrombr_acct_no_fa().substring(9));
				objFromMSAcc_Details.setNickName(dsgetCancelPayOutTO.getFrmAccNickName());
				objFromMSAcc_Details.setFriendlyName(dsgetCancelPayOutTO.getFrmAccFriendlyName());
			}

			//To account mappings..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(dsgetCancelPayOutTO.getTobr_acct_no_fa()!=null){
				objToMSAcc_Details.setAccountNumber(dsgetCancelPayOutTO.getTobr_acct_no_fa().substring(3,9));
				objToMSAcc_Details.setOfficeNumber(dsgetCancelPayOutTO.getTobr_acct_no_fa().substring(0,3));
				objToMSAcc_Details.setFaNumber(dsgetCancelPayOutTO.getTobr_acct_no_fa().substring(9));
				objToMSAcc_Details.setNickName(dsgetCancelPayOutTO.getToAccNickName());
				objToMSAcc_Details.setFriendlyName(dsgetCancelPayOutTO.getToAccFriendlyName());
			}

			//External account mappings..
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO = new DsExtPayeeDetailsOutTO();
			objDsExtPayeeDetailsOutTO.setNick_name(dsgetCancelPayOutTO.getPaypayeename1());
			objDsExtPayeeDetailsOutTO.setCpyaccnum(dsgetCancelPayOutTO.getPayeeaccnum());

			//Loan Account mappings..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			objLoanAcntDetails.setLoanAccount(dsgetCancelPayOutTO.getLoanAcnt());

			txnDetails.put("PaymentDetails",objPaymentDetails);
			txnDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			txnDetails.put("MSToAccDetails",objToMSAcc_Details);
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);
			txnDetails.put("LoanAccountDetails",objLoanAcntDetails);

			//Setting the Frequency Value.....
			objCancelTransfer.setFrequencyValue("2");
			objCancelTransfer.setCancelTransFrequency(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));
			objCancelTransfer.setCancelTransRepeat(MSCommonUtils.getRepeatDesc_View(objPaymentDetails));

			//Setting the local currency by default..
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			objCancelTransfer.setCurrencyCode(currencyCode);

			//Checking the recurring criteria . If Recurring condition is not satisfied then disable the Skip Transfer Button...
			boolean isRecurring= true;
			String frequencyValue = dsgetCancelPayOutTO.getPayfrequency();
			String txn_Type = dsgetCancelPayOutTO.getTxn_type();
			Date parentTxnDate = dsgetCancelPayOutTO.getPar_txn_request_dt();
			Date currentTransferDate = dsgetCancelPayOutTO.getPrefered_previous_txn_dt(); // Transfer date without considering the holidays...
			if(currentTransferDate==null){
				currentTransferDate = dsgetCancelPayOutTO.getRequested_exe_dt();
			}
			Date nextBusinessDate = MSCommonUtils.evaluateNextTxnDate(frequencyValue,currentTransferDate,parentTxnDate,txn_Type,Boolean.FALSE,context);
			isRecurring = checkRecurringStatusAfterSkip(dsgetCancelPayOutTO,nextBusinessDate);
			if(!isRecurring){
				objCancelTransfer.setState("CancelTransfer_LastRecurring");
			}

			//Checking for the Dont_Spawn_flag and disabling the Skip transfer button accordingly..
			String dont_spawn_flag = dsgetCancelPayOutTO.getDont_spawn_flag();
			if(dont_spawn_flag!=null && dont_spawn_flag.equalsIgnoreCase("Y")){
				objCancelTransfer.setState("CancelTransfer_LastRecurring");
			}

			//Checking the status consistency ...
			StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
			ArrayList event_valid_statuses = ((ArrayList)((Object []) objReturn)[2]); 
			String txn_Paystatus = dsgetCancelPayOutTO.getPayccsstatuscode();
			ServiceContext contextData = (ServiceContext)statusConsistency.verifyStatusConsistency(event_valid_statuses,txn_Paystatus);

			//Setting the From account and To account Details...
			objCancelTransfer.setCancelTransFromAcc (FormatAccount.getDebitAccountDisp(txnDetails));
			objCancelTransfer.setCancelTransToAcc (FormatAccount.getCreditAccountDisp(txnDetails));

			//Response...
			MSCommonUtils.logEventResponse(contextData);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished postCancelTransferCancelTransfer_RecurringINIT"); 
	}

	/**
	 * Method for CancelTransfer Screen, CancelTransfer_Recurring State and cancelBtn Event. 
	 * @throws Exception 
	 */
	public void preCancelTransferCancelTransfer_RecurringcancelBtn(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preCancelTransferCancelTransfer_RecurringcancelBtn"); 
		try 
		{
			CancelTransferForm objCancelTransfer=(CancelTransferForm) objSourceForm;
			Object cancelTransferToObj[] = (Object[])objParam[1];

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			ServiceContext serviceContext = new ServiceContext();
			String fromKeyAccount =objCancelTransfer.getFromKeyAccNumber(); // Value may be MS Key account or Reference Id for the external account...
			String toKeyAccount =objCancelTransfer.getToKeyAccNumber(); // Value may be MS Key account or Reference Id for the external account...
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)cancelTransferToObj[0];

			MMContext objMMContextData = objUserPrincipal.getContextData();
			boolean isFA= objMMContextData.isFA();
			if(isFA){
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.ACCESS_ERROR);
				String errorMessage = MSCommonUtils.extractContextErrMessage(serviceContext);
				throw new Exception(errorMessage);
			}

			ArrayList objMMContextAcc = objMMContextData.getAccounts();
			for(int i=0;i<objMMContextData.getAccounts().size();i++)
			{
				MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(i);
				objMMAccount = MSCommonUtils.getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...
				if(objMMAccount.getKeyAccount().equalsIgnoreCase(fromKeyAccount))
				{
					//Setting From account details...
					objPaymentDetails.setFrom_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setFrom_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setFrmAcc_InContext(true);

					FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
					objFromMSAcc_Details = objMSCommonUtils.setFromMSAccDetailsTO(objMMAccount);
					cancelTransferToObj[1] = objFromMSAcc_Details;
				}
				if(objMMAccount.getKeyAccount().equalsIgnoreCase(toKeyAccount))
				{
					//Setting To account details...
					objPaymentDetails.setTo_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setTo_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setToAcc_InContext(true);

					ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
					objToMSAcc_Details = objMSCommonUtils.setToMSAccDetailsTO(objMMAccount);
					cancelTransferToObj[2]= objToMSAcc_Details;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			cancelTransferToObj[3] = objMSUserDetails;

			objPaymentDetails.setBusiness_Date(objCancelTransfer.getBusinessDate());
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Cancel);
			objPaymentDetails.setTxnCancelled(true);
			objPaymentDetails.setPrevAction("Cancel");
			objPaymentDetails.setTransactionId(objCancelTransfer.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objCancelTransfer.getVersionnum());
			objPaymentDetails.setTxnBatchId(objCancelTransfer.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objCancelTransfer.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnId(objCancelTransfer.getParentTxnNumber());
			objPaymentDetails.setRecParentTxnVersion(objCancelTransfer.getParTxnVersionnum());
			objPaymentDetails.setExtAccount_RefId(objCancelTransfer.getPayeeId());
			objPaymentDetails.setTxnCurrentStatusCode(objCancelTransfer.getPaymentstatus());
			objPaymentDetails.setTxnPrevStatusCode(objCancelTransfer.getPaymentstatus());
		}
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preCancelTransferCancelTransfer_RecurringcancelBtn"); 
	}

	public void postCancelTransferCancelTransfer_RecurringcancelBtn(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postCancelTransferCancelTransfer_RecurringcancelBtn");
		try 
		{
			CancelTransferConfirmForm objCancelTransferConfirm=(CancelTransferConfirmForm) objTargetForm;
			objUserPrincipal.setBrErrorMessages(null);

			//Output extraction....
			ArrayList cancelPayOut = (ArrayList)objReturn;
			ServiceContext contextData = (ServiceContext)cancelPayOut.get(0);

			MSCommonUtils.logEventResponse(contextData);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}
			else 
			{
				if(contextData.getMaxSeverity()==MessageType.SEVERE)
				{
					//Extraction....
					JSONArray jsonError = MSCommonUtils.extractContextSevereErrMsg(contextData,true);

					//JSON Mapping....
					HashMap busniessErrorMessages = new HashMap();
					busniessErrorMessages.put("BusinessErrors", jsonError);
					JSONObject jsonErrorMess = JSONObject.fromObject(busniessErrorMessages);

					//Settings..
					objCancelTransferConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.PENDING_TRANSFERS);
				}
				else if(contextData.getMaxSeverity()==MessageType.ERROR)
				{
					//Extraction....
					JSONArray jsonError = MSCommonUtils.extractContextErrMsg(contextData,true);

					//JSON Mapping....
					HashMap busniessErrorMessages = new HashMap();
					busniessErrorMessages.put("BusinessErrors", jsonError);
					JSONObject jsonErrorMess = JSONObject.fromObject(busniessErrorMessages);

					//Settings..
					objCancelTransferConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.PENDING_TRANSFERS);
				}
				else if(cancelPayOut.get(1)!=null)
				{
					HashMap txnOutDetails = (HashMap)cancelPayOut.get(1);
					PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
					if(txnOutDetails.containsKey("PaymentDetails")){
						objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
					}

					objCancelTransferConfirm.setCancelConfirmationNo(objPaymentDetails.getTransactionId());
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postCancelTransferCancelTransfer_RecurringcancelBtn"); 
	}

	/**
	 * Method for CancelTransfer Screen, CancelTransfer_Recurring State and skipBtn Event. 
	 */
	public void preCancelTransferCancelTransfer_RecurringskipBtn(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject)
	{
		EBWLogger.trace(this, "Starting preCancelTransferCancelTransfer_RecurringskipBtn"); 
		EBWLogger.trace(this, "Finished preCancelTransferCancelTransfer_RecurringskipBtn"); 
	}

	public void postCancelTransferCancelTransfer_RecurringskipBtn(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postCancelTransferCancelTransfer_RecurringskipBtn");
		try 
		{
			SkipTransferConfirmForm objSkipTransferConfirm=(SkipTransferConfirmForm) objTargetForm;
			ServiceContext context = new ServiceContext();

			Date transferCancelDate = ConvertionUtil.convertToDate(objSkipTransferConfirm.getCancelTransTransferDate());
			Date preferred_nextTxnDate = ConvertionUtil.convertToDate(objSkipTransferConfirm.getPrefered_previous_txn_dt());
			Date parentTxnDate = ConvertionUtil.convertToDate(objSkipTransferConfirm.getParentTxnDate());
			Date skippedDate = transferCancelDate;
			if(preferred_nextTxnDate!=null){
				skippedDate = preferred_nextTxnDate;
			}

			String recurringFreq = objSkipTransferConfirm.getRecurringFreq();
			String transferType = objSkipTransferConfirm.getTransactiontype();
			Date nextBusinessDate = MSCommonUtils.evaluateNextTxnDate(recurringFreq,skippedDate,parentTxnDate,transferType,Boolean.FALSE,context);

			objSkipTransferConfirm.setNextTransferAmount(MSCommonUtils.formatTxnAmount(objSkipTransferConfirm.getCancelTransAmount()));
			objSkipTransferConfirm.setNextTransferDateVal(ConvertionUtil.convertToString(nextBusinessDate));
		}
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished postCancelTransferCancelTransfer_RecurringskipBtn"); 
	}

	//Checking the Recurring condition for hiding the Skip button ...
	public static boolean checkRecurringStatusAfterSkip(Object recurringDetails,Date nextTransferDate)
	{
		DsGetCancelPayOutTO objDsGetCancelPayOutTO=(DsGetCancelPayOutTO)recurringDetails;
		int durationSelected = ConvertionUtil.convertToint(objDsGetCancelPayOutTO.getDuration());
		Date untildate = objDsGetCancelPayOutTO.getPayfreqenddate();
		Date nextBusinessDate = nextTransferDate;
		Double acculatedTxnNo =	objDsGetCancelPayOutTO.getAccumulatedTransfers();
		Double maxTxnNo = objDsGetCancelPayOutTO.getPayfreqpaymentcount();
		BigDecimal thresholdAmount = objDsGetCancelPayOutTO.getPayfreqlimit();
		BigDecimal accumulatedAmount = objDsGetCancelPayOutTO.getAccumulatedAmt();
		boolean isRecurringFlag = true;

		if((durationSelected==1) || 
				((durationSelected==2 && !nextBusinessDate.after(untildate))) || 
				((durationSelected==3 && ((accumulatedAmount.compareTo(thresholdAmount)==-1)))) || 
				((durationSelected==4 && ((acculatedTxnNo.doubleValue())< maxTxnNo.doubleValue()))))
		{
			isRecurringFlag = true;
		}
		else
		{
			isRecurringFlag = false;
		}
		return isRecurringFlag;
	}
}
