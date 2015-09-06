/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.MO_ATTR_EVENT_EVAL;
import com.tcs.Payments.EAITO.MO_RCVR_ADRS;
import com.tcs.Payments.EAITO.NOTIFICATION_EVENT;
import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.NotificationEventDesc;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.NotificationInDetails;
import com.tcs.ebw.payments.transferobject.NotificationOutDetails;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *   224703			30-05-2011		P3				 3rd Party ACH - Alerts
 *    224703       23-09-2011        P3-B            PLA  
 * **********************************************************
 */
public class SendNotifications 
{
	/** 
	 * This function sets the input required for the Notification service for both External and Transactions....
	 * @param notifyDetails
	 * @return Vector
	 * @throws Exception
	 * @throws SQLException
	 */
	public Vector setNotificationDetails(HashMap notifyDetails,ServiceContext serviceContext) throws Exception,SQLException 
	{
		EBWLogger.trace(this, "Setting the notifications vector input parameters..");
		Vector notification_input = new Vector();
		try
		{
			//Notification Service attributes...
			NOTIFICATION_EVENT objNotification_Event = new NOTIFICATION_EVENT();
			Vector<MO_ATTR_EVENT_EVAL> objMo_attr_event_eval_vec = new Vector<MO_ATTR_EVENT_EVAL>();
			Vector<MO_RCVR_ADRS> objMo_rcvr_adrs_vec = new Vector<MO_RCVR_ADRS>();

			NotificationInDetails objNotificationInDetails = new NotificationInDetails();
			if(notifyDetails.containsKey("NotificationInAttributes")){
				objNotificationInDetails = (NotificationInDetails)notifyDetails.get("NotificationInAttributes");
			}

			NotificationOutDetails objNotificationOut = new NotificationOutDetails();
			if(notifyDetails.containsKey("NotificationOutAttributes")){
				objNotificationOut = (NotificationOutDetails)notifyDetails.get("NotificationOutAttributes");
			}

			//Setting all the attribute flag for various events..
			String event_desc = objNotificationInDetails.getEVENT_DESC();
			boolean isExternalAcc = objNotificationInDetails.isExternalAccount();
			boolean isDsntKeyAcntNumber = objNotificationInDetails.isDsntKeyAcntNumber();
			boolean isFrequency = objNotificationInDetails.isFrequency();
			boolean isUnitlValue = objNotificationInDetails.isUnitlValue();
			boolean isSkippedExecDate = objNotificationInDetails.isSkippedExecDate();
			boolean isNextTransferDate = objNotificationInDetails.isNextTransferDate();
			boolean isAccountInitDate = objNotificationInDetails.isAccountInitDate();
			boolean isAccountSuspendDate = objNotificationInDetails.isAccountSuspendDate();
			boolean isTxnSuspendDate = objNotificationInDetails.isTxnSuspendDate();
			boolean isReturnCodeDesc = objNotificationInDetails.isReturnCodeDesc();
			boolean isAcntOwnerMSSB = objNotificationInDetails.isAccountOwnerMSSB();
			boolean isPrimaryAccount = objNotificationInDetails.isPrimaryAccount();
			boolean isBankName = objNotificationInDetails.isBankName();

			if(!isExternalAcc)
			{
				//Setting Notification EVENT_ID(Single Row Object) [NOTIFICATION_EVENT Class]..
				if(event_desc!=null && !event_desc.trim().equalsIgnoreCase("")){
					objNotification_Event.setEVENT_DESC(event_desc);
				}

				//1.Setting the DebitAccount in the specified format (3-6-3 format).
				if(objNotificationOut.getPaydebitaccnum()!=null){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("DebitAccount");
					if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
						String payeeDebiAcc = objNotificationOut.getExternalAccNickName();
						// The external account holder's NickName is stored in [nickname] XX-[last 4 digits] format
						objMo_attr_event_eval.setATTR_VAL(payeeDebiAcc);
					}
					else if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						objMo_attr_event_eval.setATTR_VAL(formatMSAcc(objNotificationOut.getFrombr_acct_no_fa()));
					}
					else{
						objMo_attr_event_eval.setATTR_VAL(formatMSAcc(objNotificationOut.getFrombr_acct_no_fa()));
					}
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//2.Setting the DebitAccountMask in the specified format (XXX-XX1234).
				if(objNotificationOut.getPaydebitaccnum()!=null){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					String debitAccount = "";
					objMo_attr_event_eval.setATTR_NM("DebitAccountMask");
					if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
						debitAccount = objNotificationOut.getExternalAccNickName();
						// The external account holder's NickName is stored in [nickname] XX[last 4 digits] format
						objMo_attr_event_eval.setATTR_VAL(debitAccount);
					}
					else if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						debitAccount = objNotificationOut.getPaydebitaccnum();
						debitAccount = "XXX-XX"+ debitAccount.substring(2);
						objMo_attr_event_eval.setATTR_VAL(debitAccount);
					}
					else{
						debitAccount=objNotificationOut.getPaydebitaccnum();
						debitAccount = "XXX-XX"+ debitAccount.substring(2);
						objMo_attr_event_eval.setATTR_VAL(debitAccount);
					}
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//3.Setting the CreditAccount in the specified format (3-6-3 format).
				if(objNotificationOut.getPaydebitaccnum()!=null && objNotificationOut.getPaypayeeaccnum()!=null){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("CreditAccount");
					if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
						objMo_attr_event_eval.setATTR_VAL(formatMSAcc(objNotificationOut.getTobr_acct_no_fa()));
					}
					else if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						String creditAccount = objNotificationOut.getExternalAccNickName();
						// The external account holder's NickName is stored in [nickname] XX[last 4 digits] format
						objMo_attr_event_eval.setATTR_VAL(creditAccount);
					}
					else if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
						objMo_attr_event_eval.setATTR_VAL(getLoanAcntFormat(objNotificationOut.getPaypayeeaccnum()));
					}
					else{
						objMo_attr_event_eval.setATTR_VAL(formatMSAcc(objNotificationOut.getTobr_acct_no_fa()));
					}
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//4.Setting the CreditAccountMask in the specified format (XXX-XX1234).
				if(objNotificationOut.getPaydebitaccnum()!=null && objNotificationOut.getPaypayeeaccnum()!=null){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					String creditAccount = "" ;
					objMo_attr_event_eval.setATTR_NM("CreditAccountMask");
					if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
						creditAccount = objNotificationOut.getPaydebitaccnum();
						creditAccount = "XXX-XX"+ creditAccount.substring(2);
						objMo_attr_event_eval.setATTR_VAL(creditAccount);
					}
					else if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						creditAccount = objNotificationOut.getExternalAccNickName();
						//The external account holder's NickName is stored in [nickname] XX[last 4 digits] format
						objMo_attr_event_eval.setATTR_VAL(creditAccount);
					}
					else if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
						objMo_attr_event_eval.setATTR_VAL(getLoanAcntFormat(objNotificationOut.getPaypayeeaccnum()));
					}
					else{
						creditAccount = objNotificationOut.getPaypayeeaccnum();
						creditAccount = "XXX-XX"+ creditAccount.substring(2);
						objMo_attr_event_eval.setATTR_VAL(creditAccount);
					}
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//5.Setting the Currency.
				if(objNotificationOut.getCurrency()!=null){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("Currency");
					objMo_attr_event_eval.setATTR_VAL(objNotificationOut.getCurrency());
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//6.Setting the Amount.
				if(objNotificationOut.getAmount()!=null){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("Amount");
					objMo_attr_event_eval.setATTR_VAL(ConvertionUtil.convertToString(objNotificationOut.getAmount()));
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//7.Setting the ExecutionDate.
				if(objNotificationOut.getExecutionDate()!=null){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); //Unformatted Amount to be sent to the Notification service
					objMo_attr_event_eval.setATTR_NM("ExecutionDate");
					objMo_attr_event_eval.setATTR_VAL(dateFormat.format(objNotificationOut.getExecutionDate()));
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//8.Setting the Frequency.
				if(objNotificationOut.getFrequency()!=null && isFrequency){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("Frequency");
					objMo_attr_event_eval.setATTR_VAL(objNotificationOut.getFrequency());
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//9.Setting the UntilValue.
				if(objNotificationOut.getUntilValue()!=null && isUnitlValue){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("UntilValue");
					String untilValue = getNotification_UntilValue(objNotificationOut);
					objMo_attr_event_eval.setATTR_VAL(untilValue);
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//10.Setting the ConfirmationNumber.
				if(objNotificationOut.getConfirmationNumber()!=null){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("ConfirmationNumber");
					objMo_attr_event_eval.setATTR_VAL(objNotificationOut.getConfirmationNumber());
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//11.Setting the TxnType.
				if(objNotificationOut.getTxnType()!=null){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("TxnType");
					String txn_type = getNotification_Txn_Type(objNotificationOut);
					objMo_attr_event_eval.setATTR_VAL(txn_type);
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//12.Setting the SrcKeyAcntNumber.
				if(objNotificationOut.getSrcKeyAcntNumber()!=null){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("SrcKeyAcntNumber");
					objMo_attr_event_eval.setATTR_VAL(objNotificationOut.getSrcKeyAcntNumber());
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//13.Setting the DstnKeyAcntNumber.
				if(objNotificationOut.getDstnKeyAcntNumber()!=null && isDsntKeyAcntNumber){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("DstnKeyAcntNumber");
					objMo_attr_event_eval.setATTR_VAL(ConvertionUtil.convertToString(objNotificationOut.getDstnKeyAcntNumber()));
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//14.Setting the Skipped Execution Date ONLY in case of Skip Transfer.
				if(objNotificationOut.getSkippedExecutionDate()!=null && isSkippedExecDate){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); //Unformatted Date to be sent to the Notification service
					objMo_attr_event_eval.setATTR_NM("SkippedExecutionDate");
					objMo_attr_event_eval.setATTR_VAL(dateFormat.format(objNotificationOut.getSkippedExecutionDate()));
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//15.Setting the Next Execution Date ONLY in case of Skip Transfer.
				if(objNotificationOut.getNextExecutionDate()!=null && isNextTransferDate){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); //Unformatted Date to be sent to the Notification service
					objMo_attr_event_eval.setATTR_NM("NextExecutionDate");
					objMo_attr_event_eval.setATTR_VAL(dateFormat.format(objNotificationOut.getNextExecutionDate()));
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//16.Setting the TxnSuspendDate ONLY in case of Suspend Transactions.
				if(isTxnSuspendDate){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); //Unformatted Date to be sent to the Notification service
					objMo_attr_event_eval.setATTR_NM("TxnSuspendDate");
					objMo_attr_event_eval.setATTR_VAL(dateFormat.format(MSCommonUtils.getCurrentTime()));
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//17.Setting the ReturnCodeDesc in case of Suspend Transactions.
				if(isReturnCodeDesc){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("ReturnCodeDesc");
					objMo_attr_event_eval.setATTR_VAL("External Account Suspend");
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//18.LifeTimeUserID mapping for determining EMAIL or ALERT Setup for Transaction case...
				MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
				objMo_attr_event_eval.setATTR_NM("LifeTimeUserID");
				objMo_attr_event_eval.setATTR_VAL(isLifeUserIdExists(objNotificationOut.getLifeTimeUserId()));
				objMo_attr_event_eval_vec.add(objMo_attr_event_eval);

				//19.Setting the RMRKS and DISPATCH_ADDR values [MO_RCVR_ADRS Class](Multiple Row Object)..
				MO_RCVR_ADRS objMo_rcvr_adrs = new MO_RCVR_ADRS();
				objMo_rcvr_adrs.setRMRKS(NotificationEventDesc.APPLICATION_ID); 
				objMo_rcvr_adrs.setDISPATCH_ADDR(objNotificationOut.getLifeTimeUserId());
				objMo_rcvr_adrs_vec.add(objMo_rcvr_adrs);
			}
			else 
			{
				//Setting Notification EVENT_ID [NOTIFICATION_EVENT Class](Single Row Object)..
				if(event_desc!=null && !event_desc.trim().equalsIgnoreCase("")){
					objNotification_Event.setEVENT_DESC(event_desc);
				}

				//Setting the Notification ATTR_NM and ATTR_VAL [MO_ATTR_EVENT_EVAL Class](Multiple Row Object)...
				//1.Setting the External Account Number.
				if(objNotificationOut.getExternalAccount()!=null ){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("ExternalAccount");
					objMo_attr_event_eval.setATTR_VAL(objNotificationOut.getExternalAccount());
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//2.Setting the External Account Initiation Date.
				if(objNotificationOut.getAccountInitDate()!=null && isAccountInitDate){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); //Unformatted Amount to be sent to the Notification service
					objMo_attr_event_eval.setATTR_NM("AccountInitDate");
					objMo_attr_event_eval.setATTR_VAL(dateFormat.format(objNotificationOut.getAccountInitDate()));
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//3.Setting the AccountSuspendDate ONLY in case of Suspend Account.
				if(isAccountSuspendDate){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); //Unformatted Date to be sent to the Notification service
					objMo_attr_event_eval.setATTR_NM("AccountSuspendDate");
					objMo_attr_event_eval.setATTR_VAL(dateFormat.format(MSCommonUtils.getCurrentTime()));
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//4.Setting the Bank Name.
				if(isBankName){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("BankName");
					objMo_attr_event_eval.setATTR_VAL(objNotificationOut.getBankName());
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//5.Setting the PrimaryAccount.
				if(isPrimaryAccount){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("PrimaryAccount");
					objMo_attr_event_eval.setATTR_VAL(formatMSAcc(objNotificationOut.getPrimaryAccount()));
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//6.Setting the MSSBAccountOwner.
				if(isAcntOwnerMSSB){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("MSSBAcctOwner");
					objMo_attr_event_eval.setATTR_VAL(objNotificationOut.getAccountOwnerMSSB());
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//7.Setting the PrimaryAccount.
				if(isPrimaryAccount){
					MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
					objMo_attr_event_eval.setATTR_NM("isPrimaryAccountExist");
					objMo_attr_event_eval.setATTR_VAL(isPrimaryAcntExists(objNotificationOut.getPrimaryAccount()));
					objMo_attr_event_eval_vec.add(objMo_attr_event_eval);
				}

				//8.LifeTimeUserID mapping for determining EMAIL or ALERT Setup for Transaction case...
				MO_ATTR_EVENT_EVAL objMo_attr_event_eval = new MO_ATTR_EVENT_EVAL();
				objMo_attr_event_eval.setATTR_NM("LifeTimeUserID");
				objMo_attr_event_eval.setATTR_VAL(isLifeUserIdExists(objNotificationOut.getLifeTimeUserId()));
				objMo_attr_event_eval_vec.add(objMo_attr_event_eval);

				//9.Setting the TxnType.
				MO_ATTR_EVENT_EVAL objMo_attr_event_txntype = new MO_ATTR_EVENT_EVAL();
				objMo_attr_event_txntype.setATTR_NM("TxnType");
				String txn_type = NotificationEventDesc.EXTAccountTxn;
				objMo_attr_event_txntype.setATTR_VAL(txn_type);
				objMo_attr_event_eval_vec.add(objMo_attr_event_txntype);

				//10.Setting the RMRKS and DISPATCH_ADDR[MO_RCVR_ADRS Class]values..
				MO_RCVR_ADRS objMo_rcvr_adrs = new MO_RCVR_ADRS();
				objMo_rcvr_adrs.setRMRKS(NotificationEventDesc.APPLICATION_ID); 
				objMo_rcvr_adrs.setDISPATCH_ADDR(objNotificationOut.getLifeTimeUserId());
				objMo_rcvr_adrs_vec.add(objMo_rcvr_adrs);
			}

			//Setting the NotificationInput Details to be sent to SI in vector format ...
			notification_input.add(objNotification_Event);
			notification_input.add(objMo_attr_event_eval_vec);
			notification_input.add(objMo_rcvr_adrs_vec);

			EBWLogger.logDebug(this, "Executed setNotificationDetails");
			EBWLogger.trace(this, "Finished with setNotificationDetails method of SendNotifications class");
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
		EBWLogger.logDebug(this, "Input parameters to the Notifications are :"+notification_input.toString());
		return notification_input;
	}

	/**
	 * Sets the Txn_type as per the Modeling sheet for Notification service..
	 * @param objNotificationOut
	 * @return String
	 * @throws Exception
	 */
	public String getNotification_Txn_Type(NotificationOutDetails objNotificationOut) throws Exception
	{
		try 
		{
			String notification_TxnType = null;
			if(objNotificationOut.getTxnType()!=null && (objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.INTERNAL) || objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
				notification_TxnType = NotificationEventDesc.MSInternal;
			}
			else if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)) {
				notification_TxnType= NotificationEventDesc.ACHDeposit;
			}
			else if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)) {
				notification_TxnType= NotificationEventDesc.ACHWithdrawal;
			}
			else if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(ChkReqConstants.CHK_LOC)) {
				notification_TxnType= NotificationEventDesc.CHKLOC;
			}
			else if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(ChkReqConstants.CHK_REG)) {
				notification_TxnType= NotificationEventDesc.CHKREG;
			}
			else if(objNotificationOut.getTxnType()!=null && objNotificationOut.getTxnType().equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) && objNotificationOut.getTrial_depo()!=null && objNotificationOut.getTrial_depo().equalsIgnoreCase("Y")) {
				notification_TxnType= NotificationEventDesc.ACHTrialDeposit;
			}
			return notification_TxnType;
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Sets the Until Value for the Notification Service.
	 * @param objNotificationOut
	 * @return String
	 * @throws Exception
	 */
	public String getNotification_UntilValue(NotificationOutDetails objNotificationOut) throws Exception
	{
		try 
		{
			String notification_UntilValue = null;
			String transferType = objNotificationOut.getTxnType();

			if(objNotificationOut.getUntilValue()!=null && objNotificationOut.getUntilValue().equalsIgnoreCase("1")){
				notification_UntilValue = NotificationEventDesc.UNTILVALUE_CANCEL;
			}
			else if(objNotificationOut.getUntilValue()!=null && objNotificationOut.getUntilValue().equalsIgnoreCase("2")) {
				notification_UntilValue= ConvertionUtil.convertToAppDateStr(objNotificationOut.getUntilEndDate());
			}
			else if(objNotificationOut.getUntilValue()!=null && objNotificationOut.getUntilValue().equalsIgnoreCase("3")) {
				notification_UntilValue = MSCommonUtils.formatTxnAmount(objNotificationOut.getUntilAmountLimit())+ NotificationEventDesc.UNTILVALUE_DOLLARLIMIT;
			}
			else if(objNotificationOut.getUntilValue()!=null && objNotificationOut.getUntilValue().equalsIgnoreCase("4")) {
				notification_UntilValue= ConvertionUtil.convertToString(objNotificationOut.getUntilNoOfTransfers())+ NotificationEventDesc.UNTILVALUE_NOOFTRANSFERS;
				if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
					notification_UntilValue = ConvertionUtil.convertToString(objNotificationOut.getUntilNoOfTransfers())+ NotificationEventDesc.UNTILVALUE_NOOFTRANSFERS_CHK;
				}
			}
			return notification_UntilValue;
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Formats the MS account to 3-6-3 format
	 * @param msAccount is the 12 digit account number 
	 * @return
	 */
	public String formatMSAcc(String msAccount) {
		String msFormattedAcc = msAccount;
		if(msAccount!= null && msAccount.length()>=12){
			msFormattedAcc = msAccount.substring(0,3)+"-"+msAccount.substring(3,9)+"-"+msAccount.substring(9);
		}
		return msFormattedAcc;
	}

	/**
	 * Setting the Notification Loan Account format .... 
	 */
	public static String getLoanAcntFormat(String loanAcntNo)
	{
		if(loanAcntNo!=null && !loanAcntNo.trim().equalsIgnoreCase(""))
		{
			String bankNum = loanAcntNo.substring(0,3); 
			String loanNum = loanAcntNo.substring(3); 
			if(loanNum.length()>3)
			{
				int dispStartIndex = loanNum.length()-3;
				loanNum = loanNum.substring(dispStartIndex);
			}
			loanAcntNo = MSSystemDefaults.LOAN_ACC_PREFIX_TEXT+" "+bankNum+" "+"XXXX"+loanNum;
		}
		return loanAcntNo;
	}
	/**
	 * Flag to indicate if Primary Account exists...
	 * @param primaryAccount
	 * @return
	 */
	public String isPrimaryAcntExists (String primaryAccount) {
		String primaryAcntFlag = "0";
		if(primaryAccount!=null && !primaryAccount.trim().equalsIgnoreCase("")){
			primaryAcntFlag = "1" ;
		}
		return primaryAcntFlag;
	}

	/**
	 * Flag to indicate if Life UserId exists...
	 * @param lifeUserId
	 * @return
	 */
	public String isLifeUserIdExists (String lifeUserId) {
		String lifeUserIdFlag = "0";
		if(lifeUserId!=null && !lifeUserId.trim().equalsIgnoreCase("")){
			lifeUserIdFlag = "1" ;
		}
		return lifeUserIdFlag;
	}
}
