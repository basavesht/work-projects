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
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.MissingResourceException;
import com.tcs.ebw.exception.EbwException;

public class EbwBreadCrumb extends TagSupport {
    private String seperator;
 	 
    public String getSeperator() {
		return seperator;
    }
 
    public void setSeperator(String seperator) {
        this.seperator = seperator;
    }

    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            out.println("<table class=\"breadcrumbtable\"><tr><td class=\"breadcrumb\">");
        } catch(NullPointerException nullexp){
			new EbwException("NullPointer",nullexp).printEbwException();
		} catch(MissingResourceException missexp){
			new EbwException("ResourceNotFound",missexp).printEbwException();
		} catch(ClassCastException cceexp){
			new EbwException("MissingResourceKey",cceexp).printEbwException();
		} catch(Exception exp){
			new EbwException("AfterAllCatch",exp).printEbwException();
		}
		return EVAL_BODY_INCLUDE;
    }

	public int doEndTag() throws JspException {
	    try{
	        JspWriter out = pageContext.getOut();
            out.println("</td></tr></table>");
		} catch(NullPointerException nullexp){
			new EbwException("NullPointer",nullexp).printEbwException();
		} catch(MissingResourceException missexp){
			new EbwException("ResourceNotFound",missexp).printEbwException();
		} catch(ClassCastException cceexp){
			new EbwException("MissingResourceKey",cceexp).printEbwException();
		} catch(Exception exp){
			new EbwException("AfterAllCatch",exp).printEbwException();
		}
        return EVAL_PAGE;
	}

    public void release() {
        seperator = null;
    }
}