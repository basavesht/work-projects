package com.tcs.ebw.mvc.taglib.html;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.common.util.PropertyFileReader;
import com.tcs.ebw.common.util.StringUtil;
import com.tcs.ebw.mvc.validator.EbwForm;
import com.tcs.ebw.taglib.CommonTemplateHTML;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class SelectTag extends org.apache.struts.taglib.html.SelectTag
{    
	private static final long serialVersionUID = 1L;
	public SelectTag()
    {    
        htmlOutput = false;
        formname = null;
        name = null;
        returnInput = "";
        combo = null;
        txtStyleClass = null;
        
    }

    public int doStartTag()
        throws JspException
    {
        try
        {
        	combo = PropertyFileReader.getProperty("AutoComplete");
        	
            Object ebwForm = null;
            String className = null;
            Class classLoad = null;
            //Object obj[] = {Character.valueOf('Y'), Character.valueOf('N')};            
            if(this.combo.equalsIgnoreCase("Y"))
            {
            	
            	if(getMultiple()==null){
            		
	                CommonTemplateHTML objCommonTemp = new CommonTemplateHTML();
	                //String mode = null;
	                //String formName = null;
	                Vector vec = null;
	                JspWriter out = null;
	                String value = null;
	                String divID = (new StringBuilder("div")).append(name).toString();
	                String txtId = (new StringBuilder("txt")).append(name).toString();
	                String cboId = (new StringBuilder("cbo")).append(name).toString();
	                String divClass = null;
	                String textClass = null;
	                String imageSource = null;
	                //String combo = null;
	                //combo = PropertyFileReader.getProperty("AutoComplete");
	                outputBuffer = new StringBuffer();
	                bundle = ResourceBundle.getBundle("com.tcs.ebw.taglib.ComponentTypeResource");
	                if(txtStyleClass == null || txtStyleClass.equals(""))
	                {
	                    EBWLogger.trace(this, "the css class not specified by the user");
	                    textClass = bundle.getString("autoCompleteText.css");
	                } else
	                {
	                    textClass = txtStyleClass;
	                }
	                //if(divClass == null || divClass.equals(""))
	                //{
	                //    EBWLogger.trace(this, "the css class not specified by the user");
	                    divClass = bundle.getString("autoCompleteDiv.css");
	                //}
	                //if(imageSource == null || imageSource.equals(""))
	                // {
	                //   EBWLogger.trace(this, "the css class not specified by the user");
	                    imageSource = bundle.getString("autoComplete.image");
	                //}
	                if(!htmlOutput) 
	                {
	                    out = pageContext.getOut();
	                    ebwForm = (EbwForm)pageContext.getRequest().getAttribute(formname);
	                    className = ebwForm.getClass().getName();
	                    EBWLogger.trace(this, (new StringBuilder("Class Name")).append(className).toString());
	                    classLoad = Class.forName(className);
	                } else
	                {
	                    className = formname;
	                    classLoad = Class.forName(className);
	                    ebwForm = (EbwForm)classLoad.newInstance();
	                }
	                outputBuffer.append((new StringBuilder("\n<table> <tr><td>")).toString());
	                outputBuffer.append((new StringBuilder("\n<div> ")).toString());
	                outputBuffer.append((new StringBuilder("\n<input type=\"text\"")).toString());
	                
	                if(getOnchange()==null){
	                outputBuffer.append((new StringBuilder(" name=\"")).append(name).append("\" size=\"1\" id=\"").append(txtId).append("\" " +
            		"onclick=\"setComboData('").append(txtId).append("','").append(cboId).append("','").append(divID).append("');showDiv('").append(divID).append("','").append(txtId).append("');\"" +
            		" onKeyUp=\"findIt(cboValue,cboText,'").append(cboId).append("','").append(txtId).append("','").append(divID).append("',"+name+");" +
            				"setComboData('").append(txtId).append("','").append(cboId).append("','").append(divID).append("');\" autocomplete=\"off\" onKeyDown=\"changeFocus('").append(cboId).append("','").append(txtId).append("','").append(divID).append("',").append((new StringBuilder("document.forms[0].")).append(name).toString()).append((new StringBuilder(")\" "  + " onblur=\"hideDiv('").append(divID).append("');\""+
            				" class=\""))).append(textClass).append("\" >").toString());
	                
	                }else if(getOnchange()!=null){            
	                
	                outputBuffer.append((new StringBuilder(" name=\"")).append(name).append("\" size=\"1\" id=\"").append(txtId).append("\" " +
	                		"onclick=\"setComboData('").append(txtId).append("','").append(cboId).append("','").append(divID).append("');showDiv('").append(divID).append("','").append(txtId).append("');\"" +
	                		" onKeyUp=\"findIt(cboValue,cboText,'").append(cboId).append("','").append(txtId).append("','").append(divID).append("',"+name+");"+
	                				"setComboData('").append(txtId).append("','").append(cboId).append("','").append(divID).append("');\" autocomplete=\"off\" onKeyDown=\"changeFocus('").append(cboId).append("','").append(txtId).append("','").append(divID).append("',").append((new StringBuilder("document.forms[0].")).append(name).toString()).append((new StringBuilder(")\" "  + " onchange=\""+getOnchange()+"\""+ " onblur=\"hideDiv('").append(divID).append("');\""+
	                				" class=\""))).append(textClass).append("\" >").toString());
	                }
	                
	                
	                outputBuffer.append((new StringBuilder((new StringBuilder("<IMG  SRC=\"")).append(imageSource).append("\" WIDTH=\"18\" HEIGHT=\"21\" border=\"0\" align=\"absbottom\" onclick=\"showDiv('").toString())).append(divID).append("','").append(txtId).append("');setComboData('").append(txtId).append("','").append(cboId).append("','").append(divID).append("');\" onload=\"setComboData('").append(txtId).append("','").append(cboId).append("','").append(divID).append("');\">").toString());
	                outputBuffer.append((new StringBuilder("<div id=\"")).append(divID).append((new StringBuilder("\" class=\"")).append(divClass).append("\" style=\"display:none;\"" +
	                		" >").toString()).toString());
	                //mode = ((EbwForm)ebwForm).getState().substring(((EbwForm)ebwForm).getState().indexOf("_") + 1, ((EbwForm)ebwForm).getState().length());
	                //formName = className.substring(className.lastIndexOf(".") + 1, className.length());
	                vec = (Vector)classLoad.getMethod((new StringBuilder("get")).append(StringUtil.initCaps((new StringBuilder(String.valueOf(getName()))).append("Collection").toString())).toString(), null).invoke(ebwForm, null);
	                java.util.Enumeration objEnum = vec.elements();
	                
	                value = "";
	                returnInput = objCommonTemp.setComboOptionsTag(getName(), getAttribute(), value, objEnum, bundle.getString("autoCompleteDiv.size"),getOnchange());
	                
	                outputBuffer.append((new StringBuilder(returnInput.toString())).toString());
	                outputBuffer.append((new StringBuilder("\n</div>")).toString());
	                outputBuffer.append((new StringBuilder("\n</div>")).toString());
	                outputBuffer.append((new StringBuilder("\n </td></tr></table>")).toString());
	                if(out != null)
	                out.print(outputBuffer);
	                destroyObjects(); 
	                
            	}
            	else {
            		
            		super.doStartTag();
            	
            	}
            } else  if(this.combo.equalsIgnoreCase("N"))
            {
                
                                
                super.doStartTag();
                
            }
        }
        catch(Exception exc)
        {        	
            
            exc.printStackTrace();
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag()
        throws JspException
    {
    	return super.doEndTag();
    }

    private void destroyObjects()
    {
        //returnInput = null;
        attribute = null;
        combo = null;
        txtStyleClass = null;
        name = null;
        formname = null;
    }

    public int doAfterBody()
        throws JspException
    {
        return super.doAfterBody();
    }

    public void release()
    {
        super.release();
    }

    public String getFormname()
    {
        return formname;
    }

    public void setFormname(String formname)
    {
        this.formname = formname;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAttribute()
    {
        return attribute;
    }
    
    public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
    
    public String getTxtStyleClass()
    {
        return txtStyleClass;
    }

    public void setTxtStyleClass(String txtStyleClass)
    {
        this.txtStyleClass = txtStyleClass;
    }

    private boolean htmlOutput;
    private StringBuffer outputBuffer;
    private String formname;
    private String name;
    private String returnInput;
    private String attribute;
    private ResourceBundle bundle;
    private String combo;
    private String txtStyleClass;
	
    
}