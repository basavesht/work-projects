/*
 * Created on Dec 28, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.taglib;

import java.util.Enumeration;
import java.util.Vector;
import com.tcs.ebw.common.util.*;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.LabelValueBean;

import com.tcs.ebw.codegen.beans.ComboboxData;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.common.util.StringUtil;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.exception.EbwException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author 193350
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ComboTextHandler extends TagSupport{
		
		Object formObj;
		private String displayType=null;
		private String returnInput="";		// the value which will be writeen to the output buffer.
		private boolean htmlOutput = false;
		StringBuffer outputBuffer =null;	// Buffer to store the data.
		private String fileName=null;
		private String attribute=null;
		private String formname=null;
		private String name=null;
		private String css=null;
		private String maxLength=null;
		

		private ResourceBundle bundle;

		public ComboTextHandler(boolean htmlOutput) {
			 this.htmlOutput = htmlOutput;
		}
		
		public ComboTextHandler() {
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

		public int doStartTag()
		{
			
			try{
				outputBuffer = new StringBuffer();
				CommonTemplateHTML objCommonTemp=new CommonTemplateHTML();
				ComboboxData comboObj=new ComboboxData();
				JspWriter out =null;
				Object ebwForm=null;
				String className=null;
				Class classLoad=null;										// the java.lang.Class object which contains the object of the associated class.
				String mode=null;
				String formName=null;
				String value = null;
				String displayComponent=null;	
				Vector vec=null;
							
				if(!htmlOutput){
					out=pageContext.getOut();
					ebwForm= (EbwForm)pageContext.getRequest().getAttribute(formname);
					className = ebwForm.getClass().getName();
					EBWLogger.trace(this, "Class Name" + className);
					classLoad= Class.forName(className);
				}else {
					className=formname;
					classLoad=Class.forName(className);
					ebwForm=(EbwForm)classLoad.newInstance();
					System.out.println("Method name --"+classLoad.getMethod("get" + StringUtil.initCaps(getName()+"Collection"), null).getReturnType());
				}
				// Get the mode,formname and the displayComponent Type
					mode = ((EbwForm)ebwForm).getState().substring(((EbwForm)ebwForm).getState().indexOf("_")+1,((EbwForm)ebwForm).getState().length());
					formName = className.substring(className.lastIndexOf(".")+1, className.length());

				//the following code enumarates the vector value		
					vec=(Vector)classLoad.getMethod("get" + StringUtil.initCaps(getName()+"Collection"), null).invoke(ebwForm, null);
					Enumeration objEnum = vec.elements();	
					
					//Get the resource Bundles for the tag's property file & the locale-specific resources
						bundle = ResourceBundle.getBundle(TagLibConstants.RESOURCE_BUNDLE_FILE_MODECOMPONENT);
											
					    // Populate default values if user has not specified the attribute value
						// Set the default value of the css
						if(css==null || css.equals("")){
							EBWLogger.trace(this, "the css class not specified by the user");
							css=bundle.getString("ModeComboTextField.css");
						}

						// Set the default value of the maxlength
						if(maxLength==null || maxLength.equals("")){
							EBWLogger.trace(this, "the css class not specified by the user");
							maxLength=bundle.getString("ModeComboTextField.maxlength");
						}

					if(displayType==null || displayType.equals("")){
						displayComponent= PropertyFileReader.getProperty("modeComboTextField." + mode);
					}else {
						displayComponent=getDisplayType();
					}

				if(displayComponent!=null){
					if(displayComponent.equalsIgnoreCase("combobox")){
						    value = (String)classLoad.getMethod("get" + StringUtil.initCaps(getName()), null).invoke(ebwForm, null);
							returnInput=objCommonTemp.setComboOptionsTag(getName(),getAttribute(),value,objEnum);
					  }else if(displayComponent.equalsIgnoreCase("textfield")){	
							value = (String)classLoad.getMethod("get" + StringUtil.initCaps(getName()), null).invoke(ebwForm, null);
							returnInput=objCommonTemp.setTextFieldHTML(getName(),value,css,maxLength,getAttribute());
					  }else{
							EBWLogger.trace(this, "There is no display components for such a mode");
					}
						outputBuffer.append(returnInput);			
				}
				 System.out.println("Resultant Date Buffer--"+outputBuffer);
				if (out!=null){
		    		 out.println(outputBuffer);
				}
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
	
	private void destroyObjects() {

		 formname=null;
		 name=null;
		 displayType=null;
		 css=null;
		 maxLength=null;
	}

		//getters & setters for file name
		/**
		 * @return Returns the fileName.
		 */
		public String getFileName() {
			return fileName;
		}
		/**
		 * @param fileName The fileName to set.
		 */
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		
		//getters & setters for file name
		
		/**
		 * @return Returns the fileName.
		 */
		public String getAttribute() {
			return attribute;
		}
		/**
		 * @param fileName The fileName to set.
		 */
		public void setAttribute(String attribute) {
			this.attribute = attribute;
		}
		
		//getters & setters for formname
		public String getFormname() {
			return formname;
		}
		public void setFormname(String formname) {
			this.formname = formname;
		}
		
		//getters & Setters for nameofcomponet
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		//getters & setters for cssclass
		public String getCss() {
			return css;
		}
		public void setCss(String cssclass) {
			this.css = cssclass;
		}
		
		//getters & setters for maxLength
		public String getMaxLength() {
			return maxLength;
		}
		public void setMaxLength(String maxLength) {
			this.maxLength = maxLength;
		}

		/**	
		 * @return Returns the displayType.
		 */
		public String getDisplayType()
		{
			return displayType;
		}

		/**
		 * @param type The name to set.
		 */
		public void setDisplayType(String displayType)
		{
			this.displayType = displayType;
		}

	public static void main(String args[]){
		ComboTextHandler objComboTextHandler=new ComboTextHandler(true);
		objComboTextHandler.setAttributes("orderstatus","com.tcs.ebw.custody.formbean.OutstandingSettlementForm","","","");
		objComboTextHandler.doStartTag();
	}
}
