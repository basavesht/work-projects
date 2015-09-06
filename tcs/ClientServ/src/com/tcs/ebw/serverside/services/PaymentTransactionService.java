package com.tcs.ebw.serverside.services;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.AuditTrialAction;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.Message;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountLink;
import com.tcs.ebw.serverside.services.channelPaymentServices.BusinessRulesService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CancelPaymentService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CancelRecurringPaymentService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CreatePaymentLogService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CreatePaymentService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CreateRecurringPayService;
import com.tcs.ebw.serverside.services.channelPaymentServices.GetExternalAccounts;
import com.tcs.ebw.serverside.services.channelPaymentServices.GetQZBusinessDate;
import com.tcs.ebw.serverside.services.channelPaymentServices.MapPaymentInputDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.PayConfirmationNum;
import com.tcs.ebw.serverside.services.channelPaymentServices.PortfolioLoansService;
import com.tcs.ebw.serverside.services.channelPaymentServices.ResetApprovalDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.SkipNextTransfer;
import com.tcs.ebw.serverside.services.channelPaymentServices.UpdatePaymentConfNoService;
import com.tcs.ebw.serverside.services.channelPaymentServices.UpdateTransitionDate;
import com.tcs.ebw.serverside.services.channelPaymentServices.UpdateTxnParentService;
import com.tcs.ebw.serverside.services.channelPaymentServices.UserEntilements;
import com.tcs.ebw.serverside.services.channelPaymentServices.ValidateCutOffTime;
import com.tcs.ebw.serverside.services.channelPaymentServices.VerifyVersionMismatch;
import com.tcs.mswitch.common.channel.DBProcedureChannel;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 * **********************************************************
 */
public class PaymentTransactionService extends DatabaseService implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3629028013977872508L;
	private transient Connection conn = null;

	public PaymentTransactionService(){

	}

	/**
	 * Method for calling the Business Rule service onLoad of the Create Screen . 
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @param transferType
	 * @return
	 */
	public ArrayList onLoadAccDetails(String stmntId, Object toObjects[], Boolean boolean1,String transferType)
	{
		conn = (Connection)serviceConnection;
		EBWLogger.trace(this, "Starting onLoadAccDetails method in Implementation class");
		ArrayList<Object> filteredAccountList = new ArrayList<Object>();
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		ArrayList<Object> onLoadDetails =new ArrayList<Object>();
		HashMap txnDetails = new HashMap();
		try
		{
			//Mapping the Payment details (Internal/External Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setPaymentOnLoadDetails(toObjects,objserviceContext);

			//Invoke PLA Web service to get the The Variable Rate Revolving Credit Loans for all the MSSB Internal Accounts...
			if(transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
				EBWLogger.logDebug(this, "Getting the PLA Loan Accounts ...");
				PaymentsUtility.fetchPLACreditLoans(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					onLoadDetails.add(objserviceContext);
					return onLoadDetails; 
				}
			}

			//Call Business Rule Service for BLOCKED DISPLAY CASE
			EBWLogger.logDebug(this, "Calling BusinessRule Service for Blocked Display case...");
			PaymentsUtility.executeBlockDisplay(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				onLoadDetails.add(objserviceContext);
				return onLoadDetails; 
			}

			//Call the External Account Service to get the external accounts 
			if(transferType.equalsIgnoreCase(TxnTypeCode.ACH_TYPE)){
				EBWLogger.logDebug(this, "Getting the External ACCOUNTS list");
				GetExternalAccounts objgetExternalAccounts = new GetExternalAccounts();
				objgetExternalAccounts.setConnection(conn);
				objgetExternalAccounts.getExternalAccounts(txnDetails,objserviceContext);
			}
			objserviceContext.setServiceCallSuccessful(true);
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(sqlexception));
		}
		catch(Exception exception){
			exception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(exception));
		}
		finally {

		}
		filteredAccountList.add(objserviceContext);  // Service Context 
		filteredAccountList.add(txnDetails); 
		return filteredAccountList;
	}

	/**
	 * Methods to get all the external accounts (Same name + Non-same Name) when accounts are not available in session..
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @param transferType
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayList onLoadExtAccounts(String stmntId, Object toObjects[], Boolean boolean1) throws SQLException,Exception
	{
		conn = (Connection)serviceConnection;
		EBWLogger.trace(this, "Starting onLoadAccDetails method in Implementation class");
		ArrayList<Object> filteredAccountList = new ArrayList<Object>();
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		HashMap txnDetails = new HashMap();
		try
		{
			//Mapping the Payment details (Internal/External Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setPaymentOnLoadDetails(toObjects,objserviceContext);

			//Call the External Account Service to get the external accounts 
			EBWLogger.logDebug(this, "Getting the External Accounts list (Same name + Non-same Name)");
			GetExternalAccounts objgetExternalAccounts = new GetExternalAccounts();
			objgetExternalAccounts.setConnection(conn);
			objgetExternalAccounts.getExternalAccounts(txnDetails,objserviceContext);

			EBWLogger.trace(this, "Finished with onLoadAccDetails...");
			objserviceContext.setServiceCallSuccessful(true);
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(sqlexception));
		}
		catch(Exception exception){
			exception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(exception));
		}
		finally {

		}
		filteredAccountList.add(objserviceContext);
		filteredAccountList.add(txnDetails); 
		return filteredAccountList;
	}

	/**
	 * Method for calling the RTAB and AccountView service on selecting the Internal Bank Account  
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 */
	public ArrayList onSelectedAccDetails(String stmntId, Object toObjects[], Boolean boolean1)
	{
		conn = (Connection)serviceConnection;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		EBWLogger.trace(this, "Starting onSelectedAccDetails method in Implementation class");
		ArrayList<Serializable> selectedAccountDetails=new ArrayList<Serializable>();
		HashMap txnDetails = new HashMap();
		try
		{
			//Mapping the Payment details (Internal/External Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setOnAccSelectDetails(toObjects,objserviceContext);

			EBWLogger.logDebug(this, "Executing the RTAB Service..");
			PaymentsUtility.getAccountBalance(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				selectedAccountDetails.add(objserviceContext);
				return selectedAccountDetails; 
			}

			EBWLogger.logDebug(this, "Executed the RTAB Service..");
			EBWLogger.trace(this, "Finished with onSelectedAccDetails...");
			objserviceContext.setServiceCallSuccessful(true);
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(sqlexception));
		}
		catch(Exception exception){
			exception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(exception));
		}
		finally {

		}
		selectedAccountDetails.add(objserviceContext);
		selectedAccountDetails.add(txnDetails);
		return selectedAccountDetails;
	}

	/**
	 * Method for getting the Account plating details on selecting the Portfolio Loan Account...
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 */
	public ArrayList getLoanAccPlatingDetails(String stmntId, Object toObjects[], Boolean boolean1) throws SQLException,Exception
	{
		conn = (Connection)serviceConnection;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		EBWLogger.trace(this, "Starting getLoanAccPlatingDetails method in Implementation class");
		ArrayList<Serializable> accountPlatingDetails=new ArrayList<Serializable>();
		HashMap txnDetails = new HashMap();
		try
		{
			//Mapping the Payment details (Internal/External Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setOnAccSelectDetails(toObjects,objserviceContext);

			EBWLogger.logDebug(this, "Executing the Portfolio Loan Account Plating details....");
			PortfolioLoansService objLoanAcnt = new PortfolioLoansService();
			objLoanAcnt.getLoanAcntPlatingInfo(txnDetails,objserviceContext);
			EBWLogger.logDebug(this, "Executed the Portfolio Loan Account Plating details....");

			EBWLogger.trace(this, "Finished with getLoanAccPlatingDetails method in Implementation class");
			objserviceContext.setServiceCallSuccessful(true);
			((Connection)serviceConnection).commit();
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(sqlexception));
		}
		catch(Exception exception){
			exception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(exception));
		}
		finally 
		{
			try 
			{
				//RollBack is called in case of Error code return other than 1 from SI for external interfaces or any technical errors encountered..
				if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
				{
					try {
						((Connection)serviceConnection).rollback();
						if(objserviceContext.isRTARollbackReq())
						{
							EBWLogger.logDebug(this, "Calling RTA Rollback");
							DBProcedureChannel.rollback();
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				((Connection)serviceConnection).close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		accountPlatingDetails.add(objserviceContext);
		accountPlatingDetails.add(txnDetails);
		return accountPlatingDetails;
	}

	/**
	 * Function called on submitting the transaction.. 
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @param transferType
	 * @param businessDate
	 * @param paymentDate
	 * @param isTxnModified
	 * @param branch_Id
	 * @return
	 */
	public ArrayList<Object> createSubmitTransfer(String stmntId, Object toObjects[], Boolean boolean1)
	{
		conn = (Connection)serviceConnection;
		EBWLogger.trace(this, "Starting createConfirmTransfer method in Implementation class");
		ArrayList<Object> createConfirmDetails = new ArrayList<Object>();
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		HashMap txnDetails = new HashMap();
		String transferType = "";
		Date paymentDate = new Date();
		Date businessDate = new Date();
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		try
		{
			//Mapping the Payment details (Internal/External Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setCreatePaySubmitDetails(toObjects,objserviceContext);

			//Get the business Date from the QZ_Dates View 
			EBWLogger.logDebug(this, "Getting the Business Date");
			GetQZBusinessDate getBusinessDt = new GetQZBusinessDate();
			getBusinessDt.setConnection(conn);
			getBusinessDt.getQzBusinessDate(txnDetails,objserviceContext);
			EBWLogger.logDebug(this, "Finished getting Business Date : "+businessDate);

			//Mapping the payment attributes...
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			transferType= objPaymentDetails.getTransfer_Type();
			paymentDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
			businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());

			//Checking the version in case of failure only during Edit case...
			if(objPaymentDetails.isTxnModified()){
				VerifyVersionMismatch objVerMismatch = new VerifyVersionMismatch();
				String versionType = "TXNVER";
				objVerMismatch.setConnection(serviceConnection);
				objVerMismatch.compareVersionNum(txnDetails,versionType,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
			}

			//User Entitlements Check for Verifying the internal and external accounts getting passed to the service are valid or not...
			UserEntilements userEntilements = new UserEntilements();
			userEntilements.setConnection(conn);
			boolean isAccSuspended=false;
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
				userEntilements.checkIntAccEntitlement(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
				userEntilements.checkExtTxnRules(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
				userEntilements.checkExtAccOwner(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
				userEntilements.checkExtAccRoutingNum(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
				userEntilements.checkExtAccEntitlements(txnDetails,isAccSuspended,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
			}

			//Check the Cut Off Time for the same date transactions..
			if(paymentDate.compareTo(businessDate) == 0 && !MSCommonUtils.check24x7Access(transferType)) {
				EBWLogger.logDebug(this, "Checking the Cut Off Time for the same day transactions..");
				ValidateCutOffTime objValidateCutOffTime = new ValidateCutOffTime();
				objValidateCutOffTime.setConnection(conn);
				objValidateCutOffTime.checkCutOffTime_Info(txnDetails,objserviceContext);
				paymentDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
			}

			//Getting the Loan Account Details...
			EBWLogger.logDebug(this, "Validating Loan Acnt Details...");
			if(transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				PortfolioLoansService objGetLoanAcntInfo = new PortfolioLoansService();
				objGetLoanAcntInfo.getLoanAcntDetails(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Finished validating Loan Acnt Details...");
			}

			//Calling the Merlin Service for FROM and TO accounts if applicable ... 
			EBWLogger.logDebug(this, "Executing the Merlin Service..");
			PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				createConfirmDetails.add(objserviceContext);
				return createConfirmDetails; 
			}
			EBWLogger.logDebug(this, "Executed the Merlin Service..");

			// Calling the RTAB Service for Debit account ..
			EBWLogger.logDebug(this, "Executing the RTAB Service..");
			if(transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))
			{
				PaymentsUtility.getAccountBalance(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
				EBWLogger.logDebug(this, "Executed the RTAB Service..");
			}

			//External Account Link extraction to pass it to business rule..
			if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
				EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
				AccountLink objAccountLink= new AccountLink();
				objAccountLink.setConnection(serviceConnection);
				objAccountLink.getExtAccountLink(txnDetails,objserviceContext);
				if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
				EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
			}

			//Call Business Rule Service 
			EBWLogger.logDebug(this, "Executing BusinessRule Service ON SUBMIT CLICK");
			PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				EBWLogger.logDebug(this, "BusinessRule encountered Hard Error or Technical Failure");
				createConfirmDetails.add(objserviceContext);
				return createConfirmDetails; 
			}
			EBWLogger.logDebug(this, "Executed createSubmitTransfer ....");
			EBWLogger.trace(this, "Finished with createSubmitTransfer...");
			objserviceContext.setServiceCallSuccessful(true);
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(sqlexception));
		}
		catch(Exception exception){
			exception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(exception));
		}
		finally {

		}
		createConfirmDetails.add(objserviceContext);
		createConfirmDetails.add(txnDetails);
		return createConfirmDetails;
	}

	/**
	 * The following method creates a new transaction ..
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 */
	public ArrayList<Object> setPaymentsTransation(String stmntId,Object toObjects[], Boolean boolean1)
	{
		conn = (Connection)serviceConnection;
		EBWLogger.trace(this, "Starting setPaymentsTransation method in Implementation class");
		boolean1 = Boolean.TRUE;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		int paymentStatusFlag=0;
		int childPayStatusFlag = 0;
		ArrayList<Object> setPaymentOutputDetails = new ArrayList<Object>();
		String notificationEventId = "";
		boolean isExternalAccount = false;
		HashMap txnDetails = new HashMap();
		String transferType = "";
		String frequencyFlag ="";
		int isRecurringFlag = 0;
		Date paymentDate = new Date();
		Date businessDate = new Date();
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		String loggingAction="";
		try
		{
			//Mapping the Payment details (Internal/External Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setCreatePayConfirmDetails(toObjects,objserviceContext);

			//Get the business Date from the QZ_Dates View 
			EBWLogger.logDebug(this, "Getting the Business Date");
			GetQZBusinessDate getBusinessDt = new GetQZBusinessDate();
			getBusinessDt.setConnection(conn);
			getBusinessDt.getQzBusinessDate(txnDetails,objserviceContext);
			EBWLogger.logDebug(this, "Finished getting Business Date : "+businessDate);

			//Mapping the payment attributes...
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			transferType= objPaymentDetails.getTransfer_Type();
			paymentDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
			businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());
			frequencyFlag = objPaymentDetails.getFrequency_Type();
			isRecurringFlag= ConvertionUtil.convertToint(frequencyFlag);

			//User Entitlements Check for Verifying the internal and external accounts getting passed to the service are valid or not...
			UserEntilements userEntilements = new UserEntilements();
			userEntilements.setConnection(conn);
			boolean isAccSuspended=false;
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
				userEntilements.checkIntAccEntitlement(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
				userEntilements.checkExtTxnRules(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				userEntilements.checkExtAccOwner(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				userEntilements.checkExtAccRoutingNum(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				userEntilements.checkExtAccEntitlements(txnDetails,isAccSuspended,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}

			//Check the Cut Off Time for the same day transactions and return the PaymentDate if any...
			if(paymentDate.compareTo(businessDate) == 0 && !MSCommonUtils.check24x7Access(transferType)) {
				EBWLogger.logDebug(this, "Checking the Cut Off Time for the same day transactions..");
				ValidateCutOffTime objValidateCutOffTime = new ValidateCutOffTime();
				objValidateCutOffTime.setConnection(conn);
				objValidateCutOffTime.checkCutOffTime_Info(txnDetails,objserviceContext);
				paymentDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}

			//Getting the Loan Account Details...
			EBWLogger.logDebug(this, "Validating Loan Acnt Details...");
			if(transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				PortfolioLoansService objGetLoanAcntInfo = new PortfolioLoansService();
				objGetLoanAcntInfo.getLoanAcntDetails(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Finished validating Loan Acnt Details...");
			}

			//Calling AccountView Service ..
			EBWLogger.logDebug(this, "Executing the Merlin Service..");
			PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}
			EBWLogger.logDebug(this, "Executed the Merlin Service..");

			//Call RTAB Service
			EBWLogger.logDebug(this, "Executing the RTAB Service..");
			if(transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)) 
			{  
				//Calling the RTAB Service..
				PaymentsUtility.getAccountBalance(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				EBWLogger.logDebug(this, "Executed the RTAB Service..");
			}

			//Getting the confirmation number for the FTM Call service invoke from the sequence only for ACH Transfers .... 
			PayConfirmationNum paymentConfirmationId = new PayConfirmationNum();
			paymentConfirmationId.setConnection(conn);
			paymentConfirmationId.getPaymentNumberFrmSeq(txnDetails);

			//External Account Link extraction to pass it to business rule..
			if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
				EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
				AccountLink objAccountLink= new AccountLink();
				objAccountLink.setConnection(serviceConnection);
				objAccountLink.getExtAccountLink(txnDetails,objserviceContext);
				if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
			}

			// If the Transaction is One-Time Immediate Transfer 
			if(isRecurringFlag==1)
			{
				if(paymentDate.compareTo(businessDate)==0)
				{
					EBWLogger.logDebug(this, "The Processing transaction is ONETIME SAME DATED Transaction");

					//Call Business Rule Service 
					EBWLogger.logDebug(this, "Calling BusinessRule Service");
					PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.ERROR || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						EBWLogger.logDebug(this, "BusinessRule Service encountered Technical Failure or Hard Error");
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					if((objserviceContext.getMaxSeverity()== MessageType.WARNING || objserviceContext.getMaxSeverity()== MessageType.RISK))
					{
						EBWLogger.logDebug(this, "BusinessRule Service encountered Warning");

						//Status code mapping based on Max severity..
						if(objserviceContext.getMaxSeverity()== MessageType.WARNING){
							paymentStatusFlag = 2;
						}
						else if(objserviceContext.getMaxSeverity()== MessageType.RISK){
							paymentStatusFlag = 80;
						}
						EBWLogger.logDebug(this, "Creating Record in channels in Awaiting Approval state");
						CreatePaymentService objCreatePaymentService = new CreatePaymentService();
						objCreatePaymentService.setConnection(conn);
						objCreatePaymentService.setCreatePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction created in channels successfully");

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//RTA Hold Service 
						EBWLogger.logDebug(this, "Calling the RTA Service");
						PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "RTA Executed successfully ");

						//BR Validation Log Service 
						EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
						PaymentsUtility.executeBRLogging(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "BR Warnings Logged successfully");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P19";
						else
							notificationEventId = "NOTIFY_P7";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");	

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB (Created Status)");
						loggingAction=AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB successfully");
					}
					else
					{
						// Call the CreatePaymentService With Status as "Executed"
						EBWLogger.logDebug(this, "Creating Transaction in channels in Executed Status ");

						paymentStatusFlag=4;
						CreatePaymentService objCreatePaymentService = new CreatePaymentService();
						objCreatePaymentService.setConnection(conn);
						objCreatePaymentService.setCreatePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Created Transaction in channels in Executed Status ");

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
							{
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Call the Payment HUB Service
						EBWLogger.logDebug(this, "Calling Payments HUB Service ");
						PaymentsUtility.executePaymentsHub(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Payments created in HUB ");

						//UpdatePaymentConfirmationNo Service	
						EBWLogger.logDebug(this, "Updating the Payments Confirmation Number in Channels DB ");
						UpdatePaymentConfNoService objUpdatePaymentConfNoService = new UpdatePaymentConfNoService();
						objUpdatePaymentConfNoService.setConnection(conn);
						objUpdatePaymentConfNoService.setUpdatePaymentConfNoService(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Payment Return Confirmation Number updated successfully in Channels DB ");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P19";
						else
							notificationEventId = "NOTIFY_P7";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");		

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB (Created Status)");
						loggingAction=AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB successfully");				

						//Update Transition date : Executed State 
						EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
						UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
						objUpdtTransitionDt.setConnection(conn);
						objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Updated the transition date successfully");
					}
					EBWLogger.trace(this, "Executed setPaymentsTransation for One Time Immediate Transfer");
					EBWLogger.trace(this, "Finished with setPaymentsTransation for One Time Immediate Transfer");
				}
				else if(paymentDate.compareTo(businessDate)>0)
				{
					EBWLogger.logDebug(this, "The Processing transaction is ONETIME FUTURE DATED Transaction");

					//Call Business Rule Service 
					EBWLogger.logDebug(this, "Calling BusinessRule Service");
					PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.ERROR || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						EBWLogger.logDebug(this, "BusinessRule Service encountered Technical Failure or Hard Error");
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					if((objserviceContext.getMaxSeverity()== MessageType.WARNING || objserviceContext.getMaxSeverity()== MessageType.RISK))
					{
						EBWLogger.logDebug(this, "Creating Transaction in channels in future dated awaiting approval Status ");

						//Mapping the status code based on Max Severity...
						if(objserviceContext.getMaxSeverity()== MessageType.WARNING){
							paymentStatusFlag = 2;
						}
						else if(objserviceContext.getMaxSeverity()== MessageType.RISK){
							paymentStatusFlag = 80;
						}
						CreatePaymentService objCreatePaymentService = new CreatePaymentService();
						objCreatePaymentService.setConnection(conn);
						objCreatePaymentService.setCreatePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Created Transaction in channels in Scheduled Status ");

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//BR Validation Log Service 
						EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
						PaymentsUtility.executeBRLogging(txnDetails, objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "BR Warnings Logged successfully");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P19";
						else
							notificationEventId = "NOTIFY_P7";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Call the Logging service...
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB (Created Status)");
						loggingAction=AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB (Created Status)");
					}
					else
					{
						//Call the CreatePaymentService With Status as "Scheduled"
						EBWLogger.logDebug(this, "Creating Transaction in channels in Scheduled Status ");
						paymentStatusFlag=6;
						CreatePaymentService objCreatePaymentService = new CreatePaymentService();
						objCreatePaymentService.setConnection(conn);
						objCreatePaymentService.setCreatePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Created Transaction in channels in Scheduled Status ");

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P19";
						else
							notificationEventId = "NOTIFY_P7";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");	

						//Call the Logging service...
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB (Created Status)");
						loggingAction=AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB (Created Status)");
					}
					EBWLogger.logDebug(this, "Executed setPaymentsTransation for Future Dated Transfer");
					EBWLogger.trace(this, "Finished with setPaymentsTransation for Future Dated Transfer");
				}
				else {
					objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}
			else if(isRecurringFlag==2)
			{
				if(paymentDate.compareTo(businessDate)==0)
				{
					EBWLogger.logDebug(this, "The Processing transaction is RECURRING SAME DATED Transaction");

					//Call Business Rule Service 
					EBWLogger.logDebug(this, "Calling BusinessRule Service");
					PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.ERROR || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						EBWLogger.logDebug(this, "BusinessRule Service encountered Technical Failure");
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					if((objserviceContext.getMaxSeverity()== MessageType.WARNING || objserviceContext.getMaxSeverity()== MessageType.RISK))
					{
						EBWLogger.logDebug(this, "Creating Record in channels in Awaiting Approval state");

						//Status code mapping based on Max severity..
						if(objserviceContext.getMaxSeverity()== MessageType.WARNING){
							paymentStatusFlag = 2;
						}
						else if(objserviceContext.getMaxSeverity()== MessageType.RISK){
							paymentStatusFlag = 80;
						}
						CreateRecurringPayService objCreateRecurringPayService = new CreateRecurringPayService();
						objCreateRecurringPayService.setConnection(conn);
						objCreateRecurringPayService.setCreateRecurringPayDetails(paymentStatusFlag,txnDetails,objserviceContext);

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
							{
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Call the RTAB Create Hold Service
						EBWLogger.logDebug(this, "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "RTA Service encountered Technical Failure");
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "RTA Executed successfully");

						//Call the BR Validation Log Service 
						EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
						PaymentsUtility.executeBRLogging(txnDetails, objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "BR Validation Service encountered Technical Failure");
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "BR Exceptions Logged successfully");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P20";
						else
							notificationEventId = "NOTIFY_P8";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB (Created Status)");
						loggingAction=AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB (Created Status)");
					}
					else
					{
						//Call the CreateRecurringPayService with the Parent Transaction Status as Active and Child Transaction Status as "Executed"
						EBWLogger.logDebug(this, "Creating Transaction in channels in Executed Status ");

						paymentStatusFlag=4;
						CreateRecurringPayService objCreateRecurringPayService = new CreateRecurringPayService();
						objCreateRecurringPayService.setConnection(conn);
						objCreateRecurringPayService.setCreateRecurringPayDetails(paymentStatusFlag,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction created in channels successfully");

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Call the Payment HUB Service
						EBWLogger.logDebug(this, "Calling Payments HUB Service ");
						PaymentsUtility.executePaymentsHub(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "Payments HUB Service encountered Technical Failure");
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Payments created in HUB ");

						//Call UpdatePaymentConfirmationNo Service	
						EBWLogger.logDebug(this, "Updating the Payments Confirmation Number in Channels DB ");
						UpdatePaymentConfNoService objUpdatePaymentConfNoService = new UpdatePaymentConfNoService();
						objUpdatePaymentConfNoService.setConnection(conn);
						objUpdatePaymentConfNoService.setUpdatePaymentConfNoService(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Payment Return Confirmation Number updated successfully in Channels DB ");

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB (Created Status)");
						loggingAction=AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB (Created Status)");

						//Call the UpdateTxnParent Service to update the Parent Transaction and create next Child Transaction
						EBWLogger.logDebug(this, "Creating the next child transaction");
						UpdateTxnParentService objUpdateTxnParentService = new UpdateTxnParentService();
						objUpdateTxnParentService.setConnection(conn);
						objUpdateTxnParentService.setUpdateTxnParentDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Next child created successfully");

						//Call the CreatePaymentDetailsLog Service for child transaction logging.
						if(objPaymentDetails.isChildTxnCreated()){
							EBWLogger.logDebug(this, "Child Transaction Logging in the Channels DB (Created Status)");
							childPayStatusFlag=6;
							CreatePaymentLogService objChildTxnLogging = new CreatePaymentLogService();
							objChildTxnLogging.setConnection(conn);
							objChildTxnLogging.setNextChildPaymentsLog(childPayStatusFlag,txnDetails,objserviceContext);
							EBWLogger.logDebug(this, "Child Transaction Logged in the Channels DB (Created Status)");
						}

						//Call the limit check service through Business rule service for all the child ...
						if(objPaymentDetails.isChildTxnCreated() && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
							EBWLogger.logDebug(this, "Calling limit check Service for all child future dated transactions");
							PaymentsUtility.processLimitRequest(txnDetails,objserviceContext);
							if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								EBWLogger.logDebug(this, "limit check Service failed for child future dated transactions");
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Limit check service for future dated transactions");
						}

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P20";
						else
							notificationEventId = "NOTIFY_P8";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Update Transition date : Executed State 
						EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
						UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
						objUpdtTransitionDt.setConnection(conn);
						objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Updated the transition date successfully");
					}
					EBWLogger.logDebug(this, "Executed setPaymentsTransation for One Time Immediate Transfer");
					EBWLogger.trace(this, "Finished with setPaymentsTransation for One Time Immediate Transfer");
				}
				else if(paymentDate.compareTo(businessDate)>0)
				{
					EBWLogger.logDebug(this, "The Processing transaction is RECURRING FUTURE DATED Transaction");

					//Call Business Rule Service 
					EBWLogger.logDebug(this, "Calling BusinessRule Service");
					PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.ERROR || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						EBWLogger.logDebug(this, "BusinessRule Service encountered Technical Failure or Hard Error");
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					if((objserviceContext.getMaxSeverity()== MessageType.WARNING || objserviceContext.getMaxSeverity()== MessageType.RISK))
					{
						//Future dated transactions going for awaiting approvals..
						EBWLogger.logDebug(this, "Creating Recurring Transaction in channels in future dated awaiting approval Status ");

						//Status code mapping based on Max severity..
						if(objserviceContext.getMaxSeverity()== MessageType.WARNING){
							paymentStatusFlag = 2;
						}
						else if(objserviceContext.getMaxSeverity()== MessageType.RISK){
							paymentStatusFlag = 80;
						}
						CreateRecurringPayService objCreateRecurringPayService = new CreateRecurringPayService();
						objCreateRecurringPayService.setConnection(conn);
						objCreateRecurringPayService.setCreateRecurringPayDetails(paymentStatusFlag,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Created Transaction in channels in Scheduled Status ");

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Call the BR Validation Log Service 
						EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
						PaymentsUtility.executeBRLogging(txnDetails, objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "BR Validation Service encountered Technical Failure");
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "BR Exceptions Logged successfully");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P20";
						else
							notificationEventId = "NOTIFY_P8";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB (Created Status)");
						loggingAction=AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB (Created Status)");
					}
					else
					{
						//Call the CreateRecurringPayService with the Parent Transaction Status as Active and Child Transaction Status as "Scheduled"
						EBWLogger.logDebug(this, "Creating Transaction in channels in Scheduled Status ");
						paymentStatusFlag=6;
						CreateRecurringPayService objCreateRecurringPayService = new CreateRecurringPayService();
						objCreateRecurringPayService.setConnection(conn);
						objCreateRecurringPayService.setCreateRecurringPayDetails(paymentStatusFlag,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Created Transaction in channels in Scheduled Status ");

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P20";
						else
							notificationEventId = "NOTIFY_P8";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB (Created Status)");
						loggingAction=AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB (Created Status)");
					}
					EBWLogger.logDebug(this, "Executed setPaymentsTransation for Future Dated Transfer");
					EBWLogger.trace(this, "Finished with setPaymentsTransation for Future Dated Transfer");
				}
				else {
					objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}
			else {
				objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}
			objserviceContext.setServiceCallSuccessful(true);
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(sqlexception));
		}
		catch(Exception exception){
			exception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(exception));
		}
		finally {

		}
		setPaymentOutputDetails.add(objserviceContext);
		setPaymentOutputDetails.add(txnDetails); // Adding all the Transfer Object in the Output Object ....
		return setPaymentOutputDetails;
	}

	/**
	 * Function for modifying the Payments 
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 */
	public ArrayList<Object> updatePaymentsTransation(String stmntId,Object toObjects[], Boolean boolean1)
	{
		EBWLogger.trace(this, "Starting updatePaymentsTransation method in Implementation class");
		boolean1 = Boolean.TRUE;
		conn = (Connection)serviceConnection;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		int paymentStatusFlag=0;
		int childPayStatusFlag = 0;
		BusinessRulesService objBusinessRule =new BusinessRulesService();
		ArrayList<Object> setPaymentOutputDetails = new ArrayList<Object>();
		String notificationEventId = "";
		boolean isExternalAccount = false;
		HashMap txnDetails = new HashMap();
		String transferType = "";
		String frequencyFlag ="";
		int isRecurringFlag=ConvertionUtil.convertToint(frequencyFlag);
		Date paymentDate = new Date();
		Date businessDate = new Date();
		String loggingAction = "";
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		try
		{
			//Mapping the Payment details (Internal/External Modify Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setPaymentConfirmDetails(toObjects,objserviceContext);

			//Get the business Date from the QZ_Dates View 
			EBWLogger.logDebug(this, "Getting the Business Date");
			GetQZBusinessDate getBusinessDt = new GetQZBusinessDate();
			getBusinessDt.setConnection(conn);
			getBusinessDt.getQzBusinessDate(txnDetails,objserviceContext);
			EBWLogger.logDebug(this, "Finished getting Business Date : "+businessDate);

			//Mapping the payment attributes...
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			transferType= objPaymentDetails.getTransfer_Type();
			paymentDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
			businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());
			frequencyFlag = objPaymentDetails.getFrequency_Type();
			isRecurringFlag= ConvertionUtil.convertToint(frequencyFlag);

			//User Entitlements Check for Verifying the internal and external accounts getting passed to the service are valid or not...
			UserEntilements userEntilements = new UserEntilements();
			userEntilements.setConnection(conn);
			boolean isAccSuspended=false;
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
				userEntilements.checkIntAccEntitlement(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
				userEntilements.checkExtTxnRules(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				userEntilements.checkExtAccOwner(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				userEntilements.checkExtAccRoutingNum(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				userEntilements.checkExtAccEntitlements(txnDetails,isAccSuspended,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}

			//Check the Cut Off Time for the same date transactions..
			if(paymentDate.compareTo(businessDate) == 0 && !MSCommonUtils.check24x7Access(transferType)) {
				EBWLogger.logDebug(this, "Checking the Cut Off Time for the same day transactions..");
				ValidateCutOffTime objValidateCutOffTime = new ValidateCutOffTime();
				objValidateCutOffTime.setConnection(conn);
				objValidateCutOffTime.checkCutOffTime_Info(txnDetails,objserviceContext);
				paymentDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}

			//Getting the Loan Account Details...
			EBWLogger.logDebug(this, "Validating Loan Acnt Details...");
			if(transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				PortfolioLoansService objGetLoanAcntInfo = new PortfolioLoansService();
				objGetLoanAcntInfo.getLoanAcntDetails(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Finished validating Loan Acnt Details...");
			}

			//Calling the Merlin Service for FROM and TO accounts if applicable ... 
			EBWLogger.logDebug(this, "Executing the Merlin Service..");
			PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}
			EBWLogger.logDebug(this, "Executed the Merlin Service..");

			//Call RTAB Service
			EBWLogger.logDebug(this, "Executing the RTAB Service..");
			if(transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))
			{  
				// Calling the RTAB Service..
				PaymentsUtility.getAccountBalance(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				EBWLogger.logDebug(this, "Executed the RTAB Service..");
			}

			//External Account Link extraction to pass it to business rule..
			if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
				EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
				AccountLink objAccountLink= new AccountLink();
				objAccountLink.setConnection(serviceConnection);
				objAccountLink.getExtAccountLink(txnDetails,objserviceContext);
				if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
			}

			// If the Transaction is One-Time Immediate Transfer 
			if(isRecurringFlag==1)
			{
				if(paymentDate.compareTo(businessDate)==0)
				{
					EBWLogger.logDebug(this, "The Processing transaction is ONETIME SAME DATED Transaction");

					//Call Business Rule Service 
					EBWLogger.logDebug(this, "Calling BusinessRule Service");
					PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.ERROR || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						EBWLogger.logDebug(this, "BusinessRule Service encountered Hard Error");
						setPaymentOutputDetails.add(objBusinessRule);
						return setPaymentOutputDetails;
					}
					if((objserviceContext.getMaxSeverity()== MessageType.WARNING || objserviceContext.getMaxSeverity()== MessageType.RISK))
					{
						//Call the CreatePaymentService With Status as "Awaiting Approval"
						EBWLogger.logDebug(this, "Creating Record in channels in Awaiting Approval state");

						//Status code mapping based on Max severity..
						if(objserviceContext.getMaxSeverity()== MessageType.WARNING){
							paymentStatusFlag = 2;
						}
						else if(objserviceContext.getMaxSeverity()== MessageType.RISK){
							paymentStatusFlag = 80;
						}
						CreatePaymentService objCreatePaymentService = new CreatePaymentService();
						objCreatePaymentService.setConnection(conn);
						objCreatePaymentService.modifyCreatePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Call the RTAB Create Hold Service
						EBWLogger.logDebug(this, "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "RTA Executed successfully ");

						//BR Validation Log Service 
						EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
						PaymentsUtility.executeBRLogging(txnDetails, objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "BR Exceptions Logged successfully");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P21";
						else
							notificationEventId = "NOTIFY_P9";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
						loggingAction=AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");
					}
					else
					{
						// Call the CreatePaymentService With Status as "Executed"
						EBWLogger.logDebug(this, "Creating Record in channels in Executed state");
						paymentStatusFlag=4;
						CreatePaymentService objCreatePaymentService = new CreatePaymentService();
						objCreatePaymentService.setConnection(conn);
						objCreatePaymentService.modifyCreatePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Call the Payment HUB Service
						PaymentsUtility.executePaymentsHub(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						//Call UpdatePaymentConfirmationNo Service	
						UpdatePaymentConfNoService objUpdatePaymentConfNoService = new UpdatePaymentConfNoService();
						objUpdatePaymentConfNoService.setConnection(conn);
						objUpdatePaymentConfNoService.setUpdatePaymentConfNoService(txnDetails,objserviceContext);

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P21";
						else
							notificationEventId = "NOTIFY_P9";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
						loggingAction=AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

						//Reset Approval Details : Executed State 
						EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
						ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
						objResetApproverInfo.setConnection(conn);
						objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Reset the approver details successfully");

						//Update Transition date : Executed State 
						EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
						UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
						objUpdtTransitionDt.setConnection(conn);
						objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Updated the transition date successfully");
					}
					EBWLogger.logDebug(this, "Executed setPaymentsTransation for One Time Immediate Transfer");
					EBWLogger.trace(this, "Finished with setPaymentsTransation for One Time Immediate Transfer");
				}
				else if(paymentDate.compareTo(businessDate)>0)
				{
					EBWLogger.logDebug(this, "The Processing transaction is ONETIME FUTURE DATED Transaction");

					//Call Business Rule Service 
					EBWLogger.logDebug(this, "Calling BusinessRule Service");
					PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.ERROR || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						EBWLogger.logDebug(this, "BusinessRule Service encountered Hard Error");
						setPaymentOutputDetails.add(objBusinessRule);
						return setPaymentOutputDetails;
					}
					if((objserviceContext.getMaxSeverity()== MessageType.WARNING || objserviceContext.getMaxSeverity()== MessageType.RISK))
					{
						EBWLogger.logDebug(this, "Creating Recurring Transaction in channels in future dated awaiting approval Status ");

						//Status code mapping based on Max severity..
						if(objserviceContext.getMaxSeverity()== MessageType.WARNING){
							paymentStatusFlag = 2;
						}
						else if(objserviceContext.getMaxSeverity()== MessageType.RISK){
							paymentStatusFlag = 80;
						}
						CreatePaymentService objCreatePaymentService = new CreatePaymentService();
						objCreatePaymentService.setConnection(conn);
						objCreatePaymentService.modifyCreatePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Remove the hold from RTA in case Previous status is in Awaiting Approval Status... 
						EBWLogger.logDebug(this, "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "RTA Executed successfully ");

						//BR Validation Log Service 
						EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
						PaymentsUtility.executeBRLogging(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "BR Exceptions Logged successfully");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P21";
						else
							notificationEventId = "NOTIFY_P9";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Create the transaction log..
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
						loggingAction=AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");
					}
					else
					{
						//Call the CreatePaymentService With Status as "Scheduled"
						EBWLogger.logDebug(this, "Creating Record in channels in Scheduled state");
						paymentStatusFlag=6;
						CreatePaymentService objCreatePaymentService = new CreatePaymentService();
						objCreatePaymentService.setConnection(conn);
						objCreatePaymentService.modifyCreatePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Remove the hold from RTA in case Previous status is in Awaiting Approval Status... 
						EBWLogger.logDebug(this, "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "RTA Executed successfully ");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P21";
						else
							notificationEventId = "NOTIFY_P9";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Reset Approval Details : Scheduled State 
						EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
						ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
						objResetApproverInfo.setConnection(conn);
						objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Reset the approver details successfully");

						//Create the transaction log..
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
						loggingAction=AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");
					}
					EBWLogger.logDebug(this, "Executed setPaymentsTransation for Future Dated Transfer");
					EBWLogger.trace(this, "Finished with setPaymentsTransation for Future Dated Transfer");
				}
				else {
					objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}
			else if(isRecurringFlag==2)
			{
				if(paymentDate.compareTo(businessDate)==0)
				{
					EBWLogger.logDebug(this, "The Processing transaction is RECURRING SAME DATED Transaction");

					//Call Business Rule Service 
					EBWLogger.logDebug(this, "Calling BusinessRule Service");
					PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.ERROR || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						EBWLogger.logDebug(this, "BusinessRule Service encountered Hard Error or Technical Failure");
						setPaymentOutputDetails.add(objBusinessRule);
						return setPaymentOutputDetails;
					}
					if (objserviceContext.getMaxSeverity()== MessageType.WARNING || objserviceContext.getMaxSeverity()== MessageType.RISK)
					{
						//Status code mapping based on Max severity..
						if(objserviceContext.getMaxSeverity()== MessageType.WARNING){
							paymentStatusFlag = 2;
						}
						else if(objserviceContext.getMaxSeverity()== MessageType.RISK){
							paymentStatusFlag = 80;
						}
						CreateRecurringPayService objCreateRecurringPayService = new CreateRecurringPayService();
						objCreateRecurringPayService.setConnection(conn);
						objCreateRecurringPayService.modifyCreateRecurringPayDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Call the RTAB Create Hold Service
						EBWLogger.logDebug(this, "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "RTA Executed successfully ");

						//BR Validation Log Service 
						EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
						PaymentsUtility.executeBRLogging(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "BR Exceptions Logged successfully");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P22";
						else
							notificationEventId = "NOTIFY_P10";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
						loggingAction=AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");
					}
					else
					{
						//Call the CreateRecurringPayService with the Parent Transaction Status as Active and Child Transaction Status as "Executed"
						paymentStatusFlag=4;
						CreateRecurringPayService objCreateRecurringPayService = new CreateRecurringPayService();
						objCreateRecurringPayService.setConnection(conn);
						objCreateRecurringPayService.modifyCreateRecurringPayDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Call the Payment HUB Service
						PaymentsUtility.executePaymentsHub(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						//Call UpdatePaymentConfirmationNo Service	
						UpdatePaymentConfNoService objUpdatePaymentConfNoService = new UpdatePaymentConfNoService();
						objUpdatePaymentConfNoService.setConnection(conn);
						objUpdatePaymentConfNoService.setUpdatePaymentConfNoService(txnDetails,objserviceContext);

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
						loggingAction=AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

						//Call the UpdateTxnParent Service to update the Parent Transaction and create next Child Transaction
						UpdateTxnParentService objUpdateTxnParentService = new UpdateTxnParentService();
						objUpdateTxnParentService.setConnection(conn);
						objUpdateTxnParentService.setUpdateTxnParentDetails(txnDetails,objserviceContext);

						//Call the CreatePaymentDetailsLog Service for child transaction logging.
						if(objPaymentDetails.isChildTxnCreated()){
							EBWLogger.logDebug(this, "Child Transaction Logging in the Channels DB (Created Status)");
							childPayStatusFlag=6;
							CreatePaymentLogService objChildTxnLogging = new CreatePaymentLogService();
							objChildTxnLogging.setConnection(conn);
							objChildTxnLogging.setNextChildPaymentsLog(childPayStatusFlag,txnDetails,objserviceContext);
							EBWLogger.logDebug(this, "Child Transaction Logged in the Channels DB (Created Status)");
						}

						//Call the limit check service through Business rule service for all the child ...
						if(objPaymentDetails.isChildTxnCreated() && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
							EBWLogger.logDebug(this, "Calling limit check Service for all child future dated transactions");
							PaymentsUtility.processLimitRequest(txnDetails,objserviceContext);
							if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								EBWLogger.logDebug(this, "limit check Service failed for child future dated transactions");
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Limit check service for future dated transactions");
						}

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P22";
						else
							notificationEventId = "NOTIFY_P10";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Reset Approval Details : Executed State 
						EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
						ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
						objResetApproverInfo.setConnection(conn);
						objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Reset the approver details successfully");

						//Update Transition date : Executed State 
						EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
						UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
						objUpdtTransitionDt.setConnection(conn);
						objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Updated the transition date successfully");
					}
					EBWLogger.logDebug(this, "Executed setPaymentsTransation for One Time Immediate Transfer");
					EBWLogger.trace(this, "Finished with setPaymentsTransation for One Time Immediate Transfer");
				}
				else if(paymentDate.compareTo(businessDate)>0)
				{
					EBWLogger.logDebug(this, "The Processing transaction is RECURRING FUTURE DATED Transaction");

					//Call Business Rule Service 
					EBWLogger.logDebug(this, "Calling BusinessRule Service");
					PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.ERROR || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						EBWLogger.logDebug(this, "BusinessRule Service encountered Hard Error");	
						setPaymentOutputDetails.add(objBusinessRule);
						return setPaymentOutputDetails;
					}
					if (objserviceContext.getMaxSeverity()== MessageType.WARNING || objserviceContext.getMaxSeverity()== MessageType.RISK)
					{
						EBWLogger.logDebug(this, "Creating Recurring Transaction in channels in future dated awaiting approval Status ");

						//Status code mapping based on Max severity..
						if(objserviceContext.getMaxSeverity()== MessageType.WARNING){
							paymentStatusFlag = 2;
						}
						else if(objserviceContext.getMaxSeverity()== MessageType.RISK){
							paymentStatusFlag = 80;
						}
						CreateRecurringPayService objCreateRecurringPayService = new CreateRecurringPayService();
						objCreateRecurringPayService.setConnection(conn);
						objCreateRecurringPayService.modifyCreateRecurringPayDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
						{
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Remove the hold from RTA in case Previous status is in Awaiting Approval Status... 
						EBWLogger.logDebug(this, "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "RTA Executed successfully ");

						//BR Validation Log Service 
						EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
						PaymentsUtility.executeBRLogging(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "BR Exceptions Logged successfully");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P22";
						else
							notificationEventId = "NOTIFY_P10";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
						loggingAction=AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");
					}
					else
					{ 
						//Call the CreateRecurringPayService with the Parent Transaction Status as Active and Child Transaction Status as "Scheduled"
						paymentStatusFlag=6;
						CreateRecurringPayService objCreateRecurringPayService = new CreateRecurringPayService();
						objCreateRecurringPayService.setConnection(conn);
						objCreateRecurringPayService.modifyCreateRecurringPayDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						//External Account Link creation..
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE)){
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setPaymentOutputDetails.add(objserviceContext);
								return setPaymentOutputDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}

						//Remove the hold from RTA in case Previous status is in Awaiting Approval Status... 
						EBWLogger.logDebug(this, "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "RTA Executed successfully ");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && ((transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P22";
						else
							notificationEventId = "NOTIFY_P10";
						PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "Notification Service Executed successfully");

						//Reset Approval Details : Scheduled State 
						EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
						ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
						objResetApproverInfo.setConnection(conn);
						objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Reset the approver details successfully");

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
						loggingAction=AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");
					}
					EBWLogger.logDebug(this, "Executed setPaymentsTransation for Future Dated Transfer");
					EBWLogger.trace(this, "Finished with setPaymentsTransation for Future Dated Transfer");
				}
				else {
					objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}
			else {
				objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}
			objserviceContext.setServiceCallSuccessful(true);
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(sqlexception));
		}
		catch(Exception exception){
			exception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(exception));
		}
		finally {

		}
		setPaymentOutputDetails.add(objserviceContext);
		setPaymentOutputDetails.add(txnDetails);
		return setPaymentOutputDetails;
	}

	/**
	 * For Canceling the transaction 
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 */ 
	public ArrayList<Object> cancelPaymentTransfer(String stmntId,Object toObjects[], Boolean boolean1)
	{
		conn = (Connection)serviceConnection;
		EBWLogger.trace(this, "Starting cancelPaymentTransfer method in Implementation class");
		boolean1 = Boolean.TRUE;
		int paymentStatusFlag = 0;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		ArrayList<Object> cancelPayOutDetails = new ArrayList<Object>();
		String notificationEventId = "";
		boolean isExternalAccount = false;
		HashMap txnDetails = new HashMap();
		String transferType = "";
		String frequencyFlag ="";
		int isRecurringFlag=0;
		String transactionStatus = "";
		String loggingAction ="";
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		try
		{
			//Mapping the Payment details (Internal/External Cancel Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setPaymentConfirmDetails(toObjects,objserviceContext);

			//Mapping the payment attributes...
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			transferType= objPaymentDetails.getTransfer_Type();
			frequencyFlag = objPaymentDetails.getFrequency_Type();
			isRecurringFlag= ConvertionUtil.convertToint(frequencyFlag);
			transactionStatus = objPaymentDetails.getTxnPrevStatusCode();

			//User Entitlements Check for Verifying the internal and external accounts getting passed to the service are valid or not...
			UserEntilements userEntilements = new UserEntilements();
			userEntilements.setConnection(conn);
			boolean isAccSuspended=false;
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
				userEntilements.checkIntAccEntitlement(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					cancelPayOutDetails.add(objserviceContext);
					return cancelPayOutDetails; 
				}
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
				if(transactionStatus!=null && transactionStatus.equalsIgnoreCase("46")){
					isAccSuspended=true;
				}
				userEntilements.checkExtAccRoutingNum(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					cancelPayOutDetails.add(objserviceContext);
					return cancelPayOutDetails; 
				}
				userEntilements.checkExtAccEntitlements(txnDetails,isAccSuspended,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					cancelPayOutDetails.add(objserviceContext);
					return cancelPayOutDetails; 
				}
			}

			if(isRecurringFlag==1)
			{
				//Scheduled Status 
				if(transactionStatus.equalsIgnoreCase("6"))
				{
					// Cancel the One time Transfer with the status 20 and create the log in the DS_PAY_TXNS
					EBWLogger.logDebug(this, "Canceling the Record in DB and creating the log ");
					paymentStatusFlag=20;
					CancelPaymentService objCancelPaymentService = new CancelPaymentService();
					objCancelPaymentService.setConnection(conn);
					objCancelPaymentService.cancelOneTimeTransfer(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}

					//Call limit check service through Business Rule for deleting the limit ....
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))
					{
						//Calling Merlin Service...
						EBWLogger.logDebug(this, "Executing the Merlin Service..");
						PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							cancelPayOutDetails.add(objserviceContext);
							return cancelPayOutDetails; 
						}
						EBWLogger.logDebug(this, "Executed the Merlin Service..");

						//Calling Business Rule Limit check Service...
						EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
						PaymentsUtility.processLimitRequest(txnDetails, objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
							cancelPayOutDetails.add(objserviceContext);
							return cancelPayOutDetails;
						}
					}

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
						notificationEventId = "NOTIFY_P23";
					else
						notificationEventId = "NOTIFY_P11";
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction=AuditTrialAction.Cancel;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

					//Reset Approval Details : Canceled State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : Canceled State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");
				}
				//Suspended Status , no RTA delete required as its deleted already once transaction is suspended through Batch or COE..
				else if(transactionStatus.equalsIgnoreCase("46"))
				{
					// Cancel the One time Transfer with the status 20 and create the log in the DS_PAY_TXNS
					EBWLogger.logDebug(this, "Canceling the Record in DB and creating the log ");
					paymentStatusFlag=20;
					CancelPaymentService objCancelPaymentService = new CancelPaymentService();
					objCancelPaymentService.setConnection(conn);
					objCancelPaymentService.cancelOneTimeTransfer(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
						notificationEventId = "NOTIFY_P23";
					else
						notificationEventId = "NOTIFY_P11";
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction=AuditTrialAction.Cancel;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

					//Reset Approval Details : Canceled State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : Canceled State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");
				}
				// Call RTA Delete only in case of transaction status is in Awaiting Approval or Pending Risk Review....
				else if(transactionStatus.equalsIgnoreCase("2") || transactionStatus.equalsIgnoreCase("80"))
				{
					//Cancel the One time Transfer with the status 20 and create the log in the DS_PAY_TXNS
					EBWLogger.logDebug(this, "Canceling the Record in DB and creating the log ");
					paymentStatusFlag=20;
					CancelPaymentService objCancelPaymentService = new CancelPaymentService();
					objCancelPaymentService.setConnection(conn);
					objCancelPaymentService.cancelOneTimeTransfer(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}

					//Call limit check service through Business Rule for deleting the limit ....
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))
					{
						//Calling Merlin Service...
						EBWLogger.logDebug(this, "Executing the Merlin Service..");
						PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							cancelPayOutDetails.add(objserviceContext);
							return cancelPayOutDetails; 
						}
						EBWLogger.logDebug(this, "Executed the Merlin Service..");

						//Calling Business Rule Limit check Service..
						EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
						PaymentsUtility.processLimitRequest(txnDetails, objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
							cancelPayOutDetails.add(objserviceContext);
							return cancelPayOutDetails;
						}
					}

					// Remove the RTA hold 
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "RTA Executed successfully ");

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
						notificationEventId = "NOTIFY_P23";
					else
						notificationEventId = "NOTIFY_P11";
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction=AuditTrialAction.Cancel;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

					//Reset Approval Details : Canceled State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : Canceled State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");
				}
				else {
					MSCommonUtils.pushTxnVersionMismatch(objserviceContext);
					cancelPayOutDetails.add(objserviceContext);
					return cancelPayOutDetails; 
				}
				EBWLogger.logDebug(this, "Executed cancelPaymentTransfer for One time transfer");
				EBWLogger.trace(this, "Finished with cancelPaymentTransfer for One time transfer");
			}
			else if(isRecurringFlag==2)
			{
				//Scheduled Status 
				if(transactionStatus.equalsIgnoreCase("6"))
				{
					// Cancel the One time Transfer with the status 20
					EBWLogger.logDebug(this, "Canceling the Record in DB and creating the log ");
					paymentStatusFlag=20;
					CancelRecurringPaymentService objCancelRecurringPaymentService = new CancelRecurringPaymentService();
					objCancelRecurringPaymentService.setConnection(conn);
					objCancelRecurringPaymentService.cancelRecurringTransfer(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}

					//Call limit check service through Business Rule for deleting the limit ....
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))
					{
						//Calling Account View Service...
						EBWLogger.logDebug(this, "Executing the Merlin Service..");
						PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							cancelPayOutDetails.add(objserviceContext);
							return cancelPayOutDetails; 
						}
						EBWLogger.logDebug(this, "Executed the Merlin Service..");

						//Calling the BR limit service...
						EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
						PaymentsUtility.processLimitRequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
							cancelPayOutDetails.add(objserviceContext);
							return cancelPayOutDetails;
						}
					}

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
						notificationEventId = "NOTIFY_P24";
					else
						notificationEventId = "NOTIFY_P12";
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction=AuditTrialAction.Cancel;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

					//Reset Approval Details : Canceled State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : Canceled State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");
				}
				//Suspended Status , no RTA delete required as its deleted already once transaction is suspended through Batch or COE..
				else if(transactionStatus.equalsIgnoreCase("46"))
				{
					// Cancel the One time Transfer with the status 20
					EBWLogger.logDebug(this, "Canceling the Record in DB and creating the log ");
					paymentStatusFlag=20;
					CancelRecurringPaymentService objCancelRecurringPaymentService = new CancelRecurringPaymentService();
					objCancelRecurringPaymentService.setConnection(conn);
					objCancelRecurringPaymentService.cancelRecurringTransfer(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
						notificationEventId = "NOTIFY_P24";
					else
						notificationEventId = "NOTIFY_P12";
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction=AuditTrialAction.Cancel;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

					//Reset Approval Details : Canceled State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : Canceled State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");
				}
				// Awaiting Approval or Pending Risk Review...
				else if(transactionStatus.equalsIgnoreCase("2") || transactionStatus.equalsIgnoreCase("80"))
				{
					//Cancel the One time Transfer with the status 20
					EBWLogger.logDebug(this, "Canceling the Record in DB and creating the log ");
					paymentStatusFlag=20;
					CancelRecurringPaymentService objCancelRecurringPaymentService = new CancelRecurringPaymentService();
					objCancelRecurringPaymentService.setConnection(conn);
					objCancelRecurringPaymentService.cancelRecurringTransfer(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}

					//Call limit check service through Business Rule for deleting the limit ....
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))
					{
						//Calling Account View Service...
						EBWLogger.logDebug(this, "Executing the Merlin Service..");
						PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							cancelPayOutDetails.add(objserviceContext);
							return cancelPayOutDetails; 
						}
						EBWLogger.logDebug(this, "Executed the Merlin Service..");

						//Calling BR Limit check service...
						EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
						PaymentsUtility.processLimitRequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
							cancelPayOutDetails.add(objserviceContext);
							return cancelPayOutDetails;
						}
					}

					// Remove the RTA hold 
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "RTA Executed successfully ");

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
						notificationEventId = "NOTIFY_P24";
					else
						notificationEventId = "NOTIFY_P12";
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction=AuditTrialAction.Cancel;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

					//Reset Approval Details : Canceled State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : Canceled State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");
				}
				else {
					MSCommonUtils.pushTxnVersionMismatch(objserviceContext);
					cancelPayOutDetails.add(objserviceContext);
					return cancelPayOutDetails; 
				}
				EBWLogger.logDebug(this, "Executed cancelPaymentTransfer for Recurring Transfer");
				EBWLogger.trace(this, "Finished with cancelPaymentTransfer for Recurring transfer");
			}
			else {
				objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
				cancelPayOutDetails.add(objserviceContext);
				return cancelPayOutDetails; 
			}
			objserviceContext.setServiceCallSuccessful(true);
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(sqlexception));
		}
		catch(Exception exception){
			exception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(exception));
		}
		finally {

		}
		cancelPayOutDetails.add(objserviceContext);
		cancelPayOutDetails.add(txnDetails);
		return cancelPayOutDetails; 
	}

	/**
	 * For Skipping next transaction 
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 */

	public ArrayList<Object> skipNextTransfer(String stmntId,Object toObjects[], Boolean boolean1)
	{
		conn = (Connection)serviceConnection;
		EBWLogger.trace(this, "Starting cancelPaymentTransfer method in Implementation class");
		boolean1 = Boolean.TRUE;
		int paymentStatusFlag=0;
		int childPayStatusFlag = 0;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		ArrayList<Object> skipNextTransferOut = new ArrayList<Object>();
		String notificationEventId = "";
		boolean isExternalAccount = false;
		HashMap txnDetails = new HashMap();
		String transferType = "";
		String transactionStatus = "";
		String loggingAction ="";
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		try
		{
			//Mapping the Payment details (Internal/External Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setPaymentConfirmDetails(toObjects,objserviceContext);

			//Mapping the payment attributes...
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			transferType= objPaymentDetails.getTransfer_Type();
			transactionStatus = objPaymentDetails.getTxnPrevStatusCode();

			//User Entitlements Check for Verifying the internal and external accounts getting passed to the service are valid or not...
			UserEntilements userEntilements = new UserEntilements();
			userEntilements.setConnection(conn);
			boolean isAccSuspended=false;
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN))){
				userEntilements.checkIntAccEntitlement(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
				if(transactionStatus!=null && transactionStatus.equalsIgnoreCase("46")){
					isAccSuspended=true;
				}
				userEntilements.checkExtAccRoutingNum(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}
				userEntilements.checkExtAccEntitlements(txnDetails,isAccSuspended,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}
			}

			//Transaction Status defines the Old Status of the Transaction before cancel or skip .... Scheduled Status 
			if(transactionStatus.equalsIgnoreCase("6"))
			{
				//Skip Next Transfers for the Recurring Payment
				paymentStatusFlag=20;
				EBWLogger.logDebug(this, "Canceling the Existing Record in DB and creating the log ");
				CancelPaymentService objCancelPaymentService = new CancelPaymentService();
				objCancelPaymentService.setConnection(conn);
				objCancelPaymentService.cancelOneTimeTransfer(paymentStatusFlag,txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}

				//Call limit check service through Business Rule for deleting the limit ....
				if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))
				{
					//Calling Merlin Service...
					EBWLogger.logDebug(this, "Executing the Merlin Service..");
					PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						skipNextTransferOut.add(objserviceContext);
						return skipNextTransferOut; 
					}
					EBWLogger.logDebug(this, "Executed the Merlin Service..");

					//Calling BR limit check service...
					EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
					PaymentsUtility.processLimitRequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
						skipNextTransferOut.add(objserviceContext);
						return skipNextTransferOut;
					}
				}

				//Call the CreatePaymentDetailsLog Service
				EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
				loggingAction=AuditTrialAction.Cancel;
				CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
				objCreatePaymentLogService.setConnection(conn);
				objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

				EBWLogger.logDebug(this, " Creating the next child transaction in DB ");
				paymentStatusFlag=6;
				SkipNextTransfer objSkipNextTransfer = new SkipNextTransfer();
				objSkipNextTransfer.setConnection(conn);
				objSkipNextTransfer.skipNextTransfer(paymentStatusFlag,txnDetails,objserviceContext);
				if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}

				//Call the CreatePaymentDetailsLog Service for child transaction logging.
				if(objPaymentDetails.isChildTxnCreated()){
					EBWLogger.logDebug(this, "Child Transaction Logging in the Channels DB (Created Status)");
					childPayStatusFlag=6;
					CreatePaymentLogService objChildTxnLogging = new CreatePaymentLogService();
					objChildTxnLogging.setConnection(conn);
					objChildTxnLogging.setNextChildPaymentsLog(childPayStatusFlag,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Child Transaction Logged in the Channels DB (Created Status)");
				}

				//Call the limit check service through Business rule service for all the child ...
				if(objPaymentDetails.isChildTxnCreated() && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
					EBWLogger.logDebug(this, "Calling limit check Service for all child future dated transactions");
					PaymentsUtility.processLimitRequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						EBWLogger.logDebug(this, "limit check Service failed for child future dated transactions");
						skipNextTransferOut.add(objserviceContext);
						return skipNextTransferOut; 
					}
					EBWLogger.logDebug(this, "Limit check service for future dated transactions");
				}

				//Call Notification Service 
				EBWLogger.logDebug(this, "Call Notification Service");
				if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
					notificationEventId = "NOTIFY_P25";
				else
					notificationEventId = "NOTIFY_P13";
				PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}
				EBWLogger.logDebug(this, "Notification Service Executed successfully");

				//Reset Approval Details : Canceled State 
				EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
				ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
				objResetApproverInfo.setConnection(conn);
				objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Reset the approver details successfully");

				//Update Transition date : Canceled State 
				EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
				UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
				objUpdtTransitionDt.setConnection(conn);
				objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Updated the transition date successfully");
			}

			// Suspended transactions , Next child will be created in the suspended status only (46) ....
			else if(transactionStatus.equalsIgnoreCase("46"))
			{
				//Skip Next Transfers for the Recurring Payment
				paymentStatusFlag=20;
				EBWLogger.logDebug(this, "Canceling the Existing Record in DB and creating the log ");
				CancelPaymentService objCancelPaymentService = new CancelPaymentService();
				objCancelPaymentService.setConnection(conn);
				objCancelPaymentService.cancelOneTimeTransfer(paymentStatusFlag,txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}

				//Call the CreatePaymentDetailsLog Service
				EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
				loggingAction=AuditTrialAction.Cancel;
				CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
				objCreatePaymentLogService.setConnection(conn);
				objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

				//If the suspended recurring transaction is skipped next child will be created in SUSPENDED Status (46)
				EBWLogger.logDebug(this, " Creating the next child transaction in DB ");
				paymentStatusFlag=46;
				SkipNextTransfer objSkipNextTransfer = new SkipNextTransfer();
				objSkipNextTransfer.setConnection(conn);
				objSkipNextTransfer.skipNextTransfer(paymentStatusFlag,txnDetails,objserviceContext);
				if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}

				//Call the CreatePaymentDetailsLog Service for child transaction logging.
				if(objPaymentDetails.isChildTxnCreated()){
					EBWLogger.logDebug(this, "Child Transaction Logging in the Channels DB (Created Status)");
					childPayStatusFlag=46;
					CreatePaymentLogService objChildTxnLogging = new CreatePaymentLogService();
					objChildTxnLogging.setConnection(conn);
					objChildTxnLogging.setNextChildPaymentsLog(childPayStatusFlag,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Child Transaction Logged in the Channels DB (Created Status)");
				}

				//Call Notification Service 
				EBWLogger.logDebug(this, "Call Notification Service");
				if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
					notificationEventId = "NOTIFY_P25";
				else
					notificationEventId = "NOTIFY_P13";
				PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}
				EBWLogger.logDebug(this, "Notification Service Executed successfully");

				//Reset Approval Details : Canceled State 
				EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
				ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
				objResetApproverInfo.setConnection(conn);
				objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Reset the approver details successfully");

				//Update Transition date : Canceled State 
				EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
				UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
				objUpdtTransitionDt.setConnection(conn);
				objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Updated the transition date successfully");
			}

			// Call RTA Delete only in case of transaction status is in Awaiting Approval or Pending Risk review....
			else if(transactionStatus.equalsIgnoreCase("2") || transactionStatus.equalsIgnoreCase("80"))
			{
				//Cancel the current parent transfer...
				EBWLogger.logDebug(this, "Canceling the Existing Record in DB and creating the log ");
				paymentStatusFlag=20;
				CancelPaymentService objCancelPaymentService = new CancelPaymentService();
				objCancelPaymentService.setConnection(conn);
				objCancelPaymentService.cancelOneTimeTransfer(paymentStatusFlag,txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}

				//Call limit check service through Business Rule for deleting the limit ....
				if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))
				{
					//Calling AccountView Service...
					EBWLogger.logDebug(this, "Executing the Merlin Service..");
					PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						skipNextTransferOut.add(objserviceContext);
						return skipNextTransferOut; 
					}
					EBWLogger.logDebug(this, "Executed the Merlin Service..");

					//Calling BR Limit check service..
					EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
					PaymentsUtility.processLimitRequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
						skipNextTransferOut.add(objserviceContext);
						return skipNextTransferOut;
					}
				}

				// Remove the RTA hold 
				EBWLogger.logDebug(this, "Calling the RTA Service ");
				PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}
				EBWLogger.logDebug(this, "RTA Service executed successfully");

				//Call the CreatePaymentDetailsLog Service
				EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
				loggingAction=AuditTrialAction.Cancel;
				CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
				objCreatePaymentLogService.setConnection(conn);
				objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

				//Creating a new Child record if condition passes...
				EBWLogger.logDebug(this, " Creating the next child transaction in DB ");
				paymentStatusFlag=6;
				SkipNextTransfer objSkipNextTransfer = new SkipNextTransfer();
				objSkipNextTransfer.setConnection(conn);
				objSkipNextTransfer.skipNextTransfer(paymentStatusFlag,txnDetails,objserviceContext);
				if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}

				//Call the CreatePaymentDetailsLog Service for child transaction logging.
				if(objPaymentDetails.isChildTxnCreated()){
					EBWLogger.logDebug(this, "Child Transaction Logging in the Channels DB (Created Status)");
					childPayStatusFlag=6;
					CreatePaymentLogService objChildTxnLogging = new CreatePaymentLogService();
					objChildTxnLogging.setConnection(conn);
					objChildTxnLogging.setNextChildPaymentsLog(childPayStatusFlag,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Child Transaction Logged in the Channels DB (Created Status)");
				}

				//Call the limit check service through Business rule service for new child creation ...
				if(objPaymentDetails.isChildTxnCreated() && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
					EBWLogger.logDebug(this, "Calling limit check Service for all child future dated transactions");
					PaymentsUtility.processLimitRequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						EBWLogger.logDebug(this, "limit check Service failed for child future dated transactions");
						skipNextTransferOut.add(objserviceContext);
						return skipNextTransferOut; 
					}
					EBWLogger.logDebug(this, "Limit check service for future dated transactions");
				}

				//Call Notification Service 
				EBWLogger.logDebug(this, "Call Notification Service");
				if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)))
					notificationEventId = "NOTIFY_P25";
				else
					notificationEventId = "NOTIFY_P13";
				PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}
				EBWLogger.logDebug(this, "Notification Service Executed successfully");

				//Reset Approval Details : Canceled State 
				EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
				ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
				objResetApproverInfo.setConnection(conn);
				objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Reset the approver details successfully");

				//Update Transition date : Canceled State 
				EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
				UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
				objUpdtTransitionDt.setConnection(conn);
				objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Updated the transition date successfully");
			}
			else {
				MSCommonUtils.pushTxnVersionMismatch(objserviceContext);
				skipNextTransferOut.add(objserviceContext);
				return skipNextTransferOut; 
			}
			objserviceContext.setServiceCallSuccessful(true);
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(sqlexception));
		}
		catch(Exception exception){
			exception.printStackTrace();
			objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(exception));
		}
		finally {

		}
		skipNextTransferOut.add(objserviceContext);
		skipNextTransferOut.add(txnDetails);
		return skipNextTransferOut;
	}
}