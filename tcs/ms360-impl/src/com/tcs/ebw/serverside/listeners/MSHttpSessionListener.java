package com.tcs.ebw.serverside.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;
import javax.servlet.http.HttpSession;
import com.tcs.ebw.common.util.EBWLogger;

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
		try 
		{
			HttpSession session = mmSessionEvent.getSession();
			String mmSessionid = session.getId();
			Date currentDate = new Date();
			String message = new StringBuffer("Session destoyed on ").append(currentDate.toString()).append("\nID: ").append(mmSessionid).toString();
			System.out.println(message);
			EBWLogger.trace(this,message); 
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
