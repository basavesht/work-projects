/* This software is the confidential and proprietary information of 
 * Tata Consultancy Services ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Tata Consultancy Services.
 */

package com.tcs.ebw.taglib;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.exception.EbwException;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.TagSupport;


public class EbwSlider extends TagSupport
{

    public EbwSlider(Object obj)
    {
        name = null;
        formname = null;
        val = null;
        images = null;
        topconstraint = null;
        bottomconstraint = null;
        keyincrement = null;
        sliderid = null;
        
        isHtmlOutput = false;
        outputBuff = null;
       
        noDataFoundMsg = "";
      
   
        outputBuff = new StringBuffer();
   
    }

    public EbwSlider()
    {
        name = null;
        formname = null;
        val = null;
        images = null;
        topconstraint = null;
        bottomconstraint = null;
        keyincrement = null;
        sliderid = null;
       type = null;
       
     
        isHtmlOutput = false;
        outputBuff = null;
       
     
        noDataFoundMsg = "";
        
   
    }

    public int doEndTag()
        throws JspException
    {
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

    public int doStartTag()
        throws JspException
    {
        System.out.println("Inside EBWSlider class");
        EBWLogger.trace(this, "Into doStartTag of EBWSliderTag");
        EBWLogger.trace(this, (new StringBuilder()).append("Values are").append(getVal()).toString());
        String s = getVal();
        String s1 = getTopconstraint();
        String s2 = getBottomconstraint();
        String s3 = getKeyincrement();
        String s4 = getSliderid();
        String s5 = getImages();
        String s6 = getType();
        String as[] = s.split(",");
        EBWLogger.trace(this, (new StringBuilder()).append("slidervalues").append(as).toString());
        EBWLogger.trace(this, (new StringBuilder()).append("Length of slidervalues is").append(as.length).toString());
        String as1[] = s5.split(",");
        EBWLogger.trace(this, (new StringBuilder()).append("Length of imagevalues is").append(as1.length).toString());
        int i = as.length;
        int j = as1.length;
        EBWLogger.trace(this, (new StringBuilder()).append("Typeofslider is").append(s6).toString());
        EBWLogger.trace(this, (new StringBuilder()).append("topconstr is").append(s1).toString());
        EBWLogger.trace(this, (new StringBuilder()).append("bottomconstr is").append(s2).toString());
        EBWLogger.trace(this, (new StringBuilder()).append("keyincr is").append(s3).toString());
        try
        {
            if(outputBuff == null)
                outputBuff = new StringBuffer();
            else
                outputBuff.delete(0, outputBuff.toString().length());
            EBWLogger.logDebug(this, (new StringBuilder()).append("outputBuff is").append(outputBuff).toString());
            EBWLogger.logDebug(this, (new StringBuilder()).append("formname is").append(formname).toString());
           
         

            if(j == 6)
            {
                String s7 = getName();
                EBWLogger.trace(this, (new StringBuilder()).append("Name is").append(s7).toString());
                int k = 1;
                int l = 100 / i;
                EBWLogger.logDebug(this, (new StringBuilder()).append("width is").append(l).toString());
                outputBuff.append("<div id=\"");
                outputBuff.append(getName());
                outputBuff.append("-bg\" class=\"jt-slider-bg\" tabindex=\"-1\" title=\"Loan Type\" onclick=\"javascript:loadslider(");
                outputBuff.append(s1);
                outputBuff.append(",");
                outputBuff.append(s2);
                outputBuff.append(",");
                outputBuff.append(s3);
                outputBuff.append(",");
                outputBuff.append("'");
                outputBuff.append(s7);
                outputBuff.append("'");
                outputBuff.append(",");
                outputBuff.append("'");
                outputBuff.append(s4);
                outputBuff.append("'");
                outputBuff.append(",");
                outputBuff.append("'");
                outputBuff.append(s);
                outputBuff.append("'");
                outputBuff.append(");\">");
                outputBuff.append("<table style=\"width: 500px;\" cellpadding=0 cellspacing=0>");
                outputBuff.append("<tr><td height=8></td></tr>");
                outputBuff.append("<tr>");
                if(s6 == "Discrete")
                {
                    EBWLogger.logDebug(this, "Inside discrete method");
                    for(int i1 = 0; i1 <= as.length - 1; i1++)
                    {
                        outputBuff.append("<td class=\"sliderOption\" width=");
                        outputBuff.append(l);
                        outputBuff.append("%>");
                        outputBuff.append(as[i1]);
                        outputBuff.append("</td>");
                    }

                    outputBuff.append("</tr>");
                }
                outputBuff.append("</table>");
                outputBuff.append("<div id=\"");
                outputBuff.append(getName());
                outputBuff.append("-thumb\" class=\"jt-slider-thumb\">");
                outputBuff.append("<table cellpadding=0 cellspacing=0 valign=bottom>");
                outputBuff.append("<tr><td><img style=\"filter:alpha(opacity=100);-moz-opacity:.5;opacity:.5;\" src=\"../images/");
                outputBuff.append(as1[0]);
                outputBuff.append("\"></td>");
                outputBuff.append(" <td class=\"sliderTC\" style=\"width: 166px;\"></td>");
                outputBuff.append("<td><img style=\"filter:alpha(opacity=100);-moz-opacity:.5;opacity:.5;\" src=\"../images/");
                outputBuff.append(as1[1]);
                outputBuff.append("  \"></td>");
                outputBuff.append("</tr>");
                outputBuff.append("<tr><td><img style=\"filter:alpha(opacity=100);-moz-opacity:.5;opacity:.5;\" src=\"../images/");
                outputBuff.append(as1[2]);
                outputBuff.append("\"></td>");
                outputBuff.append("<td></td>");
                outputBuff.append("<td><img style=\"filter:alpha(opacity=100);-moz-opacity:.5;opacity:.5;\" src=\"../images/");
                outputBuff.append(as1[3]);
                outputBuff.append("\"></td>");
                outputBuff.append("</tr>");
                outputBuff.append("<tr><td><img style=\"filter:alpha(opacity=100);-moz-opacity:.5;opacity:.5;\" src=\"../images/");
                outputBuff.append(as1[4]);
                outputBuff.append("\"></td>");
                outputBuff.append("<td class=\"sliderBC\" style=\"width: 166px;\"></td>");
                outputBuff.append("<td><img style=\"filter:alpha(opacity=100);-moz-opacity:.5;opacity:.5;\" src=\"../images/");
                outputBuff.append(as1[5]);
                outputBuff.append("\" onload=\"javascript:loadinit(");
                outputBuff.append(s1);
                outputBuff.append(",");
                outputBuff.append(s2);
                outputBuff.append(",");
                outputBuff.append(s3);
                outputBuff.append(",");
                outputBuff.append("'");
                outputBuff.append(s7);
                outputBuff.append("'");
                outputBuff.append(",");
                outputBuff.append("'");
                outputBuff.append(s4);
                outputBuff.append("'");
                outputBuff.append(",");
                outputBuff.append("'");
                outputBuff.append(s);
                outputBuff.append("'");
                outputBuff.append(");\"></td>");
                outputBuff.append("</tr> ");
                outputBuff.append("</table>");
                outputBuff.append("</div>");
                outputBuff.append("</div> ");
                k++;
            } else
            if(j == 2)
            {
                EBWLogger.logDebug(this, "Inside imagesvalueslength=2 method");
                outputBuff.append("<table >");
                outputBuff.append("<tr>");
                outputBuff.append("<td >");
                outputBuff.append("<div id='");
                outputBuff.append(getName());
                outputBuff.append("' class=\"ui-slider\" style=\"background:  url(../images/");
                outputBuff.append(as1[0]);
                outputBuff.append(") no-repeat scroll 5px 0px;\">");
                outputBuff.append("<div class='ui-slider-handle' style='background-image: url(../images/");
                outputBuff.append(as1[1]);
                outputBuff.append("\t);'></div>");
                outputBuff.append("</div>");
                outputBuff.append("</td>");
                outputBuff.append("</table>");
            }
            outputBuff.append("<input type=\"hidden\" id=\"");
            outputBuff.append(getName()+"\"");
            outputBuff.append("  value=\"\"");
            outputBuff.append("  name=\""+getName());
           	outputBuff.append("\">");
        }
        catch(NullPointerException nullpointerexception)
        {
            nullpointerexception.printStackTrace();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return 0;
    }

  

	public String getBottomconstraint() {
		return bottomconstraint;
	}

	public void setBottomconstraint(String bottomconstraint) {
		this.bottomconstraint = bottomconstraint;
	}

	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public boolean isHtmlOutput() {
		return isHtmlOutput;
	}

	public void setHtmlOutput(boolean isHtmlOutput) {
		this.isHtmlOutput = isHtmlOutput;
	}

	public String getKeyincrement() {
		return keyincrement;
	}

	public void setKeyincrement(String keyincrement) {
		this.keyincrement = keyincrement;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StringBuffer getOutputBuff() {
		return outputBuff;
	}

	public void setOutputBuff(StringBuffer outputBuff) {
		this.outputBuff = outputBuff;
	}

	public String getSliderid() {
		return sliderid;
	}

	public void setSliderid(String sliderid) {
		this.sliderid = sliderid;
	}

	public String getTopconstraint() {
		return topconstraint;
	}

	public void setTopconstraint(String topconstraint) {
		this.topconstraint = topconstraint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getNoDataFoundMsg() {
		return noDataFoundMsg;
	}

	public void setNoDataFoundMsg(String noDataFoundMsg) {
		this.noDataFoundMsg = noDataFoundMsg;
	}
    

    private String name;
    private String formname;
    private String val;
    private String images;
    private String topconstraint;
    private String bottomconstraint;
    private String keyincrement;
    private String sliderid;
    private String type;
    private String noDataFoundMsg;
    private boolean isHtmlOutput;
    private StringBuffer outputBuff;
    
  
}