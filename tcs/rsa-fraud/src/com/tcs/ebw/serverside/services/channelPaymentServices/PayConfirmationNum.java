/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class PayConfirmationNum  extends DatabaseService {

	/**
	 * Get payment confirmation number from RSA...
	 * @param txnDetails
	 * @throws SQLException
	 * @throws Exception
	 */
	public void getPaymentNumberFrmSeq(HashMap txnDetails) throws SQLException, Exception
	{
		EBWLogger.trace(this, "Starting getPaymentNumberFrmSeq method in PayConfirmationNum class");
		String stmntId = "getPayConfirmationId";
		Boolean boolean1 = Boolean.TRUE;
		Object toObject = new Object();
		try 
		{
			Object objOutput = executeQuery(stmntId,toObject,boolean1);
			ArrayList objFTMDetailsTO = (ArrayList)objOutput;

			//Mapping the payment detail object with the confirmation number for FTM Call...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			objPaymentDetails.setTransactionId((String)((ArrayList)objFTMDetailsTO.get(1)).get(0)); // Confirmation Number...
			EBWLogger.logDebug(this, "The confirmation from the Sequemce is :"+(String)((ArrayList)objFTMDetailsTO.get(1)).get(0));
		} 
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
		EBWLogger.trace(this, "Starting getPaymentNumberFrmSeq method in PayConfirmationNum class");
	}
}
