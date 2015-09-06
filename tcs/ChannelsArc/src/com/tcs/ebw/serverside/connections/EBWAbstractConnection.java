package com.tcs.ebw.serverside.connections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.exception.SecurityManagerClassNotFoundException;
import com.tcs.ebw.serverside.exception.SecurityManagerIllegalAccessException;
import com.tcs.ebw.serverside.exception.SecurityManagerInstanstiationException;
import com.tcs.ebw.serverside.factory.IEBWConnection;

public class EBWAbstractConnection implements IEBWConnection{

	public Object connect(LinkedHashMap systemInfo) throws Exception {
		if(systemInfo.containsKey(SystemConstants.SECURITY_MANAGER)){
        	invokeSecurityManager(systemInfo);
        }
		
		return null;
	}

	public Object disconnect() throws Exception {

		return null;
	}
	
	
	private Object invokeSecurityManager(LinkedHashMap systemInfo) throws Exception{
		EBWLogger.trace(this,"Entering method invokeSecurityManager(LinkedHashMap systemInfo)");
		EBWLogger.trace(this,"invokeSecurityManager systemInfo is: "+systemInfo);
		Object returnObj =null;
		String securityMngr =null;
		
		if(systemInfo.get(SystemConstants.SECURITY_MANAGER)!=null){
			securityMngr= (String)systemInfo.get(SystemConstants.SECURITY_MANAGER);
			try{
				Class cls = Class.forName(securityMngr);
				Object obj = cls.newInstance();
				Class paramTypes[] = {HashMap.class};
				Method method = obj.getClass().getDeclaredMethod("invoke", paramTypes);
				Object params[] = {};
				returnObj = method.invoke(obj, params);
				
				EBWLogger.logDebug(this,"Mehod invocation of SecurityManager method invoke returned :"+returnObj);
			}catch(ClassNotFoundException e){
				e.printStackTrace();
				throw new SecurityManagerClassNotFoundException((HashMap)systemInfo);
			}
			catch(IllegalAccessException ie){
				ie.printStackTrace();
				throw new SecurityManagerIllegalAccessException((HashMap)systemInfo);
			}
			catch(InstantiationException ine){
				ine.printStackTrace();
				throw new SecurityManagerInstanstiationException((HashMap)systemInfo);
			}
		}
		else
			EBWLogger.logDebug(this,"No Security manager configured for this System :"+systemInfo);
		
		return returnObj;
		
	}

}
