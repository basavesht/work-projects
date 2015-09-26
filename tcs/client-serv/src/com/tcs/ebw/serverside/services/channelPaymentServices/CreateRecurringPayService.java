/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.BRUtils;
import com.tcs.Payments.ms360Utils.InitiatorRoleDesc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsBatchTO;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.DsPayTxnsTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.MerlinOutResponse;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.TxnParentTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 *    224703       01-07-2011        3               CR 262
 * **********************************************************
 */
public class CreateRecurringPayService  extends DatabaseService{

	public CreateRecurringPayService() {

	}

	/**
	 * The method maps the payment details to the transfer object and finally executes the 
	 * query for the data insertion in the (TXN_PARENT,DS_BATCH and DS_PAY_TXNS) table...
	 * @param statusFlag
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 * @throws SQLException
	 */
	public void setCreateRecurringPayDetails(int statusFlag,HashMap txnDetails,ServiceContext serviceContext) throws Exception,SQLException 
	{
		EBWLogger.trace(this, "Setting the Create recurring payments input details...");
		try
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
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

			//Payment account details...
			String from_MSAccountNumber = MSCommonUtils.getCompleteMSAccNumber(objFromMSAcc_Details);
			String to_MSAccountNumber = MSCommonUtils.getCompleteMSAccNumber(objToMSAcc_Details);
			String debitAccount_Num = MSCommonUtils.getDebitAccountNum(txnDetails);
			String creditAccount_Num = MSCommonUtils.getCreditAccountNum(txnDetails);
			String transferType = objPaymentDetails.getTransfer_Type();

			//MS User attributes..
			String userId=objMSUserDetails.getLoginId();
			String lifeTimeUserId=objMSUserDetails.getUuid();
			String userName=MSCommonUtils.extractCurrentUserName(objMSUserDetails);
			String userRole=null;
			boolean isFA= objMSUserDetails.isFA();
			if(isFA)
				userRole =InitiatorRoleDesc.FA;
			else
				userRole =InitiatorRoleDesc.Client;

			//Mapping the TXN_PARENT Transfer Object for the current recurring transaction..
			TxnParentTO recTxnParentTO = new TxnParentTO();
			recTxnParentTO.setAmount(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			recTxnParentTO.setFrequency(ConvertionUtil.convertToString(objPaymentDetails.getFrequency_DurationDesc()));
			recTxnParentTO.setDuration(ConvertionUtil.convertToString(objPaymentDetails.getFrequency_DurationValue()));
			recTxnParentTO.setUntildate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getDuration_EndDate()));
			recTxnParentTO.setThrshold_amt(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit()));
			recTxnParentTO.setMax_txn_no(ConvertionUtil.convertToDouble(objPaymentDetails.getDuration_NoOfTransfers()));
			recTxnParentTO.setNext_txn_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			recTxnParentTO.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			recTxnParentTO.setPar_txn_request_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			recTxnParentTO.setFrom_acct(debitAccount_Num);
			recTxnParentTO.setTo_acct(creditAccount_Num);
			recTxnParentTO.setModifiedby(userId);

			//Mapping the DS_BATCH Transfer Object for the current transaction.
			DsBatchTO currentTxnBatchTO = new DsBatchTO();
			currentTxnBatchTO.setBatapplicationid(objPaymentDetails.getApplicationId());
			currentTxnBatchTO.setBatcreateddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnBatchTO.setTxn_entry_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnBatchTO.setBatmodifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnBatchTO.setBatnooftxns(new Double(1D));
			currentTxnBatchTO.setBatcreatedby(userId);
			currentTxnBatchTO.setBatmodifiedby(userId);
			currentTxnBatchTO.setBatgrpid(userRole);

			//Mapping the DS_PAY_TXNS Transfer Object for the current transaction.
			DsPayTxnsTO currentTxnPaymentTO = new DsPayTxnsTO();
			currentTxnPaymentTO.setPaytypecode(objPaymentDetails.getScreen_Type());
			currentTxnPaymentTO.setPayservicetypecode(objPaymentDetails.getScreen_Type());
			currentTxnPaymentTO.setPaydebitaccnum(MSCommonUtils.getPayDebitaccnum(txnDetails));
			currentTxnPaymentTO.setPaypayeeaccnum(MSCommonUtils.getPaypayeeaccnum(txnDetails));
			currentTxnPaymentTO.setPaycurrcode(objPaymentDetails.getTransfer_Currency());
			currentTxnPaymentTO.setPaydebitamt(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			currentTxnPaymentTO.setPaypymtdate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			currentTxnPaymentTO.setPayfxrate(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getFxRate()));
			//External Account related Details if any...
			currentTxnPaymentTO.setPaypayeename1(objExternalAccDetails.getCpypayeename1());
			currentTxnPaymentTO.setPaypayeebankcode(objExternalAccDetails.getCpybankcode());
			currentTxnPaymentTO.setPaypayeeadd1(objExternalAccDetails.getCpyadd1());
			currentTxnPaymentTO.setPaypayeeadd2(objExternalAccDetails.getCpyadd2());
			currentTxnPaymentTO.setPaypayeeadd3(objExternalAccDetails.getCpyadd3());
			currentTxnPaymentTO.setPaypayee_acct_type(objExternalAccDetails.getCpyacctype());
			currentTxnPaymentTO.setPaydisctype(objExternalAccDetails.getSign_up_mode());  //Stores the Sign up mode for the external account..
			currentTxnPaymentTO.setPaypayeeid(objExternalAccDetails.getCpyaccnum());
			//External Account details end..
			currentTxnPaymentTO.setPayfrequency(objPaymentDetails.getFrequency_DurationDesc());
			currentTxnPaymentTO.setPayfreqpaymentcount(ConvertionUtil.convertToDouble(objPaymentDetails.getDuration_NoOfTransfers()));
			currentTxnPaymentTO.setPayfreqenddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getDuration_EndDate()));
			currentTxnPaymentTO.setPayfreqlimit(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit()));
			currentTxnPaymentTO.setCreateddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnPaymentTO.setRequested_exe_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			currentTxnPaymentTO.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnPaymentTO.setTxn_type(objPaymentDetails.getTransfer_Type());
			currentTxnPaymentTO.setKeyaccountnumber_from(objFromMSAcc_Details.getKeyAccount());
			currentTxnPaymentTO.setFriendlyname_from(objFromMSAcc_Details.getFriendlyName());
			currentTxnPaymentTO.setNickname_from(objFromMSAcc_Details.getNickName());
			currentTxnPaymentTO.setFromfa_id(ConvertionUtil.convertToDouble(objFromMSAcc_Details.getFaNumber()));
			currentTxnPaymentTO.setFromfa_id_hist(null);
			currentTxnPaymentTO.setFrombranch_id(ConvertionUtil.convertToDouble(objFromMSAcc_Details.getOfficeNumber()));
			currentTxnPaymentTO.setFrombranch_id_hist(null);
			currentTxnPaymentTO.setFromacct_tier(null);
			currentTxnPaymentTO.setFromacct_no_hist(null);
			currentTxnPaymentTO.setFrombr_acct_no_fa(from_MSAccountNumber);
			currentTxnPaymentTO.setFromacct_type1("Cash");
			currentTxnPaymentTO.setKeyaccountnumber_to(objToMSAcc_Details.getKeyAccount());
			currentTxnPaymentTO.setFriendlyname_to(objToMSAcc_Details.getFriendlyName());
			currentTxnPaymentTO.setNickname_to(objToMSAcc_Details.getNickName());
			currentTxnPaymentTO.setTofa_id(ConvertionUtil.convertToDouble(objToMSAcc_Details.getFaNumber()));
			currentTxnPaymentTO.setTofa_id_hist(null);
			currentTxnPaymentTO.setTobranch_id(ConvertionUtil.convertToDouble(objToMSAcc_Details.getOfficeNumber()));
			currentTxnPaymentTO.setTobranch_id_hist(null);
			currentTxnPaymentTO.setToacct_tier(null);
			currentTxnPaymentTO.setToacct_no_hist(null);
			currentTxnPaymentTO.setTobr_acct_no_fa(to_MSAccountNumber);
			currentTxnPaymentTO.setToacct_type1("Cash");
			currentTxnPaymentTO.setAcct_user_id(userId);
			currentTxnPaymentTO.setAcct_user_name(userName);
			currentTxnPaymentTO.setCreated_by_role(userRole);
			currentTxnPaymentTO.setModifiedby_name(userName);
			currentTxnPaymentTO.setCreatedby_name(userName);
			currentTxnPaymentTO.setCreatedby(userId);
			currentTxnPaymentTO.setModifiedby(userId);
			currentTxnPaymentTO.setLife_user_id(lifeTimeUserId);
			currentTxnPaymentTO.setPar_txn_no(null);
			currentTxnPaymentTO.setAcct_from_type(MSCommonUtils.getFrom_AccType(objPaymentDetails));
			currentTxnPaymentTO.setAcct_to_type(MSCommonUtils.getTo_AccType(objPaymentDetails));
			currentTxnPaymentTO.setPaygroupid(userRole);
			currentTxnPaymentTO.setPayissueddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getInitiation_Date()));
			currentTxnPaymentTO.setInitiator_id(objPaymentDetails.getInitiator_id());
			currentTxnPaymentTO.setInitiator_role(objPaymentDetails.getInitiator_role());
			currentTxnPaymentTO.setCurrent_owner_id(objPaymentDetails.getCurrent_owner_id());
			currentTxnPaymentTO.setCurrent_owner_role(objPaymentDetails.getCurrent_owner_role());
			currentTxnPaymentTO.setSame_name_flag(BRUtils.getSameNameFlag(txnDetails));

			//StatementId and Object mapping for the execute query...
			String[] recPaymentStmntId={"setRecurringTransaction","setBatch","setpayTransaction"};
			Object[] recPaymentTOobj={recTxnParentTO,currentTxnBatchTO,currentTxnPaymentTO};
			Boolean isTxnCommitReq = Boolean.TRUE;
			String statusFlagVal = Integer.toString(statusFlag);
			Date txnExpiryDate = new Date();
			Object txnEnrichmentDtl = new Object();

			//Type casting the ToObject instances ...
			TxnParentTO objTxnParentto = (TxnParentTO)recPaymentTOobj[0];
			DsBatchTO objDsbatchto = (DsBatchTO)recPaymentTOobj[1];
			DsPayTxnsTO dsPayTxnsTO = (DsPayTxnsTO)recPaymentTOobj[2];
			for (int i= 0; i< recPaymentTOobj.length;i++)  
			{
				if(recPaymentTOobj[i] instanceof com.tcs.ebw.payments.transferobject.TxnParentTO){
					objTxnParentto.setStatus("1");
					objTxnParentto.setCreate_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
					objTxnParentto.setInitiation_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
					objTxnParentto.setAccum_amt(new BigDecimal(0D));
					objTxnParentto.setAccum_txn_no(new Double(0D));
				}
				if(recPaymentTOobj[i] instanceof com.tcs.ebw.payments.transferobject.DsBatchTO){
					objDsbatchto.setBatstatus(statusFlagVal);
				}
				if(recPaymentTOobj[i] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsTO){
					//Following method sets the DB TO Objects with the external Interfaces response if required...
					setExternalSrvcResponseDetails((DsPayTxnsTO)recPaymentTOobj[i],txnDetails);

					((DsPayTxnsTO)recPaymentTOobj[i]).setPaybatchref(objDsbatchto.getBatbatchref());
					((DsPayTxnsTO)recPaymentTOobj[i]).setPayccsstatuscode(statusFlagVal);
					((DsPayTxnsTO)recPaymentTOobj[i]).setPayrecurring("Y");
					((DsPayTxnsTO)recPaymentTOobj[i]).setTrial_depo("N");
					((DsPayTxnsTO)recPaymentTOobj[i]).setPar_txn_no(objTxnParentto.getPar_txn_no());
					if(statusFlagVal.equalsIgnoreCase("4")){
						TxnProperties.calculateTxnSettleDate(txnDetails,(DsPayTxnsTO)recPaymentTOobj[i],serviceContext);
						((DsPayTxnsTO)recPaymentTOobj[i]).setSent_to_paymnt("Y");
					}
					else {
						((DsPayTxnsTO)recPaymentTOobj[i]).setSent_to_paymnt("N");
						((DsPayTxnsTO)recPaymentTOobj[i]).setActual_exe_dt(null);
						if(statusFlagVal.equalsIgnoreCase("2") || statusFlagVal.equalsIgnoreCase("80")){
							txnExpiryDate = MSCommonUtils.calculateTxnExpiryDate(dsPayTxnsTO.getRequested_exe_dt(),transferType,Boolean.TRUE,serviceContext);
							((DsPayTxnsTO)recPaymentTOobj[i]).setPaytxneprydate(ConvertionUtil.convertToTimestamp(txnExpiryDate));
						}
					}
					//Setting the payment detail TO Object ....
					txnEnrichmentDtl=(DsPayTxnsTO)recPaymentTOobj[i];
				}
				EBWLogger.logDebug(this,"Executing Query :"+recPaymentStmntId[i]);
				EBWLogger.logDebug(this,"Input Parameters mapped"+recPaymentTOobj[i]);
				execute(recPaymentStmntId[i],recPaymentTOobj[i],isTxnCommitReq);
				EBWLogger.logDebug(this,"Execution Completed.... "+recPaymentStmntId[i]);
			} 

			//Updating the TXN_PARENT table with the Parent Confirmation Number of the Recurring transaction instruction only during the First create instance... (CR 51)
			SetRecTxnParConfNo objSetRecTxnParConfNo = new SetRecTxnParConfNo();
			objSetRecTxnParConfNo.setConnection(serviceConnection);
			objSetRecTxnParConfNo.setRecParConfNumber(objTxnParentto,dsPayTxnsTO);

			//Populate Transaction OutDetails in the PaymentDetails TO..
			populatePaymentDetails(txnDetails,currentTxnBatchTO,currentTxnPaymentTO,objTxnParentto);

			//Inserting into the Transaction enrich Table after making entry into the BANCS DB for each MS Account....
			TransactionEnrichment objTransactionEnrichment = new TransactionEnrichment();
			objTransactionEnrichment.setConnection(serviceConnection);
			objTransactionEnrichment.setTransactionEnrichment(txnEnrichmentDtl,serviceContext);
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
	 * The method maps the payment details to the transfer object and finally 
	 * executes the query for the data modification in the (TXN_PARENT,DS_BATCH,DS_PAY_TXNS)table .... 
	 * @param statusFlag
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 * @throws SQLException
	 */
	public void modifyCreateRecurringPayDetails(int statusFlag,HashMap txnDetails,ServiceContext serviceContext) throws Exception,SQLException 
	{
		EBWLogger.trace(this, "Setting the input parameters for the Modify recurring transfers .... ");
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

			//Payment account details...
			String transfer_duration = ConvertionUtil.convertToString(objPaymentDetails.getFrequency_DurationValue());

			//MS User attributes..
			String userId=objMSUserDetails.getLoginId();
			String userName=MSCommonUtils.extractCurrentUserName(objMSUserDetails);
			String transferType = objPaymentDetails.getTransfer_Type();

			//Mapping the TXN_PARENT Transfer object for the current transaction.
			TxnParentTO recTxnParentTO = new TxnParentTO();
			recTxnParentTO.setPar_txn_no(ConvertionUtil.convertToDouble(objPaymentDetails.getRecParentTxnId()));
			recTxnParentTO.setAmount(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			recTxnParentTO.setFrequency(ConvertionUtil.convertToString(objPaymentDetails.getFrequency_DurationDesc()));
			recTxnParentTO.setDuration(ConvertionUtil.convertToString(objPaymentDetails.getFrequency_DurationValue()));
			recTxnParentTO.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			recTxnParentTO.setModifiedby(userId);
			if(transfer_duration.equalsIgnoreCase("1")){
				recTxnParentTO.setUntildate(null);
				recTxnParentTO.setThrshold_amt(null);
				recTxnParentTO.setMax_txn_no(null);
			}
			else if(transfer_duration.equalsIgnoreCase("2")){
				recTxnParentTO.setUntildate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getDuration_EndDate()));
				recTxnParentTO.setThrshold_amt(null);
				recTxnParentTO.setMax_txn_no(null);
			}
			else if(transfer_duration.equalsIgnoreCase("3")){
				recTxnParentTO.setUntildate(null);
				recTxnParentTO.setThrshold_amt(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit()));
				recTxnParentTO.setMax_txn_no(null);
			}
			else if(transfer_duration.equalsIgnoreCase("4")){
				recTxnParentTO.setUntildate(null);
				recTxnParentTO.setThrshold_amt(null);
				recTxnParentTO.setMax_txn_no(ConvertionUtil.convertToDouble(objPaymentDetails.getDuration_NoOfTransfers()));
			}

			//Mapping the DS_BATCH Transfer object for the current transaction.
			DsBatchTO currentTxnBatchTO = new DsBatchTO();
			currentTxnBatchTO.setBatbatchref(ConvertionUtil.convertToString(objPaymentDetails.getTxnBatchId()));
			currentTxnBatchTO.setBatmodifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnBatchTO.setBatmodifiedby(userId);

			//Mapping the DS_PAY_TXNS Transfer object for the current transaction.
			DsPayTxnsTO currentTxnPaymentTO = new DsPayTxnsTO();
			currentTxnPaymentTO.setPaypayref(ConvertionUtil.convertToString(objPaymentDetails.getTransactionId()));
			currentTxnPaymentTO.setPaybatchref(ConvertionUtil.convertToString(objPaymentDetails.getTxnBatchId()));
			currentTxnPaymentTO.setPaydebitamt(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			currentTxnPaymentTO.setPaypymtdate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			currentTxnPaymentTO.setPayfrequency(ConvertionUtil.convertToString(objPaymentDetails.getFrequency_DurationDesc()));
			currentTxnPaymentTO.setPayfreqpaymentcount(ConvertionUtil.convertToDouble(objPaymentDetails.getDuration_NoOfTransfers()));
			currentTxnPaymentTO.setPayfreqenddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getDuration_EndDate()));
			currentTxnPaymentTO.setPayfreqlimit(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit()));
			currentTxnPaymentTO.setAuth_mode(null);
			currentTxnPaymentTO.setAuth_info_reciever(null);
			currentTxnPaymentTO.setVerbal_auth_client_name(null);
			currentTxnPaymentTO.setVerbal_auth_date(null);
			currentTxnPaymentTO.setVerbal_auth_time(null);
			currentTxnPaymentTO.setSame_name_flag(BRUtils.getSameNameFlag(txnDetails));
			currentTxnPaymentTO.setClient_verification_desc(null);
			currentTxnPaymentTO.setAuth_confirmed_by(null);
			currentTxnPaymentTO.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnPaymentTO.setRequested_exe_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			currentTxnPaymentTO.setModifiedby(userId);
			currentTxnPaymentTO.setModifiedby_name(userName);
			currentTxnPaymentTO.setCurrent_owner_id(objPaymentDetails.getCurrent_owner_id());
			currentTxnPaymentTO.setCurrent_owner_role(objPaymentDetails.getCurrent_owner_role());
			currentTxnPaymentTO.setLife_user_id(objMSUserDetails.getUuid());

			//StatementId and Object mapping for the execute query...
			String[] recPaymentStmntId={"modifyBatch","modifypayTransaction","modifyRecurringTransaction"};
			Object[] recPaymentTOobj={currentTxnBatchTO,currentTxnPaymentTO,recTxnParentTO};
			Boolean isTxnCommitReq = Boolean.TRUE;
			String statusFlagVal = Integer.toString(statusFlag);
			Date txnExpiryDate = new Date();

			//Type casting the ToObject instances ...
			DsBatchTO objDsbatchto = (DsBatchTO)recPaymentTOobj[0];
			DsPayTxnsTO dsPayTxnsTO = (DsPayTxnsTO)recPaymentTOobj[1];
			for (int i= 0; i< recPaymentTOobj.length;i++)  {
				if(recPaymentTOobj[i] instanceof com.tcs.ebw.payments.transferobject.DsBatchTO)
				{
					objDsbatchto.setBatstatus(statusFlagVal);

					//Checking the Transaction Batch version before modifying the transactions...
					EBWLogger.logDebug(this, "Checking the Transaction Record Version before modifying the transactions...");
					VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
					String versionType = "BATVER";
					objVerMismatch.setConnection(serviceConnection);
					objVerMismatch.compareVersionNum(txnDetails,versionType, serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return; 
					}
				}
				if(recPaymentTOobj[i] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsTO)
				{
					//Following method sets the DB TO Objects with the external Interfaces response if required...
					setExternalSrvcResponseDetails((DsPayTxnsTO)recPaymentTOobj[i],txnDetails);

					((DsPayTxnsTO)recPaymentTOobj[i]).setPayccsstatuscode(statusFlagVal);
					if(statusFlagVal.equalsIgnoreCase("4")){
						TxnProperties.calculateTxnSettleDate(txnDetails,(DsPayTxnsTO)recPaymentTOobj[i],serviceContext);
						((DsPayTxnsTO)recPaymentTOobj[i]).setSent_to_paymnt("Y");
					}
					else {
						((DsPayTxnsTO)recPaymentTOobj[i]).setSent_to_paymnt("N");
						((DsPayTxnsTO)recPaymentTOobj[i]).setActual_exe_dt(null);
						if(statusFlagVal.equalsIgnoreCase("2") || statusFlagVal.equalsIgnoreCase("80")){
							txnExpiryDate = MSCommonUtils.calculateTxnExpiryDate(dsPayTxnsTO.getRequested_exe_dt(),transferType,Boolean.TRUE,serviceContext);
							((DsPayTxnsTO)recPaymentTOobj[i]).setPaytxneprydate(ConvertionUtil.convertToTimestamp(txnExpiryDate));
						}
					}

					//Checking the Transaction Record version before modifying the transactions...
					EBWLogger.logDebug(this, "Checking the Transaction Batch Version before modifying the transactions...");
					VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
					String versionType = "TXNVER";
					objVerMismatch.setConnection(serviceConnection);
					objVerMismatch.compareVersionNum(txnDetails,versionType,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return; 
					}
				}
				if(recPaymentTOobj[i] instanceof com.tcs.ebw.payments.transferobject.TxnParentTO)
				{
					//Checking the Transaction Parent version before modifying the transactions...
					EBWLogger.logDebug(this, "Checking the Recurring Transaction Parent Version before modifying the transactions...");
					VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
					String versionType = "RECVER";
					objVerMismatch.setConnection(serviceConnection);
					objVerMismatch.compareVersionNum(txnDetails,versionType,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return; 
					}
				}
				EBWLogger.logDebug(this,"Executing Query :"+recPaymentStmntId[i]);
				EBWLogger.logDebug(this,"Input Parameters mapped"+recPaymentTOobj[i]);
				execute(recPaymentStmntId[i], recPaymentTOobj[i], isTxnCommitReq);
				EBWLogger.logDebug(this,"Execution Completed.... "+recPaymentStmntId[i]);
			} 

			//Updating the TXN_PARENT table with the parent txn date only in case the screen date is modified , else no....
			UpdateRecTxnParentDate objUpdateRecTxnParentDate = new UpdateRecTxnParentDate();
			objUpdateRecTxnParentDate.setConnection(serviceConnection);
			objUpdateRecTxnParentDate.setRecParTxnDate(txnDetails,recTxnParentTO);

			//Populate Transaction OutDetails in the PaymentDetails TO..
			populateEditPaymentDetails(txnDetails,currentTxnBatchTO,currentTxnPaymentTO);
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

	/** Following function is used to set the external services response details in the BR Service
	 * 
	 * @param toObjects
	 * @param externalSrvResponse
	 */
	public static void setExternalSrvcResponseDetails(DsPayTxnsTO dsPayTxnsTO,HashMap txnDetails)
	{
		String fromAccTier = "";
		String toAccTier="";
		String rsa_review_flag="N";
		String next_approver_role = null;
		String dont_spawn_flg = "N";
		String ofac_case_id = null;

		//Account View Response Mappings ..
		if(txnDetails.containsKey("MerlinOutputDetails"))
		{
			AccountDetails objaccDetails= (AccountDetails)txnDetails.get("MerlinOutputDetails");
			ArrayList merlinAccDetails = objaccDetails.getMerlinOutResponse(); 
			for(int j=0;j<merlinAccDetails.size();j++){
				if(merlinAccDetails.get(j) instanceof MerlinOutResponse){
					MerlinOutResponse accMerlinResponse =(MerlinOutResponse)merlinAccDetails.get(j);
					if(accMerlinResponse.getAccountNumber().equalsIgnoreCase(dsPayTxnsTO.getPaydebitaccnum())){
						if(dsPayTxnsTO.getTxn_type().equalsIgnoreCase(TxnTypeCode.INTERNAL) || dsPayTxnsTO.getTxn_type().equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
							fromAccTier=accMerlinResponse.getCatTier();
						}
						else if(dsPayTxnsTO.getTxn_type().equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
							fromAccTier=accMerlinResponse.getCatTier();
						}
						else if(dsPayTxnsTO.getTxn_type().equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
							toAccTier=accMerlinResponse.getCatTier();
						}
					}
					else if(accMerlinResponse.getAccountNumber().equalsIgnoreCase(dsPayTxnsTO.getPaypayeeaccnum())){
						if(dsPayTxnsTO.getTxn_type().equalsIgnoreCase(TxnTypeCode.INTERNAL)){
							toAccTier=accMerlinResponse.getCatTier();
						}
					}
				}
			}
			dsPayTxnsTO.setFromacct_tier(fromAccTier);
			dsPayTxnsTO.setToacct_tier(toAccTier);
		}

		//Mapping from the Business Rule response
		if(txnDetails.containsKey("BROutputDetails"))
		{
			BusinessRulesService objBussRulesService= (BusinessRulesService)txnDetails.get("BROutputDetails");
			//RSA_Review flag mapping...
			if(objBussRulesService.getRsa_Review_Flag()!=null && !objBussRulesService.getRsa_Review_Flag().trim().equalsIgnoreCase("")){
				rsa_review_flag = objBussRulesService.getRsa_Review_Flag();
			}

			//Next_approver_role mapping...
			if(objBussRulesService.getNext_approver_role()!=null && !objBussRulesService.getNext_approver_role().trim().equalsIgnoreCase("")){
				next_approver_role = objBussRulesService.getNext_approver_role();
			}

			//Dont_spawn flag mapping..
			if(objBussRulesService.getDont_spawn_flag()!=null && !objBussRulesService.getDont_spawn_flag().trim().equalsIgnoreCase("")){
				dont_spawn_flg = objBussRulesService.getDont_spawn_flag();
			}

			//OFAC Case Id..
			if(objBussRulesService.getOfac_case_id()!=null && !objBussRulesService.getOfac_case_id().trim().equalsIgnoreCase("")){
				ofac_case_id = objBussRulesService.getOfac_case_id();
			}

			dsPayTxnsTO.setDont_spawn_flag(dont_spawn_flg);
			dsPayTxnsTO.setRsa_review_flag(rsa_review_flag);
			dsPayTxnsTO.setNext_approver_role(next_approver_role);
			dsPayTxnsTO.setOfac_case_id(ofac_case_id);
		}

		//Mapping the Confirmation Number , the same as that passed for the FTM BR invoke will be present only during the transfer initiation...
		if(txnDetails.containsKey("PaymentDetails")){
			PaymentDetailsTO objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			String clientTxnID = objPaymentDetails.getTransactionId();
			if(clientTxnID!=null && !clientTxnID.trim().equalsIgnoreCase("")){
				dsPayTxnsTO.setPaypayref(clientTxnID);
			}
		}
	}

	/**
	 * Populate Create payment transaction details ...
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void populatePaymentDetails(HashMap txnDetails,DsBatchTO currentTxnBatchTO,DsPayTxnsTO currentTxnPaymentTO,TxnParentTO txnParentTO) throws Exception
	{	
		try 
		{
			//Mapping the payment detail object with the confirmation number for FTM Call...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Putting the Transaction Object details in the HashMap only during create transfers case..
			txnDetails.put("Current_DsBatchObject", currentTxnBatchTO); //DS_BATCH Object.
			txnDetails.put("Current_DsPayTxnObject", currentTxnPaymentTO); //DS_PAY_TXNS Object.

			//Setting the payment attributes...
			objPaymentDetails.setTxnBatchId(currentTxnBatchTO.getBatbatchref());
			objPaymentDetails.setTransactionId(currentTxnPaymentTO.getPaypayref());
			objPaymentDetails.setRecParentTxnId(ConvertionUtil.convertToString(currentTxnPaymentTO.getPar_txn_no()));
			objPaymentDetails.setTxnCurrentStatusCode(currentTxnPaymentTO.getPayccsstatuscode());
			objPaymentDetails.setActualExecDate(ConvertionUtil.convertToString(currentTxnPaymentTO.getActual_exe_dt()));
			objPaymentDetails.setCurrent_approver_role(currentTxnPaymentTO.getNext_approver_role());
			objPaymentDetails.setTxnExpiryDate(ConvertionUtil.convertToString(currentTxnPaymentTO.getPaytxneprydate()));
			objPaymentDetails.setParTxnRequestDate(txnParentTO.getPar_txn_request_dt());
			objPaymentDetails.setSame_name_flag(currentTxnPaymentTO.getSame_name_flag());
			objPaymentDetails.setDont_spawn_flag(currentTxnPaymentTO.getDont_spawn_flag());
			objPaymentDetails.setOfac_case_id(currentTxnPaymentTO.getOfac_case_id());
		} 
		catch (Exception exception){
			throw exception;
		}
	}

	/**
	 * Populate edit transaction details ...
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void populateEditPaymentDetails(HashMap txnDetails,DsBatchTO currentTxnBatchTO,DsPayTxnsTO currentTxnPaymentTO) throws Exception
	{	
		try 
		{
			//Mapping the payment detail object with the confirmation number for FTM Call...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Updating the DS_BATCH Object to be passed to HUB...
			DsBatchTO modifyTxnBatchTO = new DsBatchTO();
			if(txnDetails.containsKey("Current_DsBatchObject")){
				modifyTxnBatchTO = (DsBatchTO)txnDetails.get("Current_DsBatchObject");
			}
			modifyTxnBatchTO.setBatmodifieddate(currentTxnBatchTO.getBatmodifieddate());
			modifyTxnBatchTO.setBatmodifiedby(currentTxnBatchTO.getBatmodifiedby());

			//Updating the DS_PAY_TXNS Object to be passed to HUB...
			DsPayTxnsTO modifyTxnPaymentTO = new DsPayTxnsTO();
			if(txnDetails.containsKey("Current_DsPayTxnObject")){
				modifyTxnPaymentTO = (DsPayTxnsTO)txnDetails.get("Current_DsPayTxnObject");
			}
			modifyTxnPaymentTO.setPaydebitamt(currentTxnPaymentTO.getPaydebitamt());
			modifyTxnPaymentTO.setPaypymtdate(currentTxnPaymentTO.getPaypymtdate());
			modifyTxnPaymentTO.setPayfrequency(currentTxnPaymentTO.getPayfrequency());
			modifyTxnPaymentTO.setPayfreqpaymentcount(currentTxnPaymentTO.getPayfreqpaymentcount());
			modifyTxnPaymentTO.setPayfreqenddate(currentTxnPaymentTO.getPayfreqenddate());
			modifyTxnPaymentTO.setPayfreqlimit(currentTxnPaymentTO.getPayfreqlimit());
			modifyTxnPaymentTO.setAuth_mode(null);
			modifyTxnPaymentTO.setAuth_info_reciever(null);
			modifyTxnPaymentTO.setVerbal_auth_client_name(null);
			modifyTxnPaymentTO.setVerbal_auth_date(null);
			modifyTxnPaymentTO.setVerbal_auth_time(null);
			modifyTxnPaymentTO.setClient_verification_desc(null);
			modifyTxnPaymentTO.setSame_name_flag(currentTxnPaymentTO.getSame_name_flag());
			modifyTxnPaymentTO.setDont_spawn_flag(currentTxnPaymentTO.getDont_spawn_flag());
			modifyTxnPaymentTO.setAuth_confirmed_by(null);
			modifyTxnPaymentTO.setModifieddate(currentTxnPaymentTO.getModifieddate());
			modifyTxnPaymentTO.setRequested_exe_dt(currentTxnPaymentTO.getRequested_exe_dt());
			modifyTxnPaymentTO.setModifiedby(currentTxnPaymentTO.getModifiedby());
			modifyTxnPaymentTO.setModifiedby_name(currentTxnPaymentTO.getModifiedby_name());
			modifyTxnPaymentTO.setCurrent_owner_id(currentTxnPaymentTO.getCurrent_owner_id());
			modifyTxnPaymentTO.setCurrent_owner_role(currentTxnPaymentTO.getCurrent_owner_role());
			modifyTxnPaymentTO.setLife_user_id(currentTxnPaymentTO.getLife_user_id());
			modifyTxnPaymentTO.setActual_exe_dt(currentTxnPaymentTO.getActual_exe_dt());
			modifyTxnPaymentTO.setPayabpaysendflag(currentTxnPaymentTO.getPayabpaysendflag());
			modifyTxnPaymentTO.setOfac_case_id(currentTxnPaymentTO.getOfac_case_id());

			//Setting the payment attributes...
			objPaymentDetails.setTxnCurrentStatusCode(currentTxnPaymentTO.getPayccsstatuscode());
			objPaymentDetails.setActualExecDate(ConvertionUtil.convertToString(currentTxnPaymentTO.getActual_exe_dt()));
			objPaymentDetails.setCurrent_approver_role(currentTxnPaymentTO.getNext_approver_role());
			objPaymentDetails.setTxnExpiryDate(ConvertionUtil.convertToString(currentTxnPaymentTO.getPaytxneprydate()));
			objPaymentDetails.setSame_name_flag(currentTxnPaymentTO.getSame_name_flag());
			objPaymentDetails.setDont_spawn_flag(currentTxnPaymentTO.getDont_spawn_flag());
			objPaymentDetails.setAuth_mode(null);
			objPaymentDetails.setAuth_info_reciever(null);
			objPaymentDetails.setVerbal_auth_client_name(null);
			objPaymentDetails.setVerbal_auth_date(null);
			objPaymentDetails.setVerbal_auth_time(null);
			objPaymentDetails.setClient_verification_desc(null);
			objPaymentDetails.setAuth_confirmed_by(null);
			objPaymentDetails.setOfac_case_id(currentTxnPaymentTO.getOfac_case_id());
		} 
		catch (Exception exception){
			throw exception;
		}
	}
}


