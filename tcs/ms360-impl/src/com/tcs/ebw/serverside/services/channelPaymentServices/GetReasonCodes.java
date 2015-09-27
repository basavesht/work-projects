package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.TrxnDetailsInputTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class GetReasonCodes extends DatabaseService {

	/**
	 * Getting the BR Reason codes for the transactions selected...
	 * @param txnDetails
	 * @param context
	 * @throws Exception
	 */
	public void getBrReasonCodes(HashMap txnDetails , ServiceContext context) throws Exception 
	{
		Object objOutput=null;
		Boolean boolean1 = Boolean.TRUE;
		try
		{
			String stmntId = "getReasonCodeDetails";

			//Payment details attributes mapping...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Input Details mappings..
			TrxnDetailsInputTO trxnDetailsInputTO = new TrxnDetailsInputTO();
			trxnDetailsInputTO.setPaypayref(objPaymentDetails.getTransactionId());
			trxnDetailsInputTO.setPaybatchref(objPaymentDetails.getTxnBatchId());

			objOutput = executeQuery(stmntId,trxnDetailsInputTO,boolean1);

			//Output mappings...
			txnDetails.put("BRReasonCodes",objOutput);

			EBWLogger.logDebug(this, "Executed getBrReasonCodes");
			EBWLogger.trace(this, "Finished with getBrReasonCodes method of GetReasonCodes class");
		}
		catch(SQLException sqlexception) {
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}
}
