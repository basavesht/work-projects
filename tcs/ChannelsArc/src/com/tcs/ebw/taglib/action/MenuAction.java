/*
 * Created on Fri Jun 09 16:20:28 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.taglib.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tcs.ebw.mvc.action.EbwAction;
import com.tcs.ebw.taglib.formbean.MenuForm;

public class MenuAction extends EbwAction {

	public ActionForward performTask(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) 
			throws Exception { 
        HttpSession session = request.getSession();
		MenuForm objmenuForm = (MenuForm) form; 
        String hiddenValue = objmenuForm.getHiddenValue();
        session.setAttribute("BreadCrumb", hiddenValue);
		String forwardTo = "input";
		return (mapping.findForward(forwardTo));
	}
}