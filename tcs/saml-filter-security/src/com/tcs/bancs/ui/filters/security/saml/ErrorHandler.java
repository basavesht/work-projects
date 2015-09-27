package com.tcs.bancs.ui.filters.security.saml;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tcs.bancs.ui.helpers.security.saml.SAMLValidationException;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;

public abstract class ErrorHandler 
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ErrorHandler.class);

	public void handle(HttpServletRequest httpRequest,HttpServletResponse httpResponse, Errors error, String samlResponse, Throwable e, String idpUrl) throws IOException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("handle(HttpServletRequest, HttpServletResponse, Errors, Throwable) - start"); //$NON-NLS-1$
		}
		String stackTrace = null;
		logger.error(String.format("Handling error condition: %s", error.name()));
		
		if ( e!=null )
		{
			logger.error("Handling exception error", e);
			stackTrace = getExceptionStackTrace(samlResponse,e);
		}
		else
		{
			Throwable t = new Throwable();
			logger.error("Trace --> " + getExceptionStackTrace(samlResponse, t));
		}
		String html;
		try {
			html = getHtml(httpRequest, error, stackTrace, idpUrl);
		} catch (ConfigXMLParsingException e1) {
			html = getExceptionStackTrace(samlResponse,e1);
		}
		httpResponse.setStatus(500);
		httpResponse.getWriter().print(html);
		httpResponse.getWriter().flush();

		if (logger.isDebugEnabled()) {
			logger.debug("handle(HttpServletRequest, HttpServletResponse, Errors, Throwable) - end"); //$NON-NLS-1$
		}
	}

	public abstract String getHtml(HttpServletRequest httpRequest, Errors error, String stackTrace, String idpUrl) throws ConfigXMLParsingException;

	public abstract void init(String invalidSessionHandlerInitParam) throws ConfigXMLParsingException; 

	private final String getInnerStackTrace(Throwable e)
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getInnerStackTrace(Throwable) - start"); //$NON-NLS-1$
		}

		StringBuffer trace = new StringBuffer();
		trace.append("<h4 style='padding-left: 10px'>").append(e.getClass().getName()).append(":").append(e.getMessage()).append("</h4>");
		
		StackTraceElement[] stackTrace = e.getStackTrace();
		for(StackTraceElement stackLine:stackTrace)
		{
			String fileName = (stackLine.getFileName()==null)?"":stackLine.getFileName();
			String lineNo = (stackLine.getLineNumber() < 1)?"":String.valueOf(stackLine.getLineNumber());
			String fileAndLine = (lineNo.length()==0)?fileName:fileName+":"+lineNo;
			String at = stackLine.getClassName() + "." + stackLine.getMethodName() + "(" + fileAndLine + ")";
			trace.append("<div style='padding-left: 15px'><span style='padding-right: 5px'>at</span><span>").append(at).append("</span></div>");
		}
		if ( e.getCause() != null )
		{
			trace.append("<div style='padding-left: 10px;padding-top: 5px; padding-bottom: 5px;'><b><u>caused by</u></b></div>");
			trace.append(getInnerStackTrace(e.getCause()));
		}
		if ( e instanceof InvocationTargetException)
		{
			InvocationTargetException t = (InvocationTargetException)e;
			if ( t.getTargetException() != null )
			{
				trace.append("<div style='padding-left: 10px;padding-top: 5px; padding-bottom: 5px;'><b><u>remote exception is</u></b></div>");
				trace.append(getInnerStackTrace(t.getTargetException()));
			}
		}
		if ( e instanceof SAMLValidationException )
		{
			SAMLValidationException se = ( SAMLValidationException)e;
			String misc = se.getMiscInfo();
			if( misc != null )
			{
				trace.append("<div style='padding-left: 10px;padding-top: 5px; padding-bottom: 5px;'><b><u>Miscelineous Information</u></b></div>");
				trace.append(encode(misc));				
			}
		}
		String returnString = trace.toString();

		if (logger.isDebugEnabled()) {
			logger.debug("getInnerStackTrace(Throwable) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private final String formatSAMLResponce(String samlResponse)	{
		if (logger.isDebugEnabled()) {
			logger.debug("formatSAMLResponce(String) - start"); //$NON-NLS-1$
		}

		StringBuffer trace = new StringBuffer();
		trace.append("<br><h4 style='padding-left: 10px'>SAML Response XML Received").append("</h4><br>");
		
		trace.append(encode(samlResponse));
		String returnString = trace.toString();

		if (logger.isDebugEnabled()) {
			logger.debug("formatSAMLResponce(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}
	private String encode(String string) {
		
		return string.replaceAll("<","&lt;").replaceAll(">", "&gt;");
	}

	private final String getExceptionStackTrace(String samlResponse, Throwable e) 
	{
		if (logger.isDebugEnabled()) {
			logger.debug("getExceptionStackTrace(Throwable) - start"); //$NON-NLS-1$
		}

		StringBuffer trace = new StringBuffer();
		String msg = e.getMessage();
		if (msg == null)
		{
			msg = e.getClass().getName();
		}
		trace.append("<div style='border-color: red; background-color: #f8e8e4; border-width: thin; border-style: solid; '>");
		trace.append("<h2 style='padding-left: 10px'>").append(msg).append("</h2>");
		trace.append("<script type='text/javascript'>");
		trace.append("	function toggle(e1,e2)");
		trace.append("	{");
		trace.append("		if (e2.style.display == 'none' )");
		trace.append("		{");
		trace.append("			e2.style.display = 'block';");
		trace.append("			e1.innerHTML = 'Hide Details';");
		trace.append("			if(resize) { resize(); }");
		trace.append("		}");
		trace.append("		else");
		trace.append("		{");
		trace.append("			e2.style.display = 'none';");
		trace.append("			e1.innerHTML = 'Show Details';");
		trace.append("			if(resize) { resize(); }");
		trace.append("		}");
		trace.append("	}");
		trace.append("</script>");
		trace.append("<a style='padding-left: 10px;padding-bottom:5px;' href='#' onclick='toggle(this,document.getElementById(\"exp_details\"));return false;'>Show Details</a>");
		trace.append("<div id='exp_details' style='display: none;'>");
		trace.append(getInnerStackTrace(e));
		trace.append(formatSAMLResponce(samlResponse));
		trace.append("</div>");
		trace.append("</div>");
		

		String returnString = trace.toString();

		if (logger.isDebugEnabled()) {
			logger.debug("getExceptionStackTrace(Throwable) - end"); //$NON-NLS-1$
		}
		return returnString;

	}



}
