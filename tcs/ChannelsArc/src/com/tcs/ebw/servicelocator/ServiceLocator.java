/*
 * Created on Nov 14, 2005
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.servicelocator;

//Java utility class
import java.util.Hashtable;

// JNDI classes
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.tcs.ebw.config.EBWApplConfig;
import com.tcs.ebw.server.EBWService;
import com.tcs.ebw.server.EbwEJBHome;

import com.tcs.ebw.common.util.StringUtil;

/**
 * This class is implemented as a singleton class and is a central place for
 * looking up objects in the JNDI tree.
 * 
 * @author TCS
 */
public class ServiceLocator {
    // Singleton instance
    private static ServiceLocator serviceLocator = new ServiceLocator();

    // Cache of objects in JNDI tree
    private Hashtable homeCache;

    // Initial context
    private InitialContext defaultContext;

    /**
     * Private constructor, which initializes all the tables and the default
     * InitialContext.
     */
    private ServiceLocator() {
        try {
            Hashtable hash = new Hashtable();
            EBWApplConfig config = EBWApplConfig.getInstance();
            hash.put(Context.INITIAL_CONTEXT_FACTORY,
                    config.getValue(Context.INITIAL_CONTEXT_FACTORY));
            hash.put(Context.PROVIDER_URL, 
                    config.getValue(Context.PROVIDER_URL));
            
            homeCache = new Hashtable();
            
            StringUtil.printConsole("ServiceLocator() : InitialContext : ", hash);
            StringUtil.printConsole("ServiceLocator() : Config : ", config);
            
            defaultContext = new InitialContext(hash);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Exception in the constructor of ServiceLocator "
                            + "class : " + ex.toString());
        }
    }

    /**
     * Method to access the Singleton instance of the ServiceLocator
     * 
     * @return <b>ServiceLocator </b> The instance of this class
     */
    public static ServiceLocator getLocator() {
        if (serviceLocator==null) {
            serviceLocator = new ServiceLocator();
        }
        
        return serviceLocator;
    }

    /**
     * Method to return an object in the default JNDI context, with the supplied
     * JNDI name.
     * 
     * @param jndiName The JNDI name
     * @return Object The object in the JNDI tree for this name.
     * @throws ServiceLocatorException Exception this method can throw
     */
    public EBWService getService(String jndiName) throws Exception {
        try {
            StringUtil.printConsole("ServiceLocator.getService() : jndiName : ", jndiName);
            
            if (!homeCache.containsKey(jndiName)) {
                // If the service is not in the cache, get the object for the
                // supplied jndi name and put it in the cache
                homeCache.put(jndiName, defaultContext.lookup(jndiName));
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            throw new ServiceLocatorException("Exception thrown from getService "
                    + "method of ServiceLocator class : " + ex.getMessage());
        } catch (SecurityException ex) {
            ex.printStackTrace();
            throw new ServiceLocatorException("Exception thrown from from getService "
                    + "method of ServiceLocator class : " + ex.getMessage());
        }
        // Return object from cache
        EBWService objService = ((EbwEJBHome) homeCache.get(jndiName)).create();
        return objService;
    }
}