package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.MS_ACCOUNT_OUT_DTL;
import com.tcs.Payments.EAITO.MS_PLA_INPUT;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.PaymentDetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       23-09-2011        P3-B            PLA  
 * **********************************************************
 */
public class PortfolioLoansInterfaceIn {

	public static Vector<Object> setPLAAccountsDtls(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.logDebug("PortfolioLoansInterfaceIn", "Setting the PLA Inputs..");
		Vector plaInputVector = new Vector();
		try 
		{
			//MSSB Accounts Details..
			ArrayList mssbAcntsInputDetails = new ArrayList();
			if(txnDetails.containsKey("PLAInput")){
				mssbAcntsInputDetails = (ArrayList)txnDetails.get("PLAInput");
			}

			//Payment Details ...
			PaymentDetailsTO objPaymentDetails = new PaymentDetailsTO();
			if(txnDetails.containsKey("PaymentDetails")){
				objPaymentDetails = (PaymentDetailsTO)txnDetails.get("PaymentDetails");
			}

			//PLA Input mappings...
			MS_PLA_INPUT objMS_PLA_INPUTTO = new MS_PLA_INPUT();
			objMS_PLA_INPUTTO.setREQUEST_HOST(WSDefaultInputsMap.getPLARequestHost(serviceContext));
			objMS_PLA_INPUTTO.setSUBMIT_TIME(new Timestamp(new Date().getTime()).toString());
			objMS_PLA_INPUTTO.setSIGN_ON_RACF_ID(WSDefaultInputsMap.getPLAUserID(serviceContext));
			objMS_PLA_INPUTTO.setAPPL_ID(WSDefaultInputsMap.getPLAAppID(serviceContext));

			//Adding MSSB Account mappings ...
			MS_PLA_INPUT.SBLACCOUNT_IN_MSG_INFO objSBLAccountMsgInfoArr[] = new MS_PLA_INPUT.SBLACCOUNT_IN_MSG_INFO[50];
			int i = 0;
			if(!mssbAcntsInputDetails.isEmpty()) {
				for( int j=0; j<mssbAcntsInputDetails.size() ;j++)
				{
					if(mssbAcntsInputDetails.get(j) instanceof MS_ACCOUNT_OUT_DTL){
						MS_ACCOUNT_OUT_DTL objMMAccount = (MS_ACCOUNT_OUT_DTL)mssbAcntsInputDetails.get(j);
						MS_PLA_INPUT.SBLACCOUNT_IN_MSG_INFO objSBLAccountMsgInfo = objMS_PLA_INPUTTO.new SBLACCOUNT_IN_MSG_INFO();
						objSBLAccountMsgInfo.setOFFICE(objMMAccount.getOFFICE());
						objSBLAccountMsgInfo.setACCT_NO(objMMAccount.getACCOUNT_NO());
						objSBLAccountMsgInfo.setKEY_ACCT(objMMAccount.getKEY_ACCOUNT_NO());
						objSBLAccountMsgInfoArr[i] = objSBLAccountMsgInfo;
						i++;
					}
				}
			}
			else {
				MS_PLA_INPUT.SBLACCOUNT_IN_MSG_INFO objSBLAccountMsgInfo = objMS_PLA_INPUTTO.new SBLACCOUNT_IN_MSG_INFO();
				objSBLAccountMsgInfo.setLOAN_NO(objPaymentDetails.getLoanAcntNo());
				objSBLAccountMsgInfoArr[i] = objSBLAccountMsgInfo;
			}
			objMS_PLA_INPUTTO.setMS_SBLACCOUNT_IN_MSG_INFO(objSBLAccountMsgInfoArr);

			//Adding the PLA Input Object to the Vector...
			plaInputVector.add(objMS_PLA_INPUTTO);		
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.logDebug("PortfolioLoansInterfaceIn", "Input parameters for the PLA is"+plaInputVector.toString());
		return plaInputVector;
	}
}
