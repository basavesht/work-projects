package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.BR_VALIDATION_LOG;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.mswitch.common.channel.SI_RETURN;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class BRLogInterfaceIn {

	public BRLogInterfaceIn(){

	}

	/**
	 * Setting the input parameters for the BR Validation Log Input...
	 * @param txnDetails
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Vector setBRLogInputDetails(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace("BRLogInterfaceIn", "Setting the BR Log input details...");
		Vector brValidationInVector= new Vector();
		try 
		{
			//Mapping the payment attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the business rule attributes...
			BusinessRulesService objBusinessRule =new BusinessRulesService();
			if(txnDetails.containsKey("BROutputDetails")){
				objBusinessRule = (BusinessRulesService)txnDetails.get("BROutputDetails");
			}

			// Setting the Confirmation  No in the BR_VALIDATION Vector...
			SI_RETURN si_return = (SI_RETURN)objBusinessRule.getBrValidationOut().get(0);
			if(si_return.getReturnCode() == 0)
			{
				if(si_return.getReturnCode() == 0)
				{
					for(int i=0;i<si_return.getOutputVector().size();i++)
					{
						BR_VALIDATION_LOG objBr_validation_log = new BR_VALIDATION_LOG();
						objBr_validation_log = (BR_VALIDATION_LOG)si_return.getOutputVector().get(i);
						if(objBr_validation_log!=null)
						{
							String error_level = objBr_validation_log.getERR_LVL();
							if(error_level!=null){
								objBr_validation_log.setBO_KEY(objPaymentDetails.getTransactionId());
								brValidationInVector.add(objBr_validation_log);
							}
						}
					}
				}
			}
		} 
		catch(Exception exception) {
			throw exception;
		}
		return brValidationInVector;
	}
}
