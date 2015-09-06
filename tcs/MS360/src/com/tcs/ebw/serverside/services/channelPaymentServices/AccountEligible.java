package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.EligibleAccInTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;
import com.tcs.mswitch.common.channel.DBProcedureChannel;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       23-09-2011        P3-B            PLA  
 * **********************************************************
 */
public class AccountEligible extends DatabaseService 
{

	/**
	 * Checks the account validity from the BR service output ....
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void validateAccEligibilty(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Starting validateAccEligibilty method in EligibilityCheckService class");
		String flag_DR_CR = null;
		MultiTransferProcessor multiTxnProcessor = new MultiTransferProcessor();
		multiTxnProcessor.setConnection(serviceConnection);
		ArrayList transferList = new ArrayList();
		try 
		{
			//Mapping from the Business Rule response
			BusinessRulesService objBussRulesService = new BusinessRulesService();
			if(txnDetails.containsKey("BROutputDetails")){
				objBussRulesService=(BusinessRulesService)txnDetails.get("BROutputDetails");
			}

			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			Date businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());
			String cancelTxnFlag = WSDefaultInputsMap.getCancelTxnFlag(serviceContext);

			//Setting the account eligible flag based on the business rule response..
			if(!objBussRulesService.isCR_ACC_IN_ELIGIBLE() && !objBussRulesService.isDR_ACC_IN_ELIGIBLE()){
				objPaymentDetails.setAccounEligible(true);
			}

			//Checking the cancel transaction flag value , in case value is other than 1 , transactions should not be canceled . 
			if(cancelTxnFlag!=null && cancelTxnFlag.equalsIgnoreCase("1"))
			{
				//Commit the RTA for the System Rejected transactions....
				if(objBussRulesService.isCR_ACC_IN_ELIGIBLE() || objBussRulesService.isDR_ACC_IN_ELIGIBLE()){
					if(serviceContext.isRTACommitReq()){
						EBWLogger.logDebug(this, "Calling RTA Immediate Commit for single System Rejected txn");
						DBProcedureChannel.commit();
						serviceContext.setRTACommitReq(false);
					}
				}

				//Cancel transaction flag is true and CR indicator is set..
				if(objBussRulesService.isCR_ACC_IN_ELIGIBLE())
				{
					flag_DR_CR = "CR";
					//Getting all the transaction Id List 
					transferList = getTxnIdList(txnDetails,flag_DR_CR);
					if(transferList!=null && !transferList.isEmpty()){
						//Cancel all selected transfers in the List..
						multiTxnProcessor.cancelMultiTransfer(transferList,businessDate,txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return; 
						}
					}
				}

				//Cancel transaction flag is true and DR indicator is set..
				if(objBussRulesService.isDR_ACC_IN_ELIGIBLE())
				{
					flag_DR_CR = "DR";
					//Getting all the transaction Id List 
					transferList = getTxnIdList(txnDetails,flag_DR_CR);
					if(transferList!=null && !transferList.isEmpty()){
						//Cancel all selected transfers in the List..
						multiTxnProcessor.cancelMultiTransfer(transferList,businessDate,txnDetails,serviceContext);
						if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
							return; 
						}
					}
				}
			}
		} 
		catch(Exception exception){
			throw exception;
		}
		finally {

		}
	}

	/**
	 * The method gets  all the transaction confirmation Ids for the input specified in form of ArrayList...
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 * @throws Exception
	 */
	public ArrayList getTxnIdList(HashMap txnDetails,String flag_DR_CR) throws Exception 
	{
		EBWLogger.trace(this, "Starting getTxnIdList method in EligibilityCheckService class");
		Object output = null;
		ArrayList txnIdList = new ArrayList();
		String eligibleAccStmntId= null;
		Boolean boolean1 = Boolean.TRUE;
		try
		{
			//Payment Output Details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//From account details...
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//To account details...
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			String transferType = objPaymentDetails.getTransfer_Type();

			//Input Mappings for the eligible accounts..
			EligibleAccInTO objEligibleAccInTO = new EligibleAccInTO();
			objEligibleAccInTO.setPaypayref(objPaymentDetails.getTransactionId());
			objEligibleAccInTO.setKeyaccountnumber_from(objFromMSAcc_Details.getKeyAccount());
			objEligibleAccInTO.setKeyaccountnumber_to(objToMSAcc_Details.getKeyAccount());

			if(flag_DR_CR!=null && flag_DR_CR.equalsIgnoreCase("DR"))
			{
				if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
				{
					eligibleAccStmntId="getCancelDRPaymentDetails";
					EBWLogger.logDebug(this,"Executing.... "+eligibleAccStmntId);
					output = executeQuery(eligibleAccStmntId,objEligibleAccInTO,boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+eligibleAccStmntId);
				}
			}
			else if(flag_DR_CR!=null && flag_DR_CR.equalsIgnoreCase("CR"))
			{
				if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)))
				{
					eligibleAccStmntId="getCancelCRPaymentDetails";
					EBWLogger.logDebug(this,"Executing.... "+eligibleAccStmntId);
					output = executeQuery(eligibleAccStmntId,objEligibleAccInTO,boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+eligibleAccStmntId);
				}
			}

			//Casting the output to the arrayList...
			if(output!=null){
				txnIdList = (ArrayList)output;
				if(txnIdList!=null && !txnIdList.isEmpty()){
					txnIdList.remove(0); //Removing the header...
				}
			}

			EBWLogger.logDebug(this, "Executed getTxnIdList");
			EBWLogger.trace(this, "Finished with getTxnIdList method of EligibilityCheckService class");
		}
		catch(SQLException sqlexception) {
			sqlexception.printStackTrace();
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally {

		}
		return txnIdList;
	}
}
