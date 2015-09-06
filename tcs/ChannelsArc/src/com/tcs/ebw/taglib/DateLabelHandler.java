// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 12/11/2006 9:50:27 AM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) deadcode 
// Source File Name:   ModeTextAreaLabelHandler.java

package com.tcs.ebw.taglib;

import com.tcs.ebw.common.util.*;
import com.tcs.ebw.mvc.validator.EbwForm;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import com.tcs.ebw.exception.EbwException;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import com.tcs.ebw.taglib.TagLibConstants;



public class DateLabelHandler extends TagSupport
{
	Object formObj;
    private String formname=null; 
    private String name=null;			// name of the textarealabelmode tag
    private String css=null;
    private String maxLength=null;
	private String returnInput="";		// the value which will be writeen to the output buffer.
	StringBuffer outputBuffer =null;	// Buffer to store the data.
	private ResourceBundle bundle;
	private ResourceBundle languageRsBundle;
	private String textField="";
	private String anchorTag="";
	private boolean htmlOutput = false;
	private String displayType=null;
	private String attribute=null;


    public DateLabelHandler(boolean htmlOutput) {
		 this.htmlOutput = htmlOutput;
	}
	
	public DateLabelHandler() {
	    this.htmlOutput = false;
	}
	
	// Method to set the attributes if workign in HTML mode
	public void setAttributes(String name,String formname,String css,String maxLength,String displayType){
		this.name=name;
		this.formname=formname;
		this.css=css;
		this.maxLength=maxLength;
		this.displayType=displayType;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */

    public int doStartTag()
    {
		String calenderImage="";
		String propDateTooltip="";
		String dateTooltip="";
		String onclick="";
        try
        {	
			outputBuffer = new StringBuffer();
			CommonTemplateHTML objCommonTemp=new CommonTemplateHTML(); // the new Instance of the method which has the code for dumping the tags.
            JspWriter out =null;									  //   JspWriter Object	
			Object ebwForm=null;
			String className=null;
			Class classLoad=null;										// the java.lang.Class object which contains the object of the associated class.
			String mode=null;
			String formName=null;
			String value = null;
			String displayComponent=null;								

			if(!htmlOutput){
				out=pageContext.getOut();
				ebwForm= (EbwForm)pageContext.getRequest().getAttribute(formname);
				className = ebwForm.getClass().getName();
				EBWLogger.trace(this, "Class Name" + className);
				classLoad= Class.forName(className);
			}
			else {
					className=formname;
					classLoad=Class.forName(className);
					ebwForm=(EbwForm)classLoad.newInstance();
				 }
				
			// Get the mode,formname and the displayComponent Type
				mode = ((EbwForm)ebwForm).getState().substring(((EbwForm)ebwForm).getState().indexOf("_") + 1, ((EbwForm)ebwForm).getState().length()); // Mode of the Screen
				formName = className.substring(className.lastIndexOf(".") + 1, className.length());
				if(displayType==null || displayType.equals("")){
					displayComponent= PropertyFileReader.getProperty("modeDateLabel." + mode);
				}else {
					displayComponent=getDisplayType();
				}
		
			//Get the resource Bundles for the tag's property file & the locale-specific resources
			bundle = ResourceBundle.getBundle(TagLibConstants.RESOURCE_BUNDLE_FILE_MODECOMPONENT);
			languageRsBundle=ResourceBundle.getBundle(TagLibConstants.RESOURCE_BUNDLE_FILE_TAGLIBMULTILINGUAL);
			
			//Get the values from the tag property resource bundle
            calenderImage=bundle.getString("ModeDateTextField.image");	
			propDateTooltip=bundle.getString("ModeDateTextField.tooltip"); 
			
			//Get the values from the locale-specific resource bundle
			dateTooltip=languageRsBundle.getString(propDateTooltip);

			// Populate default values if user has not specified the attribute value
			// Set the default value of the css
			if(css==null || css.equals("")){
				EBWLogger.trace(this, "the css class not specified by the user");
				css=bundle.getString("ModeDateTextField.css");
			}

			// Set the default value of the maxlength
			if(maxLength==null || maxLength.equals("")){
				EBWLogger.trace(this, "the css class not specified by the user");
				maxLength=bundle.getString("ModeDateTextField.maxlength");
			}
	
			if(displayComponent!=null){
				
			   if(displayComponent.equalsIgnoreCase("date"))
		        {   
					 onclick="window.dateField=document."+formName+"."+getName()+";calendar=window.open('../js/calendar.html','cal','WIDTH=220%,HEIGHT=300%,resizable=yes')";
					 value = (String)classLoad.getMethod("get" + StringUtil.initCaps(getName()), null).invoke(ebwForm, null);
					 textField=objCommonTemp.setTextFieldHTML(getName(),value,css,maxLength,getAttribute());
					 anchorTag=objCommonTemp.setAnchorTag(displayComponent,onclick,calenderImage,dateTooltip,"");
					 returnInput=textField+anchorTag;
					 
				} else if(displayComponent.equalsIgnoreCase("label")){
					 value = (String)classLoad.getMethod("get" + StringUtil.initCaps(getName()), null).invoke(ebwForm, null);
	                 returnInput=objCommonTemp.setLabel(value);
		        } else{

					 EBWLogger.trace(this, "There is no display components for such a mode");
	            }
				 outputBuffer.append(returnInput);
			}
			System.out.println("Resultant Date Buffer--"+outputBuffer);
			if (out!=null)
        	 out.println (outputBuffer);
        	destroyObjects();

        }catch(NullPointerException nullexp){
			new EbwException(this,nullexp).printEbwException();
		}
		catch(MissingResourceException missexp){
			new EbwException(this,missexp).printEbwException();
		}
		catch(ClassCastException cceexp){
			new EbwException(this,cceexp).printEbwException();
		}
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
	
	// 
	private void destroyObjects() {

		 formname=null;
		 name=null;
		 css=null;
		 displayType=null;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		// TODO Auto-generated method stub
		super.release();
	}

	/**
	 * @return Returns the formname.
	 */
	public String getFormname()
    {
        return formname;
    }

	/**
	 * @param type The formname to set.
	 */
    public void setFormname(String formname)
    {
        this.formname = formname;
    }

	/**	
	 * @return Returns the name.
	 */
    public String getName()
    {
        return name;
    }

	/**
	 * @param type The name to set.
	 */
    public void setName(String name)
    {
        this.name = name;
    }

	/**	
	 * @return Returns the css.
	 */

    public String getCss()
    {
        return css;
    }

	/**
	 * @param type The css to set.
	 */
    public void setCss(String css)
    {
        this.css = css;
    }


	/**	
	 * @return Returns the displayType.
	 */
    public String getDisplayType()
    {
        return displayType;
    }
   
    /**
	 * @return Returns the attribute.
	 */
	public String getAttribute() {
		return attribute;
	}
	/**
	 * @param attribute The attribute to set.
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	/**
	 * @param type The name to set.
	 */
    public void setDisplayType(String displayType)
    {
        this.displayType = displayType;
    }

	public static void main(String args[]){
		DateLabelHandler objDateHandler=new DateLabelHandler(true);
		objDateHandler.setAttributes("datelabelfield","com.tcs.ebw.custody.formbean.OutstandingSettlementForm","","10","date");
		objDateHandler.doStartTag();
	}
	
}