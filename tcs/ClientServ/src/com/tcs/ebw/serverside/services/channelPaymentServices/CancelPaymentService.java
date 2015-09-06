/**
 * 
 */
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
 *
 * **********************************************************
 */
public class CancelPaymentService extends DatabaseService{

	public CancelPaymentService(){

	}

	/**
	 * The method maps the details for canceling the one time transfers..
	 * @param statusFlag
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void cancelOneTimeTransfer(int statusFlag,HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Setting the input details for the Cancel One time transfer...");
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
			String userId=objMSUserDetails.getLoginId();
			String userName=MSCommonUtils.extractCurrentUserName(objMSUserDetails);

			//Mapping the DS_BATCH Transfer object for the current transaction...
			DsBatchTO currentTxnBatchTO = new DsBatchTO();
			currentTxnBatchTO.setBatbatchref(ConvertionUtil.convertToString(objPaymentDetails.getTxnBatchId()));
			currentTxnBatchTO.setBatmodifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnBatchTO.setBatmodifiedby(userId);
			currentTxnBatchTO.setBatauthby1(userName); // Canceler/Rejecter Name details
			currentTxnBatchTO.setEmp_id1(userId); // Canceler/Rejecter Employee Id details

			//Mapping the DS_PAY_TXNS Transfer object for the current transaction...
			DsPayTxnsTO currentTxnPaymentTO = new DsPayTxnsTO();
			currentTxnPaymentTO.setPaypayref(ConvertionUtil.convertToString(objPaymentDetails.getTransactionId()));
			currentTxnPaymentTO.setPaybatchref(ConvertionUtil.convertToString(objPaymentDetails.getTxnBatchId()));
			currentTxnPaymentTO.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnPaymentTO.setModifiedby(userId);
			currentTxnPaymentTO.setModifiedby_name(userName);
			currentTxnPaymentTO.setCoe_user_name(userName);
			currentTxnPaymentTO.setLife_user_id(objMSUserDetails.getUuid());
			currentTxnPaymentTO.setNext_approver_role(null);

			//StatementId and Object mapping for the execute query...
			String[] cancelpaymentStmntId={"cancelBatch","cancelPaytransaction"};
			Object[] cancelpaymentTOObj={currentTxnBatchTO,currentTxnPaymentTO};
			Boolean isTxnCommitReq = Boolean.TRUE;
			String statusFlagVal = Integer.toString(statusFlag);

			//Type casting the ToObject instances ...
			for (int i= 0; i< cancelpaymentTOObj.length;i++)  
			{
				if(cancelpaymentTOObj[i] instanceof com.tcs.ebw.payments.transferobject.DsBatchTO){
					((DsBatchTO)cancelpaymentTOObj[i]).setBatstatus(statusFlagVal);

					//Checking the Transaction Batch version before modifying the transactions...
					EBWLogger.logDebug(this, "Checking the Transaction Batch Version before cancelling the transactions...");
					VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
					String versionType = "BATVER";
					objVerMismatch.setConnection(serviceConnection);
					objVerMismatch.compareVersionNum(txnDetails,versionType,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return; 
					}
				}
				if(cancelpaymentTOObj[i] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsTO){
					((DsPayTxnsTO)cancelpaymentTOObj[i]).setPayccsstatuscode(statusFlagVal);

					//Checking the Transaction Record version before modifying the transactions...
					EBWLogger.logDebug(this, "Checking the Transaction Record Version before cancelling the transactions...");
					VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
					String versionType = "TXNVER";
					objVerMismatch.setConnection(serviceConnection);
					objVerMismatch.compareVersionNum(txnDetails,versionType,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return; 
					}
				}
				EBWLogger.logDebug(this,"Executing Query : "+cancelpaymentStmntId[i]);
				EBWLogger.logDebug(this,"Input Parameters mapped "+cancelpaymentTOObj[i]);
				execute(cancelpaymentStmntId[i], cancelpaymentTOObj[i], isTxnCommitReq);
				EBWLogger.logDebug(this,"Execution Completed.... "+cancelpaymentStmntId[i]);
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

			//Setting the payment attributes...
			objPaymentDetails.setTxnCurrentStatusCode(currentTxnPaymentTO.getPayccsstatuscode());
		} 
		catch (Exception exception){
			throw exception;
		}
	}
}
