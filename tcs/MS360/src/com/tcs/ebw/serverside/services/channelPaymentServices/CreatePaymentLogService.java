package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.AuditTrialAction;
import com.tcs.Payments.ms360Utils.InitiatorRoleDesc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.Check_Print_Dtls;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.DsPayTxnsLogTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-07-2011        3               CR 262
 *    224703       29-08-2011        3-B             CR 193
 * **********************************************************
 */
public class CreatePaymentLogService extends DatabaseService{

	public CreatePaymentLogService() {

	}

	/**
	 * The methods maps the log input details and execute the query for the log insert 
	 * in the DS_PAY_TXNS_LOG Table...
	 * @param statusFlag
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 * @throws SQLException
	 */
	public void setCreatePaymentDetailsLog(int statusFlag,String loggingAction,HashMap txnDetails,ServiceContext serviceContext)throws Exception,SQLException 
	{
		EBWLogger.trace(this, "Setting the Create Payment Log Details..");
		try
		{
			//Payment details..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//User Details...
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//From account details..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//To account details..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			//External AccountDetails..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Check request mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			//Print Check request mappings details..
			Check_Print_Dtls objCheckPrint = new Check_Print_Dtls();
			if(txnDetails.containsKey("PrintCheckDetails")){
				objCheckPrint = (Check_Print_Dtls)txnDetails.get("PrintCheckDetails");
			}

			//Payment account details...
			String debitAccount_Num = MSCommonUtils.getDebitAccountNum(txnDetails);
			String creditAccount_Num = MSCommonUtils.getCreditAccountNum(txnDetails);

			//MS User attributes..
			String userId=objMSUserDetails.getRcafId();
			String lifeTimeUserId= "" ; //Life_user_id is not present in the MS360..
			String userName=MSCommonUtils.extractCurrentUserName(objMSUserDetails);
			String userInitiatorRole = MSCommonUtils.getInitiatorRoleDesc(objMSUserDetails.getInitiatorRole());
			String userApproverRole  = MSCommonUtils.getApproverRoleDesc(objPaymentDetails.getCurrent_approver_role(),serviceContext);

			//Mapping the DS_PAY_TXNS LOG Transfer object input details...
			DsPayTxnsLogTO currentTxnLogTO = new DsPayTxnsLogTO();

			//Transaction Details..
			currentTxnLogTO.setPaybatchref(null);
			currentTxnLogTO.setPaypayref(null);
			currentTxnLogTO.setAmount(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			currentTxnLogTO.setInitiation_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnLogTO.setRequest_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			currentTxnLogTO.setDebit_acct_no(debitAccount_Num);
			currentTxnLogTO.setCredit_act_no(creditAccount_Num);
			currentTxnLogTO.setKeyaccountnumber_from(objFromMSAcc_Details.getKeyAccount());
			currentTxnLogTO.setKeyaccountnumber_to(objToMSAcc_Details.getKeyAccount());
			currentTxnLogTO.setPaypayeeid(objExternalAccDetails.getCpypayeeid());
			currentTxnLogTO.setFromfa_id(ConvertionUtil.convertToDouble(objFromMSAcc_Details.getFaNumber()));
			currentTxnLogTO.setFrombranch_id(ConvertionUtil.convertToDouble(objFromMSAcc_Details.getOfficeNumber()));
			currentTxnLogTO.setTofa_id(ConvertionUtil.convertToDouble(objToMSAcc_Details.getFaNumber()));
			currentTxnLogTO.setTobranch_id(ConvertionUtil.convertToDouble(objToMSAcc_Details.getOfficeNumber()));
			currentTxnLogTO.setLife_user_id(lifeTimeUserId);
			currentTxnLogTO.setLast_action(loggingAction);
			currentTxnLogTO.setPaytxneprydate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getTxnExpiryDate()));
			currentTxnLogTO.setFrequency(objPaymentDetails.getFrequency_Type());
			currentTxnLogTO.setRecurring_freq(objPaymentDetails.getFrequency_DurationDesc());
			currentTxnLogTO.setUntilDollarLimit(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit()));
			currentTxnLogTO.setUntilFreqEndDate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getDuration_EndDate()));
			currentTxnLogTO.setUntilNoOfTransfers(ConvertionUtil.convertToDouble(objPaymentDetails.getDuration_NoOfTransfers()));
			if(objPaymentDetails.getFrequency_Type()!=null && objPaymentDetails.getFrequency_Type().equalsIgnoreCase("2")){
				currentTxnLogTO.setRepeat_untill(objPaymentDetails.getFrequency_DurationValue());
			}

			//User Details..
			currentTxnLogTO.setCreated_modified_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnLogTO.setCreated_modified_by_name(userName);
			currentTxnLogTO.setCreated_modified_by_emp_id(userId);
			currentTxnLogTO.setCreated_modified_by_id(userId);
			currentTxnLogTO.setCreated_modified_by_comments(objPaymentDetails.getCreated_by_comments());
			currentTxnLogTO.setOwner_id(objPaymentDetails.getCurrent_owner_id());
			currentTxnLogTO.setOwner_role(objPaymentDetails.getCurrent_owner_role());
			if(loggingAction!=null && (loggingAction.equalsIgnoreCase(AuditTrialAction.Create) || loggingAction.equalsIgnoreCase(AuditTrialAction.Modify))){
				currentTxnLogTO.setNext_approver_role(objPaymentDetails.getCurrent_approver_role());
			}
			else if(loggingAction!=null && (loggingAction.equalsIgnoreCase(AuditTrialAction.Approve))){
				currentTxnLogTO.setNext_approver_role(objPaymentDetails.getNext_approver_role());
			}

			//Verbal authorization ,IRA ,RTA related Details...
			currentTxnLogTO.setAuth_mode(objPaymentDetails.getAuth_mode());
			currentTxnLogTO.setAuth_info_reciever(objPaymentDetails.getAuth_info_reciever());
			currentTxnLogTO.setVerbal_auth_client_name(objPaymentDetails.getVerbal_auth_client_name());
			currentTxnLogTO.setVerbal_auth_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getVerbal_auth_date()));
			currentTxnLogTO.setVerbal_auth_time(objPaymentDetails.getVerbal_auth_time());
			currentTxnLogTO.setAuthinfo_confby_name(objPaymentDetails.getAuth_confirmed_by());
			currentTxnLogTO.setClient_verification_desc(objPaymentDetails.getClient_verification_desc());
			currentTxnLogTO.setSame_name_flag(objPaymentDetails.getSame_name_flag());
			currentTxnLogTO.setDont_spawn_flag(objPaymentDetails.getDont_spawn_flag());
			currentTxnLogTO.setRetirement_mnemonic(objPaymentDetails.getRetirement_mnemonic());
			currentTxnLogTO.setQualifier(objPaymentDetails.getQualifier());
			currentTxnLogTO.setDisplay_qualifier(objPaymentDetails.getDisplay_qualifier());
			currentTxnLogTO.setReverse_qualifier(objPaymentDetails.getReverse_qualifier());
			currentTxnLogTO.setRta_booked_in_flag(objPaymentDetails.getCurrent_rta_booked_flag());
			currentTxnLogTO.setOfac_case_id(objPaymentDetails.getOfac_case_id());

			//Check transaction details..
			currentTxnLogTO.setPayeeName(objCheckRequest.getPayeeName());
			currentTxnLogTO.setThirdPartyReason(objCheckRequest.getThirdPartyReason());
			currentTxnLogTO.setCertifiedFlag(objCheckRequest.getCertifiedFlag());
			currentTxnLogTO.setFee(objPaymentDetails.getTxn_Fee_Charge());
			currentTxnLogTO.setChargedTo(objCheckRequest.getChargedTo());
			currentTxnLogTO.setPickUpOption(objCheckRequest.getPickUpOption());
			currentTxnLogTO.setDefaultAddressFlag(objCheckRequest.getDefaultAddressFlag());
			currentTxnLogTO.setForeignAddressFlag(objCheckRequest.getForeignAddressFlag());
			currentTxnLogTO.setPrintingBranch(objCheckRequest.getPrintingBranch());
			currentTxnLogTO.setDeliveryAddrLine1(objCheckRequest.getDeliveryAddrLine1());
			currentTxnLogTO.setDeliveryAddrLine2(objCheckRequest.getDeliveryAddrLine2());
			currentTxnLogTO.setDeliveryAddrLine3(objCheckRequest.getDeliveryAddrLine3());
			currentTxnLogTO.setDeliveryAddrLine4(objCheckRequest.getDeliveryAddrLine4());
			currentTxnLogTO.setPrintAddressFlag(objCheckRequest.getPrintAddressFlag());
			currentTxnLogTO.setMemoLine1(objCheckRequest.getMemoLine1());
			currentTxnLogTO.setMemoLine2(objCheckRequest.getMemoLine2());
			currentTxnLogTO.setPrintMemoCheckFlag(objCheckRequest.getPrintMemoCheckFlag());
			currentTxnLogTO.setPrintMemoStubFlag(objCheckRequest.getPrintMemoStubFlag());
			currentTxnLogTO.setDeliveredToName(objCheckRequest.getDeliveredToName());
			currentTxnLogTO.setDeliveredToType(objCheckRequest.getDeliveredToType());
			currentTxnLogTO.setTypeOfId(objCheckRequest.getTypeOfId());
			currentTxnLogTO.setIdNumber(objCheckRequest.getIdNumber());
			currentTxnLogTO.setCheckNo(objCheckRequest.getCheckNo());
			currentTxnLogTO.setTrackingId(objCheckRequest.getTrackingId());
			currentTxnLogTO.setEstPickupTime(objCheckRequest.getEstPickupTime());
			currentTxnLogTO.setPayee_Type(objCheckRequest.getPayeeType());
			currentTxnLogTO.setPrinterName(objCheckPrint.getPrinter_name());
			currentTxnLogTO.setIssue_dt(objCheckPrint.getIssue_dt());
			currentTxnLogTO.setCheck_status(objCheckPrint.getCheck_status());
			currentTxnLogTO.setVoid_reason_code(objCheckPrint.getVoid_reason_code());
			currentTxnLogTO.setVoid_reason_desc(objCheckPrint.getVoid_reason_desc());

			//Department/Group column mapping changes based on the type of logging action.
			if(loggingAction!=null && (loggingAction.equalsIgnoreCase(AuditTrialAction.Approve) || loggingAction.equalsIgnoreCase(AuditTrialAction.Reject))){
				currentTxnLogTO.setCreated_modified_by_role(userApproverRole);
				currentTxnLogTO.setPaygroupid(userApproverRole);
			}
			else if(loggingAction!=null && (loggingAction.equalsIgnoreCase(AuditTrialAction.Create) || loggingAction.equalsIgnoreCase(AuditTrialAction.Modify))){
				currentTxnLogTO.setCreated_modified_by_role(userInitiatorRole);
				currentTxnLogTO.setPaygroupid(userInitiatorRole);
			}
			else {
				currentTxnLogTO.setCreated_modified_by_role(InitiatorRoleDesc.BLANK);
				currentTxnLogTO.setPaygroupid(InitiatorRoleDesc.BLANK);
			}

			//StatementId and Object mapping for the execute query...
			String[] createPaymentLogStmntId={"setTransactionLog"};
			Object[] createPaymentLogTOobj={currentTxnLogTO};
			String statusFlagVal = Integer.toString(statusFlag);
			Boolean isTxnCommitReq = Boolean.TRUE;
			if(createPaymentLogTOobj[0] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsLogTO)
			{
				//Updating the user details in case the transaction is system rejected ....
				if(statusFlagVal!=null && statusFlagVal.equalsIgnoreCase("52")){
					updateSystemDetails(txnDetails,(DsPayTxnsLogTO)createPaymentLogTOobj[0]);
				}

				((DsPayTxnsLogTO)createPaymentLogTOobj[0]).setPaybatchref(objPaymentDetails.getTxnBatchId());
				((DsPayTxnsLogTO)createPaymentLogTOobj[0]).setPaypayref(objPaymentDetails.getTransactionId());
				((DsPayTxnsLogTO)createPaymentLogTOobj[0]).setTxn_status(statusFlagVal);
				((DsPayTxnsLogTO)createPaymentLogTOobj[0]).setTrial_depo(objPaymentDetails.getTrial_depo());
			}
			EBWLogger.logDebug(this,"Executing Query : "+createPaymentLogStmntId[0]);
			EBWLogger.logDebug(this,"Input parameters to be mapped : "+createPaymentLogTOobj[0]);
			execute(createPaymentLogStmntId[0], createPaymentLogTOobj[0], isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+createPaymentLogStmntId[0]);
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * The methods maps the next child transaction (if created)log input details and execute the query for the log insert 
	 * in the DS_PAY_TXNS_LOG Table...
	 * @param statusFlag
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 * @throws SQLException
	 */
	public void setNextChildPaymentsLog(int paymentStatusFlag,HashMap txnDetails,ServiceContext serviceContext)throws Exception,SQLException 
	{
		EBWLogger.trace(this, "Setting the Create Payment Log Details for the child transaction (if created)..");
		try
		{
			//Payment details..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//From account details..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//To account details..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			//External AccountDetails..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Check request mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			//Print Check request mappings details..
			Check_Print_Dtls objCheckPrint = new Check_Print_Dtls();
			if(txnDetails.containsKey("PrintCheckDetails")){
				objCheckPrint = (Check_Print_Dtls)txnDetails.get("PrintCheckDetails");
			}

			//Payment account details...
			String debitAccount_Num = MSCommonUtils.getDebitAccountNum(txnDetails);
			String creditAccount_Num = MSCommonUtils.getCreditAccountNum(txnDetails);
			String statusFlagVal = Integer.toString(paymentStatusFlag);

			//MS User attributes..
			String lifeTimeUserId="";

			//Mapping the DS_PAY_TXNS LOG Transfer object input details...
			DsPayTxnsLogTO nextChildTxnLogTO = new DsPayTxnsLogTO();

			//Transaction Details..
			nextChildTxnLogTO.setPaybatchref(null);
			nextChildTxnLogTO.setPaypayref(null);
			nextChildTxnLogTO.setAmount(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			nextChildTxnLogTO.setInitiation_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getChildTxnRequestedExecDate()));
			nextChildTxnLogTO.setRequest_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			nextChildTxnLogTO.setDebit_acct_no(debitAccount_Num);
			nextChildTxnLogTO.setCredit_act_no(creditAccount_Num);
			nextChildTxnLogTO.setKeyaccountnumber_from(objFromMSAcc_Details.getKeyAccount());
			nextChildTxnLogTO.setKeyaccountnumber_to(objToMSAcc_Details.getKeyAccount());
			nextChildTxnLogTO.setPaypayeeid(objExternalAccDetails.getCpypayeeid());
			nextChildTxnLogTO.setFromfa_id(ConvertionUtil.convertToDouble(objFromMSAcc_Details.getFaNumber()));
			nextChildTxnLogTO.setFrombranch_id(ConvertionUtil.convertToDouble(objFromMSAcc_Details.getOfficeNumber()));
			nextChildTxnLogTO.setTofa_id(ConvertionUtil.convertToDouble(objToMSAcc_Details.getFaNumber()));
			nextChildTxnLogTO.setTobranch_id(ConvertionUtil.convertToDouble(objToMSAcc_Details.getOfficeNumber()));
			nextChildTxnLogTO.setLife_user_id(lifeTimeUserId);
			nextChildTxnLogTO.setLast_action(AuditTrialAction.Create);
			nextChildTxnLogTO.setPaytxneprydate(null);
			nextChildTxnLogTO.setFrequency(objPaymentDetails.getFrequency_Type());
			nextChildTxnLogTO.setRecurring_freq(objPaymentDetails.getFrequency_DurationDesc());
			nextChildTxnLogTO.setUntilDollarLimit(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit()));
			nextChildTxnLogTO.setUntilFreqEndDate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getDuration_EndDate()));
			nextChildTxnLogTO.setUntilNoOfTransfers(ConvertionUtil.convertToDouble(objPaymentDetails.getDuration_NoOfTransfers()));
			if(objPaymentDetails.getFrequency_Type()!=null && objPaymentDetails.getFrequency_Type().equalsIgnoreCase("2")){
				nextChildTxnLogTO.setRepeat_untill(objPaymentDetails.getFrequency_DurationValue());
			}

			//User Details..
			nextChildTxnLogTO.setCreated_modified_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			nextChildTxnLogTO.setCreated_modified_by_name(objPaymentDetails.getMMSystemDesc());
			nextChildTxnLogTO.setCreated_modified_by_emp_id(objPaymentDetails.getMMSystemDesc());
			nextChildTxnLogTO.setCreated_modified_by_role(objPaymentDetails.getMMSystemDesc());
			nextChildTxnLogTO.setCreated_modified_by_id(objPaymentDetails.getMMSystemDesc());
			nextChildTxnLogTO.setCreated_modified_by_comments(null);
			nextChildTxnLogTO.setPaygroupid(objPaymentDetails.getMMSystemDesc());
			nextChildTxnLogTO.setOwner_id(objPaymentDetails.getCurrent_owner_id());
			nextChildTxnLogTO.setOwner_role(objPaymentDetails.getCurrent_owner_role());
			nextChildTxnLogTO.setNext_approver_role(null);

			//Verbal authorization ,IRA ,RTA related Details...
			nextChildTxnLogTO.setAuth_mode(objPaymentDetails.getAuth_mode());
			nextChildTxnLogTO.setAuth_info_reciever(objPaymentDetails.getAuth_info_reciever());
			nextChildTxnLogTO.setVerbal_auth_client_name(objPaymentDetails.getVerbal_auth_client_name());
			nextChildTxnLogTO.setVerbal_auth_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getVerbal_auth_date()));
			nextChildTxnLogTO.setVerbal_auth_time(objPaymentDetails.getVerbal_auth_time());
			nextChildTxnLogTO.setClient_verification_desc(objPaymentDetails.getClient_verification_desc());
			nextChildTxnLogTO.setSame_name_flag(objPaymentDetails.getSame_name_flag());
			nextChildTxnLogTO.setDont_spawn_flag(objPaymentDetails.getDont_spawn_flag());
			nextChildTxnLogTO.setRetirement_mnemonic(objPaymentDetails.getRetirement_mnemonic());
			nextChildTxnLogTO.setQualifier(objPaymentDetails.getQualifier());
			nextChildTxnLogTO.setDisplay_qualifier(objPaymentDetails.getDisplay_qualifier());
			nextChildTxnLogTO.setReverse_qualifier(objPaymentDetails.getReverse_qualifier());
			nextChildTxnLogTO.setRta_booked_in_flag(null);
			nextChildTxnLogTO.setOfac_case_id(null);

			//Check transaction details..
			nextChildTxnLogTO.setPayeeName(objCheckRequest.getPayeeName());
			nextChildTxnLogTO.setThirdPartyReason(objCheckRequest.getThirdPartyReason());
			nextChildTxnLogTO.setCertifiedFlag(objCheckRequest.getCertifiedFlag());
			nextChildTxnLogTO.setFee(objPaymentDetails.getTxn_Fee_Charge());
			nextChildTxnLogTO.setChargedTo(objCheckRequest.getChargedTo());
			nextChildTxnLogTO.setPickUpOption(objCheckRequest.getPickUpOption());
			nextChildTxnLogTO.setDefaultAddressFlag(objCheckRequest.getDefaultAddressFlag());
			nextChildTxnLogTO.setForeignAddressFlag(objCheckRequest.getForeignAddressFlag());
			nextChildTxnLogTO.setPrintingBranch(objCheckRequest.getPrintingBranch());
			nextChildTxnLogTO.setDeliveryAddrLine1(objCheckRequest.getDeliveryAddrLine1());
			nextChildTxnLogTO.setDeliveryAddrLine2(objCheckRequest.getDeliveryAddrLine2());
			nextChildTxnLogTO.setDeliveryAddrLine3(objCheckRequest.getDeliveryAddrLine3());
			nextChildTxnLogTO.setDeliveryAddrLine4(objCheckRequest.getDeliveryAddrLine4());
			nextChildTxnLogTO.setPrintAddressFlag(objCheckRequest.getPrintAddressFlag());
			nextChildTxnLogTO.setMemoLine1(objCheckRequest.getMemoLine1());
			nextChildTxnLogTO.setMemoLine2(objCheckRequest.getMemoLine2());
			nextChildTxnLogTO.setPrintMemoCheckFlag(objCheckRequest.getPrintMemoCheckFlag());
			nextChildTxnLogTO.setPrintMemoStubFlag(objCheckRequest.getPrintMemoStubFlag());
			nextChildTxnLogTO.setDeliveredToName(objCheckRequest.getDeliveredToName());
			nextChildTxnLogTO.setDeliveredToType(objCheckRequest.getDeliveredToType());
			nextChildTxnLogTO.setTypeOfId(objCheckRequest.getTypeOfId());
			nextChildTxnLogTO.setIdNumber(objCheckRequest.getIdNumber());
			nextChildTxnLogTO.setCheckNo(objCheckRequest.getCheckNo());
			nextChildTxnLogTO.setTrackingId(objCheckRequest.getTrackingId());
			nextChildTxnLogTO.setEstPickupTime(objCheckRequest.getEstPickupTime());
			nextChildTxnLogTO.setPayee_Type(objCheckRequest.getPayeeType());
			nextChildTxnLogTO.setPrinterName(objCheckPrint.getPrinter_name());
			nextChildTxnLogTO.setIssue_dt(null);
			nextChildTxnLogTO.setCheck_status(null);
			nextChildTxnLogTO.setVoid_reason_code(null);
			nextChildTxnLogTO.setVoid_reason_desc(null);


			//StatementId and Object mapping for the execute query...
			String[] createPaymentLogStmntId={"setTransactionLog"};
			Object[] createPaymentLogTOobj={nextChildTxnLogTO};
			Boolean isTxnCommitReq = Boolean.TRUE;
			if(createPaymentLogTOobj[0] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsLogTO){
				((DsPayTxnsLogTO)createPaymentLogTOobj[0]).setPaybatchref(objPaymentDetails.getChildTxnBatchId());
				((DsPayTxnsLogTO)createPaymentLogTOobj[0]).setPaypayref(objPaymentDetails.getChildTransactionId());
				((DsPayTxnsLogTO)createPaymentLogTOobj[0]).setTxn_status(statusFlagVal);
				((DsPayTxnsLogTO)createPaymentLogTOobj[0]).setTrial_depo(objPaymentDetails.getTrial_depo());
			}
			EBWLogger.logDebug(this,"Executing Query : "+createPaymentLogStmntId[0]);
			EBWLogger.logDebug(this,"Input parameters to be mapped : "+createPaymentLogTOobj[0]);
			execute(createPaymentLogStmntId[0], createPaymentLogTOobj[0], isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+createPaymentLogStmntId[0]);
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * Setting the UserDetails to the MM System in case of "System Rejected"
	 * @param dsPayTxnsLog
	 * @throws Exception 
	 */
	public void updateSystemDetails(HashMap txnDetails,DsPayTxnsLogTO dsPayTxnsLog) throws Exception
	{
		try 
		{
			//Getting the payment details ....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the log details to the MM System in case of "System Rejected" case only...
			dsPayTxnsLog.setCreated_modified_by_comments(null);
			dsPayTxnsLog.setCreated_modified_by_name(objPaymentDetails.getMMSystemDesc());
			dsPayTxnsLog.setCreated_modified_by_emp_id(objPaymentDetails.getMMSystemDesc());
			dsPayTxnsLog.setCreated_modified_by_role(objPaymentDetails.getMMSystemDesc());
			dsPayTxnsLog.setCreated_modified_by_id(objPaymentDetails.getMMSystemDesc());
			dsPayTxnsLog.setPaygroupid(objPaymentDetails.getMMSystemDesc());
		} 
		catch (Exception exception) {
			throw exception;
		}

	}
}
