package com.tcs.bancs.csrfguard.tag;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.tcs.bancs.csrfguard.CSRFGuard;

public class CSRFGuardTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8246647371881827072L;
	private CSRFGuard guard = null;
	private String config; 
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	@Override
	public int doStartTag() throws JspException {
		try{
			
			if ( guard == null ){
				String configFile = pageContext.getServletContext().getRealPath(config);
				this.guard = new CSRFGuard(pageContext.getServletContext(), configFile);
			}
			 
	         HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
	         
	         HttpSession session = request.getSession(false);
	         
	         if ( session != null ){
	        	 
	        	 String token = guard.generateToken();
	        	 JspWriter out = pageContext.getOut();  
	        	 out.println(guard.getFormField(token,session.getId()));  
	        	 guard.setToken(session, token);
	        	 
	         }
	         return EVAL_PAGE;
		}catch(NoSuchAlgorithmException e){
			throw new JspException(e);
			
		} catch (IOException e) {
			throw new JspException(e);
		}
	}

}
