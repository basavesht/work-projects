/*
 * Created on Dec 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.common.util;

import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.LinkedHashMap;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Locale;
import java.io.File;
import java.io.FileInputStream;

import com.tcs.ebw.exception.EbwException;

/**
 * @author 152820
 * This class is used to read property files to get configuration informations like database
 * connection settings.
 */
public class PropertyFileReader {

	
	/**
	 * Stores resource bundle (property file) object. Specific to ebw.properties
	 */
	private static Properties ebwconnProperties=null;
	
	private static ResourceBundle ebwconfigProperties=null;
	
	private   static ResourceBundle connProperties=null;
	
	private static HashMap propertyValCache=new HashMap();
	
	static{
			//System.out.println("Property file reader static block");
			populateEBWProperties();
	}
	/**
	 * 
	 * @param propertyName - Name of the key to be retrieved from the property file.
	 * @return  - returns the value of the read key given in propertyName.
	 * @throws Exception 
	 */
	public static String getProperty(final String propertyName) throws Exception{
		Object propertyVal =null;
		propertyVal = propertyValCache.get(propertyName);
			if(propertyVal==null){
				if(ebwconnProperties==null){
					/*ebwconfigProperties = ResourceBundle.getBundle("ebwconfig");
					String ebwpropertiespath = ebwconfigProperties.getString("ebw_properties_path");
					if(!ebwpropertiespath.endsWith("/"))
						ebwpropertiespath+="/";
					try{
						ebwconnProperties = new Properties();
						ebwconnProperties.load(new FileInputStream(new File(ebwpropertiespath+"ebw.properties")));
					}catch(Exception e){
						e.printStackTrace();
					}
					
					ebwconnProperties.list(System.out);
					*/
					
					
				}
				propertyVal=ResourceBundle.getBundle("ebw").getString(propertyName).trim();
			}
			//EBWLogger.trace("PropertyFileReader","Finished getProperty(final String propertyName)");
			return (String)propertyVal;
	}
	
	private static void  populateEBWProperties() {
		try{
			/*ebwconfigProperties = ResourceBundle.getBundle("ebwconfig");
			String ebwpropertiespath = ebwconfigProperties.getString("ebw_properties_path");
			if(!ebwpropertiespath.endsWith("/"))
				ebwpropertiespath+="/";
			//ebwconnProperties = ResourceBundle.getBundle(ebwpropertiespath+"ebw");
			ebwconnProperties = new Properties();
			try{
				ebwconnProperties.load(new FileInputStream(new File(ebwpropertiespath+"ebw.properties")));
			}catch(Exception e){
				e.printStackTrace();
			}
		    Enumeration enum1 = ebwconnProperties.keys();
		    */
			ebwconfigProperties = ResourceBundle.getBundle("ebw");
			
			Enumeration enum1 = ebwconfigProperties.getKeys();
			
		    String key=null;
		    while(enum1.hasMoreElements()){
		        key = (String)enum1.nextElement();
		        propertyValCache.put(key,ebwconfigProperties.getString(key));    
		    }
		}catch(Exception mre){
			//new EbwException("PropertyFileReader",mre).printEbwException();
			mre.printStackTrace();
		}
		//EBWLogger.trace("PropertyFileReader","Finished populateEBWProperties");
	} 
	
	public static HashMap getProperties(String propertyFileName) {
		HashMap resultMap = new HashMap();
		try{
		    connProperties = ResourceBundle.getBundle(propertyFileName);
		    Enumeration enum1 = connProperties.getKeys();
		    String key=null;
		    
		    while(enum1.hasMoreElements()){
		        key = (String)enum1.nextElement();
		        resultMap.put(key,connProperties.getString(key));    
		    }
		}catch(Exception mre){
			new EbwException("PropertyFileReader",mre).printEbwException();
			EBWLogger.logError("PropertyFileReader",mre.getCause().getMessage());
		}
	    return resultMap;
	}
	
	public  static String getPropertyKeyValue(String propertyFileName, String keyName) {
	    try{
	    	connProperties = ResourceBundle.getBundle(propertyFileName);
	    }catch(Exception mre){
			new EbwException("PropertyFileReader",mre).printEbwException();
			EBWLogger.logError("PropertyFileReader",mre.getCause().getMessage());
		}
	    return (String)connProperties.getString(keyName);
	}
	
	/**
	 * This method will get the resource bundle object based on the langCode
	 * and countryCode setted in SessionUtil.If nothing is setted in SessionUtil
	 * bundle will be chosen as per the JVM locale.This will allow a provision in changing
	 * Resource bundle at runtime as a part of Internationalization.
	 * @param filename
	 * @return rb
	 */
	
	public static ResourceBundle getBundle(String filename){
	String langCode= (String)SessionUtil.get("langCode");
	String countryCode=(String)SessionUtil.get("countryCode");
	Locale currentLocale=null;
	if(langCode!=null && countryCode!=null)
		currentLocale=new Locale(langCode,countryCode);
	else if(langCode!=null )
		currentLocale=new Locale(langCode);
	else
		currentLocale=Locale.getDefault();
	    ResourceBundle rb=ResourceBundle.getBundle(filename,currentLocale);
		return rb;
	}
	
	
	public static void main(String args[]){
		try{
			//SessionUtil.set("langCode", "fr");
			//SessionUtil.set("countryCode", "CN");
			//ResourceBundle rb_fr=PropertyFileReader.getBundle("com.tcs.ebw.common.util.test");
			//String msg=rb_fr.getString("CARTS");
			
			
			//System.out.println("Hi Test Me--"+msg);
			//System.out.println("property val :"+PropertyFileReader.getPropertyKeyValue("EBWErrorCodes", "0002"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
