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

import com.tcs.Payments.ms360Utils.ActionDesc;
import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.FormatAccount;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.PageStateDesc;
import com.tcs.Payments.ms360Utils.ScreenDesc;
import com.tcs.Payments.serverValidations.ValidateSearches;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
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
	 * Pre hook method for Cancel transfer onload event in init mode...
	 * @throws Exception 
	 */
	public void preCancelTransferCancelTransfer_INITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preCancelTransferCancelTransfer_INITINIT"); 
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			ArrayList cancelFunRoleList = new ArrayList();

			//Multiple Role List mappings..
			cancelFunRoleList.add(ScreenDesc.ICT+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.ACT+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.CHQ+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.APR+PageStateDesc.I+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.SCH+PageStateDesc.I+ActionDesc.CNL);
			objPaymentDetails.setUserFuncRoleList(cancelFunRoleList);

			//Server side field validations for FAP...
			ValidateSearches objValidateSearches= new ValidateSearches();
			ServiceContext contextData = objValidateSearches.validatePageAccess(objUserPrincipal,objPaymentDetails);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}

			//Object mapping for getting the business date...
			Object[] cancelTransferTO = (Object[])objParam[1];
			DsConfigDetailsTO objDsConfigDetails = (DsConfigDetailsTO)cancelTransferTO[0];
			String qzBranchCode = PropertyFileReader.getProperty("OU_ID"); 
			objDsConfigDetails.setBranch_code(qzBranchCode);
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preCancelTransferCancelTransfer_INITINIT"); 
	}

	/**
	 * Post hook method for Cancel transfer onload event in init mode...
	 * @throws Exception 
	 */
	public void postCancelTransferCancelTransfer_INITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postCancelTransferCancelTransfer_INITINIT");
		try 
		{
			//Initialization...
			HashMap txnDetails = new HashMap();
			CancelTransferForm objCancelTransfer=(CancelTransferForm) objSourceForm;
			String pageNavigationFlag = objCancelTransfer.getScreenNavigationFlag();
			DsGetCancelPayOutTO dsgetCancelPayOutTO = ((DsGetCancelPayOutTO)((Object []) objReturn)[1]); 
			DsConfigDetailsOut dsConfigDetailsOutTO = ((DsConfigDetailsOut)((Object []) objReturn)[0]);
			ArrayList event_valid_statuses = ((ArrayList)((Object []) objReturn)[2]); 

			//Required Payment Details Mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setTxnCancelled(true);
			objPaymentDetails.setFrequency_Type("1");
			objPaymentDetails.setTransfer_Type(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getTxn_type()));
			objPaymentDetails.setTxnCurrentStatusCode(dsgetCancelPayOutTO.getPayccsstatuscode());
			objPaymentDetails.setRequestedDate(ConvertionUtil.convertToAppDateStr(dsgetCancelPayOutTO.getRequested_exe_dt()));
			objPaymentDetails.setBusiness_Date(ConvertionUtil.convertToAppDateStr(dsConfigDetailsOutTO.getBusiness_date()));
			objPaymentDetails.setActualExecDate(ConvertionUtil.convertToAppDateStr(dsgetCancelPayOutTO.getActual_exe_dt()));

			//From account mappings..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(dsgetCancelPayOutTO.getFrombr_acct_no_fa()!=null){
				objFromMSAcc_Details.setAccountNumber(dsgetCancelPayOutTO.getFrombr_acct_no_fa().substring(3,9));
				objFromMSAcc_Details.setOfficeNumber(dsgetCancelPayOutTO.getFrombr_acct_no_fa().substring(0,3));
				objFromMSAcc_Details.setFaNumber(dsgetCancelPayOutTO.getFrombr_acct_no_fa().substring(9));
			}

			//To account mappings..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(dsgetCancelPayOutTO.getTobr_acct_no_fa()!=null){
				objToMSAcc_Details.setAccountNumber(dsgetCancelPayOutTO.getTobr_acct_no_fa().substring(3,9));
				objToMSAcc_Details.setOfficeNumber(dsgetCancelPayOutTO.getTobr_acct_no_fa().substring(0,3));
				objToMSAcc_Details.setFaNumber(dsgetCancelPayOutTO.getTobr_acct_no_fa().substring(9));
			}

			//CheckRequest TO Mappings..
			CheckRequestTO objChkRequest = new CheckRequestTO();
			objChkRequest.setPayeeName(dsgetCancelPayOutTO.getCheckPayeeToName());

			//External account mappings..
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO = new DsExtPayeeDetailsOutTO();
			objDsExtPayeeDetailsOutTO.setNick_name(dsgetCancelPayOutTO.getPaypayeename1());
			objDsExtPayeeDetailsOutTO.setAccount_owner(dsgetCancelPayOutTO.getExtAccOwner());
			objDsExtPayeeDetailsOutTO.setCpyaccnum(dsgetCancelPayOutTO.getPayeeaccnum());

			//Loan Account mappings..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			objLoanAcntDetails.setLoanAccount(dsgetCancelPayOutTO.getLoanAcnt());

			//Creating a HashMap..
			txnDetails.put("PaymentDetails",objPaymentDetails);
			txnDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			txnDetails.put("MSToAccDetails",objToMSAcc_Details);
			txnDetails.put("CheckDetails",objChkRequest);
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);
			txnDetails.put("LoanAccountDetails",objLoanAcntDetails);

			//Validate cancel request...
			ValidateSearches validateTxn = new ValidateSearches();
			ServiceContext contextData = (ServiceContext)validateTxn.validateCancelRequest(txnDetails,event_valid_statuses);
			MSCommonUtils.logEventResponse(contextData);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}
			else if(contextData.getMaxSeverity()==MessageType.SEVERE) 
			{
				//Extraction....
				JSONArray jsonError = MSCommonUtils.extractContextSevereErrMsg(contextData,true);

				//JSON Mapping....
				HashMap busniessErrorMessages = new HashMap();
				busniessErrorMessages.put("BusinessErrors", jsonError);
				JSONObject jsonErrorMess = JSONObject.fromObject(busniessErrorMessages);

				//Settings..
				objCancelTransfer.setBrErrors(true);
				objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
				if(pageNavigationFlag!=null && pageNavigationFlag.equalsIgnoreCase("1")){
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_SEARCHES);
				}
				else {
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_APPROVALS);
				}
				return;
			}

			//Setting the Frequency Value.....
			objCancelTransfer.setFrequencyValue("1");
			objCancelTransfer.setCancelTransFrequency(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));
			objCancelTransfer.setCancelTransType(MSCommonUtils.getTxnTypeDesc_View(objPaymentDetails));

			//Setting the local currency by default..
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			objCancelTransfer.setCurrencyCode(currencyCode);

			//Setting the From account and To account Details...
			objCancelTransfer.setCancelTransFromAcc (FormatAccount.getDebitAccountDisp(txnDetails));
			objCancelTransfer.setCancelTransToAcc (FormatAccount.getCreditAccountDisp(txnDetails));

		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postCancelTransferCancelTransfer_INITINIT"); 
	}

	/**
	 * Pre hook method for Cancel transfer cancel event in init mode...
	 * @throws Exception 
	 */
	public void preCancelTransferCancelTransfer_INITcancelBtn(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preCancelTransferCancelTransfer_INITcancelBtn"); 
		try 
		{
			CancelTransferForm objCancelTransfer=(CancelTransferForm) objSourceForm;
			Object cancelTransferToObj[] = (Object[])objParam[1];
			ArrayList cancelFunRoleList = new ArrayList();

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)cancelTransferToObj[0];

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			cancelTransferToObj[3] = objMSUserDetails;

			//Payment Details attributes...
			objPaymentDetails.setFrmAcc_InContext(false);
			objPaymentDetails.setToAcc_InContext(false);
			objPaymentDetails.setBusiness_Date(objCancelTransfer.getBusinessDate());
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Cancel);
			objPaymentDetails.setTxnCancelled(true);
			objPaymentDetails.setPrevAction("Cancel");
			objPaymentDetails.setTransfer_Type(objCancelTransfer.getTransactiontype());
			objPaymentDetails.setTransactionId(objCancelTransfer.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objCancelTransfer.getVersionnum());
			objPaymentDetails.setTxnBatchId(objCancelTransfer.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objCancelTransfer.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnId(objCancelTransfer.getParentTxnNumber());
			objPaymentDetails.setRecParentTxnVersion(objCancelTransfer.getParTxnVersionnum());
			objPaymentDetails.setExtAccount_RefId(objCancelTransfer.getPayeeId());
			objPaymentDetails.setTxnCurrentStatusCode(objCancelTransfer.getPaymentstatus());
			objPaymentDetails.setTxnPrevStatusCode(objCancelTransfer.getPaymentstatus());
			objPaymentDetails.setCreated_by_comments(objCancelTransfer.getCancelComments());

			//Multiple Role List mappings..
			cancelFunRoleList.add(ScreenDesc.ICT+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.ACT+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.CHQ+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.APR+PageStateDesc.I+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.SCH+PageStateDesc.I+ActionDesc.CNL);
			objPaymentDetails.setUserFuncRoleList(cancelFunRoleList);

			EBWLogger.trace(this, "Finished preCancelTransferCancelTransfer_INITcancelBtn");

		} catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Post hook method for Cancel transfer cancel event in init mode...
	 * @throws Exception 
	 */
	public void postCancelTransferCancelTransfer_INITcancelBtn(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postCancelTransferCancelTransfer_INITcancelBtn");
		try 
		{
			CancelTransferForm objCancelTransfer=(CancelTransferForm) objSourceForm;
			CancelTransferConfirmForm objCancelTransferConfirm=(CancelTransferConfirmForm) objTargetForm;
			String pageNavigationFlag = objCancelTransfer.getScreenNavigationFlag();
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
					if(pageNavigationFlag!=null && pageNavigationFlag.equalsIgnoreCase("1")){
						objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_SEARCHES);
					}
					else {
						objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_APPROVALS);
					}
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
					if(pageNavigationFlag!=null && pageNavigationFlag.equalsIgnoreCase("1")){
						objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_SEARCHES);
					}
					else {
						objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_APPROVALS);
					}
				}
				else if(cancelPayOut.get(1)!=null)
				{
					HashMap txnOutDetails = (HashMap)cancelPayOut.get(1);
					PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
					if(txnOutDetails.containsKey("PaymentDetails")){
						objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
					}

					objCancelTransferConfirm.setCancelConfirmationNo(objPaymentDetails.getTransactionId());
					objCancelTransferConfirm.setScreenNavigationFlag(pageNavigationFlag);
				}
			}
			EBWLogger.trace(this, "Finished postCancelTransferCancelTransfer_INITcancelBtn");
		} catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Pre hook method for Cancel transfer onload event in recurring mode...
	 * @throws Exception 
	 */
	public void preCancelTransferCancelTransfer_RecurringINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preCancelTransferCancelTransfer_RecurringINIT"); 
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			ArrayList cancelFunRoleList = new ArrayList();

			//Multiple Role List mappings..
			cancelFunRoleList.add(ScreenDesc.ICT+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.ACT+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.CHQ+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.APR+PageStateDesc.I+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.SCH+PageStateDesc.I+ActionDesc.CNL);
			objPaymentDetails.setUserFuncRoleList(cancelFunRoleList);

			//Server side field validations for FAP...
			ValidateSearches objValidateSearches= new ValidateSearches();
			ServiceContext contextData = objValidateSearches.validatePageAccess(objUserPrincipal,objPaymentDetails);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}

			//Object mapping for getting the business date..
			Object[] cancelTransferTO = (Object[])objParam[1];
			DsConfigDetailsTO objDsConfigDetails = (DsConfigDetailsTO)cancelTransferTO[0];
			String qzBranchCode = PropertyFileReader.getProperty("OU_ID"); 
			objDsConfigDetails.setBranch_code(qzBranchCode);
		} 
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preCancelTransferCancelTransfer_RecurringINIT"); 
	}

	/**
	 * Post hook method for Cancel transfer onload event in recurring mode...
	 * @throws Exception 
	 */
	public void postCancelTransferCancelTransfer_RecurringINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postCancelTransferCancelTransfer_RecurringINIT");
		try 
		{
			//Initialization...
			HashMap txnDetails = new HashMap();
			CancelTransferForm objCancelTransfer=(CancelTransferForm) objSourceForm;
			String pageNavigationFlag = objCancelTransfer.getScreenNavigationFlag();
			ServiceContext context = new ServiceContext();
			DsGetCancelPayOutTO dsgetCancelPayOutTO = ((DsGetCancelPayOutTO)((Object []) objReturn)[1]); 
			DsConfigDetailsOut dsConfigDetailsOutTO = ((DsConfigDetailsOut)((Object []) objReturn)[0]);

			//Required Payment Details Mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setTxnCancelled(true);
			objPaymentDetails.setFrequency_Type("2");
			objPaymentDetails.setDuration_AmountLimit(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayfreqlimit()));
			objPaymentDetails.setDuration_EndDate(ConvertionUtil.convertToAppDateStr(dsgetCancelPayOutTO.getPayfreqenddate()));
			objPaymentDetails.setDuration_NoOfTransfers(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayfreqpaymentcount()));
			objPaymentDetails.setFrequency_DurationValue(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getDuration()));
			objPaymentDetails.setFrequency_DurationDesc(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayfrequency()));
			objPaymentDetails.setTransfer_Type(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getTxn_type()));
			objPaymentDetails.setTxnCurrentStatusCode(dsgetCancelPayOutTO.getPayccsstatuscode());
			objPaymentDetails.setRequestedDate(ConvertionUtil.convertToAppDateStr(dsgetCancelPayOutTO.getRequested_exe_dt()));
			objPaymentDetails.setBusiness_Date(ConvertionUtil.convertToAppDateStr(dsConfigDetailsOutTO.getBusiness_date()));
			objPaymentDetails.setActualExecDate(ConvertionUtil.convertToAppDateStr(dsgetCancelPayOutTO.getActual_exe_dt()));

			//From account mappings..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(dsgetCancelPayOutTO.getFrombr_acct_no_fa()!=null){
				objFromMSAcc_Details.setAccountNumber(dsgetCancelPayOutTO.getFrombr_acct_no_fa().substring(3,9));
				objFromMSAcc_Details.setOfficeNumber(dsgetCancelPayOutTO.getFrombr_acct_no_fa().substring(0,3));
				objFromMSAcc_Details.setFaNumber(dsgetCancelPayOutTO.getFrombr_acct_no_fa().substring(9));
			}

			//To account mappings..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(dsgetCancelPayOutTO.getTobr_acct_no_fa()!=null){
				objToMSAcc_Details.setAccountNumber(dsgetCancelPayOutTO.getTobr_acct_no_fa().substring(3,9));
				objToMSAcc_Details.setOfficeNumber(dsgetCancelPayOutTO.getTobr_acct_no_fa().substring(0,3));
				objToMSAcc_Details.setFaNumber(dsgetCancelPayOutTO.getTobr_acct_no_fa().substring(9));
			}

			//CheckRequest TO Mappings..
			CheckRequestTO objChkRequest = new CheckRequestTO();
			objChkRequest.setPayeeName(dsgetCancelPayOutTO.getCheckPayeeToName());

			//External account mappings..
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO = new DsExtPayeeDetailsOutTO();
			objDsExtPayeeDetailsOutTO.setNick_name(dsgetCancelPayOutTO.getPaypayeename1());
			objDsExtPayeeDetailsOutTO.setAccount_owner(dsgetCancelPayOutTO.getExtAccOwner());
			objDsExtPayeeDetailsOutTO.setCpyaccnum(dsgetCancelPayOutTO.getPayeeaccnum());

			//Loan Account mappings..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			objLoanAcntDetails.setLoanAccount(dsgetCancelPayOutTO.getLoanAcnt());

			//Creating HashMap..
			txnDetails.put("PaymentDetails",objPaymentDetails);
			txnDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			txnDetails.put("MSToAccDetails",objToMSAcc_Details);
			txnDetails.put("CheckDetails",objChkRequest);
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);
			txnDetails.put("LoanAccountDetails",objLoanAcntDetails);

			//Validate cancel request...
			ValidateSearches validateTxn = new ValidateSearches();
			ArrayList event_valid_statuses = ((ArrayList)((Object []) objReturn)[2]); 
			ServiceContext contextData = (ServiceContext)validateTxn.validateCancelRequest(txnDetails,event_valid_statuses);
			MSCommonUtils.logEventResponse(contextData);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}
			else if(contextData.getMaxSeverity()==MessageType.SEVERE) 
			{
				//Extraction....
				JSONArray jsonError = MSCommonUtils.extractContextSevereErrMsg(contextData,true);

				//JSON Mapping....
				HashMap busniessErrorMessages = new HashMap();
				busniessErrorMessages.put("BusinessErrors", jsonError);
				JSONObject jsonErrorMess = JSONObject.fromObject(busniessErrorMessages);

				//Settings..
				objCancelTransfer.setBrErrors(true);
				objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
				if(pageNavigationFlag!=null && pageNavigationFlag.equalsIgnoreCase("1")){
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_SEARCHES);
				}
				else {
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_APPROVALS);
				}
				return;
			}

			//Setting the Frequency Value.....
			objCancelTransfer.setFrequencyValue("2");
			objCancelTransfer.setCancelTransFrequency(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));
			objCancelTransfer.setCancelTransRepeat(MSCommonUtils.getRepeatDesc_View(objPaymentDetails));
			objCancelTransfer.setCancelTransType(MSCommonUtils.getTxnTypeDesc_View(objPaymentDetails));

			//From and To account mappings..
			objCancelTransfer.setCancelTransFromAcc (FormatAccount.getDebitAccountDisp(txnDetails));
			objCancelTransfer.setCancelTransToAcc (FormatAccount.getCreditAccountDisp(txnDetails));

			//Setting the local currency by default..
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			objCancelTransfer.setCurrencyCode(currencyCode);

			//Checking the recurring criteria . If Recurring condition is not satisfied then disable the Skip Transfer Button...
			boolean isRecurring= true;
			String frequencyValue = dsgetCancelPayOutTO.getPayfrequency();
			Date parentTxnDate = dsgetCancelPayOutTO.getPar_txn_request_dt();
			Date currentTransferDate = dsgetCancelPayOutTO.getPrefered_previous_txn_dt(); // Transfer date without considering the holidays...
			String txn_type = dsgetCancelPayOutTO.getTxn_type();
			if(currentTransferDate==null){
				currentTransferDate = dsgetCancelPayOutTO.getRequested_exe_dt();
			}
			Date nextBusinessDate = MSCommonUtils.evaluateNextTxnDate(frequencyValue,currentTransferDate,parentTxnDate,txn_type,context);
			isRecurring = checkRecurringStatusAfterSkip(dsgetCancelPayOutTO,nextBusinessDate);
			if(!isRecurring){
				objCancelTransfer.setState("CancelTransfer_LastRecurring");
			}

			//Checking for the Dont_Spawn_flag and disabling the Skip transfer button accordingly..
			String dont_spawn_flag = dsgetCancelPayOutTO.getDont_spawn_flag();
			if(dont_spawn_flag!=null && dont_spawn_flag.equalsIgnoreCase("Y")){
				objCancelTransfer.setState("CancelTransfer_LastRecurring");
			}

			//Checking for the Status code and disabling the Skip transfer button accordingly..
			String status_code = dsgetCancelPayOutTO.getPayccsstatuscode();
			if(status_code!=null && status_code.equalsIgnoreCase("4")){
				objCancelTransfer.setState("CancelTransfer_LastRecurring");
			}

		} 
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished postCancelTransferCancelTransfer_RecurringINIT"); 
	}

	/**
	 * Pre hook method for Cancel transfer cancel event in recurring mode...
	 * @throws Exception 
	 */
	public void preCancelTransferCancelTransfer_RecurringcancelBtn(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preCancelTransferCancelTransfer_RecurringcancelBtn"); 
		try 
		{
			CancelTransferForm objCancelTransfer=(CancelTransferForm) objSourceForm;
			Object cancelTransferToObj[] = (Object[])objParam[1];
			ArrayList cancelFunRoleList = new ArrayList();

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)cancelTransferToObj[0];

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			cancelTransferToObj[3] = objMSUserDetails;

			//Payment Details attributes...
			objPaymentDetails.setFrmAcc_InContext(false);
			objPaymentDetails.setToAcc_InContext(false);
			objPaymentDetails.setBusiness_Date(objCancelTransfer.getBusinessDate());
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Cancel);
			objPaymentDetails.setTxnCancelled(true);
			objPaymentDetails.setPrevAction("Cancel");
			objPaymentDetails.setTransfer_Type(objCancelTransfer.getTransactiontype());
			objPaymentDetails.setTransactionId(objCancelTransfer.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objCancelTransfer.getVersionnum());
			objPaymentDetails.setTxnBatchId(objCancelTransfer.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objCancelTransfer.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnId(objCancelTransfer.getParentTxnNumber());
			objPaymentDetails.setRecParentTxnVersion(objCancelTransfer.getParTxnVersionnum());
			objPaymentDetails.setExtAccount_RefId(objCancelTransfer.getPayeeId());
			objPaymentDetails.setTxnCurrentStatusCode(objCancelTransfer.getPaymentstatus());
			objPaymentDetails.setTxnPrevStatusCode(objCancelTransfer.getPaymentstatus());
			objPaymentDetails.setCreated_by_comments(objCancelTransfer.getCancelComments());

			//Multiple Role List mappings..
			cancelFunRoleList.add(ScreenDesc.ICT+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.ACT+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.CHQ+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.APR+PageStateDesc.I+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.SCH+PageStateDesc.I+ActionDesc.CNL);
			objPaymentDetails.setUserFuncRoleList(cancelFunRoleList);
		}
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preCancelTransferCancelTransfer_RecurringcancelBtn"); 
	}

	/**
	 * Post hook method for Cancel transfer cancel event in recurring mode...
	 * @throws Exception 
	 */
	public void postCancelTransferCancelTransfer_RecurringcancelBtn(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postCancelTransferCancelTransfer_RecurringcancelBtn");
		try 
		{
			CancelTransferForm objCancelTransfer=(CancelTransferForm) objSourceForm;
			CancelTransferConfirmForm objCancelTransferConfirm=(CancelTransferConfirmForm) objTargetForm;
			objUserPrincipal.setBrErrorMessages(null);
			String pageNavigationFlag = objCancelTransfer.getScreenNavigationFlag();

			//Output Extraction...
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
					if(pageNavigationFlag!=null && pageNavigationFlag.equalsIgnoreCase("1")){
						objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_SEARCHES);
					}
					else {
						objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_APPROVALS);
					}
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
					if(pageNavigationFlag!=null && pageNavigationFlag.equalsIgnoreCase("1")){
						objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_SEARCHES);
					}
					else {
						objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_APPROVALS);
					}
				}
				else if(cancelPayOut.get(1)!=null)
				{
					HashMap txnOutDetails = (HashMap)cancelPayOut.get(1);
					PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
					if(txnOutDetails.containsKey("PaymentDetails")){
						objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
					}

					objCancelTransferConfirm.setScreenNavigationFlag(pageNavigationFlag);
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
	 * Pre hook method for Cancel transfer onload event in skip mode from Recurring transfer mode...
	 * @throws Exception 
	 * @throws Exception 
	 */
	public void preCancelTransferCancelTransfer_RecurringskipBtn(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preCancelTransferCancelTransfer_RecurringskipBtn"); 
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			ArrayList cancelFunRoleList = new ArrayList();

			//Multiple Role List mappings..
			cancelFunRoleList.add(ScreenDesc.ICT+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.ACT+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.CHQ+PageStateDesc.E+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.APR+PageStateDesc.I+ActionDesc.CNL);
			cancelFunRoleList.add(ScreenDesc.SCH+PageStateDesc.I+ActionDesc.CNL);
			objPaymentDetails.setUserFuncRoleList(cancelFunRoleList);

			//Server side field validations for FAP...
			ValidateSearches objValidateSearches= new ValidateSearches();
			ServiceContext contextData = objValidateSearches.validatePageAccess(objUserPrincipal,objPaymentDetails);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}
		}
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preCancelTransferCancelTransfer_RecurringskipBtn"); 
	}

	/**
	 * Post hook method for Cancel transfer onload event in skip mode...
	 * @throws Exception 
	 */
	public void postCancelTransferCancelTransfer_RecurringskipBtn(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postCancelTransferCancelTransfer_RecurringskipBtn");
		try 
		{
			ServiceContext context = new ServiceContext();
			SkipTransferConfirmForm objSkipTransferConfirm=(SkipTransferConfirmForm) objTargetForm;

			//Initialization...
			Date transferCancelDate = ConvertionUtil.convertToDate(objSkipTransferConfirm.getCancelTransTransferDate());
			Date preferred_nextTxnDate = ConvertionUtil.convertToDate(objSkipTransferConfirm.getPrefered_previous_txn_dt());
			String recurringFreq = objSkipTransferConfirm.getRecurringFreq();
			String transferType = objSkipTransferConfirm.getTransactiontype();
			BigDecimal accumulatedAmount = ConvertionUtil.convertToBigDecimal(objSkipTransferConfirm.getCancelTransDollarsTransfered());
			BigDecimal thresholdAmount = ConvertionUtil.convertToBigDecimal(objSkipTransferConfirm.getThreasholdAmount());
			BigDecimal transferAmount = ConvertionUtil.convertToBigDecimal(objSkipTransferConfirm.getCancelTransAmount());
			int durationSelected = ConvertionUtil.convertToint(objSkipTransferConfirm.getDurationValue());
			Date parentTxnDate = ConvertionUtil.convertToDate(objSkipTransferConfirm.getParentTxnDate());
			Date skippedDate = transferCancelDate;
			if(preferred_nextTxnDate!=null){
				skippedDate = preferred_nextTxnDate;
			}

			Date nextBusinessDate = MSCommonUtils.evaluateNextTxnDate(recurringFreq,skippedDate,parentTxnDate,transferType,context);
			BigDecimal nextTxnAmount =  MSCommonUtils.calculateNextTxnAmount(durationSelected,thresholdAmount,accumulatedAmount,transferAmount);
			objSkipTransferConfirm.setNextTransferAmount(MSCommonUtils.formatTxnAmount(nextTxnAmount));
			objSkipTransferConfirm.setNextTransferDateVal(ConvertionUtil.convertToAppDateStr(nextBusinessDate));
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
