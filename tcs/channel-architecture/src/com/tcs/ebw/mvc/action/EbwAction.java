/*
 * Created on Dec 12, 2005
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.mvc.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.tcs.ebw.common.util.ConvertionUtil;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.common.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Iterator;

import javax.security.auth.Subject;

import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.refresh.RefreshOutputGenerator;
import com.tcs.ebw.serverside.jaas.auth.Auth;
import com.tcs.ebw.common.services.TemplateService;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.taglib.DataInterface;
import com.tcs.ebw.taglib.EbwTableHelper;
import com.tcs.ebw.taglib.TableColAttrObj;
import com.tcs.ebw.taglib.ReorderTableHelper;

/**
 * EbwAction is extending from Struts Action class.
 * This is the super class for all generated Action classes.
 * performTask method is used for actual Task Execution. 
 * execute method will have the gateway checkings based on the
 * check it will forward to performTask method.  
 * 
 * @author TCS
 */
public abstract class EbwAction extends Action {
    public static final String USER_OBJ = "USER_OBJ";

    /**
     * EbwAction Constructor 
     */
    public EbwAction() {
        super();
    }
    
	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
	   request.setAttribute("PATH",mapping.getPath());
	     request.setAttribute("MODULECONFIG",mapping.getModuleConfig());
	     
	     try{
	    	 if(PropertyFileReader.getProperty("IsRefreshCheck").equals("yes")){
	    	     RefreshOutputGenerator ref=new RefreshOutputGenerator();
	    		 ArrayList arrMviews=ref.fetchRefreshMviews();
	    	     request.getSession().setAttribute("RefreshList", arrMviews);
	    	     EBWLogger.logDebug("EBWAction","The of the MViews ArrayList is :"+request.getSession().getAttribute("RefreshList"));
	    	 }
	     }catch(MissingResourceException mre){
	    	 EBWLogger.trace(this,"Mview Refresh Check is not present");
	     }
	     
	     if(form instanceof EbwForm){
	    	 EbwForm ebwForm1=(EbwForm)form;
	     if(mapping.getModuleConfig().getPrefix().length()>0)
	    	 ebwForm1.setModuleName(mapping.getModuleConfig().getPrefix().substring(1,mapping.getModuleConfig().getPrefix().length()));	     
	     }
	     String action = null;
	     EbwForm returnForm =null;
	     EbwForm requestForm=null;
	     
        try 
        {
            if(form instanceof EbwForm)
            {
				if(((EbwForm)form).getReorderCols()!=null && ((EbwForm)form).getReorderCols().length()>0)
					callReorder(((EbwForm)form),request);
                EbwForm ebwForm = (EbwForm)form;
                String className = ebwForm.getClass().getName();
                Class classLoad = Class.forName(className);
                String formName = className.substring(className.lastIndexOf(".") + 1, className.length());
                Method method = classLoad.getMethod("get" + StringUtil.initCaps("action"), null);
                action = (String)method.invoke(ebwForm, null);
                UserPrincipal objUserPrincipal = getUserPrincipal(request);
                
               
                if(action != null && action.equals("btnSaveTemp"))
                {
                    requestForm = (EbwForm)request.getAttribute(formName);
                    requestForm.setTemplateInfo("templateName=" + requestForm.getTemplateInfo() + "#publicFlag=N");
                    EBWLogger.logDebug(this,"(String)request.getSession().getAttribute(\"action\") "+(String)request.getSession().getAttribute("action"));
                    requestForm.setAction((String)request.getSession().getAttribute("action"));
                    EbwForm sessionForm = (EbwForm)request.getSession().getAttribute(formName);
                    
                    /*if(request.getSession().getAttribute(formName) != null)
                    {
                        EbwForm sessionForm = (EbwForm)request.getSession().getAttribute(formName);
                        sessionForm.setTemplateInfo(requestForm.getTemplateInfo());
                        sessionForm.setScreenName(requestForm.getScreenName());
                        saveTemplate(sessionForm, objUserPrincipal);
                        request.setAttribute(formName, sessionForm);
                    } else
                    {*/
                        saveTemplate(requestForm, objUserPrincipal);
                        request.setAttribute(formName, requestForm);
                        //request.setAttribute(formName, sessionForm);
                    //}
                } else
                if(action != null && action.equals("btnOpenTemp"))
                {	
                    EBWLogger.logDebug(this,"btnOpenTemp in EbwAction class");
                    returnForm = openTemplate(ebwForm, objUserPrincipal);
                    request.setAttribute(formName, returnForm);
                }
            }
        }
        catch(Throwable objThrow)
        {
            objThrow.printStackTrace();
            saveErrorMessage(request, objThrow);
        }
        if(action != null && (action.equals("btnSaveTemp") || action.equals("btnOpenTemp"))){
            EBWLogger.logDebug(this,"mapping.findForward(\"input\"): " +mapping.findForward("input"));
            
            if(action.equals("btnOpenTemp"))
                return performTask(mapping, returnForm, request, response);
            else
                //return mapping.findForward("input");    
            	return performTask(mapping, requestForm, request, response);
        }
        else
            return performTask(mapping, form, request, response);

	}
	
	/**
	 * This method returns UserPrincipal. This user principal
	 * object is passed to the delegate and the delegate hook.
	 *  
	 * @param request
	 * @return
	 */
	public UserPrincipal getUserPrincipal (HttpServletRequest request) {
	    UserPrincipal objUserPrincipal = null;
	    try {
		    HttpSession session = request.getSession();
			Set objSet = ((Subject)session.getAttribute(Auth.SUBJECT_SESSION_KEY)).getPrincipals();
			Iterator objIterator= objSet.iterator();
			objUserPrincipal = (UserPrincipal) objIterator.next();
			objUserPrincipal.setIpAddr(request.getRemoteAddr());
	    } catch (Exception exc) {
	        System.out.println ("EbwAction:getUserPrincipal - " + exc.getMessage());
	    }
	    return objUserPrincipal;
	}
	
	/**
	 * This method returns User defined object from the session.
	 * If it is not in the session it will add and returns that object.
	 * 
	 * @param request
	 * @return
	 */
	/*public java.util.HashMap getUserObject (HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    java.util.HashMap objUserObject = (java.util.HashMap) session.getAttribute(USER_OBJ);
	    if (objUserObject == null) {
	        objUserObject = new java.util.HashMap();
	        objUserObject.put("sessionid",session.getId());
	        session.setAttribute(USER_OBJ, objUserObject);
	    } 
	    EBWLogger.trace(this,"session id:"+objUserObject);
        System.out.println ("action obj : " + objUserObject.getClass());
	    return objUserObject;
	}*/


	public java.util.HashMap getUserObject (HttpServletRequest request) {
	    HttpSession session = request.getSession();
	    java.util.HashMap objUserObject = (java.util.HashMap) session.getAttribute(USER_OBJ);
	    if (objUserObject == null) {
	        objUserObject = new java.util.HashMap();
	        objUserObject.put("sessionid",session.getId());
	        objUserObject.put("isDiffScreen","false");
	        session.setAttribute(USER_OBJ, objUserObject);
	    } 
	    EBWLogger.trace(this,"session id:"+objUserObject);
        System.out.println ("action obj : " + objUserObject.getClass());
	    return objUserObject;
	}

	/**
	 * performTask method is an abstract method. This method is called in 
	 * execute method after initial the validations.
	 * 
	 * @param mapping	Action mapping object. It has all struts-config mapping
	 * @param form		Action Form object - EbwForm is passed as a parameter.
	 * @param request	Http Request is passed from execute method
	 * @param response	Http Response is passed from execute Method
	 * @return
	 * @throws Exception
	 */
	public abstract ActionForward performTask (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) 
			throws Exception;
    
	/**
	 * This method is used for save the error message which is
	 * generated by serverside classes.
	 * 
	 * @param request
	 * @param objThrowable
	 */
	protected void saveErrorMessage(HttpServletRequest request, Throwable objThrowable) {
		EBWLogger.logDebug(this,"Inside saveErrorMessage" + objThrowable.getMessage());
	    ActionMessages objMsgs = new ActionMessages();
	    String errMsg = objThrowable.getMessage();
	    ActionMessage objMsg ;
	    if (errMsg!=null && errMsg.startsWith("errors.")) {
	        objMsg = new ActionMessage(objThrowable.getMessage());
	        objMsgs.add("error", objMsg);
	    } else {
	    	if(objThrowable.getMessage()!=null)
	    		objMsg = new ActionMessage("Error : " + objThrowable.getMessage(), false);
	    	else
	    	{
	    		if(objThrowable.getCause()!=null)
	    		  objMsg = new ActionMessage("Error : " + objThrowable.getCause().getMessage(), false);
	    		else
	    		  objMsg = new ActionMessage("Error : " + "Null Pointer Exception", false);
	    	}
	        objMsgs.add("error", objMsg);
	    }
	    
		if (objThrowable instanceof InvocationTargetException &&  objThrowable.getCause() != null) {
		    objMsg = new ActionMessage("Cause : " + objThrowable.getCause().getMessage().replaceAll("\n", ""), false);
			objMsgs.add("error", objMsg);
		}
		saveErrors(request, objMsgs);
	}
	
	/**
	 * This method is used for creating breadcrumb and putting into session.
	 * 
	 * @param request
	 * @param objForm
	 * @param forwardTo
	 */
	protected void addToBreadCrumb(HttpServletRequest request, EbwForm objForm, String forwardTo) {
		HttpSession session = request.getSession();
		String strBreadCrumb = (String) session.getAttribute("BreadCrumb");
	  
		if (strBreadCrumb != null && strBreadCrumb.length() > 0 && 
		        strBreadCrumb.indexOf(forwardTo) > -1) {
			strBreadCrumb = strBreadCrumb.substring(0, strBreadCrumb.indexOf(forwardTo));
		}

		if (strBreadCrumb==null || strBreadCrumb.length() == 0) {
			session.setAttribute("BreadCrumb", forwardTo);
		} else {
		    if (!forwardTo.equals("input")) {
			    if (strBreadCrumb.trim().endsWith(">")) {
			        session.setAttribute("BreadCrumb", strBreadCrumb + forwardTo);
			    } else {
			        session.setAttribute("BreadCrumb", strBreadCrumb + ">" + forwardTo);
				}
				
		    }
     	}
	}
	
		
	 public EbwForm callReorderFromExport(EbwForm objForm,HttpServletRequest request){
		 if(EBWLogger.isInfoEnabled())
				EBWLogger.trace(this,"Entered into  callReorderFromExport() in base action");
		
	    	String columnsBeforeSuffle=null;
	    	ReorderTableHelper reorderTblHelper = new ReorderTableHelper();
	    	String tableName = null;
	    	String reorderColsStr =null;
	    	if(objForm.getReorderCols()!=null && objForm.getReorderCols().length()>0){
	    		reorderColsStr = objForm.getReorderCols();
	    		String reorderStrArr[] = reorderColsStr.split(":");
	    		if(reorderStrArr!=null && reorderStrArr.length>0)
	    			tableName = reorderStrArr[0];
	    	}
	    	
	    	if(reorderColsStr !=null && reorderColsStr.length()>0 && tableName!=null && tableName.length()>0){
		    	try{
		   		String formname=objForm.getClass().getName();
		    	if(EBWLogger.isDebugEnabled()){
					EBWLogger.logDebug(this,"Reordering is there for the table "+tableName+ " and formname is :"+formname);
		    	}
		    	Class formClass=Class.forName(formname);
		        String s2 = formname.substring(formname.lastIndexOf(".") + 1, formname.length());
		        
		        Method method = formClass.getMethod("get" + StringUtil.initCaps(tableName+"Collection"), null);
		        DataInterface formObj = (DataInterface)method.invoke(objForm, null);
		       
		        ArrayList tableHeader=null;
				tableHeader = formObj.getRowHeader();
		    	ArrayList data= (ArrayList)formObj.getData();
				ArrayList colattrobj = ((DataInterface) formObj).getColAttrObjs();
				LinkedHashMap dataValue=null;
				dataValue = (LinkedHashMap) tableHeader.get(0);
				
				ResourceBundle formRB = ResourceBundle.getBundle(ResourceBundle.getBundle("ApplicationConfig").getString("message-resources"));
				columnsBeforeSuffle = ReorderTableHelper.getColumnHeaderAsString(dataValue,formRB,s2,tableName);
				if(EBWLogger.isInfoEnabled())
					EBWLogger.logDebug(this,"columnsBeforeSuffle inside EbwAction:"+columnsBeforeSuffle);
					
				HttpSession sessionObj = request.getSession();
					EBWLogger.logDebug(this,"sessionObj getID:"+sessionObj.getId());
					String key1 = sessionObj.getId()+"_"+tableName;
					HashMap userObject = new HashMap();
					String reOrderColsFrmSession = null;
					if(sessionObj.getAttribute("USER_OBJ")!=null){
						userObject = (HashMap)sessionObj.getAttribute("USER_OBJ");
						reOrderColsFrmSession = (String)userObject.get(key1);
					}		
					if(EBWLogger.isDebugEnabled())
						EBWLogger.logDebug(this,"reOrderColsFrmSession inside EbwAction:"+reOrderColsFrmSession);
					if(reOrderColsFrmSession!=null && reOrderColsFrmSession.length()>0){
						String reorderCols = reOrderColsFrmSession;
						
						
						System.out.println("Reorder cols :"+reorderCols);
						LinkedHashMap headerShufffleMap = ReorderTableHelper.shuffleHeader(reorderCols,dataValue,formRB,s2,tableName);
						
						ArrayList headerShuffleList = new ArrayList();
						headerShuffleList.add(headerShufffleMap);
						//header shuffle over . Now Data shuffle.
						ArrayList dataListArr = (ArrayList)formObj.getData();
						ArrayList reorderdData = new ArrayList();
						Set setHeader = headerShufffleMap.keySet();
						Iterator it = setHeader.iterator();
						ArrayList firstRow = new ArrayList();
						if(it.hasNext())
							firstRow.add(it.next());
						reorderdData.add(firstRow);
						for(int i=0;i<dataListArr.size();i++){
							reorderdData.add(ReorderTableHelper.shuffleData(reorderCols,dataValue,(ArrayList)dataListArr.get(i),formRB,s2,tableName));
						}
						//System.out.println("output Shuffled data:"+reorderdData);
						//Shuffling colattrObj arraylist
						ArrayList colattrobjForShuffle = ((DataInterface) formObj).getColAttrObjs();
					
						String displayFlagForShuffleColAttr = null;
						if(colattrobjForShuffle!=null && colattrobjForShuffle.size()>0){
							TableColAttrObj tablecolattrobj = (TableColAttrObj) colattrobj.get(0);
							String firstTdRadChk = tablecolattrobj.getColName();
							if(firstTdRadChk.endsWith("SelectId"))
								displayFlagForShuffleColAttr = "yes";
						}
						//System.out.println("displayFlagForShuffleColAttr:"+displayFlagForShuffleColAttr);
						
						ArrayList colAttrObjShuffledArrList = ReorderTableHelper.colAttrObjShuffle(colattrobjForShuffle,headerShufffleMap,displayFlagForShuffleColAttr);
						
						//System.out.println("colAttrObjShuffledArrList.size :"+colAttrObjShuffledArrList.size());
						//System.out.println("colAttrObjShuffledArrList :"+colAttrObjShuffledArrList);
						
						
						String posDate=null;
						String posDateTime=null;
						String posDoubleDecimal = "";
						posDate = "";
						posDateTime = "";
						formObj.setColAttrObjs(colAttrObjShuffledArrList);
						colattrobj = colAttrObjShuffledArrList;
						if(reorderdData!=null && reorderdData.size()>1){
							boolean checkRadio = false;
							//for(int i=0;i<colattrobjForShuffle.size();i++){
							for(int i=0;i<colAttrObjShuffledArrList.size();i++){
								//System.out.println(" i is :"+i);
								TableColAttrObj tablecolattrobj = (TableColAttrObj) colattrobj.get(i);
								//System.out.println("colname is :"+tablecolattrobj.getColName());
								if(tablecolattrobj.getColName().endsWith("SelectId")){
									checkRadio = true;
								}else{
									String dataType = tablecolattrobj.getDataType();
									if(dataType!=null && dataType.length()>0){
										if( dataType.equalsIgnoreCase("date")){
											if(checkRadio){
												int z=i-1;
												posDate = posDate+z+":";
											}
											else
												posDate = posDate+i+":";
										}else if( dataType.equalsIgnoreCase("datetime")){
											if(checkRadio){
												int z=i-1;
												posDateTime = posDateTime + z +":";
											}else
												posDateTime = posDateTime + i +":";
										}else if( dataType.equalsIgnoreCase("Double") || dataType.equalsIgnoreCase("BigDecimal")){
											if(checkRadio){
												int z=i-1;
												posDoubleDecimal = posDoubleDecimal + z +":";
											}else
												posDoubleDecimal = posDoubleDecimal + i +":";
										}
										
									}
								}
							}
							
							posDate = reorderTblHelper.removeLstStr(posDate, ":");
							posDateTime = reorderTblHelper.removeLstStr(posDateTime, ":");
							posDoubleDecimal = reorderTblHelper.removeLstStr(posDoubleDecimal, ":");
							
							
							if(posDateTime !=null && posDateTime.length()>0){
								if(reorderdData !=null && reorderdData.size()>1){
									String[] posDateTimeArr = posDateTime.split(":");
									
										for(int dataArr=1;dataArr<reorderdData.size();dataArr++){
											ArrayList dataList = (ArrayList)reorderdData.get(dataArr);
												for(int i=0;i<posDateTimeArr.length;i++){
													String value = (String)dataList.get(Integer.parseInt(posDateTimeArr[i]));
													//System.out.println("value :"+value);
													String valCorrect = null;
													if(value!=null && value.length()>0){
														//if(value.indexOf(" ")>-1)
														//	valCorrect = ConvertionUtil.convertToAppDateTimeStr(value);
														//else
															valCorrect = ConvertionUtil.convertToAppDateTimeStr(value);
															String app_datetime_format = PropertyFileReader.getProperty("APP_DATETIME_FORMAT");
															if(app_datetime_format!=null && app_datetime_format.length()>0)
															{
																if(app_datetime_format.indexOf("HH:mm:ss")>-1)
																	valCorrect =valCorrect+"&nbsp;";
															}
															
													}
													//System.out.println("valCorrect :"+valCorrect);
													dataList.set(Integer.parseInt(posDateTimeArr[i]), valCorrect);
												}
												reorderdData.set(dataArr, dataList);
											}
										}
								}
							
							if(posDate !=null && posDate.length()>0){
								//System.out.println("inside changing date format");
								if(reorderdData !=null && reorderdData.size()>1){
									String[] posArr = posDate.split(":");
									
										for(int dataArr=1;dataArr<reorderdData.size();dataArr++){
											ArrayList dataList = (ArrayList)reorderdData.get(dataArr);
											//System.out.println("Arraylist ["+dataArr+"] :"+dataList);
												for(int i=0;i<posArr.length;i++){
													String value = (String)dataList.get(Integer.parseInt(posArr[i]));
													//System.out.println("value :"+value);
													String valCorrect = null;
													if(value!=null && value.length()>0){
														if(value.indexOf(" ")>-1)
															valCorrect = ConvertionUtil.convertToAppDateStr(value.substring(0,value.indexOf(" ")));
														else
															valCorrect = ConvertionUtil.convertToAppDateStr(value);
													}
													//System.out.println("valCorrect :"+valCorrect);
													dataList.set(Integer.parseInt(posArr[i]), valCorrect);
												}
												reorderdData.set(dataArr, dataList);
											}
										}
								}
							
							if(posDoubleDecimal !=null && posDoubleDecimal.length()>0){
								System.out.println("inside changing posDoubleDecimal format");
								if(reorderdData !=null && reorderdData.size()>1){
									String[] posArr = posDoubleDecimal.split(":");
									
										for(int dataArr=1;dataArr<reorderdData.size();dataArr++){
											ArrayList dataList = (ArrayList)reorderdData.get(dataArr);
											System.out.println("For bigDeci Arraylist ["+dataArr+"] :"+dataList);
												for(int i=0;i<posArr.length;i++){
													String value = (String)dataList.get(Integer.parseInt(posArr[i]));
													System.out.println(" for bigdeci value :"+value);
													String valCorrect = null;
													if(value!=null && value.length()>0){
														int dec = 4;
														char sep = ',';
														char cDec = '.';
														int informat = 3;
														valCorrect =  ConvertionUtil.ConvertToUserNumberFormat(value,dec,sep,cDec,informat);
															
														
													}
													System.out.println("for bigdeci valCorrect :"+valCorrect);
													dataList.set(Integer.parseInt(posArr[i]), valCorrect);
												}
												reorderdData.set(dataArr, dataList);
											}
										}
								}
							
							//System.out.println("reorderdData after date set:"+reorderdData);
						}	
						
						
						formObj.setData(reorderdData);
						
						data= (ArrayList)formObj.getData(); 
						formObj.setRowHeader(headerShuffleList);
						tableHeader = formObj.getRowHeader();
						if(EBWLogger.isDebugEnabled()){
							EBWLogger.logDebug(this,"data :"+data);
							EBWLogger.logDebug(this,"tableHeader :"+tableHeader.get(0));
						}
						// Invoking a set Method for the Table COllection and setting the formObj 
						
						Class formClass1=Class.forName(formname);
						Class[] argType={DataInterface.class};
						Object objParams[]={formObj};
						Method method1 = formClass1.getMethod("set" + StringUtil.initCaps(tableName+"Collection"), argType);
						method1.invoke(objForm, objParams);
							
				    }
				}catch(Exception e){
					System.out.println("This is the undefined: "+e);
				}
				if(EBWLogger.isInfoEnabled())
					EBWLogger.trace(this,"Exiting from callReorderFromExport() in base action ");
	    	}
			return objForm;
	}
	
	public void saveTemplate(EbwForm objForm, UserPrincipal objUserPrincipal) throws Exception {
		TemplateService tempService=new TemplateService();
		String templateInfo= objForm.getTemplateInfo();
		tempService.saveAsTemplate(objUserPrincipal,objForm,templateInfo);
	}
	
	public EbwForm openTemplate (EbwForm objForm, UserPrincipal objUserPrincipal) throws Exception {
		String status[];
		TemplateService tempService = new TemplateService();
		String selectedTempName= objForm.getTemplateNameSelected();
		return tempService.openTemplate(selectedTempName, objForm);
	}

	protected void callReorder(EbwForm objCorporateActionViewForm,HttpServletRequest request){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into callReorder() in base action");
		String reorderCol = objCorporateActionViewForm.getReorderCols();
		EBWLogger.trace(this,"reorderColumns :"+reorderCol);
		//System.out.println("reorderColumns :"+reorderCol);
		HashMap usrObject = new HashMap();
		usrObject = getUserObject(request);
		String sessionId = null;
		if(usrObject != null && usrObject.get("sessionid")!=null){
			sessionId = (String)usrObject.get("sessionid");
			if(reorderCol!=null && reorderCol.length()>0){
				String[] reorderStr= reorderCol.split(":");
				if(reorderStr.length==2){
					String key=sessionId+"_"+reorderStr[0];
					usrObject.put(key,reorderStr[1]);
				}
				
			}
		}
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exiting from callReorder() in base action");
	}

		
    /**
     * @param msg
     */
    public void log(String msg) {
        System.out.println (msg);
    }
    
    
	protected EbwForm setFormForExport(EbwForm formForExport,HttpServletRequest request){
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Entered into setFormForExport() in base action");
    	request.setAttribute("CorporateActionViewForm", formForExport);
		request.getSession().setAttribute("CorporateActionViewForm", formForExport);
		formForExport.setAction(formForExport.getPrevAction());
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exiting from setFormForExport() in base action");
		return formForExport;
    }
	
	 protected EbwForm setFormForExport(EbwForm formForExport,HttpServletRequest request,String formName){
		 if(EBWLogger.isInfoEnabled())
				EBWLogger.trace(this,"Entered into setFormForExport() in base action");
		String className = formForExport.getClass().getName();
		formName = className.substring(className.lastIndexOf(".")+".".length(), className.length()); 
    	request.setAttribute(formName, formForExport);
		request.getSession().setAttribute(formName, formForExport);
		formForExport.setAction(formForExport.getPrevAction());
		if(EBWLogger.isInfoEnabled())
			EBWLogger.trace(this,"Exiting from setFormForExport() in base action");
		return formForExport;
    }

}
