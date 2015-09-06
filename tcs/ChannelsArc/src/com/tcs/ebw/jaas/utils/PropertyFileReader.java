/*
 * Created on Dec 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.jaas.utils;

import java.util.ResourceBundle;

/**
 * 
 * This class is used to read property files to get configuration informations like database
 * connection settings.
 * 
 * @author  TCS
 * @version 1.0
 */
public class PropertyFileReader {

	
	/**
	 * Stores resource bundle (property file) object
	 */
	private static ResourceBundle connProperties;
	
	/**
	 * This method reads the property file using resource bundle
	 * @param propertyName - Name of the key to be retrieved from the property file.
	 * @return  - returns the value of the read key given in propertyName.
	 * @throws Exception 
	 */
	public static String getProperty(final String propertyName) throws Exception{
			connProperties = ResourceBundle.getBundle("ebw");
			return connProperties.getString(propertyName);
	}
}
