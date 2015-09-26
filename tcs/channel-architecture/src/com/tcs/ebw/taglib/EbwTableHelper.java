/*
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.taglib;


import org.apache.struts.action.ActionForm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.Set;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
//import java.util.ResourceBundle;
//import java.util.Locale;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;

import com.tcs.ebw.codegen.util.ExcelConnection;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.StringUtil;
import com.tcs.ebw.taglib.DataInterface;



/**
 * @author tcs
 *
 */
public class EbwTableHelper implements DataInterface, Serializable{	
	private ExcelConnection excel=null;
	private ResultSet rs = null;
	private ResultSetMetaData rsmd = null;
	private ArrayList headerRow = null;
	private Object data = null;
	private ActionForm searchForm=null;
	private String strQuery =null;
	private String tableName=null;
	private String strDb=null;
	private String tableTitle=null;
	private ArrayList colAttrObjs=null;
	private String whereCondition;
	private ArrayList dataWithHeader = null;
	private int rowCount=0;
	
	public EbwTableHelper(ActionForm form,String strDbname, String strTableName){
	 	searchForm=form;
	 	tableName = strTableName;
	 	populateData(strDbname,strTableName);
	}
	
	public EbwTableHelper(String strQuery,String strDbname, String strTableName){
	 	this.strQuery=strQuery;
	 	tableName = strTableName;
	 	populateData(strDbname,strTableName);
	}
	
	public EbwTableHelper(String strTableName){
        excel = null;
        rs = null;
        rsmd = null;
        headerRow = null;
        data = null;
        searchForm = null;
        strQuery = null;
        tableName = null;
        strDb = null;
        tableTitle = null;
        colAttrObjs = null;
        rowCount = 0;
        tableName = strTableName;
        
        if (strTableName != null && strTableName.length() > 0) {
	        try {
				//Service Call.....
				IEBWService objService = EBWServiceFactory.create(strTableName);
				java.util.ArrayList objArrayList = ((java.util.ArrayList) objService.execute(null, null));
		
				/**
				 * Get the datacollection object (DataInterface) from Form which will populate
				 * table column definitions by reading the excel sheet if any
				 * non label fields are to be dislayed. The set the data to it,
				 * which has been got by the above service call.
				 */
				setData(objArrayList);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
        }
	}
	
	public EbwTableHelper(String strDbname, String strTableName){
		tableName = strTableName;
		populateData(strDbname, strTableName);
	}

	/*public EbwTableHelper(EbwForm form,String tableName,String keyCol,String sessionId,HashMap usrObjMap,int dataRowCount,ArrayList data,Object[] colnames){
		
	}
	
		public void performPgnSession(String keyCol,String sessionId,HashMap usrObjMap,int dataRowCount,ArrayList data,Object[] colnames){
		
	}*/
	
	/*public static void performPgnSession(Object formObj,String keyCol,String sessionId,HashMap usrObjMap,Object[] colnames){
		String[] primaryColArray = null;
		int dataRowCount = formObj.getDataRowCount();
		if(keyCol!=null && keyCol.length()>0){
			primaryColArray = keyCol.split(",");
			//EBWLogger.trace(this,"primaryColArray :"+primaryColArray.length);
		}	
		
	}*/
	
	/**
	 * This method will populate data from excel into 
	 * Form sent to generate list. 
	 * 
	 * @throws Exception
	 */
	
	private void populateData(String strDbname, String strTableName){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into populateData():- strDbname="+strDbname+"strTableName="+strTableName);
		try{
			excel = new ExcelConnection();
			strDb = strDbname;
			excel.connect(strDb);
			
			if(searchForm!=null) {
			    //if (whereCondition!=null) {
			        //rs = excel.getResultSet(strTableName,searchForm, whereCondition);
			    //} else {
			        rs = excel.getResultSet(strTableName,searchForm);
			    //}
			} else if(strQuery!=null && strQuery.length()>0) {
				rs = excel.getResultSet(strTableName,strQuery);
			} else {
				rs = excel.getResultSet(strTableName);
			}
			
			// Take the header
			headerRow = new ArrayList();
			int colCount=0;
			int rowCount=0; 
			if(rs!=null){
				//String bundleName = db.substring(db.lastIndexOf("\\")+1,db.length()-4);
				//Consider table name as resource bundle name 
				
				/*//EBWLogger.logDebug(this,"bundle name is "+strTableName);
				ResourceBundle bundle = ResourceBundle.getBundle("com.tcs.ebw.codegen.beans."+strTableName,Locale.getDefault(),this.getClass().getClassLoader());
				colCount =excel.getColumnCount();
				//EBWLogger.logDebug(this,"Column count :"+colCount);
				//Fill Data
				data = new ArrayList();
				while(rs.next()){
					ArrayList row = new ArrayList();
						if(rowCount==0){
							LinkedHashMap headerMap = new LinkedHashMap();
							
							//Put the table title string if exists
							if(bundle.getString("searchtitle")!=null)
								headerMap.put("searchtitle",bundle.getString("searchtitle"));
							if(bundle.getString("tabletitle")!=null)
								headerMap.put("tabletitle",bundle.getString("tabletitle"));
							
							for(int i=1;i<=colCount;i++)
							headerMap.put(excel.getRsMetaData().getColumnName(i),bundle.getString(excel.getRsMetaData().getColumnName(i)));
							headerRow.add(headerMap);
							setRowHeader(headerRow);
							rowCount++;
							//EBWLogger.logDebug(this,"Header over");
						}
						else{
							row = new ArrayList();
							for(int i=1;i<=colCount;i++){
								row.add(rs.getString(i));
							}
							//EBWLogger.logDebug(this,"row "+rowCount++);					
							data.add(row);
						}
				}
				*/

				colCount =excel.getColumnCount();
				//EBWLogger.logDebug(this,"Column count :"+colCount);
				//Fill Data
				data = new ArrayList();
				rowCount=0;
				
					LinkedHashMap headerMap = new LinkedHashMap();
					
					//Put the table title string if exists
					if(tableTitle!=null){
						headerMap.put("searchtitle",tableTitle+" Search");
						headerMap.put("tabletitle",tableTitle);
						//EBWLogger.logDebug(this,"Table title is :"+tableTitle);
					}
					String colname="";
					for(int i=1;i<=colCount;i++){
						colname=excel.getRsMetaData().getColumnName(i);
						//EBWLogger.logDebug(this,"colname from form "+colname);
						headerMap.put(colname, StringUtil.allInitCaps(colname));
					}
					
					headerRow.add(headerMap);
					setRowHeader(headerRow);
				
					
				int colType;
				String colData;
				while(rs.next()){
					//EBWLogger.logDebug(this,"Processing Row "+rowCount++);
					ArrayList row = new ArrayList();
					for(int i=1;i<=colCount;i++){
					    colType = excel.getRsMetaData().getColumnType(i);
					    if (colType == Types.FLOAT || 
					            colType == Types.DOUBLE ||
					            colType == Types.DECIMAL) {
						    colData = rs.getString(i);
						    colData = StringUtil.convertToIntegerStr(colData); 
					    } else if (colType == Types.DATE || colType == Types.TIMESTAMP) {
					        colData = StringUtil.convertToDateStr(rs.getDate(i));
					    } else {
						    colData = rs.getString(i);
					    }
				        row.add(colData);
					}
					((ArrayList)data).add(row);
				}
			}
			excel.closeConnection();
		}catch(NullPointerException nullexp){
			new EbwException(this,nullexp).printEbwException();
		}catch(SQLException sqlexp){
			new EbwException(this,sqlexp).printEbwException();
		}
		catch(Exception exp){
			new EbwException(this,exp).printEbwException();
		}
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exiting from populateData()");
	}
	
	
	

	
	/* (non-Javadoc)
	 *  * @see com.tcs.ebw.codegen.taglib.DataInterface#getData()
	 */
	public Object getData() {
			return data;
	}

	/* (non-Javadoc)
	 * @see com.tcs.ebw.codegen.taglib.DataInterface#setData(java.util.ArrayList)
	 */
	public void setData(Object data) {
				
			if(EBWLogger.isInfoEnabled())
				EBWLogger.trace(this,"Entered into setData()"+data);
			
		if(data.getClass().getName().equals("java.util.ArrayList")){
		
	    dataWithHeader =  new ArrayList();
	    dataWithHeader.addAll((ArrayList)data);
	    
	    if (data != null) {
			/**
			 * For setting rowHeader we need to convert Arraylist contents
			 * to have LinkedHashMap for each value. Key of LinkedHashMap will
			 * be the column name and value of the LinkedHashMap will be the Label
			 * for the column read from resource bundle for displaying as table column name.
			 */
	    
	        ArrayList rowHeader = (ArrayList)((ArrayList) data).get(0); //First row of result has column names.
			LinkedHashMap lm=new LinkedHashMap();
			
			for(int i=0;i<rowHeader.size();i++)
				lm.put(rowHeader.get(i).toString(), rowHeader.get(i).toString());
	
			rowHeader = new ArrayList();
			rowHeader.add(lm);
			
			setRowHeader(rowHeader);
			
			
			((ArrayList)data).remove(0); //remove first row as it is the 
	        setDataRowCount(((ArrayList)data).size());
	      // System.out.println ("rowHeader : " + rowHeader);
	       System.out.println("setDataRowCount()**"+((ArrayList)data).size());
	    	}
		} 
       // System.out.println ("Data : " + data);
        this.data = data;
        if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exiting from setData()");
	}

	/* (non-Javadoc)
	 * @see com.tcs.ebw.codegen.taglib.DataInterface#getDataRowCount()
	 */
	public int getDataRowCount() {
		return ((ArrayList)data).size();
	}

	/* (non-Javadoc)
	 * @see com.tcs.ebw.codegen.taglib.DataInterface#setDataRowCount(int)
	 */
	public void setDataRowCount(int rowCount) {
		this.rowCount=rowCount;
		
	}

	/* (non-Javadoc)
	 * @see com.tcs.ebw.codegen.taglib.DataInterface#getRowHeader()
	 */
	public ArrayList getRowHeader() {
		return this.headerRow;
	}

	/* (non-Javadoc)
	 * @see com.tcs.ebw.codegen.taglib.DataInterface#setRowHeader(java.util.ArrayList)
	 */
	public void setRowHeader(ArrayList rowHeader) {
		this.headerRow=rowHeader;
		EBWLogger.logDebug(this,"in tblhelper setrowheader:"+rowHeader);
		
	}
    
    public DataInterface getTable1Collection(){
    	return this;
    }
    
    public DataInterface getCollection(){
    	return this;
    }
    
    public static void main(String args[]){
    	String dbName = "D:\\BalaWorks\\Coding\\CodeGenerator\\Model\\Portftable.xls";
    	String tableName = "PortfolioAnalysis";
    	EbwTableHelper excelform = new EbwTableHelper(dbName,tableName);
    	excelform.setTableTitle("Table Maintenance");
    	//EBWLogger.logDebug(this,"Excelform completed...");
    }
    
    
	/**
	 * @return Returns the tableName.
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName The tableName to set.
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * @return Returns the tableTitle.
	 */
	public String getTableTitle() {
		return tableTitle;
	}
	/**
	 * @param tableTitle The tableTitle to set.
	 */
	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
		/*if(tableTitle!=null){
		    ((LinkedHashMap) getRowHeader().get(0)).put("tabletitle",tableTitle);
		    ((LinkedHashMap) getRowHeader().get(0)).put("searchtitle",tableTitle+" Search");
		}*/
		//populateData(strDb,tableName);
	}
	/**
	 * @return Returns the colAttrObjs.
	 */
	public ArrayList getColAttrObjs() {
		return colAttrObjs;
	}
	/**
	 * @param colAttrObjs The colAttrObjs to set.
	 */
	public void setColAttrObjs(ArrayList colAttrObjs) {
		this.colAttrObjs = colAttrObjs;
	}
    /**
     * @param whereCondition The whereCondition to set.
     */
    public void setWhereCondition(String whereCondition) {
        this.whereCondition = whereCondition;
    }
    /**
     * @return Returns the strQuery.
     */
    public String getStrQuery() {
        return strQuery;
    }
    /**
     * @param strQuery The strQuery to set.
     */
    public void setStrQuery(String strQuery) {
        this.strQuery = strQuery;
    }
    public ArrayList getDataWithHeader() {
        return dataWithHeader;
    }
    
	
}
