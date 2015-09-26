/* This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.taglib;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.tcs.ebw.codegen.beans.ComboboxData;
import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.common.util.StringUtil;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.jaas.auth.Auth;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.taglib.common.CommonConstants;

/**
 * EBWTable taglibrary is used for generate html table. 
 * It gets the data from DataInterface implemented classes. 
 * EBWTable get the form bean from request attribute. It checks 
 * session, if request doesn’t have form bean attribute. After getting 
 * formBean, it uses reflection to find the appropriate DataInterface 
 * object for the table by passing table name. It populates the data 
 * from the DataInterface. 
 *
 * EBWTable has name, border, doubleBorder, displayType, 
 * displaySearch as parameters. DispalyType can be SelectRadio or 
 * SelectCheck. Table has first column as Radio button, or Checkbox 
 * based on the above displayTypes. If the displayType is not given or 
 * empty, then table’s first column will not have Radio or check box 
 * option. DisplaySearch is used for adding search header in the table. 
 * It will have all textfields about the table header if display search 
 * is true. Based on the search condition, it displays filtered data.
 * @author tcs
 * *///total no. of lines 3588  

public class EbwTable extends TagSupport {
	/*Attributes fot this tag handler*/
	private String name = null;
	private String formname = null;
	private String border = null;
	private String displayType = null;
	private boolean doubleBorder = false;
	private String displaySearch = "";
	private String noDataFoundMsg="";
	private String rowsPerPage = "";
	private String rowsPerLine = "";
	private String reordering="";
	private boolean reorderingImg = true;
	private String sorting="";
	private String nestedTable = null;
	private String nestedTableService = null;
	private String tableLevelCSS =null;
	private String colLevelCSS=null;
	private boolean printEnabled = false;
	private String exportEnabled = "false";
	private String xlpdfExportEnabled = null;
	private boolean rowHighLight = false;
	private boolean grouping = false;//Grouping for columns by using Colspan & Rowspan
	private boolean multiLang = false;
	private boolean tableSplit = false;
	private boolean pageDisplay = false;
	private String defRowComp = null;
	private String hiddenCols = null;
	private boolean ajaxCall = false;
	private String divClass = null;//tag attribute to dump the div class name as scrolldiv or autodiv
	private String toolTipCol = null;//tag attribute to dump tooltip on a particular column cell.
	private boolean defRow=false;
	private String editableColumns=null;
	private String hrefCols = null;
	private String headerSortCol = null;
	private String rowLevelCSS = null;//a tag attribute to check whether rowlevelcss there or not
	private String mouseOverEvent=null;
	private String defaultSortedCol = null; // a tagattribute to specify the column number and the order(ASC/DESC) which has been sorted by default by the service
	private String conditionRowCss = null;

	private DataInterface formObj = null;
	private ArrayList data = null;
	private Object attrObj = null;
	private boolean isHtmlOutput = false;
	private StringBuffer outputBuff = null;
	private StringBuilder outputtbl = null;
	private String accesskey = null;
	private String where = "";
	private String scrollable = "";
	private String scrollProperties = "";
	private String tblTitle="";
	private boolean isColAttrPresent=false;
	private boolean isNestedTable = false;
	LinkedHashMap colTooltipCssMap = new LinkedHashMap();
	private ResourceBundle ebwTableRb = null;
	private boolean rowIdVal = false;
	private  String isAjax = "no";
	String reorderingColmns = null;
	private String restartKeyFromFormbean = null;
	private ArrayList rowData = null;
	private boolean isEditColsPresent=false;
	private boolean storeHiddenValue=false;//To Check if the row value has to be stored in a Hidden Variable
	private String actionEbwtable=null;
	private String stateEbwtable=null;
	private String visibleColumns = null;
	private String availableColumns = null;
	public String initLowerName=null;
	public HttpSession sessionObj;
	public ResourceBundle appRB = null;
	public ResourceBundle formRB = null;
	public String displayFlagForShuffleColAttr = null;
	public int totalRowCount = 1;
	String totalRow=null;

	private final String DOUBLE_BORDER_TABLE_WITHOUTCSS = "<table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" border=\"1\"><tr><td>\r\n";
	private final String DOUBLE_BORDER_TABLE_WITHCSS = "<table cellspacing=\"0\" cellpadding=\"0\" class=\""+getTableLevelCSS()+" border=\"1\"><tr><td>\r\n";
	private final String SCROLLABLE_TABLE_WITHOUTCSS="<table width=\"100%\">";
	private final String SCROLLABLE_TABLE_WITHCSS="<table  class=\""+getTableLevelCSS()+"\">";
	private final String CLOSE_TABLE_WITHNEWLINE="</table>\r\n";
	private final String CLOSE_DIV="</td></tr></table></DIV>";
	//private final String NO_DATA_FOUND_ROW_WITHNEWLINE="<tr><td  colspan=\"25\" class=\"tableData\" ><center> "+getBundleString("NoDataFoundMsg","No Data Found","No Data Found")+" </center></td></tr>\r\n";
	private final String CLOSE_TR_WITHNEWLINE="</tr>\r\n";
	private final String DEFAULT_NESTED_INNERTABLEHEADER_CSS=" class=\"InnerTableHeader\"";
	private final String SCROLL_PROPERTIES="WIDTH: 100%; HEIGHT: 125px";	

	/** This Constructor is for testing from main method only.This constructor will not be called by Taglib at runtime from JSP*/
	public EbwTable(Object formObj) {		
		attrObj = formObj;
		name = ((DataInterface) formObj).getTableName();
		outputBuff = new StringBuffer();
	}
	public EbwTable() {}
	public void loadResourceBundles() {
		outputBuff = null;
		try {
			/* Resource bundle for getting values from EbwTableResourceBundle */
			ebwTableRb = ResourceBundle.getBundle(TagLibConstants.RESOURCE_BUNDLE_FILE_EBWTABLE);
			/* Loading the Application's Property file that contains the location of the property file that contains the displaynames */
			appRB = ResourceBundle.getBundle("ApplicationConfig");

			/* Loading up the properties file  that contains the fieldId and Displaynames as key, value respectively */   
			if (!isHtmlOutput){
				formRB = PropertyFileReader.getBundle(appRB.getString("message-resources"));
			}
			tblTitle =formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + name);
		} catch (Exception exc) {
			System.out.println ("Resource Bundle Not found...");
		}
	}

	/* doEndTag() used to nullify the classlevel variables used. */
	public int doEndTag() throws JspException {
		try {
			if (isDoubleBorder()) 
				outputBuff.append("</td></tr></table>\r\n");
			if (!isHtmlOutput) {
				JspWriter out = pageContext.getOut();
				out.println(outputBuff.toString());
			}
		} catch (Exception ex) {
			new EbwException(this, ex).printEbwException();
		}
		tableLevelCSS =null;
		colLevelCSS=null;
		rowLevelCSS=null;
		return SKIP_BODY;
	}

	/*  Tag handler starts here. */
	public int doStartTag() throws JspException {
		ReorderTableHelper reorderTableHelperObj = new ReorderTableHelper();
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into doStartTag of EBWTableTag");
		System.out.println("EBWTABLE-Start");
		LinkedHashMap dataValue = null;
		int titles = 0;//This variable is the count of titles in HashMap like tabletitle and searchtitle etc
		boolean colDefFound = false;
		String  pgnIndexStr = null;	  	
		String strData = "";
		String chkData = "";
		String align="align=\"left\"";
		initLowerName = StringUtil.initLower(name);
		if(EBWLogger.isInfoEnabled()) 
			EBWLogger.logDebug(this, "InitLowerName "+initLowerName);
		String columnValuesForFilter="";
		boolean isDifferentScreen = false;
		boolean defRowBool = isDefRow();
		String numericalDataType = "integer,float,number,numeric,double,BigDecimal,''";
		String otherDataType = "varchar,date,Date,DateTime,Timestamp,timestamp,datetime,TimeStamp";
		String strDataTableLevelCSS=" class=\"tableheader\" ";
		ArrayList firstRow=new ArrayList();

		if(getTableLevelCSS()!=null){
			if(EBWLogger.isInfoEnabled())
				EBWLogger.logDebug(this, "Setting TableLevelCSS class as given by user");
			strDataTableLevelCSS = " class=\""+getTableLevelCSS()+"\"";
		}else
			EBWLogger.logDebug(this, "TableLevelCSS class is not given by user");

		loadResourceBundles();	
		final String NO_DATA_FOUND_ROW_WITHNEWLINE="<tr><td  colspan=\"25\" class=\"tableData\" ><center> "+getBundleString("NoDataFoundMsg","No Data Found","No Data Found")+" </center></td></tr>\r\n";
		try {/**   try block which covers the entire doStartTag().*/
			if (outputBuff == null) 
				outputBuff = new StringBuffer();
			if(isAjax !=null && isAjax.length()>0 && isAjax.equalsIgnoreCase("yes")){
				ajaxCall = true;
			}else{
				//attrObj object may come from the constructor for html generation. (attrObj == null) 
				if (formname != null) 
					attrObj = pageContext.getRequest().getAttribute(formname);//getting from the request
				if ((attrObj == null) && (formname != null)) 
					attrObj = pageContext.getSession().getAttribute(formname);//getting from the session
			}
			/** the below class level variables are used for serverside export,pgn,reorder */
			System.out.println("----isHtmlOutput--- "+isHtmlOutput);
			if (!isHtmlOutput) {
				pgnIndexStr = ((EbwForm)attrObj).getPaginationIndex();
				stateEbwtable = ((EbwForm)attrObj).getState();
				actionEbwtable = ((EbwForm)attrObj).getAction();
				restartKeyFromFormbean = ((EbwForm)attrObj).getRestartKey();
				EBWLogger.logDebug(this, "In EbwTable action is :"+actionEbwtable+"  and state is : "+stateEbwtable);			
				EBWLogger.logDebug(this, "header restartKey From Formbean :"+restartKeyFromFormbean);
			}
			if(!isHtmlOutput){
				if(((EbwForm)attrObj).getRowsPerPage()!=null && rowsPerPage!=null && rowsPerPage.length()>0)
					rowsPerPage = ((EbwForm)attrObj).getRowsPerPage();//overwriting rowsperpage with the rowsperpage value obtained from the DB.
				/** Getting the previous action from the session if the differentscreen is true;
				 * if input and output screens are different then isdiffscrenn = true; */
				if(!ajaxCall)
					sessionObj = ((HttpServletRequest)(pageContext.getRequest())).getSession();
			}

			HashMap userObject = new HashMap();
			String prev_Action = null;String prevUrl = null;String prevActn = null;	String prevState = null;
			if(!isHtmlOutput){
				userObject = (HashMap)sessionObj.getAttribute("USER_OBJ");
				if( userObject!=null && userObject.get("isDiffScreen")!=null && ((String)userObject.get("isDiffScreen")).equalsIgnoreCase("true")){
					if(sessionObj!=null && ((sessionObj.getAttribute(sessionObj.getId()+"_PrevActn"))!=null)){
						prev_Action =(String)sessionObj.getAttribute(sessionObj.getId()+"_PrevActn");
						isDifferentScreen = true;
						String[] prevActnArray = prev_Action.split(":");
						if(prevActnArray!=null && prevActnArray.length>=3){
							prevUrl = prevActnArray[0]+".do";
							prevActn = prevActnArray[1];	
							prevState = prevActnArray[2];
						}	
					}
				}
			}
			EBWLogger.logDebug(this,"isDifferentScreen :"+isDifferentScreen);			
			/** do Introspection and get the invoking method to get DataInterface type data from the UI framework form.*/
			Class formClass = Class.forName(attrObj.getClass().getName());
			EBWLogger.trace(this,"Into Non HTML output generation of EBWTableTag");
			if (!isHtmlOutput && (isAjax==null || (isAjax!=null && !isAjax.equalsIgnoreCase("yes")))) {
				EBWLogger.logDebug(this,"formClass :"+formClass+" -- Name -- "+name+" -- attrObj -- "+attrObj);
				formObj = (DataInterface) formClass.getMethod("get" + StringUtil.initCaps(name) +"Collection", null).invoke(attrObj, null);
				EBWLogger.logDebug(this,"formObj :"+formObj);
				/*if(((HttpServletRequest)pageContext.getRequest()).getSession().getAttribute(formname)!=null && rowsPerPage.length()>0 && (formObj.getData()==null || ((ArrayList)formObj.getData()).isEmpty())){ //To get formbean from session 
					if(EBWLogger.isInfoEnabled())
						EBWLogger.logDebug(this, "Loading form in EBWTable:"+formname);
					formClass = Class.forName( ((HttpServletRequest)pageContext.getRequest()).getSession().getAttribute(formname).getClass().getName());
					formObj = (DataInterface) formClass.getMethod("get" + StringUtil.initCaps(name) +"Collection", null).invoke((EbwForm)((HttpServletRequest)(pageContext.getRequest())).getSession().getAttribute(formname), null);
					EBWLogger.logDebug(this,"formClass :"+formClass+" --formObj-- "+formObj);
				}*/
			} else {
				if(ajaxCall)
					formObj = (DataInterface) formClass.getMethod("get" + StringUtil.initCaps(name) +"Collection",null).invoke(attrObj, null);
				else{
					formObj = (DataInterface) formClass.getMethod("getCollection",null).invoke(attrObj, null);
					if(formObj.getData()==null || ((ArrayList)formObj.getData()).isEmpty()){ //To get formbean from session
						formClass = Class.forName(pageContext.getSession().getAttribute(formname).getClass().getName());
						formObj = (DataInterface) formClass.getMethod("get" + StringUtil.initCaps(name) +"Collection", null).invoke(attrObj, null);
					}
				}				
			}
			/**  1.  These variables are used to reset the value After doing reordering reverting the form back to the original state
			 *  2.   tabledata values  and header values taken before performing reordering and hide column
					 used for manipulating the value(chkdata) for the radio/check box. */	
			ArrayList headerEbwStart = formObj.getRowHeader();
			ArrayList dataEbwStart = (ArrayList)formObj.getData();
			ArrayList colAttrStart = formObj.getColAttrObjs();
			EBWLogger.trace(this,"FormObject in EBWTable Header1 " + headerEbwStart);
			EBWLogger.trace(this,"FormObject in EBWTable Data1   " + dataEbwStart);			
			String[] selectId = null;//getSelectId from form bean
			if(!isHtmlOutput){
				if(getDisplayType()!=null && getDisplayType().length()>0){//Stores the selected value radio/check in the form
					selectId = (String[])formClass.getMethod("get" + StringUtil.initCaps(name) +"SelectId", null).invoke(attrObj, null);
				}		
			}
			formObj.setWhereCondition(where);
			if (scrollable != null && scrollable.equalsIgnoreCase("true")) 
				doubleBorder=true;
			if(getDisplayType()==null)//To Check if the row Value has to be put in a Hidden Variable
				storeHiddenValue=true;
			if (formObj != null) {
				EBWLogger.trace(this,"Form object is not null So starting to render Table");
				/** This flag is true if first col has Checkbox/RadSelect */
				displayFlagForShuffleColAttr = null;
				if(getDisplayType()!=null && getDisplayType().length()>0 ) 
					displayFlagForShuffleColAttr = "yes";
				ArrayList colattrobj = ((DataInterface) formObj).getColAttrObjs();
				if(colattrobj!=null && colattrobj.size()>0)
					isColAttrPresent=true;
				if (formObj.getRowHeader() == null) {
					EBWLogger.trace(this,"Table Header is NULL");
				} else {
					if(isHtmlOutput ){ //changed for Html table preview header
						if(headerEbwStart !=null && headerEbwStart.size()>0 ){  
							ArrayList arr = new ArrayList();
							Object[] str = ((((LinkedHashMap)headerEbwStart.get(0)).keySet()).toArray());
							for(int j=2;j<str.length;j++)
								arr.add(str[j]);
							dataEbwStart.add(0,arr);
						}
					}
					data = (ArrayList)formObj.getData();

					if(pageDisplay){ 
						if(data!=null && data.size()>0){
							if (((EbwForm)attrObj).getTotalRows()!=null && ((EbwForm)attrObj).getTotalRows().length()>0){
								totalRow = ((EbwForm)attrObj).getTotalRows();
							}else{
								firstRow = (ArrayList)data.get(0);
								totalRow = firstRow.get(firstRow.size()-1).toString().trim();
								((EbwForm)attrObj).setTotalRows(totalRow);
							}
						}else{
							totalRow = "1";
						}
						try{
							totalRowCount = Integer.parseInt(totalRow);
						}catch (NumberFormatException e) {
							System.out.println("Could not convert from String to Integer..");
						}
					}

					LinkedHashMap dataValueBeforeReorder = (LinkedHashMap) formObj.getRowHeader().get(0);//tableheader values taken before performing reordering 
					if(data.size()>0 || (data.size()==0 && noDataFoundMsg.equalsIgnoreCase("true"))){
						dataValue = (LinkedHashMap) formObj.getRowHeader().get(0);
						/*****************************code related to reordering starts here******************/
						String colNamesFromAttrbeforeReorde = null;
						if(dataValue!=null){
							//columns in the colattr before reorder							
							colNamesFromAttrbeforeReorde = ReorderTableHelper.getColNamesFromColAttr(colAttrStart,getHiddenCols(),dataValue);
							if(!isHtmlOutput){
								try{
									String reOrderColsFrmSession = null;

									Set set=((Subject)sessionObj.getAttribute(Auth.SUBJECT_SESSION_KEY)).getPrincipals();
									Iterator it=set.iterator();
									UserPrincipal user=(UserPrincipal)it.next();
									String usrUserId=user.getUsruserid().toUpperCase();
									//String usrGrppId=user.getUsrgrpid();								
									if(!(appRB.getString("Reordering").equalsIgnoreCase("No")) || !(reordering.equalsIgnoreCase("No"))){								
										reOrderColsFrmSession = reorderTableHelperObj.getReorderingColsFromSession(formObj,userObject,dataValue,sessionObj,formRB,formname,name,displayType,usrUserId,stateEbwtable,reordering,appRB,multiLang);									
									}
									if(reOrderColsFrmSession!=null && reOrderColsFrmSession.length()>0){
										String reorderCols = reOrderColsFrmSession;
										if(reorderCols.indexOf(":")>-1)
											reorderCols = reorderCols.substring(reorderCols.indexOf(":")+":".length(), reorderCols.length());
										int[] reorderColPos =  ReorderTableHelper.getReorderColPos(reorderCols,dataValue,formRB,formname,name);
										if(headerSortCol!=null && headerSortCol.length()>0)
											headerSortCol = ReorderTableHelper.getShuffledHeaderSort(reorderColPos,headerSortCol);
										if(defaultSortedCol!=null && defaultSortedCol.length()>0)
											defaultSortedCol = ReorderTableHelper.getShuffledDefaultHeaderSort(reorderColPos,defaultSortedCol);
										if(isDefRow() && editableColumns!=null && editableColumns.length()>0)
											editableColumns = ReorderTableHelper.getShuffledEditableCols(reorderColPos,editableColumns);
										if(isDefRow() && getDefRowComp()!=null && getDefRowComp().length()>0)
											defRowComp = ReorderTableHelper.getShuffledDefRowComp(reorderColPos,defRowComp);
										if(getColLevelCSS() !=null && getColLevelCSS().length()>0)
											colLevelCSS = ReorderTableHelper.getShuffledColLevelCSS(reorderColPos,colLevelCSS);
										reorderTableHelperObj.setReorderInFormbean(reorderCols,dataValue,formObj,colattrobj,formRB,formname,name,displayFlagForShuffleColAttr);
										data=(ArrayList)formObj.getData();
										colattrobj=formObj.getColAttrObjs();
										dataValue = (LinkedHashMap) formObj.getRowHeader().get(0);
										EBWLogger.logDebug(this,"Final dataValue:"+dataValue);
										EBWLogger.logDebug(this,"Final data:"+data);
									}
								}catch(Exception e){
									System.out.println("This is the undefined: "+e);
								}
							}
						}
						/**This piece of code is used to calculate visible cols and available cols being passed to the popup.jsp and popupdiff.jsp. for this we need cols after reorder.*/
						ArrayList colInColAtrAfterReorder = ((DataInterface) formObj).getColAttrObjs();
						String colNamesAfterReorderInColAttr = ReorderTableHelper.getColNamesFromColAttrAfter(colInColAtrAfterReorder);
						visibleColumns = ReorderTableHelper.getVisibleCols(colNamesAfterReorderInColAttr,formRB,formname,name,displayFlagForShuffleColAttr);
						availableColumns =ReorderTableHelper.getAvailableCols(colNamesFromAttrbeforeReorde,colNamesAfterReorderInColAttr,formRB,formname,name,displayFlagForShuffleColAttr);
						/***************************Code related to reordering end here*************/

						Iterator iterator = dataValue.keySet().iterator();
						Object obj = null;
						int cols = (dataValue.size() - titles);
						/**the below function call dumps the html content for the table tiltle bar above the table*/
						if(!isHtmlOutput){
							reorderingColmns=(String)(((HashMap)(sessionObj.getAttribute("USER_OBJ"))).get(sessionObj.getId()+"_"+name));
							if( userObject!=null && userObject.get("isDiffScreen")!=null &&  ((String)userObject.get("isDiffScreen")).equalsIgnoreCase("true"))
								outputBuff.append(addCheckLinkPrev(cols, initLowerName, pgnIndexStr,formObj.getDataRowCount(),prev_Action,restartKeyFromFormbean,actionEbwtable+":"+stateEbwtable,reorderingColmns));
							else
								outputBuff.append(addCheckLinkNotPrev(cols, initLowerName, pgnIndexStr,formObj.getDataRowCount(),restartKeyFromFormbean,actionEbwtable,reorderingColmns));

							/** added div after table title.this feature is for RJ */
							if(divClass !=null && divClass.length()>0)
								outputBuff.append("<DIV class=\""+divClass+"\"><table  class=\"layoutMaxDivTable\"><tr><td>");
							else
								outputBuff.append("<DIV class=\"autodiv\"><table  class=\"layoutMaxTable\"><tr><td>");
						}
						if (isDoubleBorder()) {
							if(getTableLevelCSS()!=null)
								outputBuff.append(DOUBLE_BORDER_TABLE_WITHCSS);
							else
								outputBuff.append(DOUBLE_BORDER_TABLE_WITHOUTCSS);
						}
						if (scrollable != null && scrollable.equalsIgnoreCase("true")) {
							if(getTableLevelCSS()!=null)
								outputBuff.append(SCROLLABLE_TABLE_WITHCSS);
							else
								outputBuff.append(SCROLLABLE_TABLE_WITHOUTCSS);
						} else {
							if(EBWLogger.isDebugEnabled())
								EBWLogger.logDebug(this,"Starting TableHeader generation :");
							if (border != null) //Render an outer table for border if border attribute is given.
								outputBuff.append("<table name=\""+initLowerName+"\" id=\""+initLowerName+"\" cols="+(displayType==null ? (cols+1) : cols)+" border=\"" + border + "\" "+strDataTableLevelCSS+">\r\n");
							else
								outputBuff.append("<table name=\""+initLowerName+"\"  id=\""+initLowerName+"\" cols="+(displayType==null ? cols : cols+1)+" "+strDataTableLevelCSS+">\r\n");
						}

						tblTitle = getBundleString(name, (String) dataValue.get("tabletitle"), "");
						if ((dataValue.containsKey("tabletitle") || dataValue.containsKey("searchtitle"))) {// TableTitle and Search Title;
							titles = 2; 
							cols = (dataValue.size() - titles);
						}						

						// *** Creation of Search Panel Starts Here
						if ((displaySearch != null) && displaySearch.equalsIgnoreCase("true") && (displaySearch.length() > 0)) {
							createSearchPanel(obj, initLowerName,iterator);
						} else {
							outputBuff.append("<tr class=\"scrollTH\">");
						}// *** End of Search Panel Creation
						int dataIndex=0; //dataIndex is used in getting value from colattributes for each column. Used in below codes.
						if (getDisplayType() != null && getDisplayType().length()>0) {//Decide and add a column header for checkbox/radio column
							String strTH="";
							boolean dispCheckBox = false;
							TableColAttrObj tablecolattrobjH1=null;
							if (colattrobj!=null && colattrobj.size() > 0) {
								TableColAttrObj tablecolattrobj = (TableColAttrObj) colattrobj.get(0);
								tablecolattrobjH1 = (TableColAttrObj) colattrobj.get(1);//added for ColSpan

								if (tablecolattrobj!=null && tablecolattrobj.getColName().equalsIgnoreCase(name + "SelectId")) {									
									if(!isHtmlOutput){
										strTH=getBundleString(name + "SelectId", "", "");
									}else{
										strTH=tablecolattrobj.getDisplayLabel();
									}
								}			
								if(getDisplayType().equals(TagLibConstants.TABLE_TYPE_SELECT_CHECK) )
									dispCheckBox=true;
							}
							if(!dispCheckBox && (formObj.getData() !=null && ((ArrayList)formObj.getData()).size()>0 ) && (getDisplayType().equals(TagLibConstants.TABLE_TYPE_SELECT_RADIO_SHOW))){
								if(grouping)
									outputBuff.append("<th class=\"colCSSelect\" rowspan=\"2\"><b>"+strTH+"</th>");
								else
									outputBuff.append("<th class=\"colCSSelect\" ><b>"+strTH+"</th>");
							}else if(!dispCheckBox && (formObj.getData() !=null && ((ArrayList)formObj.getData()).size()>0 )){
								outputBuff.append("<th class=\"hidden\"></th>"); // REMOVE_RAD_SELECT_OBJ
							}else if (tablecolattrobjH1!=null && tablecolattrobjH1.getColSpan()!=null)//added for ColSpan
								outputBuff.append("<th width=\"5%\" rowspan=\"2\" class=\"scrollTH\"><b>");
							else if(getDisplayType().equals(TagLibConstants.TABLE_TYPE_SELECT_CHECK)) {
								if(grouping)
									outputBuff.append("<th class=\"colCSSelect\" rowspan=\"2\" ><b>");
								else
									outputBuff.append("<th class=\"colCSSelect\"><b>");
							}
							if(dispCheckBox && isRowHighLight())																											
								outputBuff.append("<input type=\"checkbox\" name=\"CBHeader\" id = \"CBHeader\" onClick=\"javascript:checkIt(this,document.forms[0]." + initLowerName + "SelectId,"+isDefRow()+","+initLowerName+",'"+initLowerName+"_');\">"+strTH+"</th>");
							else if(dispCheckBox && !isRowHighLight())
								outputBuff.append("<input type=\"checkbox\" name=\"CBHeader\" id = \"CBHeader\" onClick=\"javascript:checkIt(this,document.forms[0]." + initLowerName + "SelectId);\">"+strTH+"</th>");
							dataIndex=1;
						}					

						/***Started printing  Table Header  ***************************************/
						iterator = dataValue.keySet().iterator();
						obj = null;
						String strColHd = "";
						int colIndex =-1;
						TableColAttrObj tablecolattrobj2=null;
						/**code to get the access key*/
						String arrKeys[] = null; 
						if (getAccesskey()!=null) 
							arrKeys = getAccesskey().split(",");							
						ArrayList keyPos = new ArrayList();
						//to get the access keys positions before performing reordering and hide columns
						int countOfHeaderBeforeReorder = -1;
						if(arrKeys!=null && arrKeys.length>0){
							Iterator itrForHeaderBeforeReorder = dataValueBeforeReorder.keySet().iterator();
							Object headerObj  = null;
							while (itrForHeaderBeforeReorder.hasNext()) {
								headerObj = itrForHeaderBeforeReorder.next();
								countOfHeaderBeforeReorder++;								
								if (headerObj!=null) { 
									String strColHdbeforeReorder = headerObj.toString();
									for (int i=0; i < arrKeys.length; i++) {
										if (arrKeys[i].equals(strColHdbeforeReorder)) 
											keyPos.add(new Integer(countOfHeaderBeforeReorder));
									}
								}
							}
						}/**end of code for getting access key positions*/
						String columnWidth="";
						ArrayList colHeaderAL = new ArrayList();
						boolean flag=true;//added for ColSpan
						int colSpnCt=0;
						while (iterator.hasNext()) {
							obj = iterator.next();
							colIndex++;	
							strColHd=(obj!=null)?obj.toString():"";
							if((colattrobj!=null && colattrobj.size()>0) && (dataIndex>colattrobj.size()-1)) {

								//dataIndex=0;

							}
							else if ((colattrobj!=null && colattrobj.size()>0) && !(dataIndex>colattrobj.size()-1)){
								tablecolattrobj2=(TableColAttrObj)colattrobj.get(dataIndex);

							}
							if (!strColHd.equalsIgnoreCase("tabletitle") && !strColHd.equals("searchtitle")) {
								if (strColHd.length() > 0) {									
									if(tablecolattrobj2 != null && (appRB.getString("Sorting").equalsIgnoreCase("No") || (sorting!=null && sorting.length()>0 && sorting.equalsIgnoreCase("No")))) {										
										String colHeading = new String();
										String headerRowSpan = new String();
										String headerColSpan = new String();
										String spanedHeading = new String();

										if(!isHtmlOutput){
											colHeading = getBundleString(strColHd, (String) dataValue.get(strColHd), tablecolattrobj2.getDisplayLabel());											
										}else{
											colHeading = tablecolattrobj2.getDisplayLabel();
											headerRowSpan =tablecolattrobj2.getRowSpan();
											headerColSpan =tablecolattrobj2.getColSpan();										    
										}
										//String colHeading = getBundleString(strColHd, (String) dataValue.get(strColHd), tablecolattrobj2.getDisplayLabel());
										if(colHeading.trim().length()>0){
											if(dataIndex<colattrobj.size()){
												spanedHeading = tablecolattrobj2.getSpanDisplayLabel();
												headerColSpan =tablecolattrobj2.getColSpan();
												/*if(!(headerRowSpan.equalsIgnoreCase("") || headerRowSpan == null)){
											    outputBuff.append("<th rowspan=\""+headerRowSpan+"\">" + colHeading + "</th>");
										    	dataIndex++;
										   }else */
												if(grouping && headerColSpan != null  && spanedHeading!=null){											   
													colSpnCt= Integer.parseInt(headerColSpan);
													outputBuff.append("<th colspan=\""+headerColSpan+"\">" + spanedHeading + "</th>");											   
													colHeaderAL.add(colHeading);											   
													flag=false;
													colSpnCt--;
													dataIndex++;  
												}else{		
													if(flag) {
														if(!grouping)
															outputBuff.append("<th>" + colHeading + "</th>");
														else
															outputBuff.append("<th rowspan=\"2\">" + colHeading + "</th>");
													}else {
														colHeaderAL.add(colHeading);
														colSpnCt--;
													}
													dataIndex++;
												}
											} if(colHeaderAL!=null  && colHeaderAL.size()>1 && dataIndex > (colattrobj.size()-1)){											   
												if(colHeaderAL.size()>1){												   
													outputBuff.append("</tr><tr class=\"scrollTH\">");
													for(int x=0; x < colHeaderAL.size(); x++){
														outputBuff.append("<th>" + colHeaderAL.get(x) + "</th>");
													}
													outputBuff.append("</tr>");
													colHeaderAL.clear();
												}
											}
										} 
										if(colSpnCt==0) {
											flag=true;
										}
									} else if(!isHtmlOutput){
										//String sortImgSrc = getHeaderSortImgStr("ascShade","descShade");
										String sortImgSrc = null;
										String sortType = null;
										if(data!=null && data.size()>0)
											sortImgSrc = getHeaderSortImgStr(sortType);
										if(isDefaultSortCol(colIndex)&& (restartKeyFromFormbean == null || restartKeyFromFormbean.length()==0 || restartKeyFromFormbean.equalsIgnoreCase("null")))
											sortImgSrc = getDefaultSortImgStr(sortType,colIndex);

										if(restartKeyFromFormbean != null && restartKeyFromFormbean.length()>0 && !restartKeyFromFormbean.equalsIgnoreCase("null")){
											if(restartKeyFromFormbean.substring(0,restartKeyFromFormbean.indexOf(":")).equalsIgnoreCase(strColHd)){
												sortType = restartKeyFromFormbean.substring(restartKeyFromFormbean.lastIndexOf(":")+1,restartKeyFromFormbean.length());												
												if(data!=null && data.size()>0 && sortType!=null && sortType.length()>0)
													sortImgSrc=sortType.equalsIgnoreCase("asc")?getHeaderSortImgStr("asc"):getHeaderSortImgStr("desc");
											}
										}										
										String headerSortKey = strColHd+":"+(tablecolattrobj2.getDataType()).toLowerCase()+":ASC";
										String javaScriptUrlSubmit = "getSortType(document.forms[0],'"+sortType+"');pgnUrlSubmit(document.forms[0],'"+prevUrl+"','"+prevActn+"','"+prevState+"','"+headerSortKey+"','','list','');";
										String javaScriptSubmit = "getSortType(document.forms[0],'"+sortType+"');pgnSubmit(document.forms[0],'"+actionEbwtable+"','','"+headerSortKey+"','','list','"+totalRow+"');";
										//String startTbl = "<table><tr><td class=\"headercss\">";
										//String closeTdTrTbl = "</td></tr></table>";
										String startTbl = " ";
										String closeTdTrTbl = " ";
										String pgnCall = isDifferentScreen?javaScriptUrlSubmit:javaScriptSubmit;
										if(displayType==null) {
											if(colattrobj!=null && colattrobj.size()>0) {
												if(dataIndex>colattrobj.size()-1) 
													dataIndex=0;
												else
													tablecolattrobj2=(TableColAttrObj)colattrobj.get(dataIndex);

												if((tablecolattrobj2.getColName()).equals(strColHd)) {
													if(isDefaultSortCol(colIndex) && data!=null && data.size()>0 && (restartKeyFromFormbean == null || restartKeyFromFormbean.length()==0 || restartKeyFromFormbean.equalsIgnoreCase("null")))
														outputBuff.append("<th "+columnWidth + ">"+startTbl+"<a  onclick=\"javascript:"+pgnCall+"\">"+getBundleString(strColHd, (String) dataValue.get(strColHd), tablecolattrobj2.getDisplayLabel())+sortImgSrc+"</a>"+closeTdTrTbl+"</th>");
													else  if(isHeaderSortCol(colIndex) && data!=null && data.size()>0)
														outputBuff.append("<th "+columnWidth + ">"+startTbl+"<a  onclick=\"javascript:"+pgnCall+"\">"+getBundleString(strColHd, (String) dataValue.get(strColHd), tablecolattrobj2.getDisplayLabel())+sortImgSrc+"</a>"+closeTdTrTbl+"</th>");
													else
														outputBuff.append("<th " + columnWidth + ">"+ getBundleString(strColHd, (String) dataValue.get(strColHd), tablecolattrobj2.getDisplayLabel()) +"</th>");
													dataIndex++;
												}
											}else{											    
												if(EBWLogger.isInfoEnabled()) 
													EBWLogger.logDebug(this, "ColAttribute Not Null or size 0");
												if(isDefaultSortCol(colIndex) && data!=null && data.size()>0 && (restartKeyFromFormbean == null || restartKeyFromFormbean.length()==0 || restartKeyFromFormbean.equalsIgnoreCase("null")))
													outputBuff.append("<th "+ columnWidth + ">"+startTbl+"<a  onclick=\"javascript:"+pgnCall+"\">"+getBundleString(strColHd, (String) dataValue.get(strColHd), "")+sortImgSrc+ "</a>"+closeTdTrTbl+"</th>");
												else  if(isHeaderSortCol(colIndex) && data!=null && data.size()>0)											    	
													outputBuff.append("<th "+ columnWidth + ">"+startTbl+"<a  onclick=\"javascript:"+pgnCall+"\">"+getBundleString(strColHd, (String) dataValue.get(strColHd), "")+sortImgSrc+ "</a>"+closeTdTrTbl+"</th>");
												else
													outputBuff.append("<th " + columnWidth + ">"+ getBundleString(strColHd, (String) dataValue.get(strColHd), "") +"</th>");											}
										} else {
											if(EBWLogger.isInfoEnabled())
												EBWLogger.logDebug(this, "Display Type is not null");
											if(colattrobj!=null && colattrobj.size()>0){
												if(dataIndex>(colattrobj.size()-1)) 
													dataIndex=0;
												else 
													tablecolattrobj2=(TableColAttrObj)colattrobj.get(dataIndex);
												/**This is to print columns based on datatype. If columnAttribute is present then get the datatype and print table cell based on that dataype.
												 * If colattribute columnname does not match dbcolumnname, then print cell as varchar.*/
												if((tablecolattrobj2.getColName()).equals(strColHd)) {
													if(isDefaultSortCol(colIndex) && data!=null && data.size()>0 && (restartKeyFromFormbean == null || restartKeyFromFormbean.length()==0 || restartKeyFromFormbean.equalsIgnoreCase("null")))
														outputBuff.append("<th >"+startTbl+"<a  onclick=\"javascript:"+pgnCall+"\">"+ getBundleString(strColHd, (String) dataValue.get(strColHd), "") +sortImgSrc+ "</a>"+closeTdTrTbl+"</th>");
													else if(isHeaderSortCol(colIndex) && data!=null && data.size()>0)														
														outputBuff.append("<th >"+startTbl+"<a  onclick=\"javascript:"+pgnCall+"\">"+ getBundleString(strColHd, (String) dataValue.get(strColHd), "") +sortImgSrc+ "</a>"+closeTdTrTbl+"</th>");
													else
														outputBuff.append("<th "+ ">"+ getBundleString(strColHd, (String) dataValue.get(strColHd), "") +"</th>");
													dataIndex++;
												} else {
													if(EBWLogger.isInfoEnabled()) 
														EBWLogger.logDebug(this, "Attribute ColumnName Not equals strColHd, ColumnName:"+tablecolattrobj2.getColName()+",strColHd :"+strColHd);
												}             		                  		
											} else {
												if(isDefaultSortCol(colIndex) && data!=null && data.size()>0 && (restartKeyFromFormbean == null || restartKeyFromFormbean.length()==0 || restartKeyFromFormbean.equalsIgnoreCase("null")))
													outputBuff.append("<th " + "><center>"+startTbl+"<a  onclick=\"javascript:"+pgnCall+"\">"+getBundleString(strColHd, (String) dataValue.get(strColHd), "") +sortImgSrc+ "</a>"+closeTdTrTbl+"</th>");
												else  if(isHeaderSortCol(colIndex)&& data != null && data.size() > 0)											    	
													outputBuff.append("<th " + "><center>"+startTbl+"<a  onclick=\"javascript:"+pgnCall+"\">"+getBundleString(strColHd, (String) dataValue.get(strColHd), "") +sortImgSrc+ "</a>"+closeTdTrTbl+"</th>");
												else
													outputBuff.append("<th "+ "><center>"+ getBundleString(strColHd, (String) dataValue.get(strColHd), "") +"</center></th>");
											}
										}
									} else if(isHtmlOutput && (appRB.getString("Sorting").equalsIgnoreCase("No") || (sorting!=null && sorting.length()>0 && sorting.equalsIgnoreCase("No")))){
										outputBuff.append( "<th " + "><center>"+tablecolattrobj2.getDisplayLabel()+"</center></th>");
										dataIndex++;
									}
								} else 
									outputBuff.append( "<th " + "><center>-</center></th>");
							}
						}

						ArrayList outputtblA = null;
						ArrayList outputtblB = null;

						if(tableSplit){
							outputtbl = new StringBuilder();//outputtbl, is for table split based on columns
							outputtblA = new ArrayList();
							outputtblB = new ArrayList();
							outputtbl.append(outputBuff);
							outputtbl.delete(0, outputtbl.indexOf("<td><table name"));							
							outputtbl.insert(0, "<td class=\"tblSplit\" ></td></td>");
							//System.out.println("Header H="+outputtbl);
						}

						if(rowsPerLine!=null && rowsPerLine.length()>0){							
							outputBuff.append("<tr><td colspan="+(displayType==null ? cols : cols+1)+"><hr class=\"hrCSSTH\"></td></tr>\r\n");							
						}

						addScrollProperty(initLowerName,cols,strDataTableLevelCSS,isDefRow());
						addSearchRow(cols);						
						/**** Table Header generation Ends Here************************************/

						if(EBWLogger.isDebugEnabled()) 
							EBWLogger.logDebug(this, "noDataFound flag value: "+noDataFoundMsg);

						if(data!=null && data.size()>0){

							int count=0;
							boolean rowALFlag = false;  
							if(tableSplit){								
								if(Integer.parseInt(rowsPerPage)<formObj.getDataRowCount())
									data.remove(data.size()-1);
								ArrayList rowAL = (ArrayList)data.get(0);

								if(rowAL.size()<=3){
									for(int j=0;j<data.size();j++){
										if(j<data.size()/2){
											outputtblA.add(data.get(j));
										}else{
											outputtblB.add(data.get(j));
										}
									}
									rowALFlag = true;
								}	
							}



							//*** Table Body Starts here
							Object[] colnames = dataValue.keySet().toArray();
							boolean[] defRowColComp = getBollArrDefCom(colnames.length);

							/**  code to determine the number of rows to be displayed in a page **/						
							int dataRowCount = formObj.getDataRowCount();/** Getting the actual number of rows  */
							int loopCount = 0;/** loopCount says till which index of rows we have to loop thru have to loop thru */
							int startIndex = 0;/** StartIndex from which index of rows we have to loop thru for display of data in the table */
							int pageRowCount = 0;/** pageRowCount will have the count of number of rows per page */
							int rowsLine = 0;/** rowsLine will have the count of rows within a line has to display in the table */
							if(rowsPerPage!=null && rowsPerPage.length()>0){//if it has the attribute rowsPerPage
								pageRowCount = Integer.parseInt(rowsPerPage);//Converting rowsPerPage to primitive type int
								/**if count of rows per page is less than the actual number of rows of data
								 * storing the pageRowCont to loopCount to restrict the actual number of
								 * rows to the number of rowsPerPage set in the taglib attribute, if not assigning the actual rowcount to the loopcount */
								loopCount=(pageRowCount<dataRowCount)?pageRowCount:dataRowCount;
							}else if(rowsPerPage==null || rowsPerPage.length()<=0)
								loopCount = dataRowCount;/**if rowsPerPage attribute is not set, setting the loopCount tothe actual rowCount*/

							if(rowsPerLine!=null && rowsPerLine.length()>0){
								rowsLine = Integer.parseInt(rowsPerLine);
							}
							int i = 0;int p=0;
							/**  code to determine the number of rows to be displayed in a page ends here **/

							StringBuilder strBInnerTable = new StringBuilder();/** strBInnerTable is used for Nested Table */
							String[] CBXData = null;/** combobox values got from getComboboxOptionData() are cashed in this variable*/
							/** code to insert an extra row which contains the editable input components control enters the loop if the tag attribute defRow = true*/						
							String editcolumns = getEditableColumns(); //getting the attribute value (columns to be edited)
							String editCol[] =null;
							if(isDefRow()){
								if(editcolumns!=null && editcolumns.length()>0){
									editCol = editcolumns.split(",");/**the attribute is comma separated,so splitting the using comma to get ediable I/P column numbers */
									isEditColsPresent=true;
								}
								sessionObj.setAttribute("sessionToChekDefRowAdd","no");
								if(isDefRow()){
									if((sessionObj.getAttribute("sessionToChekDefRowAdd").toString()).equalsIgnoreCase("no")){
										data.add(0,data.get(0));
										dataRowCount++;loopCount++;
										sessionObj.setAttribute("sessionToChekDefRowAdd","yes");
									}
								}
							}

							/** selectIdMatching boolean variable is used to check whether any row is to be highlighted.
							 * if the value is false no row is highlighted, therefore highlight first row. If the value if true some row has been highlighted. */
							boolean selectIdMatching = false;
							/** Loop through the datarow to print the table data */
							if (getDisplayType() != null) 
								selectIdMatching = getselectIdMatching(keyPos,dataEbwStart,startIndex,dataRowCount,selectId,loopCount);

							/** Method call to get the column level css as an hashmap where key is column number and value is the column css */
							LinkedHashMap columnCSSMap = getColumnCSSMap(titles,colnames.length);

							/**Method call to get the column level tooltip css as an hashmap where key is column number and value is the column css */
							LinkedHashMap columnToltipCSSMap = getColumnTooltipCSSMap(titles,colnames.length);

							/**Method call to get the column datatype as an hashmap where key is column number and value is the datatype of the column */
							LinkedHashMap columnDataTypeMap = new LinkedHashMap();
							if (colattrobj != null && colattrobj.size()>0 )
								columnDataTypeMap = getDataTypeMap(colattrobj);

							while(count<2){

								if(tableSplit && rowALFlag){
									if(count==0){
										data=outputtblA;
										loopCount = data.size();
										dataRowCount = data.size();
										startIndex = 0;
										p = 0;
									}else{
										data=outputtblB;
										loopCount = data.size();
										dataRowCount = data.size();
										startIndex = 0;
										p = 0;
										initLowerName=initLowerName+"_tbl";
									}							
								}
								for (i = startIndex; p < loopCount && i<dataRowCount; i++) {
									ArrayList rowData = (ArrayList) data.get(i);
									columnValuesForFilter=""; //Reset the filter values for every row
									p++;
									/**code dumped for rowlevel css*/
									String cssname = null;
									boolean extrnlRowlevelCss = false;
									/*if(isRowLevelCSS()){
								String cssnameTemp = (String)rowData.get(0);
								if(cssnameTemp!=null && cssnameTemp.length()>0){
									if(cssnameTemp.indexOf("=")>-1){
										String cssnameArr[] = cssnameTemp.split("=");
										cssname = cssnameArr[1];
										if(cssname!=null && cssname.length()>0)
											extrnlRowlevelCss = true;
									}
								}
							}*/

									String mouseOver="";

									if(getMouseOverEvent()!=null && getMouseOverEvent().length()>0 && getMouseOverEvent().equalsIgnoreCase("yes") ){
										String mouseOverColor="#e6f6ff";

										try{
											mouseOverColor=ebwTableRb.getString("mouseOverColor");
										}catch(Exception e)
										{}
										mouseOver=" onmouseover=\"document.getElementById('"+ initLowerName + "_" + i +"').style.backgroundColor='"+mouseOverColor+"';\""+" "+" onmouseout=\"document.getElementById('"+ initLowerName + "_" + i +"').style.backgroundColor='';\"";
									}

									/* Code added for applying conditionalRowlevelCSS*/
									String condRowCss = "";
									if(getConditionRowCss()!=null && getConditionRowCss().length()>0 ){
										String condRowCssVal="";
										EbwTableComponent ebwCompobj=new EbwTableComponent();
										ArrayList rowDataTemp = new ArrayList();

										rowDataTemp = isDefRow() ? i != 0 ? (ArrayList)(ArrayList)dataEbwStart.get(i - 1) : (ArrayList)(ArrayList)dataEbwStart.get(i) : (ArrayList)(ArrayList)dataEbwStart.get(i);

										try{
											condRowCssVal=ebwCompobj.getConditionalRowCSSString(headerEbwStart,getConditionRowCss(),rowDataTemp);
										}catch(Exception e)
										{}
										if(condRowCssVal.length()>0)
											condRowCss=" style=\""+condRowCssVal+"\"";
										else
											condRowCss="";
									}
									/*end of conditionalRowlevelCSS*/

									String trCssName="";								
									/*if(extrnlRowlevelCss){
								rowData.remove(0);
								trCssName="class=\""+cssname+"\"";*/
									if(getRowLevelCSS()!=null && getTableLevelCSS()!=null){
										trCssName = ((i % 2) == 0)? "class=\""+getRowLevelCSS()+"evenrow\"" : "class=\""+getRowLevelCSS()+"oddrow\"";
									}else
										trCssName = ((i % 2) == 0)? "class=\"evenrow\"" : "class=\"oddrow\"";
									//<tr can have evenrow or oddrow css or any other user specified rowlevelcss.
									if(isRowIdVal()){
										if(i==0 && isDefRow() && isRowHighLight() && (getDisplayType()!=null && getDisplayType().length()>0)) 
											outputBuff.append("<tr id=\"" + initLowerName + "_" + i + "\" class=\"scrollTR\"\" "+condRowCss+" onclick=\"javascript:colSelectColor("+initLowerName+","+initLowerName+"SelectId,'"+ initLowerName + "_" + i +"',"+defRowBool+");setRowId(this,document.forms[0]);"+ mouseOver+"\">");
										else if(isRowHighLight() && (getDisplayType()!=null && getDisplayType().length()>0)) 
											outputBuff.append("<tr id=\"" + initLowerName + "_" + i + "\" "+trCssName+" "+condRowCss+" onclick=\"javascript:colSelectColor("+initLowerName+","+initLowerName+"SelectId,'"+ initLowerName + "_" + i +"',"+defRowBool+");setRowId(this,document.forms[0]);"+ mouseOver+"\">");
										else if(isRowHighLight() && getDisplayType()==null)
											outputBuff.append("<tr id=\"" + initLowerName + "_" + i + "\" "+trCssName+" "+condRowCss+" ondblclick=\"javascript:colDblclik('"+initLowerName+"','"+ initLowerName + "_" + i +"');\" onclick=\"javascript:colSelectColor("+initLowerName+","+null+",'"+ initLowerName + "_" + i +"',"+defRowBool+");setRowId(this,document.forms[0]);"+ mouseOver+"\">");
										else
											outputBuff.append("<tr id=\"" + initLowerName + "_" + i + "\" onclick=\"javascript:setRowId(this,document.forms[0]);\" "+trCssName+" "+condRowCss+" "+ mouseOver+" >");
									}else{
										if(i==0 && isDefRow() && isRowHighLight() && (getDisplayType()!=null && getDisplayType().length()>0)) 
											outputBuff.append("<tr id=\"" + initLowerName + "_" + i + "\" class=\"scrollTR\"\" "+condRowCss+" onclick=\"javascript:colSelectColor("+initLowerName+","+initLowerName+"SelectId,'"+ initLowerName + "_" + i +"',"+defRowBool+");\" "+ mouseOver+">");
										else if(isRowHighLight() && (getDisplayType()!=null && getDisplayType().length()>0)) 
											outputBuff.append("<tr id=\"" + initLowerName + "_" + i + "\" "+trCssName+" "+condRowCss+" onclick=\"javascript:colSelectColor("+initLowerName+","+initLowerName+"SelectId,'"+ initLowerName + "_" + i +"',"+defRowBool+");\" "+ mouseOver+">");
										else if(isRowHighLight() && getDisplayType()==null)
											outputBuff.append("<tr id=\"" + initLowerName + "_" + i + "\" "+trCssName+" "+condRowCss+" ondblclick=\"javascript:colDblclik('"+initLowerName+"','"+ initLowerName + "_" + i +"');\" onclick=\"javascript:colSelectColor("+initLowerName+","+null+",'"+ initLowerName + "_" + i +"',"+defRowBool+");\" "+ mouseOver+">");
										else
											outputBuff.append("<tr id=\"" + initLowerName + "_" + i + "\"   "+trCssName+" "+condRowCss+" "+ mouseOver+">");
									}

									/** If it is a nested table, it queries and creates html table. */
									if (isNestedTable) 
										strBInnerTable = getNestedTable(dataValue, rowData, initLowerName, i, cols);
									if(getDisplayType() == null || getDisplayType()!=null){ 
										/*make this code avaliable for globally-Start*/
										ArrayList rowDataTemp = new ArrayList();
										//rowDataTemp = (isDefRow() ? ( i==0 ? ((ArrayList)formObj.getData().get(i)) : (ArrayList)formObj.getData().get(i-1)) : (ArrayList)formObj.getData().get(i) );
										rowDataTemp = isDefRow() ? i != 0 ? (ArrayList)(ArrayList)dataEbwStart.get(i - 1) : (ArrayList)(ArrayList)dataEbwStart.get(i) : (ArrayList)(ArrayList)dataEbwStart.get(i);
										/** Access Key ie. column def. for check/radio is separated and used. */
										int k= keyPos.size();
										chkData = "";
										if (k > 0) {
											for (int j=0; j < k; j++) {
												chkData = chkData + rowDataTemp.get(((Integer) keyPos.get(j)).intValue()) + "~";
											}
										} else {
											for(int z=0;z<rowDataTemp.size();z++){
												chkData = chkData + (String)rowDataTemp.get(z)+ "~";;
											}
										}
										if(chkData.indexOf("~")>-1)
											chkData = chkData.substring(0, chkData.lastIndexOf("~"));																			//added to chk if the data in any field is having dQuot (")..It is then replaced wth Ascii code										if(chkData.indexOf("\"")!=-1)										{											chkData = chkData.replaceAll("\"", "&#34");										}
										/*make this code avaliable for globally-End*/
										if(getDisplayType() != null) {								

											if(i==0 && isDefRow())
												outputBuff.append("<th > </th>");
											else if (getDisplayType().equals(TagLibConstants.TABLE_TYPE_SELECT_CHECK)) {
												String isToBeChecked = (selectId!=null && selectId.length>0 && isToBeChecked(selectId,chkData)) ? "checked" : "";
												if(isRowHighLight())
													outputBuff.append("<td class=\"colCSSelect\"><input type=\"checkbox\" name=\"" + initLowerName + "SelectId\" onclick=\"javascript:colSelectColor("+initLowerName+","+initLowerName+"SelectId,'"+ initLowerName + "_" + i +"',"+defRowBool+");\"  value=\"" + chkData + "\" "+isToBeChecked+"  ></center></td>");
												else
													outputBuff.append("<td class=\"colCSSelect\"><input type=\"checkbox\" name=\"" + initLowerName + "SelectId\" value=\"" + chkData + "\" "+isToBeChecked+"  ></center></td>");
											} else if ((getDisplayType().equals(TagLibConstants.TABLE_TYPE_SELECT_RADIO)) || (getDisplayType().equals(TagLibConstants.TABLE_TYPE_SELECT_RADIO_SHOW))) {			// REMOVE_RAD_SELECT_OBJ					
												String isToBeChecked = "";
												if(i==0 && ((selectId == null || selectId.length<0) || (selectId!=null && !selectIdMatching)))
													isToBeChecked="checked";
												else
													isToBeChecked = (selectId!=null && selectId.length>0 && isToBeChecked(selectId,chkData)) ? "checked" : "";										
												if(getDisplayType().equals(TagLibConstants.TABLE_TYPE_SELECT_RADIO_SHOW)){
													if(isRowHighLight())
														outputBuff.append("<td class=\"colCSSelect\"><input type=\"radio\" name=\"" + initLowerName + "SelectId\" onclick=\"javascript:colSelectColorRad("+initLowerName+","+initLowerName+"SelectId,'"+ initLowerName + "_" + i +"',"+defRowBool+");\" value=\"" + chkData + "\" "+isToBeChecked+" ></center></td>");
													else
														outputBuff.append("<td class=\"colCSSelect\"><input type=\"radio\" name=\"" + initLowerName + "SelectId\"   value=\"" + chkData + "\" "+isToBeChecked+" ></center></td>");
												}
												else{
													outputBuff.append("<td class=\"hidden\"><input type=\"radio\" name=\"" + initLowerName + "SelectId\" value=\"" + chkData + "\" "+isToBeChecked+" ></td>");
													// outputBuff.append("<td><input type=\"radio\" class=\"hidden\" name=\"" + initLowerName + "SelectId\"   value=\"" + chkData + "</td>");
												}
											}
										}/** end of code for displaying the first data of each row if it is a check/redio btn*/
									}//end of Globel condition
									for (int j = titles; j < (colnames.length); j++) { // -1 will ignore the table title key in the Map
										colDefFound = false;					
										String columnCSSMapStr = (String)columnCSSMap.get(Integer.toString(j-titles));								
										if (colattrobj != null) {
											for (int h = 0; h < colattrobj.size(); h++) {
												//Get Component Information and proceed creating the table cell
												TableColAttrObj tablecolattrobj = (TableColAttrObj) colattrobj.get(h);
												String  dataTypeStr = ((String)columnDataTypeMap.get(Integer.toString(h))).toLowerCase();
												if ((tablecolattrobj == null) || (colnames[j] == null) || (tablecolattrobj.getColName() == null)) {
													colDefFound = false;
												} else if (tablecolattrobj.getColName().equalsIgnoreCase(StringUtil.initLower(colnames[j].toString())) && !colnames[j].toString().equalsIgnoreCase("searchtitle") && !colnames[j].toString().equalsIgnoreCase("tabletitle")) {
													colDefFound = true;	                                    

													//Get data for columns from rowData arraylist
													if (rowData != null) {
														try{
															strData = convertToStr(rowData.get(j-titles));
														}catch(IndexOutOfBoundsException ioe){ // This column data is not found so adding empty to data.
															strData ="";
														}
														columnValuesForFilter =  columnValuesForFilter+tablecolattrobj.getColName().toLowerCase()+"=" +strData+",";
													} else 
														strData ="";
													/** NestedTable Starts */
													if (isNestedTable) {
														if (j==titles && strBInnerTable.length() > 0) 
															strData="<a href=\"#\" onclick=\"treetable_toggleRow('" + initLowerName + "_" + i + "');\"><img border=\"0\" id=\"Img_" + initLowerName + "_" + i +"\" src=\"../images/tog_minus.gif\" /></a>&nbsp;&nbsp;" + strData;
													}/** NestedTable Ends */
													align="";										
													if (tablecolattrobj.getDataType()!=null) {

														if (dataTypeStr.length() > 0 && otherDataType.indexOf(dataTypeStr)>-1 && (columnCSSMapStr ==null || (columnCSSMapStr!=null && columnCSSMapStr.length()<=0)))
															align=" align=\"left\"";
														else if(dataTypeStr.length() > 0 && numericalDataType.indexOf(dataTypeStr) > -1 && (columnCSSMapStr==null || (columnCSSMapStr!=null && columnCSSMapStr.length()<=0))) 
															align=" align=\"right\"";
														else if(columnCSSMapStr!=null && columnCSSMapStr.length()>=0) 
															align="";
														else
															align=" align=\"right\"";
													}	

													String colCompType = tablecolattrobj.getComponentType();
													String colColSpan = new String();
													if(isHtmlOutput){
														colColSpan = tablecolattrobj.getColSpan();
													}
													if (colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_COMBOBOX)) {
														/**If the isDefRow() is true then default row is to be added. which contains the editabel input components as in the other row.*/
														if(i==0 && (isDefRow()) && isEditColsPresent) {
															if(getColumnPresent(editCol,h) == true) {
																if(columnCSSMapStr!=null && columnCSSMapStr.length()>0){
																	if(h==0 && storeHiddenValue==true)
																		outputBuff.append("<th  class=\""+columnCSSMapStr+"\"> <input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\"><center><select name=\"" + tablecolattrobj.getColName() + "\" id = \"" + tablecolattrobj.getColName() + "\" onchange=\"javascript:fillCombo(document,'"+tablecolattrobj.getColName()+"','"+initLowerName+ "SelectId')\">");
																	else
																		outputBuff.append("<th  class=\""+columnCSSMapStr+"\"> <center><select name=\"" + tablecolattrobj.getColName() + "\" id = \"" + tablecolattrobj.getColName() + "\" onchange=\"javascript:fillCombo(document,'"+tablecolattrobj.getColName()+"','"+initLowerName+ "SelectId')\">");
																}else{ 
																	if(h==0 && storeHiddenValue==true)
																		outputBuff.append("<th  ><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\"><center><select name=\"" + tablecolattrobj.getColName() + "\" id = \"" + tablecolattrobj.getColName() + "\" onchange=\"javascript:fillCombo(document,'"+tablecolattrobj.getColName()+"','"+initLowerName+ "SelectId')\">");
																	else
																		outputBuff.append("<th  ><center><select name=\"" + tablecolattrobj.getColName() + "\" id = \"" + tablecolattrobj.getColName() + "\" onchange=\"javascript:fillCombo(document,'"+tablecolattrobj.getColName()+"','"+initLowerName+ "SelectId')\">");
																}
															}else
																outputBuff.append("<th " + columnWidth + " ></th>");
														}else {
															if(columnCSSMapStr!=null && columnCSSMapStr.length()>0) {											    		
																if(h==0 && storeHiddenValue==true)
																	outputBuff.append("<td class=\""+columnCSSMapStr+"\"><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\"><center><select name=\"" + tablecolattrobj.getColName() + "\" id=\"" + tablecolattrobj.getColName()+"_"+i+ "\">");
																else
																	outputBuff.append("<td  class=\""+columnCSSMapStr+"\"><center><select name=\"" + tablecolattrobj.getColName() + "\" id=\"" + tablecolattrobj.getColName()+"_"+i+ "\">");
															} else {						    		
																if(h==0 && storeHiddenValue==true)
																	outputBuff.append("<td  ><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\"><center><select name=\"" + tablecolattrobj.getColName() + "\" id=\"" + tablecolattrobj.getColName()+"_"+i+ "\">");
																else
																	outputBuff.append("<td ><center><select name=\"" + tablecolattrobj.getColName() + "\" id=\"" + tablecolattrobj.getColName()+"_"+i+ "\">");
															}
														}												
														/**strdata is not needed for combobox as the data has to come from a service call. 
												This has to be cached and displayed for pagewise display of table, when the user
												clicks next,previous etc, it should not call the service again.*/
														//Pass column names and values to this combobox for filtering. Also remove last comma and add with Dyna value
														columnValuesForFilter = columnValuesForFilter + tablecolattrobj.getDynamicValue() + ",";
														columnValuesForFilter = columnValuesForFilter + "tablerowdata=" + i + "~" + chkData;
														/** The call to getComboboxOptionData()  will happen only once for each column.
														 * That value is cashed in CBXData[colno] while dumping first row contents in 
														 * outputBuff and reused for the rest fo the rows for that column */
														if (CBXData == null)
															CBXData = new String[colattrobj.size()];
														//CBXData = new String[colnames.length];

														if (CBXData[h] == null)
															CBXData[h] = getComboboxOptionData(tablecolattrobj.getDefaultValue(), tablecolattrobj.getColName(), columnValuesForFilter);
														outputBuff.append(CBXData[h]);
														if(i==0 && (isDefRow()) && isEditColsPresent) 
															outputBuff.append("</select></center></th>");
														else
															outputBuff.append("</select></center></td>");
													}else if( colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_IMAGE)) {
														outputBuff.append("<td align=\"left\">"+tablecolattrobj.getTagContent()+"</td>");
													}else if (colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_TEXTFIELD) ||
															colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_BUTTON) ||
															colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_CHECKBOX) ||
															colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_BROWSE) ||
															colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_DATE) ||
															colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_DATETIME) ||
															colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_HREFHIDDEN) ||
															colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_HREFHIDDENENABLE)){	

														if(columnCSSMapStr!=null && columnCSSMapStr.length()>0){
															if(h==0 && storeHiddenValue==true)
																outputBuff.append("<td class=\""+columnCSSMapStr+"\"><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\"><center>" + replaceWithValue(tablecolattrobj, strData, chkData,columnCSSMapStr,i,h,defRowColComp) +"</center></td>");
															else{
																if(i==0 && isDefRow())
																	outputBuff.append("<th  class=\""+columnCSSMapStr+"\"><center>" + replaceWithValue(tablecolattrobj, strData, chkData,columnCSSMapStr,i,h,defRowColComp) +"</center></th>");
																else
																	outputBuff.append("<td  class=\""+columnCSSMapStr+"\">" + replaceWithValue(tablecolattrobj, strData, chkData,columnCSSMapStr,i,h,defRowColComp) +"</td>");
															}
														}else {
															if(h==0 && storeHiddenValue==true)
																outputBuff.append("<td  ><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\"><center>" + replaceWithValue(tablecolattrobj, strData, chkData,i,h,defRowColComp) +"</center></td>");
															else{
																if(i==0 && isDefRow())
																	outputBuff.append("<th ><center>" + replaceWithValue(tablecolattrobj, strData, chkData,i,h,defRowColComp) +"</center></th>");
																else
																	outputBuff.append("<td >" + replaceWithValue(tablecolattrobj, strData, chkData,i,h,defRowColComp) +"</td>");
															}
														}
													}else if(colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_COLORCODEDIMAGE)){//Code Start Added by 163974												
														String onclickAttr="";
														if(i==0 && isDefRow())
															outputBuff.append("<th > </th>");
														else if(strData.equals("0"))
															outputBuff.append("<td " + columnWidth + " class=\"tableData\">&nbsp;</td>");
														else{
															EbwTableComponent objList=new EbwTableComponent();													
															if(tablecolattrobj.getFieldAttribute().indexOf("onclick")!=-1){
																EBWLogger.logDebug(this,"Inside colorcoded img tablecolattrobj.getFieldAttribute() :"+tablecolattrobj.getFieldAttribute());
																onclickAttr=tablecolattrobj.getFieldAttribute().substring(tablecolattrobj.getFieldAttribute().indexOf("onclick"),tablecolattrobj.getFieldAttribute().length());
															}
															String img=objList.getImageValue(strData,tablecolattrobj.getFieldAttribute().substring(0,tablecolattrobj.getFieldAttribute().lastIndexOf(",")));
															//String imgTooltip ="";
															String imgVal ="";
															try{
																/* imgTooltip = ebwTableRb.getString(img+"_alt");
														 if(imgTooltip.length()<=0)
															 imgTooltip=""; */
																imgVal = ebwTableRb.getString(img);
															}
															catch(Exception e)
															{
																//imgTooltip = strData;
																imgVal = "";
															}
															outputBuff.append((new StringBuilder("<td ")).append(columnWidth).append(" class=\"tableData\">").append(imgVal).append("</td>").toString());

															/*if(ebwTableRb.getString(img)!=null && ebwTableRb.getString(img).length()>0 ){
														if(onclickAttr.equals(""))
															outputBuff.append("<td " + columnWidth + " class=\"tableData\"><img src=\""+ebwTableRb.getString(img)+"\" alt=\""+imgTooltip+"\"></td>");
														else
															outputBuff.append("<td " + columnWidth + " class=\"tableData\"><img id=\""+tablecolattrobj.getColName()+"Img_"+i+"\" "+onclickAttr+" src=\""+ebwTableRb.getString(img)+"\" alt=\""+imgTooltip+"\"></td>");
													}
													else
														outputBuff.append("<td " + columnWidth + " class=\"tableData\"></td>");*/
														}	

													}else if(colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_CONDITIONAL_COLCSS)){											
														EbwTableComponent ebwCompobj=new EbwTableComponent();
														String condColCssStr= ebwCompobj.getConditionalcolCSSString(headerEbwStart,tablecolattrobj.getFieldAttribute(),chkData);

														if(i==0 && isDefRow())
															outputBuff.append("<th > </th>");
														else{
															if(condColCssStr.length()>0){
																outputBuff.append("<td " + columnWidth + " class=\"tableData\" style=\""+condColCssStr+"\">"+strData+"</td>");
															}
															else if(columnCSSMapStr!=null && columnCSSMapStr.length()>0){
																outputBuff.append("<td " + columnWidth + " class=\""+columnCSSMapStr+"\" >"+strData+"</td>");	
															}
															else
																outputBuff.append("<td " + columnWidth + " class=\"tableData\" >"+strData+"</td>");	
														}
													}else if(colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_COLORSTATUSBAR)){
														String strContent = tablecolattrobj.getFieldAttribute();												
														if(strContent.indexOf("=")!=-1){
															String attr1 = strContent.substring(strContent.indexOf("=")+1, strContent.indexOf("%"));
															attr1=attr1.trim();
															int attr2= (100-Integer.parseInt(attr1));
															String attrVal = "<table width=\"100%\"><tr><td id=\"td1clrStatus_"+i+"\" class=\"clrStatus1\">"+attr1+"%</td><td id=\"td2clrStatus_"+i+"\" class=\"clrStatus2\">"+attr2+"%</td></tr></table>";
															outputBuff.append("<td " + columnWidth + " class=\"tableData\">"+attrVal+"</td>");
														}else
															outputBuff.append("<td " + columnWidth + " class=\"tableData\">&nbsp;</td>");
													} else if(colCompType.equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_HREF_ACTION)){//Added for MS_OBJ
														String strContent = tablecolattrobj.getTagContent();
														//tablecolattrobj.getDynamicValue()
														String dynValue = strData;
														StringBuffer strBReplace = new StringBuffer();
														if (strContent.indexOf("\"#\"") == -1)  
															strContent = strContent.replaceFirst(".do\"", ".do" + "?tablerowdata=" + chkData + "\"");
														if(dynValue!=null && dynValue.length()>0){
															String[] dynColValue = dynValue.split(",");	    	
															for(int n=0;n<dynColValue.length;n++){
																strBReplace.append(strContent.replaceAll("~VARIABLE", dynColValue[n]));	    		 
																if(n<(dynColValue.length-1))	    			
																	strBReplace.append("<span class=\"seperator\">|</span>");
															}
															// outputBuff.append("<td>"+strBReplace.toString()+"</td>");
															if((String)columnCSSMapStr!=null && columnCSSMapStr.length()>0)
																outputBuff.append("<td class=\""+columnCSSMapStr+"\">"+strBReplace.toString()+"</td>");
															else
																outputBuff.append("<td>"+strBReplace.toString()+"</td>");

														}
													}else {
														if(i==0 && isDefRow())
															outputBuff.append("<th > </th>");
														else {
															EBWLogger.logDebug(this,"Component type is not defined for column extrnl:"+strData);	
															if(extrnlRowlevelCss){							
																if (align.indexOf("right") > -1 || align.indexOf("center")>-1) {
																	if(h==0 && storeHiddenValue==true)
																		outputBuff.append("<td " + columnWidth + " class=\""+cssname+"\"><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\"><div "+align+">" + replaceWithValue(tablecolattrobj, strData, chkData,columnCSSMapStr,i,h,defRowColComp) +"</div></td>");
																	else
																		outputBuff.append("<td " + columnWidth + " class=\""+cssname+"\"><div "+align+">" + replaceWithValue(tablecolattrobj, strData, chkData,columnCSSMapStr,i,h,defRowColComp) +"</div></td>");
																}else {
																	if(h==0 && storeHiddenValue==true)
																		outputBuff.append("<td " + columnWidth + " class=\""+cssname+"\"><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\">" + replaceWithValue(tablecolattrobj, strData, chkData,i,h,defRowColComp)  + "</td>");
																	else
																		outputBuff.append("<td " + columnWidth + " class=\""+cssname+"\">" + replaceWithValue(tablecolattrobj, strData, chkData,i,h,defRowColComp)  + "</td>");
																}
															}else{
																if (align.indexOf("right") > -1) {
																	if((String)columnCSSMapStr!=null && columnCSSMapStr.length()>0){
																		if(h==0 && storeHiddenValue==true)
																			outputBuff.append("<td  class=\""+columnCSSMapStr+"\"><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\"><div "+align+">" + replaceWithValue(tablecolattrobj, strData, chkData,columnCSSMapStr,i,h,defRowColComp) +"</div></td>");
																		else
																			outputBuff.append("<td  class=\""+columnCSSMapStr+"\"><div "+align+">" + replaceWithValue(tablecolattrobj, strData, chkData,columnCSSMapStr,i,h,defRowColComp) +"</div></td>");
																	}else{
																		if(h==0 && storeHiddenValue==true)
																			outputBuff.append("<td  ><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\"><div "+align+">" + replaceWithValue(tablecolattrobj, strData, chkData,i,h,defRowColComp) +"</div></td>");
																		else
																			outputBuff.append("<td  ><div "+align+">" + replaceWithValue(tablecolattrobj, strData, chkData,i,h,defRowColComp) +"</div></td>");
																	}
																} else {
																	if(columnCSSMapStr!=null && columnCSSMapStr.length()>0){
																		if(h==0 && storeHiddenValue==true)
																			outputBuff.append("<td  class=\""+columnCSSMapStr+"\"><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\">" + replaceWithValue(tablecolattrobj, strData, chkData,columnCSSMapStr,i,h,defRowColComp) + "</td>");
																		else
																			outputBuff.append("<td  class=\""+columnCSSMapStr+"\">" + replaceWithValue(tablecolattrobj, strData, chkData,columnCSSMapStr,i,h,defRowColComp) + "</td>");
																	}else{
																		if(h==0 && storeHiddenValue==true){
																			if(!isHtmlOutput){
																				outputBuff.append("<td ><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\">" + replaceWithValue(tablecolattrobj, strData, chkData,i,h,defRowColComp) + "</td>");
																			}else{
																				outputBuff.append("<td ><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\"><div "+align+">" + replaceWithValue(tablecolattrobj, strData, chkData,i,h,defRowColComp) + "</div></td>");
																			}
																		}else{
																			if(!isHtmlOutput){
																				outputBuff.append("<td >" + replaceWithValue(tablecolattrobj, strData, chkData,i,h,defRowColComp) + "</td>");
																			}else if(isHtmlOutput &&(colColSpan == null || colColSpan.equalsIgnoreCase(""))){
																				outputBuff.append("<td ><div "+align+">"  + replaceWithValue(tablecolattrobj, strData, chkData,i,h,defRowColComp) + "</div></td>");

																			}

																		}


																	}
																}
															}
														}
													}
													align="";
												} else {
													//EBWLogger.logDebug(this, "tablecolattrobj.getColName() is not matching with column[i]:"+tablecolattrobj.getColName());
												}
											}
										}else
											EBWLogger.logDebug(this,  "ColAttribute not present for column");								    

										if (!colDefFound && !isColAttrPresent) {
											strData = convertToStr(rowData.get(j-titles));
											align="";
											try {
												if (!Double.isNaN(Double.parseDouble(strData))) 
													align=" align=\"right\"";
											} catch (Exception exc) {}
											if (align.indexOf("right") > -1) {
												if(columnCSSMapStr!=null && columnCSSMapStr.length()>0){
													if((j-titles)==0 && storeHiddenValue==true)
														outputBuff.append("<td class=\""+columnCSSMapStr+"\"><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\">"+ strData + "</div></td>");
													else
														outputBuff.append("<td class=\""+columnCSSMapStr+"\">"+ strData + "</div></td>");
												}else{
													if((j-titles)==0 && storeHiddenValue==true)
														outputBuff.append("<td ><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\"><div "+align+">" + strData + "</div></td>");
													else
														outputBuff.append("<td ><div "+align+">" + strData + "</div></td>");
												}
											} else {
												if(columnCSSMapStr!=null && columnCSSMapStr.length()>0 ){
													if((j-titles)==0 && storeHiddenValue==true)
														outputBuff.append("<td class=\""+columnCSSMapStr+"\"><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\">" + strData + "</td>");
													else
														outputBuff.append("<td class=\""+columnCSSMapStr+"\">" + strData + "</td>");
												}else{
													if((j-titles)==0 && storeHiddenValue==true)
														outputBuff.append("<td ><input type=\"hidden\" name=\"" + initLowerName + "_RowValue\" value=\"" + chkData + "\">" + strData + "</td>");
													else
														outputBuff.append("<td >" + strData + "</td>");
												}
											}
											align="";
										}
									}
									tablecolattrobj2 = null;							
									outputBuff.append(CLOSE_TR_WITHNEWLINE);

									if(rowsPerLine!=null && rowsPerLine.length()>0 && i==rowsLine-1){								
										outputBuff.append("<tr><td colspan="+(displayType==null ? cols : cols+1)+"><hr class=\"hrCSSTD\"></td></tr>\r\n");
										rowsLine=rowsLine+Integer.parseInt(rowsPerLine);
									}

									/**	 * NestedTable Starts */
									if (isNestedTable) {
										if (strBInnerTable.length() > 0)
											outputBuff.append(strBInnerTable);
									} /** NestedTable Ends	 */
								}
								outputBuff.append(CLOSE_TABLE_WITHNEWLINE);						

								if(tableSplit && rowALFlag){
									if(count==0)
										outputBuff.append(outputtbl);
									count++;
								}else{
									count=count+2;
								}
							}//end of while, tableSplit
						}else{
							if(noDataFoundMsg.equalsIgnoreCase("true"))
								outputBuff.append(NO_DATA_FOUND_ROW_WITHNEWLINE+CLOSE_TABLE_WITHNEWLINE);
						}
					}else{
						if(noDataFoundMsg.equalsIgnoreCase("true"))
							outputBuff.append(NO_DATA_FOUND_ROW_WITHNEWLINE+CLOSE_TABLE_WITHNEWLINE);
					}					
					if (scrollable != null && scrollable.equalsIgnoreCase("true")) 
						outputBuff.append(CLOSE_DIV+"</td></tr></table>");
					if(!ajaxCall)
						outputBuff.append(CLOSE_DIV); 
				}				
			} else 
				outputBuff.append(NO_DATA_FOUND_ROW_WITHNEWLINE+CLOSE_TABLE_WITHNEWLINE);
			/** setting the original values back to the form. i.e)before reordering and hiding columns*/
			if(headerEbwStart !=null && headerEbwStart.size()>0 ){
				ArrayList arr = new ArrayList();
				Object[] str = ((((LinkedHashMap)headerEbwStart.get(0)).keySet()).toArray());
				for(int j=0;j<str.length;j++)
					arr.add(str[j]);
				dataEbwStart.add(0,arr);
				formObj.setData(dataEbwStart);
			}
			if(colAttrStart != null && colAttrStart.size()>0)
				formObj.setColAttrObjs(colAttrStart);
			/** Setting back to the form end here*/
			EBWLogger.trace(this,"Exited from EBWTABLE-do start tag");
			System.out.println("EBWTABLE-End");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return SKIP_BODY;/** Must return SKIP_BODY because we are not supporting a body for this tag.*/
	}

	/*private String getHeaderSortImgStr(String asc,String desc){
		StringBuffer sb = new StringBuffer();
		if(asc.endsWith("Shade"))
			sb.append(ebwTableRb.getString("HeaderSortAscShadeImg"));
		else
			sb.append(ebwTableRb.getString("HeaderSortAscImg"));

		if(desc.endsWith("Shade"))
			sb.append(ebwTableRb.getString("HeaderSortDescShadeImg"));
		else
			sb.append(ebwTableRb.getString("HeaderSortDescImg"));
		return sb.toString();
	}*/
	/**
	 * Due to New Sorting implementation, Old is modified. Now by default page will load without any sorting image,
	 * onclick of text of the column header Ascending order with arrow image will appear.
	 * For Decending order you can click on the same. 
	 * @param asc
	 * @return
	 */
	private String getHeaderSortImgStr(String asc){
		StringBuffer sb = new StringBuffer();

		if (asc!=null){			
			if(asc.equalsIgnoreCase("asc")){
				sb.append(ebwTableRb.getString("HeaderSortAscImg"));			
			}else{
				sb.append(ebwTableRb.getString("HeaderSortDescImg"));
			}
		}else{
			sb.append(ebwTableRb.getString("HeaderSortDftImg"));
		}

		return sb.toString();
	}

	private String getDefaultSortImgStr(String asc,int colIndex){
		StringBuffer sb = new StringBuffer();		
		if(defaultSortedCol!=null && defaultSortedCol.length()>0)
		{
			//if(defaultSortedColOrder!=null && defaultSortedColOrder.length()>0){
			String[] defaultSortColumn = defaultSortedCol.split(",");
			//String[] defaultSortColumnOrder = defaultSortColumn.split(",");
			for(int i = 0;i<defaultSortColumn.length;i++){
				String column = defaultSortColumn[i].substring(0,defaultSortColumn[i].indexOf("="));
				String defaultSortColumnOrder = defaultSortColumn[i].substring(defaultSortColumn[i].indexOf("=")+1,defaultSortColumn[i].length());
				if(colIndex == Integer.parseInt(column)){
					if(defaultSortColumnOrder.equalsIgnoreCase("ASC"))
						sb.append(ebwTableRb.getString("HeaderSortDefaultImgASC"));
					else
						sb.append(ebwTableRb.getString("HeaderSortDefaultImgDESC"));
				}
			}
			//}
			//else
			//	sb.append(ebwTableRb.getString("HeaderSortDefaultImg"));
		}
		return sb.toString();
	}



	private String getHeaderSortImgStr1(boolean val){
		StringBuffer sb = new StringBuffer();
		if(val)
			sb.append(ebwTableRb.getString("HeaderSortAscImg"));
		else
			sb.append(ebwTableRb.getString("HeaderSortDescImg"));


		return sb.toString();
	}
	private boolean[] getBollArrDefCom(int colCount){
		boolean result[] =null;
		boolean displayType = false;		
		if(getDisplayType()!=null && getDisplayType().length()>0){
			displayType = true;
			result = new boolean[colCount+1];
		}else
			result = new boolean[colCount];
		String cols = "";
		String defCompStr = getDefRowComp();
		if(defCompStr !=null && defCompStr.length()>0){
			String[] defcols = defCompStr.split("~");
			for(int i=0;i<defcols.length;i++){
				String[] eachColDef = defcols[i].split(":");
				if(eachColDef !=null && eachColDef.length>0)
					cols  = cols+eachColDef[0]+":";	
			}
		}			
		for(int z=0;z<result.length;z++){
			if(z==0 && displayType )
				result[z++] = false;
			else
				result[z]=(cols.indexOf(z+"")>-1)?true:false;
		}
		return result;
	}

	private String getComboboxOptionData(String fileName, String compName, String strDynValue) throws Exception {
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entering method String getComboboxOptionData():- fileName="+fileName+", compName="+compName+", strDynValue="+strDynValue);
		String strCboOptions=(isHtmlOutput)?(new ComboboxData().getDataForTableCombo(fileName, compName, strDynValue)):(new ComboboxData().getComboboxDataForTable(fileName, strDynValue));
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Returning from method String getComboboxOptionData(String fileName, String compName, String strDynValue)");
		return strCboOptions;
	}

	private String convertToStr(Object obj) {
		return ConvertionUtil.convertToUIString(obj);
	}

	private boolean getselectIdMatching(ArrayList keyPos,ArrayList forReOrChekHiddenRowData,int startIndex,int dataRowCount,String[] selectId,int loopCount){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entering method getselectIdMatching()");
		boolean result = false;
		int p=0;int i=0;
		String chkData = null;
		if(forReOrChekHiddenRowData==null && forReOrChekHiddenRowData.size()>0){
			for (i = startIndex; p < loopCount && i<dataRowCount; i++) {
				ArrayList rowDataTemp = (ArrayList)forReOrChekHiddenRowData.get(i);
				if(rowDataTemp !=null && rowDataTemp.size()>0){
					int k= keyPos.size();
					if (k > 0) {
						chkData = "";
						for (int j=0; j < k; j++) 
							chkData = chkData + rowDataTemp.get(((Integer) keyPos.get(j)).intValue()) + "~";
						chkData = chkData.substring(0, chkData.lastIndexOf("~"));
					} else {
						for(int z=0;z<rowData.size();z++)
							chkData = chkData + (String)rowData.get(z)+ "~";
						chkData = chkData.substring(0, chkData.lastIndexOf("~"));
					}
					if(isToBeChecked(selectId,chkData))
						result = true;
					break;
				}				
			}
		}
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exiting from method getselectIdMatching():- output:"+result);
		return result;		
	}	

	private String addCheckLinkPrev(int cols, String tblName, String pgnIndex, int dataRowCount,String previousAction,String restartKeyFromFormbean,String ebwactionState,String reorderColmns) {
		StringBuilder pageDispChk = new StringBuilder();		
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entering method inside prev_action():- tblName="+tblName+"  previousAction="+previousAction+"   restartKeyFromFormbean="+restartKeyFromFormbean+"  ebwactionState="+ebwactionState);
		StringBuffer strBChk = new StringBuffer();
		int paginationIndex = 0;int rowsInPage = 0;
		boolean isMultiplePage = false;
		String tblTitle = null;	String prevActn = null;	String prevState = null;
		String prevUrl = null; String ebwActn  = null;String ebwState = null;
		if(ebwactionState!=null && ebwactionState.length()>0 && ebwactionState.indexOf(":")>-1){
			String[] actnState = ebwactionState.split(":");
			ebwActn = actnState[0];
			ebwState = actnState[1];
		}	
		tblTitle =formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + tblName);
		if(getTableLevelCSS()!=null)
			strBChk.append("<table class=\""+getTableLevelCSS()+"Bar\"><tr>");
		else
			strBChk.append("<table  class=\"layoutMaxTable\"  ><tr>");

		/**piece of code to display table title wn not null.if some reorder or export icons there in the right corner , 
		 * then width is to be calculated for the tbltitle  */
		int colSpan=0;
		if(!reorderingImg){			
			colSpan=cols+3;
		}
		if(tblTitle!=null || tblTitle!=""){
			if(isExportEnabled().equalsIgnoreCase("false") ){
				if(appRB.getString("Reordering").equalsIgnoreCase("No") || (reordering!=null && reordering.length()>0 && reordering.equalsIgnoreCase("No"))) {
					int len=(tblTitle.length())*10;
					strBChk.append("<td colspan=\""+colSpan+"\" class=\"tabletitle\" width=\""+len+"px\">"+tblTitle+"</td>");
				}else{
					int len=(tblTitle.length())*10;
					strBChk.append("<td colspan=\""+colSpan+"\" class=\"tabletitle\" width=\""+len+"px\">"+tblTitle+"</td>");
				}
			}else
				strBChk.append("<td colspan=\""+colSpan+"\" class=\"tabletitle\" >"+tblTitle+"</td>");
		}else
			strBChk.append("<td colspan=\""+colSpan+"\" class=\"tabletitle\" >\"Noname\"</td>");

		if(!reorderingImg){
			strBChk.append("</tr><tr><td class=\"tabletitle\">");
		}

		if(rowsPerPage!=null && rowsPerPage.length()>0){			
			rowsInPage = Integer.parseInt(rowsPerPage);
			String[] prevActnArray = previousAction.split(":");
			if(prevActnArray!=null && prevActnArray.length>=3){
				prevUrl = prevActnArray[0]+".do";
				prevActn = prevActnArray[1];	
				prevState = prevActnArray[2];						
			}
			if(rowsInPage<dataRowCount)
				isMultiplePage = true;

			if(!reorderingImg){
				if(!(appRB.getString("Reordering").equalsIgnoreCase("No") || (reordering!=null && reordering.length()>0 && reordering.equalsIgnoreCase("No")))) 
					strBChk.append("<a href=\"#\" onClick=\"popupwindow=setToReorderParent(document.forms[0]);window.open(\'popupDiff.jsp?dis="+displaySearch+"&availableCols="+availableColumns+"&visiblecols="+visibleColumns+"&actnType=reorder&prevAction="+prevActn+"&prevState="+prevState+"&prevUrl="+prevUrl+"&tablename="+tblName+"\','popupwindow','resizable=no,width=350,height=240,top=233,left=325,status=No')\"><img align=\"absmiddle\" src=\"../images/reorderactive.gif\" border=\"0\" alt=\"Reorder Columns\"></a></td>");
			}
			if(!pageDisplay)
				strBChk.append("<td colspan=\""+cols+"\" class=\"pageNo\">");
			if(pgnIndex !=null && pgnIndex.length()>0){				
				paginationIndex = Integer.parseInt(pgnIndex);				
				if(paginationIndex>1)					
					pageDispChk.append("<a  onclick=\"javascript:pgnUrlSubmit(document.forms[0],'"+prevUrl+"','"+prevActn+"','"+prevState+"','"+restartKeyFromFormbean+"','"+paginationIndex+"','Prev','Prev');\"><img align=\"absmiddle\" id=\"prevBtnId\" src=\"../images/prev.gif\" border=\"0\" alt=\"Previous\" >" +"</a>");					
				if(pgnIndex!=null && pgnIndex.length()>0){
					if(pageDisplay){
						int totalPages=1;
						if(totalRowCount%rowsInPage == 0)
							totalPages = totalRowCount/rowsInPage;
						else
							totalPages = (totalRowCount/rowsInPage)+1;
						pageDispChk.append("&nbsp;&nbsp;<input type=\"text\" id=\"pagedisplaytxt\" class=\"pagedisplay\" value=\""+paginationIndex+" of "+totalPages+"\" readonly >&nbsp;&nbsp;");						
					}else if(isMultiplePage)
						pageDispChk.append("Page : "+paginationIndex+"+");
					else
						pageDispChk.append("Page : "+paginationIndex);
				}
				if(isMultiplePage)
					pageDispChk.append("<a href=\"#\" onclick=\"javascript:pgnUrlSubmit(document.forms[0],'"+prevUrl+"','"+prevActn+"','"+prevState+"','"+restartKeyFromFormbean+"','"+paginationIndex+"','next','next');\"><img align=\"absmiddle\" id=\"nextBtnId\" src=\"../images/next1.gif\" border=\"0\" alt=\"Next\" >" +"</a>&nbsp;&nbsp;");
			}		

			if(!pageDisplay){
				strBChk.append(pageDispChk);
				strBChk.append("</td>");
			}
		}

		strBChk.append("<td colspan=\"2\" class=\"reorder\">" );
		if (isNestedTable) {
			strBChk.append("<a href=\"#\" onclick=\"javascript:treetable_expandAll('" + tblName + "')\"><img src=\"../images/expand.gif\" border=\"0\" alt=\"Expand\"></a>&nbsp;&nbsp;");
			strBChk.append("<a href=\"#\" onclick=\"javascript:treetable_collapseAll('" + tblName + "')\"><img src=\"../images/collapse.gif\" border=\"0\" alt=\"Collapse\"></a>&nbsp;&nbsp;");
		}
		if(reorderingImg){
			if(!(appRB.getString("Reordering").equalsIgnoreCase("No") || (reordering!=null && reordering.length()>0 && reordering.equalsIgnoreCase("No")))) 
				strBChk.append("<a href=\"#\" onClick=\"popupwindow=setToReorderParent(document.forms[0]);window.open(\'popupDiff.jsp?dis="+displaySearch+"&availableCols="+availableColumns+"&visiblecols="+visibleColumns+"&actnType=reorder&prevAction="+prevActn+"&prevState="+prevState+"&prevUrl="+prevUrl+"&tablename="+tblName+"\','popupwindow','resizable=no,width=350,height=240,top=233,left=325,status=No')\"><img align=\"absmiddle\" src=\"../images/reorderactive.gif\" border=\"0\" alt=\"Reorder Columns\"></a>&nbsp");
		}
		if(isExportEnabled().equalsIgnoreCase("true")){
			if(rowsPerPage!=null && rowsPerPage.length()>0){
				if(xlpdfExportEnabled.equalsIgnoreCase("excel")||xlpdfExportEnabled.equalsIgnoreCase("all"))
					strBChk.append("<a href=\"#\" onclick=\"javascript:exportSubmit(document.forms[0],'"+ebwActn+"','"+ebwState+"','"+prevUrl+"','"+prevActn+"','"+prevState+"','"+rowsPerPage+"','Excel','"+reorderColmns+"');return false;\"><img align=\"absmiddle\" src=\"../images/xlsactive.gif\" border=\"0\" class=\"exportImg\" alt=\""+ebwTableRb.getString("excel.tooltip")+"\">&nbsp;Export</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp");
				if(xlpdfExportEnabled.equalsIgnoreCase("pdf")||xlpdfExportEnabled.equalsIgnoreCase("all"))
					strBChk.append("<a href=\"#\" onclick=\"javascript:exportSubmit(document.forms[0],'"+ebwActn+"','"+ebwState+"','"+prevUrl+"','"+prevActn+"','"+prevState+"','"+rowsPerPage+"','PDF','"+reorderColmns+"');return false;\"><img align=\"absmiddle\" src=\"../images/pdfactive.gif\" border=\"0\" alt=\""+ebwTableRb.getString("pdf.tooltip")+"\"></a>&nbsp;&nbsp");
				//CSV export has been removed to match NCS L& F. 
				//strBChk.append("<a href=\"#\" onclick=\"javascript:exportSubmit(document.forms[0],'"+ebwActn+"','"+ebwState+"','"+prevUrl+"','"+prevActn+"','"+prevState+"','"+rowsPerPage+"','CSV','"+reorderColmns+"');\"><img align=\"absmiddle\" src=\"../images/csv.jpg\" border=\"0\" alt=\"Export all data to CSV\"></a>&nbsp;&nbsp");
				//strBChk.append("<select name=\"exportCombo\" id=\"exportCombo\" onchange=\"javascript:document.forms[0].exportPages.value=exportPageFn(document.forms[0]);\"><option>1</option><option>2</option><option>3</option><option>4</option><option>5</option><option>6</option><option>7</option><option>8</option><option>9</option></select>");
			}else{
				if(xlpdfExportEnabled.equalsIgnoreCase("excel")||xlpdfExportEnabled.equalsIgnoreCase("all"))
					strBChk.append("<a href=\"#\" onClick='javascript:callExport(document.forms[0],\"Excel\");'><img align=\"absmiddle\" src=\"../images/xlsactive.gif\" border=\"0\" class=\"exportImg\" alt=\""+ebwTableRb.getString("excel.tooltip")+"\">&nbsp;Export</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp");
				if(xlpdfExportEnabled.equalsIgnoreCase("pdf")||xlpdfExportEnabled.equalsIgnoreCase("all"))
					strBChk.append("<a href=\"#\" onClick='javascript:callExport(document.forms[0],\"PDF\");'><img align=\"absmiddle\" src=\"../images/pdfactive.gif\" border=\"0\" alt=\""+ebwTableRb.getString("pdf.tooltip")+"\"></a>&nbsp;&nbsp");
				//strBChk.append("<a href=\"#\" onClick='javascript:callExport(document.forms[0],\"CSV\");'><img align=\"absmiddle\" src=\"../images/csv.jpg\" border=\"0\" alt=\"Export all data to CSV\"></a>&nbsp;&nbsp");
			}
		}

		if(pageDisplay){
			strBChk.append(pageDispChk);
		}

		if(isPrintEnabled())
			strBChk.append("<a href=\"#\" onClick='javascript:callExport(document.forms[0],\"Printer\");'><img align=\"absmiddle\" src=\"../images/print.jpg\" border=\"0\" alt=\"Print\"></a>&nbsp;&nbsp");

		if(displaySearch=="")
			displaySearch="false";
		strBChk.append("</td></tr></table>\r\n");
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exited  method inside prev_action()");
		return strBChk.toString();
	}

	private String addCheckLinkNotPrev(int cols, String tblName, String pgnIndex, int dataRowCount,String restartKeyFromFormbean,String presentActn,String reorderColmns) {
		StringBuilder pageDispChk = new StringBuilder();
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered  inside method  notPrev_action():- tblName="+tblName+"  pgnIndex="+pgnIndex+"  restartKeyFromFormbean="+restartKeyFromFormbean+" presentActn="+presentActn);
		StringBuffer strBChk = new StringBuffer();
		String tblTitle = null;
		int paginationIndex = 0;
		boolean isMultiplePage = false;
		int rowsInPage = 0;
		tblTitle =formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + tblName);

		if(getTableLevelCSS()!=null)
			strBChk.append("<table class=\""+getTableLevelCSS()+"Bar\"><tr>");
		else
			strBChk.append("<table  class=\"layoutMaxTable\"  ><tr>");

		/** piece of code to display table title wn not null. 
		 * if some reorder or export icons there in the right corner , then width is to be calculated for the tbltitle */
		int colSpan=0;
		if(!reorderingImg){			
			colSpan=cols+3;
		}		
		if(tblTitle!=null || (tblTitle!="" && tblTitle.length()>0)){
			if(isExportEnabled().equalsIgnoreCase("false") ){
				if(appRB.getString("Reordering").equalsIgnoreCase("No") || (reordering!=null && reordering.length()>0 && reordering.equalsIgnoreCase("No"))) {
					int len=(tblTitle.length())*10;
					strBChk.append("<td colspan=\""+colSpan+"\" class=\"tabletitle\" width=\""+len+"px\">"+tblTitle+"</td>");
				}else{
					int len=(tblTitle.length())*10;
					strBChk.append("<td colspan=\""+colSpan+"\" class=\"tabletitle\" width=\""+len+"px\">"+tblTitle+"</td>");
				}
			}else{
				strBChk.append("<td colspan=\""+colSpan+"\" class=\"tabletitle\" >"+tblTitle+"</td>");
			}
		}else
			strBChk.append("<td colspan=\""+colSpan+"\" class=\"tabletitle\" >\"Noname\"</td>");		

		if(!reorderingImg){
			strBChk.append("</tr><tr><td class=\"tabletitle\">");
		}

		if(rowsPerPage!=null && rowsPerPage.length()>0){
			rowsInPage = Integer.parseInt(rowsPerPage);
			if(rowsInPage<dataRowCount){
				isMultiplePage = true;
			}

			if(!reorderingImg){
				if(!(appRB.getString("Reordering").equalsIgnoreCase("No") || (reordering!=null && reordering.length()>0 && reordering.equalsIgnoreCase("No")))) {
					strBChk.append("<a href=\"#\" onClick=\"popupwindow=setToReorderParent(document.forms[0]);window.open(\'popup.jsp?dis="+displaySearch+"&availableCols="+availableColumns+"&visiblecols="+visibleColumns+"&presentAction="+presentActn+"&tablename="+tblName+"\','popupwindow','resizable=no,width=350,height=240,top=233,left=325,status=No')\">Reorder</a></td>");
				}
			}
			if(!pageDisplay)
				strBChk.append("<td colspan=\""+cols+"\" class=\"pageNo\">");
			if(pgnIndex !=null && pgnIndex.length()>0){
				paginationIndex = Integer.parseInt(pgnIndex);
				if(paginationIndex>1)
					pageDispChk.append("<a  onClick=\"javascript:pgnSubmit(document.forms[0],'"+actionEbwtable+"','Prev','"+restartKeyFromFormbean+"','"+paginationIndex+"','prev','"+totalRow+"');\""  + ">" + "<img align=\"absmiddle\" id=\"prevBtnId\" src=\"../images/prev.gif\" border=\"0\" alt=\"Previous\" >" +"</a>");
				if(pgnIndex!=null && pgnIndex.length()>0){
					if(pageDisplay){
						int totalPages=1;
						if(totalRowCount%rowsInPage == 0)
							totalPages = totalRowCount/rowsInPage;
						else
							totalPages = (totalRowCount/rowsInPage)+1;						
						pageDispChk.append("&nbsp;&nbsp;<input type=\"text\" id=\"pagedisplaytxt\" class=\"pagedisplay\" value=\""+paginationIndex+" of "+totalPages+"\" readonly >&nbsp;&nbsp;");						
					}else if(isMultiplePage)
						pageDispChk.append("Page : "+paginationIndex+"+");
					else
						pageDispChk.append("Page : "+paginationIndex);
				}
				if(isMultiplePage)
					pageDispChk.append("<a  onClick=\"javascript:pgnSubmit(document.forms[0],'"+actionEbwtable+"','Next','"+restartKeyFromFormbean+"','"+paginationIndex+"','next','"+totalRow+"');\""  + ">" + "<img align=\"absmiddle\" id=\"nextBtnId\" src=\"../images/next1.gif\" border=\"0\" alt=\"Next\" >" +"</a>&nbsp;&nbsp;");
			}

			if(!pageDisplay){
				strBChk.append(pageDispChk);
				strBChk.append("</td>");
			}
		}

		strBChk.append("<td colspan=\"2\" class=\"reorder\"><table style=\"cellspacing:0;cellpadding:0;border:0px\"><tr><td>");			

		if (isNestedTable) {
			strBChk.append("<a href=\"#\" onclick=\"javascript:treetable_expandAll('" + tblName + "')\"><img src=\"../images/expand.gif\" border=\"0\" alt=\"Expand\"></a>&nbsp;&nbsp;");
			strBChk.append("<a href=\"#\" onclick=\"javascript:treetable_collapseAll('" + tblName + "')\"><img src=\"../images/collapse.gif\" border=\"0\" alt=\"Collapse\"></a>&nbsp;&nbsp;");
		}
		if(reorderingImg){
			if(!(appRB.getString("Reordering").equalsIgnoreCase("No") || (reordering!=null && reordering.length()>0 && reordering.equalsIgnoreCase("No")))) {
				strBChk.append("<a href=\"#\" onClick=\"popupwindow=setToReorderParent(document.forms[0]);window.open(\'popup.jsp?dis="+displaySearch+"&availableCols="+availableColumns+"&visiblecols="+visibleColumns+"&presentAction="+presentActn+"&tablename="+tblName+"\','popupwindow','resizable=no,width=350,height=240,top=233,left=325,status=No')\"><img align=\"absmiddle\" src=\"../images/reorderactive.gif\" border=\"0\" alt=\"Reorder Columns\"></a>&nbsp");
			}
		}
		if(isExportEnabled().equalsIgnoreCase("true")){
			if(rowsPerPage!=null && rowsPerPage.length()>0){
				if(xlpdfExportEnabled.equalsIgnoreCase("excel")||xlpdfExportEnabled.equalsIgnoreCase("all"))
					strBChk.append("<a href=\"#\" onClick=\"javascript:exportSubmit(document.forms[0],'"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+((EbwForm)attrObj).getScreenName()+"','"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+rowsPerPage+"','Excel','"+reorderColmns+"');return false;\"><img align=\"absmiddle\" src=\"../images/xlsactive.gif\" border=\"0\" class=\"exportImg\" alt=\""+ebwTableRb.getString("excel.tooltip")+"\">&nbsp;Export</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp");//new
				if(xlpdfExportEnabled.equalsIgnoreCase("pdf")||xlpdfExportEnabled.equalsIgnoreCase("all"))
					strBChk.append("<a href=\"#\" onClick=\"javascript:exportSubmit(document.forms[0],'"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+((EbwForm)attrObj).getScreenName()+"','"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+rowsPerPage+"','PDF','"+reorderColmns+"');return false;\"><img align=\"absmiddle\" src=\"../images/pdfactive.gif\" border=\"0\" alt=\""+ebwTableRb.getString("pdf.tooltip")+"\"></a>&nbsp;&nbsp");//new
				/*It's old method to export...
				 * strBChk.append("<a href=\"#\" onClick='javascript:document.forms[0].prevAction.value=\""+((EbwForm)attrObj).getAction()+"\";document.forms[0].exportType.value=\"Excel\";document.forms[0].rowsPerPage.value=\""+rowsPerPage+"\";document.forms[0].state.value=\""+((EbwForm)attrObj).getState()+"\";document.forms[0].prevState.value=\""+((EbwForm)attrObj).getState()+"\";document.forms[0].actionType.value=\"Export\";document.forms[0].exportPages.value=exportPageFn(document.forms[0]);document.forms[0].reorderCols.value=\""+reorderColmns+"\";document.forms[0].submit();'><img align=\"absmiddle\" src=\"../images/xlsactive.gif\" border=\"0\" alt=\""+ebwTableRb.getString("excel.tooltip")+"\"></a>&nbsp;&nbsp");
				strBChk.append("<a href=\"#\" onClick='javascript:document.forms[0].prevAction.value=\""+((EbwForm)attrObj).getAction()+"\";document.forms[0].exportType.value=\"PDF\";document.forms[0].rowsPerPage.value=\""+rowsPerPage+"\";document.forms[0].state.value=\""+((EbwForm)attrObj).getState()+"\";document.forms[0].prevState.value=\""+((EbwForm)attrObj).getState()+"\";document.forms[0].actionType.value=\"Export\";document.forms[0].exportPages.value=exportPageFn(document.forms[0]);document.forms[0].reorderCols.value=\""+reorderColmns+"\";document.forms[0].submit();'><img align=\"absmiddle\" src=\"../images/pdfactive.gif\" border=\"0\" alt=\""+ebwTableRb.getString("pdf.tooltip")+"\"></a>&nbsp;&nbsp");*/
				/* csv is commented for RJ
				strBChk.append("<a href=\"#\" onClick='javascript:document.forms[0].prevAction.value=\""+prevActn+"\";document.forms[0].exportType.value=\"CSV\";document.forms[0].action.value=\"export\";document.forms[0].rowsPerPage.value=\""+rowsPerPage+"\";document.forms[0].state.value=\""+prevState+"\";document.forms[0].prevState.value=\""+prevState+"\";document.forms[0].actionType.value=\"Export\";document.forms[0].exportPages.value=exportPageFn(document.forms[0]);document.forms[0].submit();'><img align=\"absmiddle\" src=\"../images/csv.jpg\" border=\"0\" alt=\"Export all data to CSV\"></a>&nbsp;&nbsp");
				 */
				//strBChk.append("<select name=\"exportCombo\" id=\"exportCombo\" ><option>1</option><option>2</option><option>3</option><option>4</option><option>5</option><option>6</option><option>7</option><option>8</option><option>9</option></select>");
			}else{
				if(xlpdfExportEnabled.equalsIgnoreCase("excel")||xlpdfExportEnabled.equalsIgnoreCase("all"))
					strBChk.append("<a href=\"#\" onClick='javascript:callExport(document.forms[0],\"Excel\");'><img align=\"absmiddle\" src=\"../images/xlsactive.gif\" border=\"0\" class=\"exportImg\" alt=\""+ebwTableRb.getString("excel.tooltip")+"\">&nbsp;Export</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp");
				if(xlpdfExportEnabled.equalsIgnoreCase("pdf")||xlpdfExportEnabled.equalsIgnoreCase("all"))
					strBChk.append("<a href=\"#\" onClick='javascript:callExport(document.forms[0],\"PDF\");'><img align=\"absmiddle\" src=\"../images/pdfactive.gif\" border=\"0\" alt=\""+ebwTableRb.getString("pdf.tooltip")+"\"></a>&nbsp;&nbsp");
				//strBChk.append("<a href=\"#\" onClick='javascript:callExport(document.forms[0],\"CSV\");'><img align=\"absmiddle\" src=\"../images/csv.jpg\" border=\"0\" alt=\"Export all data to CSV\"></a>&nbsp;&nbsp");
			}
		}		
		if(pageDisplay){
			strBChk.append(pageDispChk);
		}

		if(isPrintEnabled())
			strBChk.append("<a href=\"#\" onClick='javascript:callExport(document.forms[0],\"Printer\");'><img align=\"absmiddle\" src=\"../images/print.jpg\" border=\"0\" alt=\"Print\"></a>&nbsp;&nbsp");
		if(displaySearch=="")
			displaySearch="false";		
		strBChk.append("</td></tr></table></td></tr></table>\r\n");
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exited  method  notprev_action()");
		return strBChk.toString();
	}

	public boolean isToBeChecked(String[] selectId,String chkData){
		boolean checked = false;
		if(selectId!=null && selectId.length>0){
			for(int i=0;i<selectId.length;i++){
				if((selectId[i]).equalsIgnoreCase(chkData)){
					checked = true;
					break;
				}
			}
		}
		return checked;
	}

	private String replaceWithValue(TableColAttrObj colAttrObj, String value, String rowdata,int row,int col,boolean[] defRowColComp) {
		//EBWLogger.trace(this,"Entered into replaceWithValue()(withoutcss) first method with value :"+value+"    colAttrObj :"+colAttrObj);		
		/** If the value(targetString) contains '&' or '\' then replaceAll or replaceFirst() in java.lang package
		 * will give an IndexOutOfBound Exception. The solution is before applying replaceAll() we have to apply
		 * java.util.regex.Matcher.quoteReplacement(target String) */

		if(value!=null && value.length()>0 && value!=" "){
			//value = java.util.regex.Matcher.quoteReplacement(value);
			value= forRegexSpecialCharacter(value);
		}else{
			value="";
		}
		StringBuffer strBReplace = new StringBuffer();
		String strContent;
		String editcolumns = getEditableColumns();
		String editCol[]=null;
		String columnTooltipCSSMapStr="";
		boolean tooltip=false;
		isEditColsPresent=false;
		columnTooltipCSSMapStr = (String)colTooltipCssMap.get(String.valueOf(col));
		if(columnTooltipCSSMapStr!=null && columnTooltipCSSMapStr.length()>0)
			tooltip=true;

		if(editcolumns!=null && editcolumns.length()>0){			
			editCol= editcolumns.split(",");
			if(editCol !=null && editCol.length>0)
				isEditColsPresent=true;
		}

		if(colAttrObj.getComponentType().equalsIgnoreCase("HrefHidden") || colAttrObj.getComponentType().equalsIgnoreCase("HrefHiddenEnable")){
			name = ((DataInterface) formObj).getTableName();
			if( isDefRow() && row!=0)
				strBReplace = EbwTableComponent.getHrefHidden(value,colAttrObj,row,StringUtil.initLower(name),formname,formRB);
			else if(!isDefRow())
				strBReplace = EbwTableComponent.getHrefHidden(value,colAttrObj,row,StringUtil.initLower(name),formname,formRB);
		}else if(colAttrObj.getComponentType().equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_DATE)){
			strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", colAttrObj.getColName()+"_"+row));
		}else{
			if(row==0 && isDefRow() && isEditColsPresent && !defRowColComp[col]){
				if(getColumnPresent(editCol,col)) {
					strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ""));	 					
					if(colAttrObj.getComponentType().equalsIgnoreCase("TextField")) {						
						name = ((DataInterface) formObj).getTableName();					
						String tag = strBReplace.toString().substring(strBReplace.toString().indexOf('<'),strBReplace.toString().indexOf('>'));
						String tag1=" onchange=\"javascript:fillText(document,'"+colAttrObj.getColName()+"','"+StringUtil.initLower(name)+ "SelectId');\"";
						strBReplace.delete(0,strBReplace.toString().length());
						strBReplace.append(tag);
						strBReplace.append(tag1+'>');
					}
				}
			}else if(row==0 && isDefRow() && defRowColComp[col]){				
				strBReplace.append(getDefComponent(col,colAttrObj.getColName()));			
			}else {			
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
							String val=strContent.replaceAll("~VARIABLE", value);
							if(tooltip)
								strBReplace.append(getToolTipContent(val,columnTooltipCSSMapStr));
							else
								strBReplace.append(strContent.replaceAll("~VARIABLE", value));
						}
					}else if (colAttrObj.getComponentType().equals(CommonConstants.COMPONENT_TYPE_CHECKBOX) && row==0 && isDefRow()) {
					}else  {
						if (value == null) {
							strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ""));	                
						} else {													
							if(!isHtmlOutput){
								/*if(colAttrObj.getDataType().equalsIgnoreCase("Double") || colAttrObj.getDataType().equalsIgnoreCase("BigDecimal")){
								EBWLogger.logDebug(this,"data BigDecimal issue in ebwtable :");
								try {
									char sep=ebwTableRb.getString("seperator").charAt(0);
									char cDec=ebwTableRb.getString("cDecimal").charAt(0);
									strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.ConvertToUserNumberFormat(value,Integer.parseInt(ebwTableRb.getString("decFormat")),sep,cDec,Integer.parseInt(ebwTableRb.getString("inFormat")))));
								}catch(NumberFormatException e){
									 EBWLogger.logDebug(this,"NumberFormatException :" + e);
								}catch(Exception e){
									EBWLogger.logDebug(this,"ResourceBudle key not found :" + e);
								}

							}*/

								if(colAttrObj.getDataType().equalsIgnoreCase("SignedCurrencyAmount") || colAttrObj.getDataType().equalsIgnoreCase("CurrencyAmount") || colAttrObj.getDataType().equalsIgnoreCase("Double") || colAttrObj.getDataType().equalsIgnoreCase("BigDecimal") || colAttrObj.getDataType().equalsIgnoreCase("Amount") || colAttrObj.getDataType().equalsIgnoreCase("SignedAmount") || colAttrObj.getDataType().equalsIgnoreCase("SignedQuantity"))
								{
									EBWLogger.logDebug(this, (new StringBuilder("Data Type of Column :")).append(colAttrObj.getDataType()).toString());
									EBWLogger.logDebug(this, (new StringBuilder(" Column :")).append(colAttrObj.getColName()).toString());
									String dataType = colAttrObj.getDataType();
									strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToReqCol(value, dataType)));
								} else 
									//}
									/* else if(colAttrObj.getDataType().equalsIgnoreCase("DateTime")){*/
									if(colAttrObj.getDataType().equalsIgnoreCase("DateTime")){
										EBWLogger.logDebug(this,"data time date issue in ebwtable :");
										if(value.length()>0 && value!=null){
											/*if(value.indexOf(" ")>-1)
										strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToDBDateTimeStr(value.substring(0,value.indexOf(" ")))));
									else*/
											strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToAppDateTimeStr(value)));
										}
									}else if(colAttrObj.getDataType().equalsIgnoreCase("Date")){
										EBWLogger.logDebug(this,"data  date issue in ebwtable :");
										if(value.length()>0 && value!=null){
											if(value.indexOf(" ")>-1)
												strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToAppDateStr(value.substring(0,value.indexOf(" ")))));
											else
												strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToAppDateStr(value)));
										}
									}else{
										if(tooltip)
											strBReplace.append(getToolTipContent(colAttrObj.getTagContent().replaceAll("~VARIABLE", value),columnTooltipCSSMapStr));
										else{
											strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", value));
											if((colAttrObj.getComponentType().equalsIgnoreCase("TextField"))||(colAttrObj.getComponentType().equalsIgnoreCase("Button"))||(colAttrObj.getComponentType().equalsIgnoreCase("JSButton"))) {	//TPD_MALLI					
												name = ((DataInterface) formObj).getTableName();
												String tag1 = "";
												String tag = strBReplace.toString().substring(strBReplace.toString().indexOf('<'),strBReplace.toString().indexOf('>'));
												if(colAttrObj.getComponentType().equalsIgnoreCase("TextField"))										
													tag1=" onchange=\"javascript:fillText(document,'"+colAttrObj.getColName()+"','"+StringUtil.initLower(name)+ "_"+row+"');\"";
												else
													tag1=" onclick=\"javascript:fillBtn('"+StringUtil.initLower(name)+ "_"+row+"');\"";
												strBReplace.delete(0,strBReplace.toString().length());
												strBReplace.append(tag);
												strBReplace.append(tag1+'>');										
											}
										}
									}

							}else
								if(colAttrObj.getDataType().equalsIgnoreCase("DateTime")){
									EBWLogger.logDebug(this,"data time date issue in ebwtable :");
									if(value.length()>0 && value!=null){
										/*if(value.indexOf(" ")>-1)
										strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToDBDateTimeStr(value.substring(0,value.indexOf(" ")))));
									else*/
										strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToAppDateTimeStr(value)));
									}
								}else if(colAttrObj.getDataType().equalsIgnoreCase("Date")){
									EBWLogger.logDebug(this,"data  date issue in ebwtable :");
									if(value.length()>0 && value!=null){
										if(value.indexOf(" ")>-1)
											strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToAppDateStr(value.substring(0,value.indexOf(" ")))));
										else
											strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToAppDateStr(value)));
									}
								}else{
									if(tooltip)
										strBReplace.append(getToolTipContent(colAttrObj.getTagContent().replaceAll("~VARIABLE", value),columnTooltipCSSMapStr));
									else
										strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", value));
								}	
						}
					}
				}
			}
		}
		return strBReplace.toString();
	}

	private String getToolTipContent(String val,String columnTooltipCSSMapStr){
		StringBuffer strBf = new StringBuffer();
		if (val.length()>9){
			strBf.append("<table class=\""+columnTooltipCSSMapStr+"\"><tr><td><div title=\""+val+"\">"+val+"</div>");
			strBf.append("</td><td>...</td></tr></table>");
		}else
		{
			strBf.append("<table class=\""+columnTooltipCSSMapStr+"\"><tr><td><div>"+val+"</div>");
			strBf.append("</td></tr></table>");	
		}
		return strBf.toString();
	}

	/** generates the tag content to be dumped for the external comoponent in the default row. */
	private String getDefComponent(int colNo,String colName){
		String result = "";
		String component = null;
		String onclickStr = null;
		String defCompStr = getDefRowComp();
		if(defCompStr !=null && defCompStr.length()>0){
			String[] defcols = defCompStr.split("~");
			for(int i=0;i<defcols.length;i++){
				String eachCol = defcols[i];
				String[] eachColDef = eachCol.split(":");
				String colStr = colNo+"";
				if(eachColDef !=null && eachColDef.length>0){
					if(eachColDef[0].indexOf(colStr)>-1){
						component  = eachColDef[1];	
						onclickStr =  eachColDef[2];
					}
				}				
			}
		}
		colName = "DEF"+colName;
		if(component !=null && onclickStr!=null)
			result = "<input type=\""+component+"\" name=\""+colName+"\" id=\""+colName+"\"  onclick=\"javascript:"+onclickStr+"\">";
		return result;
	}

	private String replaceWithValue(TableColAttrObj colAttrObj, String value, String rowdata, String cssName,int row,int col,boolean[] defRowColComp) {
		//EBWLogger.trace(this,"inside replace with value with css :-"+value+"  colAttrObj:"+colAttrObj);
		/* If the value(targetString) contains '&' or '\' then replaceAll or replaceFirst() in 
		 * java.lang package will give an IndexOutOfBound Exception. The solution is before applying replaceAll() we have to apply
		 * java.util.regex.Matcher.quoteReplacement(target String)*/
		if(value!=null && value.length()>0 && value!=" "){
			//value = java.util.regex.Matcher.quoteReplacement(value);
			value= forRegexSpecialCharacter(value);
		}else{
			value="";
		}		
		StringBuffer strBReplace = new StringBuffer();
		String strContent;		
		String columnTooltipCSSMapStr="";
		boolean tooltip=false;
		columnTooltipCSSMapStr = (String)colTooltipCssMap.get(String.valueOf(col));		
		if(columnTooltipCSSMapStr!=null && columnTooltipCSSMapStr.length()>0)
			tooltip=true;		
		if (colAttrObj != null && colAttrObj.getTagContent() != null) {
			if(colAttrObj.getComponentType().equalsIgnoreCase("HrefHidden") || colAttrObj.getComponentType().equalsIgnoreCase("HrefHiddenEnable")){
				name = ((DataInterface) formObj).getTableName();
				if( isDefRow() && row!=0)
					strBReplace = EbwTableComponent.getHrefHidden(value,colAttrObj,row,StringUtil.initLower(name),formname,formRB);
				else if(!isDefRow())
					strBReplace = EbwTableComponent.getHrefHidden(value,colAttrObj,row,StringUtil.initLower(name),formname,formRB);
			}else if (colAttrObj.getComponentType().equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_LABEL)) {
				String tagvalue = colAttrObj.getTagContent();			       
				if(cssName!=null && tagvalue.indexOf("label")>-1){

					tagvalue = tagvalue.substring(0,tagvalue.indexOf("label"))+" class=\""+cssName+"\" "+
					tagvalue.substring(tagvalue.indexOf("label >"),tagvalue.length());
				}
				if(tooltip)
					strBReplace.append(getToolTipContent(tagvalue,columnTooltipCSSMapStr));
				else
					strBReplace.append(tagvalue);					
			} else if (colAttrObj.getComponentType().equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_HREF)) {
				strContent = colAttrObj.getTagContent();
				if (value == null) {
					strBReplace.append(strContent.replaceAll("~VARIABLE", ""));	                
				} else {
					if (strContent.indexOf("\"#\"") == -1)  
						strContent = strContent.replaceFirst(".do\"", ".do" + "?tablerowdata=" + rowdata + "\"");
					strBReplace.append(strContent.replaceAll("~VARIABLE", value));
				}
			} else if(colAttrObj.getComponentType().equalsIgnoreCase(CommonConstants.COMPONENT_TYPE_DATE)){
				strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", colAttrObj.getColName()+"_"+row));
			}else {
				if (value == null) {
					strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ""));	                
				}else {
					if(tooltip){
						strBReplace.append(getToolTipContent(colAttrObj.getTagContent().replaceAll("~VARIABLE", value),columnTooltipCSSMapStr));
					}else if((colAttrObj.getComponentType().equalsIgnoreCase("TextField"))||(colAttrObj.getComponentType().equalsIgnoreCase("Button"))||(colAttrObj.getComponentType().equalsIgnoreCase("JSButton"))) {	//TPD_MALLI					
						strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", value));	
						name = ((DataInterface) formObj).getTableName();
						String tag1 = "";
						String tag = strBReplace.toString().substring(strBReplace.toString().indexOf('<'),strBReplace.toString().indexOf('>'));
						if(colAttrObj.getComponentType().equalsIgnoreCase("TextField"))										
							tag1=" onchange=\"javascript:fillText(document,'"+colAttrObj.getColName()+"','"+StringUtil.initLower(name)+ "_"+row+"');\"";
						else
							tag1=" onclick=\"javascript:fillBtn('"+StringUtil.initLower(name)+ "_"+row+"');\"";
						strBReplace.delete(0,strBReplace.toString().length());
						strBReplace.append(tag);
						strBReplace.append(tag1+'>');										

					}else  {

						if(!isHtmlOutput){

							if(colAttrObj.getDataType().equalsIgnoreCase("SignedCurrencyAmount") || colAttrObj.getDataType().equalsIgnoreCase("CurrencyAmount") || colAttrObj.getDataType().equalsIgnoreCase("Double") || colAttrObj.getDataType().equalsIgnoreCase("BigDecimal") || colAttrObj.getDataType().equalsIgnoreCase("Amount") || colAttrObj.getDataType().equalsIgnoreCase("SignedAmount") || colAttrObj.getDataType().equalsIgnoreCase("SignedQuantity"))
							{
								if(EBWLogger.isInfoEnabled())
									EBWLogger.logDebug(this, (new StringBuilder("Data Type of Column :")).append(colAttrObj.getDataType()).toString());
								if(EBWLogger.isInfoEnabled())
									EBWLogger.logDebug(this, (new StringBuilder(" Column :")).append(colAttrObj.getColName()).toString());
								String dataType = colAttrObj.getDataType();
								strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToReqCol(value, dataType)));
							} else 
								//}
								/* else if(colAttrObj.getDataType().equalsIgnoreCase("DateTime")){*/
								if(colAttrObj.getDataType().equalsIgnoreCase("DateTime")){
									if(EBWLogger.isInfoEnabled())
										EBWLogger.logDebug(this,"data time date issue in ebwtable :");
									if(value.length()>0 && value!=null){
										/*if(value.indexOf(" ")>-1)
												strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToDBDateTimeStr(value.substring(0,value.indexOf(" ")))));
											else*/
										strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToAppDateTimeStr(value)));
									}
								}else if(colAttrObj.getDataType().equalsIgnoreCase("Date")){
									if(EBWLogger.isInfoEnabled())
										EBWLogger.logDebug(this,"data  date issue in ebwtable :");
									if(value.length()>0 && value!=null){
										if(value.indexOf(" ")>-1)
											strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToAppDateStr(value.substring(0,value.indexOf(" ")))));
										else
											strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToAppDateStr(value)));
									}
								}else{
									if(tooltip)
										strBReplace.append(getToolTipContent(colAttrObj.getTagContent().replaceAll("~VARIABLE", value),columnTooltipCSSMapStr));
									else{
										strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", value));
										if((colAttrObj.getComponentType().equalsIgnoreCase("TextField"))||(colAttrObj.getComponentType().equalsIgnoreCase("Button"))||(colAttrObj.getComponentType().equalsIgnoreCase("JSButton"))) {	//TPD_MALLI					
											name = ((DataInterface) formObj).getTableName();
											String tag1 = "";
											String tag = strBReplace.toString().substring(strBReplace.toString().indexOf('<'),strBReplace.toString().indexOf('>'));
											if(colAttrObj.getComponentType().equalsIgnoreCase("TextField"))										
												tag1=" onchange=\"javascript:fillText(document,'"+colAttrObj.getColName()+"','"+StringUtil.initLower(name)+ "_"+row+"');\"";
											else
												tag1=" onclick=\"javascript:fillBtn('"+StringUtil.initLower(name)+ "_"+row+"');\"";
											strBReplace.delete(0,strBReplace.toString().length());
											strBReplace.append(tag);
											strBReplace.append(tag1+'>');
											//strBReplace.append(tag1+" "+enableDisableVal+""+'>');	
										}
									}
								}

						}else
							if(colAttrObj.getDataType().equalsIgnoreCase("DateTime")){
								if(EBWLogger.isInfoEnabled())
									EBWLogger.logDebug(this,"data time date issue in ebwtable :");
								if(value.length()>0 && value!=null){
									/*if(value.indexOf(" ")>-1)
												strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToDBDateTimeStr(value.substring(0,value.indexOf(" ")))));
											else*/
									strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToAppDateTimeStr(value)));
								}
							}else if(colAttrObj.getDataType().equalsIgnoreCase("Date")){
								if(EBWLogger.isInfoEnabled())
									EBWLogger.logDebug(this,"data  date issue in ebwtable :");
								if(value.length()>0 && value!=null){
									if(value.indexOf(" ")>-1)
										strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToAppDateStr(value.substring(0,value.indexOf(" ")))));
									else
										strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", ConvertionUtil.convertToAppDateStr(value)));
								}
							}else{
								if(tooltip)
									strBReplace.append(getToolTipContent(colAttrObj.getTagContent().replaceAll("~VARIABLE", value),columnTooltipCSSMapStr));
								else
									strBReplace.append(colAttrObj.getTagContent().replaceAll("~VARIABLE", value));
							}	
						//}
					}
				}
			}			
		}
		return strBReplace.toString();
	}	

	private String getBundleString(String key, String value, String displayLabel) {
		String strMsg = value;
		try {
			if(key!= null && key.indexOf("NoDataFoundMsg")>-1){
				strMsg = formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + name + "." + "NoDataFoundMsg");
			}else if (isHtmlOutput && (isAjax==null || (isAjax!=null && !isAjax.equalsIgnoreCase("yes")))) {
				if (displayLabel!=null && displayLabel.length() > 0 && !displayLabel.equals("~VARIABLE")) 
					strMsg = displayLabel;
			} else {
				if (formRB!=null && key != null) 
					strMsg = formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + name + "." + key);
				else if(formRB!=null) 
					strMsg = formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + name);
			}
		} catch (Exception exc) {		    
			if(isColAttrPresent && (key.indexOf("NoDataFoundMsg") == -1))
				strMsg="";
			EBWLogger.logDebug(this,"Resource bundle value not found for " + key);
		}
		return strMsg;
	}	

	/**
	 * This Method is used for generating Nested Table. It gets each row as a parameter
	 * and creates HashMap with column heading & data of parent row. This HashMap is passed
	 * as a parameter to the inner/nested table query.
	 * Nested Query Service is available as an attribute. From there, it takes the service
	 * and passes the Hashmap parameter. It returns an ArrayList. Nested table is formed
	 * using this arraylist.
	 * @param colHeadings 		Actual table column headings (configured headings)
	 * @param rowData			Parent Row data - this row data is passed as a where condition
	 * @param initLowerName		Actual Table Name
	 * @param rowNum			Current Row number
	 * @param cols				Number of columns available in the parent row.
	 * @return
	 */
	private StringBuilder getNestedTable(LinkedHashMap colHeadings, ArrayList rowData, String initLowerName, int rowNum, int cols) {
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into getNestedTable()");
		StringBuilder strBInnerTable = new StringBuilder();
		/** NestedTable Starts here   */
		if (nestedTableService!=null && nestedTableService.length() > 0 && isNestedTable) {
			LinkedHashMap columnAndData = new LinkedHashMap();
			columnAndData.putAll(colHeadings);
			columnAndData.remove("searchtitle");
			columnAndData.remove("tabletitle");		    
			Iterator columnIterator = columnAndData.keySet().iterator();
			int colPosInRow=0;
			while (columnIterator.hasNext()) {
				columnAndData.put((String) columnIterator.next(), rowData.get(colPosInRow++));
			}
			try {
				//Service Call.....
				Object objParams[]={columnAndData,new Boolean(false)};
				Class clsParamTypes[]={Object.class,Boolean.class};
				IEBWService objService = EBWServiceFactory.create(nestedTableService);
				java.util.ArrayList objInnerTable= ((java.util.ArrayList) objService.execute(clsParamTypes, objParams));
				/*java.util.List a=new ArrayList();
				a.add("Head1");a.add("Head2");a.add("Head3");a.add("Head4");a.add("Head1");a.add("Head2");a.add("Head3");a.add("Head4");a.add("Head1");a.add("Head2");a.add("Head3");a.add("Head4");
				java.util.List b=new ArrayList();
				b.add("Data1");b.add("Data2");b.add("Data3");b.add("Data4");b.add("Data1");b.add("Data2");b.add("Data3");b.add("Data4");b.add("Data1");b.add("Data2");b.add("Data3");b.add("Data4");
				java.util.ArrayList objInnerTable=new ArrayList();
				objInnerTable.add(a);objInnerTable.add(b);objInnerTable.add(b);
				System.out.println("objInnerTable**"+objInnerTable);*/		

				if (objInnerTable!=null && objInnerTable.size() > 1) {
					String strTD="";
					strBInnerTable.append ("<tr id=\"" + initLowerName + "_" + rowNum + "_" + "0\" >");
					strBInnerTable.append ("<td colspan=" + cols + "> <table class=\"InnerTable\">");
					String strData = "", align="";
					for (int innerCnt=0, innerRowCnt=objInnerTable.size();  innerCnt < innerRowCnt; innerCnt++) {
						ArrayList colArr = (ArrayList) objInnerTable.get(innerCnt);
						strBInnerTable.append ("<tr>");
						for (int iCol=0, inColCnt=colArr.size(); iCol < inColCnt; iCol++) {
							strData = convertToStr(colArr.get(iCol));						    
							strTD=(innerCnt==0)?"TH":"TD";
							if (strTD.equals("TH")) {
								strBInnerTable.append ("<" + strTD + DEFAULT_NESTED_INNERTABLEHEADER_CSS+">");
							} else {
								align="";
								try {
									if (!Double.isNaN(Double.parseDouble(strData))) 
										align=" align=\"right\"";
								} catch (Exception exc) {}
								strBInnerTable.append ("<" + strTD + " class=\"InnerTableData\"" + align + ">");
							}
							strBInnerTable.append (strData+"</" + strTD + ">");
						}
						strBInnerTable.append ("</tr>");
					}
					strBInnerTable.append ("</table></td></tr>");
				}
			} catch (Exception ex) {
				EBWLogger.logDebug(this,"Error : " + ex.getMessage());
			}
		} // Nested Service Name & Nested Table checking IF
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exited from getNestedTable()");
		return strBInnerTable;
		/** NestedTable ends here */	    
	}	

	private boolean getColumnPresent(String[] colNo, int column) {
		boolean state = false;
		for(int i=0;i<colNo.length;i++) {			
			if(colNo[i].equalsIgnoreCase(""+column)) 
				state = true;
		}		
		return state;
	}
	public String getScrollProperties() {
		return scrollProperties;
	}
	public void setScrollProperties(String scrollProperties) {
		this.scrollProperties = scrollProperties;
	}
	public String getRowsPerPage() {
		return rowsPerPage;
	}
	public void setRowsPerPage(String rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	public String getRowsPerLine() {
		return rowsPerLine;
	}
	public void setRowsPerLine(String rowsPerLine) {
		this.rowsPerLine = rowsPerLine;
	}
	public String getNoDataFoundMsg() {
		return noDataFoundMsg;
	}
	public void setNoDataFoundMsg(String noDataFoundMsg) {
		this.noDataFoundMsg = noDataFoundMsg;
	}
	public String getSorting() {
		return sorting;
	}
	public void setSorting(String sorting) {
		this.sorting = sorting;
	}
	public String getReordering() {
		return reordering;
	}
	public void setReordering(String reordering) {
		this.reordering = reordering;
	}
	public String getNestedTableService() {
		return nestedTableService;
	}
	public void setNestedTableService(String nestedTableService) {
		this.nestedTableService = nestedTableService;
	}
	public void release() {
		super.release();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBorder() {
		return border;
	}
	public void setBorder(String border) {
		this.border = border;
	}
	public String getDisplayType() {
		return displayType;
	}
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	public boolean isDoubleBorder() {
		return doubleBorder;
	}
	public void setDoubleBorder(boolean doubleBorder) {
		this.doubleBorder = doubleBorder;
	}
	public String getDisplaySearch() {
		return displaySearch;
	}
	public void setDisplaySearch(String displaySearch) {
		this.displaySearch = displaySearch;
	}
	public String getFormname() {
		return formname;
	}
	public void setFormname(String formname) {
		this.formname = formname;
	}
	public boolean isHtmlOutput() {
		return isHtmlOutput;
	}
	public void setHtmlOutput(boolean isHtmlOutput) {
		this.isHtmlOutput = isHtmlOutput;
	}
	public StringBuffer getOutputBuff() {
		return outputBuff;
	}
	public void setOutputBuff(StringBuffer outputBuff) {
		this.outputBuff = outputBuff;
	}
	public String getAccesskey() {
		return accesskey;
	}
	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}
	public boolean isDefRow() {
		return defRow;
	}
	public void setDefRow(boolean defRow) {
		this.defRow = defRow;
	}
	public boolean isReorderingImg() {
		return reorderingImg;
	}
	public void setReorderingImg(boolean reorderingImg) {
		this.reorderingImg = reorderingImg;
	}
	public String getEditableColumns() {
		return editableColumns;
	}
	public void setEditableColumns(String editableColumns) {
		this.editableColumns = editableColumns;
	}

	public static void main(String[] args) {
		/*
		try {
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
			ArrayList colAttrObjs = new ArrayList();
			colAttrObjs.add(colAttrObj1);
			colAttrObjs.add(colAttrObj2);
			excelform.setColAttrObjs(colAttrObjs);
			EbwTable ebw = new EbwTable(excelform);
			ebw.setHtmlOutput(true);
			ebw.doStartTag();
			ebw.doEndTag();
		} catch (Exception exp) {
			exp.printStackTrace();
		}*/		
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	} 
	public String getNestedTable() {
		return nestedTable;
	}
	public void setNestedTable(String nestedTable) {
		if (nestedTable!=null && nestedTable.equalsIgnoreCase("true"))
			isNestedTable = true;
		this.nestedTable = nestedTable;
	}
	public String getColLevelCSS() {
		return colLevelCSS;
	}
	public void setColLevelCSS(String colLevelCSS) {
		this.colLevelCSS = colLevelCSS;
	}
	public String getTableLevelCSS() {
		return tableLevelCSS;
	}
	public void setTableLevelCSS(String tableLevelCSS) {
		this.tableLevelCSS = tableLevelCSS;
	}

	private LinkedHashMap getColumnCSSMap(int titles,int colNamesLength){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into  getColumnCSSMap()");
		LinkedHashMap colCssMap = new LinkedHashMap();
		String columnCSS=null;    	        
		if(getColLevelCSS()!=null){
			if(getColLevelCSS().indexOf("all")!=-1){
				String csss[] = getColLevelCSS().split("=");
				columnCSS=csss[1];
				for (int j = titles; j < colNamesLength; j++) {
					String colNo = (j-titles)+"";
					colCssMap.put(colNo, columnCSS);
				}
			}else{
				String csss[] = getColLevelCSS().split(",");
				for(int i=0;i<csss.length;i++){
					StringTokenizer tokens = new StringTokenizer(csss[i],"=");
					String key="",value="";
					while(tokens.hasMoreTokens()){
						key = tokens.nextToken();
						value = tokens.nextToken();
						colCssMap.put(key,value);
					}
				}
				for (int z = titles; z < colNamesLength; z++) {
					String colNo = (z-titles)+"";
					String val = "";
					if(!colCssMap.containsKey(colNo))
						colCssMap.put(colNo, val);
				}
			}
		}else{
			String colVal = "";
			for (int j = titles; j < colNamesLength; j++) {
				String colNo = (j-titles)+"";
				colCssMap.put(colNo, colVal);
			}
		}
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exited from  getColumnCSSMap():- output="+colCssMap);
		return colCssMap;
	}

	private LinkedHashMap getColumnTooltipCSSMap(int titles,int colNamesLength){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into  getColumnCSSMap()");
		String columnCSS=null; 
		if(getToolTipCol()!=null){
			if(getToolTipCol().indexOf("all")!=-1){
				String csss[] = getToolTipCol().split("=");
				columnCSS=csss[1];
				for (int j = titles; j < colNamesLength; j++) {
					String colNo = (j-titles)+"";
					colTooltipCssMap.put(colNo, columnCSS);
				}
			}else{
				String csss[] = getToolTipCol().split(",");
				for(int i=0;i<csss.length;i++){
					StringTokenizer tokens = new StringTokenizer(csss[i],"=");
					String key="",value="";
					while(tokens.hasMoreTokens()){
						key = tokens.nextToken();
						value = tokens.nextToken();
						colTooltipCssMap.put(key,value);
					}
				}
				for (int z = titles; z < colNamesLength; z++) {
					String colNo = (z-titles)+"";
					String val = "";
					if(!colTooltipCssMap.containsKey(colNo))
						colTooltipCssMap.put(colNo, val);
				}
			}
		}else{
			String colVal = "";
			for (int j = titles; j < colNamesLength; j++) {
				String colNo = (j-titles)+"";
				colTooltipCssMap.put(colNo, colVal);
			}
		}
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exited from  getColumnTooltipCSSMap():- output="+colTooltipCssMap);
		return colTooltipCssMap;
	}   

	private LinkedHashMap getDataTypeMap(ArrayList colAttr){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into getDataTypeMap()");
		LinkedHashMap dataTypeMap = new LinkedHashMap();
		for(int i=0;i<colAttr.size();i++){
			TableColAttrObj tableColAttrObj = (TableColAttrObj)colAttr.get(i);
			String dataType = tableColAttrObj.getDataType();
			dataTypeMap.put(Integer.toString(i), dataType);
		}
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exited from getDataTypeMap():- output datatype map="+dataTypeMap);
		return dataTypeMap;    	
	}

	private void addScrollProperty(String initLowerName,int cols, String strDataTableLevelCSS,boolean defRowBool){
		if (scrollable != null && scrollable.equalsIgnoreCase("true")) 
			outputBuff.append("<td width=\"15\">&nbsp;</td>CLOSE_TR_WITHNEWLINE");
		if (scrollable != null && scrollable.equalsIgnoreCase("true")) {
			outputBuff.append(CLOSE_TABLE_WITHNEWLINE);
			if (scrollProperties == null || scrollProperties.length() == 0) 
				scrollProperties = SCROLL_PROPERTIES;
			outputBuff.append("<DIV style=\"VISIBILITY: visible; OVERFLOW: auto; " + scrollProperties +  "\">");
			int dispCols=cols;
			if(displayType!=null)
				dispCols=cols+1;			
			outputBuff.append("<table name=\""+initLowerName+"\"  id="+initLowerName+"\" cols="+dispCols+"  "+strDataTableLevelCSS+" >\r\n");
			outputBuff.append("<tr>");
			for (int k=0; k < cols; k++)
				outputBuff.append("<td></td>");
			outputBuff.append("</tr>"); 			
		}
	}

	private void addSearchRow(int cols){
		if ((displaySearch != null) &&	displaySearch.equalsIgnoreCase("true") && (displaySearch.length() > 0)) {
			outputBuff.append("<tr>");
			for (int k=0; k < cols; k++)
				outputBuff.append("<td></td>");
			outputBuff.append("</tr>"); 
		}
	}

	private void createSearchPanel(Object obj,String initLowerName,Iterator iterator){
		outputBuff.append("<tr class=tableheadsearch>");
		if (getDisplayType() != null) 
			outputBuff.append("<td align=\"center\"><input name=\"searchSubmit\" type=\"image\" src=\"../images/search.gif\"></td>");
		String strObj = "";		
		StringBuffer searchCondition = new StringBuffer();
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
						if (getDisplayType() != null) 
							strSearch.append("<td><input type=\"text\" style=\"width:100%\" name=\"search" + strObj + "\"/></td>");
						else 
							strSearch.append("&nbsp;<input type=\"text\" style=\"width:90%\" name=\"search" + strObj + "\"/></td>");
					} else 
						strSearch.append("<td><input type=\"text\" style=\"width:100%\" name=\"search" + strObj + "\"/></td>");
				}
			}
		}
		if ((displaySearch != null) &&	displaySearch.equalsIgnoreCase("true") && (displaySearch.length() > 0)) 
			outputBuff.append("<td align=\"center\"><input name=\"" + initLowerName + "Search\" type=\"image\" src=\"../images/search.gif\" " + searchCondition.toString() + ">");
		outputBuff.append(strSearch+"</tr><tr>\r\n");
	}    

	public boolean isHeaderSortCol(int colIndex){
		boolean colToBeSort = false;
		if(headerSortCol!=null && headerSortCol.length()>0){
			String[] headerSortColumn = headerSortCol.split(",");	    	
			for(int i = 0;i<headerSortColumn.length;i++){
				if(colIndex == Integer.parseInt(headerSortColumn[i]))
					colToBeSort = true;
			}
		}
		return colToBeSort ;
	}

	public boolean isDefaultSortCol(int colIndex){
		boolean colToBeSort = false;
		if(defaultSortedCol!=null && defaultSortedCol.length()>0){
			String[] defaultSortColumn = defaultSortedCol.split(",");	    	
			for(int i = 0;i<defaultSortColumn.length;i++){
				String col = defaultSortColumn[i].substring(0,defaultSortColumn[i].indexOf("="));
				System.out.println("col=="+col+" colIndex=="+colIndex);
				if(colIndex == Integer.parseInt(col))
					colToBeSort = true;
			}
		}
		return colToBeSort ;
	}

	public String getDefaultSortedCol() {
		return defaultSortedCol;
	}
	public void setDefaultSortedCol(String defaultSortedCol) {
		this.defaultSortedCol = defaultSortedCol;
	}
	private String forRegexSpecialCharacter(String val){
		StringBuffer result = new StringBuffer();

		final StringCharacterIterator iterator = new StringCharacterIterator(val);
		char character =  iterator.current();

		while (character != CharacterIterator.DONE ){
			//boolean flag=false;
			/* All literals need to have backslashes doubled.*/

			if (character == '\\' || character == '$') {//By avoiding StringOutOfBound exception we are useing regx pattern
				result.append("\\");
			}

			//the char is not a special one
			//add it to the result as is
			/*  if(character == '<' || character == '>'){
    			int code = character;
    			result.append("&#"+code+";");
    			flag=true;
	        }
	        if(!flag)
	        	result.append(character);*/	     
			result.append(character);
			character = iterator.next();
		}
		return result.toString();	
	}


	public String getHrefCols() {
		return hrefCols;
	}
	public void setHrefCols(String hrefCols) {
		this.hrefCols = hrefCols;
	}
	public String getHeaderSortCol() {
		return headerSortCol;
	}
	public void setHeaderSortCol(String headerSortCol) {
		this.headerSortCol = headerSortCol;
	}
	public boolean isPrintEnabled() {
		return printEnabled;
	}
	public void setPrintEnabled(boolean printEnabled) {
		this.printEnabled = printEnabled;
	}
	public String isExportEnabled() {
		return exportEnabled;
	}
	public void setExportEnabled(String exportEnabled) {
		if(exportEnabled.contains(",")){
			String[] tt= exportEnabled.split(",");
			this.exportEnabled = tt[0];
			this.xlpdfExportEnabled = tt[1];
		}else{
			this.exportEnabled = exportEnabled;
			this.xlpdfExportEnabled = "all";
		}		
	}
	public boolean isRowHighLight() {
		return rowHighLight;
	}
	public void setRowHighLight(boolean rowHighLight) {
		this.rowHighLight = rowHighLight;
	}

	public boolean isGrouping() {
		return grouping;
	}
	public void setGrouping(boolean grouping) {
		this.grouping = grouping;
	}

	public boolean isMultiLang() {
		return multiLang;
	}
	public void setMultiLang(boolean multiLang) {
		this.multiLang = multiLang;
	}	
	public boolean isTableSplit() {
		return tableSplit;
	}
	public void setTableSplit(boolean tableSplit) {
		this.tableSplit = tableSplit;
	}	
	public boolean isPageDisplay() {
		return pageDisplay;
	}
	public void setPageDisplay(boolean pageDisplay) {
		this.pageDisplay = pageDisplay;
	}	
	public String getDefRowComp() {
		return defRowComp;
	}
	public void setDefRowComp(String defRowComp) {
		this.defRowComp = defRowComp;
	}
	public boolean isRowIdVal() {
		return rowIdVal;
	}
	public void setRowIdVal(boolean rowIdVal) {
		this.rowIdVal = rowIdVal;
	}
	public String getHiddenCols() {
		return hiddenCols;
	}
	public void setHiddenCols(String hiddenCols) {
		this.hiddenCols = hiddenCols;
	}
	public String getIsAjax() {
		return isAjax;
	}
	public void setIsAjax(String isAjax) {
		this.isAjax = isAjax;
	}
	public Object getAttrObj() {
		return attrObj;
	}
	public void setAttrObj(Object attrObj) {
		this.attrObj = attrObj;
	}
	/*
	 * This methods invokes doStartTag of EbwTable.java  handler by setting all the tag attributes 
	 * before invoking the method.
	 * The attributes and its values are passed as arguments to this method.
	 * attributeMap is the hashmap whose key is tag attribute name and value is attribute value.
	 * sessionObject is the httpsession object used to get usersessionObject inside EbwTable.
	 * formObj is the EbwForm Object which has got the table collection to be displayed.
	 * The return type is a StringBuffer which contains the TABLE HTML TAGS  to be dumped to the screen.
	 *NOTE: Newly added tag attributes must have ebwtable.set of the attribute here is importanat for ajax call.*/	
	public StringBuffer getTableThruAjax(HashMap attributeMap,HttpSession sessionObject,Object formObj){
		System.out.println("inside ajax call method in ebwtable");
		StringBuffer resultBuffer = new StringBuffer();
		EbwTable ebwtable = new EbwTable();
		if(attributeMap!=null ){
			ebwtable.setName((String)attributeMap.get("name"));
			ebwtable.setBorder((String)attributeMap.get("border"));
			ebwtable.setDisplayType((String)attributeMap.get("displayType"));
			ebwtable.setDisplaySearch((String)attributeMap.get("displaySearch"));
			ebwtable.setFormname((String)attributeMap.get("formname"));
			ebwtable.setHrefCols((String)attributeMap.get("hrefCols"));
			ebwtable.setDivClass((String)attributeMap.get("divClass"));
			ebwtable.setRowsPerPage((String)attributeMap.get("rowsPerPage"));
			ebwtable.setRowsPerPage((String)attributeMap.get("rowsPerLine"));
			ebwtable.setNoDataFoundMsg((String)attributeMap.get("noDataFoundMsg"));
			ebwtable.setSorting((String)attributeMap.get("sorting"));
			ebwtable.setAccesskey((String)attributeMap.get("accesskey"));
			ebwtable.setReordering((String)attributeMap.get("reordering"));
			ebwtable.setNestedTable((String)attributeMap.get("nestedTable"));
			ebwtable.setNestedTableService((String)attributeMap.get("nestedTableService"));
			ebwtable.setTableLevelCSS((String)attributeMap.get("tableLevelCSS"));
			ebwtable.setColLevelCSS((String)attributeMap.get("colLevelCSS"));
			ebwtable.setEditableColumns((String)attributeMap.get("editableColumns"));
			ebwtable.setHeaderSortCol((String)attributeMap.get("headerSortCol"));
			ebwtable.setHtmlOutput(false);
			ebwtable.setDefRowComp((String)attributeMap.get("defRowComp"));
			ebwtable.setHiddenCols((String)attributeMap.get("hiddenCols"));
			ebwtable.setRowIdVal(new Boolean((String)attributeMap.get("rowIdVal")).booleanValue());
			ebwtable.setPrintEnabled(new Boolean((String)attributeMap.get("printEnabled")).booleanValue());
			ebwtable.setExportEnabled((String)attributeMap.get("exportEnabled"));
			ebwtable.setDoubleBorder(new Boolean((String)attributeMap.get("doubleBorder")).booleanValue());
			ebwtable.setDefRow(new Boolean((String)attributeMap.get("defRow")).booleanValue());
			ebwtable.setRowHighLight(new Boolean((String)attributeMap.get("rowHighLight")).booleanValue());
			ebwtable.setIsAjax("yes");
			ebwtable.sessionObj = sessionObject;
			ebwtable.setAttrObj(formObj);
			try{
				ebwtable.doStartTag();
			}catch(Exception e){
				System.out.println("exception occured in getting table thru Ajax: "+e);
			}
			resultBuffer = ebwtable.getOutputBuff();
			System.out.println("ebwtable.op---" +ebwtable.getName());
		}
		return resultBuffer;
	}
	public String getRowLevelCSS() {
		return rowLevelCSS;
	}
	public void setRowLevelCSS(String rowLevelCSS) {
		this.rowLevelCSS = rowLevelCSS;
	}
	public String getDivClass() {
		return divClass;
	}
	public void setDivClass(String divClass) {
		this.divClass = divClass;
	}
	public String getToolTipCol() {
		return toolTipCol;
	}
	public void setToolTipCol(String toolTipCol) {
		this.toolTipCol = toolTipCol;
	}
	public String getScrollable() {
		return scrollable;
	}
	public void setScrollable(String scrollable) {
		this.scrollable = scrollable;
	}
	public String getMouseOverEvent() {
		return mouseOverEvent;
	}
	public void setMouseOverEvent(String mouseOverEvent) {
		this.mouseOverEvent = mouseOverEvent;
	}
	public String getConditionRowCss() {
		return conditionRowCss;
	}
	public void setConditionRowCss(String conditionRowCss) {
		this.conditionRowCss = conditionRowCss;
	}
}
