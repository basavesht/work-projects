package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.MS_MERLIN_INP;
import com.tcs.Payments.ms360Utils.ChkReqConstants;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;
import com.tcs.ebw.payments.transferobject.ToMSAcc_DetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MerlinInterfaceIn {

	/**
	 * Setting the Merlin Input Details...
	 * @param txnDetails
	 * @return
	 * @throws Exception
	 */
	public static Vector setMerlinInputDetails(HashMap txnDetails,ServiceContext serviceContext) throws Exception 
	{
		EBWLogger.logDebug("MerlinInterfaceIn", "Setting the Merlin Inputs..");	
		Vector merlinInputVector = new Vector();
		try
		{
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			ToMSAcc_DetailsTO objToMSAcc_Details = new ToMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSToAccDetails")){
				objToMSAcc_Details = (ToMSAcc_DetailsTO)txnDetails.get("MSToAccDetails");
			}

			String transfer_Type = objPaymentDetails.getTransfer_Type();

			// MERLIN Input Mappings for From account
			MS_MERLIN_INP objFromMS_MERLIN_INPUT  = new MS_MERLIN_INP ();
			objFromMS_MERLIN_INPUT.setACNT_NUM(objFromMSAcc_Details.getAccountNumber());
			objFromMS_MERLIN_INPUT.setEXT_STR("001");
			objFromMS_MERLIN_INPUT.setFANO(objFromMSAcc_Details.getFaNumber());
			objFromMS_MERLIN_INPUT.setKEY_ACNT(objFromMSAcc_Details.getKeyAccount());
			objFromMS_MERLIN_INPUT.setOFFICE_ACNT(objFromMSAcc_Details.getOfficeNumber());
			objFromMS_MERLIN_INPUT.setRMRKS_L01("Remarks");
			objFromMS_MERLIN_INPUT.setAPPL_ID(WSDefaultInputsMap.getAcntViewAppID(serviceContext));
			objFromMS_MERLIN_INPUT.setAUTH_APPL_ID(WSDefaultInputsMap.getAcntViewAuthAppID(serviceContext));
			objFromMS_MERLIN_INPUT.setAUTH_VERB(WSDefaultInputsMap.getAcntViewAuthVerb(serviceContext));
			objFromMS_MERLIN_INPUT.setUSER_ID(WSDefaultInputsMap.getAcntViewUserID(serviceContext));
			objFromMS_MERLIN_INPUT.setWEB_SERVER_ID(WSDefaultInputsMap.getAcntViewWebServerID(serviceContext));

			// MERLIN Input Mappings for TO account
			MS_MERLIN_INP objToMS_MERLIN_INPUT  = new MS_MERLIN_INP ();
			objToMS_MERLIN_INPUT.setACNT_NUM(objToMSAcc_Details.getAccountNumber());
			objToMS_MERLIN_INPUT.setEXT_STR("001");
			objToMS_MERLIN_INPUT.setFANO(objToMSAcc_Details.getFaNumber());
			objToMS_MERLIN_INPUT.setKEY_ACNT(objToMSAcc_Details.getKeyAccount());
			objToMS_MERLIN_INPUT.setOFFICE_ACNT(objToMSAcc_Details.getOfficeNumber());
			objToMS_MERLIN_INPUT.setRMRKS_L01("Remarks");
			objToMS_MERLIN_INPUT.setAPPL_ID(WSDefaultInputsMap.getAcntViewAppID(serviceContext));
			objToMS_MERLIN_INPUT.setAUTH_APPL_ID(WSDefaultInputsMap.getAcntViewAuthAppID(serviceContext));
			objToMS_MERLIN_INPUT.setAUTH_VERB(WSDefaultInputsMap.getAcntViewAuthVerb(serviceContext));
			objToMS_MERLIN_INPUT.setUSER_ID(WSDefaultInputsMap.getAcntViewUserID(serviceContext));
			objToMS_MERLIN_INPUT.setWEB_SERVER_ID(WSDefaultInputsMap.getAcntViewWebServerID(serviceContext));

			//Adding the Merlin Input Object to the Vector...
			if(transfer_Type!=null && transfer_Type.equalsIgnoreCase(TxnTypeCode.INTERNAL)){
				merlinInputVector.add(objFromMS_MERLIN_INPUT);
				if(objToMSAcc_Details.isAcntViewCallReq()){
					merlinInputVector.add(objToMS_MERLIN_INPUT);
				}
			}
			else if(transfer_Type!=null && transfer_Type.startsWith(ChkReqConstants.CHK)){
				merlinInputVector.add(objFromMS_MERLIN_INPUT);
			}
			else if(transfer_Type!=null && transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL)){
				merlinInputVector.add(objFromMS_MERLIN_INPUT);
			}
			else if(transfer_Type!=null && transfer_Type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT)){
				merlinInputVector.add(objToMS_MERLIN_INPUT);
			}
		} 
		catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
		EBWLogger.logDebug("MerlinInterfaceIn", "Input parameters for the Merlin :"+merlinInputVector.toString());	
		return merlinInputVector;
	}

}
