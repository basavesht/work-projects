/*
 * Created on Wed Jul 12 15:41:42 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.serverside.bi.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.tcs.ebw.mvc.action.EbwAction;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import com.tcs.ebw.serverside.bi.formbean.*;
/**
 * Sentdata.jsp is using this Action class. 
 */

public class SentdataAction extends EbwAction {

	/**
	 * performTask method of SentdataAction class. 
	 */ 
	public ActionForward performTask (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) 
			throws Exception { 

		String forwardTo = "input";

		try {
			EBWLogger.trace(this, "Starting performTask()"); 
			SentdataForm objSentdataForm = (SentdataForm) form; 
			String action = objSentdataForm.getAction();
			String state = objSentdataForm.getState();
			log ("SentdataForm Action : " + action);
			log ("SentdataForm State  : " + state);

			if (!action.equals("INIT"))
				objSentdataForm.setPaginationIndex("0");
			
			request.setAttribute("SentdataForm", objSentdataForm);
			UserPrincipal objUserPrincipal = getUserPrincipal(request);
			EBWLogger.trace(this, "Finished performTask()"); 
		} catch (Throwable objThrow) {
			objThrow.printStackTrace();
			saveErrorMessage(request, objThrow);
		}
		return (mapping.findForward(forwardTo));
	}
}