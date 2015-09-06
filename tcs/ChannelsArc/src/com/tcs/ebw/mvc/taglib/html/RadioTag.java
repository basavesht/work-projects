/*
 * Created on Nov 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.mvc.taglib.html;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.tcs.ebw.exception.EbwException;


public class RadioTag extends org.apache.struts.taglib.html.RadioTag {
	private StringBuffer outputBuff;
	private String property;
	private String value;
	private String styleClass;
	private String defaultValue;
	private boolean isHtmlOutput = false;
	private String checked;
	private String onclick;
	private String tabindex;
	
    public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public boolean isHtmlOutput() {
		return isHtmlOutput;
	}

	public void setHtmlOutput(boolean isHtmlOutput) {
		this.isHtmlOutput = isHtmlOutput;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public StringBuffer getOutputBuff() {
		return outputBuff;
	}

	public void setOutputBuff(StringBuffer outputBuff) {
		this.outputBuff = outputBuff;
	}

	public RadioTag()
    {
    }
    
    public int doStartTag() throws JspException {
    	try {
    
    			System.out.println("**********  RadioTag   **********");
    			
		       if(outputBuff == null)
		    	   outputBuff = new StringBuffer();
		        else
		        	outputBuff.delete(0, outputBuff.toString().length());
		       
		        String str=getValue();
		        
		        System.out.println("str value in the radio button tag "+str);
		    	outputBuff.append("<input type=\"radio\" name=\""+getProperty()+"\" class=\"ButtonRadio\"");
		    	outputBuff.append(" value=\""+getValue() +"\"");
		    	
		    	if(getChecked()!=null)
		    		outputBuff.append(" checked");
		    	
		    	if(getOnclick()!=null)
		    		outputBuff.append(" onclick=\""+getOnclick()+"\"");
		       			       	
		       	if(tabindex !=null)
		    		outputBuff.append(" tabindex=\""+getTabindex()+"\"");
		       	outputBuff.append(">");
		    	
 
	
		}
		catch(Exception exp){
			new EbwException(this,exp).printEbwException();
		}
		//System.out.println("Exiting of Buttontag");
		return SKIP_BODY;
	}
	
    public int doEndTag() throws JspException {
		

		 try
	        {
	        
	            if(!isHtmlOutput)
	            {
	                JspWriter jspwriter = pageContext.getOut();
	                jspwriter.println(outputBuff.toString());
	            }
	        }
	        catch(Exception exception)
	        {
	            (new EbwException(this, exception)).printEbwException();
	        }
	    
	        return 0;
				
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	/**
	 * @return the tabindex
	 */
	public String getTabindex() {
		return tabindex;
	}

	/**
	 * @param tabindex the tabindex to set
	 */
	public void setTabindex(String tabindex) {
		this.tabindex = tabindex;
	}



}
