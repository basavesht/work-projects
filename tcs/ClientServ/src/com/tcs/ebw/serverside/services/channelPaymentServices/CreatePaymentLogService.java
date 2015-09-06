/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.AuditTrialAction;
import com.tcs.Payments.ms360Utils.InitiatorRoleDesc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
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
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Payment account details...
			String debitAccount_Num = MSCommonUtils.getDebitAccountNum(txnDetails);
			String creditAccount_Num = MSCommonUtils.getCreditAccountNum(txnDetails);

			//MS User attributes..
			String userId=objMSUserDetails.getLoginId();
			String lifeTimeUserId=objMSUserDetails.getUuid();
			String userName=MSCommonUtils.extractCurrentUserName(objMSUserDetails);
			String userRole=null;
			boolean isFA= objMSUserDetails.isFA();
			if(isFA)
				userRole=InitiatorRoleDesc.FA;
			else
				userRole=InitiatorRoleDesc.Client;

			//Mapping the DS_PAY_TXNS LOG Transfer object input details...
			DsPayTxnsLogTO currentTxnLogTO = new DsPayTxnsLogTO();

			//Transaction Details..
			currentTxnLogTO.setPaybatchref(null);
			currentTxnLogTO.setPaypayref(null);
			currentTxnLogTO.setAmount(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			currentTxnLogTO.setInitiation_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnLogTO.setRequest_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			currentTxnLogTO.setLast_action(loggingAction);
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
			currentTxnLogTO.setCreated_modified_by_role(userRole);
			currentTxnLogTO.setCreated_modified_by_id(userId);
			currentTxnLogTO.setCreated_modified_by_comments(objPaymentDetails.getCreated_by_comments());
			currentTxnLogTO.setPaygroupid(userRole);
			currentTxnLogTO.setOwner_id(objPaymentDetails.getCurrent_owner_id());
			currentTxnLogTO.setOwner_role(objPaymentDetails.getCurrent_owner_role());
			if(loggingAction!=null && (loggingAction.equalsIgnoreCase(AuditTrialAction.Create) || loggingAction.equalsIgnoreCase(AuditTrialAction.Modify))){
				currentTxnLogTO.setNext_approver_role(objPaymentDetails.getCurrent_approver_role());
			}

			//Verbal authorization, IRA, RTA related Details..
			currentTxnLogTO.setAuth_mode(objPaymentDetails.getAuth_mode());
			currentTxnLogTO.setAuth_info_reciever(objPaymentDetails.getAuth_info_reciever());
			currentTxnLogTO.setVerbal_auth_client_name(objPaymentDetails.getVerbal_auth_client_name());
			currentTxnLogTO.setVerbal_auth_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getVerbal_auth_date()));
			currentTxnLogTO.setVerbal_auth_time(objPaymentDetails.getVerbal_auth_time());
			currentTxnLogTO.setClient_verification_desc(objPaymentDetails.getClient_verification_desc());
			currentTxnLogTO.setSame_name_flag(objPaymentDetails.getSame_name_flag());
			currentTxnLogTO.setDont_spawn_flag(objPaymentDetails.getDont_spawn_flag());
			currentTxnLogTO.setRta_booked_in_flag(objPaymentDetails.getCurrent_rta_booked_flag());
			currentTxnLogTO.setOfac_case_id(objPaymentDetails.getOfac_case_id());

			//StatementId and Object mapping for the execute query...
			String[] createPaymentLogStmntId={"setTransactionLog"};
			Object[] createPaymentLogTOobj={currentTxnLogTO};
			String statusFlagVal = Integer.toString(statusFlag);
			Boolean isTxnCommitReq = Boolean.TRUE;
			if(createPaymentLogTOobj[0] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsLogTO){
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
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Payment account details...
			String debitAccount_Num = MSCommonUtils.getDebitAccountNum(txnDetails);
			String creditAccount_Num = MSCommonUtils.getCreditAccountNum(txnDetails);
			String statusFlagVal = Integer.toString(paymentStatusFlag);

			//MS User attributes..
			String lifeTimeUserId=objMSUserDetails.getUuid();

			//Mapping the DS_PAY_TXNS LOG Transfer object input details...
			DsPayTxnsLogTO nextChildTxnLogTO = new DsPayTxnsLogTO();

			//Transaction Details..
			nextChildTxnLogTO.setPaybatchref(null);
			nextChildTxnLogTO.setPaypayref(null);
			nextChildTxnLogTO.setCreated_modified_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			nextChildTxnLogTO.setAmount(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			nextChildTxnLogTO.setInitiation_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getChildTxnRequestedExecDate()));
			nextChildTxnLogTO.setRequest_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			nextChildTxnLogTO.setDebit_acct_no(debitAccount_Num);
			nextChildTxnLogTO.setCredit_act_no(creditAccount_Num);
			nextChildTxnLogTO.setKeyaccountnumber_from(objFromMSAcc_Details.getKeyAccount());
			nextChildTxnLogTO.setKeyaccountnumber_to(objToMSAcc_Details.getKeyAccount());
			nextChildTxnLogTO.setPaypayeeid(objExternalAccDetails.getCpypayeeid());
			nextChildTxnLogTO.setLast_action(AuditTrialAction.Create);
			nextChildTxnLogTO.setFromfa_id(ConvertionUtil.convertToDouble(objFromMSAcc_Details.getFaNumber()));
			nextChildTxnLogTO.setFrombranch_id(ConvertionUtil.convertToDouble(objFromMSAcc_Details.getOfficeNumber()));
			nextChildTxnLogTO.setTofa_id(ConvertionUtil.convertToDouble(objToMSAcc_Details.getFaNumber()));
			nextChildTxnLogTO.setTobranch_id(ConvertionUtil.convertToDouble(objToMSAcc_Details.getOfficeNumber()));
			nextChildTxnLogTO.setLife_user_id(lifeTimeUserId);
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
			nextChildTxnLogTO.setCreated_modified_by_name(objPaymentDetails.getMMSystemDesc());
			nextChildTxnLogTO.setCreated_modified_by_emp_id(objPaymentDetails.getMMSystemDesc());
			nextChildTxnLogTO.setCreated_modified_by_role(objPaymentDetails.getMMSystemDesc());
			nextChildTxnLogTO.setCreated_modified_by_id(objPaymentDetails.getMMSystemDesc());
			nextChildTxnLogTO.setCreated_modified_by_comments(null);
			nextChildTxnLogTO.setPaygroupid(objPaymentDetails.getMMSystemDesc());
			nextChildTxnLogTO.setOwner_id(objPaymentDetails.getCurrent_owner_id());
			nextChildTxnLogTO.setOwner_role(objPaymentDetails.getCurrent_owner_role());
			nextChildTxnLogTO.setNext_approver_role(null);

			//Verbal authorization, IRA, RTA Details..
			nextChildTxnLogTO.setAuth_mode(objPaymentDetails.getAuth_mode());
			nextChildTxnLogTO.setAuth_info_reciever(objPaymentDetails.getAuth_info_reciever());
			nextChildTxnLogTO.setVerbal_auth_client_name(objPaymentDetails.getVerbal_auth_client_name());
			nextChildTxnLogTO.setVerbal_auth_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getVerbal_auth_date()));
			nextChildTxnLogTO.setVerbal_auth_time(objPaymentDetails.getVerbal_auth_time());
			nextChildTxnLogTO.setClient_verification_desc(objPaymentDetails.getClient_verification_desc());
			nextChildTxnLogTO.setSame_name_flag(objPaymentDetails.getSame_name_flag());
			nextChildTxnLogTO.setDont_spawn_flag(objPaymentDetails.getDont_spawn_flag());
			nextChildTxnLogTO.setRta_booked_in_flag(null);
			nextChildTxnLogTO.setOfac_case_id(null);

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
}
