package com.tcs.bancs.handler.response.transformer.payments.transfers;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.handler.response.transformer.ResponseTransformationException;
import com.tcs.bancs.handler.response.transformer.ResponseTransformer;
import com.tcs.bancs.objects.schema.request.payments.transfers.Account;
import com.tcs.bancs.objects.schema.request.payments.transfers.AcntType;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest;
import com.tcs.bancs.objects.schema.response.payments.transfers.AccountResponse;
import com.tcs.bancs.objects.schema.response.payments.transfers.PaymentResponse;
import com.tcs.bancs.objects.schema.response.payments.transfers.ResponseObject;
import com.tcs.bancs.objects.schema.response.payments.transfers.ReturnCode;
import com.tcs.bancs.objects.schema.response.payments.transfers.TransferResponse;
import com.tcs.bancs.utils.TransferRequestUtil;
import com.tcs.ebw.serverside.services.channelPaymentServices.BusinessRulesService;

/**
 * @author 224703
 *
 */
public class OnLoadResponseTransformer implements ResponseTransformer
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OnLoadResponseTransformer.class);

	TransferResponse objTransferResponse = new TransferResponse();
	ResponseObject objResponseObject = new ResponseObject();
	PaymentResponse objPaymentResponse = new PaymentResponse();

	public Map<String, Object> transformResponse(Map<String, Object> response) throws ResponseTransformationException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("transformResponse(Map<String,Object>) - start");
		}

		Map<String, Object> onLoadResponseMap = new HashMap<String, Object>();
		List<AccountResponse> accountResponseList = new ArrayList<AccountResponse>();
		try
		{
			Object businessResponse = null;
			if(response.containsKey("BusinessResponse")){
				businessResponse = response.get("BusinessResponse");
			}

			//Output extraction...
			ArrayList onLoadAccOut = (ArrayList) businessResponse;
			ServiceContext contextData = (ServiceContext) onLoadAccOut.get(0);

			if (contextData.getMaxSeverity() == MessageType.CRITICAL) {
				String errorMessage = TransferRequestUtil.extractContextErrMessage(contextData);
				objResponseObject = TransferRequestUtil.setResponseObject(ReturnCode.FAILURE,errorMessage);
			} 
			else 
			{
				HashMap txnOutDetails = (HashMap) onLoadAccOut.get(1);
				BusinessRulesService objBusinessRulesService = new BusinessRulesService();
				ArrayList blockedAccList = new ArrayList();

				if (txnOutDetails.containsKey("BlockedDispOutputDetails")) {
					objBusinessRulesService = (BusinessRulesService) txnOutDetails.get("BlockedDispOutputDetails");
					blockedAccList = objBusinessRulesService.getBlockedAccountDetails();
				}

				//Same Name external accounts..
				Object externalAccounts = null;
				if (txnOutDetails.containsKey("ExternalAccountsList")) {
					externalAccounts = txnOutDetails.get("ExternalAccountsList");
				}

				//Non-Same Name external accounts..
				Object thirdPartyExtAccounts = null;
				if (txnOutDetails.containsKey("ThirdPartyExtAccountsList")) {
					thirdPartyExtAccounts = txnOutDetails.get("ThirdPartyExtAccountsList");
				}

				//Context Accounts..
				TransferRequest objTransferRequest = new TransferRequest();
				if(response.containsKey("TransferRequest")){
					objTransferRequest = (TransferRequest)response.get("TransferRequest");
				}

				//Adding Internal MS accounts..
				ArrayList<Account> contextAcntsList = objTransferRequest.getPaymentRequest().getAccount();
				for(Account acnt : contextAcntsList){
					boolean goodAcnt = false;
					for(int i = 0; i < blockedAccList.size(); i++){
						ArrayList blockedAccVal = (ArrayList) blockedAccList.get(i);
						String accountNum = (String)blockedAccVal.get(0);
						String acntRetVal = (String)blockedAccVal.get(1);
						if(accountNum.equals(TransferRequestUtil.formatToMSAcntType(acnt.getAccount(),6)) &&
								(acntRetVal.equals("1") || acntRetVal.equals("2") || acntRetVal.equals("3"))){
							goodAcnt = true;
						}
					}
					if(goodAcnt){
						AccountResponse acntResp = new AccountResponse();
						acntResp.setAcntType(acnt.getAcntType());
						acntResp.setOffice(acnt.getOffice());
						acntResp.setFa(acnt.getFa());
						acntResp.setAccount(acnt.getAccount());
						acntResp.setKeyAccount(acnt.getKeyAccount());
						acntResp.setDisplayName(acnt.getDisplayName());
						accountResponseList.add(acntResp);
					}
				}

				//Adding External accounts ...
				if (externalAccounts != null) {
					ArrayList<ArrayList<Object>> extAccDetails = (ArrayList<ArrayList<Object>>) externalAccounts;
					for (ArrayList<Object> externalAccountValues : extAccDetails) {
						AccountResponse acntResp = new AccountResponse();
						acntResp.setAcntType(AcntType.External);
						acntResp.setDisplayName((String) externalAccountValues.get(0));
						acntResp.setExternalAcntRefId((String) externalAccountValues.get(1));
						accountResponseList.add(acntResp);
					}
				}

				//Adding Third Party External accounts ...
				if (thirdPartyExtAccounts != null) {
					ArrayList<ArrayList<Object>>  thirdPartyExtAccDetails = (ArrayList<ArrayList<Object>>) thirdPartyExtAccounts;
					if (!thirdPartyExtAccDetails.isEmpty()) //Checking if any third party accounts are present...
					{
						for (ArrayList<Object> thirdPartyExtAccountValues : thirdPartyExtAccDetails) {
							AccountResponse acntResp = new AccountResponse();
							acntResp.setAcntType(AcntType.External);
							acntResp.setDisplayName((String) thirdPartyExtAccountValues.get(0));
							acntResp.setExternalAcntRefId((String) thirdPartyExtAccountValues.get(1));
							accountResponseList.add(acntResp);
						}
					}
				}
				if(!accountResponseList.isEmpty()) {
					objPaymentResponse.setAccountResponse((ArrayList<AccountResponse>)accountResponseList);
					objTransferResponse.setPaymentResponse(objPaymentResponse);

					//Response Object...
					objResponseObject = TransferRequestUtil.setResponseObject(ReturnCode.SUCCESS,ReturnCode.SUCCESS.name());
				}
				else {
					//Response Object...
					objResponseObject = TransferRequestUtil.setResponseObject(ReturnCode.FAILURE,ReturnCode.FAILURE.name());
				}
			}
		}
		catch(Exception exception){
			throw new ResponseTransformationException("Exception : Onload Response",exception);
		}
		finally {
			objTransferResponse.setResponseObject(objResponseObject);
			onLoadResponseMap.put("TransferResponse", objTransferResponse);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("transformResponse(Map<String,Object>) - end");
		}
		return onLoadResponseMap;
	}
}
