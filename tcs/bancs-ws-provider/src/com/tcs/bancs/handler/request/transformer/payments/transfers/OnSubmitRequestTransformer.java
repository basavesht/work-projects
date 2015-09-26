/**
 * 
 */
package com.tcs.bancs.handler.request.transformer.payments.transfers;

import org.apache.log4j.Logger;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import com.tcs.Payments.ms360Utils.InitiatorRoleDesc;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.handler.request.transformer.RequestTransformationException;
import com.tcs.bancs.handler.request.transformer.RequestTransformer;
import com.tcs.bancs.objects.schema.request.payments.transfers.Account;
import com.tcs.bancs.objects.schema.request.payments.transfers.AcntType;
import com.tcs.bancs.objects.schema.request.payments.transfers.DrCrIndicator;
import com.tcs.bancs.objects.schema.request.payments.transfers.PaymentRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferSchedule;
import com.tcs.bancs.objects.schema.request.payments.transfers.UserDetails;
import com.tcs.bancs.utils.TransferRequestUtil;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;

/**
 * @author 224703
 *
 */
public class OnSubmitRequestTransformer implements  RequestTransformer 
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OnSubmitRequestTransformer.class);
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
			PaymentRequest objPaymentRequest = objTransferRequest.getPaymentRequest();
			UserDetails objUserDetails = objTransferRequest.getUserDetails();
			ArrayList<Account> objAccount = objTransferRequest.getPaymentRequest().getAccount();

			//Setting default values..
			String branch_Id = PropertyFileReader.getProperty("OU_ID");
			String currencyCode = PropertyFileReader.getProperty("Currency_code_local"); 
			String applicationId = PropertyFileReader.getProperty("APPL_ID");
			String systemDesc = PropertyFileReader.getProperty("MM_SYSTEM_DESC");
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();

			//From and To account object mappings..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();

			String transferType = TxnTypeCode.INTERNAL;

			//Mapping the account attributes...
			for(Account objAccountDetails : objAccount)
			{
				if(AcntType.Internal.equals(objAccountDetails.getAcntType())) {
					if(objAccountDetails.getDrCrIndicator().equals(DrCrIndicator.DR)) {
						objPaymentDetails.setFrom_Account(TransferRequestUtil.formatToMSAcntType(objAccountDetails.getAccount(),6));
						objPaymentDetails.setFrom_KeyAccount(objAccountDetails.getKeyAccount());
						objPaymentDetails.setFrmAcc_InContext(true);
						objFromMSAcc_Details = TransferRequestUtil.setFromMSAccDetailsTO(objAccountDetails);
					}
					else if(objAccountDetails.getDrCrIndicator().equals(DrCrIndicator.CR)){
						objPaymentDetails.setTo_Account(TransferRequestUtil.formatToMSAcntType(objAccountDetails.getAccount(),6));
						objPaymentDetails.setTo_KeyAccount(objAccountDetails.getKeyAccount());
						objPaymentDetails.setToAcc_InContext(true);
						objToMSAcc_Details = TransferRequestUtil.setToMSAccDetailsTO(objAccountDetails);
					}
				}
				if(AcntType.External.equals(objAccountDetails.getAcntType())){
					objPaymentDetails.setExtAccount_RefId(objAccountDetails.getExternalAcntRefId());
					if(objAccountDetails.getDrCrIndicator().equals(DrCrIndicator.DR)) {
						transferType = TxnTypeCode.ACH_DEPOSIT;
					}
					else {
						transferType = TxnTypeCode.ACH_WITHDRAWAL;
					}
				}
			}

			//Mapping the User details..
			MSUser_DetailsTO objMSUserDetails = TransferRequestUtil.setMSUserDetailsTO(objUserDetails);

			//Setting the payment Details...
			objPaymentDetails.setTransfer_Type(transferType);
			objPaymentDetails.setTransfer_Amount(ConvertionUtil.convertToString(objPaymentRequest.getAmount()));
			objPaymentDetails.setChildTxnAmount(ConvertionUtil.convertToString(objPaymentRequest.getAmount()));
			objPaymentDetails.setTransfer_Currency(currencyCode);
			objPaymentDetails.setFxRate(null);
			objPaymentDetails.setInitiation_Date(ConvertionUtil.convertToString(objPaymentRequest.getTransferDate()));
			objPaymentDetails.setRequestedDate(ConvertionUtil.convertToString(objPaymentRequest.getTransferDate()));
			objPaymentDetails.setEstimatedArrivalDate(ConvertionUtil.convertToString(objPaymentRequest.getTransferDate()));
			objPaymentDetails.setFrequency_Type(TransferRequestUtil.setTransferSchedule(objPaymentRequest));
			objPaymentDetails.setTransfer_Type(transferType);
			objPaymentDetails.setScreen_Type(TransferRequestUtil.setPageType(objPaymentRequest));
			objPaymentDetails.setMsBranchId(branch_Id);
			objPaymentDetails.setApplicationId(applicationId);
			objPaymentDetails.setTrial_depo("N");
			objPaymentDetails.setVersionChkId("Transaction");
			objPaymentDetails.setMMSystemDesc(systemDesc);
			objPaymentDetails.setEventDesc(TransferRequestUtil.setBusinessRuleEventDesc(objPaymentRequest));
			objPaymentDetails.setTxnInitiated(true);
			objPaymentDetails.setPrevAction("Create");
			objPaymentDetails.setInitiator_id(objUserDetails.getClientUUID());
			objPaymentDetails.setInitiator_role(InitiatorRoleDesc.Client);
			objPaymentDetails.setCurrent_owner_id(objUserDetails.getClientUUID());
			objPaymentDetails.setCurrent_owner_role(InitiatorRoleDesc.Client);
			setRecurringDetails(objPaymentRequest,objPaymentDetails);

			Object objTOParam[] = {objPaymentDetails,objFromMSAcc_Details,objToMSAcc_Details,objMSUserDetails};
			Object objParams[]={"",objTOParam,new Boolean(false)};

			businessRequest.put("SERVICE_ID", operationId);
			businessRequest.put("SERVICE_PARAMS", objParams);

		} catch (Exception exception) {
			throw new RequestTransformationException("Exception : Submit Transfer Request", exception);		
		}

		if (logger.isDebugEnabled()) {
			logger.debug("transformRequest(Map<String,Object>, String) - end");
		}
		return businessRequest;
	}

	public void setRecurringDetails(PaymentRequest objPaymentRequest, PaymentDetailsTO objPaymentDetails) {
		if(TransferSchedule.Recurring.equals(objPaymentRequest.getTransferSchedule())){		
			objPaymentDetails.setFrequency_DurationDesc(TransferRequestUtil.setFrequency(objPaymentRequest));
			objPaymentDetails.setFrequency_DurationValue(TransferRequestUtil.setRepeat(objPaymentRequest));
			objPaymentDetails.setDuration_EndDate(ConvertionUtil.convertToString(objPaymentRequest.getRecurringDetails().getEndDate()));
			objPaymentDetails.setDuration_NoOfTransfers(objPaymentRequest.getRecurringDetails().getNoOfTransfers());
			objPaymentDetails.setDuration_AmountLimit(ConvertionUtil.convertToString(objPaymentRequest.getRecurringDetails().getUntilAmount()));
		}
	}
}
