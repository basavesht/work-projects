/*
 * Created on Oct 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.template.beans.templates;

/**
 * @author tcs
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PageParameter {
    private String content, direct;

    public void setContent(String s) {
        content = s;
    }

    public void setDirect(String s) {
        direct = s;
    }

    public String getContent() {
        return content;
    }

    public boolean isDirect() {
        return Boolean.valueOf(direct).booleanValue();
    }

    public PageParameter(String content, String direct) {
        this.content = content;
        this.direct = direct;
        System.out.println ("content : " + content);
    }
    
    public String toString() {
        return "Content : " + content + "\tDirect : " + direct + "\n";
    }
}