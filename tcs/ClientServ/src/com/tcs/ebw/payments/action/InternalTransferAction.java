/*
 * Created on Tue Mar 24 16:15:01 IST 2009
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
public class InternalTransferAction extends EbwAction {
	/**
	 * Logger for this class
	 */
	private static final Logger performanceLogger = Logger.getLogger("MSPerformanceLogger"); //$NON-NLS-1$

	/**
	 * performTask method of InternalTransferAction class. 
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
			InternalTransferForm objInternalTransferForm = (InternalTransferForm) form; 

			UserPrincipal objUserPrincipal = getUserPrincipal(request);

			PaymentsActionHook objPaymentsActionHook = new PaymentsActionHook();
			objPaymentsActionHook.preInternalTransfer (objInternalTransferForm, request, getUserObject(request), objUserPrincipal);

			String action = objInternalTransferForm.getAction();
			String state = objInternalTransferForm.getState();
			ajaxCall = objInternalTransferForm.getAjaxCall();
			fieldName = objInternalTransferForm.getFieldName();
			log ("InternalTransferForm Action : " + action);
			log ("InternalTransferForm State  : " + state);

			EbwForm objPostActionForm = null;

			String actionType = objInternalTransferForm.getActionType();
			if(actionType!=null && actionType.length()>0 && actionType.equalsIgnoreCase("export")){
				action = "export";
				state = objInternalTransferForm.getPrevState();
			}
			request.setAttribute("InternalTransferForm", objInternalTransferForm);
			if (action.equals("INIT")) {
				InternalTransferDelegate objInternalTransferDelegate = new InternalTransferDelegate(objInternalTransferForm, getUserObject(request), objUserPrincipal);
				InternalTransferForm objInternalTransferFrm = null;
				if (state.equals("InternalTransfer_INIT")) {
					objInternalTransferFrm = objInternalTransferDelegate.internalTransfer_INIT_iNIT();
					objPostActionForm=(EbwForm)objInternalTransferFrm;
				} 
				else if(state.equals("InternalTransfer_Edit")){
					objInternalTransferFrm = objInternalTransferDelegate.internalTransfer_Edit_iNIT();
					objPostActionForm = (EbwForm)objInternalTransferFrm;
				}
				if(getUserObject(request).get("InternalTransferForm")!=null)
					objInternalTransferFrm.copyCollections(((InternalTransferForm)getUserObject(request).get("InternalTransferForm")));
				request.setAttribute("InternalTransferForm", objInternalTransferFrm);
				forwardTo = "InternalTransfer";

			} else if (action.equals("submitbut")) {
				InternalTransferDelegate objInternalTransferDelegate = new InternalTransferDelegate(objInternalTransferForm, getUserObject(request), objUserPrincipal);
				InternalPreConfirmForm objInternalPreConfirmFrm = null;
				if (state.equals("InternalTransfer_INIT")) {
					objInternalPreConfirmFrm = objInternalTransferDelegate.internalTransfer_INIT_submitbut();
					objPostActionForm=(EbwForm)objInternalPreConfirmFrm;
				}
				else if (state.equals("InternalTransfer_Edit")) {
					objInternalPreConfirmFrm = objInternalTransferDelegate.internalTransfer_Edit_submitbut();
					objPostActionForm=(EbwForm)objInternalPreConfirmFrm;
				}
				if(getUserObject(request).get("InternalPreConfirmForm")!=null)
					objInternalPreConfirmFrm.copyCollections(((InternalPreConfirmForm)getUserObject(request).get("InternalPreConfirmForm")));
				request.setAttribute("InternalPreConfirmForm", objInternalPreConfirmFrm);
				forwardTo = "InternalPreConfirm";
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

			forwardTo = objPaymentsActionHook.postInternalTransfer (action, state,objInternalTransferForm,objPostActionForm, request, getUserObject(request), objUserPrincipal, forwardTo);

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
			if(fieldName.equals("internaltransfercredit")){
				param = new HashMap();
				param.put("displayType","SelectRadio");
				param.put("formname","InternalTransferForm");
				param.put("name","internaltransfercredit");
				com.tcs.ebw.taglib.EbwTable ebwTableAjax = new com.tcs.ebw.taglib.EbwTable();
				try{
					if(param!=null)
						responseHTML = ebwTableAjax.getTableThruAjax(param, request.getSession(), request.getAttribute("InternalTransferForm"));
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