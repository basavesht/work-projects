/*
 * Created on Oct 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
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
		//bundle = ResourceBundle.getBundle("com.tcs.ebw.exception.EBWErrors",locale);
		bundle = ResourceBundle.getBundle("com.tcs.ebw.exception.EBWErrors");
		EBWLogger.logDebug("EbwException","Resource Bundle loaded");
	}
	
	public EbwException(String errorCode) {
				super(PropertyFileReader.getPropertyKeyValue("EBWErrorCodes",errorCode));
	}
	
	/**
	 * 
	 * @param errorCode - Corresponds to the error code as given in EBWErrorCodes file
	 * @param errInfoObj - This object can be an error object or Object containing additional info about the error.
	 */
	/*public EbwException(String errorCode, Object errInfoObj){
		super(PropertyFileReader.getPropertyKeyValue("EBWErrorCodes",errorCode));
		if(errInfoObj instanceof Exception){
			EBWLogger.logError(PropertyFileReader.getPropertyKeyValue("EBWErrorCodes",errorCode)+".log",expObj.getCause().getMessage());
			log.error(expObj.getStackTrace());
		}
		else 
			EBWLogger.logError(this,PropertyFileReader.getPropertyKeyValue("EBWErrorCodes",errorCode+".log")+": Additional Info - "+errInfoObj);
	}
	*/
	
	/**
	 * 
	 * @param appObj - Application object which raises the exception.
	 * @param expObj - Exception object which is the actuall exception raised. 
	 */
	public EbwException(Object appObj,Object expObj){
			super((appObj!=null && appObj instanceof String && expObj!=null && (appObj.toString().startsWith("SYS") ||appObj.toString().startsWith("NCS") ||appObj.toString().startsWith("TELLER")) )? PropertyFileReader.getPropertyKeyValue("EBWErrorCodes",(String)appObj): getExceptionMessage((Exception)expObj) );
		
			if(expObj instanceof Exception && appObj instanceof String && (appObj.toString().startsWith("SYS") ||appObj.toString().startsWith("NCS") ||appObj.toString().startsWith("TELLER"))){ //In this case appObj is the errorCode
				EBWLogger.logError(PropertyFileReader.getPropertyKeyValue("EBWErrorCodes",(String)appObj)+".log",((Exception)expObj).getCause().getMessage());
				log.error(((Exception)expObj).getStackTrace());
			}
			else if(appObj instanceof String && (appObj.toString().startsWith("SYS") ||appObj.toString().startsWith("NCS") ||appObj.toString().startsWith("TELLER")))
				EBWLogger.logError(this,PropertyFileReader.getPropertyKeyValue("EBWErrorCodes",(String)appObj+".log")+": Additional Info - "+expObj);
		try {
	        
	    	this.errObj = appObj;
			this.expObj = (Exception)expObj;
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
    	if(getExceptionMessage()!=null)
    		return getExceptionMessage();
    	else
    		return super.getMessage();
    }
    
	public static String getExceptionMessage(Exception expObj){
	    String errorStr="";
	    try{
	        EBWLogger.logDebug("EbwException ","expObj class "+expObj.getClass().getName());

	        //This tells that the Exception class is EBW framework exception like ServiceConnectionNotFoundException
	        if(expObj.getClass().getName().indexOf("com.tcs.ebw.serverside.exception")>-1){ 
			    errorStr= expObj.getCause().getMessage();
			    EBWLogger.logError("EbwException",errorStr); 
	        }
			else{
				EBWLogger.logError("EbwException","It is a java type exception"); 
			    if(expObj!=null && expObj.getClass()!=null && expObj instanceof java.lang.reflect.InvocationTargetException)
			        errorStr = ((InvocationTargetException)expObj).getTargetException().getMessage();
			        
			    errorStr=bundle.getString(expObj.getClass().getName())+errorStr;//Other checkedExceptions
			    EBWLogger.logError("EbwException",errorStr);
			    EBWLogger.logError("EbwException","Error string after errrors. is :"+errorStr);
			}
	    }catch(Exception e){
	    	EBWLogger.logError("EbwException","Came to Exception of Exception class ");
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
	        if(expObj !=null)
	        	errorStr=bundle.getString(expObj.getClass().getName());
	        else
	        	errorStr=bundle.getString("AfterAllCatch");
	        
	        EBWLogger.logDebug(this,"errorStr in getExceptionMessage is :"+errorStr);
			

	    }catch(MissingResourceException mre){
	    	EBWLogger.logError(this, mre.getMessage());
	    	errorStr=bundle.getString("AfterAllCatch");
	    	//errorStr=mre.getMessage();
	    }
	    catch(Exception e){
	    	if (expObj != null && expObj.getCause()!=null)
	            errorStr = expObj.getCause().getMessage();
	    	else if(expObj==null){
	    		return super.getMessage();
	    	}else
		    	if(expObj.getCause()!=null)
		            errorStr = expObj.getCause().getMessage();
		        else
		            errorStr = expObj.getMessage();
	    }
		return errorStr;
	}
	
	
}
