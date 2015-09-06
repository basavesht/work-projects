package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.sql.SQLException;

import com.tcs.Payments.ms360Utils.BRUtils;
import com.tcs.Payments.ms360Utils.ChkReqConstants;
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
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 *    224703       01-07-2011        3               CR 262
 *    224703       29-08-2011        3-B             CR 193
 *    224703       23-09-2011        P3-B            PLA  
 * **********************************************************
 */
public class CreatePaymentService extends DatabaseService {

	public CreatePaymentService() {

	}

	/**
	 * The method maps the payment details to the transfer object and finally executes the 
	 * query for the data insertion in the (DS_PAY_TXNS and DS_BATCH) table...
	 * @param statusFlag
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 * @throws SQLException
	 */
	public void setCreatePaymentDetails(int statusFlag,HashMap txnDetails,ServiceContext serviceContext) throws Exception,SQLException 
	{
		EBWLogger.trace(this, "Setting the Create payments input details...");
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

			//MS User attributes..
			String userId= objMSUserDetails.getRcafId();
			String lifeTimeUserId= objPaymentDetails.getLife_user_id();
			String userName=MSCommonUtils.extractCurrentUserName(objMSUserDetails);
			String userRole=objMSUserDetails.getInitiatorRole();
			String transferType = objPaymentDetails.getTransfer_Type();

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
			currentTxnPaymentTO.setPaypayeename1(objExternalAccDetails.getCpypayeename1());
			currentTxnPaymentTO.setPaypayeebankcode(objExternalAccDetails.getCpybankcode());
			currentTxnPaymentTO.setPaypayeeadd1(objExternalAccDetails.getCpyadd1());
			currentTxnPaymentTO.setPaypayeeadd2(objExternalAccDetails.getCpyadd2());
			currentTxnPaymentTO.setPaypayeeadd3(objExternalAccDetails.getCpyadd3());
			currentTxnPaymentTO.setPaypayee_acct_type(objExternalAccDetails.getCpyacctype());
			currentTxnPaymentTO.setPaydisctype(objExternalAccDetails.getSign_up_mode());  //Stores the Sign up mode for the external account..
			currentTxnPaymentTO.setPaypayeeid(objExternalAccDetails.getCpyaccnum());
			currentTxnPaymentTO.setPayfrequency(objPaymentDetails.getFrequency_DurationDesc());
			currentTxnPaymentTO.setPayfreqpaymentcount(ConvertionUtil.convertToDouble(objPaymentDetails.getDuration_NoOfTransfers()));
			currentTxnPaymentTO.setPayfreqenddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getDuration_EndDate()));
			currentTxnPaymentTO.setPayfreqlimit(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit()));
			currentTxnPaymentTO.setCreateddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnPaymentTO.setRequested_exe_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			currentTxnPaymentTO.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnPaymentTO.setTxn_type(objPaymentDetails.getTransfer_Type());
			currentTxnPaymentTO.setKeyaccountnumber_from(objFromMSAcc_Details.getKeyAccount());
			currentTxnPaymentTO.setFriendlyname_from(null);
			currentTxnPaymentTO.setNickname_from(null);
			currentTxnPaymentTO.setFromfa_id(ConvertionUtil.convertToDouble(objFromMSAcc_Details.getFaNumber()));
			currentTxnPaymentTO.setFromfa_id_hist(null);
			currentTxnPaymentTO.setFrombranch_id(ConvertionUtil.convertToDouble(objFromMSAcc_Details.getOfficeNumber()));
			currentTxnPaymentTO.setFrombranch_id_hist(null);
			currentTxnPaymentTO.setFromacct_tier(null);
			currentTxnPaymentTO.setFromacct_no_hist(null);
			currentTxnPaymentTO.setFrombr_acct_no_fa(from_MSAccountNumber);
			currentTxnPaymentTO.setFromacct_type1("Cash");
			currentTxnPaymentTO.setKeyaccountnumber_to(objToMSAcc_Details.getKeyAccount());
			currentTxnPaymentTO.setFriendlyname_to(null);
			currentTxnPaymentTO.setNickname_to(null);
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
			currentTxnPaymentTO.setCreatedby_name(userName);
			currentTxnPaymentTO.setModifiedby_name(userName);
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
			currentTxnPaymentTO.setAuth_mode(objPaymentDetails.getAuth_mode());
			currentTxnPaymentTO.setAuth_info_reciever(objPaymentDetails.getAuth_info_reciever());
			currentTxnPaymentTO.setVerbal_auth_client_name(MSCommonUtils.extractSpokeTo(txnDetails,objPaymentDetails.getVerbal_auth_client_name()));
			currentTxnPaymentTO.setVerbal_auth_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getVerbal_auth_date()));
			currentTxnPaymentTO.setVerbal_auth_time(objPaymentDetails.getVerbal_auth_time());
			currentTxnPaymentTO.setClient_verification_desc(objPaymentDetails.getClient_verification_desc());
			currentTxnPaymentTO.setSame_name_flag(BRUtils.getSameNameFlag(txnDetails));
			currentTxnPaymentTO.setRetirement_mnemonic(objPaymentDetails.getRetirement_mnemonic());
			currentTxnPaymentTO.setQualifier(objPaymentDetails.getQualifier());
			currentTxnPaymentTO.setReverse_qualifier(objPaymentDetails.getReverse_qualifier());
			currentTxnPaymentTO.setDisplay_qualifier(objPaymentDetails.getDisplay_qualifier());
			currentTxnPaymentTO.setAuth_confirmed_by(userName);

			//StatementId and Object mapping for the execute query...
			String[] createPaymentStmntId={"setBatch","setpayTransaction"};
			Object[] createPaymentTOObj={currentTxnBatchTO,currentTxnPaymentTO};
			Boolean isTxnCommitReq = Boolean.TRUE;
			String statusFlagVal = Integer.toString(statusFlag);
			Date txnExpiryDate = new Date();
			Object txnEnrichmentDtl = new Object();

			//Type casting the ToObject instances ...
			DsBatchTO dsbatchto = (DsBatchTO)createPaymentTOObj[0];
			DsPayTxnsTO dsPayTxnsTO = (DsPayTxnsTO)createPaymentTOObj[1];
			for (int i= 0; i< createPaymentTOObj.length;i++)
			{
				if(createPaymentTOObj[i] instanceof com.tcs.ebw.payments.transferobject.DsBatchTO){
					dsbatchto.setBatstatus(statusFlagVal);
				}
				if(createPaymentTOObj[i] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsTO){
					//Following method sets the DB TO Objects with the external Interfaces response...
					setExternalSrvcResponseDetails((DsPayTxnsTO)createPaymentTOObj[i],txnDetails);

					((DsPayTxnsTO)createPaymentTOObj[i]).setPaybatchref(dsbatchto.getBatbatchref());
					((DsPayTxnsTO)createPaymentTOObj[i]).setPayccsstatuscode(statusFlagVal);
					((DsPayTxnsTO)createPaymentTOObj[i]).setPayrecurring("N");
					((DsPayTxnsTO)createPaymentTOObj[i]).setTrial_depo("N");
					if(statusFlagVal.equalsIgnoreCase("4")){
						TxnProperties.calculateTxnSettleDate(txnDetails,(DsPayTxnsTO)createPaymentTOObj[i],serviceContext);
						((DsPayTxnsTO)createPaymentTOObj[i]).setSent_to_paymnt("Y");
					}
					else {
						((DsPayTxnsTO)createPaymentTOObj[i]).setSent_to_paymnt("N");
						((DsPayTxnsTO)createPaymentTOObj[i]).setActual_exe_dt(null);
						if(statusFlagVal.equalsIgnoreCase("2") || statusFlagVal.equalsIgnoreCase("80") || statusFlagVal.equalsIgnoreCase("103")){
							txnExpiryDate = MSCommonUtils.calculateTxnExpiryDate(dsPayTxnsTO.getRequested_exe_dt(),transferType,serviceContext);
							((DsPayTxnsTO)createPaymentTOObj[i]).setPaytxneprydate(ConvertionUtil.convertToTimestamp(txnExpiryDate));
						}
					}

					//Setting the payment detail TO Object ....
					txnEnrichmentDtl=(DsPayTxnsTO)createPaymentTOObj[i];
				}
				EBWLogger.logDebug(this,"Executing Query : "+createPaymentStmntId[i]);
				EBWLogger.logDebug(this,"Input Parameters mapped"+createPaymentTOObj[i]);
				execute(createPaymentStmntId[i],createPaymentTOObj[i],isTxnCommitReq);
				EBWLogger.logDebug(this,"Execution Completed.... "+createPaymentStmntId[i]);
			} 

			//Populate Transaction OutDetails in the PaymentDetails TO..
			populatePaymentDetails(txnDetails,currentTxnBatchTO,currentTxnPaymentTO);

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
	 * executes the query for the data modification in the (DS_PAY_TXNS and DS_BATCH)table .... 
	 * @param stmntId
	 * @param statusFlag
	 * @param busineesDate
	 * @param toObjects
	 * @param boolean1
	 * @param serviceContext
	 * @throws Exception
	 * @throws SQLException
	 */
	public void modifyCreatePaymentDetails(int statusFlag,HashMap txnDetails,ServiceContext serviceContext) throws Exception,SQLException 
	{
		EBWLogger.trace(this, "Setting the Modify Input details..");
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

			//MS User attributes..
			String userId= objMSUserDetails.getRcafId();
			String userName=MSCommonUtils.extractCurrentUserName(objMSUserDetails);
			String transferType = objPaymentDetails.getTransfer_Type();

			DsBatchTO currentTxnBatchTO = new DsBatchTO();
			currentTxnBatchTO.setBatbatchref(ConvertionUtil.convertToString(objPaymentDetails.getTxnBatchId()));
			currentTxnBatchTO.setBatmodifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnBatchTO.setBatmodifiedby(userId);

			DsPayTxnsTO currentTxnPaymentTO = new DsPayTxnsTO();
			currentTxnPaymentTO.setPaypayref(ConvertionUtil.convertToString(objPaymentDetails.getTransactionId()));
			currentTxnPaymentTO.setPaybatchref(ConvertionUtil.convertToString(objPaymentDetails.getTxnBatchId()));
			currentTxnPaymentTO.setPaydebitamt(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			currentTxnPaymentTO.setPaypymtdate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			currentTxnPaymentTO.setPayfrequency(ConvertionUtil.convertToString(objPaymentDetails.getFrequency_DurationDesc()));
			currentTxnPaymentTO.setPayfreqpaymentcount(ConvertionUtil.convertToDouble(objPaymentDetails.getDuration_NoOfTransfers()));
			currentTxnPaymentTO.setPayfreqenddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getDuration_EndDate()));
			currentTxnPaymentTO.setPayfreqlimit(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit()));
			currentTxnPaymentTO.setAuth_mode(objPaymentDetails.getAuth_mode());
			currentTxnPaymentTO.setAuth_info_reciever(objPaymentDetails.getAuth_info_reciever());
			currentTxnPaymentTO.setVerbal_auth_client_name(MSCommonUtils.extractSpokeTo(txnDetails,objPaymentDetails.getVerbal_auth_client_name()));
			currentTxnPaymentTO.setVerbal_auth_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getVerbal_auth_date()));
			currentTxnPaymentTO.setVerbal_auth_time(objPaymentDetails.getVerbal_auth_time());
			currentTxnPaymentTO.setClient_verification_desc(objPaymentDetails.getClient_verification_desc());
			currentTxnPaymentTO.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnPaymentTO.setRequested_exe_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			currentTxnPaymentTO.setModifiedby(userId);
			currentTxnPaymentTO.setModifiedby_name(userName);
			currentTxnPaymentTO.setCurrent_owner_id(objPaymentDetails.getCurrent_owner_id());
			currentTxnPaymentTO.setCurrent_owner_role(objPaymentDetails.getCurrent_owner_role());
			currentTxnPaymentTO.setSame_name_flag(BRUtils.getSameNameFlag(txnDetails));
			currentTxnPaymentTO.setAuth_confirmed_by(userName);

			//StatementId and Object mapping for the execute query...
			String[] createPaymentStmntId={"modifyBatch","modifypayTransaction"};
			Object[] createPaymentTOObj={currentTxnBatchTO,currentTxnPaymentTO};
			Boolean isTxnCommitReq = Boolean.TRUE;
			String statusFlagVal = Integer.toString(statusFlag);
			Date txnExpiryDate = new Date();

			//Type casting the ToObject instances ...
			DsBatchTO dsbatchto = (DsBatchTO)createPaymentTOObj[0];
			DsPayTxnsTO dsPayTxnsTO = (DsPayTxnsTO)createPaymentTOObj[1];
			for (int i= 0; i< createPaymentTOObj.length;i++){
				if(createPaymentTOObj[i] instanceof com.tcs.ebw.payments.transferobject.DsBatchTO)
				{
					dsbatchto.setBatstatus(statusFlagVal);

					//Checking the Transaction Batch version before modifying the transactions...
					EBWLogger.logDebug(this, "Checking the Transaction Batch Version before modifying the transactions...");
					VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
					String versionType = "BATVER";
					objVerMismatch.setConnection(serviceConnection);
					objVerMismatch.compareVersionNum(txnDetails,versionType,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return; 
					}
				}
				if(createPaymentTOObj[i] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsTO)
				{
					//Following method sets the DB TO Objects with the external Interfaces response...
					setExternalSrvcResponseDetails((DsPayTxnsTO)createPaymentTOObj[i],txnDetails);

					((DsPayTxnsTO)createPaymentTOObj[i]).setPayccsstatuscode(statusFlagVal);
					if(statusFlagVal.equalsIgnoreCase("4")){
						TxnProperties.calculateTxnSettleDate(txnDetails,(DsPayTxnsTO)createPaymentTOObj[i],serviceContext);
						((DsPayTxnsTO)createPaymentTOObj[i]).setSent_to_paymnt("Y");
					}
					else {
						((DsPayTxnsTO)createPaymentTOObj[i]).setSent_to_paymnt("N");
						((DsPayTxnsTO)createPaymentTOObj[i]).setActual_exe_dt(null);
						if(statusFlagVal.equalsIgnoreCase("2") || statusFlagVal.equalsIgnoreCase("80") || statusFlagVal.equalsIgnoreCase("103")){
							txnExpiryDate = MSCommonUtils.calculateTxnExpiryDate(dsPayTxnsTO.getRequested_exe_dt(),transferType,serviceContext);
							((DsPayTxnsTO)createPaymentTOObj[i]).setPaytxneprydate(ConvertionUtil.convertToTimestamp(txnExpiryDate));
						}
					}

					//Checking the Transaction Batch version before modifying the transactions...
					EBWLogger.logDebug(this, "Checking the Transaction Batch Version before modifying the transactions...");
					VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
					String versionType = "TXNVER";
					objVerMismatch.setConnection(serviceConnection);
					objVerMismatch.compareVersionNum(txnDetails,versionType,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return; 
					}
				}
				EBWLogger.logDebug(this,"Executing Query :"+createPaymentStmntId[i]);
				EBWLogger.logDebug(this,"Input Parameters mapped"+createPaymentTOObj[i]);
				execute(createPaymentStmntId[i], createPaymentTOObj[i],isTxnCommitReq);
				EBWLogger.logDebug(this,"Execution Completed.... "+createPaymentStmntId[i]);
			} 

			//Populate Transaction OutDetails in the PaymentDetails TO..
			populateEditPaymentDetails(txnDetails,currentTxnBatchTO,currentTxnPaymentTO);
		}
		catch(SQLException sqlexception) {
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
						if(dsPayTxnsTO.getTxn_type().equalsIgnoreCase(TxnTypeCode.INTERNAL) || dsPayTxnsTO.getTxn_type().startsWith(ChkReqConstants.CHK) || dsPayTxnsTO.getTxn_type().equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
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

			dsPayTxnsTO.setRsa_review_flag(rsa_review_flag);
			dsPayTxnsTO.setNext_approver_role(next_approver_role);
			dsPayTxnsTO.setDont_spawn_flag(dont_spawn_flg);
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
	 * Populate transaction details ...
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void populatePaymentDetails(HashMap txnDetails,DsBatchTO currentTxnBatchTO,DsPayTxnsTO currentTxnPaymentTO) throws Exception
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
			objPaymentDetails.setTxnCurrentStatusCode(currentTxnPaymentTO.getPayccsstatuscode());
			objPaymentDetails.setActualExecDate(ConvertionUtil.convertToAppDateStr(currentTxnPaymentTO.getActual_exe_dt()));
			objPaymentDetails.setSame_name_flag(currentTxnPaymentTO.getSame_name_flag());
			objPaymentDetails.setDont_spawn_flag(currentTxnPaymentTO.getDont_spawn_flag());
			objPaymentDetails.setCreatedDate(ConvertionUtil.convertToAppDateStr(currentTxnPaymentTO.getCreateddate()));
			objPaymentDetails.setCurrent_approver_role(currentTxnPaymentTO.getNext_approver_role());
			objPaymentDetails.setAuth_confirmed_by(currentTxnPaymentTO.getAuth_confirmed_by());
			objPaymentDetails.setVerbal_auth_client_name(currentTxnPaymentTO.getVerbal_auth_client_name());
			objPaymentDetails.setTxnExpiryDate(ConvertionUtil.convertToAppDateStr(currentTxnPaymentTO.getPaytxneprydate()));
			objPaymentDetails.setOfac_case_id(currentTxnPaymentTO.getOfac_case_id());
		} 
		catch (Exception exception){
			throw exception;
		}
	}

	/**
	 * Populate Edit transaction details ...
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

			DsBatchTO modifyTxnBatchTO = new DsBatchTO();
			if(txnDetails.containsKey("Current_DsBatchObject")){
				modifyTxnBatchTO = (DsBatchTO)txnDetails.get("Current_DsBatchObject");
			}
			modifyTxnBatchTO.setBatmodifieddate(currentTxnBatchTO.getBatmodifieddate());
			modifyTxnBatchTO.setBatmodifiedby(currentTxnBatchTO.getBatmodifiedby());

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
			modifyTxnPaymentTO.setAuth_mode(currentTxnPaymentTO.getAuth_mode());
			modifyTxnPaymentTO.setAuth_info_reciever(currentTxnPaymentTO.getAuth_info_reciever());
			modifyTxnPaymentTO.setVerbal_auth_client_name(currentTxnPaymentTO.getVerbal_auth_client_name());
			modifyTxnPaymentTO.setVerbal_auth_date(currentTxnPaymentTO.getVerbal_auth_date());
			modifyTxnPaymentTO.setVerbal_auth_time(currentTxnPaymentTO.getVerbal_auth_time());
			modifyTxnPaymentTO.setClient_verification_desc(currentTxnPaymentTO.getClient_verification_desc());
			modifyTxnPaymentTO.setModifieddate(currentTxnPaymentTO.getModifieddate());
			modifyTxnPaymentTO.setRequested_exe_dt(currentTxnPaymentTO.getRequested_exe_dt());
			modifyTxnPaymentTO.setModifiedby(currentTxnPaymentTO.getModifiedby());
			modifyTxnPaymentTO.setModifiedby_name(currentTxnPaymentTO.getModifiedby_name());
			modifyTxnPaymentTO.setCurrent_owner_id(currentTxnPaymentTO.getCurrent_owner_id());
			modifyTxnPaymentTO.setCurrent_owner_role(currentTxnPaymentTO.getCurrent_owner_role());
			modifyTxnPaymentTO.setSame_name_flag(currentTxnPaymentTO.getSame_name_flag());
			modifyTxnPaymentTO.setDont_spawn_flag(currentTxnPaymentTO.getDont_spawn_flag());
			modifyTxnPaymentTO.setAuth_confirmed_by(currentTxnPaymentTO.getAuth_confirmed_by());
			modifyTxnPaymentTO.setActual_exe_dt(currentTxnPaymentTO.getActual_exe_dt());
			modifyTxnPaymentTO.setPayabpaysendflag(currentTxnPaymentTO.getPayabpaysendflag());
			modifyTxnPaymentTO.setOfac_case_id(currentTxnPaymentTO.getOfac_case_id());

			//Setting the payment attributes...
			objPaymentDetails.setTxnCurrentStatusCode(currentTxnPaymentTO.getPayccsstatuscode());
			objPaymentDetails.setActualExecDate(ConvertionUtil.convertToAppDateStr(currentTxnPaymentTO.getActual_exe_dt()));
			objPaymentDetails.setSame_name_flag(currentTxnPaymentTO.getSame_name_flag());
			objPaymentDetails.setDont_spawn_flag(currentTxnPaymentTO.getDont_spawn_flag());
			objPaymentDetails.setCurrent_approver_role(currentTxnPaymentTO.getNext_approver_role());
			objPaymentDetails.setAuth_confirmed_by(currentTxnPaymentTO.getAuth_confirmed_by());
			objPaymentDetails.setVerbal_auth_client_name(currentTxnPaymentTO.getVerbal_auth_client_name());
			objPaymentDetails.setTxnExpiryDate(ConvertionUtil.convertToAppDateStr(currentTxnPaymentTO.getPaytxneprydate()));
			objPaymentDetails.setOfac_case_id(currentTxnPaymentTO.getOfac_case_id());
		} 
		catch (Exception exception){
			throw exception;
		}
	}
}
