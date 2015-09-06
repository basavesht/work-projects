package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.VersionChkInDtls;
import com.tcs.ebw.payments.transferobject.VersionChkOutDtls;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class VerifyVersionMismatch extends DatabaseService{

	public VerifyVersionMismatch(){

	}

	public final static String BATCHVERSION ="BATVER";  //DS_BATCH Version
	public final static String TXNVERSION   ="TXNVER";  //DS_PAY_TXNS Version
	public final static String RECCVERSION  ="RECVER";  //TXN_PARENT Version
	public final static String CHKVERSION   ="CHKVER";  //CHECK_TXN_DTLS Version

	/**
	 * Checks the Version Number of the current modified record stored in the hidden field with the version present in the DB 
	 *  if both the version matches then transaction is allowed to modified/delete/cancel, else error is thrown.
	 *  The following check needs to be done just before Update/delete/cancel of any transactions...
	 * @param versionType //Identifies which Table needs to be checked ...
	 * @param toObjects
	 * @param context
	 * @throws SQLException
	 * @throws Exception
	 */

	public void compareVersionNum(HashMap txnDetails,String versionType,ServiceContext context) throws SQLException, Exception 
	{
		EBWLogger.trace(this, "Setting and executing the Vesion check service..");
		try
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//Bypassing the Version Check based on the input flag..
			if(!objPaymentDetails.isVersionChkReq()){
				return;
			}

			//Getting the version check input details....
			VersionChkInDtls objVersionChkInDtls = new VersionChkInDtls();
			objVersionChkInDtls.setVersionChkType(objPaymentDetails.getVersionChkId());

			boolean boolean1 = Boolean.TRUE;
			Object objOutput = null;
			int currentVersion = 0;
			boolean isVersionMatched = false;
			String versionChkType = objVersionChkInDtls.getVersionChkType(); // Defines the TYPE for the Version Check(Transaction or Account)
			String versionChkStmntId = "";

			//Statement Id's and Transfer Object mapping...
			if(versionChkType!=null && versionChkType.equalsIgnoreCase("Transaction"))
			{
				objVersionChkInDtls.setTxnBatchRefId(objPaymentDetails.getTxnBatchId());
				objVersionChkInDtls.setTxnReferenceId(objPaymentDetails.getTransactionId());
				objVersionChkInDtls.setCurrentVersion(ConvertionUtil.convertToint(objPaymentDetails.getTransactionVersion()));
				objVersionChkInDtls.setCurrentBatVersion(ConvertionUtil.convertToint(objPaymentDetails.getTxnBatchVersion()));
				objVersionChkInDtls.setCurrentParVersion(ConvertionUtil.convertToint(objPaymentDetails.getRecParentTxnVersion()));
				objVersionChkInDtls.setCurrentChkReqVersion(ConvertionUtil.convertToint(objPaymentDetails.getChkTxnVersion()));

				if(versionType!=null && versionType.equalsIgnoreCase(RECCVERSION))
				{
					//TXN_PARENT Table Version check..
					versionChkStmntId = "getParentTxnVersionNum";
					currentVersion = objVersionChkInDtls.getCurrentParVersion();

					//Executing the Version number check for the transfers..
					EBWLogger.logDebug(this,"Executing.... "+versionChkStmntId);
					objOutput = executeQuery(versionChkStmntId,objVersionChkInDtls,boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+versionChkStmntId);

					//Comparing both the versions
					if(objOutput!=null && objOutput instanceof VersionChkOutDtls)
					{
						VersionChkOutDtls objVersionChkOutDtls = (VersionChkOutDtls)objOutput;
						Double dbVersionNum = objVersionChkOutDtls.getTxnversionnum();

						//Checking the db version number with the value stored at the start. 
						if(dbVersionNum!=null && currentVersion!=0){
							int dbVersionVal = dbVersionNum.intValue(); // Latest VERSION value obtained from the DB ..
							if(dbVersionVal==currentVersion){
								isVersionMatched=true;
							}
						}
					}
				}
				else if(versionType!=null && versionType.equalsIgnoreCase(BATCHVERSION))
				{
					//DS_BATCH Table Version check..
					versionChkStmntId = "getBatchVersionNum";
					currentVersion = objVersionChkInDtls.getCurrentBatVersion();

					//Executing the Version number check for the transfers..
					EBWLogger.logDebug(this,"Executing.... "+versionChkStmntId);
					objOutput = executeQuery(versionChkStmntId,objVersionChkInDtls,boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+versionChkStmntId);

					//Comparing both the versions
					if(objOutput!=null && objOutput instanceof VersionChkOutDtls)
					{
						VersionChkOutDtls objVersionChkOutDtls = (VersionChkOutDtls)objOutput;
						Double dbVersionNum = objVersionChkOutDtls.getBatversionnum();

						//Checking the db version number with the value stored at the start. 
						if(dbVersionNum!=null && currentVersion!=0){
							int dbVersionVal = dbVersionNum.intValue(); // Latest VERSION value obtained from the DB ..
							if(dbVersionVal==currentVersion){
								isVersionMatched=true;
							}
						}
					}
				}
				else if(versionType!=null && versionType.equalsIgnoreCase(TXNVERSION))
				{
					//DS_PAY_TXNS Table Version check..
					versionChkStmntId = "getTxnVersionNum";
					currentVersion = objVersionChkInDtls.getCurrentVersion();

					//Executing the Version number check for the transfers..
					EBWLogger.logDebug(this,"Executing.... "+versionChkStmntId);
					objOutput = executeQuery(versionChkStmntId,objVersionChkInDtls,boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+versionChkStmntId);

					//Comparing both the versions
					if(objOutput!=null && objOutput instanceof VersionChkOutDtls)
					{
						VersionChkOutDtls objVersionChkOutDtls = (VersionChkOutDtls)objOutput;
						Double dbVersionNum = objVersionChkOutDtls.getVersionnum();

						//Checking the db version number with the value stored at the start. 
						if(dbVersionNum!=null && currentVersion!=0){
							int dbVersionVal = dbVersionNum.intValue(); // Latest VERSION value obtained from the DB ..
							if(dbVersionVal==currentVersion){
								isVersionMatched=true;
							}
						}
					}
				}
				else if(versionType!=null && versionType.equalsIgnoreCase(CHKVERSION))
				{
					//CHECK_TXN_DTLS Table Version check..
					versionChkStmntId = "getChkVersionNum";
					currentVersion = objVersionChkInDtls.getCurrentChkReqVersion();

					//Executing the Version number check for the transfers..
					EBWLogger.logDebug(this,"Executing.... "+versionChkStmntId);
					objOutput = executeQuery(versionChkStmntId,objVersionChkInDtls,boolean1);
					EBWLogger.logDebug(this,"Execution Completed.... "+versionChkStmntId);

					//Comparing both the versions
					if(objOutput!=null && objOutput instanceof VersionChkOutDtls)
					{
						VersionChkOutDtls objVersionChkOutDtls = (VersionChkOutDtls)objOutput;
						Double dbVersionNum = objVersionChkOutDtls.getChkversionnum();

						//Checking the db version number with the value stored at the start. 
						if(dbVersionNum!=null && currentVersion!=0){
							int dbVersionVal = dbVersionNum.intValue(); // Latest VERSION value obtained from the DB ..
							if(dbVersionVal==currentVersion){
								isVersionMatched=true;
							}
						}
					}
				}
			}
			else if(versionChkType!=null && versionChkType.equalsIgnoreCase("Account"))
			{
				objVersionChkInDtls.setAccReferenceId(objPaymentDetails.getExtAccount_RefId());
				objVersionChkInDtls.setAccRefNumber(objPaymentDetails.getExtAccount_Number());
				objVersionChkInDtls.setCurrentVersion(ConvertionUtil.convertToint(objPaymentDetails.getExtAccount_Version()));

				versionChkStmntId = "getAccVersionNum";
				currentVersion = objVersionChkInDtls.getCurrentVersion();

				//Executing the Version number check for the external accounts.. (DS_PAYEE_REF)
				EBWLogger.logDebug(this,"Executing.... "+versionChkStmntId);
				objOutput = executeQuery(versionChkStmntId,objVersionChkInDtls,boolean1);
				EBWLogger.logDebug(this,"Execution Completed.... "+versionChkStmntId);

				//Comparing both the versions 
				if(objOutput!=null && objOutput instanceof VersionChkOutDtls)
				{
					VersionChkOutDtls objVersionChkOutDtls = (VersionChkOutDtls)objOutput;
					Double dbVersionNum = objVersionChkOutDtls.getVersionnum();

					//Checking the db version number with the value stored at the start. 
					if(dbVersionNum!=null && currentVersion!=0){
						int dbVersionVal = dbVersionNum.intValue(); // Latest VERSION value obtained from the DB ..
						if(dbVersionVal==currentVersion){
							isVersionMatched=true;
						}
					}
				}
			}
			else if(versionChkType!=null && versionChkType.equalsIgnoreCase("ReasonCode"))
			{
				objVersionChkInDtls.setAccReferenceId(objPaymentDetails.getExtAccount_RefId());
				objVersionChkInDtls.setCurrentVersion(ConvertionUtil.convertToint(objPaymentDetails.getExtAccount_Version()));

				versionChkStmntId = "getReasonCodeVersionNum";
				currentVersion = objVersionChkInDtls.getCurrentVersion();

				//Executing the Version number check for the external accounts.. (REASON_CODES)
				EBWLogger.logDebug(this,"Executing.... "+versionChkStmntId);
				objOutput = executeQuery(versionChkStmntId,objVersionChkInDtls,boolean1);
				EBWLogger.logDebug(this,"Execution Completed.... "+versionChkStmntId);

				//Comparing both the versions 
				if(objOutput!=null && objOutput instanceof VersionChkOutDtls)
				{
					VersionChkOutDtls objVersionChkOutDtls = (VersionChkOutDtls)objOutput;
					Double dbVersionNum = objVersionChkOutDtls.getVersionnum();

					//Checking the db version number with the value stored at the start. 
					if(dbVersionNum!=null && currentVersion!=0){
						int dbVersionVal = dbVersionNum.intValue(); // Latest VERSION value obtained from the DB ..
						if(dbVersionVal==currentVersion){
							isVersionMatched=true;
						}
					}
				}
			}

			//Checking the Version query output...
			if(!isVersionMatched){
				EBWLogger.logDebug(this,"Version Mismatch error occured....");

				ResourceBundle messages = ResourceBundle.getBundle("ErrMessage");
				String versionMismatchCode = "";
				if(versionChkType!=null && versionChkType.equalsIgnoreCase("Account")){
					versionMismatchCode="Pay_0060";
					context.addMessage(MessageType.SEVERE,ChannelsErrorCodes.BUSSINESS_ERROR,messages.getString(versionMismatchCode));
				}
				else if(versionChkType!=null && versionChkType.equalsIgnoreCase("Transaction")){
					versionMismatchCode="Pay_0054";
					context.addMessage(MessageType.SEVERE,ChannelsErrorCodes.VERSION_MISMATCH_ERROR,messages.getString(versionMismatchCode));
				}
				else if(versionChkType!=null && versionChkType.equalsIgnoreCase("ReasonCode")){
					versionMismatchCode="Pay_0054";
					context.addMessage(MessageType.SEVERE,ChannelsErrorCodes.VERSION_MISMATCH_ERROR,messages.getString(versionMismatchCode));
				}
			}
			else {
				EBWLogger.logDebug(this, "No Version Mismatch occurred");
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
		EBWLogger.trace("VerifyVersionMismatch", "Finished compareVersionNum method in VerifyVersionMismatch class");
	}
}
