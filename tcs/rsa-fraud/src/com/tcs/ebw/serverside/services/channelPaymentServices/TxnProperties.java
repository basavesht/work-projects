package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.DateUtilities.DateFunctions;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.payments.transferobject.DsPayTxnsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 * **********************************************************
 */
public class TxnProperties 
{
	/**
	 * Calculate Transaction Settlement date to be passed to Payments service..
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public static void calculateTxnSettleDate(HashMap txnDetails,DsPayTxnsTO ds_pay_txns,ServiceContext serviceContext) throws Exception 
	{
		boolean isTransferCutOffPassed = false;
		boolean isTransferDtHoliday = false;
		boolean isDirectDBcall = true;
		try
		{
			//Payment Details..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			String transferType = objPaymentDetails.getTransfer_Type();
			Date settleDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());

			//Default Mappings..
			ds_pay_txns.setActual_exe_dt(ConvertionUtil.convertToTimestamp(settleDate));
			ds_pay_txns.setPayabpaysendflag("N");

			if(MSCommonUtils.check24x7Access(transferType)) 
			{
				//Check if the Transfer date is holiday or not...
				Calendar transferDate_Cal = Calendar.getInstance();
				transferDate_Cal.setTime(settleDate);
				isTransferDtHoliday = DateFunctions.checkHoliday(transferDate_Cal,transferType,isDirectDBcall,serviceContext);

				//Check the Cut Off Time ...
				if(!isTransferDtHoliday) {
					ValidateCutOffTime objValidateCutOffTime = new ValidateCutOffTime();
					objValidateCutOffTime.setConnection(serviceContext.getConnection());
					objValidateCutOffTime.checkCutOffTime_Error(txnDetails,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						isTransferCutOffPassed = true;
						serviceContext.removeMessage(MessageType.SEVERE,ChannelsErrorCodes.CUT_OFF_TIME_EXCEEDED);
					}
				}

				//Setting the settle date and margin booking flag . If flag is N then transaction has to go in current date's margin..
				if(isTransferDtHoliday || isTransferCutOffPassed){
					settleDate = DateFunctions.getNextFutureBusinessDay(transferDate_Cal, transferType, isDirectDBcall, serviceContext);
					ds_pay_txns.setActual_exe_dt(ConvertionUtil.convertToTimestamp(settleDate));
					ds_pay_txns.setPayabpaysendflag("Y");
				}
			}
		}
		catch(Exception exception){
			throw exception;
		}
	}
}
