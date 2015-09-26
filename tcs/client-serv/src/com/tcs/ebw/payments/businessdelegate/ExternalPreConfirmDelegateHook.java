/*
 * Created on Tue Mar 24 11:05:07 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.FormatAccount;
import com.tcs.Payments.ms360Utils.InitiatorRoleDesc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.channels.integration.MMAccount;
import com.tcs.bancs.channels.integration.MMContext;
import com.tcs.ebw.mvc.validator.EbwForm;
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
 *
 * **********************************************************
 */
public class ExternalPreConfirmDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for ExternalPreConfirm Screen, ExternalPreConfirm_INIT State and confirmbut Event. 
	 * @throws Exception 
	 */
	public void preExternalPreConfirmExternalPreConfirm_INITconfirmbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preExternalPreConfirmExternalPreConfirm_INITconfirmbut"); 
		try 
		{
			ExternalPreConfirmForm objExternalPreConfirm = (ExternalPreConfirmForm)objSourceForm;
			Object extPreConfirmToObj[] = (Object[])objParam[1];

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			ServiceContext serviceContext = new ServiceContext();
			String fromKeyAccount =objExternalPreConfirm.getFromKeyAccNumber(); 
			String toKeyAccount =objExternalPreConfirm.getToKeyAccNumber();
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)extPreConfirmToObj[0];

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
				objMMAccount = MSCommonUtils.getMSAccFormat(objMMAccount); //Formatting FA, Office, Account Number in 3-6-3 format...
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(fromKeyAccount)))
				{
					//Setting From account details...
					objPaymentDetails.setFrom_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setFrom_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setFrmAcc_InContext(true);
					objPaymentDetails.setTransfer_Type(TxnTypeCode.ACH_WITHDRAWAL);

					FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
					objFromMSAcc_Details = objMSCommonUtils.setFromMSAccDetailsTO(objMMAccount);
					extPreConfirmToObj[1] = objFromMSAcc_Details;
				}
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(toKeyAccount)))
				{
					//Setting To account details...
					objPaymentDetails.setTo_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setTo_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setToAcc_InContext(true);
					objPaymentDetails.setTransfer_Type(TxnTypeCode.ACH_DEPOSIT);

					ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
					objToMSAcc_Details = objMSCommonUtils.setToMSAccDetailsTO(objMMAccount);
					extPreConfirmToObj[2]= objToMSAcc_Details;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			String lifeTimeUserId=objMSUserDetails.getUuid();
			String userRole=null;
			if(isFA){
				userRole = InitiatorRoleDesc.Client; //Business rule User/Group mapping for the FA login
			}
			else{
				userRole = InitiatorRoleDesc.Client;
			}
			extPreConfirmToObj[3] = objMSUserDetails;

			objPaymentDetails.setTransfer_Amount(objExternalPreConfirm.getPaymentamount());
			objPaymentDetails.setChildTxnAmount(objExternalPreConfirm.getPaymentamount());
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setFxRate(null);
			objPaymentDetails.setInitiation_Date(objExternalPreConfirm.getInitiationDate());
			objPaymentDetails.setRequestedDate(objExternalPreConfirm.getInitiationDate());
			objPaymentDetails.setEstimatedArrivalDate(objExternalPreConfirm.getEstArrivalDate());
			objPaymentDetails.setFrequency_Type(objExternalPreConfirm.getFrequencyValue());
			objPaymentDetails.setFrequency_DurationDesc(objExternalPreConfirm.getFrequencycombo());
			objPaymentDetails.setFrequency_DurationValue(objExternalPreConfirm.getDurationValue());
			objPaymentDetails.setDuration_EndDate(objExternalPreConfirm.getDurationenddate());
			objPaymentDetails.setDuration_NoOfTransfers(objExternalPreConfirm.getDurationNoOfTransfers());
			objPaymentDetails.setDuration_AmountLimit(objExternalPreConfirm.getDurationdollarlimit());
			objPaymentDetails.setBusiness_Date(objExternalPreConfirm.getBusinessDate());
			objPaymentDetails.setScreen_Type(TxnTypeCode.ACH_TYPE);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setTrial_depo("N");
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Create_Confirm);
			objPaymentDetails.setTxnInitiated(true);
			objPaymentDetails.setPrevAction("Create");
			objPaymentDetails.setExtAccount_RefId(objExternalPreConfirm.getPayeeId());
			objPaymentDetails.setRequestHeaderInfo((RequestHeaderInfo)objExternalPreConfirm.getRequestHeaderInfo());
			objPaymentDetails.setInitiator_id(lifeTimeUserId);
			objPaymentDetails.setInitiator_role(userRole);
			objPaymentDetails.setCurrent_owner_id(lifeTimeUserId);
			objPaymentDetails.setCurrent_owner_role(userRole);

			// Setting Payment Date in case of First Business Day and Last Business Date to the Initiation Date from the create screen...
			if(objExternalPreConfirm.getFrequencyValue().equalsIgnoreCase("2") && (objExternalPreConfirm.getFrequencycombo().equalsIgnoreCase("FBD") || objExternalPreConfirm.getFrequencycombo().equalsIgnoreCase("LBD"))){
				objPaymentDetails.setInitiation_Date(objExternalPreConfirm.getFirstLastInitiationDate());
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preExternalPreConfirmExternalPreConfirm_INITconfirmbut"); 
	}


	public void postExternalPreConfirmExternalPreConfirm_INITconfirmbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting postExternalPreConfirmExternalPreConfirm_INITconfirmbut");
			ExternalPreConfirmForm objExternalPreConfirm=(ExternalPreConfirmForm) objSourceForm;
			ExternalConfirmForm objExternalConfirm=(ExternalConfirmForm) objTargetForm;
			objUserPrincipal.setBrErrorMessages(null);
			String brMessages="";

			//Output extraction..
			ArrayList transactionOutDetails = (ArrayList)objReturn;
			ServiceContext contextData = (ServiceContext)transactionOutDetails.get(0);

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
					objExternalConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.EXTERNAL_TRANSFERS);
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
					objExternalConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.EXTERNAL_TRANSFERS);
				}
				else 
				{
					if(contextData.getMaxSeverity()==MessageType.WARNING || contextData.getMaxSeverity()==MessageType.INFORMATION || contextData.getMaxSeverity()==MessageType.RISK)
					{
						HashMap<String, JSONArray> busniessRuleMessages = new HashMap<String, JSONArray>();

						//Risk Message Extraction....
						JSONArray jsonRisk = new JSONArray();
						if(!objExternalPreConfirm.isBrRisk()){
							jsonRisk = MSCommonUtils.extractContextRiskMsg(contextData,true);
						}
						busniessRuleMessages.put("BusinessRisk", jsonRisk);

						//Warnings Message Extraction....
						JSONArray jsonWarning = new JSONArray();
						if(!objExternalPreConfirm.isBrWarnings()){
							jsonWarning = MSCommonUtils.extractContextWarningMsg(contextData,true);
						}
						busniessRuleMessages.put("BusinessWarnings", jsonWarning);

						//Information Message Extraction...
						JSONArray jsonInformation = MSCommonUtils.extractContextInfoMsg(contextData,false);
						busniessRuleMessages.put("BusinessInformations", jsonInformation);

						//JSON Output Mapping...
						JSONObject jsonBRObject = JSONObject.fromObject(busniessRuleMessages);

						//Settings..
						brMessages = jsonBRObject.toString();
					}
					objExternalConfirm.setBusinessRuleMessages(brMessages);

					if(transactionOutDetails.get(1)!=null)
					{
						HashMap txnOutDetails = (HashMap)transactionOutDetails.get(1);
						PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
						if(txnOutDetails.containsKey("PaymentDetails")){
							objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
						}

						objExternalConfirm.setConfirmNo(objPaymentDetails.getTransactionId());
						objExternalConfirm.setPayamnt(objExternalPreConfirm.getPaymentamount());
						objExternalConfirm.setDate(objPaymentDetails.getRequestedDate());
						objExternalConfirm.setStatus(MSCommonUtils.getTxnStatusDesc_View(objPaymentDetails));
						objExternalConfirm.setFrequency(objExternalPreConfirm.getFrequencyradio());
						objExternalConfirm.setDuration(objExternalPreConfirm.getDurationradio());

						//From account and To account mappings..
						objExternalConfirm.setFrmacc(FormatAccount.getDebitAccountDisp(txnOutDetails));
						objExternalConfirm.setToacc(FormatAccount.getCreditAccountDisp(txnOutDetails));
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postExternalPreConfirmExternalPreConfirm_INITconfirmbut"); 
	}

	/**
	 * Method for ExternalPreConfirm Screen, ExternalPreConfirm_Edit State and confirmbut Event. 
	 * @throws Exception 
	 */
	public void preExternalPreConfirmExternalPreConfirm_Editconfirmbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preExternalPreConfirmExternalPreConfirm_Editconfirmbut"); 
			ExternalPreConfirmForm objExternalPreConfirm = (ExternalPreConfirmForm)objSourceForm;
			Object extPreConfirmToObj[] = (Object[])objParam[1];

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();	
			ServiceContext serviceContext = new ServiceContext();
			String fromKeyAccNumber = objExternalPreConfirm.getFromKeyAccNumber();
			String toKeyAccNumber = objExternalPreConfirm.getToKeyAccNumber();
			String transferType = objExternalPreConfirm.getTransactionType();
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)extPreConfirmToObj[0];

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
				objMMAccount = MSCommonUtils.getMSAccFormat(objMMAccount);//Formatting FA, Office, Account Number in 3-6-3 format...
				if(objMMAccount.getKeyAccount().equalsIgnoreCase(fromKeyAccNumber))
				{
					//Setting From account details...
					objPaymentDetails.setFrom_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setFrom_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setFrmAcc_InContext(true);

					FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
					objFromMSAcc_Details = objMSCommonUtils.setFromMSAccDetailsTO(objMMAccount);
					extPreConfirmToObj[1] = objFromMSAcc_Details;
				}
				if(objMMAccount.getKeyAccount().equalsIgnoreCase(toKeyAccNumber))
				{
					//Setting To account details...
					objPaymentDetails.setTo_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setTo_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setToAcc_InContext(true);

					ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
					objToMSAcc_Details = objMSCommonUtils.setToMSAccDetailsTO(objMMAccount);
					extPreConfirmToObj[2]= objToMSAcc_Details;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			String lifeTimeUserId=objMSUserDetails.getUuid();
			String userRole=null;
			if(isFA){
				userRole = InitiatorRoleDesc.Client; //Business rule User/Group mapping for the FA login
			}
			else{
				userRole = InitiatorRoleDesc.Client;
			}
			extPreConfirmToObj[3] = objMSUserDetails;

			objPaymentDetails.setTransfer_Type(transferType);
			objPaymentDetails.setTransfer_Amount(objExternalPreConfirm.getPaymentamount());
			objPaymentDetails.setChildTxnAmount(objExternalPreConfirm.getPaymentamount());
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setFxRate(null);
			objPaymentDetails.setInitiation_Date(objExternalPreConfirm.getInitiationDate());
			objPaymentDetails.setRequestedDate(objExternalPreConfirm.getInitiationDate());
			objPaymentDetails.setEstimatedArrivalDate(objExternalPreConfirm.getEstArrivalDate());
			objPaymentDetails.setFrequency_Type(objExternalPreConfirm.getFrequencyValue());
			objPaymentDetails.setFrequency_DurationDesc(objExternalPreConfirm.getFrequencycombo());
			objPaymentDetails.setFrequency_DurationValue(objExternalPreConfirm.getDurationValue());
			objPaymentDetails.setDuration_EndDate(objExternalPreConfirm.getDurationenddate());
			objPaymentDetails.setDuration_NoOfTransfers(objExternalPreConfirm.getDurationNoOfTransfers());
			objPaymentDetails.setDuration_AmountLimit(objExternalPreConfirm.getDurationdollarlimit());
			objPaymentDetails.setBusiness_Date(objExternalPreConfirm.getBusinessDate());
			objPaymentDetails.setScreen_Type(TxnTypeCode.ACH_TYPE);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setTrial_depo("N");
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Modify_Confirm);
			objPaymentDetails.setTxnModified(true);
			objPaymentDetails.setPrevAction("Modify");
			objPaymentDetails.setTransactionId(objExternalPreConfirm.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objExternalPreConfirm.getVersionnum());
			objPaymentDetails.setTxnBatchId(objExternalPreConfirm.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objExternalPreConfirm.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnId(objExternalPreConfirm.getParentTxnNumber());
			objPaymentDetails.setRecParentTxnVersion(objExternalPreConfirm.getParTxnVersionnum());
			objPaymentDetails.setExtAccount_RefId(objExternalPreConfirm.getPayeeId());
			objPaymentDetails.setRequestHeaderInfo((RequestHeaderInfo)objExternalPreConfirm.getRequestHeaderInfo());
			objPaymentDetails.setTxnCurrentStatusCode(objExternalPreConfirm.getPrevPaystatus());
			objPaymentDetails.setTxnPrevStatusCode(objExternalPreConfirm.getPrevPaystatus());
			objPaymentDetails.setCurrent_owner_id(lifeTimeUserId);
			objPaymentDetails.setCurrent_owner_role(userRole);

			// Setting Payment Date in case of First Business Day and Last Business Date to the Initiation Date from the create screen...
			if(objExternalPreConfirm.getFrequencyValue().equalsIgnoreCase("2") && (objExternalPreConfirm.getFrequencycombo().equalsIgnoreCase("FBD") || objExternalPreConfirm.getFrequencycombo().equalsIgnoreCase("LBD")))	
			{
				objPaymentDetails.setInitiation_Date(objExternalPreConfirm.getFirstLastInitiationDate());
			}


		} catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preExternalPreConfirmExternalPreConfirm_Editconfirmbut"); 
	}

	/**
	 * 
	 * @param objSourceForm
	 * @param objTargetForm
	 * @param objReturn
	 * @param objParam
	 * @param objUserPrincipal
	 * @param retainDataMap
	 * @param objUserSessionObject
	 * @throws Exception
	 */
	public void postExternalPreConfirmExternalPreConfirm_Editconfirmbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postExternalPreConfirmExternalPreConfirm_Editconfirmbut");
		try 
		{
			ExternalPreConfirmForm objExternalPreConfirm=(ExternalPreConfirmForm) objSourceForm;
			ExternalConfirmForm objExternalConfirm=(ExternalConfirmForm) objTargetForm;
			objUserPrincipal.setBrErrorMessages(null);
			String brMessages="";

			//Output extraction...
			ArrayList transactionOutDetails = (ArrayList)objReturn;
			ServiceContext contextData = (ServiceContext)transactionOutDetails.get(0);

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
					objExternalConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.EXTERNAL_TRANSFERS);
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
					objExternalConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.EXTERNAL_TRANSFERS);
				}
				else 
				{
					if(contextData.getMaxSeverity()==MessageType.WARNING || contextData.getMaxSeverity()==MessageType.INFORMATION || contextData.getMaxSeverity()==MessageType.RISK)
					{
						HashMap<String, JSONArray> busniessRuleMessages = new HashMap<String, JSONArray>();

						//Risk Message Extraction....
						JSONArray jsonRisk = new JSONArray();
						if(!objExternalPreConfirm.isBrRisk()){
							jsonRisk = MSCommonUtils.extractContextRiskMsg(contextData,true);
						}
						busniessRuleMessages.put("BusinessRisk", jsonRisk);

						//Warnings Message Extraction only in case its not show on PreConfirm screen....
						JSONArray jsonWarning = new JSONArray();
						if(!objExternalPreConfirm.isBrWarnings()){
							jsonWarning = MSCommonUtils.extractContextWarningMsg(contextData,true);
						}
						busniessRuleMessages.put("BusinessWarnings", jsonWarning);

						//Information Message Extraction...
						JSONArray jsonInformation = MSCommonUtils.extractContextInfoMsg(contextData,false);
						busniessRuleMessages.put("BusinessInformations", jsonInformation);

						//JSON Output Mapping...
						JSONObject jsonBRObject = JSONObject.fromObject(busniessRuleMessages);

						//Settings..
						brMessages = jsonBRObject.toString();
					}
					objExternalConfirm.setBusinessRuleMessages(brMessages);

					if(transactionOutDetails.get(1)!=null)
					{
						HashMap txnOutDetails = (HashMap)transactionOutDetails.get(1);
						PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
						if(txnOutDetails.containsKey("PaymentDetails")){
							objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
						}

						objExternalConfirm.setConfirmNo(objPaymentDetails.getTransactionId());
						objExternalConfirm.setPayamnt(objExternalPreConfirm.getPaymentamount());
						objExternalConfirm.setDate(objPaymentDetails.getRequestedDate());
						objExternalConfirm.setStatus(MSCommonUtils.getTxnStatusDesc_View(objPaymentDetails));
						objExternalConfirm.setFrequency(objExternalPreConfirm.getFrequencyradio());
						objExternalConfirm.setDuration(objExternalPreConfirm.getDurationradio());

						//From account and To account mappings..
						objExternalConfirm.setFrmacc(FormatAccount.getDebitAccountDisp(txnOutDetails));
						objExternalConfirm.setToacc(FormatAccount.getCreditAccountDisp(txnOutDetails));
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished postExternalPreConfirmExternalPreConfirm_Editconfirmbut"); 
	}
}