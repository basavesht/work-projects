package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.NotificationInDetails;
import com.tcs.ebw.payments.transferobject.NotificationOutDetails;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class SetNotificationInput 
{
	/**
	 * Mapping the Notification Details required for Transaction or External Account...
	 * @param notificationId
	 * @param txnDetails
	 * @param isExternalAcc
	 * @param serviceContext
	 * @throws Exception
	 */
	public static void setNotificationTxnInputDetails(String notificationId,HashMap txnDetails,Boolean isExternalAcc,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace("SetNotificationInput", "Setting the notifications input parameters ...");
		NotificationInDetails objNotificationInDetails = new NotificationInDetails();
		NotificationOutDetails objNotificationOut = new NotificationOutDetails();
		try 
		{
			//Payment Details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//External Details...
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//From account Details..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//To account Details...
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			//MSUser Details..
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//Setting the isExternalAccount Flag.
			objNotificationInDetails.setExternalAccount(isExternalAcc);

			//Getting the EventDesc for the EventId from the property file. 
			String notify_event_desc = PropertyFileReader.getPropertyKeyValue("NotificationEvents",notificationId); 
			objNotificationInDetails.setEVENT_ID(notificationId);
			objNotificationInDetails.setEVENT_DESC(notify_event_desc);

			//Mapping the details..
			if(!isExternalAcc)
			{
				//Transaction Case...
				setNotifyAttrEventFlag(notificationId,objNotificationInDetails);
				objNotificationOut.setPaydebitaccnum(MSCommonUtils.getPayDebitaccnum(txnDetails));
				objNotificationOut.setPaypayeeaccnum(MSCommonUtils.getPaypayeeaccnum(txnDetails));
				objNotificationOut.setAmount(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
				objNotificationOut.setCurrency(objPaymentDetails.getTransfer_Currency());
				objNotificationOut.setExecutionDate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
				objNotificationOut.setSrcKeyAcntNumber(objFromMSAcc_Details.getKeyAccount());
				objNotificationOut.setDstnKeyAcntNumber(objToMSAcc_Details.getKeyAccount());
				objNotificationOut.setFrombr_acct_no_fa(MSCommonUtils.getCompleteMSAccNumber(objFromMSAcc_Details));
				objNotificationOut.setTobr_acct_no_fa(MSCommonUtils.getCompleteMSAccNumber(objToMSAcc_Details));
				objNotificationOut.setLifeTimeUserId(objMSUserDetails.getUuid());
				objNotificationOut.setTrial_depo(objPaymentDetails.getTrial_depo());
				objNotificationOut.setExternalAccNickName(MSCommonUtils.formatExtAcc_Notify_INIT(objExternalAccDetails));
				objNotificationOut.setTxnType(objPaymentDetails.getTransfer_Type());
				objNotificationOut.setConfirmationNumber(objPaymentDetails.getTransactionId());
				objNotificationOut.setFrequency(ConvertionUtil.convertToString(objPaymentDetails.getFrequency_DurationDesc()));
				objNotificationOut.setUntilValue(ConvertionUtil.convertToString(objPaymentDetails.getFrequency_DurationValue()));
				objNotificationOut.setUntilAmountLimit(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit()));
				objNotificationOut.setUntilEndDate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getDuration_EndDate()));
				objNotificationOut.setUntilNoOfTransfers(ConvertionUtil.convertToDouble(objPaymentDetails.getDuration_NoOfTransfers()));
				objNotificationOut.setSkippedExecutionDate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
				objNotificationOut.setNextExecutionDate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getChildTxnRequestedExecDate()));
			}
			else
			{
				//External Account Case...
				setNotifyAttrEventFlag(notificationId,objNotificationInDetails);
				objNotificationOut.setExternalAccount(objExternalAccDetails.getCpyaccnum());
				objNotificationOut.setAccountInitDate(objExternalAccDetails.getCreated_date());
				objNotificationOut.setLifeTimeUserId(objExternalAccDetails.getLife_user_id());
			}

			//Adding the Notifications input attributes in the HashMap...
			txnDetails.put("NotificationInAttributes",objNotificationInDetails);
			txnDetails.put("NotificationOutAttributes",objNotificationOut);
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Setting the attribute required flag for various event id....
	 * @param notificationId
	 * @param objNotificationInDetails
	 * @throws Exception
	 */
	public static void setNotifyAttrEventFlag(String notificationId,NotificationInDetails objNotificationInDetails) throws Exception
	{
		try 
		{
			//Setting the FLAG for the below optional ATTRNAME and ATTR VALUE for passing to the Notification Service...
			List<String> sendDstnKeyAcntNumber = new ArrayList<String>(Arrays.asList("NOTIFY_P7","NOTIFY_P8","NOTIFY_P9","NOTIFY_P10","NOTIFY_P11","NOTIFY_P12","NOTIFY_P13","NOTIFY_P15","NOTIFY_P17","NOTIFY_P18"));
			List<String> sendFrequency = new ArrayList<String>(Arrays.asList("NOTIFY_P8","NOTIFY_P10","NOTIFY_P12","NOTIFY_P20","NOTIFY_P22","NOTIFY_P24"));
			List<String> sendUntilValue = new ArrayList<String>(Arrays.asList("NOTIFY_P8","NOTIFY_P10","NOTIFY_P20","NOTIFY_P22"));
			List<String> sendSkippedTransferDate = new ArrayList<String>(Arrays.asList("NOTIFY_P13","NOTIFY_P25"));
			List<String> sendNextTransferDate = new ArrayList<String>(Arrays.asList("NOTIFY_P13","NOTIFY_P25"));
			List<String> sendAccountInitDate = new ArrayList<String>(Arrays.asList("NOTIFY_P3"));
			List<String> sendAccountSuspendDate = new ArrayList<String>(Arrays.asList("NOTIFY_P4"));
			List<String> sendTxnSuspendDate = new ArrayList<String>(Arrays.asList("NOTIFY_P17","NOTIFY_P18"));
			List<String> sendReturnCodeDesc = new ArrayList<String>(Arrays.asList("NOTIFY_P17","NOTIFY_P18"));

			//Setting the DstnKeyAcntNumber value.
			if(sendDstnKeyAcntNumber.contains(notificationId)){
				objNotificationInDetails.setDsntKeyAcntNumber(true);
			}
			//Setting the UnitlValue value.
			if(sendFrequency.contains(notificationId)){
				objNotificationInDetails.setFrequency(true);
			}
			//Setting the UnitlValue value.
			if(sendUntilValue.contains(notificationId)){
				objNotificationInDetails.setUnitlValue(true);
			}
			//Setting the SkippedExecDate value.
			if(sendSkippedTransferDate.contains(notificationId)){
				objNotificationInDetails.setSkippedExecDate(true);
			}
			//Setting the NextTransferDate value.
			if(sendNextTransferDate.contains(notificationId)){
				objNotificationInDetails.setNextTransferDate(true);
			}
			//Setting the AccountInitDate value.
			if(sendAccountInitDate.contains(notificationId)){
				objNotificationInDetails.setAccountInitDate(true);
			}
			//Setting the AccountSuspendDate value.
			if(sendAccountSuspendDate.contains(notificationId)){
				objNotificationInDetails.setAccountSuspendDate(true);
			}
			//Setting the TxnSuspendDate value.
			if(sendTxnSuspendDate.contains(notificationId)){
				objNotificationInDetails.setTxnSuspendDate(true);
			}
			//Setting the ReturnCodeDesc value.
			if(sendReturnCodeDesc.contains(notificationId)){
				objNotificationInDetails.setReturnCodeDesc(true);
			}
		}
		catch(Exception exception){
			throw exception;
		}
	}
}
