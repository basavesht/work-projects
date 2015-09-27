package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.FEE_CHARGE_INP;
import com.tcs.Payments.ms360Utils.BRUtils;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class FeeChargeInterfaceIn {

	/**
	 * FEE CHARGE INP for invoke...
	 * @param txnDetails
	 * @return
	 * @throws Exception 
	 */
	public static Vector setFeeChargeDetails(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.logDebug("FeeChargeInterfaceIn", "Setting the Fee Charge Inputs..");
		Vector feeChargeInpVector = new Vector();
		try 
		{
			//Check request mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//From account mappings ...
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//FEE Charge Input mappings...
			FEE_CHARGE_INP objFeeCharge = new FEE_CHARGE_INP();
			objFeeCharge.setTXN_TYPE(objPaymentDetails.getTransfer_Type());
			objFeeCharge.setTXN_AMOUNT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			objFeeCharge.setCHK_PICK_UP_OPTION(BRUtils.getFeePickUpOption(objPaymentDetails, objCheckRequest));
			objFeeCharge.setCHK_CERTIFIED_FLAG(objCheckRequest.getCertifiedFlag());
			objFeeCharge.setTIER(objFromMSAcc_Details.getAccount_tier());

			//Adding the FEE Charge Object to the Vector...
			feeChargeInpVector.add(objFeeCharge);	
		} 
		catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
		EBWLogger.logDebug("FeeChargeInterfaceIn", "Input parameters for the FEE Charge is"+feeChargeInpVector.toString());
		return feeChargeInpVector;
	}
}
