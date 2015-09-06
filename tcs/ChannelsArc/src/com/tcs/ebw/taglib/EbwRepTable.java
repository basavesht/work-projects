package com.tcs.ebw.taglib;



import com.tcs.ebw.codegen.beans.ComboboxData;

import com.tcs.ebw.codegen.beans.ExcelForm;

import com.tcs.ebw.common.util.*;

import com.tcs.ebw.exception.EbwException;

import com.tcs.ebw.mvc.validator.EbwForm;

import com.tcs.ebw.serverside.factory.EBWServiceFactory;

import com.tcs.ebw.serverside.factory.IEBWService;

import com.tcs.ebw.serverside.jaas.auth.Auth;

import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

import com.tcs.ebw.taglib.*;

//import com.tcs.ebw.serverside.services.TMFields;

import java.io.*;

import java.lang.*;

import java.lang.reflect.*;

import java.text.MessageFormat;

import java.util.*;

import java.math.*;



import javax.security.auth.Subject;

import javax.servlet.ServletRequest;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import javax.servlet.jsp.*;

import javax.servlet.jsp.tagext.TagSupport;







public class EbwRepTable extends TagSupport

{

	StringBuffer out;

	private Object attrObj;

	ArrayList headerFunctionAL;

	ArrayList headersAL;

	ArrayList falseHeaderAL;

	ArrayList thirdHeaderFunctionAL;

	ArrayList secondHeaderFunctionAL;

	ArrayList secondHeaderSumAL;

	ArrayList hrefLinkAL;

	ArrayList toDisplay;

	ArrayList toDouble;

	ArrayList tablesummarycolsAL;//Nithya

	ArrayList colHeaderAL;//Nithya

	ArrayList falseHeadAL;

	int secondHeaderSumALPosition ;

	int hFCount;

	int f3Count;

	int f2Count;

	int f3Count1=0;

	int f2Count1=0;

	int noOfHeaders;

	boolean rowFlag;

	private boolean ajaxCall = false;

	boolean rowLevelFlag;

	ArrayList falseHeaderColAL;

	ArrayList currHeaderColAL;

	ArrayList dispColIndx;

	ArrayList dispColConstIndx;

	String  displayCol= new String();

	String TableCSSName;

	String className = new String();

	String rowClass = new String();

	String  currency = new String();

	String userCSA=new String();

	String  constdisplayCol;

	boolean  flagForDisplay=false;

	String[] constDBCol;

	String[] constDsplCol;

	String[] dispCol;

	String[] dispColConst;

	String[] falseHeaderColSA;

	String[] currColA;

	String[] currHeaderColSA;	

	String[] tsummarydispColConst;//Nithya

	String[] toptsummarydispColConst;

	String[] testCol;

	StringBuffer stBuff;

	private LinkedHashMap columnOps;

	private Integer headerLevel;

	LinkedHashMap columnCSSMap;

	private String sorting;

	private String headerSortCol;

	LinkedHashMap rowHead;

	HashMap sortKeys =null;

	String sortImgSrc=null;

	HashMap sortImgSrcKeys=null;

	int rowcount=-1;

	int hcount=-1;

	private String nestedTable = null;

	private boolean isNestedTable = false;

	private String rowsPerPage = null;

	int startIndx;

	int endIndx;

	private String reordering=null;

	String reorderingColmns = null;

	private String visibleColumns = null;

	private String availableColumns = null;

	public HttpSession sessionObj;

	private String hiddenCols = null;

	ArrayList colSpan;

	ArrayList spanDispLabel;

	ArrayList colSpanValues;

	ArrayList spanDispLabelValues;

	HashMap fieldAttrAL=null;

	HashMap fxRateConst;

	HashMap fxRatesrcTrgtVal;

	



	public EbwRepTable(ArrayList tableResultAL)

	{

	}



	public EbwRepTable()

	{

		  out= new StringBuffer();

		  tableHeader = new ArrayList();

		  columnOps = new LinkedHashMap();

		  headerLevel = null;

		  noOfHeaders=0;

		  secondHeaderSumAL = new ArrayList();

		  dispColIndx = new ArrayList();

		  dispColConstIndx = new ArrayList();

		  constdisplayCol = new String();

		  hrefLinkAL = new ArrayList();

		  toDisplay = new ArrayList();

		  falseHeadAL= new ArrayList();

		  toDouble = new ArrayList();

		  falseHeaderColAL=new ArrayList();	   

		  tablesummarycolsAL = new ArrayList();//Nithya

		  colHeaderAL = new ArrayList();//Nithya

		  currHeaderColAL=new ArrayList();

		  rowHead = new LinkedHashMap();

		  sortKeys =  new HashMap();

		  sortImgSrcKeys = new HashMap();

		  TableCSSName= new String();

		  colSpan = new ArrayList();

		  spanDispLabel = new ArrayList();

		  colSpanValues = new ArrayList();

		  spanDispLabelValues = new ArrayList();

		  fieldAttrAL = new HashMap();

		  fxRateConst= new HashMap();

		  fxRatesrcTrgtVal = new HashMap();

	}

	

	public int doEndTag() throws JspException {

		try{

				JspWriter jspout = pageContext.getOut();

				jspout.println(out.toString());

		}catch (Exception e) {

				e.printStackTrace();

		}

				out.delete (0,out.length());

				dispColIndx.clear();

				dispColConstIndx.clear();

				secondHeaderSumAL.clear();

				tableHeader.clear();

				hrefLinkAL.clear();

				toDisplay.clear();

				toDouble.clear();

				falseHeaderColAL.clear();

				currHeaderColAL.clear();

				falseHeadAL.clear();

				noOfHeaders=0;

				f3Count1=0;

				f2Count1=0;

				f3Count=0;

				flagForDisplay=false;

				rowcount=-1;

				  hcount=-1;

				  colSpan.clear();

				  spanDispLabel.clear();

				  colSpanValues.clear();

				  spanDispLabelValues.clear();

				  fieldAttrAL.clear();

				  fxRateConst.clear();

				  fxRatesrcTrgtVal.clear();

				return 0;

	}



	public int doStartTag()throws JspException {

		System.out.println("Inside doStartTag");

    	try 

    	{

    		//System.out.println("Inside doStartTag=ajaxCall="+ajaxCall);

    		//System.out.println("Inside doStartTag=ajaxCall="+isAjaxCall());

    		if(!ajaxCall){

			if(formname != null){

                attrObj = pageContext.getRequest().getAttribute(formname);               

			}

		    if(attrObj == null && formname != null){

                attrObj = pageContext.getSession().getAttribute(formname);              

		    }

    		}

    		sessionObj = ((HttpServletRequest)(pageContext.getRequest())).getSession();

    		boolean isDifferentScreen = false;



            Class formClass = Class.forName(attrObj.getClass().getName());

			appRB = ResourceBundle.getBundle("ApplicationConfig");

			formRB = ResourceBundle.getBundle(this.appRB.getString("message-resources"));	

			formObj=(DataInterface)formClass.getMethod("get" + StringUtil.initCaps(name) + "Collection", null).invoke(attrObj, null);

			System.out.println("\n**************************************************************************************************************************** ");

			System.out.println("--------------------------                              EBWREPORTTABLE:::STARTING                   ------------------- ");

			System.out.println("**************************************************************************************************************************** \n");

			ArrayList arValAL=(ArrayList)formObj.getData();

			

			if(arValAL == null ||arValAL.size()==0 ||  arValAL.isEmpty()){

				System.out.println("\n     DATA COMING FROM DB IS NULL........  "+arValAL);

				out.append("\n   Data is not available  ");

			}else{

			ArrayList rowHeadAL = (ArrayList)formObj.getRowHeader();

			LinkedHashMap rowHead = (LinkedHashMap)rowHeadAL.get(0);

			Set rowHeadSet = rowHead.keySet();

			Object[] rowHeadStr = (Object[])rowHeadSet.toArray();

			//System.out.println("\n:::------  rowHeadAL --------- "+rowHeadAL+"\n:::-------   rowHead    -------- "+rowHead+"\n:::-------   rowHeadSet     -------- "+rowHeadSet);

			ArrayList colattrobj=(ArrayList)formObj.getColAttrObjs();	

			ArrayList colattrobjAfterReorder = (ArrayList)formObj.getColAttrObjs();

			System.out.println("\n COLATTROBJ FIRST:::--------------> "+colattrobj);

			ReorderTableHelper reorderTableHelperObj = new ReorderTableHelper();

			LinkedHashMap dataValueBeforeReorder = (LinkedHashMap) formObj.getRowHeader().get(0);

			String colNamesFromAttrbeforeReorde = null;

			HashMap userObject = new HashMap();

			String prev_Action = null;String prevUrl = null;String prevActn = null;	String prevState = null;

			

			userObject = (HashMap)sessionObj.getAttribute("USER_OBJ");

			System.out.println("userObject=="+userObject);

			System.out.println("prev_Action in Rep Comp=="+(String)sessionObj.getAttribute(sessionObj.getId()+"_PrevActn"));

			if( userObject!=null && userObject.get("isDiffScreen")!=null && ((String)userObject.get("isDiffScreen")).equalsIgnoreCase("true")){

				if(sessionObj!=null && ((sessionObj.getAttribute(sessionObj.getId()+"_PrevActn"))!=null)){

					prev_Action =(String)sessionObj.getAttribute(sessionObj.getId()+"_PrevActn");

					System.out.println("prev_Action in Rep Comp=="+prev_Action);

					isDifferentScreen = true;

					String[] prevActnArray = prev_Action.split(":");

					if(prevActnArray!=null && prevActnArray.length>=3){

						prevUrl = prevActnArray[0]+".do";

						prevActn = prevActnArray[1];	

						prevState = prevActnArray[2];

					}	

				}

			}

			String stateEbwtable = ((EbwForm)attrObj).getState();

			

			String displayFlagForShuffleColAttr = null;

			if(rowHead!=null){

				//columns in the colattr before reorder							

				colNamesFromAttrbeforeReorde = ReorderTableHelper.getColNamesFromColAttr(colattrobj,getHiddenCols(),rowHead);

				

				

				try{

					String reOrderColsFrmSession = null;

					

					Set set=((Subject)sessionObj.getAttribute(Auth.SUBJECT_SESSION_KEY)).getPrincipals();

					Iterator it=set.iterator();

					UserPrincipal user=(UserPrincipal)it.next();

					String usrUserId=user.getUsruserid().toUpperCase();

					String displayType=null;

					

					//String usrGrppId=user.getUsrgrpid();								

					if(!(appRB.getString("Reordering").equalsIgnoreCase("No")) || !(reordering.equalsIgnoreCase("No"))){								

						reOrderColsFrmSession = reorderTableHelperObj.getReorderingColsFromSession(formObj,userObject,rowHead,sessionObj,formRB,formname,name,displayType,usrUserId,stateEbwtable,reordering,appRB);

						System.out.println("reOrderColsFrmSession=="+reOrderColsFrmSession);

					}

					if(reOrderColsFrmSession!=null && reOrderColsFrmSession.length()>0){

						String reorderCols = reOrderColsFrmSession;

						if(reorderCols.indexOf(":")>-1)

							reorderCols = reorderCols.substring(reorderCols.indexOf(":")+":".length(), reorderCols.length());

						System.out.println("reorderCols=="+reorderCols);

						int[] reorderColPos =  ReorderTableHelper.getReorderColPos(reorderCols,rowHead,formRB,formname,name);

						for(int i=0;i<reorderColPos.length;i++)

						System.out.println("reorderColPos["+i+"]=="+reorderColPos[i]);

						if(headerSortCol!=null && headerSortCol.length()>0)

							headerSortCol = ReorderTableHelper.getShuffledHeaderSort(reorderColPos,headerSortCol);

						

						System.out.println("headerSortCol=="+headerSortCol);

						if(getColLevelCSS() !=null && getColLevelCSS().length()>0)

							colLevelCSS = ReorderTableHelper.getShuffledColLevelCSS(reorderColPos,colLevelCSS);

						

						EBWLogger.logDebug(this,"formObj.getColAttrObjs() b4 settying in FB:"+formObj.getColAttrObjs());

						reorderTableHelperObj.setReorderInFormbean(reorderCols,rowHead,formObj,colattrobj,formRB,formname,name,displayFlagForShuffleColAttr);

						EBWLogger.logDebug(this,"formObj.getColAttrObjs() after settying in FB:"+formObj.getColAttrObjs());

						//arValAL=(ArrayList)formObj.getData();						

						//colattrobj=formObj.getColAttrObjs();

						colattrobjAfterReorder =formObj.getColAttrObjs();

						System.out.println("colattrobjAfterReorder=="+colattrobjAfterReorder);

						//rowHead = (LinkedHashMap) formObj.getRowHeader().get(0);

						//rowHeadStr=(Object[])rowHead.keySet().toArray();

						//EBWLogger.logDebug(this,"Final dataValue:"+rowHead);

						//EBWLogger.logDebug(this,"Final data:"+arValAL);

					}

				}catch(Exception e){

					System.out.println("This is the undefined: "+e);

				}

				

			}

			/**This piece of code is used to calculate visible cols and available cols being passed to the popup.jsp and popupdiff.jsp. for this we need cols after reorder.*/

			ArrayList colInColAtrAfterReorder = ((DataInterface) formObj).getColAttrObjs();

			//System.out.println("colInColAtrAfterReorder=="+colInColAtrAfterReorder);

			String colNamesAfterReorderInColAttr = ReorderTableHelper.getColNamesFromColAttrAfter(colInColAtrAfterReorder);

			//System.out.println("colNamesAfterReorderInColAttr=="+colNamesAfterReorderInColAttr);

			visibleColumns = ReorderTableHelper.getVisibleCols(colNamesAfterReorderInColAttr,formRB,formname,name,displayFlagForShuffleColAttr);

			availableColumns =ReorderTableHelper.getAvailableCols(colNamesFromAttrbeforeReorde,colNamesAfterReorderInColAttr,formRB,formname,name,displayFlagForShuffleColAttr);

			//System.out.println("visibleColumns=="+visibleColumns);

			//System.out.println("availableColumns=="+availableColumns);

			/***************************Code related to reordering end here*************/

			reorderingColmns=(String)(((HashMap)(sessionObj.getAttribute("USER_OBJ"))).get(sessionObj.getId()+"_"+name));

			//System.out.println("reorderingColmns=="+reorderingColmns);

			

			

			

			

			

			

			/** This piece of code belongs to  TableLevelCSS **/

			

			if(getTableLevelCSS()!=null){				

				TableCSSName=getTableLevelCSS();

			}else

				TableCSSName="tableheader";

			

			

			String prevRowSumboo;

			if(prevRowSum!=null)

			   prevRowSumboo=prevRowSum;

			else

				prevRowSumboo="";

			stBuff = new StringBuffer(); 

			StringBuffer sBuffer = new StringBuffer();

			int colattrobjSize = colattrobj.size();

			testCol = new String[colattrobjSize+1];

			//constDsplCol = new String[colattrobjSize+1];

			constDsplCol = new String[colattrobjAfterReorder.size()+1];

			//constDBCol= new String[colattrobjSize+1];

			constDBCol= new String[colattrobjAfterReorder.size()+1];

			

			String restartKeyFromFormbean = ((EbwForm)attrObj).getRestartKey();

			String sortType = null;

			

			if(xcolopstr==null)

				xcolopstr="Sum:Sum";

			String usercolOp = (String)xcolopstr;

			System.out.println("usercolOp="+usercolOp);

			

			if(colattrobj != null && colattrobj.size() > 0)

			{

				

				for(int d = 0; d < colattrobj.size(); d++)

				{

					TableColAttrObj tablecolattrobj = (TableColAttrObj)colattrobj.get(d);

					String columnOpStr = "";

					String headerStr = tablecolattrobj.getHeader().toString();

					columnOpStr = tablecolattrobj.getColumnOp().toString();

					String colNameStr = tablecolattrobj.getColName().toString();

					String TagContentVal = new String();

					

					if(d == 0 ){

						tableHeader.add(disptype);

						columnOps.put(colNameStr,columnOpStr);

						if(!headerStr.equalsIgnoreCase("No") || !headerStr.equalsIgnoreCase("")){							

							if(headerStr.equalsIgnoreCase("Header1")){

							   tableHeader.add(1,colNameStr);

							   noOfHeaders++;}

							else if(headerStr.equalsIgnoreCase("Header2")){

								   tableHeader.add(2,colNameStr);

								   noOfHeaders++;}

							else if(headerStr.equalsIgnoreCase("Header3")){

								   tableHeader.add(3,colNameStr);

								   noOfHeaders++;}

							else if(headerStr.equalsIgnoreCase("Header4")){

								   tableHeader.add(4,colNameStr);

								   noOfHeaders++;}

						}

					}else {

						columnOps.put(colNameStr,columnOpStr);

						

						if(!headerStr.equalsIgnoreCase("No") || !headerStr.equalsIgnoreCase("")){							

							if(headerStr.equalsIgnoreCase("Header1")){

							   tableHeader.add(1,colNameStr);

							   noOfHeaders++;}

							else if(headerStr.equalsIgnoreCase("Header2")){

								   tableHeader.add(2,colNameStr);

								   noOfHeaders++;}

							else if(headerStr.equalsIgnoreCase("Header3")){

								   tableHeader.add(3,colNameStr);

								   noOfHeaders++;}

							else if(headerStr.equalsIgnoreCase("Header4")){

								   tableHeader.add(4,colNameStr);

								   noOfHeaders++;}

						}

					}

					



					String str = new String();

					String componentStrValue = new String();

					boolean toDisplayVal;

					String toDoubleVal= new String();

					String strChkHead = new String();

					String colspanVal= new String();

					String spanDisplabel= new String();

			        str = tablecolattrobj.getColName().toString();

					componentStrValue = tablecolattrobj.getComponentType().toString();

					toDoubleVal = tablecolattrobj.getDataType().toString();

					if(tablecolattrobj.getColSpan()!=null)

						colspanVal = tablecolattrobj.getColSpan().toString();

					if(tablecolattrobj.getSpanDisplayLabel()!=null)

						spanDisplabel = tablecolattrobj.getSpanDisplayLabel().toString();

					strChkHead = 	formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + str);	

					

					TagContentVal = tablecolattrobj.getTagContent().toString();

					//constDBCol[d] = str;

					//constDsplCol[d] = strChkHead;

				

					if(componentStrValue.equalsIgnoreCase("href")){

						hrefLinkAL.add(str); 

					}

					if(toDoubleVal.equalsIgnoreCase("Double")){

						toDouble.add(str); 

					}

					if(!componentStrValue.equalsIgnoreCase("Hidden")){

						toDisplay.add(str);

					}

					if(!colspanVal.equals("")){

						colSpan.add(str);

						colSpanValues.add(colspanVal);

					}

					if(!spanDisplabel.equals("")){

						spanDispLabel.add(str);

						spanDispLabelValues.add(spanDisplabel);

					}

					if(TagContentVal.indexOf("#")!=-1 && TagContentVal.indexOf("onclick")!=-1)

						fieldAttrAL.put(str,TagContentVal );

					

					

					

					//sBuffer.append(strChkHead).append(",");

					//stBuff.append(str).append(",");

					if(d == (colattrobj.size()-1) && (!usercolOp.equals("")))	{

						String[] colOp = new String[2];

						colOp = usercolOp.split(":");

						String str1 = colOp[1];

						String str1Head = 	formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + str1);

						//constDBCol[d+1] = str1;

						//constDsplCol[d+1] = str1Head;

						//sBuffer.append(str1Head).append(",");

						//stBuff.append(str1).append(",");

					}

					if(arValAL!=null && arValAL.size()>0)

						 sortImgSrc = getHeaderSortImgStr(sortType);										 

					//System.out.println("---------restartKeyFromFormbean------xX>"+restartKeyFromFormbean);

				

					if(restartKeyFromFormbean != null && restartKeyFromFormbean.length()>0 && !restartKeyFromFormbean.equalsIgnoreCase("null")){

						if(restartKeyFromFormbean.substring(0,restartKeyFromFormbean.indexOf(":")).equalsIgnoreCase(str)){

							 sortType = restartKeyFromFormbean.substring(restartKeyFromFormbean.lastIndexOf(":")+1,restartKeyFromFormbean.length());												

							if(arValAL!=null && arValAL.size()>0 && sortType!=null && sortType.length()>0)

								sortImgSrc=sortType.equalsIgnoreCase("asc")?getHeaderSortImgStr("desc"):getHeaderSortImgStr("asc");

						}

					}

					String headerSortKey = str+":"+(tablecolattrobj.getDataType()).toLowerCase()+":ASC";

					sortKeys.put(str, headerSortKey);

					sortImgSrcKeys.put(str, sortImgSrc);

				}

				System.out.println("colSpan =="+colSpan);

				System.out.println("colSpanValues =="+colSpanValues);

				System.out.println("spanDispLabel =="+spanDispLabel);

				System.out.println("spanDispLabelValues =="+spanDispLabelValues);

				System.out.println("TagContentVal =="+fieldAttrAL);

				

			}

			

			if(colattrobjAfterReorder != null && colattrobjAfterReorder.size() > 0)

			{

				String str = new String();

				String strChkHead = new String();

				for(int d = 0; d < colattrobjAfterReorder.size(); d++)

				{

					TableColAttrObj tablecolattrobj = (TableColAttrObj)colattrobjAfterReorder.get(d);

					 str = tablecolattrobj.getColName().toString();

					 strChkHead = 	formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + str);

						constDBCol[d] = str;

						constDsplCol[d] = strChkHead;

						sBuffer.append(strChkHead).append(",");

						stBuff.append(str).append(",");

						

						

						if(d == (colattrobjAfterReorder.size()-1) && (!usercolOp.equals("")))	{

							String[] colOp = new String[2];

							colOp = usercolOp.split(":");

							String str1 = colOp[1];

							String str1Head = 	formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + str1);

							constDBCol[d+1] = str1;

							constDsplCol[d+1] = str1Head;

							sBuffer.append(str1Head).append(",");

							stBuff.append(str1).append(",");

						}

						

				}

				

			}

			if( fxrate == null || fxrate.length()==0 || fxrate.equalsIgnoreCase("") || fxrate ==""){

			}else{

				calculateFXRate(arValAL,rowHeadStr);

				

				/*String[] fxRateCols = fxrate.split(",");

				for(int i=0;i<fxRateCols.length;i++){

					XtraColumnAddToTableResultAL(tableResultAL,i);

				}*/

			}

			

			System.out.println("tableHeader="+tableHeader);

			

			stBuff.deleteCharAt((stBuff.length()-1));

			sBuffer.deleteCharAt((sBuffer.length()-1));

			

            ArrayList displayAL = (ArrayList)tableHeader;

			userCSA = (String)displayAL.get(0);

			headerLevel = new Integer(noOfHeaders);

			//System.out.println("\n tableHeader::---------------> "+tableHeader);

			TableManipulator tmObject = new TableManipulator();

			//System.out.println("\n arValAL:::---------------> "+arValAL);

			ArrayList tableResultAL=new ArrayList();

			System.out.println("isDifferentScreen--> "+isDifferentScreen+" &((EbwForm)attrObj).getPaginationIndex()="+((EbwForm)attrObj).getPaginationIndex());

			if(isDifferentScreen || ((EbwForm)attrObj).getPaginationIndex()==null){

				System.out.println("Call the TableManipulator");

			tableResultAL = (ArrayList)tmObject.getManipulatedData(arValAL,rowHeadStr,headerLevel,tableHeader,columnOps,xcolopstr,prevRowSumboo);

			pageContext.getSession().setAttribute("tableResultAL", tableResultAL);

			}

			else if(!isDifferentScreen || ((EbwForm)attrObj).getPaginationIndex()!=null)

			{

				System.out.println("Take it from the session");

				tableResultAL = (ArrayList)(pageContext.getSession().getAttribute("tableResultAL"));

			}

			//System.out.println("tableResultAL--> "+tableResultAL);

			

			

			displayCol=(String)formClass.getMethod("getReorderCols", null).invoke(attrObj, null);

			if(displayCol == null){

				displayCol = "";

			}

			displayCol = "";

			if(displayCol.equals("") || displayCol.equalsIgnoreCase("null") || displayCol == null){

				constdisplayCol = sBuffer.toString();

				displayCol = stBuff.toString();

				testCol = constdisplayCol.split(",");

			}

			dispCol = displayCol.split(",");

			for(int x = 0 ; x < dispCol.length ; x++){

				for(int y = 0 ; y < constDsplCol.length ; y++){

					if(dispCol[x].equalsIgnoreCase(constDsplCol[y])){

						dispCol[x] = constDBCol[y];

						testCol[x] = constDsplCol[y];

					}

				}

			}

		    

			if(toptabcols == null ||toptabcols.length()==0 ||  toptabcols.equalsIgnoreCase("") || toptabcols ==""){

				dispColConst = null;

			}else{

				dispColConst = toptabcols.split(",");

			}



			if( tablesummarycols == null || tablesummarycols.length()==0 || tablesummarycols.equalsIgnoreCase("") || tablesummarycols ==""){

				tsummarydispColConst = null;

			}else{

				tsummarydispColConst = tablesummarycols.split(",");

			}

			if( toptablesummarycols == null || toptablesummarycols.length()==0 || toptablesummarycols.equalsIgnoreCase("") || toptablesummarycols ==""){

				toptsummarydispColConst = null;

			}else{

				toptsummarydispColConst = toptablesummarycols.split(",");

			}

			

			if(falseHeaderCol == null ||  falseHeaderCol ==""){

				falseHeaderColSA = null;

			}else{

				falseHeaderColSA = falseHeaderCol.split(",");

			}

			if(currHeaderCol == null ||  currHeaderCol ==""){

				currHeaderColSA = null;

			}else{

				currHeaderColSA = currHeaderCol.split(",");

			}

		 

			ArrayList tempColHeaderAL = (ArrayList)tableResultAL.get(0);

			for (int q = 0;q<dispCol.length;q++){ 

				for (int i = 0;i< tempColHeaderAL.size();i++){ 

					if(dispCol[q].equalsIgnoreCase(tempColHeaderAL.get(i).toString()))

						dispColIndx.add(new Integer(i));

				}

			}

				

			if(dispColConst!= null){

				for (int q = 0;q<dispColConst.length;q++){ 

					for (int i = 0;i< tempColHeaderAL.size();i++) {

						if(dispColConst[q].equalsIgnoreCase(tempColHeaderAL.get(i).toString()))

							dispColConstIndx.add(new Integer(i));

					}

				}

			}

		

		if(falseHeaderColSA!= null){

				for (int q = 0;q<falseHeaderColSA.length;q++){ 

					for (int i = 0;i< tempColHeaderAL.size();i++) {

					//	System.out.println("falseHeaderColSA[q]   "+falseHeaderColSA[q]);

					//	System.out.println("tempColHeaderAL.get(i).toString()  "+tempColHeaderAL.get(i).toString());

						if(falseHeaderColSA[q].trim().equalsIgnoreCase(tempColHeaderAL.get(i).toString().trim()))

							falseHeaderColAL.add(new Integer(i));

					}

				}

			}

			if(currHeaderColSA!= null){

				for (int q = 0;q<currHeaderColSA.length;q++){ 

					for (int i = 0;i< tempColHeaderAL.size();i++) {

						if(currHeaderColSA[q].equalsIgnoreCase(tempColHeaderAL.get(i).toString()))

							currHeaderColAL.add(new Integer(i));

					}

				}

			}

			

			

			currColA=new String[tempColHeaderAL.size()];

			colHeaderAL = (ArrayList)tableResultAL.get(0);

			

			/** This piece of code belongs to ColLevelCSS **/

			columnCSSMap = getColumnCSSMap(0,colHeaderAL.size()-2);

		//	System.out.println("columnCSSMap   :  "+columnCSSMap);

			if(getTableWidth()==null)

				setTableWidth("100%");

			

			/*	System.out.println("currColA   Length :  "+currColA.length);

			System.out.println("falseHeaderColAL::::::::::----->"+falseHeaderColAL);

			System.out.println("currHeaderColAL::::::::::----->"+currHeaderColAL);

			System.out.println("dispColIndx::::::::::----->"+dispColIndx);

			System.out.println("currHeaderColAL::::::::::----->"+currHeaderColAL);

			System.out.println("toDouble::::::::::----->"+toDouble);

			System.out.println("hrefLinkAL::::::::::----->"+hrefLinkAL);

			System.out.println("stBuff::::::::::----->"+stBuff);

			System.out.println("displayCol::::::::::----->"+displayCol);

			System.out.println("sBuffer::::::::::----->"+sBuffer);

			System.out.println("constdisplayCol::::::::::----->"+constdisplayCol);

			for(int h=0;h<testCol.length;h++)

			{

			   System.out.println("::::::::::tesctcol[i]::::"+testCol[h]);	

			}

			for(int h=0;h<dispCol.length;h++)

			{

			   System.out.println("::::::::::dispCol[i]::::"+dispCol[h]);	

			}

			for(int h=0;h<constDBCol.length;h++)

			{

			   System.out.println("::::::::::constDBCol[i]::::"+constDBCol[h]);	

			}

			for(int h=0;h<constDsplCol.length;h++)

			{

			   System.out.println("::::::::::constDsplCol[i]::::"+constDsplCol[h]);	

			}

			System.out.println("toptabcols::::::::::----->"+toptabcols);

			for(int h=0;h<dispColConst.length;h++)

			{

			   System.out.println("::::::::::dispColConst[i]::::"+dispColConst[h]);	

			}*/

		

			

		

			

			String headerSortKey="";

			if(restartKeyFromFormbean != null && restartKeyFromFormbean.length()>0 && !restartKeyFromFormbean.equalsIgnoreCase("null")){

			System.out.println("restartKeyFromFormbean=="+restartKeyFromFormbean);

			sortType = restartKeyFromFormbean.substring(restartKeyFromFormbean.lastIndexOf(":")+1,restartKeyFromFormbean.length());

			String restartKey[] = restartKeyFromFormbean.split(":");

			headerSortKey = restartKey[0];

			

			}

			//if(sortImgSrc==null)

			//	sortImgSrc=getHeaderSortImgStr(sortType);

			

			//String headerSortType= ((EbwForm)attrObj).getHeaderSortType();

			//System.out.println("headerSortType="+headerSortType);

			//if(headerSortType!=null)

				//sortType = headerSortType;

			

		//	if(arValAL!=null && arValAL.size()>0 && sortType!=null && sortType.length()>0)

			//	sortImgSrc=sortType.equalsIgnoreCase("asc")?getHeaderSortImgStr("desc"):getHeaderSortImgStr("asc");

			if(restartKeyFromFormbean!=null && restartKeyFromFormbean!="" && restartKeyFromFormbean.length()>0 && !restartKeyFromFormbean.equalsIgnoreCase("null") &&!restartKeyFromFormbean.equals("")){

				System.out.println("restartkey is not null");

				System.out.println("sortType=="+sortType);

				tableResultAL=	getSortedTableResultSetAL(tableResultAL,headerSortKey,sortType);

			}

			

			/**  code to determine the number of rows to be displayed in a page **/						

			int dataRowCount = formObj.getDataRowCount();/** Getting the actual number of rows  */

			int loopCount = 0;/** loopCount says till which index of rows we have to loop thru have to loop thru */

			int startIndex = 0;/** StartIndex from which index of rows we have to loop thru for display of data in the table */

			int pageRowCount = 0;/** pageRowCount will have the count of number of rows per page */

			//if(rowsPerPage!=null && rowsPerPage.length()>0){//if it has the attribute rowsPerPage

				//pageRowCount = Integer.parseInt(rowsPerPage);//Converting rowsPerPage to primitive type int

				/**if count of rows per page is less than the actual number of rows of data

				 * storing the pageRowCont to loopCount to restrict the actual number of

				 * rows to the number of rowsPerPage set in the taglib attribute, if not assigning the actual rowcount to the loopcount */

			//	loopCount=(pageRowCount<dataRowCount)?pageRowCount:dataRowCount;

				//tableResultAL=	getPagnTableResultSetAL(tableResultAL);

				

			//}else if(rowsPerPage==null || rowsPerPage.length()<=0)

			//	loopCount = dataRowCount;/**if rowsPerPage attribute is not set, setting the loopCount tothe actual rowCount*/

			if(reordering!=null && reordering.equalsIgnoreCase("Yes"))

				printReorderImage();

			if(isExportEnabled())

				out.append("<a href=\"#\" onClick=\"javascript:exportSubmit(document.forms[0],'"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+((EbwForm)attrObj).getScreenName()+"','"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','0','Excel','0');\"><img align=\"absmiddle\" src=\"../images/excel.GIF\" border=\"0\" alt=\"excel\"></a>&nbsp;&nbsp");//new

			if(isPdfEnabled())

				out.append("<a href=\"#\" onClick=\"javascript:exportSubmit(document.forms[0],'"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+((EbwForm)attrObj).getScreenName()+"','"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','0','Pdf','0');\"><img align=\"absmiddle\" src=\"../images/pdf.GIF\" border=\"0\" alt=\"pdf\"></a>&nbsp;&nbsp");//new

	     	

	     	int rowsInPage=0;

	     	boolean isMultiplePage = false;

	     	String pgnIndex=null;

	     	

	     	String actionType = ((EbwForm)attrObj).getActionType();

	     	System.out.println("actionType=="+actionType);

	     	pgnIndex = ((EbwForm)attrObj).getPaginationIndex();

	     	if(pgnIndex==null || pgnIndex=="" || pgnIndex.equals(""))

	     		pgnIndex="1";

	     	

	     		

	     	if(actionType!=null && actionType.length()>0){

	     		if(actionType.equalsIgnoreCase("prev"))

	     			pgnIndex=Integer.toString(Integer.parseInt(((EbwForm)attrObj).getPaginationIndex())-1);

	     		else if(actionType.equalsIgnoreCase("next"))

	     			pgnIndex=Integer.toString(Integer.parseInt(((EbwForm)attrObj).getPaginationIndex())+1);

	     	}

	     		

	     	System.out.println("pgnIndex=="+pgnIndex);

	     	System.out.println("rowsPerPage=="+rowsPerPage);

	     	int paginationIndex=0;

	     	String fnname = "FN"+headerLevel;

	     	System.out.println("tableResultAL.size()=="+tableResultAL.size());

	    	System.out.println("fnname=="+fnname);

	     	

	     	if(rowsPerPage!=null && rowsPerPage.length()>0){

				/*rowsInPage = Integer.parseInt(rowsPerPage);

				if(rowsInPage<dataRowCount){

					isMultiplePage = true;

				}*/

				out.append("<td colspan=\""+(dispColIndx.size()-1)+"\" class=\"pageNo\">");

				if(pgnIndex !=null && pgnIndex.length()>0 && rowsPerPage!=null && rowsPerPage.length()>0){

					paginationIndex = Integer.parseInt(pgnIndex);

					/*if(paginationIndex>1)

						out.append("<a  onClick=\"javascript:pgnSubmit(document.forms[0],'"+((EbwForm)attrObj).getAction()+"','Prev','"+restartKeyFromFormbean+"','"+paginationIndex+"','prev');\""  + ">" + "<img align=\"absmiddle\" src=\"../images/prev.gif\" border=\"0\" alt=\"Previous\" >" +"</a>");

					if(pgnIndex!=null && pgnIndex.length()>0){

						if(isMultiplePage)

							out.append("Page : "+paginationIndex+" +");

						else

							out.append("Page : "+paginationIndex);

					}

					if(isMultiplePage)

						out.append("<a  onClick=\"javascript:pgnSubmit(document.forms[0],'"+((EbwForm)attrObj).getAction()+"','Next','"+restartKeyFromFormbean+"','"+paginationIndex+"','next');\""  + ">" + "<img align=\"absmiddle\" src=\"../images/next1.gif\" border=\"0\" alt=\"Next\" >" +"</a>&nbsp;&nbsp;</td>");*/

				}

			}

	     	System.out.println("paginationIndex=="+paginationIndex);

	     	if(paginationIndex==0 && rowsPerPage==null){

	     		startIndx=0;

	     		endIndx=tableResultAL.size();

	     	}

	     	else

	     	{

	     		startIndx=(paginationIndex - 1)* Integer.parseInt(rowsPerPage)+1;

	     		endIndx = paginationIndex * Integer.parseInt(rowsPerPage);;

	     	}

	     	System.out.println("startIndx - BEFORE=="+startIndx);

	     	int rowcount=0;

	     	int remRowcount=0;

	     	int fcounts=0;

	     	if(rowsPerPage!=null && rowsPerPage.length()>0  ){

	     		for(int d=1;d<tableResultAL.size() && rowcount<startIndx;d++){

	     			ArrayList tempAL = (ArrayList)tableResultAL.get(d);

	    			String elemntAtZ = tempAL.get(0).toString();

	    			if(userCSA.equalsIgnoreCase("disp3")){

		    			if(elemntAtZ.startsWith("F")){

		    				if(elemntAtZ.equalsIgnoreCase(fnname)){

		    					rowcount++;

		    				    fcounts++;

		    				}

		    			}

	    			}

	    			else if(userCSA.equalsIgnoreCase("disp2")){

	    				if(elemntAtZ.startsWith("H")){

	    					if(elemntAtZ.equalsIgnoreCase("H1"))

		    					rowcount++;

	    				}

		    			if(elemntAtZ.startsWith("F")){

		    				if(elemntAtZ.equalsIgnoreCase(fnname)){

		    					rowcount++;

		    					System.out.println("rowcount="+rowcount+" &fcounts=="+fcounts+" &tempAL="+tempAL);

		    				    fcounts++;

		    				}

		    			}

	    			}

	    			else if(userCSA.equalsIgnoreCase("disp4")){

	    				if(elemntAtZ.startsWith("D")){

		    					rowcount++;

		    			}

	    			}

	    			startIndex=d;

	    				

	     		}

	     		System.out.println("startIndex=="+startIndex);

	     		System.out.println("rowcount=="+rowcount);

	     		System.out.println("fcounts=="+fcounts);

	     		if(paginationIndex!=1){

	     		startIndx=	startIndex;

	     		f3Count=(fcounts-1)*(headerLevel.intValue()-1);

	     		}

	     		System.out.println("f3Count=="+f3Count);

	     		 

	     		

	     		for(int d=startIndex;d<tableResultAL.size() ;d++){

	     			ArrayList tempAL = (ArrayList)tableResultAL.get(d);

	    			String elemntAtZ = tempAL.get(0).toString();

	    			if(userCSA.equalsIgnoreCase("disp3")){

		    			if(elemntAtZ.startsWith("F")){

		    				if(elemntAtZ.equalsIgnoreCase(fnname))

		    					remRowcount++;

		    			}

	    			}

	    			else if(userCSA.equalsIgnoreCase("disp2")){

	    				if(elemntAtZ.startsWith("H")){

	    					if(elemntAtZ.equalsIgnoreCase("H1"))

	    						remRowcount++;

	    				}

		    			if(elemntAtZ.startsWith("F")){

		    				if(elemntAtZ.equalsIgnoreCase(fnname))

		    					remRowcount++;

		    			}

	    			}

	    			else if(userCSA.equalsIgnoreCase("disp4")){

	    				if(elemntAtZ.startsWith("D")){

	    					remRowcount++;

		    			}

	    			}

	    			

	    				

	     		}

	     		System.out.println("remRowcount=="+remRowcount);

	     		

	     	}

	     	

	     	if(rowsPerPage!=null && rowsPerPage.length()>0){

	     		rowsInPage = Integer.parseInt(rowsPerPage);

	     		if(rowsInPage<remRowcount ){

					isMultiplePage = true;

				}

	     	}

	     		if(pgnIndex !=null && pgnIndex.length()>0 && rowsPerPage!=null && rowsPerPage.length()>0){

				if(paginationIndex>1)

					out.append("<a  onClick=\"javascript:pgnSubmit(document.forms[0],'"+((EbwForm)attrObj).getAction()+"','Prev','"+restartKeyFromFormbean+"','"+paginationIndex+"','prev');\""  + ">" + "<img align=\"absmiddle\" src=\"../images/prev.gif\" border=\"0\" alt=\"Previous\" >" +"</a>");

				if(pgnIndex!=null && pgnIndex.length()>0){

					if(isMultiplePage)

						out.append("Page : "+paginationIndex+" +");

					else

						out.append("Page : "+paginationIndex);

				}

				if(isMultiplePage)

					out.append("<a  onClick=\"javascript:pgnSubmit(document.forms[0],'"+((EbwForm)attrObj).getAction()+"','Next','"+restartKeyFromFormbean+"','"+paginationIndex+"','next');\""  + ">" + "<img align=\"absmiddle\" src=\"../images/next1.gif\" border=\"0\" alt=\"Next\" >" +"</a>&nbsp;&nbsp;</td>");

			}

	     	System.out.println("startIndx=="+startIndx+" & endIndx="+endIndx);

		

			if(userCSA.equalsIgnoreCase("disp1"))

			{

				printSeperateTableTop(tableResultAL);

			}else if(userCSA.equalsIgnoreCase("bottom")){

				printSeperateTableBottom(tableResultAL);

			}

			else if(userCSA.equalsIgnoreCase("disp2") || userCSA.equalsIgnoreCase("disp3") || userCSA.equalsIgnoreCase("disp4")){

			    

				printTables(tableResultAL,currHeaderColAL);

			} 

			else if(userCSA.equalsIgnoreCase("disp2withoutaddingtable")){

				

				printTableForDisp2WithoutAddingTable(tableResultAL);

			} 

            else if(userCSA.equalsIgnoreCase("dataDisp2")){

				printSeperateTableforDataDisp2(tableResultAL);

			}else if(userCSA.equalsIgnoreCase("dataDispX")){

				printSeperateTableforDataDisp2(tableResultAL);

			}else if(userCSA.equalsIgnoreCase("dataDisp")){

				printSeperateTableforDataDisp(tableResultAL);

			} 

		}

			

			System.out.println("\n**************************************************************************************************************************** ");

			System.out.println("--------------------------                              EBWREPORTTABLE:::END                   ------------------- ");

			System.out.println("**************************************************************************************************************************** \n");	

			

    	

      }catch(Exception e){

			e.printStackTrace();

	  }

		return 0;

    }



	

	public void printTables (ArrayList tableResultAL,ArrayList currHeaderColAL ) throws IOException {

		int 	tableResultALSize = tableResultAL.size();

	

		/** Here we are calling this method for displaying the toptable */

			//if(toptabcols==null || toptabcols=="" || toptabcols.length()==0 || toptabcols.equalsIgnoreCase("")){

			//}else

			   printTopTable(tableResultAL);

	

		/** This method is used to reorder the columns of the below table */

		

		     //printReorderImage();

     	

	

		

	

		     String selectID="";

		     int countRows=-1;

				int rowsInPage=0;

				

		if(userCSA.equalsIgnoreCase("disp2"))

		{

			boolean firstHeader=false;

			hcount=-1;

			if(rowsPerPage!=null && rowsPerPage.length()>0){

				rowsInPage = Integer.parseInt(rowsPerPage);

				countRows++;

				}

		

			for(int x = startIndx; x < tableResultALSize && countRows<rowsInPage; x++){

			{

			ArrayList tempAL = (ArrayList)tableResultAL.get(x);

			String elemntAtZ = tempAL.get(0).toString();

			

			if(elemntAtZ.startsWith("H"))

				

			{

				if(elemntAtZ.equalsIgnoreCase("H1")){

					firstHeader = true;

					if(rowsPerPage!=null && rowsPerPage.length()>0)

						countRows++;							

							hcount++;

							rowcount=-1;

					printSeperateTableTopHeaderForDisp2(tableResultAL);

				}else if(elemntAtZ.equalsIgnoreCase("H2")){

					if(headerLevel.intValue() == 2){

						if(rowLevelFlag){

							rowClass = "oddrow";

							rowLevelFlag=false;

						}else{

							rowClass = "evenrow";

							rowLevelFlag=true;

						}

						rowcount++;

						//out.append ("<tr class="+rowClass+"  id="+name+ "_"+hcount+"_1"+"_"+count+">");

						out.append ("<tr class="+rowClass+"  id="+name+ "_"+hcount+"_"+rowcount+">");

						String tempStr = (String)(tempAL.get(1).toString());

						String[] tempStrHeader = new String[4];

						tempStrHeader = tempStr.split(":");

						String strContent="";	

						String tempStrHeaderString = tempStrHeader[0].toString();

						if(fieldAttrAL.get(tempStrHeaderString)!=null && fieldAttrAL.get(tempStrHeaderString)!=""){

							strContent = fieldAttrAL.get(tempStrHeaderString).toString().replaceAll("~VARIABLE", tempStrHeader[1]);

						}

						else

							strContent = tempStrHeader[1];

						out.append ("<td class=\"tableData\" align=\"left\">"+strContent+"</td>");

					}

				}else if(elemntAtZ.equalsIgnoreCase("H3")){

				}else if(elemntAtZ.equalsIgnoreCase("H4")){

				}else{

					printTopHeaderForDisp2(tempAL);

				}

			}

			

			else if(elemntAtZ.startsWith("D"))

			{

				for(int k=0;k<currHeaderColAL.size();k++){

					String str=currHeaderColAL.get(k).toString();

					int ind=Integer.parseInt(str);

				

					for(int j=0;j<falseHeaderColAL.size();j++){

						if(k==j){

					 currColA[ind]=tempAL.get(Integer.parseInt(falseHeaderColAL.get(j).toString())).toString();

				}

				}

				}

			}

			else if(elemntAtZ.startsWith("F"))

			{

				

				if(elemntAtZ.equalsIgnoreCase("FN1"))

				{}

				else if(elemntAtZ.equalsIgnoreCase("FN2"))

				{

				if(headerLevel.intValue() == 2)

					printOperationsForDisp2WithoutTable(tempAL,currHeaderColAL,true,selectID);

				}

				else{

					if(headerLevel.intValue() == 3){

						if(rowsPerPage!=null && rowsPerPage.length()>0)

							countRows++;

						if(!firstHeader){

							String elemntAtZ1 = "";						

							for(int j=startIndx;j>0 && !elemntAtZ1.equalsIgnoreCase("H1");j--){

								ArrayList tempAL1 = (ArrayList)tableResultAL.get(j);

								elemntAtZ1 = tempAL1.get(0).toString();

								 if(elemntAtZ1.startsWith("H")){

									if(elemntAtZ1.equalsIgnoreCase("H1")){

										System.out.println("tempAL1="+tempAL1);

										hFCount = headerFunctionAL.indexOf(tempAL1);

										printSeperateTableTopHeaderForDisp2(tableResultAL);

										firstHeader = true;

									}

								 }

							}							

						}					

					rowLevelFlag=flagForDisplay;

						printOperationsForDisp2WithoutTable(tempAL,currHeaderColAL,true,selectID);

						//selectID=printOperationsForDisp2WithoutTable(tempAL,currHeaderColAL,false,selectID);

						//out.append ("</tr>");

					}

					else if(headerLevel.intValue() == 4 && elemntAtZ.equalsIgnoreCase("FN3")){

					}else if(headerLevel.intValue() == 4 && elemntAtZ.equalsIgnoreCase("FN4")){

						if(rowsPerPage!=null && rowsPerPage.length()>0)

							countRows++;

						if(!firstHeader){

							String elemntAtZ1 = "";

						

							for(int j=startIndx;j>0 && !elemntAtZ1.equalsIgnoreCase("H1");j--){

								ArrayList tempAL1 = (ArrayList)tableResultAL.get(j);

								elemntAtZ1 = tempAL1.get(0).toString();

								 if(elemntAtZ1.startsWith("H")){

									if(elemntAtZ1.equalsIgnoreCase("H1")){

										System.out.println("tempAL1="+tempAL1);

										hFCount = headerFunctionAL.indexOf(tempAL1);

										printSeperateTableTopHeaderForDisp2(tableResultAL);

										firstHeader = true;

									}

								 }

							}

							

						}

						//selectID=printOperationsForDisp2WithoutTable(tempAL,currHeaderColAL,false,selectID);

					     rowLevelFlag=flagForDisplay;

						printOperationsForDisp2WithoutTable(tempAL,currHeaderColAL,true,selectID);

					}

					

				}

			}

		}

	 }

	}//end of if disp2

	

	

		boolean headerDisp=true;	 

		String tempStrHeaders = "";

		String tempStrHeader1 = "";

	if(userCSA.equalsIgnoreCase("disp3"))

	{

		hcount=-1;

		if(rowsPerPage!=null && rowsPerPage.length()>0){

			rowsInPage = Integer.parseInt(rowsPerPage);

			countRows++;

			}

		if(headerDisp){

			hcount++;

			   printColumnTableForDisp2((ArrayList)tableResultAL.get(0));

			   headerDisp=false;

			}

		for(int x = startIndx ; x < tableResultALSize && countRows<rowsInPage; x++){

			ArrayList tempAL = (ArrayList)tableResultAL.get(x);

			String elemntAtZ = tempAL.get(0).toString();

			 if(elemntAtZ.startsWith("H")){

				if(elemntAtZ.equalsIgnoreCase("H1")){

					

					tempStrHeader1 = tempAL.get(1).toString();

					tempStrHeaders = tempStrHeader1;

					//String tempStrHeaderString1 = tempStrHeader[1].toString();

					//ArrayList colAL=(ArrayList)tableResultAL.get(0);

					

					

				}else if(elemntAtZ.equalsIgnoreCase("H2")){

					if(headerLevel.intValue() == 2){

					String tempStr = tempAL.get(1).toString();

					tempStrHeaders=tempStrHeader1+","+tempStr;

					}

					/*if(headerLevel.intValue() == 2){

						if(rowLevelFlag){

							rowClass = "oddrow";

							rowLevelFlag=false;

						}else{

							rowClass = "evenrow";

							rowLevelFlag=true;

						}

						out.append ("<tr class="+rowClass+">");

						String tempStr = (String)(tempAL.get(1).toString());

						String[] tempStrHeader = new String[4];

						tempStrHeader = tempStr.split(":");

						String tempStrHeaderString = tempStrHeader[0].toString();

						out.append ("<td class=\"tableData\" align=\"left\">"+tempStrHeader[1]+"</td>");

					}*/

					

				}else if(elemntAtZ.equalsIgnoreCase("H3")){

				}else if(elemntAtZ.equalsIgnoreCase("H4")){

				}else{ 

					printTopHeaderForDisp2(tempAL);

				}

			}

			 else if(elemntAtZ.startsWith("D"))

				{

				for(int k=0;k<currHeaderColAL.size();k++){

						String str=currHeaderColAL.get(k).toString();

						int ind=Integer.parseInt(str);

						for(int j=0;j<falseHeaderColAL.size();j++){

							if(k==j){

								currColA[ind]=tempAL.get(Integer.parseInt(falseHeaderColAL.get(j).toString())).toString();

							}

						}

					}

				}

			 else if(elemntAtZ.startsWith("F")){

				if(elemntAtZ.equalsIgnoreCase("FN1")){

				}else if(elemntAtZ.equalsIgnoreCase("FN2")){

					if(headerLevel.intValue() == 2){

						printOperationsForDisp3WithoutTable(tempAL,currHeaderColAL,true,selectID,tempStrHeaders);

					}

				}	

				else{

					

					if(headerLevel.intValue() == 3){

						if(rowsPerPage!=null && rowsPerPage.length()>0)

							countRows++;

						if(tempStrHeaders==""){

							String elemntAtZ1 = "";

						

							for(int j=startIndx;j>0 && !elemntAtZ1.equalsIgnoreCase("H1");j--){

								ArrayList tempAL1 = (ArrayList)tableResultAL.get(j);

								elemntAtZ1 = tempAL1.get(0).toString();

								 if(elemntAtZ1.startsWith("H")){

									if(elemntAtZ1.equalsIgnoreCase("H1")){

										tempStrHeader1 = tempAL1.get(1).toString();

										tempStrHeaders = tempStrHeader1;

									}

								 }

							}

						}

								

						

							

						//selectID=printOperationsForDisp3WithoutTable(tempAL,currHeaderColAL,false,selectID,tempStrHeaders);

					    f3Count=f3Count1;

						f2Count=f2Count1;

						rowLevelFlag=flagForDisplay;

						printOperationsForDisp3WithoutTable(tempAL,currHeaderColAL,true,selectID,tempStrHeaders);

					}else if(headerLevel.intValue() == 4 && elemntAtZ.equalsIgnoreCase("FN3")){

					}else if(headerLevel.intValue() == 4 && elemntAtZ.equalsIgnoreCase("FN4")){

						if(rowsPerPage!=null && rowsPerPage.length()>0)

							countRows++;

						if(tempStrHeaders==""){

							String elemntAtZ1 = "";

						

							for(int j=startIndx;j>0 && !elemntAtZ1.equalsIgnoreCase("H1");j--){

								ArrayList tempAL1 = (ArrayList)tableResultAL.get(j);

								elemntAtZ1 = tempAL1.get(0).toString();

								 if(elemntAtZ1.startsWith("H")){

									if(elemntAtZ1.equalsIgnoreCase("H1")){

										tempStrHeader1 = tempAL1.get(1).toString();

										tempStrHeaders = tempStrHeader1;

									}

								 }

							}

						}

					//	selectID=printOperationsForDisp3WithoutTable(tempAL,currHeaderColAL,false,selectID,tempStrHeaders);

						f3Count=f3Count1;

						f2Count=f2Count1;

						rowLevelFlag=flagForDisplay;

						printOperationsForDisp3WithoutTable(tempAL,currHeaderColAL,true,selectID,tempStrHeaders);

					}

					

				}

			 }

		}

	}//end of if disp4

	if(userCSA.equalsIgnoreCase("disp4"))

	{

		hcount=-1;

		//out.append("<table width=\"40%\" name="+name+" id="+name+"   cols=\"3\"  cellpadding=\"1\" cellspacing=\"0\" class=\""+TableCSSName+"\" >");

		ArrayList colmnAL=(ArrayList)tableResultAL.get(0);

		

		if(rowsPerPage!=null && rowsPerPage.length()>0){

			rowsInPage = Integer.parseInt(rowsPerPage);

			countRows++;

			}

		System.out.println("rowsInPage=="+rowsInPage);

		hcount++;

		printColumnTableForDisp2((ArrayList)tableResultAL.get(0));

		

		for(int x = startIndx ; x < tableResultALSize && countRows<rowsInPage; x++){

			ArrayList tempAL = (ArrayList)tableResultAL.get(x);

			

			String elemntAtZ = tempAL.get(0).toString();

			//if(elemntAtZ.startsWith("C"))

			//{

			//	printSeperateTableTopHeaderForDisp2(tableResultAL);

			//	printColumnTablefordisp4(tempAL);

			//	printColumnTableForDisp2(tempAL);

			//}S

			

			if(elemntAtZ.startsWith("D"))

			{

			 if(rowsPerPage!=null && rowsPerPage.length()>0)

				countRows++;

				

				selectID=printDatarows(tempAL,colmnAL,false,selectID);

				rowLevelFlag=flagForDisplay;

				printDatarows(tempAL,colmnAL,true,selectID);

			}

			

			

		}

		out.append("</table>");

	}

}





public void printTopHeader(ArrayList tempAL) throws IOException{

	//className="layoutMaxTable";

	//className="tableTitle";

	String hName = (String)tempAL.get(0);



	if(hName.equalsIgnoreCase("H1")){

		className="H1lable";

	}else if(hName.equalsIgnoreCase("H2")){

		className="H2lable";

	}else if(hName.equalsIgnoreCase("H3")){

		className="H3lable";

	}



	String hVal = (String)tempAL.get(1);

	String[] colHeader = new String[4];

	colHeader = hVal.split(":");

	for (int z = 0;z<colHeader.length;z++) {

		for (int q = 0;q<constDBCol.length;q++) {

			if(constDBCol[q].equalsIgnoreCase(colHeader[z])){

				colHeader[z]=constDsplCol[q];

			}

		}

	}

	out.append ("<tr>");

	out.append ("<td class="+className+"><B>"+colHeader[0]+": "+colHeader[1]+"</B></td>");

}





public void printColumnTablefordisp4 (ArrayList xcolNames) throws IOException {



	int siz = xcolNames.size();

	out.append("<tr>");

	for (int q = 0;q<dispCol.length;q++) {

		for (int i = 1;i< siz-1;i++) {

			if(dispCol[q].equalsIgnoreCase(xcolNames.get(i).toString())){

			out.append ("<th class=\"tableheader\"><label  class=\"fontWeight\">");

				 if(isHeaderSortCol(q)){

					 out.append ("<a  onClick=\"javascript:pgnUrlSubmit(document.forms[0],'"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+((EbwForm)attrObj).getScreenName()+"','"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+sortKeys.get(dispCol[q]).toString()+"'"+ ");\">");

				 	 out.append (testCol[q]);

				 	out.append (sortImgSrcKeys.get(dispCol[q]).toString()+"</a>");

				 }

				 else

					 out.append (testCol[q]);

				out.append ("</label></th>");

			}

		}

	}

	out.append("</tr>");

	}





public void printReorderImage () throws IOException {

//	System.out.println("constdisplayCol[q]::::---->> "+constdisplayCol);

	out.append("<table width=\"50%\" class=\"grouptable\"><tr><td class=\"tableTitle\"></td><td align=\"right\" class=\"tablechecklink\" colspan=\"6\">");

//	out.append("<a href=\"#\" onClick=\"popupwindow=window.open(\'popup.jsp?dis="+false+"&availableCols="+availableColumns+"&visiblecols="+visibleColumns+"&presentAction="+presentActn+"&tablename="+name+"\','popupwindow','resizable=no,width=350,height=240,top=233,left=325,status=No')\"><img align=\"absmiddle\" src=\"../images/reorderactive.gif\" border=\"0\" alt=\"Reorder Columns\"></a>&nbsp");

//	out.append("<a href=\"#\" onClick=\"popupwindow=window.open(\'popupDiff.jsp?dis=false+&availableCols="+visibleColumns+"&visiblecols="+availableColumns+"&actnType=reorder&prevAction="+prevActn+"&prevState="+prevState+"&prevUrl="+prevUrl+"&tablename="+name+"\','popupwindow','resizable=no,width=350,height=240,top=233,left=325,status=No')\"><img align=\"absmiddle\" src=\"../images/reorderactive.gif\" border=\"0\" alt=\"Reorder Columns\"></a>&nbsp");

	out.append("<a href=\"#\" onClick=\"popupwindow=setToReorderParent(document.forms[0]);window.open(\'popup.jsp?dis=&availableCols="+availableColumns+"&visiblecols="+visibleColumns+"&presentAction="+"INIT"+"&tablename="+name+"\','popupwindow','resizable=no,width=350,height=240,top=233,left=325,status=No')\"><img align=\"absmiddle\" src=\"../images/reorderactive.gif\" border=\"0\" alt=\"Reorder Columns\"></a>&nbsp");

	out.append("</td>");

	out.append("</table>");

}



public String printOperationsForDisp3WithoutTable (ArrayList xOpValues,ArrayList currHeaderColAL,boolean flagForSelectID,String selectID,String headerVal) throws IOException{

	int len = xOpValues.size();

	int pCount=headerLevel.intValue()-1;

	

	String[] headerColVals = new String[8];

	headerColVals = headerVal.split(",");

	System.out.println("headerVal="+headerVal);

		

	String [] header1ColVal=new String[2];

	

	

	if(flagForSelectID){

		 if(headerLevel.intValue() != 2){

			if(rowLevelFlag){

				rowClass = "oddrow";

				rowLevelFlag=false;

			}else{

				rowClass = "evenrow";

				rowLevelFlag=true;

			}

			

			flagForDisplay=rowLevelFlag;

		 }

		}

	

	String strContent="";	

	String fnName = (String)xOpValues.get(0);

	if(fnName.equalsIgnoreCase("FN1")){

		className="f3Table";

	}else if(fnName.equalsIgnoreCase("FN2")){

		className="f1Table";

	}else if(fnName.equalsIgnoreCase("FN3")){

		className="f2Table";

	}	

	String dispRowVals="";

	

	if(headerLevel.intValue() != 2)

	{

	 if(flagForSelectID){

	//out.append ("<tr class="+rowClass+"  id="+name+ "_"+hcount+"_1"+"_"+count+">");

		 out.append ("<tr class="+rowClass+"  id="+name+ "_"+hcount+"_"+rowcount+">");

		  }

	}

	

	boolean markFlag = false,flag=false;

//	LinkedHashMap  = getColumnCSSMap(0,colHeaderAL.size()-1);

	String cssname="tableData";

	for(int x = 1; x < len ; x++){

		ArrayList opArray = (ArrayList)xOpValues.get(x);

		for(int z = 0; z < dispColIndx.size() ; z++){

			for(int d = 1; d < opArray.size() ; d++){

				String colOps = opArray.get(d).toString();

				if(d == Integer.parseInt(dispColIndx.get(z).toString())){

					if(colOps.equalsIgnoreCase("Null")){

					}else{

						markFlag = true;

					}

				}

			}

		}

		if(markFlag){

			boolean linkFlag = false;

			for(int t=0;t<headerColVals.length;t++){

				header1ColVal=headerColVals[t].split(":");

				

				for(int i=0;i < hrefLinkAL.size();i++){

					String headerString = (String)hrefLinkAL.get(i).toString();

				if(header1ColVal[0].equalsIgnoreCase(headerString))

					linkFlag =true;					

							

				}

			

			if(flagForSelectID){				

					if(linkFlag)

					{

						if(fieldAttrAL.get(header1ColVal[0])!=null && fieldAttrAL.get(header1ColVal[0])!="")

						{											

							strContent = fieldAttrAL.get(header1ColVal[0]).toString().replaceAll("~VARIABLE", header1ColVal[1]);

						}

						else

							strContent ="<a href=\"#\" onclick=\"javascript:formDrillDownHeaders(document.forms[0],'"+header1ColVal[0]+"','"+header1ColVal[1]+"','"+selectID+"');\">"+header1ColVal[1]+"</a>";

						  //  out.append ("<td class=\""+cssname+"\" align=\"left\"><B><u><a href=\"#\" onclick=\"javascript:formDrillDown(document.forms[0],'"+header1ColVal[0]+"','"+header1ColVal[1]+"','"+selectID+"');\">"+header1ColVal[1]+"</a></u></B></td>");

					}

					else					

						strContent = header1ColVal[1];					

					out.append ("<td class=\""+cssname+"\" align=\"left\">"+strContent+"</td>");

				}

			  

			}

			  

			

			boolean displayFlag = false;

			boolean oLinkFlag = false;

			while(pCount!=0){

			if(fnName.equalsIgnoreCase("FN3")){

				System.out.println("pCount="+pCount+"  *f3Count="+f3Count);

				System.out.println("secondHeaderFunctionAL.get(f3Count)="+secondHeaderFunctionAL.get(f3Count));

				String tempStr = (String)(secondHeaderFunctionAL.get(f3Count).toString());

				String[] tempStrHeader = new String[4];

				tempStrHeader = tempStr.split(":");

				String tempStrHeaderString = tempStrHeader[0].toString();

				for(int w=0;w < toDisplay.size();w++){

					String headerString = (String)toDisplay.get(w).toString();

					if(tempStrHeaderString.equalsIgnoreCase(headerString)){

						displayFlag =true;						

					}

					//System.out.println("  tempStrHeaderString="+tempStrHeaderString+

					//		"  headerString="+headerString+"  displayFlag="+displayFlag);

				}

				if(displayFlag){

				for(int i=0;i < hrefLinkAL.size();i++){

					String headerString = (String)hrefLinkAL.get(i).toString();

					if(tempStrHeaderString.equalsIgnoreCase(headerString)){

						oLinkFlag =true;

						

							

					}

				}

				if(flagForSelectID){

				boolean cssflag=false;

				int ind=colHeaderAL.indexOf(tempStrHeaderString);

				for(int h=0;h<dispColIndx.size();h++){

					if(ind==Integer.parseInt(dispColIndx.get(h).toString())){

						cssflag=true;

						ind=h;

					}

				}

				if(cssflag){

					if(columnCSSMap.get((dispColIndx.get(ind).toString()))!=null 

							&& columnCSSMap.get((dispColIndx.get(ind).toString()))!="" 

								&& !columnCSSMap.get((dispColIndx.get(ind).toString())).equals(""))

						cssname=columnCSSMap.get((dispColIndx.get(ind).toString())).toString();

				}		

				

				if(oLinkFlag){

					if(fieldAttrAL.get(tempStrHeaderString)!=null && fieldAttrAL.get(tempStrHeaderString)!="")

					{											

						strContent = fieldAttrAL.get(tempStrHeaderString).toString().replaceAll("~VARIABLE", tempStrHeader[1]);

					}

					else

						strContent ="<a href=\"#\" onclick=\"javascript:formDrillDownHeaders(document.forms[0],'"+tempStrHeader[0]+"','"+tempStrHeader[1]+"','"+selectID+"');\">"+tempStrHeader[1]+"</a>";

				}

				else

					strContent =tempStrHeader[1];

				//out.append ("<td class=\""+cssname+"\" align=\"left\"><B><u><a href=\"#\" onclick=\"javascript:formDrillDown(document.forms[0],'"+tempStrHeader[0]+"','"+tempStrHeader[1]+"','"+selectID+"');\">"+tempStrHeader[1]+"</a></u></B></td>");

				

					out.append ("<td class=\""+cssname+"\" align=\"left\">"+strContent+"</td>");

			

			}

				

				for(int t=0;t<headerColVals.length;t++){

					header1ColVal=headerColVals[t].split(":");

					if(dispRowVals=="")

						dispRowVals=header1ColVal[1];

					else

					dispRowVals=dispRowVals+"~"+header1ColVal[1];

				}

				

				

				if(dispRowVals=="")

					dispRowVals=tempStrHeader[1];

				else

				dispRowVals=dispRowVals+"~"+tempStrHeader[1];

				} 

				f3Count++;

				displayFlag=false;

			}

			pCount--;

			}

			while(pCount!=0){

			if(fnName.equalsIgnoreCase("FN4")){

				for(int z =0; z< 2;z++){

					String tempStr = (String)(secondHeaderFunctionAL.get(f3Count).toString());

					String[] tempStrHeader = new String[4];

					tempStrHeader = tempStr.split(":");

					String tempStrHeaderString = tempStrHeader[0].toString();

					for(int w=0;w < toDisplay.size();w++){

						String headerString = (String)toDisplay.get(w).toString();

						if(tempStrHeaderString.equalsIgnoreCase(headerString)){

							displayFlag =true;						

						}

						//System.out.println("  tempStrHeaderString="+tempStrHeaderString+

						//		"  headerString="+headerString+"  displayFlag="+displayFlag);

					}

					if(displayFlag){

					for(int i=0;i < hrefLinkAL.size();i++){

						String headerString = (String)hrefLinkAL.get(i).toString();

						if(tempStrHeaderString.equalsIgnoreCase(headerString)){

							oLinkFlag =true;

							

								

						}

					}

					if(flagForSelectID){

					boolean cssflag=false;

					int ind=colHeaderAL.indexOf(tempStrHeaderString);

					for(int h=0;h<dispColIndx.size();h++){

						if(ind==Integer.parseInt(dispColIndx.get(h).toString())){

							cssflag=true;

							ind=h;

						}

					}

					if(cssflag){

						if(columnCSSMap.get((dispColIndx.get(ind).toString()))!=null 

								&& columnCSSMap.get((dispColIndx.get(ind).toString()))!="" 

									&& !columnCSSMap.get((dispColIndx.get(ind).toString())).equals(""))

							cssname=columnCSSMap.get((dispColIndx.get(ind).toString())).toString();

					}		

					

					if(oLinkFlag){

						if(fieldAttrAL.get(tempStrHeaderString)!=null && fieldAttrAL.get(tempStrHeaderString)!="")

						{											

							strContent = fieldAttrAL.get(tempStrHeaderString).toString().replaceAll("~VARIABLE", tempStrHeader[1]);

						}

						else

							strContent ="<a href=\"#\" onclick=\"javascript:formDrillDownHeaders(document.forms[0],'"+tempStrHeader[0]+"','"+tempStrHeader[1]+"','"+selectID+"');\">"+tempStrHeader[1]+"</a>";

					}

					else

						strContent =tempStrHeader[1];

					//out.append ("<td class=\""+cssname+"\" align=\"left\"><B><u><a href=\"#\" onclick=\"javascript:formDrillDown(document.forms[0],'"+tempStrHeader[0]+"','"+tempStrHeader[1]+"','"+selectID+"');\">"+tempStrHeader[1]+"</a></u></B></td>");

					

						out.append ("<td class=\""+cssname+"\" align=\"left\">"+strContent+"</td>");

				

				}

					

					for(int t=0;t<headerColVals.length;t++){

						header1ColVal=headerColVals[t].split(":");

						if(dispRowVals=="")

							dispRowVals=header1ColVal[1];

						else

						dispRowVals=dispRowVals+"~"+header1ColVal[1];

					}

					

					

					if(dispRowVals=="")

						dispRowVals=tempStrHeader[1];

					else

					dispRowVals=dispRowVals+"~"+tempStrHeader[1];

					} 

					f3Count++;

					displayFlag=false;

				}

				

				

			}

			pCount--;

			}

			boolean linkFlagVal = false;

			int i=0;

			for(int z = 0; z < dispColIndx.size() ; z++){

				for(int d = 1; d < opArray.size() ; d++){

					String colOps = (String)opArray.get(d).toString();

					String dataType = "BigDecimal";

					if(d == Integer.parseInt(dispColIndx.get(z).toString())){

						for(int f=0;f < hrefLinkAL.size();f++){

							String headerString = (String)hrefLinkAL.get(f).toString();

							if(dispCol[z].equalsIgnoreCase(headerString)){

								linkFlagVal =true;

							}		    			

						}

						if(colOps.equalsIgnoreCase("Null")){

						}else{							

							int a=Integer.parseInt(dispColIndx.get(z).toString());						

						//this is 4 currency concatinating. ......	

							for(int f = 0; f< currHeaderColAL.size() ; f++){

								int b=Integer.parseInt(currHeaderColAL.get(f).toString());

								if(a==b){

										flag=true;									

										i=b;										

									}

								} 							

							if(columnCSSMap.get(Integer.toString(a))!=null &&

									columnCSSMap.get(Integer.toString(a))!="" &&

									!columnCSSMap.get(Integer.toString(a)).equals("")){

								cssname=columnCSSMap.get(Integer.toString(a)).toString();							

							}				

						

							if((fnName.equalsIgnoreCase("FN2"))){

								String colOpsStr = ConvertionUtil.convertToReqCol(colOps,dataType);

								for(int w=0;w < toDisplay.size();w++){

									String headerString = (String)toDisplay.get(w).toString();

									if(dispCol[z].toString().equalsIgnoreCase(headerString)){

										displayFlag =true;

									}

								}

								if(displayFlag==true){

								if(flagForSelectID==true){  

									if(linkFlag){										

										if(fieldAttrAL.get(dispCol[z])!=null && fieldAttrAL.get(dispCol[z])!="")

											strContent = fieldAttrAL.get(dispCol[z]).toString().replaceAll("~VARIABLE", colOpsStr);

										else

											strContent =  colOpsStr;

									}

									else{

										strContent =  colOpsStr;

									}

									System.out.println("linkFlag="+linkFlag+"  colOpsStr ="+colOpsStr+"  strContent="+strContent);

										out.append ("<td class=\""+cssname+"\" align=\"right\">"+strContent+"</td>");							 

									}

										dispRowVals=dispRowVals+"~"+colOpsStr;

								}

							}

			

						    if((fnName.equalsIgnoreCase("FN3") || fnName.equalsIgnoreCase("FN4"))){

						    	String colOpsStr = ConvertionUtil.convertToReqCol(colOps,"SignedCurrencyAmount");

								String temp3Str2 = colOpsStr;

								String curr="";

								if(flag==true){

									flag=false;

									if(i!=0)

										temp3Str2=currColA[i]+" "+temp3Str2;

								}

							  	dispRowVals=dispRowVals+"~"+temp3Str2;

							  	

								if(flagForSelectID==true){

									if(linkFlagVal){

										

										if(fieldAttrAL.get(dispCol[z])!=null && fieldAttrAL.get(dispCol[z])!=""){

											

											strContent = fieldAttrAL.get(dispCol[z]).toString().replaceAll("~VARIABLE", temp3Str2);

										}

									else{

												strContent =  temp3Str2;

										}

										out.append ("<td class=\""+cssname+"\" align=\"right\">"+strContent+"</td>");

								f2Count++;

								}								

								//f2Flag = false;

							} 

						}

					}

				}

			}

		}

		}

	}



	out.append("<td class=\"hidden\" >"+dispRowVals+"</td>");



	out.append ("</tr>");

	f3Count1=f3Count;

	f2Count1=f2Count;

	return dispRowVals;

}



public String printDatarows(ArrayList tempAL,ArrayList colmnAL,boolean flagForSelectID,String selectID)throws IOException{

	int len = tempAL.size();

	String strContent="";

	if(flagForSelectID){

	if(rowLevelFlag){

		rowClass = "oddrow";

		rowLevelFlag=false;

	}else{

		rowClass = "evenrow";

		rowLevelFlag=true;

	}

	flagForDisplay=rowLevelFlag;

	}

	String dispRowVals="";

	 out.append ("<tr class="+rowClass+"  id="+name+ "_"+hcount+"_"+(++rowcount)+">");

	

	String columnName="";

	String tempStr="";

	String str="";

	String cssname="tableData";

	boolean flagDisp=false;

	boolean markFlag = false;

	boolean oLinkFlag = false;

	String sss="";

	for(int a=0;a<dispColIndx.size();a++)

	{

        int indexx=Integer.parseInt(dispColIndx.get(a).toString());

        if(tempAL.get(indexx)!=null)

        	sss=tempAL.get(indexx).toString();

        else

        	sss="0";

		//String ss2=colmnAL.get(indexx).toString();

		//String sss=ss1;

		str=colmnAL.get(indexx).toString();

		for(int i=0;i < hrefLinkAL.size();i++){

			String headerString = (String)hrefLinkAL.get(i).toString();

			if(str.equalsIgnoreCase(headerString)){

				oLinkFlag =true;

				

			}

		}

	

		for(int w=0;w < toDisplay.size();w++){

			String headerString = (String)toDisplay.get(w).toString();

			if(str.equalsIgnoreCase(headerString)){

				markFlag =true;

				for(int d=0;d < toDouble.size();d++){

					String doubleString = (String)toDouble.get(d).toString();

					if(str.equalsIgnoreCase(doubleString)){

					/*	double da=java.lang.Math.round(Double.parseDouble(sss));

						String s="";

						sss=s+da;

						sss.trim();*/

						sss=ConvertionUtil.convertToReqCol(sss, "SignedCurrencyAmount");

					}

				}

			}

		}

		

			

		if(markFlag==true)

		{

		  if(flagForSelectID==true){

			  boolean cssflag=false;

				int ind=colHeaderAL.indexOf(str);				

				for(int h=0;h<dispColIndx.size();h++){

					if(ind==Integer.parseInt(dispColIndx.get(h).toString())){

						cssflag=true;

						ind=h;

					}

				}

				if(cssflag){

					if(columnCSSMap.get((dispColIndx.get(ind).toString()))!=null &&

							columnCSSMap.get((dispColIndx.get(ind).toString()))!="" &&

							!columnCSSMap.get((dispColIndx.get(ind).toString())).equals(""))

						cssname=columnCSSMap.get((dispColIndx.get(ind).toString())).toString();

				

				}

			if(oLinkFlag){

				if(fieldAttrAL.get(str)!=null && fieldAttrAL.get(str)!="")

				{											

					strContent = fieldAttrAL.get(str).toString().replaceAll("~VARIABLE", sss);

				}

				else

					strContent ="<a href=\"#\" onclick=\"javascript:formDrillDownHeaders(document.forms[0],'"+str+"','"+sss+"','"+selectID+"');\">"+sss+"</a>";

				

				oLinkFlag= false;

				//out.append ("<td class=\""+cssname+"\" align=\"left\"><B><u><a href=\"#\" onclick=\"javascript:formDrillDown(document.forms[0],'"+str+"','"+sss+"','"+selectID+"');\">"+sss+"</a></u></B></td>");

			}else

				strContent = sss;

			if(toDouble.indexOf(str)==-1)	

				out.append ("<td class=\""+cssname+"\" align=\"left\">"+strContent+"</td>");

			else

				out.append ("<td class=\""+cssname+"\" align=\"right\">"+strContent+"</td>");

			

			}

			if(dispRowVals=="")

				dispRowVals=sss;

			else

			dispRowVals=dispRowVals+"~"+sss;

			markFlag = false;

		}

	

		flagDisp=false;

	}

	out.append("<td class=\"hidden\" >"+dispRowVals+"</td>");

	out.append ("</tr>");

	return dispRowVals;

}









public void printSeperateTableforDataDisp2 (ArrayList tableResultAL) throws IOException {

	int 	tableResultALSize = tableResultAL.size();

	//printReorderImage();

boolean myFlag=true;

//boolean tsumcoldisp = false;

	for(int x = 0 ; x < tableResultALSize ; x++){

		ArrayList tempAL = (ArrayList)tableResultAL.get(x);

		String elemntAtZ = tempAL.get(0).toString();

		

		if(elemntAtZ.startsWith("C")){

			printColumnNamesHeading(tempAL);

		}else if(elemntAtZ.startsWith("D")){

			rowFlag=false;

			if(myFlag){

				className="evenrow";

			}else{

				className="oddrow";

			}

			printRows(tempAL);

			myFlag = !myFlag;

		}

		else if(elemntAtZ.startsWith("F")){

			

			ArrayList firstOp=(ArrayList)tempAL.get(1);

			if(!firstOp.get(0).toString().equalsIgnoreCase("Sum"))

			{

			printOperations(tempAL);

			}

		}

		if(tableHeader.get(0).toString().equalsIgnoreCase("dataDispX")){

			if(elemntAtZ.startsWith("X")){

				

				ArrayList firstOp=(ArrayList)tempAL.get(1);

				

				printXRow(tempAL);

				

			}

		}

	}

}



public void printXRow (ArrayList xOpValues) throws IOException{

	int len = xOpValues.size();

	//className="layoutMaxTable";

	String fnName = (String)xOpValues.get(0);

	if(fnName.equalsIgnoreCase("X")){

		if(tableHeader.get(0).toString().equalsIgnoreCase("dataDispX"))

		className="evenrow";

		else

			className="fXTable";

	 }	

	boolean markFlag = false;

	for(int x = 1; x < len ; x++){

		ArrayList opArray = (ArrayList)xOpValues.get(x);

		for(int z = 0; z < dispColIndx.size() ; z++){

			for(int d = 1; d < opArray.size() ; d++){

				String colOps = opArray.get(d).toString();

				if(d == Integer.parseInt(dispColIndx.get(z).toString())){

					if(colOps.equalsIgnoreCase("Null")){

					}else{

						markFlag = true;

					}

				}

			}

		}

		if(markFlag){

			out.append ("<tr><td></td></tr>");

			out.append ("<tr class="+className+">");

			if(tableHeader.get(0).toString().equalsIgnoreCase("dataDispX")){

				 for(int j=0;j<dispColIndx.size()-4;j++)

				 {

					 className="oddrow";

				    	out.append ("<td  class="+className+"></td>");

				 }

				 className="evenrow";

			    //out.append ("<td class=\"tableheader\" align=\"left\">"+opArray.get(0)+"</td>");

				 out.append ("<th  class="+className+" align=\"left\">"+(String)formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." +((opArray.get(0).toString()).replaceAll(" ","")))+"</th>");

			}

			else

			//out.append ("<table>");

			out.append ("<td class="+className+" align=\"left\">"+opArray.get(0)+"</td>");

			//out.append ("<td class="+className+"></td>");

			for(int z = 0; z < dispColIndx.size() ; z++){

				for(int d = 1; d < opArray.size() ; d++){

					String colOps = opArray.get(d).toString();

					if(d == Integer.parseInt(dispColIndx.get(z).toString())){

						if(colOps.equalsIgnoreCase("Null")){

							if(!tableHeader.get(0).toString().equalsIgnoreCase("dataDispX"))

							{

							out.append ("<td class="+className+"></td>");

							}

							

						}else{

							out.append ("<td  align=\"right\">"+ConvertionUtil.convertToReqCol(colOps, "BigDecimal")+"</td>");

						}

					}

				}

			}

			//out.append ("</table>");

		}

	}

	out.append ("</tr><tr><td></td></tr>");

}





public void printOperations (ArrayList xOpValues) throws IOException{

	int len = xOpValues.size();

	//className="layoutMaxTable";

	String fnName = (String)xOpValues.get(0);

	//className="layoutMaxTable";

	if(fnName.equalsIgnoreCase("FN1")){

		className="f3Table";

	}else if(fnName.equalsIgnoreCase("FN2")){

		className="f1Table";

	}else if(fnName.equalsIgnoreCase("FN3")){

		className="f2Table";

	}

	



	boolean markFlag = false;

	for(int x = 1; x < len ; x++){

		ArrayList opArray = (ArrayList)xOpValues.get(x);

		for(int z = 0; z < dispColIndx.size() ; z++){

			for(int d = 1; d < opArray.size() ; d++){

				String colOps = opArray.get(d).toString();

				if(d == Integer.parseInt(dispColIndx.get(z).toString())){

					if(colOps.equalsIgnoreCase("Null")){

					}else{

						markFlag = true;

					}

				}

			}

		}



		if(markFlag){

			if(tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2"))

			{

				out.append ("<tr></tr>");

			    out.append ("<tr></tr>");

			    className="evenrow";

			}

			if(tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2"))

			 out.append ("<tr class="+className+">");

			else

			 out.append ("<tr>");

			if(tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2")){

				className="oddrow";

				 for(int j=0;j<len-1;j++)

				    	out.append ("<td class="+className+"></td>");

				 className="evenrow";

			   //out.append ("<td class=\"tableheader\" align=\"left\">"+opArray.get(0)+"</td>");

				 out.append ("<th  class="+className+"  align=\"left\">"+(String)formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." +((opArray.get(0).toString()).replaceAll(" ","")))+"</th>");;

				    	

			}

			else

				out.append ("<td class="+className+" align=\"left\">"+opArray.get(0)+"</td>");

			//out.append ("<td class="+className+"></td>");

			for(int z = 0; z < dispColIndx.size() ; z++){

				for(int d = 1; d < opArray.size() ; d++){

					String colOps = opArray.get(d).toString();

					if(d == Integer.parseInt(dispColIndx.get(z).toString())){

						if(colOps.equalsIgnoreCase("Null")){

							if(!tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2"))

							 out.append ("<td class="+className+"></td>");

							

						}else{

							//out.append ("<td class="+className+" align=\"right\">"+colOps+"</td>");

							out.append ("<td  align=\"right\">"+ConvertionUtil.convertToReqCol(colOps, "BigDecimal")+"</td>");

						}

					}

				}

			}

		}

	}

	out.append ("</tr><tr><td></td></tr>");

}



public void printColumnNamesHeading (ArrayList xcolNames) throws IOException {

	int siz = xcolNames.size();

	int l = dispCol.length-2;

	int j=0;

	if(tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2") || tableHeader.get(0).toString().equalsIgnoreCase("dataDispX"))

		out.append("<table name="+name+" id="+name+"  cols="+l+" width=\"100%\" class=\""+TableCSSName+"\" >");

	else

		out.append("<table name="+name+" id="+name+"  width=\"100%\" border=\"0\" class=\"columnheader\">");

	

	out.append("<tr>");

	if(!tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2")&& !tableHeader.get(0).toString().equalsIgnoreCase("dataDispX"))

	 out.append ("<td class=\"tableheader\"></td>");

	if(tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2") || tableHeader.get(0).toString().equalsIgnoreCase("dataDispX"))

		j=1;

	

	for (int q = j;q<dispCol.length-1;q++) {

		for (int i = 1;i< siz;i++) {

			

			if(dispCol[q].equalsIgnoreCase(xcolNames.get(i).toString()) ){

				

				dispColIndx.add(new Integer(i));

				if(tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2") || tableHeader.get(0).toString().equalsIgnoreCase("dataDispX"))

					out.append ("<th align=\"center\">"+testCol[q]+"</th>");

				else

					out.append ("<td class=\"tableheader\">"+testCol[q]+"</td>");

			}

		}

	}

	out.append("</tr>");

	}

public void printRows (ArrayList xcolValues) throws IOException{

	int len = xcolValues.size();

	//System.out.println("toDouble=="+toDouble);

	

	//int j=0;

	//if(tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2"))

	//	j=1;

	if(rowFlag){

	out.append ("<tr>");

	if(!tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2") && !tableHeader.get(0).toString().equalsIgnoreCase("dataDispX"))

	 out.append ("<td class=\"tableData\"></td>");

	

	for (int q = 0;q<dispColIndx.size();q++) {

		boolean linkFlag=false;

		for(int w=0;w < toDouble.size();w++){

			String colStr = (String)toDouble.get(w).toString();

			if(colStr.equalsIgnoreCase(dispCol[q+1])){

				linkFlag =true;

			}					

		}

		for (int i = 1;i< len;i++){

			int dCIndx =Integer.parseInt(dispColIndx.get(q).toString());

			if(i == dCIndx)	{

				if(!linkFlag){

				//if((xcolValues.get(i).getClass().toString()).equalsIgnoreCase("class java.lang.String")){

					if(!tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2") && !tableHeader.get(0).toString().equalsIgnoreCase("dataDispX"))

						  out.append ("<td class=\"tableData\">"+xcolValues.get(i)+"</td>");

						else

							 out.append ("<td class=\"tableData\" align=\"center\">"+xcolValues.get(i)+"</td>");

				}else {

					out.append ("<td class=\"tableData\" align=\"right\">"+ConvertionUtil.convertToReqCol(xcolValues.get(i).toString(), "BigDecimal")+"</td>");

				}

			}

		}

	}

	

	rowFlag = false;

	out.append ("</tr></tr>");

	}else{

		out.append ("<tr class="+className+" >");

		if(!tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2") && !tableHeader.get(0).toString().equalsIgnoreCase("dataDispX"))

		 out.append ("<td class=\"tableData2\"></td>");

		for (int q = 0;q<dispColIndx.size();q++) {

			boolean linkFlag=false;

			for(int w=0;w < toDouble.size();w++){

				String colStr = (String)toDouble.get(w).toString();

				if(colStr.equalsIgnoreCase(dispCol[q+1])){

					linkFlag =true;

				}					

			}

			//System.out.println("Linkflag="+linkFlag+" displCol="+dispCol[q+1]);

			for (int i = 1;i< len;i++){					

				

				int dCIndx =Integer.parseInt(dispColIndx.get(q).toString());

				if(i == dCIndx)	{

				//	System.out.println("xcolValues.get(i)=="+xcolValues.get(i));

					String data=xcolValues.get(i)==null?"":(String)xcolValues.get(i);

					

					if(!linkFlag){

						

						//System.out.println("Nithya Linkflag="+linkFlag+" displCol="+dispCol[q]+ "xcolValues.get(i)="+xcolValues.get(i));

					//if((xcolValues.get(i).getClass().toString()).equalsIgnoreCase("class java.lang.String")){

						//System.out.println("Class Name of the Object:::----->"+xcolValues.get(i).getClass());

						if(!tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2") && !tableHeader.get(0).toString().equalsIgnoreCase("dataDispX"))

						  out.append ("<td class=\"tableData2\">"+xcolValues.get(i)+"</td>");

						else

							 out.append ("<td class=\"tableData2\" align=\"center\">"+data+"</td>");

					}else {

						String dataAmt=ConvertionUtil.convertToReqCol((String)xcolValues.get(i), "BigDecimal")==null?"":ConvertionUtil.convertToReqCol((String)xcolValues.get(i), "BigDecimal");

						//System.out.println("Tarun Linkflag="+linkFlag+" displCol="+dispCol[q]+ "xcolValues.get(i)="+xcolValues.get(i));

						//out.append ("<td class=\"tableData2\" align=\"right\">"+ConvertionUtil.convertToReqCol(xcolValues.get(i).toString(), "BigDecimal")+"</td>");

						out.append ("<td class=\"tableData2\" align=\"right\">"+dataAmt+"</td>");

						//linkFlag=false;

					}

				}

			}

		}

		if(!tableHeader.get(0).toString().equalsIgnoreCase("dataDisp2"))

		rowFlag = true;

		out.append ("</tr></tr>");

	}

}



public void printSeperateTableTopHeaderForDisp2(ArrayList tableResultAL) throws IOException {

	int	pCount = 2;

	boolean currencyF = true;

	boolean currencyF1 = true;

	ArrayList hFValAL = new ArrayList();

	ArrayList colAL = new ArrayList();

	String tempStr="";

	boolean flagDisp=false;

	String secCcyVal="";

	String m=new String();

	colAL =(ArrayList)headerFunctionAL.get(0);

	ArrayList head=new ArrayList();

	if(hFCount != 1){

		out.append("</td>");

		out.append("</tr>");

	out.append("</table>");

	out.append("</table>");

	}

	out.append("<table>");

	out.append ("<tr><td></td></tr>");

	out.append ("<tr><td></td></tr>");

	out.append ("<tr><td></td></tr>");

	out.append ("<tr><td></td></tr>");

	out.append("</table>");

	//out.append("<table  name=\"TableTopHeader\" id=\"TableTopHeader\"   border=\"0\" >");

	out.append("<table   name="+name+" id="+name+hcount+"    cellpadding=\"0\" cellspacing=\"0\"  style=\"border:0;\" width=\""+getTableWidth()+"\">");

	for(int x = hFCount ; x < headerFunctionAL.size() ; x++){

		hFValAL = (ArrayList)headerFunctionAL.get(x);

		if(pCount != 0){

			String hIndx = (String)hFValAL.get(0);

			

			if(hIndx.startsWith("H")){

				String hVal = (String)hFValAL.get(1);

				String[] colHeader = new String[4];

				colHeader = hVal.split(":");

				String colHeaderString = colHeader[0].toString();

				for (int z = 0;z<colHeader.length;z++) {

					for (int q = 0;q<constDBCol.length;q++) {

						if(constDBCol[q].equalsIgnoreCase(colHeader[z])){

							colHeader[z]=constDsplCol[q];

						}

					}

				}

			

			

				boolean linkFlag = false;

				//boolean tagLinkFlag = false;

				for(int i=0;i < hrefLinkAL.size();i++){

					String headerString = (String)hrefLinkAL.get(i).toString();

					if(colHeaderString.equalsIgnoreCase(headerString)){

						linkFlag =true;

					}		    			

				}

				

				String[] colHeaderVal=new String[4];

				colHeaderVal=colHeader[1].split("~");

				

				String colSuperName =formname.substring(0, formname.lastIndexOf("Form"));

				

				

				out.append("<tr id="+name+ "_"+hcount+ ">");

				out.append ("<td class=\"H1lable\" colspan = "+(toDisplay.size()-1)+" style=\"BACKGROUND-COLOR:white\"   align=\"left\">");

				if(isNestedTable)

						out.append ("<a href=\"#\" onclick=\"treetable_toggleRow('" + name + "_" + hcount + "');\"><img border=\"0\" src=\"../images/minus.gif\" /></a>");

				if(linkFlag){

					

					if(fieldAttrAL.get(colHeaderString)!=null && fieldAttrAL.get(colHeaderString)!=""){

						String strContent="";

						if(colHeaderVal.length>1)

							strContent = fieldAttrAL.get(colHeaderString).toString().replaceAll("~VARIABLE", colHeaderVal[1]);

						else

							strContent = fieldAttrAL.get(colHeaderString).toString().replaceAll("~VARIABLE", colHeaderVal[0]);

						

						out.append (strContent);

					}

					else{//If Tag Content is not defined default is Formdrilldown js function

					//out.append ("<td class=\"H1lable\"><B><u><a href=\"#\" onclick=\"javascript:formDrillDown(document.forms[0],'"+colSuperName+"','"+colHeaderString+"','"+colHeader[1]+"');\">"+colHeader[1]+"</a></u></B></td>");

					if(colHeaderVal.length>1)

							out.append ("<B><u><a href=\"#\" onclick=\"javascript:formDrillDown(document.forms[0],'"+colSuperName+"','"+colHeaderString+"','"+colHeader[1]+"');\">"+colHeaderVal[1]+"</a></u></B>");

					else

						out.append ("<B><u><a href=\"#\" onclick=\"javascript:formDrillDown(document.forms[0],'"+colSuperName+"','"+colHeaderString+"','"+colHeader[1]+"');\">"+colHeaderVal[0]+"</a></u></B>");

					}

						

				}else{

					//out.append ("<td class=\"H1lable\">"+colHeader[1]+"</td>");

					if(colHeaderVal.length>1)

					     out.append (colHeaderVal[1]);

					else

					     out.append (colHeaderVal[0]);

					

				}

				out.append ("<td>");

				out.append("</tr>");

				head.add(colHeader[1].toString());

			

			

			}

			if(hIndx.startsWith("F")){

				for (int q = 1;q<hFValAL.size();q++) {

					ArrayList fValAL = new ArrayList();

					fValAL = (ArrayList)hFValAL.get(q);

					out.append("<tr id="+name+ "_"+hcount+"_"+(++rowcount)+" style=\"BACKGROUND-COLOR:white\" colspan = "+(toDisplay.size()-1)+">");

					

					

					if(tsummarydispColConst!=null  && dispColConst!=null)

					{

						for(int j=0;j<tsummarydispColConst.length;j++)

						{

							

									String lable=(String)formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." +tsummarydispColConst[j].trim()+"."+"tablesummary");

									if(fxRatesrcTrgtVal.get(hcount)!=null)

										lable=MessageFormat.format(lable, (fxRatesrcTrgtVal.get(hcount).toString().split(";"))[0]);

									ArrayList temp1=(ArrayList)tableResultAL.get(0);

									int trgtindex=temp1.indexOf(tsummarydispColConst[j].trim());

									String str=fValAL.get(trgtindex).toString();

									str=ConvertionUtil.convertToReqCol(str, " SignedCurrencyAmount");

									out.append ("<td  style=\"BACKGROUND-COLOR:white;padding-bottom:4px\"   align=\"left\"><label>"+lable + str +"</label></td>");

						}

						if(fxrate!=null){

						//	if(userCSA.equalsIgnoreCase("disp2"))

							//{

								out.append("<tr id="+name+ "_"+hcount+"_"+(++rowcount)+" style=\"BACKGROUND-COLOR:white\" colspan = "+(toDisplay.size()-1)+">");

								out.append ("<td   style=\"BACKGROUND-COLOR:white;padding-bottom:4px\"  " +

										" align=\"left\"><label>"+"Indicative FxRate "+fxRateConst.get(hcount)+"</label></td>");

								out.append("</tr>");

							//out.append("<tr>");

						    //secCcyVal =(String)formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." +(head.get((head.size()-1)))+"."+"SECURITYCCY");

							//String constant =(String)formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." +(head.get((head.size()-1)))+"."+"Constant");

							//out.append("<td class=\"H2lable\" align=\"left\">FX rate "+ secCcyVal +" 1 "+" = "+" USD "+constant);

							//}

						}

					}

					out.append("</tr>");

				}

				

			}

			pCount--;

		}else{ 

			hFCount = x;

			break;

		}

		

	}

	

	if( !userCSA.equalsIgnoreCase("disp2"))

		out.append("</table>");

	printColumnTableForDisp2(colAL);

	//currencyFlag++;

	flagForDisplay=false;

}

public void printColumnTableForDisp2 (ArrayList xcolNames) throws IOException {

	//for(int i=0;i<dispCol.length;i++)

		//System.out.println("dispCol["+i+"]=="+dispCol[i]);

	//System.out.println("dispColIndx=="+dispColIndx);

	int siz = xcolNames.size();

	String tableHeading = new String();

	String tempStr="";

	boolean flagDisp=false;

	rowFlag= false;

	String cssName="tableheader";

	//if( !userCSA.equalsIgnoreCase("disp2"))

	//out.append("<table  	name="+name+" id="+name+"   cols=\"3\"  cellpadding=\"1\" cellspacing=\"0\" class=\""+TableCSSName+"\" width=\""+getTableWidth()+"\">");

	if( userCSA.equalsIgnoreCase("disp2")){

		out.append("<tr id="+name+ "_"+hcount+"_"+(++rowcount)+">");

		out.append("<td colspan = "+(toDisplay.size()-1)+">");	

	    out.append("<table name="+name+" id="+name+ "_"+hcount+"_"+(++rowcount)+"   cellpadding=\"0\" cellspacing=\"0\" class=\""+TableCSSName+"\" style=\"border:0;\" width=\"100%\">");	

	}

	if( !userCSA.equalsIgnoreCase("disp2"))

		out.append("<table name="+name+" id="+name+ "_"+hcount+"_"+(++rowcount)+"   cellpadding=\"0\" cellspacing=\"0\" class=\""+TableCSSName+"\" style=\"border:0;\" width=\""+getTableWidth()+"\">");

	out.append("<tr id="+name+ "_"+hcount+"_"+(++rowcount)+">");

	String columnName = null;

	String nonDisplayColumn="";

	String spanDisplbl="";

	int n;

	if(userCSA.equalsIgnoreCase("disp3") || userCSA.equalsIgnoreCase("disp4"))

		n=0;

	else

		n=1;

	

	



//	LinkedHashMap columnCSSMap = getColumnCSSMap(0,colHeaderAL.size()-2);

	try{

	if(colSpan.isEmpty() && spanDispLabel.isEmpty()){

		System.out.println("colspan is empty");

	for (int q = n;q<dispCol.length-1;q++) {		

		

		if(columnCSSMap.get((dispColIndx.get(q).toString()))!=null)

			cssName=columnCSSMap.get((dispColIndx.get(q).toString())).toString();		

		for (int i = 0;i< siz;i++) {			

			if(dispCol[q].equalsIgnoreCase(xcolNames.get(i).toString())){

				columnName= (String)xcolNames.get(i).toString();

				boolean oLinkFlag = false;

				for(int w=n;w < toDisplay.size();w++){

					String headerString = (String)toDisplay.get(w).toString();

					if(columnName.equalsIgnoreCase(headerString)){

						oLinkFlag =true;

					}

				}

				

				

				if(headerLevel.intValue()!=2)

				{

					if(!falseHeaderAL.isEmpty())

				  nonDisplayColumn = (String)falseHeaderAL.get(0).toString();

				  

				}

				if(columnName.equalsIgnoreCase(nonDisplayColumn) || columnName.equalsIgnoreCase(nonDisplayColumn) || columnName.equalsIgnoreCase("Sum") || oLinkFlag==false){

				}

				else{					

					for(int j=0;j<constDBCol.length;j++)

					{

						if(constDBCol[j].equalsIgnoreCase(columnName))

						{

							 tempStr = (String)constDsplCol[j].toString();

							 if(fxRatesrcTrgtVal.get(hcount)!=null)

								 tempStr = MessageFormat.format(tempStr, (fxRatesrcTrgtVal.get(hcount).toString().split(";"))[0]);

							 flagDisp=true;

						}					

					}					

				if(flagDisp)

				{

					out.append ("<th class=\""+TableCSSName+"\" ><label  class=\"fontWeight\">");

					 if(isHeaderSortCol(q)){

						 out.append ("<a  onClick=\"javascript:pgnUrlSubmit(document.forms[0],'"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+((EbwForm)attrObj).getScreenName()+"','"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+sortKeys.get(dispCol[q]).toString()+"'"+ ");\">");

					 	 out.append (tempStr);

					 	out.append (sortImgSrcKeys.get(dispCol[q]).toString()+"</a>");

					 }

					 else

				out.append(tempStr);

				out.append ("</label></th>");

				flagDisp=false;

				}				

				}

			}

		}

		}

	}

	else{

		System.out.println("colspan is NOT empty");

		for (int q = n;q<dispCol.length-1;q++) {		

			

			if(columnCSSMap.get((dispColIndx.get(q).toString()))!=null)

				cssName=columnCSSMap.get((dispColIndx.get(q).toString())).toString();		

			for (int i = 0;i< siz;i++) {			

				if(dispCol[q].equalsIgnoreCase(xcolNames.get(i).toString())){

					columnName= (String)xcolNames.get(i).toString();

					boolean oLinkFlag = false;

					for(int w=n;w < toDisplay.size();w++){

						String headerString = (String)toDisplay.get(w).toString();

						if(columnName.equalsIgnoreCase(headerString)){

							oLinkFlag =true;

						}

					}

					

					

					if(headerLevel.intValue()!=2)

					{

						if(!falseHeaderAL.isEmpty())

					  nonDisplayColumn = (String)falseHeaderAL.get(0).toString();

					  

					}

					if(columnName.equalsIgnoreCase(nonDisplayColumn) || columnName.equalsIgnoreCase(nonDisplayColumn) || columnName.equalsIgnoreCase("Sum") || oLinkFlag==false){

					}

					else{					

						for(int j=0;j<constDBCol.length;j++)

						{

							if(constDBCol[j].equalsIgnoreCase(columnName))

							{

								 tempStr = (String)constDsplCol[j].toString();

								 if(fxRatesrcTrgtVal.get(hcount)!=null)

									 tempStr = MessageFormat.format(tempStr, (fxRatesrcTrgtVal.get(hcount).toString().split(";"))[0]);

								 flagDisp=true;

							}					

						}					

					if(flagDisp)

					{

						for(int m=0;m<colSpan.size();m++){

							if(dispCol[q].equalsIgnoreCase(colSpan.get(m).toString())){

								out.append ("<th colspan=\""+colSpanValues.get(m).toString()+"\"  class=\"tableheader\"><label  class=\"fontWeight\">");

								out.append(spanDispLabelValues.get(m).toString());

								q=q+Integer.parseInt(colSpanValues.get(m).toString());

							}

						

							else{							 

									 out.append ("<th  rowspan=\"2\" class=\"tableheader\"><label  class=\"fontWeight\">");

									 if(isHeaderSortCol(q)){

										 out.append ("<a  onClick=\"javascript:pgnUrlSubmit(document.forms[0],'"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+((EbwForm)attrObj).getScreenName()+"','"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+sortKeys.get(dispCol[q]).toString()+"'"+ ");\">");

										 }

									 out.append(tempStr);

									 if(isHeaderSortCol(q)){

									 out.append (sortImgSrcKeys.get(dispCol[q]).toString()+"</a>");

									 }

								 }

								//q++;

								break;

							}

						}

						

						out.append ("</label></th>");

					flagDisp=false;

					}				

					}

				}

			}

			}

		 out.append("</tr>");

		// out.append ("<tr class="+rowClass+"  id="+name+ "_"+hcount+"_"+count+">");

		 out.append("<tr id="+name+ "_"+hcount+"_"+(++rowcount)+">");

		int strt=0;

		int end=0;

		for(int h=0;h<colSpan.size();h++){

			for(int j=n;j<dispCol.length;j++)	{	

			if(dispCol[j].equalsIgnoreCase(colSpan.get(h).toString())){

				strt=j;

				end=j+Integer.parseInt(colSpanValues.get(h).toString());

				//System.out.println("j="+j+" ** dispCol[j]="+constDBCol[j]+"  **dispCol[j]="+dispCol[j]+"  **colSpan.get(h).toString()="+colSpan.get(h).toString());

				//System.out.println("strt="+strt+"  **end="+end);

				for(int g=strt;g<end;g++){					

				out.append ("<th class=\"tableheader\"><label  class=\"fontWeight\">");

				if(isHeaderSortCol(g)){

					 out.append ("<a  onClick=\"javascript:pgnUrlSubmit(document.forms[0],'"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+((EbwForm)attrObj).getScreenName()+"','"+((EbwForm)attrObj).getAction()+"','"+((EbwForm)attrObj).getState()+"','"+sortKeys.get(dispCol[g]).toString()+"'"+ ");\">");

				}

				out.append((String)constDsplCol[g].toString());

				if(isHeaderSortCol(g)){

				out.append (sortImgSrcKeys.get(dispCol[g]).toString()+"</a>");

					}

				out.append ("</label></th>");

				}

			  }

			}

		}

		

		

	

	}catch(Exception e){

		e.printStackTrace();

	}

	out.append("</tr>");

	rowLevelFlag=false;

}

public void printSeperateTableforDataDisp (ArrayList tableResultAL) throws IOException {

	int 	tableResultALSize = tableResultAL.size();

	boolean flag=true;

	//printReorderImage();

	ArrayList colNames=(ArrayList)tableResultAL.get(0);

	

	out.append("<table name="+name+" id="+name+"  width=\"100%\" border=\"1\" class=\""+TableCSSName+"\" >");

	

	boolean myFlag=true;

	for(int x = 1 ; x < tableResultALSize ; x++){

		

		

		//System.out.println("LinkFlag="+linkFlag +" dispCol="+dispCol[x-1]);

		ArrayList tempAL = (ArrayList)tableResultAL.get(x);

		String elemntAtZ = tempAL.get(0).toString();

		if(elemntAtZ.startsWith("D")){

			for(int i=1;i<tempAL.size()-1;i++)

			{

				

				

			

				boolean linkFlag=false;

				for(int w=0;w < toDouble.size();w++){

					String colStr = (String)toDouble.get(w).toString();

					if(colStr.equalsIgnoreCase(dispCol[i-1])){

						linkFlag =true;

					}					

				}

				if(i%2!=0)

				{

					if(i!=1)

					{

					out.append("</tr>");

					myFlag = !myFlag;

					}

					if(myFlag){

						className="evenrow";

					}else{

						className="oddrow";

					}

					//if(flag)

					//	out.append("<tr >");

					//else

						out.append("<tr class="+className+" >");

						

				}

				

				

				out.append("<th  align=\"right\" style=\"font-weight:bold\" class="+className+" width=\"15%\"><span >");

				out.append(testCol[i-1]);

				out.append ("</span></th>");

				out.append("<th width=\"14%\" style=\"font-weight:normal\" class="+className+"><span  >");

				if(!linkFlag){

					//System.out.println("Nithya LinkFlag="+linkFlag +" dispCol="+dispCol[x-1]+" tempAL.get(i).toString()="+tempAL.get(i).toString());	

				   out.append(tempAL.get(i).toString());

				}

				else{

					out.append(ConvertionUtil.convertToReqCol(tempAL.get(i).toString(), "BigDecimal"));

					//System.out.println("Tarun LinkFlag="+linkFlag +" dispCol="+dispCol[x-1]+" tempAL.get(i).toString()="+tempAL.get(i).toString());

				}

				out.append("</span></th>");

				

					

				

			}

			

		}

	}

	out.append("</table>");

}



public void printTableForDisp2WithoutAddingTable (ArrayList tableResultAL) throws IOException {

	int	tableResultALSize = tableResultAL.size();

	headerFunctionAL = new ArrayList();

	headersAL = new ArrayList();

	falseHeaderAL = new ArrayList();

	f3Count = 0;

	f2Count = 0;

	//currencyFlag =0;

	secondHeaderSumALPosition = 0;

	secondHeaderSumAL.clear();

	hFCount = 1;

	headerFunctionAL.add(tableResultAL.get(0));

    int currencyIndex  = 0;



	for(int x = 1 ; x < tableResultALSize ; x++){

		ArrayList tempAL = (ArrayList)tableResultAL.get(x);

		String elemntAtZ = tempAL.get(0).toString();

		String elemntAtOne = tempAL.get(1).toString();

		if(elemntAtZ.equalsIgnoreCase("H1")){

			headerFunctionAL.add(tempAL);

		}

		if(elemntAtZ.equalsIgnoreCase("FN1")){

			headerFunctionAL.add(tempAL);

		}

	}



	for(int x = 0 ; x < tableResultALSize ; x++){

		ArrayList tempAL = (ArrayList)tableResultAL.get(x);

		String elemntAtZ = tempAL.get(0).toString();

		String elemntAtOne = tempAL.get(1).toString();

		

		if(elemntAtZ.startsWith("C")){

		}else if(elemntAtZ.startsWith("D")){

		}else if(elemntAtZ.startsWith("X")){

		}else if(elemntAtZ.startsWith("H")){

			if(elemntAtZ.equalsIgnoreCase("H1")){

				printSeperateTableTopHeaderForDisp2(tableResultAL);

			}

			if(headerLevel.intValue() == 3){

				 if(elemntAtZ.equalsIgnoreCase("H2")){

					 headersAL.add(((String)tempAL.get(1)));	

				}else if(elemntAtZ.equalsIgnoreCase("H3")){

					elemntAtOne = tempAL.get(1).toString();

					String[] colHeader = new String[4];

					colHeader = elemntAtOne.split(":");

					String headerName = (String)colHeader[0];

					String headerValue = (String)colHeader[1];

					if(falseHeaderAL.isEmpty()){

						falseHeaderAL.add(headerName);

					}else{

						falseHeaderAL.set(0, headerName);

					}

					

					falseHeaderAL.add((String)tempAL.get(1));

					headersAL.add(((String)headersAL.get((headersAL.size()-1))));

				}

			}else if(headerLevel.intValue() == 4){

				if(elemntAtZ.equalsIgnoreCase("H2")){

					headersAL.add(((String)tempAL.get(1)));	

				}else if(elemntAtZ.equalsIgnoreCase("H3")){

					headersAL.add(((String)tempAL.get(1)));

					headersAL.add(((String)headersAL.get((headersAL.size()-1))));

					

				}else if(elemntAtZ.equalsIgnoreCase("H4")){

					elemntAtOne = tempAL.get(1).toString();

					String[] colHeader = new String[4];

					colHeader = elemntAtOne.split(":");

					String headerName = (String)colHeader[0];

					String headerValue = (String)colHeader[1];

					if(falseHeaderAL.isEmpty()){

						falseHeaderAL.add(headerName);

					}else{

						falseHeaderAL.set(0, headerName);

					}

					falseHeaderAL.add(((String)tempAL.get(1)));

					headersAL.add(((String)headersAL.get((headersAL.size()-2))));

					headersAL.add(((String)headersAL.get((headersAL.size()-3))));

				

				}

			}

		}else if(elemntAtZ.startsWith("F")){

			if(elemntAtZ.equalsIgnoreCase("FN1")){

				headerFunctionAL.add(tempAL);

			}

			if(headerLevel.intValue() == 3){

				if(elemntAtZ.equalsIgnoreCase("FN2")){

				}else if(elemntAtZ.equalsIgnoreCase("FN3")){

					

					printOperationsForDisp2(tempAL);

					headersAL.clear();

				}

			}else if(headerLevel.intValue() == 4){

				if(elemntAtZ.equalsIgnoreCase("FN2")){

				}else if(elemntAtZ.equalsIgnoreCase("FN3")){

				}else if(elemntAtZ.equalsIgnoreCase("FN4")){

					

					printOperationsForDisp2(tempAL);

					headersAL.clear();

				}

			}

		}

	}

}

public String printOperationsForDisp2WithoutTable (ArrayList xOpValues,ArrayList currHeaderColAL,boolean flagForSelectID,String selectID) throws IOException{

	int len = xOpValues.size();

	int pCount=headerLevel.intValue()-1;

	String strContent="";

	if(flagForSelectID && headerLevel.intValue() != 2){

		rowcount++;

	}

if(flagForSelectID){

 if(headerLevel.intValue() != 2){

	if(rowLevelFlag){

		rowClass = "oddrow";

		rowLevelFlag=false;

	}else{

		rowClass = "evenrow";

		rowLevelFlag=true;

	}

	

	flagForDisplay=rowLevelFlag;

	  }

	}

	String fnName = (String)xOpValues.get(0);

	if(fnName.equalsIgnoreCase("FN1")){

		className="f3Table";

	}else if(fnName.equalsIgnoreCase("FN2")){

		className="f1Table";

	}else if(fnName.equalsIgnoreCase("FN3")){

		className="f2Table";

	}	

	String dispRowVals="";

//	out.append ("<tr class="+rowClass+">");

	String cssname="tableData";



	if(headerLevel.intValue() != 2)

	{

	 if(flagForSelectID){

	//out.append ("<tr class="+rowClass+"  id="+name+ "_"+hcount+"_1"+"_"+count+">");

		 out.append ("<tr class="+rowClass+"  id="+name+ "_"+hcount+"_"+rowcount+">");

		  }

	}

	

	boolean markFlag = false,flag=false;

	for(int x = 1; x < len ; x++){

		ArrayList opArray = (ArrayList)xOpValues.get(x);

	

		for(int z = 0; z < dispColIndx.size() ; z++){			

			for(int d = 1; d < opArray.size() ; d++){

				String colOps = opArray.get(d).toString();

				if(d == Integer.parseInt(dispColIndx.get(z).toString())){

					if(colOps.equalsIgnoreCase("Null")){

					}else{						

						markFlag = true;

					}

				}

			}

		}

		if(markFlag){

			boolean oLinkFlag = false;

			boolean displayFlag = false;

			while(pCount!=0){

			if(fnName.equalsIgnoreCase("FN3")){

				//System.out.println("f3Count="+f3Count);

				String tempStr = (String)(secondHeaderFunctionAL.get(f3Count).toString());

				//System.out.println("tempStr="+tempStr);

			    String[] tempStrHeader = new String[4];

				tempStrHeader = tempStr.split(":");

				String tempStrHeaderString = tempStrHeader[0].toString();

				for(int w=0;w < toDisplay.size();w++){

					String headerString = (String)toDisplay.get(w).toString();

					if(tempStrHeaderString.equalsIgnoreCase(headerString)){

						displayFlag =true;						

					}

					//System.out.println("  tempStrHeaderString="+tempStrHeaderString+

					//		"  headerString="+headerString+"  displayFlag="+displayFlag);

				}

				

				if(displayFlag){

				for(int i=0;i < hrefLinkAL.size();i++){

					String headerString = (String)hrefLinkAL.get(i).toString();

					if(tempStrHeaderString.equalsIgnoreCase(headerString)){

						oLinkFlag =true;

					 }

				   }

				

				if(flagForSelectID==true){					

					boolean cssflag=false;

					int ind=colHeaderAL.indexOf(tempStrHeaderString);				

					for(int h=0;h<dispColIndx.size();h++){

						if(ind==Integer.parseInt(dispColIndx.get(h).toString())){

							cssflag=true;

							ind=h;

						}

					}

					if(cssflag){

						if(columnCSSMap.get((dispColIndx.get(ind).toString()))!=null &&

								columnCSSMap.get((dispColIndx.get(ind).toString()))!="" &&

								!columnCSSMap.get((dispColIndx.get(ind).toString())).equals(""))

							cssname=columnCSSMap.get((dispColIndx.get(ind).toString())).toString();

					

					}

					if(oLinkFlag){

						if(fieldAttrAL.get(tempStrHeaderString)!=null && fieldAttrAL.get(tempStrHeaderString)!="")

						{											

							strContent = fieldAttrAL.get(tempStrHeaderString).toString().replaceAll("~VARIABLE", tempStrHeader[1]);

						}

						else

							strContent ="<a href=\"#\" onclick=\"javascript:formDrillDownHeaders(document.forms[0],'"+tempStrHeader[0]+"','"+tempStrHeader[1]+"','"+selectID+"');\">"+tempStrHeader[1]+"</a>";

					}

					else

						strContent =tempStrHeader[1];

					//out.append ("<td class=\""+cssname+"\" align=\"left\"><B><u><a href=\"#\" onclick=\"javascript:formDrillDown(document.forms[0],'"+tempStrHeader[0]+"','"+tempStrHeader[1]+"','"+selectID+"');\">"+tempStrHeader[1]+"</a></u></B></td>");

					

						out.append ("<td class=\""+cssname+"\" align=\"left\">"+strContent+"</td>");

				

				}

				if(dispRowVals.equals(""))

					dispRowVals=tempStrHeader[1];

				else

					dispRowVals=dispRowVals+"~"+tempStrHeader[1];

				}

				f3Count++;

				displayFlag=false;

			 }

			pCount--;

			}

			while(pCount!=0){

			if(fnName.equalsIgnoreCase("FN4")){

				for(int z =0; z< 2;z++){

					String tempStr = (String)(secondHeaderFunctionAL.get(f3Count).toString());

					String[] tempStrHeader = new String[4];

					tempStrHeader = tempStr.split(":");

					String tempStrHeaderString = tempStrHeader[0].toString();

					for(int w=0;w < toDisplay.size();w++){

						String headerString = (String)toDisplay.get(w).toString();

						if(tempStrHeaderString.equalsIgnoreCase(headerString)){

							displayFlag =true;

						}

					}

					if(displayFlag){

					for(int i=0;i < hrefLinkAL.size();i++){

						String headerString = (String)hrefLinkAL.get(i).toString();

						if(tempStrHeaderString.equalsIgnoreCase(headerString)){

							oLinkFlag =true;

						 }

					   }

					//boolean oLinkFlag = false;

					

					

					

					if(flagForSelectID==true){  

						

						boolean cssflag=false;

						int ind=colHeaderAL.indexOf(tempStrHeaderString);

						for(int h=0;h<dispColIndx.size();h++){

							if(ind==Integer.parseInt(dispColIndx.get(h).toString())){

								cssflag=true;

								ind=h;

							}

						}

						if(cssflag){

							if(columnCSSMap.get((dispColIndx.get(ind).toString()))!=null &&

									columnCSSMap.get((dispColIndx.get(ind).toString()))!="" &&

									!columnCSSMap.get((dispColIndx.get(ind).toString())).equals(""))

								cssname=columnCSSMap.get((dispColIndx.get(ind).toString())).toString();

						}

					if(oLinkFlag){

						if(fieldAttrAL.get(tempStrHeaderString)!=null && fieldAttrAL.get(tempStrHeaderString)!="")

						{											

							strContent = fieldAttrAL.get(tempStrHeaderString).toString().replaceAll("~VARIABLE", tempStrHeader[1]);

						}

						else

							strContent ="<a href=\"#\" onclick=\"javascript:formDrillDownHeaders(document.forms[0],'"+tempStrHeader[0]+"','"+tempStrHeader[1]+"','"+selectID+"');\">"+tempStrHeader[1]+"</a>";

					}

					else

						strContent =tempStrHeader[1];

					//out.append ("<td class=\""+cssname+"\" align=\"left\"><B><u><a href=\"#\" onclick=\"javascript:formDrillDown(document.forms[0],'"+tempStrHeader[0]+"','"+tempStrHeader[1]+"','"+selectID+"');\">"+tempStrHeader[1]+"</a></u></B></td>");

					

						out.append ("<td class=\""+cssname+"\" align=\"left\">"+strContent+"</td>");

					

					f3Count++;

					}

				}

			  } 

			}

			pCount--;

			}

			//System.out.println("hrefLinkAL="+hrefLinkAL);

			//for(int i=0;i<dispCol.length;i++)

			//System.out.println("dispCol["+i+"]=="+dispCol[i]);

			

			//System.out.println("dispColIndx"+dispColIndx);

			boolean linkFlag = false;

			int i=0;

			for(int z = 0; z < dispColIndx.size() ; z++){

				for(int d = 1; d < opArray.size() ; d++){

					String colOps = (String)opArray.get(d).toString();

					String dataType = "BigDecimal";

					if(d == Integer.parseInt(dispColIndx.get(z).toString())){

						for(int f=0;f < hrefLinkAL.size();f++){

							String headerString = (String)hrefLinkAL.get(f).toString();

							if(dispCol[z].equalsIgnoreCase(headerString)){

								linkFlag =true;

							}		    			

						}

						if(colOps.equalsIgnoreCase("Null")){

						}else{							

							int a=Integer.parseInt(dispColIndx.get(z).toString());						

						//this is 4 currency concatinating. ......	

							for(int f = 0; f< currHeaderColAL.size() ; f++){

								int b=Integer.parseInt(currHeaderColAL.get(f).toString());

								if(a==b){

										flag=true;									

										i=b;										

									}

								} 							

							if(columnCSSMap.get(Integer.toString(a))!=null &&

									columnCSSMap.get(Integer.toString(a))!="" &&

									!columnCSSMap.get(Integer.toString(a)).equals("")){

								cssname=columnCSSMap.get(Integer.toString(a)).toString();							

							}				

						

							if((fnName.equalsIgnoreCase("FN2"))){

								String colOpsStr = ConvertionUtil.convertToReqCol(colOps,dataType);

								for(int w=0;w < toDisplay.size();w++){

									String headerString = (String)toDisplay.get(w).toString();

									if(dispCol[z].toString().equalsIgnoreCase(headerString)){

										displayFlag =true;

									}

								}

								if(displayFlag==true){

								if(flagForSelectID==true){  

									if(linkFlag){										

										if(fieldAttrAL.get(dispCol[z])!=null && fieldAttrAL.get(dispCol[z])!="")

											strContent = fieldAttrAL.get(dispCol[z]).toString().replaceAll("~VARIABLE", colOpsStr);

										else

											strContent =  colOpsStr;

									}

									else{

										strContent =  colOpsStr;

									}

									System.out.println("linkFlag="+linkFlag+"  colOpsStr ="+colOpsStr+"  strContent="+strContent);

										out.append ("<td class=\""+cssname+"\" align=\"right\">"+strContent+"</td>");							 

									}

										dispRowVals=dispRowVals+"~"+colOpsStr;

								}

							}

			

						    if((fnName.equalsIgnoreCase("FN3") || fnName.equalsIgnoreCase("FN4"))){

						    	String colOpsStr = ConvertionUtil.convertToReqCol(colOps,"SignedCurrencyAmount");

								String temp3Str2 = colOpsStr;

								String curr="";

								if(flag==true){

									flag=false;

									if(i!=0)

										temp3Str2=currColA[i]+" "+temp3Str2;

								}

							  	dispRowVals=dispRowVals+"~"+temp3Str2;

							  	

								if(flagForSelectID==true){

									if(linkFlag){

										

										if(fieldAttrAL.get(dispCol[z])!=null && fieldAttrAL.get(dispCol[z])!=""){

											

											strContent = fieldAttrAL.get(dispCol[z]).toString().replaceAll("~VARIABLE", temp3Str2);

										}

									else{

												strContent =  temp3Str2;

										}

										out.append ("<td class=\""+cssname+"\" align=\"right\">"+strContent+"</td>");

								f2Count++;

								}								

								//f2Flag = false;

							} 

						}

					}

				}

			}

		}

	}

}

    out.append("<td class=\"hidden\" >"+dispRowVals+"</td>");

	out.append ("</tr>");

    System.out.println("dispRowVals="+dispRowVals);

	return dispRowVals;

}



  public void printTopTable(ArrayList tableResultAL) throws IOException  {



  		int tableResultALSize = tableResultAL.size();

	    TableManipulator tm1Object = new TableManipulator();

		headerFunctionAL = new ArrayList();

		secondHeaderFunctionAL = new ArrayList();

		thirdHeaderFunctionAL = new ArrayList();

		falseHeaderAL= new ArrayList();

		//f3Count = 0;

		f2Count = 0;

		//currencyFlag =0;

		secondHeaderSumALPosition = 0;

		secondHeaderSumAL.clear();

		hFCount = 1;

		headerFunctionAL.add(tableResultAL.get(0));

		int currencyIndex  = 0;

		String firstHeaderElement = tableHeader.get(1).toString();

		String secondHeaderElement=tableHeader.get(1).toString();

		boolean flag=false;

		if(dispColConstIndx!=null && dispColConstIndx.size()!=0){

			if(dispColConst[0].equals(firstHeaderElement))

				flag=true;

		}

		

	//	System.out.println("dispColConstIndx -- --H2 --> "+dispColConstIndx);

	

		

	//	System.out.println("currHeaderColAL -- --H2 --> "+currHeaderColAL);

		falseHeadAL.add(0,0);

		for(int x = 1 ; x < tableResultALSize ; x++){

			ArrayList tempAL = (ArrayList)tableResultAL.get(x);

			String elemntAtZ = tempAL.get(0).toString();

			String elemntAtOne = null;

			if(elemntAtZ.equalsIgnoreCase("DR")){

				if(dispColConstIndx!=null){

				for(int d=1;d < dispColConstIndx.size();d++){

					for(int i=0;i < currHeaderColAL.size();i++){

					if(dispColConstIndx.get(d).equals(currHeaderColAL.get(i))){

						falseHeadAL.add(tempAL.get(Integer.parseInt(falseHeaderColAL.get(i).toString())).toString());

						}                                                         

					}

				}

				}

			}

			if(elemntAtZ.equalsIgnoreCase("H1")){

				

				headerFunctionAL.add(tempAL);

			}

			if(elemntAtZ.equalsIgnoreCase("FN1")){

				headerFunctionAL.add(tempAL);

			}

			if(elemntAtZ.equalsIgnoreCase("H2")){

				secondHeaderFunctionAL.add(((String)tempAL.get(1)));

				if(dispColConst!= null && !flag){

					elemntAtOne = tempAL.get(1).toString();

					String[] colHeader = new String[4];

					colHeader = elemntAtOne.split(":");

					String headerName = (String)colHeader[0];

					String headerValue = (String)colHeader[1];



					if(secondHeaderSumAL.isEmpty()){

						secondHeaderSumAL.add(headerName);

						secondHeaderSumAL.add(headerValue);

						for(int z=1;z < dispColConst.length;z++){

							secondHeaderSumAL.add(new BigDecimal(0.00));

						}

						secondHeaderSumALPosition = secondHeaderSumAL.indexOf(headerValue);

					}else if(secondHeaderSumAL.contains(headerValue)){

						secondHeaderSumALPosition = secondHeaderSumAL.indexOf(headerValue);

					}else{

						secondHeaderSumAL.add(headerValue);

						for(int z=1;z < dispColConst.length;z++){

							secondHeaderSumAL.add(new BigDecimal(0.00));

						}

						secondHeaderSumALPosition = secondHeaderSumAL.indexOf(headerValue);

					}

				}

				//System.out.println("secondHeaderFunctionAL in H2="+secondHeaderFunctionAL);

			}

			if(elemntAtZ.equalsIgnoreCase("H3")){

				if(headerLevel.intValue() == 3){

					secondHeaderFunctionAL.add(((String)tempAL.get(1)));

					elemntAtOne = tempAL.get(1).toString();

					String[] colHeader = new String[4];

					colHeader = elemntAtOne.split(":");

					String headerName = (String)colHeader[0];

					String headerValue = (String)colHeader[1];

					

					/*if(falseHeaderAL.isEmpty()){

						falseHeaderAL.add(headerName);

					}else{

						falseHeaderAL.set(0, headerName);

					}

					falseHeaderAL.add(((String)tempAL.get(1)));*/

					//secondHeaderFunctionAL.add(((String)secondHeaderFunctionAL.get((secondHeaderFunctionAL.size()-1))));

					secondHeaderFunctionAL.add(((String)secondHeaderFunctionAL.get((secondHeaderFunctionAL.size()-2))));

					secondHeaderFunctionAL.add(((String)secondHeaderFunctionAL.get((secondHeaderFunctionAL.size()-2))));

				}else if(headerLevel.intValue() == 4){

					secondHeaderFunctionAL.add(((String)tempAL.get(1)));

				}

				//System.out.println("falseHeadAL in H3="+falseHeadAL);

				//System.out.println("secondHeaderFunctionAL in H3="+secondHeaderFunctionAL);

			}

			if(elemntAtZ.equalsIgnoreCase("H4")){

				secondHeaderFunctionAL.add(((String)tempAL.get(1)));

				elemntAtOne = tempAL.get(1).toString();

				String[] colHeader = new String[4];

				colHeader = elemntAtOne.split(":");

				String headerName = (String)colHeader[0];

				String headerValue = (String)colHeader[1];

				

				/*if(falseHeaderAL.isEmpty()){

					falseHeaderAL.add(headerName);

				}else{

					falseHeaderAL.set(0, headerName);

				}

				falseHeaderAL.add(((String)tempAL.get(1)));*/

				secondHeaderFunctionAL.add(((String)secondHeaderFunctionAL.get((secondHeaderFunctionAL.size()-2))));

				secondHeaderFunctionAL.add(((String)secondHeaderFunctionAL.get((secondHeaderFunctionAL.size()-2))));

				secondHeaderFunctionAL.add(((String)secondHeaderFunctionAL.get((secondHeaderFunctionAL.size()-2))));

			}

			if(elemntAtZ.equalsIgnoreCase("FN2")){

				secondHeaderFunctionAL.remove((secondHeaderFunctionAL.size()-1));

				String curr="";

				String constant="";

				String str="";

				//if(country!=null){

				//if(!falseHeaderAL.isEmpty())

					//	str=falseHeaderAL.get(falseHeaderAL.size()-1).toString();

				//System.out.println("str in FN2="+str);

				    	String[] colHeader = new String[4];

						colHeader = str.split(":");

						String headerName = (String)colHeader[0];

						String headerValue="";

						if(colHeader.length>1)

							headerValue = (String)colHeader[1];

						str=headerValue;

					

				  

			//	}  

				if(dispColConst!= null && !flag){

					secondHeaderSumAL = (ArrayList)tm1Object.sumOfSecondHeader(tempAL,secondHeaderSumALPosition,secondHeaderSumAL,dispColConstIndx,curr,constant,str);

				}

				//System.out.println("falseHeadAL in FN2="+falseHeadAL);

				//System.out.println("secondHeaderFunctionAL in FN2="+secondHeaderFunctionAL);

			}

			//if(elemntAtZ.equalsIgnoreCase("FN3") && headerLevel.intValue() == 4){

			if(elemntAtZ.equalsIgnoreCase("FN3") ){

				secondHeaderFunctionAL.remove((secondHeaderFunctionAL.size()-1));

				

			}

			if(elemntAtZ.equalsIgnoreCase("FN4") ){

				secondHeaderFunctionAL.remove((secondHeaderFunctionAL.size()-1));

				

			}

			if(elemntAtZ.startsWith("X")){

				if(dispColConst!= null && !flag){

					ArrayList elementAtOne = (ArrayList)tempAL.get(1);

					secondHeaderSumAL.add(elementAtOne);

				}

				

			}

		}



		String selectID="";

		System.out.println("secondHeaderFunctionAL in Top Table="+secondHeaderFunctionAL);

		    if(userCSA.equalsIgnoreCase("disp2") || userCSA.equalsIgnoreCase("disp3") ||  userCSA.equalsIgnoreCase("disp4"))

			{

				if(dispColConst!= null && !flag)

							printFirstTable(secondHeaderSumAL,tableResultAL,true,selectID);

				if(dispColConst!= null && flag)

					printSecondTableForDisp2forFirstHeader(headerFunctionAL,tableResultAL,true,selectID);

				

			}

		   

  }

  

  public void printOperationsForDisp2 (ArrayList xOpValues) throws IOException{

		int len = xOpValues.size();

		if(rowLevelFlag){

			rowClass = "oddrow";

			rowLevelFlag=false;

		}else{

			rowClass = "evenrow";

			rowLevelFlag=true;

		}



		String fnName = (String)xOpValues.get(0);

		if(fnName.equalsIgnoreCase("FN1")){

			className="f3Table";

		}else if(fnName.equalsIgnoreCase("FN2")){

			className="f1Table";

		}else if(fnName.equalsIgnoreCase("FN3")){

			className="f2Table";

		}	

		out.append ("<tr class="+rowClass+">");

		boolean markFlag = false;

		for(int x = 1; x < len ; x++){

			ArrayList opArray = (ArrayList)xOpValues.get(x);

			for(int z = 0; z < dispColIndx.size() ; z++){

				for(int d = 1; d < opArray.size() ; d++){

					String colOps = opArray.get(d).toString();

					if(d == Integer.parseInt(dispColIndx.get(z).toString())){

						if(colOps.equalsIgnoreCase("Null")){

						}else{

							markFlag = true;

						}

					}

				}

			}



			if(markFlag){

			if(fnName.equalsIgnoreCase("FN3")){

					String tempStr = (String)(secondHeaderFunctionAL.get(f3Count).toString());

					String[] tempStrHeader = new String[4];

					tempStrHeader = tempStr.split(":");

					String tempStrHeaderString = tempStrHeader[0].toString();

					boolean oLinkFlag = false;

					for(int i=0;i < hrefLinkAL.size();i++){

						String headerString = (String)hrefLinkAL.get(i).toString();

						if(tempStrHeaderString.equalsIgnoreCase(headerString)){

							oLinkFlag =true;

						}

					}

		

					if(oLinkFlag){

						out.append ("<td class=\"tableData\" align=\"left\"><B><u><a href=\"#\" onclick=\"javascript:formDrillDown(document.forms[0],'"+tempStrHeader[0]+"','"+tempStrHeader[1]+"');\">"+tempStrHeader[1]+"</a></u></B></td>");

					}else{

						out.append ("<td class=\"tableData\" align=\"left\">"+tempStrHeader[1]+"</td>");

					}

					f3Count++;

				} 

				boolean f2Flag = true;

				for(int z = 0; z < dispColIndx.size() ; z++){

					for(int d = 1; d < opArray.size() ; d++){

						String colOps = (String)opArray.get(d).toString();

						String dataType = "BigDecimal";

						if(d == Integer.parseInt(dispColIndx.get(z).toString())){

							if(colOps.equalsIgnoreCase("Null")){

							}else{

						

								if(fnName.equalsIgnoreCase("FN3") && f2Flag){

									String temp3Str = (String)(thirdHeaderFunctionAL.get(f2Count).toString());

									String[] temp3StrHeader = new String[4];

									temp3StrHeader = temp3Str.split(":");

									String colOpsStr = ConvertionUtil.convertToReqCol(colOps,"SignedCurrencyAmount");

									String temp3Str2 = temp3StrHeader[1]+" "+colOpsStr;

									out.append ("<td class=\"tableData\" align=\"right\">"+temp3Str2+"</td>");

									f2Flag = false;

									f2Count++;

								} else {

									String colOpsStr = ConvertionUtil.convertToReqCol(colOps,"SignedCurrencyAmount");

									out.append ("<td class=\"tableData\" align=\"right\">"+colOpsStr+"</td>");

								}

							}

						}

					}

				}

			}

		}

	}





 

  public void printSecondTableForDisp2forFirstHeader (ArrayList headerFunctionAL,ArrayList tableResultAL,boolean flagForSelectID,String selectID) throws IOException {

	  

	  ArrayList hFValAL = new ArrayList();

	ArrayList colAL = new ArrayList();

	colAL =(ArrayList)headerFunctionAL.get(0);

	boolean myFlag = true;

	if(flagForSelectID){

	out.append("<table  width=\"60.5%\" border=\"0\" class=\""+TableCSSName+"\" >");

	out.append("<tr>");

	if(toptsummarydispColConst!=null  && dispColConst!=null)

	{

		for(int j=0;j<toptsummarydispColConst.length;j++)

		{

			String Lable=(String)formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." +toptsummarydispColConst[j].trim()+"."+"tablesummary");

			ArrayList temp=(ArrayList)tableResultAL.get(tableResultAL.size()-1);

			ArrayList temp1=(ArrayList)tableResultAL.get(0);

			int trgtindex=temp1.indexOf(toptsummarydispColConst[j].trim());

			ArrayList temp2=(ArrayList)temp.get(1);

			String str=temp2.get(trgtindex).toString();

			out.append ("<td  class=\"H2lable\" align=\"left\"><label>"+Lable + str +"</label></td>");

		}	

	}

	out.append("<tr>");

	for(int i=0;i<dispColConst.length;i++){

		String colSuperName =(String)formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." +((dispColConst[i].toString()).replaceAll(" ","")));

		out.append ("<th class=\"tableheader\"><label  class=\"fontWeight\">"+colSuperName+"</label></th>");

	}

	out.append("</tr>");

	}

	

	boolean rFlag = true;

	boolean currFlag = true;

	boolean linkFlag = false;

	boolean flag=false;

	boolean flagCurr=false;

	boolean flagFalse=false;

	

	for(int x = 1 ; x <(headerFunctionAL.size()) ; x++){

		if(myFlag){

			className="evenrow";

		}else{

			className="oddrow";

		}

		hFValAL = (ArrayList)headerFunctionAL.get(x);

		//if(pCount != 0){

			String hIndx = (String)hFValAL.get(0);

			

			if(hIndx.startsWith("H")){

				String hVal = (String)hFValAL.get(1);

				String[] colHeader = new String[4];

				colHeader = hVal.split(":");

				String colHeaderString = colHeader[0].toString();

				for (int z = 0;z<colHeader.length;z++) {

					for (int q = 0;q<constDBCol.length;q++) {

						if(constDBCol[q].equalsIgnoreCase(colHeader[z])){

							colHeader[z]=constDsplCol[q];

						}

					}

				}

				String[] colHeaderVal=new String[4];

			//	System.out.println("Tarun------"+colHeader[1].toString());

				colHeaderVal=colHeader[1].toString().split("~");

			

				out.append ("<tr  class="+className+">");

				//out.append ("<td class=\"tableData\" align=\"left\">"+colHeader[1]+"</td>");

				if(colHeaderVal.length>1)

				   out.append ("<td class=\"tableData\" align=\"left\">"+colHeaderVal[1]+"</td>");

				else

					out.append ("<td class=\"tableData\" align=\"left\">"+colHeaderVal[0]+"</td>");

				

				

			//out.append("<tr>");

			//out.append ("<td class=\"H1lable\"><B><u><a href=\"#\" onClick=\"window.open(\'PortfolioByCountry.htm\','popupwindow','top=100,left=100,resizable=yes,height=275,width=840,status=yes')\">"+colHeader[1]+"</a></u></B></td>");

			//out.append ("<td class=\"H1lable\"><B><u><a href=\"#\" onclick=\"javascript:colSelectColor("+colHeader[1]+");\">"+colHeader[1]+"</a></u></B></td>");

			//out.append("</tr>");

			}

			String curr1="";

			if(hIndx.startsWith("F")){

				for (int q = 1;q<hFValAL.size();q++) {

					ArrayList fValAL = new ArrayList();

					fValAL = (ArrayList)hFValAL.get(q);

					for (int i = 0;i< fValAL.size();i++) {

						String fStr = fValAL.get(i).toString();

						

						for(int j=0;j<currHeaderColAL.size();j++){

							//	System.out.println("dispColConstIndx.get(d)           "+dispColConstIndx.get(d));

							//	System.out.println("currHeaderColAL.get(j)           "+currHeaderColAL.get(j));

					if(dispColConstIndx.get(j+1).equals(currHeaderColAL.get(j))){

							//		System.out.println("Equal.2   ");

								flagCurr=true;

							}

						}

						

						//System.out.println("fStr--->> "+fStr);

						if(!fStr.equalsIgnoreCase("Null")){

							if(i==0){

								//out.append ("<td><B>"+fStr+" of -</B>");

							}else{

								 if(flagCurr==true){

										//	System.out.println("Flag .2   ");

											flagCurr=false;

										//	curr1=(String)formRB.getString("USD" + "." +"Currency");

											curr1=falseHeadAL.get(i).toString();

										//	System.out.println("curr1   "+curr1);

									/*		double da=java.lang.Math.round(Double.parseDouble(tempStrr1));

											String s="";

											tempStrr1=s+da; */

											fStr=ConvertionUtil.convertToReqCol(fStr, "SignedCurrencyAmount");

											fStr=curr1+" "+fStr;

											fStr.trim();

										 //   System.out.println("tempStrr1::::--   1  -->> "+tempStrr1);

										}

								 else

								 {

										//	System.out.println("tempStrr1::::--  2  -->> "+tempStrr1);

									 fStr=ConvertionUtil.convertToReqCol(fStr, "BigDecimal");

										}

								 if(flagForSelectID){

										//	System.out.println("tempStrr1::::--  3  -->> "+tempStrr1);	

										 out.append ("<td class=\"tableData\" align=\"right\">"+fStr+"</td>");

										}

							}

						}

					}

					//out.append("</td>");

					out.append("</tr>");

					if(myFlag){

						myFlag = false;

					}else{

						myFlag = true;

					}

				}

			}

			//pCount--;

		//}else{ 

			//hFCount = x;

		//	break;

		//}

		//hFCount = x-1;

	}

	out.append("</table>");

	out.append("<table>");

	out.append ("<tr><td></td></tr>");

	out.append ("<tr><td></td></tr>");

	out.append ("<tr><td></td></tr>");

	out.append ("<tr><td></td></tr>");

	out.append("</table>");

	}

  

  public void printTopHeaderForDisp2(ArrayList tempAL) throws IOException{

		

		if(f2Count != 0){

			out.append ("</table>");

			out.append ("<table>");

			out.append ("<tr><td></td></tr>");

			out.append ("<tr><td></td></tr>");

			out.append ("<tr><td></td></tr>");

			out.append ("<tr><td></td></tr>");

			out.append ("<tr><td></td></tr>");

			out.append ("<tr><td></td></tr>");

			out.append ("</table>");

		}

		

		//className="layoutMaxTable";

		//className="tableTitle";

		String hName = (String)tempAL.get(0);

		

		if(hName.equalsIgnoreCase("H1")){

			className="H1lable";

		}else if(hName.equalsIgnoreCase("H2")){

			className="H2lable";

		}else if(hName.equalsIgnoreCase("H3")){

			className="H3lable";

		}

	

		String hVal = (String)tempAL.get(1);

		String[] colHeader = new String[4];

		colHeader = hVal.split(":");

		for (int z = 0;z<colHeader.length;z++) {

			for (int q = 0;q<constDBCol.length;q++) {

				if(constDBCol[q].equalsIgnoreCase(colHeader[z])){

					colHeader[z]=constDsplCol[q];

				}

			}

		}

		out.append ("<tr>");

		out.append ("<th class="+className+"><B>"+colHeader[1]+"</B></td>");

		out.append ("</tr>");

	}

 

  public void printSeperateTableTopHeader () throws IOException {

		int	pCount = 2;

		ArrayList hFValAL = new ArrayList();

		ArrayList colAL = new ArrayList();

		colAL =(ArrayList)headerFunctionAL.get(0);

		if(hFCount != 1){

			out.append("</table>");

			dispColIndx.clear();

		}

		out.append("<table name=\"TableTopHeader\" id=\"TableTopHeader\"  width=\"35%\" border=\"0\" class=\"layoutRepTable\">");

	

		out.append("<tr>");

		for(int x = hFCount ; x < headerFunctionAL.size() ; x++){

			hFValAL = (ArrayList)headerFunctionAL.get(x);

			if(pCount != 0){

				String hIndx = (String)hFValAL.get(0);

				if(hIndx.startsWith("H")){

					String hVal = (String)hFValAL.get(1);

					String[] colHeader = new String[4];

					colHeader = hVal.split(":");

					for (int z = 0;z<colHeader.length;z++) {

						for (int q = 0;q<constDBCol.length;q++) {

							if(constDBCol[q].equalsIgnoreCase(colHeader[z])){

								colHeader[z]=constDsplCol[q];

							}

						}

					}

					out.append("<tr>");

					out.append ("<td class=\"H1lable\"><B>"+colHeader[0]+": "+colHeader[1]+"</B></td>");

					out.append("</tr>");

				}

				if(hIndx.startsWith("F")){

					for (int q = 1;q<hFValAL.size();q++) {

						ArrayList fValAL = new ArrayList();

						fValAL = (ArrayList)hFValAL.get(q);

						out.append("<tr>");

						for (int i = 0;i< fValAL.size();i++) {

							String fStr = fValAL.get(i).toString();

							if(!fStr.equalsIgnoreCase("Null")){

								if(i==0){

									out.append ("<td><B>"+fStr+" of -</B>");

								}else{

									out.append ("<td>"+constDsplCol[i-1]+" : "+fStr+"</td>");

								}

							}

						}

						out.append("</td>");

						out.append("</tr>");

					}

				}

				pCount--;

			}else{ 

				hFCount = x;

				break;

			}

			//hFCount = x-1;

		}

		out.append("</tr>");

		out.append("</table>");

		printColumnTable(colAL);

	}



  

  public String printFirstTable(ArrayList secondHeaderSumAL,ArrayList tableResultAL,boolean flagForSelectID,String selectID) throws IOException {

	  hcount=-1;

	  hcount++;

  		String dispRowVals="";

  		String cssname="tableData";

  		ArrayList tempColArray = (ArrayList)tableResultAL.get(0);

		int secondHeaderSumALSize = (int)secondHeaderSumAL.size();

		ArrayList tempArray = (ArrayList)secondHeaderSumAL.get(secondHeaderSumALSize-1);

	//	LinkedHashMap columnCSSMap = getColumnCSSMap(0,colHeaderAL.size()-2);

		

		if(flagForSelectID){

		out.append("<table   border=\"0\">");

		out.append("<tr>");

		

		/** This loop is used to display toptable summary **/

		

		if(toptsummarydispColConst!=null  && dispColConst!=null)

		{

			for(int j=0;j<toptsummarydispColConst.length;j++)

			{				

				String Lable=(String)formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." +toptsummarydispColConst[j].trim()+"."+"toptablesummary");

				if(fxRatesrcTrgtVal.get(hcount)!=null)

					Lable=MessageFormat.format(Lable, (fxRatesrcTrgtVal.get(hcount).toString().split(";"))[0]);

				ArrayList temp=(ArrayList)tableResultAL.get(tableResultAL.size()-1);

				ArrayList temp1=(ArrayList)tableResultAL.get(0);

				int trgtindex=temp1.indexOf(toptsummarydispColConst[j].trim());

				ArrayList temp2=(ArrayList)temp.get(1);

				String str=ConvertionUtil.convertToReqCol(temp2.get(trgtindex).toString(), "SignedCurrencyAmount");

				out.append ("<td  class=\"H2lable\" align=\"left\"><label>"+Lable + str +"</label></td>");

			}	

		}

			

		out.append("</tr></table>");

		

		out.append("<table  name=\""+name+"top"+"\" id=\""+name+"top"+"\"  cellpadding=\"1\" cellspacing=\"0\" class=\""+TableCSSName+"\" width=\""+getTopTableWidth()+"\">");

		out.append("<tr>");

		int a=Integer.parseInt(dispColConstIndx.get(0).toString());

		

	/** This for loop is used to display Column names for the toptable */	

		

		for(int i=0;i<dispColConstIndx.size();i++){

			String colSuperName =(String)formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." +((dispColConst[i].toString()).replaceAll(" ","")));

			if(fxRatesrcTrgtVal.get(hcount)!=null)

				colSuperName = MessageFormat.format(colSuperName, (fxRatesrcTrgtVal.get(hcount).toString().split(";"))[0]);

			if(columnCSSMap.get((dispColConstIndx.get(i).toString()))!=null &&

					!columnCSSMap.get((dispColConstIndx.get(i).toString())).equals(""))

				out.append ("<th class=\""+columnCSSMap.get(dispColConstIndx.get(i).toString())+"\"><label  class=\"fontWeight\">"+colSuperName+"</label></th>");

			else

				out.append ("<th class=\"tableheader\"><label  class=\"fontWeight\">"+colSuperName+"</label></th>");

		}

		out.append("</tr>");

		}

		

		boolean rFlag = true;

		boolean myFlag = true;

		boolean currFlag = true;

		boolean linkFlag = false;

		boolean flag=false;

		boolean flagCurr=false;

		boolean flagFalse=false;

		

		for (int i = 1;i<(secondHeaderSumAL.size()-1);i++) 

		{

			if(myFlag){

				className="evenrow";

			}else{

				className="oddrow";

			}

			

			if(rFlag)

			{

				out.append ("<tr  class="+className+">");

				if((secondHeaderSumAL.get(i).getClass().toString()).equalsIgnoreCase("class java.lang.String")){

				

					for(int i1=0;i1 < hrefLinkAL.size();i1++){

					

						String headerString = (String)hrefLinkAL.get(i1).toString();

						if(secondHeaderSumAL.get(0).toString().equalsIgnoreCase(headerString)){

							linkFlag =true;

						}					

					}

					if(flagForSelectID){

						

						boolean cssflag=false;

						int ind=tempColArray.indexOf(secondHeaderSumAL.get(0).toString());

						for(int h=0;h<dispColConstIndx.size();h++){

							if(ind==Integer.parseInt(dispColConstIndx.get(h).toString())){

								cssflag=true;

								ind=h;

							}

						}

						if(cssflag){

							if(columnCSSMap.get((dispColConstIndx.get(ind).toString()))!=null && !columnCSSMap.get((dispColConstIndx.get(ind).toString())).equals(""))

								cssname=columnCSSMap.get((dispColConstIndx.get(ind).toString())).toString();

						}

						System.out.println("cssname=="+cssname);

						

						if(linkFlag){	

							linkFlag = false;  

							out.append ("<td class=\""+cssname+"\" align=\"left\"><B><u><a href=\"#\" onclick=\"javascript:formDrillDownTop(document.forms[0],'"+secondHeaderSumAL.get(0).toString()+"','"+secondHeaderSumAL.get(i).toString()+"','"+selectID+"');\">"+secondHeaderSumAL.get(i).toString()+"</a></u></B></td>");

						}else{

							out.append ("<td class=\""+cssname+"\" align=\"left\">"+secondHeaderSumAL.get(i)+"</td>");

						}

					}

					if(dispRowVals=="")

						dispRowVals=secondHeaderSumAL.get(i).toString();

					else

					dispRowVals=dispRowVals+"~"+secondHeaderSumAL.get(i).toString();

				}else {

					if(flagForSelectID)

					   out.append ("<td class=\""+cssname+"\" align=\"right\"> "+secondHeaderSumAL.get(i)+"</td>");

					if(dispRowVals=="")

						dispRowVals=secondHeaderSumAL.get(i).toString();

					else

					dispRowVals=dispRowVals+"~"+secondHeaderSumAL.get(i).toString();

				}

				

				rFlag = false;

			}else{

				if((secondHeaderSumAL.get(i).getClass().toString()).equalsIgnoreCase("class java.lang.String")){

					

					for(int n=i,d=1;d < dispColConst.length;n++,d++){

						

						String curr1="";

						String tempStrr1 = (String)secondHeaderSumAL.get(n).toString();

						for(int j=0;j<currHeaderColAL.size();j++){

							if(dispColConstIndx.get(d).equals(currHeaderColAL.get(j))){

								flagCurr=true;

								}

							}

								

						if(flagCurr==true){

							flagCurr=false;

							curr1=falseHeadAL.get(d).toString();

							tempStrr1=ConvertionUtil.convertToReqCol(tempStrr1, "SignedCurrencyAmount");

							tempStrr1=curr1+" "+tempStrr1;

							tempStrr1.trim();

						}else{

							tempStrr1=ConvertionUtil.convertToReqCol(tempStrr1, "BigDecimal");

						}

							if(dispRowVals=="")

								dispRowVals=tempStrr1;

							else

							dispRowVals=dispRowVals+"~"+tempStrr1;

							if(flagForSelectID){

								if(columnCSSMap.get((dispColConstIndx.get(d).toString()))!=null &&  !columnCSSMap.get((dispColConstIndx.get(d).toString())).equals(""))	

									out.append ("<td class=\""+columnCSSMap.get((dispColConstIndx.get(d).toString())).toString()+"\" align=\"right\">"+tempStrr1+"</td>");

								else

									out.append ("<td class=\"tableData\" align=\"right\">"+tempStrr1+"</td>");

							}

							i=n;

						

					}

					

				}else {

					for(int n=i,d=1;d < dispColConst.length;n++,d++){

						String tempStrr2 = (String)secondHeaderSumAL.get(n).toString();

						String curr1="";

						if(dispRowVals=="")

							dispRowVals=curr1+" "+ConvertionUtil.convertToReqCol(tempStrr2, "SignedCurrencyAmount");

						else

						dispRowVals=dispRowVals+"~"+curr1+" "+ConvertionUtil.convertToReqCol(tempStrr2, "SignedCurrencyAmount");

						if(flagForSelectID){

						   out.append ("<td class=\"tableData\" align=\"right\">"+curr1+" "+ConvertionUtil.convertToReqCol(tempStrr2, "SignedCurrencyAmount")+"</td>");

						}

					    	i=n;

					}

				}

				

				rFlag = true;

				out.append ("</tr>");

				if(myFlag){

					myFlag = false;

				}else{

					myFlag = true;

				}

			}

		}



		out.append("</table>");

		out.append("<table>");

		out.append ("<tr><td></td></tr>");

		out.append ("<tr><td></td></tr>");

		out.append ("<tr><td></td></tr>");

		out.append ("<tr><td></td></tr>");

		out.append("</table>");

		return dispRowVals;

  }

  

  public void printColumnTable (ArrayList xcolNames) throws IOException {



		int siz = xcolNames.size();

		//System.out.println("xcolNames::::---->> "+xcolNames);

		//System.out.println("testCol::::---->> "+testCol);

		out.append("<table name="+name+" id="+name+"  width=\"40%\"  border=\"0\" class=\"columnheader\" >");

		

		out.append("<tr>");

		out.append ("<td class=\"tableheader\"></td>");

		for (int q = 0;q<dispCol.length;q++) {

			//System.out.println("dispCol[q]::::---->> "+dispCol[q]);

			for (int i = 1;i< siz;i++) {

				if(dispCol[q].equalsIgnoreCase(xcolNames.get(i).toString())){

					dispColIndx.add(new Integer(i));

					out.append ("<td class=\"tableheader\">"+testCol[q]+"</td>");

				}

			}

		}

		out.append("</tr>");

		}

  

 

  public void printSeperateTableTop (ArrayList tableResultAL) throws IOException {

  	int 	tableResultALSize = tableResultAL.size();

  	headerFunctionAL = new ArrayList();

  	hFCount = 1;

  	headerFunctionAL.add(tableResultAL.get(0));

		

  	for(int x = 1 ; x < tableResultALSize ; x++){

			ArrayList tempAL = (ArrayList)tableResultAL.get(x);

			String elemntAtZ = tempAL.get(0).toString();

							

			if(elemntAtZ.equalsIgnoreCase("H1")){

				headerFunctionAL.add(tempAL);

			}else if(elemntAtZ.equalsIgnoreCase("FN1")){

				headerFunctionAL.add(tempAL);

			}

		}



  	printReorderImage();

		for(int x = 0 ; x < tableResultALSize ; x++){

			ArrayList tempAL = (ArrayList)tableResultAL.get(x);

			String elemntAtZ = tempAL.get(0).toString();

			

			if(elemntAtZ.startsWith("C")){

				//printColumnNamesHeading(tempAL);

			}else if(elemntAtZ.startsWith("D")){

				printRows(tempAL);

			}else if(elemntAtZ.startsWith("X")){

				printXRow(tempAL);

			}else if(elemntAtZ.startsWith("H")){

				if(elemntAtZ.equalsIgnoreCase("H1")){

					printSeperateTableTopHeader();

				}else{

					printTopHeader(tempAL);

				}

			}else if(elemntAtZ.startsWith("F")){

				if(elemntAtZ.equalsIgnoreCase("FN1")){

				}else{

				printOperations(tempAL);

				}

			}

			

		}



	}



  public void printSeperateTableBottom (ArrayList tableResultAL) throws IOException {

		int 	tableResultALSize = tableResultAL.size();

		printReorderImage();

	

		for(int x = 0 ; x < tableResultALSize ; x++){

			ArrayList tempAL = (ArrayList)tableResultAL.get(x);

			String elemntAtZ = tempAL.get(0).toString();

			

			if(elemntAtZ.startsWith("C")){

				printColumnNamesHeading(tempAL);

			}else if(elemntAtZ.startsWith("D")){

				printRows(tempAL);

			}else if(elemntAtZ.startsWith("H")){

				printHeader(tempAL);

			}else if(elemntAtZ.startsWith("F")){

				printOperations(tempAL);

			}else if(elemntAtZ.startsWith("X")){

				printXRow(tempAL);

			}

		}

	}



	public void calculateFXRate(ArrayList data,Object[] rowHeadStr){

		

		String[] fxRateCols = fxrate.split(",");

		String frmCurrency = fxRateCols[0];

		String toCurrency = fxRateCols[1];

		System.out.println("toCurrency="+toCurrency+"** frmCurrency="+frmCurrency);

		String dateField=null;

		String srcCol=null;

		String trgtCol=null;

		

			if(src!=null)

				srcCol=src;

			if(trgt!=null)

				trgtCol=trgt;	

			

			

		if(fxRateCols.length>2)

			dateField = fxRateCols[2];

		ArrayList iTempAL = new ArrayList();

		for(int t = 0 ; t < rowHeadStr.length ; t++){

			iTempAL.add(t,(rowHeadStr[t].toString()));

		}

		System.out.println("iTempAL=="+iTempAL);

		int toCurrrencyindx=iTempAL.indexOf(toCurrency);

		int frmCurrencyindx = iTempAL.indexOf(frmCurrency);

		System.out.println("toCurrrencyindx="+toCurrrencyindx+"** frmCurrencyindx="+frmCurrencyindx);

		int srcindex=iTempAL.indexOf(srcCol);

		int trgtindex = iTempAL.indexOf(trgtCol);

		System.out.println("srcindex="+srcindex+"** trgtindex="+trgtindex);

		

		String toCurVal=null;

		String frmCurVal=null;

		String fxrateConst=null;

		double fxCalculatedVal=0.0d;

		String srcVal=null;

		hcount++;

		

		for(int i=0;i<data.size();i++){

			if(data.get(i)!=null){

				System.out.println("INSIDE CALCULASTEFXRATE");

				FxRateUtility fxRateUtility=null;

				ArrayList tempAL = (ArrayList)data.get(i);

				toCurVal = tempAL.get(toCurrrencyindx).toString();

				frmCurVal = tempAL.get(frmCurrencyindx).toString();

				//fxrateConst = fxRateUtility.getFxRateConstantFromDB();

				if(i==0){

					fxRateUtility= new FxRateUtility(frmCurVal,toCurVal,"");

					fxrateConst = fxRateUtility.getFxRateConstantFromDB();

					fxRateConst.put(hcount,frmCurVal +" 1 = " +toCurVal+" "+String.valueOf(fxrateConst));

					fxRatesrcTrgtVal.put(hcount,frmCurVal+";"+toCurVal);

					System.out.println(fxrateConst);

					System.out.println("fxRatesrcTrgtVal="+fxRatesrcTrgtVal);

					System.out.println("fxRateConst=="+fxRateConst);

					hcount++;

				}else{

				ArrayList nextTempAL = (ArrayList)data.get(i-1);

				

				System.out.println("tempAL=="+tempAL);

				System.out.println("nextTempAL=="+nextTempAL);

				

				 System.out.println("frmCurVal="+frmCurVal+"   toCurVal="+toCurVal);

				if(!nextTempAL.get(frmCurrencyindx).toString().equalsIgnoreCase(frmCurVal))

				{

					fxRateUtility= new FxRateUtility(frmCurVal,toCurVal,"");

					fxrateConst = fxRateUtility.getFxRateConstantFromDB();

					fxRateConst.put(hcount,frmCurVal +" 1 = " +toCurVal+" "+String.valueOf(fxrateConst));

					fxRatesrcTrgtVal.put(hcount,frmCurVal+";"+toCurVal);

					System.out.println(fxrateConst);

					System.out.println("fxRatesrcTrgtVal="+fxRatesrcTrgtVal);

					System.out.println("fxRateConst=="+fxRateConst);

					hcount++;

				}

				}

				if(tempAL.get(srcindex)!=null && !tempAL.get(srcindex).toString().equalsIgnoreCase("null")

						&& !tempAL.get(srcindex).toString().equals("") ){

					srcVal = tempAL.get(srcindex).toString();

				fxCalculatedVal = Double.parseDouble(fxrateConst) * Double.parseDouble(srcVal);

				System.out.println("cond satisfied fxCalculatedVal="+fxCalculatedVal);

				}

				tempAL.set(trgtindex,String.valueOf(fxCalculatedVal));

				System.out.println("tempAL AFTER CALCULATING=="+tempAL);

			}

				

		}

	}



  

  public void XtraColumnAddToTableResultAL(ArrayList tableResultAL, int itr)

  {

	  	String[] srcCols = src.split(",");//src currency- BaseCCY

	  	String[] trgtCols = trgt.split(",");//trgt currency- Security CCY

	  	String[] fxrateCols = fxrate.split(",");//AssetVal-BaseCCyAssetVal

	  	String[] fxrate = fxrateCols[itr].split("-");

	  	ArrayList temp=(ArrayList)tableResultAL.get(0);

		int srcindex=temp.indexOf(srcCols[itr]);

		int trgtindex = temp.indexOf(trgtCols[itr]);

		int index=temp.size()-2;

		

		temp.add(index+1,fxrate[1]);

	//	System.out.println("temp=="+temp);

		

		int fxrateSrcColindx = temp.indexOf(fxrate[0]);

		int fxrateTrgtColindx = temp.indexOf(fxrate[1]);

		double f1Value=0.0,f2Value=0.0,f3Value=0.0,xvalue=0.0;

		for(int i=0;i<tableResultAL.size();i++)

		{

			

			ArrayList tempAL = (ArrayList)tableResultAL.get(i);

			String elemntAtZ = tempAL.get(0).toString();

			

			if(elemntAtZ.equalsIgnoreCase("H1")){

				f1Value=0.0;		

				 }

			if(elemntAtZ.equalsIgnoreCase("H2")){

				f2Value=0.0;		

			   }

			if(elemntAtZ.equalsIgnoreCase("H3")){

				f3Value=0.0;		

			}

			if(elemntAtZ.equalsIgnoreCase("DR"))

			{

				double val=0.0;

				double constant=0.0;

				double fxx=0.0;

			    if((tempAL.get(fxrateSrcColindx)==null))

				{

					val=0.0;

				}

				else

				{

					if(tempAL.get(srcindex).toString().equalsIgnoreCase(tempAL.get(trgtindex).toString()))

					{

						tempAL.add(fxrateTrgtColindx,tempAL.get(fxrateSrcColindx).toString());

					}

					else

					{

					//	System.out.println("B4 tempAL=="+tempAL);

						TableManipulator tmObject1 = new TableManipulator();

						String constStr = tempAL.get(srcindex).toString()+"-"+tempAL.get(trgtindex).toString();

						val=Double.parseDouble(tempAL.get(fxrateSrcColindx).toString());

				///		System.out.println("constStr=="+constStr);

						constant=Double.parseDouble((String)formRB.getString(constStr + "." +"Constant"));

					//	System.out.println("constant=="+constant);

						fxx=tmObject1.getFXrates(val,constant);

						tempAL.add(fxrateTrgtColindx,String.valueOf(fxx));

					//	System.out.println("tempAL=="+tempAL);

						

					}

					

							

							

					}

			    if(headerLevel==3)

			    	f3Value=f3Value+Double.parseDouble(tempAL.get(fxrateTrgtColindx).toString());

			    else if(headerLevel==2)

			    	f2Value=f2Value+Double.parseDouble(tempAL.get(fxrateTrgtColindx).toString());

			    else if(headerLevel==1)

			    	f1Value=f1Value+Double.parseDouble(tempAL.get(fxrateTrgtColindx).toString());

			}

			if(elemntAtZ.equalsIgnoreCase("FN3")){

				f2Value=f2Value+f3Value;

				ArrayList tempAL1 = (ArrayList)tempAL.get(1);

				tempAL1.add(fxrateTrgtColindx,String.valueOf(f3Value));

				f3Value=0.0;

			}

			if(elemntAtZ.equalsIgnoreCase("FN2")){

			    f1Value=f1Value+f2Value;

				ArrayList tempAL1 = (ArrayList)tempAL.get(1);

				tempAL1.add(fxrateTrgtColindx,String.valueOf(f2Value));

				f2Value=0.0;

			}

			if(elemntAtZ.equalsIgnoreCase("FN1")){

			    xvalue=xvalue+f1Value;

				ArrayList tempAL1 = (ArrayList)tempAL.get(1);

				tempAL1.add(fxrateTrgtColindx,String.valueOf(f1Value));

				f1Value=0.0;

			}

			if(elemntAtZ.equalsIgnoreCase("X")){

				ArrayList tempAL1 = (ArrayList)tempAL.get(1);

				tempAL1.add(fxrateTrgtColindx,String.valueOf(xvalue));

				f1Value=0.0;

			}

		}

	

	}

  public void printHeader(ArrayList tempAL) throws IOException{

		

		//className="layoutMaxTable";

		//className="tableTitle";

		String hName = (String)tempAL.get(0);

	

		if(hName.equalsIgnoreCase("H1")){

			className="H1title";

		}else if(hName.equalsIgnoreCase("H2")){

			className="H2title";

		}else if(hName.equalsIgnoreCase("H3")){

			className="H3title";

		}





		String hVal = (String)tempAL.get(1);

		String[] colHeader = new String[4];

		colHeader = hVal.split(":");

		for (int z = 0;z<colHeader.length;z++) {

			for (int q = 0;q<constDBCol.length;q++) {

				if(constDBCol[q].equalsIgnoreCase(colHeader[z])){

					colHeader[z]=constDsplCol[q];

				}

			}

		}

		out.append ("<tr>");

		out.append ("<td width=\"100%\" colspan=\"10\">");

		out.append ("<table width=\"35%\" class=\"grouptable\"><tr>");

		

		out.append ("<td class="+className+">"+colHeader[0]+": "+colHeader[1]+"</td>");

		//out.append ("<td><B><U>"+colHeader[0]+": "+colHeader[1]+"</U></B></td>");

		out.append ("</tr>");

		out.append ("</table>");

		out.append ("</td></tr>");

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

      System.out.println("colCssMap="+colCssMap);

 	return colCssMap;

 }



public String getFXrate(String fStr,String constant)

{

	double d=Double.parseDouble(fStr);

	double e=Double.parseDouble(constant);

	double f=(double)(d/e);

	String s=new String();

	s=s+f;

	return s;

	

}



public Object getReportDataForExport(HashMap attributeMap,HttpSession sessionObject,Object formObj1,Object formObjname)

{

	System.out.println("formObj1="+formObj1);

	

	System.out.println("attributeMap="+attributeMap);

	

	

	EbwRepTable ebwReptable = new EbwRepTable();

	if(attributeMap!=null ){

		ebwReptable.setName((String)attributeMap.get("name"));

		ebwReptable.setFormname((String)attributeMap.get("formname"));

		ebwReptable.setXcolopstr((String)attributeMap.get("xcolopstr"));

		ebwReptable.setAttrObj(formObj1);

	}

	

	name=ebwReptable.getName();

	formname=ebwReptable.getFormname();

	xcolopstr = ebwReptable.getXcolopstr();

	System.out.println("formname="+this.formname);

	System.out.println("name="+this.name);

	try 

	{

		if(formname != null){

            //attrObj = pageContext.getRequest().getAttribute(formname);

			// attrObj = formObj1;

			 attrObj = formObjname;

		}

	    if(attrObj == null && formname != null){

            attrObj = pageContext.getSession().getAttribute(formname);

	    	//attrObj = formObjname;

	    }



        Class formClass = Class.forName(attrObj.getClass().getName());

		appRB = ResourceBundle.getBundle("ApplicationConfig");

		formRB = ResourceBundle.getBundle(this.appRB.getString("message-resources"));	

		formObj=(DataInterface)formClass.getMethod("get" + StringUtil.initCaps(name) + "Collection", null).invoke(attrObj, null);

		System.out.println("\n**************************************************************************************************************************** ");

		System.out.println("--------------------------                              EBWREPORTTABLE:::STARTING                   ------------------- ");

		System.out.println("**************************************************************************************************************************** \n");

	

		ArrayList arValAL=(ArrayList)formObj.getData();

		

		if(arValAL == null ||arValAL.size()==0 ||  arValAL.isEmpty()){

			System.out.println("\n     DATA COMING FROM DB IS NULL........  "+arValAL);

			out.append("\n   Data is not available  ");

		}else{

		ArrayList rowHeadAL = (ArrayList)formObj.getRowHeader();

		

		rowHead = (LinkedHashMap)rowHeadAL.get(0);

		Set rowHeadSet = rowHead.keySet();

		Object[] rowHeadStr = (Object[])rowHeadSet.toArray();

		

		//ArrayList colattrobj=(ArrayList)formObj.getColAttrObjs();	



		String prevRowSumboo;

		if(prevRowSum!=null)

		   prevRowSumboo=prevRowSum;

		else

			prevRowSumboo="";

		

		getHeaderInformationFromBean();

		

        ArrayList displayAL = (ArrayList)tableHeader;

		userCSA = (String)displayAL.get(0);

		headerLevel = new Integer(noOfHeaders);

		System.out.println("\n tableHeader::---------------> "+tableHeader);

		TableManipulator tmObject = new TableManipulator();

		Object objOutput = null;

		objOutput =tmObject.getManipulatedDataForExport(arValAL,rowHeadStr,headerLevel,tableHeader,columnOps,xcolopstr,prevRowSumboo);

		

		Object tableResultALEx=null;

		tableResultALEx = objOutput;

		System.out.println("tableResultALEx.getClass()="+tableResultALEx.getClass());

		Class cls[]={Class.forName("java.lang.Object")};

		Object obj[]={tableResultALEx};

		formClass.getMethod("set" + StringUtil.initCaps(name) + "TableData", cls).invoke(attrObj, obj);

		

		}

	}

		catch(Exception e)

		{

			e.printStackTrace();

			

			}

		return formObjname;

}

		

	

	private void getHeaderInformationFromBean()

	{

		ArrayList colattrobj=(ArrayList)formObj.getColAttrObjs();	

		

		stBuff = new StringBuffer(); 

		StringBuffer sBuffer = new StringBuffer();

		int colattrobjSize = colattrobj.size();

		testCol = new String[colattrobjSize+1];

		constDsplCol = new String[colattrobjSize+1];

		constDBCol= new String[colattrobjSize+1];

		if(colattrobj != null && colattrobj.size() > 0)

		{

			if(xcolopstr==null)

				xcolopstr="Sum:Sum";

			String usercolOp = (String)xcolopstr;

			for(int d = 0; d < colattrobj.size(); d++)

			{

				TableColAttrObj tablecolattrobj = (TableColAttrObj)colattrobj.get(d);

				String columnOpStr = "";

				String headerStr = tablecolattrobj.getHeader().toString();

				columnOpStr = tablecolattrobj.getColumnOp().toString();

				String colNameStr = tablecolattrobj.getColName().toString();

				

				if(d == 0 ){

					tableHeader.add(disptype);

					columnOps.put(colNameStr,columnOpStr);

					if(!headerStr.equalsIgnoreCase("No") || !headerStr.equalsIgnoreCase("")){

						

						if(headerStr.equalsIgnoreCase("Header1")){

						   tableHeader.add(1,colNameStr);

						   noOfHeaders++;}

						else if(headerStr.equalsIgnoreCase("Header2")){

							   tableHeader.add(2,colNameStr);

							   noOfHeaders++;}

						else if(headerStr.equalsIgnoreCase("Header3")){

							   tableHeader.add(3,colNameStr);

							   noOfHeaders++;}

						else if(headerStr.equalsIgnoreCase("Header4")){

							   tableHeader.add(4,colNameStr);

							   noOfHeaders++;}

					}

				}else {

					columnOps.put(colNameStr,columnOpStr);

					

					if(!headerStr.equalsIgnoreCase("No") || !headerStr.equalsIgnoreCase("")){

						

						if(headerStr.equalsIgnoreCase("Header1")){

						   tableHeader.add(1,colNameStr);

						   noOfHeaders++;}

						else if(headerStr.equalsIgnoreCase("Header2")){

							   tableHeader.add(2,colNameStr);

							   noOfHeaders++;}

						else if(headerStr.equalsIgnoreCase("Header3")){

							   tableHeader.add(3,colNameStr);

							   noOfHeaders++;}

						else if(headerStr.equalsIgnoreCase("Header4")){

							   tableHeader.add(4,colNameStr);

							   noOfHeaders++;}

					}

				}

				



				String str = new String();

				String componentStrValue = new String();

				boolean toDisplayVal;

				String toDoubleVal= new String();

				String strChkHead = new String();

		        str = tablecolattrobj.getColName().toString();

				componentStrValue = tablecolattrobj.getComponentType().toString();

				toDoubleVal = tablecolattrobj.getDataType().toString();

				strChkHead = 	formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + str);	

				

				constDBCol[d] = str;

				constDsplCol[d] = strChkHead;

			

				if(componentStrValue.equalsIgnoreCase("href")){

					hrefLinkAL.add(str); 

				}

				if(toDoubleVal.equalsIgnoreCase("Double")){

					toDouble.add(str); 

				}

				if(!componentStrValue.equalsIgnoreCase("Hidden")){

					toDisplay.add(str);

				}

				

				sBuffer.append(strChkHead).append(",");

				stBuff.append(str).append(",");

				if(d == (colattrobj.size()-1) && (!usercolOp.equals("")))	{

					String[] colOp = new String[2];

					colOp = usercolOp.split(":");

					String str1 = colOp[1];

					String str1Head = 	formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + str1);

					constDBCol[d+1] = str1;

					constDsplCol[d+1] = str1Head;

					sBuffer.append(str1Head).append(",");

					stBuff.append(str1).append(",");

				}

			

			}

		}

		

		stBuff.deleteCharAt((stBuff.length()-1));

		sBuffer.deleteCharAt((sBuffer.length()-1));



	}

	

	public StringBuffer getRepTableThruAjax(HashMap attributeMap,HttpSession sessionObject,Object reqformObj){

		System.out.println("inside ajax call method in ebwreptable");

		ajaxCall = true;

		StringBuffer resultBuffer = new StringBuffer();

		EbwRepTable ebwReptable = new EbwRepTable();

		if(attributeMap!=null ){

			ebwReptable.setName((String)attributeMap.get("name"));

			ebwReptable.setFormname((String)attributeMap.get("formname"));

			ebwReptable.setXcolopstr((String)attributeMap.get("xcolopstr"));

			ebwReptable.setAttrObj(reqformObj);

			ebwReptable.setSorting((String)attributeMap.get("sorting"));

			ebwReptable.setHeaderSortCol((String)attributeMap.get("headerSortCol"));

			ebwReptable.setDisptype((String)attributeMap.get("disptype"));

		}

		

		name=ebwReptable.getName();

		formname=ebwReptable.getFormname();

		xcolopstr = ebwReptable.getXcolopstr();

		sorting = ebwReptable.getSorting();

		headerSortCol = ebwReptable.getHeaderSortCol();

		System.out.println("formname="+this.formname);

		System.out.println("name="+this.name);

		try 

		{

			if(formname != null){

	            //attrObj = pageContext.getRequest().getAttribute(formname);

				// attrObj = formObj1;

				 attrObj = reqformObj;

			}

		    if(attrObj == null && formname != null){

	            attrObj = pageContext.getSession().getAttribute(formname);

		    	//attrObj = formObjname;

		    }

		    System.out.println("name="+this.name);



	        Class formClass = Class.forName(attrObj.getClass().getName());

			appRB = ResourceBundle.getBundle("ApplicationConfig");

			formRB = ResourceBundle.getBundle(this.appRB.getString("message-resources"));	

			formObj=(DataInterface)formClass.getMethod("get" + StringUtil.initCaps(name) + "Collection", null).invoke(attrObj, null);

			System.out.println("\n**************************************************************************************************************************** ");

			System.out.println("--------------------------                              EBWREPORTTABLE:::STARTING                   ------------------- ");

			System.out.println("**************************************************************************************************************************** \n");

		

			ArrayList arValAL=(ArrayList)formObj.getData();

			

			if(arValAL == null ||arValAL.size()==0 ||  arValAL.isEmpty()){

				System.out.println("\n     DATA COMING FROM DB IS NULL........  "+arValAL);

				out.append("\n   Data is not available  ");

			}else{

			ArrayList rowHeadAL = (ArrayList)formObj.getRowHeader();

			

			rowHead = (LinkedHashMap)rowHeadAL.get(0);

			Set rowHeadSet = rowHead.keySet();

			Object[] rowHeadStr = (Object[])rowHeadSet.toArray();

			

			//ArrayList colattrobj=(ArrayList)formObj.getColAttrObjs();	



			String prevRowSumboo;

			if(prevRowSum!=null)

			   prevRowSumboo=prevRowSum;

			else

				prevRowSumboo="";

			

			getHeaderInformationFromBean();

			

	        ArrayList displayAL = (ArrayList)tableHeader;

			userCSA = (String)displayAL.get(0);

			headerLevel = new Integer(noOfHeaders);

			System.out.println("\n tableHeader::---------------> "+tableHeader);

			TableManipulator tmObject = new TableManipulator();

			Object objOutput = null;

			objOutput =tmObject.getManipulatedDataForExport(arValAL,rowHeadStr,headerLevel,tableHeader,columnOps,xcolopstr,prevRowSumboo);

			System.out.println("objOutput=="+objOutput);

			Object tableResultALEx=null;

			tableResultALEx = objOutput;

			

			String restartKeyFromFormbean = ((EbwForm)attrObj).getRestartKey();

			System.out.println("restartKeyFromFormbean=="+restartKeyFromFormbean);

			String restartKey[] = restartKeyFromFormbean.split(":");

			String headerSortKey = restartKey[0];

				int sortingColindx=0;

				ArrayList sortingAL = new ArrayList();

				ArrayList afterSortingAL = new ArrayList();

				

				System.out.println("sorting=="+sorting);

			if(sorting!=null)

			{

				Comparator comparator = Collections.reverseOrder(); 

				ArrayList tableResultAL = (ArrayList)tableResultALEx;

				ArrayList colNames = (ArrayList)tableResultAL.get(0);

				ArrayList colValAL = new ArrayList();

				colValAL.add("CL");

				for(int d=0;d<colNames.size();d++){

					colValAL.add(colNames.get(d).toString());

				}

				afterSortingAL.add(colValAL);

				for(int q=0;q<colNames.size();q++)

				{

					String colVal=(String)colNames.get(q);

					if(colVal.equalsIgnoreCase(headerSortKey))

						sortingColindx=q;

				}

				System.out.println("sortingColindx="+sortingColindx);

				for(int j=1;j<tableResultAL.size();j++)

				{

					ArrayList rowAL = (ArrayList)tableResultAL.get(j);

					sortingAL.add(rowAL.get(sortingColindx).toString()+j);

					

				}

				System.out.println("sortingAL - before="+sortingAL);

				Collections.sort(sortingAL,comparator);  

				System.out.println("sortingAL - After="+sortingAL);

				

				for(int k=0;k<sortingAL.size();k++)

				{

					char rowIdVal = sortingAL.get(k).toString().charAt(sortingAL.get(k).toString().length()-1);

					Character c = new Character(rowIdVal);

					String chr = c.toString();

					for(int j=1;j<tableResultAL.size();j++){

						

						if(String.valueOf(j).equals(chr)){

							ArrayList rowAL = (ArrayList)tableResultAL.get(j);

							ArrayList rowVal = new ArrayList();

							//System.out.println("rowAL before setting="+rowAL);

							//String sortedData = sortingAL.get(k).toString().substring(0,sortingAL.get(k).toString().length()-1);

						//rowAL.set(sortingColindx, sortedData);

						//System.out.println("rowAL after setting="+rowAL);

							rowVal.add("DR");

							for(int h=0;h<rowAL.size();h++)

								rowVal.add(rowAL.get(h).toString());

						afterSortingAL.add(rowVal);

						}

					}

					

				}

				System.out.println("afterSortingAL  ="+afterSortingAL);

				tableResultALEx = afterSortingAL;

			}		

			System.out.println("tableResultALEx.getClass()="+tableResultALEx.getClass());

			Class cls[]={Class.forName("java.lang.Object")};

			Object obj[]={tableResultALEx};

			formClass.getMethod("set" + StringUtil.initCaps(name) + "TableData", cls).invoke(attrObj, obj);

			ebwReptable.setAjaxCall(true);

			System.out.println("Before doStartTag=ajaxCall="+ajaxCall);

			ebwReptable.doStartTag();

			System.out.println("after doStartTag=out=="+ebwReptable.getOut());

			}

		}

			catch(Exception e)

			{

				e.printStackTrace();

				

				}

			System.out.println("out=="+ebwReptable.getOut());

			return ebwReptable.getOut();



	}





private ArrayList getSortedTableResultSetAL(ArrayList tableResultAL,String headerSortKey,String sortType){

System.out.println("Inside getSortedTableResultSetAL");

	ArrayList sortedTableResultAL = new ArrayList();

	int sortingColindx=-1;

	ArrayList sortingAL = new ArrayList();

	Comparator comparator=null;

	if(sorting!=null)

	{

		if(!sortType.equalsIgnoreCase("ASC"))

		 comparator = Collections.reverseOrder(); 

		ArrayList colNames = (ArrayList)tableResultAL.get(0);

		ArrayList colValAL = new ArrayList();

		//colValAL.add("CL");

		for(int d=0;d<colNames.size();d++){

			colValAL.add(colNames.get(d).toString());

		}

		sortedTableResultAL.add(colValAL);

		String colVal="";

		for(int q=0;q<colNames.size();q++)

		{

			colVal=(String)colNames.get(q);

			if(colVal.equalsIgnoreCase(headerSortKey))

				sortingColindx=q;

		}

		System.out.println("sortingColindx="+sortingColindx);

		ArrayList rowAL = new ArrayList();

		String elementZ="";

		for(int j=1;j<tableResultAL.size();j++)

		{

			rowAL = (ArrayList)tableResultAL.get(j);

			elementZ=(String)rowAL.get(0);

			if(elementZ.startsWith("D"))

				sortingAL.add(rowAL.get(sortingColindx).toString()+"~"+j);

			

		}

		System.out.println("sortingAL - before="+sortingAL);

		Collections.sort(sortingAL,comparator);  

		System.out.println("sortingAL - After="+sortingAL);

		

		String rowIdVal;

		ArrayList sortedRowAL = new ArrayList();

		ArrayList rowVal=null;

		ArrayList rowAL1 = new ArrayList();

		for(int k=0;k<sortingAL.size();k++)

		{

			rowIdVal = sortingAL.get(k).toString().substring(sortingAL.get(k).toString().indexOf("~")+1,sortingAL.get(k).toString().length());

			//System.out.println("rowIdVal="+rowIdVal);

			// c = new Character(rowIdVal);

			//chr = c.toString();

			for(int j=1;j<tableResultAL.size();j++){

				rowAL1 = (ArrayList)tableResultAL.get(j);

				

				elementZ=(String)rowAL1.get(0);

				if(elementZ.startsWith("D")){

				if(String.valueOf(j).equals(rowIdVal)){

					sortedRowAL = (ArrayList)tableResultAL.get(j);

					//System.out.println("table row="+sortedRowAL);

					rowVal = new ArrayList();

					//rowVal.add("DR");

					for(int h=0;h<sortedRowAL.size();h++){

						

						if(sortedRowAL.get(h)!=null)

							rowVal.add(sortedRowAL.get(h).toString());

						else

							rowVal.add("0");

					}

					//System.out.println("table row after="+rowVal);

					sortedTableResultAL.add(rowVal);

				 }

				}

			}

			

		}

		System.out.println("afterSortingAL  ="+sortedTableResultAL);

		

	}

	

	

	return sortedTableResultAL;

}

private ArrayList getPagnTableResultSetAL(ArrayList tableResultAL){

	System.out.println("Inside getSortedTableResultSetAL");

		ArrayList pagnTableResultSetAL = new ArrayList();

		ArrayList tempAL = new ArrayList();

		String elementZ = "";

		int rowsInPage=0;

		ArrayList pgnResultSetAL = new ArrayList();

		ArrayList colNames = (ArrayList)tableResultAL.get(0);

		ArrayList colValAL = new ArrayList();

		//colValAL.add("CL");

		for(int d=0;d<colNames.size();d++){

			colValAL.add(colNames.get(d).toString());

		}

		pgnResultSetAL.add(colValAL);

		if(rowsPerPage!=null)

		{

			rowsInPage = Integer.parseInt(rowsPerPage);

			for(int h=0;h<tableResultAL.size();h++){

				if(rowsInPage!=h){

					tempAL = (ArrayList)tableResultAL.get(h);

					

					elementZ=(String)tempAL.get(0);

					if(elementZ.startsWith("D")){

						pgnResultSetAL.add(tempAL);

					}

				}

				else

					break;

			}

			

			

			

			

			

			

		}

		

		

		return pagnTableResultSetAL;

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





private String getHeaderSortImgStr(String asc){

	StringBuffer sb = new StringBuffer();

	

	if (asc!=null){			

		if(asc.equalsIgnoreCase("asc")){

			sb.append(formRB.getString("HeaderSortAscImg"));			

		}else{

			sb.append(formRB.getString("HeaderSortDescImg"));

		}

	}else{

		sb.append(formRB.getString("HeaderSortDftImg"));

	}

	//System.out.println("sb.toString()=="+sb.toString());

	

	return sb.toString();

}









  public String getName()

  {

      return name;

  }



  public void setName(String name)

  {

      this.name = name;

  }





  public String getServicename()

  {

      return servicename;

  }



  public void setServicename(String servicename)

  {

      this.servicename = servicename;

  }





	 public String getFormname()

  {

      return formname;

  }



  public void setFormname(String formname)

  {

      this.formname = formname;

  }

	

  public String getDisptype()

  {

      return disptype;

  }



  public void setDisptype(String disptype)

  {

      this.disptype = disptype;

  }

  public String getLevel()

  {

      return level;

  }



  public void setLevel(String level)

  {

      this.level = level;

  }

  public String getToptabcols()

  {

      return toptabcols;

  }



  public void setToptabcols(String toptabcols)

  {

      this.toptabcols = toptabcols;

  }

  

  public String getXcolopstr()

  {

      return xcolopstr;

  }



  public void setXcolopstr(String xcolopstr)

  {

      this.xcolopstr = xcolopstr;

  }

  

  public String getCntryname() {

		return cntryname;

	}



	public void setCntryname(String cntryname) {

		this.cntryname = cntryname;

	}



	public void setFxrate(String fxrate) {

		this.fxrate = fxrate;

	}



	public String getSrc() {

		return src;

	}



	public void setSrc(String src) {

		this.src = src;

	}



	public String getTrgt() {

		return trgt;

	}



	public void setTrgt(String trgt) {

		this.trgt = trgt;

	}



	public String getCurcol() {

		return curcol;

	}



	public void setCurcol(String curcol) {

		this.curcol = curcol;

	}



	public String getFalseHeaderCol() {

		return falseHeaderCol;

	}



	public void setFalseHeaderCol(String falseHeaderCol) {

		this.falseHeaderCol = falseHeaderCol;

	}



	public String getCurrHeaderCol() {

		return currHeaderCol;

	}



	public void setCurrHeaderCol(String currHeaderCol) {

		this.currHeaderCol = currHeaderCol;

	}

	

	public String getPrevRowSum()

    {

        return prevRowSum;

    }



    public void setPrevRowSum(String prevRowSum)

    {

        this.prevRowSum = prevRowSum;

    }

    

    public String getTablesummarycols()

    {

        return tablesummarycols;

    }



    public void setTablesummarycols(String tablesummarycols)

    {

        this.tablesummarycols = tablesummarycols;

    }

    public String getToptablesummarycols() {

		return toptablesummarycols;

	}



	public void setToptablesummarycols(String toptablesummarycols) {

		this.toptablesummarycols = toptablesummarycols;

	}

  

  private ResourceBundle formRB;

  private ResourceBundle appRB;

	//private ResourceBundle  statRB;

	private String name;

	

	private String servicename;

	private String formname;

	private String disptype;

	private String level;

	private String xcolopstr;

	private String toptabcols;

	private DataInterface formObj;

	private ArrayList tableHeader;

	

	private String fxrate;

	private String src;

	private String trgt;

	private String curcol;

	private String cntryname;

	private String falseHeaderCol;

	private String currHeaderCol;  

	private String prevRowSum;

	

	private String tablesummarycols;

	private String toptablesummarycols;

	private String tableLevelCSS;

	private String colLevelCSS;

	

	private boolean exportEnabled=false;

	private boolean pdfEnabled=false;

	

	private String tableWidth;

	private String topTableWidth;

	

	

	

	public String getTableWidth() {

		return tableWidth;

	}



	public void setTableWidth(String tableWidth) {

		this.tableWidth = tableWidth;

	}



	public String getTopTableWidth() {

		return topTableWidth;

	}



	public void setTopTableWidth(String topTableWidth) {

		this.topTableWidth = topTableWidth;

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

	

   public Object getAttrObj() {

		return attrObj;

	}

	public void setAttrObj(Object attrObj) {

		this.attrObj = attrObj;

	}



	public String getHeaderSortCol() {

		return headerSortCol;

	}



	public void setHeaderSortCol(String headerSortCol) {

		this.headerSortCol = headerSortCol;

	}



	public String getSorting() {

		return sorting;

	}



	public void setSorting(String sorting) {

		this.sorting = sorting;

	}



	public boolean isAjaxCall() {

		return ajaxCall;

	}



	public void setAjaxCall(boolean ajaxCall) {

		this.ajaxCall = ajaxCall;

	}



	public StringBuffer getOut() {

		return out;

	}



	public void setOut(StringBuffer out) {

		this.out = out;

	}

	

	 public String getNestedTable() {

	        return nestedTable;

	    }

	    public void setNestedTable(String nestedTable) {

	        if (nestedTable!=null && nestedTable.equalsIgnoreCase("true"))

	            isNestedTable = true;

	        this.nestedTable = nestedTable;

	    }



		public String getRowsPerPage() {

			return rowsPerPage;

		}



		public void setRowsPerPage(String rowsPerPage) {

			this.rowsPerPage = rowsPerPage;

		}

		

		public String getHiddenCols() {

			return hiddenCols;

		}



		public void setHiddenCols(String hiddenCols) {

			this.hiddenCols = hiddenCols;

		}



		public String getReordering() {

			return reordering;

		}



		public void setReordering(String reordering) {

			this.reordering = reordering;

		}



		public boolean isExportEnabled() {

			return exportEnabled;

		}



		public void setExportEnabled(boolean exportEnabled) {

			this.exportEnabled = exportEnabled;

		}



		public boolean isPdfEnabled() {

			return pdfEnabled;

		}



		public void setPdfEnabled(boolean pdfEnabled) {

			this.pdfEnabled = pdfEnabled;

		}

}



		    

		    