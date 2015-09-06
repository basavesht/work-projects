/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.BrValidationLogTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class UpdateBRValidationLog extends DatabaseService{

	public UpdateBRValidationLog() {

	}

	/**
	 * Updates the BR_VALIDATION_LOG Table entry with approved_by name.
	 * @param toObjects
	 * @param approved_by_name
	 * @throws Exception
	 * @throws SQLException
	 */
	public void updateBRValidationLog(HashMap txnDetails)throws Exception,SQLException 
	{
		EBWLogger.trace(this, "Starting UpdateBRValidationLog method in UpdateBRValidationLog class");
		String brUpdtStmdId="updateBRValidation";
		Boolean boolean1 = Boolean.TRUE;
		try
		{
			//Mapping the payment attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the BrValidationLog input details...
			BrValidationLogTO objBrValidationLog = new BrValidationLogTO();
			objBrValidationLog.setBOKey(objPaymentDetails.getTransactionId());
			objBrValidationLog.setApproved_by(objPaymentDetails.getCurrent_approver_role());

			//Executing the update query for the same.
			EBWLogger.logDebug(this,"Executing.... "+brUpdtStmdId);
			execute(brUpdtStmdId,objBrValidationLog,boolean1);
			EBWLogger.logDebug(this,"Execution Completed.... "+brUpdtStmdId);

			EBWLogger.trace(this, "Executed UpdateBRValidationLog");
			EBWLogger.trace(this, "Finished with UpdateBRValidationLog method of UpdateBRValidationLog class");
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
