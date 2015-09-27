package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.Auth_Mode;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.UtilizedAmountTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *  224703         13-05-2011        Phase 3         Key Account Number
 * **********************************************************
 */
public class GetUtilizedAmount extends DatabaseService
{
	/**
	 * Getting the utilized amount for transactions as per the specs . 
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void getTxnUtilizedAmount(HashMap txnDetails,ServiceContext context) throws Exception
	{
		EBWLogger.trace(this, "Starting getTxnUtilizedAmount method in GetUtilizedAmount class");
		try
		{
			String utilizedAmntStmntId = "getTxnUtilizedAmount";
			Boolean boolean1 = Boolean.TRUE;
			Object output = null;
			int expPeriod = 0;

			//Mapping the payment attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//Getting the expiry period from the cache..
			String txnExpPeriod = WSDefaultInputsMap.getTxnExpiryPeriod(context);
			if(txnExpPeriod!=null && !txnExpPeriod.trim().equalsIgnoreCase("")){
				expPeriod = ConvertionUtil.convertToint(txnExpPeriod.trim());
			}

			Calendar startDate = Calendar.getInstance();
			startDate.setTime(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			startDate.add(Calendar.DATE, -expPeriod);

			//Mapping the Utilized amount input details...
			UtilizedAmountTO objUtilizedAmnt = new UtilizedAmountTO();
			objUtilizedAmnt.setAuth_mode(Auth_Mode.VERBAL);
			objUtilizedAmnt.setSame_name_flg("N");
			objUtilizedAmnt.setFrom_account(objFromMSAcc_Details.getKeyAccount());
			objUtilizedAmnt.setStartDate(ConvertionUtil.convertToTimestamp(startDate.getTime()));
			objUtilizedAmnt.setEndDate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));

			//Executing the  query for the same and getting the amount.
			EBWLogger.logDebug(this,"Executing.... "+utilizedAmntStmntId);
			output = executeQuery(utilizedAmntStmntId,objUtilizedAmnt,boolean1);
			if(output!=null){
				objUtilizedAmnt = (UtilizedAmountTO)output;
				if(objUtilizedAmnt.getUtilized_amount()!=null){
					objPaymentDetails.setUtilizedAmount(objUtilizedAmnt.getUtilized_amount());
				}
			}
			EBWLogger.logDebug(this,"Execution Completed.... "+utilizedAmntStmntId);
			EBWLogger.trace(this, "Finished with getTxnUtilizedAmount method of GetUtilizedAmount class");
		}
		catch(SQLException sqlexception){
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * Getting the utilized amount for Checks as per the specs . 
	 * @param txnDetails
	 * @throws Exception 
	 */
	public void getCheckUtilizedAmount(HashMap txnDetails,ServiceContext context) throws Exception
	{
		EBWLogger.trace(this, "Starting getCheckUtilizedAmount method in GetUtilizedAmount class");
		try
		{
			String utilizedAmntStmntId = "getChkUtilizedAmount";
			Boolean boolean1 = Boolean.TRUE;
			Object output = null;
			int expPeriod = 0;

			//Mapping the payment attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//Getting the expiry period from the cache..
			String txnExpPeriod = WSDefaultInputsMap.getChkExpiryPeriod(context);
			if(txnExpPeriod!=null && !txnExpPeriod.trim().equalsIgnoreCase("")){
				expPeriod = ConvertionUtil.convertToint(txnExpPeriod.trim());
			}

			Calendar startDate = Calendar.getInstance();
			startDate.setTime(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			startDate.add(Calendar.DATE, -expPeriod);

			//Mapping the Utilized amount input details...
			UtilizedAmountTO objUtilizedAmnt = new UtilizedAmountTO();
			objUtilizedAmnt.setAuth_mode(Auth_Mode.VERBAL);
			objUtilizedAmnt.setSame_name_flg("N");
			objUtilizedAmnt.setFrom_account(objFromMSAcc_Details.getKeyAccount());
			objUtilizedAmnt.setStartDate(ConvertionUtil.convertToTimestamp(startDate.getTime()));
			objUtilizedAmnt.setEndDate(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));

			//Executing the  query for the same and getting the amount.
			EBWLogger.logDebug(this,"Executing.... "+utilizedAmntStmntId);
			output = executeQuery(utilizedAmntStmntId,objUtilizedAmnt,boolean1);
			if(output!=null){
				objUtilizedAmnt = (UtilizedAmountTO)output;
				if(objUtilizedAmnt.getUtilized_amount()!=null){
					objPaymentDetails.setUtilizedAmount(objUtilizedAmnt.getUtilized_amount());
				}
			}
			EBWLogger.logDebug(this,"Execution Completed.... "+utilizedAmntStmntId);
			EBWLogger.trace(this, "Finished with getCheckUtilizedAmount method of GetUtilizedAmount class");
		}
		catch(SQLException sqlexception){
			throw sqlexception;
		}
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}
}
