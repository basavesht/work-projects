package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.serverValidations.StatusConsistencyChk;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.DsPayTxnsTO;
import com.tcs.ebw.payments.transferobject.DsPayeeRefTO;
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
public class SuspendAccountService extends DatabaseService{

	public SuspendAccountService(){

	}

	/**
	 * The method maps the details for suspending account...
	 * @param statusFlag
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void suspendAccount(int statusFlag,HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Setting the input details for Suspend account..");
		boolean isAcntStatusValid = false;
		try
		{
			//Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//MSUser details..
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//External account details...
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//MS User attributes..
			String userId=objMSUserDetails.getRcafId();

			//DsPayeeRef TO mappings...
			DsPayeeRefTO dsPayeeRef = new DsPayeeRefTO();
			dsPayeeRef.setCpystatus(Integer.toString(statusFlag));
			dsPayeeRef.setCpypayeeid(objExternalAccDetails.getCpypayeeid());
			dsPayeeRef.setModifiedby(userId);
			dsPayeeRef.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));

			//StatementId and Object mapping for the execute query...
			String[] suspendAccStmntId={"suspendAccount"};
			Object[] suspendAccTOObj={dsPayeeRef};
			Boolean isTxnCommitReq = Boolean.TRUE;

			//Checking the Account Version before modifying the account...
			EBWLogger.logDebug(this, "Checking the Account Version before modifying the account...");
			VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
			String versionType = "";
			objVerMismatch.setConnection(serviceConnection);
			objVerMismatch.compareVersionNum(txnDetails,versionType,serviceContext);
			if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
				return; 
			}

			//Checking the Account Status Consistency ...
			StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
			statusConsistency.setConnection(serviceConnection);
			objPaymentDetails.setStatusChkId("ACNTSTATUS");
			isAcntStatusValid = statusConsistency.verifyCurrentStatusChk(txnDetails,serviceContext);
			if(!isAcntStatusValid){
				serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.STATUS_CONSISTENCY_FAILURE);
				return ;
			}

			//Service call...
			EBWLogger.logDebug(this,"Executing Query : "+suspendAccStmntId[0]);
			EBWLogger.logDebug(this,"Input Parameters mapped "+suspendAccTOObj[0]);
			execute(suspendAccStmntId[0], suspendAccTOObj[0], isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+suspendAccStmntId[0]);
		}
		catch(SQLException sqlexception) {
			throw sqlexception;
		}
		catch(Exception exception) {
			throw exception;
		}
		finally {

		}
	}

	/**
	 * Suspending the transactions pertaining to the existing external account...
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void suspendTransactions(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Starting suspendTransactions method in SuspendAccountService class");
		MultiTransferProcessor multiTxnProcessor = new MultiTransferProcessor();
		multiTxnProcessor.setConnection(serviceConnection);
		ArrayList transferList = new ArrayList();
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			Date businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());

			//Getting all the transaction Id List 
			transferList = getTxnIdList(txnDetails);
			if(transferList!=null && !transferList.isEmpty()){
				//Cancel all selected transfers in the List..
				multiTxnProcessor.suspendMultiTransfer(transferList,businessDate,txnDetails,serviceContext);
				if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
					return; 
				}
			}
		} 
		catch(Exception exception){
			throw exception;
		}
		finally {

		}
	}

	/**
	 * The method gets  all the transaction confirmation Ids for the input specified in form of ArrayList...
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 * @throws Exception
	 */
	public ArrayList getTxnIdList(HashMap txnDetails) throws Exception 
	{
		EBWLogger.trace(this, "Starting getTxnIdList method in SuspendAccountService class");
		Object output = null;
		ArrayList txnIdList = new ArrayList();
		String suspendTxnStmntId="getSuspendTransactions";
		Boolean boolean1 = Boolean.TRUE;
		try
		{
			//External account details...
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Input Mappings for the external account ...
			DsPayTxnsTO objDsPayTxnsTO = new DsPayTxnsTO();
			objDsPayTxnsTO.setPaypayeeaccnum(objExternalAccDetails.getCpypayeeid());

			//Statement Id and Transfer objects...
			EBWLogger.logDebug(this,"Executing.... "+suspendTxnStmntId);
			output = executeQuery(suspendTxnStmntId,objDsPayTxnsTO,boolean1);
			EBWLogger.logDebug(this,"Execution Completed.... "+suspendTxnStmntId);

			//Casting the output to the arrayList...
			if(output!=null){
				txnIdList = (ArrayList)output;
				if(txnIdList!=null && !txnIdList.isEmpty()){
					txnIdList.remove(0); //Removing the header...
				}
			}
			EBWLogger.logDebug(this, "Executed getTxnIdList");
			EBWLogger.trace(this, "Finished with getTxnIdList method of SuspendAccountService class");
		}
		catch(SQLException sqlexception) {
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally {

		}
		return txnIdList;
	}
}
