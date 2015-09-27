package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.serverValidations.StatusConsistencyChk;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsBatchTO;
import com.tcs.ebw.payments.transferobject.DsPayTxnsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *   224703        05-07-2011        P3              CR 261
 * **********************************************************
 */
public class ApproveTransactionService extends DatabaseService{

	public void approvePaymentDetails(int statusFlag,HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Setting the Input details for the approval case..");
		boolean isTxnStatusValid = false;
		try
		{
			//Payment account details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//MS User attributes..
			String userId=objMSUserDetails.getRcafId();
			String userName=MSCommonUtils.extractCurrentUserName(objMSUserDetails);

			//For updating the status in the BATCH table...
			DsBatchTO currentTxnBatchTO = new DsBatchTO();
			currentTxnBatchTO.setBatbatchref(objPaymentDetails.getTxnBatchId());
			currentTxnBatchTO.setBatmodifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnBatchTO.setBatmodifiedby(userId);
			currentTxnBatchTO.setBatauthby1(userName); // Approver Name details
			currentTxnBatchTO.setEmp_id1(userId); // Approver Employee Id details

			//For updating the status in the DS_PAY_TXNS table....
			DsPayTxnsTO currentTxnPaymentTO = new DsPayTxnsTO();
			currentTxnPaymentTO.setPaypayref(objPaymentDetails.getTransactionId());
			currentTxnPaymentTO.setPaybatchref(objPaymentDetails.getTxnBatchId());
			currentTxnPaymentTO.setModifieddate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			currentTxnPaymentTO.setModifiedby(userId);
			currentTxnPaymentTO.setModifiedby_name(userName);
			currentTxnPaymentTO.setPaytypecode(objPaymentDetails.getScreen_Type());
			currentTxnPaymentTO.setCoe_user_name(userName);

			//StatementId and Object mapping for the execute query...
			String[] approvePaymentStmntId={"approveBatch","approvepayTransaction"};
			Object[] approvePaymentTOObj={currentTxnBatchTO,currentTxnPaymentTO};
			Boolean isTxnCommitReq = Boolean.TRUE;
			String statusFlagVal = Integer.toString(statusFlag);

			//Type casting the ToObject instances ...
			DsBatchTO dsbatchto = (DsBatchTO)approvePaymentTOObj[0];
			for (int i= 0; i< approvePaymentTOObj.length;i++) 
			{
				if(approvePaymentTOObj[i] instanceof com.tcs.ebw.payments.transferobject.DsBatchTO)
				{
					//Updating the user details in case the transaction is system rejected ....
					if(statusFlagVal!=null && statusFlagVal.equalsIgnoreCase("52")){
						updateBatchSystemDetails(txnDetails,(DsBatchTO)approvePaymentTOObj[i]);
					}

					dsbatchto.setBatstatus(statusFlagVal);

					//Checking the Transaction Batch version before modifying the transactions...
					EBWLogger.logDebug(this, "Checking the Transaction Batch Version before modifying the transactions...");
					VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
					String versionType = "BATVER";
					objVerMismatch.setConnection(serviceConnection);
					objVerMismatch.compareVersionNum(txnDetails,versionType,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return; 
					}

					//Checking the Transaction Batch Status Consistency ...
					StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
					statusConsistency.setConnection(serviceConnection);
					objPaymentDetails.setStatusChkId("BATCHSTATUS");
					isTxnStatusValid = statusConsistency.verifyCurrentStatusChk(txnDetails,serviceContext);
					if(!isTxnStatusValid){
						serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.STATUS_CONSISTENCY_FAILURE);
						return ;
					}
				}
				if(approvePaymentTOObj[i] instanceof com.tcs.ebw.payments.transferobject.DsPayTxnsTO)
				{
					//Following method sets the DB TO Objects with the external Interfaces response...
					setExternalSrvcResponseDetails((DsPayTxnsTO)approvePaymentTOObj[i],txnDetails);

					//Updating the user details in case the transaction is system rejected ....
					if(statusFlagVal!=null && statusFlagVal.equalsIgnoreCase("52")){
						updatePaymentSystemDetails(txnDetails,(DsPayTxnsTO)approvePaymentTOObj[i]);
					}

					((DsPayTxnsTO)approvePaymentTOObj[i]).setPayccsstatuscode(statusFlagVal);
					if(statusFlagVal.equalsIgnoreCase("4")){
						TxnProperties.calculateTxnSettleDate(txnDetails,(DsPayTxnsTO)approvePaymentTOObj[i],serviceContext);
						((DsPayTxnsTO)approvePaymentTOObj[i]).setSent_to_paymnt("Y");
					}

					//Checking the Transaction version before modifying the transactions...
					EBWLogger.logDebug(this, "Checking the Transaction Batch Version before modifying the transactions...");
					VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
					String versionType = "TXNVER";
					objVerMismatch.setConnection(serviceConnection);
					objVerMismatch.compareVersionNum(txnDetails,versionType,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						return; 
					}

					//Checking the Transaction Status Consistency ...
					StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
					statusConsistency.setConnection(serviceConnection);
					objPaymentDetails.setStatusChkId("TXNSTATUS");
					isTxnStatusValid = statusConsistency.verifyCurrentStatusChk(txnDetails,serviceContext);
					if(!isTxnStatusValid){
						serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.STATUS_CONSISTENCY_FAILURE);
						return ;
					}
				}
				EBWLogger.logDebug(this,"Executing Query : "+approvePaymentStmntId[i]);
				EBWLogger.logDebug(this,"Input Parameters mapped"+approvePaymentTOObj[i]);
				execute(approvePaymentStmntId[i], approvePaymentTOObj[i], isTxnCommitReq);
				EBWLogger.logDebug(this,"Execution Completed.... "+approvePaymentStmntId[i]);
			} 

			//Populate Transaction OutDetails in the PaymentDetails TO..
			populatePaymentDetails(txnDetails,currentTxnBatchTO,currentTxnPaymentTO);
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
	public void populatePaymentDetails(HashMap txnDetails,DsBatchTO currentTxnBatchTO,DsPayTxnsTO currentTxnPaymentTO) throws Exception
	{	
		try 
		{
			//Mapping the payment detail object with the confirmation number for FTM Call...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Updating the payment detail batch object...
			DsBatchTO modifyTxnBatchTO = new DsBatchTO();
			if(txnDetails.containsKey("Current_DsBatchObject")){
				modifyTxnBatchTO = (DsBatchTO)txnDetails.get("Current_DsBatchObject");
			}
			modifyTxnBatchTO.setBatmodifieddate(currentTxnBatchTO.getBatmodifieddate());
			modifyTxnBatchTO.setBatmodifiedby(currentTxnBatchTO.getBatmodifiedby());
			modifyTxnBatchTO.setBatmodifiedby(currentTxnBatchTO.getBatmodifiedby());
			modifyTxnBatchTO.setBatauthby1(currentTxnBatchTO.getBatauthby1()); // Approver Name details
			modifyTxnBatchTO.setEmp_id1(currentTxnBatchTO.getEmp_id1()); // Approver Employee Id details

			//Updating the payment details object..
			DsPayTxnsTO modifyTxnPaymentTO = new DsPayTxnsTO();
			if(txnDetails.containsKey("Current_DsPayTxnObject")){
				modifyTxnPaymentTO = (DsPayTxnsTO)txnDetails.get("Current_DsPayTxnObject");
			}
			modifyTxnPaymentTO.setPaypymtdate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			modifyTxnPaymentTO.setSent_to_paymnt(currentTxnPaymentTO.getSent_to_paymnt());
			modifyTxnPaymentTO.setActual_exe_dt(currentTxnPaymentTO.getActual_exe_dt());
			modifyTxnPaymentTO.setPayccsstatuscode(currentTxnPaymentTO.getPayccsstatuscode());
			modifyTxnPaymentTO.setModifieddate(currentTxnPaymentTO.getModifieddate());
			modifyTxnPaymentTO.setModifiedby(currentTxnPaymentTO.getModifiedby());
			modifyTxnPaymentTO.setModifiedby_name(currentTxnPaymentTO.getModifiedby_name());
			modifyTxnPaymentTO.setPaytypecode(currentTxnPaymentTO.getPaytypecode());
			modifyTxnPaymentTO.setCoe_user_name(currentTxnPaymentTO.getCoe_user_name());
			modifyTxnPaymentTO.setNext_approver_role(currentTxnPaymentTO.getNext_approver_role());
			modifyTxnPaymentTO.setActual_exe_dt(currentTxnPaymentTO.getActual_exe_dt());
			modifyTxnPaymentTO.setPayabpaysendflag(currentTxnPaymentTO.getPayabpaysendflag());

			//Setting the payment attributes...
			objPaymentDetails.setTxnBatchId(currentTxnBatchTO.getBatbatchref());
			objPaymentDetails.setTransactionId(currentTxnPaymentTO.getPaypayref());
			objPaymentDetails.setTxnCurrentStatusCode(currentTxnPaymentTO.getPayccsstatuscode());
			objPaymentDetails.setActualExecDate(ConvertionUtil.convertToAppDateStr(currentTxnPaymentTO.getActual_exe_dt()));
			objPaymentDetails.setDont_spawn_flag(currentTxnPaymentTO.getDont_spawn_flag());
			objPaymentDetails.setNext_approver_role(currentTxnPaymentTO.getNext_approver_role());
		} 
		catch (Exception exception){
			throw exception;
		}
	}


	/**
	 * Setting the UserDetails to the MM System in case of "System Rejected"
	 * @param dsPayTxnsLog
	 * @throws Exception 
	 */
	public void updateBatchSystemDetails(HashMap txnDetails,DsBatchTO dsBatchTO) throws Exception
	{
		try 
		{
			//Getting the payment details ....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the log details to the MM System in case of "System Rejected" case only...
			dsBatchTO.setBatmodifiedby(objPaymentDetails.getMMSystemDesc());
			dsBatchTO.setBatauthby1(null); // Approver Name details
			dsBatchTO.setEmp_id1(null); // Approver Emp Id details
		} 
		catch (Exception exception) {
			throw exception;
		}

	}

	/**
	 * Setting the UserDetails to the MM System in case of "System Rejected"
	 * @param dsPayTxnsLog
	 * @throws Exception 
	 */
	public void updatePaymentSystemDetails(HashMap txnDetails,DsPayTxnsTO dsPayTxnsTO) throws Exception
	{
		try 
		{
			//Getting the payment details ....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Mapping the log details to the MM System in case of "System Rejected" case only...
			dsPayTxnsTO.setModifiedby(objPaymentDetails.getMMSystemDesc());
			dsPayTxnsTO.setCoe_user_name(null);
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/** Following function is used to set the external services response details in the BR Service
	 * Added only for CallBack RSA since the BR action is Approve-PreConfirm...
	 * @param toObjects
	 * @param externalSrvResponse
	 */
	public static void setExternalSrvcResponseDetails(DsPayTxnsTO dsPayTxnsTO,HashMap txnDetails)
	{
		String next_approver_role = null;
		String dont_spawn_flg = "N";
		String ofac_case_id = null;

		//Mapping from the Business Rule response
		if(txnDetails.containsKey("BROutputDetails"))
		{
			BusinessRulesService objBussRulesService= (BusinessRulesService)txnDetails.get("BROutputDetails");

			//Next_approver_role mapping...
			if(objBussRulesService.getNext_approver_role()!=null && !objBussRulesService.getNext_approver_role().trim().equalsIgnoreCase("")){
				next_approver_role = objBussRulesService.getNext_approver_role();
			}

			//Dont_spawn flag mapping..
			if(objBussRulesService.getDont_spawn_flag()!=null && !objBussRulesService.getDont_spawn_flag().trim().equalsIgnoreCase("")){
				dont_spawn_flg = objBussRulesService.getDont_spawn_flag();
			}

			//OFAC Case Id..
			if(objBussRulesService.getOfac_case_id()!=null && !objBussRulesService.getOfac_case_id().trim().equalsIgnoreCase("")){
				ofac_case_id = objBussRulesService.getOfac_case_id();
			}

			//Updating the record based on BR output..
			dsPayTxnsTO.setNext_approver_role(next_approver_role);
			dsPayTxnsTO.setDont_spawn_flag(dont_spawn_flg);
			dsPayTxnsTO.setOfac_case_id(ofac_case_id);
		}
	}
}
