/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsOnloadAccDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class GetExternalAccounts  extends DatabaseService 
{
	/**
	 * Get external account ...
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 * @throws SQLException
	 */
	public void getExternalAccounts(HashMap txnDetails,ServiceContext serviceContext) throws Exception,SQLException  
	{
		EBWLogger.trace(this, "Getting the external accounts for the logged in client...");
		Object externalAccounts=null;
		Object thirdPartyExtAccounts=null;
		try
		{
			//Payment attributes mappings...
			Boolean isTxnCommitReq = Boolean.TRUE;
			DsOnloadAccDetailsTO objDsOnloadAccDetailsTO = new DsOnloadAccDetailsTO();
			if(txnDetails.containsKey("ExtAccKeyClientIds")){
				objDsOnloadAccDetailsTO = (DsOnloadAccDetailsTO)txnDetails.get("ExtAccKeyClientIds");
			}

			// External Accounts StatementId's and TransferObjects
			String externalAccsStmntId="getExternalAccounts";
			externalAccounts = executeQuery(externalAccsStmntId,objDsOnloadAccDetailsTO,isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+externalAccsStmntId);

			// Third Party External Accounts StatementId's and TransferObjects
			String thirdPartyExtAccsStmntId="getThirdPartyExtAccounts";
			thirdPartyExtAccounts = executeQuery(thirdPartyExtAccsStmntId,objDsOnloadAccDetailsTO,isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+externalAccsStmntId);

			//Setting the external accounts in the HashMap.
			txnDetails.put("ExternalAccountsList", externalAccounts);
			txnDetails.put("ThirdPartyExtAccountsList", thirdPartyExtAccounts);
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
