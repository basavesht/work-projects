/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.Ext_Account_LinkTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class AccountLink extends DatabaseService
{
	/**
	 * Verify if the account link already exists..
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void getExtAccountLink(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Starting checkAccountLink method in AccountLink class");
		boolean isExtAccLinkExist = false;
		try
		{
			//External AccountDetails..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Instantiation...
			Boolean isTxnCommitReq = Boolean.TRUE;
			String stmntId="verifyAccountLink";
			ArrayList accLinkList = new ArrayList();

			//Input mappings..
			Ext_Account_LinkTO objAccountLink = new Ext_Account_LinkTO();
			objAccountLink.setExt_reference_id(objExternalAccDetails.getCpypayeeid());
			updateAccountDetails(txnDetails,objAccountLink,serviceContext);

			//Service call...
			Object output = null;
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);
			output = executeQuery(stmntId,objAccountLink,isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);

			//Output Extraction..
			if(output!=null)
			{
				accLinkList = (ArrayList)output;
				accLinkList.remove(0);
				if(!accLinkList.isEmpty()){
					String extAccCheck = (String)((ArrayList)accLinkList.get(0)).get(0);
					int extAccVerifyKey = ConvertionUtil.convertToint(extAccCheck); 
					if(extAccVerifyKey == 1){
						isExtAccLinkExist = true;
					}
				}
			}
			else {
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
			}

			//Business logic based on the external account link flag..
			if(isExtAccLinkExist){
				objExternalAccDetails.setExternal_acct_link("Y");
			}
			else { 
				objExternalAccDetails.setExternal_acct_link("N");
			}
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
	 * Verify if the account link already exists and create if its dosen't exist..
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void checkAccountLink(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Starting checkAccountLink method in AccountLink class");
		boolean isExtAccLinkExist = false;
		try
		{
			//External AccountDetails..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Instantiation...
			Boolean isTxnCommitReq = Boolean.TRUE;
			String stmntId="verifyAccountLink";
			ArrayList accLinkList = new ArrayList();
			String ext_sign_up_mode = objExternalAccDetails.getSign_up_mode();

			//Input mappings..
			Ext_Account_LinkTO objAccountLink = new Ext_Account_LinkTO();
			objAccountLink.setExt_reference_id(objExternalAccDetails.getCpypayeeid());
			updateAccountDetails(txnDetails,objAccountLink,serviceContext);

			//Service call...
			Object output = null;
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);
			output = executeQuery(stmntId,objAccountLink,isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);

			//Output Extraction..
			if(output!=null) {
				accLinkList = (ArrayList)output;
				accLinkList.remove(0);
				if(!accLinkList.isEmpty()){
					String extAccCheck = (String)((ArrayList)accLinkList.get(0)).get(0);
					int extAccVerifyKey = ConvertionUtil.convertToint(extAccCheck); 
					if(extAccVerifyKey == 1){
						isExtAccLinkExist = true;
					}
				}
			}
			else {
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
				return;
			}

			//Link creation in case the link is absent and external account sign up mode is online.
			if(!isExtAccLinkExist){
				if (ext_sign_up_mode!=null && ext_sign_up_mode.equalsIgnoreCase(MSSystemDefaults.ONLINE_MODE)){
					createAccountLink(txnDetails,serviceContext); //Create external account link with the internal account..
				}
			}
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
	 * Creating or inserting an link between MoneyMovement Identifier and Internal MS Account in EXT_ACCOUNT_LINK Table..
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void createAccountLink(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace(this, "Starting createAccountLink method in AccountLink class");
		Boolean isTxnCommitReq = Boolean.TRUE;
		String stmntId="createAccountLink";
		try
		{
			//Payment Detail attributes...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//User Details...
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//External AccountDetails..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			String userName=MSCommonUtils.extractCurrentUserName(objMSUserDetails);

			//Input mappings..
			Ext_Account_LinkTO objAccountLink = new Ext_Account_LinkTO();
			objAccountLink.setApprover_id(objMSUserDetails.getLoginId());
			objAccountLink.setApprover_name(userName);
			objAccountLink.setConfirmation_date(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			objAccountLink.setExt_reference_id(objExternalAccDetails.getCpypayeeid());
			updateAccountDetails(txnDetails,objAccountLink,serviceContext);

			//Service call...
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);
			execute(stmntId,objAccountLink,isTxnCommitReq);
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);
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
	 * Updating the External account link input TO with the accounDetais based on the transfer type..
	 * @param txnDetails
	 * @param objAccountLink
	 * @param serviceContext
	 * @throws Exception
	 */
	public void updateAccountDetails(HashMap txnDetails,Ext_Account_LinkTO objAccountLink,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Starting updateAccountDetails method in AccountLink class");
		try 
		{
			//Payment Detail attributes...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//From account details..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//To account details..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			String transferType = objPaymentDetails.getTransfer_Type();
			if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				objAccountLink.setInt_key_account_no(objFromMSAcc_Details.getKeyAccount());
				objAccountLink.setInt_office(objFromMSAcc_Details.getOfficeNumber());
				objAccountLink.setInt_account(objFromMSAcc_Details.getAccountNumber());
				objAccountLink.setInt_fa(objFromMSAcc_Details.getFaNumber());
			}
			else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				objAccountLink.setInt_key_account_no(objToMSAcc_Details.getKeyAccount());
				objAccountLink.setInt_office(objToMSAcc_Details.getOfficeNumber());
				objAccountLink.setInt_account(objToMSAcc_Details.getAccountNumber());
				objAccountLink.setInt_fa(objToMSAcc_Details.getFaNumber());
			}
		} 
		catch (Exception exception){
			throw exception;
		}
	}
}
