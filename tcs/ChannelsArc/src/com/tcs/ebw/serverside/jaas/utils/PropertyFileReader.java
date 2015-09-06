/*
 * Created on Dec 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.serverside.jaas.utils;

import java.util.ResourceBundle;

/**
 * @author 152820
 * This class is used to read property files to get configuration informations like database
 * connection settings.
 */
public class PropertyFileReader {

	
	/**
	 * Stores resource bundle (property file) object
	 */
	private static ResourceBundle connProperties;
	
	/**
	 * 
	 * @param propertyName - Name of the key to be retrieved from the property file.
	 * @return  - returns the value of the read key given in propertyName.
	 * @throws Exception 
	 */
	public static String getProperty(final String propertyName) throws Exception{
			connProperties = ResourceBundle.getBundle("ebw");
			return connProperties.getString(propertyName);
	}
}
