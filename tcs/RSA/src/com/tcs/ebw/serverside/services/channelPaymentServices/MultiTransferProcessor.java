/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.AuditTrialAction;
import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.Payments.serverValidations.StatusConsistencyChk;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;
import com.tcs.ebw.serverside.services.PaymentsUtility;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MultiTransferProcessor extends DatabaseService 
{
	/**
	 * Canceling multiple transfers....
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void cancelMultiTransfer(ArrayList transferList,Date businessDate,HashMap txnDetails,ServiceContext serviceContext)throws Exception
	{
		EBWLogger.trace(this, "Starting cancelMultiTransfer method in Implementation class");
		int paymentStatusFlag=0;
		String notificationEventId = "";
		boolean isExternalAccount = false;
		HashMap cancelTxnDetails = new HashMap();
		try 
		{
			//Get all the transaction details with the status Awaiting Approval , Suspended , Scheduled ...
			EBWLogger.logDebug(this, "Getting the Payment Details for account passed");
			EBWLogger.logDebug(this, "Finshed getting the Payment Details for account passed");

			if(transferList!=null && !transferList.isEmpty())
			{
				if(transferList.size()>0)
				{
					for(int j = 0; j < transferList.size(); j++)
					{
						//Setting the transaction data...
						Object[] cancelTxnData = setCancelTransactionData(txnDetails,(ArrayList)transferList.get(j),businessDate);

						//Mapping the Payment details (Internal/External Cancel Transfers)...
						MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
						objMapConfirmDetails.setConnection(serviceConnection);
						cancelTxnDetails = objMapConfirmDetails.setPaymentConfirmDetails(cancelTxnData,serviceContext);

						//Mapping the payment attributes...
						PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
						if(cancelTxnDetails.containsKey("PaymentDetails")){
							objPaymentDetails = (PaymentDetailsTO)cancelTxnDetails.get("PaymentDetails");
						}
						String transferType= objPaymentDetails.getTransfer_Type();
						String frequencyFlag = objPaymentDetails.getFrequency_Type();
						int isRecurringFlag= ConvertionUtil.convertToint(frequencyFlag);
						boolean isTxnStatusValid = false;
						String loggingAction = null;

						//Checking the status consistency only on Load of the screen...
						StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
						statusConsistency.setConnection(serviceConnection);
						isTxnStatusValid = statusConsistency.verifyCurrentStatusChk(cancelTxnDetails,serviceContext);

						//Check if the status consistency check is failed , if yes then don't update the selected record, proceed with next record update... 
						if(isTxnStatusValid)
						{
							//Transaction canceling based on the Recurring flag...
							if(isRecurringFlag == 1)
							{
								paymentStatusFlag=20;
								CancelPaymentService objCancelPaymentService = new CancelPaymentService();
								objCancelPaymentService.setConnection(serviceConnection);
								objCancelPaymentService.cancelOneTimeTransfer(paymentStatusFlag,cancelTxnDetails,serviceContext);
								if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									return; 
								}
							}
							else if(isRecurringFlag == 2)
							{
								EBWLogger.logDebug(this, "Canceling the Record in DB and creating the log ");
								paymentStatusFlag=20;
								CancelRecurringPaymentService objCancelRecurringPaymentService = new CancelRecurringPaymentService();
								objCancelRecurringPaymentService.setConnection(serviceConnection);
								objCancelRecurringPaymentService.cancelRecurringTransfer(paymentStatusFlag,cancelTxnDetails,serviceContext);
								if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									return; 
								}
							}

							//Call the CreatePaymentDetailsLog Service
							EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
							loggingAction=AuditTrialAction.Cancel;
							CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
							objCreatePaymentLogService.setConnection(serviceConnection);
							objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,cancelTxnDetails,serviceContext);
							EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

							//Call limit check service through Business Rule for deleting the limit ....
							if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))
							{
								//Calling Merlin Service...
								EBWLogger.logDebug(this, "Executing the Merlin Service..");
								PaymentsUtility.getAccountDetails(txnDetails,serviceContext);
								if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									return;
								}
								EBWLogger.logDebug(this, "Executed the Merlin Service..");

								//Limit Check processing..
								EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
								PaymentsUtility.processLimitRequest(cancelTxnDetails,serviceContext);
								if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									return;
								}
							}

							//RTA Hold Service 
							EBWLogger.logDebug(this, "Calling the RTA Service");
							serviceContext.setCallRTAImmediateCommit(true);
							PaymentsUtility.processRTARequest(cancelTxnDetails,serviceContext);
							if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
								return;
							}
							EBWLogger.logDebug(this, "RTA Executed successfully ");

							//Call Notification Service 
							EBWLogger.logDebug(this, "Call Notification Service");
							if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
								if(isRecurringFlag == 2)
									notificationEventId = "NOTIFY_P24";
								else
									notificationEventId = "NOTIFY_P23";
							}
							else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
								if(isRecurringFlag == 2)
									notificationEventId = "NOTIFY_P35";
								else
									notificationEventId = "NOTIFY_P34";
							}
							else {
								if(isRecurringFlag == 2)
									notificationEventId = "NOTIFY_P12";
								else
									notificationEventId = "NOTIFY_P11";
							}
							PaymentsUtility.sendNotificationDetails(cancelTxnDetails,notificationEventId,isExternalAccount,serviceContext);
							if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
								return;
							}
							EBWLogger.logDebug(this, "Notification Service Executed successfully");

							//Update Transition date : Canceled State 
							EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
							UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
							objUpdtTransitionDt.setConnection(serviceConnection);
							objUpdtTransitionDt.setCurTxnTransitionDate(cancelTxnDetails,serviceContext);
							EBWLogger.logDebug(this, "Updated the transition date successfully");
						}
					}
				}
			}
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
	 * Setting the transaction for each individual transactions...
	 * @param txnDetails
	 * @param txnRefDetails
	 * @return
	 * @throws Exception 
	 */
	public static Object[] setCancelTransactionData(HashMap txnDetails,ArrayList txnRefDetails,Date businessDate) throws Exception 
	{
		try 
		{
			//User Details...
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//Mapping payment attributes...
			PaymentDetailsTO cancelPayDtls = new PaymentDetailsTO();

			//Mapping the payment attributes...
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");

			//Mapping the payment details object..
			cancelPayDtls.setFrmAcc_InContext(false);
			cancelPayDtls.setToAcc_InContext(false);
			cancelPayDtls.setTransfer_Currency(currencyCode);
			cancelPayDtls.setMsBranchId(branch_Id);
			cancelPayDtls.setApplicationId(applicationId);
			cancelPayDtls.setMMSystemDesc(systemDesc);
			cancelPayDtls.setTxnCancelled(true);
			cancelPayDtls.setStatusChkEventDesc("Cancel_Txn");
			cancelPayDtls.setStatusChkId("TXNSTATUS");
			cancelPayDtls.setStatusChkReq(true);
			cancelPayDtls.setVersionChkReq(false);
			cancelPayDtls.setTransactionId((String)txnRefDetails.get(0));
			cancelPayDtls.setTxnBatchId((String)txnRefDetails.get(1));
			cancelPayDtls.setBusiness_Date(ConvertionUtil.convertToAppDateStr(businessDate));

			Object objTOParam[] = {cancelPayDtls,objMSUserDetails};

			return objTOParam;
		}
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Suspending multiple transfers....
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void suspendMultiTransfer(ArrayList transferList,Date businessDate,HashMap txnDetails,ServiceContext serviceContext)throws Exception
	{
		EBWLogger.trace(this, "Starting cancelMultiTransfer method in Implementation class");
		int paymentStatusFlag=0;
		String notificationEventId = "";
		boolean isExternalAccount = false;
		HashMap suspendTxnDetails = new HashMap();
		try 
		{
			//Get all the transaction details with the status Awaiting Approval , Suspended , Scheduled ...
			EBWLogger.logDebug(this, "Getting the Payment Details for account passed");
			EBWLogger.logDebug(this, "Finshed getting the Payment Details for account passed");

			if(transferList!=null && !transferList.isEmpty())
			{
				if(transferList.size()>0)
				{
					for(int j = 0; j < transferList.size(); j++)
					{
						//Setting the transaction data...
						Object[] suspendTxnData = setSuspendTransactionData(txnDetails,(ArrayList)transferList.get(j),businessDate);

						//Mapping the Payment details (Internal/External Cancel Transfers)...
						MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
						objMapConfirmDetails.setConnection(serviceConnection);
						suspendTxnDetails = objMapConfirmDetails.setPaymentConfirmDetails(suspendTxnData,serviceContext);

						//Mapping the payment attributes...
						PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
						if(suspendTxnDetails.containsKey("PaymentDetails")){
							objPaymentDetails = (PaymentDetailsTO)suspendTxnDetails.get("PaymentDetails");
						}
						String transferType= objPaymentDetails.getTransfer_Type();
						String frequencyFlag = objPaymentDetails.getFrequency_Type();
						String prevStatusCode = objPaymentDetails.getTxnPrevStatusCode();
						int isRecurringFlag= ConvertionUtil.convertToint(frequencyFlag);
						Date transferDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
						boolean isTxnStatusValid = false;
						String loggingAction = null;

						//Checking the status consistency for each transaction in loop...
						StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
						statusConsistency.setConnection(serviceConnection);
						isTxnStatusValid = statusConsistency.verifyCurrentStatusChk(suspendTxnDetails,serviceContext);

						//Check if the status consistency check is failed , if yes then don't update the selected record, proceed with next record update... 
						if(isTxnStatusValid)
						{
							//Transaction canceling based on the Recurring flag...
							paymentStatusFlag=46;
							CancelPaymentService objCancelPaymentService = new CancelPaymentService();
							objCancelPaymentService.setConnection(serviceConnection);
							objCancelPaymentService.cancelOneTimeTransfer(paymentStatusFlag,suspendTxnDetails,serviceContext);
							if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
								return; 
							}

							//Call the CreatePaymentDetailsLog Service
							EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
							loggingAction=AuditTrialAction.Suspend;
							CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
							objCreatePaymentLogService.setConnection(serviceConnection);
							objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,suspendTxnDetails,serviceContext);
							EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

							//Call limit check service through Business Rule for deleting the limit ....
							if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))
							{
								//Calling Merlin Service...
								EBWLogger.logDebug(this, "Executing the Merlin Service..");
								PaymentsUtility.getAccountDetails(txnDetails,serviceContext);
								if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									return;
								}
								EBWLogger.logDebug(this, "Executed the Merlin Service..");

								//Limit check processing...
								EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
								objPaymentDetails.setChildTxnCreated(false);
								PaymentsUtility.processLimitRequest(suspendTxnDetails,serviceContext);
								if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									return;
								}
							}

							//RTA Hold Service 
							EBWLogger.logDebug(this, "Calling the RTA Service");
							serviceContext.setCallRTAImmediateCommit(true);
							PaymentsUtility.processRTARequest(suspendTxnDetails,serviceContext);
							if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
								return;
							}
							EBWLogger.logDebug(this, "RTA Executed successfully ");

							//Call Notification Service 
							EBWLogger.logDebug(this, "Call Notification Service");
							if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
								if(isRecurringFlag == 2){
									notificationEventId = "NOTIFY_P18";
								}
								else{
									notificationEventId = "NOTIFY_P17";
								}
							}
							PaymentsUtility.sendNotificationDetails(suspendTxnDetails,notificationEventId,isExternalAccount,serviceContext);
							if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
								return;
							}
							EBWLogger.logDebug(this, "Notification Service Executed successfully");

							//Update Transition date : Suspend State 
							EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
							UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
							objUpdtTransitionDt.setConnection(serviceConnection);
							objUpdtTransitionDt.setCurTxnTransitionDate(suspendTxnDetails,serviceContext);
							EBWLogger.logDebug(this, "Updated the transition date successfully");

							//Reset Approval Details : Suspend State 
							if(prevStatusCode.equalsIgnoreCase("80")){
								EBWLogger.logDebug("processRSARequest", "Call ResetApprovalDetails Service");
								ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
								objResetApproverInfo.setConnection(serviceConnection);
								objResetApproverInfo.clearApproverDetails(suspendTxnDetails,serviceContext);
								EBWLogger.logDebug("processRSARequest", "Reset the approver details successfully");
							}

							//Spawning next child in case of recurring current dated awaiting approval or pending risk review transactions..
							if(isRecurringFlag == 2 && (prevStatusCode.equalsIgnoreCase("2") || prevStatusCode.equalsIgnoreCase("80")) && !transferDate.after(businessDate))
							{
								//Call the UpdateTxnParent Service to update the Parent Transaction and create next Child Transaction
								UpdateTxnParentService objUpdateTxnParentService = new UpdateTxnParentService();
								objUpdateTxnParentService.setConnection(serviceConnection);
								objUpdateTxnParentService.setUpdateTxnParentDetails(suspendTxnDetails,serviceContext);

								//Call the CreatePaymentDetailsLog Service for child transaction logging.
								if(objPaymentDetails.isChildTxnCreated()) {
									EBWLogger.logDebug(this, "Child Transaction Logging in the Channels DB (Created Status)");
									int childPayStatusFlag = 46;
									CreatePaymentLogService objChildTxnLogging = new CreatePaymentLogService();
									objChildTxnLogging.setConnection(serviceConnection);
									objChildTxnLogging.setNextChildPaymentsLog(childPayStatusFlag,suspendTxnDetails,serviceContext);
									EBWLogger.logDebug(this, "Child Transaction Logged in the Channels DB (Created Status)");
								}
							}
						}
					}
				}
			}
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
	 * Setting the transaction for each individual transactions...
	 * @param txnDetails
	 * @param txnRefDetails
	 * @return
	 * @throws Exception 
	 */
	public static Object[] setSuspendTransactionData(HashMap txnDetails,ArrayList txnRefDetails,Date businessDate) throws Exception 
	{
		try 
		{
			//User Details...
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//Payment details..
			PaymentDetailsTO objAccountDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objAccountDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping payment attributes...
			PaymentDetailsTO suspendPayDtls = new PaymentDetailsTO();

			//Mapping the payment attributes...
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");

			//Mapping the payment details object..
			suspendPayDtls.setFrmAcc_InContext(false);
			suspendPayDtls.setToAcc_InContext(false);
			suspendPayDtls.setTransfer_Currency(currencyCode);
			suspendPayDtls.setMsBranchId(branch_Id);
			suspendPayDtls.setApplicationId(applicationId);
			suspendPayDtls.setMMSystemDesc(systemDesc);
			suspendPayDtls.setParentTxnSuspended(true);
			suspendPayDtls.setStatusChkEventDesc("Suspend_Txn");
			suspendPayDtls.setCreated_by_comments(objAccountDetails.getCreated_by_comments());
			suspendPayDtls.setStatusChkId("TXNSTATUS");
			suspendPayDtls.setStatusChkReq(true);
			suspendPayDtls.setVersionChkReq(false);
			suspendPayDtls.setTransactionId((String)txnRefDetails.get(0));
			suspendPayDtls.setTxnBatchId((String)txnRefDetails.get(1));
			suspendPayDtls.setBusiness_Date(ConvertionUtil.convertToAppDateStr(businessDate));
			suspendPayDtls.setCase_status(objAccountDetails.getCase_status());

			Object objTOParam[] = {suspendPayDtls,objMSUserDetails};

			return objTOParam;
		}
		catch (Exception exception) {
			throw exception;
		}
	}
}
