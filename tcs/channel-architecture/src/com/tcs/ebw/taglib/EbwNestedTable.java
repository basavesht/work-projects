 /* Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */


package com.tcs.ebw.taglib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.tcs.ebw.codegen.beans.ComboboxData;
import com.tcs.ebw.codegen.beans.ExcelForm;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.taglib.DataInterface;
import com.tcs.ebw.taglib.TagLibConstants;
import com.tcs.ebw.taglib.common.CommonConstants;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.StringUtil;
import com.tcs.ebw.common.util.ConvertionUtil;

/**
 * EBWNestedTable taglibrary is used for generate html table. 
 * It gets the data from DataInterface implemented classes. 
 * EBWNestedTable get the form bean from request attribute. It checks 
 * session, if request doesn’t have form bean attribute. After getting 
 * formBean, it uses reflection to find the appropriate DataInterface 
 * object for the table by passing table name. It populates the data 
 * from the DataInterface. 
 *
 * EBWNestedTable has name, border, doubleBorder, displayType, 
 * displaySearch as parameters. DispalyType can be SelectRadio or 
 * SelectCheck. Table has first column as Radio button, or Checkbox 
 * based on the above displayTypes. If the displayType is not given or 
 * empty, then table’s first column will not have Radio or check box 
 * option. DisplaySearch is used for adding search header in the table. 
 * It will have all textfields about the table header if display search 
 * is true. Based on the search condition, it displays filtered data.
 * 
 * @author tcs
 */
public class EbwNestedTable extends TagSupport {
	//Attributes
	private String name = null;
	private String formname = null;
	private String border = null;
	private String css = null;
	private String displayType = null;
	private boolean doubleBorder = false;
	private String displaySearch = "";
	private DataInterface formObj = null;
	private ArrayList data = null;
	private ArrayList tableHeader = null;
	private Object attrObj = null;
	private boolean isHtmlOutput = false;
	private StringBuffer outputBuff = null;
	private String accesskey = null;
	private boolean isMultipleHref = false;
	private String where = "";
	private ResourceBundle formRB = null;
	private ResourceBundle appRB = null;
	private String scrollable = "";
	private String scrollProperties = "";
	private String rowsPerPage = "";
	private String tblTitle="";
	private String noDataFoundMsg="";
	private boolean isColAttrPresent=false;
	private String reordering="";
	private String sorting="";
	private String hideColumns = null;
	
	/**
	 * This Constructor is for testing from main method only.This constructor will not
	 * be called by Taglib at runtime from JSP.
	 * @param formObj-Form Object Coming From request attribute
	 */
	public EbwNestedTable(Object formObj) {
		attrObj = formObj;
		name = ((DataInterface) formObj).getTableName();
		outputBuff = new StringBuffer();
	}
	
	public EbwNestedTable() {
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		try {
			//outputBuff.append("</table>\r\n");
			
			if (isDoubleBorder()) {
				outputBuff.append("</td></tr></table>\r\n");
			}
			
			if (!isHtmlOutput) {
				JspWriter out = pageContext.getOut();
				out.println(outputBuff.toString());
			}
		} catch (Exception ex) {
			new EbwException(this, ex).printEbwException();
		}
		
		return SKIP_BODY;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
	    EBWLogger.trace(this,"Into doStartTag of EBWNestedTableTag");
		boolean colDefFound = false;
		String strData = "";
		String chkData = "";
		String dtType="",align="";
		String initLowerName = StringUtil.initLower(name);
		String columnValuesForFilter="";
		
		String numericalDataType = "integer,float,number,numeric,double";
		
		try {
			/**
			 * Loading the Application's Property file that contains the location of
			 * the property file that contains the displaynames
			 */
			appRB = ResourceBundle.getBundle("ApplicationConfig");
			
			if (!isHtmlOutput) {
				/**
				 * Loading up the properties file  that contains the fieldId and Displaynames as key, value respectively
				 */          
				formRB = ResourceBundle.getBundle(appRB.getString("message-resources"));
			}
			
			tblTitle = formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + name);
		} catch (Exception exc) {
			System.out.println ("Resource Bundle Not found...");
		}
		
		try {
			if (outputBuff == null) {
				outputBuff = new StringBuffer();
			} else {
				outputBuff.delete(0, outputBuff.toString().length());
			}
			
			// This object may come from the constructor for html generation. (attrObj == null) && 
			if (formname != null) { 
				attrObj = pageContext.getRequest().getAttribute(formname);
			}
			// This object may come from the constructor for html generation.
			if ((attrObj == null) && (formname != null)) { 
				attrObj = pageContext.getSession().getAttribute(formname);
			}
			
			/**
			 * do Introspection and get the invoking method to get
			 * DataInterface type data from the UI framework form.
			 */
			Class formClass = Class.forName(attrObj.getClass().getName());
			
			if (!isHtmlOutput) {
			    EBWLogger.trace(this,"Into Non HTML output generation of EBWNestedTableTag");
			    
				Class[] cls = {String.class};
				
				/* This is form clearing the search criteria */
				Object[] objArgNull = {null};
				
				formObj = (DataInterface) formClass.getMethod("get" + StringUtil.initCaps(name) +
						"Collection", null).invoke(attrObj, null);
				
				EBWLogger.logDebug(this,"----"+ ((HttpServletRequest)pageContext.getRequest()).getSession().getAttribute(formname));
				
				if(((HttpServletRequest)pageContext.getRequest()).getSession().getAttribute(formname)!=null && rowsPerPage.length()>0 && (formObj.getData()==null || ((ArrayList)formObj.getData()).isEmpty())){ //To get formbean from session 
					EBWLogger.logDebug(this,"Loading form in EBWNestedTable:"+formname);
				    // Pagewise display for getting
				    formClass = Class.forName( ((HttpServletRequest)pageContext.getRequest()).getSession().getAttribute(formname).getClass().getName());
				    
				    formObj = (DataInterface) formClass.getMethod("get" + StringUtil.initCaps(name) +
				            			"Collection", null).invoke((EbwForm)((HttpServletRequest)(pageContext.getRequest())).getSession().getAttribute(formname), null);
					}
				EBWLogger.trace(this,"FormObject in EBWNestedTable Header " + formObj.getRowHeader());
				EBWLogger.trace(this,"FormObject in EBWNestedTable Data   " + formObj.getData());
			} else {
				formObj = (DataInterface) formClass.getMethod("getCollection",
						null).invoke(attrObj, null);
				if(formObj.getData()==null || ((ArrayList)formObj.getData()).isEmpty()){ //To get formbean from session 
				    									// Pagewise display for getting
				    formClass = Class.forName(pageContext.getSession().getAttribute(formname).getClass().getName());
				    formObj = (DataInterface) formClass.getMethod("get" + StringUtil.initCaps(name) +
							"Collection", null).invoke(attrObj, null);
				}
				
			}
			EBWLogger.trace(this,"Setting where condition to " + where);
			formObj.setWhereCondition(where);
			
			if (scrollable != null && scrollable.equalsIgnoreCase("true")) {
				doubleBorder=true;
			}
			
			LinkedHashMap dataValue = null;
			
			//	This variable is the count of titles in HashMap like tabletitle and searchtitle etc
			int titles = 0;
			
			if (formObj != null) {
			    EBWLogger.trace(this,"Form object is not null So starting to render Table");
				ArrayList colattrobj = ((DataInterface) formObj).getColAttrObjs();
				if(colattrobj!=null && colattrobj.size()>0)isColAttrPresent=true;
				EBWLogger.logDebug(this,"colattrobj got from FormBean for tabledata is :"+colattrobj);
				tableHeader = formObj.getRowHeader();
				
				if (tableHeader == null) {
					//outputBuff.append("tableHeader is null");
				    EBWLogger.trace(this,"Table Header is NULL");
				} else {
					
					EBWLogger.trace(this, "Table Header Before removing " + tableHeader);
					
				    EBWLogger.trace(this,"Table Header Size"+tableHeader.size());
					data = (ArrayList)formObj.getData();
					
					EBWLogger.trace(this,"Data in EBWNestedTable is  :"+data);
					
					if(data.size()>0 || (data.size()==0 && noDataFoundMsg.equalsIgnoreCase("true"))){
						dataValue = (LinkedHashMap) tableHeader.get(0);
				/** Code related to hiding Columns modified by author:152993**/
						/** Checking whether the attribute hideColumns is set as attribute of EBWNestedTable **/
						if(getHideColumns()!=null){
							    /** The attribute hideColumns is given **/
								String hiddenColumns = getHideColumns();
								/** As the attribute is comma separated splitting the String using comma
								 * to get the index to be removed
								 */
								String colsIndex[] = hiddenColumns.split(",");
								//dataValue LinkedHashMap contains the header information of the table
								Iterator headerIterator = dataValue.keySet().iterator();
								//headerNamesList to store the headers in the dataValue
								ArrayList headerNamesList = null;
								//varible to i to add the indexes at the proper index into headerNamesList
								int i=0;
								/**
								 * Iterating thru the headerInfromation and adding each header information
								 * and adding each header to the headerNamesList
								 */ 
								while(headerIterator.hasNext()){
									if(headerNamesList==null){
										headerNamesList = new ArrayList();
									}
									headerNamesList.add(i++,(String)headerIterator.next());
								}
								/**
								 * Iterating thru the indexes to be removed, get the headerName at
								 * the particular index and removing the same from the headerValue which
								 * is of type LinkedHashMap
								 */
								for(int j=0;j<colsIndex.length;j++){
									int index = Integer.parseInt(colsIndex[j]);
									String headerName = (String) headerNamesList.get(index-1);
									dataValue.remove(headerName);
								}
							
						}
						
						EBWLogger.logDebug(this,"Header after removing the given indexes " + dataValue);
				/**  Code related to hidingColumns ends here **/		
						Iterator iterator = dataValue.keySet().iterator();
						Object obj = null;
						
						if (isDoubleBorder()) {
							outputBuff.append(
							"<table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"1\"><tr><td>\r\n");
						}
						int cols = (dataValue.size() - titles);
						outputBuff.append(addCheckLink(cols, initLowerName));
						
						if (scrollable != null && scrollable.equalsIgnoreCase("true")) {
							outputBuff.append("<table width=\"100%\">"); 
						} else {
							// Start table header
						    EBWLogger.trace(this,"Starting TableHeader generation :");
							if (border != null) { //Render an outer table for border if border attribute is given.
								if(displayType==null) {
									outputBuff.append("<table name=\""+initLowerName+"\" id="+initLowerName+" cols="+cols+" width=\"100%\" border=\"" + border + "\">\r\n");
								} else {
									outputBuff.append("<table name=\""+initLowerName+"\" id="+initLowerName+" cols="+(cols+1)+" width=\"100%\" border=\"" + border + "\">\r\n");
								}
							} else { 
								if(displayType==null) {
									outputBuff.append("<table name=\""+initLowerName+"\"  id="+initLowerName+" cols="+cols+" width=\"100%\" border=\"0\">\r\n");
								} else {
									outputBuff.append("<table name=\""+initLowerName+"\"  id="+initLowerName+" cols="+(cols+1)+" width=\"100%\" border=\"0\">\r\n");
								}
							}
						}
						
						tblTitle = getBundleString(name, (String) dataValue.get("tabletitle"), "");
						//System.out.println ("Before Cols : " + cols);
						if ((dataValue.containsKey("tabletitle") || dataValue.containsKey("searchtitle"))) {
							// TableTitle and Search Title;
							titles = 2; 
							cols = (dataValue.size() - titles);
							//if  (tblTitle != null && tblTitle.length() > 0) {
								/*if (border!=null) {
								 outputBuff.append(tblTitle + "<legend>");
								 } else {*/
								/*outputBuff.append("<tr><td class=\"tabletitle\" colspan=\"" +
										cols + "\">" +
										tblTitle +
								"</td></tr>\r\n");*/
							//}
						}
						
						
						// *** Creation of Search Panel Starts Here
						if ((displaySearch != null) &&
								displaySearch.equalsIgnoreCase("true") &&
								(displaySearch.length() > 0)) {
							
							outputBuff.append("<tr class=tableheadsearch>");
							if (getDisplayType() != null) {
							outputBuff.append("<td align=\"center\"><input name=\"searchSubmit\" type=\"image\" src=\"../images/search.gif\"></td>");
							}
							String strObj = "";
							
							StringBuffer searchCondition = new StringBuffer();
							
							//outputBuff.append("<input name=\"" + initLowerName + "FilHide\" type=\"hidden\">");
							searchCondition.append ("onclick=\"javascript:document.forms[0].state.value='");
							if (formname!=null && formname.length() > 0) {
								searchCondition.append (formname.substring(0, formname.lastIndexOf("Form")));
							};
							
							searchCondition.append ("_Search'; document.forms[0].action.value='" + initLowerName + "Search'; document.forms[0].submit();\"");
							boolean firstCol = false;
							StringBuffer strSearch = new StringBuffer();
							while (iterator.hasNext()) {
								obj = iterator.next();
								if (obj != null) {
									strObj = obj.toString();
									if (!strObj.equals("searchtitle") && !strObj.equals("tabletitle")) {
										if (!firstCol) {
											firstCol=true;
											if (getDisplayType() != null) {
												strSearch.append("<td><input type=\"text\" style=\"width:100%\" name=\"search" + strObj + "\"/></td>");
											} else {
												strSearch.append("&nbsp;<input type=\"text\" style=\"width:90%\" name=\"search" + strObj + "\"/></td>");
											}
										} else {
											strSearch.append("<td><input type=\"text\" style=\"width:100%\" name=\"search" + strObj + "\"/></td>");
										}
										
										//searchCondition.append ("'" + initLowerName + strObj + "=' + " + initLowerName + strObj + ".value");
										
										//if (iterator.hasNext()) {
										//searchCondition.append (" + '&' + ");
										//}
									}
								}
							}
							//alert('test : ' + " + initLowerName + "FilHide.value); 
							//searchCondition.append ("document.forms[0].action='" + initLowerName + "Search'; document.forms[0].submit();\"");
							if ((displaySearch != null) &&
									displaySearch.equalsIgnoreCase("true") &&
									(displaySearch.length() > 0)) {
								outputBuff.append("<td align=\"center\"><input name=\"" + initLowerName + "Search\" type=\"image\" src=\"../images/search.gif\" " + searchCondition.toString() + ">");
							}
							outputBuff.append(strSearch);
							outputBuff.append("</tr><tr>\r\n");
						} else {
							outputBuff.append("<tr>");
						}
						// *** End of Search Panel
						int dataIndex=0; //dataIndex is used in getting value from colattributes for each column. Used in below codes.
						
						if (getDisplayType() != null) {//Decide and add a column header for checkbox/radio column
							String strTH="Select";
							if (colattrobj!=null && colattrobj.size() > 0) {
								TableColAttrObj tablecolattrobj = (TableColAttrObj) colattrobj.get(0);
								EBWLogger.logDebug(this,"Value in TableColAttrobj is :"+((TableColAttrObj)colattrobj.get(0)).toString());
								
								if (tablecolattrobj!=null) {
									String dispType = tablecolattrobj.getDataType();
									String strChkHead = tablecolattrobj.getDisplayLabel();
									if (dispType != null &&
											strChkHead != null && strChkHead.length() > 0 &&
											(dispType.equals(TagLibConstants.TABLE_TYPE_SELECT_CHECK) ||
													dispType.equals(TagLibConstants.TABLE_TYPE_SELECT_RADIO))) {
										strTH=strChkHead;
									}
									
								}
							}
							outputBuff.append("<th><b>" + getBundleString(name + "SelectId", strTH, "") + "</b></th>");
							dataIndex=1;
						}
						
						//Print Table Header
						ResourceBundle appRB = ResourceBundle.getBundle("ApplicationConfig");
						iterator = dataValue.keySet().iterator();
						obj = null;
						String strColHd = "";
						int colIndex =-1;
						
						TableColAttrObj tablecolattrobj2=null;
						// Access Key
						String arrKeys[] = null; 
						if (getAccesskey()!=null) {
							arrKeys = getAccesskey().split(",");
						}
						
						ArrayList keyPos = new ArrayList();
						String columnWidth="";
						while (iterator.hasNext()) {
							obj = iterator.next();
							colIndex++;
							
							if (obj!=null) {
								strColHd = obj.toString();
							} else {
								strColHd = "";
							}
							
							if (arrKeys!=null) {
								for (int i=0; i < arrKeys.length; i++) {
									if (arrKeys[i].equals(strColHd)) {
										keyPos.add(new Integer(colIndex));
									}
								}
							}
							
							if (tablecolattrobj2==null) { 
								tablecolattrobj2 = new TableColAttrObj();
								//columnWidth = " width=\"" + String.valueOf(Math.round(100 / cols)) + "%\"";
							}
							
							if (!strColHd.equalsIgnoreCase("tabletitle") && 
									!strColHd.equals("searchtitle")) {
								if (strColHd.length() > 0) {
									if(appRB.getString("Sorting").equalsIgnoreCase("No") || sorting.equalsIgnoreCase("No")) {
									    EBWLogger.logDebug(this,"getting Resource bundle for column :"+strColHd);
									    String colHeading = getBundleString(strColHd, (String) dataValue.get(strColHd), tablecolattrobj2.getDisplayLabel());
									    
									    if(colHeading.trim().length()>0)
									        outputBuff.append("<th class=\"tableheader\"><center>" + colHeading + "</center></th>");
									    
									} else {	
									    EBWLogger.logDebug(this,"Sorting enabled for table"+displayType);
										if(displayType==null) {
											if(colattrobj!=null&&colattrobj.size()>0) {
												if(dataIndex>colattrobj.size()-1) {
													dataIndex=0;
												} else { 
													tablecolattrobj2=(TableColAttrObj)colattrobj.get(dataIndex);
												}
												if((tablecolattrobj2.getColName()).equals(strColHd)) {
												    EBWLogger.logDebug(this,"ColAttribute Equals");
												    outputBuff.append("<th " + columnWidth + "><center><a class=\"tableheader\" href=\"javascript:sortTable("+(colIndex)+","+initLowerName+",'"+tablecolattrobj2.getDataType()+"','"+displaySearch+"');\">" + getBundleString(strColHd, (String) dataValue.get(strColHd), tablecolattrobj2.getDisplayLabel()) + "</a></center></th>");
													dataIndex++;
												} else {
												    EBWLogger.logDebug(this,"ColAttribute Not Equal with strColHd");
													outputBuff.append("<th " + columnWidth + "><center><a class=\"tableheader\" href=\"javascript:sortTable("+(colIndex)+","+initLowerName+",'Varchar','"+displaySearch+"');\">" + getBundleString(strColHd, (String) dataValue.get(strColHd), tablecolattrobj2.getDisplayLabel()) + "</a></center></th>");	
												}
											} else {
											    EBWLogger.logDebug(this,"ColAttribute Not Null or size 0");
											    outputBuff.append("<th " + columnWidth + "><center><a class=\"tableheader\" href=\"javascript:sortTable("+(colIndex)+","+initLowerName+",'Varchar','"+displaySearch+"');\">" + getBundleString(strColHd, (String) dataValue.get(strColHd), "") + "</a></center></th>");
											}
										} else {
										    EBWLogger.logDebug(this,"Display Type is not null");
											if(colattrobj!=null&&colattrobj.size()>0){
												if(dataIndex>(colattrobj.size()-1)) {
													dataIndex=0;
												} else {
													tablecolattrobj2=(TableColAttrObj)colattrobj.get(dataIndex);
												}
//												
												/**
												This is to print columns based on datatype. If columnAttribute is present
												then get the datatype and print table cell based on that dataype.
												If colattribute columnname does not match dbcolumnname, then print
												cell as varchar.
												*/
												if((tablecolattrobj2.getColName()).equals(strColHd)) { 
												    
													if (tablecolattrobj2.getDisplayLength() != null && !tablecolattrobj2.getDisplayLength().equals("")) {
														columnWidth = " width=\"" + tablecolattrobj2.getDisplayLength() + "%\"";
													}else {
														columnWidth="";
													}
													
													EBWLogger.logDebug(this,"Attribute ColumnName equals strColHd, columnName:"+tablecolattrobj2.getColName()+",strColHd:"+strColHd);
													outputBuff.append("<th " + columnWidth + "><center><a class=\"tableheader\" href=\"javascript:sortTable("+(colIndex+1)+","+initLowerName+",'"+tablecolattrobj2.getDataType()+"','"+displaySearch+"');\">" + getBundleString(strColHd, (String) dataValue.get(strColHd), "") + "</a></center></th>");
													dataIndex++;
												} else {
												    EBWLogger.logDebug(this,"Attribute ColumnName Not equals strColHd, ColumnName:"+tablecolattrobj2.getColName()+",strColHd :"+strColHd);
													//outputBuff.append("<th " + columnWidth + "><center><a class=\"tableheader\" href=\"javascript:sortTable("+(colIndex+1)+","+initLowerName+",'Varchar','"+displaySearch+"');\">" + getBundleString(strColHd, (String) dataValue.get(strColHd), "") + "</a></center></th>");
												}             		                  		
											} else {
											    EBWLogger.logDebug(this,"Came to Else part");
												outputBuff.append("<th " + columnWidth + "><center><a class=\"tableheader\" href=\"javascript:sortTable("+(colIndex+1)+","+initLowerName+",'Varchar','"+displaySearch+"');\">" + getBundleString(strColHd, (String) dataValue.get(strColHd), "") + "</a></center></th>");	
											}
										}
									} 
								} else {
									outputBuff.append( "<th " + columnWidth + "><center>-</center></th>");
								}
							}
						}
						
						if (scrollable != null && scrollable.equalsIgnoreCase("true")) {
							outputBuff.append("<td width=\"15\">&nbsp;</td>");
						}
						
						outputBuff.append("</tr>\r\n");
						
						if (scrollable != null && scrollable.equalsIgnoreCase("true")) {
							outputBuff.append("</table>\r\n");
							if (scrollProperties == null || scrollProperties.length() == 0) {
								scrollProperties = "WIDTH: 100%; HEIGHT: 125px";
							}
							outputBuff.append("<DIV style=\"VISIBILITY: visible; OVERFLOW: auto; " + scrollProperties +  "\">");
							
							if(displayType==null) {
								outputBuff.append("<table name=\""+initLowerName+"\"  id="+initLowerName+" cols="+cols+" width=\"100%\" border=\"0\">\r\n");
							} else {
								outputBuff.append("<table name=\""+initLowerName+"\"  id="+initLowerName+" cols="+(cols+1)+" width=\"100%\" border=\"0\">\r\n");
							}
							
							outputBuff.append("<tr>");
							for (int k=0; k < cols; k++) {
								outputBuff.append("<td></td>");
							}
							outputBuff.append("</tr>"); 
							
							if ((displaySearch != null) &&
									displaySearch.equalsIgnoreCase("true") &&
									(displaySearch.length() > 0)) {
								outputBuff.append("<tr>");
								for (int k=0; k < cols; k++) {
									outputBuff.append("<td></td>");
								}
								outputBuff.append("</tr>"); 
							}
						}
						
						//*** Table Header Ends Here
						
						if(data!=null && data.size()>0){
						//*** Table Body Starts here
						Object[] colnames = dataValue.keySet().toArray();
						
						/**  Pagination Realated code **/ 
						//Getting the actual number of rows 
						int dataRowCount = formObj.getDataRowCount();
						//loopCount says till which index of rows we have to loop thru
						//for display of data in the table
						int loopCount = 0;
						//StartIndex from which index of rows we have to loop thru 
						//for display of data in the table 
						int startIndex = 0;
						//pageRowCount will have the count of number of rows per page
						int pageRowCount = 0;
						//isMultiplePage can be used to trace whether the page should
						//have Next and Last link defaulting to false
						boolean isMultiplePage = false;
						//if it has the attribute rowsPerPage
						if(rowsPerPage!=null && rowsPerPage.length()>0){
							//Converting rowsPerPage to primitive type int
							pageRowCount = Integer.parseInt(rowsPerPage);
							//if count of rows per page is less than the actual number of rowsof data
							if(pageRowCount<dataRowCount){
								EBWLogger.logDebug(this,"Rows per page is less than the ActualData Row Count");
								//storing the pageRowCont to loopCount to restrict the actual number of 
								//rows to the number of rowsPerPage set in the taglib attribute
								loopCount = pageRowCount;
								EBWLogger.logDebug(this,"Getting the PaginationIndex Attribute from the request Attribute");
								String pageIndex = ((EbwForm) attrObj).getPaginationIndex();
								if(pageIndex !=null && !pageIndex.equals("")){
									startIndex = Integer.parseInt(pageIndex);
									EBWLogger.logDebug(this,"Start Index for the Pagination is " + startIndex);
								}
								//if pageRowcoutn is greater than or equal to the actual row count
							}else{
								//assigning the actual rowcount to the loopcount 
								EBWLogger.logDebug(this,"Rowsperpage is not less than the actual datarowcount");
								loopCount=dataRowCount;
							}
						}else if(rowsPerPage==null || rowsPerPage.length()<=0){
							//if rowsPerPage attribute is not set, setting the loopCount to
							//the actual rowCount
							EBWLogger.logDebug(this, "RowsPerPage Attribute is not Configured for EBWNestedTable");
							loopCount = dataRowCount;
						}   
						
						int i = 0;
						int p=0;
						/** Pagination Related  Code Ends Here and continued further below**/
						
						EBWLogger.logDebug(this," dataRowCount "+dataRowCount);
						/** Loop through the datarow to print the table data */
						for (i = startIndex; p < loopCount && i<dataRowCount; i++) {
						    columnValuesForFilter=""; //Reset the filter values for every row
							p++;
							if ((i % 2) == 0) {
								outputBuff.append("<tr id=\"" + initLowerName + i + "\" class=\"evenrow\">");
							} else {
								outputBuff.append("<tr id=\"" + initLowerName + i + "\" class=\"oddrow\">");
							}
							
							ArrayList rowData = (ArrayList) data.get(i);
							
							if (getDisplayType() != null) {
								/**
								 * Access Key ie. column def. for check/radio is separated and used.
								 */
								int k= keyPos.size();
								if (k > 0) {
									chkData = "";
									for (int j=0; j < k; j++) {
										chkData = chkData + rowData.get(((Integer) keyPos.get(j)).intValue()) + "~";
									}
									chkData = chkData.substring(0, chkData.lastIndexOf("~"));
								} else {
									chkData = rowData.toString().replaceAll(", ", "~");
									chkData = chkData.substring(chkData.indexOf("[")+1, chkData.lastIndexOf("]"));
								}
								
								if (getDisplayType().equals(TagLibConstants.TABLE_TYPE_SELECT_CHECK)) {
									outputBuff.append("<td class=\"tableData\"><center><input type=\"checkbox\" name=\"" + initLowerName + "SelectId\" value=\"" + chkData + "\"></center></td>");
								} else if (getDisplayType().equals(TagLibConstants.TABLE_TYPE_SELECT_RADIO)) {
									outputBuff.append("<td class=\"tableData\"><center><input type=\"radio\" name=\"" + initLowerName + "SelectId\" value=\"" + chkData + "\"></center></td>");
								}								
							}
							
							for (int j = titles; j < (colnames.length); j++) { // -1 will ignore the table title key in the Map
								colDefFound = false;
								if (colattrobj != null) {
									for (int h = 0; h < colattrobj.size(); h++) {
										//Get Component Information and proceed creating the table cell
										TableColAttrObj tablecolattrobj = (TableColAttrObj) colattrobj.get(h);
										if (tablecolattrobj != null && tablecolattrobj.getDisplayLength() != null && 
												!tablecolattrobj.getDisplayLength().equals("")) {
											columnWidth = " width=\"" + tablecolattrobj.getDisplayLength() + "%\"";
										} else {
											columnWidth="";
										}
										//System.out.println (tablecolattrobj.getColName() + " .equalsIgnoreCase " + (StringUtil.initLower(colnames[j].toString())));
										if ((tablecolattrobj == null) || (colnames[j] == null) ||
												(tablecolattrobj.getColName() == null)) {
											colDefFound = false;
										} else if (tablecolattrobj.getColName().equalsIgnoreCase(StringUtil.initLower(colnames[j].toString())) &&
												!colnames[j].toString().equalsIgnoreCase("searchtitle") &&
												!colnames[j].toString().equalsIgnoreCase("tabletitle")) {
											colDefFound = true;	                                    
											
											//Get data for columns from rowData arraylist
											if (rowData != null) {
												strData = convertToStr(rowData.get(j-titles));
												columnValuesForFilter =  columnValuesForFilter+tablecolattrobj.getColName().toLowerCase()+"=" +strData+",";
												EBWLogger.logDebug(this,"Adding datarow "+columnValuesForFilter);
											} else {
												strData ="";
											}
											
											if (tablecolattrobj.getDataType()!=null) {
												dtType = tablecolattrobj.getDataType().toLowerCase();
												if (dtType.length() > 0 && numericalDataType.indexOf(dtType) > -1) {
													align=" align=\"right\"";
												}
											} else {
												if (!Double.isNaN(Double.parseDouble(strData))) {
													align=" align=\"right\"";
												}
											}
											
											EBWLogger.logDebug(this,"Component Type is :"+tablecolattrobj.getComponentType());
											EBWLogger.logDebug(this,"tablecolattrobj.getColName() "+tablecolattrobj.getColName());
											EBWLogger.logDebug(this,"tablecolattrobj.getDefaultValue() "+tablecolattrobj.getDefaultValue());
											EBWLogger.logDebug(this, "tablecolattrobj.getDynamicValue() "+tablecolattrobj.getDynamicValue());
											
											chkData = rowData.toString().replaceAll(", ", "~");
											chkData = chkData.substring(chkData.indexOf("[")+1, chkData.lastIndexOf("]"));
											
											if (tablecolattrobj.getComponentType().equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_COMBOBOX)) {
											    
											    outputBuff.append("<td " + columnWidth + " class=\"tableData\"><center><select name=\"" + tablecolattrobj.getColName() + "\">");
												//outputBuff.append(getComboboxOptionData(tablecolattrobj.getDefaultValue(), tablecolattrobj.getColName(), strData));
												EBWLogger.logDebug(this,"Dynamic Value passed to Combobox is :"+tablecolattrobj.getDynamicValue());
												//outputBuff.append(getComboboxOptionData(tablecolattrobj.getColName(), tablecolattrobj.getDefaultValue(), tablecolattrobj.getDynamicValue()));
												
												/**strdata is not needed for combobox as the data has to come from a service call. 
												This has to be cached and displayed for pagewise display of table, when the user
												clicks next,previous etc, it should not call the service again.
												*/
												
												//Pass column names and values to this combobox for filtering. Also remove last comma and add with Dyna value
												columnValuesForFilter = columnValuesForFilter + tablecolattrobj.getDynamicValue() + ",";
												//chkData = rowData.toString().replaceAll("~ ~", "~");
												columnValuesForFilter = columnValuesForFilter + "tablerowdata=" + i + "~" + chkData;
												outputBuff.append(getComboboxOptionData(tablecolattrobj.getDefaultValue(), tablecolattrobj.getColName(), columnValuesForFilter));
												outputBuff.append("</select></center></td>");
											}else if (tablecolattrobj.getComponentType().equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_TEXTFIELD)) {
												outputBuff.append("<td " + columnWidth + " class=\"tableData\"><center>" + replaceWithValue(tablecolattrobj, strData, chkData) +"</center></td>");
											} else {
											    EBWLogger.logDebug(this,"Component type is not defined for column :"+strData);
											    if (align.indexOf("right") > -1) {
											        outputBuff.append("<td " + columnWidth + " class=\"tableData\"><div "+align+">" + replaceWithValue(tablecolattrobj, strData, chkData) +"</div></td>");
											    } else {
											        EBWLogger.logDebug(this,"Writing data for undefined component type"+strData);
											        outputBuff.append("<td " + columnWidth + " class=\"tableData\">" + replaceWithValue(tablecolattrobj, strData, chkData) + "</td>");
											    }
											    //outputBuff.append("<td " + columnWidth + align + " class=\"tableData\">" + replaceWithValue(tablecolattrobj, strData) +"</td>");
											}
											align="";
										} else {
										    //EBWLogger.logDebug(this,"tablecolattrobj.getColName() is not matching with column[i]:"+tablecolattrobj.getColName());
										    //outputBuff.append("<td " + columnWidth + " class=\"tableData\">" + replaceWithValue(tablecolattrobj, strData, chkData) + "</td>");
										}
									}
								}
								/*
								 * Commented for hiding all columns undefined in excel sheet. 
								 */if (!colDefFound && !isColAttrPresent) {
									strData = convertToStr(rowData.get(j-titles));
									align="";
									try {
										if (!Double.isNaN(Double.parseDouble(strData))) {
											align=" align=\"right\"";
										}
									} catch (Exception exc) {
										// Number format error skipped.
									}
									if (align.indexOf("right") > -1) {
									    outputBuff.append("<td class=\"tableData\"><div "+align+">" + strData + "</div></td>");
									} else {
									    outputBuff.append("<td class=\"tableData\">" + strData + "</td>");
									}
									//outputBuff.append("<td " + align + "class=\"tableData\">" + strData + "</td>");
									align="";
								}
								
							}
							outputBuff.append("</tr>\r\n");
							
							
						}
						outputBuff.append("</table>\r\n");
						/** Pagination Related Code Continued Here **/
						   //if i variable used in for loop above value is less thatn datarow count
						if(i<dataRowCount){
							//setting the boolean variable isMultiplePage to true, so that 
							//we can form two links Next and Last Link
							isMultiplePage = true;
						}
						
						if(rowsPerPage!=null && rowsPerPage.length()>0){
							
							outputBuff.append("<br><table width=100%>");
							if(startIndex!=0){
								//if startIndex is not equla to zero adding the link Previous, First
								//outputBuff.append("<td align=left><a href=\""+ ((HttpServletRequest)pageContext.getRequest()).getRequestURI()+"?startIndex="+"0"+"\"" + ">" + "First" +"</a></td>");
								outputBuff.append("<td align=right><a href='#' onClick= 'javascript:document.forms[0].paginationIndex.value=" + "\"" + "0" + "\"" + ";document.forms[0].action.value=\"INIT\";document.forms[0].submit();" + "'"  + ">" + "<img src=\"../images/pagination_first.gif\" border=\"0\" alt=\"First\">" +"</a></td>");
								outputBuff.append("<td align=right><a href='#' onClick= 'javascript:document.forms[0].paginationIndex.value=" + "\"" + (startIndex-pageRowCount) + "\"" + ";document.forms[0].action.value=\"INIT\";document.forms[0].submit();" + "'"  + ">" + "<img src=\"../images/pagination_previous.gif\" border=\"0\" alt=\"Previous\">" +"</a></td>");
							}
							outputBuff.append("<td align=center width=100%>");
							int pageNo = (startIndex/pageRowCount)+1; 
							double tempCnt = new Double(dataRowCount).doubleValue()/new Double(pageRowCount).doubleValue();
							EBWLogger.logDebug(this,"tempCnt pageNo :"+tempCnt);
							int totalPages = Integer.parseInt(String.valueOf((int)Math.ceil(tempCnt)));
							if(totalPages>1)
							    outputBuff.append( "Page "+ pageNo + " of   " + totalPages);
							outputBuff.append("</td>");
							
							if(isMultiplePage){
								//outputBuff.append("<td align=right><a href=\""+ ((HttpServletRequest)pageContext.getRequest()).getRequestURI()+"?startIndex="+ i +"\"" + ">" + "Next" +"</a></td>");
								EBWLogger.logDebug(this,"Request URI " + ((HttpServletRequest)pageContext.getRequest()).getRequestURI());
								//outputBuff.append("<td align=right><a href='" + ((HttpServletRequest)pageContext.getRequest()).getRequestURI()+"?startIndex="+ i + "'" + "onClick='javascript:doucment.forms[0].action.value=" + "\"" + ((HttpServletRequest)pageContext.getRequest()).getRequestURI() + "\"" + "'" + ">" + "Next" +"</a></td>");
								outputBuff.append("<td align=right><a href='#' onClick= 'javascript:document.forms[0].paginationIndex.value=" + "\"" + i + "\"" + ";document.forms[0].action.value=\"INIT\";document.forms[0].submit();" + "'"  + ">" + "<img src=\"../images/pagination_next.gif\" border=\"0\" alt=\"Next\">" +"</a></td>");
								outputBuff.append("<td align=right><a href='#' onClick= 'javascript:document.forms[0].paginationIndex.value=" + "\"" + (dataRowCount-(pageRowCount-((pageRowCount*totalPages)-dataRowCount))) + "\"" + ";document.forms[0].action.value=\"INIT\";document.forms[0].submit();" + "'"  + ">" + "<img src=\"../images/pagination_last.gif\" border=\"0\" alt=\"Last\">" +"</a></td>");
							} 
							outputBuff.append("</table>");
						}
						/** Pagination Realated Code ends here **/
					}else{
					    EBWLogger.logDebug(this,"noDataFound flag value: "+noDataFoundMsg);
						if(noDataFoundMsg.equalsIgnoreCase("true")){
					        outputBuff.append("<tr><td class=\"tableData\" colspan="+cols+"><center> "+getBundleString("NoDataFoundMsg","No Data Found","No Data Found")+" </center></td></tr>\r\n");
					        outputBuff.append("</table>\r\n");
					    }
					}
					}else{
					    EBWLogger.logDebug(this,"noDataFound flag value: "+noDataFoundMsg);
					    if(noDataFoundMsg.equalsIgnoreCase("true")){
					        outputBuff.append("<tr><td class=\"tableData\" ><center> "+getBundleString("NoDataFoundMsg","No Data Found","No Data Found")+" </center></td></tr>\r\n");
					        outputBuff.append("</table>\r\n");
					    }
					}
					
					if (scrollable != null && scrollable.equalsIgnoreCase("true")) {
						outputBuff.append("</DIV>"); 
					}
					//outputBuff.append(addCheckLink(cols, initLowerName));
				}
			} else {
				outputBuff.append("<tr><td class=\"tableData\"><center> Form is Null </center></td></tr>\r\n");
				outputBuff.append("</table>\r\n");
			}
		} catch (NullPointerException nullExp) {
			nullExp.printStackTrace();
			//new EbwException("NullPointer").printEbwException();
		} catch (Exception ex) {
			ex.printStackTrace();
			//new EbwException("AfterAllCatch").printEbwException();
		}
		
		// Must return SKIP_BODY because we are not supporting a body for this 
		// tag.
		return SKIP_BODY;
	}
	
	private String getComboboxOptionData(String fileName, String compName, String strDynValue) {
	    EBWLogger.trace(this,"Entering method String getComboboxOptionData(String fileName, String compName, String strDynValue)");
	    EBWLogger.trace(this,"fileName :"+fileName);
	    EBWLogger.trace(this,"compName :"+compName);
	    EBWLogger.trace(this,"strDynValue :"+strDynValue);
	    
		ComboboxData objCboData = new ComboboxData();
		String strCboOptions="";
		if (isHtmlOutput) {
			strCboOptions = objCboData.getDataForTableCombo(fileName, compName, strDynValue);
		} else {
			strCboOptions = objCboData.getComboboxDataForTable(fileName, strDynValue);
		}
		EBWLogger.trace(this,"Returning from method String getComboboxOptionData(String fileName, String compName, String strDynValue)");
		return strCboOptions;
	}
	
	private String convertToStr(Object obj) {
		return ConvertionUtil.convertToUIString(obj);
		/*String strReturn = "";
		 if (obj!=null) {
		 strReturn = obj.toString();
		 }
		 return strReturn;*/
	}
	
	/**
	 * Adding Check and UnCheck links into the table.
	 * 
	 * @param cols
	 * @param tblName
	 * @return
	 */
	private String addCheckLink(int cols, String tblName) {
	    EBWLogger.trace(this,"Into method private String addCheckLink(int cols, String tblName) ");
	    EBWLogger.trace(this,"cols :"+cols);
	    EBWLogger.trace(this,"tblName :"+tblName);
		StringBuffer strBChk = new StringBuffer();
		if(appRB.getString("Reordering").equalsIgnoreCase("No") || reordering.equalsIgnoreCase("No")) {
			if(getDisplayType() != null && getDisplayType().equals("SelectCheck")) {
				strBChk.append("<table width=\"100%\"><tr><td class=\"tableTitle\">" + convertToStr(tblTitle) + "</td><td class=\"tablechecklink\">");
				strBChk.append("<a href=\"javascript:checkall(document.forms[0]." + tblName + "SelectId)\"><u>Check All</u></a>&nbsp;&nbsp;");
				strBChk.append("<a href=\"javascript:uncheckall(document.forms[0]." + tblName + "SelectId)\"><u>UnCheck All</u></a>");
				strBChk.append("</td></tr></table>\r\n");
			}
			else
			{
			    strBChk.append("<table width=\"100%\"><tr><td class=\"tableTitle\">" + convertToStr(tblTitle) + "</td></tr></table>");
			    
			}
		} else {
			if (getDisplayType() != null) {
				if (getDisplayType().equals(TagLibConstants.TABLE_TYPE_SELECT_CHECK)) {
					strBChk.append("<table width=\"100%\"><tr><td class=\"tableTitle\">" + convertToStr(tblTitle) + "</td><td class=\"tablechecklink\" colspan=\"" + String.valueOf(cols) + "\">");
					strBChk.append("<a href=\"javascript:checkall(document.forms[0]." + tblName + "SelectId)\"><u>Check All</u></a>&nbsp;&nbsp;");
					strBChk.append("<a href=\"javascript:uncheckall(document.forms[0]." + tblName + "SelectId)\"><u>UnCheck All</u></a>&nbsp;&nbsp;</td><td align=\"right\">");
					if(displaySearch=="")
						displaySearch="false";
					strBChk.append("<a href=\"#\" onClick=\"popupwindow=window.open(\'popup.jsp?dis="+displaySearch+"&tablename="+tblName+"\','popupwindow','resizable=no,width=500,height=150,status=yes')\"><img src=\"../images/reorder.gif\" border=\"0\" alt=\"Reorder Columns\"></a>&nbsp;&nbsp;");
					strBChk.append("</td></tr></table>\r\n");
				} else if(getDisplayType().equals(TagLibConstants.TABLE_TYPE_SELECT_RADIO)) {
					if(displaySearch=="")
						displaySearch="false";
					strBChk.append("<table width=\"100%\"><tr><td class=\"tableTitle\">" + convertToStr(tblTitle) + "</td><td class=\"tablechecklink\" colspan=\"" + String.valueOf(cols) + "\">");
					strBChk.append("</td><td align=\"right\"><a href=\"#\" onClick=\"popupwindow=window.open(\'popup.jsp?dis="+displaySearch+"&tablename="+tblName+"\','popupwindow','resizable=no,width=500,height=150,status=yes')\"><img src=\"../images/reorder.gif\" border=\"0\" alt=\"Reorder Columns\"></a>&nbsp;&nbsp;");
					strBChk.append("</td></tr></table>\r\n");
				}
			} else {
				if(displaySearch=="")
					displaySearch="false";
				strBChk.append("<table width=\"100%\"><tr><td class=\"tableTitle\">" + convertToStr(tblTitle) + "</td><td class=\"tablechecklink\" colspan=\"" + String.valueOf(cols) + "\">");
				strBChk.append("</td><td align=\"right\"><a href=\"#\" onClick=\"popupwindow=window.open(\'popup.jsp?dis="+displaySearch+"&tablename="+tblName+"\','popupwindow','resizable=no,width=500,height=150,status=yes')\"><img src=\"../images/reorder.gif\" border=\"0\" alt=\"Reorder Columns\"></a>&nbsp;&nbsp;");
				strBChk.append("</td></tr></table>\r\n");
			}
		}
		return strBChk.toString();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		super.release();
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Returns the border.
	 */
	public String getBorder() {
		return border;
	}
	
	/**
	 * @param border The border to set.
	 */
	public void setBorder(String border) {
		this.border = border;
	}
	
	/**
	 * @return Returns the css.
	 */
	public String getCss() {
		return css;
	}
	
	/**
	 * @param css The css to set.
	 */
	public void setCss(String css) {
		this.css = css;
	}
	
	/**
	 * @return Returns the displayType.
	 */
	public String getDisplayType() {
		return displayType;
	}
	
	/**
	 * @param displayType The displayType to set.
	 */
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	
	/**
	 * @return Returns the doubleBorder.
	 */
	public boolean isDoubleBorder() {
		return doubleBorder;
	}
	
	/**
	 * @param doubleBorder The doubleBorder to set.
	 */
	public void setDoubleBorder(boolean doubleBorder) {
		this.doubleBorder = doubleBorder;
	}
	
	/**
	 * @return Returns the displaySearch.
	 */
	public String getDisplaySearch() {
		return displaySearch;
	}
	
	/**
	 * @param displaySearch The displaySearch to set.
	 */
	public void setDisplaySearch(String displaySearch) {
		this.displaySearch = displaySearch;
	}
	
	/**
	 * @return Returns the formname.
	 */
	public String getFormname() {
		return formname;
	}
	
	/**
	 * @param formname The formname to set.
	 */
	public void setFormname(String formname) {
		this.formname = formname;
	}
	
	/**
	 * @return Returns the isHtmlOutput.
	 */
	public boolean isHtmlOutput() {
		return isHtmlOutput;
	}
	
	/**
	 * @param isHtmlOutput The isHtmlOutput to set.
	 */
	public void setHtmlOutput(boolean isHtmlOutput) {
		this.isHtmlOutput = isHtmlOutput;
	}
	
	/**
	 * @return Returns the outputBuff.
	 */
	public StringBuffer getOutputBuff() {
		return outputBuff;
	}
	
	/**
	 * @param outputBuff The outputBuff to set.
	 */
	public void setOutputBuff(StringBuffer outputBuff) {
		this.outputBuff = outputBuff;
	}
	
	/**
	 * @return Returns the hrefCols.
	 */
	public String getAccesskey() {
		return accesskey;
	}
	
	/**
	 * @param hrefCols The hrefCols to set.
	 */
	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}
	
	private String getTableProperty(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle(
				"com.tcs.ebw.codegen.beans." + name, Locale.getDefault(),
				this.getClass().getClassLoader());
		
		return bundle.getString(key);
	}
	
	private String replaceWithValue(TableColAttrObj colAttrObj, String value, String rowdata) {
		StringBuffer strBReplace = new StringBuffer();
		String strContent;
		if (colAttrObj != null && colAttrObj.getTagContent() != null) {
			if (colAttrObj.getComponentType().equals(CommonConstants.COMPONENT_TYPE_LABEL)) {
				strBReplace.append(colAttrObj.getTagContent());
			} else if (colAttrObj.getComponentType().equals(CommonConstants.COMPONENT_TYPE_HREF)) {
			    strContent = colAttrObj.getTagContent();
				if (value == null) {
					strBReplace.append(strContent.replaceAll("~VARIABLE", ""));	                
				} else {
				    if (strContent.indexOf("\"#\"") == -1) { 
				        strContent = strContent.replaceFirst(".do\"", ".do" + "?tablerowdata=" + rowdata + "\"");
				    }
					strBReplace.append(strContent.replaceAll("~VARIABLE", value));
				}
			} else {
				if (value == null) {
					strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ""));	                
				} else {
					strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", value));	                
				}
			}
		}
		
		/*if (colAttrObj.getComponentType().equals(CommonConstants.COMPONENT_TYPE_HREF)) {
			 if (colAttrObj.getDefaultValue() != null && colAttrObj.getDefaultValue().length() > 0) {
			     strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", value));
			 } else {
			     strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", value));
			 }
		}*/
		
		return strBReplace.toString();
	}
	
	public static void main(String[] args) {
		try {
			//ExcelForm excelform = new ExcelForm("D:\\BalaWorks\\Coding\\CodeGenerator\\Model\\Portftable.xls",
			//       "PortfolioAnalysis");
			ExcelForm excelform = new ExcelForm("D:\\BalaWorks\\Coding\\CodeGenerator\\Model\\Portftable.xls", "PortfolioAnalysis");
			excelform.setTableTitle("Table  Maintenance Title");
			
			TableColAttrObj colAttrObj1 = new TableColAttrObj();
			colAttrObj1.setColName("CurrentValue");
			colAttrObj1.setDisplayLabel("Current Value");
			colAttrObj1.setComponentType("Label");
			
			TableColAttrObj colAttrObj2 = new TableColAttrObj();
			colAttrObj2.setColName("RealisedPL");
			colAttrObj2.setDisplayLabel("Realised PL");
			colAttrObj2.setComponentType("TextField");
			
			/*TableColAttrObj colAttrObj3 = new TableColAttrObj();
			 colAttrObj3.setColName("UnrealisedPL");
			 colAttrObj3.setDisplayLabel("Unrealised PL");
			 colAttrObj3.setComponentType("Label");*/
			
			TableColAttrObj colAttrObj4 = new TableColAttrObj();
			colAttrObj4.setColName("YTD");
			colAttrObj4.setDisplayLabel("YTD");
			colAttrObj4.setComponentType("TextField");
			
			TableColAttrObj colAttrObj5 = new TableColAttrObj();
			colAttrObj5.setColName("months");
			colAttrObj5.setDisplayLabel("Months");
			colAttrObj5.setComponentType("Label");
			
			TableColAttrObj colAttrObj6 = new TableColAttrObj();
			colAttrObj6.setColName("Sinceinception");
			colAttrObj6.setDisplayLabel("Since Inception");
			colAttrObj6.setComponentType("Combobox");
			
			ArrayList colAttrObjs = new ArrayList();
			colAttrObjs.add(colAttrObj1);
			colAttrObjs.add(colAttrObj2);
			//colAttrObjs.add(colAttrObj3);
			colAttrObjs.add(colAttrObj4);
			colAttrObjs.add(colAttrObj5);
			colAttrObjs.add(colAttrObj6);
			
			excelform.setColAttrObjs(colAttrObjs);
			
			EbwNestedTable ebw = new EbwNestedTable(excelform);
			ebw.setHtmlOutput(true);
			ebw.doStartTag();
			ebw.doEndTag();
			//System.out.println(ebw.getOutputBuff().toString());
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	/**
	 * @return Returns the where.
	 */
	public String getWhere() {
		return where;
	}
	/**
	 * @param where The where to set.
	 */
	public void setWhere(String where) {
		this.where = where;
	}
	
	private String getBundleString(String key, String value, String displayLabel) {
		String strMsg = value;
		try {
			if (isHtmlOutput) {
				if (displayLabel!=null && displayLabel.length() > 0 && 
						!displayLabel.equals("~VARIABLE")) {
					strMsg = displayLabel;
				}
			} else {
				if (formRB!=null) {
				    if (key != null) {
				        strMsg = formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + name + "." + key);
				    } else {
				        strMsg = formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + name);
				    }
				}
			}
		} catch (Exception exc) {
		    
		    if(isColAttrPresent && (key.indexOf("NoDataFoundMsg") == -1))
		        strMsg="";
			System.out.println ("Resource bundle value not found for " + key);
			//exc.printStackTrace();
		}
		return strMsg;
	}
	/**
	 * @return Returns the scrollable.
	 */
	public String getScrollable() {
		return scrollable;
	}
	/**
	 * @param scrollable The scrollable to set.
	 */
	public void setScrollable(String scrollable) {
		this.scrollable = scrollable;
	}
	/**
	 * @return Returns the scrollProperties.
	 */
	public String getScrollProperties() {
		return scrollProperties;
	}
	/**
	 * @param scrollProperties The scrollProperties to set.
	 */
	public void setScrollProperties(String scrollProperties) {
		this.scrollProperties = scrollProperties;
	}
	/**
	 * @return Returns the rowsPerPage.
	 */
	public String getRowsPerPage() {
		return rowsPerPage;
	}
	/**
	 * @param rowsPerPage The rowsPerPage to set.
	 */
	public void setRowsPerPage(String rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	
    public String getNoDataFoundMsg() {
        return noDataFoundMsg;
    }
    public void setNoDataFoundMsg(String noDataFoundMsg) {
        this.noDataFoundMsg = noDataFoundMsg;
    }
    /**
     * @return Returns the sorting.
     */
    public String getSorting() {
        return sorting;
    }
    /**
     * @param sorting The sorting to set.
     */
    public void setSorting(String sorting) {
        this.sorting = sorting;
    }
    /**
     * @return Returns the reordering.
     */
    public String getReordering() {
        return reordering;
    }
    /**
     * @param reordering The reordering to set.
     */
    public void setReordering(String reordering) {
        this.reordering = reordering;
    }
	/**
	 * @return Returns the hideColumns.
	 */
	public String getHideColumns() {
		return hideColumns;
	}
	/**
	 * @param hideColumns The hideColumns to set.
	 */
	public void setHideColumns(String hideColumns) {
		this.hideColumns = hideColumns;
	}
}
