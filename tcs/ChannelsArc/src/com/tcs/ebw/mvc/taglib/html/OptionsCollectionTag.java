// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 6/26/2008 7:29:33 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   OptionsCollectionTag.java

package com.tcs.ebw.mvc.taglib.html;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.IteratorAdapter;
import org.apache.struts.util.MessageResources;

// Referenced classes of package org.apache.struts.taglib.html:
//            SelectTag

public class OptionsCollectionTag extends TagSupport
{

    public OptionsCollectionTag()
    {
        filter = true;
        label = "label";
        name = "org.apache.struts.taglib.html.BEAN";
        property = null;
        style = null;
        styleClass = null;
        value = "value";
    }

    public boolean getFilter()
    {
        return filter;
    }

    public void setFilter(boolean filter)
    {
        this.filter = filter;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getProperty()
    {
        return property;
    }

    public void setProperty(String property)
    {
        this.property = property;
    }

    public String getStyle()
    {
        return style;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

    public String getStyleClass()
    {
        return styleClass;
    }

    public void setStyleClass(String styleClass)
    {
        this.styleClass = styleClass;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public int doStartTag()
        throws JspException
    {
		//String txtMsg =  messages.getMessage("rewrite.url");

		//System.out.println("=====================*********************************"+txtMsg);
		
		SelectTag selectTag = (SelectTag)super.pageContext.getAttribute("org.apache.struts.taglib.html.SELECT");
        if(selectTag == null)
        {

			//System.out.println(".........."+messages.getMessage("optionsCollectionTag.select"));
            JspException e = new JspException(messages.getMessage("optionsCollectionTag.select"));
            TagUtils.getInstance().saveException(super.pageContext, e);
            throw e;
        }
        Object collection = TagUtils.getInstance().lookup(super.pageContext, name, property, null);
        if(collection == null)
        {
            JspException e = new JspException(messages.getMessage("optionsCollectionTag.collection"));
            TagUtils.getInstance().saveException(super.pageContext, e);
            throw e;
        }
        Iterator iter = getIterator(collection);
        StringBuffer sb = new StringBuffer();
        String stringLabel;
        String stringValue;
        for(; iter.hasNext(); addOption(sb, stringLabel, stringValue, selectTag.isMatched(stringValue)))
        {
            Object bean = iter.next();
            Object beanLabel = null;
            Object beanValue = null;
            try
            {
                beanLabel = PropertyUtils.getProperty(bean, label);
                if(beanLabel == null)
                    beanLabel = "";
            }
            catch(IllegalAccessException e)
            {
                JspException jspe = new JspException(messages.getMessage("getter.access", label, bean));
                TagUtils.getInstance().saveException(super.pageContext, jspe);
                throw jspe;
            }
            catch(InvocationTargetException e)
            {
                Throwable t = e.getTargetException();
                JspException jspe = new JspException(messages.getMessage("getter.result", label, t.toString()));
                TagUtils.getInstance().saveException(super.pageContext, jspe);
                throw jspe;
            }
            catch(NoSuchMethodException e)
            {
                JspException jspe = new JspException(messages.getMessage("getter.method", label, bean));
                TagUtils.getInstance().saveException(super.pageContext, jspe);
                throw jspe;
            }
            try
            {
                beanValue = PropertyUtils.getProperty(bean, value);
                if(beanValue == null)
                    beanValue = "";
            }
            catch(IllegalAccessException e)
            {
                JspException jspe = new JspException(messages.getMessage("getter.access", value, bean));
                TagUtils.getInstance().saveException(super.pageContext, jspe);
                throw jspe;
            }
            catch(InvocationTargetException e)
            {
                Throwable t = e.getTargetException();
                JspException jspe = new JspException(messages.getMessage("getter.result", value, t.toString()));
                TagUtils.getInstance().saveException(super.pageContext, jspe);
                throw jspe;
            }
            catch(NoSuchMethodException e)
            {
                JspException jspe = new JspException(messages.getMessage("getter.method", value, bean));
                TagUtils.getInstance().saveException(super.pageContext, jspe);
                throw jspe;
            }
            stringLabel = beanLabel.toString();
            stringValue = beanValue.toString();
        }

        TagUtils.getInstance().write(super.pageContext, sb.toString());
        return 0;
    }

    public void release()
    {
        super.release();
        filter = true;
        label = "label";
        name = "org.apache.struts.taglib.html.BEAN";
        property = null;
        style = null;
        styleClass = null;
        value = "value";
    }

    protected void addOption(StringBuffer sb, String label, String value, boolean matched)
    {

		//System.out.println("VIJAYYYYYYYYYYYYYXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+TagUtils.getInstance().filter(value));
		//System.out.println("VIJAYYYYYYYYYYYYYyyyyyyyyyyyyyyyyyyyyyyyy"+value);
        sb.append("<option value=\"");
        if(filter)
            sb.append(TagUtils.getInstance().filter(value));
        else
            sb.append(value);
        sb.append("\"");
        if(matched)
            sb.append(" selected=\"selected\"");
        if(style != null)
        {
            sb.append(" style=\"");
            sb.append(style);
            sb.append("\"");
        }
        if(styleClass != null)
        {
            sb.append(" class=\"");
            sb.append(styleClass);
            sb.append("\"");
        }
        sb.append(">");
        String def = getDefaultValue();
    	String temp = getProperty();
		temp = temp.substring(0, temp.indexOf("Collection"));
        if(filter){
			if(label.equals("Select"))
			{
					System.out.println("========Select================="+getDefaultValue());
				
					System.out.println("SelectSelectSelectSelectSelectSelectSelectVIJAYYYYYYYYYYYYY"+label+temp);
					
		     	
				if((!(def==null)) && (def.equalsIgnoreCase("yes")))			
							label = "Select "+temp;

			}
            sb.append(TagUtils.getInstance().filter(label));
		}
        else
		{
			if(label.equals("Select"))
			{
			System.out.println("VIJAYSelectSelectSelectSelectSelectSelectSelectSelect"+label);
			if((!(def==null)) && (def.equalsIgnoreCase("yes")))		
					label = "Select "+temp;
			}
			sb.append(label);
		}
        sb.append("</option>\r\n");
    }

    protected Iterator getIterator(Object collection)
        throws JspException
    {
        if(collection.getClass().isArray())
            collection = Arrays.asList((Object[])collection);
        if(collection instanceof Collection)
            return ((Collection)collection).iterator();
        if(collection instanceof Iterator)
            return (Iterator)collection;
        if(collection instanceof Map)
            return ((Map)collection).entrySet().iterator();
        if(collection instanceof Enumeration)
            return new IteratorAdapter((Enumeration)collection);
        else
            throw new JspException(messages.getMessage("optionsCollectionTag.iterator", collection.toString()));
    }

    protected static MessageResources messages = MessageResources.getMessageResources("org.apache.struts.taglib.html.LocalStrings");
    protected boolean filter;
    protected String label;
    protected String name;
    protected String property;
    private String style;
    private String styleClass;
    protected String value;
    private String key;
    private String defaultValue;
    
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		System.out.println("OptionsCollectionTag*****************"+key);
		this.key = key;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}