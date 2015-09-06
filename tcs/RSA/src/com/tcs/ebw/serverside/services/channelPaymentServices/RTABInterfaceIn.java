/**
 * 
 */
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.HashMap;
import java.util.Vector;

import com.tcs.Payments.EAITO.MS_RTAB_INPUT;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.FromMSAcc_DetailsTO;
import com.tcs.ebw.payments.transferobject.MSUser_DetailsTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class RTABInterfaceIn{

	/**
	 * RTAB call will happen only for the MS Debit accounts only...
	 * @param txnDetails
	 * @return
	 * @throws Exception 
	 */
	public static Vector setRTABInputDetails(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.logDebug("RTABInterfaceIn", "Setting the RTAB Inputs..");
		Vector rtabInputVector = new Vector();
		try 
		{
			//Debit account details..
			FromMSAcc_DetailsTO objFromMSAcc_Details = new FromMSAcc_DetailsTO();
			if(txnDetails.containsKey("MSFromAccDetails")){
				objFromMSAcc_Details = (FromMSAcc_DetailsTO)txnDetails.get("MSFromAccDetails");
			}

			//User details..
			MSUser_DetailsTO objMSUserDetails = new MSUser_DetailsTO();
			if(txnDetails.containsKey("MSUserDetails")){
				objMSUserDetails = (MSUser_DetailsTO)txnDetails.get("MSUserDetails");
			}

			//RTAB Input mappings...
			MS_RTAB_INPUT objMS_RTAB_INPUTTO = new MS_RTAB_INPUT();
			objMS_RTAB_INPUTTO.setUSER_ID(objMSUserDetails.getRcafId());
			objMS_RTAB_INPUTTO.setAPPL_ID(WSDefaultInputsMap.getRTABAppID(serviceContext));
			objMS_RTAB_INPUTTO.setOFFICE_ID(objFromMSAcc_Details.getOfficeNumber());
			objMS_RTAB_INPUTTO.setACCOUNT_NUM(objFromMSAcc_Details.getAccountNumber());
			objMS_RTAB_INPUTTO.setFA_ID(objFromMSAcc_Details.getFaNumber());

			//Adding the RTAB Input Object to the Vector...
			rtabInputVector.add(objMS_RTAB_INPUTTO);	
		} 
		catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
		EBWLogger.logDebug("RTABInterfaceIn", "Input parameters for the RTAB is"+rtabInputVector.toString());
		return rtabInputVector;
	}
}
