package com.tcs.bancs.handler.request.validator.payments.transfers;

import java.util.ArrayList;
import java.util.Map;

import com.tcs.bancs.handler.request.validator.RequestValidationException;
import com.tcs.bancs.handler.request.validator.RequestValidator;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.objects.schema.request.payments.transfers.Account;
import com.tcs.bancs.objects.schema.request.payments.transfers.AcntType;
import com.tcs.bancs.objects.schema.request.payments.transfers.DrCrIndicator;
import com.tcs.bancs.objects.schema.request.payments.transfers.PaymentRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.UserDetails;

/**
 * @author 224703
 *
 */
public class SelectAccountRequestValidator implements RequestValidator  
{
	public boolean validate(Map<String, Object> request) throws RequestValidationException {
		try
		{
			//Mapping the Transfer Details attributes...
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
	}

	//Account details validation method
	private static void assertAccountValidator(Account objAccount) throws ConfigXMLParsingException {
		assertAttribute(objAccount.getDrCrIndicator()==null,"DrCrIndicator");
		assertAttribute(objAccount.getAcntType()==null,"AcntType");
		assertAttribute(objAccount.getOffice()==null,"Office");
		assertAttribute(objAccount.getFa()==null,"FA");
		assertAttribute(objAccount.getAccount()==null,"Account");
		assertAttribute(objAccount.getKeyAccount()==null,"KeyAccount");
		assertCondition(!DrCrIndicator.DR.equals(objAccount.getDrCrIndicator()),"Account should be debit account");
		assertCondition(!AcntType.Internal.equals(objAccount.getAcntType()),"Account should be an internal MS account");
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
			throw new ConfigXMLParsingException("Exception : Select Account " + "XML Child tag " + attribute + "not found in request");
		}
	}

	//Validator for throwing exception	
	private static void assertCondition(boolean raiseError,String message) throws ConfigXMLParsingException {
		if(raiseError) {
			throw new ConfigXMLParsingException("Exception : Select Account request fault : " + message);
		}
	}

}
