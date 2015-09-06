/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class GetExternalAccountsDetails extends DatabaseService {

	/**
	 * Get external account details...
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void getExternalPayeeDetails(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Starting getExternalPayeeDetails method in GetExternalAccountsDetails class");
		try
		{
			//Payment Detail attributes...
			Boolean isTxnCommitReq = Boolean.TRUE;
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Input mappings for the external account details....
			com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsTO dsExtPayeeDetailsTO = new com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsTO();
			dsExtPayeeDetailsTO.setExternalPayeeId(objPaymentDetails.getExtAccount_RefId());

			//StatementId and Object mapping for getting the external account details..
			String externalAccsstmntId="getExternalPayeeDetails";
			Object externalAccsTOObj=dsExtPayeeDetailsTO;
			Object objOutput=null;
			DsExtPayeeDetailsOutTO objDsExtPayeeDetailsOutTO=new DsExtPayeeDetailsOutTO();
			objOutput = executeQuery(externalAccsstmntId,externalAccsTOObj,isTxnCommitReq);
			objDsExtPayeeDetailsOutTO = (DsExtPayeeDetailsOutTO)objOutput;
			EBWLogger.logDebug(this,"Execution Completed.... "+externalAccsstmntId);

			//Mapping the external account details in the PaymentDetails Transfer Object...
			objPaymentDetails.setExtAcc_NickName(objDsExtPayeeDetailsOutTO.getCpypayeename1());
			objPaymentDetails.setExtAccount_Number(objDsExtPayeeDetailsOutTO.getCpyaccnum());
			objPaymentDetails.setExtAccount_RefId(objDsExtPayeeDetailsOutTO.getCpypayeeid());

			//Mapping details to the payment details transfer object..
			txnDetails.put("ExternalAccDetails",objDsExtPayeeDetailsOutTO);
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

