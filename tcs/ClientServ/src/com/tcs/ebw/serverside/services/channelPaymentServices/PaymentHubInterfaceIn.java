/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.MS_ACCOUNT_OUT_DTL;
import com.tcs.Payments.ms360Utils.RTAActionKeyDesc;
import com.tcs.Payments.ms360Utils.TxnTypeCode;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.DsBatchTO;
import com.tcs.ebw.payments.transferobject.DsPayTxnsTO;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class PaymentHubInterfaceIn {

	public PaymentHubInterfaceIn(){

	}

	/**
	 * Mapping input details for the Payment Hub call...
	 * @param txnDetails
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Vector setPaymentHubParams_Transfer(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace("PaymentHubInterfaceIn", "Setting the Payments Hub required input details...");
		Vector paymentHubInputVec = new Vector();
		try 
		{
			//Mapping Payment attributes...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}
			String prevStatusCode = objPaymentDetails.getTxnPrevStatusCode(); // Transaction Status before modification..
			String transfer_type = objPaymentDetails .getTransfer_Type(); //Transfer type..
			String prev_rta_booked_in_flag = objPaymentDetails.getRta_booked_flag(); // Previous RTA booked in flag...
			String payAccType = null;

			//Mapping Account View output details..
			AccountDetails objAccDetails= (AccountDetails)txnDetails.get("MerlinOutputDetails");
			ArrayList responseMerlin=(ArrayList)objAccDetails.getInfoMerlinObject();
			for(int i=0;i<responseMerlin.size();i++)
			{
				if(responseMerlin.get(i) instanceof MS_ACCOUNT_OUT_DTL)
				{
					MS_ACCOUNT_OUT_DTL objMS_ACCOUNT_OUT_DTL =(MS_ACCOUNT_OUT_DTL)responseMerlin.get(i);
					payAccType = objMS_ACCOUNT_OUT_DTL.getMS_BUSINESS_ACC(); // Required only if the transfer type is ACH-IN or ACH-OUT (Business or personal), where only 1 internal MS account is involved..
					paymentHubInputVec.add(objMS_ACCOUNT_OUT_DTL);
				}
			}

			//Mapping DS_BATCH attributes..
			DsBatchTO currentTxnBatchTO = new DsBatchTO();
			if(txnDetails.containsKey("Current_DsBatchObject")){
				currentTxnBatchTO = (DsBatchTO)txnDetails.get("Current_DsBatchObject");
			}

			//Mapping DS_PAY_TXNS attributes...
			DsPayTxnsTO currentTxnPaymentTO = new DsPayTxnsTO();
			if(txnDetails.containsKey("Current_DsPayTxnObject"))
			{
				currentTxnPaymentTO = (DsPayTxnsTO)txnDetails.get("Current_DsPayTxnObject");

				//This flag stores the Internal MS account type (Personal or Business) . Further required in NACHA File Mappings..
				if(transfer_type!=null && (transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_DEPOSIT) || transfer_type.equalsIgnoreCase(TxnTypeCode.ACH_WITHDRAWAL))){
					currentTxnPaymentTO.setPayacctype(payAccType);
				}

				//This flag indicates to remove the hold from the RTA for the Payments Hub...
				if(prev_rta_booked_in_flag!=null && prev_rta_booked_in_flag.equalsIgnoreCase(RTAActionKeyDesc.RTA_PENDING)) {
					if(prevStatusCode!=null && (prevStatusCode.equalsIgnoreCase("2") || prevStatusCode.equalsIgnoreCase("80"))){
						currentTxnPaymentTO.setFLG_ATTR1("1"); 
					}
				}
			}

			//Adding the Payments Hub attributes...
			paymentHubInputVec.add(currentTxnBatchTO);
			paymentHubInputVec.add(currentTxnPaymentTO);
		} 
		catch (Exception exception){
			throw exception;
		}
		return paymentHubInputVec;
	}

	/**
	 * Mapping input details for the Payments Hub call in case of Trial Deposits..
	 * @param txnDetails
	 * @param serviceContext
	 * @return
	 * @throws Exception
	 */
	public static Vector setPaymentHubParams_TrialDepo(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace("PaymentHubInterfaceIn", "Setting the Payments Hub required input details in case of TRIAL Depo...");
		Vector paymentHubInputVec = new Vector();
		try 
		{
			//Mapping DS_BATCH attributes..
			DsBatchTO currentTxnBatchTO = new DsBatchTO();
			if(txnDetails.containsKey("Current_DsBatchObjTrial")){
				currentTxnBatchTO = (DsBatchTO)txnDetails.get("Current_DsBatchObjTrial");
			}

			//Mapping DS_PAY_TXNS attributes for the trial deposit...
			DsPayTxnsTO currentTxnPaymentTO1 = new DsPayTxnsTO();
			if(txnDetails.containsKey("Current_DsPayTxnObjTrial1")){
				currentTxnPaymentTO1 = (DsPayTxnsTO)txnDetails.get("Current_DsPayTxnObjTrial1");
			}

			//Mapping DS_PAY_TXNS attributes for the trial deposit...
			DsPayTxnsTO currentTxnPaymentTO2 = new DsPayTxnsTO();
			if(txnDetails.containsKey("Current_DsPayTxnObjTrial2")){
				currentTxnPaymentTO2 = (DsPayTxnsTO)txnDetails.get("Current_DsPayTxnObjTrial2");
			}

			//Adding the Payments Hub attributes...
			paymentHubInputVec.add(currentTxnBatchTO);
			paymentHubInputVec.add(currentTxnPaymentTO1);
			paymentHubInputVec.add(currentTxnPaymentTO2);
		} 
		catch (Exception exception) {
			throw exception;
		}
		return paymentHubInputVec;
	}
}
