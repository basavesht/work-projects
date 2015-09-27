/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.MS_RTA_INPUT;
import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.CheckRequestTO;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       23-05-2011        3               RTA Mapping
 * **********************************************************
 */
public class RTAInterfaceIn {

	public RTAInterfaceIn(){

	}

	/**
	 * Setting the RTA Input Details for transaction...
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public static Vector setRTATxnRequest(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.logDebug("RTAInterfaceIn", "Setting the RTA Inputs for transaction amount..");	
		Vector rtaInputVector = new Vector();
		try 
		{
			//Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//User details...
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//Check request mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			//Input Mapping for the RTA Service...
			MS_RTA_INPUT objMS_RTA_INPUT = new MS_RTA_INPUT();

			//Mapping the account details..
			mapAccountingDetails(txnDetails,objMS_RTA_INPUT);

			//Mapping other RTA input details..
			objMS_RTA_INPUT.setUSER_ID(objMSUserDetails.getRcafId());
			objMS_RTA_INPUT.setENTRY_DATE(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			objMS_RTA_INPUT.setTRADE_DATE(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			objMS_RTA_INPUT.setPROCESS_DATE(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			objMS_RTA_INPUT.setNET_AMT(ConvertionUtil.convertToBigDecimal(objPaymentDetails.getTransfer_Amount()));
			objMS_RTA_INPUT.setACTION_KEY(objPaymentDetails.getRta_Action_key());
			objMS_RTA_INPUT.setREFERENCE_KEY(objPaymentDetails.getTransactionId());
			objMS_RTA_INPUT.setEXECUTION_TIME(new Timestamp(MSCommonUtils.getCurrentTime().getTime()));
			objMS_RTA_INPUT.setSETTLE_DATE(MSCommonUtils.getRTASettleDate(objPaymentDetails));
			objMS_RTA_INPUT.setEXT_STR(objPaymentDetails.getCurrent_owner_role());
			objMS_RTA_INPUT.setTXN_TYP(MSCommonUtils.getRTATxnTypeValue(objPaymentDetails));
			objMS_RTA_INPUT.setIN_STATUS(MSCommonUtils.getRTAInStatusFlag(objPaymentDetails.getRta_Action_key()));
			objMS_RTA_INPUT.setJRNL_DESC(MSCommonUtils.getRTAJrnlDesc(objPaymentDetails,objCheckRequest));
			objMS_RTA_INPUT.setFROM_ACCT_TRLR_1(MSCommonUtils.getRTAFrmAccTrlr1(txnDetails));
			objMS_RTA_INPUT.setTO_ACCT_TRLR_1(MSCommonUtils.getRTAToAccTrlr1(txnDetails));
			objMS_RTA_INPUT.setFROM_ACCT_TRLR_2(MSCommonUtils.getRTAFrmAccTrlr2(txnDetails));
			objMS_RTA_INPUT.setTO_ACCT_TRLR_2(MSCommonUtils.getRTAToAccTrlr2(txnDetails));
			objMS_RTA_INPUT.setEXT_STR2(objPaymentDetails.getIraTxnFlag());

			//Adding the RTA Input Object to the input vector...
			rtaInputVector.add(objMS_RTA_INPUT);
		} 
		catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
		EBWLogger.trace("RTAInterfaceIn", "Input Parameters for RTA transaction amount :"+rtaInputVector.toString());
		return rtaInputVector;
	}

	/**
	 * Setting the RTA Input Details for FEE...
	 * Swapping the From and To account details..
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public static Vector setRTAFeeRequest(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.logDebug("RTAInterfaceIn", "Setting the RTA Inputs for Fee Amount..");	
		Vector rtaInputVector = new Vector();
		try 
		{
			//Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//User details...
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//Check request mappings details..
			CheckRequestTO objCheckRequest = new CheckRequestTO();
			if(txnDetails.containsKey("CheckDetails")){
				objCheckRequest = (CheckRequestTO)txnDetails.get("CheckDetails");
			}

			//Input Mapping for the RTA Service...
			MS_RTA_INPUT objMS_RTA_INPUT = new MS_RTA_INPUT();

			//Mapping the account details..
			mapAccountingDetails(txnDetails,objMS_RTA_INPUT);

			//Mapping other RTA input details..
			objMS_RTA_INPUT.setUSER_ID(objMSUserDetails.getRcafId());
			objMS_RTA_INPUT.setENTRY_DATE(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			objMS_RTA_INPUT.setTRADE_DATE(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			objMS_RTA_INPUT.setPROCESS_DATE(ConvertionUtil.convertToTimestamp(objPaymentDetails.getRequestedDate()));
			objMS_RTA_INPUT.setNET_AMT(objPaymentDetails.getTxn_Fee_Charge());
			objMS_RTA_INPUT.setACTION_KEY(objPaymentDetails.getRta_Action_Key_Fee());
			objMS_RTA_INPUT.setREFERENCE_KEY(objPaymentDetails.getTransactionId());
			objMS_RTA_INPUT.setSETTLE_DATE(MSCommonUtils.getRTASettleDate(objPaymentDetails));
			objMS_RTA_INPUT.setEXT_STR(objPaymentDetails.getCurrent_owner_role());
			objMS_RTA_INPUT.setEXECUTION_TIME(new Timestamp(MSCommonUtils.getCurrentTime().getTime()));
			objMS_RTA_INPUT.setTXN_TYP(MSCommonUtils.getRTATxnTypeValue_Fee(objPaymentDetails));
			objMS_RTA_INPUT.setIN_STATUS(MSCommonUtils.getRTAInStatusFlag(objPaymentDetails.getRta_Action_key()));
			objMS_RTA_INPUT.setJRNL_DESC(MSCommonUtils.getRTAJrnlDesc(objPaymentDetails,objCheckRequest));
			objMS_RTA_INPUT.setFROM_ACCT_TRLR_1(MSCommonUtils.getRTAFrmAccTrlr1(txnDetails));
			objMS_RTA_INPUT.setTO_ACCT_TRLR_1(MSCommonUtils.getRTAToAccTrlr1(txnDetails));
			objMS_RTA_INPUT.setFROM_ACCT_TRLR_2(MSCommonUtils.getRTAFrmAccTrlr2(txnDetails));
			objMS_RTA_INPUT.setTO_ACCT_TRLR_2(MSCommonUtils.getRTAToAccTrlr2(txnDetails));
			objMS_RTA_INPUT.setEXT_STR2(objPaymentDetails.getIraTxnFlag());

			//Adding the RTA Input Object to the input vector...
			rtaInputVector.add(objMS_RTA_INPUT);
		} 
		catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
		EBWLogger.trace("RTAInterfaceIn", "Input Parameters to the Fee Amount RTA  :"+rtaInputVector.toString());
		return rtaInputVector;
	}

	/**
	 * Mapping the RTA accounting details based on RTA reversal flag. 
	 * If RTA Reversal flag is true , reversing the from and to account details..
	 * @param txnDetails
	 * @param objMS_RTA_INPUT
	 * @throws Exception
	 */
	public static void mapAccountingDetails(HashMap txnDetails,MS_RTA_INPUT objMS_RTA_INPUT) throws Exception
	{
		try
		{
			//Payment details...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//From account details..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//To account details...
			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			if(objPaymentDetails.isReverseRTAFlag()) {
				objMS_RTA_INPUT.setFROM_OFFICE(objToMSAcc_Details.getOfficeNumber());
				objMS_RTA_INPUT.setFROM_ACCT(objToMSAcc_Details.getAccountNumber());
				objMS_RTA_INPUT.setFROM_FA_NBR(objToMSAcc_Details.getFaNumber());
				objMS_RTA_INPUT.setTO_OFFICE(objFromMSAcc_Details.getOfficeNumber());
				objMS_RTA_INPUT.setTO_ACCT(objFromMSAcc_Details.getAccountNumber());
				objMS_RTA_INPUT.setTO_FA_NBR(objFromMSAcc_Details.getFaNumber());
			}
			else if(objPaymentDetails.isCancelRTAFlag()){
				objMS_RTA_INPUT.setFROM_OFFICE(objToMSAcc_Details.getOfficeNumber());
				objMS_RTA_INPUT.setFROM_ACCT(objToMSAcc_Details.getAccountNumber());
				objMS_RTA_INPUT.setFROM_FA_NBR(objToMSAcc_Details.getFaNumber());
				objMS_RTA_INPUT.setTO_OFFICE(objFromMSAcc_Details.getOfficeNumber());
				objMS_RTA_INPUT.setTO_ACCT(objFromMSAcc_Details.getAccountNumber());
				objMS_RTA_INPUT.setTO_FA_NBR(objFromMSAcc_Details.getFaNumber());
			}
			else {
				objMS_RTA_INPUT.setFROM_OFFICE(objFromMSAcc_Details.getOfficeNumber());
				objMS_RTA_INPUT.setFROM_ACCT(objFromMSAcc_Details.getAccountNumber());
				objMS_RTA_INPUT.setFROM_FA_NBR(objFromMSAcc_Details.getFaNumber());
				objMS_RTA_INPUT.setTO_OFFICE(objToMSAcc_Details.getOfficeNumber());
				objMS_RTA_INPUT.setTO_ACCT(objToMSAcc_Details.getAccountNumber());
				objMS_RTA_INPUT.setTO_FA_NBR(objToMSAcc_Details.getFaNumber());
			}
		}
		catch (Exception exception) {
			throw exception;
		}
	}
}
