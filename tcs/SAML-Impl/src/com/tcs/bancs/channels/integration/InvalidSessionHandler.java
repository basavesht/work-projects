package com.tcs.bancs.channels.integration;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.tcs.bancs.ui.helpers.security.saml.SAMLValidationException;
import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;

public class InvalidSessionHandler extends
		com.tcs.bancs.ui.filters.security.saml.InvalidSessionHandler {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(InvalidSessionHandler.class);

	private InvalidSessionHandlerConfig handlerConfig;

	@Override
	public String getHTML(HttpServletRequest httpRequest, String idp_url) throws ConfigXMLParsingException {
		if (logger.isDebugEnabled()) {
			logger.debug("getHtml(HttpServletRequest, String) - start"); //$NON-NLS-1$
		}
		
		try {
			handlerConfig.checkNew();
		} catch (FileNotFoundException e) {
			throw new ConfigXMLParsingException(e);
		} catch (ConfigXMLParsingException e) {
			throw new ConfigXMLParsingException(e);
		}

		String pageId = getPageId(httpRequest.getRequestURI());
		String url = idp_url 
		+ "?pageID=" 
		+ pageId;
		
		StringBuffer html = new StringBuffer("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		html.append("<%@page language=\"java\" contentType=\"text/html; charset=ISO-8859-1\" pageEncoding=\"ISO-8859-1\"%>");
		html.append("<html>");
		html.append("<head>");
		html.append("<title>RedirectToIDP</title>");
		html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		html.append("<script type='text/javascript'>");
		html.append("	document.domain='").append(handlerConfig.getDomain()).append("';");
		
		String token = httpRequest.getParameter(handlerConfig.getPostAttrToken());
		if ( token == null )
			token = handlerConfig.getDefaultTokenId();

		ResourceBundle messages = ResourceBundle.getBundle("ErrMessage");
		html.append("   var errorDesc=\"").append(messages.getString("Sys_0101")).append("\";");
		html.append("	function raiseError(token, ErrorCode, ErrorDesc)");
		html.append("	{");
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
		html.append("	function showError()");
		html.append("	{");
		html.append("		if (!raiseError('").append(token).append("','Sys_0101',errorDesc))");
		html.append("		{");
		html.append("			document.getElementById('errdisplay').style.display = 'block';");
		html.append("		}");
		html.append("	}");

		html.append("	function redirect()");
		html.append("	{");
		html.append("		if ( parent == self )");
		html.append("		{");
		html.append("			document.cookie = 'PostAttrbuteNotPresentCk=;path=/;expires=Thu, 01-Jan-1970 00:00:01 GMT';");
		html.append("	        self.location.href = '").append(url).append("';");
		html.append("	        return;");
		html.append("		}");
		html.append("		try");
		html.append("		{");
		html.append("			top.proxyIFrameManager.ClearSession();");
		//html.append("			showError();");
		//html.append("			document.location.href = 'logout';");
		html.append("			top.location.href = '").append(url).append("';");
		html.append("		}");
		html.append("		catch (err)");
		html.append("		{");
		html.append("			document.getElementById('jscallerror').innerHTML = 'Error calling top.proxyIFrameManager.ClearSession() - ' + err.name + ' : ' + err.message;");
		html.append("			document.getElementById('errdisplay').style.display='block';");
		html.append("		}");
		html.append("	}");
		html.append("</script>");
		html.append("</head>");
		html.append("<body onload='redirect()'>");
		html.append("<div id='errdisplay' style='display: none;'>");
		html.append("	<div style='color:red;' id='jscallerror'></div>");
		html.append("</div>");
		html.append("</body>");
		html.append("</html>");

		String returnString = html.toString();

		if (logger.isDebugEnabled()) {
			logger.debug("getHtml(HttpServletRequest, String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	private String getPageId(String requestURI) {
		if (logger.isDebugEnabled()) {
			logger.debug("getPageId(String) - start"); //$NON-NLS-1$
		}

		HashMap<String, String> pageIdMap = handlerConfig.getPageIdMap();
		String pageId = pageIdMap.get(requestURI);
		if(pageId == null)
			pageId = handlerConfig.getDefaultPageId();

		if (logger.isDebugEnabled()) {
			logger.debug("getPageId(String) - end"); //$NON-NLS-1$
		}
		return pageId;
	}

	@Override
	public void init(String configXML)
			throws ConfigXMLParsingException {
		if (logger.isDebugEnabled()) {
			logger.debug("init(String) - start"); //$NON-NLS-1$
		}

		handlerConfig = InvalidSessionHandlerConfig.getInstance();

		try {
			handlerConfig.parse(configXML);
		} catch (FileNotFoundException e) {
			logger.error("init(String)", e); //$NON-NLS-1$

			throw new ConfigXMLParsingException(e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("init(String) - end"); //$NON-NLS-1$
		}
	}

}
