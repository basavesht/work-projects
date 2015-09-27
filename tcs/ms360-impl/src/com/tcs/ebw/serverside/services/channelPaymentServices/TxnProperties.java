package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.DateUtilities.DateFunctions;
import com.tcs.Payments.EAITO.MS_ACCOUNT_OUT_DTL;
import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.IRA_Input_Desc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.ms360.integration.MMAccount;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsPayTxnsTO;
import com.tcs.ebw.payments.transferobject.IRATransferTypeTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 *    224703       29-08-2011        3-B             CR 193
 * **********************************************************
 */
public class TxnProperties extends DatabaseService {

	/**
	 * Retrieve IRA Transaction properties..
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void getIRATxnDetails(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.logDebug(this,"IRA Txn details begins");
		boolean iraAccFlag = false;
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Account View Output Details...
			AccountDetails objAccDetails= new AccountDetails();
			if(txnDetails.containsKey("MerlinOutputDetails")){
				objAccDetails= (AccountDetails)txnDetails.get("MerlinOutputDetails");
			}

			MMAccount acntDetails = new MMAccount();
			String txn_type = objPaymentDetails.getTransfer_Type();
			String iraTxn_Mnemonic = objPaymentDetails.getRetirement_mnemonic();
			if(txn_type!=null && (txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || txn_type.startsWith(ChkReqConstants.CHK)))
			{
				ArrayList<Object> merlinOutputList = (ArrayList)objAccDetails.getInfoMerlinObject();

				//From account details extraction...
				MS_ACCOUNT_OUT_DTL fromAccMerlin = (MS_ACCOUNT_OUT_DTL)merlinOutputList.get(0);
				acntDetails.setAccountClass(fromAccMerlin.getACNT_CLS());
				acntDetails.setSubClass(fromAccMerlin.getSUB_CLASS());
				acntDetails.setIraCode(fromAccMerlin.getIRACODE());
				acntDetails.setDivPay(fromAccMerlin.getDIVPAY());

				iraAccFlag = MSCommonUtils.getIRATxnFlag(acntDetails);
			}
			else if(txn_type!=null && (txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)))
			{
				ArrayList<Object> merlinOutputList = (ArrayList)objAccDetails.getInfoMerlinObject();

				//To account details extraction...
				MS_ACCOUNT_OUT_DTL toAccMerlin = (MS_ACCOUNT_OUT_DTL)merlinOutputList.get(0);
				acntDetails.setAccountClass(toAccMerlin.getACNT_CLS());
				acntDetails.setSubClass(toAccMerlin.getSUB_CLASS());
				acntDetails.setIraCode(toAccMerlin.getIRACODE());
				acntDetails.setDivPay(toAccMerlin.getDIVPAY());

				iraAccFlag = MSCommonUtils.getIRATxnFlag(acntDetails);
			}

			//Validating the IRA Transaction flag...
			//If the Transaction is an IRA transaction but Retirement mnemonic is not selected then return error..
			if(iraAccFlag && (iraTxn_Mnemonic == null || iraTxn_Mnemonic.trim().equalsIgnoreCase(""))){
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.IRA_TXN_FAILURE);
			}
			else if(iraAccFlag && (iraTxn_Mnemonic!=null && !iraTxn_Mnemonic.trim().equalsIgnoreCase(""))){
				getIRATxnProperties(txnDetails,acntDetails,serviceContext);
			}
			else {
				objPaymentDetails.setRetirement_mnemonic(null);
				objPaymentDetails.setQualifier(null);
				objPaymentDetails.setReverse_qualifier(null);
				objPaymentDetails.setDisplay_qualifier(null);
				objPaymentDetails.setIraTxnFlag("N");
			}
			EBWLogger.logDebug(this,"IRA flag is "+ iraAccFlag);
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Retrieve Qualifier and Reverse Qualifier...
	 * @param txnDetails
	 * @param acntDetails
	 * @throws Exception 
	 */
	public void getIRATxnProperties(HashMap txnDetails,MMAccount acntDetails,ServiceContext serviceContext) throws Exception
	{
		Object objOutput=null;
		Boolean boolean1 = Boolean.TRUE;
		try
		{
			String stmntId = "getIRATxnProperties";

			//Payment details attributes mapping...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			String transferType = objPaymentDetails.getTransfer_Type();

			//Input Details mappings..
			//Data mapping for getting the IRA Transfer types...
			IRATransferTypeTO objIRATransferTypeTO = new IRATransferTypeTO();
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK))){
				objIRATransferTypeTO.setFrom_plan_code(acntDetails.getIraCode());
				objIRATransferTypeTO.setTo_plan_code(MSCommonUtils.getDefaultIRAPlanCode(transferType,serviceContext));
			}
			else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				objIRATransferTypeTO.setFrom_plan_code(MSCommonUtils.getDefaultIRAPlanCode(transferType,serviceContext));
				objIRATransferTypeTO.setTo_plan_code(acntDetails.getIraCode());
			}
			objIRATransferTypeTO.setTxn_type(MSCommonUtils.getIRATxnType(objPaymentDetails.getTransfer_Type()));
			objIRATransferTypeTO.setActive_flag(IRA_Input_Desc.ACTIVE);
			objIRATransferTypeTO.setRetirement_mnemonic(objPaymentDetails.getRetirement_mnemonic());

			//Execute the service..
			objOutput = executeQuery(stmntId,objIRATransferTypeTO,boolean1);

			//Output mappings...
			IRATransferTypeTO objIRATxnDetailsOut = (IRATransferTypeTO)objOutput;
			objPaymentDetails.setQualifier(objIRATxnDetailsOut.getQualifier());
			objPaymentDetails.setReverse_qualifier(objIRATxnDetailsOut.getReverse_qualifier());
			objPaymentDetails.setRetirement_mnemonic_desc(objIRATxnDetailsOut.getRetirement_mnemonic_desc());
			objPaymentDetails.setDisplay_qualifier(objIRATxnDetailsOut.getDisplay_qualifier());
			objPaymentDetails.setIraTxnFlag("Y");

			EBWLogger.logDebug(this, "Executed getIRATxnProperties");
			EBWLogger.trace(this, "Finished with getIRATxnProperties method of TxnProperties class");
		}
		catch(SQLException sqlexception) {
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
	 * Retrieve IRA Txn Type Description , Qualifier and Reverse Qualifier...
	 * @param txnDetails
	 * @param acntDetails
	 * @throws Exception 
	 */
	public void getIRATxnViewProperties(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		Object objOutput=null;
		Boolean boolean1 = Boolean.TRUE;
		try
		{
			String stmntId = "getIRATxnViewProperties";

			//Payment details attributes mapping...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Getting the details only if the transaction is an IRA...
			if(objPaymentDetails.getRetirement_mnemonic()!=null && !objPaymentDetails.getRetirement_mnemonic().trim().equalsIgnoreCase(""))
			{
				//Input Details mappings..
				//Data mapping for getting the IRA Transfer types...
				IRATransferTypeTO objIRATransferTypeTO = new IRATransferTypeTO();
				objIRATransferTypeTO.setTxnConfirmation_No(objPaymentDetails.getTransactionId());

				//Execute the service..
				objOutput = executeQuery(stmntId,objIRATransferTypeTO,boolean1);

				//Output mappings...
				IRATransferTypeTO objIRATxnDetailsOut = (IRATransferTypeTO)objOutput;
				objPaymentDetails.setRetirement_mnemonic_desc(objIRATxnDetailsOut.getRetirement_mnemonic_desc());
			}
			EBWLogger.logDebug(this, "Executed getIRATxnProperties");
			EBWLogger.trace(this, "Finished with getIRATxnProperties method of TxnProperties class");
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
	}

	/**
	 * Calculate Transaction Settlement date to be passed to Payments service..
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public static void calculateTxnSettleDate(HashMap txnDetails,DsPayTxnsTO ds_pay_txns,ServiceContext serviceContext) throws Exception 
	{
		boolean isTransferCutOffPassed = false;
		boolean isTransferDtHoliday = false;
		boolean isDirectDBcall = true;
		try
		{
			//Payment Details..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			String transferType = objPaymentDetails.getTransfer_Type();
			Date settleDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());

			//Default Mappings..
			ds_pay_txns.setActual_exe_dt(ConvertionUtil.convertToTimestamp(settleDate));
			ds_pay_txns.setPayabpaysendflag("N");

			if(MSCommonUtils.check24x7Access(transferType)) 
			{
				//Check if the Transfer date is holiday or not...
				Calendar transferDate_Cal = Calendar.getInstance();
				transferDate_Cal.setTime(settleDate);
				isTransferDtHoliday = DateFunctions.checkHoliday(transferDate_Cal,transferType,isDirectDBcall,serviceContext);

				//Check the Cut Off Time ...
				if(!isTransferDtHoliday) {
					ValidateCutOffTime objValidateCutOffTime = new ValidateCutOffTime();
					objValidateCutOffTime.setConnection(serviceContext.getConnection());
					objValidateCutOffTime.checkCutOffTime_Error(txnDetails,serviceContext);
					if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
						isTransferCutOffPassed = true;
						serviceContext.removeMessage(MessageType.SEVERE,ChannelsErrorCodes.CUT_OFF_TIME_EXCEEDED);
					}
				}

				//Setting the settle date and margin booking flag . If flag is N then transaction has to go in current date's margin..
				if(isTransferDtHoliday || isTransferCutOffPassed){
					settleDate = DateFunctions.getNextFutureBusinessDay(transferDate_Cal, transferType, isDirectDBcall, serviceContext);
					ds_pay_txns.setActual_exe_dt(ConvertionUtil.convertToTimestamp(settleDate));
					ds_pay_txns.setPayabpaysendflag("Y");
				}
			}
		}
		catch(Exception exception){
			throw exception;
		}
	}
}
