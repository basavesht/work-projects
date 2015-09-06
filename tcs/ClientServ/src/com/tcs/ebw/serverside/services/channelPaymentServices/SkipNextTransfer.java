/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsBatchTO;
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
public class SkipNextTransfer extends DatabaseService {

	/**
	 * Creating the next child transaction and updating the recurring details in the 
	 * DS_PAY_TXNS & TXN_PARENT Table after canceling or skipping the transactions..
	 * Setting the input details..
	 * @param statusFlag
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void skipNextTransfer(int statusFlag,HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Setting the Skip Next transfer input details ...");
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

			//Payment account details...
			String from_MSAccountNumber = MSCommonUtils.getCompleteMSAccNumber(objFromMSAcc_Details);
			String to_MSAccountNumber = MSCommonUtils.getCompleteMSAccNumber(objToMSAcc_Details);
			String frequencyValue = objPaymentDetails.getFrequency_DurationDesc();
			String transferType = objPaymentDetails.getTransfer_Type();
			Date parentTxnDate = objPaymentDetails.getParTxnRequestDate();

			//MS User attributes..
			String userId=objMSUserDetails.getLoginId();
			String lifeTimeUserId=objMSUserDetails.getUuid();
			String userName=MSCommonUtils.extractCurrentUserName(objMSUserDetails);

			//The functions to calculate next business date and preferred next business date for the TXN_PARENT Entry...
			Date nextBusinessDate = new Date();
			Date pref_next_business_dt = new Date();
			if(objPaymentDetails.getPrefered_next_txn_dt()!=null) {
				nextBusinessDate = MSCommonUtils.evaluateNextTxnDate(frequencyValue,objPaymentDetails.getPrefered_next_txn_dt(),parentTxnDate,transferType,Boolean.TRUE,serviceContext);
				pref_next_business_dt = MSCommonUtils.calculatePrefNextTxnDate(frequencyValue,objPaymentDetails.getPrefered_next_txn_dt(),parentTxnDate,transferType);
			}
			else {
				nextBusinessDate = MSCommonUtils.evaluateNextTxnDate(frequencyValue,objPaymentDetails.getNext_txn_dt(),parentTxnDate,transferType,Boolean.TRUE,serviceContext);
				pref_next_business_dt = MSCommonUtils.calculatePrefNextTxnDate(frequencyValue,objPaymentDetails.getNext_txn_dt(),parentTxnDate,transferType);
			}

			//Mapping input details for the TXN_PARENT Updates..
			TxnParentTO objUpdateTxnParent = new TxnParentTO();
			objUpdateTxnParent.setPar_txn_no(ConvertionUtil.convertToDouble(objPaymentDetails.getRecParentTxnId()));
			objUpdateTxnParent.setNext_txn_dt(ConvertionUtil.convertToTimestamp(nextBusinessDate));
			objUpdateTxnParent.setPrefered_previous_txn_dt(ConvertionUtil.convertToTimestamp(pref_next_business_dt));

			//Mapping input details for the DS_BATCH Insert...
			DsBatchTO nextTxnBatchTO = new DsBatchTO();
			nextTxnBatchTO.setBatapplicationid(objPaymentDetails.getApplicationId());
			nextTxnBatchTO.setBatcreateddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			nextTxnBatchTO.setTxn_entry_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			nextTxnBatchTO.setBatmodifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			nextTxnBatchTO.setBatnooftxns(new Double(1D));
			nextTxnBatchTO.setBatcreatedby(objPaymentDetails.getMMSystemDesc());
			nextTxnBatchTO.setBatmodifiedby(objPaymentDetails.getMMSystemDesc());
			nextTxnBatchTO.setBatgrpid(objPaymentDetails.getMMSystemDesc());

			//Mapping Input details for the DS_PAY_TXNS Insert....
			DsPayTxnsTO nextTxnPaymentTO = new DsPayTxnsTO();
			nextTxnPaymentTO.setPaytypecode(objPaymentDetails.getScreen_Type());
			nextTxnPaymentTO.setPayservicetypecode(objPaymentDetails.getScreen_Type());
			nextTxnPaymentTO.setPaydebitaccnum(MSCommonUtils.getPayDebitaccnum(txnDetails));
			nextTxnPaymentTO.setPaypayeeaccnum(MSCommonUtils.getPaypayeeaccnum(txnDetails));
			nextTxnPaymentTO.setPaycurrcode(objPaymentDetails.getTransfer_Currency());
			nextTxnPaymentTO.setPaydebitamt(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getChildTxnAmount()));
			nextTxnPaymentTO.setPayfxrate(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getFxRate()));
			nextTxnPaymentTO.setPayfrequency(objPaymentDetails.getFrequency_DurationDesc());
			nextTxnPaymentTO.setPayfreqpaymentcount(ConvertionUtil.convertToDouble(objPaymentDetails.getDuration_NoOfTransfers()));
			nextTxnPaymentTO.setPayfreqenddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getDuration_EndDate()));
			nextTxnPaymentTO.setPayfreqlimit(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit()));
			nextTxnPaymentTO.setCreateddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			nextTxnPaymentTO.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			nextTxnPaymentTO.setTxn_type(objPaymentDetails.getTransfer_Type());
			nextTxnPaymentTO.setKeyaccountnumber_from(objFromMSAcc_Details.getKeyAccount());
			nextTxnPaymentTO.setFriendlyname_from(objFromMSAcc_Details.getFriendlyName());
			nextTxnPaymentTO.setNickname_from(objFromMSAcc_Details.getNickName());
			nextTxnPaymentTO.setFromfa_id(ConvertionUtil.convertToDouble(objFromMSAcc_Details.getFaNumber()));
			nextTxnPaymentTO.setFromfa_id_hist(null);
			nextTxnPaymentTO.setFrombranch_id(ConvertionUtil.convertToDouble(objFromMSAcc_Details.getOfficeNumber()));
			nextTxnPaymentTO.setFrombranch_id_hist(null);
			nextTxnPaymentTO.setFromacct_tier(null);
			nextTxnPaymentTO.setFromacct_no_hist(null);
			nextTxnPaymentTO.setFrombr_acct_no_fa(from_MSAccountNumber);
			nextTxnPaymentTO.setFromacct_type1("Cash");
			nextTxnPaymentTO.setKeyaccountnumber_to(objToMSAcc_Details.getKeyAccount());
			nextTxnPaymentTO.setFriendlyname_to(objToMSAcc_Details.getFriendlyName());
			nextTxnPaymentTO.setNickname_to(objToMSAcc_Details.getNickName());
			nextTxnPaymentTO.setTofa_id(ConvertionUtil.convertToDouble(objToMSAcc_Details.getFaNumber()));
			nextTxnPaymentTO.setTofa_id_hist(null);
			nextTxnPaymentTO.setTobranch_id(ConvertionUtil.convertToDouble(objToMSAcc_Details.getOfficeNumber()));
			nextTxnPaymentTO.setTobranch_id_hist(null);
			nextTxnPaymentTO.setToacct_tier(null);
			nextTxnPaymentTO.setToacct_no_hist(null);
			nextTxnPaymentTO.setToacct_type1("Cash");
			nextTxnPaymentTO.setTobr_acct_no_fa(to_MSAccountNumber);
			nextTxnPaymentTO.setAcct_user_id(userId);
			nextTxnPaymentTO.setAcct_user_name(userName);
			nextTxnPaymentTO.setCreated_by_role(objPaymentDetails.getMMSystemDesc());
			nextTxnPaymentTO.setCreatedby_name(objPaymentDetails.getMMSystemDesc());
			nextTxnPaymentTO.setModifiedby_name(objPaymentDetails.getMMSystemDesc());
			nextTxnPaymentTO.setCreatedby(objPaymentDetails.getMMSystemDesc());
			nextTxnPaymentTO.setModifiedby(objPaymentDetails.getMMSystemDesc());
			nextTxnPaymentTO.setLife_user_id(lifeTimeUserId);
			nextTxnPaymentTO.setAcct_from_type(MSCommonUtils.getFrom_AccType(objPaymentDetails));
			nextTxnPaymentTO.setAcct_to_type(MSCommonUtils.getTo_AccType(objPaymentDetails));
			nextTxnPaymentTO.setPaygroupid(objPaymentDetails.getMMSystemDesc());
			nextTxnPaymentTO.setPayissueddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getInitiation_Date()));
			nextTxnPaymentTO.setPayrecurring("Y");
			nextTxnPaymentTO.setTrial_depo("N");
			nextTxnPaymentTO.setPar_txn_no(ConvertionUtil.convertToDouble(objPaymentDetails.getRecParentTxnId()));
			nextTxnPaymentTO.setSent_to_paymnt("N");
			nextTxnPaymentTO.setRequested_exe_dt(objUpdateTxnParent.getNext_txn_dt());
			nextTxnPaymentTO.setPaypymtdate(objUpdateTxnParent.getNext_txn_dt());
			nextTxnPaymentTO.setActual_exe_dt(null);
			nextTxnPaymentTO.setRsa_review_flag("N");
			nextTxnPaymentTO.setInitiator_id(objPaymentDetails.getInitiator_id());
			nextTxnPaymentTO.setInitiator_role(objPaymentDetails.getInitiator_role());
			nextTxnPaymentTO.setCurrent_owner_id(objPaymentDetails.getCurrent_owner_id());
			nextTxnPaymentTO.setCurrent_owner_role(objPaymentDetails.getCurrent_owner_role());
			nextTxnPaymentTO.setAuth_mode(objPaymentDetails.getAuth_mode());
			nextTxnPaymentTO.setAuth_info_reciever(objPaymentDetails.getAuth_info_reciever());
			nextTxnPaymentTO.setVerbal_auth_client_name(objPaymentDetails.getVerbal_auth_client_name());
			nextTxnPaymentTO.setVerbal_auth_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getVerbal_auth_date()));
			nextTxnPaymentTO.setVerbal_auth_time(objPaymentDetails.getVerbal_auth_time());
			nextTxnPaymentTO.setClient_verification_desc(objPaymentDetails.getClient_verification_desc());
			nextTxnPaymentTO.setSame_name_flag(objPaymentDetails.getSame_name_flag());
			nextTxnPaymentTO.setAuth_confirmed_by(objPaymentDetails.getAuth_confirmed_by());
			nextTxnPaymentTO.setOfac_case_id(null);

			//Updating the TXN_PARENT Table with the next transfer details..
			updateTxn_ParentDetails(txnDetails,objUpdateTxnParent,serviceContext);

			//Creating the next child instance in case recurring check is valid...
			createNextTxnInstance(statusFlag,txnDetails,nextTxnBatchTO,nextTxnPaymentTO,objUpdateTxnParent,serviceContext);
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
	 * Update the parent transaction details...
	 * @param txnDetails
	 * @param objUpdateTxnParent
	 * @param serviceContext
	 * @throws Exception
	 */
	public void updateTxn_ParentDetails(HashMap txnDetails,TxnParentTO objUpdateTxnParent,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Setting the input details for updating the recurring transfer details in case of Skip Trasnfers..");
		try 
		{
			//Payment details attribute mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//StatementId and Object mapping for updating the txn_parent with already executed transactions..
			String updateTxnPaymentStmntId="updateParentTxnAfterSkip";
			Boolean isTxnCommitReq = Boolean.TRUE;

			//Checking the Transaction Parent version at the first instance on table update in a transaction...
			EBWLogger.logDebug(this, "Checking the Recurring Transaction Parent Version before modifying the transactions...");
			VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
			String versionType = "RECVER";
			objVerMismatch.setConnection(serviceConnection);
			objVerMismatch.compareVersionNum(txnDetails,versionType,serviceContext);
			if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
				//No need to return any object in case of failure , it will be handled in main class through service context..
				return ; 
			}

			EBWLogger.logDebug(this,"Executing Query : "+updateTxnPaymentStmntId);
			EBWLogger.logDebug(this,"Input Parameters mapped"+objUpdateTxnParent);
			execute(updateTxnPaymentStmntId, objUpdateTxnParent, isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+updateTxnPaymentStmntId);

			//Updating the calculated next_transfer_date...
			objPaymentDetails.setNext_txn_dt(objUpdateTxnParent.getNext_txn_dt());
		} 
		catch (SQLException exception) {
			throw exception;
		} 
		catch (Exception exception) {
			throw exception;
		}
		finally {

		}
	}

	/**
	 * Create the next transaction instance and update the txn_parent for success or failure of child creation..
	 * @param txnDetails
	 * @param objUpdateTxnParent
	 * @param serviceContext
	 * @throws Exception
	 */
	public void createNextTxnInstance(int statusFlag,HashMap txnDetails,DsBatchTO nextTxnBatchTO,DsPayTxnsTO nextTxnPaymentTO,TxnParentTO objUpdateTxnParent,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Setting the input details for updating the recurring transfer details in case of Skip Trasnfers..");
		try 
		{
			Boolean isTxnCommitReq = Boolean.TRUE;
			boolean isRecurringActive = true;
			Object txnEnrichmentDtl = new Object();

			//Payment details attributes...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//StatementId and Object mapping for creating next child transactions(if applicable only) and also updating the status in the txn_parent entry....
			String[] nextChildTxnStmntId={"setBatch","setpayTransaction","updateNextBussDtTxnParent"};
			Object[] nextChildTxnTOobj={nextTxnBatchTO,nextTxnPaymentTO,objUpdateTxnParent};
			DsPayTxnsTO objDsPayTxnsTO = (DsPayTxnsTO)nextChildTxnTOobj[1];
			String statusFlagVal = Integer.toString(statusFlag);
			DsBatchTO objDsbatchto = (DsBatchTO)nextChildTxnTOobj[0];
			isRecurringActive = CheckRecurringStatus.checkRecurringStatus(txnDetails);
			String dont_spawn_flag = objPaymentDetails.getDont_spawn_flag();

			if(isRecurringActive && (dont_spawn_flag==null || dont_spawn_flag.equalsIgnoreCase("N")))
			{
				for (int i= 0; i< nextChildTxnTOobj.length;i++)  
				{
					if(nextChildTxnTOobj[i] instanceof com.tcs.ebw.payments.transferobject.DsBatchTO){
						((DsBatchTO)nextChildTxnTOobj[i]).setBatstatus(statusFlagVal);
					}
					if(nextChildTxnTOobj[i] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsTO){
						//Following method sets the DB TO Objects with the external Interfaces response if required...
						setExternalSrvcResponseDetails((DsPayTxnsTO)nextChildTxnTOobj[i],txnDetails);

						//Setting the amount value again to handle the until dollar limit response..
						((DsPayTxnsTO)nextChildTxnTOobj[i]).setPaydebitamt(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getChildTxnAmount()));
						((DsPayTxnsTO)nextChildTxnTOobj[i]).setPayccsstatuscode(statusFlagVal);
						((DsPayTxnsTO)nextChildTxnTOobj[i]).setPaybatchref(objDsbatchto.getBatbatchref());

						//Setting the payment detail TO Object ....
						txnEnrichmentDtl=(DsPayTxnsTO)nextChildTxnTOobj[i];
					}
					if(nextChildTxnTOobj[i] instanceof com.tcs.ebw.payments.transferobject.TxnParentTO){
						((TxnParentTO)nextChildTxnTOobj[i]).setNext_txn_id(objDsPayTxnsTO.getPaypayref());
						((TxnParentTO)nextChildTxnTOobj[i]).setStatus("1");
					}
					EBWLogger.logDebug(this,"Executing Query : "+nextChildTxnStmntId[i]);
					EBWLogger.logDebug(this,"Input Parameters mapped"+nextChildTxnTOobj[i]);
					execute(nextChildTxnStmntId[i], nextChildTxnTOobj[i], isTxnCommitReq);
					EBWLogger.logDebug(this,"Execution Completed.... "+nextChildTxnStmntId[i]);
				} 
				//Inserting into the Transaction enrich Table after making entry into the BANCS DB for each MS Account....
				TransactionEnrichment objTransactionEnrichment = new TransactionEnrichment();
				objTransactionEnrichment.setConnection(serviceConnection);
				objTransactionEnrichment.setTransactionEnrichment(txnEnrichmentDtl,serviceContext);

				//Populate Transaction OutDetails in the PaymentDetails TO..
				populateChildPaymentDetails(txnDetails,nextTxnBatchTO,nextTxnPaymentTO);
			}
			else
			{
				TxnParentTO objTxnParentTO = (TxnParentTO)nextChildTxnTOobj[2];
				objTxnParentTO.setNext_txn_id(null);
				objTxnParentTO.setStatus("3");

				//Updating the status of the recurring record in the TXN_PARENT to closed/completed...
				EBWLogger.logDebug(this,"Executing Query : "+nextChildTxnStmntId[2]);
				EBWLogger.logDebug(this,"Input Parameters mapped"+nextChildTxnTOobj[2]);
				execute(nextChildTxnStmntId[2], nextChildTxnTOobj[2], isTxnCommitReq);
				EBWLogger.logDebug(this,"Execution Completed.... "+nextChildTxnStmntId[2]);
			}
		} 
		catch (SQLException exception) {
			throw exception;
		} 
		catch (Exception exception) {
			throw exception;
		}
		finally {

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
	}

	/**
	 * Populate transaction details ...
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void populateChildPaymentDetails(HashMap txnDetails,DsBatchTO nextTxnBatchTO,DsPayTxnsTO nextTxnPaymentTO) throws Exception
	{	
		try 
		{
			//Mapping the payment detail object with the confirmation number for FTM Call...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Setting the payment attributes...
			if(nextTxnPaymentTO!=null && nextTxnPaymentTO.getPaypayref()!=null){
				objPaymentDetails.setChildTxnBatchId(nextTxnBatchTO.getBatbatchref());
				objPaymentDetails.setChildTransactionId(nextTxnPaymentTO.getPaypayref());
				objPaymentDetails.setChildTxnRequestedExecDate(ConvertionUtil.convertToString(nextTxnPaymentTO.getRequested_exe_dt()));
				objPaymentDetails.setChildTxnStatusCode(nextTxnPaymentTO.getPayccsstatuscode());
				objPaymentDetails.setChildTxnCreated(true);
			}
		} 
		catch (Exception exception){
			throw exception;
		}
	}
}
