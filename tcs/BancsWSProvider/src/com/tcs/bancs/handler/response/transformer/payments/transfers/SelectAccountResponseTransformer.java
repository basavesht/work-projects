package com.tcs.bancs.handler.response.transformer.payments.transfers;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bancs.handler.response.transformer.ResponseTransformationException;
import com.tcs.bancs.handler.response.transformer.ResponseTransformer;
import com.tcs.bancs.objects.schema.request.payments.transfers.Account;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest;
import com.tcs.bancs.objects.schema.response.payments.transfers.AccountResponse;
import com.tcs.bancs.objects.schema.response.payments.transfers.AcntBalance;
import com.tcs.bancs.objects.schema.response.payments.transfers.PaymentResponse;
import com.tcs.bancs.objects.schema.response.payments.transfers.ResponseObject;
import com.tcs.bancs.objects.schema.response.payments.transfers.ReturnCode;
import com.tcs.bancs.objects.schema.response.payments.transfers.TransferResponse;
import com.tcs.bancs.utils.TransferRequestUtil;
import com.tcs.ebw.serverside.services.channelPaymentServices.AccountBalance;

/**
 * @author 224703
 *
 */
public class SelectAccountResponseTransformer implements ResponseTransformer {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SelectAccountResponseTransformer.class);

	private TransferResponse objTransferResponse = new TransferResponse();
	private ResponseObject objResponseObject = new ResponseObject();
	private PaymentResponse objPaymentResponse = new PaymentResponse();
	private ArrayList<AccountResponse> accountResponseList = new ArrayList<AccountResponse>();
	private AccountResponse acntResp = new AccountResponse();
	private AcntBalance objAccountBal = new AcntBalance();

	public Map<String, Object> transformResponse(Map<String, Object> response)  throws ResponseTransformationException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("transformResponse(Map<String,Object>) - start");
		}

		Map<String, Object> onAcntBalRespMap = new HashMap<String, Object>();
		try
		{
			Object businessResponse = null;
			if(response.containsKey("BusinessResponse")){
				businessResponse = response.get("BusinessResponse");
			}

			if (businessResponse != null) 
			{
				ArrayList onSelectAccBalance = (ArrayList) businessResponse;
				ServiceContext contextData = (ServiceContext) onSelectAccBalance.get(0);

				if ((contextData.getMaxSeverity() == MessageType.CRITICAL) || (contextData.getMaxSeverity() == MessageType.SEVERE)) {
					String errorMessage = TransferRequestUtil.extractContextErrMessage(contextData);
					objResponseObject = TransferRequestUtil.setResponseObject(ReturnCode.FAILURE,errorMessage);
				}
				else
				{
					//Transfer Request
					TransferRequest request = null;
					if(response.containsKey("TransferRequest")){
						request = (TransferRequest) response.get("TransferRequest");
					}

					//Account Balance
					HashMap acntBalanceRespMap = (HashMap) onSelectAccBalance.get(1);
					AccountBalance objAccBalance = null;
					if(acntBalanceRespMap != null && acntBalanceRespMap.containsKey("RTABOutputDetails"))
					{
						objAccBalance = (AccountBalance) acntBalanceRespMap.get("RTABOutputDetails");
						HashMap rtabBalDetails = objAccBalance.getInfoRTAB();

						String acntNum = (String)rtabBalDetails.get("AccountNumber");
						objAccountBal.setAvailSpndPower((BigDecimal)rtabBalDetails.get("AvailableSpendingPower"));
						objAccountBal.setCashAvail((BigDecimal)rtabBalDetails.get("CashAvailable"));
						objAccountBal.setCashAvailablePend((BigDecimal)rtabBalDetails.get("CashAvailable"));
						objAccountBal.setMarginAvail((BigDecimal)rtabBalDetails.get("MarginAvailable"));
						objAccountBal.setMarginPendTxn((BigDecimal)rtabBalDetails.get("MarginPendingTransaction"));
						objAccountBal.setDelayedDebitCardTxn((BigDecimal)rtabBalDetails.get("DelayedDebitCardTxn"));

						if(request != null) {
							for(Account acntReq : request.getPaymentRequest().getAccount()) 
							{
								//RTAB response contains 7 digit account number...
								if(acntNum!=null && TransferRequestUtil.formatToMSAcntType(acntNum,7).equals(TransferRequestUtil.formatToMSAcntType(acntReq.getAccount(),7))){
									acntResp.setAcntType(acntReq.getAcntType());
									acntResp.setOffice(acntReq.getOffice());
									acntResp.setFa(acntReq.getFa());
									acntResp.setAccount(acntReq.getAccount());
									acntResp.setKeyAccount(acntReq.getKeyAccount());
									acntResp.setDisplayName(acntReq.getDisplayName());
									acntResp.setAcntBalance(objAccountBal);
									accountResponseList.add(acntResp);
									break;
								}
							}
						}

						if(!accountResponseList.isEmpty()){
							objPaymentResponse.setAccountResponse(accountResponseList);
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
			}
		}
		catch (Exception exception) {
			throw new ResponseTransformationException("Exception : Select Account Response",exception);
		}
		finally {
			objTransferResponse.setResponseObject(objResponseObject);
			onAcntBalRespMap.put("TransferResponse", objTransferResponse);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("transformResponse(Map<String,Object>) - end");
		}
		return onAcntBalRespMap;
	}
}


