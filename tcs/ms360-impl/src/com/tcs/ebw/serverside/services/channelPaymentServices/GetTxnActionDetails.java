/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.TrxnDetailsInputTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class GetTxnActionDetails extends DatabaseService {

	/**
	 * Getting the BR Reason codes for the transactions selected...
	 * @param txnDetails
	 * @param context
	 * @throws Exception
	 */
	public void getTxnActionLogDetails(HashMap txnDetails,ServiceContext context) throws SQLException,Exception
	{
		Object objOutput=null;
		Boolean boolean1 = Boolean.TRUE;
		try
		{
			String stmntId = "getActionTableDetails";

			//Payment details attributes mapping...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Input Details mappings..
			TrxnDetailsInputTO trxnDetailsInputTO = new TrxnDetailsInputTO();
			trxnDetailsInputTO.setPaypayref(objPaymentDetails.getTransactionId());
			trxnDetailsInputTO.setPaybatchref(objPaymentDetails.getTxnBatchId());

			//Execute the service..
			objOutput = executeQuery(stmntId,trxnDetailsInputTO,boolean1);

			//Updating the log with other business requirements..
			eventLogUpdater(txnDetails,objOutput,context);

			//Output mappings...
			txnDetails.put("TxnActionDetails",objOutput);

			EBWLogger.logDebug(this, "Executed getTxnActionDetails");
			EBWLogger.trace(this, "Finished with getTxnActionDetails method of getTxnActionDetails class");
		}
		catch(SQLException sqlexception) {
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * vent log handler for updating the action log tables in case of awaiting approval....
	 * @param txnDetails
	 * @param logDetails
	 * @throws SQLException
	 * @throws Exception
	 */
	public void eventLogUpdater(HashMap txnDetails,Object logDetails,ServiceContext context) throws SQLException,Exception
	{
		try 
		{
			//Payment details attributes mapping...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			String txnStatuscode = objPaymentDetails.getTxnCurrentStatusCode();

			//Log updates from the br_approver_role table to be done for list queries...
			if(txnStatuscode!=null && (txnStatuscode.equalsIgnoreCase("2") || txnStatuscode.equalsIgnoreCase("80")))
			{
				// Existing transaction log output from the DS_PAY_TXNS table...
				ArrayList txnLogDetails = (ArrayList)logDetails; 

				//Getting all the pending approvals for a transaction...(BR_TXN_APPROVERS and BR_VALIDATION_LOG Join)
				TrxnDetailsInputTO trxnDetailsInputTO = new TrxnDetailsInputTO();
				trxnDetailsInputTO.setPaypayref(objPaymentDetails.getTransactionId());
				trxnDetailsInputTO.setNext_approver_role(objPaymentDetails.getCurrent_approver_role());

				Object approver_roles = executeQuery("getTxnNextApprovers",trxnDetailsInputTO,Boolean.TRUE);

				//Appending all the latest_approvers to the transaction log updates...
				ArrayList approver_roles_list = (ArrayList)approver_roles;
				if(!approver_roles_list.isEmpty())
				{
					for(int i=1;i<approver_roles_list.size();i++)
					{
						ArrayList approver_role = (ArrayList)approver_roles_list.get(i);
						String approver_role_str = (String)approver_role.get(0); //Contains the Approver_roles ...

						//Creating the next_approver_role entry in the existing arraylist...
						ArrayList next_approver_role = new ArrayList();
						next_approver_role.add(MSCommonUtils.getApproverRoleDesc(approver_role_str,context)); //DEPT/GROUP
						next_approver_role.add(""); //ACTION
						next_approver_role.add(""); //LOGINID
						next_approver_role.add(""); //USERNAME
						next_approver_role.add(""); //ACTIONDATE
						next_approver_role.add(""); //STATUS
						next_approver_role.add(""); //COMMENTS
						txnLogDetails.add(next_approver_role);
					}
				}
			}
		} 
		catch (SQLException sqlException){
			throw sqlException;
		} 
		catch (Exception exception){
			throw exception;
		}
		finally {

		}
	}
}
