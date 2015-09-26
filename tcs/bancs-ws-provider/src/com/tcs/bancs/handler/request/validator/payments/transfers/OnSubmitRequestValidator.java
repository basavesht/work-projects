package com.tcs.bancs.handler.request.validator.payments.transfers;

import java.util.ArrayList;
import java.util.Map;

import com.tcs.bancs.handler.request.validator.RequestValidationException;
import com.tcs.bancs.handler.request.validator.RequestValidator;
import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;
import com.tcs.bancs.objects.schema.request.payments.transfers.Account;
import com.tcs.bancs.objects.schema.request.payments.transfers.AcntType;
import com.tcs.bancs.objects.schema.request.payments.transfers.PaymentRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.RecurringDetails;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferAction;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferSchedule;
import com.tcs.bancs.objects.schema.request.payments.transfers.UserDetails;

/**
 * @author 224703
 *
 */
public class OnSubmitRequestValidator implements RequestValidator 
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
			assertAttribute(objTransferRequest.getPaymentRequest()== null,"PaymentRequest");
			assertPaymentRequestValidator(objPaymentRequest);

			//Recurring Details..
			RecurringDetails objRecurring = objPaymentRequest.getRecurringDetails();
			if(TransferSchedule.Recurring.equals(objTransferRequest.getPaymentRequest().getTransferSchedule())){			
				assertAttribute(objRecurring == null,"RecurringDetails");
				assertRecurringValidator(objRecurring);
			}

			//Account Request..
			ArrayList<Account> objAccount = objTransferRequest.getPaymentRequest().getAccount();
			for(Account objAccountDetails : objAccount) {						
				assertAttribute(objAccountDetails==null,"Account");
				assertAccountValidator(objAccountDetails);
			}

			//User details..
			UserDetails objUserDetails = objTransferRequest.getUserDetails();
			assertAttribute(objUserDetails==null,"UserDetails");
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
		assertAttribute(objPaymentRequest.getAmount()==null,"Amount");
		assertAttribute(objPaymentRequest.getTransferDate()==null,"TransferDate");
		assertAttribute(objPaymentRequest.getComments()==null,"Comments");
		assertAttribute(objPaymentRequest.getTransferSchedule()==null,"TransferSchedule");
		assertAttribute(objPaymentRequest.getAccount()==null,"Account");
		if(!TransferAction.CreateConfirm.equals(objPaymentRequest.getTransferAction()) && 
				!TransferAction.CreatePreConfirm.equals(objPaymentRequest.getTransferAction())){
			assertAttribute(objPaymentRequest.getTxnConfirmationNo()==null,"TxnConfirmationNo");
		}
	}

	//Account details validation method
	private static void assertAccountValidator(Account objAccount) throws ConfigXMLParsingException {
		assertAttribute(objAccount.getDrCrIndicator()==null,"DrCrIndicator");
		assertAttribute(objAccount.getAcntType()==null,"AcntType");
		if(AcntType.Internal.equals(objAccount.getAcntType())){
			assertAttribute(objAccount.getOffice()==null,"Office");
			assertAttribute(objAccount.getFa()==null,"FA");
			assertAttribute(objAccount.getAccount()==null,"Account");
			assertAttribute(objAccount.getKeyAccount()==null,"KeyAccount");
		}
		else if(AcntType.External.equals(objAccount.getAcntType())){
			assertAttribute(objAccount.getExternalAcntRefId()==null,"ExternalAcntRefId");
		}
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

	//Recurring details validation mehtod
	private static void assertRecurringValidator(RecurringDetails objRecurring) throws ConfigXMLParsingException {
		assertAttribute(objRecurring.getFrequency()==null,"Frequency");
		assertAttribute(objRecurring.getRepeat()==null,"Repeat");
		if(objRecurring.getRepeat()== 1)
			assertAttribute(objRecurring.getEndDate()==null,"EndDate");
		if(objRecurring.getRepeat()== 2)
			assertAttribute(objRecurring.getUntilAmount()==null,"UntilAmount");
		if(objRecurring.getRepeat()== 3)
			assertAttribute(objRecurring.getNoOfTransfers()==null,"NoOfTransfers");
	}

	//Validator for throwing exception			
	private static void assertAttribute(boolean raiseError,String attribute) throws ConfigXMLParsingException {
		if(raiseError) {
			throw new ConfigXMLParsingException("Exception : Create/Submit Transfer " + "XML Child tag " + attribute + "not found in request");
		}
	}
}
