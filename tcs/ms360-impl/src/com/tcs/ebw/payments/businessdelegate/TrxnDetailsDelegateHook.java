/*
 * Created on Mon May 25 16:53:52 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.ActionDesc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.PageStateDesc;
import com.tcs.Payments.ms360Utils.ScreenDesc;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.businessdelegate.BusinessDelegateHook;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.transferobject.*;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       10-05-2011        2               CR 215 
 *    224703       29-08-2011        3-B             CR 193
 * **********************************************************
 */
public class TrxnDetailsDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for TrxnDetails Screen, TrxnDetails_INIT State and INIT Event. 
	 * @throws Exception 
	 */
	public void preTrxnDetailsTrxnDetails_INITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preTrxnDetailsTrxnDetails_INITINIT"); 
		TrxnDetailsForm objTrxnDetails=(TrxnDetailsForm) objSourceForm;
		Object[] txnViewDetailsTO = (Object[])objParam[1];
		try 
		{
			//Mapping the payment attributes...
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)txnViewDetailsTO[0];
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();	

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			txnViewDetailsTO[1] = objMSUserDetails;

			//Mapping the payment details object..
			objPaymentDetails.setFrmAcc_InContext(false);
			objPaymentDetails.setToAcc_InContext(false);
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setTxnApproved(true);
			objPaymentDetails.setPrevAction("View");
			objPaymentDetails.setStatusChkEventDesc("Approve_Txn");
			objPaymentDetails.setStatusChkId("TXNSTATUS");
			objPaymentDetails.setStatusChkReq(true);
			objPaymentDetails.setTransfer_Type(objTrxnDetails.getTxnTypeCode());
			objPaymentDetails.setTransactionId(objTrxnDetails.getConfirmationNoHidden());
			objPaymentDetails.setTxnBatchId(objTrxnDetails.getBatchRefNumber());
			objPaymentDetails.setTxnCurrentStatusCode(objTrxnDetails.getPayStatusCode());
			objPaymentDetails.setTxnPrevStatusCode(objTrxnDetails.getPayStatusCode());

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.APR);
			objPaymentDetails.setAction(ActionDesc.VEW);
			objPaymentDetails.setState(PageStateDesc.I);
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preTrxnDetailsTrxnDetails_INITINIT"); 
	}

	/**
	 * Post delegate hook population...
	 * @param objSourceForm
	 * @param objTargetForm
	 * @param objReturn
	 * @param objParam
	 * @param objUserPrincipal
	 * @param retainDataMap
	 * @param objUserSessionObject
	 * @throws Exception
	 */
	public void postTrxnDetailsTrxnDetails_INITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postTrxnDetailsTrxnDetails_INITINIT");
		try 
		{
			TrxnDetailsForm objTrxnDetailsForm=(TrxnDetailsForm) objSourceForm;
			ArrayList<Object> transactionOutDetails = (ArrayList<Object>)objReturn;
			ServiceContext contextData = (ServiceContext)transactionOutDetails.get(0);

			//Response...
			MSCommonUtils.logEventResponse(contextData);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}
			else
			{
				if(transactionOutDetails.get(1)!=null) 
				{
					HashMap txnOutDetails = (HashMap)transactionOutDetails.get(1);
					TrxnDetailsOutputTO trxnDetailsOutputTO = new TrxnDetailsOutputTO();

					//Setting the payment attributes..
					if(txnOutDetails.containsKey("TxnViewDetails")){
						trxnDetailsOutputTO = (TrxnDetailsOutputTO)txnOutDetails.get("TxnViewDetails");
					}

					String auth_mode = trxnDetailsOutputTO.getAuth_mode();

					//Setting the screen field attributes ...
					objTrxnDetailsForm.setConfirmationNo(trxnDetailsOutputTO.getConfirmationNo());
					objTrxnDetailsForm.setTrxnType(trxnDetailsOutputTO.getTrxnType());
					objTrxnDetailsForm.setTrxnAmount(MSCommonUtils.formatTxnAmount(trxnDetailsOutputTO.getTrxnAmount()));
					objTrxnDetailsForm.setFrequencyType(trxnDetailsOutputTO.getFrequencyType());
					objTrxnDetailsForm.setUntilDollarLimit(ConvertionUtil.convertToString(trxnDetailsOutputTO.getUntilDollarLimit()));

					//Account tier details...
					objTrxnDetailsForm.setAccTierFromAcc(trxnDetailsOutputTO.getAccTierFromAcc());
					objTrxnDetailsForm.setAccTierToAcc(trxnDetailsOutputTO.getAccTierToAcc());

					//Account number (3-6-3) details..
					objTrxnDetailsForm.setAccNoFromAcc(trxnDetailsOutputTO.getAccNoFromAcc());
					objTrxnDetailsForm.setAccNoToAcc(trxnDetailsOutputTO.getAccNoToAcc());

					//Routing number details..
					objTrxnDetailsForm.setRoutingNoFromAcc(trxnDetailsOutputTO.getRoutingNoFromAcc());
					objTrxnDetailsForm.setRoutingNoToAcc(trxnDetailsOutputTO.getRoutingNoToAcc());

					//Account Owner details..
					objTrxnDetailsForm.setOwnerFromAcc(trxnDetailsOutputTO.getOwnerFromAcc());
					objTrxnDetailsForm.setOwnerToAcc(trxnDetailsOutputTO.getOwnerToAcc());

					//Account held at details..
					objTrxnDetailsForm.setAccHeldAtFromAcc(trxnDetailsOutputTO.getAccHeldAtFromAcc());
					objTrxnDetailsForm.setAccHeldAtToAcc(trxnDetailsOutputTO.getAccHeldAtToAcc());

					//Account class details..
					objTrxnDetailsForm.setAccClassFromAcc(trxnDetailsOutputTO.getAccClassFromAcc());
					objTrxnDetailsForm.setAccClassToAcc(trxnDetailsOutputTO.getAccClassToAcc());

					//Account sub class details..
					objTrxnDetailsForm.setAccSubClassFromAcc(trxnDetailsOutputTO.getAccSubClassFromAcc());
					objTrxnDetailsForm.setAccSubClassToAcc(trxnDetailsOutputTO.getAccSubClassToAcc());

					//Account name details...
					objTrxnDetailsForm.setAccNameFromAcc(trxnDetailsOutputTO.getAccNameFromAcc());
					objTrxnDetailsForm.setAccNameToAcc(trxnDetailsOutputTO.getAccNameToAcc());

					//Account Other Details ...
					objTrxnDetailsForm.setAccDtlsFromAcc(trxnDetailsOutputTO.getAccDtlsFromAcc());
					objTrxnDetailsForm.setAccDtlsToAcc(trxnDetailsOutputTO.getAccDtlsToAcc());

					//Payment hidden attributes...
					objTrxnDetailsForm.setStatusHidden(trxnDetailsOutputTO.getStatus());
					objTrxnDetailsForm.setBatchRefNumber(trxnDetailsOutputTO.getPaybatchref());
					objTrxnDetailsForm.setVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getVersionnum().intValue()));
					objTrxnDetailsForm.setBatchVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getBatversionnum()));
					objTrxnDetailsForm.setParTxnVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getPartxnversionnum()));
					objTrxnDetailsForm.setTxnTypeCode(ConvertionUtil.convertToString(trxnDetailsOutputTO.getTxnTypeCode()));
					objTrxnDetailsForm.setEntryDate(ConvertionUtil.convertToAppDateStr(trxnDetailsOutputTO.getTxnEntryDate()));
					objTrxnDetailsForm.setTransDate(ConvertionUtil.convertToAppDateStr(trxnDetailsOutputTO.getTxnInitiationDate()));
					objTrxnDetailsForm.setRetirement_TxnTypeDesc(trxnDetailsOutputTO.getRetirement_TxnTypeDesc());
					objTrxnDetailsForm.setQualifier(trxnDetailsOutputTO.getQualifier());
					objTrxnDetailsForm.setReverse_qualifier(trxnDetailsOutputTO.getReverse_qualifier());
					objTrxnDetailsForm.setDisplay_qualifier(trxnDetailsOutputTO.getDisplay_qualifier());
					objTrxnDetailsForm.setRetirement_mnemonic(trxnDetailsOutputTO.getRetirement_mnemonic());

					//Setting the Table Data for the Business Code and Txn Details Table...
					objTrxnDetailsForm.setReasonCodesTableData(trxnDetailsOutputTO.getReasonCodesList());
					objTrxnDetailsForm.setActionDetailsTableData(trxnDetailsOutputTO.getActionsList());
					if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_VERBAL)){
						objTrxnDetailsForm.setAuthDetailsTableData(trxnDetailsOutputTO.getAuthDetailsList());
					}
					else if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_SIGNED)){
						objTrxnDetailsForm.setSignedAuthDetailsTableData(trxnDetailsOutputTO.getSignedAuthDetailsList());
					}
					objTrxnDetailsForm.setAuth_mode(trxnDetailsOutputTO.getAuth_mode());
					objTrxnDetailsForm.setRecurringFlag(trxnDetailsOutputTO.getRecurringFlag());
					objTrxnDetailsForm.setRecuringActiveFlg(trxnDetailsOutputTO.getRecurringActiveFlg());
					objTrxnDetailsForm.setOfac_case_id(trxnDetailsOutputTO.getOfac_case_id());

					//Setting the current dated flag...
					Date transferDate = trxnDetailsOutputTO.getTxnInitiationDate();
					Date businessDate = trxnDetailsOutputTO.getBusiness_Date();
					if(transferDate!=null && businessDate!=null  && transferDate.after(businessDate)){
						objTrxnDetailsForm.setCurrentDatedFlg("N");
					}
					else if(transferDate!=null && businessDate!=null  && transferDate.compareTo(businessDate) == 0){
						objTrxnDetailsForm.setCurrentDatedFlg("Y");
					}

					//Setting the Transaction Settlement flag...
					Date settleDate = trxnDetailsOutputTO.getTxnSettleDate();
					if(settleDate!=null && businessDate!=null && settleDate.compareTo(businessDate) >= 0 ){
						objTrxnDetailsForm.setTxnSettlememtFlg("N");
					}
					else {
						objTrxnDetailsForm.setTxnSettlememtFlg("Y");
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postTrxnDetailsTrxnDetails_INITINIT"); 
	}

	/**
	 * Method for TrxnDetails Screen, TrxnDetails_View State and INIT Event. 
	 * @throws Exception 
	 */
	public void preTrxnDetailsTrxnDetails_ViewINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preTrxnDetailsTrxnDetails_ViewINIT"); 
		TrxnDetailsForm objTrxnDetails=(TrxnDetailsForm) objSourceForm;
		Object[] txnViewDetailsTO = (Object[])objParam[1];
		try 
		{
			//Mapping the payment attributes...
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)txnViewDetailsTO[0];
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();	

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			txnViewDetailsTO[1] = objMSUserDetails;

			//Mapping the payment details object..
			objPaymentDetails.setFrmAcc_InContext(false);
			objPaymentDetails.setToAcc_InContext(false);
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setTxnApproved(true);
			objPaymentDetails.setPrevAction("View");
			objPaymentDetails.setStatusChkReq(false);
			objPaymentDetails.setTransfer_Type(objTrxnDetails.getTxnTypeCode());
			objPaymentDetails.setTransactionId(objTrxnDetails.getConfirmationNoHidden());
			objPaymentDetails.setTxnBatchId(objTrxnDetails.getBatchRefNumber());
			objPaymentDetails.setTxnCurrentStatusCode(objTrxnDetails.getPayStatusCode());
			objPaymentDetails.setTxnPrevStatusCode(objTrxnDetails.getPayStatusCode());

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.SCH);
			objPaymentDetails.setAction(ActionDesc.VEW);
			objPaymentDetails.setState(PageStateDesc.I);
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preTrxnDetailsTrxnDetails_ViewINIT"); 
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
	public void postTrxnDetailsTrxnDetails_ViewINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postTrxnDetailsTrxnDetails_ViewINIT");
		try 
		{
			TrxnDetailsForm objTrxnDetailsForm=(TrxnDetailsForm) objSourceForm;
			ArrayList<Object> transactionOutDetails = (ArrayList<Object>)objReturn;
			ServiceContext contextData = (ServiceContext)transactionOutDetails.get(0);

			//Response...
			MSCommonUtils.logEventResponse(contextData);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}
			else 
			{
				if(transactionOutDetails.get(1)!=null) 
				{
					HashMap txnOutDetails = (HashMap)transactionOutDetails.get(1);
					TrxnDetailsOutputTO trxnDetailsOutputTO = new TrxnDetailsOutputTO();

					//Setting the payment attributes..
					if(txnOutDetails.containsKey("TxnViewDetails")){
						trxnDetailsOutputTO = (TrxnDetailsOutputTO)txnOutDetails.get("TxnViewDetails");
					}

					String auth_mode = trxnDetailsOutputTO.getAuth_mode();

					//Setting the screen field attributes ...
					objTrxnDetailsForm.setConfirmationNo(trxnDetailsOutputTO.getConfirmationNo());
					objTrxnDetailsForm.setTrxnType(trxnDetailsOutputTO.getTrxnType());
					objTrxnDetailsForm.setTrxnAmount(MSCommonUtils.formatTxnAmount(trxnDetailsOutputTO.getTrxnAmount()));
					objTrxnDetailsForm.setFrequencyType(trxnDetailsOutputTO.getFrequencyType());
					objTrxnDetailsForm.setUntilDollarLimit(ConvertionUtil.convertToString(trxnDetailsOutputTO.getUntilDollarLimit()));

					//Account tier details...
					objTrxnDetailsForm.setAccTierFromAcc(trxnDetailsOutputTO.getAccTierFromAcc());
					objTrxnDetailsForm.setAccTierToAcc(trxnDetailsOutputTO.getAccTierToAcc());

					//Account number (3-6-3) details..
					objTrxnDetailsForm.setAccNoFromAcc(trxnDetailsOutputTO.getAccNoFromAcc());
					objTrxnDetailsForm.setAccNoToAcc(trxnDetailsOutputTO.getAccNoToAcc());

					//Routing number details..
					objTrxnDetailsForm.setRoutingNoFromAcc(trxnDetailsOutputTO.getRoutingNoFromAcc());
					objTrxnDetailsForm.setRoutingNoToAcc(trxnDetailsOutputTO.getRoutingNoToAcc());

					//Account Owner details..
					objTrxnDetailsForm.setOwnerFromAcc(trxnDetailsOutputTO.getOwnerFromAcc());
					objTrxnDetailsForm.setOwnerToAcc(trxnDetailsOutputTO.getOwnerToAcc());

					//Account held at details..
					objTrxnDetailsForm.setAccHeldAtFromAcc(trxnDetailsOutputTO.getAccHeldAtFromAcc());
					objTrxnDetailsForm.setAccHeldAtToAcc(trxnDetailsOutputTO.getAccHeldAtToAcc());

					//Account class details..
					objTrxnDetailsForm.setAccClassFromAcc(trxnDetailsOutputTO.getAccClassFromAcc());
					objTrxnDetailsForm.setAccClassToAcc(trxnDetailsOutputTO.getAccClassToAcc());

					//Account sub class details..
					objTrxnDetailsForm.setAccSubClassFromAcc(trxnDetailsOutputTO.getAccSubClassFromAcc());
					objTrxnDetailsForm.setAccSubClassToAcc(trxnDetailsOutputTO.getAccSubClassToAcc());

					//Account name details...
					objTrxnDetailsForm.setAccNameFromAcc(trxnDetailsOutputTO.getAccNameFromAcc());
					objTrxnDetailsForm.setAccNameToAcc(trxnDetailsOutputTO.getAccNameToAcc());

					//Account Other Details ...
					objTrxnDetailsForm.setAccDtlsFromAcc(trxnDetailsOutputTO.getAccDtlsFromAcc());
					objTrxnDetailsForm.setAccDtlsToAcc(trxnDetailsOutputTO.getAccDtlsToAcc());

					//Payment hidden attributes...
					objTrxnDetailsForm.setStatusHidden(trxnDetailsOutputTO.getStatus());
					objTrxnDetailsForm.setBatchRefNumber(trxnDetailsOutputTO.getPaybatchref());
					objTrxnDetailsForm.setVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getVersionnum().intValue()));
					objTrxnDetailsForm.setBatchVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getBatversionnum()));
					objTrxnDetailsForm.setParTxnVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getPartxnversionnum()));
					objTrxnDetailsForm.setTxnTypeCode(ConvertionUtil.convertToString(trxnDetailsOutputTO.getTxnTypeCode()));
					objTrxnDetailsForm.setEntryDate(ConvertionUtil.convertToAppDateStr(trxnDetailsOutputTO.getTxnEntryDate()));
					objTrxnDetailsForm.setTransDate(ConvertionUtil.convertToAppDateStr(trxnDetailsOutputTO.getTxnInitiationDate()));
					objTrxnDetailsForm.setRetirement_TxnTypeDesc(trxnDetailsOutputTO.getRetirement_TxnTypeDesc());
					objTrxnDetailsForm.setQualifier(trxnDetailsOutputTO.getQualifier());
					objTrxnDetailsForm.setReverse_qualifier(trxnDetailsOutputTO.getReverse_qualifier());
					objTrxnDetailsForm.setDisplay_qualifier(trxnDetailsOutputTO.getDisplay_qualifier());
					objTrxnDetailsForm.setRetirement_mnemonic(trxnDetailsOutputTO.getRetirement_mnemonic());

					//Setting the Table Data for the Business Code and Txn Details Table...
					objTrxnDetailsForm.setReasonCodesTableData(trxnDetailsOutputTO.getReasonCodesList());
					objTrxnDetailsForm.setActionDetailsTableData(trxnDetailsOutputTO.getActionsList());
					if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_VERBAL)){
						objTrxnDetailsForm.setAuthDetailsTableData(trxnDetailsOutputTO.getAuthDetailsList());
					}
					else if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_SIGNED)){
						objTrxnDetailsForm.setSignedAuthDetailsTableData(trxnDetailsOutputTO.getSignedAuthDetailsList());
					}
					objTrxnDetailsForm.setAuth_mode(trxnDetailsOutputTO.getAuth_mode());
					objTrxnDetailsForm.setRecurringFlag(trxnDetailsOutputTO.getRecurringFlag());
					objTrxnDetailsForm.setRecuringActiveFlg(trxnDetailsOutputTO.getRecurringActiveFlg());
					objTrxnDetailsForm.setOfac_case_id(trxnDetailsOutputTO.getOfac_case_id());

					//Setting the current dated flag...
					Date transferDate = trxnDetailsOutputTO.getTxnInitiationDate();
					Date businessDate = trxnDetailsOutputTO.getBusiness_Date();
					if(transferDate.after(businessDate)){
						objTrxnDetailsForm.setCurrentDatedFlg("N");
					}
					else if(transferDate.compareTo(businessDate) == 0){
						objTrxnDetailsForm.setCurrentDatedFlg("Y");
					}

					//Setting the Transaction Settlement flag...
					Date settleDate = trxnDetailsOutputTO.getTxnSettleDate();
					if(settleDate!=null && businessDate!=null && settleDate.compareTo(businessDate) >= 0 ){
						objTrxnDetailsForm.setTxnSettlememtFlg("N");
					}
					else {
						objTrxnDetailsForm.setTxnSettlememtFlg("Y");
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postTrxnDetailsTrxnDetails_ViewINIT"); 
	}

}