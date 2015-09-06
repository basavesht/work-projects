/*
 * Created on Thu Apr 02 09:00:29 IST 2009
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
public class ExternalTransferAction extends EbwAction {
	/**
	 * Logger for this class
	 */
	private static final Logger performanceLogger = Logger.getLogger("MSPerformanceLogger"); //$NON-NLS-1$

	/**
	 * performTask method of ExternalTransferAction class. 
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
			ExternalTransferForm objExternalTransferForm = (ExternalTransferForm) form; 

			UserPrincipal objUserPrincipal = getUserPrincipal(request);

			PaymentsActionHook objPaymentsActionHook = new PaymentsActionHook();
			objPaymentsActionHook.preExternalTransfer (objExternalTransferForm, request, getUserObject(request), objUserPrincipal);

			String action = objExternalTransferForm.getAction();
			String state = objExternalTransferForm.getState();
			ajaxCall = objExternalTransferForm.getAjaxCall();
			fieldName = objExternalTransferForm.getFieldName();
			log ("ExternalTransferForm Action : " + action);
			log ("ExternalTransferForm State  : " + state);

			EbwForm objPostActionForm = null;

			String actionType = objExternalTransferForm.getActionType();
			if(actionType!=null && actionType.length()>0 && actionType.equalsIgnoreCase("export")){
				action = "export";
				state = objExternalTransferForm.getPrevState();
			}
			request.setAttribute("ExternalTransferForm", objExternalTransferForm);
			if (action.equals("INIT")) {
				ExternalTransferDelegate objExternalTransferDelegate = new ExternalTransferDelegate(objExternalTransferForm, getUserObject(request), objUserPrincipal);
				ExternalTransferForm objExternalTransferFrm = null;
				if (state.equals("ExternalTransfer_INIT")) {
					objExternalTransferFrm = objExternalTransferDelegate.externalTransfer_INIT_iNIT();
					objPostActionForm=(EbwForm)objExternalTransferFrm;
				}
				else if (state.equals("ExternalTransfer_Edit")) {
					objExternalTransferFrm = objExternalTransferDelegate.externalTransfer_Edit_iNIT();
					objPostActionForm=(EbwForm)objExternalTransferFrm;
				}
				if(getUserObject(request).get("ExternalTransferForm")!=null)
					objExternalTransferFrm.copyCollections(((ExternalTransferForm)getUserObject(request).get("ExternalTransferForm")));
				request.setAttribute("ExternalTransferForm", objExternalTransferFrm);
				forwardTo = "ExternalTransfer";

			} else if (action.equals("submitbut")) {
				ExternalTransferDelegate objExternalTransferDelegate = new ExternalTransferDelegate(objExternalTransferForm, getUserObject(request), objUserPrincipal);
				ExternalPreConfirmForm objExternalPreConfirmFrm = null;
				if (state.equals("ExternalTransfer_INIT")) {
					objExternalPreConfirmFrm = objExternalTransferDelegate.externalTransfer_INIT_submitbut();
					objPostActionForm=(EbwForm)objExternalPreConfirmFrm;
				}
				else if (state.equals("ExternalTransfer_Edit")) {
					objExternalPreConfirmFrm = objExternalTransferDelegate.externalTransfer_Edit_submitbut();
					objPostActionForm=(EbwForm)objExternalPreConfirmFrm;
				}
				if(getUserObject(request).get("ExternalPreConfirmForm")!=null)
					objExternalPreConfirmFrm.copyCollections(((ExternalPreConfirmForm)getUserObject(request).get("ExternalPreConfirmForm")));
				request.setAttribute("ExternalPreConfirmForm", objExternalPreConfirmFrm);
				forwardTo = "ExternalPreConfirm";
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

			forwardTo = objPaymentsActionHook.postExternalTransfer (action, state,objExternalTransferForm,objPostActionForm, request, getUserObject(request), objUserPrincipal, forwardTo);

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
			if(fieldName.equals("externaltransfercredit")){
				param = new HashMap();
				param.put("displayType","SelectRadio");
				param.put("formname","ExternalTransferForm");
				param.put("name","externaltransfercredit");
				com.tcs.ebw.taglib.EbwTable ebwTableAjax = new com.tcs.ebw.taglib.EbwTable();
				try{
					if(param!=null)
						responseHTML = ebwTableAjax.getTableThruAjax(param, request.getSession(), request.getAttribute("ExternalTransferForm"));
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