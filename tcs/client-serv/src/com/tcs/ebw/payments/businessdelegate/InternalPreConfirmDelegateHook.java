/*
 * Created on Tue Mar 24 16:17:22 IST 2009
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
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.channels.integration.MMAccount;
import com.tcs.bancs.channels.integration.MMContext;
import com.tcs.ebw.businessdelegate.BusinessDelegateHook;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.payments.formbean.InternalConfirmForm;
import com.tcs.ebw.payments.formbean.InternalPreConfirmForm;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class InternalPreConfirmDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for InternalPreConfirm Screen, InternalPreConfirm_INIT State and confirmbut Event. 
	 * @throws Exception 
	 */
	public void preInternalPreConfirmInternalPreConfirm_INITconfirmbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preInternalPreConfirmInternalPreConfirm_INITconfirmbut"); 
		try 
		{
			InternalPreConfirmForm objInternalPreConfirm = (InternalPreConfirmForm)objSourceForm;
			Object intPreConfirmToObj[] = (Object[])objParam[1];

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			ServiceContext serviceContext = new ServiceContext();
			String fromKeyAccount = objInternalPreConfirm.getFromKeyAccNumber(); 
			String toKeyAccount = objInternalPreConfirm.getToKeyAccNumber();
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			String transferType = objInternalPreConfirm.getTransactionType();
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)intPreConfirmToObj[0];

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
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(fromKeyAccount)) )
				{
					//Setting From account details...
					objPaymentDetails.setFrom_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setFrom_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setFrmAcc_InContext(true);

					FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
					objFromMSAcc_Details = objMSCommonUtils.setFromMSAccDetailsTO(objMMAccount);
					intPreConfirmToObj[1] = objFromMSAcc_Details;
				}
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(toKeyAccount)))
				{
					//Setting To account details...
					objPaymentDetails.setTo_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setTo_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setToAcc_InContext(true);

					ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
					objToMSAcc_Details = objMSCommonUtils.setToMSAccDetailsTO(objMMAccount);
					intPreConfirmToObj[2]= objToMSAcc_Details;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			String lifeTimeUserId=objMSUserDetails.getUuid();
			String userRole=null;
			if(isFA)
				userRole = InitiatorRoleDesc.Client; //Business rule User/Group mapping for the FA login
			else
				userRole =InitiatorRoleDesc.Client;
			intPreConfirmToObj[3] = objMSUserDetails;

			objPaymentDetails.setTransfer_Amount(objInternalPreConfirm.getPaymentamount());
			objPaymentDetails.setChildTxnAmount(objInternalPreConfirm.getPaymentamount());
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setFxRate(null);
			objPaymentDetails.setInitiation_Date(objInternalPreConfirm.getInitiationDate());
			objPaymentDetails.setRequestedDate(objInternalPreConfirm.getInitiationDate());
			objPaymentDetails.setEstimatedArrivalDate(objInternalPreConfirm.getEstArrivalDate());
			objPaymentDetails.setFrequency_Type(objInternalPreConfirm.getFrequencyValue());
			objPaymentDetails.setFrequency_DurationDesc(objInternalPreConfirm.getFrequencycombo());
			objPaymentDetails.setFrequency_DurationValue(objInternalPreConfirm.getDurationValue());
			objPaymentDetails.setDuration_EndDate(objInternalPreConfirm.getDurationenddate());
			objPaymentDetails.setDuration_NoOfTransfers(objInternalPreConfirm.getDurationNoOfTransfers());
			objPaymentDetails.setDuration_AmountLimit(objInternalPreConfirm.getDurationdollarlimit());
			objPaymentDetails.setBusiness_Date(objInternalPreConfirm.getBusinessDate());
			objPaymentDetails.setTransfer_Type(transferType);
			objPaymentDetails.setScreen_Type(transferType);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setTrial_depo("N");
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Create_Confirm);
			objPaymentDetails.setTxnInitiated(true);
			objPaymentDetails.setPrevAction("Create");
			objPaymentDetails.setInitiator_id(lifeTimeUserId);
			objPaymentDetails.setInitiator_role(userRole);
			objPaymentDetails.setCurrent_owner_id(lifeTimeUserId);
			objPaymentDetails.setCurrent_owner_role(userRole);

			// Setting Payment Date in case of First Business Day and Last Business Date to the Initiation Date from the create screen...
			if(objInternalPreConfirm.getFrequencyValue().equalsIgnoreCase("2") && (objInternalPreConfirm.getFrequencycombo().equalsIgnoreCase("FBD") || objInternalPreConfirm.getFrequencycombo().equalsIgnoreCase("LBD"))){
				objPaymentDetails.setInitiation_Date(objInternalPreConfirm.getFirstLastInitiationDate());
			}
		}
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preInternalPreConfirmInternalPreConfirm_INITconfirmbut"); 
	}

	public void postInternalPreConfirmInternalPreConfirm_INITconfirmbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting postInternalPreConfirmInternalPreConfirm_INITconfirmbut");
			InternalPreConfirmForm objInternalPreConfirm=(InternalPreConfirmForm) objSourceForm;
			InternalConfirmForm objInternalConfirm=(InternalConfirmForm) objTargetForm;
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
					objInternalConfirm.setBrErrors(true);
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
					objInternalConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.INTERNAL_TRANSFERS);
				}
				else 
				{
					if(contextData.getMaxSeverity()==MessageType.WARNING || contextData.getMaxSeverity()==MessageType.INFORMATION)
					{
						HashMap<String, JSONArray> busniessRuleMessages = new HashMap<String, JSONArray>();

						//Warnings Message Extraction only in case its not show on PreConfirm screen....
						JSONArray jsonWarning = new JSONArray();
						if(!objInternalPreConfirm.isBrWarnings()){
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
					//TO Object Mapping..
					if(transactionOutDetails.get(1)!=null)
					{
						HashMap txnOutDetails = (HashMap)transactionOutDetails.get(1);
						PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
						if(txnOutDetails.containsKey("PaymentDetails")){
							objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
						}

						//Payment Details..
						objInternalConfirm.setConfirmNo(objPaymentDetails.getTransactionId());
						objInternalConfirm.setPayamnt(objInternalPreConfirm.getPaymentamount());
						objInternalConfirm.setDate(objPaymentDetails.getRequestedDate());
						objInternalConfirm.setStatus(MSCommonUtils.getTxnStatusDesc_View(objPaymentDetails));
						objInternalConfirm.setFrequency(objInternalPreConfirm.getFrequencyradio());
						objInternalConfirm.setDuration(objInternalPreConfirm.getDurationradio());

						//From & To account mappings..
						objInternalConfirm.setFrmacc(FormatAccount.getDebitAccountDisp(txnOutDetails));
						objInternalConfirm.setToacc(FormatAccount.getCreditAccountDisp(txnOutDetails));
					}
					objInternalConfirm.setBusinessRuleMessages(brMessages);
				}
			}
			EBWLogger.trace(this, "Finished postInternalPreConfirmInternalPreConfirm_INITconfirmbut");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}

	/**
	 * Method for InternalPreConfirm Screen, InternalPreConfirm_Edit State and confirmbut Event. 
	 * @throws Exception 
	 */
	public void preInternalPreConfirmInternalPreConfirm_Editconfirmbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preInternalPreConfirmInternalPreConfirm_Editconfirmbut"); 

			InternalPreConfirmForm objInternalPreConfirm=(InternalPreConfirmForm) objSourceForm;
			Object intPreConfirmToObj[] = (Object[])objParam[1];

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();	
			ServiceContext serviceContext = new ServiceContext();
			String fromKeyAccNumber = objInternalPreConfirm.getFromKeyAccNumber();
			String toKeyAccNumber = objInternalPreConfirm.getToKeyAccNumber();
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			String transferType = objInternalPreConfirm.getTransactionType();
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)intPreConfirmToObj[0];

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
				if(objMMAccount.getKeyAccount().equalsIgnoreCase(fromKeyAccNumber))
				{
					//Setting From account details...
					objPaymentDetails.setFrom_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setFrom_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setFrmAcc_InContext(true);

					FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
					objFromMSAcc_Details = objMSCommonUtils.setFromMSAccDetailsTO(objMMAccount);
					intPreConfirmToObj[1] = objFromMSAcc_Details;

				}
				if(objMMAccount.getKeyAccount().equalsIgnoreCase(toKeyAccNumber))
				{
					//Setting To account details...
					objPaymentDetails.setTo_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setTo_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setToAcc_InContext(true);

					ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
					objToMSAcc_Details = objMSCommonUtils.setToMSAccDetailsTO(objMMAccount);
					intPreConfirmToObj[2]= objToMSAcc_Details;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			String lifeTimeUserId=objMSUserDetails.getUuid();
			String userRole=null;
			if(isFA)
				userRole = InitiatorRoleDesc.Client; //Business rule User/Group mapping for the FA login
			else
				userRole = InitiatorRoleDesc.Client;
			intPreConfirmToObj[3] = objMSUserDetails;

			objPaymentDetails.setTransfer_Amount(objInternalPreConfirm.getPaymentamount());
			objPaymentDetails.setChildTxnAmount(objInternalPreConfirm.getPaymentamount());
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setFxRate(null);
			objPaymentDetails.setInitiation_Date(objInternalPreConfirm.getInitiationDate());
			objPaymentDetails.setRequestedDate(objInternalPreConfirm.getInitiationDate());
			objPaymentDetails.setEstimatedArrivalDate(objInternalPreConfirm.getEstArrivalDate());
			objPaymentDetails.setFrequency_Type(objInternalPreConfirm.getFrequencyValue());
			objPaymentDetails.setFrequency_DurationDesc(objInternalPreConfirm.getFrequencycombo());
			objPaymentDetails.setFrequency_DurationValue(objInternalPreConfirm.getDurationValue());
			objPaymentDetails.setDuration_EndDate(objInternalPreConfirm.getDurationenddate());
			objPaymentDetails.setDuration_NoOfTransfers(objInternalPreConfirm.getDurationNoOfTransfers());
			objPaymentDetails.setDuration_AmountLimit(objInternalPreConfirm.getDurationdollarlimit());
			objPaymentDetails.setBusiness_Date(objInternalPreConfirm.getBusinessDate());
			objPaymentDetails.setTransfer_Type(transferType);
			objPaymentDetails.setScreen_Type(transferType);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setTrial_depo("N");
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Modify_Confirm);
			objPaymentDetails.setTxnModified(true);
			objPaymentDetails.setPrevAction("Modify");
			objPaymentDetails.setTransactionId(objInternalPreConfirm.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objInternalPreConfirm.getVersionnum());
			objPaymentDetails.setTxnBatchId(objInternalPreConfirm.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objInternalPreConfirm.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnId(objInternalPreConfirm.getParentTxnNumber());
			objPaymentDetails.setRecParentTxnVersion(objInternalPreConfirm.getParTxnVersionnum());
			objPaymentDetails.setTxnCurrentStatusCode(objInternalPreConfirm.getPrevPaystatus());
			objPaymentDetails.setTxnPrevStatusCode(objInternalPreConfirm.getPrevPaystatus());
			objPaymentDetails.setCurrent_owner_id(lifeTimeUserId);
			objPaymentDetails.setCurrent_owner_role(userRole);

			// Setting Payment Date in case of First Business Day and Last Business Date to the Initiation Date from the create screen...
			if(objInternalPreConfirm.getFrequencyValue().equalsIgnoreCase("2") && (objInternalPreConfirm.getFrequencycombo().equalsIgnoreCase("FBD") || objInternalPreConfirm.getFrequencycombo().equalsIgnoreCase("LBD"))){
				objPaymentDetails.setInitiation_Date(objInternalPreConfirm.getFirstLastInitiationDate());
			}

			EBWLogger.trace(this, "Finished preInternalPreConfirmInternalPreConfirm_Editconfirmbut");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}

	public void postInternalPreConfirmInternalPreConfirm_Editconfirmbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting postInternalPreConfirmInternalPreConfirm_Editconfirmbut");
			InternalPreConfirmForm objInternalPreConfirm=(InternalPreConfirmForm) objSourceForm;
			InternalConfirmForm objInternalConfirm=(InternalConfirmForm) objTargetForm;
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
					objInternalConfirm.setBrErrors(true);
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
					objInternalConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.INTERNAL_TRANSFERS);
				}
				else 
				{
					if(contextData.getMaxSeverity()==MessageType.WARNING || contextData.getMaxSeverity()==MessageType.INFORMATION)
					{
						HashMap<String, JSONArray> busniessRuleMessages = new HashMap<String, JSONArray>();

						//Warnings Message Extraction only in case its not show on PreConfirm screen....
						JSONArray jsonWarning = new JSONArray();
						if(!objInternalPreConfirm.isBrWarnings()){
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
					objInternalConfirm.setBusinessRuleMessages(brMessages);

					if(transactionOutDetails.get(1)!=null)
					{
						//Payment Details..
						HashMap txnOutDetails = (HashMap)transactionOutDetails.get(1);
						PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
						if(txnOutDetails.containsKey("PaymentDetails")){
							objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
						}

						//Payment Details..
						objInternalConfirm.setConfirmNo(objPaymentDetails.getTransactionId());
						objInternalConfirm.setPayamnt(objInternalPreConfirm.getPaymentamount());
						objInternalConfirm.setDate(objPaymentDetails.getRequestedDate());
						objInternalConfirm.setStatus(MSCommonUtils.getTxnStatusDesc_View(objPaymentDetails));
						objInternalConfirm.setFrequency(objInternalPreConfirm.getFrequencyradio());
						objInternalConfirm.setDuration(objInternalPreConfirm.getDurationradio());

						//From & To account mappings..
						objInternalConfirm.setFrmacc(FormatAccount.getDebitAccountDisp(txnOutDetails));
						objInternalConfirm.setToacc(FormatAccount.getCreditAccountDisp(txnOutDetails));
					}
				}
			}
			EBWLogger.trace(this, "Finished postInternalPreConfirmInternalPreConfirm_Editconfirmbut");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}
}