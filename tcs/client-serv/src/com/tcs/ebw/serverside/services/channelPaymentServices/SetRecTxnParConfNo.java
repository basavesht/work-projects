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
	public void setRecParConfNumber(TxnParentTO objTxnParentto,DsPayTxnsTO dsPayTxnsTO) throws Exception,SQLException 
	{
		Boolean boolean1 = Boolean.TRUE;
		TxnParentTO objUpdtTxnParTO = new TxnParentTO();
		String[] updateParTxnStmntId={"updateRecParentConfNo"};
		Object[] updateParTxnTOobj={objUpdtTxnParTO};
		try 
		{
			objUpdtTxnParTO.setPar_txn_no(objTxnParentto.getPar_txn_no());
			objUpdtTxnParTO.setNext_txn_id(dsPayTxnsTO.getPaypayref());
			objUpdtTxnParTO.setPar_txn_confno(dsPayTxnsTO.getPaypayref());
			EBWLogger.logDebug(this,"Executing "+updateParTxnStmntId[0]);
			execute(updateParTxnStmntId[0], updateParTxnTOobj[0], boolean1);
			EBWLogger.logDebug(this,"Execution Completed "+updateParTxnStmntId[0]);
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
