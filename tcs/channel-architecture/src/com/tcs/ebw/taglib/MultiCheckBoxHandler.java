/*
 * Created on Oct 9, 2006
 *
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */ 
package com.tcs.ebw.taglib;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.LabelValueBean;

import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.codegen.beans.ComboboxData;
import com.tcs.ebw.common.util.StringUtil;
import java.text.MessageFormat;
/**
 * @author 193350
 *
 * this is a tag handler java class which provides
 * a list of check box as a single component
 */
public class MultiCheckBoxHandler extends TagSupport{
	
	Object formObj;
	private boolean isHtmlOutput=false;
	private StringBuffer multiCheckContent=null;
	private String fileName;
	private String formname=null;
	private String name=null;
	private String css=null;
	private String serviceName;
	private String whereCondition;
	private String refFieldInfo;
	private String nodiv=null;
	private String defaultSelected=null;
	private final String checkboxcontent="<li><input type=\"checkbox\" name='\"{0}\"' id='\"{0}\"' value='\"{1}\"' {3}>{2}</li>";
	
	public String getDefaultSelected() {
		return defaultSelected;
	}
	public void setDefaultSelected(String defaultSelected) {
		this.defaultSelected = defaultSelected;
	}	
	public boolean getIsHtmlOutput() {
		return isHtmlOutput;
	}	
	public void setIsHtmlOutput(boolean isHtmlOutput) {
		this.isHtmlOutput = isHtmlOutput;
	}
	public String getFileName() {
		return fileName;
	}	
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	public String getCss() {
		return css;
	}
	public void setCss(String css) {
		this.css = css;
	}
	public String getServiceName() {
		return serviceName;
	}	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getWhereCondition() {
		return whereCondition;
	}	
	public void setWhereCondition(String whereCondition) {
		this.whereCondition = whereCondition;
	}
	public String getRefFieldInfo() {
		return refFieldInfo;
	}	
	public void setRefFieldInfo(String refFieldInfo) {
		this.refFieldInfo = refFieldInfo;
	}
	public String getNodiv() {
		return nodiv;
	}	
	public void setNodiv(String nodiv) {
		this.nodiv = nodiv;
	}
	
	
	public int doStartTag() {
		try {
			multiCheckContent=new StringBuffer();
			ComboboxData comboObj=new ComboboxData();
			final String serviceNameArg=getServiceName();
			final String whereConditionArg=getWhereCondition();
			final String refFieldInfoArg=getRefFieldInfo();
			final String filenNmaeArg=getFileName();
			final String compNameArg=getName();
			String[] selectedValues=null;
			String values=null;
			JspWriter out = null;
			Object ebwForm=null;
			String className=null;
			Class classLoad=null;
			
			if(!isHtmlOutput){
				out=pageContext.getOut();
				ebwForm = (EbwForm) pageContext.getRequest().getAttribute(formname); //DownCasting to EbwForm 
				className = ebwForm.getClass().getName();
				classLoad = Class.forName(className);
			}								
            
			Vector vecData=null;
			if (isHtmlOutput) {
				vecData=(Vector) comboObj.getData(filenNmaeArg,compNameArg);				
			} else {
				vecData=(Vector)classLoad.getMethod("get" + StringUtil.initCaps(getName()+"Collection"), null).invoke(ebwForm, null);
				selectedValues=(String[])classLoad.getMethod("get" + StringUtil.initCaps(getName()), null).invoke(ebwForm, null);
			}			
			Enumeration en = vecData.elements();	
			LabelValueBean s1=null;
			if(getNodiv()==null || getNodiv().equals("false"))
				multiCheckContent.append("<UL class=\""+getCss()+"\"><div class=\""+getCss()+"div\">");
			else
				multiCheckContent.append("<UL style=\"display:inline\" class=\""+getCss()+"\">");
			String[] defaultValues=null;			
			if(defaultSelected!=null && defaultSelected.indexOf(",")>-1)
				defaultValues=defaultSelected.split(",");
			else if(defaultSelected!=null){
				defaultValues=new String[1];
				defaultValues[0]=defaultSelected;
			}		
			while (en.hasMoreElements()) {
			    s1 = (LabelValueBean)(en.nextElement());
			    Object[] objArr={getName(),s1.getValue(),s1.getLabel(),""};
			    if(!((s1.getValue().equals("")) &&(s1.getLabel().equalsIgnoreCase("select")))){
					if (isHtmlOutput) {
						multiCheckContent.append(MessageFormat.format(checkboxcontent,objArr));
					}else {
						if(selectedValues!=null && selectedValues.length>0){							
							if(checkValues(selectedValues,s1.getValue())){
								objArr[3]="Checked";
								multiCheckContent.append(MessageFormat.format(checkboxcontent,objArr));
							}else {
								multiCheckContent.append(MessageFormat.format(checkboxcontent,objArr));
							}											
						}else if(defaultValues!=null){													
							if(checkValues(defaultValues,s1.getValue())){
								objArr[3]="Checked";
								multiCheckContent.append(MessageFormat.format(checkboxcontent,objArr));
							}else {
								multiCheckContent.append(MessageFormat.format(checkboxcontent,objArr));
							}
						}else{							
							multiCheckContent.append(MessageFormat.format(checkboxcontent,objArr));
						}
					}
			    }
			}
			if(getNodiv()==null || getNodiv().equals("false"))			
				multiCheckContent.append("</div></UL>");
			else
				multiCheckContent.append("</UL>");
			
			if (!isHtmlOutput) {
				out.print(multiCheckContent.toString());
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	public String getMultiCheckContent() {
		return multiCheckContent.toString();
	}	
	
	public boolean checkValues(String[] selectedValues,String val){
		boolean checked=false;
		for(int i=0;i<selectedValues.length;i++){
			if(selectedValues[i].equals(val)){
				checked = true;
				break;
			}				
		}
		return checked;
	}
}
