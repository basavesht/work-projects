package com.tcs.ebw.template.tags.templates;

import java.util.Hashtable;
import java.util.Stack;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class InsertTag extends TagSupport
{

    public InsertTag()
    {
    }

    public void setTemplate(String template)
    {
        this.template = template;
    }

    public int doStartTag()
        throws JspException
    {
        stack = getStack();
        stack.push(new Hashtable());
        return 1;
    }

    public int doEndTag()
        throws JspException
    {
        try
        {
            pageContext.include(template);
        }
        catch(Exception ex)
        {
            throw new JspException(ex.getMessage());
        }
        stack.pop();
        return 6;
    }

    public void release()
    {
        template = null;
        stack = null;
    }

    public Stack getStack()
    {
        Stack s = (Stack)pageContext.getAttribute("template-stack", 2);
        if(s == null)
        {
            s = new Stack();
            pageContext.setAttribute("template-stack", s, 2);
        }
        return s;
    }

    private String template;
    private Stack stack;
}