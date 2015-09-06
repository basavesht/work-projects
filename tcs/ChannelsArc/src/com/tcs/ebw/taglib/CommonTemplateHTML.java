/*
 * Copyright (c) Tata Consutlancy Services, Inc. All Rights Reserved.
 * This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */
package com.tcs.ebw.taglib;

import com.tcs.ebw.taglib.TagLibConstants;
import org.apache.struts.util.LabelValueBean;
import java.util.Enumeration;

public class CommonTemplateHTML{

	// Method to set the label and the input tags
	public String setLabelTextHTML(String txtSearchTemplate,String txtCssClass,String txtmaxlength,String lableStyleClass){
		
			String labelVal="<label class=\""+lableStyleClass+"\" >" +txtSearchTemplate+" </label>"+"&nbsp;";
			String textVal="<input type=\"text\"" +" name=\"templateInfo\""+" value=\"\"" +" class=\"" +txtCssClass+ "\""+" maxlength=\""+txtmaxlength+"\"" +" >"+"&nbsp;";
			String inputval=labelVal+textVal;
			return inputval;
	}
	
	// Method to dump the TextArea /Label bases pn the display component type
	public String setTextAreaLabelHTML(String componentType,String name,String value,String txtCssClass,String cols,String rows){
			
			String inputval="";
			String textVal=null;
			if(componentType.equalsIgnoreCase("textarea")){
				if(value!=null){
					textVal="<textarea  name=\""+name+"\""+" value=\""+value+"\"" +" class=\"" +txtCssClass+ "\""+" cols=\""+cols+"\""+" rows=\""+rows+"\""+" >"+"</textarea>"+"&nbsp;";
				}else {
					textVal="<textarea  name=\""+name+"\""+" value=\"\"" +" class=\"" +txtCssClass+ "\""+" cols=\""+cols+"\""+" rows=\""+rows+"\""+" >"+"</textarea>"+"&nbsp;";
				}
				inputval=textVal;
			}else if(componentType.equalsIgnoreCase("label")){

				String labelVal=setLabel(value);
				inputval=labelVal;
			}
				return inputval;
	}


	// Method dump the tags based on the  Button Style Type and returns the string.
	public String SearchTempBtn(String name,String alt,String value,String btnCssClass,String btnImage,String btnTooltip,String onclickevent,String btntype,String linkAlign){
	
		String tempname="";
		String tempalt="";
		String tempvalue="";
		String onclick="";
		String inputType="";
		String srcval="";
		String buttonCss="";
		String butonImage="";
		String tooltip="";
		String inputtype="";
      
		onclick=" onclick=\" "+onclickevent+"\"";

		if(btntype.equals(TagLibConstants.EBWBUTTON_TYPE_BUTTON)){
			tempname="name=\""+name+"_btn\"";
			tempalt=" alt=\""+alt+"\"";
			tempvalue=" value=\""+value+"\"";
			buttonCss=" class=\""+btnCssClass+"\"";
			if(!btnTooltip.equals("")){
				tooltip=" title=\""+ btnTooltip +"\"";
			}
			
		    inputType="<input type=\"button\" "+tempname+tempalt+tempvalue+buttonCss+tooltip+onclick+">"+"&nbsp;";

		}else if(btntype.equals(TagLibConstants.EBWBUTTON_TYPE_IMAGE)){
			tempname="name=\""+name+"_image\"";
			
				if(btnImage.equals("")){
					tempalt=" alt=\""+alt+"\"";
				}else{
					 tempalt=" alt=\"\"";
				}
				
			    if(!btnTooltip.equals("")){
					tooltip=" title=\""+ btnTooltip +"\"";
			    }
			
     		srcval=" src=\"" + btnImage + "\"";
		    inputType="<input type=\"image\" "+tempname+tempalt+srcval+tooltip+onclick+">"+"&nbsp;";

		}else if(btntype.equals(TagLibConstants.EBWBUTTON_TYPE_LINK)){
		
			inputType=setAnchorTag(btntype,onclickevent,btnImage,btnTooltip,linkAlign);
			  
			}
		 return inputType;
	}
	
	// Method to dump the Anchor tag
	public String setAnchorTag(String componentType,String onclick,String image, String tooltip,String linkAlign){
		
		String alt="";
		String anchor="";

		if(!tooltip.equals("")){
			alt=" alt=\""+ tooltip +"\"";
		}
		
		anchor="<a href=\"#\""+" onclick=\""+onclick+"\">";
		String imagetag="<img src=\""+image+"\""+"align=\""+linkAlign+"\""+alt +" border=0 ></a>"+"&nbsp;";

		return (anchor+imagetag);
	}
	
	// Method to dump the TextField
	public String setTextFieldHTML(String name,String value,String txtCssClass,String txtmaxlength,String attribute){
			String textVal="";
			if(value!=null){
				textVal="<input type=\"text\"" +" name=\""+name+"\""+" value=\""+value+"\"" +" class=\"" +txtCssClass+ "\""+" maxlength=\""+txtmaxlength+"\"" +attribute+" >"+"&nbsp;";
			}else {
				textVal="<input type=\"text\"" +" name=\""+name+"\""+" value=\"\"" +" class=\"" +txtCssClass+ "\""+" maxlength=\""+txtmaxlength+"\"" +" >"+"&nbsp;";
			}
			String inputval=textVal;
			return inputval;
	}
	
	// Method to Dump the Label
	public String setLabel(String labelValue){
		String labelVal="";
		if(labelValue!=null){
			labelVal="<label> "+labelValue+" </label>"+"&nbsp;";
		}else {
			labelVal="<label>"+""+"</label>"+"&nbsp;";
		}
		return labelVal;
	}
	
	// Method to Dump the Combobox's Select Options
	public String setComboOptionsTag(String name,String attribute,String value,Enumeration enum1 ){
			String comboSelect="";
			LabelValueBean labelVal=null;
			comboSelect="<SELECT name='"+name+"' id='"+name+"' "+attribute+">";
			while (enum1.hasMoreElements()) {
						labelVal = (LabelValueBean)(enum1.nextElement());
						if(labelVal.getValue().equals(value)){
						    	comboSelect=comboSelect+"<OPTION value='"+labelVal.getValue()+"' selected=\"selected\">" +labelVal.getLabel()+"</OPTION>";
						} else {
						    	comboSelect=comboSelect+"<OPTION value='"+labelVal.getValue()+"'>" +labelVal.getLabel()+"</OPTION>";
						}
		   }
			String inputval=comboSelect;
			return inputval;
	}
	
//	Method to Dump the Combobox's Select Options with size to feel it as a list
	public String setComboOptionsTag(String name,String attribute,String value,Enumeration enum1, String size ,String onchangefunc)
	{
			String comboSelect="";
			LabelValueBean labelVal=null;
			String txtId="txt"+name;
			String cboId="cbo"+name;			
			String divId="div"+name;
			
			//comboSelect="<select name="+"\""+name + "\""+" id='" + cboId + "' size='"+size+"' onclick=" +"\"setText('" + cboId + "','" + txtId + "','" + divId + "',"+name+");\" style=\"position:absolute\"" +">";
			
			if(onchangefunc==null){
			comboSelect="<select name='"+name + "' id='" + cboId + "' size='"+size+"' onclick=" +
					 "\"setText('" + cboId + "','" + txtId + "','" + divId + "',"+name+");\" style=\"position:absolute\"" +">";
			}else if(onchangefunc!=null){
				onchangefunc=onchangefunc.substring(onchangefunc.indexOf(":")+1,onchangefunc.length());
				comboSelect="<select name='"+name + "' id='" + cboId + "' size='"+size+"' onclick=" +
				 "\"setText('" + cboId + "','" + txtId + "','" + divId + "',"+name+");"+"\" onchange=\""+"setText('" + cboId + "','" + txtId + "','" + divId + "',"+name+");"+onchangefunc+"\""+" style=\"position:absolute\"" +">";
			}
			
			while (enum1.hasMoreElements()) 
			{
				labelVal = (LabelValueBean)(enum1.nextElement());
				if(labelVal.getValue().equals(value))
				{
				    	comboSelect=comboSelect+"<OPTION value='"+labelVal.getValue()+"' selected=\"selected\">" +labelVal.getLabel()+"</OPTION>";
				} 
				else 
				{
				    	comboSelect=comboSelect+"<OPTION value='"+labelVal.getValue()+"'>" +labelVal.getLabel()+"</OPTION>";
				}
		   }
			comboSelect=comboSelect+"</select>";
			return comboSelect;
	}
}
