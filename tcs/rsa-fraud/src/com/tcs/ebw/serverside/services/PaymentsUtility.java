package com.tcs.ebw.serverside.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.bancs.EAI.MO_SI_HEADER;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountBalance;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.BRLimitCheckInterfaceIn;
import com.tcs.ebw.serverside.services.channelPaymentServices.BRLogInterfaceIn;
import com.tcs.ebw.serverside.services.channelPaymentServices.BRLogValidate;
import com.tcs.ebw.serverside.services.channelPaymentServices.BusinessRuleInterfaceIn;
import com.tcs.ebw.serverside.services.channelPaymentServices.BusinessRulesService;
import com.tcs.ebw.serverside.services.channelPaymentServices.LimitRequestProcessor;
import com.tcs.ebw.serverside.services.channelPaymentServices.MerlinInterfaceIn;
import com.tcs.ebw.serverside.services.channelPaymentServices.NotifyOutDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.PaymentHubInterfaceIn;
import com.tcs.ebw.serverside.services.channelPaymentServices.PaymentOutDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.RTABInterfaceIn;
import com.tcs.ebw.serverside.services.channelPaymentServices.RTAInterfaceIn;
import com.tcs.ebw.serverside.services.channelPaymentServices.RTAOutDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.RTARequestProcessor;
import com.tcs.ebw.serverside.services.channelPaymentServices.SendNotifications;
import com.tcs.ebw.serverside.services.channelPaymentServices.SetNotificationInput;
import com.tcs.ebw.serverside.services.channelPaymentServices.UpdateRTABookedInFlag;
import com.tcs.mswitch.protocol.api.JavaAPIHelper;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 * **********************************************************
 */
public class PaymentsUtility {

	public final static String CONFIGPATH="Channels";

	/**** 
	 * 
	 * Interface : Channel --> SI --> RTAB 
	 *
	 ****/
	public static void getAccountBalance(HashMap txnDetails,final ServiceContext context)throws Exception,SQLException
	{
		EBWLogger.trace("PaymentsUtility", "Starting getAccountBalance method in PaymentsUtility class");
		AccountBalance objAccBalance = new AccountBalance();
		try 
		{
			//Interface attributes..
			Vector<Object> rtabSIOutput = null;

			//Mapping the interface input vectors...
			Vector<Object> rtabInputVector = RTABInterfaceIn.setRTABInputDetails(txnDetails,context);
			Vector<Object> rtabCtrlVector = setInterfaceCntrlVector("MSCHIO","RTAB",context);

			//Invoking Interface call...
			JavaAPIHelper.init(CONFIGPATH);
			rtabSIOutput = JavaAPIHelper.processMessage(rtabInputVector, rtabCtrlVector);
			if(rtabSIOutput!=null && !rtabSIOutput.isEmpty()){
				objAccBalance = AccountBalance.getBalance(rtabSIOutput,context);
				txnDetails.put("RTABOutputDetails",objAccBalance);
			}
			else{
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace("PaymentsUtility", "Finished getAccountBalance method in PaymentsUtility class");
	}

	/**** 
	 * 
	 * Interface : Channel --> SI --> Account View...
	 *
	 ****/

	public static void getAccountDetails(HashMap txnDetails,final ServiceContext context)throws Exception,SQLException
	{
		EBWLogger.trace("PaymentsUtility", "Started getAccountDetails method in PaymentsUtility class");
		AccountDetails objAccDetails = new AccountDetails();
		try 
		{
			//Interface attributes..
			Vector<Object> accountViewSIOutput = null;

			//Mapping the interface input vectors...
			Vector<Object> merlinInputVector = MerlinInterfaceIn.setMerlinInputDetails(txnDetails,context);
			Vector<Object> merlinCtrlVector = setInterfaceCntrlVector("MSCHIO","AcntSrch",context);

			//Invoking Interface call...
			JavaAPIHelper.init(CONFIGPATH);
			accountViewSIOutput = JavaAPIHelper.processMessage(merlinInputVector, merlinCtrlVector);	
			if(accountViewSIOutput!=null && !accountViewSIOutput.isEmpty()){
				objAccDetails= AccountDetails.getAccountDetails(accountViewSIOutput,context);
				txnDetails.put("MerlinOutputDetails",objAccDetails);
			}
			else{
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
			}
			EBWLogger.trace("PaymentsUtility", "Finished getAccountDetails method in PaymentsUtility class");
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**** 
	 * 
	 * RTA Request processor and handler...
	 *
	 ****/
	public static void processRTARequest(HashMap txnDetails,final ServiceContext context) throws Exception,SQLException
	{
		EBWLogger.trace("PaymentsUtility", "Started processRTARequest method in PaymentsUtility class");
		boolean isRTAActionRequired = false;
		try 
		{
			//Mapping the Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Check request mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			//Getting the RTA Booked In flag and RTA Action key...
			isRTAActionRequired = RTARequestProcessor.getRTAAction(txnDetails);
			if(isRTAActionRequired)
			{
				//Processing the RTA request for the transaction case...
				PaymentsUtility.processRTATxnRequest(txnDetails,context);
				if (context.getMaxSeverity()== MessageType.CRITICAL || context.getMaxSeverity() == MessageType.SEVERE){
					return ; 
				}

				//Processing the RTA request for the fee case only if Fee amount is applicable...
				if(objPaymentDetails.isFeeApplicable()){
					if(objCheckRequest!=null && objCheckRequest.getChargedTo()!=null && objCheckRequest.getChargedTo().equalsIgnoreCase(ChkReqConstants.CHARGE_TO_CLIENT)){
						PaymentsUtility.processRTAFeeRequest(txnDetails,context);
						if (context.getMaxSeverity()== MessageType.CRITICAL || context.getMaxSeverity() == MessageType.SEVERE){
							return ; 
						}
					}
				}
			}
			EBWLogger.trace("PaymentsUtility", "Finished processRTARequest method in PaymentsUtility class");
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**** 
	 * 
	 * Interface : Channel --> SI --> RTA (TxnCase)
	 *
	 ****/
	public static void processRTATxnRequest(HashMap txnDetails,final ServiceContext context) throws Exception,SQLException
	{
		EBWLogger.trace("PaymentsUtility", "Started processRTATxnRequest method in PaymentsUtility class");
		try 
		{
			//Interface attributes..
			Vector<Object> rtaSIOutput = null;

			//Mapping the interface input vectors...
			Vector<Object> rtaInputVector = RTAInterfaceIn.setRTATxnRequest(txnDetails,context);
			Vector<Object> rtaCtrlVector = setInterfaceCntrlVector("MSCHIO","RTA",context);

			//Invoking Interface call...
			JavaAPIHelper.init(CONFIGPATH);
			rtaSIOutput = JavaAPIHelper.processMessage(rtaInputVector, rtaCtrlVector);
			if(rtaSIOutput!=null && !rtaSIOutput.isEmpty())
			{
				RTAOutDetails.checkRTACall(rtaSIOutput,context);
				if(context.getMaxSeverity()== MessageType.CRITICAL || context.getMaxSeverity()==MessageType.SEVERE){
					return;
				}

				//Update RTA Booked In Flag in Ds_Pay_Txns Table..
				UpdateRTABookedInFlag objRTABooking = new UpdateRTABookedInFlag();
				objRTABooking.setConnection(context.getConnection());
				objRTABooking.updateBooked_InFlg(txnDetails);
			}
			else{
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
			}
			EBWLogger.trace("PaymentsUtility", "Finished processRTATxnRequest method in PaymentsUtility class");
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**** 
	 * 
	 * Interface : Channel --> SI --> RTA (Fee case)
	 *
	 ****/
	public static void processRTAFeeRequest(HashMap txnDetails,final ServiceContext context) throws Exception,SQLException
	{
		EBWLogger.trace("PaymentsUtility", "Started processRTAFeeRequest method in PaymentsUtility class");
		boolean isRTAActionRequired = false;
		try 
		{
			//Getting the RTA Action key for fee amount...
			isRTAActionRequired = RTARequestProcessor.getRTAAction_Fee(txnDetails);
			if(isRTAActionRequired)
			{
				//Interface attributes..
				Vector<Object> rtaSIOutput = null;

				//Mapping the interface input vectors...
				Vector<Object> rtaInputVector = RTAInterfaceIn.setRTAFeeRequest(txnDetails,context);
				Vector<Object> rtaCtrlVector = setInterfaceCntrlVector("MSCHIO","RTA",context);

				//Invoking Interface call...
				JavaAPIHelper.init(CONFIGPATH);
				rtaSIOutput = JavaAPIHelper.processMessage(rtaInputVector, rtaCtrlVector);
				if(rtaSIOutput!=null && !rtaSIOutput.isEmpty())
				{
					RTAOutDetails.checkRTACall(rtaSIOutput,context);
					if(context.getMaxSeverity()== MessageType.CRITICAL || context.getMaxSeverity()==MessageType.SEVERE){
						return;
					}
				}
				else{
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
				}
				EBWLogger.trace("PaymentsUtility", "Finished processRTAFeeRequest method in PaymentsUtility class");
			} 
		}
		catch (Exception exception) {
			throw exception;
		}
	}

	/**** 
	 * 
	 * Interface : Channel --> SI --> Payments Hub ( Remittance or DDRemittance )
	 *
	 ****/
	public static void executePaymentsHub(HashMap txnDetails,final ServiceContext context)throws Exception,SQLException
	{
		EBWLogger.trace("PaymentsUtility", "Calling the Payments Hub service thorugh SI");
		ArrayList objPayOutDetails = new ArrayList();
		try 
		{
			//Getting the RTA Booked In flag...
			RTARequestProcessor.getRTAAction(txnDetails);

			//Interface attributes..
			Vector<Object> paymentHubSIOutput = null;

			//Mapping the interface input vectors...
			Vector<Object> paymentsInputVector = PaymentHubInterfaceIn.setPaymentHubParams_Transfer(txnDetails,context);
			Vector<Object> paymentsCtrlVector = setInterfaceCntrlVector("MSCHIO","STP",context);

			//Invoking Interface call...
			JavaAPIHelper.init(CONFIGPATH);
			paymentHubSIOutput = JavaAPIHelper.processMessage(paymentsInputVector, paymentsCtrlVector);
			if(paymentHubSIOutput!=null && !paymentHubSIOutput.isEmpty())
			{
				objPayOutDetails= PaymentOutDetails.getPaymentOutDetails(paymentHubSIOutput,context);
				if(context.getMaxSeverity()== MessageType.CRITICAL || context.getMaxSeverity()==MessageType.SEVERE){
					return;
				}
				txnDetails.put("HubOuputDetails", objPayOutDetails);

				//Update RTA Booked In Flag in Ds_Pay_Txns Table..
				UpdateRTABookedInFlag objRTABooking = new UpdateRTABookedInFlag();
				objRTABooking.setConnection(context.getConnection());
				objRTABooking.updateBooked_InFlg(txnDetails);
			}
			else {
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
			}
			EBWLogger.trace("PaymentsUtility", "Finished the Payments Hub service thorugh SI");
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**** 
	 * 
	 * Interface : Channel --> SI --> Payments Hub (Remove Booking during Txn cancellation Remittance or DDRemittance )
	 *
	 ****/
	public static void executePaymentHubCancel(HashMap txnDetails,final ServiceContext context)throws Exception,SQLException
	{
		EBWLogger.trace("PaymentsUtility", "Calling the Payments Hub service through SI");
		ArrayList objPayOutDetails = new ArrayList();
		try 
		{
			//Interface attributes..
			Vector<Object> paymentHubSIOutput = null;

			//Mapping the interface input vectors...
			Vector<Object> paymentsInputVector = PaymentHubInterfaceIn.setPaymentHubParams_CancelTxn(txnDetails,context);
			Vector<Object> paymentsCtrlVector = setInterfaceCntrlVector("MSCHIO","STP",context);

			//Invoking Interface call...
			JavaAPIHelper.init(CONFIGPATH);
			paymentHubSIOutput = JavaAPIHelper.processMessage(paymentsInputVector, paymentsCtrlVector);
			if(paymentHubSIOutput!=null && !paymentHubSIOutput.isEmpty())
			{
				objPayOutDetails= PaymentOutDetails.getCancelPaymentOutDetails(paymentHubSIOutput,context);
				if(context.getMaxSeverity()== MessageType.CRITICAL || context.getMaxSeverity()==MessageType.SEVERE){
					return;
				}
				txnDetails.put("HubOuputDetails", objPayOutDetails);
			}
			else {
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
			}
			EBWLogger.trace("PaymentsUtility", "Finished the Payments Hub service thorugh SI");
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * 
	 * Interface : Channel --> SI --> BusinessRules Service for Transaction Rule..
	 * @throws Exception 
	 *
	 ****/

	public static void executeBRTransaction(HashMap txnDetails,final ServiceContext context) throws Exception
	{
		EBWLogger.trace("PaymentsUtility", "Calling the Business Rule Service (Transaction) through SI from Channels");
		BusinessRulesService objBusinessRulesService = new BusinessRulesService();
		try 
		{
			//Interface attributes..
			Vector<Object> businessRuleSIOutput = null;

			//Mapping the interface input vectors...
			Vector<Object> blockDispInputVector = BusinessRuleInterfaceIn.setBRInterfaceTransactionRuleIn(txnDetails,context);
			Vector<Object> blockDispCtrlVector = setInterfaceCntrlVector("MSCHIO","STP",context);

			//Invoking Interface call...
			JavaAPIHelper.init(CONFIGPATH);
			businessRuleSIOutput = JavaAPIHelper.processMessage(blockDispInputVector, blockDispCtrlVector);
			if(businessRuleSIOutput!=null && !businessRuleSIOutput.isEmpty()){
				objBusinessRulesService= BusinessRulesService.getBusinessRuleOutput(txnDetails,businessRuleSIOutput,context);
				txnDetails.put("BROutputDetails", objBusinessRulesService);
			}
			else{
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/***
	 * 
	 * Interface : Channel --> SI --> BR Validation Logging..
	 * @throws Exception 
	 *
	 ****/
	public static void executeBRLogging(HashMap txnDetails,final ServiceContext context) throws Exception
	{
		EBWLogger.trace("PaymentsUtility","Calling the BR Validation Logging through SI from Channels");
		try 
		{
			//Interface attributes..
			Vector<Object> brVaildateLogSIOut = null;

			//Mapping the interface input vectors...
			Vector<Object> brLogInputVector = BRLogInterfaceIn.setBRLogInputDetails(txnDetails,context);
			Vector<Object> brLogCntrlVector = setInterfaceCntrlVector("MSCHIO","STP",context);

			//Invoking Interface call...
			JavaAPIHelper.init(CONFIGPATH);
			brVaildateLogSIOut = JavaAPIHelper.processMessage(brLogInputVector, brLogCntrlVector);
			if(brVaildateLogSIOut!=null && !brVaildateLogSIOut.isEmpty()){
				BRLogValidate.chechBRUpdate(brVaildateLogSIOut,context);
			}
			else{
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/***
	 * 
	 * Interface : Channel --> SI --> Send Notifications
	 * @throws Exception 
	 *
	 ****/
	public static void sendNotificationDetails(HashMap txnDetails,String notificationEventId,boolean isExternalAccount,ServiceContext context) throws Exception
	{
		EBWLogger.trace("PaymentsUtility","Calling the Notification Engine through SI from Channels");
		try 
		{
			//Interface attributes..
			Vector<Object> notifySIOut = null;
			SendNotifications objSendNotifications  = new SendNotifications();

			//Mapping the interface input vectors...
			SetNotificationInput.setNotificationTxnInputDetails(notificationEventId,txnDetails,isExternalAccount,context);
			Vector<Object> notifyInputVector = objSendNotifications.setNotificationDetails(txnDetails,context);
			Vector<Object> notifyCntrlVector = setInterfaceCntrlVector("MSCHIO","STP",context);

			//Invoking Interface call...
			JavaAPIHelper.init(CONFIGPATH);
			notifySIOut = JavaAPIHelper.processMessage(notifyInputVector, notifyCntrlVector);
			if(notifySIOut!=null && !notifySIOut.isEmpty()){
				NotifyOutDetails.checkNotificationAck(notifySIOut,context);
			}
			else{
				context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
	}


	/***
	 * The Interface is used to call limit check through business Rule service ....
	 * Interface : Channel --> SI --> Business Rule ---> Limit check ...
	 * Create Limits will be called in case the transaction is current owned by Client only.
	 * @throws Exception 
	 *
	 ****/

	public static void processLimitRequest(HashMap txnDetails,final ServiceContext context) throws Exception
	{
		EBWLogger.trace("PaymentsUtility","Calling the Business Rules for Limit check through SI from Channels...");
		boolean isLimitActionReq = false;
		try 
		{
			isLimitActionReq = LimitRequestProcessor.getLimitAction(txnDetails);
			if(isLimitActionReq)
			{
				//Interface attributes..
				Vector<Object> limitBRSIOut = new Vector<Object>();

				//Mapping the interface input vectors...
				Vector<Object> limitBRInputVector = BRLimitCheckInterfaceIn.setLimitParameters(txnDetails,context);
				Vector<Object> limitBRCntrlVector = setInterfaceCntrlVector("MSCHIO","STP",context);

				//Invoking Interface call...
				JavaAPIHelper.init(CONFIGPATH);
				limitBRSIOut = JavaAPIHelper.processMessage(limitBRInputVector, limitBRCntrlVector);
				if(limitBRSIOut!=null && !limitBRSIOut.isEmpty()){
					BusinessRulesService.getBusinessRuleOutput(txnDetails,limitBRSIOut,context);
				}
				else{
					context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
				}
			}
			else {
				EBWLogger.logDebug("PaymentsUtility","Limit checks not applicable for this transaction as per the entitlements.");
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Setting the MO_SI_HEADER Object for all the interfaces ....
	 * @param interfaceId
	 * @param msgType
	 * @return
	 * @throws Exception 
	 */
	public static Vector setInterfaceCntrlVector(String interfaceId,String msgType,ServiceContext context) throws Exception
	{
		Vector<Object> interfaceCntrlVec = new Vector<Object>();
		try 
		{
			MO_SI_HEADER objMO_SI_HEADER = new MO_SI_HEADER();

			//Mapping the interface name details..
			StringBuffer interfaceName = new StringBuffer();
			interfaceName.append(interfaceId);
			objMO_SI_HEADER.setINTFID(interfaceName);

			//Mapping the interface message details..
			StringBuffer msgTypeValue = new StringBuffer();
			msgTypeValue.append(msgType);
			objMO_SI_HEADER.setMESSAGE_TYPE(msgTypeValue);

			//Putting in the Interface Vector..
			interfaceCntrlVec.add(objMO_SI_HEADER);
			interfaceCntrlVec.add(context.getConnection());
		} 
		catch(Exception exception){
			throw exception;
		}
		return interfaceCntrlVec;
	}
}
