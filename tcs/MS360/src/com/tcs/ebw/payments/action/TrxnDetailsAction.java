/*
 * Created on Mon Jun 01 15:51:38 IST 2009
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.payments.action;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.tcs.ebw.payments.formbean.*;
import com.tcs.ebw.payments.businessdelegate.*;
import com.tcs.ebw.mvc.action.EbwAction;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.mvc.validator.EbwForm;
import java.util.HashMap;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

/**
 * @author : 224703
 * **************   Revision History ************************
 * Modified-By |  Modified-Date |  Project-Phase  | Changes
 *
 * **********************************************************
 */
public class TrxnDetailsAction extends EbwAction {
	/**
	 * Logger for this class
	 */
	private static final Logger performanceLogger = Logger.getLogger("MSPerformanceLogger"); //$NON-NLS-1$

	/**
	 * performTask method of TrxnDetailsAction class. 
	 */ 
	public ActionForward performTask (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		long startTime = System.currentTimeMillis();		
		long elapsedTime=0; 
		String sessionId ="";
		if (request.getSession()!=null){
			sessionId=request.getSession().getId();
		}	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : " + request.getRemoteAddr() + " - Thread Id : " + Thread.currentThread().getId() + " - Session Id : " + sessionId + " - " + this.getClass().getName() + " - performTask(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - Start" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		} 

		String forwardTo = "input";

		String ajaxCall =null;

		String fieldName = null;

		try {
			EBWLogger.trace(this, "Starting performTask()"); 
			TrxnDetailsForm objTrxnDetailsForm = (TrxnDetailsForm) form; 

			UserPrincipal objUserPrincipal = getUserPrincipal(request);

			String action = objTrxnDetailsForm.getAction();
			String state = objTrxnDetailsForm.getState();
			ajaxCall = objTrxnDetailsForm.getAjaxCall();
			fieldName = objTrxnDetailsForm.getFieldName();
			log ("TrxnDetailsForm Action : " + action);
			log ("TrxnDetailsForm State  : " + state);

			EbwForm objPostActionForm = null;

			String actionType = objTrxnDetailsForm.getActionType();
			if(actionType!=null && actionType.length()>0 && actionType.equalsIgnoreCase("export")){
				action = "export";
				state = objTrxnDetailsForm.getPrevState();
			}
			request.setAttribute("TrxnDetailsForm", objTrxnDetailsForm);
			if (action.equals("INIT")) {
				TrxnDetailsDelegate objTrxnDetailsDelegate = new TrxnDetailsDelegate(objTrxnDetailsForm, getUserObject(request), objUserPrincipal);
				TrxnDetailsForm objTrxnDetailsFrm = null;
				if (state.equals("TrxnDetails_INIT")) {
					objTrxnDetailsFrm = objTrxnDetailsDelegate.trxnDetails_INIT_iNIT();
					objPostActionForm=(EbwForm)objTrxnDetailsFrm;
				}
				else if (state.equals("TrxnDetails_View")) {
					objTrxnDetailsFrm = objTrxnDetailsDelegate.trxnDetails_View_iNIT();
					objPostActionForm=(EbwForm)objTrxnDetailsFrm;
				}
				if(getUserObject(request).get("TrxnDetailsForm")!=null)
					objTrxnDetailsFrm.copyCollections(((TrxnDetailsForm)getUserObject(request).get("TrxnDetailsForm")));
				request.setAttribute("TrxnDetailsForm", objTrxnDetailsFrm);
				forwardTo = "TrxnDetails";
			} else {
				//Action Not found Error Message. 
				if (!action.equals("INIT")) 
					throw new Exception("Action is not configured.");
			}

			String actnUrlPath = mapping.getPath();
			String actnUrl = actnUrlPath.substring(1,actnUrlPath.length());
			String addToSession = actnUrl+":"+action+":"+state;
			HashMap userMap = new HashMap();
			if((getUserObject(request)).get("sessionid") != null ){
				String key = (getUserObject(request)).get("sessionid")+"_PrevActn";
				(request.getSession()).setAttribute(key,addToSession);
			}
			EBWLogger.trace(this, "Finished performTask()"); 
		} catch (Throwable objThrow) {
			objThrow.printStackTrace();
			if(objThrow.toString().substring(objThrow.toString().indexOf(":")+1).trim().equalsIgnoreCase("2")){
				forwardTo="errorpage";
				request.setAttribute("error",objThrow.getCause().getMessage());
			}else{
				forwardTo="input";
				saveErrorMessage(request, objThrow);
			}
		}
		if(ajaxCall!=null && ajaxCall.length()>0 && ajaxCall.equalsIgnoreCase("yes")){
			forwardTo=null;
		}
		if(forwardTo != null) {
			ActionForward returnActionForward = (mapping.findForward(forwardTo));
			elapsedTime = System.currentTimeMillis() - startTime;
			if (performanceLogger.isInfoEnabled()) {
				performanceLogger.info("IP Address : " + request.getRemoteAddr() + " - Thread Id : " + Thread.currentThread().getId() + " - Session Id : " + sessionId + " - " + this.getClass().getName() + " - performTask(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}
			return returnActionForward;
		} else {
			 HashMap param=null;
			 StringBuffer responseHTML=null;
			if(fieldName.equals("reasonCodes")){
				param = new HashMap();
				param.put("reordering","no");
				param.put("noDataFoundMsg","true");
				param.put("tableLevelCSS","dataReasonCodes");
				param.put("divClass","autodivReasonCodes");
				param.put("displayType","SelectRadio");
				param.put("formname","TrxnDetailsForm");
				param.put("name","reasonCodes");
				com.tcs.ebw.taglib.EbwTable ebwTableAjax = new com.tcs.ebw.taglib.EbwTable();
				try{
					if(param!=null)
						responseHTML = ebwTableAjax.getTableThruAjax(param, request.getSession(), request.getAttribute("TrxnDetailsForm"));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(fieldName.equals("actionDetails")){
				param = new HashMap();
				param.put("reordering","no");
				param.put("noDataFoundMsg","true");
				param.put("tableLevelCSS","dataActionsList");
				param.put("divClass","autodivActionsList");
				param.put("displayType","SelectRadio");
				param.put("formname","TrxnDetailsForm");
				param.put("name","actionDetails");
				com.tcs.ebw.taglib.EbwTable ebwTableAjax = new com.tcs.ebw.taglib.EbwTable();
				try{
					if(param!=null)
						responseHTML = ebwTableAjax.getTableThruAjax(param, request.getSession(), request.getAttribute("TrxnDetailsForm"));
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			response.setContentType("text/html");
			java.io.PrintWriter out = response.getWriter();
			out.println(responseHTML);
			out.flush();

			elapsedTime = System.currentTimeMillis() - startTime;
			if (performanceLogger.isInfoEnabled()) {
				performanceLogger.info("IP Address : " + request.getRemoteAddr() + " - Thread Id : " + Thread.currentThread().getId() + " - Session Id : " + sessionId + " - " + this.getClass().getName() + " - performTask(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			}
			return null;
		}
	}
}