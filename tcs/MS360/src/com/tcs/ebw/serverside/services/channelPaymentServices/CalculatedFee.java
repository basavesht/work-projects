package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.FEE_CHARGE_OUT;
import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.mswitch.common.channel.SI_RETURN;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *   224703       15-06-2011        3               Print Branch Validation
 * **********************************************************
 */
public class CalculatedFee 
{
	/**
	 * Calculated fee extraction...
	 * @param out
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static CalculatedFee getFee(HashMap txnDetails,Vector<Object> out, ServiceContext context) throws Exception 
	{
		EBWLogger.logDebug("CalculatedFee","Fee charge Extraction and mapping process");
		CalculatedFee feeCharge = new CalculatedFee();
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Check request mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			//Interface Output attributes...
			SI_RETURN si_return = (SI_RETURN)out.get(0);

			//Output Extraction and storage...
			if(si_return.getReturnCode()==0) 
			{
				//Interface Output attributes...
				FEE_CHARGE_OUT objCalculatedFee = new FEE_CHARGE_OUT();

				//Output Extraction and storage in payment Details.....
				objCalculatedFee = (FEE_CHARGE_OUT)si_return.getOutputVector().get(0);
				objPaymentDetails.setTxn_Fee_Charge(objCalculatedFee.getCHARGED_AMNT());
				objCheckRequest.setFee(objCalculatedFee.getCHARGED_AMNT());

				//Fee charge validation ..
				Double pickUpOption = objCheckRequest.getPickUpOption();
				if(pickUpOption!=null && pickUpOption.equals(2D)){
					BigDecimal feeAmount = objCalculatedFee.getCHARGED_AMNT();
					if(feeAmount == null || (feeAmount.compareTo(new BigDecimal(0)) <= 0)){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.NO_FEE_CHARGE_OVERNIGHT_MAIL);
					}
				}
			}
			else 
			{
				EBWLogger.logDebug("CalculatedFee","The Fee charge call was failure....");
				for(int k=0;k<si_return.getErrorVector().size();k++){
					if(si_return.getErrorVector().get(k) instanceof MS_INTERFACE_TECH_FAILURE ){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
						break;
					}
				}
			} 
		}
		catch(Exception exception) {
			throw exception;
		}
		return feeCharge;
	}
}
