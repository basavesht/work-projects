/*
 * Created on Dec 9, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.businessdelegate;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

//import org.apache.log4j.lf5.util.Resource;

import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.serverside.query.EBWStatement;
//import com.tcs.ebw.taglib.DataInterface;

/**
 * @author 193350
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PaginationDelegateHelper {
	String sessionId = null;
	String sessionID = null;
	String keyInUsrObj = null;
	LinkedHashMap pgnMap = new LinkedHashMap();
	String paginationIndex = null;
	String paginationDirection = null;
	ArrayList valArrList = new ArrayList();
	String restartKey = null;
	String[] restartColKeyArr = null;
	private final int  ROWS_TO_MINUS_TOGET_LASTROW =2;
	private final int  ROWS_TO_MINUS_TOGET_LASTROW_WHEN_ONEROW_REMAINS=1;
	private final String dbDateFormat="dd-Mon-yyyy hh24:mi:ss";
	private final String dbOutDateTimeFormat="dd/MM/yyyy hh24:mi:ss";
	private final String KEY_VAL_SEPARATOR ="~";
	String restarKeyPos = null;
	String pgnKeyPos = null;
	HashMap stmtMap = new HashMap();
	String isExtrnlStmt = null;
	
	//ResourceBundle rb1= ResourceBundle.getBundle("ebw");
	
	//String isExtrnlStmt = rb1.getString("isExternalisedStatements");
	public PaginationDelegateHelper(){
		try{
			isExtrnlStmt = PropertyFileReader.getProperty("isExternalisedStatements");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	}
	
	/**
	 * 
	 *  @param userSessionObj
	 * @param formObj
	 * @param actionType
	 * @param serviceId
	 * @param statementId
	 * @param hiddenVarRestartKey
	 * @return HashMap
	 * 
	 */
	public HashMap preExecute(HashMap userSessionObj,EbwForm formObj,String actionType,String serviceId,String statementId,String hiddenVarRestartKey){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into PreExecute():- statementId="+statementId+"  actionType="+actionType);
		HashMap toQueryExecutor = new HashMap();
		HashMap toFormQueryMap = new HashMap();
		int keyCount = 0;
		
		stmtMap = EBWStatement.getMap();
		/*
		 * Some common class level variables to be used in all methods are defined here
		 */
		if(userSessionObj.containsKey("sessionid"))
			sessionID =(String)userSessionObj.get("sessionid");
		 keyInUsrObj=sessionID+"_"+serviceId+"_"+statementId;
		 
		if(actionType !=null && actionType.length()>0 && actionType.equalsIgnoreCase("list")){
			restartKey = getRestartKeyCol(hiddenVarRestartKey,statementId);
		}else{
			if(userSessionObj.containsKey(keyInUsrObj)){
				LinkedHashMap pgnUserMap = (LinkedHashMap)(userSessionObj.get(keyInUsrObj));
				if(pgnUserMap.containsKey("RestartKey"))
					restartKey = (String)pgnUserMap.get("RestartKey");
		}
		}
		
		
		if(restartKey !=null && restartKey.length()>0 ){
			restartColKeyArr = restartKey.split("#");
			keyCount = restartColKeyArr.length;
		}
		Object[] tempString =new Object[keyCount];
		
		if(actionType.equalsIgnoreCase("list")){
			PreCreateSessionObj(userSessionObj,serviceId,statementId,formObj);
			Object[] tempString1 = null;
			toFormQueryMap.put(restartKey,tempString1);
		}else if(actionType.equalsIgnoreCase("prev")){
			//get the one but last val from sessionkey val array
			//call getFromSession()
			tempString =getPrev(userSessionObj,serviceId,statementId,hiddenVarRestartKey,formObj);
			toFormQueryMap.put(restartKey,tempString);
		}else if(actionType.equalsIgnoreCase("retain")){
			//get the one but last val from sessionkey val array
			//call getFromSession()
			
			tempString =getRetain(userSessionObj,serviceId,statementId,hiddenVarRestartKey,formObj);
			toFormQueryMap.put(restartKey,tempString);
		}else if(actionType.equalsIgnoreCase("next")){				
				//get last val from sessionkey val array
				//call getFromSession()if(usrObj !=null){
				tempString =getLast(userSessionObj,serviceId,statementId,hiddenVarRestartKey,formObj);
				
				toFormQueryMap.put(restartKey,tempString);
			}					
		
		
		toQueryExecutor = formConditionQuery(formObj,toFormQueryMap,userSessionObj,serviceId,statementId,hiddenVarRestartKey,actionType);
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"toQueryExecutor"+toQueryExecutor);
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exited from PreExecute():- statementId="+statementId+"  actionType="+actionType+"  userSessionObj="+userSessionObj);
		return toQueryExecutor;
		
	}
	
	/**
	 * 
	 * @param userSessionObj
	 * @param formObj
	 * @param actionType
	 * @param tableArray
	 * @param serviceId
	 * @param statementId
	 * @param hiddenVarRestartKey
	 */
	public void postExecute(HashMap userSessionObj,EbwForm formObj,String actionType,ArrayList tableArray,String serviceId,String statementId,String hiddenVarRestartKey){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into postExecute():- statementId="+statementId+"  actionType="+actionType+"   hiddenVarRestartKey="+hiddenVarRestartKey );
		stmtMap = EBWStatement.getMap();
		int keyCount = 0;
		if(userSessionObj.containsKey("sessionid"))
			sessionID =(String)userSessionObj.get("sessionid");
		 keyInUsrObj=sessionID+"_"+serviceId+"_"+statementId;
		 if(actionType !=null && actionType.length()>0 && actionType.equalsIgnoreCase("list")){
				restartKey = getRestartKeyCol(hiddenVarRestartKey,statementId);
				
		}else{
				if(userSessionObj.containsKey(keyInUsrObj)){
					LinkedHashMap pgnUserMap = (LinkedHashMap)(userSessionObj.get(keyInUsrObj));
					if(pgnUserMap.containsKey("RestartKey"))
						restartKey = (String)pgnUserMap.get("RestartKey");
				}
		}
		
		 EBWLogger.logDebug(this,"restart key postexceute:" +restartKey);
		if(restartKey !=null && restartKey.length()>0 ){
			restartColKeyArr = restartKey.split("#");
			keyCount = restartColKeyArr.length;
		}
		
		
		
		
		
		if(actionType.equalsIgnoreCase("list")){
			
			//add the first and last row val to the session
			 postCreateSessionObj(userSessionObj,formObj,serviceId,statementId,tableArray,hiddenVarRestartKey);			
		}else if(actionType.equalsIgnoreCase("prev")){
			// put the current last row val in the session 	
			addToSession(userSessionObj,formObj,serviceId,statementId,tableArray,hiddenVarRestartKey);
		}else if(actionType.equalsIgnoreCase("retain")){
			// put the current last row val in the session 	
			addToSession(userSessionObj,formObj,serviceId,statementId,tableArray,hiddenVarRestartKey);
		}else{
			if(actionType.equalsIgnoreCase("next")){
		    // add teh last row data to session
			addToSession(userSessionObj,formObj,serviceId,statementId,tableArray,hiddenVarRestartKey);
			}
		}	
		reset(formObj,actionType);
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exited from postExecute():- statementId="+statementId+"  actionType="+actionType+"  userSessionObj="+userSessionObj);
		
	}
	
	/**
	 * 
	 * @param userSessionObj
	 * @param serviceId
	 * @param statementId
	 * @param hiddenVarRestartKey
	 * @param formObj
	 * @return
	 */
	public Object[] getLast(HashMap userSessionObj,String serviceId,String statementId,String hiddenVarRestartKey,EbwForm formObj){
			if(EBWLogger.isDebugEnabled())
				EBWLogger.logDebug(this,"Entered into getLast()");
			HashMap toQueryExe = new HashMap();
			LinkedHashMap pgnLinkedMap = new LinkedHashMap();
			int keyCount = 0;
			if(restartKey !=null && restartKey.length()>0 && restartKey.indexOf("#")>-1){
				String[] keyStrArr = restartKey.split("#");
				keyCount = keyStrArr.length;
			}
			Object[] columnValue =new Object[keyCount];
			if(userSessionObj.containsKey(keyInUsrObj) && userSessionObj.get(keyInUsrObj)!=null){
				pgnLinkedMap = (LinkedHashMap)userSessionObj.get(keyInUsrObj);
				if(pgnLinkedMap.containsKey(restartKey)){
					ArrayList pgnList = (ArrayList)pgnLinkedMap.get(restartKey);
					columnValue = (Object[])pgnList.get(pgnList.size()-1);
					
					formObj.setPaginationIndex(Integer.toString((Integer.parseInt(formObj.getPaginationIndex()))+1));
					pgnLinkedMap.put("PaginationIndex",formObj.getPaginationIndex());
				}
			}
			
			//For debugging
			if(columnValue!=null){
				EBWLogger.logDebug(this,"started printing the key column values");
				for(int i=0;i<columnValue.length;i++){
					EBWLogger.logDebug(this,"key colVal["+i+"] :"+columnValue[i]);
				}
				EBWLogger.logDebug(this,"printing key  colval  is over"); 
			}
			if(EBWLogger.isDebugEnabled())
				EBWLogger.logDebug(this,"Exited from getLast()"); 
			return columnValue;
		}
		
		
	/**
	 * 
	 * @param userSessionObj
	 * @param serviceId
	 * @param statementId
	 * @param hiddenVarRestartKey
	 * @param formObj
	 * @return
	 */
	public Object[] getPrev(HashMap userSessionObj,String serviceId,String statementId,String hiddenVarRestartKey,EbwForm formObj){
		
			if(EBWLogger.isDebugEnabled())
				EBWLogger.logDebug(this,"Entered into getPrev()"); 
			HashMap toQueryExe1 = new HashMap();int keyCount = 0;
			//added newly for object array
			
			if(restartKey !=null && restartKey.length()>0 && restartKey.indexOf("#")>-1){
				String[] keyStrArr = restartKey.split("#");
				keyCount = keyStrArr.length;
			}
			Object[] columnValue =new Object[keyCount];
			//end
			//String columnValue = null;
			if(userSessionObj.containsKey(keyInUsrObj) && userSessionObj.get(keyInUsrObj)!=null){
					LinkedHashMap pgnLinkedMap = (LinkedHashMap)userSessionObj.get(keyInUsrObj);
					if(pgnLinkedMap.containsKey(restartKey)){
						ArrayList pgnList = (ArrayList)pgnLinkedMap.get(restartKey);
						//remove the last value and get the last but one value
						EBWLogger.logDebug(this,"Pagination List Before Removing : "+pgnList);
						pgnList.remove(pgnList.size()-1); //Remove the previous value needed for previous page.
						pgnList.remove(pgnList.size()-1);// Remove the last value added for next.
						EBWLogger.logDebug(this,"Pagination List After Removing : "+pgnList);
						columnValue=(Object[]) pgnList.get(pgnList.size()-1);
						formObj.setPaginationIndex(Integer.toString((Integer.parseInt(formObj.getPaginationIndex()))-1));
						pgnLinkedMap.put("PaginationIndex",formObj.getPaginationIndex());
					}
				}
			
			
			/*if(columnValue!=null){
				EBWLogger.logDebug(this,"started printing the key column values");
				for(int i=0;i<columnValue.length;i++){
					EBWLogger.logDebug(this," key colVal["+i+"] :"+columnValue[i]);
				}
				EBWLogger.logDebug(this,"printing key  colval  is over"); 
			}*/
			if(EBWLogger.isDebugEnabled())
				EBWLogger.logDebug(this,"Exited from getPrev()"); 
			return columnValue;
		}
		
	/**
	 * 
	 * @param userSessionObj
	 * @param serviceId
	 * @param statementId
	 * @param hiddenVarRestartKey
	 * @param formObj
	 * @return
	 */
	public Object[] getRetain(HashMap userSessionObj,String serviceId,String statementId,String hiddenVarRestartKey,EbwForm formObj){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into getRetain()"); 
			HashMap toQueryExe1 = new HashMap();
			//String columnValue = null;
			int keyCount = 0;
			//String keyStr = getRestartKeyCol(hiddenVarRestartKey,statementId);
						
			if(restartKey !=null && restartKey.length()>0 && restartKey.indexOf("#")>-1){
				String[] keyStrArr = restartKey.split("#");
				keyCount = keyStrArr.length;
			}
			Object[] columnValue =new Object[keyCount];

				
				if(userSessionObj.containsKey(keyInUsrObj) && userSessionObj.get(keyInUsrObj)!=null){
					LinkedHashMap pgnLinkedMap = (LinkedHashMap)userSessionObj.get(keyInUsrObj);
					if(pgnLinkedMap.containsKey(restartKey)){
						
						ArrayList pgnList = (ArrayList)pgnLinkedMap.get(restartKey);
						
						if(pgnList!=null && pgnList.size()>0){
							//remove the last value and get the last but one value
							EBWLogger.logDebug(this,"Pagination List Before Removing : "+pgnList);
							if(pgnList.size() > 1)
							pgnList.remove(pgnList.size()-1); 
							
							EBWLogger.logDebug(this,"Pagination List After Removing : "+pgnList);
							columnValue = (Object[])pgnList.get(pgnList.size()-1);
							formObj.setPaginationIndex(Integer.toString((Integer.parseInt(formObj.getPaginationIndex()))));
							pgnLinkedMap.put("PaginationIndex",formObj.getPaginationIndex());
						}
					}
				}
			
				if(EBWLogger.isDebugEnabled())
					EBWLogger.logDebug(this,"Exitec from getRetain()"); 
			return columnValue;
		}
		
		
	
	
	/**
	 * 
	 * @param usrObj
	 * @param serviceId
	 * @param statementId
	 * @param formObj
	 */
	public void PreCreateSessionObj(HashMap usrObj,String serviceId,String statementId,EbwForm formObj){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into PreCreateSessionObj()"); 
		if(usrObj.containsKey(keyInUsrObj))
			deleteSessionObj(usrObj,sessionId,serviceId,statementId);
		paginationIndex="1";
		paginationDirection = "Next";
		formObj.setPaginationIndex(paginationIndex)	;
		pgnMap.put("PaginationIndex",paginationIndex);
		pgnMap.put("PagiationDirection",paginationDirection);
		
		usrObj.put(keyInUsrObj,pgnMap);
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exited from PreCreateSessionObj():- usrObj="+usrObj); 
	}
	
	/**
	 * 
	 * @param usrObj
	 * @param sessionid
	 * @param serviceId
	 * @param statementId
	 */
	public void deleteSessionObj(HashMap usrObj,String sessionid,String serviceId,String statementId){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into deleteSessionObj()");
		if(usrObj.containsKey(keyInUsrObj)){
			usrObj.remove(keyInUsrObj);
		}
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exited from deleteSessionObj()");
	}
	
    /**
     * 
     * @param userSessionObj
     * @param formObj
     * @param serviceId
     * @param statementId
     * @param tableArray
     * @param hiddenVarRestartKey
     */
	public  void postCreateSessionObj(HashMap userSessionObj,EbwForm formObj,String serviceId,String statementId,ArrayList tableArray,String hiddenVarRestartKey){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into postCreateSessionObj()");
		//for debugging
		EBWLogger.logDebug(this,"ebwltableArray  is :"+tableArray);
		int arrListSize = 0;
		ArrayList header = new ArrayList(); 
		String pos = "";
		/*for(int i=0;i<restartColKey.length;i++)
			EBWLogger.trace(this,"restartColKey :"+restartColKey[i]);*/
		ArrayList restartValList = new  ArrayList();
		int lastRowInArrList = 0; 
		String rowsInPage = formObj.getRowsPerPage();
		
		if(tableArray!=null ){
			if(Integer.parseInt(rowsInPage)<((tableArray.size())-1))
				lastRowInArrList =(tableArray.size()-2);
			else
				lastRowInArrList = tableArray.size()-1;
			
			
			/*
			 * Storing the key column position in sesion for next and previous btn click
			 */
			if(userSessionObj.containsKey(keyInUsrObj)){
				LinkedHashMap pgnUsrMap = (LinkedHashMap)userSessionObj.get(keyInUsrObj);
				if(!(isExtrnlStmt.equalsIgnoreCase("true"))){
					EBWLogger.logDebug(this,"hiddenVarRestartKey    :"+hiddenVarRestartKey);
					
					if(hiddenVarRestartKey == null || (hiddenVarRestartKey !=null && hiddenVarRestartKey.length()<=0) ||(hiddenVarRestartKey !=null && hiddenVarRestartKey.equalsIgnoreCase("null"))){
						
						pos = getRestatKeyFrmMap(statementId+".SortKeysId") + getRestatKeyFrmMap(statementId+".PgnUniqueKeysId");
					}else{
						
						ArrayList headerList=(ArrayList)tableArray.get(0);
						for(int z=0;z<headerList.size();z++){
							String headerStr = (String)headerList.get(z);
							if(headerStr.equalsIgnoreCase(hiddenVarRestartKey.substring(0,hiddenVarRestartKey.indexOf(":")))){
									pos=z+":";
									break;
							}	
						}
						pos = pos + getRestatKeyFrmMap(statementId+".PgnUniqueKeysId");
					}
				}else{
					header=(ArrayList)tableArray.get(0);
					String[] headerKeycol = new String[header.size()];
					arrListSize = tableArray.size();
					int z=0;
					for (Iterator it = header.iterator (); it.hasNext (); ) {
						headerKeycol[z] = (String)it.next ();
						
						z++;
					  }
					
					for(int j=0;j<restartColKeyArr.length;j++){
						for(int i=0;i<headerKeycol.length;i++){
							if(restartColKeyArr[j].indexOf(".")>-1){
								if(headerKeycol[i].equalsIgnoreCase(restartColKeyArr[j].substring((restartColKeyArr[j].indexOf(".")+1),restartColKeyArr[j].indexOf(":"))))
									pos=pos+i+":";
							}else{
								if(headerKeycol[i].equalsIgnoreCase(restartColKeyArr[j].substring(0,restartColKeyArr[j].indexOf(":"))))
									pos=pos+i+":";
							}
							
						}
					}
				}
					pos=pos.substring(0,(pos.lastIndexOf(":")));
					pgnUsrMap.put("SortColIndexStr", pos);
					userSessionObj.put(keyInUsrObj, pgnUsrMap);;
				}
			EBWLogger.logDebug(this,"key positions :"+pos);
			String[] posStrArr=pos.split(":");
			
			
			//iterating the tableArray to get the values
			String colValue = "";
			
			for(int start=0;start<tableArray.size();start++){
				if(start == 1 || start ==(lastRowInArrList)){ // becoz zeroth row will be header 
					
					 ArrayList tableRowList = new ArrayList();
					 tableRowList =(ArrayList)tableArray.get(start);
					
					
					 Object valObjArr[] = new Object[posStrArr.length];
					
					
					 
					
					 for(int k=0;k<posStrArr.length;k++){
				 		for(int i=0;i<tableRowList.size();i++){
						 	if(Integer.toString(i).equalsIgnoreCase(posStrArr[k]))
					 			valObjArr[k] = tableRowList.get(i);
					 	}
					 }
					 //for debugging
					 EBWLogger.logDebug(this,"in post add session values stored in sesion");
					 for(int i=0;i<valObjArr.length;i++)
						 EBWLogger.logDebug(this,"Val of "+i+"is :"+valObjArr[i]); 
					 restartValList.add(valObjArr);
					 if(formObj.getRowsPerPage()!=null && Integer.parseInt(formObj.getRowsPerPage())==1) 
					 	restartValList.add(valObjArr);
					 valObjArr=null;
					 colValue = "";
				} 
			}
			pos = "";
			
			
		 }
			//NOTE : pgnMap is to be passed to query executor 
		if(userSessionObj.containsKey(keyInUsrObj)){
			LinkedHashMap pgnUsrMap = (LinkedHashMap)userSessionObj.get(keyInUsrObj);
			if(restartColKeyArr!=null && restartKey.length()>0)
				pgnUsrMap.put(restartKey, restartValList);
			pgnUsrMap.put("RestartKey", restartKey);
			
			if(userSessionObj.containsKey(keyInUsrObj))
				userSessionObj.put(keyInUsrObj,pgnUsrMap);
	
		}
			
			LinkedHashMap pgnLinkedMap = new LinkedHashMap();
			pgnMap.put("RestartKey",restartKey);
			if(restartColKeyArr!=null && restartKey.length()>0)
				pgnMap.put(restartKey,restartValList);
			
			if(EBWLogger.isDebugEnabled())
				EBWLogger.logDebug(this,"Exited from postCreateSessionObj():- userSessionObj="+userSessionObj);
			
		}
		
	public String getRestatKeyFrmMap(String keyInStmtJava){
		String valInStmtJava = null;
		valInStmtJava = (String)stmtMap.get(keyInStmtJava);
		if(valInStmtJava == null)
			valInStmtJava = "";
		return valInStmtJava;
	}
	
	/**
	 * 
	 * @param userSessionObj
	 * @param formObj
	 * @param serviceId
	 * @param statementId
	 * @param tableArray
	 * @param hiddenVarRestartKey
	 */
	public void addToSession(HashMap userSessionObj,EbwForm formObj,String serviceId,String statementId,ArrayList tableArray,String hiddenVarRestartKey){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into addToSession()");
		LinkedHashMap pgnLinkedMap = new LinkedHashMap();
		ArrayList addingToSesionList = new ArrayList();
	
		
			if(userSessionObj.containsKey(keyInUsrObj) && userSessionObj.get(keyInUsrObj)!=null){
				pgnLinkedMap =(LinkedHashMap)userSessionObj.get(keyInUsrObj);
				addingToSesionList =(ArrayList)pgnLinkedMap.get(restartKey);
				
			}
			int arrListSize = 0;
			ArrayList header = new ArrayList(); 
			String pos = "";
			//code for getting restart keys from statemetn.properties
			
			ArrayList restartValList = new  ArrayList();
			String rowsInaPage = formObj.getRowsPerPage();
			
			int lastRow = 0;
			if(tableArray!=null && tableArray.size()>=1){
				if(Integer.parseInt(rowsInaPage)<((tableArray.size())-1))
					 lastRow =(tableArray.size()-ROWS_TO_MINUS_TOGET_LASTROW);
				else
					lastRow = tableArray.size()-ROWS_TO_MINUS_TOGET_LASTROW_WHEN_ONEROW_REMAINS;
				
				LinkedHashMap pgnUsrMap = (LinkedHashMap)userSessionObj.get(keyInUsrObj);
				if(pgnUsrMap.containsKey("SortColIndexStr"))
					pos=(String)pgnUsrMap.get("SortColIndexStr");
				String[] posStrArr=pos.split(":");
				
				//iterating the tableArray to get the values
				String colValue = "";
				ArrayList tableRowList = new ArrayList();
				tableRowList =(ArrayList)tableArray.get(lastRow);
				Object valObjArr[] = new Object[posStrArr.length];
				int colmn = 0;
				for(int k=0;k<posStrArr.length;k++){
					for(int i=0;i<tableRowList.size();i++){
				 		if(Integer.toString(i).equalsIgnoreCase(posStrArr[k]))
				 			valObjArr[k] = tableRowList.get(i);
					 }
				 }
					
				addingToSesionList.add(valObjArr);
				/**for debugging    **************************/
				if(addingToSesionList !=null && addingToSesionList.size()>0){
					for(int i=0;i< addingToSesionList.size();i++){
						Object[] obj =(Object[]) addingToSesionList.get(i);
						EBWLogger.logDebug(this,"starting adding in the session ");
						for(int j=0;j<obj.length;j++){
							EBWLogger.logDebug(this,"obj["+j+"]:"+obj[j]);
						}
						EBWLogger.logDebug(this,"Adding to the session is over");
					}
				}
				/**end of debugging      **************************/
 						
 				if(formObj.getRowsPerPage()!=null && Integer.parseInt(formObj.getRowsPerPage())==1) 
 			 	restartValList.add(valObjArr);
 						
 				pos = "";
				colValue = ""; 
						
				 
				}else{
					if(tableArray!=null && tableArray.size()==1)
						EBWLogger.logDebug(this,"there is no data in the table only header is there");
				}
			
				if(restartColKeyArr!=null && restartKey.length()>0)
					pgnLinkedMap.put(restartKey,addingToSesionList);
				pgnLinkedMap.put("PaginationIndex",formObj.getPaginationIndex());
				pgnLinkedMap.put("PagiantionDirection",formObj.getPaginationDirection());
				if(userSessionObj.containsKey(keyInUsrObj))
						userSessionObj.put(keyInUsrObj,pgnLinkedMap);
				if(EBWLogger.isDebugEnabled())
					EBWLogger.logDebug(this,"Exited from addToSession()");
		}

		
	/**
	 * 
	 * @param hiddenVarRestartKey
	 * @param statementId
	 * @return
	 */						
	public String getRestartKeyCol(String hiddenVarRestartKey,String statementId){
		/*if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into getRestartKeyCol() :- hiddenVarRestartKey   :"+hiddenVarRestartKey+"statementId  :" +statementId);*/
		String keyForRbRestart = null;
		String restartLogic = null;
		String keyForRbPgn = null;
		String pgnKeys = null;
		HashMap linkedHash = null;
		keyForRbPgn = statementId+".PgnUniqueKeys";
		EBWLogger.logDebug(this,"isExtrnlStmt  is : "+isExtrnlStmt);
		
		
		try {
			if(!(isExtrnlStmt.equalsIgnoreCase("true")))
			{	
				linkedHash = EBWStatement.getMap();
				pgnKeys = linkedHash.get(keyForRbPgn).toString();
			}
			else
			{	
				ResourceBundle rbPgn = ResourceBundle.getBundle("Statement");
				pgnKeys = rbPgn.getString(keyForRbPgn);
			}
		}catch(Exception e){
			EBWLogger.trace(this,"key not found in the resource bundle"+e);
		}
	    if(hiddenVarRestartKey == null || hiddenVarRestartKey.length()<0 || hiddenVarRestartKey.equalsIgnoreCase("") ){
	    	
	    	//keyForRbRestart = statementId+".RestartColKey";
	    	keyForRbRestart = statementId+".SortKeys";
	    	
	    	
	    	
			try {
				if(!(isExtrnlStmt.equalsIgnoreCase("true")))
				{
					linkedHash = EBWStatement.getMap();
					restartLogic = linkedHash.get(keyForRbRestart).toString();
				}
				else
				{
					ResourceBundle rbPgn = ResourceBundle.getBundle("Statement");
					restartLogic = rbPgn.getString(keyForRbRestart);
				}
				
			}catch(Exception e){
				EBWLogger.trace(this,"key not found in the resource bundle"+e);
			}
	    }else{
	    	if(hiddenVarRestartKey.equalsIgnoreCase("null")){
	    		keyForRbRestart = statementId+".SortKeys";
		    	
		    	
				try {
					if(!(isExtrnlStmt.equalsIgnoreCase("true")))
					{
						linkedHash = EBWStatement.getMap();
						restartLogic =  linkedHash.get(keyForRbRestart).toString();
					}
					else
					{
						//EBWLogger.logDebug(this,"Looking in Statement");
						ResourceBundle rbPgn = ResourceBundle.getBundle("Statement");
						restartLogic = rbPgn.getString(keyForRbRestart);
					}
					
				}catch(Exception e){
					EBWLogger.trace(this,"key not found in the resource bundle"+e);
				}
	    	}else{
		    	
		    	restartLogic = hiddenVarRestartKey;
	    	}
	    }
	  
	    if(restartLogic!=null && restartLogic.length()>0){
		    if(restartLogic.endsWith("#"))
		    	restartLogic = restartLogic+pgnKeys;
			else
				restartLogic = restartLogic+"#"+pgnKeys;
	    }else
	    	restartLogic = pgnKeys;
	    if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exited from getRestartKeyCol():- restartLogic="+restartLogic); 
		return restartLogic;
	}
	
	/**
	 * 
	 * @param formObj
	 * @param keyValueMap
	 * @param userSessionObj
	 * @param serviceId
	 * @param statementId
	 * @param hiddenVarRestartKey
	 * @param actionType
	 * @return
	 */	
	
	public HashMap formConditionQuery(EbwForm formObj,HashMap keyValueMap,HashMap userSessionObj,String serviceId,String statementId,String hiddenVarRestartKey,String actionType){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into formConditionQuery():- keyValueMap="+keyValueMap);
		HashMap ConditionQueryMap = new HashMap();
		String queryToBeAdded = "";
		String tempStrKey = null;
		String tempStrVal = null;
		if(getRestartKeyCol(hiddenVarRestartKey,statementId)!=null){
			if(keyValueMap!=null && keyValueMap.containsKey(getRestartKeyCol(hiddenVarRestartKey,statementId))){
				String colKeyString= restartKey;
				
				String[] colArray1= null;
				int keycount = 0;
				if(colKeyString!=null && colKeyString.length()>0){
						colArray1 = colKeyString.split("#");
						/*
						 * krishna
						 */
						
						/*List list = Arrays.asList(colArray1);
						Set set = new HashSet(list);
						String[] result=new String[set.size()];
						set.toArray(result);
						colArray1 = result;
						*/
						
						if(colArray1!=null && colArray1.length>0)
							keycount = colArray1.length;
				}
				Object[] colValString = new Object[keycount];
				Object[] tempObj = (Object[])keyValueMap.get(colKeyString);
				if(tempObj !=null)
					colValString=(Object[])tempObj.clone();
				else
					colValString=(Object[])keyValueMap.get(colKeyString);
				
				
				EBWLogger.trace(this,"colKeyString  :"+colKeyString +"&&&&&"+"colValString :"+colValString );
				
				
				String[] colArray = null;
				//String[] valArray = null;
				//String[] dataTypeArray = null;
				Object[] valArray = null;
				
				if(colValString == null){
					
					
					//String[] datatypeArray=null;
					//String temp[]  = colKeyString.split("#");
					//datatypeArray = new String[temp.length];
					//queryToBeAdded = getOrderByValue(colKeyString,datatypeArray);
					queryToBeAdded = getOrderByValue(colKeyString);
					
				}else{
					// for debugging 
					EBWLogger.logDebug(this,"valarr.size :"+colValString.length);
					for(int v=0;v<colValString.length;v++)
						EBWLogger.logDebug(this,"colval["+v+"]:"+colValString[v]);
					// debuggong code over
					
					if(colKeyString!=null && colKeyString.length()>0){
						
						colArray = colKeyString.split("#");
						String[] dataTypeArray =new String[colArray.length];
						String[] orderTypeArray =new String[colArray.length];
						for(int i=0;i<colArray.length;i++){
							
							orderTypeArray[i] = colArray[i].substring(colArray[i].indexOf(":")+1,colArray[i].length());
							dataTypeArray[i] =  colArray[i].substring(colArray[i].indexOf(":")+1,colArray[i].lastIndexOf(":"));
						}						
						
						if(colValString!=null && colValString.length>0){
							
							valArray = colValString;
						}
						
						String tempColArr[]=new String[colArray.length];
						String orderTypeArr[]=new String[colArray.length];
						boolean orderTypeArrBool[]=new boolean[colArray.length];
						for(int k=0;k<colArray.length ;k++){
							tempColArr[k] = colArray[k].substring(0,colArray[k].indexOf(":"));
							//NOTE :  This key is from statementname.properties. (Ex:a.assetnaem) . If i get from header click it will be like assetname. code differ needs to be modified. 
								tempColArr[k] = tempColArr[k].substring(tempColArr[k].indexOf(".")+1,tempColArr[k].length());
								orderTypeArr[k] = colArray[k].substring(colArray[k].lastIndexOf(":")+1, colArray[k].length());
								if(orderTypeArr[k].equalsIgnoreCase("DESC"))
									orderTypeArrBool[k] = true;
								else
									orderTypeArrBool[k] = false;
						}
						String aliasArr[] = new String[tempColArr.length];
						aliasArr = getAliasName(tempColArr,statementId);
						//changing the date format according to db data format
						Object[] dataFormattedArr = valArray;
						
						//if(actionType !=null && actionType.equalsIgnoreCase("prev") && (Integer.parseInt(formObj.getPaginationIndex())) ==1)
							// dataFormattedArr = changeDateFormat(valArray,dataTypeArray);
						//else if(actionType !=null && !actionType.equalsIgnoreCase("prev"))
							 dataFormattedArr = changeDateFormat(valArray,dataTypeArray);
						queryToBeAdded = generateRestartLogicStr(tempColArr,dataFormattedArr,dataTypeArray,aliasArr,statementId,orderTypeArrBool);
						if(isFirstPage(userSessionObj,formObj,serviceId,statementId)){
							if(queryToBeAdded.indexOf(">")>-1)
								queryToBeAdded = queryToBeAdded.replaceAll(">",">=");
							if(queryToBeAdded.indexOf("<")>-1)
								queryToBeAdded = queryToBeAdded.replaceAll("<","<=");
							
						}
					}
				
					//queryToBeAdded = queryToBeAdded +" "+getOrderByValue(colKeyString,dataTypeArray);
					queryToBeAdded = queryToBeAdded +" "+getOrderByValue(colKeyString);
					EBWLogger.trace(this,"query :"+queryToBeAdded);
				}
			}
		}				
		ConditionQueryMap.put("restartlogic",queryToBeAdded);
		queryToBeAdded = null;
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exited from formConditionQuery():- ConditionQueryMap="+ConditionQueryMap);
		return ConditionQueryMap;
	}
	
	/**
	 * 
	 * @param valArray
	 * @param dataTypeArray
	 * @return
	 */
	public Object[] changeDateFormat(Object[] valArray, String[] dataTypeArray) {
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into changeDateFormat()");
		for (int i = 0; i < valArray.length && i < dataTypeArray.length; i++) {
			//EBWLogger.logDebug(this,"valArray[" + i + "] :" + valArray[i]+ "**********" + "dataTypeArr[" + i + "]:"+ dataTypeArray[i]);
			// the below if don't have any meaning. should find solution
			try {
				if (valArray[i] != null && valArray[i].toString().length() > 0
						&& !valArray[i].toString().equalsIgnoreCase("null")) {
					if (dataTypeArray[i].equalsIgnoreCase("date") || dataTypeArray[i].equalsIgnoreCase("DateTime")) {
						//EBWLogger.logDebug(this,"before :" + valArray[i]);
						/*No need to call this method. Because conversion is not needed. 
						//but have the code for future use.
						valArray[i] = (Object) ConvertionUtil.convertToDBDateTimeStr(valArray[i].toString());
						 * */
						
						//EBWLogger.logDebug(this,"after :" + valArray[i]);
					}
				}
			} catch (Exception e) {
				EBWLogger.logDebug(this,"val arr is null so couldnot perform changeDateFormat. but no problem");
			}
		}
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exited from changeDateFormat()");
		return valArray;
	}
	
	/**
	 * 
	 * @param dataArr
	 * @return
	 */
	public boolean[] getDataTypeBool(String[] dataArr) {
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into getDataTypeBool()");
		boolean resultBool[] = new boolean[dataArr.length];
		for (int i = 0; i < dataArr.length; i++) {
			
			if (dataArr[i] != null && dataArr[i].length() > 0) {
				if (dataArr[i].toLowerCase().equalsIgnoreCase("varchar")
						|| dataArr[i].toLowerCase().equalsIgnoreCase("date")) {
					resultBool[i] = true;

				} else
					resultBool[i] = false;
			} else
				resultBool[i] = false;

		}
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exited from getDataTypeBool()");
		return resultBool;
	}
	
	/**
	 * 
	 * @param dataArr
	 * @return
	 */
	public boolean[] getDataDATETypeBool(String[] dataArr) {
		boolean resultBool[] = new boolean[dataArr.length];
		for (int i = 0; i < dataArr.length; i++) {
			
			if (dataArr[i] != null && dataArr[i].length() > 0) {
				if (dataArr[i].toLowerCase().equalsIgnoreCase("date")) {
					resultBool[i] = true;
				} else
					resultBool[i] = false;
			} else
				resultBool[i] = false;

		}
		return resultBool;
	}
	
	/**
	 * 
	 * @param pos
	 * @param key
	 * @return
	 */
	public String[] callChangePos(int pos[],String key[]){
		String result[] = new String[pos.length];
		int k=0;
		for(int i=0;i<key.length;i++){
			for(int j=0;j<pos.length;j++){
				if(pos[j] == i)
					result[k] = key[i]; 
			}
		}
		return result;
	}
	
	
	public String generateRestartLogicStr(String[] newkeyArr,Object[] newvalArr,String[] newdataArr,String[] newaliasArr,String statementId,boolean[] orderTypeArrBool){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into generateRestartLogicStr()");
		String finalStr =null;
		
		/*
		if(newkeyArr!=null )
			EBWLogger.logDebug(this,"key Arr Length:"+newkeyArr.length);
		if(newvalArr!=null)
			EBWLogger.logDebug(this,"val arr Length :"+newvalArr.length);
		for(int i=0;i<newkeyArr.length;i++)
			EBWLogger.logDebug(this,"key Arr:"+newkeyArr[i]);
		for(int i=0;i<newvalArr.length;i++)
			EBWLogger.logDebug(this," val Arr:"+newvalArr[i]);
		*/
		int pos[] = new int[newvalArr.length];
		int no = 0;
		for(int i=0;i<newkeyArr.length;i++){
			if(newvalArr[i] !=null ){
				pos[no] = i;
				no++;
			}
		}
		
		String keyArr[] = new String[newkeyArr.length];
		Object[] valArr = new Object[newkeyArr.length];
		String dataArr[] = new String[newkeyArr.length];
		String aliasArr[] = new String[newkeyArr.length];
		
		keyArr = newkeyArr;
		valArr = newvalArr;
		dataArr = newdataArr;
		aliasArr = newaliasArr;
		
		boolean[] datafinalArr = getDataTypeBool(dataArr);
		boolean[] dataDATEfinalArr = getDataDATETypeBool(dataArr);
		StringBuffer finalBuff = new StringBuffer();
		StringBuffer stBuff = new StringBuffer();
		
		boolean nullColArrBool[] = new boolean[keyArr.length];
		nullColArrBool = getNullColArr(keyArr,statementId);
		boolean nullColPresent = false;
		for(int i=0;i<nullColArrBool.length;i++){
			if(nullColArrBool[i]){
				nullColPresent = true;
				break;
			}				
		}
		/*
		for(int i=0;i<datafinalArr.length;i++)
			EBWLogger.logDebug(this,"datafinalArr Arr:"+datafinalArr[i]);
		for(int i=0;i<dataDATEfinalArr.length;i++)
			EBWLogger.logDebug(this," dataDATEfinalArr Arr:"+dataDATEfinalArr[i]);
		for(int i=0;i<orderTypeArrBool.length;i++)
			EBWLogger.logDebug(this,"orderTypeArrBool Arr:"+orderTypeArrBool[i]);
		for(int i=0;i<nullColArrBool.length;i++)
			EBWLogger.logDebug(this," nullColArrBool Arr:"+nullColArrBool[i]);
		*/
			for(int j=keyArr.length-1;j>=0;j--){
				stBuff = new StringBuffer();
				stBuff.append("( ");
				for(int k=0;k<keyArr.length;k++){
					
					if(valArr[k]!=null ){
						if(datafinalArr[k]){
							if(dataDATEfinalArr[k]){
								if(k==j){
									if(orderTypeArrBool[k]){
										if(nullColArrBool[k]){
											//stBuff.append(" "+""+ aliasArr[k]+keyArr[k]+"<to_date('"+valArr[k]+"','"+dbOutDateTimeFormat+"'))");
											stBuff.append(" "+"date("+keyArr[k]+")<'"+valArr[k]+"')");
										}else{
											//stBuff.append(" "+"("+ aliasArr[k]+keyArr[k]+"<to_date('"+valArr[k]+"','"+dbOutDateTimeFormat+"') or "+ aliasArr[k]+keyArr[k]+" IS NULL))");
											stBuff.append(" "+"(date("+ keyArr[k]+")<'"+valArr[k]+"' or date("+ keyArr[k]+") IS NULL))");
										}
											break;
									}else{
										if(nullColArrBool[k]){
											//stBuff.append(" "+"("+ aliasArr[k]+keyArr[k]+">to_date('"+valArr[k]+"','"+dbOutDateTimeFormat+"') or "+ aliasArr[k]+keyArr[k]+" IS NULL))");
											stBuff.append(" "+"(date("+keyArr[k]+")>'"+valArr[k]+"' or date("+keyArr[k]+") IS NULL))");
										}else{
											//stBuff.append(" "+""+ aliasArr[k]+keyArr[k]+">to_date('"+valArr[k]+"','"+dbOutDateTimeFormat+"'))");
											stBuff.append(" "+"date("+keyArr[k]+")>'"+valArr[k]+"')");
										}
											break;
									}
								}else
									if(nullColArrBool[k]){
										if(orderTypeArrBool[k]){
											//stBuff.append(" "+ aliasArr[k]+keyArr[k]+"=to_date('"+valArr[k]+"','"+dbOutDateTimeFormat+"') AND " );
											stBuff.append(" date("+keyArr[k]+")='"+valArr[k]+"' AND " );
										}else{
										//stBuff.append(" "+"("+ aliasArr[k]+keyArr[k]+"=to_date('"+valArr[k]+"','"+dbOutDateTimeFormat+"')  or "+ aliasArr[k]+keyArr[k]+" IS NULL) AND " );
											stBuff.append(" "+"(date("+keyArr[k]+")='"+valArr[k]+"' or date("+keyArr[k]+") IS NULL) AND " );
										}
									}else{
										//stBuff.append(" "+""+ aliasArr[k]+keyArr[k]+"=to_date('"+valArr[k]+"','"+dbOutDateTimeFormat+"') AND ");
										stBuff.append(" "+"date("+keyArr[k]+")='"+valArr[k]+"' AND ");
									}
								}else{
								if(k==j){
									if(orderTypeArrBool[k]){
										if(nullColArrBool[k]){
											stBuff.append(keyArr[k]+"<'"+valArr[k]+"' )");
										}else{
											stBuff.append(" "+ keyArr[k]+"<'"+valArr[k]+"' )");
										}
											break;
									}else{
										if(nullColArrBool[k]){
											stBuff.append(" ("+ keyArr[k]+">'"+valArr[k]+"' or "+keyArr[k]+" IS NULL))");
										}else{
											stBuff.append(" "+ keyArr[k]+">'"+valArr[k]+"' )");
										}
											break;
									}
								}else{
									if(nullColArrBool[k]){
										stBuff.append(" ( "+ keyArr[k]+"='"+valArr[k]+"' ) AND ");
									}else{
										stBuff.append(keyArr[k]+"='"+valArr[k]+"' AND ");
									}
								}
										
							}
						}else{
							if(k==j){
								if(orderTypeArrBool[k]){
									if(nullColArrBool[k]){
										stBuff.append(" ( "+keyArr[k]+"<"+valArr[k]+" or "+keyArr[k]+" IS NULL ))");
									}else{
										if(dataArr[k].equalsIgnoreCase("datetime")){
										stBuff.append("date("+keyArr[k]+")<'"+valArr[k]+"')");
										}else{
											stBuff.append(keyArr[k]+"<"+valArr[k]+" )");										}
									}
									break;
								}else{
									if(nullColArrBool[k]){
										stBuff.append(" ( "+keyArr[k]+">"+valArr[k]+" or "+keyArr[k]+" IS NULL ))");
									}else{
										if(dataArr[k].equalsIgnoreCase("datetime")){
										stBuff.append("date("+keyArr[k]+")>'"+valArr[k]+"')");
									}else{
										stBuff.append(keyArr[k]+">"+valArr[k]+" )");
									}
									}
									break;
								}
							}else{
								if(nullColArrBool[k]){
									stBuff.append(" ( "+keyArr[k]+"="+valArr[k]+" or "+keyArr[k]+" IS NULL )AND ");
								}else{
									if(dataArr[k].equalsIgnoreCase("datetime")){
									stBuff.append("date("+keyArr[k]+")='"+valArr[k]+"' AND ");
									}else{
										stBuff.append(keyArr[k]+"="+valArr[k]+" AND ");
									}
								}
							}
						}									
					}else{
						if(k==j){
							if(j==0){
								if(orderTypeArrBool[k]){
									stBuff.append(keyArr[k]+" IS NOT NULL )");
									stBuff.deleteCharAt(stBuff.length()-2);
								}else{
									stBuff.deleteCharAt(stBuff.length()-2);
								}
								
							}else{
								if(j<keyArr.length-1 ){
									stBuff.delete(0,stBuff.length());
								}
								else{
									stBuff.append(keyArr[k]+" IS NULL )");
								}
								
							}
							break;
						}else{
							
							stBuff.append(keyArr[k]+" IS NULL AND ");
						}
					}
					
					
				}
				EBWLogger.logDebug(this," stBuff Arr:"+stBuff);
				finalBuff.append(stBuff);
				if(stBuff.length()<=0){
					finalBuff.delete(finalBuff.lastIndexOf("OR"),finalBuff.length() );
				}
				finalBuff.append(" OR ");	
			}
				
		finalStr = finalBuff.substring(0,finalBuff.lastIndexOf(" OR "));
		if(finalStr.endsWith("OR  ")){
			finalStr = finalBuff.substring(0,finalBuff.lastIndexOf("OR  "));
			
		}
		finalStr=" AND ("+finalStr+" )";
		
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exited from generateRestartLogicStr():- finalStr="+finalStr);
			return finalStr;
	}
	
	/**
	 * 
	 * @param keyArr
	 * @param statementId
	 * @return
	 */	
	
	boolean[] getNullColArr(String[] keyArr,String statementId){
		//int result[] = new int[keyArr.length];
		boolean result[] = new boolean[keyArr.length];
		String nullValColKey = statementId+".NullValCol";
		String nullValCols = null;
		ResourceBundle rb1= ResourceBundle.getBundle("ebw");
		String externalishedStatement = rb1.getString("isExternalisedStatements");
		try {
			if(externalishedStatement.equalsIgnoreCase("true"))
			{
				ResourceBundle rbPgn = ResourceBundle.getBundle("Statement");
				nullValCols = rbPgn.getString(nullValColKey);
			}
			else
			{
				HashMap linkedHash = EBWStatement.getMap();
				nullValCols = linkedHash.get(nullValColKey).toString();
			}
			if(nullValCols!=null && nullValCols.length()>0 ){
				
				if(nullValCols.endsWith("#"))
					nullValCols = nullValCols.substring(0,nullValCols.lastIndexOf("#"));
				String nulValColArr[] = nullValCols.split("#");
				for(int i=0;i<keyArr.length;i++){
					for(int j=0;j<nulValColArr.length;j++){
						if(keyArr[i].equalsIgnoreCase(nulValColArr[j]))
							result[i] = true;
						else
							result[i] = false;
					}
				}
			}
			
		}catch(Exception e){
			EBWLogger.trace(this,"key not found in the resource bundle"+e);
		}
		
		return result;
	}
	
	
	
	/**
	 * 
	 * @param colArr
	 * @param statementId
	 * @return
	 */	
	public String[] getAliasName(String[] colArr,String statementId){
		String result[] = new String[colArr.length];
		
		 ResourceBundle formRB = null;
		 String inputQuery = null;
		 ResourceBundle rb1= ResourceBundle.getBundle("ebw");
		 String externalishedStatement = rb1.getString("isExternalisedStatements");
		 HashMap linkedHash = EBWStatement.getMap();
		 if(externalishedStatement.equalsIgnoreCase("true"))
			 formRB = ResourceBundle.getBundle("Statement");
		 statementId = statementId +"_page.Query";
		 if(externalishedStatement.equalsIgnoreCase("true"))
			 inputQuery = formRB.getString(statementId);
		 else
			 inputQuery = linkedHash.get(statementId).toString(); 
		 
		 
			String keyCol = null;
			String  alias = null;
			//EBWLogger.logDebug(this,"index :"+keyCol.substring(keyCol.indexOf(".")+1,keyCol.length()));
			for(int i=0;i<colArr.length;i++){
				alias = "";
				String query = inputQuery;
				keyCol = colArr[i];
				String condition="."+keyCol;
				if(query.indexOf(condition)>-1){
					query = query.substring(0,query.indexOf(condition)+condition.length());
					//EBWLogger.logDebug(this,"query:"+query);
					
					
						if(query.lastIndexOf(",")>-1){
							query = query.substring(query.lastIndexOf(",")+1,query.length());
							
							if(query.lastIndexOf(" ")>-1){
								query = query.substring(query.lastIndexOf(" ")+1,query.length());
								
								alias = query.substring(0,query.lastIndexOf(condition));
								alias=alias.trim();
								alias=alias+".";
							}
						}else{
							if(query.lastIndexOf(" ")>-1){
								query = query.substring(query.lastIndexOf(" ")+1,query.length());
							
								alias = query.substring(0,query.lastIndexOf(condition));
								alias=alias.trim();
								alias=alias+".";
							}
						}
						if(query.endsWith(condition)){
							
						}
					
							
					}
				result[i] = alias;
					
			
											 
			}
		 return result;
	}
	
	/**
	 * 
	 * @param colKeyString
	 * @return
	 */
	public String getOrderByValue(String colKeyString){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into getOrderByValue():- colKeyString="+colKeyString);
		String queryToBeAdded = null;
		queryToBeAdded = "";
		if(colKeyString!=null && colKeyString.length()>0){
			String[] colArray = null; 
			//String tempStrKey = null;
			queryToBeAdded = " order by ";
			colArray = colKeyString.split("#");
			String[] keyArray = new String[colArray.length];
			String[] orderTypeArr = new String[colArray.length];
			boolean orderTypeBoolArr[]= new boolean[colArray.length];
			for(int i=0;i<colArray.length;i++){
				keyArray[i] = colArray[i].substring(0,colArray[i].indexOf(":"));
				orderTypeArr[i] = colArray[i].substring(colArray[i].lastIndexOf(":")+1,colArray[i].length());
				if(orderTypeArr[i].equalsIgnoreCase("desc"))
					orderTypeBoolArr[i] = true;
				else
					orderTypeBoolArr[i] = false;
			}
			for(int j=0;j<colArray.length;j++){
				//queryToBeAdded = queryToBeAdded+colArray[j].substring(0,colArray[j].lastIndexOf(":"))+",";
				if(orderTypeBoolArr[j])
					queryToBeAdded = queryToBeAdded+ " " + keyArray[j] + " DESC "+" , ";
				else
					queryToBeAdded = queryToBeAdded+" "+keyArray[j] + " , ";
				//tempStrKey = colArray[j].substring(0,colArray[j].lastIndexOf(":"));
			}
			queryToBeAdded = queryToBeAdded.substring(0,queryToBeAdded.lastIndexOf(","));
			if(EBWLogger.isDebugEnabled())
				EBWLogger.logDebug(this,"Exited from getOrderByValue():- queryToBeAdded="+queryToBeAdded);
		}
		return queryToBeAdded;
	}
	
	/**
	 * 
	 * @param colKeyString
	 * @param dataTypeArray
	 * @return
	 */
	public String getOrderByValuewithtwoArguments(String colKeyString,String[] dataTypeArray){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into getOrderByValue():- colKeyString="+colKeyString);
		String queryToBeAdded = null;
		queryToBeAdded = "";
		if(colKeyString!=null && colKeyString.length()>0){
			String[] colArray = null; 
			if(dataTypeArray !=null && dataTypeArray.length>0)
				for(int i=0;i<dataTypeArray.length;i++)
					EBWLogger.logDebug(this," Bharathi dataType checking :"+dataTypeArray[i]);
				
			
			String tempStrKey = null;
			queryToBeAdded = " order by ";
			colArray = colKeyString.split("#");
			for(int j=0;j<colArray.length;j++){
				if(dataTypeArray !=null && dataTypeArray.length>0){
					EBWLogger.logDebug(this," Bharathi  data Type arr date not equal to null");
					if(dataTypeArray[j]!=null && dataTypeArray[j].length()>0 && dataTypeArray[j].equalsIgnoreCase("date")){
						String dateKey = null;
						EBWLogger.logDebug(this," Bharathi  data Type arr date  equal to date");
						//to_date(to_char(DAT,'dd-Mon-yyyy'),'dd/mm/yyyy')
						dateKey = colArray[j].substring(0,colArray[j].lastIndexOf(":"));
						String finaldateKey= "to_date(to_char("+dateKey+",'dd-Mon-yyyy'),'dd/mm/yyyy')";
						queryToBeAdded = queryToBeAdded+finaldateKey+",";
					}
				}
				else
					queryToBeAdded = queryToBeAdded+colArray[j].substring(0,colArray[j].lastIndexOf(":"))+",";
				tempStrKey = colArray[j].substring(0,colArray[j].lastIndexOf(":"));
			}
			queryToBeAdded = queryToBeAdded.substring(0,queryToBeAdded.lastIndexOf(","));
		}
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exited from getOrderByValue():- queryToBeAdded="+queryToBeAdded);
		return queryToBeAdded;
	}
	
	/**
	 * 
	 * @param userSessionObj
	 * @param formObj
	 * @param serviceId
	 * @param statementId
	 * @return
	 */
	public boolean isFirstPage(HashMap userSessionObj,EbwForm formObj,String serviceId,String statementId){
		String actionType = formObj.getActionType();
		EBWLogger.trace(this,"In isFirstPage():"+userSessionObj+" && "+actionType+" && "+serviceId);
		boolean result = false;
		LinkedHashMap pgnLinkedMap =new LinkedHashMap(); 
		if(userSessionObj !=null){
			if(userSessionObj.containsKey("sessionid"))
				sessionId =(String)userSessionObj.get("sessionid");
			String key=sessionId+"_"+serviceId+"_"+statementId;
			if(userSessionObj.containsKey(key) && userSessionObj.get(key)!=null){
				pgnLinkedMap =(LinkedHashMap)userSessionObj.get(key);
				String pgnNo = (String)pgnLinkedMap.get("PaginationIndex");
				
				if((actionType.equalsIgnoreCase("prev") || actionType.equalsIgnoreCase("retain")) && Integer.parseInt(pgnNo) == 1)
					result = true;
			}
		}
		EBWLogger.trace(this,"in isFirstPage():"+result);
		return result;
	}
	
	
	
	
	/**
	 * 
	 * @param formObj
	 * @param actionType
	 */
	public void reset(EbwForm formObj, String actionType){
		actionType = null;
	    formObj.setActionType(actionType);
	}
	
	/*public String getAliasName(String keyCol,String statementId){
	EBWLogger.trace(this,"in getAliasName() keyCol:"+keyCol);
	 ResourceBundle formRB = null;
	 String condition="."+keyCol;
	 formRB = ResourceBundle.getBundle("Statement");
	 statementId = statementId +"_page.Query";
	 String query = formRB.getString(statementId);
	 EBWLogger.trace(this,"condition:"+condition);
	 String result = null;
	 if(query.indexOf(condition)>-1)
	 	result = query.substring(((query.indexOf(condition))-1),((query.indexOf(condition))+1));
	 else
	 	result = null;
	 return result;
	}*/
	
	/**
	 * 
	 * @param stmtId
	 * @param hiddenVarRestartKey
	 * @return
	 */
	//being used for export 
	public HashMap orderByStrForQuery(String stmtId,String hiddenVarRestartKey){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into orderByStrForQuery():- stmtId="+stmtId+"  hiddenVarRestartKey="+hiddenVarRestartKey);
		HashMap resultMap = new HashMap();
		String restartKey = getRestartKeyCol(hiddenVarRestartKey,stmtId);
		String dataType[] = null;
		String orderbyVal = getOrderByValue(restartKey);
		HashMap tempMap = new HashMap();
		tempMap.put("export","y");
		tempMap.put("restartlogic",orderbyVal);
		//tempMap.put("RowsPerPage","11");
		resultMap.put("export",tempMap);		
		return resultMap;
	}
}
