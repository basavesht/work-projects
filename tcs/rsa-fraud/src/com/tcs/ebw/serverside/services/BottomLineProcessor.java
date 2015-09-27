package com.tcs.ebw.serverside.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.Payments.EAITO.MS_BOTTOM_LINE_ASYNC_IN;
import com.tcs.Payments.EAITO.MS_BOTTOM_LINE_ASYNC_OUT;
import com.tcs.Payments.ms360Utils.AsyncBTUtilities;
import com.tcs.Payments.ms360Utils.AuditTrialAction;
import com.tcs.Payments.serverValidations.ValidateBTRequest;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.Message;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.channelPaymentServices.CreatePaymentLogService;
import com.tcs.ebw.serverside.services.channelPaymentServices.GetQZBusinessDate;
import com.tcs.ebw.serverside.services.channelPaymentServices.IdentifyTransaction;
import com.tcs.ebw.serverside.services.channelPaymentServices.MapPaymentInputDetails;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class BottomLineProcessor  extends DatabaseService 
{
	private transient Connection conn = null;

	public BottomLineProcessor() {

	}

	/**
	 * Response handler after executing the CallBack BT Print service as a whole...
	 * @param asynchBTRequest
	 * @param serviceContext
	 */
	public MS_BOTTOM_LINE_ASYNC_OUT processAsynchBTRequest(MS_BOTTOM_LINE_ASYNC_IN asynchBTRequest)
	{
		conn = (Connection)serviceConnection;
		MS_BOTTOM_LINE_ASYNC_OUT reponseOut = new MS_BOTTOM_LINE_ASYNC_OUT();
		ServiceContext serviceContext = new ServiceContext();
		try 
		{
			//Executing the BT CallBack business implementation...
			serviceContext.setConnection(conn);
			executeBTRequest(asynchBTRequest,serviceContext);
		} 
		catch (Exception exception) {
			exception.printStackTrace();
		}
		finally {
			//BT Response creator....
			AsyncBTUtilities.ResponseCreator(reponseOut,serviceContext);
		}
		return reponseOut;
	}

	/**
	 * Wrapper service to execute the BT Request and send the response code and message..
	 * @param asynchBTRequest
	 * @param context
	 * @return
	 */
	public void executeBTRequest(MS_BOTTOM_LINE_ASYNC_IN asynchBTRequest,ServiceContext serviceContext)
	{
		boolean isTxnExists = false;
		try 
		{
			//1.Validate BT Request Object...
			ValidateBTRequest validateReq = new ValidateBTRequest();
			validateReq.validateBTRequest(asynchBTRequest, serviceContext);
			if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity()== MessageType.SEVERE){
				return;
			}

			//2.Transaction identification in MM System..
			IdentifyTransaction identifyTxn = new IdentifyTransaction();
			identifyTxn.setConnection(conn);
			isTxnExists = identifyTxn.identifyTransaction(asynchBTRequest, serviceContext);
			if(isTxnExists) {
				processBTRequest(asynchBTRequest, serviceContext);
			}
			else {
				serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.TXN_NOT_IDENTIFIED);
				return;
			}

			//MM DB Transaction Commit...
			serviceContext.setServiceCallSuccessful(true);
			((Connection)conn).commit();
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
	 * BT Request execution..
	 * @param asynchBTRequest
	 * @param context
	 */
	public void processBTRequest(MS_BOTTOM_LINE_ASYNC_IN asynchBTRequest,ServiceContext serviceContext) throws SQLException,Exception 
	{
		EBWLogger.trace("processBTRequest", "Starting processRSAGenuineCase method in Implementation class");
		int paymentStatusFlag=0;
		HashMap txnDetails = new HashMap();
		String loggingAction ="";
		int txnStatusCode = 0;
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		Object[] toObjects = new Object[2];
		try
		{
			//BT Extractor creator....
			toObjects = AsyncBTUtilities.RequestExtractor_Transaction(asynchBTRequest,serviceContext);

			//Mapping the Payment details (Internal/External Cancel Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setPaymentConfirmDetails(toObjects,serviceContext);

			//Get the business Date from the QZ_Dates View 
			EBWLogger.logDebug("processBTRequest", "Getting the Business Date");
			GetQZBusinessDate getBusinessDt = new GetQZBusinessDate();
			getBusinessDt.setConnection(conn);
			getBusinessDt.getQzBusinessDate(txnDetails,serviceContext);
			EBWLogger.logDebug("processBTRequest", "Finished getting Business Date : ");

			//Mapping the payment attributes...
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Payment Details..
			txnStatusCode = ConvertionUtil.convertToint(objPaymentDetails.getTxnCurrentStatusCode());

			//Call the CreatePaymentDetailsLog Service with the action as "RSA CallBack"...
			paymentStatusFlag = txnStatusCode;
			EBWLogger.logDebug("processBTRequest", "Logging in the channels DB  ");
			loggingAction=AuditTrialAction.BTCallback;
			CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
			objCreatePaymentLogService.setConnection(conn);
			objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,serviceContext);
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
