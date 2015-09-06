/*
 * Created on Tue Mar 24 16:17:22 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.FormatAccount;
import com.tcs.Payments.ms360Utils.InitiatorRoleDesc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.Payments.serverValidations.StatusConsistencyChk;
import com.tcs.Payments.serverValidations.ValidateINTTransfer;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.channels.integration.MMAccount;
import com.tcs.bancs.channels.integration.MMContext;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.businessdelegate.BusinessDelegateHook;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;

import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.DsConfigDetailsTO;
import com.tcs.ebw.payments.transferobject.DsGetEditTransferInTO;
import com.tcs.ebw.payments.transferobject.DsGetEditTransferOutTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 * **********************************************************
 */
public class InternalTransferDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for InternalTransfer Screen, InternalTransfer_INIT State and INIT Event. 
	 * @throws Exception 
	 */
	public void preInternalTransferInternalTransfer_INITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preInternalTransferInternalTransfer_INITINIT"); 
			InternalTransferForm objInternalTransfer=(InternalTransferForm) objSourceForm;
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();	
			MMContext objMMContextData = objUserPrincipal.getContextData();

			//Input Mappings for the Business Date...
			Object[] internalTrInitTO = (Object[])objParam[1];
			DsConfigDetailsTO objDsConfigDetails = (DsConfigDetailsTO)internalTrInitTO[0];
			String qzBranchCode = PropertyFileReader.getProperty("OU_ID"); 
			objDsConfigDetails.setBranch_code(qzBranchCode);

			//Setting loginUserName for showing on header...
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			String userName = MSCommonUtils.extractCurrentUserName(objMSUserDetails);
			objInternalTransfer.setLoginUserName(userName);

			EBWLogger.trace(this, "Finished preInternalTransferInternalTransfer_INITINIT");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}
	public void postInternalTransferInternalTransfer_INITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try {
			EBWLogger.trace(this, "Starting postInternalTransferInternalTransfer_INITINIT");
			InternalTransferForm objInternalTransfer=(InternalTransferForm) objSourceForm;

			//Setting the local currency by default..
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			objInternalTransfer.setCurrencyCode(currencyCode);

			EBWLogger.trace(this, "Finished postInternalTransferInternalTransfer_INITINIT"); 
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Method for InternalTransfer Screen, InternalTransfer_INIT State and submitbut Event. 
	 * @throws Exception 
	 */
	public void preInternalTransferInternalTransfer_INITsubmitbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preInternalTransferInternalTransfer_INITsubmitbut"); 
		try 
		{
			InternalTransferForm objInternalTransfer=(InternalTransferForm) objSourceForm;
			Object intTransferToObj[] = (Object[])objParam[1];

			//Server side field validations...
			ValidateINTTransfer objVaidateIntTransfer= new ValidateINTTransfer();
			String validationErr = objVaidateIntTransfer.validateINTTransferFrm(objInternalTransfer);
			if(validationErr!=null && !validationErr.trim().equalsIgnoreCase("")){
				throw new Exception(validationErr); // Form field validation failed ...
			}

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			String fromKeyAccount =objInternalTransfer.getFromAccount(); //Value stores the MS key account number..
			String toKeyAccount =objInternalTransfer.getToAccount(); //Value stores the MS key account number..
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)intTransferToObj[0];
			String transferType = TxnTypeCode.INTERNAL;

			//From/To account object mappings..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();

			MMContext objMMContextData = objUserPrincipal.getContextData();
			ArrayList objMMContextAcc = objMMContextData.getAccounts();
			for(int i =0; i<objMMContextData.getAccounts().size();i++)
			{
				MMAccount objMMAccount = (MMAccount)objMMContextAcc.get(i);
				objMMAccount = MSCommonUtils.getMSAccFormat(objMMAccount); //Formatting the FA,Office,Account Number ...
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(fromKeyAccount)))
				{
					//Setting From account details...
					objPaymentDetails.setFrom_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setFrom_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setFrmAcc_InContext(true);

					objFromMSAcc_Details = objMSCommonUtils.setFromMSAccDetailsTO(objMMAccount);
					intTransferToObj[1] = objFromMSAcc_Details;
				}
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(toKeyAccount)))
				{
					//Setting To account details...
					objPaymentDetails.setTo_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setTo_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setToAcc_InContext(true);

					objToMSAcc_Details = objMSCommonUtils.setToMSAccDetailsTO(objMMAccount);
					intTransferToObj[2]= objToMSAcc_Details;
				}
			}

			//Loan Or Other MS Account extraction process..
			if(!objPaymentDetails.isToAcc_InContext()){
				if(toKeyAccount!=null && toKeyAccount.startsWith(MSSystemDefaults.LOAN_ACC_IND_REGX)) {
					//PLA Account..
					String loanAcnt = toKeyAccount.split(MSSystemDefaults.LOAN_ACC_IND_REGX)[1];
					objPaymentDetails.setLoanAcntNo(loanAcnt);
					transferType = TxnTypeCode.PORTFOLIO_LOAN;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			String lifeTimeUserId=objMSUserDetails.getUuid();
			String userRole=null;
			boolean isFA= objMSUserDetails.isFA();
			if(isFA)
				userRole =InitiatorRoleDesc.Client; //Business rule User/Group mapping for the FA login
			else
				userRole =InitiatorRoleDesc.Client;
			intTransferToObj[3] = objMSUserDetails;

			//Setting the Date Parameters for the First business Day or Last Business Day selection 
			if(objInternalTransfer.getFrequencyValue().equalsIgnoreCase("2") && (objInternalTransfer.getFrequencycombo().equalsIgnoreCase("FBD") || objInternalTransfer.getFrequencycombo().equalsIgnoreCase("LBD")))	
			{
				//Set the Initiation Date in the Hidden field to store the same in the Payment Date .. 
				objInternalTransfer.setFirstLastInitiationDate(objInternalTransfer.getInitiationDate());

				//Calculating the Initiation & Estimated Arrival date  for the FBD and LBD , 
				String paymentInitiationDate = objInternalTransfer.getInitiationDate();
				Date paymentInitDateVal = ConvertionUtil.convertToDate(paymentInitiationDate);
				String recurringFreq = objInternalTransfer.getFrequencycombo();

				//Initiation & Estimated Arrival Date in case of FBD or LBD...
				Date startBusinessDate = MSCommonUtils.calculateStartBusinessDate(recurringFreq,paymentInitDateVal,transferType,Boolean.FALSE);
				objInternalTransfer.setInitiationDate(ConvertionUtil.convertToString(startBusinessDate));
				objInternalTransfer.setEstArrivalDate(ConvertionUtil.convertToString(startBusinessDate));
			}

			objPaymentDetails.setTransfer_Amount(objInternalTransfer.getPaymentamount());
			objPaymentDetails.setChildTxnAmount(objInternalTransfer.getPaymentamount());
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setFxRate(null);
			objPaymentDetails.setInitiation_Date(objInternalTransfer.getInitiationDate());
			objPaymentDetails.setRequestedDate(objInternalTransfer.getInitiationDate());
			objPaymentDetails.setEstimatedArrivalDate(objInternalTransfer.getEstArrivalDate());
			objPaymentDetails.setFrequency_Type(objInternalTransfer.getFrequencyValue());
			objPaymentDetails.setFrequency_DurationDesc(objInternalTransfer.getFrequencycombo());
			objPaymentDetails.setFrequency_DurationValue(objInternalTransfer.getDurationValue());
			objPaymentDetails.setDuration_EndDate(objInternalTransfer.getDurationenddate());
			objPaymentDetails.setDuration_NoOfTransfers(objInternalTransfer.getDurationNoOfTransfers());
			objPaymentDetails.setDuration_AmountLimit(objInternalTransfer.getDurationdollarlimit());
			objPaymentDetails.setBusiness_Date(objInternalTransfer.getBusinessDate());
			objPaymentDetails.setTransfer_Type(transferType);
			objPaymentDetails.setScreen_Type(transferType);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setTrial_depo("N");
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Create_PreConfirm);
			objPaymentDetails.setTxnInitiated(true);
			objPaymentDetails.setPrevAction("Create");
			objPaymentDetails.setInitiator_id(lifeTimeUserId);
			objPaymentDetails.setInitiator_role(userRole);
			objPaymentDetails.setCurrent_owner_id(lifeTimeUserId);
			objPaymentDetails.setCurrent_owner_role(userRole);

			EBWLogger.trace(this, "Finished preInternalTransferInternalTransfer_INITsubmitbut");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}

	public void postInternalTransferInternalTransfer_INITsubmitbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting postInternalTransferInternalTransfer_INITsubmitbut");
			InternalPreConfirmForm objInternalPreConfirm=(InternalPreConfirmForm) objTargetForm;
			InternalTransferForm objInternalTransfer=(InternalTransferForm) objSourceForm;
			objUserPrincipal.setBrErrorMessages(null); 
			String brMessages="";

			//Setting the Date Parameters for the First business Day or Last business Day selection in Pre Confirm Screen...
			if(objInternalPreConfirm.getFrequencyValue().equalsIgnoreCase("2") && (objInternalPreConfirm.getFrequencycombo().equalsIgnoreCase("FBD") || objInternalPreConfirm.getFrequencycombo().equalsIgnoreCase("LBD")))	
			{
				// Set the Initiation Date in the Hidden field to store the same in the Payment Date .. 
				objInternalPreConfirm.setFirstLastInitiationDate(objInternalTransfer.getFirstLastInitiationDate());
				objInternalPreConfirm.setInitiationDate(objInternalTransfer.getInitiationDate());
				objInternalPreConfirm.setEstArrivalDate(objInternalTransfer.getEstArrivalDate());
			}

			//Output extraction...
			ArrayList createSubmitDetails = (ArrayList)objReturn;
			ServiceContext contextData = (ServiceContext)createSubmitDetails.get(0);

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
					objInternalPreConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.INTERNAL_TRANSFERS);
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
					objInternalPreConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.INTERNAL_TRANSFERS);
				}
				else
				{
					if(contextData.getMaxSeverity()==MessageType.WARNING || contextData.getMaxSeverity()==MessageType.INFORMATION)
					{
						HashMap<String, JSONArray> busniessRuleMessages = new HashMap<String, JSONArray>();

						//Warnings Message Extraction....
						JSONArray jsonWarning = MSCommonUtils.extractContextWarningMsg(contextData,true);
						if(!jsonWarning.isEmpty()){
							objInternalPreConfirm.setBrWarnings(true);
						}
						busniessRuleMessages.put("BusinessWarnings", jsonWarning);

						//Information Message Extraction...
						JSONArray jsonInformation = MSCommonUtils.extractContextInfoMsg(contextData,true);
						busniessRuleMessages.put("BusinessInformations", jsonInformation);

						//JSON Output Mapping...
						JSONObject jsonBRObject = JSONObject.fromObject(busniessRuleMessages);

						//Settings..
						brMessages = jsonBRObject.toString();
					}
					//Setting the Initiation Date and Estimated Arrival Date in case of the Cut Off Time failure ...
					if(createSubmitDetails.get(1)!=null)
					{
						//Payment Details ..
						HashMap txnOutDetails = (HashMap)createSubmitDetails.get(1);
						PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
						if(txnOutDetails.containsKey("PaymentDetails")){
							objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
						}

						//From Account Details ..
						FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
						if(txnOutDetails.containsKey("MSFromAccDetails")){
							objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnOutDetails.get("MSFromAccDetails");
						}

						//To Account Details ..
						ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
						if(txnOutDetails.containsKey("MSToAccDetails")){
							objToMSAcc_Details = (ToMSAcc_DetailsTO)txnOutDetails.get("MSToAccDetails");
						}

						//Loan Account Details ..
						PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
						if(txnOutDetails.containsKey("LoanAccountDetails")){
							objLoanAcntDetails = (PortfolioLoanAccount)txnOutDetails.get("LoanAccountDetails");
						}

						//If the ArrayList is not Empty then we need to update the Initiation Date and Estimated Arrival Date since Cut Off Time has Failed...
						objInternalPreConfirm.setInitiationDate(objPaymentDetails.getRequestedDate());
						objInternalPreConfirm.setEstArrivalDate(objPaymentDetails.getEstimatedArrivalDate());
						objInternalPreConfirm.setPaymentamount(MSCommonUtils.formatTxnAmount(objPaymentDetails.getTransfer_Amount()));
						objInternalPreConfirm.setTransactionType(objPaymentDetails.getTransfer_Type());

						//Setting the frequency and duration attributes in case of recurring transfers...
						objInternalPreConfirm.setFrequencyradio(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));
						objInternalPreConfirm.setDurationradio(MSCommonUtils.getRepeatDesc_View(objPaymentDetails));

						//Payment Details..
						String transferType = objPaymentDetails.getTransfer_Type();

						//From & To account mappings..
						String frmAccountDisplay = FormatAccount.getDebitAccountDisp(txnOutDetails);
						String toAccountDisplay = FormatAccount.getCreditAccountDisp(txnOutDetails);
						if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)) {
							objInternalPreConfirm.setFromAccount(frmAccountDisplay);
							objInternalPreConfirm.setFromKeyAccNumber(objFromMSAcc_Details.getKeyAccount());
							objInternalPreConfirm.setToAccount(toAccountDisplay);
							objInternalPreConfirm.setToKeyAccNumber(objToMSAcc_Details.getKeyAccount());
						}
						else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)) {
							objInternalPreConfirm.setFromAccount(frmAccountDisplay);
							objInternalPreConfirm.setFromKeyAccNumber(objFromMSAcc_Details.getKeyAccount());
							objInternalPreConfirm.setToAccount(toAccountDisplay);
							objInternalPreConfirm.setLoanAcntNo(objLoanAcntDetails.getLoanAccount());
						}
					}
				}
			}
			objInternalPreConfirm.setBusinessRuleMessages(brMessages);
			EBWLogger.trace(this, "Finished postInternalTransferInternalTransfer_INITsubmitbut");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}

	/**
	 * Method for InternalTransfer Screen, InternalTransfer_Edit State and INIT Event. 
	 * @throws Exception 
	 */
	public void preInternalTransferInternalTransfer_EditINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preInternalTransferInternalTransfer_EditINIT"); 

			//Business Date Mappings ...
			InternalTransferForm objInternalTransfer=(InternalTransferForm) objSourceForm;
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();	
			MMContext objMMContextData = objUserPrincipal.getContextData();

			Object[] internalTrInitTO = (Object[])objParam[1];
			DsConfigDetailsTO objDsConfigDetails = (DsConfigDetailsTO)internalTrInitTO[0];
			String qzBranchCode = PropertyFileReader.getProperty("OU_ID"); 
			objDsConfigDetails.setBranch_code(qzBranchCode);

			//Setting loginUserName for showing on header ...
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			String userName = MSCommonUtils.extractCurrentUserName(objMSUserDetails);
			objInternalTransfer.setLoginUserName(userName);

			//Updating the input TO...
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			DsGetEditTransferInTO dsGetEditTransferInTO = (DsGetEditTransferInTO)internalTrInitTO[1];
			dsGetEditTransferInTO.setLifeUserId(objMSUserDetails.getUuid());

			EBWLogger.trace(this, "Finished preInternalTransferInternalTransfer_EditINIT");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}


	public void postInternalTransferInternalTransfer_EditINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{		
		try 
		{
			EBWLogger.trace(this, "Starting postInternalTransferInternalTransfer_EditINIT");
			InternalTransferForm objInternalTransfer=(InternalTransferForm) objSourceForm;
			String recuringFlag = null;
			DsGetEditTransferOutTO objDsGetEditTransferOutTO = (DsGetEditTransferOutTO)((Object []) objReturn)[1];
			HashMap txnDetails = new HashMap();

			//Required Payment Details Mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setTransfer_Type(ConvertionUtil.convertToString(objDsGetEditTransferOutTO.getTransactionType()));

			//From account mappings..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(objDsGetEditTransferOutTO.getFrombr_acct_no_fa()!=null){
				objFromMSAcc_Details.setAccountNumber(objDsGetEditTransferOutTO.getFrombr_acct_no_fa().substring(3,9));
				objFromMSAcc_Details.setOfficeNumber(objDsGetEditTransferOutTO.getFrombr_acct_no_fa().substring(0,3));
				objFromMSAcc_Details.setFaNumber(objDsGetEditTransferOutTO.getFrombr_acct_no_fa().substring(9));
				objFromMSAcc_Details.setNickName(objDsGetEditTransferOutTO.getFrmAccNickName());
				objFromMSAcc_Details.setFriendlyName(objDsGetEditTransferOutTO.getFrmAccFriendlyName());
			}

			//To account mappings..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(objDsGetEditTransferOutTO.getTobr_acct_no_fa()!=null){
				objToMSAcc_Details.setAccountNumber(objDsGetEditTransferOutTO.getTobr_acct_no_fa().substring(3,9));
				objToMSAcc_Details.setOfficeNumber(objDsGetEditTransferOutTO.getTobr_acct_no_fa().substring(0,3));
				objToMSAcc_Details.setFaNumber(objDsGetEditTransferOutTO.getTobr_acct_no_fa().substring(9));
				objToMSAcc_Details.setNickName(objDsGetEditTransferOutTO.getToAccNickName());
				objToMSAcc_Details.setFriendlyName(objDsGetEditTransferOutTO.getToAccFriendlyName());
			}

			//Loan Account mappings..
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();
			objLoanAcntDetails.setLoanAccount(objDsGetEditTransferOutTO.getLoanAcnt());

			txnDetails.put("PaymentDetails",objPaymentDetails);
			txnDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			txnDetails.put("MSToAccDetails",objToMSAcc_Details);
			txnDetails.put("LoanAccountDetails",objLoanAcntDetails);

			// Setting the Recurring Radio ....
			recuringFlag=objDsGetEditTransferOutTO.getTransferFrequency();
			if(recuringFlag!=null && recuringFlag.equalsIgnoreCase("N")){
				objInternalTransfer.setFrequencyValue("1");
			}
			else{
				objInternalTransfer.setFrequencyValue("2");
			}

			// Setting the Frequency EndDate Radio ....
			String freqEndDate=ConvertionUtil.convertToString(objDsGetEditTransferOutTO.getPayfreqenddate());
			if(freqEndDate!=null && freqEndDate.indexOf("1970")==-1){
				objInternalTransfer.setDurationenddate(ConvertionUtil.convertToString(objDsGetEditTransferOutTO.getPayfreqenddate()));
			}

			//Setting the local currency by default..
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			objInternalTransfer.setCurrencyCode(currencyCode);

			//Checking the status consistency ...
			StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
			ArrayList event_valid_statuses = ((ArrayList)((Object []) objReturn)[2]); 
			String txn_Paystatus = objDsGetEditTransferOutTO.getPayccsstatuscode();
			ServiceContext contextData = (ServiceContext)statusConsistency.verifyStatusConsistency(event_valid_statuses,txn_Paystatus);

			//From & To account mappings..
			objInternalTransfer.setFromAccEdit(FormatAccount.getDebitAccountDisp(txnDetails));
			objInternalTransfer.setToAccEdit(FormatAccount.getCreditAccountDisp(txnDetails));

			//Response...
			MSCommonUtils.logEventResponse(contextData);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}
			EBWLogger.trace(this, "Finished postInternalTransferInternalTransfer_EditINIT");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}

	/**
	 * Method for InternalTransfer Screen, InternalTransfer_Edit State and submitbut Event. 
	 * @throws Exception 
	 */
	public void preInternalTransferInternalTransfer_Editsubmitbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preInternalTransferInternalTransfer_Editsubmitbut"); 
			InternalTransferForm objInternalTransfer=(InternalTransferForm) objSourceForm;
			Object intTransferToObj[] = (Object[])objParam[1]; 

			//Server side field validations...
			ValidateINTTransfer objVaidateIntTransfer= new ValidateINTTransfer();
			String validationErr = objVaidateIntTransfer.validateINTTransferFrm(objInternalTransfer);
			if(validationErr!=null && !validationErr.trim().equalsIgnoreCase("")){
				throw new Exception(validationErr); // Form field validation failed ...
			}

			//TO Objects Mappings...
			String fromKeyAccount=objInternalTransfer.getFromKeyAccNumber();
			String toKeyAccount=objInternalTransfer.getToKeyAccNumber();
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			String transferType = objInternalTransfer.getTransactionType();

			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)intTransferToObj[0];

			MMContext objMMContextData = objUserPrincipal.getContextData();
			ArrayList objMMContextAcc = objMMContextData.getAccounts();
			for(int i =0; i<objMMContextData.getAccounts().size();i++)
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
					intTransferToObj[1] = objFromMSAcc_Details;
				}
				if(objMMAccount.getKeyAccount().equalsIgnoreCase(toKeyAccount))
				{
					//Setting To account details...
					objPaymentDetails.setTo_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setTo_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setToAcc_InContext(true);

					ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
					objToMSAcc_Details = objMSCommonUtils.setToMSAccDetailsTO(objMMAccount);
					intTransferToObj[2]= objToMSAcc_Details;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			String lifeTimeUserId=objMSUserDetails.getUuid();
			String userRole=null;
			boolean isFA= objMSUserDetails.isFA();
			if(isFA)
				userRole = InitiatorRoleDesc.Client; //Business rule User/Group mapping for the FA login
			else
				userRole =InitiatorRoleDesc.Client;
			intTransferToObj[3] = objMSUserDetails;

			//Setting the Date Parameters for the First business Day selection  && Last business day selection..
			if(objInternalTransfer.getFrequencyValue().equalsIgnoreCase("2") && (objInternalTransfer.getFrequencycombo().equalsIgnoreCase("FBD") || objInternalTransfer.getFrequencycombo().equalsIgnoreCase("LBD")))	
			{
				// Set the Initiation Date in the Hidden field to store the same in the Payment Date .. 
				objInternalTransfer.setFirstLastInitiationDate(objInternalTransfer.getInitiationDate());

				//Calculating the Initiation & Estimated Arrival date  for the FBD and LBD , 
				String paymentInitiationDate = objInternalTransfer.getInitiationDate();
				Date paymentInitDateVal = ConvertionUtil.convertToDate(paymentInitiationDate);
				String recurringFreq = objInternalTransfer.getFrequencycombo();

				//Initiation & Estimated Arrival Date in case of FBD or LBD...
				Date startBusinessDate = MSCommonUtils.calculateStartBusinessDate(recurringFreq,paymentInitDateVal,transferType,Boolean.FALSE);
				objInternalTransfer.setInitiationDate(ConvertionUtil.convertToString(startBusinessDate));
				objInternalTransfer.setEstArrivalDate(ConvertionUtil.convertToString(startBusinessDate));
			}

			objPaymentDetails.setTransfer_Amount(objInternalTransfer.getPaymentamount());
			objPaymentDetails.setChildTxnAmount(objInternalTransfer.getPaymentamount());
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setFxRate(null);
			objPaymentDetails.setInitiation_Date(objInternalTransfer.getInitiationDate());
			objPaymentDetails.setRequestedDate(objInternalTransfer.getInitiationDate());
			objPaymentDetails.setEstimatedArrivalDate(objInternalTransfer.getEstArrivalDate());
			objPaymentDetails.setFrequency_Type(objInternalTransfer.getFrequencyValue());
			objPaymentDetails.setFrequency_DurationDesc(objInternalTransfer.getFrequencycombo());
			objPaymentDetails.setFrequency_DurationValue(objInternalTransfer.getDurationValue());
			objPaymentDetails.setDuration_EndDate(objInternalTransfer.getDurationenddate());
			objPaymentDetails.setDuration_NoOfTransfers(objInternalTransfer.getDurationNoOfTransfers());
			objPaymentDetails.setDuration_AmountLimit(objInternalTransfer.getDurationdollarlimit());
			objPaymentDetails.setBusiness_Date(objInternalTransfer.getBusinessDate());
			objPaymentDetails.setTransfer_Type(transferType);
			objPaymentDetails.setScreen_Type(transferType);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setTrial_depo("N");
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Modify_PreConfirm);
			objPaymentDetails.setTxnModified(true);
			objPaymentDetails.setPrevAction("Modify");
			objPaymentDetails.setTransactionId(objInternalTransfer.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objInternalTransfer.getVersionnum());
			objPaymentDetails.setTxnBatchId(objInternalTransfer.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objInternalTransfer.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnId(objInternalTransfer.getParentTxnNumber());
			objPaymentDetails.setRecParentTxnVersion(objInternalTransfer.getParTxnVersionnum());
			objPaymentDetails.setTxnCurrentStatusCode(objInternalTransfer.getPrevPaystatus());
			objPaymentDetails.setTxnPrevStatusCode(objInternalTransfer.getPrevPaystatus());
			objPaymentDetails.setCurrent_owner_id(lifeTimeUserId);
			objPaymentDetails.setCurrent_owner_role(userRole);
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}


	public void postInternalTransferInternalTransfer_Editsubmitbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting postInternalTransferInternalTransfer_Editsubmitbut");
			InternalPreConfirmForm objInternalPreConfirm=(InternalPreConfirmForm) objTargetForm;
			InternalTransferForm objInternalTransfer=(InternalTransferForm) objSourceForm;
			objUserPrincipal.setBrErrorMessages(null); 
			String brMessages="";

			//Setting the Date Parameters for the First business Day or Last business Day selection in Pre Confirm Screen...
			if(objInternalPreConfirm.getFrequencyValue().equalsIgnoreCase("2") && (objInternalPreConfirm.getFrequencycombo().equalsIgnoreCase("FBD") || objInternalPreConfirm.getFrequencycombo().equalsIgnoreCase("LBD")))	
			{
				// Set the Initiation Date in the Hidden field to store the same in the Paypayment Date .. 
				objInternalPreConfirm.setFirstLastInitiationDate(objInternalTransfer.getFirstLastInitiationDate());
				objInternalPreConfirm.setInitiationDate(objInternalTransfer.getInitiationDate());
				objInternalPreConfirm.setEstArrivalDate(objInternalTransfer.getEstArrivalDate());
			}

			//Output extraction...
			ArrayList createSubmitDetails = (ArrayList)objReturn;
			ServiceContext contextData = (ServiceContext)createSubmitDetails.get(0);

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
					objInternalPreConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.INTERNAL_TRANSFERS);
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
					objInternalPreConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.INTERNAL_TRANSFERS);
				}
				else 
				{
					if(contextData.getMaxSeverity()==MessageType.WARNING || contextData.getMaxSeverity()==MessageType.INFORMATION)
					{
						HashMap<String, JSONArray> busniessRuleMessages = new HashMap<String, JSONArray>();

						//Warnings Message Extraction....
						JSONArray jsonWarning = MSCommonUtils.extractContextWarningMsg(contextData,true);
						if(!jsonWarning.isEmpty()){
							objInternalPreConfirm.setBrWarnings(true);
						}
						busniessRuleMessages.put("BusinessWarnings", jsonWarning);

						//Information Message Extraction...
						JSONArray jsonInformation = MSCommonUtils.extractContextInfoMsg(contextData,true);
						busniessRuleMessages.put("BusinessInformations", jsonInformation);

						//JSON Output Mapping...
						JSONObject jsonBRObject = JSONObject.fromObject(busniessRuleMessages);

						//Settings..
						brMessages = jsonBRObject.toString();
					}
					//Setting the Initiation Date and Estimated Arrival Date in case of the Cut Off Time failure ...
					if(createSubmitDetails.get(1)!=null)
					{
						HashMap txnOutDetails = (HashMap)createSubmitDetails.get(1);
						PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
						if(txnOutDetails.containsKey("PaymentDetails")){
							objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
						}

						//If the ArrayList is not Empty then we need to update the Initiation Date and Estimated Arrival Date since Cut Off Time has Failed...
						objInternalPreConfirm.setInitiationDate(objPaymentDetails.getRequestedDate());
						objInternalPreConfirm.setEstArrivalDate(objPaymentDetails.getEstimatedArrivalDate());
						objInternalPreConfirm.setPaymentamount(MSCommonUtils.formatTxnAmount(objPaymentDetails.getTransfer_Amount()));
						objInternalPreConfirm.setTransactionType(objPaymentDetails.getTransfer_Type());

						//Setting the frequency and duration attributes in case of recurring transfers...
						objInternalPreConfirm.setFrequencyradio(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));
						objInternalPreConfirm.setDurationradio(MSCommonUtils.getRepeatDesc_View(objPaymentDetails));

						//From & To account mappings..
						objInternalPreConfirm.setFromAccEdit(FormatAccount.getDebitAccountDisp(txnOutDetails));
						objInternalPreConfirm.setToAccEdit(FormatAccount.getCreditAccountDisp(txnOutDetails));
					}
				}
				objInternalPreConfirm.setBusinessRuleMessages(brMessages);
			}
			EBWLogger.trace(this, "Finished postInternalTransferInternalTransfer_Editsubmitbut");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}
}