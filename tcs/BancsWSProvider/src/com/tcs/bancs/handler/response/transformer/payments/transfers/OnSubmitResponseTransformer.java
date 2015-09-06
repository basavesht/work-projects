package com.tcs.bancs.handler.response.transformer.payments.transfers;

import org.apache.log4j.Logger;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.handler.response.transformer.ResponseTransformationException;
import com.tcs.bancs.handler.response.transformer.ResponseTransformer;
import com.tcs.bancs.objects.schema.response.payments.transfers.PaymentResponse;
import com.tcs.bancs.objects.schema.response.payments.transfers.ResponseObject;
import com.tcs.bancs.objects.schema.response.payments.transfers.ReturnCode;
import com.tcs.bancs.objects.schema.response.payments.transfers.ServiceContext;
import com.tcs.bancs.objects.schema.response.payments.transfers.TransferResponse;
import com.tcs.bancs.utils.TransferRequestUtil;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author 224703
 *
 */
public class OnSubmitResponseTransformer implements ResponseTransformer {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OnSubmitResponseTransformer.class);

	private TransferResponse objTransferResponse = new TransferResponse();
	private PaymentResponse objPaymentResponse = new PaymentResponse();
	private ResponseObject objResponseObject = new ResponseObject();
	private ServiceContext objRespMessageContext = new ServiceContext();

	public Map<String, Object> transformResponse(Map<String, Object> response) throws ResponseTransformationException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("transformResponse(Map<String,Object>) - start");
		}

		Map<String, Object> transferResponseMap = new HashMap<String, Object>();
		try
		{
			Object businessResponse = null;
			if(response.containsKey("BusinessResponse")){
				businessResponse = response.get("BusinessResponse");
			}

			if(businessResponse != null) 
			{
				//Output extraction...
				ArrayList createSubmitDetails = (ArrayList) businessResponse;
				com.tcs.bancs.channels.context.ServiceContext contextData = (com.tcs.bancs.channels.context.ServiceContext) createSubmitDetails.get(0);

				if (contextData.getMaxSeverity() == MessageType.CRITICAL){
					String errorMessage = TransferRequestUtil.extractContextErrMessage(contextData);
					objResponseObject = TransferRequestUtil.setResponseObject(ReturnCode.FAILURE,errorMessage);
				}
				else 
				{
					if (contextData.getMaxSeverity() == MessageType.SEVERE){
						objRespMessageContext = TransferRequestUtil.setRespMessageContext(contextData,MessageType.SEVERE);
						objResponseObject = TransferRequestUtil.setResponseObject(ReturnCode.SUCCESS,ReturnCode.SUCCESS.name());
					} 
					else if (contextData.getMaxSeverity() == MessageType.ERROR) {	
						objRespMessageContext = TransferRequestUtil.setRespMessageContext(contextData,MessageType.ERROR);
						objResponseObject = TransferRequestUtil.setResponseObject(ReturnCode.SUCCESS,ReturnCode.SUCCESS.name());
					} 
					else if (contextData.getMaxSeverity() == MessageType.RISK) {	
						objRespMessageContext = TransferRequestUtil.setRespMessageContext(contextData,MessageType.RISK);
						objResponseObject = TransferRequestUtil.setResponseObject(ReturnCode.SUCCESS,ReturnCode.SUCCESS.name());
					} 
					else if (contextData.getMaxSeverity() == MessageType.WARNING) {	
						objRespMessageContext = TransferRequestUtil.setRespMessageContext(contextData,MessageType.WARNING);
						objResponseObject = TransferRequestUtil.setResponseObject(ReturnCode.SUCCESS,ReturnCode.SUCCESS.name());
					} 
					else if (contextData.getMaxSeverity() == MessageType.INFORMATION) {	
						objRespMessageContext = TransferRequestUtil.setRespMessageContext(contextData,MessageType.INFORMATION);
						objResponseObject = TransferRequestUtil.setResponseObject(ReturnCode.SUCCESS,ReturnCode.SUCCESS.name());
					}
					else 
					{
						if (createSubmitDetails.get(1) != null) {
							HashMap txnOutDetails = (HashMap) createSubmitDetails.get(1);

							//Payment Details..
							PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
							if (txnOutDetails.containsKey("PaymentDetails")) {
								objPaymentDetails = (PaymentDetailsTO) txnOutDetails.get("PaymentDetails");
							}
							objPaymentResponse.setConfirmationNo(objPaymentDetails.getTransactionId());
							objPaymentResponse.setPaymentStatus(ConvertionUtil.convertToint(objPaymentDetails.getTxnCurrentStatusCode()));
							objResponseObject = TransferRequestUtil.setResponseObject(ReturnCode.SUCCESS,ReturnCode.SUCCESS.name());
						}
					}
					objPaymentResponse.setServiceContext(objRespMessageContext);
					objTransferResponse.setPaymentResponse(objPaymentResponse);
				}
			}
		}
		catch (Exception exception){
			throw new ResponseTransformationException("Exception : Submit Transfer Response",exception);
		}
		finally {
			objTransferResponse.setResponseObject(objResponseObject);
			transferResponseMap.put("TransferResponse", objTransferResponse);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("transformResponse(Map<String,Object>) - end");
		}
		return transferResponseMap;
	}

}
