/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.taglib;


import javax.security.auth.Subject;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


import java.util.ResourceBundle;
import java.util.MissingResourceException;

import com.tcs.ebw.taglib.TagLibConstants;
import com.tcs.ebw.exception.EbwException;
import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;

/**
 * This taglibrary is used for the serach template. 
 * It takes type, name, value, alt, formname, txtMaxlength,findBtnStyletype,saveBtnStyletype 
 * txtMaxlength, findBtnStyleClass, findBtnStyleClass, findBtnImage, saveBtnImage
 * as parameters.BtnStyletype parameter is used for changing the button type. 
 * It can be Link, Image or a button. If the type is not given, 
 * it reads from SearchTemplateResource.properties file to get the default 
 * button type.
 *
 *
 * This taglibrary is added in a JSP file by JSPCreator for struts code 
 * type.
 * 
 * @author 164109
 */
public class SearchTemplate extends TagSupport{

	private String name=null;                   // name of the search template tag
	private String value=null;		
	private String formname = null;  
	private String alt=null;
	private String txtMaxlength=null;
	private String txtStyleClass=null;
	private String findBtnStyleClass=null;
	private String saveBtnStyleClass=null;
	private String findBtnStyletype=null;
	private String saveBtnStyleType=null;
	private String findBtnImage=null;
	private String saveBtnImage=null;
	private StringBuffer outputBuffer = null;    // Buffer to store the data 
	private ResourceBundle bundle;
	private ResourceBundle languageRsBundle;
	private String searchTemp="";
	private boolean htmlOutput = false;
	private String onclick="";
	//private String action="";
	
	static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SearchTemplate.class.getName()); 
	
		
	public SearchTemplate(boolean htmlOutput) {
		 this.htmlOutput = htmlOutput;
	}
	
	public SearchTemplate() {
	    this.htmlOutput = false;
	}

	public void setAttributes(String name,String formname,String txtMaxlength,String txtStyleClass,String findBtnStyleClass,String saveBtnStyleClass,String findBtnStyletype,String saveBtnStyleType,String findBtnImage,String saveBtnImage){
		//this.action=action;
		this.name=name;
		this.formname=formname;
		this.txtMaxlength=txtMaxlength;
		this.txtStyleClass=txtStyleClass;
		this.findBtnStyleClass=findBtnStyleClass;
		this.saveBtnStyleClass=saveBtnStyleClass;
		this.findBtnStyletype=findBtnStyletype;
		this.saveBtnStyleType=saveBtnStyleType;
		this.findBtnImage=findBtnImage;
		this.saveBtnImage=saveBtnImage;
	}
		
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {

			return super.doEndTag();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		
		String txtSearchTemplate="";
		String imageSrc="";
		String buttonImg="";
		String buttonValue="";
		String saveButtonValue="";
	    String findButtonValue="";
	    String savebtnValue="";
		String txtSearchTemplateKey="";
		String findBtnTooltip="";
		String saveBtnTooltip="";
		String findBtnalt="";
		String saveBtnalt="";
		String propfindBtnTooltip="";
		String propsaveBtnTooltip="";
		String lableStyleClass="";
		String linkAlign="";

		CommonTemplateHTML objTemp=new CommonTemplateHTML();
		try {
			JspWriter out=null;
			if(!htmlOutput){
				out= pageContext.getOut();
				pageContext.getSession().setAttribute("action", "search");
	           //     pageContext.getSession().setAttribute("action", action);
	        }
			outputBuffer = new StringBuffer();
						
			//Get the resource Bundles for the tag's property file & the locale-specific resources
			bundle = ResourceBundle.getBundle(TagLibConstants.RESOURCE_BUNDLE_FILE_SEARCHTEMPLATE);
			languageRsBundle=ResourceBundle.getBundle(TagLibConstants.RESOURCE_BUNDLE_FILE_TAGLIBMULTILINGUAL);

			//Get the values from the tag property resource bundle
			txtSearchTemplateKey=bundle.getString("SearchTemplate.label");  // label value key
			imageSrc=bundle.getString("SaveTemplateBtn.image");				// save button image
			buttonImg=bundle.getString("FindTemplateBtn.image");			// find button image
			findButtonValue=bundle.getString("FindTemplateBtn.value");		// find button value key
			saveButtonValue=bundle.getString("SaveTemplateBtn.value");		// save button value key 
			propfindBtnTooltip=bundle.getString("FindTemplateBtn.tooltip"); // find button tool tip
			propsaveBtnTooltip=bundle.getString("SaveTemplateBtn.tooltip"); // save button tool tip
			findBtnalt=bundle.getString("FindTemplateBtn.alt");				// find button alt key 
			saveBtnalt=bundle.getString("SaveTemplateBtn.alt");				// save button alt key 
			lableStyleClass=bundle.getString("SearchTemplate.class");		// label css class
			linkAlign=bundle.getString("SaveTemplateBtn.imgAlignMid");		// alignment for the link
			
			//Get the values from the locale-specific resource bundle
			buttonValue=languageRsBundle.getString(findButtonValue);                    // find button value
			savebtnValue=languageRsBundle.getString(saveButtonValue);					// save button value
			txtSearchTemplate=languageRsBundle.getString(txtSearchTemplateKey.trim());  // label value
			findBtnTooltip=languageRsBundle.getString(propfindBtnTooltip);				// find button alt
			saveBtnTooltip=languageRsBundle.getString(propsaveBtnTooltip);				// save button alt
				
		    // Populate default values if user has not specified the attribute value
			// Set default value for maxlenght for the search template text field
			if(txtMaxlength==null){
				txtMaxlength=bundle.getString("SearchTemplateTxt.maxlength");
			}
			// Populate alinment fot he buttontype as link
			
			if(txtMaxlength==null){
				txtMaxlength=bundle.getString("SearchTemplateTxt.maxlength");
			}
			
			// Set default css class  for the search template text field
			if(txtStyleClass==null || txtStyleClass.equals("")){

   				EBWLogger.logDebug(this,"Text Style Class CSS  is not given by user");	
				txtStyleClass=bundle.getString("FindTemplateTxt.class");
			}
			
			// Set default style type  for the search template find button
			if(findBtnStyletype==null || findBtnStyletype.equals("")){
				EBWLogger.logDebug(this,"Find  Button CSS  Class is not  given by user");	
				findBtnStyletype=bundle.getString("FindTemplateBtn.style");
			}

			// Set default style type  for the search template savebutton
			if(saveBtnStyleType==null || saveBtnStyleType.equals("")){
				EBWLogger.logDebug(this,"Save Button CSS  Class is not  given by user");	
				saveBtnStyleType=bundle.getString("SaveTemplateBtn.style");
			}
			
			// Set default css class  for the search template find button
			if(findBtnStyleClass==null || findBtnStyleClass.equals("")){
				EBWLogger.logDebug(this,"Find  Button CSS  Class is not  given by user");	
				findBtnStyleClass=bundle.getString("FindTemplateBtn.class");
			}else {
				EBWLogger.logDebug(this,"Find  Button CSS  Class is given by user");	
			 }
			 
			// Set default css class  for the search template save button
			if(saveBtnStyleClass==null || saveBtnStyleClass.equals("")){
				EBWLogger.logDebug(this,"Save Button CSS  Class is not  given by user");	
				saveBtnStyleClass=bundle.getString("SaveTemplateBtn.class");
			}else {
				EBWLogger.logDebug(this,"Save Button CSS  Class is given by user");  
				}
			
			// Set default image src for the search template find button
			if(findBtnImage==null || findBtnImage.equals("")){
				EBWLogger.logDebug(this,"Find  Button Image for the Button Type as Image is not given by user");
				findBtnImage=bundle.getString("FindButton.image");
			}else {
				EBWLogger.logDebug(this,"Find  Button Image for the Button Type as Image is given by user");
			}

			// Set default image src for the search template save button
			if(saveBtnImage==null || saveBtnImage.equals("")){
				EBWLogger.logDebug(this,"Save  Button Image for the Button Type as Image is not given by user");
			    saveBtnImage=bundle.getString("SaveButton.image");
			}else {
				EBWLogger.logDebug(this,"Save  Button Image for the Button Type as Image is given by user");

			}
			
			// Dump the Label, TextField, & the Save/Find buttons

			// Print out Label & TextField
			String labelTxtString=objTemp.setLabelTextHTML(txtSearchTemplate,txtStyleClass,txtMaxlength,lableStyleClass);
			outputBuffer.append(labelTxtString);
			//Code Is Now Working//
			String option=PropertyFileReader.getProperty("templateOption");
			/*if(option.equalsIgnoreCase("true"))
				onclick="javascript:if(document.forms[0].templateInfo.value==\'\'){alert(\'Please enter the search template name\');document.forms[0].templateInfo.focus();}else{openTemplateInfoDialog(document.forms[0],\'btnSaveTemp\');}";
			else
				onclick="javascript:if(document.forms[0].templateInfo.value==\'\'){alert(\'Please enter the search template name\');document.forms[0].templateInfo.focus();}else{document.forms[0].action.value=\'btnSaveTemp\';document.forms[0].submit();}";*/
			
			
			
			//added because of export problem +pagination
			
			if(option.equalsIgnoreCase("true"))
				//onclick="javascript:if(document.forms[0].templateInfo.value==\'\'){alert(\'Please enter the search template name\');document.forms[0].templateInfo.focus();}else{openTemplateInfoDialog(document.forms[0],\'btnSaveTemp\');}";
				onclick="javascript:if(document.forms[0].templateInfo.value==\'\'){alert(\'Please enter the search template name\');document.forms[0].templateInfo.focus();}else{resetPgnHiddenVariableValues(document.forms[0]);openTemplateInfoDialog(document.forms[0],\'btnSaveTemp\');}";
			else
				onclick="javascript:if(document.forms[0].templateInfo.value==\'\'){alert(\'Please enter the search template name\');document.forms[0].templateInfo.focus();}else{resetPgnHiddenVariableValues(document.forms[0]);document.forms[0].action.value=\'btnSaveTemp\';document.forms[0].submit();}"; 
			
			//onclick="javascript:templateNameChk(document.forms[0].validationfailed);if(validationfailed.value==\'false\') return false; document.forms[0].action.value=\'btnSaveTemp\';document.forms[0].submit();";
		
			
			//Print out Save Button
			if(saveBtnStyleType!=null && saveBtnStyleType.equals(TagLibConstants.EBWBUTTON_TYPE_LINK)){
					 
					 // If the Save Button Style is Link
				   searchTemp=objTemp.SearchTempBtn("","","","",imageSrc,saveBtnTooltip,onclick,saveBtnStyleType,linkAlign);		 			 
				 }else if(saveBtnStyleType!=null && saveBtnStyleType.equals(TagLibConstants.EBWBUTTON_TYPE_IMAGE)){
					
					 // If the Save Button Style is Image
					  searchTemp=objTemp.SearchTempBtn(name,saveBtnalt,"","",saveBtnImage,saveBtnTooltip,onclick,saveBtnStyleType,"");
					
				 }else if(saveBtnStyleType!=null && saveBtnStyleType.equals(TagLibConstants.EBWBUTTON_TYPE_BUTTON)){
					  
					  // If the Save Button Style is Button 
					   searchTemp=objTemp.SearchTempBtn(name,savebtnValue,savebtnValue,saveBtnStyleClass,"",saveBtnTooltip,onclick,saveBtnStyleType,"");
				 }
				 outputBuffer.append(searchTemp);
			     onclick="javascript:openTemplateListDialog(document.forms[0],'btnOpenTemp');";
			// Print out Find Button
			if(findBtnStyletype!=null && findBtnStyletype.equals(TagLibConstants.EBWBUTTON_TYPE_BUTTON)){
			
					// If the Find Button Style is Button 
					   searchTemp=objTemp.SearchTempBtn(name,buttonValue,buttonValue,findBtnStyleClass,"",findBtnTooltip,onclick,findBtnStyletype,"");
					   outputBuffer.append(searchTemp);

				}else if(findBtnStyletype!=null && findBtnStyletype.equals(TagLibConstants.EBWBUTTON_TYPE_LINK)){
				
					// If the Find Button Style is Link 
					   searchTemp=objTemp.SearchTempBtn("","","","",buttonImg,findBtnTooltip,onclick,findBtnStyletype,linkAlign);
					   outputBuffer.append(searchTemp);
					  

			   }else if(findBtnStyletype!=null && findBtnStyletype.equals(TagLibConstants.EBWBUTTON_TYPE_IMAGE)){
				
				// If the Find Button Style is Image
					   searchTemp=objTemp.SearchTempBtn(name,findBtnalt,"","",findBtnImage,findBtnTooltip,onclick,findBtnStyletype,"");
					   outputBuffer.append(searchTemp);
			   }

			System.out.println("Buffer Result###### "+outputBuffer);
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
		catch(Exception exp){
			new EbwException(this,exp).printEbwException();
		}
		
		return SKIP_BODY;
	}
	
	
	private void destroyObjects() {
		
		name=null;
		value=null;
		formname = null;
		alt=null;
		txtMaxlength=null;
		txtStyleClass=null;
		findBtnStyletype=null;
	    saveBtnStyleType=null;
		findBtnImage=null;
		saveBtnImage=null;
	}
	
					
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		// TODO Auto-generated method stub
		super.release();
	}
	

	

	/**
	 * @return Returns the txtMaxlength.
	 */
	public String getTxtMaxlength() {
		return txtMaxlength;
	}
	/**
	 * @param type The txtMaxlength to set.
	 */
	public void setTxtMaxlength(String txtMaxlength) {
		this.txtMaxlength = txtMaxlength;
	}

	/**
	 * @return Returns the findBtnStyletype.
	 */
	public String getFindBtnStyletype() {
		return findBtnStyletype;
	}
	/**
	 * @param type The findBtnStyletype to set.
	 */
	public void setFindBtnStyletype(String findBtnStyletype) {
		this.findBtnStyletype = findBtnStyletype;
	}

	/**
	 * @return Returns the findBtnStyleclass.
	 */
	public String getFindBtnStyleClass() {
		return findBtnStyleClass;
	}
	/**
	 * @param type The findBtnStyleClass to set.
	 */
	public void setFindBtnStyleClass(String findBtnStyleClass) {
		this.findBtnStyleClass = findBtnStyleClass;
	}


	/**
	 * @return Returns the saveBtnStyleType.
	 */
	public String getSaveBtnStyleType() {
		return saveBtnStyleType;
	}
	/**
	 * @param type The saveBtnStyleType to set.
	 */
	public void setSaveBtnStyleType(String saveBtnStyleType) {
		this.saveBtnStyleType = saveBtnStyleType;
	}

	/**
	 * @return Returns the saveBtnStyleClass.
	 */
	public String getSaveBtnStyleClass() {
		return saveBtnStyleClass;
	}
	/**
	 * @param type The saveBtnStyleType to set.
	 */
	public void setSaveBtnStyleClass(String saveBtnStyleClass) {
		this.saveBtnStyleClass = saveBtnStyleClass;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	

	/**
	 * @return Returns the alt.
	 */
	public String getAlt() {
		return alt;
	}
	/**
	 * @param alt The alt to set.
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}

	/**
	 * @return Returns the txtStyleClass.
	 */
	public String getTxtStyleClass() {
		return txtStyleClass;
	}
	/**
	 * @param txtStyleClass The txtStyleClass to set.
	 */
	public void setTxtStyleClass(String txtStyleClass) {
		this.txtStyleClass = txtStyleClass;
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
	 * @return Returns the findBtnImage.
	 */
	public String getFindBtnImage() {
		return findBtnImage;
	}
	/**
	 * @param value The findBtnImage to set.
	 */
	public void setFindBtnImage(String findBtnImage) {
		this.findBtnImage = findBtnImage;
	}


	/**
	 * @return Returns the saveBtnImage.
	 */
	public String getSindBtnImage() {
		return saveBtnImage;
	}
	/**
	 * @param value The findBtnImage to set.
	 */
	public void setSaveBtnImage(String saveBtnImage) {
		this.saveBtnImage = saveBtnImage;
	}
	
    /**
     * @return Returns the outputBuffer.
     */
    public StringBuffer getOutputBuffer() {
        return outputBuffer;
    }
    /**
     * @param outputBuffer The outputBuffer to set.
     */
    public void setOutputBuffer(StringBuffer outputBuffer) {
        this.outputBuffer = outputBuffer;
    }
    
    
    /**
     * @return Returns the formname.
     */
    public String getFormname() {
        return formname;
    }
    /**
     * @param formname The formname to set.
     */
    public void setFormname(String formname) {
        this.formname = formname;
    }
    
    /**
	 * @return Returns the saveBtnImage.
	 */
	/*public String getAction() {
		return action;
	}
	/**
	 * @param value The findBtnImage to set.
	 
	public void setAction(String action) {
		this.action = action;
	}*/


	public static void main(String args[]){

		try{
			SearchTemplate searchTemp=new SearchTemplate(true);
			//searchTemp.setAttributes("searchtemplate","OutstandingSettlementForm","30","searchtextfield","buttoncls","","Button","Link","../images/UpArrow.gif","../images/UpArrow.gif","action");
			searchTemp.doStartTag();
		}catch(JspException e){
			System.out.println(e);
		}
	}
}
