package com.tcs.ebw.serverside.services.channelPaymentServices;

import java.sql.SQLException;
import java.util.ArrayList;

import com.tcs.bancs.channels.ChannelsErrorCodes;
import com.tcs.bancs.channels.context.Message;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.services.DatabaseService;
import com.tcs.ebw.transferobject.SecurityCheckTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class DestroyMMSession extends DatabaseService{

	public DestroyMMSession() {

	}

	/**
	 * Method that will be invoked on the session invalid using the HTTPSession Listener (Destroy method)...
	 * @param stmntId
	 * @param toObjects
	 * @param boolean1
	 * @throws Exception
	 * @throws SQLException
	 */
	public ArrayList destroySessionEntry(String stmntId,Object toObjects[],Boolean boolean1)
	{
		EBWLogger.trace(this, "Starting destroySessionEntry method in DestroyMMSession class");
		ServiceContext serviceContext = new ServiceContext();
		ArrayList destroyMMSession = new ArrayList();
		try
		{
			SecurityCheckTO securityCheckTO = (SecurityCheckTO)toObjects[0];

			//Deleting the MM Session entries from the SECURITY_CHECK Table...
			deleteMMSessionEntries(securityCheckTO);

			EBWLogger.trace(this, "Exiting destroySessionEntry method in DestroyMMSession class");
			serviceContext.setServiceCallSuccessful(true);
		}
		catch(SQLException sqlexception){
			sqlexception.printStackTrace();
			serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(sqlexception));
		}
		catch(Exception exception){
			exception.printStackTrace();
			serviceContext.addMessage(MessageType.CRITICAL,ChannelsErrorCodes.INTERNAL_ERROR,Message.getExceptionMessage(exception));
		}
		finally{

		}
		destroyMMSession.add(serviceContext);
		return destroyMMSession;
	}

	/**
	 * /Deleting the MMSession Entries from the SECURITY_CHECK Table..
	 * @param securityCheck
	 * @throws Exception 
	 */
	public void deleteMMSessionEntries(SecurityCheckTO securityCheck) throws Exception
	{
		try 
		{
			Boolean boolean1 = Boolean.TRUE;
			String stmntId = "deleteMMSessionEntry";

			EBWLogger.logDebug(this,"Executing.... "+stmntId);
			execute(stmntId,securityCheck,boolean1);
			EBWLogger.logDebug(this,"Execution Completed.... "+stmntId);
		} 
		catch (SQLException exception) {
			exception.printStackTrace();
			throw exception;
		} 
		catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
}
