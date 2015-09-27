package com.tcs.bancs.ui.filters.security.saml;

import javax.servlet.http.HttpServletRequest;

import com.tcs.bancs.ui.helpers.xml.ConfigXMLParsingException;

public class DefaultInvalidSessionHandler extends InvalidSessionHandler {

	@Override
	public String getHTML(HttpServletRequest httpRequest, String idp_url) {
		
	
		StringBuffer html = new StringBuffer("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		html.append("<%@page language=\"java\" contentType=\"text/html; charset=ISO-8859-1\" pageEncoding=\"ISO-8859-1\"%>");
		html.append("<html>");
		html.append("<head>");
		html.append("<title>RedirectToIDP</title>");
		html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		html.append("<script type='text/javascript'>");
		html.append("	function redirect()");
		html.append("	{");
		html.append("		top.location.href = '").append(idp_url).append("';");
		html.append("	}");
		html.append("</script>");
		html.append("</head>");
		html.append("<body onload='redirect()'>");
		html.append("</body>");
		html.append("</html>");

		String returnString = html.toString();

		return returnString;
	}

	@Override
	public void init(String invalidSessionHandlerInitParam)
			throws ConfigXMLParsingException {
		// Do Nothing

	}

}
