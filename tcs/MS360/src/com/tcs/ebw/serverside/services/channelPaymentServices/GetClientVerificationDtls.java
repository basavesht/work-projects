package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.MSSystemDefaults;
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
public class GetClientVerificationDtls extends DatabaseService
{
	/**
	 * Getting the Client Verifications auth data for the transactions selected...
	 * @param txnDetails
	 * @param context
	 * @throws Exception
	 */
	public void getClientVerificationDetails(HashMap txnDetails , ServiceContext context) throws SQLException,Exception
	{
		Object objOutput=null;
		Boolean boolean1 = Boolean.TRUE;
		try
		{
			//Payment details attributes mapping...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			String auth_mode = objPaymentDetails.getAuth_mode();
			if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_VERBAL))
			{
				String stmntId = "getVerbalAuthDetails";

				//Input Details mappings..
				TrxnDetailsInputTO trxnDetailsInputTO = new TrxnDetailsInputTO();
				trxnDetailsInputTO.setPaypayref(objPaymentDetails.getTransactionId());
				trxnDetailsInputTO.setPaybatchref(objPaymentDetails.getTxnBatchId());

				//Execute the service..
				objOutput = executeQuery(stmntId,trxnDetailsInputTO,boolean1);

				//Output mappings...
				txnDetails.put("VerbalAuthDetails",objOutput);
			}
			else if(auth_mode!=null && auth_mode.equalsIgnoreCase(MSSystemDefaults.AUTH_SIGNED))
			{
				String stmntId = "getSignedAuthDetails";

				//Input Details mappings..
				TrxnDetailsInputTO trxnDetailsInputTO = new TrxnDetailsInputTO();
				trxnDetailsInputTO.setPaypayref(objPaymentDetails.getTransactionId());
				trxnDetailsInputTO.setPaybatchref(objPaymentDetails.getTxnBatchId());

				//Execute the service..
				objOutput = executeQuery(stmntId,trxnDetailsInputTO,boolean1);

				//Output mappings...
				txnDetails.put("SignedAuthDetails",objOutput);
			}

			EBWLogger.logDebug(this, "Executed getClientVerificationDetails");
			EBWLogger.trace(this, "Finished with getClientVerificationDetails method of GetClientVerificationDtls class");
		}
		catch(SQLException sqlexception) {
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}
}
