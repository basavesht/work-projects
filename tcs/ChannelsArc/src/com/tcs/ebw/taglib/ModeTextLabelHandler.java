/*
 * Created on Sep 28, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.taglib;
import java.lang.reflect.Method;

import com.tcs.ebw.common.util.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.common.util.EBWLogger;
/**
 * @author 193350
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ModeTextLabelHandler extends TagSupport {
	
	
	Object formObj;
	
//	getters & setters for formname
	private String formname=null;
	
	public String getFormname() {
		return formname;
	}
	public void setFormname(String formname) {
		this.formname = formname;
	}
	
	//getters & Setters for nameofcomponet
	private String name=null;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	//getters & setters for cssclass
	private String css=null;
	public String getCss() {
		return css;
	}
	public void setCss(String css) {
		this.css = css;
	}
	
	public int doStartTag()
	{
		
		try{
			JspWriter out = pageContext.getOut();
			Object ebwForm = (EbwForm) pageContext.getRequest().getAttribute(formname); //DownCasting to EbwForm 
			String className = ebwForm.getClass().getName();
			EBWLogger.trace(this,"Class Name"+className);
			Class classLoad = Class.forName(className);
			String mode = ((EbwForm)ebwForm).getState().substring(((EbwForm)ebwForm).getState().indexOf("_")+1,((EbwForm)ebwForm).getState().length());
			String formName = className.substring(className.lastIndexOf(".")+1, className.length());
			
			
			String value = null;						    
			String displayComponent=PropertyFileReader.getProperty("modeTextLabel."+mode);
		if(displayComponent.equalsIgnoreCase("textfield") && ((mode.equalsIgnoreCase("new"))||(mode.equalsIgnoreCase("edit"))||(mode.equalsIgnoreCase("init")))) {
			out.print("<input type=text name="+getName()+" >");
			value = (String)classLoad.getMethod("get" + StringUtil.initCaps(getName()), null).invoke(ebwForm, null);
		}
		
	
		else if(displayComponent.equalsIgnoreCase("label") && (mode.equalsIgnoreCase("view")))
		{	
			value = (String)classLoad.getMethod("get" + StringUtil.initCaps(getName()), null).invoke(ebwForm, null);
			out.print("<label>"+value+ "</label>");
		}
		else
		{
			System.out.println("<br>there is no display components for such a mode");
		}
						
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return SKIP_BODY;
	
	}
}
