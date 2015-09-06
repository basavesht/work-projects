package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.HashMap;

import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSAcntPlatingDetails;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;
import com.tcs.ebw.serverside.services.DatabaseService;
import com.tcs.ebw.serverside.services.PaymentsUtility;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       23-09-2011        P3-B            PLA  
 * **********************************************************
 */
public class AccountProperties extends DatabaseService 
{ 
	/**
	 * Debit_Account Plating info...
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void getDebitAcountProps(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//From account details..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//User details..
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			String transferType = objPaymentDetails.getTransfer_Type();
			if(!objPaymentDetails.isDebitAcntDataInSession()) 
			{
				//Input Mapping for the account plating details...
				MSAcntPlatingDetails objMSAccPlatingDetails = new MSAcntPlatingDetails();
				objMSAccPlatingDetails.setOffice(objFromMSAcc_Details.getOfficeNumber());
				objMSAccPlatingDetails.setAccount_number(objFromMSAcc_Details.getAccountNumber());
				objMSAccPlatingDetails.setFa(objFromMSAcc_Details.getFaNumber());
				objMSAccPlatingDetails.setUser_id(objMSUserDetails.getRcafId());

				//Putting the input object in the HashMap...
				txnDetails.put("MSAcntPlatingInDetails",objMSAccPlatingDetails);

				//Calling account plating details...
				PaymentsUtility.getAccountPlatingInfo(txnDetails,serviceContext);
				if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
					return;
				}

				//Output extraction from account plating response...
				AccountPlating acntPlating = new AccountPlating();
				if(txnDetails.containsKey("AcntPlatingOutputDetails")){
					acntPlating = (AccountPlating)txnDetails.get("AcntPlatingOutputDetails");
				}

				//Updating the From/To account details with the Hashed UniqueIds for validating external account owner..
				if(acntPlating!=null){
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.INTERNAL) || transferType.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL) || transferType.startsWith(ChkReqConstants.CHK) || transferType.startsWith(TxnTypeCode.PORTFOLIO_LOAN))){
						objFromMSAcc_Details.setKeyClientId(MSCommonUtils.extractKeyClientId(acntPlating));
						objFromMSAcc_Details.setAccountPlating(acntPlating);
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Credit_Account Plating info...
	 * @param txnDetails
	 * @param serviceContext
	 * @throws Exception
	 */
	public void getCreditAcountProps(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		try 
		{
			//Payment Output Details .....
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//User details..
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//To account details..
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			String transferType = objPaymentDetails.getTransfer_Type();
			if(!objPaymentDetails.isCreditAcntDataInSession()) 
			{
				//Input Mapping for the account plating details...
				MSAcntPlatingDetails objMSAccPlatingDetails = new MSAcntPlatingDetails();
				objMSAccPlatingDetails.setOffice(objToMSAcc_Details.getOfficeNumber());
				objMSAccPlatingDetails.setAccount_number(objToMSAcc_Details.getAccountNumber());
				objMSAccPlatingDetails.setFa(objToMSAcc_Details.getFaNumber());
				objMSAccPlatingDetails.setUser_id(objMSUserDetails.getRcafId());

				//Putting the input object in the HashMap...
				txnDetails.put("MSAcntPlatingInDetails",objMSAccPlatingDetails);

				//Calling account plating details...
				PaymentsUtility.getAccountPlatingInfo(txnDetails,serviceContext);
				if (serviceContext.getMaxSeverity()== MessageType.CRITICAL || serviceContext.getMaxSeverity() == MessageType.SEVERE){
					return;
				}

				//Output extraction from account plating response...
				AccountPlating acntPlating = new AccountPlating();
				if(txnDetails.containsKey("AcntPlatingOutputDetails")){
					acntPlating = (AccountPlating)txnDetails.get("AcntPlatingOutputDetails");
				}

				//Updating the From/To account details with the Hashed UniqueIds for validating external account owner..
				if(acntPlating!=null){
					if(transferType!=null && (transferType.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT))){
						objToMSAcc_Details.setKeyClientId(MSCommonUtils.extractKeyClientId(acntPlating));
						objToMSAcc_Details.setAccountPlating(acntPlating);
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}
	}
}
