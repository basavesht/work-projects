package com.tcs.ebw.taglib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;

public class ReorderTableHelper extends EbwTable{
	/*
	 * the below method has to insert/update the DB reorderinfo table 
	 * with reorder column information.
	 * the inputs being usrUserId,name,formname,stateEbwtable,applnName
	 * After updating/inserting the DB, the same info is to be stored in the session also.
	 * 
	 */
	public String getReorderFromHiddenVar(String reorderColHidnVar,String usrUserId,String tblname,String formname,String stateEbwtable,String applnName,HttpSession sessionObj){
		EBWLogger.trace(this,"Entered inside getReorderFromHiddenVar");
		String reorderCols = null;
		callInsertIntoReorderDB(reorderColHidnVar,usrUserId,tblname,formname,stateEbwtable,applnName);
		reorderCols = reorderColHidnVar;
		if(reorderCols != null && reorderCols.length()>0){
			//have to put the reorderinfo in the session object.
			setSessionReorder(tblname,sessionObj,stateEbwtable,reorderCols);

		}
		EBWLogger.trace(this,"Exited from getReorderFromHiddenVar with reordecols :"+reorderCols);
		return reorderCols;
	}

	/*
	 *this method is used to get the reorderinfo from the usersession Object 
	 */
	public String getReorderFromUsrObj(String tblname,String stateEbwtable,HttpSession sessionObj){
		EBWLogger.trace(this,"Entered inside getReorderFromUsrObj");
		String reorderCols = null;
		String key1 = stateEbwtable+"_"+tblname;
		HashMap userObject = new HashMap();
		if(sessionObj.getAttribute("USER_OBJ")!=null){
			userObject = (HashMap)sessionObj.getAttribute("USER_OBJ");
			reorderCols = (String)userObject.get(key1);
		}
		EBWLogger.trace(this,"Exited from getReorderFromUsrObj with reordecols :"+reorderCols);
		return reorderCols;
	}

	/*
	 * this method is used to get the reorder column information from the reoderinfo table 
	 * by making a DB call.
	 * After getting the reorder col info, the same is to be stored in the session.
	 */
	public String getReorderFromApplnLevel(String usrUserId,String tblname,String formname,String stateEbwtable,String applnName,HttpSession sessionObj){
		EBWLogger.trace(this,"Entered inside getReorderFromApplnLevel");
		String reorderCols = null;
		reorderCols = getReorderInfoBeforeInsert(usrUserId,tblname,formname,stateEbwtable);
		if(reorderCols != null && reorderCols.length()>0){
			//have to put the reorderinfo in the session object.
			setSessionReorder(tblname,sessionObj,stateEbwtable,reorderCols);

		}
		EBWLogger.trace(this,"Exited from getReorderFromApplnLevel with reordecols :"+reorderCols);	
		return reorderCols;
	}

	public  String getColAtrHeader(ArrayList colattrobjForShuffle,ResourceBundle formRB,String formname,String tblName,String getdisplaytype,String stateEbwtable,HttpSession sessionObj){

		//System.out.println("Inside getColAtrHeader");
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace("EbwTableHelper :","Entered into getColAtrHeader()");
		String colAttrHeaderName = null;
		colAttrHeaderName="";

		TableColAttrObj tableColAttrObj = new TableColAttrObj();
		for(int i=0;i<colattrobjForShuffle.size();i++){
			String colName = null;
			tableColAttrObj = (TableColAttrObj)colattrobjForShuffle.get(i);
			colName = tableColAttrObj.getColName();
			if(getdisplaytype!=null && getdisplaytype.length()>0 && i==0){

			}else
				colAttrHeaderName=colAttrHeaderName+getBundleString(colName, colName, formRB, formname, tblName,"inHelper")+",";

		}
		if(colAttrHeaderName !=null && colAttrHeaderName.length()>0)
			colAttrHeaderName = colAttrHeaderName.substring(0,colAttrHeaderName.lastIndexOf(","));

		//have to set the reorder info into the session
		if(colAttrHeaderName != null && colAttrHeaderName.length()>0){
			//have to put the reorderinfo in the session object.
			setSessionReorder(tblName,sessionObj,stateEbwtable,colAttrHeaderName);

		}

		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace("EbwTableHelper :","Exiting from getColAtrHeader():- output="+colAttrHeaderName);
		return colAttrHeaderName;

	}

	private  String getBundleString(String key, String value,ResourceBundle formRB,String formname,String tblName,String helper) {
		String strMsg = value;
		try {
			if (formRB!=null) {
				if (key != null) {
					strMsg = formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + tblName + "." + key);
				} else {
					strMsg = formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + tblName);
				}
			}
		}catch (Exception exc) {

			if((key.indexOf("NoDataFoundMsg") == -1))
				strMsg="";


		}
		return strMsg;
	}

	/*
	 * This method  makes a DB insert/update .
	 * If there is no such row in the DB , we have to do a DB insert.
	 * If already there , we have to make a DB update
	 */
	public void callInsertIntoReorderDB(String reorderDataInTablePresent,String colNamesAfterReorderInColAttr,String usrUserId,String tblname,String formname,String stateEbwtable){
		EBWLogger.trace(this,"Entered into callInsertIntoReorderDB(String reorderColHidnVar,String usrUserId,String tblname,String formname,String stateEbwtable,String applnName)");
		EBWLogger.trace(this,"userId: "+usrUserId);
		EBWLogger.trace(this,"tblname: "+tblname);
		EBWLogger.trace(this,"colNamesAfterReorderInColAttr: "+colNamesAfterReorderInColAttr);
		EBWLogger.trace(this,"reorderDataInTablePresent: "+reorderDataInTablePresent);
		//System.out.println("Entered into callInsertIntoReorderDB");

		//LinkedHashMap map = new LinkedHashMap();
		//String  reorderDataInTablePresent = getReorderInfoBeforeInsert(usrUserId,tblname,formname,stateEbwtable,applnName);
		if(reorderDataInTablePresent !=null && reorderDataInTablePresent.length()>0){
			try{


				IEBWService service = EBWServiceFactory.create("updateReorderInfo");

				HashMap param = new HashMap();


				param.put("USERID",usrUserId);
				param.put("TABLENAME",tblname);
				param.put("SCREENNAME",formname);
				param.put("STATE",stateEbwtable);
				//param.put("applicationname",applnName);
				param.put("REORDERCOLS",colNamesAfterReorderInColAttr);

				Class cls [] ={Object.class, Boolean.class};
				Object objParams[] = {param,new Boolean(false)};
				service.execute(cls, objParams);
			}catch (Exception e){
				System.out.println("serviceid not found1:"+e);
			}
		}else{
			try{

				//System.out.println("INTO INSET");
				IEBWService service = EBWServiceFactory.create("insertReorderInfo");
				HashMap param = new HashMap();
				param.put("USERID",usrUserId);
				param.put("TABLENAME",tblname);
				param.put("SCREENNAME",formname);
				param.put("STATE",stateEbwtable);
				//param.put("applicationname",applnName);
				param.put("REORDERCOLS",colNamesAfterReorderInColAttr);
				//System.out.println("INTO INSET:PARAM#"+param);

				Class cls [] ={Object.class, Boolean.class};
				Object objParams[] = {param,new Boolean(false)};
				service.execute(cls, objParams);
				//System.out.println("INTO INSET:Afer Service#");
			}catch (Exception e){
				System.out.println("serviceid not found1:"+e);
			}

		}


		EBWLogger.trace(this,"Finished callInsertIntoReorderDB(String userId,String stmtID,String reorderColumns)");
		//return map;

	}

	/*
	 * This method reads the reorder info from the reorderinfo table
	 */
	public String getReorderInfoBeforeInsert(String usrUserId,String tblname,String formname,String stateEbwtable){

		EBWLogger.trace(this,"Entered insie getReorderInfoBeforeInsert");
		String reorderCol = null;
		try{
			IEBWService service = EBWServiceFactory.create("getReorderInfo");
			HashMap param = new HashMap();


			param.put("USERID",usrUserId);
			param.put("TABLENAME",tblname);
			param.put("SCREENNAME",formname);
			param.put("STATE",stateEbwtable);
			//param.put("applicationname",applnName);
			//System.out.println("INTO getReorderInfo:PARAM#"+param);

			Class cls [] ={Object.class, Boolean.class};
			Object objParams[] = {param,new Boolean(false)};	    	
			ArrayList objOutput = (ArrayList) service.execute(cls,objParams);
			//System.out.println("ReorderCols fetched from the DB :"+objOutput);

			if(objOutput !=null && objOutput.size()>=2){
				ArrayList dataLi = new ArrayList();
				dataLi = (ArrayList)objOutput.get(1);
				//System.out.println("reorderCols data :"+dataLi);
				//System.out.println("datLi[0]:"+dataLi.get(0));
				reorderCol =(String)dataLi.get(0);
				//System.out.println("reorderCol got from DB:"+reorderCol);

				objOutput = null;
				//System.out.println("datLi[1]:"+dataLi.get(1));
				//System.out.println("datLi[2]:"+dataLi.get(2));


				/*if(dataLi !=null && dataLi.size()>=3){
	    			reorderCol =(String)dataLi.get(2);
	    			System.out.println("reorderCol got from DB:"+reorderCol);
	    		}*/
			}

		}catch(Exception e){
			System.out.println("Error when getting reorder data from DB:"+e);
		}
		EBWLogger.trace(this,"Exited from getReorderInfoBeforeInsert with reordecols :"+reorderCol);
		return reorderCol;
	}

	/*
	 * This method is used to set the reorder cols in the session
	 */
	public void setSessionReorder(String tblname,HttpSession sessionObj,String stateEbwtable,String reorderCols){

		String key1 = stateEbwtable+"_"+tblname;
		if(reorderCols.indexOf(":")<0)
			reorderCols = tblname+":"+reorderCols;
		HashMap userObject = new HashMap();
		if(sessionObj.getAttribute("USER_OBJ")!=null){
			userObject = (HashMap)sessionObj.getAttribute("USER_OBJ");
			EBWLogger.trace(this,"Entered inside setSessionReorder with userObject :"+userObject);
			userObject.put(key1,reorderCols);
			sessionObj.setAttribute("USER_OBJ", userObject);
		}
		EBWLogger.trace(this,"Exited from setSessionReorder with userObject :"+userObject);
	}

	//To remove the last index of a string in a given string  
	public String removeLstStr(String toBemodified,String toBeRemoved){

		if(toBemodified!=null && toBemodified.length()>0 && toBemodified.indexOf(toBeRemoved)>-1)
			toBemodified = toBemodified.substring(0,toBemodified.lastIndexOf(toBeRemoved));
		return toBemodified;
	}

	/*
	 * Getting reordercolumns from session if null what ever there in the colattr thet will become
	 * reordercols and then store it in session
	 */	
	public String getReorderingColsFromSession(DataInterface formObj,HashMap userObject,LinkedHashMap dataValue,HttpSession sessionObj,ResourceBundle formRB,String formname,String name,String displayType,String usrUserId,String stateEbwtable,String reordering,ResourceBundle appRB,boolean multiLang){
		//System.out.println("inside EbwTblReorderHelper");
		//System.out.println("userObject :"+userObject);
		//System.out.println("dataValue :"+dataValue);
		//System.out.println("sessionObj :"+sessionObj);
		//System.out.println("formRB :"+formRB);
		//System.out.println("formname :"+formname);
		//System.out.println("name :"+name);
		//System.out.println("displayType :"+displayType);

		String reOrderColsFrmSession = null;
		String  reorderDataInTablePresent = null;
		if(reordering!=null && reordering=="yes" && appRB.getString("ReorderFrmDB").equalsIgnoreCase("Yes")) {
			reorderDataInTablePresent = getReorderInfoBeforeInsert(usrUserId,name,formname,stateEbwtable);
		}
		ArrayList colattrobjForShuffle = ((DataInterface) formObj).getColAttrObjs();
		String key1 = sessionObj.getId()+"_"+name;
		reOrderColsFrmSession = (String)userObject.get(key1);//1. From Session		
		if(reOrderColsFrmSession==null && (reorderDataInTablePresent !=null && reorderDataInTablePresent.length()>0)){
			reOrderColsFrmSession = reorderDataInTablePresent; //2. from DB 						
		}
		if(reOrderColsFrmSession == null ||( reOrderColsFrmSession !=null && reOrderColsFrmSession.length()<=0)){
			String  colsFromColAttr = getColAtrHeader(colattrobjForShuffle,formRB,formname,name,displayType);
			reOrderColsFrmSession = colsFromColAttr;//3. From Bean
		}
		if(multiLang){
			reOrderColsFrmSession = changeLanguage(reOrderColsFrmSession,formObj,formRB,appRB,formname,name,dataValue,sessionObj);//for i18n
		}
		/**
		 * Setting the reorder in session . 
		 * This sets the visible columns as reorder  cols in session. 
		 * It has been mainly used while export
		 */
		if(reOrderColsFrmSession.indexOf(":")<0)
			reorderingColmns = name+":"+reOrderColsFrmSession;
		else
			reorderingColmns = reOrderColsFrmSession;
		if(userObject!=null){
			userObject.put(key1, reorderingColmns);
			sessionObj.setAttribute("USER_OBJ", userObject);
		}

		/**
		 * altering reorderingcolumnsfrmsession variable with the column to be hide
		 *  if hidden cols are there we have to remove that from reordercols
		 */
		if(getHiddenCols()!=null && getHiddenCols().length()>0){
			String hiddenColumns = getHiddenCols();
			String[] colIndex = hiddenColumns.split(",");
			reOrderColsFrmSession = alterReordercolBasedOnHidnAttr(formRB,formname,name,reOrderColsFrmSession,colIndex,dataValue);
		}/**  Code related to hidingColumns ends here **/	
		if(reordering!=null && reordering=="yes" && appRB.getString("ReorderFrmDB").equalsIgnoreCase("Yes")) {
			callInsertIntoReorderDB(reorderDataInTablePresent,reOrderColsFrmSession,usrUserId,name,formname,stateEbwtable);//tpdtpd
		}
		EBWLogger.logDebug(this,"reOrderColsFrmSession in reoderhelper new class:"+reOrderColsFrmSession);

		return reOrderColsFrmSession;
	}

	public String changeLanguage(String reorderColsFrmSes,DataInterface formObj,ResourceBundle formRB,ResourceBundle appRB,String formname,String name,LinkedHashMap dataValue,HttpSession sessionObj) {

		if(reorderColsFrmSes.indexOf(":")>-1)
			reorderColsFrmSes = reorderColsFrmSes.substring(reorderColsFrmSes.indexOf(":")+":".length(), reorderColsFrmSes.length());		
		//System.out.println("After** reorderColsFrmSes="+reorderColsFrmSes);

		boolean flag;

		Locale prevLocale=(Locale)sessionObj.getAttribute("prevLocale");
		if (prevLocale==null)
			flag=false;
		else
			flag=true;

		String[] dynColValue = reorderColsFrmSes.split(",");		
		String[] finKeys=new String[dynColValue.length];//(String[])sessionObj.getAttribute("keys");

		/*if(finKeys!=null)
		System.out.println("finKeys::"+finKeys);

		if(finKeys==null) {
			finKeys=new String[dynColValue.length];
		}else {
			for(int i=0;i<finKeys.length;i++) {
			dynColValue[i]=formRB.getString(finKeys[i]);
			}
			flag=true;
		}*/
		//System.out.println("after finKeys::"+finKeys);

		//System.out.println("1");//Store all Header Set values into Keys[] 
		Set headerSet = dataValue.keySet();
		//System.out.println("headerSet.toArray()="+headerSet.toString());
		Object[] headerObj = headerSet.toArray();
		String[] keys=new String[headerObj.length];
		String headerStr[] = new String[headerObj.length];
		for(int i=0;i<headerObj.length;i++){			
			headerStr[i] = headerObj[i].toString();			
			keys[i]=formname.substring(0, formname.lastIndexOf("Form")) + "." + name + "." + headerStr[i];
		}

		//sessionObj.setAttribute("keys",keys);

		//System.out.println("2=");
		//Locale prevLocale=(Locale)sessionObj.getAttribute("prevLocale");//null en fr
		//if (prevLocale==null) {
		//	sessionObj.setAttribute("prevLocale",formRB.getLocale());
		//Locale prevLocale=formRB.getLocale();//fr
		//System.out.println("formRB.getLocale()++"+formRB.getLocale());
		//}
		//Locale prevLocale=Locale.getDefault();
		//System.out.println("3"+prevLocale+"Locale:"+formRB.getLocale());		

		//prevRB=ResourceBundle.getBundle(appRB.getString("message-resources"),prevLocale);//fr en fr
		//System.out.println("prevRB**");	

		/*for(int i=0;i<dynColValue.length;i++) {
			System.out.println("dynColValue=="+i+":"+dynColValue[i]);			
		}

		System.out.println("flag:"+flag);
		System.out.println("dynColValue[3]=="+dynColValue[3]);
		System.out.println("keys[3]=="+formRB.getString(keys[3]));*/

		for(int j=0;j<dynColValue.length;j++) {//Compare dynColValue[] into KEYs[]value and store finKeys[] with keys, finKeys have the same order of dynColValue[]
			for(int i=0;i<keys.length;i++) {

				if(!flag) {
					String desc = formRB.getString(keys[i]);

					if(dynColValue[j].equals(desc)){
						finKeys[j]=keys[i];
						//System.out.println("finKeys==1:"+j+":"+finKeys[j]);								
					}
					//sessionObj.setAttribute("prevLocale",formRB.getLocale());								
				}else {
					//Locale prevLocale=(Locale)sessionObj.getAttribute("prevLocale");
					ResourceBundle prevRB=ResourceBundle.getBundle(appRB.getString("message-resources"),prevLocale);

					String desc = prevRB.getString(keys[i]);
					if(dynColValue[j].equals(desc)){
						finKeys[j]=keys[i];
						//System.out.println("finKeys==2:"+j+":"+finKeys[j]);								
					}
					//sessionObj.setAttribute("prevLocale",formRB.getLocale());
				}
				sessionObj.setAttribute("prevLocale",formRB.getLocale());
			}
		}

		//store finKeys[] into session
		//Locale prevLocale=(Locale)sessionObj.getAttribute("prevLocale");//null en fr
		//if (prevLocale==null) {
		//	sessionObj.setAttribute("finKeys",finKeys);
		//Locale prevLocale=formRB.getLocale();//fr
		//	System.out.println("formRB.getLocale()++"+formRB.getLocale());
		//}

		//System.out.println("4"+finKeys.length+"::"+formRB.getString(finKeys[0]));

		String reorderCols="";
		for(int i=0;i<finKeys.length;i++) {
			reorderCols=reorderCols+formRB.getString(finKeys[i])+",";
		}		
		//System.out.println("5"+finKeys);
		reorderCols=reorderCols.substring(0,reorderCols.lastIndexOf(','));
		//sessionObj.setAttribute("prevLocale",formRB.getLocale());
		//System.out.println("After reorderColsFrmSes::"+reorderCols);
		return reorderCols;
	}

	public void setReorderInFormbean(String reOrderColsFrmSession,LinkedHashMap dataValue,DataInterface formObj,ArrayList colattrobj,ResourceBundle formRB,String formname,String name,String displayFlag){
		ArrayList colattrobjForShuffle = ((DataInterface) formObj).getColAttrObjs();
//		shuffelling header
		LinkedHashMap headerShufffleMap = shuffleHeader(reOrderColsFrmSession,dataValue,formRB,formname,name);

		ArrayList headerShuffleList = new ArrayList();
		headerShuffleList.add(headerShufffleMap);
//		header shuffle over . Now Data shuffle.
		ArrayList dataListArr = (ArrayList)formObj.getData();
		ArrayList reorderdData = new ArrayList();
		Set setHeader = headerShufffleMap.keySet();
		Iterator it = setHeader.iterator();
		ArrayList firstRow = new ArrayList();
		if(it.hasNext())
			firstRow.add(it.next());
		reorderdData.add(firstRow);
		for(int i=0;i<dataListArr.size();i++){
			reorderdData.add(shuffleData(reOrderColsFrmSession,dataValue,(ArrayList)dataListArr.get(i),formRB,formname,name));
		}//data shuffling is over

//		Shuffling colattrObj arraylist
		ArrayList colAttrObjShuffledArrList = colAttrObjShuffle(colattrobjForShuffle,headerShufffleMap,displayFlag);

		/*
		 * Setting the shuffled data ,header and colattr  to formObj
		 */ 
		formObj.setColAttrObjs(colAttrObjShuffledArrList);
		colattrobj = colAttrObjShuffledArrList;

		formObj.setData(reorderdData);
		formObj.setRowHeader(headerShuffleList);
	}

	public void setReordercolInTblTagAttrs(String reorderCols,LinkedHashMap dataValue,ResourceBundle formRB,String formname,String name){
		if(reorderCols.indexOf(":")>-1)
			reorderCols = reorderCols.substring(reorderCols.indexOf(":")+":".length(), reorderCols.length());
		String 	headerSortCol= getHeaderSortCol();		
		String editableColumns = getEditableColumns();
		String defRowComp = getDefRowComp();
		String colLevelCSS = getColLevelCSS();

		int[] reorderColPos =  getReorderColPos(reorderCols,dataValue,formRB,formname,name);

		if(headerSortCol!=null && headerSortCol.length()>0)
			setHeaderSortCol(getShuffledHeaderSort(reorderColPos,headerSortCol));
		setHeaderSortCol("2");

		if(isDefRow() && editableColumns!=null && editableColumns.length()>0)
			setEditableColumns(getShuffledEditableCols(reorderColPos,editableColumns));

		if(isDefRow() && getDefRowComp()!=null && getDefRowComp().length()>0)
			setDefRowComp(getShuffledDefRowComp(reorderColPos,defRowComp));

		if(getColLevelCSS() !=null && getColLevelCSS().length()>0)
			setColLevelCSS(getShuffledColLevelCSS(reorderColPos,colLevelCSS));
	}

	//-----------------------------------------------------------------------------//


	/*
	 * For getting the column names in visible columns and available columns
	 */

	public static String getColNamesFromColAttr(ArrayList colattrobj,String hiddenCols,LinkedHashMap dataValue){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Entered into getColNamesFromColAttr()");
		String[] hiddenheader = null;

		Set headerSet = dataValue.keySet();
		Object[] headerObj = headerSet.toArray();
		String headerStr[] = new String[headerObj.length];
		for(int i=0;i<headerObj.length;i++)
			headerStr[i] = headerObj[i].toString();

		if(hiddenCols != null && hiddenCols.length()>0){
			String[] hiddenStrArr = hiddenCols.split(",");
			hiddenheader = new String[hiddenStrArr.length];

			if(hiddenStrArr != null && hiddenStrArr.length>0){
				for(int z=0;z<hiddenStrArr.length;z++){
					hiddenheader[z] = headerStr[Integer.parseInt(hiddenStrArr[z])];


				}
			}



		}
		String colsIncolAttrStr = "";


		//System.out.println("hidden header len:"+hiddenheader.length);
		for(int i=0;i<colattrobj.size();i++){
			TableColAttrObj tableColAttrObj = new TableColAttrObj();
			tableColAttrObj = (TableColAttrObj)colattrobj.get(i);
			String colName = tableColAttrObj.getColName();
			boolean flagPresent = false; 
			if(hiddenheader !=null && hiddenheader.length>0){
				for(int j=0;j<hiddenheader.length;j++){
					if(colName.equalsIgnoreCase(hiddenheader[j])){
						//System.out.println("Equal -:"+colName   +" and "+ hiddenheader[j]);

						flagPresent= true;
						break;
					}
				}
			}
			if(!flagPresent)
				colsIncolAttrStr=colsIncolAttrStr+colName+",";
		}
		if(colsIncolAttrStr!=null && colsIncolAttrStr.length()>0)
			colsIncolAttrStr = colsIncolAttrStr.substring(0,colsIncolAttrStr.lastIndexOf(","));
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Exiting from getColNamesFromColAttr():- output="+colsIncolAttrStr);
		return colsIncolAttrStr;
	}

	public static String getColNamesFromColAttrAfter(ArrayList colattrobj){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Entered into getColNamesFromColAttr()");

		String colsIncolAttrStr = "";
		for(int i=0;i<colattrobj.size();i++){
			TableColAttrObj tableColAttrObj = new TableColAttrObj();
			tableColAttrObj = (TableColAttrObj)colattrobj.get(i);
			String colName = tableColAttrObj.getColName();
			colsIncolAttrStr=colsIncolAttrStr+colName+",";
		}
		if(colsIncolAttrStr!=null && colsIncolAttrStr.length()>0)
			colsIncolAttrStr = colsIncolAttrStr.substring(0,colsIncolAttrStr.lastIndexOf(","));
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Exiting from getColNamesFromColAttr():- output="+colsIncolAttrStr);
		return colsIncolAttrStr;
	}

	//Getting the display naemsof the visible columns from the DB name

	public static String getVisibleCols(String colNamesAfterReorderInColAttr,ResourceBundle formRB,String formname,String tblName,String displayFlag){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Entered into getVisibleCols()");
		String visibleCols = "";
		String[] dbNameArr = colNamesAfterReorderInColAttr.split(",");
		for(int i=0;i<dbNameArr.length;i++){
			String headerName = dbNameArr[i];
			String headerNameRB = getBundleString(headerName,headerName,formRB,formname,tblName);
			visibleCols=visibleCols+headerNameRB+",";
		}
		if(visibleCols!=null && visibleCols.length()>0){
			if(displayFlag !=null && displayFlag.length()>0){
				if(displayFlag.equalsIgnoreCase("yes")){
					visibleCols = visibleCols.substring(visibleCols.indexOf(",")+",".length(), visibleCols.length());
				}
			}
			visibleCols = visibleCols.substring(0,visibleCols.lastIndexOf(","));
		}
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Exiting from getVisibleCols():- output="+visibleCols);
		return visibleCols;
	}

	public static String getAvailableCols(String colNamesFromAttrbeforeReorde,String colNamesAfterReorderInColAttr,ResourceBundle formRB,String formname,String tblName,String displayFlag){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Entered into getAvailableCols()");
		String availableCols = "";
		String befreoReorder[] = colNamesFromAttrbeforeReorde.split(",");
		String afterReorder[] = colNamesAfterReorderInColAttr.split(",");
		int reorderlen = 0;
		for(int i=0;i<befreoReorder.length;i++){
			int count = 0 ;

			for(int j=0;j<afterReorder.length;j++){
				if(befreoReorder[i].equalsIgnoreCase(afterReorder[j])){
					count = 1;
				}
			}
			if(count == 0){
				String availableCol1 = getBundleString(befreoReorder[i],befreoReorder[i],formRB,formname,tblName);
				availableCols = availableCols+availableCol1+",";
			}
		}
		if(availableCols!=null && availableCols.length()>0)
			availableCols = availableCols.substring(0,availableCols.lastIndexOf(","));

		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Exiting from availableCols():- output="+availableCols);
		return availableCols;
	}

//	shuffling data
	public static ArrayList shuffleData(String reorderCols,LinkedHashMap headerMap,ArrayList dataList,ResourceBundle formRB,String formname,String tblName){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Entered into shuffleData()");
		Set headerSet = headerMap.keySet();
		ArrayList header=new ArrayList();
		header.addAll(headerSet);
		ArrayList resultArr = new ArrayList();
		Object objHeader = null;
		Object objData = null;

		String[] reorderColArr = null;
		reorderColArr = reorderCols.split(",");
		if(header !=null && header.size()>0){
			for(int reOrCol=0;reOrCol<reorderColArr.length;reOrCol++){
				Iterator headerItr = header.iterator();
				Iterator dataItr = dataList.iterator();
				while (headerItr.hasNext() && dataItr.hasNext()){
					objHeader = headerItr.next();
					objData = dataItr.next();
					String dataName = null;
					if((String)objData!=null)
						dataName = objData.toString();
					else{
						dataName="";
					}
					String headerName = objHeader.toString();
					String headerNameRB = getBundleString(headerName,headerName,formRB,formname,tblName);
					if(reorderColArr[reOrCol].equalsIgnoreCase(headerNameRB))
						resultArr.add(dataName);
				}
			}
		}
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Exiting from shuffleData():- output="+resultArr);
		return resultArr;
	}

	//For Shuffling header////
	public static LinkedHashMap shuffleHeader(String reorderCols,LinkedHashMap headerMap,ResourceBundle formRB,String formname,String tblName){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Entered into shuffleHeader()");
		//System.out.println("EbwTableHelper : Entered into shuffleHeader()");
		//System.out.println("reorderCols :"+reorderCols);
		//System.out.println("headerMap :"+headerMap);

		Set headerSet = headerMap.keySet();
		ArrayList header=new ArrayList();
		header.addAll(headerSet);

		ArrayList resultArr = new ArrayList();

		Object obj = null;
		String[] reorderColArr = null;
		reorderColArr = reorderCols.split(",");
		if(header !=null && header.size()>0){
			for(int reOrCol=0;reOrCol<reorderColArr.length;reOrCol++){		 		
				for(int i=0;i<header.size();i++){
					obj = header.get(i);
					String objStrHead = (String)obj;
					String headerName = getBundleString(objStrHead,objStrHead,formRB,formname,tblName);

					if((reorderColArr[reOrCol]).trim().equalsIgnoreCase(headerName.trim()) ){
						resultArr.add(objStrHead);
						break;
					}
				}
			}
		}
		Iterator itr = resultArr.iterator();
		LinkedHashMap resultMap = new LinkedHashMap();

		while (itr.hasNext()){
			Object objectHeader=itr.next();
			resultMap.put(objectHeader,objectHeader);
		}
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Exiting from shuffleHeader():- output="+resultMap);
		//System.out.println("EbwTableHelper : Entered into shuffleHeader() O/P :"+resultMap);
		return resultMap;
	}

//	For getting reorder colPos////
	public static int[] getReorderColPos(String reorderCols,LinkedHashMap headerMap,ResourceBundle formRB,String formname,String tblName){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Entered into getReorderColPos()");
		Set headerSet = headerMap.keySet();
		Object[] headerObj = headerSet.toArray();
		String headerStr[] = new String[headerObj.length];
		for(int i=0;i<headerObj.length;i++)
			headerStr[i] = headerObj[i].toString();
		String[] reorderColArr = null;
		reorderColArr = reorderCols.split(",");
		int pos[] = new int[reorderColArr.length];
		if(headerStr !=null && headerStr.length>0){
			int k = 0;
			for(int reOrCol=0;reOrCol<reorderColArr.length;reOrCol++){
				for(int i=0;i<headerStr.length;i++){
					String headerName = getBundleString(headerStr[i],headerStr[i],formRB,formname,tblName);
					if(reorderColArr[reOrCol].equalsIgnoreCase(headerName)){
						pos[k] = i;
						k++;
					}

				}

			}	
		}

		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Exiting from getReorderColPos():- output="+pos);
		return pos;
	}

	public static String alterReordercolBasedOnHidnAttr(ResourceBundle formRB,String formname,String tblName,String reOrderColsFrmSession,String[] colIndex,LinkedHashMap headerMap){
		String reorderedColStr ="";
		ArrayList reoderArrLst = new ArrayList();
		String[] reorderColArr = reOrderColsFrmSession.split(",");
		for(int k=0;k<reorderColArr.length;k++){
			reoderArrLst.add(reorderColArr[k]);
		}
		Set headerSet = headerMap.keySet();
		Object[] headerObj = headerSet.toArray();
		String headerStr[] = new String[headerObj.length];
		for(int i=0;i<headerObj.length;i++)
			headerStr[i] = headerObj[i].toString();
		for(int i=0;i<colIndex.length;i++){
			String hiddenHeader = headerStr[Integer.parseInt(colIndex[i])];
			String headerName = getBundleString(hiddenHeader,hiddenHeader,formRB,formname,tblName);
			for(int j=0;j<reoderArrLst.size();j++){
				if(headerName.equalsIgnoreCase((String)reoderArrLst.get(j))){
					reoderArrLst.remove(j);
				}
			}
		}

		for(int l =0;l<reoderArrLst.size();l++){
			reorderedColStr = reorderedColStr+(String)reoderArrLst.get(l)+",";
		}

		reorderedColStr = reorderedColStr.substring(0,reorderedColStr.lastIndexOf(","));
		return reorderedColStr;
	}

	public static String getColAtrHeader(ArrayList colattrobjForShuffle,ResourceBundle formRB,String formname,String tblName,String getdisplaytype){
		/*if(EBWLogger.isInfoEnabled())
			EBWLogger.trace("EbwTableHelper :","Entered into getColAtrHeader()");*/
		String colAttrHeaderName ="";

		TableColAttrObj tableColAttrObj = new TableColAttrObj();
		for(int i=0;i<colattrobjForShuffle.size();i++){
			String colName = null;
			tableColAttrObj = (TableColAttrObj)colattrobjForShuffle.get(i);
			colName = tableColAttrObj.getColName();
			if(getdisplaytype!=null && getdisplaytype.length()>0 && i==0){

			}else
				colAttrHeaderName=colAttrHeaderName+getBundleString(colName, colName, formRB, formname, tblName)+",";

		}
		if(colAttrHeaderName !=null && colAttrHeaderName.length()>0)
			colAttrHeaderName = colAttrHeaderName.substring(0,colAttrHeaderName.lastIndexOf(","));

		/*	if(EBWLogger.isInfoEnabled())
			EBWLogger.trace("EbwTableHelper :","Exiting from getColAtrHeader():- output="+colAttrHeaderName);*/
		return colAttrHeaderName;

	}

	public static ArrayList colAttrObjShuffle(ArrayList colattrobjForShuffle,LinkedHashMap headerShufffleMap,String displayFlag){
		/*if(EBWLogger.isInfoEnabled())
			EBWLogger.trace("EbwTableHelper :","Entered into colAttrObjShuffle()");*/
		ArrayList colShuffleResult = new ArrayList();
		TableColAttrObj tableColAttrObjForShuffle = new TableColAttrObj();
		if(headerShufffleMap!=null && colattrobjForShuffle!=null ){
			ArrayList headerArr = new ArrayList();
			Set headerSet =  headerShufffleMap.keySet();
			Iterator setItr = headerSet.iterator();
			while(setItr.hasNext()){
				headerArr.add(setItr.next());
			}

			int headerTemp=0;
			if(displayFlag !=null && displayFlag.length()>0){
				if(displayFlag.equalsIgnoreCase("yes")){
					tableColAttrObjForShuffle = (TableColAttrObj)colattrobjForShuffle.get(0);
					colShuffleResult.add(tableColAttrObjForShuffle);
					tableColAttrObjForShuffle = new TableColAttrObj();

				}
			}

			for( int header=headerTemp; header<headerArr.size();header++ ){
				for(int colAttr=0;colAttr<colattrobjForShuffle.size();colAttr++){
					tableColAttrObjForShuffle = (TableColAttrObj)colattrobjForShuffle.get(colAttr);
					if(tableColAttrObjForShuffle.getColName().equalsIgnoreCase(headerArr.get(header).toString())){
						colShuffleResult.add(tableColAttrObjForShuffle);
						tableColAttrObjForShuffle = new TableColAttrObj();
					}

				}
			}
		}
		/*	if(EBWLogger.isInfoEnabled())
			EBWLogger.trace("EbwTableHelper :","Exiting from colAttrObjShuffle():- output="+colShuffleResult);*/
		return colShuffleResult;

	}

//	For reordering  /////
	private static String getBundleString(String key, String value,ResourceBundle formRB,String formname,String tblName) {
		String strMsg = value;

		//System.out.println("In getBundleString in helper key :"+key+"    value :"+value+"    formname :"+formname+"     tblName:"+tblName);



		//System.out.println("key is  :"+formname.substring(0, formname.lastIndexOf("Form")) + "." + tblName + "." + key);


		try {
			if (formRB!=null) {
				if (key != null) {
					strMsg = formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + tblName + "." + key);
				} else {
					strMsg = formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + tblName);
				}
			}
		}catch (Exception exc) {

			if((key.indexOf("NoDataFoundMsg") == -1))
				strMsg="";

			//System.out.println ("Resource bundle value not found for " + key);
			//exc.printStackTrace();
		}


		return strMsg;
	}


	public static String getColumnHeaderAsString(LinkedHashMap dataValue,ResourceBundle formRB,String formname,String tblName){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace("EbwTableHelper :","Entered into getColumnHeaderAsString()---dataValue :"+dataValue+"   formname :"+formname+"   tblName :"+tblName);

		String result="";
		int size = dataValue.size();
		if(dataValue!=null){
			Set keySet = dataValue.keySet();
			Iterator itr = keySet.iterator();

			while(itr.hasNext()){

				String str= (String)itr.next(); 
				try{
					//System.out.println("resource bundle for  :"+str);
					String temp = getBundleString(str,str,formRB,formname,tblName);
					//System.out.println("resource bundle header name :"+temp);
					if(temp!=null && temp.length()>0)
						result = result+temp+",";
				}catch(Exception e){EBWLogger.logDebug("EbwTableHelper",e.toString());}
			}
		}
		//System.out.println("Result :"+result);

		result=result.substring(0,result.length()-1);
		/*if(EBWLogger.isInfoEnabled())
			EBWLogger.trace("EbwTableHelper :","Exiting from getColumnHeaderAsString():- output="+result);*/
		return result;

	}
	//Not used anywhere but need to check throughly
	public static int[] getActualColPos(LinkedHashMap dataValue){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace("EbwTableHelper :","Entered into getActualColPos()");
		int size = dataValue.size();
		int count = 0;
		int result[] = new int[size];
		if(dataValue!=null){
			Set keySet = dataValue.keySet();
			Iterator itr = keySet.iterator();

			while(itr.hasNext()){
				itr.next();
				result[count]=count;
				count++;
			}
		}
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace("EbwTableHelper :","Exiting from getActualColPos():- output="+result);
		return result;
	}

	public static String getShuffledHeaderSort(int[] reorderColArr,String headerSortCol){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Entered into getShuffledHeaderSort()");
		String result = "";
		String headertSortArr[] =null;
		if(headerSortCol!=null && headerSortCol.length()>0){
			headertSortArr = headerSortCol.split(",");
			for(int i=0;i<reorderColArr.length;i++){
				for(int j=0;j<headertSortArr.length;j++){
					if(reorderColArr[i]==Integer.parseInt(headertSortArr[j])){
						result=result+i+",";
						break;
					}
				}
			}
		}
		if(result!=null && result.length()>0)
			result = result.substring(0,result.lastIndexOf(","));
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Exiting from getShuffledHeaderSort():- output="+result);
		return result;


	}

	public static String getShuffledDefaultHeaderSort(int[] reorderColArr,String defaultSortCol){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("ReorderTableHelper :","Entered into getShuffledDefaultHeaderSort()");
		String result = "";
		String defaultSortArr[] =null;
		if(defaultSortCol!=null && defaultSortCol.length()>0){
			defaultSortArr = defaultSortCol.split(",");
			for(int i=0;i<reorderColArr.length;i++){
				for(int j=0;j<defaultSortArr.length;j++){
					String column = defaultSortArr[j].substring(0,defaultSortArr[j].indexOf("="));
					String order = defaultSortArr[j].substring(defaultSortArr[j].indexOf("="),defaultSortArr[j].length());

					if(reorderColArr[i]==Integer.parseInt(column)){
						defaultSortArr[j] = i+order;
						if(result=="")
							result=result+defaultSortArr[j];
						else
							result=result+","+defaultSortArr[j];

						break;
					}
				}
			}
		}

		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","In getShuffledDefaultHeaderSort:- output="+result);
		return result;


	}

	public static String getShuffledEditableCols(int[] reorderColArr,String headerSortCol){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Entered into getShuffledEditableCols()");
		String result = "";
		String headertSortArr[] =null;
		if(headerSortCol!=null && headerSortCol.length()>0){
			headertSortArr = headerSortCol.split(",");
			for(int i=0;i<reorderColArr.length;i++){
				for(int j=0;j<headertSortArr.length;j++){
					if((reorderColArr[i]+1)==Integer.parseInt(headertSortArr[j])){
						int z=i+1;
						result=result+z+",";
						break;
					}
				}
			}
		}
		if(result!=null && result.length()>0)
			result = result.substring(0,result.lastIndexOf(","));
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Exiting from getShuffledEditableCols():- output="+result);
		return result;

	}

	public static String getShuffledDefRowComp(int[] reorderColArr,String defRowComp){
		String result = "";
		if(defRowComp !=null && defRowComp.length()>0){
			String[] defcols = defRowComp.split("~");
			for(int i=0;i<reorderColArr.length;i++){
				for(int z=0;z<defcols.length;z++){
					String eachCol = defcols[z];
					String[] eachColDef = eachCol.split(":");
					if(eachColDef !=null && eachColDef.length>0){
						if((reorderColArr[i]+1)==Integer.parseInt(eachColDef[0])){

							result= result+defcols[z].replaceFirst(eachColDef[0], Integer.toString(i+1));


						}
					}

				}
			}
		}
		if(result!=null && result.indexOf("~")>-1)
			result = result.substring(0,result.lastIndexOf("~"));


		return result;
	}
	//This is Old Method.
	/*public static String getShuffledColLevelCSS(int[] reorderColArr,String colLevelCSS){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Entered into getShuffledColLevelCSS()");
		String result = "";
		String col = colLevelCSS.substring(0,colLevelCSS.indexOf("="));
		String strToBeAppended = colLevelCSS.substring(colLevelCSS.indexOf("="),colLevelCSS.length());
		for(int i=0;i<reorderColArr.length;i++){
			if(reorderColArr[i] == Integer.parseInt(col) ){
				result=""+i;
				break;
			}
		}
		result += strToBeAppended;
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("EbwTableHelper :","Exiting from getShuffledColLevelCSS():- output="+result);
		return result;
	}*/
	public static String getShuffledColLevelCSS(int[] reorderColArr,String colLevelCSS){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("ReorderTableHelper :","Entered into getShuffledColLevelCSS()");
		String result = "";		

		String colLevelCSSArr[] =null;
		if(colLevelCSS!=null && colLevelCSS.length()>0){
			colLevelCSSArr = colLevelCSS.split(",");
			for(int i=0;i<reorderColArr.length;i++){
				for(int j=0;j<colLevelCSSArr.length;j++){
					String column = colLevelCSSArr[j].substring(0,colLevelCSSArr[j].indexOf("="));
					String order = colLevelCSSArr[j].substring(colLevelCSSArr[j].indexOf("="),colLevelCSSArr[j].length());

					if(reorderColArr[i]==Integer.parseInt(column)){
						colLevelCSSArr[j] = i+order;
						if(result=="")
							result=result+colLevelCSSArr[j];
						else
							result=result+","+colLevelCSSArr[j];						
						break;
					}
				}
			}
		}
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug("ReorderTableHelper :","Exiting from getShuffledColLevelCSS():- output="+result);
		return result;
	}	 public String getReorderingColsFromSession(DataInterface formObj,		        HashMap userObject, LinkedHashMap rowHead, HttpSession sessionObj,		        ResourceBundle formRB, String formname, String name,		        String displayType, String usrUserId, String stateEbwtable,		        String reordering, ResourceBundle appRB) { // TODO Auto-generated method stub		        return null;		    }
}


