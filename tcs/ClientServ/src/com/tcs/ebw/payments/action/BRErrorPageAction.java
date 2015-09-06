/*
 * Created on Sun Jun 07 11:18:27 IST 2009
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
public class BRErrorPageAction extends EbwAction {
	/**
	 * Logger for this class
	 */
	private static final Logger performanceLogger = Logger.getLogger("MSPerformanceLogger"); //$NON-NLS-1$

	/**
	 * performTask method of BRErrorPageAction class. 
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

		try {
			EBWLogger.trace(this, "Starting performTask()"); 
			BRErrorPageForm objBRErrorPageForm = (BRErrorPageForm) form; 

			UserPrincipal objUserPrincipal = getUserPrincipal(request);

			PaymentsActionHook objPaymentsActionHook = new PaymentsActionHook();
			objPaymentsActionHook.preBRErrorPage (objBRErrorPageForm, request, getUserObject(request), objUserPrincipal);

			String action = "INIT";  // Defaulting the action since the Setting could not be done at the source Formbean level...
			String state = "BRErrorPage_INIT"; // Defaulting the action since the Setting could not be done at the source Formbean level...

			log ("BRErrorPageForm Action : " + action);
			log ("BRErrorPageForm State  : " + state);

			EbwForm objPostActionForm = null;

			String actionType = objBRErrorPageForm.getActionType();
			if(actionType!=null && actionType.length()>0 && actionType.equalsIgnoreCase("export")){
				action = "export";
				state = objBRErrorPageForm.getPrevState();
			}
			request.setAttribute("BRErrorPageForm", objBRErrorPageForm);
			if (action.equals("INIT")) {
				BRErrorPageDelegate objBRErrorPageDelegate = new BRErrorPageDelegate(objBRErrorPageForm, getUserObject(request), objUserPrincipal);
				BRErrorPageForm objBRErrorPageFrm = null;
				if (state.equals("BRErrorPage_INIT")) {
					objBRErrorPageFrm = objBRErrorPageDelegate.bRErrorPage_INIT_iNIT();
					objPostActionForm=(EbwForm)objBRErrorPageFrm;
				}
				if(getUserObject(request).get("BRErrorPageForm")!=null)
					objBRErrorPageFrm.copyCollections(((BRErrorPageForm)getUserObject(request).get("BRErrorPageForm")));
				request.setAttribute("BRErrorPageForm", objBRErrorPageFrm);
				forwardTo = "BRErrorPage";
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

			forwardTo = objPaymentsActionHook.postBRErrorPage (action, state,objBRErrorPageForm,objPostActionForm, request, getUserObject(request), objUserPrincipal, forwardTo);

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
		ActionForward returnActionForward = (mapping.findForward(forwardTo));
		elapsedTime = System.currentTimeMillis() - startTime;	
		if (performanceLogger.isInfoEnabled()) {
			performanceLogger.info("IP Address : " + request.getRemoteAddr() + " - Thread Id : " + Thread.currentThread().getId() + " - Session Id : " + sessionId + " - " + this.getClass().getName() + " - performTask(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - End - ElapsedTime : " + elapsedTime); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		}
		return returnActionForward;
	}
}