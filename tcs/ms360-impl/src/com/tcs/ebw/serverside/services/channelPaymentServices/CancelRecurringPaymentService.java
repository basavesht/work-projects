package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import com.tcs.ebw.payments.transferobject.TxnParentTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class CancelRecurringPaymentService extends DatabaseService{

	public CancelRecurringPaymentService(){

	}

	/**
	 * The method maps the details for canceling the recurring transfers completely..
	 * @param statusFlag
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */

	public void cancelRecurringTransfer(int statusFlag,HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Setting the Recurring Transfer cancel details..");
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
			String userId=objMSUserDetails.getRcafId();
			String userName=MSCommonUtils.extractCurrentUserName(objMSUserDetails);
			String transactionPrevStatus = objPaymentDetails.getTxnPrevStatusCode();

			//Mapping the details for the DS_BATCH Transfer object...
			DsBatchTO currentTxnBatchTO = new DsBatchTO();
			currentTxnBatchTO.setBatbatchref(ConvertionUtil.convertToString(objPaymentDetails.getTxnBatchId()));
			currentTxnBatchTO.setBatmodifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnBatchTO.setBatmodifiedby(userId);
			currentTxnBatchTO.setBatauthby1(userName); // Canceler/Rejecter Name details
			currentTxnBatchTO.setEmp_id1(userId); // Canceler/Rejecter Employee Id details

			//Mapping the details for the DS_PAY_TXNS Transfer object...
			DsPayTxnsTO currentTxnPaymentTO = new DsPayTxnsTO();
			currentTxnPaymentTO.setPaypayref(ConvertionUtil.convertToString(objPaymentDetails.getTransactionId()));
			currentTxnPaymentTO.setPaybatchref(ConvertionUtil.convertToString(objPaymentDetails.getTxnBatchId()));
			currentTxnPaymentTO.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnPaymentTO.setModifiedby(userId);
			currentTxnPaymentTO.setModifiedby_name(userName);
			currentTxnPaymentTO.setCoe_user_name(userName);
			currentTxnPaymentTO.setNext_approver_role(null);

			//Mapping the details for the TXN_PARENT Transfer object....
			TxnParentTO recTxnParentTO = new TxnParentTO();
			recTxnParentTO.setPar_txn_no(ConvertionUtil.convertToDouble(objPaymentDetails.getRecParentTxnId()));
			recTxnParentTO.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			recTxnParentTO.setModifiedby(userId);

			//StatementId and Object mapping for the execute query...
			String[] cancelpaymentStmntId={"cancelBatch","cancelPaytransaction","cancelRecurringParent"};
			Object[] cancelpaymentTOObj={currentTxnBatchTO,currentTxnPaymentTO,recTxnParentTO};
			Boolean isTxnCommitReq = Boolean.TRUE;
			String statusFlagVal = Integer.toString(statusFlag);

			//Type casting the ToObject instances ...
			for (int i= 0; i< cancelpaymentTOObj.length;i++)  
			{
				if(cancelpaymentTOObj[i] instanceof com.tcs.ebw.payments.transferobject.DsBatchTO){
					((DsBatchTO)cancelpaymentTOObj[i]).setBatstatus(statusFlagVal);

					//Checking the Transaction Batch version before Canceling the transactions...
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

					//Checking the Transaction Record version before Canceling the transactions...
					EBWLogger.logDebug(this, "Checking the Transaction Record Version before cancelling the transactions...");
					VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
					String versionType = "TXNVER";
					objVerMismatch.setConnection(serviceConnection);
					objVerMismatch.compareVersionNum(txnDetails,versionType,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return; 
					}
				}
				if(cancelpaymentTOObj[i] instanceof com.tcs.ebw.payments.transferobject.TxnParentTO){
					((TxnParentTO)cancelpaymentTOObj[i]).setStatus("2");

					//Checking the Transaction Parent version before Canceling the transactions...
					EBWLogger.logDebug(this, "Checking the Recurring Transaction Parent Version before modifying the transactions...");
					VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
					String versionType = "RECVER";
					objVerMismatch.setConnection(serviceConnection);
					objVerMismatch.compareVersionNum(txnDetails,versionType,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return; 
					}
				}
				EBWLogger.logDebug(this,"Executing Query : "+cancelpaymentStmntId[i]);
				EBWLogger.logDebug(this,"Input Parameters mapped "+cancelpaymentTOObj[i]);
				execute(cancelpaymentStmntId[i], cancelpaymentTOObj[i],isTxnCommitReq);
				EBWLogger.logDebug(this,"Execution Completed.... "+cancelpaymentStmntId[i]);
			} 

			//Decrementing the transaction count and amount on canceling the Executed transaction only..
			if(transactionPrevStatus!=null && transactionPrevStatus.equalsIgnoreCase("4")){
				updateRecTxnCount(txnDetails);
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
	 * If the transaction is recurring, any subsequent transaction instance existing in Scheduled, Awaiting Approval, Suspended or Pending Risk Review status will also be canceled completely ..
	 * @param txnDetails
	 * @throws Exception
	 */
	public void cancelLinkedRecTxns(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Starting cancelLinkedRecTxns method in CancelRecurringPaymentService class");
		Object output = null;
		ArrayList linkedTxnIdList = new ArrayList();
		Boolean boolean1 = Boolean.TRUE;
		Date businessDate = new Date();
		try
		{
			//Payment Output Details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());

			//Input Mappings to get list of subsequent linked transactions for this recurring instruction..
			DsPayTxnsTO objDsPayTxnsTO = new DsPayTxnsTO();
			objDsPayTxnsTO.setPaypayref(objPaymentDetails.getTransactionId());
			objDsPayTxnsTO.setPaybatchref(objPaymentDetails.getTxnBatchId());
			objDsPayTxnsTO.setPar_txn_no(ConvertionUtil.convertToDouble(objPaymentDetails.getRecParentTxnId()));

			//Service call...
			String stmntId = "getLinkedRecTxnInstance";
			EBWLogger.logDebug(this,"Executing.... "+stmntId);
			output = executeQuery(stmntId,objDsPayTxnsTO,boolean1);
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);

			//Casting the output to the arrayList...
			if(output!=null){
				linkedTxnIdList = (ArrayList)output;
				if(linkedTxnIdList!=null && !linkedTxnIdList.isEmpty()){
					linkedTxnIdList.remove(0); //Removing the header...
				}
			}

			//Cancel all selected transfers in the List..
			MultiTransferProcessor multiTxnProcessor = new MultiTransferProcessor();
			multiTxnProcessor.setConnection(serviceConnection);
			multiTxnProcessor.cancelMultiTransfer(linkedTxnIdList,businessDate,txnDetails,serviceContext);
			if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
				return; 
			}

			EBWLogger.logDebug(this, "Executed cancelLinkedRecTxns");
			EBWLogger.trace(this, "Finished with cancelLinkedRecTxns method of CancelRecurringPaymentService class");
		}
		catch(SQLException sqlexception) {
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally {

		}
	}

	/**
	 * The method updates the TXN_PARENT table and decrements the accumulated amount and accumulated transaction count...
	 * @param txnDetails
	 * @throws Exception
	 * @throws SQLException
	 */
	public void updateRecTxnCount(HashMap txnDetails) throws Exception,SQLException 
	{
		Boolean boolean1 = Boolean.TRUE;
		String updateRecTxnCountStmntId="updateRecTxnCount";
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Decrementing Transaction count and amount...
			TxnParentTO objTxnParentTO = new TxnParentTO();
			objTxnParentTO.setPar_txn_no(ConvertionUtil.convertToDouble(objPaymentDetails.getRecParentTxnId()));
			objTxnParentTO.setAccum_amt(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			objTxnParentTO.setAccum_txn_no(new Double(1D));

			EBWLogger.logDebug(this,"Executing "+updateRecTxnCountStmntId);
			execute(updateRecTxnCountStmntId, objTxnParentTO, boolean1);
			EBWLogger.logDebug(this,"Execution Completed "+updateRecTxnCountStmntId);
		} 
		catch(SQLException sqlexception){
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
