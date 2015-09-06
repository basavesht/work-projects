/*
 * Created on Jan 10, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.common.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;

import com.tcs.ebw.common.util.PropertyFileReader;

/**
 * @author 152820
 *
 * This class is used to give a generic logging mechanism. This avoids
 * the use of System.out.println for debug messages. Advantage of using
 * logging instead of System.out is that, we can disable the debug messages
 * at deployment time just by changing the debug flag in log4.property file
 * to false. Debug can be enabled at development time. 
 */
public class EBWLogger {
	
	private static boolean debugEnabled =false;
	
	private static boolean infoEnabled =false;
	
	private static ResourceBundle ebwconfigProperties=null;
	
	static{
		try{
			//EBWLogger.trace("EBWLogger","Entering static block");
			//System.out.println("EBWLogger - Entering static block");
			ebwconfigProperties = ResourceBundle.getBundle("ebw");
			
			System.out.println("EBWLogger" +"ebwconfigProperties in static is :"+ebwconfigProperties);
			String log4jpropertiespath = ebwconfigProperties.getString("log4j_properties_path");
			String log4jpropertiesfile = ebwconfigProperties.getString("log4j_properties_file");
			
			if(!log4jpropertiespath.endsWith("/"))
				log4jpropertiespath+="/";
			
			//System.out.println("log4j.configuration"+"file:"+log4jpropertiespath+log4jpropertiesfile);
			System.setProperty("log4j.defaultInitOverride", "true");
			InputStream is = new FileInputStream(new File(log4jpropertiespath+log4jpropertiesfile));
			Properties p = new Properties();
			p.load(is);
			PropertyConfigurator.configure(p);
			System.setProperty("log4j.configuration","file:"+log4jpropertiespath+log4jpropertiesfile);
			//System.out.println("defaultInitOverride: " +System.getProperty("log4j.defaultInitOverride"));
			//System.out.println("log4j.configuration:"+ System.getProperty("log4j.configuration"));
			
		//	debugEnabled = Boolean.parseBoolean(PropertyFileReader.getProperty("debugEnabled"));
		//	infoEnabled = Boolean.parseBoolean(PropertyFileReader.getProperty("infoEnabled"));
		}catch(Exception e){
			e.printStackTrace();
			
		}
//		System.out.println("EBWLogger"+"Finished static block");
	}
	
	/**
	 * Variable to store the logger instance.
	 */
	private static Logger log = null;
	

	
	/**
	 * This method is used to print log information to target output as 
	 * specified in log4j.properties file. Please refer log4j documentation 
	 * - logging.apache.org/log4j/docs for further explanation of log4j.
	 * It can print messages on console as well as create a log file for
	 * future reference. 
	 *    
	 * @param classIns - Instance of the class from where logDebug is called. 
	 * 					 'this' keywork can be passed to this argument.  
	 * @param msg      - Debug message string to be displayed.  
	 */
	public static void logDebug(Object classIns,String msg){
		 if(classIns.getClass().getName().indexOf("channelPaymentServices") != -1)
	            System.out.println(msg);
	        else
	        if(classIns.getClass().getName().indexOf("com.tcs.ebw.Payments") != -1)
	            System.out.println(msg);

	    log = Logger.getLogger(classIns.getClass().getName());
		if(log.isDebugEnabled()){
			log.debug(msg);
		}
	}
	
	
	/**
	 * This method is used to print log information to target output as 
	 * specified in log4j.properties file. Please refer log4j documentation 
	 * - logging.apache.org/log4j/docs for further explanation of log4j.
	 * It can print messages on console as well as create a log file for
	 * future reference. This method should be used only in static methods. 
	 * 						
	 * @param className - Name of the class from where logDebug is called. 
	 * 					   
	 * @param msg      - Debug message string to be displayed.  
	 */
	public static void logDebug(String className,String msg){
		if(className.indexOf("PaymentsUtility") != -1)
            System.out.println(msg);
        else
        if(className.indexOf("DateFunctions") != -1)
            System.out.println(msg);
        else
        if(className.indexOf("DateFunctions") != -1)
            System.out.println(msg);
        else
            if(className.indexOf("RSAUtilities") != -1)
                System.out.println(msg);

		log = Logger.getLogger(className);
		if(log.isDebugEnabled())
		    log.debug(msg);
	}
	
	/**
	 * This method is used to display error messages. Error messages can be
	 * redirected to a separate log file or can be forwarded as an email
	 * based on the requirement.
	 * 
	 * @param classIns 	- Instance of the class from where logDebug is called. 
	 * 					 'this' keywork can be passed to this argument.
	 * @param msg		- Error message/code string. 
	 */
	public static void logError(Object classIns,String msg){
		//log = Logger.getLogger(classIns.getClass().getName());
		log = Logger.getLogger("EBWError");
		log.error(msg);
	}
	
	public static void logError(String classIns,String msg){
		log = Logger.getLogger("EBWError");
		//log = Logger.getLogger(classIns);
			log.error(msg);
	}

	/**
	 * This method should be used for tracing in the begining and end of every method implementation.
	 * This method can be used for tracing all not static method implementations. 
	 * @param classIns - Instance of the class from where trace is being called.
	 * @param msg      - Trace message.
	 */
	public static void trace(Object classIns,String msg){
		log = Logger.getLogger("EBWTracer");
		if(log.isInfoEnabled())
			log.info(classIns.getClass().getName()+" - "+msg);
	}
	
	/**
	 * This method should be used for tracing in the begining and end of every method implementation.
	 * This prototype can be used in static methods where we cannot pass 'this'.
	 * @param classIns 	- Instance of the class from where trace is being called.
	 * @param msg 		- Trace message.
	 */
	public static void trace(String classIns,String msg){
		log = Logger.getLogger(classIns);
		if(log.isInfoEnabled())
			log.info(msg);
		//logDebug(classIns,msg);
	}
	
	public static void logFatal(String classIns,String msg){
		log = Logger.getLogger(classIns);
			log.fatal(msg);
	}
	
	public static void logWarn(String classIns,String msg){
		log = Logger.getLogger(classIns);
			log.warn(msg);
		
	}
	
	public static Logger getInstance(String classIns){
	    return Logger.getLogger(classIns);
	}


	public static boolean isDebugEnabled() {
		return debugEnabled;
	}


	public static void setDebugEnabled(boolean debugEnabled) {
		EBWLogger.debugEnabled = debugEnabled;
	}


	public static boolean isInfoEnabled() {
		return infoEnabled;
	}


	public static void setInfoEnabled(boolean infoEnabled) {
		EBWLogger.infoEnabled = infoEnabled;
	}
	
}

