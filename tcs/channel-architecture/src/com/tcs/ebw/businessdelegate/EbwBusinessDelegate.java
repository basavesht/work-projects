/*
 * Created on Mar 27, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.businessdelegate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.commons.beanutils.BeanUtils;

import com.tcs.ebw.common.context.EBWAppContext;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.common.util.SessionUtil;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author TCS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class EbwBusinessDelegate {
	protected String actionType = null;
	protected String  delegetePgnFlag = null;
	//protected String delegateSortWithoutPgnFlag = null;

	protected String rowsPerPage = null;
	protected String hiddenVarRestartKey = null;
	protected UserPrincipal objUserPrincipal;
	protected Object objParams[] = null;
	protected Class clsParamTypes[] = null;
	UserPrincipal userPrincipal = null;

	/**
	 * 
	 */
	public EbwBusinessDelegate() {
		super();
	}

	public static void main(String[] args) {
	}


	/************Single table code Start*****************************************************************************/

	/*for single table
	 *
	 * This method is being used for single table pagination. 
	 * This populates the objParams befing passed to the database service with the following parameters
	 * 1. TransferObject
	 * 2. Pagination HashMap
	 * 3. Booleaan transaction variable
	 */

	protected void handlePagination(HashMap objUserSessionObject,EBWTransferObject objTO,
			EbwForm objForm,String strServiceId,String strStatementId ) throws Exception{
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into  handlePagination() single table");

		PaginationDelegateHelper paginationDelegateHelper = new PaginationDelegateHelper();
		HashMap toQueryExecutorMp = new HashMap();
		LinkedHashMap stmtidInfo = new LinkedHashMap();
		String actnType = objForm.getActionType();


		LinkedHashMap stmtMap = new LinkedHashMap();
		if(objUserSessionObject.containsKey("StatementInfo")){
			stmtMap =(LinkedHashMap)objUserSessionObject.get("StatementInfo");
			if(stmtMap.containsKey(strStatementId)){
				stmtidInfo = (LinkedHashMap)stmtMap.get(strStatementId);
			}else{
				userPrincipal = (UserPrincipal)objUserSessionObject.get("UserPrincipal");
				stmtidInfo = getStatementIdInfo(strStatementId);
				stmtMap.put(strStatementId, stmtidInfo);
				objUserSessionObject.put("StatementInfo", stmtMap);
			}

		}else{
			userPrincipal = (UserPrincipal)objUserSessionObject.get("UserPrincipal");
			stmtidInfo = getStatementIdInfo(strStatementId);
			stmtMap.put(strStatementId, stmtidInfo);
			objUserSessionObject.put("StatementInfo", stmtMap);
		}
//		rowsperpage is configuraing based on userprefarences -start		
//		SessionUtil.set("RowsPerPage","4");
		String rowsPerPageFrm=null;
		try{
			rowsPerPageFrm= (String)SessionUtil.get("RowsPerPage");
			//System.out.println("SessionUtil value:"+rowsPerPageFrm);
			if(rowsPerPageFrm!=null && rowsPerPageFrm!="" && rowsPerPageFrm.length()>0 ){					
				String strIdVal[]=stmtidInfo.toString().split("=");
				String strRowsVal[]=strIdVal[1].split(",");
				String PgnFlag = strIdVal[0];
				objForm.setRowsPerPage(rowsPerPageFrm);
				stmtidInfo.put(strStatementId, PgnFlag+","+rowsPerPageFrm);
			}
		}catch(Exception e){
			EBWLogger.logDebug(this, "rowsperpage not configured in the SessionUtil : "+e);
		}
//		rowsperpage is configuraing based on userprefarences -end



		System.out.println("statementIdInfo :"+stmtidInfo);
		LinkedHashMap stmtInfo =(LinkedHashMap) objUserSessionObject.get("StatementInfo");
		stmtidInfo=(LinkedHashMap)stmtInfo.get(strStatementId);

		if(EBWLogger.isDebugEnabled()) 
			EBWLogger.logDebug(this, "statement Info :"+stmtidInfo);

		if(objForm.getActionType()!=null && 
				objForm.getActionType().length()>0 && 
				objForm.getActionType().equalsIgnoreCase("export")){

			delegetePgnFlag="y";
			toQueryExecutorMp = callExportFn(objForm,objTO,paginationDelegateHelper,strStatementId,objForm.getRestartKey());

		}else{ 
			if(objForm.getActionType() == null || objForm.getActionType().length()<=0 || objForm.getActionType() =="" || objForm.getActionType().equalsIgnoreCase("next") || objForm.getActionType().equalsIgnoreCase("prev") ||objForm.getActionType().equalsIgnoreCase("list") || objForm.getActionType().equalsIgnoreCase("retain")){
				toQueryExecutorMp = checkPgn(objUserSessionObject,stmtidInfo,strServiceId, strStatementId ,  paginationDelegateHelper, objTO ,objForm);
			}
		}


		/**
		 * To create class array and object array dynamically. We cannot use clsParam = {} because this cannot
		 * be changed dynamically. It is a declarative statement only.
		 */
		ArrayList arrList = new ArrayList();
		System.out.println("toqueryExecutor Map for single table :"+toQueryExecutorMp);
		if(EBWLogger.isDebugEnabled()) 
			EBWLogger.logDebug(this, "toqueryExecutor Map for single table :"+toQueryExecutorMp);
		arrList.add(objTO); // Always first param is TO.
		if(delegetePgnFlag.equalsIgnoreCase("y"))
			arrList.add(toQueryExecutorMp); //To be added specific to pgntn
		arrList.add(new Boolean(false)); // Always last value is boolean for service call.

		objParams = arrList.toArray();
		clsParamTypes = new Class[arrList.size()];
		for(int i=0;i<arrList.size();i++){
			if(arrList.get(i) instanceof com.tcs.ebw.transferobject.EBWTransferObject)
				clsParamTypes[i]=Object.class;
			else
				clsParamTypes[i]=arrList.get(i).getClass();
		}
		arrList=null; // Not needed anymore
		/** Class and Object param population ends here **/
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exiting from handlePagination() single table");

	}



	protected HashMap checkPgn(HashMap objUserSessionObject, LinkedHashMap stmtidInfo, String serviceId,String statementId , PaginationDelegateHelper paginationDelegateHelper,EBWTransferObject toObj,EbwForm formObj){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into  checkpgn single table");
		Object aobj[]= new Object[3];
		Class aclass[]=null;
		HashMap toQueryExecutorMp = new HashMap();
		//getting the restart key from the form bean hiddden variable
		hiddenVarRestartKey = formObj.getRestartKey(); 
		LinkedHashMap fromBaseDelegateMap = performPagination(stmtidInfo,formObj,toObj,serviceId,statementId);

		if(fromBaseDelegateMap !=null && fromBaseDelegateMap.containsKey("DelegetePgnFlag") && fromBaseDelegateMap.containsKey("ActionType")){
			if(fromBaseDelegateMap.get("DelegetePgnFlag") != null)
				delegetePgnFlag = (String)fromBaseDelegateMap.get("DelegetePgnFlag");
			if(fromBaseDelegateMap.get("ActionType")!=null)
				actionType = (String)fromBaseDelegateMap.get("ActionType");
			if(fromBaseDelegateMap.get("RowsPerPage")!=null)
				rowsPerPage =(String)fromBaseDelegateMap.get("RowsPerPage");
		}

		//calling the preEcecute if pagn = true

		if(delegetePgnFlag.equalsIgnoreCase("y") && (rowsPerPage!=null && rowsPerPage.length()>0)){
			toQueryExecutorMp = paginationDelegateHelper.preExecute(objUserSessionObject,formObj,actionType,serviceId,statementId,hiddenVarRestartKey);
			toQueryExecutorMp.put("RowsPerPage",rowsPerPage);
			toQueryExecutorMp.put("PaginationIndex",formObj.getPaginationIndex());
		}
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exiting from checkPgn() single table ");
		return toQueryExecutorMp;
	}





	public LinkedHashMap performPagination( LinkedHashMap stmtidInfo,EbwForm formObj,EBWTransferObject toObj,String serviceId,String statementId){
		if(EBWLogger.isDebugEnabled()) 
			EBWLogger.logDebug(this,"Entered into performPagination() single table");
		String actionType = null;
		String idValue = null;
		String rowsPerPage = null;
		LinkedHashMap toDerivedDelegateMap = new LinkedHashMap();
		actionType = formObj.getActionType();

//		Checking if Pagination is there or not for the statement id.
		if(stmtidInfo!=null && stmtidInfo.containsKey(serviceId) && stmtidInfo.get(serviceId)!=null){
			idValue = (String)stmtidInfo.get(serviceId);

			if(idValue!=null && idValue.length()>0){
				String strIdVal[]=idValue.split(",");
				if(strIdVal.length>0)
					delegetePgnFlag = strIdVal[0];
				if(strIdVal.length>1)
					rowsPerPage = strIdVal[1];
				if(delegetePgnFlag.equalsIgnoreCase("y")){
					if(rowsPerPage!=null && rowsPerPage.length()>0){
						formObj.setRowsPerPage(rowsPerPage);
						toObj.setRowsinapage(Double.valueOf(rowsPerPage));
					}else{
						if(rowsPerPage == null ||  rowsPerPage.equals("")){
							try{
								if(PropertyFileReader.getProperty("RowsPerPage")!=null){
									rowsPerPage = PropertyFileReader.getProperty("RowsPerPage");
									formObj.setRowsPerPage(rowsPerPage);

									toObj.setRowsinapage(Double.valueOf(rowsPerPage));
								}
							}catch(Exception e){
								EBWLogger.logDebug(this, "rowsperpage not configured in access or in the ebw.properties : "+e);
							}
						}
					}
				}


			}
		}
		//Setting the actiontype in the initial condition if pgn is true 
		try{
			if(delegetePgnFlag.equalsIgnoreCase("y")){
				if(actionType == null || actionType.equalsIgnoreCase("list") || actionType.length()<=0){
					actionType="List";
					formObj.setPaginationIndex("1");
				}else
					actionType = formObj.getActionType();

				toDerivedDelegateMap.put("DelegetePgnFlag",delegetePgnFlag);
				toDerivedDelegateMap.put("ActionType",actionType);
				toDerivedDelegateMap.put("RowsPerPage",formObj.getRowsPerPage());

			}else
				delegetePgnFlag="n";

		}catch(Exception e){
			EBWLogger.logDebug(this, "Error in if(delegetePgnFlag.equalsIgnoreCase(\"y\") loop "+e);
		}
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exiting from performPagination() single table ");
		return toDerivedDelegateMap;
	}


	protected HashMap callExportFn(EbwForm ebwForm,EBWTransferObject responseListNDBTO,PaginationDelegateHelper paginationDelegateHelper, String stmtId,String restartKey){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into callExportFn single table");

		HashMap toQueryExMap = new HashMap();

		if(ebwForm.getExportPages()!=null && ebwForm.getExportPages().length()>0 ){
			int pageRows = (Integer.parseInt(ebwForm.getExportPages())) * (Integer.parseInt(ebwForm.getRowsPerPage()))  ;
			String rows = pageRows+"";
			responseListNDBTO.setRowsinapage(Double.valueOf(rows));
			toQueryExMap = paginationDelegateHelper.orderByStrForQuery(stmtId,restartKey);
			delegetePgnFlag="y";
		}
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exiting from callExportFn single table ");


		return toQueryExMap;

	}

	/************Single table code over*****************************************************************************/


	/************Code for Two table With single TO*****************************************************************************/

//	two table where TO is single and stmtid is str[]****************
	protected void handlePagination(HashMap objUserSessionObject,EBWTransferObject objTO,
			EbwForm objForm,String strServiceId,String strStatementId[] ) throws Exception{
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into handlePagination() with stmt[] and single TO");
		PaginationDelegateHelper paginationDelegateHelper = new PaginationDelegateHelper();
		HashMap toQueryExecutorMp = new HashMap();
		if(objForm.getActionType()!=null && 
				objForm.getActionType().length()>0 && 
				objForm.getActionType().equalsIgnoreCase("export")){
			delegetePgnFlag="y";
			toQueryExecutorMp = callExportFn(objForm,objTO,strServiceId,strStatementId,paginationDelegateHelper,objForm.getRestartKey());//TO DO u have to explicitly make actiontype =null after an export
		}else{ 
			if(objForm.getActionType() == null || objForm.getActionType().length()<=0 || objForm.getActionType() =="" || objForm.getActionType().equalsIgnoreCase("next") || objForm.getActionType().equalsIgnoreCase("prev") || objForm.getActionType().equalsIgnoreCase("list")){
				toQueryExecutorMp = checkPgn(objUserSessionObject, strServiceId, strStatementId ,  paginationDelegateHelper, objTO ,objForm);
			}
		}


		/**
		 * To create class array and object array dynamically. We cannot use clsParam = {} because this cannot
		 * be changed dynamically. It is a declarative statement only.
		 */


		if(EBWLogger.isDebugEnabled()) 
			EBWLogger.logDebug(this, "inside base delegate toQueryExecutorMp:" +toQueryExecutorMp);
		ArrayList arrList = new ArrayList();
		arrList.add(strStatementId);
		arrList.add(objTO); // Always first param is TO.
		if(delegetePgnFlag.equalsIgnoreCase("y"))
			arrList.add(toQueryExecutorMp); //To be added specific to pgntn
		arrList.add(new Boolean(false)); // Always last value is boolean for service call.

		objParams = arrList.toArray();
		clsParamTypes = new Class[arrList.size()];
		for(int i=0;i<arrList.size();i++){
			if(arrList.get(i) instanceof com.tcs.ebw.transferobject.EBWTransferObject)
				clsParamTypes[i]=Object.class;
			else
				clsParamTypes[i]=arrList.get(i).getClass();
		}
		arrList=null; // Not needed anymore
		/** Class and Object param population ends here **/
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this," Exiting from handlePagination() with stmt[] and single TO");


	}

	protected HashMap checkPgn(HashMap objUserSessionObject, String serviceId,String[] statementId , PaginationDelegateHelper paginationDelegateHelper,EBWTransferObject toObj,EbwForm formObj){
		if(EBWLogger.isDebugEnabled())	
			EBWLogger.logDebug(this,"Entered  into checkPgn() with stmt[] and single TO");
		Object aobj[]= new Object[3];
		Class aclass[]=null;
		HashMap toQueryExecutorMp = new HashMap();
		HashMap pgnInfoMap = new HashMap();
		//getting the restart key from the form bean hiddden variable
		hiddenVarRestartKey = formObj.getRestartKey(); 




		LinkedHashMap stmtidInfo = new LinkedHashMap();
		for(int i=0;i<statementId.length;i++){
			try{
				stmtidInfo = getStatementIdInfo(statementId[i]);
			}catch(Exception e){
				EBWLogger.trace(this,"statementid info not found for :"+statementId[i]);
			}
			LinkedHashMap fromBaseDelegateMap = performPagination(stmtidInfo,formObj,toObj,serviceId,statementId[i],"twotable");

			if(fromBaseDelegateMap !=null && fromBaseDelegateMap.containsKey("DelegetePgnFlag") && fromBaseDelegateMap.containsKey("ActionType")){
				if(fromBaseDelegateMap.get("DelegetePgnFlag") != null)
					delegetePgnFlag = (String)fromBaseDelegateMap.get("DelegetePgnFlag");
				if(fromBaseDelegateMap.get("ActionType")!=null)
					actionType = (String)fromBaseDelegateMap.get("ActionType");
				if(fromBaseDelegateMap.get("RowsPerPage")!=null)
					rowsPerPage =(String)fromBaseDelegateMap.get("RowsPerPage");
			}

			//calling the preEcecute if pagn = true
			HashMap tempMap = new HashMap();

			if(delegetePgnFlag.equalsIgnoreCase("y") && (rowsPerPage!=null || !((rowsPerPage).equalsIgnoreCase("")))){


				tempMap = paginationDelegateHelper.preExecute(objUserSessionObject,formObj,actionType,serviceId,statementId[i],hiddenVarRestartKey);

				tempMap.put("RowsPerPage",rowsPerPage);
				tempMap.put("PaginationIndex",formObj.getPaginationIndex());
				tempMap.put("PaginationFlag","y");
				pgnInfoMap.put(statementId[i]+"_PaginationFlag","y");

				toQueryExecutorMp.put(statementId[i],tempMap);
			}else{
				tempMap.put("PaginationFlag","n");
				pgnInfoMap.put(statementId[i]+"_PaginationFlag","n");
				toQueryExecutorMp.put(statementId[i],tempMap);
			}

		}
		HashMap toQueryExecutorMp1 = new HashMap();
		toQueryExecutorMp1.put("toQueryExecutor",toQueryExecutorMp);
		toQueryExecutorMp1.put("pgnInfoMap",pgnInfoMap);
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exiting from checkPgn() with stmt[] and single TO");
		return toQueryExecutorMp1;
	}

	public LinkedHashMap performPagination( LinkedHashMap stmtidInfo,EbwForm formObj,EBWTransferObject toObj,String serviceId,String statementId, String twoTables){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into performPagination() for n table ");
		String actionType = null;
		String idValue = null;
		String rowsPerPage = null;
		LinkedHashMap toDerivedDelegateMap = new LinkedHashMap();
		actionType = formObj.getActionType();

		//   Checking if Pagination is there or not for the statement id.
		if(stmtidInfo!=null && stmtidInfo.containsKey(statementId) && stmtidInfo.get(statementId)!=null){

			idValue = (String)stmtidInfo.get(statementId);
			if(idValue!=null && idValue.length()>0){
				String strIdVal[]=idValue.split(",");
				delegetePgnFlag = strIdVal[0];
				if(delegetePgnFlag !=null &&  delegetePgnFlag.equalsIgnoreCase("y")){
					if(strIdVal.length>1)
						rowsPerPage = strIdVal[1];
					if(rowsPerPage == null ){
						try{
							if(PropertyFileReader.getProperty("RowsPerPage")!=null)
								rowsPerPage = PropertyFileReader.getProperty("RowsPerPage");
						}catch(Exception e){
							EBWLogger.logDebug(this, "rowsperpage not configured in access or in the ebw.properties : "+e);
						}
					}
					if(rowsPerPage!=null && rowsPerPage.length()>0){


						formObj.setRowsPerPage(rowsPerPage);

						toObj.setRowsinapage(Double.valueOf(rowsPerPage));
					}
				}else
					delegetePgnFlag="n";

			}
		}

		//Setting the actiontype in the initial condition if pgn is true 
		try{
			if(delegetePgnFlag.equalsIgnoreCase("y")){

				if(rowsPerPage!=null && (actionType == null || actionType.equalsIgnoreCase("list") || actionType.length()<0) || actionType.equalsIgnoreCase("")){

					formObj.setPaginationIndex("1");
					actionType="List";

				}else if(PropertyFileReader.getProperty("RowsPerPage")!=null && (actionType == null || actionType.equalsIgnoreCase("list") || actionType.length()<0) || actionType.equalsIgnoreCase("")){
					formObj.setPaginationIndex("1");
					actionType="List";
				}
				else
					actionType = formObj.getActionType();
				toDerivedDelegateMap.put("DelegetePgnFlag",delegetePgnFlag);
				toDerivedDelegateMap.put("ActionType",actionType);
				toDerivedDelegateMap.put("RowsPerPage",formObj.getRowsPerPage());

			}else{
				toDerivedDelegateMap.put("DelegetePgnFlag","n");
			}

		}catch(Exception e){
			EBWLogger.logDebug(this, "Error in if(delegetePgnFlag.equalsIgnoreCase(\"y\") loop "+e);
		}
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exiting from performPagination() for n table");
		return toDerivedDelegateMap;
	}

	//For two table
	protected HashMap callExportFn(EbwForm ebwForm,EBWTransferObject objTO,String serviceId,String[] statementId,PaginationDelegateHelper paginationDelegateHelper,String restartKey){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into callExportFn() for n table ");
		HashMap toQueryExMap = new HashMap();
		LinkedHashMap stmtidInfo = new LinkedHashMap();
		String idValue = null;
		String  flag = null;
		HashMap stmtInfoMap = new HashMap();
		HashMap toqueryExecutorMap = new HashMap();
		if(ebwForm.getExportPages()!=null && ebwForm.getExportPages().length()>0 ){
			int pageRows = (Integer.parseInt(ebwForm.getExportPages())) * (Integer.parseInt(ebwForm.getRowsPerPage()))  ;
			String rows = pageRows+"";
			objTO.setRowsinapage(Double.valueOf(rows));
		}
		for(int i=0;i<statementId.length;i++){
			HashMap tempMap = new HashMap();
			try{
				stmtidInfo = getStatementIdInfo(statementId[i]);
			}catch(Exception e){
				EBWLogger.logDebug(this,"statementid info not found for :"+statementId[i]);
			}

			if(EBWLogger.isDebugEnabled()) 
				EBWLogger.logDebug(this, "StatemetnIdInfo["+i+"]:"+stmtidInfo );

			if(stmtidInfo!=null && stmtidInfo.containsKey(statementId[i]) && stmtidInfo.get(statementId[i])!=null){
				String key = statementId[i]+"_PaginationFlag";
				idValue = (String)stmtidInfo.get(statementId[i]);
				if(idValue!=null && idValue.length()>0){
					String strIdVal[]=idValue.split(",");
					delegetePgnFlag = strIdVal[0];
					flag = strIdVal[0];
					if(flag !=null &&  flag.equalsIgnoreCase("y")){

						stmtInfoMap.put(key,"y");
						tempMap = paginationDelegateHelper.orderByStrForQuery(statementId[i],restartKey);
						toqueryExecutorMap.put(statementId[i], tempMap);
					}else
						stmtInfoMap.put(key,"n");
				}
			}
		}

		delegetePgnFlag = "y";
		//toQueryExMap.put("restartkey1",toqueryExecutorMap);
		toQueryExMap.put("pgnInfoMap",stmtInfoMap);
		toQueryExMap.put("toQueryExecutor", toqueryExecutorMap);
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exiting from callexport fn for n table");
		return toQueryExMap;
	}

	/************Code Two table With single TO is over*****************************************************************************/

	/************Two table code Start*****************************************************************************/

//	for two table*****************************
	protected void handlePagination(HashMap objUserSessionObject,EBWTransferObject objTO[],
			EbwForm objForm,String strServiceId,String strStatementId[] ) throws Exception{
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this," Entered into handlePagination for two tables");
		PaginationDelegateHelper paginationDelegateHelper = new PaginationDelegateHelper();
		HashMap toQueryExecutorMp = new HashMap();
		LinkedHashMap stmtidInfo = new LinkedHashMap();
		//stmtidInfo = getStatementIdInfo(strServiceId);
		stmtidInfo.put("getCorporateViewDetailsLIST","y,1");


		if(objForm.getActionType()!=null && 
				objForm.getActionType().length()>0 && 
				objForm.getActionType().equalsIgnoreCase("export")){

			delegetePgnFlag="y";
			//toQueryExecutorMp = callExportFn(objForm,objTO);
		}else{ 
			if(objForm.getActionType() == null || objForm.getActionType().length()<=0 || objForm.getActionType() =="" || objForm.getActionType().equalsIgnoreCase("next") || objForm.getActionType().equalsIgnoreCase("prev")){
				toQueryExecutorMp = checkPgn(objUserSessionObject, strServiceId, strStatementId ,  paginationDelegateHelper, objTO ,objForm);
			}
		}


		/**
		 * To create class array and object array dynamically. We cannot use clsParam = {} because this cannot
		 * be changed dynamically. It is a declarative statement only.
		 */

		if(EBWLogger.isDebugEnabled()) 
			EBWLogger.logDebug(this, "inside base delegate toQueryExecutorMp:" +toQueryExecutorMp);
		ArrayList arrList = new ArrayList();
		arrList.add(strStatementId);
		arrList.add(objTO); // Always first param is TO.
		if(delegetePgnFlag.equalsIgnoreCase("y"))
			arrList.add(toQueryExecutorMp); //To be added specific to pgntn

		arrList.add(new Boolean(false)); // Always last value is boolean for service call.

		objParams = arrList.toArray();
		clsParamTypes = new Class[arrList.size()];
		for(int i=0;i<arrList.size();i++)
		{
			if(arrList.get(i) instanceof com.tcs.ebw.transferobject.EBWTransferObject)
			{
				clsParamTypes[i]=Object.class;
			}
			else if(arrList.get(i) instanceof com.tcs.ebw.transferobject.EBWTransferObject[])
			{
				clsParamTypes[i]=Object[].class;
			}
			else
			{
				clsParamTypes[i]=arrList.get(i).getClass();
			}
		}
		arrList=null; // Not needed anymore
		/** Class and Object param population ends here **/
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exiting from handlePagination for two table");

	}


	protected HashMap checkPgn(HashMap objUserSessionObject, String serviceId,String[] statementId , PaginationDelegateHelper paginationDelegateHelper,EBWTransferObject toObj[],EbwForm formObj){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into chkPgn() two table");
		Object aobj[]= new Object[3];
		Class aclass[]=null;
		HashMap toQueryExecutorMp = new HashMap();
		HashMap pgnInfoMap = new HashMap();
		//getting the restart key from the form bean hiddden variable
		hiddenVarRestartKey = formObj.getRestartKey(); 




		LinkedHashMap stmtidInfo = new LinkedHashMap();
		for(int i=0;i<statementId.length;i++){
			try{
				stmtidInfo = getStatementIdInfo(statementId[i]);
			}catch(Exception e){
				EBWLogger.logDebug(this,"statementid info not found for :"+statementId[i]);
			}
			LinkedHashMap fromBaseDelegateMap = performPagination(stmtidInfo,formObj,toObj[i],serviceId,statementId[i],"twotable");

			if(fromBaseDelegateMap !=null && fromBaseDelegateMap.containsKey("DelegetePgnFlag") && fromBaseDelegateMap.containsKey("ActionType")){
				if(fromBaseDelegateMap.get("DelegetePgnFlag") != null)
					delegetePgnFlag = (String)fromBaseDelegateMap.get("DelegetePgnFlag");
				if(fromBaseDelegateMap.get("ActionType")!=null)
					actionType = (String)fromBaseDelegateMap.get("ActionType");
				if(fromBaseDelegateMap.get("RowsPerPage")!=null)
					rowsPerPage =(String)fromBaseDelegateMap.get("RowsPerPage");
			}

			//calling the preEcecute if pagn = true
			HashMap tempMap = new HashMap();

			if(delegetePgnFlag.equalsIgnoreCase("y") && (rowsPerPage!=null || !((rowsPerPage).equalsIgnoreCase("")))){


				tempMap = paginationDelegateHelper.preExecute(objUserSessionObject,formObj,actionType,serviceId,statementId[i],hiddenVarRestartKey);

				tempMap.put("RowsPerPage",rowsPerPage);
				tempMap.put("PaginationIndex",formObj.getPaginationIndex());
				tempMap.put("PaginationFlag","y");
				pgnInfoMap.put(statementId[i]+"_PaginationFlag","y");

				toQueryExecutorMp.put(statementId[i],tempMap);
			}else{
				tempMap.put("PaginationFlag","n");
				pgnInfoMap.put(statementId[i]+"_PaginationFlag","n");
				toQueryExecutorMp.put(statementId[i],tempMap);
			}

		}
		HashMap toQueryExecutorMp1 = new HashMap();
		toQueryExecutorMp1.put("toQueryExecutor",toQueryExecutorMp);
		toQueryExecutorMp1.put("pgnInfoMap",pgnInfoMap);
		if(EBWLogger.isDebugEnabled()) 
			EBWLogger.logDebug(this, "toQueryExecutorMp for two table :"+toQueryExecutorMp);
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exiting from chkPgn() two table");
		return toQueryExecutorMp1;
	}







	/************Code for Two table is over*****************************************************************************/


	protected void callGetFormFrmSession(EbwForm newForm,EbwForm oldForm){
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into callGetFormFrmSession(): pgn index :"+newForm.getPaginationIndex()+"action:"+newForm.getAction()+"actn type:"+newForm.getActionType()+"restart key:"+newForm.getRestartKey());
		boolean copyPreActionFlag = false; 
		String actnType = null;
		String pgnIndex = null;
		String pgnDirection = null;
		String exportPages = null;
		String rows1 =null;
		String actn = null;
		String restartCols = null;
		//restartCols = newForm.getRestartKey();
		restartCols = newForm.getRestartKey();
		if((newForm.getActionType())!=null && (newForm.getActionType()).length()>0){
			copyPreActionFlag = true;
			actnType = newForm.getActionType();
			pgnIndex = newForm.getPaginationIndex();
			pgnDirection = newForm.getPaginationDirection();
			if((newForm.getActionType()).equalsIgnoreCase("export")){
				exportPages = newForm.getExportPages();

				rows1 = newForm.getRowsPerPage();

			}
			restartCols = newForm.getRestartKey();

		}
		try {
			BeanUtils.copyProperties(newForm,oldForm);
		}catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		newForm.setActionType(actnType);
		newForm.setPaginationIndex(pgnIndex);
		newForm.setPaginationDirection(pgnDirection);
		if((newForm.getActionType())!=null && (newForm.getActionType()).equalsIgnoreCase("export"))
			newForm.setExportPages(exportPages);
		newForm.setRowsPerPage(rows1);
		newForm.setRestartKey(restartCols);
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exiting from callGetFormFrmSession()"); 
	}




	protected boolean isPgnStatement(int index,HashMap pgnInfoMp,String as[]){
		boolean result = false;
		String stmKey = null;
		if(as[index]!=null && as[index].length()>0){
			stmKey = as[index]+"_PaginationFlag";
			if(pgnInfoMp!=null && pgnInfoMp.containsKey(stmKey) && pgnInfoMp.get(stmKey)!=null){
				if((pgnInfoMp.get(stmKey).toString()).equalsIgnoreCase("y"))
					result = true;
			}
		}
		return result;
	}


	protected LinkedHashMap getStatementIdInfo(String serviceId) throws Exception{
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Entered into getStatementIdInfo():- serviceId="+serviceId);
		LinkedHashMap map = new LinkedHashMap();
		EBWAppContext objAppContext1=new EBWAppContext();

		Class clsParamTypes[] = {String.class};
		Object objParams[] = {serviceId};
		objAppContext1.setUserPrincipal(userPrincipal);
		IEBWService service = EBWServiceFactory.create("getSvcConfigInfo",objAppContext1);
		Object obj1 = null;
		try{
			obj1 = service.execute(clsParamTypes,objParams);
			// System.out.println("obj1##"+obj1);

		}catch(Exception e){
			System.out.println("Faild to get the Service Info from Static Tables::");
			e.printStackTrace();   		
			// System.out.println("obj1**"+obj1);
		}
		EBWLogger.logDebug(this," The output object for pagination is "+obj1);
		//LinkedHashMap configMap = new ConfigService().getSvcConfigInfo(serviceId);

		LinkedHashMap configMap = (LinkedHashMap)obj1;

		EBWLogger.logDebug(this," The final output in delegate is "+configMap);

		//	LinkedHashMap configMap = new ConfigService().getSvcConfigInfo(serviceId);
		/*
		 * If Pagination=Y or N not mentioned in servicedefmaster. Then the application 
		 * should take the value from the ebw.properties.
		 * IF it is not mentioned there also , then take the default value 'N'
		 */
		String pgn = (String)configMap.get("ISPAGINATION");
		EBWLogger.logDebug(this,"Paginaion for "+serviceId+" is "+ pgn);
		if(pgn==null || (pgn!=null && pgn.length()<=0)){
			EBWLogger.logDebug(this,"Paginaion for "+serviceId+" is "+ pgn +" so taking the default value 'N' from the ebw.prop");
			String pgnFromProperty = null;
			try{
				if(PropertyFileReader.getProperty("RowsPerPage")!=null)
					pgnFromProperty = PropertyFileReader.getProperty("Pagination");
			}catch(Exception e){
				EBWLogger.logDebug(this, "rowsperpage not configured in access or in the ebw.properties : "+e);
			}
			if(pgnFromProperty==null ||(pgnFromProperty!=null && pgnFromProperty.length()<=0))
				pgnFromProperty="N";
			pgn=pgnFromProperty;
			configMap.put("ISPAGINATION",pgn);
		}

		map.put(serviceId, configMap.get("ISPAGINATION")+","+configMap.get("ROWSPERPAGE"));
		if(EBWLogger.isDebugEnabled())
			EBWLogger.logDebug(this,"Exiting from getStatementIdInfo():- output="+map);
		return map;
	}



	protected boolean isPgnStmtId(String statementId){
		boolean result = false;
		String idValue = null;
		String delePgnFlag = null;
		try{
			LinkedHashMap stmtidInfo = new LinkedHashMap();
			stmtidInfo = getStatementIdInfo(statementId);
			if(stmtidInfo!=null && stmtidInfo.containsKey(statementId) && stmtidInfo.get(statementId)!=null){

				idValue = (String)stmtidInfo.get(statementId);

				if(idValue!=null && idValue.length()>0){
					String strIdVal[]=idValue.split(",");
					//if(strIdVal.length>0)
					delePgnFlag = strIdVal[0];
				}
			}
		}catch (Exception e){
			EBWLogger.logDebug(this,"Statementid Info not found for :"+statementId);
		}
		if(delePgnFlag!=null && delePgnFlag.length()>0){
			if(delePgnFlag.equalsIgnoreCase("y"))
				result=true;
		}

		return result;
	}


}




