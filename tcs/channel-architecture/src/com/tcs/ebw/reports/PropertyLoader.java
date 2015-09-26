/*
 * PropertyLoader
 * 
 * Version 1.0
 *
 * Created on Jan 4th 2006
 *  
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.reports;

import java.util.Properties;

/**
 * PropertyLoader class is used in case of doing PDF Export of a Page.
 * Its a singleton Class used to load the properties needed for the pdfExport
 * once and using the same all the other exports. 
 * The PropertyLoader class has the loadProperties method which 
 * loads the properties needed for the pdf export.  
 * The class has setter and getter for the font properties, field properties
 * and group properties. 
 * @author      tcs
 * @version     1.0
 */
public class PropertyLoader {
	
	/**
	 * holds the properties related to the groups
	 */
	private Properties  groupProps; 
	/**
	 * holds the properties related to fields
	 */
	private Properties  fieldProps;
	/**
	 * holds the properties related to font
	 */
	private Properties  fontProps;
	
	/**
	 * holds the properties related to font of Data
	 */
	private Properties datafontProps;
	
	/**
	 * holds the properties related to TableGroup
	 */
	private Properties tblGroupProps;
	
	/**
	 * holds the properties related to Table
	 */
	private Properties tableProps;
	
	/**
	 * holds the reference of the PropertyLoader class
	 */
	private static  PropertyLoader propertyLoader;
	
	
	/**
	 * Constructor Implementing SigleTon Pattern
	 *
	 */
	private PropertyLoader()throws Exception{
		loadProperties();
		
	}
		
	/**
	 * This method creates an instance of PropertyLoader
	 * when one not exists. If an instance already exists returns
	 * that thereby allowing only one instance to be created.
	 * 
	 * @return returns instance of the PropertyLoader class
	 * @throws Exception 
	 */
	public static PropertyLoader getInstance()throws Exception{
		if(propertyLoader == null){
			propertyLoader = new PropertyLoader();
		}
		
		return propertyLoader;
		
	}
	
	/**
	 * This method loads the property files requiered for the PDF
	 * export.It loads group Properties, field properties and
	 * font properties requiered for the pdf export.
	 * @throws Exception
	 */
	private void  loadProperties()throws Exception{
		System.out.println("Load Properties Called");
		Properties props = null;
		props = new Properties();
		props.load(getClass().getResourceAsStream("/Group.properties"));
		setGroupProps(props);
		props = new Properties();
		props.load(getClass().getResourceAsStream("/Field.properties"));
		setFieldProps(props);
		props = null;
		props = new Properties();
		props.load(getClass().getResourceAsStream("/FontHeader.properties"));
		setFontProps(props);
		props = null;
				
		props = new Properties();
		props.load(getClass().getResourceAsStream("/FontData.properties"));
		setDatafontProps(props);
		props = null;
		
		props = new Properties();
		props.load(getClass().getResourceAsStream("/TableGroup.properties"));
		setTblGroupProps(props);
		props = null;
		props = new Properties();
		props.load(getClass().getResourceAsStream("/Tbl.properties"));
		setTableProps(props);
		props = null;
		
	}
	
	/**
	 * @return Returns the fieldProps.
	 */
	public Properties getFieldProps() {
		return fieldProps;
	}
	/**
	 * @param fieldProps The fieldProps to set.
	 */
	public void setFieldProps(Properties fieldProps) {
		this.fieldProps = fieldProps;
	}
	/**
	 * @return Returns the fontProps.
	 */
	public Properties getFontProps() {
		return fontProps;
	}
	/**
	 * @param fontProps The fontProps to set.
	 */
	public void setFontProps(Properties fontProps) {
		this.fontProps = fontProps;
	}
	/**
	 * @return Returns the groupProps.
	 */
	public Properties getGroupProps() {
		return groupProps;
	}
	/**
	 * @param groupProps The groupProps to set.
	 */
	public void setGroupProps(Properties groupProps) {
		this.groupProps = groupProps;
	}
	/**
	 * @return Returns the tableProps.
	 */
	public Properties getTableProps() {
		return tableProps;
	}
	/**
	 * @param tableProps The tableProps to set.
	 */
	public void setTableProps(Properties tableProps) {
		this.tableProps = tableProps;
	}
	/**
	 * @return Returns the tblGroupProps.
	 */
	public Properties getTblGroupProps() {
		return tblGroupProps;
	}
	/**
	 * @param tblGroupProps The tblGroupProps to set.
	 */
	public void setTblGroupProps(Properties tblGroupProps) {
		this.tblGroupProps = tblGroupProps;
	}

	/**
	 * @return the propertyLoader
	 */
	public static PropertyLoader getPropertyLoader() {
		return propertyLoader;
	}

	/**
	 * @param propertyLoader the propertyLoader to set
	 */
	public static void setPropertyLoader(PropertyLoader propertyLoader) {
		PropertyLoader.propertyLoader = propertyLoader;
	}

	/**
	 * @return the datafontProps
	 */
	public Properties getDatafontProps() {
		return datafontProps;
	}

	/**
	 * @param datafontProps the datafontProps to set
	 */
	public void setDatafontProps(Properties datafontProps) {
		this.datafontProps = datafontProps;
	}
}
