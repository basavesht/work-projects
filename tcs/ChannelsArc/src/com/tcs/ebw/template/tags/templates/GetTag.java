package com.tcs.ebw.template.tags.templates;

import com.tcs.ebw.template.beans.templates.PageParameter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Stack;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;

public class GetTag extends TagSupport
{

    public GetTag()
    {
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int doStartTag()
        throws JspException
    {
        Stack stack = (Stack)pageContext.getAttribute("template-stack", 2);
        if(stack == null)
            throw new JspException("GetTag.doStartTag(): NO STACK");
        Hashtable params = (Hashtable)stack.peek();
        if(params == null)
            throw new JspException("GetTag.doStartTag(): NO HASHTABLE");
        PageParameter param = (PageParameter)params.get(name);
        if(param != null)
        {
            String content = param.getContent();
            if(param.isDirect())
                try
                {
                    pageContext.getOut().print(content);
                }
                catch(IOException ex)
                {
                    throw new JspException(ex.getMessage());
                }
            else
                try
                {
                    pageContext.getOut().flush();
                    System.out.println("Content: " + content);
                    pageContext.include(content);
                }
                catch(Exception ex)
                {
                    throw new JspException(ex.getMessage());
                }
        }
        return 0;
    }

    public void release()
    {
        name = null;
    }

    private String name;
}