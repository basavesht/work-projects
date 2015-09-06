package com.tcs.ebw.template.tags.templates;

import com.tcs.ebw.template.beans.templates.PageParameter;
import java.util.Hashtable;
import java.util.Stack;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

// Referenced classes of package com.tcs.ebw.template.tags.templates:
//            InsertTag

public class PutTag extends TagSupport
{

    public PutTag()
    {
        direct = "false";
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setContent(String s)
    {
        content = s;
    }

    public void setDirect(String s)
    {
        direct = s;
    }

    public int doStartTag()
        throws JspException
    {
        InsertTag parent = (InsertTag)getAncestor("com.tcs.ebw.template.tags.templates.InsertTag");
        if(parent == null)
            throw new JspException("PutTag.doStartTag(): No InsertTag ancestor");
        Stack template_stack = parent.getStack();
        if(template_stack == null)
            throw new JspException("PutTag: no template stack");
        Hashtable params = (Hashtable)template_stack.peek();
        if(params == null)
        {
            throw new JspException("PutTag: no hashtable");
        } else
        {
            params.put(name, new PageParameter(content, direct));
            return 0;
        }
    }

    public void release()
    {
        name = content = direct = null;
    }

    private TagSupport getAncestor(String className)
        throws JspException
    {
        Class klass = null;
        try
        {
            klass = Class.forName(className);
        }
        catch(ClassNotFoundException ex)
        {
            throw new JspException(ex.getMessage());
        }
        return (TagSupport)TagSupport.findAncestorWithClass(this, klass);
    }

    private String name;
    private String content;
    private String direct;
}