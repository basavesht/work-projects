	/*
	 * Created on January 2, 2008
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
	 * @author 221924
	 *
	 * This is a tag handler which displays
	 * a list of Radio Button as a single component
	 */
	public class MultiRadioButtonHandler extends TagSupport{
		
		Object formObj;
		private boolean isHtmlOutput=false;
		private StringBuffer multiRadioContent=null;
		private String fileName;
		private String formname=null;
		private String name=null;
		private String css=null;
		private String serviceName;
		private String whereCondition;
		private String refFieldInfo;
		private String nodiv=null;
		private String defaultSelected=null;
		private final String radiobuttoncontent="<li><input type=\"radio\" name='\"{0}\"' id='\"{0}\"' value='\"{1}\"' {3}>{2}</li>";
		
		public String getDefaultSelected() {
			return defaultSelected;
		}
		public void setDefaultSelected(String defaultSelected) {
			this.defaultSelected = defaultSelected;
		}
		
		
		public int doStartTag() {
			try {
				multiRadioContent=new StringBuffer();
				ComboboxData comboObj=new ComboboxData();				
				String selectedValues=null;
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
					vecData=(Vector) comboObj.getData(fileName,name);
				} else {
					vecData=(Vector)classLoad.getMethod("get" + StringUtil.initCaps(getName()+"Collection"), null).invoke(ebwForm, null);
					selectedValues=(String)classLoad.getMethod("get" + StringUtil.initCaps(getName()), null).invoke(ebwForm, null);
				}
				Enumeration en = vecData.elements();	
				LabelValueBean s1=null;
				if(getNodiv()==null || getNodiv().equals("false")){
					if(getCss()!=null)
						multiRadioContent.append("<UL class=\""+getCss()+"\"><div class=\""+getCss()+"div\">");
					else
						multiRadioContent.append("<UL><div>");
					
				}
				else{
					if(getCss()!=null)
						multiRadioContent.append("<UL style=\"display:inline\" class=\""+getCss()+"\">");
					else
						multiRadioContent.append("<UL style=\"display:inline\">");
				}
				while (en.hasMoreElements()) {
				    s1 = (LabelValueBean)(en.nextElement());
				    Object[] objArr={getName(),s1.getValue(),s1.getLabel(),""};
				    if(((s1.getValue().equals("")) &&(s1.getLabel().equalsIgnoreCase("select")))) {
					} else {
						if (isHtmlOutput) {
							multiRadioContent.append(MessageFormat.format(radiobuttoncontent,objArr));
						}else {
							if(selectedValues!=null && selectedValues.length()>0){
								if(checkValues(selectedValues,s1.getValue())){
									objArr[3]="checked";
									multiRadioContent.append(MessageFormat.format(radiobuttoncontent,objArr));
								}else {
									multiRadioContent.append(MessageFormat.format(radiobuttoncontent,objArr));
								}											
							}else if(defaultSelected!=null && defaultSelected.length()>0){
								if(checkValues(defaultSelected,s1.getValue())){
									objArr[3]="checked";
									multiRadioContent.append(MessageFormat.format(radiobuttoncontent,objArr));
								}else {
									multiRadioContent.append(MessageFormat.format(radiobuttoncontent,objArr));
								}
							}else{
								multiRadioContent.append(MessageFormat.format(radiobuttoncontent,objArr));
							}
						}
				    }
				}
				if(getNodiv()==null || getNodiv().equals("false"))			
					multiRadioContent.append("</div></UL>");
				else
					multiRadioContent.append("</UL>");
				
				if (!isHtmlOutput) {
					out.print(multiRadioContent.toString());
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			return SKIP_BODY;
		}
		
		public String getMultiRadioContent() {
			return multiRadioContent.toString();
		}	
		
		public boolean checkValues(String selectedValues,String val){
			boolean checked=false;
				if(selectedValues.equals(val))
					checked = true;
			return checked;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
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

		public String getFormname() {
			return formname;
		}

		public void setFormname(String formname) {
			this.formname = formname;
		}

		public boolean isHtmlOutput() {
			return isHtmlOutput;
		}

		public void setHtmlOutput(boolean isHtmlOutput) {
			this.isHtmlOutput = isHtmlOutput;
		}
	}
