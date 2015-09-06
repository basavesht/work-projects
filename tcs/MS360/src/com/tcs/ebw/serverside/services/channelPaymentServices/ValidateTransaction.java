/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.Date;
import java.util.HashMap;

import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
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
public class ValidateTransaction 
{
	/**
	 * Cancel CASE ONLY...
	 * This check should be done only for executed transaction which are in status Executed and have the execution date as today (Both One-time and Recurring)
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public void validateCancelRequest(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		boolean isTxnValid = true;
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Needs to checked for the Reject case only...
			String txn_status = objPaymentDetails.getTxnCurrentStatusCode();
			Date settleDate = ConvertionUtil.convertToDate(objPaymentDetails.getActualExecDate());
			Date businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());

			if(txn_status!=null && txn_status.equalsIgnoreCase("4")){
				if(settleDate!=null && businessDate!=null && settleDate.compareTo(businessDate) < 0 ){
					isTxnValid = false;
				}
			}

			//If the action eligibility check is failed then Critical error message is added to the context...
			if(!isTxnValid){
				EBWLogger.logDebug(this, "Transactions executed on a past date cannot be cancelled");
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.CANCEL_TXN_NOT_ALLOWED);
			}
			else {
				EBWLogger.logDebug(this, "Transaction is allowed for cancellation");
			}
		} 
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}
}
