/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspWriter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.tcs.ebw.exception.EbwException;
import javax.servlet.http.HttpSession;

public class EbwCrumb extends TagSupport{
	private String value=null;
	private String link=null;
	
	// Resource Bundle
	private ResourceBundle appRB = null;
	private ResourceBundle formRB = null;
	
	public EbwCrumb() {
		try {
			/**
			 * Loading the Application's Property file that contains the location of
			 * the property file that contains the displaynames
			 */
			appRB = ResourceBundle.getBundle("ApplicationConfig");
			
			/**
			 * Loading up the properties file  that contains the fieldId and Displaynames as key, value respectively
			 */          
			formRB = ResourceBundle.getBundle(appRB.getString("message-resources"));
		} catch (Exception exc) {
			System.out.println ("Resource Bundle Not found...");
		}
	}
	
	public int doStartTag() throws JspException {
		try {
		    EbwBreadCrumb parent = (EbwBreadCrumb) getAncestor("com.tcs.ebw.taglib.EbwBreadCrumb");
			System.out.println("Start do Tag");
			JspWriter out = pageContext.getOut();
			HttpSession session = pageContext.getSession();
			String sessBreadCrumb = (String) session.getAttribute("BreadCrumb");
            String[] breadCrumbArr = sessBreadCrumb.split(">");
            
            for(int i=0;i<breadCrumbArr.length;i++) {			
				if (i < (breadCrumbArr.length-1)) {
				    out.println("<a class=\"breadcrumblink\" href=\"" + breadCrumbArr[i] + ".do\">" + getBundle(breadCrumbArr[i]) + "</a>");
				    out.println(" > ");
				} else {
				    out.println(getBundle(breadCrumbArr[i]));
				}
            }
        	destroyObjects();
		}catch(NullPointerException nullexp){
			new EbwException("NullPointer",nullexp).printEbwException();
		}catch(MissingResourceException missexp){
			new EbwException("ResourceNotFound",missexp).printEbwException();
		}catch(ClassCastException cceexp){
			new EbwException("MissingResourceKey",cceexp).printEbwException();
		}catch(Exception exp){
			new EbwException("AfterAllCatch",exp).printEbwException();
		}
		return SKIP_BODY;
	}
	
	private String getBundle(String key) {
	    String label = key;
	    try {
	        label = formRB.getString(key + ".title");
	        if (label==null || (label!=null && label.trim().length()==0)) {
	            label = key;
	        }
	    } catch (Exception exc) {
	        label = key;
	    }
	    System.out.println ("Label : " + label);
	    return label;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}

	
	private void destroyObjects() {
		value=null;
    	link=null;
	}

	public void release() {
		super.release();
	}

	private TagSupport getAncestor(String className) throws JspException {
        Class klass = null; // can’t name variable "class"
        try {
            klass = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new JspException(ex.getMessage());
        }
        return (TagSupport) findAncestorWithClass(this, klass);
    }
	
	/**
	 * @return Returns the value.
	 */
	
	public String getValue() {
		return value;
	}

	/**
	 * @param value The value to set.
	 */

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return Returns the actionType.
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param actionType The actionType to set.
	 */
	public void setLink(String link) {
		this.link = link;
	}
}
