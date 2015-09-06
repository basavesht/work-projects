package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class CheckRecurringStatus 
{
	/**
	 * Validate the recurring criteria..
	 * @param txnDetails
	 * @return
	 */
	public static boolean checkRecurringStatus(HashMap txnDetails)
	{
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		if(txnDetails.containsKey("PaymentDetails")){
			objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
		}

		boolean isRecurringFlag = true;
		int durationSelected = ConvertionUtil.convertToint(objPaymentDetails.getFrequency_DurationValue());
		Date nextBusinessDate = objPaymentDetails.getNext_txn_dt();
		BigDecimal accumulatedAmount = objPaymentDetails.getAccumulated_amount();
		BigDecimal paymentAmount = ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount());
		BigDecimal thresholdAmount = ConvertionUtil.convertToBigDecimal(objPaymentDetails.getDuration_AmountLimit());
		Double maxTxnNo = ConvertionUtil.convertToDouble(objPaymentDetails.getDuration_NoOfTransfers());
		Double accumulatedTxnNo =	objPaymentDetails.getAccumulated_transfers();
		Date untildate = ConvertionUtil.convertToDate(objPaymentDetails.getDuration_EndDate());

		//Logic for checking the next instance....
		if((durationSelected==1) || 
				((durationSelected==2 && !nextBusinessDate.after(untildate))) || 
				((durationSelected==4 && ((accumulatedTxnNo.doubleValue())< maxTxnNo.doubleValue())))){
			isRecurringFlag = true;
		}
		else if (durationSelected==3 && (accumulatedAmount.compareTo(thresholdAmount)== -1)){ 
			BigDecimal remaining_amnt = thresholdAmount.subtract(accumulatedAmount);
			if(remaining_amnt.compareTo(paymentAmount)==-1){
				objPaymentDetails.setChildTxnAmount(ConvertionUtil.convertToString(remaining_amnt));
			}
			else{
				objPaymentDetails.setChildTxnAmount(ConvertionUtil.convertToString(paymentAmount));
			}
			isRecurringFlag = true;
		}
		else {
			isRecurringFlag = false;
		}
		return isRecurringFlag;
	}
}
