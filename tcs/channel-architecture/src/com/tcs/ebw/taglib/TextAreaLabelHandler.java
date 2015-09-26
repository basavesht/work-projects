// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 12/11/2006 9:50:27 AM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) deadcode 
// Source File Name:   ModeTextAreaLabelHandler.java

package com.tcs.ebw.taglib;

import com.tcs.ebw.common.util.*;
import com.tcs.ebw.mvc.validator.EbwForm;
import java.io.PrintStream;
import java.lang.reflect.Method;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import com.tcs.ebw.exception.EbwException;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import com.tcs.ebw.taglib.TagLibConstants;

public class TextAreaLabelHandler extends TagSupport
{
	Object formObj;
    private String formname=null; 
    private String name=null;			// name of the textarealabelmode tag
    private String css=null;
	private String rows=null;
	private String cols=null;
	private String returnInput="";		// the value which will be writeen to the output buffer.
	StringBuffer outputBuffer =null;// Buffer to store the data.
	private ResourceBundle bundle;
	private boolean htmlOutput = false;


    public TextAreaLabelHandler(boolean htmlOutput) {
		 this.htmlOutput = htmlOutput;
	}
	
	public TextAreaLabelHandler() {
	    this.htmlOutput = false;
	}

	public void setAttributes(String name,String formname,String css,String cols,String rows){
		this.name=name;
		this.formname=formname;
		this.css=css;
		this.cols=cols;
		this.rows=rows;
	}
 
    public int doStartTag()
    {
        try
        {	
			JspWriter out=null;
            Object 	ebwForm=null;
			String className=null;
		    Class classLoad=null;
			outputBuffer = new StringBuffer();
			CommonTemplateHTML objCommonTemp=new CommonTemplateHTML();
			if(!htmlOutput){
				 out= pageContext.getOut();
				 ebwForm = (EbwForm)pageContext.getRequest().getAttribute(formname);
				 className = ebwForm.getClass().getName();
				 classLoad= Class.forName(className);
			}else {

				 className=formname;
				 classLoad=Class.forName(className);
				 ebwForm=(EbwForm)classLoad.newInstance();
			}

            
            EBWLogger.trace(this, "Class Name" + className);
            
            String mode = ((EbwForm)ebwForm).getState().substring(((EbwForm)ebwForm).getState().indexOf("_") + 1, ((EbwForm)ebwForm).getState().length()); // Mode of the Screen
            String formName = className.substring(className.lastIndexOf(".") + 1, className.length());
            String value = null;

			//Get the resource Bundles for the tag's property file & the locale-specific resources
			bundle = ResourceBundle.getBundle(TagLibConstants.RESOURCE_BUNDLE_FILE_MODECOMPONENT);

            String displayComponent = PropertyFileReader.getProperty("modeTextAreaLabel." + mode); // Component type
			
			// Set the default value of the rows
			if(rows==null || rows.equals("")){
				EBWLogger.trace(this, "the rows not specified by the user");
				rows=bundle.getString("ModeTextArea.rows");
			}

			// Set the default value of the cols
			if(cols==null || cols.equals("")){
				EBWLogger.trace(this, "the coloumns not specified by the user");
				cols=bundle.getString("ModeTextArea.cols");
			}

			// Set the default value of the css
			if(css==null || css.equals("")){
				EBWLogger.trace(this, "the css class not specified by the user");
				css=bundle.getString("ModeTextArea.css");
			}


			if(displayComponent!=null){
	            if(displayComponent.equalsIgnoreCase("textarea"))
		        {   
					 value = (String)classLoad.getMethod("get" + StringUtil.initCaps(getName()), null).invoke(ebwForm, null);
					 returnInput=objCommonTemp.setTextAreaLabelHTML(displayComponent,getName(),value,getCss(),getCols(),getRows());
					 
				 } else if(displayComponent.equalsIgnoreCase("label")){
					value = (String)classLoad.getMethod("get" + StringUtil.initCaps(getName()), null).invoke(ebwForm, null);
	                returnInput=objCommonTemp.setTextAreaLabelHTML(displayComponent,getName(),value,"","","");
		        } else{

					EBWLogger.trace(this, "There is no display components for such a mode");
	                System.out.println("<br>there is no display components for such a mode");
		         }
				 outputBuffer.append(returnInput);
			}
			System.out.println("Output Buffer -:"+outputBuffer);
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
	 * @return Returns the rows.
	 */

    public String getRows()
    {
        return rows;
    }

	/**
	 * @param type The rows to set.
	 */
    public void setRows(String rows)
    {
        this.rows = rows;
    }

	/**	
	 * @return Returns the cols.
	 */

    public String getCols()
    {
        return cols;
    }

	/**
	 * @param type The css to set.
	 */
    public void setCols(String cols)
    {
        this.cols = cols;
    }

	public static void main(String args[]){

		TextAreaLabelHandler objTextAreaLableHandler=new TextAreaLabelHandler(true);
		objTextAreaLableHandler.setAttributes("textlabelfield","com.tcs.ebw.custody.formbean.OutstandingSettlementForm","searchtextfield","10","3");
		objTextAreaLableHandler.doStartTag();
	}
}