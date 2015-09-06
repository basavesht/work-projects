/*
 * Created on Oct 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.config.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Locale;
import org.apache.log4j.Logger;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.exception.JAASLoginConnNotFoundException;
/**
 * @author  - arun.shanmugham@tcs.com
 * @version - 1.1 (Stack trace included in logging)
 *            Some more exception classes included.
 *  
 *  This class is used in all our application classes to catch exeption
 *  in a common place and log it in appropriate log file based on the
 *  type of message whether its error, debug or fatal message.
 */
public class EbwException extends Exception {
	
	/**
	 *  Resource bundle object which stores the information about property file
	 *  which has key and value for user defined error/exception messages.
	 *
	 */
	private static ResourceBundle bundle = null;
	
	/**
	 * Variable to store the locale specific information. Based on this object
	 * value, the system will load the appropriate resource bundle for that
	 * language.
	 */
	private Locale locale = null;
	
	/**
	 * Variable to store actual error message retrieved from eigther the
	 * resource bundle or from the exception object to be displyed to 
	 * end user.
	 */
	private String errorStr = null;
	
	/**
	 * Application Object which is raising the error. 
	 */
	private Object errObj = null;
	
	/**
	 * Stores the type of exception object based on the Exception. Since all
	 * exceptions are derived from this class, we have used it to capture
	 * all exceptions. 
	 */
	private Exception expObj =null;
	
	private static String errClassName=null;
	
	/**
	 * Log4j object used for logging the error /exception information to the
	 * server based on the output type (rolling file/mail etc). 
	 */
	private Logger log; 
	
	static{
	    Locale locale = Locale.getDefault();
		bundle = ResourceBundle.getBundle("com.tcs.ebw.exception.EBWErrors",locale);
		System.out.println("Resource Bundle loaded");
	}
	/**
	 * 
	 * @param appObj - Application object which raises the exception.
	 * @param expObj - Exception object which is the actuall exception raised. 
	 */
	public EbwException(Object appObj,Exception expObj){
	    super(getExceptionMessage(expObj));
	    try {
	        
	    	this.errObj = appObj;
			this.expObj = expObj;
			errClassName = appObj.getClass().getName();
			
			
	    } catch (Exception e) {
	        log.error(e.getStackTrace());
	    }
	}
	
	/**
	 * This method will get the class name information from the application object,
	 * instantiate the logging object, gets user defined language specific error
	 * message from the resource bundle based on locale and prints the output to
	 * a log file based on the log4j.properties file settings.
	 *
	 */
	public Throwable printEbwException(){
		try{
			
		if(errObj==null)
			if(log.isDebugEnabled())
				log.error(errorStr);
		else{
			log = Logger.getLogger(errObj.getClass());
			
			errorStr = getExceptionMessage(expObj);
			
			if(log.isDebugEnabled()){
				log.error(errorStr);
				log.error(expObj.getStackTrace());
			}
		}
		
		if(expObj!=null){
			expObj.printStackTrace();
		}
		
		}catch(Exception e){
			log.error(e.getStackTrace());
		}
		
		return expObj;
	}
	
    public String getMessage() {
        return getExceptionMessage();
    }
    
	public static String getExceptionMessage(Exception expObj){
	    String errorStr="";
	    try{
	        EBWLogger.logDebug("EbwException ","expObj class "+expObj.getClass().getName());

	        //This tells that the Exception class is EBW framework exception like ServiceConnectionNotFoundException
	        if(expObj.getClass().getName().indexOf("com.tcs.ebw.serverside.exception")>-1){ 
			    errorStr= expObj.getCause().getMessage();
			    EBWLogger.logError(errClassName,errorStr); 
	        }
			else{
			    errorStr=bundle.getString(expObj.getClass().getName());//Other checkedExceptions
			    EBWLogger.logError(errClassName,errorStr);
			}
	    }catch(Exception e){
	        if(expObj.getCause()!=null)
	        errorStr=bundle.getString("AfterAllCatch")+expObj.getCause().getMessage();
	        if(expObj.getCause()!=null)
	            errorStr = expObj.getCause().getMessage();
	        else
	            errorStr = expObj.getMessage();
	    }
	    finally{
	        expObj =null;
	    }
			return errorStr;
	}
	
	
	public static String getExceptionMessage(Object errObj, Exception expObj){
	    String errorStr=null,errClassName=null;
	    try{
	        
	        EBWLogger.logDebug("EbwException ","expObj class "+expObj.getClass().getName());
	        errClassName = errObj.getClass().getName();
	        
	        //This tells that the Exception class is EBW framework exception like ServiceConnectionNotFoundException
	        if(expObj.getClass().getName().indexOf("com.tcs.ebw.serverside.exception")>-1){ 
			    errorStr= expObj.getCause().getMessage();
			    EBWLogger.logError(errClassName,errorStr); 
	        }
			else{
			    errorStr=bundle.getString(expObj.getClass().getName());//Other checkedExceptions
			    EBWLogger.logError(errClassName,errorStr);
			}
	    }catch(Exception e){
	        errorStr=bundle.getString("AfterAllCatch")+expObj.getCause().getMessage();
	        if(expObj.getCause()!=null)
	            errorStr = expObj.getCause().getMessage();
	        else
	            errorStr = expObj.getMessage();
	    }
	    finally{
	        expObj=null; // Destroy the new created Exception object instance.
	    }
			return errorStr;
	}
	
	
	public String getExceptionMessage(){
	    String errorStr="";
	    try{

	        EBWLogger.logDebug("EbwException ","expObj class "+expObj.getClass().getName());
			if(expObj instanceof NullPointerException)
				errorStr=bundle.getString("NullPointer");
			else if(expObj instanceof java.sql.SQLException)
				errorStr=bundle.getString("SQLException")+" : "+expObj.getCause().getMessage();	
			else if(expObj instanceof MissingResourceException)
				errorStr=bundle.getString("MissingResourceKey");
			else if(expObj instanceof ClassNotFoundException)
				errorStr=bundle.getString("ClassNotFound");
			else if(expObj instanceof java.io.FileNotFoundException)
				errorStr=bundle.getString("FileNotFound");
			else if(expObj instanceof MissingResourceException)
				errorStr=bundle.getString("StatementNotFoundException");
			else if(expObj instanceof InvocationTargetException)
				errorStr=bundle.getString("InvocationTargetException");
			
			else
			    errorStr=bundle.getString("AfterAllCatch")+expObj.getCause().getMessage();

	        if (expObj != null) {
				if(expObj instanceof NullPointerException)
					errorStr=bundle.getString("NullPointer");
				else if(expObj instanceof java.sql.SQLException)
					errorStr=bundle.getString("SQLException");	
				else if(expObj instanceof MissingResourceException)
					errorStr=bundle.getString("MissingResourceKey");
				else if(expObj instanceof ClassNotFoundException)
					errorStr=bundle.getString("ClassNotFound");
				else if(expObj instanceof java.io.FileNotFoundException)
					errorStr=bundle.getString("FileNotFound");
				else if(expObj instanceof MissingResourceException)
					errorStr=bundle.getString("StatementNotFoundException");
				else 
					errorStr=bundle.getString("AfterAllCatch");
	        }

	    }catch(Exception e){
	        if(expObj.getCause()!=null)
	            errorStr = expObj.getCause().getMessage();
	        else
	            errorStr = expObj.getMessage();

	        if (expObj != null && expObj.getCause()!=null)
	            errorStr = expObj.getCause().getMessage();

	    }
		return errorStr;
	}
	
	
}
