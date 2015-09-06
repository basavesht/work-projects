/*
 * Created on Fri Nov 28 15:31:16 IST 2008
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.common.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.beanutils.BeanUtils;

import com.tcs.ebw.common.formbean.*;
import com.tcs.ebw.payments.businessdelegate.*;
import com.tcs.ebw.mvc.action.EbwAction;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.mvc.validator.EbwForm;
import java.util.HashMap;
import com.tcs.ebw.common.formbean.TransactionPwdForm;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;

/**
 * TemplateList.jsp is using this Action class. 
 */

public class TemplateListAction extends EbwAction {

	/**
	 * performTask method of TemplateListAction class. 
	 */ 
	public ActionForward performTask (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) 
			throws Exception { 

		String forwardTo = "input";
		UserPrincipal objUserPrincipal = getUserPrincipal(request);
		try {
			EBWLogger.trace(this, "Starting performTask()"); 
			TemplateListForm objTemplateListForm = (TemplateListForm) form; 
            objTemplateListForm.setScreenName(request.getParameter("screenName"));
            EBWLogger.logDebug(this, (new StringBuilder("Screen Name from TemplateList Form")).append(request.getParameter("screenName")).toString());
            objTemplateListForm.setUsruserid(objUserPrincipal.getUsruserid());
            
			String action = objTemplateListForm.getAction();
			String state = objTemplateListForm.getState();
			log ("TemplateListForm Action : " + action);
			log ("TemplateListForm State  : " + state);

			EbwForm objPostActionForm = null;

			String actionType = objTemplateListForm.getActionType();
			if(actionType!=null && actionType.length()>0 && actionType.equalsIgnoreCase("export")){
				action = "export";
				state = objTemplateListForm.getPrevState();
			}
			request.setAttribute("TemplateListForm", objTemplateListForm);
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
		return (mapping.findForward(forwardTo));
	}
}