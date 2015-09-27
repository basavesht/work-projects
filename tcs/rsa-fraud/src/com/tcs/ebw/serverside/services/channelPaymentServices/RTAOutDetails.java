
package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.util.Vector;
import com.tcs.Payments.EAITO.MS_INTERFACE_TECH_FAILURE;
import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.mswitch.common.channel.DBProcedureChannel;
import com.tcs.mswitch.common.channel.SI_RETURN;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */ 
public class RTAOutDetails {

	/**
	 * RTA Call response extraction code...
	 * @param out
	 * @param context
	 * @throws Exception
	 */
	public static void checkRTACall(Vector<Object> out, ServiceContext context) throws Exception 
	{
		EBWLogger.logDebug("RTAOutDetails","RTA Extraction and mapping process");
		try 
		{
			//Interface Output attributes...
			SI_RETURN si_return = (SI_RETURN)out.get(0);

			//Output Extraction and storage...
			if(si_return.getReturnCode()!=0)
			{
				EBWLogger.logDebug("RTAOutDetails","The RTA call was failure....");
				for(int k=0;k<si_return.getErrorVector().size();k++){
					if(si_return.getErrorVector().get(k) instanceof MS_INTERFACE_TECH_FAILURE ){
						context.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR);
						break;
					}
				}
				context.setRTARollbackReq(true);

				//Following check is done to handle the RTA Rollback in case of multiple rollback within the same transaction scope...
				if(context.isCallRTAImmediateCommit()){
					System.out.println("Calling RTA Immediate ROLLBACK");
					DBProcedureChannel.rollback();

					//To maintain the consistency against other RTA commit within the transaction scope reset the data back again...
					context.setRTARollbackReq(false);
					context.setCallRTAImmediateCommit(false);
				}
			}
			else
			{
				context.setRTACommitReq(true); // Following value is set in the ServiceContext , to handle the RTA Commit in the EJB Bean class.

				//Following check is done to handle the RTA Commit in case of multiple commit within same transaction scope...
				if(context.isCallRTAImmediateCommit()){
					System.out.println("Calling RTA Immediate COMMIT");
					DBProcedureChannel.commit();

					//To maintain the consistency against other RTA commit within the transaction scope reset the data back again...
					context.setRTACommitReq(false);
					context.setCallRTAImmediateCommit(false);
				}
			}
		} 
		catch(Exception exception) {
			throw exception;
		}
	}
}
