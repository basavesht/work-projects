package com.tcs.ebw.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class EbwSessionTab extends TagSupport {

	public EbwSessionTab(Object obj) {
		formname = null;
		name = null;
		data = null;
		attrObj = null;
		isHtmlOutput = false;
		outputBuff = null;
		accesskey = null;
		outputBuff = new StringBuffer();			
	}

	public EbwSessionTab() {		
		formname = null;
		name = null;		
		data = null;
		attrObj = null;
		isHtmlOutput = false;
		outputBuff = null;
		accesskey = null;			
	}

	public int doEndTag() throws JspException {
		try {			
				pageContext.getOut().println(outputBuff);
		    }
		    catch (Exception exception) {
			// (new EbwException(this, exception)).printEbwException();
		    }		
		return 0;
	}

	public int doStartTag() throws JspException { 	
		
		try {			
			if (outputBuff == null)
				outputBuff = new StringBuffer();
			else
				outputBuff.delete(0, outputBuff.toString().length());				
           
			//To display the session tabs
			outputBuff.append("<div style=\"position:absolute;right:0px;\">");
			outputBuff.append("<table id=\"outerTab\" style=\"display:none;\"><tr>");
			outputBuff.append("<td id=\"preLbl\" style=\"display:none;padding-right:10px;\">");
			outputBuff.append("<img src=\"../images/pagination_first.gif\" border=\"0\" align=\"middle\" style=\"cursor:pointer\" onClick=\"showLeftSessionTabs();\">");
			outputBuff.append("</td>");
			outputBuff.append("<td>&nbsp;</td>");
			outputBuff.append("<td id=\"preLblLeft\" style=\"display:none;padding-right:10px;\">");
			outputBuff.append("<img src=\"../images/pagination_previous.gif\" border=\"0\" align=\"middle\" style=\"cursor:pointer\" onClick=\"showPreviousSessionTab();\">");
			outputBuff.append("</td>");
			outputBuff.append("<td align=\"left\"> ");
			outputBuff.append("<table  id=\"sessionTable\">");
			outputBuff.append("<tr id=\"tableRow\" align=\"left\"></tr>");			
			outputBuff.append("</table></td>");
			outputBuff.append("<td id=\"nextLblRight\" style=\"display:none;padding-left:10px;\">");
			outputBuff.append("<img src=\"../images/pagination_next.gif\" border=\"0\" align=\"middle\" style=\"cursor:pointer\" onClick=\"showNextSessionTab();\">");
			outputBuff.append("</td>");
			outputBuff.append("<td>&nbsp;</td>");
			outputBuff.append("<td id=\"nextLbl\" style=\"display:none;padding-right:5px;padding-left:10px;\";>");
			outputBuff.append("<img src=\"../images/pagination_last.gif\" border=\"0\" align=\"middle\" onClick=\"showRightSessionTabs();\" style=\"padding-right:10px;cursor:pointer\">");
			outputBuff.append("</td>");
			outputBuff.append("<td>");
			outputBuff.append("<img id=\"slideViewImg\" src=\"../images/SlideView.PNG\" title=\"Slide View\" onclick=\"showSlideView('true');\" style=\"cursor:pointer;padding-left:5px;\">");
			outputBuff.append("</td>");
			outputBuff.append("<td style=\"padding-left:5px;padding-right:5px;\">");
			outputBuff.append("<img id=\"tileView\" src=\"../images/Arrange.gif\"  title=\"Arrange Windows\" onclick=\"showTilingMenu(this);\" style=\"cursor:pointer;\">");
			outputBuff.append("</td>");
			outputBuff.append("<td>");
			outputBuff.append("<input id=\"clear\" type=\"button\" class=\"ThumbButton\" style=\"display:none\" value=\"Clear\" onclick=\"clearSessionTabs();\" />");
			outputBuff.append("</td></tr></table></div>");
			
			
			//For tiling popup
			outputBuff.append("<div id=\"tilingOptionMain\" style=\"positon:absolute;border:3px outset; Z-index:99;width:30%;display:none;background-color:#C9DFEA;\">");
			outputBuff.append("<div id=\"hTile\" class=\"widgetText\">");
			outputBuff.append("<table cellspacing=0 cellpadding=0 width=\"100%\">");
			outputBuff.append("<tr>");
			outputBuff.append("<td align=\"left\"><img src=\"../images/objects_hor.gif\" style=\"cursor:pointer;\" onclick=\"horizontalView()\"/>&nbsp;<a href=\"#\" onclick=\"horizontalView()\">Horizontal</a></td>");
			outputBuff.append("<td align=\"center\" style=\"padding-right:5px;\"><img src=\"../images/objects_ver.gif\"  style=\"cursor:pointer;\" onclick=\"verticalView()\"/>&nbsp;<a href=\"#\"  onclick=\"verticalView()\">Vertical</a></td>");
			outputBuff.append("<td align=\"right\" style=\"padding-right:10px;\"><img src=\"../images/objects.gif\"  style=\"cursor:pointer;\" onclick=\"tiledView()\"/>&nbsp;<a href=\"#\"  onclick=\"tiledView()\">Tiled</a></td>");
			outputBuff.append("<td align=\"right\" style=\"padding-right:0px;\"><img src=\"../images/cancel_popup.gif\" onclick=\"closeTilePopup()\"/></td>"); 	
			outputBuff.append("</tr>");
			outputBuff.append("<tr><td>&nbsp;</td></tr>");
			outputBuff.append("</table>");
			outputBuff.append("</div>");
			outputBuff.append("<div id=\"tilingOption\">");	
			outputBuff.append("</div>");
			outputBuff.append("</div>"); 			
			
		} catch (NullPointerException nullpointerexception) {
			nullpointerexception.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	
	 public static void main(String args[])
	    {
	        try
	        {
	        	
	        }
	        catch(Exception exception)
	        {
	            //exception.printStackTrace();
	        }
	    }
	
	
	private String name;	

	private String data = null;

	private String attrObj = null;

	private boolean isHtmlOutput = false;

	private StringBuffer outputBuff = null;

	private String accesskey = null;
	
	private String formname = null;		


	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public String getAccesskey() {
		return accesskey;
	}

	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	public String getAttrObj() {
		return attrObj;
	}

	public void setAttrObj(String attrObj) {
		this.attrObj = attrObj;
	}	

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isHtmlOutput() {
		return isHtmlOutput;
	}

	public void setHtmlOutput(boolean isHtmlOutput) {
		this.isHtmlOutput = isHtmlOutput;
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

}