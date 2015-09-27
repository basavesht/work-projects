/**
 * 
 */
package com.tcs.ebw.serverside.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.EAITO.RSA_CALLBACK_INP;
import com.tcs.Payments.EAITO.RSA_CALLBACK_OUT;
import com.tcs.Payments.ms360Utils.AuditTrialAction;
import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.RSAUtilities;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.Payments.serverValidations.ValidateRSARequest;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.Message;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountEligible;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountLink;
import com.tcs.ebw.serverside.services.channelPaymentServices.ApproveTransactionService;
import com.tcs.ebw.serverside.services.channelPaymentServices.ChkTrxnHelperService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CreateAccountLogService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CreatePaymentLogService;
import com.tcs.ebw.serverside.services.channelPaymentServices.GetQZBusinessDate;
import com.tcs.ebw.serverside.services.channelPaymentServices.IdentifyTransaction;
import com.tcs.ebw.serverside.services.channelPaymentServices.MapPaymentInputDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.ResetApprovalDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.SuspendAccountService;
import com.tcs.ebw.serverside.services.channelPaymentServices.UpdateBRValidationLog;
import com.tcs.ebw.serverside.services.channelPaymentServices.UpdatePaymentConfNoService;
import com.tcs.ebw.serverside.services.channelPaymentServices.UpdateTransitionDate;
import com.tcs.ebw.serverside.services.channelPaymentServices.UpdateTxnParentService;
import com.tcs.ebw.serverside.services.channelPaymentServices.UserEntilements;
import com.tcs.mswitch.common.channel.DBProcedureChannel;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       12-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 * **********************************************************
 */
public class CallBackRSAProcessor extends DatabaseService 
{
	private transient Connection conn = null;

	public CallBackRSAProcessor() {

	}

	/**
	 * Response handler after executing the CallBack RSA service as a whole...
	 * @param rsaRequest
	 * @param serviceContext
	 */
	public RSA_CALLBACK_OUT processRSARequest(RSA_CALLBACK_INP rsaRequest)
	{
		conn = (Connection)serviceConnection;
		RSA_CALLBACK_OUT reponseOut = new RSA_CALLBACK_OUT();
		ServiceContext serviceContext = new ServiceContext();
		try 
		{
			//Executing the RSA CallBack business implementation...
			serviceContext.setConnection(conn);
			executeRSARequest(rsaRequest,serviceContext);
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}
		finally {
			//RSA Response creator....
			RSAUtilities.ResponseCreator(reponseOut,serviceContext);
		}
		return reponseOut;
	}

	/**
	 * Wrapper service to execute the RSA Request and send the response code and message..
	 * @param rsaRequest
	 * @param context
	 * @return
	 */
	public void executeRSARequest(RSA_CALLBACK_INP rsaRequest,ServiceContext serviceContext)
	{
		boolean isTxnExists = false;
		String case_status = null;
		try 
		{
			//1.Validate RSA Request Object...
			ValidateRSARequest validateReq = new ValidateRSARequest();
			validateReq.validateRSARequest(rsaRequest, serviceContext);
			if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity()== MessageType.SEVERE){
				return;
			}

			//2.Validate RSA Request Timing...
			ValidateRSARequest pollingTime = new ValidateRSARequest();
			pollingTime.checkRSAPollingTime(serviceContext);
			if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity()== MessageType.SEVERE){
				return;
			}

			//3.Transaction identification in MM System..
			IdentifyTransaction identifyTxn = new IdentifyTransaction();
			identifyTxn.setConnection(conn);
			isTxnExists = identifyTxn.identifyTransaction(rsaRequest, serviceContext);
			if(isTxnExists) {
				case_status = rsaRequest.getCASE_STATUS();
				if(case_status!=null && case_status.equalsIgnoreCase(MSSystemDefaults.RSA_GENUINE_CASE)){
					processRSAGenuineCase(rsaRequest,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return ; 
					}
				}
				else if(case_status!=null && case_status.equalsIgnoreCase(MSSystemDefaults.RSA_FRAUD_CASE)){
					processRSAFraudCase(rsaRequest,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return ; 
					}
				}
				else {
					serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.INVALID_RSA_CASE_STATUS);
				}
			}
			else {
				serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.TXN_NOT_IDENTIFIED);
				return;
			}

			//MM DB and RTA Transaction Commit...
			serviceContext.setServiceCallSuccessful(true);
			((Connection)conn).commit();
			if(serviceContext.isRTACommitReq()){
				EBWLogger.logDebug("processRSARequest", "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
		} 
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(sqlexception));
		}
		catch(Exception exception){
			exception.printStackTrace();
			serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(exception));
		}
		finally 
		{
			try {
				//RollBack is called in case of Error code return other than 1 from SI for external interfaces..
				if(serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE)
				{
					try {
						((Connection)conn).rollback();
						if(serviceContext.isRTARollbackReq())
						{
							EBWLogger.logDebug("processRSARequest", "Calling RTA Rollback");
							DBProcedureChannel.rollback();
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				((Connection)conn).close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * RSA CallBack Genuine case...
	 * @param rsaRequest
	 * @param context
	 */
	public void processRSAGenuineCase(RSA_CALLBACK_INP rsaRequest,ServiceContext serviceContext) throws SQLException,Exception 
	{
		EBWLogger.trace("processRSARequest", "Starting processRSAGenuineCase method in Implementation class");
		int paymentStatusFlag=0;
		String notificationEventId ="";
		boolean isExternalAccount = false;
		HashMap txnDetails = new HashMap();
		String transferType ="";
		String frequencyFlag ="";
		int isRecurringFlag=0;
		String loggingAction ="";
		int childPayStatusFlag =0;
		int txnStatusCode = 0;
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		Date paymentDate = new Date();
		Date businessDate = new Date();
		Object[] toObjects = new Object[2];
		int previousTxnStatus = 0;
		try
		{
			//RSA Extractor creator....
			toObjects = RSAUtilities.RequestExtractor_Transaction(rsaRequest,serviceContext);

			//Mapping the Payment details (Internal/External Cancel Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setPaymentConfirmDetails(toObjects,serviceContext);

			//Get the business Date from the QZ_Dates View 
			EBWLogger.logDebug("processRSARequest", "Getting the Business Date");
			GetQZBusinessDate getBusinessDt = new GetQZBusinessDate();
			getBusinessDt.setConnection(conn);
			getBusinessDt.getQzBusinessDate(txnDetails,serviceContext);
			EBWLogger.logDebug("processRSARequest", "Finished getting Business Date : ");

			//Mapping the payment attributes...
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			paymentDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
			businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());
			transferType = objPaymentDetails.getTransfer_Type();
			frequencyFlag = objPaymentDetails.getFrequency_Type();
			isRecurringFlag = ConvertionUtil.convertToint(frequencyFlag);
			txnStatusCode = ConvertionUtil.convertToint(objPaymentDetails.getTxnCurrentStatusCode());
			if(objPaymentDetails.getTxnPrevStatusCode()!=null){
				previousTxnStatus = new Integer(objPaymentDetails.getTxnPrevStatusCode()).intValue();
			}

			if(txnStatusCode == 80)
			{
				//User Entitlements Check for Verifying the external account routing number...
				UserEntilements userEntilements = new UserEntilements();
				userEntilements.setConnection(conn);
				if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
					userEntilements.checkExtAccRoutingNum(txnDetails,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return ; 
					}
				}

				//Calling the Account View Service for FROM and TO accounts if applicable ... 
				EBWLogger.logDebug("processRSARequest", "Executing  the Merlin Service");
				PaymentsUtility.getAccountDetails(txnDetails,serviceContext);
				if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
					return ; 
				}
				EBWLogger.logDebug("processRSARequest", "Executed Merlin Service successfully ... ");

				//Call RTAB Service
				EBWLogger.logDebug("processRSARequest", "Executing  the RTAB Service");
				if(transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || (transferType.startsWith(ChkReqConstants.CHK)))
				{  
					// Calling the RTAB Service..
					PaymentsUtility.getAccountBalance(txnDetails,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return ; 
					}
				}
				EBWLogger.logDebug("processRSARequest", "Executed RTAB Service");

				//External Account Link extraction to pass it to business rule..
				if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
					EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
					AccountLink objAccountLink= new AccountLink();
					objAccountLink.setConnection(serviceConnection);
					objAccountLink.getExtAccountLink(txnDetails,serviceContext);
					if(serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return; 
					}
					EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
				}

				//If the Transaction is One-Time Immediate Transfer 
				if(isRecurringFlag==1)
				{
					//Call Business Rule Service 
					EBWLogger.logDebug("processRSARequest", "The processing transaction is ONE-TIME Transfer");
					EBWLogger.logDebug("processRSARequest", "Executing the Business Rule Service");
					PaymentsUtility.executeBRTransaction(txnDetails,serviceContext);
					if(serviceContext.getMaxSeverity() == MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return ; 
					}
					else if (serviceContext.getMaxSeverity() == MessageType.ERROR)
					{
						EBWLogger.logDebug("processRSARequest", "Calling the BR Validation Log Service ");
						PaymentsUtility.executeBRLogging(txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}
						EBWLogger.logDebug("processRSARequest", "BR Warnings Logged successfully");

						// Call the CreatePaymentService With Status as "System Rejected"
						EBWLogger.logDebug("processRSARequest", "Updating the record in the channels DB");
						paymentStatusFlag=52;
						ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
						objApproveTransactionService.setConnection(conn);
						objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}

						//Call limit check service through Business Rule for deleting the limit ....
						if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
							EBWLogger.logDebug("processRSARequest", "Calling BusinessRule Service limit check for delete case..");
							PaymentsUtility.processLimitRequest(txnDetails, serviceContext);
							if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
								EBWLogger.logDebug("processRSARequest", "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
								return ;
							}
						}

						// Making RTA Entries...
						EBWLogger.logDebug("processRSARequest", "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}
						EBWLogger.logDebug("processRSARequest", "RTA Executed successfully ");

						//Call Notification Service 
						EBWLogger.logDebug("processRSARequest", "Call Notification Service");
						if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
							notificationEventId = "NOTIFY_P27";
						}
						else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
							notificationEventId = "NOTIFY_P38";
						}
						else{
							notificationEventId = "NOTIFY_P15";
						}
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}
						EBWLogger.logDebug("processRSARequest", "Notification Service Executed successfully");

						// Call the CreatePaymentDetailsLog Service with the status as "System Rejected"
						EBWLogger.logDebug("processRSARequest", "Logging in the Channels DB");
						loggingAction= AuditTrialAction.RSACallback;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,serviceContext);

						//Reset Approval Details : System Rejected State 
						EBWLogger.logDebug("processRSARequest", "Call ResetApprovalDetails Service");
						ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
						objResetApproverInfo.setConnection(conn);
						objResetApproverInfo.clearApproverDetails(txnDetails,serviceContext);
						EBWLogger.logDebug("processRSARequest", "Reset the approver details successfully");

						//Update Transition date : System Rejected State 
						EBWLogger.logDebug("processRSARequest", "Call UpdateTransitionDate Service");
						UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
						objUpdtTransitionDt.setConnection(conn);
						objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,serviceContext);
						EBWLogger.logDebug("processRSARequest", "Updated the transition date successfully");

						//Checking the Account Eligible flag from BR and accordingly cancel all the transactions ....
						EBWLogger.logDebug("processRSARequest", "Checking the Account Eligible flag from BR and accordingly cancel all the transactions");
						AccountEligible acntEligibility = new AccountEligible();
						acntEligibility.setConnection(conn);
						acntEligibility.validateAccEligibilty(txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}
						EBWLogger.logDebug("processRSARequest", "Checking the Account Eligible flag from BR and accordingly cancel all the transactions successfully");
					}
					else if(serviceContext.getMaxSeverity() == MessageType.WARNING)
					{
						//BR Validation Log Service 
						EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
						PaymentsUtility.executeBRLogging(txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}
						EBWLogger.logDebug(this, "BR Warnings Logged successfully");

						//Call the CreatePaymentService With Status as "Awaiting approval" and update the next approver role in the DS_PAY_TXNS..
						EBWLogger.logDebug("processRSARequest", "Updating the record in the channels DB");
						paymentStatusFlag=2;
						ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
						objApproveTransactionService.setConnection(conn);
						objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}

						//External Account Link Check and creation only in case the previous transaction status is 2...
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE))
						{
							if(previousTxnStatus == 2) {
								EBWLogger.logDebug("processRSARequest", "Checking the Account Link exist flag...");
								AccountLink objAccountLink= new AccountLink();
								objAccountLink.setConnection(conn);
								objAccountLink.checkAccountLink(txnDetails,serviceContext);
								if(serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									return ; 
								}
								EBWLogger.logDebug("processRSARequest", "Finished Checking the Account Link exist flag...");
							}
						}

						// Making RTA Entries...
						EBWLogger.logDebug("processRSARequest", "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}

						// Call the CreatePaymentDetailsLog Service with the status as "Approved"
						EBWLogger.logDebug("processRSARequest", "Logging in the Channels DB");
						loggingAction= AuditTrialAction.RSACallback;
						paymentStatusFlag=2;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,serviceContext);
					}
					else
					{
						//If the transactions getting approved are same dated or less than the business date (considering the transaction expiry time)
						if(!paymentDate.after(businessDate))
						{
							EBWLogger.logDebug("processRSARequest", "Updating the record in the channels DB");

							//Determining the transaction status based on the transaction type..
							if(transferType!=null && transferType.equalsIgnoreCase(ChkReqConstants.CHK_LOC)){
								paymentStatusFlag =103; //Awaiting Print...
							}
							else {
								paymentStatusFlag =4; //Executed..
							}

							//Call the CreatePaymentService With Status as "Executed" Or "Awaiting Print".
							ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
							objApproveTransactionService.setConnection(conn);
							objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,serviceContext);
							if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
								return ; 
							}

							//External Account Link Check and creation only in case the previous transaction status is 2...
							if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE))
							{
								if(previousTxnStatus == 2) {
									EBWLogger.logDebug("processRSARequest", "Checking the Account Link exist flag...");
									AccountLink objAccountLink= new AccountLink();
									objAccountLink.setConnection(conn);
									objAccountLink.checkAccountLink(txnDetails,serviceContext);
									if(serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
										return ; 
									}
									EBWLogger.logDebug("processRSARequest", "Finished Checking the Account Link exist flag...");
								}
							}

							if(transferType!=null && !transferType.startsWith(ChkReqConstants.CHK))
							{
								// Call the Payment HUB Service for transaction other than CHK..
								EBWLogger.logDebug("processRSARequest", "Calling Payments HUB Service ");
								PaymentsUtility.executePaymentsHub(txnDetails,serviceContext);
								if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									return ; 
								}
								EBWLogger.logDebug("processRSARequest", "Payments created in HUB ");

								// Call UpdatePaymentConfirmationNo Service	 for transaction other than CHK..
								EBWLogger.logDebug("processRSARequest", "Updating the Payment Conf Number in the channels DB  ");
								UpdatePaymentConfNoService objUpdatePaymentConfNoService = new UpdatePaymentConfNoService();
								objUpdatePaymentConfNoService.setConnection(conn);
								objUpdatePaymentConfNoService.setUpdatePaymentConfNoService(txnDetails,serviceContext);
								EBWLogger.logDebug("processRSARequest", "Updated the Payment Conf Number in the channels DB  ");
							}
							else 
							{
								//Making RTA entries..
								EBWLogger.logDebug("processRSARequest", "Calling the RTA Service ");
								PaymentsUtility.processRTARequest(txnDetails,serviceContext);
								if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									return ; 
								}
							}

							//Call the CreatePaymentDetailsLog Service
							EBWLogger.logDebug("processRSARequest", "Loggin in the Channels DB");
							loggingAction= AuditTrialAction.RSACallback;
							CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
							objCreatePaymentLogService.setConnection(conn);
							objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,serviceContext);

							//Update Transition date : Executed State 
							EBWLogger.logDebug("processRSARequest", "Call UpdateTransitionDate Service");
							UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
							objUpdtTransitionDt.setConnection(conn);
							objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,serviceContext);
							EBWLogger.logDebug("processRSARequest", "Updated the transition date successfully");

							//Reset Approval Details : Executed State 
							EBWLogger.logDebug("processRSARequest", "Call ResetApprovalDetails Service");
							ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
							objResetApproverInfo.setConnection(conn);
							objResetApproverInfo.clearApproverDetails(txnDetails,serviceContext);
							EBWLogger.logDebug("processRSARequest", "Reset the approver details successfully");

							//Call the BR_VALIDATION_LOG update service for updating the APPRVD_BY Column with the approved by name...
							UpdateBRValidationLog objUpdtBrValidation = new UpdateBRValidationLog();
							objUpdtBrValidation.setConnection(conn);
							objUpdtBrValidation.updateBRValidationLog(txnDetails);
						}
						else  // If the transaction getting approved are future dated..
						{
							//Call the ApproveTransactionService With Status as "Scheduled"
							EBWLogger.logDebug("processRSARequest", "Updating the record in the channels DB");
							paymentStatusFlag=6;
							ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
							objApproveTransactionService.setConnection(conn);
							objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,serviceContext);
							if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
								return ; 
							}

							//External Account Link Check and creation only in case the previous transaction status is 2...
							if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE))
							{
								if(previousTxnStatus == 2) {
									EBWLogger.logDebug("processRSARequest", "Checking the Account Link exist flag...");
									AccountLink objAccountLink= new AccountLink();
									objAccountLink.setConnection(conn);
									objAccountLink.checkAccountLink(txnDetails,serviceContext);
									if(serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
										return ; 
									}
									EBWLogger.logDebug("processRSARequest", "Finished Checking the Account Link exist flag...");
								}
							}

							//Reset Approval Details : Executed State 
							EBWLogger.logDebug("processRSARequest", "Call ResetApprovalDetails Service");
							ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
							objResetApproverInfo.setConnection(conn);
							objResetApproverInfo.clearApproverDetails(txnDetails,serviceContext);
							EBWLogger.logDebug("processRSARequest", "Reset the approver details successfully");

							//Call the CreatePaymentDetailsLog Service
							EBWLogger.logDebug("processRSARequest", "Loggin in the Channels DB");
							loggingAction= AuditTrialAction.RSACallback;
							CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
							objCreatePaymentLogService.setConnection(conn);
							objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,serviceContext);

							//Call the BR_VALIDATION_LOG update service for updating the APPRVD_BY Column with the approved by name...
							UpdateBRValidationLog objUpdtBrValidation = new UpdateBRValidationLog();
							objUpdtBrValidation.setConnection(conn);
							objUpdtBrValidation.updateBRValidationLog(txnDetails);
						}
					}
					EBWLogger.logDebug("processRSARequest", "Executed approvePaymentsTransation in ExceptionManagementService");
					EBWLogger.trace("processRSARequest", "Finished with approvePaymentsTransation in ExceptionManagementService");
				}
				else if(isRecurringFlag==2)
				{
					EBWLogger.logDebug("processRSARequest", "The processing transaction is RECURRING Transfer");
					EBWLogger.logDebug("processRSARequest", "Executing the BR Service ");

					PaymentsUtility.executeBRTransaction(txnDetails,serviceContext);
					EBWLogger.logDebug("processRSARequest", "Executed BR service");
					if(serviceContext.getMaxSeverity() == MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return ; 
					}
					else if (serviceContext.getMaxSeverity() == MessageType.ERROR)
					{
						EBWLogger.logDebug("processRSARequest", "Calling the BR Validation Log Service ");
						PaymentsUtility.executeBRLogging(txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}
						EBWLogger.logDebug("processRSARequest", "BR Warnings Logged successfully");

						// Call the CreatePaymentService With Status as "System Rejected"
						EBWLogger.logDebug("processRSARequest", "Updating the record in the channels DB");
						paymentStatusFlag=52;
						ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
						objApproveTransactionService.setConnection(conn);
						objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}

						//Call limit check service through Business Rule for deleting the limit ....
						if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
							EBWLogger.logDebug("processRSARequest", "Calling BusinessRule Service limit check for delete case..");
							PaymentsUtility.processLimitRequest(txnDetails, serviceContext);
							if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE)
							{
								EBWLogger.logDebug("processRSARequest", "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
								return ;
							}
						}

						// Making RTA Entries...
						EBWLogger.logDebug("processRSARequest", "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}
						EBWLogger.logDebug("processRSARequest", "RTA Executed successfully ");

						//Call Notification Service 
						EBWLogger.logDebug("processRSARequest", "Call Notification Service");
						if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
							notificationEventId = "NOTIFY_P27";
						}
						else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
							notificationEventId = "NOTIFY_P38";
						}
						else{
							notificationEventId = "NOTIFY_P15";
						}
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}
						EBWLogger.logDebug("processRSARequest", "Notification Service Executed successfully");

						// Call the CreatePaymentDetailsLog Service with the status and action as "System Rejected"
						EBWLogger.logDebug("processRSARequest", "Logging in the Channels DB");
						loggingAction= AuditTrialAction.RSACallback;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,serviceContext);

						//Reset Approval Details : System Rejected State 
						EBWLogger.logDebug("processRSARequest", "Call ResetApprovalDetails Service");
						ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
						objResetApproverInfo.setConnection(conn);
						objResetApproverInfo.clearApproverDetails(txnDetails,serviceContext);
						EBWLogger.logDebug("processRSARequest", "Reset the approver details successfully");

						//Update Transition date : System Rejected State 
						EBWLogger.logDebug("processRSARequest", "Call UpdateTransitionDate Service");
						UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
						objUpdtTransitionDt.setConnection(conn);
						objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,serviceContext);
						EBWLogger.logDebug("processRSARequest", "Updated the transition date successfully");

						//Checking the Account Eligible flag from BR and accordingly cancel all the transactions ....
						EBWLogger.logDebug("processRSARequest", "Checking the Account Eligible flag from BR and accordingly cancel all the transactions");
						AccountEligible acntEligibility = new AccountEligible();
						acntEligibility.setConnection(conn);
						acntEligibility.validateAccEligibilty(txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}
						EBWLogger.logDebug("processRSARequest", "Checking the Account Eligible flag from BR and accordingly cancel all the transactions successfully");

						if(objPaymentDetails.isAccounEligible())
						{
							//Call the UpdateTxnParent Service to update the Parent Transaction and create next Child Transaction
							EBWLogger.logDebug("processRSARequest", "Creating the next child transaction");
							UpdateTxnParentService objUpdateTxnParentService = new UpdateTxnParentService();
							objUpdateTxnParentService.setConnection(conn);
							objUpdateTxnParentService.setUpdateTxnParentDetails(txnDetails,serviceContext);
							EBWLogger.logDebug("processRSARequest", "Next child created successfully");

							//Creating Check Details entries for the new child if spawned..
							if(objPaymentDetails.isChildTxnCreated() && transferType.startsWith(ChkReqConstants.CHK)){
								EBWLogger.logDebug("processRSARequest", "Chk Details transaction in the Channels DB (Created Status)");
								ChkTrxnHelperService objChkTrxnHelperSrvc = new ChkTrxnHelperService();
								objChkTrxnHelperSrvc.setConnection(conn);
								objChkTrxnHelperSrvc.insertChkTrxnDetails(txnDetails,true);
								EBWLogger.logDebug("processRSARequest", "Chk Details transaction in the Channels DB (Created Status)");
							}

							//Call the CreatePaymentDetailsLog Service for child transaction logging.
							if(objPaymentDetails.isChildTxnCreated()){
								EBWLogger.logDebug("processRSARequest", "Child Transaction Logging in the Channels DB (Created Status)");
								childPayStatusFlag = 6;
								CreatePaymentLogService objChildTxnLogging = new CreatePaymentLogService();
								objChildTxnLogging.setConnection(conn);
								objChildTxnLogging.setNextChildPaymentsLog(childPayStatusFlag,txnDetails,serviceContext);
								EBWLogger.logDebug("processRSARequest", "Child Transaction Logged in the Channels DB (Created Status)");
							}

							//Call the limit check service through Business rule service for all the child ...
							if(objPaymentDetails.isChildTxnCreated() && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
								EBWLogger.logDebug("processRSARequest", "Calling limit check Service for all child future dated transactions");
								PaymentsUtility.processLimitRequest(txnDetails,serviceContext);
								if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									EBWLogger.logDebug("processRSARequest", "limit check Service failed for child future dated transactions");
									return ; 
								}
								EBWLogger.logDebug("processRSARequest", "Limit check service for future dated transactions");
							}
						}
					}
					else if(serviceContext.getMaxSeverity() == MessageType.WARNING)
					{
						//BR Validation Log Service 
						EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
						PaymentsUtility.executeBRLogging(txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}
						EBWLogger.logDebug(this, "BR Warnings Logged successfully");

						//Call the CreatePaymentService With Status as "Awaiting approval" and update the next approver role in the DS_PAY_TXNS..
						EBWLogger.logDebug("processRSARequest", "Updating the record in the channels DB");
						paymentStatusFlag=2;
						ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
						objApproveTransactionService.setConnection(conn);
						objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}

						//External Account Link Check and creation only in case the previous transaction status is 2...
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE))
						{
							if(previousTxnStatus == 2) {
								EBWLogger.logDebug("processRSARequest", "Checking the Account Link exist flag...");
								AccountLink objAccountLink= new AccountLink();
								objAccountLink.setConnection(conn);
								objAccountLink.checkAccountLink(txnDetails,serviceContext);
								if(serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									return ; 
								}
								EBWLogger.logDebug("processRSARequest", "Finished Checking the Account Link exist flag...");
							}
						}

						// Making RTA Entries...
						EBWLogger.logDebug("processRSARequest", "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}

						// Call the CreatePaymentDetailsLog Service with the status as "Approved"
						EBWLogger.logDebug("processRSARequest", "Logging in the Channels DB");
						loggingAction=AuditTrialAction.RSACallback;
						paymentStatusFlag=2;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,serviceContext);
					}
					else
					{
						//If the transactions getting approved are same dated or less than the business date (considering the transaction expiry time)
						if(!paymentDate.after(businessDate))
						{
							//Determining the txn status based on the transaction type..
							if(transferType!=null && transferType.equalsIgnoreCase(ChkReqConstants.CHK_LOC)){
								paymentStatusFlag =103; //Awaiting Print...
							}
							else {
								paymentStatusFlag =4; //Executed...
							}

							//Call the CreatePaymentService With Status as "Executed" Or "Awaiting Print".
							ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
							objApproveTransactionService.setConnection(conn);
							objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,serviceContext);
							if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
								return ; 
							}

							//External Account Link Check and creation only in case the previous transaction status is 2...
							if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE))
							{
								if(previousTxnStatus == 2) {
									EBWLogger.logDebug("processRSARequest", "Checking the Account Link exist flag...");
									AccountLink objAccountLink= new AccountLink();
									objAccountLink.setConnection(conn);
									objAccountLink.checkAccountLink(txnDetails,serviceContext);
									if(serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
										return ; 
									}
									EBWLogger.logDebug("processRSARequest", "Finished Checking the Account Link exist flag...");
								}
							}

							if(transferType!=null && !transferType.startsWith(ChkReqConstants.CHK))
							{
								//Call the Payment HUB Service for transaction other than CHK..
								EBWLogger.logDebug("processRSARequest", "Calling Payment Hub ");
								PaymentsUtility.executePaymentsHub(txnDetails,serviceContext);
								if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									return ; 
								}
								EBWLogger.logDebug("processRSARequest", "Payment created in HUB Successfully");

								//Call UpdatePaymentConfirmationNo Service for transaction other than CHK..
								EBWLogger.logDebug("processRSARequest", "Updating the paymnet conf no in channel DB  ");
								UpdatePaymentConfNoService objUpdatePaymentConfNoService = new UpdatePaymentConfNoService();
								objUpdatePaymentConfNoService.setConnection(conn);
								objUpdatePaymentConfNoService.setUpdatePaymentConfNoService(txnDetails,serviceContext);
							}
							else 
							{
								//Making RTA entries..
								EBWLogger.logDebug("processRSARequest", "Calling the RTA Service ");
								PaymentsUtility.processRTARequest(txnDetails,serviceContext);
								if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									return ; 
								}
							}

							//Call the CreatePaymentDetailsLog Service
							EBWLogger.logDebug("processRSARequest", "Logging in the channels DB  ");
							loggingAction=AuditTrialAction.RSACallback;
							CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
							objCreatePaymentLogService.setConnection(conn);
							objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,serviceContext);

							//Call the BR_VALIDATION_LOG update service for updating the APPRVD_BY Column with the approved by name...
							UpdateBRValidationLog objUpdtBrValidation = new UpdateBRValidationLog();
							objUpdtBrValidation.setConnection(conn);
							objUpdtBrValidation.updateBRValidationLog(txnDetails);

							//Call the UpdateTxnParent Service to update the Parent Transaction and create next Child Transaction
							EBWLogger.logDebug("processRSARequest", "Creating the next child transaction");
							UpdateTxnParentService objUpdateTxnParentService = new UpdateTxnParentService();
							objUpdateTxnParentService.setConnection(conn);
							objUpdateTxnParentService.setUpdateTxnParentDetails(txnDetails,serviceContext);
							EBWLogger.logDebug("processRSARequest", "Next child created successfully");

							//Creating Check Details entries for the new child if spawned..
							if(objPaymentDetails.isChildTxnCreated() && transferType.startsWith(ChkReqConstants.CHK)){
								EBWLogger.logDebug("processRSARequest", "Chk Details transaction in the Channels DB (Created Status)");
								ChkTrxnHelperService objChkTrxnHelperSrvc = new ChkTrxnHelperService();
								objChkTrxnHelperSrvc.setConnection(conn);
								objChkTrxnHelperSrvc.insertChkTrxnDetails(txnDetails,true);
								EBWLogger.logDebug("processRSARequest", "Chk Details transaction in the Channels DB (Created Status)");
							}

							//Call the CreatePaymentDetailsLog Service for child transaction logging.
							if(objPaymentDetails.isChildTxnCreated()){
								EBWLogger.logDebug("processRSARequest", "Child Transaction Logging in the Channels DB (Created Status)");
								childPayStatusFlag = 6;
								CreatePaymentLogService objChildTxnLogging = new CreatePaymentLogService();
								objChildTxnLogging.setConnection(conn);
								objChildTxnLogging.setNextChildPaymentsLog(childPayStatusFlag,txnDetails,serviceContext);
								EBWLogger.logDebug("processRSARequest", "Child Transaction Logged in the Channels DB (Created Status)");
							}

							//Call the limit check service through Business rule service for all the child ...
							if(objPaymentDetails.isChildTxnCreated() && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
								EBWLogger.logDebug("processRSARequest", "Calling limit check Service for all child future dated transactions");
								PaymentsUtility.processLimitRequest(txnDetails,serviceContext);
								if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
									EBWLogger.logDebug("processRSARequest", "limit check Service failed for child future dated transactions");
									return ; 
								}
								EBWLogger.logDebug("processRSARequest", "Limit check service for future dated transactions");
							}

							//Reset Approval Details : Executed State 
							EBWLogger.logDebug("processRSARequest", "Call ResetApprovalDetails Service");
							ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
							objResetApproverInfo.setConnection(conn);
							objResetApproverInfo.clearApproverDetails(txnDetails,serviceContext);
							EBWLogger.logDebug("processRSARequest", "Reset the approver details successfully");

							//Update Transition date : Executed State 
							EBWLogger.logDebug("processRSARequest", "Call UpdateTransitionDate Service");
							UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
							objUpdtTransitionDt.setConnection(conn);
							objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,serviceContext);
							EBWLogger.logDebug("processRSARequest", "Updated the transition date successfully");
						}
						else //Transaction will be put back to the scheduled status on successful approval. 
						{
							//Update the transaction in the Channels DB in scheduled state.
							EBWLogger.logDebug("processRSARequest", "Updating the Record in channels DB");
							paymentStatusFlag=6;
							ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
							objApproveTransactionService.setConnection(conn);
							objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,serviceContext);
							if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
								return ; 
							}

							//External Account Link Check and creation only in case the previous transaction status is 2...
							if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE))
							{
								if(previousTxnStatus == 2) {
									EBWLogger.logDebug("processRSARequest", "Checking the Account Link exist flag...");
									AccountLink objAccountLink= new AccountLink();
									objAccountLink.setConnection(conn);
									objAccountLink.checkAccountLink(txnDetails,serviceContext);
									if(serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
										return ; 
									}
									EBWLogger.logDebug("processRSARequest", "Finished Checking the Account Link exist flag...");
								}
							}

							//Reset Approval Details : Executed State 
							EBWLogger.logDebug("processRSARequest", "Call ResetApprovalDetails Service");
							ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
							objResetApproverInfo.setConnection(conn);
							objResetApproverInfo.clearApproverDetails(txnDetails,serviceContext);
							EBWLogger.logDebug("processRSARequest", "Reset the approver details successfully");

							//Call the CreatePaymentDetailsLog Service
							EBWLogger.logDebug("processRSARequest", "Logging in the channels DB  ");
							loggingAction=AuditTrialAction.RSACallback;
							CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
							objCreatePaymentLogService.setConnection(conn);
							objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,serviceContext);

							//Call the BR_VALIDATION_LOG update service for updating the APPRVD_BY Column with the approved by name...
							UpdateBRValidationLog objUpdtBrValidation = new UpdateBRValidationLog();
							objUpdtBrValidation.setConnection(conn);
							objUpdtBrValidation.updateBRValidationLog(txnDetails);
						}
					}
					EBWLogger.logDebug("processRSARequest", "Executed processRSARequest for One Time Immediate Transfer");
					EBWLogger.trace("processRSARequest", "Finished with processRSARequest for One Time Immediate Transfer");
				}
				else {
					serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
					return ; 
				}
			}
			else 
			{
				//Call the CreatePaymentDetailsLog Service with the action as "RSA CallBack"...
				paymentStatusFlag = txnStatusCode;
				EBWLogger.logDebug("processRSARequest", "Logging in the channels DB  ");
				loggingAction=AuditTrialAction.RSACallback;
				CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
				objCreatePaymentLogService.setConnection(conn);
				objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,serviceContext);
			}
		}
		catch(SQLException sqlexception){
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally {

		}
	}

	/**
	 * RSA CallBack Fraud case...
	 * @param rsaRequest
	 * @param context
	 * @throws Exception 
	 */
	public void processRSAFraudCase(RSA_CALLBACK_INP rsaRequest,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace("processRSARequest", "Starting processRSAGenuineCase method in Implementation class");
		int paymentStatusFlag=0;
		HashMap txnDetails = new HashMap();
		String notificationEventId ="";
		boolean isExternalAccount = false;
		String loggingAction ="";
		int accStatusCode = 0;
		Object[] toObjects = new Object[2];
		try
		{
			//RSA Extractor creator....
			toObjects = RSAUtilities.RequestExtractor_Account(rsaRequest,serviceContext);

			//Mapping the Payment details (Internal/External Cancel Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setPaymentConfirmDetails(toObjects,serviceContext);

			//Get the business Date from the QZ_Dates View 
			EBWLogger.logDebug("processRSARequest", "Getting the Business Date");
			GetQZBusinessDate getBusinessDt = new GetQZBusinessDate();
			getBusinessDt.setConnection(conn);
			getBusinessDt.getQzBusinessDate(txnDetails,serviceContext);
			EBWLogger.logDebug("processRSARequest", "Finished getting Business Date : ");

			//External account details...
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Status mapping...
			accStatusCode = ConvertionUtil.convertToint(objExternalAccDetails.getCpystatus());
			if(accStatusCode == 32 || accStatusCode == 40)
			{
				//External account suspension..
				paymentStatusFlag = 36;
				EBWLogger.logDebug("processRSARequest", "External Account suspension...");
				SuspendAccountService objSuspendAccount = new SuspendAccountService();
				objSuspendAccount.setConnection(conn);
				objSuspendAccount.suspendAccount(paymentStatusFlag,txnDetails,serviceContext);

				//Logging for external account suspension...
				EBWLogger.logDebug("processRSARequest", "External Account logging...");
				loggingAction=AuditTrialAction.RSAFraud;
				CreateAccountLogService objCreateAccountLogService = new CreateAccountLogService();
				objCreateAccountLogService.setConnection(conn);
				objCreateAccountLogService.setExtAccountDetailsLog(paymentStatusFlag,loggingAction,txnDetails,serviceContext);

				//Call Notification Service 
				EBWLogger.logDebug(this, "Call Notification Service for Account level");
				notificationEventId = "NOTIFY_P4";
				isExternalAccount = true;
				PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,serviceContext);
				if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
					return ; 
				}
				EBWLogger.logDebug("processRSARequest", "Notification Service for Account Executed successfully");

				//Related transaction suspension...
				EBWLogger.logDebug("processRSARequest", "Transactions Suspension...");
				SuspendAccountService objSuspendTxn = new SuspendAccountService();
				objSuspendTxn.setConnection(conn);
				objSuspendTxn.suspendTransactions(txnDetails,serviceContext);
				if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
					return ; 
				}
			}
			else  
			{
				//Call the CreateAccountLog Service with the action as "RSA Fraud"...
				paymentStatusFlag = accStatusCode;
				EBWLogger.logDebug("processRSARequest", "Logging in the channels DB");
				loggingAction=AuditTrialAction.RSAFraud;
				CreateAccountLogService objCreateAccountLogService = new CreateAccountLogService();
				objCreateAccountLogService.setConnection(conn);
				objCreateAccountLogService.setExtAccountDetailsLog(paymentStatusFlag,loggingAction,txnDetails,serviceContext);
			}
		}
		catch(SQLException sqlexception){
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally {

		}
	}
}
