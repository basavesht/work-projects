package com.tcs.bancs.handler.request.transformer.payments.transfers;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tcs.bancs.handler.request.transformer.RequestTransformationException;
import com.tcs.bancs.handler.request.transformer.RequestTransformer;
import com.tcs.bancs.objects.schema.request.payments.transfers.Account;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.UserDetails;
import com.tcs.bancs.utils.TransferRequestUtil;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;

/**
 * @author 224703
 *
 */
public class SelectAccountRequestTransformer implements RequestTransformer
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SelectAccountRequestTransformer.class);
	public Map<String, Object> transformRequest(Map<String, Object> request, String operationId) throws RequestTransformationException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("transformRequest(Map<String,Object>, String) - start");
		}

		Map<String, Object> businessRequest = new HashMap<String, Object>();
		try
		{
			//Extracting the TransferRequest attributes...
			TransferRequest objTransferRequest = new TransferRequest();
			if(request.containsKey("TransferRequest")){
				objTransferRequest = (TransferRequest)request.get("TransferRequest");
			}
			UserDetails objUserDetails = objTransferRequest.getUserDetails();
			ArrayList<Account> objAccount = objTransferRequest.getPaymentRequest().getAccount();

			//Mapping the User details..
			MSUser_DetailsTO objMSUserDetails = TransferRequestUtil.setMSUserDetailsTO(objUserDetails);

			//Extracting accountdetails to FromMSAcc_DetailsTO
			FromMSAcc_DetailsTO objFromMSAcc_DetailsTO = new FromMSAcc_DetailsTO();
			if(objAccount != null && !objAccount.isEmpty()){
				for(Account fromAccount : objAccount) {
					objFromMSAcc_DetailsTO.setAccountNumber(TransferRequestUtil.formatToMSAcntType(fromAccount.getAccount(), 6));
					objFromMSAcc_DetailsTO.setOfficeNumber(TransferRequestUtil.formatToMSAcntType(fromAccount.getOffice(), 3));
					objFromMSAcc_DetailsTO.setFaNumber(TransferRequestUtil.formatToMSAcntType(fromAccount.getFa(), 3));
					break;
				}
			}

			Object objTOParam[] = {objMSUserDetails,objFromMSAcc_DetailsTO};
			Object objParams[]={"", objTOParam, new Boolean(false)};

			businessRequest.put("SERVICE_ID", operationId);
			businessRequest.put("SERVICE_PARAMS", objParams);
		} 
		catch(Exception e){
			throw new RequestTransformationException("Exception : Select Account",e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("transformRequest(Map<String,Object>, String) - end");
		}
		return businessRequest;
	}

}
