package com.tcs.bancs.channels.integration;

import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.tcs.bancs.ui.filters.security.saml.Errors;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;

public class ErrorHandler extends
		com.tcs.bancs.ui.filters.security.saml.ErrorHandler {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ErrorHandler.class);

	private ErrorHandlerConfig handlerConfig;
	private InvalidSessionHandlerConfig invalidSessionConfig;

	private String getPageId(String requestURI) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPageId(String) - start"); //$NON-NLS-1$
		}

		HashMap<String, String> pageIdMap = invalidSessionConfig.getPageIdMap();
		String pageId = pageIdMap.get(requestURI);
		if(pageId == null)
			pageId = invalidSessionConfig.getDefaultPageId();

		if (logger.isDebugEnabled()) {
			logger.debug("getPageId(String) - end"); //$NON-NLS-1$
		}
		return pageId;
	}

	@Override
	public String getHtml(HttpServletRequest httpRequest, Errors error, String stackTrace, String idp_url) throws ConfigXMLParsingException {
		if (logger.isDebugEnabled()) {
			logger.debug("getHtml(HttpServletRequest, Errors, Throwable) - start"); //$NON-NLS-1$
		}
		
		try {
			handlerConfig.checkNew();
			invalidSessionConfig.checkNew();
		} catch (FileNotFoundException e) {
			throw new ConfigXMLParsingException(e);
		} catch (ConfigXMLParsingException e) {
			throw new ConfigXMLParsingException(e);
		}

		String token = httpRequest.getParameter(handlerConfig.getPostAttrToken());
		if ( token == null )
			token = handlerConfig.getDefaultTokenId();
		
		String url =  idp_url 
		+ "?pageID=" 
		+ getPageId(httpRequest.getRequestURI());

		StringBuffer html = new StringBuffer("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		UIError uiError = getUIError(error);
		html.append("<%@page language=\"java\" contentType=\"text/html; charset=ISO-8859-1\" pageEncoding=\"ISO-8859-1\"%>");
		html.append("<html>");
		html.append("<head>");
		html.append("<title>ErrorToCS</title>");
		html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		html.append("<script type='text/javascript'>");
		html.append("	document.domain='").append(invalidSessionConfig.getDomain()).append("';");

		html.append("	function redirect()");
		html.append("	{");
		html.append("		top.location.href = '").append(url).append("';");
		html.append("	}");
		html.append("	function raiseError(token, ErrorCode, ErrorDesc)");
		html.append("	{");
		html.append("		if(parent == self)");
		html.append("		{");
		html.append("			redirect();");
		html.append("		}");
		html.append("		try");
		html.append("		{");
		html.append("			top.proxyIFrameManager.DisplayError(token,[{ErrorCode:ErrorCode, ErrorDesc:ErrorDesc}]);");
		html.append("			return true;");
		html.append("		}");
		html.append("		catch (err)");
		html.append("		{");
		html.append("			document.getElementById('jscallerror').innerHTML = 'Error calling top.proxyIFrameManager.DisplayError - ' + err.name + ' : ' + err.message;");
		html.append("			return false;");
		html.append("		}");
		html.append("	}");
		html.append("	function getDocumentHeight()");
		html.append("	{");
		html.append("	        return document.body.scrollHeight + 20;");
		html.append("	}");

		html.append("	function resize(){");
		html.append("	        var height = getDocumentHeight();");
		html.append("	        try");
		html.append("	        {");
		html.append("	            top.proxyIFrameManager.ResizeFrame('").append(token).append("',height);");
		html.append("	            return true;");
		html.append("	        }");
		html.append("	        catch (err)");
		html.append("	        {");
   		html.append("				document.getElementById('jscallerror').innerHTML = 'Error calling top.proxyIFrameManager.ResizeFrame - ' + err.name + ' : ' + err.message;");
   		html.append("	            return false;");
   		html.append("	        }");
   		html.append("	}");

		html.append("	function showError()");
		html.append("	{");
		html.append("		if (!raiseError('").append(token).append("','").append(uiError.errorCode).append("','").append(uiError.errorDesc).append("'))");
		html.append("		{");
		html.append("			document.getElementById('errdisplay').style.display = 'block';");
		html.append("		}");
		html.append("	}");
		html.append("</script>");
		html.append("</head>");
		html.append("<body onload='showError()'>");
		html.append("<div id='errdisplay' style='display: none;'>");
		html.append("	<div style='color:red;' id='jscallerror'></div>");
		html.append("	<H2>Error: ").append(uiError.errorCode).append(" - ").append(uiError.errorDesc).append("</H2>");
		html.append("</div>");
		if ( stackTrace!= null && handlerConfig.isShowTrace()) 
		{
			html.append(stackTrace);
		}
		html.append("</body>");
		html.append("</html>");

		String returnString = html.toString();
		if (logger.isDebugEnabled()) {
			logger.debug("getHtml(HttpServletRequest, Errors, Throwable) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	@Override
	public void init(String configXML)
			throws ConfigXMLParsingException {
		if (logger.isDebugEnabled()) {
			logger.debug("init(String) - start"); //$NON-NLS-1$
		}

		handlerConfig = ErrorHandlerConfig.getInstance();
		invalidSessionConfig = InvalidSessionHandlerConfig.getInstance();

		try {
			handlerConfig.parse(configXML);
		} catch (FileNotFoundException e) {
			logger.error("init(String)", e); //$NON-NLS-1$
			throw new ConfigXMLParsingException(e);
		}


		try {
			invalidSessionConfig.parse(handlerConfig.getInvalidSessionHandlerConfigFile());
		} catch (FileNotFoundException e) {
			logger.error("init(String)", e); //$NON-NLS-1$
			throw new ConfigXMLParsingException(e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init(String) - end"); //$NON-NLS-1$
		}
	}

	private UIError getUIError(Errors error) {
		if (logger.isDebugEnabled()) {
			logger.debug("getUIError(Errors) - start"); //$NON-NLS-1$
		}

		UIError returnUIError = new UIError(error);

		if (logger.isDebugEnabled()) {
			logger.debug("getUIError(Errors) - end"); //$NON-NLS-1$
		}
		return returnUIError;
	}

}
