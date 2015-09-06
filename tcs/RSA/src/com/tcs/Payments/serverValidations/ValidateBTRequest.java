package com.tcs.Payments.serverValidations;

import com.tcs.Payments.EAITO.MS_BOTTOM_LINE_ASYNC_IN;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class ValidateBTRequest 
{
	public void validateBTRequest(MS_BOTTOM_LINE_ASYNC_IN asynchBTRequest,ServiceContext serviceContext) throws Exception
	{
		EBWLogger.trace(this, "Starting validateRSARequest method in ValidateRSARequest class");
		try 
		{
			if(asynchBTRequest!=null) 
			{
				//Extract Confirmation Number.... 
				String txn_conf_number = "";
				if(asynchBTRequest.getSPOOL_JOB_NAME()!=null){
					String strArr[] = asynchBTRequest.getSPOOL_JOB_NAME().trim().split("_");
					if (strArr.length > 0 && (strArr[0]!=null && !strArr[0].equalsIgnoreCase(""))){
						txn_conf_number = new String(strArr[0]);			
					}
				}

				if(txn_conf_number!=null && !txn_conf_number.trim().equalsIgnoreCase("")){
					EBWLogger.trace(this, "BT Request is Valid...");
				}
				else {
					EBWLogger.trace(this, "BT Request is Invalid...");
					serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.BT_REQUEST_VALIDATE_ERR);
				}
			}
			else {
				EBWLogger.trace(this, "BT Request is Invalid...");
				serviceContext.addMessage(MessageType.SEVERE,ChannelsErrorCodes.BT_REQUEST_VALIDATE_ERR);
			}
		}
		catch (Exception exception) {
			throw exception;
		}
	}
}
