package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsBatchTO;
import com.tcs.ebw.payments.transferobject.DsPayTxnsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 * **********************************************************
 */
public class ApproveTransactionService extends DatabaseService{

	/**
	 * Approve the transaction based on the status code... 
	 * @param statusFlag
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void approvePaymentDetails(int statusFlag,HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Setting the Input details for the approval case..");
		try
		{
			//Payment account details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//MS User attributes..
			String userId=objMSUserDetails.getRcafId();
			String userName=MSCommonUtils.extractCurrentUserName(objMSUserDetails);

			//For updating the status in the BATCH table...
			DsBatchTO currentTxnBatchTO = new DsBatchTO();
			currentTxnBatchTO.setBatbatchref(objPaymentDetails.getTxnBatchId());
			currentTxnBatchTO.setBatmodifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnBatchTO.setBatmodifiedby(userId);
			currentTxnBatchTO.setBatauthby1(userName); // Approver Name details
			currentTxnBatchTO.setEmp_id1(userId); // Approver Employee Id details

			//For updating the status in the DS_PAY_TXNS table....
			DsPayTxnsTO currentTxnPaymentTO = new DsPayTxnsTO();
			currentTxnPaymentTO.setPaypayref(objPaymentDetails.getTransactionId());
			currentTxnPaymentTO.setPaybatchref(objPaymentDetails.getTxnBatchId());
			currentTxnPaymentTO.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnPaymentTO.setModifiedby(userId);
			currentTxnPaymentTO.setModifiedby_name(userName);
			currentTxnPaymentTO.setCoe_user_name(userName);
			currentTxnPaymentTO.setNext_approver_role(objPaymentDetails.getNext_approver_role());
			currentTxnPaymentTO.setDont_spawn_flag(objPaymentDetails.getDont_spawn_flag());
			currentTxnPaymentTO.setOfac_case_id(objPaymentDetails.getOfac_case_id());

			//StatementId and Object mapping for the execute query...
			String[] approvePaymentStmntId={"approveBatch","approvepayTransaction"};
			Object[] approvePaymentTOObj={currentTxnBatchTO,currentTxnPaymentTO};
			Boolean isTxnCommitReq = Boolean.TRUE;
			String statusFlagVal = Integer.toString(statusFlag);

			//Type casting the ToObject instances ...
			DsBatchTO dsbatchto = (DsBatchTO)approvePaymentTOObj[0];
			for (int i= 0; i< approvePaymentTOObj.length;i++) 
			{
				if(approvePaymentTOObj[i] instanceof com.tcs.ebw.payments.transferobject.DsBatchTO)
				{
					//Updating the user details in case the transaction is system rejected ....
					if(statusFlagVal!=null && statusFlagVal.equalsIgnoreCase("52")){
						updateBatchSystemDetails(txnDetails,(DsBatchTO)approvePaymentTOObj[i]);
					}

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
				if(approvePaymentTOObj[i] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsTO)
				{
					//Updating the user details in case the transaction is system rejected ....
					if(statusFlagVal!=null && statusFlagVal.equalsIgnoreCase("52")){
						updatePaymentSystemDetails(txnDetails,(DsPayTxnsTO)approvePaymentTOObj[i]);
					}

					((DsPayTxnsTO)approvePaymentTOObj[i]).setPayccsstatuscode(statusFlagVal);
					if(statusFlagVal.equalsIgnoreCase("4")){
						TxnProperties.calculateTxnSettleDate(txnDetails,(DsPayTxnsTO)approvePaymentTOObj[i],serviceContext);
						((DsPayTxnsTO)approvePaymentTOObj[i]).setSent_to_paymnt("Y");
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
				EBWLogger.logDebug(this,"Executing Query : "+approvePaymentStmntId[i]);
				EBWLogger.logDebug(this,"Input Parameters mapped"+approvePaymentTOObj[i]);
				execute(approvePaymentStmntId[i], approvePaymentTOObj[i], isTxnCommitReq);
				EBWLogger.logDebug(this,"Execution Completed.... "+approvePaymentStmntId[i]);
			} 

			//Populate Transaction OutDetails in the PaymentDetails TO..
			populatePaymentDetails(txnDetails,currentTxnBatchTO,currentTxnPaymentTO);
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

			//Updating the payment detail batch object...
			DsBatchTO modifyTxnBatchTO = new DsBatchTO();
			if(txnDetails.containsKey("Current_DsBatchObject")){
				modifyTxnBatchTO = (DsBatchTO)txnDetails.get("Current_DsBatchObject");
			}
			modifyTxnBatchTO.setBatmodifieddate(currentTxnBatchTO.getBatmodifieddate());
			modifyTxnBatchTO.setBatmodifiedby(currentTxnBatchTO.getBatmodifiedby());
			modifyTxnBatchTO.setBatmodifiedby(currentTxnBatchTO.getBatmodifiedby());
			modifyTxnBatchTO.setBatauthby1(currentTxnBatchTO.getBatauthby1()); // Approver Name details
			modifyTxnBatchTO.setEmp_id1(currentTxnBatchTO.getEmp_id1()); // Approver Employee Id details

			//Updating the payment details object..
			DsPayTxnsTO modifyTxnPaymentTO = new DsPayTxnsTO();
			if(txnDetails.containsKey("Current_DsPayTxnObject")){
				modifyTxnPaymentTO = (DsPayTxnsTO)txnDetails.get("Current_DsPayTxnObject");
			}
			modifyTxnPaymentTO.setPaypymtdate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			modifyTxnPaymentTO.setSent_to_paymnt(currentTxnPaymentTO.getSent_to_paymnt());
			modifyTxnPaymentTO.setPayccsstatuscode(currentTxnPaymentTO.getPayccsstatuscode());
			modifyTxnPaymentTO.setModifieddate(currentTxnPaymentTO.getModifieddate());
			modifyTxnPaymentTO.setModifiedby(currentTxnPaymentTO.getModifiedby());
			modifyTxnPaymentTO.setModifiedby_name(currentTxnPaymentTO.getModifiedby_name());
			modifyTxnPaymentTO.setCoe_user_name(currentTxnPaymentTO.getCoe_user_name());
			modifyTxnPaymentTO.setNext_approver_role(currentTxnPaymentTO.getNext_approver_role());
			modifyTxnPaymentTO.setActual_exe_dt(currentTxnPaymentTO.getActual_exe_dt());
			modifyTxnPaymentTO.setPayabpaysendflag(currentTxnPaymentTO.getPayabpaysendflag());

			//Setting the payment attributes...
			objPaymentDetails.setTxnBatchId(currentTxnBatchTO.getBatbatchref());
			objPaymentDetails.setTransactionId(currentTxnPaymentTO.getPaypayref());
			objPaymentDetails.setTxnCurrentStatusCode(currentTxnPaymentTO.getPayccsstatuscode());
			objPaymentDetails.setActualExecDate(ConvertionUtil.convertToAppDateStr(currentTxnPaymentTO.getActual_exe_dt()));
			objPaymentDetails.setDont_spawn_flag(currentTxnPaymentTO.getDont_spawn_flag());
		} 
		catch (Exception exception){
			throw exception;
		}
	}


	/**
	 * Setting the UserDetails to the MM System in case of "System Rejected"
	 * @param dsPayTxnsLog
	 * @throws Exception 
	 */
	public void updateBatchSystemDetails(HashMap txnDetails,DsBatchTO dsBatchTO) throws Exception
	{
		try 
		{
			//Getting the payment details ....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the log details to the MM System in case of "System Rejected" case only...
			dsBatchTO.setBatmodifiedby(objPaymentDetails.getMMSystemDesc());
			dsBatchTO.setBatauthby1(null); // Approver Name details
			dsBatchTO.setEmp_id1(null); // Approver Emp Id details
		} 
		catch (Exception exception) {
			throw exception;
		}

	}

	/**
	 * Setting the UserDetails to the MM System in case of "System Rejected"
	 * @param dsPayTxnsLog
	 * @throws Exception 
	 */
	public void updatePaymentSystemDetails(HashMap txnDetails,DsPayTxnsTO dsPayTxnsTO) throws Exception
	{
		try 
		{
			//Getting the payment details ....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the log details to the MM System in case of "System Rejected" case only...
			dsPayTxnsTO.setModifiedby(objPaymentDetails.getMMSystemDesc());
			dsPayTxnsTO.setCoe_user_name(null);
		} 
		catch (Exception exception) {
			throw exception;
		}
	}
}
