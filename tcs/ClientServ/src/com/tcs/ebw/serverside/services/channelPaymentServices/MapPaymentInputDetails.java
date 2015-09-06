package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.PortfolioLoanAccount;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MapPaymentInputDetails extends DatabaseService
{
	/**
	 * Mapping the payment details on screen load (Internal/External)
	 * @param toObjects
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public HashMap setPaymentOnLoadDetails(Object toObjects[],ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Starting Internal/External Transfer On Load Event"); 
		HashMap objLoadPaymentDetails = new HashMap();
		Collections.synchronizedMap(objLoadPaymentDetails);
		try 
		{
			MSUser_DetailsTO objMSDetails = new MSUser_DetailsTO();
			ArrayList objEligibleAccInfo = new ArrayList();

			// Getting the Internal Account Related Details...
			for(int i=0;i<toObjects.length;i++){
				if(toObjects[i] instanceof MSUser_DetailsTO){
					objMSDetails = (MSUser_DetailsTO)toObjects[i];
				}
				if(toObjects[i] instanceof ArrayList){
					objEligibleAccInfo = (ArrayList)toObjects[i];
				}
			}
			objLoadPaymentDetails.put("MSUserDetails",objMSDetails);
			objLoadPaymentDetails.put("BlockedDisplayInput",objEligibleAccInfo);
			objLoadPaymentDetails.put("PLAInput",objEligibleAccInfo);
		} 
		catch(Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished setPaymentOnLoadDetails");
		return objLoadPaymentDetails;
	}

	/**
	 * Mapping the payment details on MS Account selection. 
	 * @param toObjects
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public HashMap setOnAccSelectDetails(Object toObjects[],ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Starting Internal/External Transfer On Load Event"); 
		HashMap objOnAccSelectDetails = new HashMap();
		Collections.synchronizedMap(objOnAccSelectDetails);
		try 
		{
			MSUser_DetailsTO objMSDetails = new MSUser_DetailsTO();
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			PortfolioLoanAccount objLoanAcntDetails = new PortfolioLoanAccount();

			// Getting the Internal Account Related Details...
			for(int i=0;i<toObjects.length;i++){
				if(toObjects[i] instanceof MSUser_DetailsTO){
					objMSDetails = (MSUser_DetailsTO)toObjects[i];
				}
				if(toObjects[i] instanceof FromMSAcc_DetailsTO){
					objFromMSAcc_Details = (FromMSAcc_DetailsTO)toObjects[i];
				}

				if(toObjects[i] instanceof PortfolioLoanAccount){
					objLoanAcntDetails = (PortfolioLoanAccount)toObjects[i];
				}
			}
			objOnAccSelectDetails.put("MSUserDetails",objMSDetails);
			objOnAccSelectDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			objOnAccSelectDetails.put("LoanAccountDetails",objLoanAcntDetails);
		} 
		catch(Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished setOnAccSelectDetails");
		return objOnAccSelectDetails;
	}

	/**
	 * Mapping the payment details on Create submit details..
	 * @param toObjects
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public HashMap setCreatePaySubmitDetails(Object toObjects[],ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Starting Internal/External Transfer Submit Event");
		HashMap objSubPaymentDetails = new HashMap();
		Collections.synchronizedMap(objSubPaymentDetails);
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			MSUser_DetailsTO objMSDetails = new MSUser_DetailsTO();

			// Getting the Internal Account Related Details...
			for(int i=0;i<toObjects.length;i++){
				if(toObjects[i] instanceof PaymentDetailsTO){
					objPaymentDetails = (PaymentDetailsTO)toObjects[i];
				}
				if(toObjects[i] instanceof FromMSAcc_DetailsTO){
					objFromMSAcc_Details = (FromMSAcc_DetailsTO)toObjects[i];
				}
				if(toObjects[i] instanceof ToMSAcc_DetailsTO){
					objToMSAcc_Details = (ToMSAcc_DetailsTO)toObjects[i];
				}
				if(toObjects[i] instanceof MSUser_DetailsTO){
					objMSDetails = (MSUser_DetailsTO)toObjects[i];
				}
			}

			//Payment Attributes mappings..
			String transferType= objPaymentDetails.getTransfer_Type();

			objSubPaymentDetails.put("PaymentDetails",objPaymentDetails);
			objSubPaymentDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			objSubPaymentDetails.put("MSToAccDetails",objToMSAcc_Details);
			objSubPaymentDetails.put("MSUserDetails",objMSDetails);

			//Call the External Account Service to get the External Accounts Information if any and populating the external account details accordingly..
			if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				EBWLogger.logDebug(this, "Call the External Account Service to get the External Accounts Information if any ");
				GetExternalAccountsDetails objgetExternalAccountsDetails = new GetExternalAccountsDetails();
				objgetExternalAccountsDetails.setConnection(serviceConnection);
				objgetExternalAccountsDetails.getExternalPayeeDetails((HashMap)objSubPaymentDetails,serviceContext);
			}

			// Get the Transaction details for all the transactions only and only during edit....
			if(objPaymentDetails.isTxnModified())
			{
				EBWLogger.logDebug(this, "Getting the Modified Record details from the channels");
				GetChannelPaymentDetails objgetChannelPaymentDetails = new GetChannelPaymentDetails();
				objgetChannelPaymentDetails.setConnection(serviceConnection);
				objgetChannelPaymentDetails.selectPaymentDetails(objSubPaymentDetails,serviceContext);

				//Payment attributes details..
				String frequencyType= objPaymentDetails.getFrequency_Type();

				//Getting the recurring transfer details only in case of the transfer is recurring...
				if(frequencyType!=null && frequencyType.equalsIgnoreCase("2")){
					objgetChannelPaymentDetails.getRecurringTransferDetails(objSubPaymentDetails,serviceContext);
				}
			}
		} 
		catch(Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished setCreatePaySubmitDetails");
		return objSubPaymentDetails;
	}

	/**
	 * Mapping the payment details on create confirm of a transfers...
	 * @param toObjects
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public HashMap setCreatePayConfirmDetails(Object toObjects[],ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Starting Internal/External Transfer Confirm Event"); 
		HashMap objConfirmPaymentDetails = new HashMap();
		Collections.synchronizedMap(objConfirmPaymentDetails);
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			MSUser_DetailsTO objMSDetails = new MSUser_DetailsTO();

			// Getting the Internal Account Related Details...
			for(int i=0;i<toObjects.length;i++){
				if(toObjects[i] instanceof PaymentDetailsTO){
					objPaymentDetails = (PaymentDetailsTO)toObjects[i];
				}
				if(toObjects[i] instanceof FromMSAcc_DetailsTO){
					objFromMSAcc_Details = (FromMSAcc_DetailsTO)toObjects[i];
				}
				if(toObjects[i] instanceof ToMSAcc_DetailsTO){
					objToMSAcc_Details = (ToMSAcc_DetailsTO)toObjects[i];
				}
				if(toObjects[i] instanceof MSUser_DetailsTO){
					objMSDetails = (MSUser_DetailsTO)toObjects[i];
				}
			}

			//Payment Attributes mappings..
			String transferType= objPaymentDetails.getTransfer_Type();

			objConfirmPaymentDetails.put("PaymentDetails",objPaymentDetails);
			objConfirmPaymentDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			objConfirmPaymentDetails.put("MSToAccDetails",objToMSAcc_Details);
			objConfirmPaymentDetails.put("MSUserDetails",objMSDetails);

			//Call the External Account Service to get the External Accounts Information if any and populating the external account details accordingly..
			if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				EBWLogger.logDebug(this, "Call the External Account Service to get the External Accounts Information if any ");
				GetExternalAccountsDetails objgetExternalAccountsDetails = new GetExternalAccountsDetails();
				objgetExternalAccountsDetails.setConnection(serviceConnection);
				objgetExternalAccountsDetails.getExternalPayeeDetails((HashMap)objConfirmPaymentDetails,serviceContext);
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.trace(this, "Finished setCreatePayConfirmDetails");
		return objConfirmPaymentDetails;
	}

	/**
	 * Mapping the payment details on the Payment confirmation details in case of Edit , cancel , skip , approve...
	 * @param toObjects
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public HashMap setPaymentConfirmDetails(Object toObjects[],ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Starting Internal/External Transfer Edit Confirm Event"); 
		HashMap objEditConfirmPaymentDetails = new HashMap();
		Collections.synchronizedMap(objEditConfirmPaymentDetails);
		try 
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			MSUser_DetailsTO objMSDetails = new MSUser_DetailsTO();

			// Getting the Internal Account Related Details...
			for(int i=0;i<toObjects.length;i++){
				if(toObjects[i] instanceof PaymentDetailsTO){
					objPaymentDetails = (PaymentDetailsTO)toObjects[i];
				}
				if(toObjects[i] instanceof FromMSAcc_DetailsTO){
					objFromMSAcc_Details = (FromMSAcc_DetailsTO)toObjects[i];
				}
				if(toObjects[i] instanceof ToMSAcc_DetailsTO){
					objToMSAcc_Details = (ToMSAcc_DetailsTO)toObjects[i];
				}
				if(toObjects[i] instanceof MSUser_DetailsTO){
					objMSDetails = (MSUser_DetailsTO)toObjects[i];
				}
			}

			objEditConfirmPaymentDetails.put("PaymentDetails",objPaymentDetails);
			objEditConfirmPaymentDetails.put("MSFromAccDetails",objFromMSAcc_Details);
			objEditConfirmPaymentDetails.put("MSToAccDetails",objToMSAcc_Details);
			objEditConfirmPaymentDetails.put("MSUserDetails",objMSDetails);

			// Get the Transaction details for all the updated transaction in the executed status. 
			EBWLogger.logDebug(this, "Getting the Modified Record details from the channels");
			GetChannelPaymentDetails objgetChannelPaymentDetails = new GetChannelPaymentDetails();
			objgetChannelPaymentDetails.setConnection(serviceConnection);
			objgetChannelPaymentDetails.selectPaymentDetails(objEditConfirmPaymentDetails,serviceContext);

			//Payment Attributes mappings from the details fetched..
			String transferType= objPaymentDetails.getTransfer_Type();
			String frequencyType= objPaymentDetails.getFrequency_Type();

			//Getting the recurring transfer details only in case of the transfer is recurring...
			if(frequencyType!=null && frequencyType.equalsIgnoreCase("2")){
				objgetChannelPaymentDetails.getRecurringTransferDetails(objEditConfirmPaymentDetails,serviceContext);
			}

			//Call the External Account Service to get the External Accounts Information if any and populating the external account details accordingly..
			if(transferType!=null && transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				EBWLogger.logDebug(this, "Call the External Account Service to get the External Accounts Information if any ");
				GetExternalAccountsDetails objgetExternalAccountsDetails = new GetExternalAccountsDetails();
				objgetExternalAccountsDetails.setConnection(serviceConnection);
				objgetExternalAccountsDetails.getExternalPayeeDetails((HashMap)objEditConfirmPaymentDetails,serviceContext);
			}
		} 
		catch (Exception exception){
			throw exception;
		}
		EBWLogger.trace(this, "Finished setPaymentConfirmDetails");
		return objEditConfirmPaymentDetails;
	}
}

