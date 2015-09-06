/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;

import com.tcs.Payments.EAITO.MS_BOTTOM_LINE_ASYNC_IN;
import com.tcs.Payments.EAITO.RSA_CALLBACK_INP;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsPayTxnsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class IdentifyTransaction extends DatabaseService {

	/**
	 * Verify if the transaction exists in the RSA Callback..
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public boolean identifyTransaction(RSA_CALLBACK_INP rsaRequest,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Starting identifyTransaction method in IdentifyTransaction class");
		boolean isTxnExists = false;
		try
		{
			//Instantiation...
			Boolean isTxnCommitReq = Boolean.TRUE;
			String stmntId ="identifyTransaction";
			ArrayList txnList = new ArrayList();

			//Input mappings..
			DsPayTxnsTO payTxnsDetails = new DsPayTxnsTO();
			payTxnsDetails.setPaypayref(rsaRequest.getTXN_CONF_ID());

			//Service call...
			Object output = null;
			EBWLogger.logDebug(this,"Execution Started.... "+stmntId);
			output = executeQuery(stmntId,payTxnsDetails,isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);

			//Output Extraction..
			if(output!=null)
			{
				txnList = (ArrayList)output;
				txnList.remove(0);
				if(!txnList.isEmpty()){
					//Mapping the batchRef number for the existing transaction ....
					String batchRef = (String)((ArrayList)txnList.get(0)).get(0);
					if(batchRef!=null && !batchRef.trim().equalsIgnoreCase("")){
						isTxnExists = true;
						rsaRequest.setTXN_BATCH_CONF_ID(batchRef);
					}
				}
			}
		}
		catch(SQLException sqlexception){
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally {

		}
		return isTxnExists;
	}

	/**
	 * Verify if the transaction exists in the BT Callback.. 
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public boolean identifyTransaction(MS_BOTTOM_LINE_ASYNC_IN asynchBTRequest,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Starting identifyTransaction method in IdentifyTransaction class");
		boolean isTxnExists = false;
		try
		{
			//Instantiation...
			Boolean isTxnCommitReq = Boolean.TRUE;
			String stmntId ="identifyTransaction";
			ArrayList txnList = new ArrayList();

			//Extract Confirmation Number.... 
			String txn_conf_number = "";
			String strArr[] = asynchBTRequest.getSPOOL_JOB_NAME().trim().split("_");
			if (strArr.length > 0 && (strArr[0]!=null && !strArr[0].equalsIgnoreCase(""))){
				txn_conf_number = new String(strArr[0]);			
			}
			else {
				return false;
			}

			//Input mappings..
			DsPayTxnsTO payTxnsDetails = new DsPayTxnsTO();
			payTxnsDetails.setPaypayref(txn_conf_number);

			//Service call...
			Object output = null;
			EBWLogger.logDebug(this,"Execution Started.... "+stmntId);
			output = executeQuery(stmntId,payTxnsDetails,isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);

			//Output Extraction..
			if(output!=null)
			{
				txnList = (ArrayList)output;
				txnList.remove(0);
				if(!txnList.isEmpty()){
					//Mapping the batchRef number for the existing transaction ....
					String batchRef = (String)((ArrayList)txnList.get(0)).get(0);
					if(batchRef!=null && !batchRef.trim().equalsIgnoreCase("")){
						isTxnExists = true;
						asynchBTRequest.setTXN_BATCH_CONF_ID(batchRef);
					}
				}
			}
		}
		catch(SQLException sqlexception){
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally {

		}
		return isTxnExists;
	}
}
