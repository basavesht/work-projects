/*
 * Created on Mon Jun 08 18:21:46 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tcs.Payments.ms360Utils.ActionDesc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.PageStateDesc;
import com.tcs.Payments.ms360Utils.ScreenDesc;
import com.tcs.Payments.serverValidations.ValidateApprovals;
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
 *
 * **********************************************************
 */
public class ConfirmPopupRejectDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for ConfirmPopupReject Screen, ConfirmPopupReject_INIT State and INIT Event. 
	 * @throws Exception 
	 */
	public void preConfirmPopupRejectConfirmPopupReject_INITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preConfirmPopupRejectConfirmPopupReject_INITINIT"); 
		try 
		{
			Object[] rejectTxnTO = (Object[])objParam[1];
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.APR.toString());
			objPaymentDetails.setAction(ActionDesc.RJT.toString());
			objPaymentDetails.setState(PageStateDesc.I.toString());

			//Server side field validations for FAP...
			ValidateApprovals objValidateApprovals= new ValidateApprovals();
			ServiceContext contextData = objValidateApprovals.validatePageAccess(objUserPrincipal,objPaymentDetails);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}

			//Business Date Input Mappings..
			DsConfigDetailsTO objDsConfigDetails = (DsConfigDetailsTO)rejectTxnTO[0];
			String qzBranchCode = PropertyFileReader.getProperty("OU_ID"); 
			objDsConfigDetails.setBranch_code(qzBranchCode);
		}
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preConfirmPopupRejectConfirmPopupReject_INITINIT"); 
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
	public void postConfirmPopupRejectConfirmPopupReject_INITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postConfirmPopupRejectConfirmPopupReject_INITINIT");
		try 
		{
			ConfirmPopupRejectForm objConfirmPopupReject=(ConfirmPopupRejectForm) objSourceForm;
			HashMap txnDetails = new HashMap();

			//Output Mappings..
			DsConfigDetailsOut dsConfigDetailsOut = ((DsConfigDetailsOut)((Object []) objReturn)[0]);
			objConfirmPopupReject.setBusinessDate(ConvertionUtil.convertToAppDateStr(dsConfigDetailsOut.getBusiness_date()));
			ArrayList event_valid_statuses = ((ArrayList)((Object []) objReturn)[1]);

			//Payment Details Mappings...
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			objPaymentDetails.setBusiness_Date(objConfirmPopupReject.getBusinessDate());
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setTransfer_Type(objConfirmPopupReject.getTransferType());
			objPaymentDetails.setTxnCurrentStatusCode(objConfirmPopupReject.getPayStatusCode());
			objPaymentDetails.setTxnRejected(true);
			txnDetails.put("PaymentDetails",objPaymentDetails);

			//Checking for the status consistency and cut off time check...
			ValidateApprovals objValidateReject = new ValidateApprovals();
			ServiceContext contextData = objValidateReject.validateRejectTxn(txnDetails,event_valid_statuses);

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
					objConfirmPopupReject.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_APPROVALS);
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
					objConfirmPopupReject.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_APPROVALS);
				}
			}
			EBWLogger.trace(this, "Finished postConfirmPopupRejectConfirmPopupReject_INITINIT");
		}
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Method for ConfirmPopupReject Screen, ConfirmPopupReject_INIT State and confirmBtn Event. 
	 * @throws Exception 
	 */
	public void preConfirmPopupRejectConfirmPopupReject_INITconfirmBtn(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preConfirmPopupRejectConfirmPopupReject_INITconfirmBtn"); 
		try 
		{
			ConfirmPopupRejectForm objConfirmPopupReject=(ConfirmPopupRejectForm) objSourceForm;
			Object[] rejectTxnTO = (Object[])objParam[1];

			//Mapping the payment attributes...
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)rejectTxnTO[0];
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();	

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			rejectTxnTO[1] = objMSUserDetails;

			//Mapping the payment details object..
			objPaymentDetails.setFrmAcc_InContext(false);
			objPaymentDetails.setToAcc_InContext(false);
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setPrevAction("Reject");
			objPaymentDetails.setTxnRejected(true);
			objPaymentDetails.setTransfer_Type(objConfirmPopupReject.getTransferType());
			objPaymentDetails.setTransactionId(objConfirmPopupReject.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objConfirmPopupReject.getVersionnum());
			objPaymentDetails.setTxnBatchId(objConfirmPopupReject.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objConfirmPopupReject.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnVersion(objConfirmPopupReject.getParTxnVersionnum());
			objPaymentDetails.setTxnCurrentStatusCode(objConfirmPopupReject.getPayStatusCode());
			objPaymentDetails.setTxnPrevStatusCode(objConfirmPopupReject.getPayStatusCode());
			objPaymentDetails.setBusiness_Date(objConfirmPopupReject.getBusinessDate());
			objPaymentDetails.setCreated_by_comments(objConfirmPopupReject.getComments());

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.APR.toString());
			objPaymentDetails.setAction(ActionDesc.RJT.toString());
			objPaymentDetails.setState(PageStateDesc.I.toString());
		} 
		catch(Exception exception){
			throw exception;
		}

		EBWLogger.trace(this, "Finished preConfirmPopupRejectConfirmPopupReject_INITconfirmBtn"); 
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
	public void postConfirmPopupRejectConfirmPopupReject_INITconfirmBtn(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postConfirmPopupRejectConfirmPopupReject_INITconfirmBtn");
		try 
		{
			ConfirmPopupRejectForm objConfirmPopupReject=(ConfirmPopupRejectForm) objSourceForm;
			objUserPrincipal.setBrErrorMessages(null);

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
					objConfirmPopupReject.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_APPROVALS);
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
					objConfirmPopupReject.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_APPROVALS);
				}
			}
		}
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postConfirmPopupRejectConfirmPopupReject_INITconfirmBtn"); 
	}
}