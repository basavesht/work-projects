/*
 * Created on Jan 3, 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.config;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * EBWApplConfig reads the configuration properties file
 * and content stored in a hashMap for further operations.
 * 
 * @author TCS
 */

public class EBWApplConfig {
    /**
     * This variable used for returning singleton EBWApplConfig object
     */
    private static EBWApplConfig config = null;
    
    /**
     * This variable used for Synchronized block.
     */
    private static Object lock = new Object();
    /**
     * Stores all the Configurations in a HashMap.
     */
    private Hashtable configMap;
    
    
    /**
     * Private constructor for Singleton implementation.
     * It loads all the properties into a HashMap object. 
     */
    private EBWApplConfig() {
        configMap = new Hashtable();
        try {
            ResourceBundle applResource = ResourceBundle.getBundle("ApplicationConfig");
            String key;
            String value;
            Enumeration enumRBKeys = applResource.getKeys();
            while (enumRBKeys.hasMoreElements()) {
                key = (String) enumRBKeys.nextElement();
            	value = applResource.getString(key).trim();
                configMap.put(key, value);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Get Instance returns a Singleton object of
     * EBWApplConfig.
     * 
     * @return EBWApplConfig Object.
     */
    public static EBWApplConfig getInstance() {
        synchronized (lock) {
            if (config == null)
                config = new EBWApplConfig();
        }
        return config;
    }

    /**
     * Get the value from HashMap which is prepopulated.
     * 
     * @param key 	Resourcebundle key
     * @return		Returns ResourceBundle value.
     */
    public String getValue(String key) {
        if (configMap == null) return null;
        return (String) configMap.get(key);
    }
    
    /**
     * ToString method for EBWApplication Config Class.
     */
    public String toString() {
        if (configMap != null) {
            return configMap.toString();
        } else {
            return null;
        }
    }
    
}