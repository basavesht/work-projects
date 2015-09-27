/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tcs.bancs.channels.context.ServiceContext;
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
public class UpdatePaymentConfNoService extends DatabaseService
{
	public UpdatePaymentConfNoService(){

	}

	/**
	 * Updates the DS_PAY_TXNS Table with the HUB Processing number for the current transactions....
	 * @param statusFlag
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 * @throws SQLException
	 */
	public void setUpdatePaymentConfNoService(HashMap txnDetails,ServiceContext serviceContext)throws Exception,SQLException 
	{
		EBWLogger.trace(this, "Setting the inputs for updating the transaction details in the DS_PAY_TXNS Table....");
		try
		{
			//Payment Mappings details..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			ArrayList objPayOutDetails = new ArrayList();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			if(txnDetails.containsKey("HubOuputDetails")){
				objPayOutDetails = (ArrayList)txnDetails.get("HubOuputDetails");
			}

			//Hub returned ArrayList .....
			PaymentOutDetails objPaymentHubReturn=(PaymentOutDetails)objPayOutDetails.get(0);

			//Input Mappings for the DS_PAY_TXNS Object update .
			DsPayTxnsTO currentTxnPaymentTO = new DsPayTxnsTO();
			currentTxnPaymentTO.setPmnt_confirmationno(objPaymentHubReturn.getPayConfirmationNumber());
			currentTxnPaymentTO.setPaypayref(objPaymentDetails.getTransactionId());
			currentTxnPaymentTO.setPaybatchref(objPaymentDetails.getTxnBatchId());

			//StatementId and Object mapping for the execute query...
			String[] updatePaymentConfNoStmntId={"updatePaymentConNo"};
			Object[] updatePaymentConfNoTOobj={currentTxnPaymentTO};
			Boolean isTxnCommit = Boolean.TRUE;

			EBWLogger.logDebug(this,"Executing Query : "+updatePaymentConfNoStmntId[0]);
			EBWLogger.logDebug(this,"Input Parameters mapped"+updatePaymentConfNoTOobj[0]);
			execute(updatePaymentConfNoStmntId[0], updatePaymentConfNoTOobj[0], isTxnCommit);
			EBWLogger.logDebug(this,"Execution Completed.... "+updatePaymentConfNoStmntId[0]);

			//Populate Transaction OutDetails in the PaymentDetails TO..
			populatePaymentDetails(txnDetails,currentTxnPaymentTO);
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
	}

	/**
	 * Populate transaction details ...
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void populatePaymentDetails(HashMap txnDetails,DsPayTxnsTO currentTxnPaymentTO) throws Exception
	{	
		try 
		{
			//Mapping the payment detail object with the confirmation number for FTM Call...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Setting the payment attributes...
			objPaymentDetails.setPaymentHubTxnId(currentTxnPaymentTO.getPmnt_confirmationno());
		} 
		catch (Exception exception){
			throw exception;
		}
	}
}
