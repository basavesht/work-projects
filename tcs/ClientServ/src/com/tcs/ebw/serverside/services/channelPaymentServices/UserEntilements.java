package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.MSSystemDefaults;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DAPChkInputDetails;
import com.tcs.ebw.payments.transferobject.DsExtPayeeDetailsOutTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       15-06-2011        3               External Account Rule
 * **********************************************************
 */
public class UserEntilements extends DatabaseService{

	public UserEntilements(){

	}

	/**
	 * During transfer initiation , both the internal accounts needs to be present in the SSO_ACCOUNT_PROPERTIES
	 * During modification or cancellation , if the transfer type is INT or ACH-OUT then only from account needs to be entitled and checked .. 
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void checkIntAccEntitlement(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		boolean isAccountEligible = false;
		try 
		{
			//Payment Details attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//MS user details attributes..
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//MS From account attributes...
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//MS To account attributes...
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			//Statement Id and transfer objects declaration & initiation....
			String validateMSAccStmntId = "verifyMSAccountDAP";
			boolean boolean1 = Boolean.TRUE;
			Object objOutput = null;
			ArrayList msAccDtlList= new ArrayList();
			ArrayList msKeyAccNumbers = new ArrayList();
			String txn_type = objPaymentDetails.getTransfer_Type();
			String frm_KeyMSAccount = objFromMSAcc_Details.getKeyAccount();
			String to_KeyMSAccount = objToMSAcc_Details.getKeyAccount();

			//Input Mapping for the DAP check details TO from the payment details...
			DAPChkInputDetails dapChkInputDetails = new DAPChkInputDetails();
			dapChkInputDetails.setLife_user_id(objMSUserDetails.getUuid());
			EBWLogger.logDebug(this,"Input parameters for DAPChkInputDetails(Internal Transfer)... "+dapChkInputDetails.toString());

			//Executing the DAP query for getting the MS Account from the SSO_ACCOUNT_PROPERTES Table for the logged in user ... 
			EBWLogger.logDebug(this,"Executing.... "+validateMSAccStmntId);
			objOutput = executeQuery(validateMSAccStmntId,dapChkInputDetails,boolean1);
			EBWLogger.logDebug(this,"Execution Completed.... "+validateMSAccStmntId);
			if(objOutput!=null)
			{
				msAccDtlList = (ArrayList)objOutput;
				msAccDtlList.remove(0); // Removing the header from the output...
				if(!msAccDtlList.isEmpty())
				{
					//Getting all the MS stored key account numbers..
					for(int j=0;j<msAccDtlList.size();j++){
						ArrayList msAccNo = (ArrayList)msAccDtlList.get(j);
						msKeyAccNumbers.add(msAccNo.get(1));//MS Key Account Number.
					}

					if(txn_type!=null && (txn_type.equalsIgnoreCase(TxnTypeCode.INTERNAL)))
					{
						if(objPaymentDetails.isTxnInitiated()){
							if(frm_KeyMSAccount==null || to_KeyMSAccount==null){
								isAccountEligible=false;
							}
							else if(frm_KeyMSAccount.equalsIgnoreCase(to_KeyMSAccount)){
								isAccountEligible=false;
							}
							else if(msKeyAccNumbers.contains(frm_KeyMSAccount) && msKeyAccNumbers.contains(to_KeyMSAccount)){
								isAccountEligible=true;
							}
						}
						else if(objPaymentDetails.isTxnModified() || objPaymentDetails.isTxnCancelled()){
							if(frm_KeyMSAccount==null){
								isAccountEligible=false;
							}
							else if(msKeyAccNumbers.contains(frm_KeyMSAccount)){
								isAccountEligible=true;
							}
						}
					}
					else if(txn_type!=null && (txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || txn_type.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
					{
						if(objPaymentDetails.isTxnInitiated()){
							if(frm_KeyMSAccount==null ){
								isAccountEligible=false;
							}
							else if(msKeyAccNumbers.contains(frm_KeyMSAccount)){
								isAccountEligible=true;
							}
						}
						else if(objPaymentDetails.isTxnModified() || objPaymentDetails.isTxnCancelled()){
							if(frm_KeyMSAccount==null){
								isAccountEligible=false;
							}
							else if(msKeyAccNumbers.contains(frm_KeyMSAccount)){
								isAccountEligible=true;
							}
						}
					}
					else if(txn_type!=null && txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT))
					{
						if(objPaymentDetails.isTxnInitiated()){
							if(to_KeyMSAccount==null){
								isAccountEligible=false;
							}
							else if(msKeyAccNumbers.contains(to_KeyMSAccount) ){
								isAccountEligible=true;
							}
						}
						else {
							isAccountEligible=true;
						}
					}
				}
			}
			if(!isAccountEligible){
				EBWLogger.logDebug(this, "Internal accounts Entitlements check is failure");
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INT_ACC_DAP_FAILURE);
			}
			else {
				EBWLogger.logDebug(this, "Internal accounts Entitlements check is successfull");
			}
		} 
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/** 
	 * Checking the mentioned requirement..
	 * For ACH transactions we also have to verify if the hashed owner id (also coming from account plating web service) 
	 * of the account owner selected by the user is same as the hashed external account owner id present 
	 * on the external account involved in the transaction.
	 * This requirement is applicable only during edit and creation of transfers...
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public void checkExtAccOwner(HashMap txnDetails,ServiceContext serviceContext) throws SQLException, Exception
	{
		EBWLogger.trace("UserEntitlement", "Starting checkExtAccOwner method in UserEntitlement class");
		boolean isExtAccSelectedValid=false;
		String verifyExtAccountStmdId = "verifyExtAccountOwner"; 
		try
		{
			//Payment Details attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//External account details..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//MS_User_Details..
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//Variable and Object declaration...
			boolean boolean1 = Boolean.TRUE;
			Object objExtAccOutput = null;
			ArrayList<Object> extAccDtlList = new ArrayList<Object>();
			String txn_type = objPaymentDetails.getTransfer_Type();

			//Return in case of ACH-OUT transactions, since the same check is done from Business rules ..
			if(txn_type!=null && txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				return ;
			}

			//Input Mapping for the DAP check details TO from the payment details...
			DAPChkInputDetails dapChkInputDetails = new DAPChkInputDetails();
			dapChkInputDetails.setExternal_ref_id(objExternalAccDetails.getCpypayeeid());
			dapChkInputDetails.setKey_client_id(objMSUserDetails.getClientIdentifier());

			//Service call..
			EBWLogger.logDebug(this,"Executing.... "+ verifyExtAccountStmdId);
			objExtAccOutput = executeQuery(verifyExtAccountStmdId,dapChkInputDetails,boolean1);
			EBWLogger.logDebug(this,"Execution Completed.... "+ verifyExtAccountStmdId);

			//Output Extraction...
			if(objExtAccOutput!=null){
				extAccDtlList = (ArrayList)objExtAccOutput;
				extAccDtlList.remove(0);
				if(!extAccDtlList.isEmpty()){
					String extAccCheck = (String)((ArrayList)extAccDtlList.get(0)).get(0);
					int extAccVerifyKey = ConvertionUtil.convertToint(extAccCheck); // If the key is 1 , indicates external account is valid account , otherwise no...
					if(txn_type!=null && txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
						if(extAccVerifyKey==1){
							isExtAccSelectedValid=true;
						}
					}
				}
			}

			//If the account verification is failed then Critical error message is added to the context...
			if(!isExtAccSelectedValid){
				EBWLogger.logDebug(this, "External account owner validation is failure");
				if(objPaymentDetails.isTxnModified()){
					serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.EXT_ACC_OWNER_FAILURE_EDIT);
				}
				else{
					serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.EXT_ACC_OWNER_FAILURE);
				}
			}
			else {
				EBWLogger.logDebug(this, "External account owner validation is successfull");
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
	 * Checking the mentioned requirement..
	 * ABA number was deleted from the directory during the monthly file upload, 
	 * after which the user tried to initiate an ACH transaction
	 * This requirement is applicable only during edit and creation of External transfers...
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public void checkExtAccRoutingNum(HashMap txnDetails,ServiceContext serviceContext) throws SQLException, Exception
	{
		EBWLogger.trace("UserEntitlement", "Starting checkExtAccRoutingNum method in UserEntitlement class");
		boolean isExtAccRoutingNumValid=false;
		String verifyExtAccStmdId = "verifyExtAccRoutingNum"; 
		try
		{
			//Payment Details attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//External account details..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Variable and Object declaration...
			boolean boolean1 = Boolean.TRUE;
			Object objExtAccOutput = null;
			ArrayList<Object> extAccDtlList = new ArrayList<Object>();
			String txn_type = objPaymentDetails.getTransfer_Type();

			//Input Mapping for the DAP check details TO from the payment details...
			DAPChkInputDetails dapChkInputDetails = new DAPChkInputDetails();
			dapChkInputDetails.setExternal_routing_num(objExternalAccDetails.getCpybankcode());

			//Service call..
			EBWLogger.logDebug(this,"Executing.... "+ verifyExtAccStmdId);
			objExtAccOutput = executeQuery(verifyExtAccStmdId,dapChkInputDetails,boolean1);
			EBWLogger.logDebug(this,"Execution Completed.... "+ verifyExtAccStmdId);

			//Output extraction...
			if(objExtAccOutput!=null)
			{
				extAccDtlList = (ArrayList)objExtAccOutput;
				extAccDtlList.remove(0);
				if(!extAccDtlList.isEmpty()){
					String extAccCheck = (String)((ArrayList)extAccDtlList.get(0)).get(0);
					int extAccVerifyKey = ConvertionUtil.convertToint(extAccCheck); 
					if(txn_type!=null && txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) ){
						if(extAccVerifyKey!=0){
							isExtAccRoutingNumValid=true;
						}
					}
				}
			}

			//If the account verification is failed then Critical error message is added to the context...
			if(!isExtAccRoutingNumValid){
				EBWLogger.logDebug(this, "External account Routing number is Invalid...");
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INVALID_ROTUING_NUM);
			}
			else {
				EBWLogger.logDebug(this, "External account Routing number is Valid...");
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
	 * Checks for both the account owner and accout entitlement check...
	 * During transfer initiation , then external account needs to be entitled to the user..
	 * During modification or cancellation , if the transfer type is ACH-IN , then user needs to be entitled for the same..
	 * @param txnDetails
	 * @param isAccSuspended
	 * @param serviceContext
	 * @throws SQLException
	 * @throws Exception
	 */
	public void checkExtAccEntitlements(HashMap txnDetails,boolean isAccSuspended,ServiceContext serviceContext) throws SQLException, Exception
	{
		EBWLogger.trace("MSDapCheck", "Starting checkExtAccEntitlements method in MSDapCheck class");
		boolean isExtAccSelectedValid=false;
		try
		{
			//Payment Details attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//External account details..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//Variable and Object declaration...
			boolean boolean1 = Boolean.TRUE;
			Object objExtAccOutput = null;
			ArrayList<Object> extAccDtlList = new ArrayList<Object>();
			String txn_type = objPaymentDetails.getTransfer_Type();

			//Input Mapping for the DAP check details TO from the payment details...
			DAPChkInputDetails dapChkInputDetails = new DAPChkInputDetails();
			dapChkInputDetails.setExternal_ref_id(objExternalAccDetails.getCpypayeeid());

			//Statement Id and transfer objects declaration & initiation & suspension ....
			String verifyExtAccountStmdId = "verifyExtAccountDAP"; 
			if(isAccSuspended){
				verifyExtAccountStmdId= "verifySuspedExtAccDAP"; 
			}

			//Executing the DAP query for getting the External Account from the DS_PAYEE_REF Table for the logged in user in case of external transactions... 
			EBWLogger.logDebug(this,"Executing.... "+verifyExtAccountStmdId);
			objExtAccOutput = executeQuery(verifyExtAccountStmdId,dapChkInputDetails,boolean1);
			EBWLogger.logDebug(this,"Execution Completed.... "+verifyExtAccountStmdId);

			if(objExtAccOutput!=null)
			{
				extAccDtlList = (ArrayList)objExtAccOutput;
				extAccDtlList.remove(0); // Removing the header from the external account output...
				if(!extAccDtlList.isEmpty()){
					String extAccCheck = (String)((ArrayList)extAccDtlList.get(0)).get(0);
					int extAccVerifyKey = ConvertionUtil.convertToint(extAccCheck);
					if(txn_type!=null && (txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
						if(extAccVerifyKey==1){
							isExtAccSelectedValid=true;
						}
					}
				}
			}

			//Error handling...
			if(!isExtAccSelectedValid){
				EBWLogger.logDebug(this, "External account Entitlements check is failure");
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.EXT_ACC_DAP_FAILURE);
			}
			else {
				EBWLogger.logDebug(this, "External account Entitlements check is successfull");
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
	 * Checking the mentioned requirement..
	 * For ACH transactions exactly one account should be external account.
	 * For ACH transactions exactly one account should be MSSB account.
	 * If the external account is 3rd party, the transaction should not be ACH-IN.
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public void checkExtTxnRules(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace("UserEntitlement", "Starting checkExtAccRules method in UserEntitlement class");
		boolean isExtAccRulesValid_1 = false;
		boolean isExtAccRulesValid_2 = false;
		boolean isThirdPartyExtAccRulesValid = true;
		try
		{
			//Payment Details attributes..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//External account details..
			DsExtPayeeDetailsOutTO objExternalAccDetails = new DsExtPayeeDetailsOutTO();
			if(txnDetails.containsKey("ExternalAccDetails")){
				objExternalAccDetails = (DsExtPayeeDetailsOutTO)txnDetails.get("ExternalAccDetails");
			}

			//MS From account attributes...
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//MS To account attributes...
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			//Input Details..
			String txn_Type = objPaymentDetails.getTransfer_Type();
			String fromKeyAccount = objFromMSAcc_Details.getKeyAccount();
			String toKeyAccount = objToMSAcc_Details.getKeyAccount();
			String third_party_ind = objExternalAccDetails.getThird_party_ind();
			String externalRefId = objExternalAccDetails.getCpypayeeid();

			//Rule 1 : For ACH transactions exactly one account should be external account..
			if(externalRefId != null && !externalRefId.trim().equalsIgnoreCase("")){
				isExtAccRulesValid_1 = true;
			}

			//Rule 2 : For ACH transactions exactly one account should be MSSB account..
			if(txn_Type!=null && txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				if((fromKeyAccount != null && !fromKeyAccount.trim().equalsIgnoreCase(""))
						&& (toKeyAccount == null)){
					isExtAccRulesValid_2 = true;
				}
			}
			else if(txn_Type!=null && txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				if((toKeyAccount != null && !toKeyAccount.trim().equalsIgnoreCase(""))
						&& (fromKeyAccount == null)){
					isExtAccRulesValid_2 = true;
				}
			}

			//Rule 3 : If the external account is 3rd party, the transaction should not be ACH-IN..
			if(third_party_ind!=null && third_party_ind.equalsIgnoreCase(MSSystemDefaults.THRD_PARTY_Y)){
				if(txn_Type!=null && txn_Type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
					isThirdPartyExtAccRulesValid = false;
				}
			}

			//External Transaction Rule Violation...
			if(!isExtAccRulesValid_1 || !isExtAccRulesValid_2 ){
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.EXT_TXN_RULES_VIOLATION);
			}
			else if(!isThirdPartyExtAccRulesValid){
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.THIRD_PARTY_EXT_TXN_RULES_VIOLATION);
			}
		}	
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}
}
