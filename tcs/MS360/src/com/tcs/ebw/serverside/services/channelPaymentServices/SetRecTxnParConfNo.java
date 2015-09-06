package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsPayTxnsTO;
import com.tcs.ebw.payments.transferobject.TxnParentTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class SetRecTxnParConfNo extends DatabaseService {

	/**
	 * The method updates the TXN_PARENT table with the next_txn_id and parent confirmation number only during the first create of recurring instruction....
	 * @param objTxnParentto
	 * @param dsPayTxnsTO
	 * @throws Exception
	 * @throws SQLException
	 */
	public void setRecParConfNumber(TxnParentTO objTxnParentTO,DsPayTxnsTO dsPayTxnsTO) throws Exception,SQLException 
	{
		Boolean boolean1 = Boolean.TRUE;
		String updateParTxnStmntId="updateRecParentConfNo";
		try 
		{
			//Mapping the next_txn_id and par_txn confirmation number...
			objTxnParentTO.setNext_txn_id(dsPayTxnsTO.getPaypayref());
			objTxnParentTO.setPar_txn_confno(dsPayTxnsTO.getPaypayref());

			EBWLogger.logDebug(this,"Executing "+updateParTxnStmntId);
			execute(updateParTxnStmntId, objTxnParentTO, boolean1);
			EBWLogger.logDebug(this,"Execution Completed "+updateParTxnStmntId);
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
