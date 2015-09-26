package com.tcs.ebw.cache;

import java.util.ArrayList;
import java.util.HashMap;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.bfsarch.common.CacheManagerImpl;

public class ConfigParameter{
	private static Object objConfig= null;
	private static String cacheType=null;
		
	static{
		try{
			cacheType=PropertyFileReader.getProperty("Config_Param_Cache_Type");
		}catch(Exception e){
			cacheType="";
		}
		EBWLogger.logDebug("","Config Parameter Caching type : "+cacheType);
		if(cacheType.equals("static")){
			try{
				
				HashMap params= new HashMap();
				IEBWService objService = EBWServiceFactory.create("getConfigParameterValues");
		        Object[] objParams = {"getConfigParameterValues", params};
		        Class[] paramTypes = {String.class,Object.class};
		        objConfig = (Object) objService.execute(paramTypes,objParams);
				EBWLogger.logDebug(objConfig, "Config Parameter Fetched from DB in Static Block.");
			}catch(Exception e){
				EBWLogger.logDebug("ERROR", "Error in Fetching Config Parameter from DB in Static Block : "+e);
			}
		}
		
    }
			
	private Object getConfigParameterList(){	
				
		EBWLogger.logDebug(this, "Config Parameter Caching type : "+cacheType);
		
		if(cacheType.equals("OpenSymphonyCache")){
			try{
				CacheManagerImpl cache= CacheManagerImpl.getInstance();
				if(cache.getObject("Config_Param_Cache_Key")== null){
					HashMap params= new HashMap();
					IEBWService objService = EBWServiceFactory.create("getConfigParameterValues");
			        Object[] objParams = {"getConfigParameterValues", params};
			        Class[] paramTypes = {String.class,Object.class};
			        objConfig = (Object) objService.execute(paramTypes,objParams);
					cache.putObject("Config_Param_Cache_Key", objConfig);
					EBWLogger.logDebug(objConfig, "Config Parameter Fetched from DB and Cashed.");
				}else{
					objConfig = cache.getObject("Config_Param_Cache_Key");
					EBWLogger.logDebug(objConfig, "Config Parameter Fetched from Cached.");
				}
			}catch(Exception e){
	    		EBWLogger.logDebug(this, "Error in Caching : "+e);
	    	}
		}else{
			if(objConfig == null){
				try{
					HashMap params= new HashMap();
					IEBWService objService = EBWServiceFactory.create("getConfigParameterValues");
			        Object[] objParams = {"getConfigParameterValues", params};
			        Class[] paramTypes = {String.class,Object.class};
			        objConfig = (Object) objService.execute(paramTypes,objParams);
				}catch(Exception e){
		    		EBWLogger.logDebug(this, "Error in Fetching Config Parameter from DB : "+e);
		    	}
			}
		}
		return objConfig;
	}
	
	public String getConfigParameter(String paramName){
		String paramValue=null;
		int paramNameIndex=0,paramValueIndex=3;
		ArrayList paramList = (ArrayList) getConfigParameterList();
		ArrayList colHeadings= (ArrayList)paramList.get(0);
		System.out.println("Data Array : "+paramList.toString());
		for(int i=0;i < colHeadings.size();i++){
			String tempStr = colHeadings.get(i).toString();
			if(tempStr.equalsIgnoreCase("PARAMETERNAME")){
				paramNameIndex = i;
			}else if(tempStr.equalsIgnoreCase("PARAMETERVALUE")){
				paramValueIndex = i;
			}
		}
		
		for(int i=1;i<paramList.size();i++){
			String temParamName = (((ArrayList)paramList.get(i)).get(paramNameIndex)).toString();
			if(temParamName.equals(paramName)){
				paramValue = (((ArrayList)paramList.get(i)).get(paramValueIndex)).toString();
				break;
			}
		}
		return paramValue;
	}
}