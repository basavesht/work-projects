
/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.serverside.services;

import java.lang.reflect.Method;
import java.util.Hashtable;

import javax.ejb.EJBHome;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.services.EBWAbstractService;

/**
*
* This is the service delegating class which implements 
* the IEBWService interface and extends EBWAbstractService.It gives generalized
* implementation for all the methods for EJBService.
* 
*/

public class EJBService extends EBWAbstractService{
    private Hashtable homeCache = new Hashtable();
    
    /**
     * Constructor of EJBServices
     */
    public EJBService() {
        super();       
    }
    
    
    /**
     * This method is used to get the EJB Home interface using the JNDI Name.
     * @param jndiName - String representing jndiName to lookup
     * @return  - Returns an EJB Home type object, using which an interface
     *  		  can be created and business methods can be called. 
     * @throws Exception
     */
    public Object getHome(String jndiName, String homeClass) throws Exception{
    EBWLogger.trace(this,"getHome(String jndiName, String homeClass)");
    EBWLogger.trace(this,"jndiName :"+jndiName);
    EBWLogger.trace(this,"homeClass :"+homeClass);
    
    EBWLogger.logDebug(this,"ServiceConnection is :"+serviceConnection.getClass().getName());
    
        if(!homeCache.containsKey(jndiName))
            homeCache.put(jndiName,
                    ((InitialContext)serviceConnection).lookup(jndiName));
        
        Object obj = homeCache.get(jndiName);
        Class homeclass = Class.forName(homeClass);
        
        EJBHome home =(EJBHome)PortableRemoteObject.narrow (obj,homeclass);
        
        EBWLogger.trace(this,"Returning from getHome(String jndiName, String homeClass)");
        return home;     
    }
    
    /**
     * This method is used to get the EJB Home interface using the JNDI Name which is
     * configured as part of eBankWorks system definition.
     * @return  - Returns an EJB Home type object, using which an interface
     *  		  can be created and business methods can be called. 
     * @throws Exception
     */
    public Object getHome() throws Exception{
    EBWLogger.trace(this,"getHome(String jndiName, String homeClass)");    
    /*
    String jndiName = (String)serviceInfo.get("Datasource_Name");
    EBWLogger.trace(this,"jndiName :"+jndiName);
    EBWLogger.logDebug(this,"ServiceConnection is :"+serviceConnection.getClass().getName());
    
        if(homeCache!=null && !homeCache.containsKey(jndiName))
            homeCache.put(jndiName,((InitialContext)serviceConnection).lookup(jndiName));
        
        Object obj = homeCache.get(jndiName);
        
        EJBHome home =(EJBHome)PortableRemoteObject.narrow (obj,EJBHome.class);*/
    
    	EJBHome home =(EJBHome)serviceConnection;
    	
        
        EBWLogger.trace(this,"Returning from getHome(String jndiName, String homeClass)");
        return home;     
    }
    
    
    /**
     * This method will invoke the method name given as parameter by instantiating the class
     * given in parameter using the passed object as parameter to the invoked method.
     * @param c - Class which has the method.
     * @param obj  - Parameter to be passed to the method.
     * @param mname  - Method name to be invoked.
     * @return - Returns the object returned by the invoked method.
     */
    static Object invoke(Class c,Object obj,String mname){
        
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

   /**
    * This method uused to close the EJBService
    */
    public Object close() throws Exception {
        EBWLogger.trace(this,"Starting close method of EJBService");
        serviceConnection=null;
        EBWLogger.trace(this,"Finished close method of EJBService");
        return null;
    }
    
    public static void main(String[] args) {
    }
}
