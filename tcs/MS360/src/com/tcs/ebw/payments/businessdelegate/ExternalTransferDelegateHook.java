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
import com.tcs.Payments.ms360Utils.ActionDesc;
import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.FormatAccount;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.PageStateDesc;
import com.tcs.Payments.ms360Utils.ScreenDesc;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.Payments.serverValidations.StatusConsistencyChk;
import com.tcs.Payments.serverValidations.ValidateACHTransfer;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.ms360.integration.MMAccount;
import com.tcs.bancs.ms360.integration.MMContext;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.businessdelegate.BusinessDelegateHook;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountPlating;
import com.tcs.ebw.serverside.services.channelPaymentServices.BusinessRulesService;
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
	 * Pre hook method for ExternalTransfer onload event in Init mode...
	 * @throws Exception 
	 */
	public void preExternalTransferExternalTransfer_IINITINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preExternalTransferExternalTransfer_MultiTransferINIT"); 
			MMContext objMMContextData = objUserPrincipal.getContextData();
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			boolean isAnchorAccountReq = true;

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.ACT.toString());
			objPaymentDetails.setAction(ActionDesc.PAC.toString());
			objPaymentDetails.setState(PageStateDesc.I.toString());

			//Server side field validations for FAP...
			ValidateACHTransfer objVaidateAchTransfer= new ValidateACHTransfer();
			ServiceContext contextData = objVaidateAchTransfer.validatePageAccess(isAnchorAccountReq,objUserPrincipal,objPaymentDetails);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}

			//Business Date Input Mappings..
			Object[] externalTrInitTO = (Object[])objParam[1];
			DsConfigDetailsTO objDsConfigDetails = (DsConfigDetailsTO)externalTrInitTO[0];
			String qzBranchCode = PropertyFileReader.getProperty("OU_ID"); 
			objDsConfigDetails.setBranch_code(qzBranchCode);

			//External Accounts Input Mappings...
			DsOnloadAccDetailsTO dsOnloadAccDetailsTO = (DsOnloadAccDetailsTO)externalTrInitTO[1];
			dsOnloadAccDetailsTO.setKey_client_id(MSCommonUtils.extractKeyClientId(objMMContextData));
		} 
		catch (Exception exception) {
			throw exception;
		}

		EBWLogger.trace(this, "Finished preExternalTransferExternalTransfer_MultiTransferINIT"); 
	}

	/**
	 * Post hook method for ExternalTransfer onload event in Init mode...
	 * @throws Exception 
	 */
	public void postExternalTransferExternalTransfer_IINITINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postExternalTransferExternalTransfer_IINITINIT");
		try 
		{
			ExternalTransferForm objExternalTransfer=(ExternalTransferForm) objSourceForm;
			String forwardTo = "ExternalTransfer";
			objUserPrincipal.setBrErrorMessages(null); 

			//Output Extraction....
			ArrayList dsOnloadAccDetails = (ArrayList)((Object []) objReturn)[1];
			ArrayList checkExtAccStatus = (ArrayList)dsOnloadAccDetails;
			ArrayList extAccStatusList  = new ArrayList();
			ArrayList businessErrors = new ArrayList();
			HashMap busniessErrorMessages = new HashMap();
			JSONArray jsonError = new JSONArray();
			ResourceBundle rsMessages = ResourceBundle.getBundle("ErrMessage");
			checkExtAccStatus.remove(0);
			if(!checkExtAccStatus.isEmpty())
			{
				//Creating an arrayList in case the output list is not empty...
				for(int i=0;i<checkExtAccStatus.size();i++){
					ArrayList externalAccStatus=(ArrayList)checkExtAccStatus.get(i);
					extAccStatusList.add(externalAccStatus.get(0));
				}

				//Based on the obtained account list , forwardTo is set accordingly...
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
			else {
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
	 * Pre hook method for ExternalTransfer submit event in Init mode...
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
			for(int i=0;i<objMMContextData.getAccounts().size();i++)
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
					objFromMSAcc_Details.setKeyClientId(MSCommonUtils.extractKeyClientId(objExternalTransfer.getDebitAcntPlatingInfo()));
					objFromMSAcc_Details.setAccountPlating(objExternalTransfer.getDebitAcntPlatingInfo());
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
					objToMSAcc_Details.setKeyClientId(MSCommonUtils.extractKeyClientId(objExternalTransfer.getCreditAcntPlatingInfo()));
					objToMSAcc_Details.setAccountPlating(objExternalTransfer.getCreditAcntPlatingInfo());
					extTransferToObj[2]= objToMSAcc_Details;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			String userRole = objMSUserDetails.getInitiatorRole();
			String userId = objMSUserDetails.getRcafId();
			String transferType = objPaymentDetails.getTransfer_Type();
			extTransferToObj[3] = objMSUserDetails;

			//Setting the Date Parameters for the First business Day or Last business Day selection ..
			if(objExternalTransfer.getFrequencyValue().equalsIgnoreCase("2") && (objExternalTransfer.getFrequencycombo().equalsIgnoreCase("FBD") || objExternalTransfer.getFrequencycombo().equalsIgnoreCase("LBD")))
			{
				// Set the Initiation Date in the Hidden field to store the same in the payment Date .. 
				objExternalTransfer.setFirstLastInitiationDate(objExternalTransfer.getInitiationDate());

				//Initiation Date in case of FBD or LBD..
				transferReqDate = MSCommonUtils.calculateStartBusinessDate(txnFrequency,transferReqDate,transferType);
				objExternalTransfer.setInitiationDate(ConvertionUtil.convertToAppDateStr(transferReqDate));
			}

			//Estimated Arrival Date in case of FBD or LBD...
			Date estArrvDate = MSCommonUtils.calculateEstArrivalDate(transferReqDate,transferType,context);
			objExternalTransfer.setEstArrivalDate(ConvertionUtil.convertToAppDateStr(estArrvDate));

			//Payment details attribute mappings..
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
			objPaymentDetails.setRetirement_mnemonic(objExternalTransfer.getTransferTypeIRA());
			objPaymentDetails.setScreen_Type(TxnTypeCode.ACH_TYPE);
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setTrial_depo("N");
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(Bus_Rule_Input_Desc.Create_PreConfirm);
			objPaymentDetails.setTxnInitiated(true);
			objPaymentDetails.setPrevAction("Create");
			objPaymentDetails.setInitiator_id(userId); //RACF Id of the FA..
			objPaymentDetails.setInitiator_role(userRole);
			objPaymentDetails.setCurrent_owner_id(userId); //RACF Id of the FA..
			objPaymentDetails.setCurrent_owner_role(userRole);
			objPaymentDetails.setCreated_by_comments(objExternalTransfer.getUserComments());
			objPaymentDetails.setDebitAcntDataInSession(objExternalTransfer.isDebitAcntDataInSession());
			objPaymentDetails.setCreditAcntDataInSession(objExternalTransfer.isCreditAcntDataInSession());

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.ACT.toString());
			objPaymentDetails.setAction(ActionDesc.CFM.toString());
			objPaymentDetails.setState(PageStateDesc.I.toString());

		} catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preExternalTransferExternalTransfer_INITsubmitbut"); 
	}

	/**
	 * Post hook method for ExternalTransfer submit event in Init mode...
	 * @throws Exception 
	 */
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
			if(objExternalTransfer.getFrequencyValue().equalsIgnoreCase("2") && (objExternalTransfer.getFrequencycombo().equalsIgnoreCase("FBD") || objExternalTransfer.getFrequencycombo().equalsIgnoreCase("LBD")))	{
				objExternalPreConfirm.setFirstLastInitiationDate(objExternalTransfer.getFirstLastInitiationDate());
				objExternalPreConfirm.setInitiationDate(objExternalTransfer.getInitiationDate());
				objExternalPreConfirm.setEstArrivalDate(objExternalTransfer.getEstArrivalDate());
			}

			//Output extraction...
			ArrayList authorisedEntities = new ArrayList();
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

						//Mapping from the Business Rule response
						BusinessRulesService objBussRulesService = new BusinessRulesService();
						if(txnOutDetails.containsKey("BROutputDetails")){
							objBussRulesService= (BusinessRulesService)txnOutDetails.get("BROutputDetails");
						}

						FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
						if(txnOutDetails.containsKey("MSFromAccDetails")){
							objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnOutDetails.get("MSFromAccDetails");
						}

						ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
						if(txnOutDetails.containsKey("MSToAccDetails")){
							objToMSAcc_Details = (ToMSAcc_DetailsTO)txnOutDetails.get("MSToAccDetails");
						}

						DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
						if(txnOutDetails.containsKey("ExternalAccDetails")){
							objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnOutDetails.get("ExternalAccDetails");
						}

						String transferType = objPaymentDetails.getTransfer_Type();

						//Output extraction from Debit account plating response or value stored in session...
						AccountPlating acntPlating = new AccountPlating();
						if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) && !objExternalTransfer.isDebitAcntDataInSession()){
							if(txnOutDetails.containsKey("AcntPlatingOutputDetails")){
								acntPlating = (AccountPlating)txnOutDetails.get("AcntPlatingOutputDetails");
							}
							if(acntPlating!=null){
								authorisedEntities = acntPlating.getAuthorisedEntities();
							}
						}
						else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
							acntPlating = objExternalTransfer.getDebitAcntPlatingInfo();
							if(acntPlating!=null){
								authorisedEntities = acntPlating.getAuthorisedEntities();
							}
						}

						//Output extraction from Credit account plating response or value stored in session...
						if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) && !objExternalTransfer.isCreditAcntDataInSession()){
							if(txnOutDetails.containsKey("AcntPlatingOutputDetails")){
								acntPlating = (AccountPlating)txnOutDetails.get("AcntPlatingOutputDetails");
							}
							if(acntPlating!=null){
								authorisedEntities = acntPlating.getAuthorisedEntities();
							}
						}
						else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
							acntPlating = objExternalTransfer.getCreditAcntPlatingInfo();
							if(acntPlating!=null){
								authorisedEntities = acntPlating.getAuthorisedEntities();
							}
						}

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

						//If the ArrayList is not Empty then we need to update the Initiation Date and Estimated Arrival Date since Cut Off Time Failed...
						objExternalPreConfirm.setInitiationDate(objPaymentDetails.getRequestedDate());
						objExternalPreConfirm.setEstArrivalDate(objPaymentDetails.getEstimatedArrivalDate());
						objExternalPreConfirm.setPayeeId(objExternalAccDetails.getCpypayeeid());
						objExternalPreConfirm.setPaymentamount(MSCommonUtils.formatTxnAmount(objPaymentDetails.getTransfer_Amount()));
						objExternalPreConfirm.setTxnTypeIraDisp(MSCommonUtils.getRetirementType(objPaymentDetails));

						//Setting the frequency and duration attributes in case of recurring transfers...
						objExternalPreConfirm.setFrequencyradio(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));
						objExternalPreConfirm.setDurationradio(MSCommonUtils.getRepeatDesc_View(objPaymentDetails));

						//Authorization mode and Spoke-To population..
						if(objBussRulesService.getAuth_mode()!=null && !objBussRulesService.getAuth_mode().trim().equalsIgnoreCase("")){
							objExternalPreConfirm.setAuth_mode(objBussRulesService.getAuth_mode());
							objExternalPreConfirm.setExternalKeyClient(objExternalAccDetails.getKey_client());
							objExternalPreConfirm.setSpokeToCollection(MSCommonUtils.getSpokeToDetails(authorisedEntities,objExternalAccDetails));
							objExternalPreConfirm.setAuthDocumentsReq(MSCommonUtils.formatDocumentsReq(objBussRulesService.getSigned_documents_req()));
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
	 * Pre hook method for ExternalTransfer onload event in edit mode...
	 * @throws Exception 
	 */
	public void preExternalTransferExternalTransfer_EditINIT(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preExternalTransferExternalTransfer_INITsubmitbut"); 
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			boolean isAnchorAccountReq = false;

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.ACT.toString());
			objPaymentDetails.setAction(ActionDesc.PAC.toString());
			objPaymentDetails.setState(PageStateDesc.E.toString());

			//Server side field validations for FAP...
			ValidateACHTransfer objVaidateAchTransfer= new ValidateACHTransfer();
			ServiceContext contextData = objVaidateAchTransfer.validatePageAccess(isAnchorAccountReq,objUserPrincipal,objPaymentDetails);
			if(contextData.getMaxSeverity()==MessageType.CRITICAL){
				String errorMessage = MSCommonUtils.extractContextErrMessage(contextData);
				throw new Exception(errorMessage);
			}

			//Business Date mappings ...
			Object[] externalTrInitTO = (Object[])objParam[1];
			DsConfigDetailsTO objDsConfigDetails = (DsConfigDetailsTO)externalTrInitTO[0];
			String qzBranchCode = PropertyFileReader.getProperty("OU_ID"); 
			objDsConfigDetails.setBranch_code(qzBranchCode);
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preExternalTransferExternalTransfer_INITsubmitbut"); 
	}

	/**
	 * Post hook method for ExternalTransfer onload event in edit mode...
	 * @throws Exception 
	 */
	public void postExternalTransferExternalTransfer_EditINIT(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting postExternalTransferExternalTransfer_INITsubmitbut");
			ExternalTransferForm objExternalTransfer=(ExternalTransferForm) objSourceForm;
			DsGetEditTransferOutTO objDsGetEditTransferOutTO = (DsGetEditTransferOutTO)((Object []) objReturn)[1];
			String recuringFlag = null;
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
			}

			//To account mappings..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(objDsGetEditTransferOutTO.getTobr_acct_no_fa()!=null){
				objToMSAcc_Details.setAccountNumber(objDsGetEditTransferOutTO.getTobr_acct_no_fa().substring(3,9));
				objToMSAcc_Details.setOfficeNumber(objDsGetEditTransferOutTO.getTobr_acct_no_fa().substring(0,3));
				objToMSAcc_Details.setFaNumber(objDsGetEditTransferOutTO.getTobr_acct_no_fa().substring(9));
			}

			//External account mappings..
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO = new DsExtPayeeDetailsOutTO();
			objDsExtPayeeDetailsOutTO.setNick_name(objDsGetEditTransferOutTO.getPaypayeename1());
			objDsExtPayeeDetailsOutTO.setAccount_owner(objDsGetEditTransferOutTO.getExtAccOwner());
			objDsExtPayeeDetailsOutTO.setCpyaccnum(objDsGetEditTransferOutTO.getPayeeaccnum());

			txnDetails.put("PaymentDetails",objPaymentDetails);
			txnDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			txnDetails.put("MSToAccDetails",objToMSAcc_Details);
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);

			//Setting the Frequency Value,....
			recuringFlag=objDsGetEditTransferOutTO.getTransferFrequency();
			if(recuringFlag!=null && recuringFlag.equalsIgnoreCase("N")){
				objExternalTransfer.setFrequencyValue("1");
			}
			else{
				objExternalTransfer.setFrequencyValue("2");
			}

			// Setting the Frequency EndDate Radio ....
			String freqEndDate=ConvertionUtil.convertToAppDateStr(objDsGetEditTransferOutTO.getPayfreqenddate());
			if(freqEndDate!=null && freqEndDate.indexOf("1970")==-1){
				objExternalTransfer.setDurationenddate(ConvertionUtil.convertToAppDateStr(objDsGetEditTransferOutTO.getPayfreqenddate()));
			}

			//Setting the local currency by default..
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			objExternalTransfer.setCurrencyCode(currencyCode);

			//From and To account mappings..
			objExternalTransfer.setFromAccEdit(FormatAccount.getDebitAccountDisp_Edit(txnDetails));
			objExternalTransfer.setToAccEdit(FormatAccount.getCreditAccountDisp_Edit(txnDetails));

			//Checking the status consistency ...
			StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
			ArrayList event_valid_statuses = ((ArrayList)((Object []) objReturn)[2]); 
			String txn_Paystatus = objDsGetEditTransferOutTO.getPayccsstatuscode();
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
		EBWLogger.trace(this, "Finished postExternalTransferExternalTransfer_INITsubmitbut"); 
	}

	/**
	 * Pre hook method for ExternalTransfer onload event in edit mode...
	 * @throws Exception 
	 */
	public void preExternalTransferExternalTransfer_Editsubmitbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try {
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
			String txnFrequency = objExternalTransfer.getFrequencycombo();
			Date transferReqDate = ConvertionUtil.convertToDate(objExternalTransfer.getInitiationDate());
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			String transferType = objExternalTransfer.getTransactionType();
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)extTransferToObj[0];

			//From Or To account key_client_id mappings if applicable...
			if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
				objFromMSAcc_Details.setKeyClientId(MSCommonUtils.extractKeyClientId(objExternalTransfer.getDebitAcntPlatingInfo()));
				objFromMSAcc_Details.setAccountPlating(objExternalTransfer.getDebitAcntPlatingInfo());
				extTransferToObj[1] = objFromMSAcc_Details;
			}
			else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)) {
				ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
				objToMSAcc_Details.setKeyClientId(MSCommonUtils.extractKeyClientId(objExternalTransfer.getCreditAcntPlatingInfo()));
				objToMSAcc_Details.setAccountPlating(objExternalTransfer.getCreditAcntPlatingInfo());
				extTransferToObj[2] = objToMSAcc_Details;
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			String userId= objMSUserDetails.getRcafId();
			String userRole= objMSUserDetails.getInitiatorRole();
			extTransferToObj[3] = objMSUserDetails;

			//Setting the Date Parameters for the First business Day or Last business Day selection ..
			if(objExternalTransfer.getFrequencyValue().equalsIgnoreCase("2") && (objExternalTransfer.getFrequencycombo().equalsIgnoreCase("FBD") || objExternalTransfer.getFrequencycombo().equalsIgnoreCase("LBD")))	
			{
				// Set the Initiation Date in the Hidden field to store the same in the Payment Date .. 
				objExternalTransfer.setFirstLastInitiationDate(objExternalTransfer.getInitiationDate());

				//Initiation Date in case of FBD or LBD...
				transferReqDate = MSCommonUtils.calculateStartBusinessDate(txnFrequency,transferReqDate,transferType);
				objExternalTransfer.setInitiationDate(ConvertionUtil.convertToAppDateStr(transferReqDate));
			}

			//Estimated Arrival Date in case of FBD or LBD...
			Date estArrvDate = MSCommonUtils.calculateEstArrivalDate(transferReqDate,transferType,context);
			objExternalTransfer.setEstArrivalDate(ConvertionUtil.convertToAppDateStr(estArrvDate));

			//Payment details attribute mappings..
			objPaymentDetails.setFrmAcc_InContext(false);
			objPaymentDetails.setToAcc_InContext(false);
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
			objPaymentDetails.setTransfer_Type(objExternalTransfer.getTransactionType());
			objPaymentDetails.setTransactionId(objExternalTransfer.getTxnPayPayRefNumber());
			objPaymentDetails.setTransactionVersion(objExternalTransfer.getVersionnum());
			objPaymentDetails.setTxnBatchId(objExternalTransfer.getTxnBatchRefNumber());
			objPaymentDetails.setTxnBatchVersion(objExternalTransfer.getBatchVersionnum());
			objPaymentDetails.setRecParentTxnId(objExternalTransfer.getParentTxnNumber());
			objPaymentDetails.setRecParentTxnVersion(objExternalTransfer.getParTxnVersionnum());
			objPaymentDetails.setExtAccount_RefId(objExternalTransfer.getPayeeId());
			objPaymentDetails.setTxnCurrentStatusCode(objExternalTransfer.getPrevPaystatus());
			objPaymentDetails.setTxnPrevStatusCode(objExternalTransfer.getPrevPaystatus());
			objPaymentDetails.setCurrent_owner_id(userId);
			objPaymentDetails.setCurrent_owner_role(userRole);
			objPaymentDetails.setCreated_by_comments(objExternalTransfer.getUserComments());
			objPaymentDetails.setDebitAcntDataInSession(objExternalTransfer.isDebitAcntDataInSession());
			objPaymentDetails.setCreditAcntDataInSession(objExternalTransfer.isCreditAcntDataInSession());

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.ACT.toString());
			objPaymentDetails.setAction(ActionDesc.CFM.toString());
			objPaymentDetails.setState(PageStateDesc.E.toString());
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished preExternalTransferExternalTransfer_Editsubmitbut"); 
	}

	/**
	 * Post hook method for ExternalTransfer submit event in edit mode...
	 * @throws Exception 
	 */
	public void postExternalTransferExternalTransfer_Editsubmitbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting postExternalTransferExternalTransfer_Editsubmitbut");
		try 
		{
			ExternalPreConfirmForm objExternalPreConfirm=(ExternalPreConfirmForm) objTargetForm;
			ExternalTransferForm objExternalTransfer=(ExternalTransferForm) objSourceForm;
			String brMessages="";
			objUserPrincipal.setBrErrorMessages(null); 

			//Setting the Date Parameters for the First business Day or Last business Day selection in Pre Confirm Screen...
			if(objExternalTransfer.getFrequencyValue().equalsIgnoreCase("2") && (objExternalTransfer.getFrequencycombo().equalsIgnoreCase("FBD") || objExternalTransfer.getFrequencycombo().equalsIgnoreCase("LBD")))	{
				objExternalPreConfirm.setFirstLastInitiationDate(objExternalTransfer.getFirstLastInitiationDate());
				objExternalPreConfirm.setInitiationDate(objExternalTransfer.getInitiationDate());
				objExternalPreConfirm.setEstArrivalDate(objExternalTransfer.getEstArrivalDate());
			}

			//Output extraction...
			ArrayList authorisedEntities = new ArrayList();
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
					objExternalPreConfirm.setBrErrors(true);
					objUserPrincipal.setBrErrorMessages(jsonErrorMess.toString());
					objUserPrincipal.setPostNavigationPageId(MSSystemDefaults.CTC_SEARCHES);
				}
				else 
				{
					if(contextData.getMaxSeverity()==MessageType.WARNING || contextData.getMaxSeverity()==MessageType.INFORMATION)
					{
						HashMap<String, JSONArray> busniessRuleMessages = new HashMap<String, JSONArray>();

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

						//PaymentDetails extraction..
						PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
						if(txnOutDetails.containsKey("PaymentDetails")){
							objPaymentDetails = (PaymentDetailsTO)txnOutDetails.get("PaymentDetails");
						}

						//Mapping from the Business Rule response
						BusinessRulesService objBussRulesService = new BusinessRulesService();
						if(txnOutDetails.containsKey("BROutputDetails")){
							objBussRulesService= (BusinessRulesService)txnOutDetails.get("BROutputDetails");
						}

						//External account details...
						DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
						if(txnOutDetails.containsKey("ExternalAccDetails")){
							objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnOutDetails.get("ExternalAccDetails");
						}

						String transferType = objPaymentDetails.getTransfer_Type();

						//Output extraction from Debit account plating response or value stored in session...
						AccountPlating acntPlating = new AccountPlating();
						if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) && !objExternalTransfer.isDebitAcntDataInSession()){
							if(txnOutDetails.containsKey("AcntPlatingOutputDetails")){
								acntPlating = (AccountPlating)txnOutDetails.get("AcntPlatingOutputDetails");
							}
							if(acntPlating!=null){
								authorisedEntities = acntPlating.getAuthorisedEntities();
							}
						}
						else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
							acntPlating = objExternalTransfer.getDebitAcntPlatingInfo();
							if(acntPlating!=null){
								authorisedEntities = acntPlating.getAuthorisedEntities();
							}
						}

						//Output extraction from Credit account plating response or value stored in session...
						if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) && !objExternalTransfer.isCreditAcntDataInSession()){
							if(txnOutDetails.containsKey("AcntPlatingOutputDetails")){
								acntPlating = (AccountPlating)txnOutDetails.get("AcntPlatingOutputDetails");
							}
							if(acntPlating!=null){
								authorisedEntities = acntPlating.getAuthorisedEntities();
							}
						}
						else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
							acntPlating = objExternalTransfer.getCreditAcntPlatingInfo();
							if(acntPlating!=null){
								authorisedEntities = acntPlating.getAuthorisedEntities();
							}
						}

						//From account and To account mappings...
						objExternalPreConfirm.setFromAccEdit(FormatAccount.getDebitAccountDisp(txnOutDetails));
						objExternalPreConfirm.setToAccEdit(FormatAccount.getCreditAccountDisp(txnOutDetails));

						//If the ArrayList is not Empty then we need to update the Initiation Date and Estimated Arrival Date since Cut Off Time Failed...
						objExternalPreConfirm.setInitiationDate(objPaymentDetails.getRequestedDate());
						objExternalPreConfirm.setEstArrivalDate(objPaymentDetails.getEstimatedArrivalDate());
						objExternalPreConfirm.setPaymentamount(MSCommonUtils.formatTxnAmount(objPaymentDetails.getTransfer_Amount()));
						objExternalPreConfirm.setTxnTypeIraDisp(MSCommonUtils.getRetirementType(objPaymentDetails));

						//Setting the frequency and duration attributes in case of recurring transfers...
						objExternalPreConfirm.setFrequencyradio(MSCommonUtils.getFreqTypeDesc_View(objPaymentDetails));
						objExternalPreConfirm.setDurationradio(MSCommonUtils.getRepeatDesc_View(objPaymentDetails));

						//Authorization mode and Spoke-To population..
						if(objBussRulesService.getAuth_mode()!=null && !objBussRulesService.getAuth_mode().trim().equalsIgnoreCase("")){
							objExternalPreConfirm.setExternalKeyClient(objExternalAccDetails.getKey_client());
							objExternalPreConfirm.setAuth_mode(objBussRulesService.getAuth_mode());
							objExternalPreConfirm.setSpokeToCollection(MSCommonUtils.getSpokeToDetails(authorisedEntities,objExternalAccDetails));
							objExternalPreConfirm.setAuthDocumentsReq(MSCommonUtils.formatDocumentsReq(objBussRulesService.getSigned_documents_req()));
						}
					}
				}
			}
		} catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished postExternalTransferExternalTransfer_Editsubmitbut"); 
	}
}