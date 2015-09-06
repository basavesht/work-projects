package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.Vector;

import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.mswitch.common.channel.SI_RETURN;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class NotifyOutDetails {

	/**
	 * Notification details extraction code...
	 * @param out
	 * @param context
	 * @throws Exception
	 */
	public static void checkNotificationAck(Vector<Object> out, ServiceContext context) throws Exception 
	{
		try 
		{
			//Interface Output attributes...
			SI_RETURN si_return = (SI_RETURN)out.get(0);

			//Output Extraction and storage...
			if(si_return.getReturnCode()!=0){
				for(int k=0;k<si_return.getErrorVector().size();k++){
					if(si_return.getErrorVector().get(k) instanceof MS_INTERFACE_TECH_FAILURE ){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
						break;
					}
				}
			}
		} 
		catch (Exception exception) {
			throw exception;
		}	
	}
}
