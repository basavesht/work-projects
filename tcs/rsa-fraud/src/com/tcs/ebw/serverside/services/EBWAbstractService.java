/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */

/**
 * This class has all the common methods of Service implementation classes
 * like getting local connection and setting the connection object in 
 * service classes, method for dynamically invoking the service method
 * defined in Service classes. This method name is taken from the 
 * configuration database.
 */
package com.tcs.ebw.serverside.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.tcs.ebw.common.context.EBWAppContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.query.Prepopulator;
import com.tcs.ebw.transferobject.EBWActivityLogMasterTO;



public abstract class EBWAbstractService implements IEBWService {
    
	/**
	 * Variable which is a reference of  Logger
	 */
  
	Logger activityLogger=Logger.getLogger("eBWUserActivityLog");
	/**
	 * Variable which is Reference of Activity Log
	 */
    protected String activityLogId;
    
    /**
     * Variable which is Reference of Audit Log
     */
    protected String auditLogId;
    
    /**
     * Variable what is the Reference of application Context
     */
    protected EBWAppContext appContext=null;
    
    /**
	 * Variable to store business logic based connection based on the
	 * definition in system configuration table.
	 */
	protected Object serviceConnection;
	
	/**
	 * HashMap to store service configuration information which is read from 
	 * Configuration table.
	 */
	protected LinkedHashMap serviceInfo;
	
	/**
	 * Object to store cash denomination information
	 */
	protected Object cashDenominationHandler;
	
	/**
	 * Object to store error override flag information
	 */
	protected boolean errorOverride = false;
	
	
	/**
	 * This variable is used for Activity Logging using JDBC Appender. This will be cached once connected to avoid connecting 
	 * at every time of activity logging.  
	 */
	protected static Connection activityLogCon; 
	
	/**
	 * This method sets the connection object for the service. This connection
	 * object will be created and set prior to calling any service.
	 * 
	 * @connectionObj - Connection object which can be a java.sql.Connection in case
	 *                  of Database connection, java.net.HttpURLConnection object in
	 *  				case of Webservices etc based on the type of system and connection.
	 * 					The Connection Handler classes will type cast the Object type of 
	 * 					connection to corresponding type.
	 * 
	 */
	public Object setConnection(Object connectionObj) {
		this.serviceConnection = connectionObj;
		
		return null;
	}
	
	/**
	 * This method is used to execute queries by passing paramTypes[] and paramTypes[].
	 * After execution of the query,it returns an object
	 * @param paramTypes Class[] as input parameter
	 * @param params  Object[] which may be TO[] or other  type of array
	 * @return  Object as output parameter which may be TO,Arraylist,Vector,Object etc..
	 * @exception Exception occurs if anything happens wrong inthe body of this method.
	 * 
	 */
	public Object execute(Class paramTypes[],Object params[]) throws Exception {
	    EBWLogger.trace(this,"Starting business module execute"+this.getClass().getName());
	    Object retVal = "-1";
		String methodName="";
		Method m = null;
			try{
			EBWLogger.logDebug(this,"serviceInfo is "+serviceInfo);    
			methodName= (String)serviceInfo.get("MethodName".toUpperCase());
			
			EBWLogger.logDebug(this,"Method name is :"+methodName);
			StringBuffer paramBuffer = new StringBuffer();
			if(params!=null )
			    if(params.length>0){
			        for(int i=0;i<params.length;i++){
			            EBWLogger.logDebug(this,"Params  :"+params[i]+" , ParamType: "+paramTypes[i]);
			            paramBuffer.append(params[i]+",");
			        }
			    }
			
			EBWLogger.logDebug(this,"Service Class name"+this.getClass().getName());
			
			
			boolean isDatabaseService = (this.getClass().newInstance() instanceof com.tcs.ebw.serverside.services.DatabaseService);
			boolean isEJBWService=(this.getClass().newInstance()  instanceof com.tcs.ebw.serverside.services.EJBService);
		
			EBWLogger.logDebug(this,"Is DatabaseService "+ isDatabaseService);
			EBWLogger.logDebug(this,"Is isEBWService "+ isEJBWService);
			EBWLogger.logDebug(this,"Before Invoking method :"+ methodName);
			m = this.getClass().getDeclaredMethod(methodName,paramTypes);
			
			EBWLogger.logDebug(this,"Invoking method :"+ m.getName());
			
			retVal = m.invoke(this,params);
			//check for condition of ServiceType having DatabaseService or EJBService
			//if ServiceType with DatabaseService,
			//					then user_activity_logging is true
			//			if ServiceType with EJBService,
			//					then server_activity_logging is true
			
			if(isDatabaseService){
				if(PropertyFileReader.getProperty("user_activity_logging").equalsIgnoreCase("true") ){
						if(!(this.getClass().newInstance() instanceof com.tcs.ebw.serverside.services.ConfigService))
					logUserActivity(paramTypes,params,paramBuffer,retVal);
				}else{
					EBWLogger.logDebug(this,"User_Activity_Logging is false");
				}
			}else if(isEJBWService){
				
				if(PropertyFileReader.getProperty("server_activity_logging").equalsIgnoreCase("true")){
					logUserActivity(paramTypes,params,paramBuffer,retVal);
				}else{
					EBWLogger.logDebug(this,"Server_Activity_Logging is false");
				}
			}
			//if(PropertyFileReader.getProperty("activity_logging").equalsIgnoreCase("true"))
			//	logUserActivity(paramTypes,params,paramBuffer,retVal);						    			
			
			}catch(InvocationTargetException ite){
				ite.printStackTrace();
			    EBWLogger.logError(this," ## ERROR ## InvocationTargetException while invoking service method "+methodName);
			    EBWLogger.logError(this," ## ERROR ## Cause of Invocation Error "+ite.getCause().getMessage());
			    printStringArrayValues(params,"## ERROR ##");
			    
			    EBWLogger.logDebug(this,ite.getTargetException().getClass().getName());
			    if(ite.getTargetException() instanceof EbwException)
			    	throw ite;
			    else{
			    	ite.printStackTrace();
			    	throw new EbwException(this,ite);
			    }
			    
			    //throw ite;
			    
			}catch(Exception e){
			    EBWLogger.logError(this," ## ERROR ## Unknown Exception invoking service method "+methodName);
			    printStringArrayValues(params,"## ERROR ##");
			    e.printStackTrace();
			    throw new EbwException(this,e); 
			}
			return retVal; 
	}
	
	/**
	 * This method checks whether LogService is there or not 
	 * @param params Object[]  as input parameter
	 * @return boolean
	 */
	
	private boolean isLogService(Object params[]){
	    boolean isLogService=false;
	    
	    if(params !=null){
		    int objLen  = params.length;
		    for(int i=0;i<objLen;i++){
		        if(params[i] instanceof String){
		            String serviceid =(String)params[i]; 
		            if(serviceid.equals("insertActivityLogMaster"))
		            	isLogService=true;
		        }
		    }
	    }
	    else
	        if(((String)serviceInfo.get("ServiceId".toUpperCase())).equalsIgnoreCase("insertActivityLogMaster"))
	                isLogService=true;     
	    
	    return isLogService;
	}
	
	/**
	 * This method is used to set Service information by Passing ServiceInfo as input parameter
	 * @param serviceInfo  Serveice information as input parameter
	 */
	
	public void setServiceInfo(LinkedHashMap serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
	
	
	
	
	/**
	 * This fuunction is used for user Activity Log Operation
	 * @param paramTypes Class[] type as input parameter
	 * @param params Object[] as input parameter
	 * @param paramBuffer StringBuffer
	 * @param retVal Object
	 * @throws Exception if some exception occurs inside the body of the method
	 */
	private void logUserActivity(Class[] paramTypes, Object params[], 
	    StringBuffer paramBuffer,Object retVal ) throws Exception{
		
	    StringBuffer activityLogQuery = new StringBuffer();
	    StringBuffer activityStr = new StringBuffer();
	    
	    EBWActivityLogMasterTO logTo = new EBWActivityLogMasterTO();
	    org.apache.log4j.jdbc.JDBCAppender jdbcappender = (org.apache.log4j.jdbc.JDBCAppender)activityLogger.getAppender("EBWJDBCAppender");
	    
	    String driverName = PropertyFileReader.getProperty("activity_drivername");
	    String url = jdbcappender.getURL();
	    String user = jdbcappender.getUser();
	    String pwd = jdbcappender.getPassword();
	    
	    
	    
	    
	    if(activityLogCon==null){
		     activityLogCon = DriverManager.getConnection(url, user, pwd);
		     Class.forName(driverName);
		     
	    }
	    
	    //Prepopulator.fillTransferObject(logTo,"activityId",activityLogCon);
	    //activityLogCon.close();
	    EBWLogger.logDebug(this,"ActivityId after fillTO in logging :"+logTo.getActivityId());
	    /** User Activity Loggin Starts here **/
		EBWLogger.logDebug(this,"It has JDBCAppender "+activityLogger.getAppender("EBWJDBCAppender"));
		EBWLogger.logDebug(this,"Application Context :"+appContext);
		
	    if(activityLogger.getAppender("EBWJDBCAppender")!=null && 
	            appContext!=null && serviceInfo.get("ServiceId".toUpperCase())!=null && 
	            ((String)serviceInfo.get("ServiceId".toUpperCase())).trim().length()>0){
	        EBWLogger.logDebug(this,"JDBC Logging is Enabled");
	        
	        activityLogQuery.append(jdbcappender.getSql());
	        String replacedQry = replaceQueryWithValue(logTo,activityLogQuery,appContext,paramBuffer,retVal);
	        EBWLogger.logDebug(this,"Replaced query is :-"+replacedQry);
	        try{
	        	EBWLogger.logDebug(this,"Before jdbcappender.setSql(replacedQry)");
		        jdbcappender.setSql(replacedQry);
		        EBWLogger.logDebug(this,"After jdbcappender.setSql(replacedQry)");
		        activityLogger.addAppender(jdbcappender);
		       
		        EBWLogger.logDebug(this,"JDBCAppender execution over");
		        EBWLogger.logDebug("eBWUserActivityLog", activityStr.toString());
		        jdbcappender.setSql(mainQuery());
		    
		        //jdbcappender.close(); // Commented temporarily for logging activity logging queries. 
	       
	        }catch(Exception e){
	        	EBWLogger.trace(this,"Did not log activity ");
	        }
	    }else		
		if(appContext!=null && serviceInfo.get("ServiceId".toUpperCase())!=null && 
		        ((String)serviceInfo.get("ServiceId".toUpperCase())).trim().length()>0){
		    EBWLogger.logDebug(this,"Text File Logging is Enabled");
		    activityStr.append(appContext.getUserPrincipal().getUsruserid()+"||");
		    activityStr.append(appContext.getUserPrincipal().getUsrgrpid()+"||");
		    activityStr.append(appContext.getUserPrincipal().getCustid()+"||");
		    activityStr.append(appContext.getUserPrincipal().getIpAddr()+"||");
		   
		    activityStr.append(appContext.getScreenAction()+"||");
		    activityStr.append(appContext.getScreenState());
		    activityStr.append((String)serviceInfo.get("ServiceId".toUpperCase())+"||");
		    activityStr.append(paramBuffer.toString()+"||");
		    activityStr.append("Output : "+retVal);
		    EBWLogger.logDebug("eBWUserActivityLog",activityStr.toString());
		}else
		    EBWLogger.logDebug(this,"Application Context is Null");
	    
		
		
		if(activityStr.toString().trim().length()>0)
		    activityStr.delete(0,activityStr.toString().length());
	}
	
	/**
	 * This function is used to replace Query with a specific value
	 * @param logTo  EBWActivityLogMasterTO as input parameter
	 * @param activityLogQuery  logQuery  which will be executed
	 * @param appContext Application Context as input parameter
	 * @param paramBuffer StringBuffer 
	 * @param retVal Object
	 * @return String 
	 */
	
	private String replaceQueryWithValue(EBWActivityLogMasterTO logTo,StringBuffer activityLogQuery,EBWAppContext appContext,StringBuffer paramBuffer,Object retVal){
	    String replacedStr =activityLogQuery.toString();
	    EBWLogger.logDebug(this,"-----------------------User Activity Log Replace Method Started-----------------------------");
	    if(appContext!=null)
	    	EBWLogger.logDebug(this,appContext.getUserPrincipal().getUserDap().toString());
	    if(logTo.getActivityId() != null) 
	    	replacedStr=replacedStr.replaceAll("activityid",logTo.getActivityId());
	    replacedStr=replacedStr.replaceAll("userid",appContext.getUserPrincipal().getUsruserid().trim());
	    replacedStr=replacedStr.replaceAll("grpid",appContext.getUserPrincipal().getUsrgrpid().trim());
	    if(appContext.getUserPrincipal().getCustid()!= null)
	    	replacedStr=replacedStr.replaceAll("custid",appContext.getUserPrincipal().getCustid());
	    else
	    	replacedStr=replacedStr.replaceAll("custid","-");
	    replacedStr=replacedStr.replaceAll("ipaddress",appContext.getUserPrincipal().getIpAddr());
	    replacedStr=replacedStr.replaceAll("serviceid",(String)serviceInfo.get("ServiceId".toUpperCase()));
	    if(appContext.getScreenAction() != null)
	    	replacedStr=replacedStr.replaceAll("activityname",appContext.getScreenAction());
	    replacedStr=replacedStr.replaceAll("eventname",appContext.getScreenState());
	    if(paramBuffer.toString().length()>0)
		    paramBuffer.delete(paramBuffer.toString().length()-1,paramBuffer.toString().length());
	    replacedStr=replacedStr.replaceAll("serviceparams",paramBuffer.toString());
	    EBWLogger.logDebug(this,"serviceparams replaced :-"+replacedStr);
	    replacedStr=replacedStr.replaceAll("result",retVal.toString());
	    EBWLogger.logDebug(this,"result replaced"+replacedStr);
	    EBWLogger.logDebug(this,"-----------------------User Activity Log Replace Ended-----------------------------");
	    return replacedStr;
	}
	
	/**
	 * This method set the appender query to initial Query 
	 * 
	 * @return String object is returned
	 */
	
	private String mainQuery()
	{
		return new String("Insert into ebw_activity_log_master (ACTIVITY_ID,USERID,GRPID,CUSTID,IPADDRESS,DATETIME,SERVICE_ID,ACTIVITY_NAME,EVENT_NAME,SERVICEPARAMS,RESULT) values('activityid','userid','grpid','custid','ipaddress',sysdate,'serviceid','activityname','eventname','serviceparams','result')");
	}
	
	/**
	 *   This method checks for the connection object type and calls the 
	 *   close method corresponding to that connection object.
	 */
	
	public abstract Object close() throws Exception;
    public EBWAppContext getAppContext() {
        return appContext;
    }
    
    /**
     * This method is used to  set the Application Context 
     * @param appContext is an application Context as input parameter
     */
    
    public void setAppContext(EBWAppContext appContext) {
        this.appContext = appContext;
    }
    
    /**
     * This function is used to print the Array values 
     * @param params  arrays of parameters passing in a object
     * @param msg message which will be printed 
     */
    
    private void printStringArrayValues(Object params[],String msg){
	    if(params!=null){
		    for(int i=0;i< params.length;i++)
		        EBWLogger.trace(this,msg+"statementids[] :"+params[i].toString());
	    }else
	        EBWLogger.logError(this,"Value params is is NULL in EBWAbstractService printStringArratValues");
	}

    /**
     * This method is used to  set the cash denominations 
     * @param cashDenominationHandler
     */
	public Object getCashDenominationHandler() {
		return cashDenominationHandler;
	}

	/**
     * This method is used to  get the cash denominations 
     * @param cashDenominationHandler
     */
	public void setCashDenominationHandler(Object cashDenominationHandler) {
		this.cashDenominationHandler = cashDenominationHandler;
	}
	
	/**
     * This method is used to  set error override flag information.
     * @param errorOverride
     */
	public boolean isErrorOverride() {
		return errorOverride;
	}

	/**
     * This method is used to get error override flag information.
     * @param errorOverride
     */
	public void setErrorOverride(boolean errorOverride) {
		this.errorOverride = errorOverride;
	}
}
