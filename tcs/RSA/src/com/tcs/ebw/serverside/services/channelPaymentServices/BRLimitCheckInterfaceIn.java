package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.BUS_RULE_INP_CRTL;
import com.tcs.Payments.EAITO.BUS_RUL_TXN_INP;
import com.tcs.Payments.ms360Utils.BRUtils;
import com.tcs.Payments.ms360Utils.Bus_Rule_Input_Desc;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.MerlinOutResponse;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class BRLimitCheckInterfaceIn {

	public BRLimitCheckInterfaceIn(){

	}

	/** 
	 * The method is used for setting the limit check parameters in case of creation new transfers .
	 * In case of creating new child transaction and during resume functionality when transactions
	 * are put back in scheduled status for future dated transactions...
	 * @param toObjects
	 * @return
	 * @throws Exception 
	 */

	public static Vector setLimitParameters(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.trace("BRLimitCheckInterfaceIn", "Setting the Create Limit check parameters for the Child transactions..");
		Vector limitCheckInputVector = new Vector();
		try 
		{
			//Mapping the Payment details..
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//User Details...
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//From Account Details...
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//Mapping the BR Transaction input details..
			BUS_RUL_TXN_INP objBUS_RUL_TXN_INP = new BUS_RUL_TXN_INP();
			objBUS_RUL_TXN_INP.setTXN_TYPE(objPaymentDetails.getTransfer_Type());
			objBUS_RUL_TXN_INP.setDEBIT_ACCOUNT(objFromMSAcc_Details.getKeyAccount());
			objBUS_RUL_TXN_INP.setCREATE_FLG(objPaymentDetails.getCreate_limit());
			objBUS_RUL_TXN_INP.setUPDATE_FLG(objPaymentDetails.getUpdate_limit());
			objBUS_RUL_TXN_INP.setDELETE_FLG(objPaymentDetails.getDelete_limit());
			objBUS_RUL_TXN_INP.setTYPE(Bus_Rule_Input_Desc.TYPE_ACH);
			objBUS_RUL_TXN_INP.setACTION(objPaymentDetails.getLimit_Action());
			objBUS_RUL_TXN_INP.setRULE_TYPE(Bus_Rule_Input_Desc.RULE_TYPE_TXN); 
			objBUS_RUL_TXN_INP.setAMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			objBUS_RUL_TXN_INP.setEXE_DATE(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			objBUS_RUL_TXN_INP.setBUS_DATE(ConvertionUtil.convertToTimestamp(objPaymentDetails.getBusiness_Date()));
			objBUS_RUL_TXN_INP.setPAGE_TYPE(BRUtils.getBRTxn_PageType(objPaymentDetails));
			objBUS_RUL_TXN_INP.setTXN_SUB_TYPE(BRUtils.getBRTxnSub_Type(objPaymentDetails));
			objBUS_RUL_TXN_INP.setFREQUENCY(BRUtils.getBRFrequency_type(objPaymentDetails));

			//Child Creation mappings..
			if(objPaymentDetails.isChildTxnCreated()){
				objBUS_RUL_TXN_INP.setSTATE(Bus_Rule_Input_Desc.STATE_FUTURE); 
				objBUS_RUL_TXN_INP.setEXT_STR(Bus_Rule_Input_Desc.EXT_STR_CHILD);
				objBUS_RUL_TXN_INP.setEXE_DATE(ConvertionUtil.convertToTimestamp(objPaymentDetails.getChildTxnRequestedExecDate()));
			}

			//Additional details for the transaction processing if required... 
			if(objPaymentDetails.getUpdate_limit() == 1){
				objBUS_RUL_TXN_INP.setORGNL_TXN_AMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getPrevTxnAmount()));
				objBUS_RUL_TXN_INP.setORGNL_TXN_DATE(ConvertionUtil.convertToDate(objPaymentDetails.getPrevTxnDate()));
				objBUS_RUL_TXN_INP.setNEW_TXN_AMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
				objBUS_RUL_TXN_INP.setNEW_TXN_DATE(ConvertionUtil.convertToDate(objPaymentDetails.getRequestedDate()));
			}
			else if(objPaymentDetails.getDelete_limit() == 1){
				objBUS_RUL_TXN_INP.setORGNL_TXN_AMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getPrevTxnAmount()));
				objBUS_RUL_TXN_INP.setORGNL_TXN_DATE(ConvertionUtil.convertToDate(objPaymentDetails.getPrevTxnDate()));
			}

			// Business Rule Input Control Mappings ...
			BUS_RULE_INP_CRTL objBUS_RULE_INP_CRTL = new BUS_RULE_INP_CRTL();
			objBUS_RULE_INP_CRTL.setUSER_ID(objMSUserDetails.getRcafId());
			objBUS_RULE_INP_CRTL.setGROUP(MSCommonUtils.getInitiatorRoleDesc(objPaymentDetails.getCurrent_owner_role()));
			objBUS_RULE_INP_CRTL.setROLE(MSCommonUtils.getInitiatorRoleDesc(objPaymentDetails.getCurrent_owner_role()));
			objBUS_RULE_INP_CRTL.setLIMIT_CHECK_REQ("Y");
			objBUS_RULE_INP_CRTL.setAPPL_ID(WSDefaultInputsMap.getBusRuleCntrlAppID(serviceContext));
			objBUS_RULE_INP_CRTL.setSERVER_IP(WSDefaultInputsMap.getBusRuleCntrlServerID(serviceContext));

			//Mappings the BUS_RULE_TXN_INP attributes from the Merlin Response...
			setExternalSrvcResponseDetails(objBUS_RUL_TXN_INP,txnDetails);

			//Adding the parameters to the vector ....
			limitCheckInputVector.add(objBUS_RUL_TXN_INP);
			limitCheckInputVector.add(objBUS_RULE_INP_CRTL);
		} 
		catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
		EBWLogger.trace("BRLimitCheckInterfaceIn", "Input parameters to the Create Limit check :"+limitCheckInputVector.toString());
		return limitCheckInputVector;
	}

	/** Following function is used to set the external services response details in the BR Service
	 * 
	 * @param toObjects
	 * @param externalSrvResponse
	 */

	public static void setExternalSrvcResponseDetails(BUS_RUL_TXN_INP objBus_rul_txn_inp,HashMap txnDetails)
	{
		String fromAccTier = "";

		//Payment Details ...
		PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
		if(txnDetails.containsKey("PaymentDetails")){
			objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
		}
		String txn_type= objPaymentDetails.getTransfer_Type();

		//Merlin Response Mappings ..
		if(txnDetails.containsKey("MerlinOutputDetails")){
			AccountDetails objaccDetails= (AccountDetails)txnDetails.get("MerlinOutputDetails");
			ArrayList merlinAccDetails = objaccDetails.getMerlinOutResponse(); 
			for(int j=0;j<merlinAccDetails.size();j++){
				if(merlinAccDetails.get(j) instanceof MerlinOutResponse){
					MerlinOutResponse accMerlinResponse =(MerlinOutResponse)merlinAccDetails.get(j);
					if(txn_type!=null && txn_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
						fromAccTier=accMerlinResponse.getCatTier();
					}
				}
			}
			objBus_rul_txn_inp.setTIER(fromAccTier);
		}
	}
}
