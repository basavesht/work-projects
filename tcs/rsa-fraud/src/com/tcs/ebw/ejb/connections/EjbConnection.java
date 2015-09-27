/**
 * This class is for getting the connection object of local as well as remote
 * ejb's.
 * @author 197479
 * @version 1.0
 */
package com.tcs.ebw.ejb.connections;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;

import com.tcs.ebw.common.context.EBWAppContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.factory.IEBWService;

public class EjbConnection
{
	/*
	 * below class variables are for caching the local and remote ejb objects
	 */
	private static LinkedHashMap localEjbMap = new LinkedHashMap();
	private static LinkedHashMap remoteEjbMap = new LinkedHashMap();
	
	
	public static EJBObject getLocalEJBConnection(String url, String jndiName , String factory) throws NamingException, RemoteException
	{
		EJBObject object = null;
		
		EBWLogger.logDebug("com.tcs.ebw.ejb.EjbConnection "," entering into the getLocalEJBConnection method ");
		if(localEjbMap==null)
		{
			localEjbMap = new LinkedHashMap();
		}
		
		if(!localEjbMap.containsKey(jndiName))
		{
			
		//	System.out.println("key not found in hashmap ");
			Properties prop = new Properties();
			prop.put("java.naming.factory.initial",factory);
			prop.put("java.naming.provider.url",url);
			InitialContext ctx = new InitialContext(prop);
			EJBHome  home = (EJBHome)ctx.lookup(jndiName);
			Class homeintf=home.getEJBMetaData().getHomeInterfaceClass();
			object = (EJBObject)invoke(homeintf,home,"create");
			if(object !=null)
				localEjbMap.put(jndiName, object);
		}
		else
			object = (EJBObject) localEjbMap.get(jndiName);
		
		
		EBWLogger.logDebug("com.tcs.ebw.ejb.EjbConnection "," The connection object is "+object);
		
		return object;
	}
	
	
	public static EJBObject getRemoteEJBConnection(String url, String jndiName , String factory) throws NamingException, RemoteException
	{
		EJBObject object = null;
		
		EBWLogger.logDebug("com.tcs.ebw.ejb.EjbConnection "," entering into the getRemoteEJBConnection method ");
		
		if(remoteEjbMap ==null)
		{
			remoteEjbMap = new LinkedHashMap();
		}
		
		if(!remoteEjbMap.containsKey(jndiName))
		{
			
			
			Properties prop = new Properties();
			prop.put("java.naming.factory.initial",factory);
			prop.put("java.naming.provider.url",url);
			InitialContext ctx = new InitialContext(prop);
			Object obj = ctx.lookup(jndiName);
			EJBHome  home = (EJBHome)PortableRemoteObject.narrow(obj,EJBHome.class);
			Class homeintf=home.getEJBMetaData().getHomeInterfaceClass();
			object = (EJBObject)invoke(homeintf,home,"create");
			if(object !=null)
				remoteEjbMap.put(jndiName, object);
		}
		else
			object = (EJBObject) remoteEjbMap.get(jndiName);
		
		
		
		EBWLogger.logDebug("com.tcs.ebw.ejb.EjbConnection "," The connection object is "+object);
		
		return object;
	}
	
	
	 static Object invoke(Class c,Object obj,String mname)
	 {
	        
	        Method[] methods = c.getMethods();
	        String name=null;
	        Object r= null;
	        try {
	            for(int i = 0; i<methods.length ;i++) {
	               name = methods[i].getName();
	               if(name.startsWith(mname)) {
	                  r = methods[i].invoke(obj,null); 
	               }
	            }
	        } catch(Exception e){
	            e.printStackTrace();
	            
	        } 
	        return r;
	     }


	
}
