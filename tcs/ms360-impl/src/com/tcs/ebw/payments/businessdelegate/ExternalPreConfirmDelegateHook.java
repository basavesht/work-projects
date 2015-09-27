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

import com.tcs.Payments.ms360Utils.ActionDesc;
import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.PageStateDesc;
import com.tcs.Payments.ms360Utils.ScreenDesc;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.Payments.serverValidations.ValidateACHTransfer;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.ms360.integration.MMAccount;
import com.tcs.bancs.ms360.integration.MMContext;
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
 *  224703			31-05-2011      P3				 Server-side check.  
 * **********************************************************
 */
public class ExternalPreConfirmDelegateHook extends BusinessDelegateHook {

	/**
	 * Pre hook method for ExternalTransfer confirm event in INIT mode...
	 * @throws Exception 
	 */
	public void preExternalPreConfirmExternalPreConfirm_INITconfirmbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preExternalPreConfirmExternalPreConfirm_INITconfirmbut"); 
		try 
		{
			ExternalPreConfirmForm objExternalPreConfirm = (ExternalPreConfirmForm)objSourceForm;
			Object extPreConfirmToObj[] = (Object[])objParam[1];

			//Server side field validations...
			ValidateACHTransfer objVaidateExtTransfer= new ValidateACHTransfer();
			String validationErr = objVaidateExtTransfer.validateACHTransferConfirmFrm(objExternalPreConfirm);
			if(validationErr!=null && !validationErr.trim().equalsIgnoreCase("")){
				throw new Exception(validationErr); // Form field validation failed ...
			}

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			String fromKeyAccount =objExternalPreConfirm.getFromKeyAccNumber(); 
			String toKeyAccount =objExternalPreConfirm.getToKeyAccNumber();
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)extPreConfirmToObj[0];

			MMContext objMMContextData = objUserPrincipal.getContextData();
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
					objFromMSAcc_Details.setKeyClientId(MSCommonUtils.extractKeyClientId(objExternalPreConfirm.getDebitAcntPlatingInfo()));
					objFromMSAcc_Details.setAccountPlating(objExternalPreConfirm.getDebitAcntPlatingInfo());
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
					objToMSAcc_Details.setKeyClientId(MSCommonUtils.extractKeyClientId(objExternalPreConfirm.getCreditAcntPlatingInfo()));
					objToMSAcc_Details.setAccountPlating(objExternalPreConfirm.getCreditAcntPlatingInfo());
					extPreConfirmToObj[2]= objToMSAcc_Details;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			String userRole=objMSUserDetails.getInitiatorRole();
			String userId= objMSUserDetails.getRcafId();
			extPreConfirmToObj[3] = objMSUserDetails;

			//Payment details attribute mappings..
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
			objPaymentDetails.setAuth_mode(objExternalPreConfirm.getAuth_mode());
			objPaymentDetails.setAuth_info_reciever(objExternalPreConfirm.getInformationRecvdBy());
			objPaymentDetails.setVerbal_auth_client_name(objExternalPreConfirm.getSpokeTo());
			objPaymentDetails.setOtherAcntOwner(objExternalPreConfirm.getOtherAcntOwner());
			objPaymentDetails.setVerbal_auth_date(objExternalPreConfirm.getVerbalAuthDate());
			objPaymentDetails.setVerbal_auth_time(MSCommonUtils.formatAuthTime(objExternalPreConfirm.getVerbalAuthHour(),objExternalPreConfirm.getVerbalAuthMin(),objExternalPreConfirm.getVerbalAuthAMorPM()));
			objPaymentDetails.setClient_verification_desc(objExternalPreConfirm.getClientVerfication());
			objPaymentDetails.setRetirement_mnemonic(objExternalPreConfirm.getTransferTypeIRA());
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
			objPaymentDetails.setInitiator_id(userId); //RACF Id of the FA..
			objPaymentDetails.setInitiator_role(userRole);
			objPaymentDetails.setCurrent_owner_id(userId); //RACF Id of the FA..
			objPaymentDetails.setCurrent_owner_role(userRole);
			objPaymentDetails.setCreated_by_comments(objExternalPreConfirm.getUserComments());
			objPaymentDetails.setDebitAcntDataInSession(objExternalPreConfirm.isDebitAcntDataInSession());
			objPaymentDetails.setCreditAcntDataInSession(objExternalPreConfirm.isCreditAcntDataInSession());

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.ACT.toString());
			objPaymentDetails.setAction(ActionDesc.CFM.toString());
			objPaymentDetails.setState(PageStateDesc.I.toString());

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

	/**
	 * Post hook method for ExternalTransfer confirm event in INIT mode...
	 * @throws Exception 
	 */
	public void postExternalPreConfirmExternalPreConfirm_INITconfirmbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting postExternalPreConfirmExternalPreConfirm_INITconfirmbut");
			ExternalConfirmForm objExternalConfirm=(ExternalConfirmForm) objTargetForm;
			objUserPrincipal.setBrErrorMessages(null);
			String brMessages="";

			//Output extraction...
			ArrayList transactionOutDetails = (ArrayList)objReturn;
			ServiceContext contextData = (ServiceContext)transactionOutDetails.get(0);

			//Response...
			MSCommonUtils.logEventResponse(contextData);
			if(contextData.getMaxSeverity()== MessageType.CRITICAL){
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
					//TO Object Mapping..
					if(transactionOutDetails.get(1)!=null)
					{
						HashMap txnOutDetails = (HashMap)transactionOutDetails.get(1);
						TrxnDetailsOutputTO trxnDetailsOutputTO = new TrxnDetailsOutputTO();

						//Setting the payment attributes..
						if(txnOutDetails.containsKey("TxnViewDetails")){
							trxnDetailsOutputTO = (TrxnDetailsOutputTO)txnOutDetails.get("TxnViewDetails");
						}

						String auth_mode = trxnDetailsOutputTO.getAuth_mode();
						objExternalConfirm.setTxnDetails(trxnDetailsOutputTO);

						//Setting the screen field attributes ...
						objExternalConfirm.setConfirmationNo(trxnDetailsOutputTO.getConfirmationNo());
						objExternalConfirm.setTrxnType(trxnDetailsOutputTO.getTrxnType());
						objExternalConfirm.setTrxnAmount(MSCommonUtils.formatTxnAmount(trxnDetailsOutputTO.getTrxnAmount()));
						objExternalConfirm.setFrequencyType(trxnDetailsOutputTO.getFrequencyType());
						objExternalConfirm.setUntilDollarLimit(ConvertionUtil.convertToString(trxnDetailsOutputTO.getUntilDollarLimit()));

						//Account tier details...
						objExternalConfirm.setAccTierFromAcc(trxnDetailsOutputTO.getAccTierFromAcc());
						objExternalConfirm.setAccTierToAcc(trxnDetailsOutputTO.getAccTierToAcc());

						//Account number (3-6-3) details..
						objExternalConfirm.setAccNoFromAcc(trxnDetailsOutputTO.getAccNoFromAcc());
						objExternalConfirm.setAccNoToAcc(trxnDetailsOutputTO.getAccNoToAcc());

						//Routing number details..
						objExternalConfirm.setRoutingNoFromAcc(trxnDetailsOutputTO.getRoutingNoFromAcc());
						objExternalConfirm.setRoutingNoToAcc(trxnDetailsOutputTO.getRoutingNoToAcc());

						//Account Owner details..
						objExternalConfirm.setOwnerFromAcc(trxnDetailsOutputTO.getOwnerFromAcc());
						objExternalConfirm.setOwnerToAcc(trxnDetailsOutputTO.getOwnerToAcc());

						//Account held at details..
						objExternalConfirm.setAccHeldAtFromAcc(trxnDetailsOutputTO.getAccHeldAtFromAcc());
						objExternalConfirm.setAccHeldAtToAcc(trxnDetailsOutputTO.getAccHeldAtToAcc());

						//Account class details..
						objExternalConfirm.setAccClassFromAcc(trxnDetailsOutputTO.getAccClassFromAcc());
						objExternalConfirm.setAccClassToAcc(trxnDetailsOutputTO.getAccClassToAcc());

						//Account sub class details..
						objExternalConfirm.setAccSubClassFromAcc(trxnDetailsOutputTO.getAccSubClassFromAcc());
						objExternalConfirm.setAccSubClassToAcc(trxnDetailsOutputTO.getAccSubClassToAcc());

						//Account name details...
						objExternalConfirm.setAccNameFromAcc(trxnDetailsOutputTO.getAccNameFromAcc());
						objExternalConfirm.setAccNameToAcc(trxnDetailsOutputTO.getAccNameToAcc());

						//Account Other Details ...
						objExternalConfirm.setAccDtlsFromAcc(trxnDetailsOutputTO.getAccDtlsFromAcc());
						objExternalConfirm.setAccDtlsToAcc(trxnDetailsOutputTO.getAccDtlsToAcc());

						//Payment hidden attributes...
						objExternalConfirm.setTxnStatusDesc(trxnDetailsOutputTO.getStatus());
						objExternalConfirm.setBatchRefNumber(trxnDetailsOutputTO.getPaybatchref());
						objExternalConfirm.setVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getVersionnum().intValue()));
						objExternalConfirm.setBatchVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getBatversionnum()));
						objExternalConfirm.setParTxnVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getPartxnversionnum()));
						objExternalConfirm.setTxnTypeCode(ConvertionUtil.convertToString(trxnDetailsOutputTO.getTxnTypeCode()));
						objExternalConfirm.setEntryDate(ConvertionUtil.convertToAppDateStr(trxnDetailsOutputTO.getTxnEntryDate()));
						objExternalConfirm.setTransDate(ConvertionUtil.convertToAppDateStr(trxnDetailsOutputTO.getTxnInitiationDate()));
						objExternalConfirm.setRetirement_TxnTypeDesc(trxnDetailsOutputTO.getRetirement_TxnTypeDesc());
						objExternalConfirm.setQualifier(trxnDetailsOutputTO.getQualifier());
						objExternalConfirm.setReverse_qualifier(trxnDetailsOutputTO.getReverse_qualifier());
						objExternalConfirm.setDisplay_qualifier(trxnDetailsOutputTO.getDisplay_qualifier());
						objExternalConfirm.setScreenNavigationFlag(MSSystemDefaults.EXTERNAL_TRANSFERS);
						objExternalConfirm.setRetirement_mnemonic(trxnDetailsOutputTO.getRetirement_mnemonic());

						//Setting the Table Data for the Business Code and Txn Details Table...
						objExternalConfirm.setReasonCodesTableData(trxnDetailsOutputTO.getReasonCodesList());
						objExternalConfirm.setActionDetailsTableData(trxnDetailsOutputTO.getActionsList());
						if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_VERBAL)){
							objExternalConfirm.setAuthDetailsTableData(trxnDetailsOutputTO.getAuthDetailsList());
						}
						else if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_SIGNED)){
							objExternalConfirm.setSignedAuthDetailsTableData(trxnDetailsOutputTO.getSignedAuthDetailsList());
						}
						objExternalConfirm.setAuth_mode(trxnDetailsOutputTO.getAuth_mode());
						objExternalConfirm.setRecurringFlag(trxnDetailsOutputTO.getRecurringFlag());
					}
					objExternalConfirm.setBusinessRuleMessages(brMessages);
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postExternalPreConfirmExternalPreConfirm_INITconfirmbut"); 
	}

	/**
	 * Pre hook method for ExternalTransfer confirm event in edit mode...
	 * @throws Exception 
	 */
	public void preExternalPreConfirmExternalPreConfirm_Editconfirmbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preExternalPreConfirmExternalPreConfirm_Editconfirmbut"); 
			ExternalPreConfirmForm objExternalPreConfirm = (ExternalPreConfirmForm)objSourceForm;
			Object extPreConfirmToObj[] = (Object[])objParam[1];

			//Server side field validations...
			ValidateACHTransfer objVaidateExtTransfer= new ValidateACHTransfer();
			String validationErr = objVaidateExtTransfer.validateACHTransferConfirmFrm(objExternalPreConfirm);
			if(validationErr!=null && !validationErr.trim().equalsIgnoreCase("")){
				throw new Exception(validationErr); // Form field validation failed ...
			}

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();	
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			String txn_type = objExternalPreConfirm.getTransactionType();
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)extPreConfirmToObj[0];

			//From Or To account key_client_id mappings if applicable...
			if(txn_type!=null && txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
				objFromMSAcc_Details.setKeyClientId(MSCommonUtils.extractKeyClientId(objExternalPreConfirm.getDebitAcntPlatingInfo()));
				objFromMSAcc_Details.setAccountPlating(objExternalPreConfirm.getDebitAcntPlatingInfo());
				extPreConfirmToObj[1] = objFromMSAcc_Details;
			}
			else if(txn_type!=null && txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)) {
				ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
				objToMSAcc_Details.setKeyClientId(MSCommonUtils.extractKeyClientId(objExternalPreConfirm.getCreditAcntPlatingInfo()));
				objToMSAcc_Details.setAccountPlating(objExternalPreConfirm.getCreditAcntPlatingInfo());
				extPreConfirmToObj[2] = objToMSAcc_Details;
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			String userId= objMSUserDetails.getRcafId();
			String userRole= objMSUserDetails.getInitiatorRole();
			extPreConfirmToObj[3] = objMSUserDetails;

			//Payment details attribute mappings..
			objPaymentDetails.setFrmAcc_InContext(false);
			objPaymentDetails.setToAcc_InContext(false);
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
			objPaymentDetails.setAuth_mode(objExternalPreConfirm.getAuth_mode());
			objPaymentDetails.setAuth_info_reciever(objExternalPreConfirm.getInformationRecvdBy());
			objPaymentDetails.setVerbal_auth_client_name(objExternalPreConfirm.getSpokeTo());
			objPaymentDetails.setOtherAcntOwner(objExternalPreConfirm.getOtherAcntOwner());
			objPaymentDetails.setVerbal_auth_date(objExternalPreConfirm.getVerbalAuthDate());
			objPaymentDetails.setVerbal_auth_time(MSCommonUtils.formatAuthTime(objExternalPreConfirm.getVerbalAuthHour(),objExternalPreConfirm.getVerbalAuthMin(),objExternalPreConfirm.getVerbalAuthAMorPM()));
			objPaymentDetails.setClient_verification_desc(objExternalPreConfirm.getClientVerfication());
			objPaymentDetails.setScreen_Type(TxnTypeCode.ACH_TYPE);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setTrial_depo("N");
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Modify_Confirm);
			objPaymentDetails.setTxnModified(true);
			objPaymentDetails.setPrevAction("Modify");
			objPaymentDetails.setTransfer_Type(objExternalPreConfirm.getTransactionType());
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
			objPaymentDetails.setCurrent_owner_id(userId);
			objPaymentDetails.setCurrent_owner_role(userRole);
			objPaymentDetails.setCreated_by_comments(objExternalPreConfirm.getUserComments());
			objPaymentDetails.setDebitAcntDataInSession(objExternalPreConfirm.isDebitAcntDataInSession());
			objPaymentDetails.setCreditAcntDataInSession(objExternalPreConfirm.isCreditAcntDataInSession());

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.ACT.toString());
			objPaymentDetails.setAction(ActionDesc.CFM.toString());
			objPaymentDetails.setState(PageStateDesc.E.toString());

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
	 * Post hook method for ExternalTransfer confirm event in edit mode...
	 * @throws Exception 
	 */
	public void postExternalPreConfirmExternalPreConfirm_Editconfirmbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postExternalPreConfirmExternalPreConfirm_Editconfirmbut");
		try 
		{
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
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_SEARCHES);
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
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_SEARCHES);
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
						objExternalConfirm.setTxnDetails(trxnDetailsOutputTO);

						//Setting the screen field attributes ...
						objExternalConfirm.setConfirmationNo(trxnDetailsOutputTO.getConfirmationNo());
						objExternalConfirm.setTrxnType(trxnDetailsOutputTO.getTrxnType());
						objExternalConfirm.setTrxnAmount(MSCommonUtils.formatTxnAmount(trxnDetailsOutputTO.getTrxnAmount()));
						objExternalConfirm.setFrequencyType(trxnDetailsOutputTO.getFrequencyType());
						objExternalConfirm.setUntilDollarLimit(ConvertionUtil.convertToString(trxnDetailsOutputTO.getUntilDollarLimit()));

						//Account tier details...
						objExternalConfirm.setAccTierFromAcc(trxnDetailsOutputTO.getAccTierFromAcc());
						objExternalConfirm.setAccTierToAcc(trxnDetailsOutputTO.getAccTierToAcc());

						//Account number (3-6-3) details..
						objExternalConfirm.setAccNoFromAcc(trxnDetailsOutputTO.getAccNoFromAcc());
						objExternalConfirm.setAccNoToAcc(trxnDetailsOutputTO.getAccNoToAcc());

						//Routing number details..
						objExternalConfirm.setRoutingNoFromAcc(trxnDetailsOutputTO.getRoutingNoFromAcc());
						objExternalConfirm.setRoutingNoToAcc(trxnDetailsOutputTO.getRoutingNoToAcc());

						//Account Owner details..
						objExternalConfirm.setOwnerFromAcc(trxnDetailsOutputTO.getOwnerFromAcc());
						objExternalConfirm.setOwnerToAcc(trxnDetailsOutputTO.getOwnerToAcc());

						//Account held at details..
						objExternalConfirm.setAccHeldAtFromAcc(trxnDetailsOutputTO.getAccHeldAtFromAcc());
						objExternalConfirm.setAccHeldAtToAcc(trxnDetailsOutputTO.getAccHeldAtToAcc());

						//Account class details..
						objExternalConfirm.setAccClassFromAcc(trxnDetailsOutputTO.getAccClassFromAcc());
						objExternalConfirm.setAccClassToAcc(trxnDetailsOutputTO.getAccClassToAcc());

						//Account sub class details..
						objExternalConfirm.setAccSubClassFromAcc(trxnDetailsOutputTO.getAccSubClassFromAcc());
						objExternalConfirm.setAccSubClassToAcc(trxnDetailsOutputTO.getAccSubClassToAcc());

						//Account name details...
						objExternalConfirm.setAccNameFromAcc(trxnDetailsOutputTO.getAccNameFromAcc());
						objExternalConfirm.setAccNameToAcc(trxnDetailsOutputTO.getAccNameToAcc());

						//Account Other Details ...
						objExternalConfirm.setAccDtlsFromAcc(trxnDetailsOutputTO.getAccDtlsFromAcc());
						objExternalConfirm.setAccDtlsToAcc(trxnDetailsOutputTO.getAccDtlsToAcc());

						//Payment hidden attributes...
						objExternalConfirm.setTxnStatusDesc(trxnDetailsOutputTO.getStatus());
						objExternalConfirm.setBatchRefNumber(trxnDetailsOutputTO.getPaybatchref());
						objExternalConfirm.setVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getVersionnum().intValue()));
						objExternalConfirm.setBatchVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getBatversionnum()));
						objExternalConfirm.setParTxnVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getPartxnversionnum()));
						objExternalConfirm.setTxnTypeCode(ConvertionUtil.convertToString(trxnDetailsOutputTO.getTxnTypeCode()));
						objExternalConfirm.setEntryDate(ConvertionUtil.convertToAppDateStr(trxnDetailsOutputTO.getTxnEntryDate()));
						objExternalConfirm.setTransDate(ConvertionUtil.convertToAppDateStr(trxnDetailsOutputTO.getTxnInitiationDate()));
						objExternalConfirm.setRetirement_TxnTypeDesc(trxnDetailsOutputTO.getRetirement_TxnTypeDesc());
						objExternalConfirm.setQualifier(trxnDetailsOutputTO.getQualifier());
						objExternalConfirm.setReverse_qualifier(trxnDetailsOutputTO.getReverse_qualifier());
						objExternalConfirm.setDisplay_qualifier(trxnDetailsOutputTO.getDisplay_qualifier());
						objExternalConfirm.setScreenNavigationFlag(MSSystemDefaults.CTC_SEARCHES);
						objExternalConfirm.setRetirement_mnemonic(trxnDetailsOutputTO.getRetirement_mnemonic());

						//Setting the Table Data for the Business Code and Txn Details Table...
						objExternalConfirm.setReasonCodesTableData(trxnDetailsOutputTO.getReasonCodesList());
						objExternalConfirm.setActionDetailsTableData(trxnDetailsOutputTO.getActionsList());
						if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_VERBAL)){
							objExternalConfirm.setAuthDetailsTableData(trxnDetailsOutputTO.getAuthDetailsList());
						}
						else if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_SIGNED)){
							objExternalConfirm.setSignedAuthDetailsTableData(trxnDetailsOutputTO.getSignedAuthDetailsList());
						}
						objExternalConfirm.setAuth_mode(trxnDetailsOutputTO.getAuth_mode());
						objExternalConfirm.setRecurringFlag(trxnDetailsOutputTO.getRecurringFlag());
					}
					objExternalConfirm.setBusinessRuleMessages(brMessages);
				}
			}
		} catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished postExternalPreConfirmExternalPreConfirm_Editconfirmbut"); 
	}

}