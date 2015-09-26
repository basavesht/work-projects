package com.tcs.bancs.handler.request.validator.payments.transfers;

import java.util.ArrayList;
import java.util.Map;

import com.tcs.bancs.handler.request.validator.RequestValidationException;
import com.tcs.bancs.handler.request.validator.RequestValidator;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.objects.schema.request.payments.transfers.Account;
import com.tcs.bancs.objects.schema.request.payments.transfers.PaymentRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.UserDetails;

/**
 * @author 224703
 *
 */
public class OnLoadRequestValidator implements RequestValidator  
{
	public boolean validate(Map<String, Object> request) throws RequestValidationException 
	{
		try
		{
			//Extracting the TransferRequest attributes...
			TransferRequest objTransferRequest = new TransferRequest();
			if(request.containsKey("TransferRequest")){
				objTransferRequest = (TransferRequest)request.get("TransferRequest");
			}
			assertAttribute(objTransferRequest==null,"TransferRequest");

			//Payment Request..
			PaymentRequest objPaymentRequest = objTransferRequest.getPaymentRequest();
			assertAttribute(objTransferRequest.getPaymentRequest()==null,"PaymentRequest");
			assertPaymentRequestValidator(objPaymentRequest); 

			//Account Request..
			ArrayList<Account> objAccount = objTransferRequest.getPaymentRequest().getAccount();
			for(Account objAccountDetails : objAccount) {						
				assertAttribute(objAccountDetails==null,"Account");
				assertAccountValidator(objAccountDetails);
			}

			//User details..
			UserDetails objUserDetails = objTransferRequest.getUserDetails();
			assertAttribute(objTransferRequest.getUserDetails()==null,"UserDetails");
			assertUserDetailsValidator(objUserDetails); 
		} 
		catch(Exception e){
			throw new RequestValidationException("Exception received", e);	
		}
		return true;
	}

	//Payment details validation method
	private static void assertPaymentRequestValidator(PaymentRequest objPaymentRequest) throws ConfigXMLParsingException {
		assertAttribute(objPaymentRequest.getTransferAction()==null,"TransferAction");
		assertAttribute(objPaymentRequest.getTransferType()==null,"TransferType");
	}

	//Account details validation method
	private static void assertAccountValidator(Account objAccount) throws ConfigXMLParsingException {
		assertAttribute(objAccount.getDrCrIndicator()==null,"DrCrIndicator");
		assertAttribute(objAccount.getAcntType()==null,"AcntType");
		assertAttribute(objAccount.getOffice()==null,"Office");
		assertAttribute(objAccount.getFa()==null,"FA");
		assertAttribute(objAccount.getAccount()==null,"Account");
		assertAttribute(objAccount.getKeyAccount()==null,"KeyAccount");
		assertAttribute(objAccount.getExternalAcntRefId()==null,"ExternalAcntRefId");
		assertAttribute(objAccount.getAccountStatus()==null,"AccountStatus");
		assertAttribute(objAccount.getAccountClass()==null,"AccountClass");
		assertAttribute(objAccount.getNovusSubProduct()==null,"NovusSubProduct");
		assertAttribute(objAccount.getDivPay()==null,"DivPay");
		assertAttribute(objAccount.getClientCategory()==null,"ClientCategory");
		assertAttribute(objAccount.getChoiceFundCode()==null,"ChoiceFundCode");
		assertAttribute(objAccount.getIraCode()==null,"IraCode");
		assertAttribute(objAccount.getTradeControl()==null,"TradeControl");
		assertAttribute(objAccount.getAccountCategory()==null,"AccountCategory");
		assertAttribute(objAccount.getCollateralAcctInd()==null,"CollateralAcctInd");
	}

	//User details validation mehtod
	private static void assertUserDetailsValidator(UserDetails objUserDetails) throws ConfigXMLParsingException {
		assertAttribute(objUserDetails.getLoginId()==null,"LoginId");
		assertAttribute(objUserDetails.getClientUUID()==null,"ClientUUID");
		assertAttribute(objUserDetails.getFirstName()==null,"FirstName");
		assertAttribute(objUserDetails.getMiddleName()==null,"MiddleName");
		assertAttribute(objUserDetails.getLastName()==null,"LastName");
		assertAttribute(objUserDetails.getClientIdentifier()==null,"ClientIdentifier");
		assertAttribute(objUserDetails.getMaskedClientIdentifier()==null,"MaskedClientIdentifier");
	}

	//Validator for throwing exception
	private static void assertAttribute(boolean raiseError,String attribute) throws ConfigXMLParsingException {
		if(raiseError) {
			throw new ConfigXMLParsingException("Exception : OnLoad Transfer " + "XML Child tag " + attribute + "not found in request");
		}
	}
}
