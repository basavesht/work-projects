package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.HashMap;
import java.util.Vector;
import com.tcs.Payments.EAITO.MS_ACNT_PLATING_INP;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.payments.transferobject.MSAcntPlatingDetails;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class AcntPlatingInterfaceIn {

	/**
	 * Account Plating interface detail mappings...
	 * @param txnDetails
	 * @return
	 * @throws Exception 
	 */
	public static Vector setAcntPlatingInputDetails(HashMap txnDetails,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.logDebug("AcntPlatingInterfaceIn", "Setting the Account Plating Inputs..");
		Vector acntPlatingInputVector = new Vector();
		try 
		{
			MSAcntPlatingDetails objMSAccPlatingDtls = new MSAcntPlatingDetails();
			if(txnDetails.containsKey("MSAcntPlatingInDetails")){
				objMSAccPlatingDtls = (MSAcntPlatingDetails)txnDetails.get("MSAcntPlatingInDetails");
			}

			//Account Plating attributes Input mappings...
			MS_ACNT_PLATING_INP objMs_acnt_plating = new MS_ACNT_PLATING_INP();
			objMs_acnt_plating.setACCOUNT_NUMBER(objMSAccPlatingDtls.getAccount_number());
			objMs_acnt_plating.setFA(objMSAccPlatingDtls.getFa());
			objMs_acnt_plating.setOFFICE(objMSAccPlatingDtls.getOffice());
			objMs_acnt_plating.setSERVER_IP(WSDefaultInputsMap.getAcntPlatingServerIP(serviceContext));
			objMs_acnt_plating.setAPPL_ID(WSDefaultInputsMap.getAcntPlatingAppID(serviceContext));
			objMs_acnt_plating.setUSERD_ID(WSDefaultInputsMap.getAcntPlatingUserID(serviceContext));

			//Adding the Account Plating Input Object to the Vector...
			acntPlatingInputVector.add(objMs_acnt_plating);	
		} 
		catch (Exception exception) {
			throw exception;
		}
		EBWLogger.logDebug("AcntPlatingInterfaceIn", "Input parameters for the Account plating is"+acntPlatingInputVector.toString());
		return acntPlatingInputVector;
	}
}
