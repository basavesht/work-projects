/*
 * Created on Tue May 26 15:36:10 IST 2009
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
import com.tcs.Payments.serverValidations.StatusConsistencyChk;
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
public class SkipTransferConfirmDelegateHook extends BusinessDelegateHook {

	/**
	 * Pre hook method for Cancel transfer onload event in skip mode...
	 * @throws Exception 
	 * @throws Exception 
	 */
	public void preSkipTransferConfirmSkipTransferConfirm_INITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preSkipTransferConfirmSkipTransferConfirm_INITINIT"); 
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			ArrayList skipFunRoleList = new ArrayList();

			//Multiple Role List mappings..
			skipFunRoleList.add(ScreenDesc.ICT+PageStateDesc.E+ActionDesc.CNL);
			skipFunRoleList.add(ScreenDesc.ACT+PageStateDesc.E+ActionDesc.CNL);
			skipFunRoleList.add(ScreenDesc.CHQ+PageStateDesc.E+ActionDesc.CNL);
			skipFunRoleList.add(ScreenDesc.APR+PageStateDesc.I+ActionDesc.CNL);
			skipFunRoleList.add(ScreenDesc.SCH+PageStateDesc.I+ActionDesc.CNL);
			objPaymentDetails.setUserFuncRoleList(skipFunRoleList);

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
	public void postSkipTransferConfirmSkipTransferConfirm_INITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postSkipTransferConfirmSkipTransferConfirm_INITINIT");
		try 
		{
			SkipTransferConfirmForm objSkipTransferConfirm=(SkipTransferConfirmForm) objSourceForm;
			DsGetCancelPayOutTO dsgetCancelPayOutTO = ((DsGetCancelPayOutTO)((Object []) objReturn)[1]);
			ServiceContext context= new ServiceContext();
			HashMap txnDetails = new HashMap();

			//Payment Details mappings..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setFrequency_Type("2");
			objPaymentDetails.setTransfer_Type(ConvertionUtil.convertToString(dsgetCancelPayOutTO.getTxn_type()));

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

			//External account mappings..
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO = new DsExtPayeeDetailsOutTO();
			objDsExtPayeeDetailsOutTO.setNick_name(dsgetCancelPayOutTO.getPaypayeename1());
			objDsExtPayeeDetailsOutTO.setAccount_owner(dsgetCancelPayOutTO.getExtAccOwner());
			objDsExtPayeeDetailsOutTO.setCpyaccnum(dsgetCancelPayOutTO.getPayeeaccnum());

			//Loan Account mappings..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			objLoanAcntDetails.setLoanAccount(dsgetCancelPayOutTO.getLoanAcnt());

			txnDetails.put("PaymentDetails",objPaymentDetails);
			txnDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			txnDetails.put("MSToAccDetails",objToMSAcc_Details);
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);
			txnDetails.put("LoanAccountDetails",objLoanAcntDetails);

			//Data Mappings...
			Date transferCancelDate = ConvertionUtil.convertToDate(ConvertionUtil.convertToAppDateStr(dsgetCancelPayOutTO.getRequested_exe_dt()));
			Date preferred_nextTxnDate = ConvertionUtil.convertToDate(ConvertionUtil.convertToAppDateStr(dsgetCancelPayOutTO.getPrefered_previous_txn_dt()));
			int durationSelected = ConvertionUtil.convertToint(dsgetCancelPayOutTO.getDuration());
			String recurringFreq = ConvertionUtil.convertToString(dsgetCancelPayOutTO.getPayfrequency());
			String transferType = ConvertionUtil.convertToString(dsgetCancelPayOutTO.getTxn_type());
			BigDecimal accumulatedAmount = dsgetCancelPayOutTO.getAccumulatedAmt();
			BigDecimal thresholdAmount = dsgetCancelPayOutTO.getPayfreqlimit();
			BigDecimal transferAmount = dsgetCancelPayOutTO.getPaydebitamt();
			Date parentTxnDate = dsgetCancelPayOutTO.getPar_txn_request_dt();
			Date skippedDate = transferCancelDate;
			if(preferred_nextTxnDate!=null)
			{
				skippedDate = preferred_nextTxnDate;
			}

			//Setting the Next transfer date and next transfer amount..
			Date nextBusinessDate = MSCommonUtils.evaluateNextTxnDate(recurringFreq,skippedDate,parentTxnDate,transferType,context);
			BigDecimal nextTxnAmount =  MSCommonUtils.calculateNextTxnAmount(durationSelected,thresholdAmount,accumulatedAmount,transferAmount);
			objSkipTransferConfirm.setNextTransferAmount(MSCommonUtils.formatTxnAmount(nextTxnAmount));
			objSkipTransferConfirm.setNextTransferDateVal(ConvertionUtil.convertToAppDateStr(nextBusinessDate));

			//Setting the Frequency Value.....
			objSkipTransferConfirm.setFrequencyValue("2");
			objSkipTransferConfirm.setCancelTransType(MSCommonUtils.getTxnTypeDesc_View(objPaymentDetails));

			//Setting the local currency by default..
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			objSkipTransferConfirm.setCurrencyCode(currencyCode);

			//From and To account mappings..
			objSkipTransferConfirm.setCancelTransFromAcc (FormatAccount.getDebitAccountDisp(txnDetails));
			objSkipTransferConfirm.setCancelTransToAcc (FormatAccount.getCreditAccountDisp(txnDetails));

			//Checking the status consistency ...
			StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
			ArrayList event_valid_statuses = ((ArrayList)((Object []) objReturn)[2]); 
			String txn_Paystatus = dsgetCancelPayOutTO.getPayccsstatuscode();
			ServiceContext contextData = (ServiceContext)statusConsistency.verifyStatusConsistency(event_valid_statuses,txn_Paystatus);

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
		EBWLogger.trace(this, "Finished postCancelTransferCancelTransfer_RecurringskipBtn"); 
	}


	/**
	 * Pre hook method for Skip next transfer confirm event in init mode...
	 * @throws Exception 
	 */
	public void preSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn"); 
		try
		{
			EBWLogger.trace(this, "Starting preCancelTransferCancelTransfer_RecurringskipBtn"); 
			SkipTransferConfirmForm objSkipTransferConfirm=(SkipTransferConfirmForm) objSourceForm;
			ArrayList skipFunRoleList = new ArrayList();
			Object skipTransferToObj[] = (Object[])objParam[1];

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)skipTransferToObj[0];

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			skipTransferToObj[3] = objMSUserDetails;

			//Payment Details attributes...
			objPaymentDetails.setFrmAcc_InContext(false);
			objPaymentDetails.setToAcc_InContext(false);
			objPaymentDetails.setBusiness_Date(objSkipTransferConfirm.getBusinessDate());
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Cancel);
			objPaymentDetails.setTxnCancelled(true);
			objPaymentDetails.setPrevAction("Cancel");
			objPaymentDetails.setTransactionId(objSkipTransferConfirm.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objSkipTransferConfirm.getVersionnum());
			objPaymentDetails.setTxnBatchId(objSkipTransferConfirm.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objSkipTransferConfirm.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnId(objSkipTransferConfirm.getParentTxnNumber());
			objPaymentDetails.setRecParentTxnVersion(objSkipTransferConfirm.getParTxnVersionnum());
			objPaymentDetails.setExtAccount_RefId(objSkipTransferConfirm.getPayeeId());
			objPaymentDetails.setTxnCurrentStatusCode(objSkipTransferConfirm.getPaymentstatus());
			objPaymentDetails.setTxnPrevStatusCode(objSkipTransferConfirm.getPaymentstatus());
			objPaymentDetails.setCreated_by_comments(objSkipTransferConfirm.getCancelComments());

			//Multiple Role List mappings..
			skipFunRoleList.add(ScreenDesc.ICT+PageStateDesc.E+ActionDesc.CNL);
			skipFunRoleList.add(ScreenDesc.ACT+PageStateDesc.E+ActionDesc.CNL);
			skipFunRoleList.add(ScreenDesc.CHQ+PageStateDesc.E+ActionDesc.CNL);
			skipFunRoleList.add(ScreenDesc.APR+PageStateDesc.I+ActionDesc.CNL);
			skipFunRoleList.add(ScreenDesc.SCH+PageStateDesc.I+ActionDesc.CNL);
			objPaymentDetails.setUserFuncRoleList(skipFunRoleList);

			EBWLogger.trace(this, "Finished preSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn"); 
		}
		catch(Exception exception)
		{
			throw exception;
		}
	}

	/**
	 * Post hook method for Skip next transfer confirm event in init mode...
	 * @throws Exception 
	 */
	public void postSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting postSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn");
			SkipTransferConfirmForm objSkipTransferConfirm=(SkipTransferConfirmForm) objSourceForm;
			CancelTransferConfirmForm objCancelTransferConfirm=(CancelTransferConfirmForm) objTargetForm;
			String pageNavigationFlag = objSkipTransferConfirm.getScreenNavigationFlag();
			objUserPrincipal.setBrErrorMessages(null);

			//Output extraction...
			ArrayList cancelPayOut = (ArrayList)objReturn;
			ServiceContext contextData = (ServiceContext)cancelPayOut.get(0);

			//Response...
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
			EBWLogger.trace(this, "Finished postSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn");
		} 
		catch(Exception exception)
		{
			throw exception;
		}
	}
}
