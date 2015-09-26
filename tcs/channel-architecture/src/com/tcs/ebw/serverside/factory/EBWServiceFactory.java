/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.serverside.factory;

//import java.util.HashMap;
import com.tcs.ebw.transferobject.EBWActivityLogMasterTO;
import java.util.LinkedHashMap;
import java.util.MissingResourceException;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.common.context.EBWAppContext;
import com.tcs.ebw.ejb.services.ChannelsEJBService;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.serverside.exception.*;

/**
 * This class is used by eBankworks Front End components to create instance
 * of the business logic implementation class by passing Service ID which
 * is defined in Service Configuration sheet.
 */
public class EBWServiceFactory {
    private static IEBWService configService =null;
    
    private static String serviceImpl = 	"";
    private static IEBWService objBusinessService = null;
    private static LinkedHashMap serviceInfo = new LinkedHashMap();
    private static LinkedHashMap businessSystemInfo  = new LinkedHashMap(); 
    private static LinkedHashMap serviceParams = new LinkedHashMap();
    private static LinkedHashMap configSystemInfo = new LinkedHashMap();
    private static IEBWConnection objConfigServiceConnection =null;
    private static IEBWConnection objBusinessServiceConnection =null;
    private static String configConnType ="";
    private static LinkedHashMap EBWErrorCodes=null;
    private static LinkedHashMap businessSystemInfoCache =new LinkedHashMap();
    private static LinkedHashMap businessServiceInfoCache =new LinkedHashMap();
    
	
    /**
     * This method is adds activity log to the create method. Please refer create method below
     * for details.
     *  
     * @param serviceId  - Service Id recieved from client for invocation.
     * @param appContext - Object having client side informations like screen, action and event.
     * @return - Returns object of type IEBWService. Business Services and ConfigServices are returned.
     * @throws Exception
     */
    public  static synchronized IEBWService create(String serviceId, EBWAppContext appContext) throws Exception{
        EBWLogger.trace("EBWServiceFactory","Entering IEBWService create(String serviceId, EBWAppContext appContext)");
        EBWLogger.logDebug("EBWServiceFactory","activity_logging"+PropertyFileReader.getProperty("activity_logging").equalsIgnoreCase("true"));
        EBWLogger.logDebug("EBWServiceFactory","appContext in create is "+appContext);
        IEBWService service  =null;
        try{
	        service  = create(serviceId);
	        service.setAppContext(appContext);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        EBWLogger.logDebug("EBWServiceFactory","RETURNING SERVICE:"+service.toString()+"  serviceID:"+serviceId);
        return service;
    }
    
	/**
	 * 
	 * This method is used to create the instance of Business logic class implementing
	 * the service methods defined in the service definition excel sheet. This method
	 * also creates the Configuration service instance for getting information about
	 * the service and system from configuration table. 
	 * 
	 * @param serviceId - ServiceId String as defined in the Service Configuration Sheet.
	 * 					  Please refere Service Confuguration Excel sheet for further details.
	 * @return			- Returns the instance of Service Implementation class which are of
	 * 					  type com.tcs.ebw.connfactory.EBWConnectionInterface.
	 */
	public  static synchronized IEBWService create(String serviceId) throws Exception{
		EBWLogger.trace("EBWServiceFactory.create","Starting .."+serviceId);
			/**
			 * Create a configuration service with configuration connection set to it.
			 * This service will be used to identify/query the details of service info
			 * using the serviceid being passed from client. Also it will query the system
			 * info table and get the system connection and set the same to create Service.
			 */
			EBWLogger.trace("EBWServiceFactory - create(String serviceid) - "+serviceId,"Configservice is null "+(configService==null));
		try
		{
			
			boolean factoryFlag = false;
			try
			{
				factoryFlag = Boolean.valueOf(PropertyFileReader.getProperty("isServletContainerOnWebServer")).booleanValue();
				EBWLogger.logDebug(EBWServiceFactory.class, " The flag for ejb based deployment model is "+factoryFlag);
			}
			catch(MissingResourceException mre)
			{
				
				EBWLogger.logDebug("EBWServiceFactory"," key \"isServletContainerOnWebServer\" is not configured in ebw.properties hence continuing with default configuration ");
				
			}
			
			if(factoryFlag)
			{
				EBWLogger.logDebug(EBWServiceFactory.class, " It is a ejb based deployment model.. Instantiating service class for ejb");
				objBusinessService = new ChannelsEJBService(serviceId);
			}
			
			else
			{
			
				if( configService==null)
				{ 
					EBWLogger.logDebug("EBWServiceFactory","Calling createConfigService");
					createConfigService();
					EBWLogger.logDebug("","ConfigService Created");
				}
			
				/*
				 * 	Get Business Service information
				 */
				serviceInfo = getBusinessServiceInfo(serviceId);
				if(serviceInfo==null ){
					EBWLogger.logError("EBWServiceFactory","Service Definition not found for Service "+serviceId);
					throw new ServiceNotFoundException(serviceId);
				}else if(serviceInfo.size()==0){
					EBWLogger.logError("EBWServiceFactory","Service Definition not found for Service "+serviceId);
					throw new ServiceNotFoundException(serviceId);
				}
			
				
				/* code added by Prasad K on 9-JUN-09 for bypassing the connection mechanism for mentioned service classes */
				
				String serviceClass = (String)serviceInfo.get("ImplClassName".toUpperCase());
				boolean servConnFlag =true;
				String servClasses = "";
				String[] classArray = null;
				try
				{
					servClasses = PropertyFileReader.getProperty("BypassConnForServices");
					
				}
				catch(MissingResourceException msre)
				{
					EBWLogger.logDebug(EBWServiceFactory.class, "Property \"BypassConnForServices\" is missing in ebw.prop..Hence continuing with default conn process ");
				}
				
				if(servClasses !="" && servClasses!=null)
				{
					classArray=servClasses.split(",");
					
					for(int i=0;i<classArray.length;i++)
					{
						if(classArray[i].equals(serviceClass))
							servConnFlag = false;
						
					}
				}
				
				if(servConnFlag)
				{
					/*
					 * Get Business System Information
					 */
					
					EBWLogger.logDebug(EBWServiceFactory.class, "Into code for getting the serviceConnection through connection framework");
					businessSystemInfo = getBusinessSystemInfo(serviceId);
					if(businessSystemInfo==null ){
						EBWLogger.logError("EBWServiceFactory","System Definition not found for Service:"+serviceId);
						throw new SystemNotFoundException(serviceId);
					}else if(businessSystemInfo.size()==0){
						EBWLogger.logError("EBWServiceFactory","System Definition not found for Service:"+serviceId);
						throw new SystemNotFoundException(serviceId);
					}
					EBWLogger.logDebug("","SystemInfo for serviceId "+serviceId+" is "+businessSystemInfo );
			
					//configService.close();
					//configService=null;
			
			
					/**
					 * Create Business Service Object. execute method of this service will be invoked by 
					 * EBWAbstractService class.
					 */
					serviceImpl = (String)serviceInfo.get("ImplClassName".toUpperCase());
					configConnType = (String)businessSystemInfo.get("system_type".toUpperCase());
					objBusinessServiceConnection =(IEBWConnection) EBWConnectionFactory.create(configConnType);
					EBWLogger.logDebug("EBWServiceFactory.create","***** ***** objBusinessServiceConnection : "+objBusinessServiceConnection);
			
					if(objBusinessServiceConnection==null){
						EBWLogger.logError("EBWServiceFactory","Connection could not be established for Service:"+serviceId);
						throw new ServiceConnectionNotFoundException((String)businessSystemInfo.get("SYSTEMID"));
					}
			    
					objBusinessService = (IEBWService)Class.forName(serviceImpl).newInstance();
					EBWLogger.logDebug("com.tcs.ebw.serverside.factory.EBWServiceFactory","ServiceImplementation class name is :"+serviceImpl);
			
					if(objBusinessService!=null){
						EBWLogger.logDebug("com.tcs.ebw.serverside.factory.EBWServiceFactory","Business Implementation class "+serviceImpl+" loaded successfully");
						objBusinessService.setServiceInfo(serviceInfo);
						
						try
						{
							objBusinessService.setConnection(objBusinessServiceConnection.connect(businessSystemInfo));
						}
						catch(Exception e)
						{
							e.printStackTrace();
							EBWLogger.logError(EBWServiceFactory.class, "Caught an exception "+e+" while getting connection to the system for service id "+serviceId);
							throw e;
						}
					}
				}
				else
				{
					EBWLogger.logDebug(EBWServiceFactory.class, "Into code for bypassing the serviceConnection through framework");
					
					serviceImpl = (String)serviceInfo.get("ImplClassName".toUpperCase());
					
					objBusinessService = (IEBWService)Class.forName(serviceImpl).newInstance();
					EBWLogger.logDebug("com.tcs.ebw.serverside.factory.EBWServiceFactory","ServiceImplementation class name is :"+serviceImpl);
				
					if(objBusinessService!=null){
						EBWLogger.logDebug("com.tcs.ebw.serverside.factory.EBWServiceFactory","Business Implementation class "+serviceImpl+" loaded successfully");
						objBusinessService.setServiceInfo(serviceInfo);
					}
				}
			}
			
		}catch(Exception e){
		    e.printStackTrace();
		    throw new com.tcs.ebw.serverside.exception.ServiceCreationException(serviceImpl);
		}
			//configService.close();	
		EBWLogger.trace("EBWServiceFactory.create","Finished.."+serviceId);
		return objBusinessService;
	}
	
	/**
	 * Reads the configuration parameters from property file (ebw.properties) and
	 * creates a service of type IEBWService. Please note that we have made the
	 * reading of configuration information from configuration database (ms-access tables like
	 * service_def_master, system_def_master etc)also as a service which is of type 
	 * com.tcs.ebw.serverside.services.ConfigService. So if we want to load the configuration
	 * details with different methods like from a JNDI service or from a remote server etc we 
	 * just have to create a new ConfigService class and mention the new ImplClassName in property file
	 * or have to replace the implementation of existing ConfigService class with the method names
	 * intact.
	 * @return
	 * @throws Exception
	 */
	private static IEBWService createConfigService() throws Exception{
	    EBWLogger.trace("EBWServiceFactory","Starting EBWServiceFactory.createConfigService ");
	    try{
		    configSystemInfo.put("datasource_name".toUpperCase(),PropertyFileReader.getProperty("datasource_name"));
			configSystemInfo.put("base_protocol".toUpperCase(),PropertyFileReader.getProperty("base_protocol"));
			configSystemInfo.put("ip_address".toUpperCase(),PropertyFileReader.getProperty("ip_address"));
			configSystemInfo.put("port_no".toUpperCase(),PropertyFileReader.getProperty("port_no"));
			configSystemInfo.put("system_sub_type".toUpperCase(),PropertyFileReader.getProperty("system_sub_type"));
			
			EBWLogger.logDebug("EBWServiceFactory","configSystemInfo Map is "+configSystemInfo);
			EBWLogger.logDebug("EBWServiceFactory","System Sub Type is : "+PropertyFileReader.getProperty("system_sub_type"));
			serviceImpl = PropertyFileReader.getProperty("ImplClassName");
			configService = (IEBWService)Class.forName(serviceImpl).newInstance();
			configConnType = PropertyFileReader.getProperty("ebwConfigType");
			
			objConfigServiceConnection=EBWConnectionFactory.create(configConnType);
	    }catch(Exception e){
	        e.printStackTrace();
	        EBWLogger.logError(EBWServiceFactory.class,  " Caught an exception "+e+ " while creating the config service");
	        throw new ServiceNotFoundException(PropertyFileReader.getProperty("datasource_name"));
	    }
		EBWLogger.logDebug("EBWServiceFactory.createConfigService","***** ***** Setting objConfigServiceConnection to ConfigService"+objConfigServiceConnection);
		try{
		    //configService.setConnection(objConfigServiceConnection.connect(configSystemInfo));
		}catch(Exception e){
		    e.printStackTrace();
		    EBWLogger.logError(EBWServiceFactory.class,  " Caught an exception "+e+ " while getting connection for config service");
		    throw new ConfigServiceConnNotFoundException(configSystemInfo);
		}
		
		return configService;
	}
	
	/**
	 * Gets service information by reading the service definition tables for the
	 * given serviceid.
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */
	public static LinkedHashMap getBusinessServiceInfo(String serviceId) throws Exception{
	    serviceParams.put("serviceId".toUpperCase(),serviceId);
		serviceParams.put("MethodName".toUpperCase(),"getServiceInfo");
		Class paramTypes[] = {LinkedHashMap.class};
	    Object newParams[] = {serviceParams};
	    
	    try{
		    configService.setServiceInfo(serviceParams);
	    }catch(Exception e){
	        e.printStackTrace();
	        throw e;
	    }
		return (LinkedHashMap)configService.execute(paramTypes,newParams);
	}
	
	
	/**
	 * 
	 * @param serviceId
	 * @return
	 * @throws Exception
	 */
	public static LinkedHashMap getBusinessSystemInfo(String serviceId) throws Exception{
	    EBWLogger.trace("WBWServiceFactory","Starting method LinkedHashMap getBusinessSystemInfo(String serviceId) throws Exception");
	    LinkedHashMap sysInfo =null;
	    serviceParams.put("serviceId".toUpperCase(),serviceId);
		serviceParams.remove("MethodName".toUpperCase());
		serviceParams.put("MethodName".toUpperCase(),"getSystemInfo");
		
		Class paramTypes[] = {LinkedHashMap.class};
		Object newParams[] = {serviceParams};
		newParams[0] = serviceParams;
		EBWLogger.logDebug("EBWServiceFactory","Getting System Information for "+serviceId +" With params "+serviceParams);
		try{
		    sysInfo = (LinkedHashMap)configService.execute(paramTypes,newParams);
		}catch(Exception e){
		    e.printStackTrace();
		    throw e;
		}
		EBWLogger.logDebug("EBWServiceFactory","Got System Information for "+serviceId+"-"+sysInfo); 
		return  sysInfo;
	}
		
	private static void addActivityLog(EBWAppContext appContext,String serviceId, IEBWService objBusinessService) throws Exception{
	    Class paramClassType[] = {String.class, Object.class};
		EBWLogger.logDebug("EBWServiceFactory ","Service Id in EBWAppContextappContext "+appContext.getServiceId());
		EBWActivityLogMasterTO activity_log_params = new EBWActivityLogMasterTO();
		activity_log_params.setUserId(appContext.getUserPrincipal().getUsruserid());
		//activity_log_params.setGroupId(appContext.getUserPrincipal().getUsrgrpid());
		//activity_log_params.setCustId(appContext.getUserPrincipal().getCustid());
		//activity_log_params.setIpaddress(appContext.getUserPrincipal().getIpAddr());
		activity_log_params.setServiceId(serviceId);
		activity_log_params.setActivityName(appContext.getScreenAction());
		activity_log_params.setEventName(appContext.getScreenState());
		Object paramObjs[] = {"insertActivityLogMaster",activity_log_params};
		
		Object resultout = objBusinessService.execute(paramClassType,paramObjs);
		EBWLogger.logDebug("EBWAbstractService","Activity log master inserted for serviceId "+serviceId);
		EBWLogger.logDebug("EBWServiceFactory","Activity Id generated is "+activity_log_params.getActivityId());
		activity_log_params.setResult("Success");
	}
}

