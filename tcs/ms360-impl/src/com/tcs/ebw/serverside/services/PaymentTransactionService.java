package com.tcs.ebw.serverside.services;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.AuditTrialAction;
import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.Payments.serverValidations.StatusConsistencyChk;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.Message;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountEligible;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountLink;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountProperties;
import com.tcs.ebw.serverside.services.channelPaymentServices.ApproveTransactionService;
import com.tcs.ebw.serverside.services.channelPaymentServices.BusinessRulesService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CancelPaymentService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CancelRecurringPaymentService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CheckHoliday;
import com.tcs.ebw.serverside.services.channelPaymentServices.ChkTrxnHelperService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CreatePaymentLogService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CreatePaymentService;
import com.tcs.ebw.serverside.services.channelPaymentServices.CreateRecurringPayService;
import com.tcs.ebw.serverside.services.channelPaymentServices.ExecuteTxnViewDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.GetExternalAccounts;
import com.tcs.ebw.serverside.services.channelPaymentServices.GetExternalAccountsDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.GetQZBusinessDate;
import com.tcs.ebw.serverside.services.channelPaymentServices.GetReasonCodes;
import com.tcs.ebw.serverside.services.channelPaymentServices.MapPaymentInputDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.PayConfirmationNum;
import com.tcs.ebw.serverside.services.channelPaymentServices.PortfolioLoansService;
import com.tcs.ebw.serverside.services.channelPaymentServices.ResetApprovalDetails;
import com.tcs.ebw.serverside.services.channelPaymentServices.SkipNextTransfer;
import com.tcs.ebw.serverside.services.channelPaymentServices.TxnProperties;
import com.tcs.ebw.serverside.services.channelPaymentServices.UpdateBRValidationLog;
import com.tcs.ebw.serverside.services.channelPaymentServices.UpdatePaymentConfNoService;
import com.tcs.ebw.serverside.services.channelPaymentServices.UpdateTransitionDate;
import com.tcs.ebw.serverside.services.channelPaymentServices.UpdateTxnParentService;
import com.tcs.ebw.serverside.services.channelPaymentServices.UserEntilements;
import com.tcs.ebw.serverside.services.channelPaymentServices.ValidateCutOffTime;
import com.tcs.ebw.serverside.services.channelPaymentServices.ValidateTransaction;
import com.tcs.ebw.serverside.services.channelPaymentServices.VerifyVersionMismatch;
import com.tcs.mswitch.common.channel.DBProcedureChannel;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       01-05-2011        2               CR 215
 *    224703       01-05-2011        3               Internal 24x7
 *    224703       01-05-2011        3               3rd Party ACH
 *    224703       25-09-2011        3               PLA
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
	public ArrayList onLoadAccDetails(String stmntId, Object toObjects[], Boolean boolean1,String transferType) throws SQLException,Exception
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
				EBWLogger.logDebug(this, "Getting the External Accounts list (Same name + Non-same Name)");
				GetExternalAccounts objgetExternalAccounts = new GetExternalAccounts();
				objgetExternalAccounts.setConnection(conn);
				objgetExternalAccounts.getExternalAccounts(txnDetails,objserviceContext);
			}
			objserviceContext.setServiceCallSuccessful(true);
			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
				//RollBack is called in case of Error code return other than 1 from SI for external interfaces..
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
		filteredAccountList.add(objserviceContext);
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

			objserviceContext.setServiceCallSuccessful(true);
			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
				//RollBack is called in case of Error code return other than 1 from SI for external interfaces..
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
	public ArrayList onSelectedAccDetails(String stmntId, Object toObjects[], Boolean boolean1) throws SQLException,Exception
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
			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
		selectedAccountDetails.add(objserviceContext);
		selectedAccountDetails.add(txnDetails);
		return selectedAccountDetails;
	}

	/**
	 * Method for getting the Account plating details on selecting the Internal Bank Account  
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 */
	public ArrayList getMSAccPlatingDetails(String stmntId, Object toObjects[], Boolean boolean1) throws SQLException,Exception
	{
		conn = (Connection)serviceConnection;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		EBWLogger.trace(this, "Starting getMSAccPlatingDetails method in Implementation class");
		ArrayList<Serializable> accountPlatingDetails=new ArrayList<Serializable>();
		HashMap txnDetails = new HashMap();
		try
		{
			//Mapping the Payment details (Internal/External Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setOnAccSelectDetails(toObjects,objserviceContext);

			EBWLogger.logDebug(this, "Executing the MS Internal Account Account Plating details....");
			PaymentsUtility.getAccountPlatingInfo(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				accountPlatingDetails.add(objserviceContext);
				return accountPlatingDetails; 
			}
			EBWLogger.logDebug(this, "Executed the MS Internal Account Account Plating details....");

			EBWLogger.trace(this, "Finished with getMSAccPlatingDetails method in Implementation class");
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
	 * Method for getting the Account plating details on selecting the External Bank Account  
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 */
	public ArrayList getExtAccPlatingDetails(String stmntId, Object toObjects[], Boolean boolean1) throws SQLException,Exception
	{
		conn = (Connection)serviceConnection;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		EBWLogger.trace(this, "Starting getExtAccPlatingDetails method in Implementation class");
		ArrayList<Serializable> accountPlatingDetails=new ArrayList<Serializable>();
		HashMap txnDetails = new HashMap();
		try
		{
			//Mapping the Payment details (Internal/External Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setOnAccSelectDetails(toObjects,objserviceContext);

			EBWLogger.logDebug(this, "Executing the MS External Account Account Plating details....");
			GetExternalAccountsDetails objExtAcntDetails = new GetExternalAccountsDetails();
			objExtAcntDetails.setConnection(serviceConnection);
			objExtAcntDetails.getExtAcntPlatingInfo(txnDetails,objserviceContext);
			EBWLogger.logDebug(this, "Executed the MS External Account Account Plating details....");

			EBWLogger.trace(this, "Finished with getExtAccPlatingDetails method in Implementation class");
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
	 * Method for calling the AccountView service on selecting the Internal Bank Account  
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 */
	public ArrayList getAccountViewDetails(String stmntId, Object toObjects[], Boolean boolean1) throws SQLException,Exception
	{
		conn = (Connection)serviceConnection;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		EBWLogger.trace(this, "Starting getAccountViewDetails method in Implementation class");
		ArrayList<Serializable> getAccountViewDetails=new ArrayList<Serializable>();
		HashMap txnDetails = new HashMap();
		try
		{
			//Mapping the Payment details (Internal/External Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setOnAccSelectDetails(toObjects,objserviceContext);

			//Calling the Account View Service for FROM and TO accounts if applicable ... 
			EBWLogger.logDebug(this, "Executing the Merlin Service..");
			PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				getAccountViewDetails.add(objserviceContext);
				return getAccountViewDetails; 
			}
			EBWLogger.logDebug(this, "Executed the Merlin Service..");

			EBWLogger.trace(this, "Finished with getAccountViewDetails...");
			objserviceContext.setServiceCallSuccessful(true);
			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
		getAccountViewDetails.add(objserviceContext);
		getAccountViewDetails.add(txnDetails);
		return getAccountViewDetails;
	}

	/**
	 * Function called on submitting the transaction.. 
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 */
	public ArrayList<Object> createSubmitTransfer(String stmntId, Object toObjects[], Boolean boolean1) throws SQLException,Exception
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

			//Getting the account properties if required only ..
			AccountProperties acntProperties = new AccountProperties();
			acntProperties.setConnection(serviceConnection);
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
				acntProperties.getDebitAcountProps(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
			}
			else if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT))){
				acntProperties.getCreditAcountProps(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
			}

			//User Entitlements Check for Verifying the internal and external accounts getting passed to the service are valid or not...
			UserEntilements userEntilements = new UserEntilements();
			userEntilements.setConnection(conn);
			boolean isAccSuspended=false;
			userEntilements.checkFunctionalEntitlement(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				createConfirmDetails.add(objserviceContext);
				return createConfirmDetails; 
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
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
				userEntilements.checkExtAccOwner(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
			}

			//Check the Cut Off Time for the same date transactions..
			if(paymentDate.compareTo(businessDate) == 0 && !MSCommonUtils.check24x7Access(transferType)){
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
			if(transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)){
				PaymentsUtility.getAccountBalance(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					createConfirmDetails.add(objserviceContext);
					return createConfirmDetails; 
				}
				EBWLogger.logDebug(this, "Executed the RTAB Service..");
			}

			// Get the TxnProperties IRA details .....
			EBWLogger.logDebug(this, "Getting the TxnProperties for the selected transaction..");
			TxnProperties objTxnProperties= new TxnProperties();
			objTxnProperties.setConnection(serviceConnection);
			objTxnProperties.getIRATxnDetails(txnDetails,objserviceContext);
			if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				createConfirmDetails.add(objserviceContext);
				return createConfirmDetails; 
			}
			EBWLogger.logDebug(this, "Finished getting the TxnProperties for the selected transaction..");

			//External account link check..
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
			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
	public ArrayList<Object> setPaymentsTransation(String stmntId,Object toObjects[], Boolean boolean1) throws SQLException,Exception
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

			//Getting the account properties if required only ..
			AccountProperties acntProperties = new AccountProperties();
			acntProperties.setConnection(serviceConnection);
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
				acntProperties.getDebitAcountProps(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}
			else if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT))){
				acntProperties.getCreditAcountProps(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}

			//User Entitlements Check for Verifying the internal and external accounts getting passed to the service are valid or not...
			UserEntilements userEntilements = new UserEntilements();
			userEntilements.setConnection(conn);
			boolean isAccSuspended=false;
			userEntilements.checkFunctionalEntitlement(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
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
				userEntilements.checkExtAccOwner(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				userEntilements.checkSpokeToOwner(txnDetails,objserviceContext);
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
			if(transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))
			{  
				//Calling the RTAB Service..
				PaymentsUtility.getAccountBalance(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				EBWLogger.logDebug(this, "Executed the RTAB Service..");
			}

			//IRA Transaction details...
			EBWLogger.logDebug(this, "Getting the TxnProperties for the selected transaction..");
			TxnProperties objTxnProperties= new TxnProperties();
			objTxnProperties.setConnection(serviceConnection);
			objTxnProperties.getIRATxnDetails(txnDetails,objserviceContext);
			if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}
			EBWLogger.logDebug(this, "Finished getting the TxnProperties for the selected transaction..");

			//External account link check..
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

			//Getting the confirmation number for the FTM Call service invoke from the sequence only for ACH Transfers .... 
			PayConfirmationNum paymentConfirmationId = new PayConfirmationNum();
			paymentConfirmationId.setConnection(conn);
			paymentConfirmationId.getPaymentNumberFrmSeq(txnDetails);

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

					if((objserviceContext.getMaxSeverity()== MessageType.WARNING)){
						EBWLogger.logDebug(this, "BusinessRule Service encountered Warning");
						paymentStatusFlag=2;
						EBWLogger.logDebug(this, "Creating Record in channels in Awaiting Approval state");
						CreatePaymentService objCreatePaymentService = new CreatePaymentService();
						objCreatePaymentService.setConnection(conn);
						objCreatePaymentService.setCreatePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction created in channels successfully");

						// Making RTA Entries...
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
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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
						loggingAction = AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB successfully");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL)|| transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P19";
						else
							notificationEventId = "NOTIFY_P7";
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

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB (Created Status)");
						loggingAction= AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB successfully");	

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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
					if((objserviceContext.getMaxSeverity()== MessageType.WARNING)){
						EBWLogger.logDebug(this, "Creating Transaction in channels in future dated awaiting approval Status ");
						paymentStatusFlag=2;
						CreatePaymentService objCreatePaymentService = new CreatePaymentService();
						objCreatePaymentService.setConnection(conn);
						objCreatePaymentService.setCreatePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Created Transaction in channels in Scheduled Status ");

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
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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
						loggingAction= AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB (Created Status)");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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
						loggingAction= AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB (Created Status)");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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
					if((objserviceContext.getMaxSeverity()== MessageType.WARNING)){
						EBWLogger.logDebug(this, "BusinessRule Service encountered Warning");
						EBWLogger.logDebug(this, "Creating Record in channels in Awaiting Approval state");
						paymentStatusFlag=2;
						CreateRecurringPayService objCreateRecurringPayService = new CreateRecurringPayService();
						objCreateRecurringPayService.setConnection(conn);
						objCreateRecurringPayService.setCreateRecurringPayDetails(paymentStatusFlag,txnDetails,objserviceContext);

						// Making RTA Entries...
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
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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
						loggingAction= AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB (Created Status)");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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
						loggingAction= AuditTrialAction.Create;
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

						//No limits will be called for the MS360 transaction created from FA/Branch..

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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
					if((objserviceContext.getMaxSeverity()== MessageType.WARNING)){
						EBWLogger.logDebug(this, "Creating Transaction in channels in Awaiting approval Status ");
						paymentStatusFlag=2;
						CreateRecurringPayService objCreateRecurringPayService = new CreateRecurringPayService();
						objCreateRecurringPayService.setConnection(conn);
						objCreateRecurringPayService.setCreateRecurringPayDetails(paymentStatusFlag,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Created Transaction in channels in Scheduled Status ");

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
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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
						loggingAction= AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB (Created Status)");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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
						loggingAction= AuditTrialAction.Create;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB (Created Status)");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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
			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
	public ArrayList<Object> updatePaymentsTransation(String stmntId,Object toObjects[], Boolean boolean1) throws SQLException,Exception
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

			//Getting the account properties if required only ..
			AccountProperties acntProperties = new AccountProperties();
			acntProperties.setConnection(serviceConnection);
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
				acntProperties.getDebitAcountProps(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}
			else if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT))){
				acntProperties.getCreditAcountProps(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}

			//User Entitlements Check for Verifying the internal and external accounts getting passed to the service are valid or not...
			UserEntilements userEntilements = new UserEntilements();
			userEntilements.setConnection(conn);
			boolean isAccSuspended=false;
			userEntilements.checkFunctionalEntitlement(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
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
				userEntilements.checkExtAccOwner(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				userEntilements.checkSpokeToOwner(txnDetails,objserviceContext);
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

			//External account link check..
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
					if((objserviceContext.getMaxSeverity()== MessageType.WARNING)){
						EBWLogger.logDebug(this, "Creating Record in channels in Awaiting Approval state");
						paymentStatusFlag=2;
						CreatePaymentService objCreatePaymentService = new CreatePaymentService();
						objCreatePaymentService.setConnection(conn);
						objCreatePaymentService.modifyCreatePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
						{
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						// Making RTA Entries...
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
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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
						loggingAction = AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P21";
						else
							notificationEventId = "NOTIFY_P9";
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

						//Reset Approval Details : Executed State 
						EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
						ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
						objResetApproverInfo.setConnection(conn);
						objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Reset the approver details successfully");

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
						loggingAction= AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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
					if((objserviceContext.getMaxSeverity()== MessageType.WARNING)){
						EBWLogger.logDebug(this, "Creating Record in channels in Awaiting approval state");
						paymentStatusFlag=2;
						CreatePaymentService objCreatePaymentService = new CreatePaymentService();
						objCreatePaymentService.setConnection(conn);
						objCreatePaymentService.modifyCreatePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						// Making RTA Entries...
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
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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
						loggingAction= AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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

						// Making RTA Entries...
						EBWLogger.logDebug(this, "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						EBWLogger.logDebug(this, "RTA Executed successfully ");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
							notificationEventId = "NOTIFY_P21";
						else
							notificationEventId = "NOTIFY_P9";
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

						//Create the transaction log..
						EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
						loggingAction= AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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
					if (objserviceContext.getMaxSeverity()== MessageType.WARNING){
						paymentStatusFlag=2;
						CreateRecurringPayService objCreateRecurringPayService = new CreateRecurringPayService();
						objCreateRecurringPayService.setConnection(conn);
						objCreateRecurringPayService.modifyCreateRecurringPayDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
						{
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						// Making RTA Entries...
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
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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
						loggingAction= AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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
						loggingAction= AuditTrialAction.Modify;
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

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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
					if (objserviceContext.getMaxSeverity()== MessageType.WARNING){
						//Future dated transactions going for awaiting approvals..
						paymentStatusFlag=2;
						CreateRecurringPayService objCreateRecurringPayService = new CreateRecurringPayService();
						objCreateRecurringPayService.setConnection(conn);
						objCreateRecurringPayService.modifyCreateRecurringPayDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}

						// Making RTA Entries...
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
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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
						loggingAction= AuditTrialAction.Modify;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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

						// Making RTA Entries...
						EBWLogger.logDebug(this, "Calling the RTA Service ");
						PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails; 
						}
						EBWLogger.logDebug(this, "RTA Executed successfully ");

						//Call Notification Service 
						EBWLogger.logDebug(this, "Call Notification Service");
						if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
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

						//Reset Approval Details : Executed State 
						EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
						ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
						objResetApproverInfo.setConnection(conn);
						objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Reset the approver details successfully");

						// Get the latest transaction view details ..
						EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
						ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
						objexecTxnViewDetails.setConnection(conn);
						objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");
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
			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
	public ArrayList<Object> cancelPaymentTransfer(String stmntId,Object toObjects[], Boolean boolean1) throws SQLException,Exception
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

			//Get the business Date from the QZ_Dates View 
			EBWLogger.logDebug(this, "Getting the Business Date");
			GetQZBusinessDate getBusinessDt = new GetQZBusinessDate();
			getBusinessDt.setConnection(conn);
			getBusinessDt.getQzBusinessDate(txnDetails,objserviceContext);
			EBWLogger.logDebug(this, "Finished getting Business Date : ");

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
			userEntilements.checkFunctionalEntitlement(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				cancelPayOutDetails.add(objserviceContext);
				return cancelPayOutDetails; 
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
				userEntilements.checkIntAccEntitlement(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					cancelPayOutDetails.add(objserviceContext);
					return cancelPayOutDetails; 
				}
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)))
			{
				if(transactionStatus!=null && !transactionStatus.equalsIgnoreCase("4")) //Allow cancellation of Executed Transactions irrespective of external account status..
				{
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
			}

			//Validate Cancel Request...
			ValidateTransaction validateTxn = new ValidateTransaction();
			validateTxn.validateCancelRequest(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				cancelPayOutDetails.add(objserviceContext);
				return cancelPayOutDetails; 
			}

			//Check the Cut Off Time for the same date transactions..
			if(transactionStatus.equalsIgnoreCase("4")){
				EBWLogger.logDebug(this, "Checking the Cut Off Time for Executed Transactions..");
				ValidateCutOffTime objValidateCutOffTime = new ValidateCutOffTime();
				objValidateCutOffTime.setConnection(conn);
				objValidateCutOffTime.checkCutOffTime_Error(txnDetails,objserviceContext);
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
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
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
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P23";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P34";
					}
					else{
						notificationEventId = "NOTIFY_P11";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction= AuditTrialAction.Cancel;
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
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P23";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P34";
					}
					else{
						notificationEventId = "NOTIFY_P11";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction= AuditTrialAction.Cancel;
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

					// Making RTA Entries...
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
					{
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "RTA Executed successfully ");

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P23";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P34";
					}
					else{
						notificationEventId = "NOTIFY_P11";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
					{
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction= AuditTrialAction.Cancel;
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
				else if(transactionStatus.equalsIgnoreCase("4"))
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

					//Call the Payment HUB Service
					if(transferType!=null && !transferType.startsWith(ChkReqConstants.CHK)){
						EBWLogger.logDebug(this, "Calling Payments HUB Service ");
						PaymentsUtility.executePaymentHubCancel(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							cancelPayOutDetails.add(objserviceContext);
							return cancelPayOutDetails; 
						}
						EBWLogger.logDebug(this, "Payments created in HUB ");
					}

					//Making RTA Entries...
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
					{
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "RTA Executed successfully ");

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P23";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P34";
					}
					else{
						notificationEventId = "NOTIFY_P11";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
					{
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction= AuditTrialAction.Cancel;
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
					//Cancel Recurring Transfer with the status 20
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
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P24";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P35";
					}
					else{
						notificationEventId = "NOTIFY_P12";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction= AuditTrialAction.Cancel;
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
				else if(transactionStatus.equalsIgnoreCase("46"))
				{
					//Cancel Recurring Transfer with the status 20
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
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P24";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P35";
					}
					else{
						notificationEventId = "NOTIFY_P12";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction= AuditTrialAction.Cancel;
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
				else if(transactionStatus.equalsIgnoreCase("2") || transactionStatus.equalsIgnoreCase("80"))
				{
					//Cancel Recurring Transfer with the status 20
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

					// Making RTA Entries...
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "RTA Executed successfully ");

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P24";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P35";
					}
					else{
						notificationEventId = "NOTIFY_P12";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction= AuditTrialAction.Cancel;
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
				else if(transactionStatus.equalsIgnoreCase("4"))
				{
					//Cancel Recurring Transfer with the status 20
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

					//Call the Payment HUB Service
					if(transferType!=null && !transferType.startsWith(ChkReqConstants.CHK)){
						EBWLogger.logDebug(this, "Calling Payments HUB Service ");
						PaymentsUtility.executePaymentHubCancel(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							cancelPayOutDetails.add(objserviceContext);
							return cancelPayOutDetails; 
						}
						EBWLogger.logDebug(this, "Payments created in HUB ");
					}

					// Making RTA Entries...
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "RTA Executed successfully ");

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P24";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P35";
					}
					else{
						notificationEventId = "NOTIFY_P12";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction= AuditTrialAction.Cancel;
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

					//Canceling all subsequent transaction linked to recurring series...
					EBWLogger.logDebug(this, "Cancelling all subsequent transaction linked to recurring series");
					CancelRecurringPaymentService objCancelLinkedTxnns = new CancelRecurringPaymentService();
					objCancelLinkedTxnns.setConnection(conn);
					objCancelLinkedTxnns.cancelLinkedRecTxns(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						cancelPayOutDetails.add(objserviceContext);
						return cancelPayOutDetails; 
					}
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
			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
	public ArrayList<Object> skipNextTransfer(String stmntId,Object toObjects[], Boolean boolean1) throws SQLException,Exception
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
			userEntilements.checkFunctionalEntitlement(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				skipNextTransferOut.add(objserviceContext);
				return skipNextTransferOut; 
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
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
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
				{
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
				loggingAction= AuditTrialAction.Cancel;
				CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
				objCreatePaymentLogService.setConnection(conn);
				objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

				//Spawning the new child...
				EBWLogger.logDebug(this, "Creating the next child transaction in DB ");
				paymentStatusFlag=6;
				SkipNextTransfer objSkipNextTransfer = new SkipNextTransfer();
				objSkipNextTransfer.setConnection(conn);
				objSkipNextTransfer.skipNextTransfer(paymentStatusFlag,txnDetails,objserviceContext);
				if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}

				//Creating Check Details entries for the new child if spawned..
				if(objPaymentDetails.isChildTxnCreated() && transferType.startsWith(ChkReqConstants.CHK)){
					EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
					ChkTrxnHelperService objChkTrxnHelperSrvc = new ChkTrxnHelperService();
					objChkTrxnHelperSrvc.setConnection(conn);
					objChkTrxnHelperSrvc.insertChkTrxnDetails(txnDetails,true);
					EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
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
				if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
					notificationEventId = "NOTIFY_P25";
				}
				else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
					notificationEventId = "NOTIFY_P36";
				}
				else{
					notificationEventId = "NOTIFY_P13";
				}
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
				loggingAction= AuditTrialAction.Cancel;
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

				//Creating Check Details entries for the new child if spawned..
				if(objPaymentDetails.isChildTxnCreated() && transferType.startsWith(ChkReqConstants.CHK)){
					EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
					ChkTrxnHelperService objChkTrxnHelperSrvc = new ChkTrxnHelperService();
					objChkTrxnHelperSrvc.setConnection(conn);
					objChkTrxnHelperSrvc.insertChkTrxnDetails(txnDetails,true);
					EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
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
				if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
					notificationEventId = "NOTIFY_P25";
				}
				else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
					notificationEventId = "NOTIFY_P36";
				}
				else{
					notificationEventId = "NOTIFY_P13";
				}
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

			// Call RTA Delete only in case of transaction status is in Awaiting Approval....
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

				// Making RTA Entries...
				EBWLogger.logDebug(this, "Calling the RTA Service ");
				PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					skipNextTransferOut.add(objserviceContext);
					return skipNextTransferOut; 
				}
				EBWLogger.logDebug(this, "RTA Service executed successfully");

				//Call the CreatePaymentDetailsLog Service
				EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
				loggingAction= AuditTrialAction.Cancel;
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

				//Creating Check Details entries for the new child if spawned..
				if(objPaymentDetails.isChildTxnCreated() && transferType.startsWith(ChkReqConstants.CHK)){
					EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
					ChkTrxnHelperService objChkTrxnHelperSrvc = new ChkTrxnHelperService();
					objChkTrxnHelperSrvc.setConnection(conn);
					objChkTrxnHelperSrvc.insertChkTrxnDetails(txnDetails,true);
					EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
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
				if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
					notificationEventId = "NOTIFY_P25";
				}
				else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
					notificationEventId = "NOTIFY_P36";
				}
				else{
					notificationEventId = "NOTIFY_P13";
				}
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
			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
		skipNextTransferOut.add(objserviceContext);
		skipNextTransferOut.add(txnDetails);
		return skipNextTransferOut;
	}

	/**
	 * Function called On Load of the approve screen ...
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public ArrayList<Object> createApproveSubmit(String stmntId,Object toObjects[], Boolean boolean1) throws SQLException,Exception
	{
		EBWLogger.trace(this, "Starting createApproveSubmit method in Implementation class");
		boolean1 = Boolean.TRUE;
		conn = (Connection)serviceConnection;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		Date businessDate = new Date();
		int paymentStatusFlag =0;
		boolean isExternalAccount = false;
		String notificationEventId = "";
		String transferType = "";
		String frequencyFlag ="";
		int isRecurringFlag=0;
		String loggingAction ="";
		int childPayStatusFlag =0;
		HashMap txnDetails = new HashMap();
		ArrayList setPaymentOutputDetails = new ArrayList();
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		try
		{
			//Mapping the Payment details (Internal/External Cancel Transfers)...
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
			frequencyFlag = objPaymentDetails.getFrequency_Type();
			isRecurringFlag= ConvertionUtil.convertToint(frequencyFlag);
			businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());

			//Checking the status consistency only on Load of the screen...
			StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
			statusConsistency.setConnection(conn);
			boolean isTxnStatusValid = false;
			isTxnStatusValid = statusConsistency.verifyCurrentStatusChk(txnDetails,objserviceContext);
			if(!isTxnStatusValid){
				objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.STATUS_CONSISTENCY_FAILURE);
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}

			//Check if the Approval/Reject date is holiday or not...
			EBWLogger.logDebug(this, "Checking the Cut Off Time for the same day transactions..");
			CheckHoliday objCheckHoliday = new CheckHoliday();
			objCheckHoliday.setConnection(conn);
			objCheckHoliday.checkHolidayDate(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}

			//Check the Cut Off Time ...
			EBWLogger.logDebug(this, "Checking the Cut Off Time for the same day transactions..");
			ValidateCutOffTime objValidateCutOffTime = new ValidateCutOffTime();
			objValidateCutOffTime.setConnection(conn);
			objValidateCutOffTime.checkCutOffTime_Error(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}

			//User Entitlements Check for Verifying the internal and external accounts getting passed to the service are valid or not...
			UserEntilements userEntilements = new UserEntilements();
			userEntilements.setConnection(conn);
			boolean isAccSuspended=false;
			userEntilements.checkFunctionalEntitlement(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}

			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
				userEntilements.checkIntAccEntitlement(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
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

			//Check the business entitlements...
			userEntilements.checkOtherEntitlements_Approve(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}

			//Getting the Loan Account Details...
			EBWLogger.logDebug(this, "Validating Loan Acnt Details...");
			if(transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				PortfolioLoansService objGetLoanAcntInfo = new PortfolioLoansService();
				objGetLoanAcntInfo.getLoanAcntDetails(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Finished validating Loan Acnt Details...");
			}

			// Calling the Account View Service for FROM and TO accounts if applicable ... 
			EBWLogger.logDebug(this, "Executing  the Merlin Service");
			PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}
			EBWLogger.logDebug(this, "Finished executing the Merlin Service");

			// Call RTAB Service
			if(transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || (transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
			{  
				EBWLogger.logDebug(this, "Executing  the RTAB Service");
				PaymentsUtility.getAccountBalance(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				EBWLogger.logDebug(this, "Finished executing the RTAB Service");
			}

			//External account link check..
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
				//Call Business Rule Service 
				EBWLogger.logDebug(this, "Executing  the Business Rule Service ...");
				PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
				if(objserviceContext.getMaxSeverity() == MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				else if (objserviceContext.getMaxSeverity() == MessageType.ERROR){
					EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
					PaymentsUtility.executeBRLogging(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					EBWLogger.logDebug(this, "BR Warnings Logged successfully");

					// Call the CreatePaymentService With Status as "System Rejected"
					EBWLogger.logDebug(this, "Updating the record in the channels DB");
					paymentStatusFlag=52;
					ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
					objApproveTransactionService.setConnection(conn);
					objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}

					//Call limit check service through Business Rule for deleting the limit ....
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
						PaymentsUtility.processLimitRequest(txnDetails, objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails;
						}
					}

					// Making RTA Entries...
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					EBWLogger.logDebug(this, "RTA Executed successfully ");

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P27";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P38";
					}
					else{
						notificationEventId = "NOTIFY_P15";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}

					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					// Call the CreatePaymentDetailsLog Service with the status as "System Rejected"
					EBWLogger.logDebug(this, "Logging in the Channels DB");
					loggingAction= AuditTrialAction.Sys_Reject;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);

					//Reset Approval Details : System Rejected State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : System Rejected State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");

					//Checking the Account Eligible flag from BR and accordingly cancel all the transactions ....
					EBWLogger.logDebug(this, "Checking the Account Eligible flag from BR and accordingly cancel all the transactions");
					AccountEligible acntEligibility = new AccountEligible();
					acntEligibility.setConnection(conn);
					acntEligibility.validateAccEligibilty(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					EBWLogger.logDebug(this, "Checking the Account Eligible flag from BR and accordingly cancel all the transactions successfully");
				}
				else if((objserviceContext.getMaxSeverity()== MessageType.WARNING))
				{
					//BR Validation Log Service 
					EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
					PaymentsUtility.executeBRLogging(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					EBWLogger.logDebug(this, "BR Warnings Logged successfully");

					// Get the latest reason codes to display the same EBW Table..
					EBWLogger.logDebug(this, "Getting the ReasonCodes for the selected transaction..");
					GetReasonCodes objgetReasonCodes= new GetReasonCodes();
					objgetReasonCodes.setConnection(conn);
					objgetReasonCodes.getBrReasonCodes(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Finished getting the ReasonCodes for the selected transaction..");
				}
				else 
				{
					// Get the latest reason codes to display the same EBW Table..
					EBWLogger.logDebug(this, "Getting the ReasonCodes for the selected transaction..");
					GetReasonCodes objgetReasonCodes= new GetReasonCodes();
					objgetReasonCodes.setConnection(conn);
					objgetReasonCodes.getBrReasonCodes(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Finished getting the ReasonCodes for the selected transaction..");
				}
			}
			else if(isRecurringFlag==2)
			{
				EBWLogger.logDebug(this, "The processing transaction is RECURRING Transfer");

				//Call Business Rule Service 
				EBWLogger.logDebug(this, "Executing the BR Service ");
				PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Executed BR service");
				if(objserviceContext.getMaxSeverity() == MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setPaymentOutputDetails.add(objserviceContext);
					return setPaymentOutputDetails; 
				}
				else if (objserviceContext.getMaxSeverity() == MessageType.ERROR){
					EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
					PaymentsUtility.executeBRLogging(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					EBWLogger.logDebug(this, "BR Warnings Logged successfully");

					// Call the CreatePaymentService With Status as "System Rejected"
					EBWLogger.logDebug(this, "Updating the record in the channels DB");
					paymentStatusFlag=52;
					ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
					objApproveTransactionService.setConnection(conn);
					objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}

					//Call limit check service through Business Rule for deleting the limit ....
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
						PaymentsUtility.processLimitRequest(txnDetails, objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
							setPaymentOutputDetails.add(objserviceContext);
							return setPaymentOutputDetails;
						}
					}

					// Making RTA Entries...
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					EBWLogger.logDebug(this, "RTA Executed successfully ");

					//Call Notification Service ...
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P27";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P38";
					}
					else{
						notificationEventId = "NOTIFY_P15";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					// Call the CreatePaymentDetailsLog Service with the status and action as "System Rejected"
					EBWLogger.logDebug(this, "Loggin in the Channels DB");
					loggingAction= AuditTrialAction.Sys_Reject;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);

					//Reset Approval Details : System Rejected State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : System Rejected State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");

					//Checking the Account Eligible flag from BR and accordingly cancel all the transactions ....
					EBWLogger.logDebug(this, "Checking the Account Eligible flag from BR and accordingly cancel all the transactions");
					AccountEligible acntEligibility = new AccountEligible();
					acntEligibility.setConnection(conn);
					acntEligibility.validateAccEligibilty(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					EBWLogger.logDebug(this, "Checking the Account Eligible flag from BR and accordingly cancel all the transactions successfully");

					if(objPaymentDetails.isAccounEligible())
					{
						//Call the UpdateTxnParent Service to update the Parent Transaction and create next Child Transaction
						EBWLogger.logDebug(this, "Creating the next child transaction");
						UpdateTxnParentService objUpdateTxnParentService = new UpdateTxnParentService();
						objUpdateTxnParentService.setConnection(conn);
						objUpdateTxnParentService.setUpdateTxnParentDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Next child created successfully");

						//Creating Check Details entries for the new child if spawned..
						if(objPaymentDetails.isChildTxnCreated() && transferType.startsWith(ChkReqConstants.CHK)){
							EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
							ChkTrxnHelperService objChkTrxnHelperSrvc = new ChkTrxnHelperService();
							objChkTrxnHelperSrvc.setConnection(conn);
							objChkTrxnHelperSrvc.insertChkTrxnDetails(txnDetails,true);
							EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
						}

						//Call the CreatePaymentDetailsLog Service for child transaction logging.
						if(objPaymentDetails.isChildTxnCreated()){
							EBWLogger.logDebug(this, "Child Transaction Logging in the Channels DB (Created Status)");
							childPayStatusFlag = 6;
							CreatePaymentLogService objChildTxnLogging = new CreatePaymentLogService();
							objChildTxnLogging.setConnection(conn);
							objChildTxnLogging.setNextChildPaymentsLog(childPayStatusFlag,txnDetails,objserviceContext);
							EBWLogger.logDebug(this, "Child Transaction Logged in the Channels DB (Created Status)");
						}

						//Call the limit check create service through Business rule service for all the child ...
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
					}

				}
				else if((objserviceContext.getMaxSeverity()== MessageType.WARNING))
				{
					//BR Validation Log Service 
					EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
					PaymentsUtility.executeBRLogging(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setPaymentOutputDetails.add(objserviceContext);
						return setPaymentOutputDetails; 
					}
					EBWLogger.logDebug(this, "BR Warnings Logged successfully");

					// Get the latest reason codes to display the same EBW Table..
					EBWLogger.logDebug(this, "Getting the ReasonCodes for the selected transaction..");
					GetReasonCodes objgetReasonCodes= new GetReasonCodes();
					objgetReasonCodes.setConnection(conn);
					objgetReasonCodes.getBrReasonCodes(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Finished getting the ReasonCodes for the selected transaction..");

				}
				else {
					// Get the latest reason codes to display the same EBW Table..
					EBWLogger.logDebug(this, "Getting the ReasonCodes for the selected transaction..");
					GetReasonCodes objgetReasonCodes= new GetReasonCodes();
					objgetReasonCodes.setConnection(conn);
					objgetReasonCodes.getBrReasonCodes(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Finished getting the ReasonCodes for the selected transaction..");
				}
			}
			else {
				objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
				setPaymentOutputDetails.add(objserviceContext);
				return setPaymentOutputDetails; 
			}
			EBWLogger.logDebug(this, "Executed createApproveSubmit in ExceptionManagementService");
			EBWLogger.trace(this, "Finished with createApproveSubmit in ExceptionManagementService");

			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
		setPaymentOutputDetails.add(objserviceContext);
		setPaymentOutputDetails.add(txnDetails);
		return setPaymentOutputDetails;
	}

	/**
	 * Function for Approving the Payment or Check ..... 
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public ArrayList<Object> createApproveConfirm(String stmntId,Object toObjects[], Boolean boolean1) throws SQLException,Exception
	{
		EBWLogger.trace(this, "Starting createApproveConfirm method in Implementation class");
		boolean1 = Boolean.TRUE;
		conn = (Connection)serviceConnection;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		int paymentStatusFlag=0;
		String notificationEventId ="";
		boolean isExternalAccount = false;
		HashMap txnDetails = new HashMap();
		String transferType ="";
		String frequencyFlag ="";
		int isRecurringFlag=0;
		String loggingAction ="";
		int childPayStatusFlag =0;
		ArrayList<Object> setApprovePayOutDetails = new ArrayList<Object>();
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		Date paymentDate = new Date();
		Date businessDate = new Date();
		int previousTxnStatus = 0;
		try
		{
			//Mapping the Payment details (Internal/External Cancel Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setPaymentConfirmDetails(toObjects,objserviceContext);

			//Get the business Date from the QZ_Dates View 
			EBWLogger.logDebug(this, "Getting the Business Date");
			GetQZBusinessDate getBusinessDt = new GetQZBusinessDate();
			getBusinessDt.setConnection(conn);
			getBusinessDt.getQzBusinessDate(txnDetails,objserviceContext);
			EBWLogger.logDebug(this, "Finished getting Business Date : ");

			//Mapping the payment attributes...
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			paymentDate = ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate());
			businessDate = ConvertionUtil.convertToDate(objPaymentDetails.getBusiness_Date());
			transferType= objPaymentDetails.getTransfer_Type();
			frequencyFlag = objPaymentDetails.getFrequency_Type();
			isRecurringFlag= ConvertionUtil.convertToint(frequencyFlag);
			if(objPaymentDetails.getTxnPrevStatusCode()!=null){
				previousTxnStatus = new Integer(objPaymentDetails.getTxnPrevStatusCode()).intValue();
			}

			//Check if the Approval/Reject date is holiday or not...
			EBWLogger.logDebug(this, "Checking the Cut Off Time for the same day transactions..");
			CheckHoliday objCheckHoliday = new CheckHoliday();
			objCheckHoliday.setConnection(conn);
			objCheckHoliday.checkHolidayDate(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setApprovePayOutDetails.add(objserviceContext);
				return setApprovePayOutDetails; 
			}

			//Check the Cut Off Time ...
			EBWLogger.logDebug(this, "Checking the Cut Off Time for the same day transactions..");
			ValidateCutOffTime objValidateCutOffTime = new ValidateCutOffTime();
			objValidateCutOffTime.setConnection(conn);
			objValidateCutOffTime.checkCutOffTime_Error(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setApprovePayOutDetails.add(objserviceContext);
				return setApprovePayOutDetails; 
			}

			//User Entitlements Check for Verifying the internal and external accounts getting passed to the service are valid or not...
			UserEntilements userEntilements = new UserEntilements();
			userEntilements.setConnection(conn);
			boolean isAccSuspended=false;
			userEntilements.checkFunctionalEntitlement(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setApprovePayOutDetails.add(objserviceContext);
				return setApprovePayOutDetails; 
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
				userEntilements.checkIntAccEntitlement(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setApprovePayOutDetails.add(objserviceContext);
					return setApprovePayOutDetails; 
				}
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
				userEntilements.checkExtAccRoutingNum(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setApprovePayOutDetails.add(objserviceContext);
					return setApprovePayOutDetails; 
				}
				userEntilements.checkExtAccEntitlements(txnDetails,isAccSuspended,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setApprovePayOutDetails.add(objserviceContext);
					return setApprovePayOutDetails; 
				}
			}

			//Check the business entitlements...
			userEntilements.checkOtherEntitlements_Approve(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setApprovePayOutDetails.add(objserviceContext);
				return setApprovePayOutDetails; 
			}

			//Getting the Loan Account Details...
			EBWLogger.logDebug(this, "Validating Loan Acnt Details...");
			if(transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				PortfolioLoansService objGetLoanAcntInfo = new PortfolioLoansService();
				objGetLoanAcntInfo.getLoanAcntDetails(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Finished validating Loan Acnt Details...");
			}

			//Calling the Account View Service for FROM and TO accounts if applicable ... 
			EBWLogger.logDebug(this, "Executing  the Merlin Service");
			PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setApprovePayOutDetails.add(objserviceContext);
				return setApprovePayOutDetails; 
			}
			EBWLogger.logDebug(this, "Executed Merlin Service successfully ... ");

			//Call RTAB Service
			EBWLogger.logDebug(this, "Executing  the RTAB Service");
			if(transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || (transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN)))
			{  
				// Calling the RTAB Service..
				PaymentsUtility.getAccountBalance(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setApprovePayOutDetails.add(objserviceContext);
					return setApprovePayOutDetails; 
				}
			}
			EBWLogger.logDebug(this, "Executed RTAB Service");

			//If the Transaction is One-Time Immediate Transfer 
			if(isRecurringFlag==1)
			{
				//Call Business Rule Service 
				EBWLogger.logDebug(this, "The processing transaction is ONE-TIME Transfer");
				EBWLogger.logDebug(this, "Executing the Business Rule Service");
				PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
				if(objserviceContext.getMaxSeverity() == MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setApprovePayOutDetails.add(objserviceContext);
					return setApprovePayOutDetails; 
				}
				else if (objserviceContext.getMaxSeverity() == MessageType.ERROR){
					EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
					PaymentsUtility.executeBRLogging(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}
					EBWLogger.logDebug(this, "BR Warnings Logged successfully");

					// Call the CreatePaymentService With Status as "System Rejected"
					EBWLogger.logDebug(this, "Updating the record in the channels DB");
					paymentStatusFlag=52;
					ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
					objApproveTransactionService.setConnection(conn);
					objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}

					//Call limit check service through Business Rule for deleting the limit ....
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
						PaymentsUtility.processLimitRequest(txnDetails, objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
						{
							EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
							setApprovePayOutDetails.add(objserviceContext);
							return setApprovePayOutDetails;
						}
					}

					// Making RTA Entries...
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}
					EBWLogger.logDebug(this, "RTA Executed successfully ");

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P27";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P38";
					}
					else{
						notificationEventId = "NOTIFY_P15";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					// Call the CreatePaymentDetailsLog Service with the status as "System Rejected"
					EBWLogger.logDebug(this, "Logging in the Channels DB");
					loggingAction= AuditTrialAction.Sys_Reject;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);

					//Reset Approval Details : System Rejected State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : System Rejected State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");

					//Checking the Account Eligible flag from BR and accordingly cancel all the transactions ....
					EBWLogger.logDebug(this, "Checking the Account Eligible flag from BR and accordingly cancel all the transactions");
					AccountEligible acntEligibility = new AccountEligible();
					acntEligibility.setConnection(conn);
					acntEligibility.validateAccEligibilty(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}
					EBWLogger.logDebug(this, "Checking the Account Eligible flag from BR and accordingly cancel all the transactions successfully");
				}
				else if(objPaymentDetails.isNextApproval_Req()) //Checking the next_approver_req flag if set on Approve-PreConfirm....
				{
					//Call the BR_VALIDATION_LOG update service for updating the APPRVD_BY Column with the approved by name...
					UpdateBRValidationLog objUpdtBrValidation = new UpdateBRValidationLog();
					objUpdtBrValidation.setConnection(conn);
					objUpdtBrValidation.updateBRValidationLog(txnDetails);

					//Call the CreatePaymentService With Status as "Awaiting approval" and update the next approver role in the DS_PAY_TXNS..
					EBWLogger.logDebug(this, "Updating the record in the channels DB");
					paymentStatusFlag=2;
					ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
					objApproveTransactionService.setConnection(conn);
					objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}

					//External Account Link Check and creation only in case the previous transaction status is 2...
					if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE))
					{
						if(previousTxnStatus == 2) {
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setApprovePayOutDetails.add(objserviceContext);
								return setApprovePayOutDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}
					}

					// Making RTA Entries...
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}

					// Call the CreatePaymentDetailsLog Service with the status as "Approved"
					EBWLogger.logDebug(this, "Logging in the Channels DB");
					loggingAction= AuditTrialAction.Approve;
					paymentStatusFlag=2;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);

				}
				else
				{
					//If the transactions getting approved are same dated or less than the business date (considering the transaction expiry time)
					if(!paymentDate.after(businessDate))
					{
						EBWLogger.logDebug(this, "Updating the record in the channels DB");

						//Determining the transaction status based on the transaction type..
						if(transferType!=null && transferType.equalsIgnoreCase(ChkReqConstants.CHK_LOC)){
							paymentStatusFlag =103; //Awaiting Print...
						}
						else {
							paymentStatusFlag =4; //Executed..
						}

						//Call the CreatePaymentService With Status as "Executed" Or "Awaiting Print".
						ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
						objApproveTransactionService.setConnection(conn);
						objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setApprovePayOutDetails.add(objserviceContext);
							return setApprovePayOutDetails; 
						}

						//External Account Link Check and creation only in case the previous transaction status is 2...
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE))
						{
							if(previousTxnStatus == 2) {
								EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
								AccountLink objAccountLink= new AccountLink();
								objAccountLink.setConnection(serviceConnection);
								objAccountLink.checkAccountLink(txnDetails,objserviceContext);
								if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
									setApprovePayOutDetails.add(objserviceContext);
									return setApprovePayOutDetails; 
								}
								EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
							}
						}

						if(transferType!=null && !transferType.startsWith(ChkReqConstants.CHK))
						{
							// Call the Payment HUB Service for transaction other than CHK..
							EBWLogger.logDebug(this, "Calling Payments HUB Service ");
							PaymentsUtility.executePaymentsHub(txnDetails,objserviceContext);
							if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setApprovePayOutDetails.add(objserviceContext);
								return setApprovePayOutDetails; 
							}
							EBWLogger.logDebug(this, "Payments created in HUB ");

							// Call UpdatePaymentConfirmationNo Service	 for transaction other than CHK..
							EBWLogger.logDebug(this, "Updating the Payment Conf Number in the channels DB  ");
							UpdatePaymentConfNoService objUpdatePaymentConfNoService = new UpdatePaymentConfNoService();
							objUpdatePaymentConfNoService.setConnection(conn);
							objUpdatePaymentConfNoService.setUpdatePaymentConfNoService(txnDetails,objserviceContext);
							EBWLogger.logDebug(this, "Updated the Payment Conf Number in the channels DB  ");
						}
						else 
						{
							//Making RTA entries..
							EBWLogger.logDebug(this, "Calling the RTA Service ");
							PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
							if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setApprovePayOutDetails.add(objserviceContext);
								return setApprovePayOutDetails; 
							}
						}

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Loggin in the Channels DB");
						loggingAction= AuditTrialAction.Approve;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);

						//Update Transition date : Executed State 
						EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
						UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
						objUpdtTransitionDt.setConnection(conn);
						objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Updated the transition date successfully");

						//Reset Approval Details : Executed State 
						EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
						ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
						objResetApproverInfo.setConnection(conn);
						objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Reset the approver details successfully");

						//Call the BR_VALIDATION_LOG update service for updating the APPRVD_BY Column with the approved by name...
						UpdateBRValidationLog objUpdtBrValidation = new UpdateBRValidationLog();
						objUpdtBrValidation.setConnection(conn);
						objUpdtBrValidation.updateBRValidationLog(txnDetails);
					}
					else  // If the transaction getting approved are future dated..
					{
						//Call the ApproveTransactionService With Status as "Scheduled"
						EBWLogger.logDebug(this, "Updating the record in the channels DB");
						paymentStatusFlag=6;
						ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
						objApproveTransactionService.setConnection(conn);
						objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setApprovePayOutDetails.add(objserviceContext);
							return setApprovePayOutDetails; 
						}

						//External Account Link Check and creation only in case the previous transaction status is 2...
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE))
						{
							if(previousTxnStatus == 2) {
								EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
								AccountLink objAccountLink= new AccountLink();
								objAccountLink.setConnection(serviceConnection);
								objAccountLink.checkAccountLink(txnDetails,objserviceContext);
								if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
									setApprovePayOutDetails.add(objserviceContext);
									return setApprovePayOutDetails; 
								}
								EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
							}
						}

						//Reset Approval Details : Executed State 
						EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
						ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
						objResetApproverInfo.setConnection(conn);
						objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Reset the approver details successfully");

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Loggin in the Channels DB");
						loggingAction= AuditTrialAction.Approve;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);

						//Call the BR_VALIDATION_LOG update service for updating the APPRVD_BY Column with the approved by name...
						UpdateBRValidationLog objUpdtBrValidation = new UpdateBRValidationLog();
						objUpdtBrValidation.setConnection(conn);
						objUpdtBrValidation.updateBRValidationLog(txnDetails);
					}
				}
				EBWLogger.logDebug(this, "Executed approvePaymentsTransation in ExceptionManagementService");
				EBWLogger.trace(this, "Finished with approvePaymentsTransation in ExceptionManagementService");
			}
			else if(isRecurringFlag==2)
			{
				EBWLogger.logDebug(this, "The processing transaction is RECURRING Transfer");

				EBWLogger.logDebug(this, "Executing the BR Service ");
				PaymentsUtility.executeBRTransaction(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Executed BR service");
				if(objserviceContext.getMaxSeverity() == MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setApprovePayOutDetails.add(objserviceContext);
					return setApprovePayOutDetails; 
				}
				else if (objserviceContext.getMaxSeverity() == MessageType.ERROR){
					EBWLogger.logDebug(this, "Calling the BR Validation Log Service ");
					PaymentsUtility.executeBRLogging(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}
					EBWLogger.logDebug(this, "BR Warnings Logged successfully");

					// Call the CreatePaymentService With Status as "System Rejected"
					EBWLogger.logDebug(this, "Updating the record in the channels DB");
					paymentStatusFlag=52;
					ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
					objApproveTransactionService.setConnection(conn);
					objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}

					//Call limit check service through Business Rule for deleting the limit ....
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
						PaymentsUtility.processLimitRequest(txnDetails, objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE)
						{
							EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
							setApprovePayOutDetails.add(objserviceContext);
							return setApprovePayOutDetails;
						}
					}

					// Making RTA Entries...
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}
					EBWLogger.logDebug(this, "RTA Executed successfully ");

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P27";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P38";
					}
					else{
						notificationEventId = "NOTIFY_P15";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					// Call the CreatePaymentDetailsLog Service with the status and action as "System Rejected"
					EBWLogger.logDebug(this, "Logging in the Channels DB");
					loggingAction= AuditTrialAction.Sys_Reject;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);

					//Reset Approval Details : System Rejected State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : System Rejected State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");

					//Checking the Account Eligible flag from BR and accordingly cancel all the transactions ....
					EBWLogger.logDebug(this, "Checking the Account Eligible flag from BR and accordingly cancel all the transactions");
					AccountEligible acntEligibility = new AccountEligible();
					acntEligibility.setConnection(conn);
					acntEligibility.validateAccEligibilty(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}
					EBWLogger.logDebug(this, "Checking the Account Eligible flag from BR and accordingly cancel all the transactions successfully");

					if(objPaymentDetails.isAccounEligible())
					{
						//Call the UpdateTxnParent Service to update the Parent Transaction and create next Child Transaction
						EBWLogger.logDebug(this, "Creating the next child transaction");
						UpdateTxnParentService objUpdateTxnParentService = new UpdateTxnParentService();
						objUpdateTxnParentService.setConnection(conn);
						objUpdateTxnParentService.setUpdateTxnParentDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Next child created successfully");

						//Creating Check Details entries for the new child if spawned..
						if(objPaymentDetails.isChildTxnCreated() && transferType.startsWith(ChkReqConstants.CHK)){
							EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
							ChkTrxnHelperService objChkTrxnHelperSrvc = new ChkTrxnHelperService();
							objChkTrxnHelperSrvc.setConnection(conn);
							objChkTrxnHelperSrvc.insertChkTrxnDetails(txnDetails,true);
							EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
						}

						//Call the CreatePaymentDetailsLog Service for child transaction logging.
						if(objPaymentDetails.isChildTxnCreated()){
							EBWLogger.logDebug(this, "Child Transaction Logging in the Channels DB (Created Status)");
							childPayStatusFlag = 6;
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
								setApprovePayOutDetails.add(objserviceContext);
								return setApprovePayOutDetails; 
							}
							EBWLogger.logDebug(this, "Limit check service for future dated transactions");
						}
					}
				}
				else if(objPaymentDetails.isNextApproval_Req())
				{
					//Call the BR_VALIDATION_LOG update service for updating the APPRVD_BY Column with the approved by name...
					UpdateBRValidationLog objUpdtBrValidation = new UpdateBRValidationLog();
					objUpdtBrValidation.setConnection(conn);
					objUpdtBrValidation.updateBRValidationLog(txnDetails);

					//Call the CreatePaymentService With Status as "Awaiting approval" and update the next approver role in the DS_PAY_TXNS..
					EBWLogger.logDebug(this, "Updating the record in the channels DB");
					paymentStatusFlag=2;
					ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
					objApproveTransactionService.setConnection(conn);
					objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}

					//External Account Link Check and creation only in case the previous transaction status is 2...
					if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE))
					{
						if(previousTxnStatus == 2) {
							EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
							AccountLink objAccountLink= new AccountLink();
							objAccountLink.setConnection(serviceConnection);
							objAccountLink.checkAccountLink(txnDetails,objserviceContext);
							if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setApprovePayOutDetails.add(objserviceContext);
								return setApprovePayOutDetails; 
							}
							EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
						}
					}

					// Making RTA Entries...
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setApprovePayOutDetails.add(objserviceContext);
						return setApprovePayOutDetails; 
					}

					// Call the CreatePaymentDetailsLog Service with the status as "Approved"
					EBWLogger.logDebug(this, "Logging in the Channels DB");
					loggingAction=AuditTrialAction.Approve;
					paymentStatusFlag=2;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
				}
				else
				{
					//If the transactions getting approved are same dated or less than the business date (considering the transaction expiry time)
					if(!paymentDate.after(businessDate))
					{
						//Determining the txn status based on the transaction type..
						if(transferType!=null && transferType.equalsIgnoreCase(ChkReqConstants.CHK_LOC)){
							paymentStatusFlag =103; //Awaiting Print...
						}
						else {
							paymentStatusFlag =4; //Executed...
						}

						//Call the CreatePaymentService With Status as "Executed" Or "Awaiting Print".
						ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
						objApproveTransactionService.setConnection(conn);
						objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setApprovePayOutDetails.add(objserviceContext);
							return setApprovePayOutDetails; 
						}

						//External Account Link Check and creation only in case the previous transaction status is 2...
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE))
						{
							if(previousTxnStatus == 2) {
								EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
								AccountLink objAccountLink= new AccountLink();
								objAccountLink.setConnection(serviceConnection);
								objAccountLink.checkAccountLink(txnDetails,objserviceContext);
								if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
									setApprovePayOutDetails.add(objserviceContext);
									return setApprovePayOutDetails; 
								}
								EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
							}
						}

						if(transferType!=null && !transferType.startsWith(ChkReqConstants.CHK))
						{
							//Call the Payment HUB Service for transaction other than CHK..
							EBWLogger.logDebug(this, "Calling Payment Hub ");
							PaymentsUtility.executePaymentsHub(txnDetails,objserviceContext);
							if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setApprovePayOutDetails.add(objserviceContext);
								return setApprovePayOutDetails; 
							}
							EBWLogger.logDebug(this, "Payment created in HUB Successfully");

							//Call UpdatePaymentConfirmationNo Service for transaction other than CHK..
							EBWLogger.logDebug(this, "Updating the paymnet conf no in channel DB  ");
							UpdatePaymentConfNoService objUpdatePaymentConfNoService = new UpdatePaymentConfNoService();
							objUpdatePaymentConfNoService.setConnection(conn);
							objUpdatePaymentConfNoService.setUpdatePaymentConfNoService(txnDetails,objserviceContext);
						}
						else 
						{
							//Making RTA entries..
							EBWLogger.logDebug(this, "Calling the RTA Service ");
							PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
							if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
								setApprovePayOutDetails.add(objserviceContext);
								return setApprovePayOutDetails; 
							}
						}

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Logging in the channels DB  ");
						loggingAction=AuditTrialAction.Approve;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);

						//Call the BR_VALIDATION_LOG update service for updating the APPRVD_BY Column with the approved by name...
						UpdateBRValidationLog objUpdtBrValidation = new UpdateBRValidationLog();
						objUpdtBrValidation.setConnection(conn);
						objUpdtBrValidation.updateBRValidationLog(txnDetails);

						//Call the UpdateTxnParent Service to update the Parent Transaction and create next Child Transaction
						EBWLogger.logDebug(this, "Creating the next child transaction");
						UpdateTxnParentService objUpdateTxnParentService = new UpdateTxnParentService();
						objUpdateTxnParentService.setConnection(conn);
						objUpdateTxnParentService.setUpdateTxnParentDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Next child created successfully");

						//Creating Check Details entries for the new child if spawned..
						if(objPaymentDetails.isChildTxnCreated() && transferType.startsWith(ChkReqConstants.CHK)){
							EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
							ChkTrxnHelperService objChkTrxnHelperSrvc = new ChkTrxnHelperService();
							objChkTrxnHelperSrvc.setConnection(conn);
							objChkTrxnHelperSrvc.insertChkTrxnDetails(txnDetails,true);
							EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
						}

						//Call the CreatePaymentDetailsLog Service for child transaction logging.
						if(objPaymentDetails.isChildTxnCreated()){
							EBWLogger.logDebug(this, "Child Transaction Logging in the Channels DB (Created Status)");
							childPayStatusFlag = 6;
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
								setApprovePayOutDetails.add(objserviceContext);
								return setApprovePayOutDetails; 
							}
							EBWLogger.logDebug(this, "Limit check service for future dated transactions");
						}

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
					else //Transaction will be put back to the scheduled status on successful approval. 
					{
						//Update the transaction in the Channels DB in scheduled state.
						EBWLogger.logDebug(this, "Updating the Record in channels DB");
						paymentStatusFlag=6;
						ApproveTransactionService objApproveTransactionService = new ApproveTransactionService();
						objApproveTransactionService.setConnection(conn);
						objApproveTransactionService.approvePaymentDetails(paymentStatusFlag,txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							setApprovePayOutDetails.add(objserviceContext);
							return setApprovePayOutDetails; 
						}

						//External Account Link Check and creation only in case the previous transaction status is 2...
						if(transferType!=null && transferType.startsWith(TxnTypeCode.ACH_TYPE))
						{
							if(previousTxnStatus == 2) {
								EBWLogger.logDebug(this, "Checking the Account Link exist flag...");
								AccountLink objAccountLink= new AccountLink();
								objAccountLink.setConnection(serviceConnection);
								objAccountLink.checkAccountLink(txnDetails,objserviceContext);
								if(objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
									setApprovePayOutDetails.add(objserviceContext);
									return setApprovePayOutDetails; 
								}
								EBWLogger.logDebug(this, "Finished Checking the Account Link exist flag...");
							}
						}

						//Reset Approval Details : Scheduled State 
						EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
						ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
						objResetApproverInfo.setConnection(conn);
						objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
						EBWLogger.logDebug(this, "Reset the approver details successfully");

						//Call the CreatePaymentDetailsLog Service
						EBWLogger.logDebug(this, "Logging in the channels DB  ");
						loggingAction=AuditTrialAction.Approve;
						CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
						objCreatePaymentLogService.setConnection(conn);
						objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);

						//Call the BR_VALIDATION_LOG update service for updating the APPRVD_BY Column with the approved by name...
						UpdateBRValidationLog objUpdtBrValidation = new UpdateBRValidationLog();
						objUpdtBrValidation.setConnection(conn);
						objUpdtBrValidation.updateBRValidationLog(txnDetails);
					}
				}
				EBWLogger.logDebug(this, "Executed createApproveConfirm for One Time Immediate Transfer");
				EBWLogger.trace(this, "Finished with createApproveConfirm for One Time Immediate Transfer");
			}
			else {
				objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
				setApprovePayOutDetails.add(objserviceContext);
				return setApprovePayOutDetails; 
			}
			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
		setApprovePayOutDetails.add(objserviceContext);
		setApprovePayOutDetails.add(txnDetails);
		return setApprovePayOutDetails;
	}

	/**
	 * Function for Rejecting the Payment or Check ..... 
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public ArrayList<Object> rejectTransfer(String stmntId,Object toObjects[], Boolean boolean1) throws SQLException,Exception
	{
		EBWLogger.trace(this, "Starting rejectTransfer method in Implementation class");
		conn = (Connection)serviceConnection;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		boolean1 = Boolean.TRUE;
		int paymentStatusFlag=0;
		String notificationEventId = "";
		boolean isExternalAccount = false;
		String transferType = "";
		String frequencyFlag ="";
		int isRecurringFlag=0;
		String loggingAction ="";
		String transactionStatus = "";
		int childPayStatusFlag = 0;
		HashMap txnDetails = new HashMap();
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		ArrayList<Object> setRejectPayOutDetails = new ArrayList<Object>();
		try
		{
			//Mapping the Payment details (Internal/External Cancel Transfers)...
			MapPaymentInputDetails objMapConfirmDetails = new MapPaymentInputDetails();
			objMapConfirmDetails.setConnection(conn);
			txnDetails = objMapConfirmDetails.setPaymentConfirmDetails(toObjects,objserviceContext);

			//Get the business Date from the QZ_Dates View 
			EBWLogger.logDebug(this, "Getting the Business Date");
			GetQZBusinessDate getBusinessDt = new GetQZBusinessDate();
			getBusinessDt.setConnection(conn);
			getBusinessDt.getQzBusinessDate(txnDetails,objserviceContext);
			EBWLogger.logDebug(this, "Finished getting Business Date : ");

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
			userEntilements.checkFunctionalEntitlement(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setRejectPayOutDetails.add(objserviceContext);
				return setRejectPayOutDetails; 
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
				userEntilements.checkIntAccEntitlement(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setRejectPayOutDetails.add(objserviceContext);
					return setRejectPayOutDetails; 
				}
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)|| transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
				userEntilements.checkExtAccRoutingNum(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setRejectPayOutDetails.add(objserviceContext);
					return setRejectPayOutDetails; 
				}
				userEntilements.checkExtAccEntitlements(txnDetails,isAccSuspended,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setRejectPayOutDetails.add(objserviceContext);
					return setRejectPayOutDetails; 
				}
			}

			//Check the business entitlements...
			userEntilements.checkOtherEntitlements_Reject(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setRejectPayOutDetails.add(objserviceContext);
				return setRejectPayOutDetails; 
			}

			//Calling Account View Service(Only in case of ACH-OUT for BR limit check ...)
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)))
			{
				EBWLogger.logDebug(this, "Executing the Merlin Service..");
				PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setRejectPayOutDetails.add(objserviceContext);
					return setRejectPayOutDetails; 
				}
				EBWLogger.logDebug(this, "Executed the Merlin Service..");
			}

			if(isRecurringFlag==1)
			{
				//Scheduled Status 
				if(transactionStatus!=null && transactionStatus.equalsIgnoreCase("6"))
				{
					// Cancel the One time Transfer with the status 20 and create the log in the DS_PAY_TXNS
					paymentStatusFlag=44; //Not Approved status
					CancelPaymentService objCancelPaymentService = new CancelPaymentService();
					objCancelPaymentService.setConnection(conn);
					objCancelPaymentService.cancelOneTimeTransfer(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setRejectPayOutDetails.add(objserviceContext);
						return setRejectPayOutDetails; 
					}

					//Call limit check service through Business Rule for deleting the limit ....
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
						PaymentsUtility.processLimitRequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
							setRejectPayOutDetails.add(objserviceContext);
							return setRejectPayOutDetails;
						}
					}

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P27";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P38";
					}
					else {
						notificationEventId = "NOTIFY_P15";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setRejectPayOutDetails.add(objserviceContext);
						return setRejectPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction= AuditTrialAction.Reject;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

					//Reset Approval Details :  Not Approved State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : Not Approved State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");
				}
				// Awaiting Approval
				else if(transactionStatus!=null && (transactionStatus.equalsIgnoreCase("2") || transactionStatus.equalsIgnoreCase("80")))
				{
					// Cancel the One time Transfer with the status 20 and create the log in the DS_PAY_TXNS
					paymentStatusFlag=44; //Not Approved status
					CancelPaymentService objCancelPaymentService = new CancelPaymentService();
					objCancelPaymentService.setConnection(conn);
					objCancelPaymentService.cancelOneTimeTransfer(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setRejectPayOutDetails.add(objserviceContext);
						return setRejectPayOutDetails; 
					}

					//Call limit check service through Business Rule for deleting the limit ....
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
						PaymentsUtility.processLimitRequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
							setRejectPayOutDetails.add(objserviceContext);
							return setRejectPayOutDetails;
						}
					}

					// Making RTA Entries...
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setRejectPayOutDetails.add(objserviceContext);
						return setRejectPayOutDetails; 
					}

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P27";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P38";
					}
					else{
						notificationEventId = "NOTIFY_P15";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setRejectPayOutDetails.add(objserviceContext);
						return setRejectPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction=AuditTrialAction.Reject;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

					//Reset Approval Details :  Not Approved State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : Not Approved State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");
				}
				else {
					MSCommonUtils.pushTxnVersionMismatch(objserviceContext);
					setRejectPayOutDetails.add(objserviceContext);
					return setRejectPayOutDetails; 
				}
				EBWLogger.logDebug(this, "Executed cancelPaymentTransfer for One time transfer");
				EBWLogger.trace(this, "Finished with cancelPaymentTransfer for One time transfer");
			}
			else if(isRecurringFlag==2)
			{
				if(transactionStatus!=null && transactionStatus.equalsIgnoreCase("6"))
				{
					//Skip Next Transfers for the Recurring Payment
					paymentStatusFlag=44; //Not Approved status
					EBWLogger.logDebug(this, "Canceling the Existing Record in DB and creating the log ");
					CancelPaymentService objCancelPaymentService = new CancelPaymentService();
					objCancelPaymentService.setConnection(conn);
					objCancelPaymentService.cancelOneTimeTransfer(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setRejectPayOutDetails.add(objserviceContext);
						return setRejectPayOutDetails; 
					}

					//Call limit check service through Business Rule for deleting the limit ....
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
						PaymentsUtility.processLimitRequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
							setRejectPayOutDetails.add(objserviceContext);
							return setRejectPayOutDetails;
						}
					}

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction= AuditTrialAction.Reject;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

					EBWLogger.logDebug(this, " Creating the next child transaction in DB ");
					paymentStatusFlag=6;
					SkipNextTransfer objSkipNextTransfer = new SkipNextTransfer();
					objSkipNextTransfer.setConnection(conn);
					objSkipNextTransfer.skipNextTransfer(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setRejectPayOutDetails.add(objserviceContext);
						return setRejectPayOutDetails; 
					}

					//Creating Check Details entries for the new child if spawned..
					if(objPaymentDetails.isChildTxnCreated() && transferType.startsWith(ChkReqConstants.CHK)){
						EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
						ChkTrxnHelperService objChkTrxnHelperSrvc = new ChkTrxnHelperService();
						objChkTrxnHelperSrvc.setConnection(conn);
						objChkTrxnHelperSrvc.insertChkTrxnDetails(txnDetails,true);
						EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
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
						PaymentsUtility.processLimitRequest(txnDetails, objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "limit check Service failed for child future dated transactions");
							setRejectPayOutDetails.add(objserviceContext);
							return setRejectPayOutDetails; 
						}
						EBWLogger.logDebug(this, "Limit check service for future dated transactions");
					}

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P27";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P38";
					}
					else{
						notificationEventId = "NOTIFY_P15";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setRejectPayOutDetails.add(objserviceContext);
						return setRejectPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Reset Approval Details :  Not Approved State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : Not Approved State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");
				}
				// Call RTA Delete only in case of transaction status is in Awaiting Approval....
				else if(transactionStatus!=null && (transactionStatus.equalsIgnoreCase("2") || transactionStatus.equalsIgnoreCase("80")))
				{
					//Skip Next Transfers for the Recurring Payment
					EBWLogger.logDebug(this, "Canceling the Existing Record in DB and creating the log ");
					paymentStatusFlag=44; //Not Approved status
					CancelPaymentService objCancelPaymentService = new CancelPaymentService();
					objCancelPaymentService.setConnection(conn);
					objCancelPaymentService.cancelOneTimeTransfer(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setRejectPayOutDetails.add(objserviceContext);
						return setRejectPayOutDetails; 
					}

					//Call limit check service through Business Rule for deleting the limit ....
					if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						EBWLogger.logDebug(this, "Calling BusinessRule Service limit check for delete case..");
						PaymentsUtility.processLimitRequest(txnDetails,objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "BusinessRule Limit check Service encountered Hard Error or Technical Failure");
							setRejectPayOutDetails.add(objserviceContext);
							return setRejectPayOutDetails;
						}
					}

					// Making RTA Entries...
					EBWLogger.logDebug(this, "Calling the RTA Service ");
					PaymentsUtility.processRTARequest(txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setRejectPayOutDetails.add(objserviceContext);
						return setRejectPayOutDetails; 
					}
					EBWLogger.logDebug(this, "RTA Service executed successfully");

					//Call the CreatePaymentDetailsLog Service
					EBWLogger.logDebug(this, "Transaction Logging in the Channels DB");
					loggingAction= AuditTrialAction.Reject;
					CreatePaymentLogService objCreatePaymentLogService = new CreatePaymentLogService();
					objCreatePaymentLogService.setConnection(conn);
					objCreatePaymentLogService.setCreatePaymentDetailsLog(paymentStatusFlag,loggingAction,txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Transaction Logged in the Channels DB");

					//Skipping the Next transfer and creating the next child if the recurring exists..
					EBWLogger.logDebug(this, " Creating the next child transaction in DB ");
					paymentStatusFlag=6;
					SkipNextTransfer objSkipNextTransfer = new SkipNextTransfer();
					objSkipNextTransfer.setConnection(conn);
					objSkipNextTransfer.skipNextTransfer(paymentStatusFlag,txnDetails,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setRejectPayOutDetails.add(objserviceContext);
						return setRejectPayOutDetails; 
					}

					//Creating Check Details entries for the new child if spawned..
					if(objPaymentDetails.isChildTxnCreated() && transferType.startsWith(ChkReqConstants.CHK)){
						EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
						ChkTrxnHelperService objChkTrxnHelperSrvc = new ChkTrxnHelperService();
						objChkTrxnHelperSrvc.setConnection(conn);
						objChkTrxnHelperSrvc.insertChkTrxnDetails(txnDetails,true);
						EBWLogger.logDebug(this, "Chk Details transaction in the Channels DB (Created Status)");
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
						PaymentsUtility.processLimitRequest(txnDetails, objserviceContext);
						if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
							EBWLogger.logDebug(this, "limit check Service failed for child future dated transactions");
							setRejectPayOutDetails.add(objserviceContext);
							return setRejectPayOutDetails; 
						}
						EBWLogger.logDebug(this, "Limit check service for future dated transactions");
					}

					//Call Notification Service 
					EBWLogger.logDebug(this, "Call Notification Service");
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						notificationEventId = "NOTIFY_P27";
					}
					else if(transferType!=null && transferType.startsWith(ChkReqConstants.CHK)){
						notificationEventId = "NOTIFY_P38";
					}
					else{
						notificationEventId = "NOTIFY_P15";
					}
					PaymentsUtility.sendNotificationDetails(txnDetails,notificationEventId,isExternalAccount,objserviceContext);
					if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
						setRejectPayOutDetails.add(objserviceContext);
						return setRejectPayOutDetails; 
					}
					EBWLogger.logDebug(this, "Notification Service Executed successfully");

					//Reset Approval Details :  Not Approved State 
					EBWLogger.logDebug(this, "Call ResetApprovalDetails Service");
					ResetApprovalDetails objResetApproverInfo = new ResetApprovalDetails();
					objResetApproverInfo.setConnection(conn);
					objResetApproverInfo.clearApproverDetails(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Reset the approver details successfully");

					//Update Transition date : Not Approved State 
					EBWLogger.logDebug(this, "Call UpdateTransitionDate Service");
					UpdateTransitionDate objUpdtTransitionDt = new UpdateTransitionDate();
					objUpdtTransitionDt.setConnection(conn);
					objUpdtTransitionDt.setCurTxnTransitionDate(txnDetails,objserviceContext);
					EBWLogger.logDebug(this, "Updated the transition date successfully");
				}
				else {
					MSCommonUtils.pushTxnVersionMismatch(objserviceContext);
					setRejectPayOutDetails.add(objserviceContext);
					return setRejectPayOutDetails; 
				}
			}
			else {
				objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
				setRejectPayOutDetails.add(objserviceContext);
				return setRejectPayOutDetails; 
			}
			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
		setRejectPayOutDetails.add(objserviceContext);
		setRejectPayOutDetails.add(txnDetails);
		return setRejectPayOutDetails;
	}

	/**
	 * Getting the Transaction view details when clicked on View from the List Window...
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Object> getTxnViewDetails(String stmntId,Object toObjects[], Boolean boolean1) throws SQLException,Exception
	{
		EBWLogger.trace(this, "Starting getTxnViewDetails method in Implementation class");
		conn = (Connection)serviceConnection;
		ServiceContext objserviceContext = new ServiceContext();
		objserviceContext.setConnection(conn);
		boolean1 = Boolean.TRUE;
		HashMap txnDetails = new HashMap();
		ArrayList<Object> setTxnViewDetails = new ArrayList<Object>();
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		boolean isStatusChkReq = false;
		String transferType = "";
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
			isStatusChkReq = objPaymentDetails.isStatusChkReq();
			transferType = objPaymentDetails.getTransfer_Type();

			//Checking the status consistency only on Load of the screen from Approval Queue List Screen...
			if(isStatusChkReq){
				StatusConsistencyChk statusConsistency = new StatusConsistencyChk();
				statusConsistency.setConnection(conn);
				boolean isTxnStatusValid = false;
				isTxnStatusValid = statusConsistency.verifyCurrentStatusChk(txnDetails,objserviceContext);
				if(!isTxnStatusValid){
					objserviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.STATUS_CONSISTENCY_FAILURE);
					setTxnViewDetails.add(objserviceContext);
					return setTxnViewDetails; 
				}
			}

			//Get the business Date from the QZ_Dates View 
			EBWLogger.logDebug(this, "Getting the Business Date");
			GetQZBusinessDate getBusinessDt = new GetQZBusinessDate();
			getBusinessDt.setConnection(conn);
			getBusinessDt.getQzBusinessDate(txnDetails,objserviceContext);

			//User Entitlement Check for Verifying the internal and external accounts getting passed to the service are valid or not...
			UserEntilements userEntilements = new UserEntilements();
			userEntilements.setConnection(conn);
			userEntilements.checkFunctionalEntitlement(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setTxnViewDetails.add(objserviceContext);
				return setTxnViewDetails; 
			}
			if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
				userEntilements.checkIntAccEntitlement(txnDetails,objserviceContext);
				if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
					setTxnViewDetails.add(objserviceContext);
					return setTxnViewDetails; 
				}
			}

			//Getting the Loan Account Details...
			EBWLogger.logDebug(this, "Validating Loan Acnt Details...");
			if(transferType.equalsIgnoreCase(TxnTypeCode.PORTFOLIO_LOAN)){
				PortfolioLoansService objGetLoanAcntInfo = new PortfolioLoansService();
				objGetLoanAcntInfo.getLoanAcntDetails(txnDetails,objserviceContext);
				EBWLogger.logDebug(this, "Finished validating Loan Acnt Details...");
			}

			//Calling the Account view Service for FROM and TO accounts if applicable ... 
			EBWLogger.logDebug(this, "Executing the Merlin Service..");
			PaymentsUtility.getAccountDetails(txnDetails,objserviceContext);
			if (objserviceContext.getMaxSeverity()== MessageType.CRITICAL || objserviceContext.getMaxSeverity() == MessageType.SEVERE){
				setTxnViewDetails.add(objserviceContext);
				return setTxnViewDetails; 
			}
			EBWLogger.logDebug(this, "Executed the Merlin Service..");

			// Get the latest transaction view details ..
			EBWLogger.logDebug(this, "Getting the Transaction View Details for the selected transaction..");
			ExecuteTxnViewDetails objexecTxnViewDetails= new ExecuteTxnViewDetails();
			objexecTxnViewDetails.setConnection(conn);
			objexecTxnViewDetails.executeTxnViewDetails(txnDetails,objserviceContext);
			EBWLogger.logDebug(this, "Finished getting the Transaction View Details for the selected transaction..");

			((Connection)serviceConnection).commit();
			if(objserviceContext.isRTACommitReq())
			{
				EBWLogger.logDebug(this, "Calling RTA Commit");
				DBProcedureChannel.commit();
			}
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
		setTxnViewDetails.add(objserviceContext);
		setTxnViewDetails.add(txnDetails);
		return setTxnViewDetails;
	}
}