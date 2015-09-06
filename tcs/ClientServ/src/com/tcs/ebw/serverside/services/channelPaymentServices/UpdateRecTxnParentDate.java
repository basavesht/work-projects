package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.TxnParentTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class UpdateRecTxnParentDate extends DatabaseService {

	/**
	 * The method updates the TXN_PARENT table with Txn date in case the same is modified...
	 * @param objTxnParentto
	 * @param dsPayTxnsTO
	 * @throws Exception
	 * @throws SQLException
	 */
	public void setRecParTxnDate(HashMap txnDetails,TxnParentTO objTxnParentTO) throws Exception,SQLException 
	{
		Boolean boolean1 = Boolean.TRUE;
		String updateParTxnStmntId="updateRecParentTxnDate";
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Payment Details...
			Date prevTxnDate =  ConvertionUtil.convertToTimestamp(objPaymentDetails.getPrevTxnDate());
			Date currentTxnDate = ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate());

			if(prevTxnDate!=null && currentTxnDate!=null && prevTxnDate.compareTo(currentTxnDate)!=0) 
			{
				//Mapping the next_txn_id and par_txn confirmation number...
				objTxnParentTO.setPar_txn_request_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
				objTxnParentTO.setNext_txn_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
				objTxnParentTO.setPrefered_previous_txn_dt(null);
				objTxnParentTO.setActual_previous_txn_dt(null);

				EBWLogger.logDebug(this,"Executing "+updateParTxnStmntId);
				execute(updateParTxnStmntId, objTxnParentTO, boolean1);
				EBWLogger.logDebug(this,"Execution Completed "+updateParTxnStmntId);

				//Updating the Payment Details..
				objPaymentDetails.setParTxnRequestDate(objTxnParentTO.getPar_txn_request_dt());
			}
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


}
