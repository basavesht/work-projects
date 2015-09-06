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

import com.tcs.Payments.ms360Utils.ActionDesc;
import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.PageStateDesc;
import com.tcs.Payments.ms360Utils.ScreenDesc;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.Payments.serverValidations.ValidateINTTransfer;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.ms360.integration.MMAccount;
import com.tcs.bancs.ms360.integration.MMContext;
import com.tcs.ebw.businessdelegate.BusinessDelegateHook;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.payments.formbean.InternalConfirmForm;
import com.tcs.ebw.payments.formbean.InternalPreConfirmForm;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.OtherMSAccountTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.TrxnDetailsOutputTO;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *  224703			31-05-2011      P3				 Server-side check.
 *  224703        23-09-2011        P3-B             PLA  
 * **********************************************************
 */
public class InternalPreConfirmDelegateHook extends BusinessDelegateHook {

	/**
	 * Pre hook method for InternalTransfer confirm event in Init mode...
	 * @throws Exception 
	 */
	public void preInternalPreConfirmInternalPreConfirm_INITconfirmbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		EBWLogger.trace(this, "Starting preInternalPreConfirmInternalPreConfirm_INITconfirmbut"); 
		try 
		{
			InternalPreConfirmForm objInternalPreConfirm = (InternalPreConfirmForm)objSourceForm;
			Object intPreConfirmToObj[] = (Object[])objParam[1];

			//Server side field validations...
			ValidateINTTransfer objVaidateIntTransfer= new ValidateINTTransfer();
			String validationErr = objVaidateIntTransfer.validateINTTransferConfirmFrm(objInternalPreConfirm);
			if(validationErr!=null && !validationErr.trim().equalsIgnoreCase("")){
				throw new Exception(validationErr); // Form field validation failed ...
			}

			//Property file reader data mappings..
			MSCommonUtils objMSCommonUtils = new MSCommonUtils();

			//Internal From/To account Mappings..
			String fromKeyAccount = objInternalPreConfirm.getFromKeyAccNumber(); 
			String toKeyAccount = objInternalPreConfirm.getToKeyAccNumber();
			String loanAccount = objInternalPreConfirm.getLoanAcntNo();
			String transferType = objInternalPreConfirm.getTransactionType();

			//Other MS account mappings..
			OtherMSAccountTO otherMSAcc = new OtherMSAccountTO();
			otherMSAcc.setOtherMSAccName(objInternalPreConfirm.getOtherMSAccountName());//JSON Account Name as obtained from MS360 Framework..
			otherMSAcc.setOtherMSKeyAccount(objInternalPreConfirm.getOtherMSKeyAccount());//JSON Key account as obtained from MS360 Framework..
			otherMSAcc.setOtherMSAccNumber(objInternalPreConfirm.getOtherMSAccountNo());//JSON Account number as obtained from MS360 Framework..

			//System Constants mappings..
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)intPreConfirmToObj[0];

			//From/To account object mappings..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();

			//Account details extraction process..
			MMContext objMMContextData = objUserPrincipal.getContextData();
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

					objFromMSAcc_Details = objMSCommonUtils.setFromMSAccDetailsTO(objMMAccount);
					objFromMSAcc_Details.setAccountPlating(objInternalPreConfirm.getDebitAcntPlatingInfo());
					intPreConfirmToObj[1] = objFromMSAcc_Details;
				}
				if((objMMAccount.getKeyAccount()!=null && objMMAccount.getKeyAccount().equalsIgnoreCase(toKeyAccount)))
				{
					//Setting To account details...
					objPaymentDetails.setTo_Account(objMMAccount.getAccountNumber());
					objPaymentDetails.setTo_KeyAccount(objMMAccount.getKeyAccount());
					objPaymentDetails.setToAcc_InContext(true);

					objToMSAcc_Details = objMSCommonUtils.setToMSAccDetailsTO(objMMAccount);
					objToMSAcc_Details.setAccountPlating(objInternalPreConfirm.getCreditAcntPlatingInfo());
					intPreConfirmToObj[2]= objToMSAcc_Details;
				}
			}

			//Other MS Account extraction process..
			if(!objPaymentDetails.isToAcc_InContext()){
				if(transferType!=null && transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)) {
					objPaymentDetails.setLoanAcntNo(loanAccount);
				}
				else {
					objToMSAcc_Details= (ToMSAcc_DetailsTO)objMSCommonUtils.getOtherMSAccountDetails(otherMSAcc,objToMSAcc_Details);
					objToMSAcc_Details.setAccountPlating(objInternalPreConfirm.getCreditAcntPlatingInfo());
					intPreConfirmToObj[2]= objToMSAcc_Details;
				}
			}

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			String userRole=objMSUserDetails.getInitiatorRole();
			String userId= objMSUserDetails.getRcafId();
			intPreConfirmToObj[3] = objMSUserDetails;

			//Payment details attribute mappings..
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
			objPaymentDetails.setAuth_mode(objInternalPreConfirm.getAuth_mode());
			objPaymentDetails.setAuth_info_reciever(objInternalPreConfirm.getInformationRecvdBy());
			objPaymentDetails.setVerbal_auth_client_name(objInternalPreConfirm.getSpokeTo());
			objPaymentDetails.setOtherAcntOwner(objInternalPreConfirm.getOtherAcntOwner());
			objPaymentDetails.setVerbal_auth_date(objInternalPreConfirm.getVerbalAuthDate());
			objPaymentDetails.setVerbal_auth_time(MSCommonUtils.formatAuthTime(objInternalPreConfirm.getVerbalAuthHour(),objInternalPreConfirm.getVerbalAuthMin(),objInternalPreConfirm.getVerbalAuthAMorPM()));
			objPaymentDetails.setClient_verification_desc(objInternalPreConfirm.getClientVerfication());
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
			objPaymentDetails.setInitiator_id(userId); //RACF Id of the FA..
			objPaymentDetails.setInitiator_role(userRole);
			objPaymentDetails.setCurrent_owner_id(userId); //RACF Id of the FA..
			objPaymentDetails.setCurrent_owner_role(userRole);
			objPaymentDetails.setCreated_by_comments(objInternalPreConfirm.getUserComments());
			objPaymentDetails.setDebitAcntDataInSession(objInternalPreConfirm.isDebitAcntDataInSession());
			objPaymentDetails.setCreditAcntDataInSession(objInternalPreConfirm.isCreditAcntDataInSession());

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.ICT.toString());
			objPaymentDetails.setAction(ActionDesc.CFM.toString());
			objPaymentDetails.setState(PageStateDesc.I.toString());

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

	/**
	 * Post hook method for InternalTransfer confirm event in Init mode...
	 * @throws Exception 
	 */
	public void postInternalPreConfirmInternalPreConfirm_INITconfirmbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting postInternalPreConfirmInternalPreConfirm_INITconfirmbut");
			InternalConfirmForm objInternalConfirm=(InternalConfirmForm) objTargetForm;
			objUserPrincipal.setBrErrorMessages(null);
			String brMessages="";

			//Output Extraction...
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
						objInternalConfirm.setTxnDetails(trxnDetailsOutputTO);

						//Setting the screen field attributes ...
						objInternalConfirm.setConfirmationNo(trxnDetailsOutputTO.getConfirmationNo());
						objInternalConfirm.setTrxnType(trxnDetailsOutputTO.getTrxnType());
						objInternalConfirm.setTrxnAmount(MSCommonUtils.formatTxnAmount(trxnDetailsOutputTO.getTrxnAmount()));
						objInternalConfirm.setFrequencyType(trxnDetailsOutputTO.getFrequencyType());
						objInternalConfirm.setUntilDollarLimit(ConvertionUtil.convertToString(trxnDetailsOutputTO.getUntilDollarLimit()));

						//Account tier details...
						objInternalConfirm.setAccTierFromAcc(trxnDetailsOutputTO.getAccTierFromAcc());
						objInternalConfirm.setAccTierToAcc(trxnDetailsOutputTO.getAccTierToAcc());

						//Account number (3-6-3) details..
						objInternalConfirm.setAccNoFromAcc(trxnDetailsOutputTO.getAccNoFromAcc());
						objInternalConfirm.setAccNoToAcc(trxnDetailsOutputTO.getAccNoToAcc());

						//Routing number details..
						objInternalConfirm.setRoutingNoFromAcc(trxnDetailsOutputTO.getRoutingNoFromAcc());
						objInternalConfirm.setRoutingNoToAcc(trxnDetailsOutputTO.getRoutingNoToAcc());

						//Account Owner details..
						objInternalConfirm.setOwnerFromAcc(trxnDetailsOutputTO.getOwnerFromAcc());
						objInternalConfirm.setOwnerToAcc(trxnDetailsOutputTO.getOwnerToAcc());

						//Account held at details..
						objInternalConfirm.setAccHeldAtFromAcc(trxnDetailsOutputTO.getAccHeldAtFromAcc());
						objInternalConfirm.setAccHeldAtToAcc(trxnDetailsOutputTO.getAccHeldAtToAcc());

						//Account class details..
						objInternalConfirm.setAccClassFromAcc(trxnDetailsOutputTO.getAccClassFromAcc());
						objInternalConfirm.setAccClassToAcc(trxnDetailsOutputTO.getAccClassToAcc());

						//Account sub class details..
						objInternalConfirm.setAccSubClassFromAcc(trxnDetailsOutputTO.getAccSubClassFromAcc());
						objInternalConfirm.setAccSubClassToAcc(trxnDetailsOutputTO.getAccSubClassToAcc());

						//Account name details...
						objInternalConfirm.setAccNameFromAcc(trxnDetailsOutputTO.getAccNameFromAcc());
						objInternalConfirm.setAccNameToAcc(trxnDetailsOutputTO.getAccNameToAcc());

						//Account Other Details ...
						objInternalConfirm.setAccDtlsFromAcc(trxnDetailsOutputTO.getAccDtlsFromAcc());
						objInternalConfirm.setAccDtlsToAcc(trxnDetailsOutputTO.getAccDtlsToAcc());

						//Payment hidden attributes...
						objInternalConfirm.setTxnStatusDesc(trxnDetailsOutputTO.getStatus());
						objInternalConfirm.setBatchRefNumber(trxnDetailsOutputTO.getPaybatchref());
						objInternalConfirm.setVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getVersionnum().intValue()));
						objInternalConfirm.setBatchVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getBatversionnum()));
						objInternalConfirm.setParTxnVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getPartxnversionnum()));
						objInternalConfirm.setTxnTypeCode(ConvertionUtil.convertToString(trxnDetailsOutputTO.getTxnTypeCode()));
						objInternalConfirm.setEntryDate(ConvertionUtil.convertToAppDateStr(trxnDetailsOutputTO.getTxnEntryDate()));
						objInternalConfirm.setTransDate(ConvertionUtil.convertToAppDateStr(trxnDetailsOutputTO.getTxnInitiationDate()));
						objInternalConfirm.setScreenNavigationFlag(MSSystemDefaults.INTERNAL_TRANSFERS);

						//Setting the Table Data for the Business Code and Txn Details Table...
						objInternalConfirm.setReasonCodesTableData(trxnDetailsOutputTO.getReasonCodesList());
						objInternalConfirm.setActionDetailsTableData(trxnDetailsOutputTO.getActionsList());
						if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_VERBAL)){
							objInternalConfirm.setAuthDetailsTableData(trxnDetailsOutputTO.getAuthDetailsList());
						}
						else if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_SIGNED)){
							objInternalConfirm.setSignedAuthDetailsTableData(trxnDetailsOutputTO.getSignedAuthDetailsList());
						}
						objInternalConfirm.setAuth_mode(trxnDetailsOutputTO.getAuth_mode());
						objInternalConfirm.setRecurringFlag(trxnDetailsOutputTO.getRecurringFlag());
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
	 * Pre hook method for InternalTransfer confirm event in Edit mode...
	 * @throws Exception 
	 */
	public void preInternalPreConfirmInternalPreConfirm_Editconfirmbut(EbwForm objSourceForm, Object[] objParam, Class[] objParamType, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting preInternalPreConfirmInternalPreConfirm_Editconfirmbut"); 
			InternalPreConfirmForm objInternalPreConfirm=(InternalPreConfirmForm) objSourceForm;
			Object intPreConfirmToObj[] = (Object[])objParam[1];

			//Server side field validations...
			ValidateINTTransfer objVaidateIntTransfer= new ValidateINTTransfer();
			String validationErr = objVaidateIntTransfer.validateINTTransferConfirmFrm(objInternalPreConfirm);
			if(validationErr!=null && !validationErr.trim().equalsIgnoreCase("")){
				throw new Exception(validationErr); // Form field validation failed ...
			}

			MSCommonUtils objMSCommonUtils = new MSCommonUtils();	
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			String transferType = objInternalPreConfirm.getTransactionType();
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)intPreConfirmToObj[0];

			//Mapping From acnt details along with the acnt plating map..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			objFromMSAcc_Details.setAccountPlating(objInternalPreConfirm.getDebitAcntPlatingInfo());
			intPreConfirmToObj[1] = objFromMSAcc_Details;

			//Mapping To acnt details along with the acnt plating map..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			objToMSAcc_Details.setAccountPlating(objInternalPreConfirm.getCreditAcntPlatingInfo());
			intPreConfirmToObj[2]= objToMSAcc_Details;

			//Mapping MS User details..
			MSUser_DetailsTO objMSUserDetails =new MSUser_DetailsTO();
			objMSUserDetails = objMSCommonUtils.setMSUserDetailsTO(objUserPrincipal);
			String userId= objMSUserDetails.getRcafId();
			String userRole= objMSUserDetails.getInitiatorRole();
			intPreConfirmToObj[3] = objMSUserDetails;

			//Payment details attribute mappings..
			objPaymentDetails.setFrmAcc_InContext(false);
			objPaymentDetails.setToAcc_InContext(false);
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
			objPaymentDetails.setAuth_mode(objInternalPreConfirm.getAuth_mode());
			objPaymentDetails.setAuth_info_reciever(objInternalPreConfirm.getInformationRecvdBy());
			objPaymentDetails.setVerbal_auth_client_name(objInternalPreConfirm.getSpokeTo());
			objPaymentDetails.setOtherAcntOwner(objInternalPreConfirm.getOtherAcntOwner());
			objPaymentDetails.setVerbal_auth_date(objInternalPreConfirm.getVerbalAuthDate());
			objPaymentDetails.setVerbal_auth_time(MSCommonUtils.formatAuthTime(objInternalPreConfirm.getVerbalAuthHour(),objInternalPreConfirm.getVerbalAuthMin(),objInternalPreConfirm.getVerbalAuthAMorPM()));
			objPaymentDetails.setClient_verification_desc(objInternalPreConfirm.getClientVerfication());
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
			objPaymentDetails.setCurrent_owner_id(userId);
			objPaymentDetails.setCurrent_owner_role(userRole);
			objPaymentDetails.setCreated_by_comments(objInternalPreConfirm.getUserComments());
			objPaymentDetails.setDebitAcntDataInSession(objInternalPreConfirm.isDebitAcntDataInSession());
			objPaymentDetails.setCreditAcntDataInSession(objInternalPreConfirm.isCreditAcntDataInSession());

			//FAP mapping details..
			objPaymentDetails.setScreen(ScreenDesc.ICT);
			objPaymentDetails.setAction(ActionDesc.CFM);
			objPaymentDetails.setState(PageStateDesc.E);

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

	/**
	 * Post hook method for InternalTransfer confirm event in Edit mode...
	 * @throws Exception 
	 */
	public void postInternalPreConfirmInternalPreConfirm_Editconfirmbut(EbwForm objSourceForm, EbwForm objTargetForm, Object objReturn, Object[] objParam, UserPrincipal objUserPrincipal, HashMap retainDataMap, HashMap objUserSessionObject) throws Exception
	{
		try 
		{
			EBWLogger.trace(this, "Starting postInternalPreConfirmInternalPreConfirm_Editconfirmbut");
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
					objInternalConfirm.setBrErrors(true);
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
						objInternalConfirm.setTxnDetails(trxnDetailsOutputTO);

						//Setting the screen field attributes ...
						objInternalConfirm.setConfirmationNo(trxnDetailsOutputTO.getConfirmationNo());
						objInternalConfirm.setTrxnType(trxnDetailsOutputTO.getTrxnType());
						objInternalConfirm.setTrxnAmount(MSCommonUtils.formatTxnAmount(trxnDetailsOutputTO.getTrxnAmount()));
						objInternalConfirm.setFrequencyType(trxnDetailsOutputTO.getFrequencyType());
						objInternalConfirm.setUntilDollarLimit(ConvertionUtil.convertToString(trxnDetailsOutputTO.getUntilDollarLimit()));

						//Account tier details...
						objInternalConfirm.setAccTierFromAcc(trxnDetailsOutputTO.getAccTierFromAcc());
						objInternalConfirm.setAccTierToAcc(trxnDetailsOutputTO.getAccTierToAcc());

						//Account number (3-6-3) details..
						objInternalConfirm.setAccNoFromAcc(trxnDetailsOutputTO.getAccNoFromAcc());
						objInternalConfirm.setAccNoToAcc(trxnDetailsOutputTO.getAccNoToAcc());

						//Routing number details..
						objInternalConfirm.setRoutingNoFromAcc(trxnDetailsOutputTO.getRoutingNoFromAcc());
						objInternalConfirm.setRoutingNoToAcc(trxnDetailsOutputTO.getRoutingNoToAcc());

						//Account Owner details..
						objInternalConfirm.setOwnerFromAcc(trxnDetailsOutputTO.getOwnerFromAcc());
						objInternalConfirm.setOwnerToAcc(trxnDetailsOutputTO.getOwnerToAcc());

						//Account held at details..
						objInternalConfirm.setAccHeldAtFromAcc(trxnDetailsOutputTO.getAccHeldAtFromAcc());
						objInternalConfirm.setAccHeldAtToAcc(trxnDetailsOutputTO.getAccHeldAtToAcc());

						//Account class details..
						objInternalConfirm.setAccClassFromAcc(trxnDetailsOutputTO.getAccClassFromAcc());
						objInternalConfirm.setAccClassToAcc(trxnDetailsOutputTO.getAccClassToAcc());

						//Account sub class details..
						objInternalConfirm.setAccSubClassFromAcc(trxnDetailsOutputTO.getAccSubClassFromAcc());
						objInternalConfirm.setAccSubClassToAcc(trxnDetailsOutputTO.getAccSubClassToAcc());

						//Account name details...
						objInternalConfirm.setAccNameFromAcc(trxnDetailsOutputTO.getAccNameFromAcc());
						objInternalConfirm.setAccNameToAcc(trxnDetailsOutputTO.getAccNameToAcc());

						//Account Other Details ...
						objInternalConfirm.setAccDtlsFromAcc(trxnDetailsOutputTO.getAccDtlsFromAcc());
						objInternalConfirm.setAccDtlsToAcc(trxnDetailsOutputTO.getAccDtlsToAcc());

						//Payment hidden attributes...
						objInternalConfirm.setTxnStatusDesc(trxnDetailsOutputTO.getStatus());
						objInternalConfirm.setBatchRefNumber(trxnDetailsOutputTO.getPaybatchref());
						objInternalConfirm.setVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getVersionnum().intValue()));
						objInternalConfirm.setBatchVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getBatversionnum()));
						objInternalConfirm.setParTxnVersionnum(ConvertionUtil.convertToString(trxnDetailsOutputTO.getPartxnversionnum()));
						objInternalConfirm.setTxnTypeCode(ConvertionUtil.convertToString(trxnDetailsOutputTO.getTxnTypeCode()));
						objInternalConfirm.setEntryDate(ConvertionUtil.convertToAppDateStr(trxnDetailsOutputTO.getTxnEntryDate()));
						objInternalConfirm.setTransDate(ConvertionUtil.convertToAppDateStr(trxnDetailsOutputTO.getTxnInitiationDate()));
						objInternalConfirm.setScreenNavigationFlag(MSSystemDefaults.CTC_SEARCHES);

						//Setting the Table Data for the Business Code and Txn Details Table...
						objInternalConfirm.setReasonCodesTableData(trxnDetailsOutputTO.getReasonCodesList());
						objInternalConfirm.setActionDetailsTableData(trxnDetailsOutputTO.getActionsList());
						if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_VERBAL)){
							objInternalConfirm.setAuthDetailsTableData(trxnDetailsOutputTO.getAuthDetailsList());
						}
						else if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_SIGNED)){
							objInternalConfirm.setSignedAuthDetailsTableData(trxnDetailsOutputTO.getSignedAuthDetailsList());
						}
						objInternalConfirm.setAuth_mode(trxnDetailsOutputTO.getAuth_mode());
						objInternalConfirm.setRecurringFlag(trxnDetailsOutputTO.getRecurringFlag());
					}
					objInternalConfirm.setBusinessRuleMessages(brMessages);
				}
			}
			EBWLogger.trace(this, "Finished postInternalPreConfirmInternalPreConfirm_Editconfirmbut");
		} 
		catch (Exception exception) {
			throw exception;
		} 
	}

}