/*
 * Created on Tue Mar 24 11:05:07 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.businessdelegate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.FormatAccount;
import com.tcs.Payments.ms360Utils.InitiatorRoleDesc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.Payments.serverValidations.StatusConsistencyChk;
import com.tcs.Payments.serverValidations.ValidateACHTransfer;
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
public class ExternalTransferDelegateHook extends BusinessDelegateHook {

	/**
	 * Method for ExternalTransfer Screen, ExternalTransfer_MultiTransfer State and INIT Event. 
	 * @throws Exception 
	 */
	public void preExternalTransferExternalTransfer_IINITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preExternalTransferExternalTransfer_MultiTransferINIT"); 

			//Business Date Input Mappings..
			Object[] externalTrInitTO = (Object[])objParam[1];
			DsConfigDetailsTO objDsConfigDetails = (DsConfigDetailsTO)externalTrInitTO[0];
			String qzBranchCode = PropertyFileReader.getProperty("OU_ID"); 
			objDsConfigDetails.setBranch_code(qzBranchCode);

			//External Accounts Input Mappings...
			DsOnloadAccDetailsTO dsOnloadAccDetailsTO = (DsOnloadAccDetailsTO)externalTrInitTO[1];
			MMContext objMMContextData = objUserPrincipal.getContextData();
			dsOnloadAccDetailsTO.setKey_client_id(objMMContextData.getClientIdentifier());
		} 
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preExternalTransferExternalTransfer_MultiTransferINIT"); 
	}

	public void postExternalTransferExternalTransfer_IINITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postExternalTransferExternalTransfer_IINITINIT");
		try 
		{
			ExternalTransferForm objExternalTransfer=(ExternalTransferForm) objSourceForm;
			String forwardTo = "ExternalTransfer";
			objUserPrincipal.setBrErrorMessages(null); 

			//Output extraction...
			ArrayList<Object> dsOnloadAccDetails = (ArrayList<Object>)((Object []) objReturn)[1];
			ArrayList<Object> checkExtAccStatus = (ArrayList<Object>)dsOnloadAccDetails;
			ArrayList<Object> extAccStatusList  = new ArrayList<Object>();
			ArrayList<Object> businessErrors = new ArrayList<Object>();
			HashMap<String, JSONArray> busniessErrorMessages = new HashMap<String, JSONArray>();
			JSONArray jsonError = new JSONArray();
			ResourceBundle rsMessages = ResourceBundle.getBundle("ErrMessage");
			checkExtAccStatus.remove(0);
			if(!checkExtAccStatus.isEmpty())
			{
				for(int i=0;i<checkExtAccStatus.size();i++){
					ArrayList externalAccStatus=(ArrayList)checkExtAccStatus.get(i);
					extAccStatusList.add(externalAccStatus.get(0));
				}
				if(extAccStatusList.contains("32")){
					forwardTo="ExternalTransfer";
				}
				else if(extAccStatusList.contains("31")){
					businessErrors.add(rsMessages.getString("Pay_0053"));
					jsonError = JSONArray.fromObject(businessErrors);
					busniessErrorMessages.put("BusinessErrors", jsonError);
					JSONObject jsonErrorMess = JSONObject.fromObject(busniessErrorMessages);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					forwardTo="AccMaintenance";
				}
				else {
					businessErrors.add(rsMessages.getString("Pay_0052"));
					jsonError = JSONArray.fromObject(businessErrors);
					busniessErrorMessages.put("BusinessErrors", jsonError);
					JSONObject jsonErrorMess = JSONObject.fromObject(busniessErrorMessages);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					forwardTo="AddExternalAccount";
				}
			}
			else{
				businessErrors.add(rsMessages.getString("Pay_0052"));
				jsonError = JSONArray.fromObject(businessErrors);
				busniessErrorMessages.put("BusinessErrors", jsonError);
				JSONObject jsonErrorMess = JSONObject.fromObject(busniessErrorMessages);
				objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
				forwardTo="AddExternalAccount";
			}
			objExternalTransfer.setCheckExternalAccStatus(forwardTo);

			//Setting the local currency by default..
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			objExternalTransfer.setCurrencyCode(currencyCode);
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postExternalTransferExternalTransfer_IINITINIT"); 
	}

	/**
	 * Method for ExternalTransfer Screen, ExternalTransfer_INIT State and submitbut Event. 
	 * @throws Exception 
	 */
	public void preExternalTransferExternalTransfer_INITsubmitbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preExternalTransferExternalTransfer_INITsubmitbut"); 
			ExternalTransferForm objExternalTransfer=(ExternalTransferForm) objSourceForm;
			ServiceContext context = new ServiceContext();
			Object extTransferToObj[] = (Object[])objParam[1];

			//Server side field validations...
			ValidateACHTransfer objVaidateExtTransfer= new ValidateACHTransfer();
			String validationErr = objVaidateExtTransfer.validateACHTransferFrm(objExternalTransfer);
			if(validationErr!=null && !validationErr.trim().equalsIgnoreCase("")){
				throw new Exception(validationErr); // Form field validation failed ...
			}

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			String fromKeyAccount =objExternalTransfer.getFromAccount(); // Value may be MS Key account or Reference Id for the external account...
			String toKeyAccount =objExternalTransfer.getToAccount(); // Value may be MS Key account or Reference Id for the external account...
			String txnFrequency = objExternalTransfer.getFrequencycombo();
			Date transferReqDate = ConvertionUtil.convertToDate(objExternalTransfer.getInitiationDate());
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)extTransferToObj[0];

			//Context sharing for the Input Mappings 
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
					objPaymentDetails.setTransfer_Type(TxnTypeCode.ACH_WITHDRAWAL);
					objPaymentDetails.setExtAccount_RefId(toKeyAccount);

					FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
					objFromMSAcc_Details = objMSCommonUtils.setFromMSAccDetailsTO(objMMAccount);
					extTransferToObj[1] = objFromMSAcc_Details;
				}
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(toKeyAccount)))
				{
					//Setting To account details...
					objPaymentDetails.setTo_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setTo_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setToAcc_InContext(true);
					objPaymentDetails.setTransfer_Type(TxnTypeCode.ACH_DEPOSIT);
					objPaymentDetails.setExtAccount_RefId(fromKeyAccount);

					ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
					objToMSAcc_Details = objMSCommonUtils.setToMSAccDetailsTO(objMMAccount);
					extTransferToObj[2]= objToMSAcc_Details;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);
			String lifeTimeUserId=objMSUserDetails.getUuid();
			String transferType = objPaymentDetails.getTransfer_Type();
			String userRole=null;
			boolean isFA= objMSUserDetails.isFA();
			if(isFA)
				userRole = InitiatorRoleDesc.Client; //Business rule User/Group mapping for the FA login
			else
				userRole = InitiatorRoleDesc.Client;
			extTransferToObj[3] = objMSUserDetails;

			//Setting the Date Parameters for the First business Day or Last business Day selection ..
			if(objExternalTransfer.getFrequencyValue().equalsIgnoreCase("2") && (objExternalTransfer.getFrequencycombo().equalsIgnoreCase("FBD") || objExternalTransfer.getFrequencycombo().equalsIgnoreCase("LBD")))
			{
				// Set the Initiation Date in the Hidden field to store the same in the payment Date .. 
				objExternalTransfer.setFirstLastInitiationDate(objExternalTransfer.getInitiationDate());

				//Initiation Date in case of FBD or LBD..
				transferReqDate = MSCommonUtils.calculateStartBusinessDate(txnFrequency,transferReqDate,transferType,Boolean.FALSE);
				objExternalTransfer.setInitiationDate(ConvertionUtil.convertToString(transferReqDate));
			}

			//Estimated Arrival Date in case of FBD or LBD...
			Date estArrvDate = MSCommonUtils.calculateEstArrivalDate(transferReqDate,transferType,Boolean.FALSE,context);
			objExternalTransfer.setEstArrivalDate(ConvertionUtil.convertToString(estArrvDate));

			objPaymentDetails.setTransfer_Amount(objExternalTransfer.getPaymentamount());
			objPaymentDetails.setChildTxnAmount(objExternalTransfer.getPaymentamount());
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setFxRate(null);
			objPaymentDetails.setInitiation_Date(objExternalTransfer.getInitiationDate());
			objPaymentDetails.setRequestedDate(objExternalTransfer.getInitiationDate());
			objPaymentDetails.setEstimatedArrivalDate(objExternalTransfer.getEstArrivalDate());
			objPaymentDetails.setFrequency_Type(objExternalTransfer.getFrequencyValue());
			objPaymentDetails.setFrequency_DurationDesc(objExternalTransfer.getFrequencycombo());
			objPaymentDetails.setFrequency_DurationValue(objExternalTransfer.getDurationValue());
			objPaymentDetails.setDuration_EndDate(objExternalTransfer.getDurationenddate());
			objPaymentDetails.setDuration_NoOfTransfers(objExternalTransfer.getDurationNoOfTransfers());
			objPaymentDetails.setDuration_AmountLimit(objExternalTransfer.getDurationdollarlimit());
			objPaymentDetails.setBusiness_Date(objExternalTransfer.getBusinessDate());
			objPaymentDetails.setScreen_Type(TxnTypeCode.ACH_TYPE);
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

		} catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preExternalTransferExternalTransfer_INITsubmitbut"); 
	}

	public void postExternalTransferExternalTransfer_INITsubmitbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting postExternalTransferExternalTransfer_INITsubmitbut");
			ExternalPreConfirmForm objExternalPreConfirm=(ExternalPreConfirmForm) objTargetForm;
			ExternalTransferForm objExternalTransfer=(ExternalTransferForm) objSourceForm;
			String brMessages="";
			objUserPrincipal.setBrErrorMessages(null); 

			//Setting the Date Parameters for the First business Day or Last business Day selection in Pre Confirm Screen...
			if(objExternalPreConfirm.getFrequencyValue().equalsIgnoreCase("2") && (objExternalPreConfirm.getFrequencycombo().equalsIgnoreCase("FBD") || objExternalPreConfirm.getFrequencycombo().equalsIgnoreCase("LBD")))	
			{
				// Set the Initiation Date in the Hidden field to store the same in the Payment Date .. 
				objExternalPreConfirm.setFirstLastInitiationDate(objExternalTransfer.getFirstLastInitiationDate());
				objExternalPreConfirm.setInitiationDate(objExternalTransfer.getInitiationDate());
				objExternalPreConfirm.setEstArrivalDate(objExternalTransfer.getEstArrivalDate());
			}

			//Output extraction...
			ArrayList createSubmitDetails = (ArrayList)objReturn;
			ServiceContext contextData = (ServiceContext)createSubmitDetails.get(0);

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
					objExternalPreConfirm.setBrErrors(true);
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
					objExternalPreConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.EXTERNAL_TRANSFERS);
				}
				else 
				{
					if(contextData.getMaxSeverity()==MessageType.WARNING || contextData.getMaxSeverity()==MessageType.INFORMATION || contextData.getMaxSeverity()==MessageType.RISK)
					{
						HashMap<String, JSONArray> busniessRuleMessages = new HashMap<String, JSONArray>();

						//Risk Message Extraction....
						JSONArray jsonRisk = MSCommonUtils.extractContextRiskMsg(contextData,true);
						if(!jsonRisk.isEmpty()){
							objExternalPreConfirm.setBrRisk(true);
						}
						busniessRuleMessages.put("BusinessRisk", jsonRisk);

						//Warnings Message Extraction....
						JSONArray jsonWarning = MSCommonUtils.extractContextWarningMsg(contextData,true);
						if(!jsonWarning.isEmpty()){
							objExternalPreConfirm.setBrWarnings(true);
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
					objExternalPreConfirm.setBusinessRuleMessages(brMessages);

					//Setting the Initiation Date and Estimated Arrival Date in case of the Cut Off Time failure ...
					if(createSubmitDetails.get(1)!=null)
					{
						//Payment Details..
						HashMap txnOutDetails = (HashMap)createSubmitDetails.get(1);
						PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
						if(txnOutDetails.containsKey("PaymentDetails")){
							objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
						}

						//From account Details..
						FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
						if(txnOutDetails.containsKey("MSFromAccDetails")){
							objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnOutDetails.get("MSFromAccDetails");
						}

						//To account Details..
						ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
						if(txnOutDetails.containsKey("MSToAccDetails")){
							objToMSAcc_Details = (ToMSAcc_DetailsTO)txnOutDetails.get("MSToAccDetails");
						}

						//External account Details..
						DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
						if(txnOutDetails.containsKey("ExternalAccDetails")){
							objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnOutDetails.get("ExternalAccDetails");
						}

						//If the ArrayList is not Empty then we need to update the Initiation Date and Estimated Arrival Date since Cut Off Time Failed...
						objExternalPreConfirm.setInitiationDate(objPaymentDetails.getRequestedDate());
						objExternalPreConfirm.setEstArrivalDate(objPaymentDetails.getEstimatedArrivalDate());
						objExternalPreConfirm.setPayeeId(objExternalAccDetails.getCpypayeeid());
						objExternalPreConfirm.setPaymentamount(MSCommonUtils.formatTxnAmount(objPaymentDetails.getTransfer_Amount()));

						//Setting the frequency and duration attributes in case of recurring transfers...
						objExternalPreConfirm.setFrequencyradio(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));
						objExternalPreConfirm.setDurationradio(MSCommonUtils.getRepeatDesc_View(objPaymentDetails));

						//From account and To account mappings..
						String frmAccount = FormatAccount.getDebitAccountDisp(txnOutDetails);
						String toAccount = FormatAccount.getCreditAccountDisp(txnOutDetails);

						if(objPaymentDetails.getTransfer_Type().equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
							objExternalPreConfirm.setFromAccount(frmAccount);
							objExternalPreConfirm.setToAccount(toAccount);
							objExternalPreConfirm.setToKeyAccNumber(objToMSAcc_Details.getKeyAccount());
						}
						else if(objPaymentDetails.getTransfer_Type().equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
							objExternalPreConfirm.setFromAccount(frmAccount);
							objExternalPreConfirm.setFromKeyAccNumber(objFromMSAcc_Details.getKeyAccount());
							objExternalPreConfirm.setToAccount(toAccount);
						}
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postExternalTransferExternalTransfer_INITsubmitbut"); 
	}

	/**
	 * Method for ExternalTransfer Screen, ExternalTransfer_EDIT State and submitbut Event. 
	 * @throws Exception 
	 */
	public void preExternalTransferExternalTransfer_EditINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preExternalTransferExternalTransfer_INITsubmitbut"); 

			//Business Date mappings ...
			Object[] externalTrInitTO = (Object[])objParam[1];
			DsConfigDetailsTO objDsConfigDetails = (DsConfigDetailsTO)externalTrInitTO[0];
			String qzBranchCode = PropertyFileReader.getProperty("OU_ID"); 
			objDsConfigDetails.setBranch_code(qzBranchCode);

			//Initialization code...
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();

			//Context sharing for the Input Mappings 
			MMContext objMMContextData = objUserPrincipal.getContextData();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objMMContextData);

			//Updating the input TO...
			DsGetEditTransferInTO dsGetEditTransferInTO = (DsGetEditTransferInTO)externalTrInitTO[1];
			dsGetEditTransferInTO.setLifeUserId(objMSUserDetails.getUuid());
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preExternalTransferExternalTransfer_INITsubmitbut"); 
	}

	public void postExternalTransferExternalTransfer_EditINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try
		{
			EBWLogger.trace(this, "Starting postExternalTransferExternalTransfer_INITsubmitbut");
			ExternalTransferForm objExternalTransfer=(ExternalTransferForm) objSourceForm;

			//Setting the Frequency Value,....
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

			//External account mappings..
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO = new DsExtPayeeDetailsOutTO();
			objDsExtPayeeDetailsOutTO.setNick_name(objDsGetEditTransferOutTO.getPaypayeename1());
			objDsExtPayeeDetailsOutTO.setCpyaccnum(objDsGetEditTransferOutTO.getPayeeaccnum());

			txnDetails.put("PaymentDetails",objPaymentDetails);
			txnDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			txnDetails.put("MSToAccDetails",objToMSAcc_Details);
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);

			//Setting the recurring details..
			recuringFlag = objDsGetEditTransferOutTO.getTransferFrequency();
			if(recuringFlag!=null && recuringFlag.equalsIgnoreCase("N")){
				objExternalTransfer.setFrequencyValue("1");
			}
			else{
				objExternalTransfer.setFrequencyValue("2");
			}

			// Setting the Frequency EndDate Radio ....
			String freqEndDate=ConvertionUtil.convertToString(objDsGetEditTransferOutTO.getPayfreqenddate());
			if(freqEndDate!=null && freqEndDate.indexOf("1970")==-1){
				objExternalTransfer.setDurationenddate(ConvertionUtil.convertToString(objDsGetEditTransferOutTO.getPayfreqenddate()));
			}

			//Setting the local currency by default..
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			objExternalTransfer.setCurrencyCode(currencyCode);

			//Checking the status consistency ...
			StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
			ArrayList event_valid_statuses = ((ArrayList)((Object []) objReturn)[2]); 
			String txn_Paystatus = objDsGetEditTransferOutTO.getPayccsstatuscode();
			ServiceContext contextData = (ServiceContext)statusConsistency.verifyStatusConsistency(event_valid_statuses,txn_Paystatus);

			//From & To account Details..
			objExternalTransfer.setFromAccEdit(FormatAccount.getDebitAccountDisp(txnDetails));
			objExternalTransfer.setToAccEdit(FormatAccount.getCreditAccountDisp(txnDetails));

			//Response....
			MSCommonUtils.logEventResponse(contextData);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postExternalTransferExternalTransfer_INITsubmitbut"); 
	}

	/**
	 * Method for ExternalTransfer Screen, ExternalTransfer_Edit State and submitbut Event. 
	 * @throws Exception 
	 */
	public void preExternalTransferExternalTransfer_Editsubmitbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preExternalTransferExternalTransfer_Editsubmitbut"); 
			ExternalTransferForm objExternalTransfer=(ExternalTransferForm) objSourceForm;
			ServiceContext context = new ServiceContext();
			Object extTransferToObj[] = (Object[])objParam[1];

			//Server side field validations...
			ValidateACHTransfer objVaidateExtTransfer= new ValidateACHTransfer();
			String validationErr = objVaidateExtTransfer.validateACHTransferFrm(objExternalTransfer);
			if(validationErr!=null && !validationErr.trim().equalsIgnoreCase("")){
				throw new Exception(validationErr); // Form field validation failed ...
			}

			//Payment attributes mappings..
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();
			String fromKeyAccount=objExternalTransfer.getFromKeyAccNumber();
			String toKeyAccount=objExternalTransfer.getToKeyAccNumber();
			String txnFrequency = objExternalTransfer.getFrequencycombo();
			String transferType = objExternalTransfer.getTransactionType();
			Date transferReqDate = ConvertionUtil.convertToDate(objExternalTransfer.getInitiationDate());
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)extTransferToObj[0];

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
					extTransferToObj[1] = objFromMSAcc_Details;
				}
				if(objMMAccount.getKeyAccount().equalsIgnoreCase(toKeyAccount))
				{
					//Setting To account details...
					objPaymentDetails.setTo_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setTo_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setToAcc_InContext(true);

					ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
					objToMSAcc_Details = objMSCommonUtils.setToMSAccDetailsTO(objMMAccount);
					extTransferToObj[2]= objToMSAcc_Details;
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
				userRole = InitiatorRoleDesc.Client;
			extTransferToObj[3] = objMSUserDetails;

			//Setting the Date Parameters for the First business Day or Last business Day selection ..
			if(objExternalTransfer.getFrequencyValue().equalsIgnoreCase("2") && (objExternalTransfer.getFrequencycombo().equalsIgnoreCase("FBD") || objExternalTransfer.getFrequencycombo().equalsIgnoreCase("LBD")))	
			{
				// Set the Initiation Date in the Hidden field to store the same in the Payment Date .. 
				objExternalTransfer.setFirstLastInitiationDate(objExternalTransfer.getInitiationDate());

				//Initiation Date in case of FBD or LBD...
				transferReqDate= MSCommonUtils.calculateStartBusinessDate(txnFrequency,transferReqDate,transferType,Boolean.FALSE);
				objExternalTransfer.setInitiationDate(ConvertionUtil.convertToString(transferReqDate));
			}

			//Estimated Arrival Date in case of FBD or LBD...
			Date estArrvDate = MSCommonUtils.calculateEstArrivalDate(transferReqDate,transferType,Boolean.FALSE,context);
			objExternalTransfer.setEstArrivalDate(ConvertionUtil.convertToString(estArrvDate));

			objPaymentDetails.setTransfer_Type(transferType);
			objPaymentDetails.setTransfer_Amount(objExternalTransfer.getPaymentamount());
			objPaymentDetails.setChildTxnAmount(objExternalTransfer.getPaymentamount());
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setFxRate(null);
			objPaymentDetails.setInitiation_Date(objExternalTransfer.getInitiationDate());
			objPaymentDetails.setRequestedDate(objExternalTransfer.getInitiationDate());
			objPaymentDetails.setEstimatedArrivalDate(objExternalTransfer.getEstArrivalDate());
			objPaymentDetails.setFrequency_Type(objExternalTransfer.getFrequencyValue());
			objPaymentDetails.setFrequency_DurationDesc(objExternalTransfer.getFrequencycombo());
			objPaymentDetails.setFrequency_DurationValue(objExternalTransfer.getDurationValue());
			objPaymentDetails.setDuration_EndDate(objExternalTransfer.getDurationenddate());
			objPaymentDetails.setDuration_NoOfTransfers(objExternalTransfer.getDurationNoOfTransfers());
			objPaymentDetails.setDuration_AmountLimit(objExternalTransfer.getDurationdollarlimit());
			objPaymentDetails.setBusiness_Date(objExternalTransfer.getBusinessDate());
			objPaymentDetails.setScreen_Type(TxnTypeCode.ACH_TYPE);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setTrial_depo("N");
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Modify_PreConfirm);
			objPaymentDetails.setTxnModified(true);
			objPaymentDetails.setPrevAction("Modify");
			objPaymentDetails.setTransactionId(objExternalTransfer.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objExternalTransfer.getVersionnum());
			objPaymentDetails.setTxnBatchId(objExternalTransfer.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objExternalTransfer.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnId(objExternalTransfer.getParentTxnNumber());
			objPaymentDetails.setRecParentTxnVersion(objExternalTransfer.getParTxnVersionnum());
			objPaymentDetails.setExtAccount_RefId(objExternalTransfer.getPayeeId());
			objPaymentDetails.setTxnCurrentStatusCode(objExternalTransfer.getPrevPaystatus());
			objPaymentDetails.setTxnPrevStatusCode(objExternalTransfer.getPrevPaystatus());
			objPaymentDetails.setCurrent_owner_id(lifeTimeUserId);
			objPaymentDetails.setCurrent_owner_role(userRole);

			EBWLogger.trace(this, "Finished preExternalTransferExternalTransfer_Editsubmitbut"); 
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	public void postExternalTransferExternalTransfer_Editsubmitbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postExternalTransferExternalTransfer_Editsubmitbut");
		try 
		{
			ExternalPreConfirmForm objExternalPreConfirm=(ExternalPreConfirmForm) objTargetForm;
			ExternalTransferForm objExternalTransfer=(ExternalTransferForm) objSourceForm;
			objUserPrincipal.setBrErrorMessages(null); 
			String brMessages="";

			//Setting the Date Parameters for the First business Day or Last business Day selection in Pre Confirm Screen...
			if(objExternalPreConfirm.getFrequencyValue().equalsIgnoreCase("2") && (objExternalPreConfirm.getFrequencycombo().equalsIgnoreCase("FBD") || objExternalPreConfirm.getFrequencycombo().equalsIgnoreCase("LBD")))	{
				// Set the Initiation Date in the Hidden field to store the same in the Payment Date .. 
				objExternalPreConfirm.setFirstLastInitiationDate(objExternalTransfer.getFirstLastInitiationDate());
				objExternalPreConfirm.setInitiationDate(objExternalTransfer.getInitiationDate());
				objExternalPreConfirm.setEstArrivalDate(objExternalTransfer.getEstArrivalDate());
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
					objExternalPreConfirm.setBrErrors(true);
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
					objExternalPreConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.EXTERNAL_TRANSFERS);
				}
				else 
				{
					if(contextData.getMaxSeverity()==MessageType.WARNING || contextData.getMaxSeverity()==MessageType.INFORMATION)
					{
						HashMap<String, JSONArray> busniessRuleMessages = new HashMap<String, JSONArray>();

						//Risk Message Extraction....
						JSONArray jsonRisk = MSCommonUtils.extractContextRiskMsg(contextData,true);
						if(!jsonRisk.isEmpty()){
							objExternalPreConfirm.setBrRisk(true);
						}
						busniessRuleMessages.put("BusinessRisk", jsonRisk);

						//Warnings Message Extraction....
						JSONArray jsonWarning = MSCommonUtils.extractContextWarningMsg(contextData,true);
						if(!jsonWarning.isEmpty()){
							objExternalPreConfirm.setBrWarnings(true);
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
					objExternalPreConfirm.setBusinessRuleMessages(brMessages);

					//Setting the Initiation Date and Estimated Arrival Date in case of the Cut Off Time failure ...
					if(createSubmitDetails.get(1)!=null)
					{
						HashMap txnOutDetails = (HashMap)createSubmitDetails.get(1);
						PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
						if(txnOutDetails.containsKey("PaymentDetails")){
							objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
						}

						//If the ArrayList is not Empty then we need to update the Initiation Date and Estimated Arrival Date since Cut Off Time Failed...
						objExternalPreConfirm.setInitiationDate(objPaymentDetails.getRequestedDate());
						objExternalPreConfirm.setEstArrivalDate(objPaymentDetails.getEstimatedArrivalDate());
						objExternalPreConfirm.setPaymentamount(MSCommonUtils.formatTxnAmount(objPaymentDetails.getTransfer_Amount()));

						//Setting the frequency and duration attributes in case of recurring transfers...
						objExternalPreConfirm.setFrequencyradio(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));
						objExternalPreConfirm.setDurationradio(MSCommonUtils.getRepeatDesc_View(objPaymentDetails));

						//From & To account mappings..
						objExternalPreConfirm.setFromAccEdit(FormatAccount.getDebitAccountDisp(txnOutDetails));
						objExternalPreConfirm.setToAccEdit(FormatAccount.getCreditAccountDisp(txnOutDetails));
					}
				}
			}
		} catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postExternalTransferExternalTransfer_Editsubmitbut"); 
	}
}