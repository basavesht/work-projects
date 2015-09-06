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
import java.util.ArrayList;
/**
 *
 * EBWTable gets the data from DataInterface implemented classes. 
 * EBWTable taglibrary uses reflection to find the appropriate 
 * DataInterface object for the table by passing table name. It 
 * populates the data from the DataInterface.
 */
public interface DataInterface extends Serializable{
	//public ArrayList getData();
	public Object getData();
	//public void setData(ArrayList data);
	public void setData(Object data);
	public int getDataRowCount();
	public void setDataRowCount(int rowCount);
	public ArrayList getRowHeader();
	public void setRowHeader(ArrayList rowHeader);
	public ArrayList getColAttrObjs();
	public void setColAttrObjs(ArrayList colAttrObjs);
	public String getTableName();
	public void setTableName(String tableName);
	public void setWhereCondition(String where);
}