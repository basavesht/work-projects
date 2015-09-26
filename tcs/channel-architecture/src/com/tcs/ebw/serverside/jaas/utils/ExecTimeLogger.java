package com.tcs.ebw.serverside.jaas.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;

import org.apache.log4j.Logger;
import org.apache.log4j.jdbc.JDBCAppender;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.query.Prepopulator;
import com.tcs.ebw.serverside.query.QueryExecutor;

/**
 * @author 231259
 * @year 2009
 */
@SuppressWarnings(value="unchecked")
public class ExecTimeLogger {
	

	public void logExecTime(String threadId,String userId, String sessionId, String serviceId, Date startTime, Date endTime, String isExtLogged) {
		EBWLogger.trace(this, "Inserting Execution time");
		EBWLogger.trace(this, "Inserting Execution time");
		EBWLogger.trace(this, "userId: [" + userId +"]");
		EBWLogger.trace(this, "sessionId: [" + sessionId +"]");
		EBWLogger.trace(this, "serviceId: [" + serviceId +"]");
		EBWLogger.trace(this, "startTime: [" + startTime.toString() +"]");
		EBWLogger.trace(this, "endTime: [" + endTime.toString() +"]");
		EBWLogger.trace(this, "isExtLogged: [" + isExtLogged +"]");
		
		try {
			ExecTimeLoggerHelper execTimeLoggerHelper = new ExecTimeLoggerHelper();
			execTimeLoggerHelper.setThreadId(threadId);
			execTimeLoggerHelper.setUserId(userId);
			execTimeLoggerHelper.setSessionId(sessionId);
			execTimeLoggerHelper.setServiceId(serviceId);
			execTimeLoggerHelper.setStartTime(new SimpleDateFormat("dd-MMM-yy hh:mm:ss:S a").format(startTime));
			execTimeLoggerHelper.setEndTime(new SimpleDateFormat("dd-MMM-yy hh:mm:ss:S a").format(endTime));
			execTimeLoggerHelper.setIsExtLogged(isExtLogged);
			
			String loggingMethod = "Service";
			try
			{
				loggingMethod = PropertyFileReader.getProperty("execTimeLogger_method");
			}
			catch(MissingResourceException msre)
			{
				EBWLogger.logError(this, "caught missing resource exception--"+msre );
				
			}
			
			if(loggingMethod.equalsIgnoreCase("log4j"))
			{
				insExecTimeThroughLog4j(execTimeLoggerHelper);
			}
			else
			{
				Class clsInsCD[] = {String.class,Object.class, Boolean.class};
				Object objInsCD[] = {"insExecTime",execTimeLoggerHelper, Boolean.valueOf(false)};				
				IEBWService objService = EBWServiceFactory.create("insExecTime");				
				objService.execute(clsInsCD, objInsCD);
			}
			
			EBWLogger.trace(this, "Inserted Execution time successfully");
			EBWLogger.trace(this, "Inserted Execution time successfully");
		} catch(Exception e) {
			e.printStackTrace();
			EBWLogger.logError(this, "Exception in inserting execution time. Message:  " + e.getMessage());
		}
	}
	
	private void insExecTimeThroughLog4j(ExecTimeLoggerHelper execTimeLoggerHelper)throws Exception
	{
		EBWLogger.trace(this, "Into insExecTimeThroughLog4j() method --- ");
		EBWLogger.trace(this, "Thread id is "+execTimeLoggerHelper.getThreadId());
		
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append(execTimeLoggerHelper.getThreadId()).append("~").append(execTimeLoggerHelper.getUserId()).append("~").append(execTimeLoggerHelper.getSessionId()).append("~")
		.append(execTimeLoggerHelper.getStartTime()).append("~").append(execTimeLoggerHelper.getEndTime()).append("~").append(execTimeLoggerHelper.getServiceId());
		
		String strLog4j = strBuilder.toString();
		EBWLogger.logDebug(this, strLog4j);
	} 
	
}
