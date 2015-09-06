package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.MS_CHK_NBR_IN;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class CheckNumberInterfaceIn {

	/**
	 * CheckNumber call ...
	 * @param txnDetails
	 * @return
	 * @throws Exception 
	 */
	public static Vector setCheckNumberInput(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.logDebug("CheckNumberInterfaceIn", "Setting the CheckNumber Inputs..");
		Vector checkNumberVector = new Vector();
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

			//Check number interface In...
			MS_CHK_NBR_IN objCheckNumberIn = new MS_CHK_NBR_IN();
			objCheckNumberIn.setCALLER_PGMID(WSDefaultInputsMap.getCheckNumCallerPgmId(serviceContext));
			objCheckNumberIn.setCALLER_APPL(WSDefaultInputsMap.getCheckNumCallerAppId(serviceContext));  
			objCheckNumberIn.setMMS_TRANS_ID(objPaymentDetails.getTransactionId());
			objCheckNumberIn.setBRANCH_NUMBER(new Integer(objCheckRequest.getPrintingBranch()).intValue());
			objCheckNumberIn.setMAILING_ADDR_LINE1(objCheckRequest.getPayeeName());
			objCheckNumberIn.setMAILING_ADDR_LINE2(objCheckRequest.getDeliveryAddrLine1());
			objCheckNumberIn.setMAILING_ADDR_LINE3(objCheckRequest.getDeliveryAddrLine2());
			objCheckNumberIn.setMAILING_ADDR_LINE4(objCheckRequest.getDeliveryAddrLine3());
			objCheckNumberIn.setMAILING_ADDR_LINE5(objCheckRequest.getDeliveryAddrLine4());
			objCheckNumberIn.setAMOUNT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			objCheckNumberIn.setCHECK_TYPE(MSCommonUtils.getCheckNumber_ChkType(objCheckRequest));
			objCheckNumberIn.setLS_EXPAND("");

			//Adding the RTAB Input Object to the Vector...
			checkNumberVector.add(objCheckNumberIn);	
		} 
		catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
		EBWLogger.logDebug("CheckNumberInterfaceIn", "Input parameters for the CheckNumber is"+checkNumberVector.toString());
		return checkNumberVector;
	}

}
