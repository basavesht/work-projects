package com.tcs.ebw.serverside.connections;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.serverside.exception.ClusterUrlException;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.serverside.factory.IEBWConnection;
//import com.tcs.weblogic.VotingListServiceHome;
//import com.tcs.weblogic._VotingListServiceHome_Stub;

/**
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */


public class EJBConnection implements IEBWConnection{
    protected HashMap ctxCache=new HashMap(); 
    protected HashMap homeCache=new HashMap();
    /* (non-Javadoc)
     * @see com.tcs.ebw.serverside.factory.IEBWConnection#connect(java.util.LinkedHashMap)
     */
    public Object connect(LinkedHashMap systemInfo) throws Exception {
        EBWLogger.trace(this,"Starting Connect (LinkedHashMap systemInfo) in EJBConnection");
        
        Context c = getContextFromCache(systemInfo);
        String jndiName = (String)systemInfo.get("DATASOURCE_NAME");
        String homeClass = (String)systemInfo.get("DRIVER_CLASS");
        EJBHome home=null;
        if(!homeCache.containsKey(jndiName)){
        	Object remoteObj=null;	
        		try{
	        		remoteObj = ((InitialContext)c).lookup(jndiName);
	        		EBWLogger.logDebug(this,"Object from lookup is:"+remoteObj.getClass().getName());	        		
	        		EBWLogger.logDebug(this,"Successfully casted Home");
        		}catch(NamingException ne){
        			throw new EbwException("SYS007",ne);
        		}
        		catch(Exception e){
        			EBWLogger.logDebug(this, "Failure in First Lookup");
        			throw new EbwException("SYS006",e);
        			
        		}
        			try{
        				home = (EJBHome) PortableRemoteObject.narrow(remoteObj, EJBHome.class);
        				EBWLogger.logDebug(this, "Got Home class successfully");
        			}catch(Exception e){
        				EBWLogger.logDebug(this, "Failure in Second Lookup");
        				throw new EbwException("SYS006",e);
        			}
                homeCache.put(jndiName, home);
        }else{
        	home =(EJBHome)homeCache.get(jndiName);  
	        EBWLogger.logDebug(this,"HomeObject instance is : "+home.getClass().getName());
	        EBWLogger.logDebug(this,"Home class for EJB is :"+homeClass);
        }
		EBWLogger.trace(this,"Finished Connect (LinkedHashMap systemInfo). Returning context..."+ c);
        return home;
    }
    /* (non-Javadoc)
     * @see com.tcs.ebw.serverside.factory.IEBWConnection#disconnect()
     */
    public Object disconnect() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    /**
     * 
     */
    public EJBConnection() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        try{
	        EJBConnection ejbConn = new EJBConnection();
	        LinkedHashMap map = new LinkedHashMap();
	        map.put("IP_ADDRESS","172.19.24.206");
	        map.put("PORT_NO","7001");
	        map.put("BASE_PROTOCOL","http");
	        map.put("FACTORY_NAME","weblogic.jndi.WLInitialContextFactory");
	        ejbConn.connect(map);
	        
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private InitialContext getContextFromCache(LinkedHashMap systemInfo) throws NamingException,Exception{
    	EBWLogger.trace(this,"Entering getCOntextFromCache of EJBConnecton");
    	EBWLogger.trace(this,"System info is :"+systemInfo);
        Context c=null;
        Properties h = new Properties();
        
        String strContextFactory = null;
        try{
        	//strContextFactory=PropertyFileReader.getProperty("JNDI_CONTEXT_FACTORY");
        	strContextFactory=(String)systemInfo.get("FACTORY_NAME");
        }catch(Exception e){
        	EBWLogger.logDebug(this, "No initialContext Factory for EJBConnection, so getting Conn from same machine");
        	throw new EbwException("SYS007",e);
        } 
        
        
        String ipaddress = (String)systemInfo.get("IP_ADDRESS");
        
        String portno = (String)systemInfo.get("PORT_NO");
        
        String url = null;
        
        
        if(ctxCache==null || ctxCache.get(systemInfo.get("IP_ADDRESS"))==null ){
        	if(strContextFactory != null && strContextFactory.length() > 0)
        	{
        		EBWLogger.logDebug(this,"Creating InitialContext for EJBConnection from remote machine "+systemInfo.get("IP_ADDRESS"));
        		   if(ipaddress.indexOf(',')>0 && portno.indexOf(',')>0)
            		{
            			System.out.println(" coming into cluster configuration");
            			String[] iparray = ipaddress.split(",");
            			String[] portarray = portno.split(",");
            			
            			EBWLogger.logDebug(this,"The no of ip addresses are "+iparray.length);
            			EBWLogger.logDebug(this,"The no of ports are "+portarray.length);
            			
            			if(iparray.length==portarray.length)
            			{
            				StringBuffer strb = new StringBuffer();
            			
            				for(int i=0,j=iparray.length,k=portarray.length;i<j&&i<k;i++)
            				{
            					strb.append(iparray[i]+":"+portarray[i]+",");
            				}
            				strb.deleteCharAt(strb.length()-1);
            			
            				url = strb.toString();
            				
            				EBWLogger.logDebug(this,"the url for clustered environment is "+url);
            				
            			}
            			else
            			{
            				throw new ClusterUrlException();
            			} 
            		}
                    else
                    {
                   	 url=systemInfo.get("IP_ADDRESS") + ":" + systemInfo.get("PORT_NO");
                   	 EBWLogger.logDebug(this," The url for single server is "+url);
                   	 
                    }
	        	
		        h.put(Context.INITIAL_CONTEXT_FACTORY,systemInfo.get("FACTORY_NAME"));
		    //  h.put(Context.PROVIDER_URL,systemInfo.get("BASE_PROTOCOL")+"://"+systemInfo.get("IP_ADDRESS")+":"+systemInfo.get("PORT_NO"));
		        
		    	h.put(Context.PROVIDER_URL,systemInfo.get("BASE_PROTOCOL")+"://"+url);
		        
				c = new InitialContext(h);
        	}else{
        		EBWLogger.logDebug(this,"Creating InitialContext from Same machine.");
        		c = new InitialContext();
        	}
			ctxCache.put(systemInfo.get("IP_ADDRESS"),c);
        }
        else{
        	c = (InitialContext)ctxCache.get(systemInfo.get("IP_ADDRESS"));
        }
        
        return (InitialContext)c;
        
    }
}
