/*
 * Created on Tue Jun 09 10:07:36 IST 2009
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
import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.PageStateDesc;
import com.tcs.Payments.ms360Utils.ScreenDesc;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.businessdelegate.BusinessDelegateHook;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.services.channelPaymentServices.BusinessRulesService;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ConfirmPopupApproveDelegateHook extends BusinessDelegateHook {

	/**
	 * Pre hook method for Approve onload event in init mode...
	 * @throws Exception 
	 */
	public void preConfirmPopupApproveConfirmPopupApprove_INITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preConfirmPopupApproveConfirmPopupApprove_INITINIT");
		try
		{
			ConfirmPopupApproveForm objConfirmPopupApprove=(ConfirmPopupApproveForm) objSourceForm;
			Object[] approvalTxnTO = (Object[])objParam[1];

			//Mapping the payment attributes...
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)approvalTxnTO[0];
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();	

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			approvalTxnTO[1] = objMSUserDetails;

			//Mapping the payment details object..
			objPaymentDetails.setFrmAcc_InContext(false);
			objPaymentDetails.setToAcc_InContext(false);
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Approve_PreConfirm);
			objPaymentDetails.setTxnApproved(true);
			objPaymentDetails.setPrevAction("Approve");
			objPaymentDetails.setStatusChkEventDesc("Approve_Txn");
			objPaymentDetails.setStatusChkId("TXNSTATUS");
			objPaymentDetails.setStatusChkReq(true);
			objPaymentDetails.setTransfer_Type(objConfirmPopupApprove.getTrxnType());
			objPaymentDetails.setTransactionId(objConfirmPopupApprove.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objConfirmPopupApprove.getVersionnum());
			objPaymentDetails.setTxnBatchId(objConfirmPopupApprove.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objConfirmPopupApprove.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnVersion(objConfirmPopupApprove.getParTxnVersionnum());
			objPaymentDetails.setTxnCurrentStatusCode(objConfirmPopupApprove.getPayStatusCode());
			objPaymentDetails.setTxnPrevStatusCode(objConfirmPopupApprove.getPayStatusCode());

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.APR.toString());
			objPaymentDetails.setAction(ActionDesc.APR.toString());
			objPaymentDetails.setState(PageStateDesc.I.toString());
		} 
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preConfirmPopupApproveConfirmPopupApprove_INITINIT"); 
	}

	/**
	 * Post hook method for Approve onload event in init mode...
	 * @throws Exception 
	 */
	public void postConfirmPopupApproveConfirmPopupApprove_INITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postConfirmPopupApproveConfirmPopupApprove_INITINIT");
		try 
		{
			ConfirmPopupApproveForm objConfirmPopupApprove=(ConfirmPopupApproveForm) objTargetForm;
			ArrayList brReasonsCode = new ArrayList();
			objUserPrincipal.setBrErrorMessages(null);

			//Output Extraction....
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
					objConfirmPopupApprove.setBrErrors(true);
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

					//JSON Mapping....
					objConfirmPopupApprove.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_APPROVALS);
				}
				else
				{
					//Setting the BR Reason Validation Table...
					if(transactionOutDetails.get(1)!=null)
					{
						HashMap txnOutDetails = (HashMap)transactionOutDetails.get(1);
						PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
						BusinessRulesService objBROut = new BusinessRulesService();

						//Setting the payment attributes..
						if(txnOutDetails.containsKey("PaymentDetails")){
							objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
						}
						objConfirmPopupApprove.setBusinessDate(ConvertionUtil.convertToAppDateStr(objPaymentDetails.getBusiness_Date()));

						//Mapping from the Business Rule response
						if(txnOutDetails.containsKey("BROutputDetails")){
							objBROut= (BusinessRulesService)txnOutDetails.get("BROutputDetails");

							//Next approver mappings..
							if(objBROut.isNEXT_APPROVAL_REQ() && (objBROut.getNext_approver_role()!=null && !objBROut.getNext_approver_role().trim().equalsIgnoreCase(""))){
								objConfirmPopupApprove.setNext_approver_req_flag("true");
								objConfirmPopupApprove.setNext_approver_role(objBROut.getNext_approver_role());
							}
							//Dont_spawn flag mapping..
							if(objBROut.getDont_spawn_flag()!=null && !objBROut.getDont_spawn_flag().trim().equalsIgnoreCase("")){
								objConfirmPopupApprove.setDont_spawn_flag(objBROut.getDont_spawn_flag());
							}
							//OFAC Case Id..
							if(objBROut.getOfac_case_id()!=null && !objBROut.getOfac_case_id().trim().equalsIgnoreCase("")){
								objConfirmPopupApprove.setOfac_case_id(objBROut.getOfac_case_id());
							}
						}

						//Setting the BR Reason codes details..
						if(txnOutDetails.containsKey("BRReasonCodes")){
							brReasonsCode = (ArrayList)txnOutDetails.get("BRReasonCodes");
							if(!brReasonsCode.isEmpty()){
								objConfirmPopupApprove.setReasonCodesTableData(brReasonsCode);
							}
						}
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postConfirmPopupApproveConfirmPopupApprove_INITINIT"); 
	}

	/**
	 * Pre hook method for Approve confirm event in init mode...
	 * @throws Exception 
	 */
	public void preConfirmPopupApproveConfirmPopupApprove_INITconfirmBtn(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preConfirmPopupApproveConfirmPopupApprove_INITconfirmBtn"); 
		try 
		{
			ConfirmPopupApproveForm objConfirmPopupApprove=(ConfirmPopupApproveForm) objSourceForm;
			Object[] approvalTxnTO = (Object[])objParam[1];

			//Mapping the payment attributes..
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)approvalTxnTO[0];
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();	
			String next_approver_req_flag = objConfirmPopupApprove.getNext_approver_req_flag();

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			approvalTxnTO[1] = objMSUserDetails;

			//Mapping the Payments details attributes..
			objPaymentDetails.setFrmAcc_InContext(false);
			objPaymentDetails.setToAcc_InContext(false);
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Approve_Confirm);
			objPaymentDetails.setTxnApproved(true);
			objPaymentDetails.setPrevAction("Approve");
			objPaymentDetails.setTransfer_Type(objConfirmPopupApprove.getTrxnType());
			objPaymentDetails.setTransactionId(objConfirmPopupApprove.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objConfirmPopupApprove.getVersionnum());
			objPaymentDetails.setTxnBatchId(objConfirmPopupApprove.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objConfirmPopupApprove.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnVersion(objConfirmPopupApprove.getParTxnVersionnum());
			objPaymentDetails.setTxnCurrentStatusCode(objConfirmPopupApprove.getPayStatusCode());
			objPaymentDetails.setTxnPrevStatusCode(objConfirmPopupApprove.getPayStatusCode());
			objPaymentDetails.setCreated_by_comments(objConfirmPopupApprove.getComments());

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.APR.toString());
			objPaymentDetails.setAction(ActionDesc.APR.toString());
			objPaymentDetails.setState(PageStateDesc.I.toString());

			//Checking for approve flag and next approver...
			if(next_approver_req_flag!=null && next_approver_req_flag.equalsIgnoreCase("true")){
				objPaymentDetails.setNextApproval_Req(true);
				objPaymentDetails.setNext_approver_role(objConfirmPopupApprove.getNext_approver_role());
			}

			//Setting the dont_spawn flag as received on the ApprovePreConfirm ...
			objPaymentDetails.setDont_spawn_flag(objConfirmPopupApprove.getDont_spawn_flag());

			//Setting the ofac_case_id as on the ApprovePreConfirm ....
			objPaymentDetails.setOfac_case_id(objConfirmPopupApprove.getOfac_case_id());

		} 
		catch (Exception exception){
			throw exception;
		}
		EBWLogger.trace(this, "Finished preConfirmPopupApproveConfirmPopupApprove_INITconfirmBtn"); 
	}

	/**
	 * Post hook method for Approve confirm event in init mode...
	 * @throws Exception 
	 */
	public void postConfirmPopupApproveConfirmPopupApprove_INITconfirmBtn(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postConfirmPopupApproveConfirmPopupApprove_INITconfirmBtn");
		try 
		{
			ConfirmPopupApproveForm objConfirmPopupApprove=(ConfirmPopupApproveForm) objSourceForm;
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
					objConfirmPopupApprove.setBrErrors(true);
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
					objConfirmPopupApprove.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_APPROVALS);
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postConfirmPopupApproveConfirmPopupApprove_INITconfirmBtn"); 
	}

}