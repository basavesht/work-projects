/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
public class ResetApprovalDetails extends DatabaseService 
{
	/**
	 * Reset the approver details only if no next approver is required...
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void clearApproverDetails(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Setting the input details for reseting  the approver details..");
		try
		{
			//Payment details attribute mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			int previousTxnStatus = 0;
			if(objPaymentDetails.getTxnPrevStatusCode()!=null){
				previousTxnStatus = new Integer(objPaymentDetails.getTxnPrevStatusCode()).intValue();
			}

			//If the transactions were in awaiting approval/pending risk review before modification , then only reset the approval info..
			if(previousTxnStatus == 2 || previousTxnStatus == 80)
			{
				//Mapping the business date to be set in the DS_PAY_TXNS Table...
				DsPayTxnsTO resetApproverDtls = new DsPayTxnsTO();
				resetApproverDtls.setPaypayref(objPaymentDetails.getTransactionId());
				resetApproverDtls.setNext_approver_role(null);
				resetApproverDtls.setPaytxneprydate(null);

				//StatementId and Object mapping for updating the transition date...
				String[] resetApproverDtlsStmntId = {"resetApprovalInfo"};
				Object[] resetApproverDtlsTO={resetApproverDtls};
				Boolean boolean1 = Boolean.TRUE;
				List<String> validTxnStatuses = new ArrayList<String>(Arrays.asList("4","6","20","44","52"));
				if(resetApproverDtlsTO[0] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsTO)
				{
					String txnStatus = objPaymentDetails.getTxnCurrentStatusCode();
					if(txnStatus!=null && validTxnStatuses.contains(txnStatus))
					{
						EBWLogger.logDebug(this,"Executing Query : "+resetApproverDtlsStmntId[0]);
						EBWLogger.logDebug(this,"Input Parameters mapped"+resetApproverDtlsTO[0]);
						execute(resetApproverDtlsStmntId[0], resetApproverDtlsTO[0], boolean1);
						EBWLogger.logDebug(this,"Execution Completed.... "+resetApproverDtlsStmntId[0]);
					}
				}
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
