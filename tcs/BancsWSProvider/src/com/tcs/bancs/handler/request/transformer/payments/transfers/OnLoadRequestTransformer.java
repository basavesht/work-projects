/**
 * 
 */
package com.tcs.bancs.handler.request.transformer.payments.transfers;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tcs.Payments.EAITO.BUS_RULE_INP_CRTL;
import com.tcs.Payments.EAITO.BUS_RUL_TXN_INP;
import com.tcs.Payments.EAITO.MS_ACCOUNT_OUT_DTL;
import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.InitiatorRoleDesc;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.handler.request.transformer.RequestTransformationException;
import com.tcs.bancs.handler.request.transformer.RequestTransformer;
import com.tcs.bancs.objects.schema.request.payments.transfers.Account;
import com.tcs.bancs.objects.schema.request.payments.transfers.PaymentRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.TransferRequest;
import com.tcs.bancs.objects.schema.request.payments.transfers.UserDetails;
import com.tcs.bancs.utils.TransferRequestUtil;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;

/**
 * @author 224703
 *
 */
public class OnLoadRequestTransformer implements  RequestTransformer
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OnLoadRequestTransformer.class);
	public Map<String, Object> transformRequest(Map<String, Object> request, String operationId) throws RequestTransformationException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("transformRequest(Map<String,Object>, String) - start");
		}

		Map<String, Object> businessRequest = new HashMap<String, Object>();
		ArrayList<Object> onLoadAccDetailsTO= new ArrayList<Object>();
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

			//Mapping the User details..
			MSUser_DetailsTO objMSUserDetails = TransferRequestUtil.setMSUserDetailsTO(objUserDetails);

			// Business Rule Input Control Mappings ...
			BUS_RULE_INP_CRTL objBUS_RULE_INP_CRTL = new BUS_RULE_INP_CRTL();
			objBUS_RULE_INP_CRTL.setUSER_ID(objUserDetails.getLoginId());
			objBUS_RULE_INP_CRTL.setGROUP(InitiatorRoleDesc.Client); 
			objBUS_RULE_INP_CRTL.setROLE("Client");
			objBUS_RULE_INP_CRTL.setAPPL_ID("MS");
			objBUS_RULE_INP_CRTL.setSERVER_IP("172.19.98.148");

			// Business Rule Input Transaction Mappings ...
			BUS_RUL_TXN_INP objBUS_RUL_TXN_INP = new BUS_RUL_TXN_INP();

			String filterType = TransferRequestUtil.setAcntFilterType(objPaymentRequest);
			if(filterType!=null && filterType.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
				objBUS_RUL_TXN_INP.setTYPE(Bus_Rule_Input_Desc.TYPE_INT);
				objBUS_RUL_TXN_INP.setPAGE_TYPE(Bus_Rule_Input_Desc.CS_INT);
			}
			else {
				objBUS_RUL_TXN_INP.setTYPE(Bus_Rule_Input_Desc.TYPE_ACH);
				objBUS_RUL_TXN_INP.setPAGE_TYPE(Bus_Rule_Input_Desc.CS_ACH);
			}
			objBUS_RUL_TXN_INP.setACTION(Bus_Rule_Input_Desc.Create); 
			objBUS_RUL_TXN_INP.setRULE_TYPE(Bus_Rule_Input_Desc.RULE_TYPE_BLCK_DISP);  

			//Adding to the arraylist..
			onLoadAccDetailsTO.add(objBUS_RULE_INP_CRTL);
			onLoadAccDetailsTO.add(objBUS_RUL_TXN_INP);

			//Mapping the account attributes...
			for(Account objAccountDetails : objAccount)
			{
				//Business Rule Input Mappings (Merlin Input)...
				MS_ACCOUNT_OUT_DTL objMS_ACCOUNT_OUT_DTL = new MS_ACCOUNT_OUT_DTL();
				objMS_ACCOUNT_OUT_DTL.setOFFICE(TransferRequestUtil.formatToMSAcntType(objAccountDetails.getOffice(),3));
				objMS_ACCOUNT_OUT_DTL.setACCOUNT_NO(TransferRequestUtil.formatToMSAcntType(objAccountDetails.getAccount(),6));
				objMS_ACCOUNT_OUT_DTL.setFA(TransferRequestUtil.formatToMSAcntType(objAccountDetails.getFa(),3));
				objMS_ACCOUNT_OUT_DTL.setDIVPAY(objAccountDetails.getDivPay());
				objMS_ACCOUNT_OUT_DTL.setSTATUS(objAccountDetails.getAccountStatus());
				objMS_ACCOUNT_OUT_DTL.setACNT_CLS(objAccountDetails.getAccountClass());
				objMS_ACCOUNT_OUT_DTL.setCHOICE_FUND_CODE(objAccountDetails.getChoiceFundCode());
				objMS_ACCOUNT_OUT_DTL.setIRACODE(objAccountDetails.getIraCode());
				objMS_ACCOUNT_OUT_DTL.setCLNT_CAT(objAccountDetails.getClientCategory());
				objMS_ACCOUNT_OUT_DTL.setNVS_SUB_PROD(objAccountDetails.getNovusSubProduct());
				objMS_ACCOUNT_OUT_DTL.setTRADCNTRL(objAccountDetails.getTradeControl());
				objMS_ACCOUNT_OUT_DTL.setACC_CATEGORY(objAccountDetails.getAccountCategory());
				objMS_ACCOUNT_OUT_DTL.setCOLLATERAL_ACC_IND(objAccountDetails.getCollateralAcctInd());

				onLoadAccDetailsTO.add(objMS_ACCOUNT_OUT_DTL);
			}

			Object[] objTOParam = {objMSUserDetails,onLoadAccDetailsTO};
			Object objParams[]={"",objTOParam,new Boolean(false),filterType};

			businessRequest.put("SERVICE_ID", operationId);
			businessRequest.put("SERVICE_PARAMS", objParams);
		} 
		catch (Exception exception) {
			throw new RequestTransformationException("Exception : Onload Transfer", exception);		
		}

		if (logger.isDebugEnabled()) {
			logger.debug("transformRequest(Map<String,Object>, String) - end");
		}
		return businessRequest;
	}
}
