package com.tcs.ebw.cache;

import java.util.HashMap;

import com.tcs.bancs.channels.context.ServiceContext;
import com.tcs.bfsarch.common.CacheManagerImpl;
import com.tcs.ebw.common.context.EBWAppContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.common.util.SessionUtil;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.query.QueryExecutor;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *    224703       29-04-2011        2               CR 215
 *    224703       29-04-2011        3               Internal 24x7
 *    224703       29-04-2011        3               3rd Party ACH
 * **********************************************************
 */
public class CacheObject {

	private Object objConfig= null;
	private static String cacheType=null;

	/**
	 * The function return the stored cache object 
	 * @param serviceName == Defines the service name parameter, it should always be a Common DataBase Service . 
	 * @param isDirectDBCall = To check if the call is from EAR or WAR side .
	 * @param serviceContext = ServiceContext is required in case we are getting the cache from the EAR Side , 
	 * if the call is made from the WAR Side , service context Object is null..
	 * @return Object
	 * @throws Exception
	 */
	public Object getCacheData(String serviceName,Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception{
		EBWLogger.logDebug(this, "getCasheData fetching Data for Service : "+serviceName); 

		try{
			cacheType=PropertyFileReader.getProperty("Object_Cache_Type");
		}catch(Exception e){
			cacheType="";
		}
		EBWLogger.logDebug(this, "Caching type : "+cacheType);

		if(cacheType.equals("OpenSymphonyCache")){
			try{
				CacheManagerImpl cache= CacheManagerImpl.getInstance();
				if(cache.getObject(serviceName)== null){
					this.objConfig = (Object)getDBData(serviceName,isDirectDBCall,serviceContext);	
					cache.putObject(serviceName, this.objConfig);
					EBWLogger.logDebug(this, "Fetched from DB and Cached for : "+serviceName);
				}else{
					this.objConfig = cache.getObject(serviceName);
					EBWLogger.logDebug(this, "Fetched from Cached for : "+serviceName);
				}
			}catch(Exception e){
				EBWLogger.logDebug(this, "Error in Caching : "+e);
				throw e;
			}
		}else{
			try{
				this.objConfig = (Object)getDBData(serviceName,isDirectDBCall,serviceContext);
			}catch(Exception e){
				EBWLogger.logDebug(this, "Error in Fetching from DB : "+e);
				throw e;
			}
		}
		return this.objConfig;
	}

	/**
	 * The Function get the Data from the DB and returns the Object to be stored in cache.
	 * @param serviceName == Defines the service name parameter, it should always be a Common DataBase Service . 
	 * @param isDirectDBCall = To check if the call is from EAR or WAR side .
	 * @param serviceContext = ServiceContext is required in case we are getting the cache from the EAR Side , 
	 * if the call is made from the WAR Side , service context Object is null..
	 * @return Object
	 * @throws Exception
	 */
	private Object getDBData(String serviceName,Boolean isDirectDBCall,ServiceContext serviceContext) throws Exception
	{
		Object objConfigServ = null;
		try{
			// Call made from WAR Side ... isDirectDBCall will false , since the call will go through EJB Layer.
			if(!isDirectDBCall){
				try 
				{
					HashMap params= new HashMap();
					EBWAppContext appContext=new EBWAppContext();
					appContext.setUserPrincipal((UserPrincipal)SessionUtil.get("UserPrincipal"));

					IEBWService objService = EBWServiceFactory.create(serviceName,appContext);
					Object[] objParams = {serviceName, params,new Boolean(false)};
					Class[] paramTypes = {String.class,Object.class,Boolean.class};
					objConfigServ = (Object) objService.execute(paramTypes,objParams);

				} catch (Exception exception) {
					throw exception;
				}
			}
			// Call made from EAR Side OR within same transaction scope... isDirectDBCall is true since the call is made from the EAR side only ... 
			else {
				try {
					QueryExecutor queryExecutor = new QueryExecutor();
					Object cacheObject = new Object();
					queryExecutor.setConnection(serviceContext.getConnection());
					objConfigServ = (Object)queryExecutor.executeQuery(serviceName,cacheObject);

				} catch (Exception exception) {
					throw exception;
				}
			}
		}catch(Exception e){
			throw e;
		}
		finally {
		}
		return objConfigServ;
	}

}
