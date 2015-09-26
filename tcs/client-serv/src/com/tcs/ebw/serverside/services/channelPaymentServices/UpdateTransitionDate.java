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
import com.tcs.ebw.common.util.ConvertionUtil;
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
public class UpdateTransitionDate extends DatabaseService 
{
	/**
	 * The Following method takes care of the following requirement . 
	 * The update needs to be called/done at the end of the transaction service flow so as to avoid any 
	 * change in the current design only for the below mentioned state transition. 
	 *  # The initiation date should be shown for these statuses:
			Scheduled
			Suspended
			Awaiting Approval
			Expired
		#The date of transition to this state should be shown for these:
			Executed
			Not Approved
			System Rejected
			Canceled
		 For Returned, the date will be the same as when the transaction went in Executed status.
	 * Requested_Exe_Dt column is updated for this requirement since all the List Queries is using the same for the Display mode. 
	 * DS_PAY_TXNS Date Column mappings..
	 * PAYPYMTDATE : Stores the initiation date. 
	 * REQUESTED_EXE_DATE : Stores the initiation date initially , state transition date is updated for the mentioned states.
	 * PAYISSUEDDATE : Stores the initiation date entered by the user only in case of First business day and Last business day processing. 
	 * CREATEDDATE : Stores the current business date .
	 * MODIDFIEDDATE: Stores the current business date OR date of modification.
	 * PAYTXNEPRYDATE : Stores the transaction expiry date in case of transaction moving to awaiting approval state. 
	 * ACTUAL_EXE_DT : Stores the date when transaction moves to Executed state.
	 * @param stmntId
	 * @param paymentStatusFlag
	 * @param toObjects
	 * @param boolean1
	 * @param serviceContext
	 * @throws Exception
	 */

	public void setCurTxnTransitionDate(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Setting the input details for updating the transition date...");
		try
		{
			//Payment details attribute mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the business date to be set in the DS_PAY_TXNS Table...
			DsPayTxnsTO updateTransitionDate = new DsPayTxnsTO();
			updateTransitionDate.setPaypayref(objPaymentDetails.getTransactionId());
			updateTransitionDate.setRequested_exe_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));

			//StatementId and Object mapping for updating the transition date...
			String[] setTransitionDtStmntId = {"updateTransitionDate"};
			Object[] setTransitionDtTOObj={updateTransitionDate};
			Boolean boolean1 = Boolean.TRUE;
			List<String> validAccStatuses = new ArrayList<String>(Arrays.asList("4","20","44","52"));
			if(setTransitionDtTOObj[0] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsTO)
			{
				String txnStatus = objPaymentDetails.getTxnCurrentStatusCode();
				if(txnStatus!=null && validAccStatuses.contains(txnStatus))
				{
					EBWLogger.logDebug(this,"Executing Query : "+setTransitionDtStmntId[0]);
					EBWLogger.logDebug(this,"Input Parameters mapped"+setTransitionDtTOObj[0]);
					execute(setTransitionDtStmntId[0], setTransitionDtTOObj[0], boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+setTransitionDtStmntId[0]);
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


	/**
	 * The Following method takes care of the following requirement for the CHILD TRANSFER CREATED... 
	 * The update needs to be called/done at the end of the transaction service flow so as to avoid any 
	 * change in the current design only for the below mentioned state transition. 
	 *  # The initiation date should be shown for these statuses:
			Scheduled
			Suspended
			Awaiting Approval
			Expired
		#The date of transition to this state should be shown for these:
			Executed
			Not Approved
			System Rejected
			Canceled
		 For Returned, the date will be the same as when the transaction went in Executed status.
	 * Requested_Exe_Dt column is updated for this requirement since all the List Queries is using the same for the Display mode. 
	 * DS_PAY_TXNS Date Column mappings..
	 * PAYPYMTDATE : Stores the initiation date. 
	 * REQUESTED_EXE_DATE : Stores the initiation date initially , state transition date is updated for the mentioned states.
	 * PAYISSUEDDATE : Stores the initiation date entered by the user only in case of First business day and Last business day processing. 
	 * CREATEDDATE : Stores the current business date .
	 * MODIDFIEDDATE: Stores the current business date OR date of modification.
	 * PAYTXNEPRYDATE : Stores the transaction expiry date in case of transaction moving to awaiting approval state. 
	 * ACTUAL_EXE_DT : Stores the date when transaction moves to Executed state.
	 * @param stmntId
	 * @param paymentStatusFlag
	 * @param toObjects
	 * @param boolean1
	 * @param serviceContext
	 * @throws Exception
	 */

	public void setNextTxnTransitionDate(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Setting the input details for updating the transition date...");
		try
		{
			//Payment details attribute mappings...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the business date to be set in the DS_PAY_TXNS Table...
			DsPayTxnsTO updateTransitionDate = new DsPayTxnsTO();
			updateTransitionDate.setPaypayref(objPaymentDetails.getChildTransactionId());
			updateTransitionDate.setRequested_exe_dt(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));

			//StatementId and Object mapping for updating the transition date...
			String[] setTransitionDtStmntId = {"updateTransitionDate"};
			Object[] setTransitionDtTOObj={updateTransitionDate};
			Boolean boolean1 = Boolean.TRUE;
			List<String> validAccStatuses = new ArrayList<String>(Arrays.asList("4","20","44","52"));
			if(setTransitionDtTOObj[0] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsTO)
			{
				String txnStatus = objPaymentDetails.getTxnCurrentStatusCode();
				if(txnStatus!=null && validAccStatuses.contains(txnStatus))
				{
					EBWLogger.logDebug(this,"Executing Query : "+setTransitionDtStmntId[0]);
					EBWLogger.logDebug(this,"Input Parameters mapped"+setTransitionDtTOObj[0]);
					execute(setTransitionDtStmntId[0], setTransitionDtTOObj[0], boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+setTransitionDtStmntId[0]);
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
