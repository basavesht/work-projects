package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsPayTxnsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class UpdateRTABookedInFlag extends DatabaseService{

	/**
	 * RTA Booked In flag update in the transaction table (Ds_Pay_Txns)
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void updateBooked_InFlg(HashMap txnDetails) throws Exception
	{
		EBWLogger.trace(this, "Starting updateBooked_InFlg method in RTAOutDetails class");
		Boolean boolean1 = Boolean.TRUE;
		try
		{
			//Mapping the payment attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the RTA Booked In Flag input details (Transaction Table)...
			DsPayTxnsTO objDsPayTxnsTO = new DsPayTxnsTO();
			objDsPayTxnsTO.setPaypayref(objPaymentDetails.getTransactionId());
			objDsPayTxnsTO.setRta_booked_in_flag(objPaymentDetails.getCurrent_rta_booked_flag());

			String rtaBookedInStmdId = "updateRTABookedInFlag";
			Object rtaBookedInObj = objDsPayTxnsTO;

			//Service Call..
			EBWLogger.logDebug(this,"Executing.... "+rtaBookedInStmdId);
			execute(rtaBookedInStmdId,rtaBookedInObj,boolean1);
			EBWLogger.logDebug(this,"Execution Completed.... "+rtaBookedInStmdId);

			EBWLogger.trace(this, "Finished with updateBooked_InFlg method of RTAOutDetails class");
		}
		catch(SQLException sqlexception){
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}
}
