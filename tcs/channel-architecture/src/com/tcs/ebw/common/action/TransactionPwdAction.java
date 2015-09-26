/*
 * Created on Mon Jun 05 17:14:05 IST 2006
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 * 
 */

package com.tcs.ebw.common.action;

import java.util.HashMap;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tcs.ebw.common.formbean.TransactionPwdForm;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.mvc.action.EbwAction;
import com.tcs.ebw.security.EBWSecurity;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.jaas.principal.UserPrincipal;
import java.lang.reflect.*;

/**
 * TransactionPwd.jsp is using this Action class. 
 */

public class TransactionPwdAction extends EbwAction {

	/**
	 * performTask method of TransactionPwdAction class. 
	 */ 
	public ActionForward performTask (ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		String forwardTo = "input";
		try {
			EBWLogger.trace(this, "Starting performTask()"); 
			TransactionPwdForm objTransactionPwdForm = (TransactionPwdForm) form; 
			String action = objTransactionPwdForm.getAction();
			String state = objTransactionPwdForm.getState();
			log ("TransactionPwdForm Action : " + action);
			log ("TransactionPwdForm State  : " + state);

			request.setAttribute("TransactionPwdForm", objTransactionPwdForm);
			UserPrincipal objUserPrincipal = getUserPrincipal(request);

			IEBWService service= EBWServiceFactory.create("ValidateTransPwd");
			Class paramTypes[] ={String.class,Object.class};
			HashMap params = new HashMap();
			params.put("usruserid", objUserPrincipal.getUsruserid());
			params.put("usrtranspwd", new String((new EBWSecurity()).computeHash(objTransactionPwdForm.getTxnPwd().getBytes(), "SHA1")));

			Object paramObjs[] ={"validateTransPwd",params};
			ArrayList output = (ArrayList)service.execute(paramTypes,paramObjs);
			int result = Integer.parseInt(((ArrayList) output.get(1)).get(0).toString());

			System.out.println ("Validate Txn Pwd result : " + result);

			if(result > 0) {
				forwardTo = objTransactionPwdForm.getForwardTo();
				String forwardFrom = objTransactionPwdForm.getForwardFrom();
				System.out.println("forwardFrom :"+forwardFrom);
				if (forwardFrom != null) {
				    EbwForm objForm = (EbwForm) request.getSession().getAttribute(forwardFrom + "Form");
					
					System.out.println("Form in Txn Password--"+objForm);
					
					if (objForm != null) {
					    objForm.setTxnPwdValid("Y");
					    System.out.println ("Action in Txn : " + objForm.getAction());
								
						Class cls =Class.forName(objTransactionPwdForm.getDelegatePackage()+"."+objTransactionPwdForm.getForwardFrom()+"Delegate");
						
						 Class[] intArgsClass = new Class[] {EbwForm.class, HashMap.class ,com.tcs.ebw.serverside.jaas.principal.UserPrincipal.class};
						 HashMap objUserSessionObject=getUserObject(request);
						 Constructor intArgsConstructor;
						 intArgsConstructor = cls.getConstructor(intArgsClass);
						 Object[] intArgs = new Object[] {objForm,objUserSessionObject, objUserPrincipal};
						 Object ob = intArgsConstructor.newInstance(intArgs);
						 Method method =cls.getMethod(objTransactionPwdForm.getDelegateMethod(),null);
						 System.out.println("method Name :"+method.getName());
						 EbwForm objNextFrm=(EbwForm)method.invoke(ob,null);
						 request.setAttribute(forwardTo + "Form",objNextFrm);
						
					}
				} 
			  } else {
				System.out.println ("Wrong password entered.");
				throw new Exception ("errors.wrongtranspwd");
			}

			EBWLogger.trace(this, "Finished performTask()"); 
		} catch (Throwable objThrow) {
			objThrow.printStackTrace();
			saveErrorMessage(request, objThrow);
		}
		return (mapping.findForward(forwardTo));
	}
}