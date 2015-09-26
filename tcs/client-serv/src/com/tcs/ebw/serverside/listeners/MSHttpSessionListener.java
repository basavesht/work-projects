package com.tcs.ebw.serverside.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import com.tcs.Payments.ms360Utils.MSCommonUtils;
import com.tcs.bancs.channels.context.MessageType;
import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.ebw.common.context.EBWAppContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.transferobject.SecurityCheckTO;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class MSHttpSessionListener implements HttpSessionListener {

	public MSHttpSessionListener() {
	}

	public void sessionCreated(HttpSessionEvent mmSessionEvent) 
	{
		EBWLogger.trace(this,"Entered the Session Created method.."); 
		try 
		{
			HttpSession session = mmSessionEvent.getSession();
			String mmSessionid = session.getId();
			Date currentDate = new Date();
			String message = new StringBuffer("New Session created on ").append(currentDate.toString()).append("\nID: ").append(mmSessionid).toString();
			System.out.println(message);
			EBWLogger.trace(this,message); 
		} 
		catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public void sessionDestroyed(HttpSessionEvent mmSessionEvent) 
	{
		EBWLogger.trace(this,"Entered the Session Destroyed Method.."); 
		HttpSession session = mmSessionEvent.getSession();
		String mmSessionid = session.getId();
		String mmSecureId = (String)session.getAttribute("MMSecureId");
		String mmuuId = (String)session.getAttribute("MMUUID");
		try 
		{
			//Not a valid MM Session
			if(mmSecureId == null || mmuuId == null){
				return;
			}

			//Mapping for the Security Check TO..
			SecurityCheckTO objSecurityCheckTO = new SecurityCheckTO();
			objSecurityCheckTO.setSessionId(mmSessionid);
			objSecurityCheckTO.setSecurityTokenId(mmSecureId);

			//Mapping for the UserPrincipal Check TO..
			UserPrincipal objUserPrincipal = new UserPrincipal();
			objUserPrincipal.setSessionId(mmSessionid);
			objUserPrincipal.setUuid(mmuuId);
			objUserPrincipal.setSecurityTokenId(mmSecureId);

			String strStatement = "" ;
			Object objTOParam[] = {objSecurityCheckTO};

			Object objParams[]={strStatement,objTOParam,new Boolean(false)};
			Class clsParamTypes[]={String.class,Object[].class,Boolean.class,};

			//Service Call.....
			EBWAppContext appContext=new EBWAppContext();
			appContext.setUserPrincipal(objUserPrincipal);
			EBWLogger.logDebug(this,"UserPrincipal"+appContext.getUserPrincipal().toString());

			IEBWService objService = EBWServiceFactory.create("deleteMMSessionEntries",appContext);
			Object objReturn = objService.execute(clsParamTypes, objParams);

			ArrayList mmSessionOut = (ArrayList)objReturn;
			if(mmSessionOut!=null && !mmSessionOut.isEmpty())
			{
				ServiceContext contextData = (ServiceContext)mmSessionOut.get(0);

				//Response...
				MSCommonUtils.logEventResponse(contextData);
				if(contextData.getMaxSeverity()==MessageType.CRITICAL){
					String message = new StringBuffer("Session could not be destroyed because of Technical Error.." + "\nValue of destroyed session ID is").append("" + appContext.getUserPrincipal().getSessionId()).toString();
					System.out.println(message);
					EBWLogger.logDebug(this,message); 
				}
				else {
					String message = new StringBuffer("Session destroyed" + "\nValue of destroyed session ID is").append("" + appContext.getUserPrincipal().getSessionId()).toString();
					System.out.println(message);
					EBWLogger.logDebug(this,message); 
				}
			}
		} 
		catch (Exception e) 
		{
			String message = "Session could not be destroyed because of Technical Error...\n" + "mmSessionId = " + mmSessionid + "\n" + "mmSecureId = " + mmSecureId + "\n" + "mmuuId = " + mmuuId + "\n" + 
			e.getMessage() ; 
			System.out.println(message); 
			EBWLogger.logDebug(this, message); 
			e.printStackTrace();
		}
	}
}
