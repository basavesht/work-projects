package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.ms360.integration.AccessibleAccount;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.ConfirmPrintSuccessOutputTO;
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
 *
 * **********************************************************
 */
public class UserEntilements extends DatabaseService{

	public UserEntilements(){

	}

	/**
	 * Checking the Internal account data access...
	 * User should be entitled to
	 * The From account: If the transaction is an internal, ACH withdrawal or check transfer request.
	 * The To account: If the transaction is an ACH deposit.
	 * @param txnDetails
	 * @return
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

			String from_account_fa = objFromMSAcc_Details.getFaNumber();
			String from_account_branchId = objFromMSAcc_Details.getOfficeNumber();
			String to_account_fa = objToMSAcc_Details.getFaNumber();
			String to_account_branchId = objToMSAcc_Details.getOfficeNumber();
			String transferType = objPaymentDetails.getTransfer_Type();

			ArrayList eligibleAccounts = objMSUserDetails.getAccessibleAccounts();
			for(int i=0;i<eligibleAccounts.size();i++){
				AccessibleAccount objAccessibleAcc = (AccessibleAccount)eligibleAccounts.get(i);
				if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK)))
				{
					if(objAccessibleAcc.isSuperUser()){
						isAccountEligible = true;
						break;
					}
					else{
						if(objAccessibleAcc.getBranch()==Integer.parseInt(from_account_branchId) && (objAccessibleAcc.getFA()==0)){
							isAccountEligible = true;
							break;
						}
						else if(objAccessibleAcc.getBranch()==Integer.parseInt(from_account_branchId) && (objAccessibleAcc.getFA()==Integer.parseInt(from_account_fa))){
							isAccountEligible = true;
							break;
						}
					}
				}
				else if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT))
				{
					if(objAccessibleAcc.isSuperUser()){
						isAccountEligible = true;
						break;
					}
					else {
						if(objAccessibleAcc.getBranch()==Integer.parseInt(to_account_branchId) && (objAccessibleAcc.getFA()==0)){
							isAccountEligible = true;
							break;
						}
						else if(objAccessibleAcc.getBranch()==Integer.parseInt(to_account_branchId) && (objAccessibleAcc.getFA()==Integer.parseInt(to_account_fa))){
							isAccountEligible = true;
							break;
						}
					}
				}
			}
			//If the account verification is failed then Critical error message is added to the context...
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

			//Variable and Object declaration...
			boolean boolean1 = Boolean.TRUE;
			Object objExtAccOutput = null;
			ArrayList<Object> extAccDtlList = new ArrayList<Object>();
			String txn_type = objPaymentDetails.getTransfer_Type();
			String[] key_client_id = null;

			//Setting the MS account key_client_id based on the transfer type..
			if(txn_type!=null && (txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
				key_client_id = objFromMSAcc_Details.getKeyClientId();
			}
			else if(txn_type!=null && (txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT))){
				key_client_id = objToMSAcc_Details.getKeyClientId();
			}

			//Input Mapping for the DAP check details TO from the payment details...
			DAPChkInputDetails dapChkInputDetails = new DAPChkInputDetails();
			dapChkInputDetails.setExternal_ref_id(objExternalAccDetails.getCpypayeeid());
			dapChkInputDetails.setKey_client_id(key_client_id);

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
					if(txn_type!=null && txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) ){
						if(extAccVerifyKey==1){
							isExtAccSelectedValid=true;
						}
					}
				}
			}

			//If the account verification is failed then Critical error message is added to the context...
			if(!isExtAccSelectedValid){
				EBWLogger.logDebug(this, "External account owner validation is failure");
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.EXT_ACC_OWNER_FAILURE);
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
	 * For ACH transactions we also have to verify if the hashed owner id (also coming from account plating web service) 
	 * of the Spoke To option selected by the user is same as the hashed external account owner id present 
	 * on the external account involved in the transaction.
	 * This requirement is applicable only during edit and creation of transfers...
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public void checkSpokeToOwner(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace("UserEntitlement", "Starting checkSpokeToOption method in UserEntitlement class");
		boolean isExtAccSpokeValid = true;
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

			//Payment Details...
			String txn_type = objPaymentDetails.getTransfer_Type();
			String auth_mode = objPaymentDetails.getAuth_mode();
			String int_acnt_owner = objPaymentDetails.getVerbal_auth_client_name();
			String ext_acnt_owner = objExternalAccDetails.getKey_client();

			//Output Extraction...
			if(auth_mode!=null && !auth_mode.trim().equalsIgnoreCase(""))
			{
				if(txn_type!=null && txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
					if(int_acnt_owner!=null && ext_acnt_owner!=null && !int_acnt_owner.equalsIgnoreCase(ext_acnt_owner)){
						isExtAccSpokeValid=false;
					}
				}
			}

			//If the account spoke to verification is failed then Critical error message is added to the context...
			if(!isExtAccSpokeValid){
				EBWLogger.logDebug(this, "External account Spoke to validation is failure");
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.EXT_ACC_SPOKE_TO_FAILURE);
			}
			else {
				EBWLogger.logDebug(this, "External account Spoke to validation is successfull");
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
			if(objExtAccOutput!=null){
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
	 * Checking the external account active status ..
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public void checkExtAccEntitlements(HashMap txnDetails,boolean isAccSuspended,ServiceContext serviceContext) throws SQLException, Exception
	{
		EBWLogger.trace("UserEntitlement", "Starting checkExtAccEntitlements method in UserEntitlement class");
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

			//Service call..
			if(txn_type!=null && (txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT))){
				EBWLogger.logDebug(this,"Executing.... "+verifyExtAccountStmdId);
				objExtAccOutput = executeQuery(verifyExtAccountStmdId,dapChkInputDetails,boolean1);
				EBWLogger.logDebug(this,"Execution Completed.... "+verifyExtAccountStmdId);
			}

			//Output extraction...
			if(objExtAccOutput!=null){
				extAccDtlList = (ArrayList)objExtAccOutput;
				extAccDtlList.remove(0); 
				if(!extAccDtlList.isEmpty()){
					String extAccCheck = (String)((ArrayList)extAccDtlList.get(0)).get(0);
					int extAccVerifyKey = ConvertionUtil.convertToint(extAccCheck); // If the key is 1 , indicates external account is valid account , otherwise no...
					if(txn_type!=null && txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || txn_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) ){
						if(extAccVerifyKey==1){
							isExtAccSelectedValid=true;
						}
					}
				}
			}

			//If the account verification is failed then Critical error message is added to the context...
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
	 * Checking the functional access...
	 * User’s functional entitlements will be verified to check if the user has functional access 
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public void checkFunctionalEntitlement(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		boolean isActionEligible = false;
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//Single functional role for any event ...
			String screen  = objPaymentDetails.getScreen();
			String action  = objPaymentDetails.getAction();
			String state   = objPaymentDetails.getState();
			String functionalRole="F"+screen+state+action;

			//Multiple functional role for the any single event (E.g Cancel) ..
			ArrayList multipleRoleList = objPaymentDetails.getUserFuncRoleList();

			//Checking the functional role..
			ArrayList userActions = objMSUserDetails.getFunctionalRoleList();
			if(!userActions.isEmpty() && userActions.contains(functionalRole)){
				isActionEligible = true;
			}
			else if(userActions!=null && !userActions.isEmpty() && multipleRoleList!=null){
				if(!multipleRoleList.isEmpty()){
					for(int i =0;i < multipleRoleList.size();i++){
						if(userActions.contains("F"+(String)multipleRoleList.get(i))){
							isActionEligible = true;
							break;
						}
					}
				}
			}

			//If the action eligibility check is failed then Critical error message is added to the context...
			if(!isActionEligible){
				EBWLogger.logDebug(this, "Action Eligibility Entitlements check is failure");
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.ACCESS_ERROR);
			}
			else {
				EBWLogger.logDebug(this, "Action Eligibility Entitlements check is successfull");
			}
		} 
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * APPROVE CASE ONLY...
	 *  Further, the user who initiated or is the current owner of the transaction will not be allowed to approve the transaction.
           This check will be carried out by comparing the RACFID of the current user with the id of the initiator and current owner present in transaction details.
	 *  The approver role on the transaction should match any of the approver roles of the logged in user.
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public void checkOtherEntitlements_Approve(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		boolean isTxnOwnerCheckValid = false;
		boolean isBussEntitlementsValid = false;
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//Needs to checked for the approval case only...
			String txn_initiatorId = objPaymentDetails.getInitiator_id();
			String txn_current_owner_id = objPaymentDetails.getCurrent_owner_id();
			String txn_approvalRole = objPaymentDetails.getCurrent_approver_role();
			ArrayList approval_roles = new ArrayList();
			String approval_role[] = objMSUserDetails.getApproveRole();
			for(int i=0;i< approval_role.length ;i++){
				approval_roles.add(approval_role[i]);
			}

			//Current owner id and Initiator id should not be same as RACFID.
			if(txn_initiatorId!=null && !txn_initiatorId.equalsIgnoreCase(objMSUserDetails.getRcafId()) && 
					txn_current_owner_id!=null && !txn_current_owner_id.equalsIgnoreCase(objMSUserDetails.getRcafId())){
				isTxnOwnerCheckValid = true;
			}

			//Approver role tagged to the transaction should be present in the EPR role..
			if(txn_approvalRole!=null && approval_roles.contains(txn_approvalRole)){
				isBussEntitlementsValid = true;
			}

			//If the action eligibility check is failed then Critical error message is added to the context...
			if(!isTxnOwnerCheckValid){
				EBWLogger.logDebug(this, "Business Entitlements check for Approver Owner is failure ");
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.APPROVER_OWNER_FAILURE);
			}
			else if(!isBussEntitlementsValid){
				EBWLogger.logDebug(this, "Business Entitlements check for Approver Role is failure ");
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.APPROVER_ROLE_FAILUE);
			}
			else {
				EBWLogger.logDebug(this, "Business Entitlements check for Approve is successfull");
			}
		} 
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * REJECT CASE ONLY...
	 *  Further, the user who initiated or is the current owner of the transaction will not be allowed to approve the transaction.
           This check will be carried out by comparing the RACFID of the current user with the id of the initiator and current owner present in transaction details.
	 *  The approver role on the transaction should match any of the approver roles of the logged in user.
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public void checkOtherEntitlements_Reject(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		boolean isBussEntitlementsValid = false;
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//Needs to checked for the Reject case only...
			String txn_approvalRole = objPaymentDetails.getCurrent_approver_role();
			ArrayList approval_roles = new ArrayList();
			String approval_role[] = objMSUserDetails.getApproveRole();
			for(int i=0;i< approval_role.length ;i++){
				approval_roles.add(approval_role[i]);
			}

			//Approver role tagged to the transaction should be present in the EPR role..
			if(txn_approvalRole!=null && approval_roles.contains(txn_approvalRole)){
				isBussEntitlementsValid = true;
			}

			//If the action eligibility check is failed then Critical error message is added to the context...
			if(!isBussEntitlementsValid){
				EBWLogger.logDebug(this, "Business Entitlements check for Approver reject role is failure");
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.APPROVER_ROLE_FAILUE);
			}
			else {
				EBWLogger.logDebug(this, "Business Entitlements check for Reject is successfull");
			}
		} 
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}

	/**
	 * PRINT CASE only..
	 * Needs to validate that the User is entitled to the printing branch...
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public void checkOtherEntitlements_Print(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		boolean isBussEntitlementsValid = false;
		try 
		{
			//Payment Details..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//MS User details..
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//Check request mappings details..
			ConfirmPrintSuccessOutputTO objConfirmPrint = new ConfirmPrintSuccessOutputTO();
			if(txnDetails.containsKey("ConfirmPrintOutputTO")){
				objConfirmPrint = (ConfirmPrintSuccessOutputTO)txnDetails.get("ConfirmPrintOutputTO");
			}

			//Needs to checked for the Print Check case only...
			String transferType = objPaymentDetails.getTransfer_Type();
			String printing_branch = objConfirmPrint.getPrintingBranch();

			ArrayList eligibleAccounts = objMSUserDetails.getAccessibleAccounts();
			for(int i=0;i<eligibleAccounts.size();i++){
				AccessibleAccount objAccessibleAcc = (AccessibleAccount)eligibleAccounts.get(i);
				if(transferType!=null && (transferType.startsWith(ChkReqConstants.CHK))){
					if(objAccessibleAcc.isSuperUser()){
						isBussEntitlementsValid = true;
						break;
					}
					else {
						if(objAccessibleAcc.getBranch() == ConvertionUtil.convertToint(printing_branch)){
							isBussEntitlementsValid = true;
							break;
						}
					}
				}
			}

			//If the action eligibility check is failed then Critical error message is added to the context...
			if(!isBussEntitlementsValid){
				EBWLogger.logDebug(this, "Business Entitlements check for Print is failure");
				serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.PRINT_ACCESS_ERROR);
			}
			else {
				EBWLogger.logDebug(this, "Business Entitlements check for Print is successfull");
			}
		} 
		catch(Exception exception){
			throw exception;
		}
		finally{

		}
	}
}
