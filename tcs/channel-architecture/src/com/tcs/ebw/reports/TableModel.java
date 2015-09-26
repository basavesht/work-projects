/*
 * TableModel
 * 
 * Version 1.1
 *
 * Date: 12/09/2005
 *  
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.reports;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.tcs.ebw.taglib.DataInterface;

/**
 * This class is used to give the layout structure to tableModel.
 * @author     tcs
 * @version    1.0
 */
public class TableModel implements DataInterface{
	public ArrayList data;
    private int dataRowCount;
    private ArrayList rowHeader;
    private ArrayList row1;
    private ArrayList row2;
    private ArrayList row3;
    private ArrayList row4;
    private ArrayList row5;
    private String whereCondition;
    
	public TableModel(){
		data = null;
        dataRowCount = 0;
        rowHeader = null;
        row1 = null;
        row2 = null;
        row3 = null;
        row4 = null;
        row5 = null;
        rowHeader = new ArrayList();
        LinkedHashMap headercols = new LinkedHashMap();
        headercols.put("payeeID", "Payee ID");
        headercols.put("localName1", "Local Name1");
        headercols.put("localName2", "Local Name2");
        headercols.put("procPymtCenter", "Proc Pymt Center");
        headercols.put("localAddress1", "Local Address1");
        headercols.put("localAddress2", "Local Address2");
        rowHeader.add(headercols);
        row1 = new ArrayList();
        row1.add("1234");
        row1.add("Alcon Exporters and Imoporters");
        row1.add("CH-ZRH");
        row1.add("23-Zurich Road");
        row1.add("Zurich");
        row1.add("Zurich");
        row2 = new ArrayList();
        row2.add("213");
        row2.add("Denmark Food Products");
        row2.add("CH-ZRH");
        row2.add("Street No.6");
        row2.add("Zurich");
        row2.add("Zurich");
        row3 = new ArrayList();
        row3.add("134");
        row3.add("Exporters and Imoporters");
        row3.add("ZRH");
        row3.add("23-Manhattan Road");
        row3.add("Zurich");
        row3.add("Zurich");
        row4 = new ArrayList();
        row4.add("34");
        row4.add("Alcon Exporters and Imoporters");
        row4.add("CH-ZRH");
        row4.add("23-Zurich Road");
        row4.add("Zurich");
        row4.add("Zurich");
        data = new ArrayList();
        data.add(row1);
        data.add(row2);
        data.add(row3);
        data.add(row4);
	}
	public ArrayList getData(){
		return data;
	}
	public void setData(ArrayList data){
		this.data = data;
	}
	public int getDataRowCount(){
		dataRowCount = data.size();
		return dataRowCount;
	}
	public void setDataRowCount(int rowCount){
		this.dataRowCount = rowCount;
	}
	public ArrayList getRowHeader(){
		return rowHeader;
	}
	public void setRowHeader(ArrayList rowHeader){
		this.rowHeader = rowHeader;
	}
	public ArrayList getColAttrObjs(){
		return null;
	}
	public void setColAttrObjs(ArrayList colAttrObjs){
		
	}
	public String getTableName(){
		return null;
	}
	public void setTableName(String tableName){
		
	}
    /**
     * @param whereCondition The whereCondition to set.
     */
    public void setWhereCondition(String whereCondition) {
        this.whereCondition = whereCondition;
    }//	@Override	public void setData(Object data) {		// TODO Auto-generated method stub			}
}
