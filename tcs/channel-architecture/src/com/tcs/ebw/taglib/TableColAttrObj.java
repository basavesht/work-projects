/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */

package com.tcs.ebw.taglib;

import java.io.Serializable;


public class TableColAttrObj implements Serializable {
	private String colName=null;
	/* Display Label stores the column heading */
	private String displayLabel=null;
	/* SpanDisplay Label stores the column heading */
	private String spanDisplayLabel=null;
	/* Check required or not */
	private String fieldLength=null;
	private String componentType=null;
	private String dataType =null;
	/* Default VAlue stores the service name for a combobox component */
	private String defaultValue=null;
	/* Check required or not */
	private String fieldAttribute=null;
	/* HTML Tag for each column */
	private String tagContent=null;
	/* Dynamic Value stores where condition for combobox service  */
	private String dynamicValue=null;
	/* TD column size can be specified in the DisplayLength */
	private String displayLength=null;
	private String rowSpan=null;
	private String colSpan=null;
	/* Attributes for Rep comp*/
	private String disptype=null;
	private String reportHeader = null;
	private String reportColumnOps = null;
	private int level = 0;
	
	
	  /*****  setter and getter methods for TMFields class  *****/
    
    // For Level
	   public void setLevel(int level)
    {
     this.level=level;
	  }
    public int getLevel()
    {
     return this.level;
	  }
	  
	  // For Headers
	 


/*****   End of TMFields Setter and getter methods  ******/
	
	
	
	/**
	 * @return Returns the colName.
	 */
	public String getColName() {
		return colName;
	}
	/**
	 * @param colName The colName to set.
	 */
	public void setColName(String colName) {
		this.colName = colName;
	}
	/**
	 * @return Returns the componentType.
	 */
	public String getComponentType() {
		return componentType;
	}
	/**
	 * @param componentType The componentType to set.
	 */
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	/**
	 * @return Returns the dataType.
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * @param dataType The dataType to set.
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return Returns the defaultValue.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue The defaultValue to set.
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @return Returns the displayLabel.
	 */
	public String getDisplayLabel() {
		return displayLabel;
	}
	/**
	 * @param displayLabel The displayLabel to set.
	 */
	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}
	
	/**
	 * @return Returns the displayLabel.
	 */
	public String getSpanDisplayLabel() {
		return spanDisplayLabel;
	}
	/**
	 * @param displayLabel The displayLabel to set.
	 */
	public void setSpanDisplayLabel(String spanDisplayLabel) {
		this.spanDisplayLabel = spanDisplayLabel;
	}
	
	/**
	 * @return Returns the fieldAttribute.
	 */
	public String getFieldAttribute() {
		return fieldAttribute;
	}
	/**
	 * @param fieldAttribute The fieldAttribute to set.
	 */
	public void setFieldAttribute(String fieldAttribute) {
		this.fieldAttribute = fieldAttribute;
	}
	/**
	 * @return Returns the fieldLength.
	 */
	public String getFieldLength() {
		return fieldLength;
	}
	/**
	 * @param fieldLength The fieldLength to set.
	 */
	public void setFieldLength(String fieldLength) {
		this.fieldLength = fieldLength;
	}
    /**
     * @return Returns the tagContent.
     */
    public String getTagContent() {
        return tagContent;
    }
    /**
     * @param tagContent The tagContent to set.
     */
    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }
    
    public String toString() {
    	StringBuffer strBToStr = new StringBuffer();
    	strBToStr.append("colName=" + colName + "\r\n");
    	strBToStr.append("spanDisplayLabel=" + spanDisplayLabel + "\r\n");
    	strBToStr.append("colSpan=" + colSpan + "\r\n");
    	strBToStr.append("displayLabel=" + displayLabel + "\r\n");
    	strBToStr.append("fieldLength=" + fieldLength + "\r\n");
    	strBToStr.append("componentType=" + componentType + "\r\n");
    	strBToStr.append("dataType=" + dataType + "\r\n");
    	strBToStr.append("defaultValue=" + defaultValue + "\r\n");
    	strBToStr.append("fieldAttribute=" + fieldAttribute + "\r\n");
    	strBToStr.append("tagContent=" + tagContent + "\r\n");
    	strBToStr.append("dynamicValue=" + dynamicValue + "\r\n");
    	
    	return strBToStr.toString();
    }
    /**
     * @return Returns the dynamicValue.
     */
    public String getDynamicValue() {
        return dynamicValue;
    }
    /**
     * @param dynamicValue The dynamicValue to set.
     */
    public void setDynamicValue(String dynamicValue) {
        this.dynamicValue = dynamicValue;
    }
    /**
     * @return Returns the displayLength.
     */
    public String getDisplayLength() {
        return displayLength;
    }
    /**
     * @param displayLength The displayLength to set.
     */
    public void setDisplayLength(String displayLength) {
        this.displayLength = displayLength;
    }
	public String getRowSpan() {
		return rowSpan;
	}
	public void setRowSpan(String rowSpan) {
		
		this.rowSpan = rowSpan;
	}
	public String getColSpan() {
		return colSpan;
	}
	public void setColSpan(String colSpan) {
		
		this.colSpan = colSpan;
	}
	
	public String getDisptype() {
		return disptype;
	}
	public void setDisptype(String disptype) {
		this.disptype = disptype;
	}
	public String getReportColumnOps() {
		return reportColumnOps;
	}
	public void setReportColumnOps(String reportColumnOps) {
		this.reportColumnOps = reportColumnOps;
	}
	public String getReportHeader() {
		return reportHeader;
	}
	public void setReportHeader(String reportHeader) {
		this.reportHeader = reportHeader;
	}	 public Object getHeader() { // TODO Auto-generated method stub	        return null;	    }	    public Object getColumnOp() { // TODO Auto-generated method stub	        return null;	    }
}
