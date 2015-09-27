/*
 * Created on Sun Apr 26 15:41:03 IST 2009
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
public class CancelTransferAction extends EbwAction {
	/**
	 * Logger for this class
	 */
	private static final Logger performanceLogger = Logger.getLogger("MSPerformanceLogger"); //$NON-NLS-1$

	/**
	 * performTask method of CancelTransferAction class. 
	 */ 
	public ActionForward performTask (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
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
		try 
		{
			EBWLogger.trace(this, "Starting performTask()"); 
			CancelTransferForm objCancelTransferForm = (CancelTransferForm) form; 

			UserPrincipal objUserPrincipal = getUserPrincipal(request);

			PaymentsActionHook objPaymentsActionHook = new PaymentsActionHook();
			objPaymentsActionHook.preCancelTransfer (objCancelTransferForm, request, getUserObject(request), objUserPrincipal);

			String action = objCancelTransferForm.getAction();
			String state = objCancelTransferForm.getState();
			log ("CancelTransferForm Action : " + action);
			log ("CancelTransferForm State  : " + state);

			EbwForm objPostActionForm = null;

			String actionType = objCancelTransferForm.getActionType();
			if(actionType!=null && actionType.length()>0 && actionType.equalsIgnoreCase("export")){
				action = "export";
				state = objCancelTransferForm.getPrevState();
			}
			request.setAttribute("CancelTransferForm", objCancelTransferForm);
			if (action.equals("INIT")) {
				CancelTransferDelegate objCancelTransferDelegate = new CancelTransferDelegate(objCancelTransferForm, getUserObject(request), objUserPrincipal);
				CancelTransferForm objCancelTransferFrm = null;
				if (state.equals("CancelTransfer_INIT")) {
					objCancelTransferFrm = objCancelTransferDelegate.cancelTransfer_INIT_iNIT();
					objPostActionForm=(EbwForm)objCancelTransferFrm;
				}
				else if (state.equals("CancelTransfer_Recurring")) {
					objCancelTransferFrm = objCancelTransferDelegate.cancelTransfer_Recurring_iNIT();
					objPostActionForm=(EbwForm)objCancelTransferFrm;
				}
				if(getUserObject(request).get("CancelTransferForm")!=null)
					objCancelTransferFrm.copyCollections(((CancelTransferForm)getUserObject(request).get("CancelTransferForm")));
				request.setAttribute("CancelTransferForm", objCancelTransferFrm);
				forwardTo = "CancelTransfer";
			} 
			else if (action.equals("cancelBtn")) {
				CancelTransferDelegate objCancelTransferDelegate = new CancelTransferDelegate(objCancelTransferForm, getUserObject(request), objUserPrincipal);
				CancelTransferConfirmForm objCancelTransferConfirmFrm = null;
				if (state.equals("CancelTransfer_INIT")) {
					objCancelTransferConfirmFrm = objCancelTransferDelegate.cancelTransfer_INIT_cancelBtn();
					objPostActionForm=(EbwForm)objCancelTransferConfirmFrm;
				}
				if(getUserObject(request).get("CancelTransferConfirmForm")!=null)
					objCancelTransferConfirmFrm.copyCollections(((CancelTransferConfirmForm)getUserObject(request).get("CancelTransferConfirmForm")));
				request.setAttribute("CancelTransferConfirmForm", objCancelTransferConfirmFrm);
				forwardTo = "CancelTransferConfirm";
			} 
			else if (action.equals("recurringCancelBtn")) {
				CancelTransferDelegate objCancelTransferDelegate = new CancelTransferDelegate(objCancelTransferForm, getUserObject(request), objUserPrincipal);
				CancelTransferConfirmForm objCancelTransferConfirmFrm = null;
				if (state.equals("CancelTransfer_Recurring")) {
					objCancelTransferConfirmFrm = objCancelTransferDelegate.cancelTransfer_Recurring_cancelBtn();
					objPostActionForm=(EbwForm)objCancelTransferConfirmFrm;
				}
				//Added to hide the Skip Next transfer button if recurring condition is over..
				else if (state.equals("CancelTransfer_LastRecurring")) {
					objCancelTransferConfirmFrm = objCancelTransferDelegate.cancelTransfer_Recurring_cancelBtn();
					objPostActionForm=(EbwForm)objCancelTransferConfirmFrm;
				}
				if(getUserObject(request).get("CancelTransferConfirmForm")!=null)
					objCancelTransferConfirmFrm.copyCollections(((CancelTransferConfirmForm)getUserObject(request).get("CancelTransferConfirmForm")));
				request.setAttribute("CancelTransferConfirmForm", objCancelTransferConfirmFrm);
				forwardTo = "CancelTransferConfirm";
			} 
			else if (action.equals("skipBtn")) {
				CancelTransferDelegate objCancelTransferDelegate = new CancelTransferDelegate(objCancelTransferForm, getUserObject(request), objUserPrincipal);
				SkipTransferConfirmForm objSkipTransferConfirmFrm = null;
				if (state.equals("CancelTransfer_Recurring")) {
					objSkipTransferConfirmFrm = objCancelTransferDelegate.cancelTransfer_Recurring_skipBtn();
					objPostActionForm=(EbwForm)objSkipTransferConfirmFrm;
				}
				if(getUserObject(request).get("SkipTransferConfirmForm")!=null)
					objSkipTransferConfirmFrm.copyCollections(((SkipTransferConfirmForm)getUserObject(request).get("SkipTransferConfirmForm")));
				request.setAttribute("SkipTransferConfirmForm", objSkipTransferConfirmFrm);
				forwardTo = "SkipTransferConfirm";
			} 
			else {
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

			forwardTo = objPaymentsActionHook.postCancelTransfer (action, state,objCancelTransferForm,objPostActionForm, request, getUserObject(request), objUserPrincipal, forwardTo);

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