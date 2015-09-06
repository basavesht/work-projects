/*
 * Created on Tue May 26 15:36:10 IST 2009
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
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
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
public class SkipTransferConfirmDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for SkipTransferConfirm Screen, SkipTransferConfirm_INIT State and confirmBtn Event. 
	 * @throws Exception 
	 */
	public void preSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn"); 
		try
		{
			EBWLogger.trace(this, "Starting preCancelTransferCancelTransfer_RecurringskipBtn"); 
			SkipTransferConfirmForm objSkipTransferConfirm=(SkipTransferConfirmForm) objSourceForm;
			Object skipTransferToObj[] = (Object[])objParam[1];

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			ServiceContext serviceContext = new ServiceContext();
			String fromKeyAccount =objSkipTransferConfirm.getFromKeyAccNumber(); // Value may be MS Key account or Reference Id for the external account...
			String toKeyAccount =objSkipTransferConfirm.getToKeyAccNumber(); // Value may be MS Key account or Reference Id for the external account...
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)skipTransferToObj[0];

			MMContext objMMContextData = objUserPrincipal.getContextData();
			boolean isFA= objMMContextData.isFA();
			if(isFA){
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.ACCESS_ERROR);
				String errorMessage = MSCommonUtils.extractContextErrMessage(serviceContext);
				throw new Exception(errorMessage);
			}

			ArrayList objMMContextAcc = objMMContextData.getAccounts();
			for(int i=0;i< objMMContextData.getAccounts().size();i++)
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
					skipTransferToObj[1] = objFromMSAcc_Details;

				}
				if(objMMAccount.getKeyAccount().equalsIgnoreCase(toKeyAccount))
				{
					//Setting To account details...
					objPaymentDetails.setTo_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setTo_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setToAcc_InContext(true);

					ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
					objToMSAcc_Details = objMSCommonUtils.setToMSAccDetailsTO(objMMAccount);
					skipTransferToObj[2]= objToMSAcc_Details;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			skipTransferToObj[3] = objMSUserDetails;

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

			EBWLogger.trace(this, "Finished preSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn"); 
		}
		catch(Exception exception)
		{
			throw exception;
		}
	}

	/**
	 * Post skip transfer delegate method...
	 */
	public void postSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try {
			EBWLogger.trace(this, "Starting postSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn");
			CancelTransferConfirmForm objCancelTransferConfirm=(CancelTransferConfirmForm) objTargetForm;
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
			EBWLogger.trace(this, "Finished postSkipTransferConfirmSkipTransferConfirm_INITconfirmBtn");
		} 
		catch(Exception exception)
		{
			throw exception;
		}
	}
}
