/*
 * Created on Tue Mar 24 11:49:46 IST 2009
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
public class ExternalPreConfirmAction extends EbwAction {
	/**
	 * Logger for this class
	 */
	private static final Logger performanceLogger = Logger.getLogger("MSPerformanceLogger"); //$NON-NLS-1$

	/**
	 * performTask method of ExternalPreConfirmAction class. 
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
			performanceLogger.info("IP Address : " + request.getRemoteAddr() + " - Thread Id : " + Thread.currentThread().getId() + " - Session Id : " + sessionId + " - " + this.getClass().getName() + " - performTask(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - Start"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		} 

		String forwardTo = "input";

		String ajaxCall =null;

		String fieldName = null;

		try {
			EBWLogger.trace(this, "Starting performTask()"); 
			ExternalPreConfirmForm objExternalPreConfirmForm = (ExternalPreConfirmForm) form; 

			UserPrincipal objUserPrincipal = getUserPrincipal(request);

			PaymentsActionHook objPaymentsActionHook = new PaymentsActionHook();
			objPaymentsActionHook.preExternalPreConfirm (objExternalPreConfirmForm, request, getUserObject(request), objUserPrincipal);

			String action = objExternalPreConfirmForm.getAction();
			String state = objExternalPreConfirmForm.getState();
			ajaxCall = objExternalPreConfirmForm.getAjaxCall();
			fieldName = objExternalPreConfirmForm.getFieldName();
			log ("ExternalPreConfirmForm Action : " + action);
			log ("ExternalPreConfirmForm State  : " + state);

			EbwForm objPostActionForm = null;

			String actionType = objExternalPreConfirmForm.getActionType();
			if(actionType!=null && actionType.length()>0 && actionType.equalsIgnoreCase("export")){
				action = "export";
				state = objExternalPreConfirmForm.getPrevState();
			}
			request.setAttribute("ExternalPreConfirmForm", objExternalPreConfirmForm);
			if (action.equals("confirmbut")) {
				ExternalPreConfirmDelegate objExternalPreConfirmDelegate = new ExternalPreConfirmDelegate(objExternalPreConfirmForm, getUserObject(request), objUserPrincipal);
				ExternalConfirmForm objExternalConfirmFrm = null;
				if (state.equals("ExternalPreConfirm_INIT")) {
					objExternalConfirmFrm = objExternalPreConfirmDelegate.externalPreConfirm_INIT_confirmbut();
					objPostActionForm=(EbwForm)objExternalConfirmFrm;
				}
				else if (state.equals("ExternalPreConfirm_Edit")) {
					objExternalConfirmFrm = objExternalPreConfirmDelegate.externalPreConfirm_Edit_confirmbut();
					objPostActionForm=(EbwForm)objExternalConfirmFrm;
				}
				if(getUserObject(request).get("ExternalConfirmForm")!=null)
					objExternalConfirmFrm.copyCollections(((ExternalConfirmForm)getUserObject(request).get("ExternalConfirmForm")));
				request.setAttribute("ExternalConfirmForm", objExternalConfirmFrm);
				forwardTo = "ExternalConfirm";
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

			forwardTo = objPaymentsActionHook.postExternalPreConfirm (action, state,objExternalPreConfirmForm,objPostActionForm, request, getUserObject(request), objUserPrincipal, forwardTo);

			EBWLogger.trace(this, "Finished performTask()"); 
		} catch (Throwable objThrow) {

			objThrow.printStackTrace();
			if(objThrow.toString().substring(objThrow.toString().indexOf(":")+1).trim().equalsIgnoreCase("2")){
				forwardTo="errorpage";
				request.setAttribute("error",objThrow.getCause().getMessage());
			}else{
				saveErrorMessage(request, objThrow);
				forwardTo="input";

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
			if(fieldName.equals("externaltransfercredit")){
				param = new HashMap();
				param.put("displayType","SelectRadio");
				param.put("formname","ExternalPreConfirmForm");
				param.put("name","externaltransfercredit");
				com.tcs.ebw.taglib.EbwTable ebwTableAjax = new com.tcs.ebw.taglib.EbwTable();
				try{
					if(param!=null)
						responseHTML = ebwTableAjax.getTableThruAjax(param, request.getSession(), request.getAttribute("ExternalPreConfirmForm"));
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