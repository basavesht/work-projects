package com.tcs.Payments.serverValidations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.DsStatusConsistency;
import com.tcs.ebw.payments.transferobject.GetTransactionStatus;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class StatusConsistencyChk extends DatabaseService{

	public StatusConsistencyChk(){

	}

	public final static String BATCHSTATUS    = "BATCHSTATUS";     //DS_BATCH Status
	public final static String TXNSTATUS      = "TXNSTATUS";       //DS_PAY_TXNS Status
	public final static String ACNTSTATUS     = "ACNTSTATUS";      //DS_PAYEE_REF Status

	/**
	 * Status consistency check from the screen ....
	 * @param event_Status_Dtls
	 * @param txnStatus
	 * @return
	 */
	public ServiceContext verifyStatusConsistency(ArrayList valid_statuses,String txnStatus)
	{

		EBWLogger.trace("StatusConsistencyChk", "Starting verifyStatusConsistency method in StatusConsistencyChk class");
		boolean isInputStatusValid = false;
		ServiceContext serviceContext = new ServiceContext();
		ArrayList<Object> event_ValidStatus = new ArrayList<Object>(); 

		//Status consistency check ...
		if(valid_statuses!=null && !valid_statuses.isEmpty()){
			valid_statuses.remove(0); 
			if(!valid_statuses.isEmpty()){
				for(int j=0; j<valid_statuses.size(); j++){
					ArrayList event_TxnStatus = (ArrayList)valid_statuses.get(j);
					event_ValidStatus.add(event_TxnStatus.get(0));
				}
				if(!event_ValidStatus.isEmpty() && event_ValidStatus.contains(txnStatus)){
					isInputStatusValid=true;
				}
			}
		}

		//If the Status Consistency is failed then Critical error message is added to the context...
		if(!isInputStatusValid){
			EBWLogger.logDebug(this, "Status Consistency check failed ...");
			serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.STATUS_CONSISTENCY_FAILURE);
		}
		else {
			EBWLogger.logDebug(this, "Status Consistency check successful ...");
		}

		EBWLogger.trace("StatusConsistencyChk", "Finished verifyStatusConsistency method in StatusConsistencyChk class");
		return serviceContext;
	}

	/**
	 * Status consistency check from server side..
	 * @param txnDetails
	 * @param objServiceContext
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public boolean verifyCurrentStatusChk(HashMap txnDetails,ServiceContext objServiceContext) throws SQLException, Exception
	{
		EBWLogger.trace("StatusConsistencyChk", "Starting verifyCurrentStatusChk method in StatusConsistencyChk class");
		boolean isStatusValid = false;
		boolean boolean1 = Boolean.TRUE;
		Object objOutput=null;
		Object outStatusCode = null;
		String txnStatusChkStmntId = "";
		String currentStatus="";
		String statusTypeChk="";
		ArrayList valid_statuses = new ArrayList(); 
		GetTransactionStatus objGetTransactionStatus = new GetTransactionStatus();
		try 
		{
			//Payment object attributes...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			statusTypeChk = objPaymentDetails.getStatusChkId();

			//External account details...
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Input Mappings for the Status Consistency check (ServerSide validations)...
			DsStatusConsistency dsStatusConsistency = new DsStatusConsistency();
			dsStatusConsistency.setApplication_id(objPaymentDetails.getApplicationId());
			dsStatusConsistency.setEvent_name(objPaymentDetails.getStatusChkEventDesc());
			dsStatusConsistency.setPaypayref(objPaymentDetails.getTransactionId());
			dsStatusConsistency.setPaybatchref(objPaymentDetails.getTxnBatchId());
			dsStatusConsistency.setCpypayeeid(objExternalAccDetails.getCpypayeeid());

			//Executing the DS_STATUS_CONSISTENCY Check query for getting valid transaction statuses..
			String statusChkStmntId ="getValidTxnStatus";
			objOutput = executeQuery(statusChkStmntId,dsStatusConsistency,boolean1);

			//Getting the status from the related tables
			if(statusTypeChk!=null && statusTypeChk.equalsIgnoreCase(BATCHSTATUS)) 
			{
				//DS_BATCH Status check...
				txnStatusChkStmntId="getTxnBatchStatus";
				outStatusCode = executeQuery(txnStatusChkStmntId,dsStatusConsistency,boolean1);
				if(outStatusCode!=null && outStatusCode instanceof GetTransactionStatus){
					objGetTransactionStatus=(GetTransactionStatus)outStatusCode;
					currentStatus = objGetTransactionStatus.getTxn_status();
				}
			}
			else if(statusTypeChk!=null && statusTypeChk.equalsIgnoreCase(TXNSTATUS)) 
			{
				//DS_PAY_TXNS Status check...
				txnStatusChkStmntId="getTxnStatus";
				outStatusCode = executeQuery(txnStatusChkStmntId,dsStatusConsistency,boolean1);
				if(outStatusCode!=null && outStatusCode instanceof GetTransactionStatus){
					objGetTransactionStatus=(GetTransactionStatus)outStatusCode;
					currentStatus = objGetTransactionStatus.getTxn_status();
				}
			}
			else if(statusTypeChk!=null && statusTypeChk.equalsIgnoreCase(ACNTSTATUS)) 
			{
				//DS_PAYEE_REF Status check...
				txnStatusChkStmntId="getExtAcntStatus";
				outStatusCode = executeQuery(txnStatusChkStmntId,dsStatusConsistency,boolean1);
				if(outStatusCode!=null && outStatusCode instanceof GetTransactionStatus){
					objGetTransactionStatus=(GetTransactionStatus)outStatusCode;
					currentStatus = objGetTransactionStatus.getAcnt_status();
				}
			}

			//Current Status comparison...
			if(objOutput!=null && !currentStatus.trim().equalsIgnoreCase("")) {
				ArrayList event_Status_Dtls = (ArrayList)objOutput;
				if(event_Status_Dtls!=null && !event_Status_Dtls.isEmpty()) {
					event_Status_Dtls.remove(0); 
					if(!event_Status_Dtls.isEmpty()) {
						for(int j=0;j<event_Status_Dtls.size();j++) {
							ArrayList event_TxnStatus = (ArrayList)event_Status_Dtls.get(j);
							valid_statuses.add(event_TxnStatus.get(0));
						}
						if(!valid_statuses.isEmpty() && valid_statuses.contains(currentStatus)){
							isStatusValid=true;
							EBWLogger.logDebug(this, "No Status Mismatch occurred");
						}
					}
				}
			}
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
		EBWLogger.trace("StatusConsistencyChk", "Finished verifyCurrentStatusChk method in StatusConsistencyChk class");
		return isStatusValid;
	}
}
