/*
 * Created on Mar 05,2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.taglib;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.LabelValueBean;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.StringUtil;
import com.tcs.ebw.mvc.validator.EbwForm;
//import com.tcs.ebw.taglib.CommonTemplateHTML;
import com.tcs.ebw.taglib.TagLibConstants;


/**
 * @author 164109
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@SuppressWarnings("serial")

public class EbwComboGrid  extends TagSupport {
	
	private boolean htmlOutput = false; 
	private StringBuffer outputBuffer;
	//private String submitPage; 
	private String formname=null;
	private String name=null;
	private String returnInput="";
	private String attribute;
	private ResourceBundle bundle=null;
	private String combo=null;
	private String txtStyleClass=null;
	private ResourceBundle appRB = null;
	private ResourceBundle formRB = null;
	private String comboGridTitle="";
	/**  
	 *  
	 */
	public EbwComboGrid() 
	{
		//super(); 
		try
		{		 
			
			/** 
			 * Loading the Application's Property file that contains the location of
			 * the property file that contains the displaynames
			 */
			appRB = ResourceBundle.getBundle("ApplicationConfig");
					
			if (!htmlOutput) { 
				/** 
				 * Loading up the properties file  that contains the fieldId and Displaynames as key, value respectively
				 */          
				//formRB = ResourceBundle.getBundle(appRB.getString("message-resources"));
				
				formRB=ResourceBundle.getBundle("com.tcs.MessageResources");		
			}
			
			//comboGridTitle =formRB.getString(formname.substring(0, formname.lastIndexOf("Form")) + "." + name);
		
		}catch(Exception e)
		{ 
			System.out.println ("Resource Bundle Not found..."+"::::::"+ e);
		}
		// TODO Auto-generated constructor stub
	}
	
	public int doStartTag() throws JspException 
	{		
		try
		{			
			Object ebwForm=null;
			String className=null;
			Class classLoad=null;// the java.lang.Class object which contains the object of the associated class.
						
//			if(combo.equalsIgnoreCase("Y"))
//		{				
				System.out.println("Inside dostart Tag "+combo);

				//cboName="sel" txtName="entry"

				//Object ebwForm=null;
				//CommonTemplateHTML objCommonTemp=new CommonTemplateHTML();
				//String className=null;
				String mode=null;
				String formName=null;
				//Class classLoad=null;// the java.lang.Class object which contains the object of the associated class.
				Vector vec=null;
				ArrayList objGridHeader=new ArrayList();
				JspWriter out = null;
				String value = null;			
				String divID="div"+name;
				String txtId="txt"+name;
				String gridId="grid"+name;
				String divClass=null;
				String gridClass=null; 
				String textClass=null;
				String imageSource=null;			
				String combo=null;
				 
				outputBuffer= new StringBuffer();
				
				//Get the resource Bundles for the tag's property file & the locale-specific resources
				bundle = ResourceBundle.getBundle(TagLibConstants.RESOURCE_BUNDLE_FILE_MODECOMPONENT);
									
			    // Populate default values if user has not specified the attribute value
				// Set the default value of the css
				if(txtStyleClass==null || txtStyleClass.equals(""))
				{
					EBWLogger.trace(this, "the css class not specified by the user");
					textClass=bundle.getString("autoCompleteText.css");
				}
				else
				{
					textClass=txtStyleClass;
				}
				
				if(divClass==null || divClass.equals(""))
				{
					EBWLogger.trace(this, "the css class not specified by the user");
					divClass=bundle.getString("autoCompleteDivTable.css");
				}
				
				if(gridClass==null || gridClass.equals(""))
				{
					EBWLogger.trace(this, "the css class not specified by the user");
					gridClass=bundle.getString("comboGrid.css");
				}
				
				if(imageSource==null || imageSource.equals(""))
				{
					EBWLogger.trace(this, "the css class not specified by the user");
					imageSource=bundle.getString("autoComplete.image");
				}
				
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
				}
										
				outputBuffer.append((new StringBuilder("\n<table style=\"position:absolute\" > <tr><td>")).toString());
				outputBuffer.append((new StringBuilder("\n<div> ")).toString());
				outputBuffer.append((new StringBuilder("\n<input type=\"text\"")).toString()); 
				 
				outputBuffer.append((new StringBuilder(" name=\"")).append(name).append("\" size=\"36\" id=\"").append(txtId).append("\" " +
						"onKeyUp=\"findIt(arrValueTab,arrTextTab,'").append(gridId).append("','").append(txtId).append("','").append(divID).append("')\" autocomplete=\"off\" " +
						"onKeyDown=\"changeFocus('").append(gridId).append("','").append(txtId).append("','").append(divID).append("','").append("document.forms[0]."+name).append("')\""+
						"onblur=\"focusLost('").append(divID).append("')\""+						
						" class=\""+textClass+"\" onclick=\"setTableData('").append(divID).append("','").append(gridId).append("','").append(txtId).append("');\" >").toString());
				 
				outputBuffer.append((new StringBuilder("<IMG  SRC=\""+imageSource+"\" WIDTH=\"18\" HEIGHT=\"21\" border=\"0\" align=\"absbottom\" style=\"position:absolute\" onclick=\"setTableData('").append(divID).append("','").append(gridId).append("','").append(txtId).append("');\" >")));
						//"onload=\"setComboData('")).append(txtId).append("','").append(gridId).append("','").append(divID).append("');\">").toString());
				outputBuffer.append((new StringBuilder("<div id=\"")).append(divID).append("\" class=\"" + divClass + "\" style=\"display:none;\">").toString());
				
				//Get the mode,formname and the displayComponent Type 
				mode = ((EbwForm)ebwForm).getState().substring(((EbwForm)ebwForm).getState().indexOf("_")+1,((EbwForm)ebwForm).getState().length());
				formName = className.substring(className.lastIndexOf(".")+1, className.length());
				
				vec=(Vector)classLoad.getMethod("get" + StringUtil.initCaps(getName()+"Collection"), null).invoke(ebwForm, null);
  
			  objGridHeader=(ArrayList)classLoad.getMethod("load" + StringUtil.initCaps(getName()+"Configuration"), null).invoke(ebwForm, null);	
				
				
				value="";
				returnInput=setComboGridTag(getName(),getAttribute(),value,vec,gridClass,objGridHeader);
				
				
				outputBuffer.append((new StringBuilder(returnInput.toString())).toString());
	
				outputBuffer.append((new StringBuilder("\n</div>")).toString());
				outputBuffer.append((new StringBuilder("\n</div>")).toString());
				outputBuffer.append((new StringBuilder("\n </td></tr></table>")).toString());
	
				out.print(outputBuffer);
			    destroyObjects();
			    System.out.println("Here it Ends");

			} catch (Exception exc) {
		        System.out.println (exc.getMessage());
			}
			return SKIP_BODY;
	}
	
	public int doEndTag() throws JspException 
	{		
		return 0;
		//return super.doEndTag();
	}
	
	private void destroyObjects()
    {
		//submitPage=null;
		outputBuffer=null;
		name=null;
		formname = null;
		txtStyleClass=null;
    }
	
	//
	 /* (non-Javadoc)
	 *doAfterBody Tag
	 */
	
	
	public int doAfterBody()
     throws JspException
	 {
	   return super.doAfterBody();
	   
	 }

//	Method to Dump the Combobox's Select Options with size to feel it as a list
	public String setComboGridTag(String name,String attribute,String value,Vector enteries, String gridClass ,ArrayList gridHeader)
	{
			String gridTag="";
			String txtId="txt"+name;
			String cboId="cbo"+name;
			String divId="div"+name;
			String gridId="grid"+name;
			int count=0;  
		 
			gridTag="<table class=\""+gridClass+"\""+" name=\""+ gridId+"\""+" id="+"\""+gridId+"\""+" border=\"1\">";
			gridTag=gridTag+"<tr>";
			if(gridHeader!=null && gridHeader.size() > 0){
				for(int j=0;j< gridHeader.size() ;j++){
					gridTag=gridTag+"<th class='locked'>"+formRB.getString(gridHeader.get(j).toString())+"</th>";
				}
			}
			//gridTag=gridTag+"<tr><th>Security Id</th><th>Seurity Description</th><th>Seurity Description</th></tr>";
			
			gridTag=gridTag+"</tr>";
			
			Vector vecData=new Vector();
			 for (int i=0, j=enteries.size(); i < j; i++) {
				 
				 vecData=(Vector)enteries.get(i);
				 				
				 if(i % 2==0){
						gridTag=gridTag+"<tr id=\"" + name + "_" + i + "\" class=\"evenrow\" >";
					}else{
						
						gridTag=gridTag+"<tr id=\"" + name + "_" + i + "\" class=\"oddrow\" >";
					}				 
					 if (vecData!= null && vecData.size() > 0){
						 for(int k=0;k < vecData.size();k++){
							 gridTag =gridTag+"<td nowrap >"+vecData.get(k)+ "</td>";
						 }
					}				 
					gridTag=gridTag+"</tr>";
			 }			
			 gridTag=gridTag+"</table>";
			 String inputval=gridTag;
			return inputval;
	}
	
	public void release()
    {
        super.release();
    }

	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAttribute() {
		return attribute;
	}

	public String getTxtStyleClass() {
		return txtStyleClass;
	}

	public void setTxtStyleClass(String txtStyleClass) {
		this.txtStyleClass = txtStyleClass;
	}


}
