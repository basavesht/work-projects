/*
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.jaas.struts.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.jaas.auth.*;
import com.tcs.ebw.jaas.struts.forms.LogonFormBean;
import java.util.*;
import javax.security.auth.login.LoginContext;
import org.apache.commons.logging.*;
import com.tcs.ebw.jaas.auth.Auth;
/**
 *
 * This class is used to autheticate a user based on username and password of the user
 * 
 * @author 	- TCS
 * @version - 1.0
 */
public class LogonAction extends Action{
	
	/**
	 * Form used to load information from user screen to this class.
	 */
	private LogonFormBean logonFormBean;
	/**
	 * Object used to store resource bundle (property file) information. 
	 */
	private ResourceBundle connProperties;
	/**
	 * Variable used to store the connection type value which is read 
	 * from the above resource bundle object.
	 */
	private String connType;
	
	/**
	 * LoginContext object which is used to find the jaas configuration file and
	 * instantiate the specified login module. It also forwards the handle to the
	 * specified callback handler. @see com.tcs.ebw.jaas.auth.EBWCallbackHandler for more details on callbacks.
	 */
	private LoginContext lc;
	
	/**
	 * String Objects used to store the credentials got from the user screen. This will
	 * be used to verify against the back end system by JAAS modules. 
	 */
	private String username,password;
	
	/**
	 * Auth is an interface defining an authenticate method which all the authentication 
	 * classes for various login modules will be implementing. 
	 */
	private Auth fa;
	
	
    /**
     * Logging output for this plug in instance.
     */
    protected Log log = LogFactory.getLog(this.getClass());
	
    /**
     * Variable to store the resultant action
     */
    private String result ;
	
    
    /**
     *  This method is used by the application to call the appropriate authentication
     *  mechanism to be followed for authentication based on the connection type. 
     */
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
		
		if(actionForm!=null && actionForm instanceof ActionForm){
			logonFormBean = (LogonFormBean) actionForm;
			username = logonFormBean.getUsername();
			password = logonFormBean.getPassword();
		}
		else {
			System.out.println("Action form is null");
			request.setAttribute("error","Authentication failed...Username or Password is invalid..");
			return mapping.findForward("failure");
		}
		
			fa = new JAASAbstractAuth(username,password);
		
		   if (fa.authenticate()) {
		   	  System.out.println("Authentication true..");
		     HttpSession sess = request.getSession();
		     sess.setAttribute(Auth.SUBJECT_SESSION_KEY, fa.getSubject());
		     result= "success";
		   }
		   else{
		   	request.setAttribute("error","Authentication failed...Username or Password is invalid..");
		   	result="failure";
		   }
		}catch(Exception e){
			result = "failure";
			request.setAttribute("error","Authentication failed...Username or Password is invalid..");
			new EbwException(this,e).printEbwException();
		}
		
		return mapping.findForward(result);
	}
}
