package com.tcs.bancs.channels.integration;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.tcs.bancs.ui.helpers.Base64Util;

public class MMSessionUtils {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MMSessionUtils.class);

	public static String getParams(String page, HttpSession session) {
		if (logger.isDebugEnabled()) {
			logger.debug("getParams(String, HttpSession) - start"); //$NON-NLS-1$
		}
		
		HashMap params = (HashMap)session.getAttribute("MMStateParameters");
		

		if (logger.isDebugEnabled()) {
			logger.debug("getParams(String, HttpSession) - Params got --> " + params, null); //$NON-NLS-1$
		}
		
		if ( params != null )
		{
			String param = (String)params.get(page);
			if (logger.isDebugEnabled()) {
				logger.debug("getParams(String, HttpSession) - Param got --> " + param, null); //$NON-NLS-1$
			}
			if(param != null)
			{
				/*** Remove this parameter value 
				 * So that on refresh page -- it restores back to INIT page
				 ****/
				params.remove(page);
				session.setAttribute("MMStateParameters", params);
				try {
					param = new String(Base64Util.decode(param));
					if (logger.isDebugEnabled()) {
						logger.debug("getParams(String, HttpSession) - Param decoded --> " + param, null); //$NON-NLS-1$
					}
				} catch (IOException e) {
					logger.error("getParams(String, HttpSession)", e); //$NON-NLS-1$
					param = getDefaultParams(page);
				}

				if (logger.isDebugEnabled()) {
					logger.debug("getParams(String, HttpSession) - end"); //$NON-NLS-1$
				}
				return param;
			}
			else
			{
				String returnString = getDefaultParams(page);
				if (logger.isDebugEnabled()) {
					logger.debug("getParams(String, HttpSession) - end"); //$NON-NLS-1$
				}
				return returnString;
			}
		}
		else
		{
			String returnString = getDefaultParams(page);
			if (logger.isDebugEnabled()) {
				logger.debug("getParams(String, HttpSession) - end"); //$NON-NLS-1$
			}
			return returnString;
		}
	

	}

	public static String getDefaultParams(String page) {
		if (logger.isDebugEnabled()) {
			logger.debug("getDefaultParams(String) - start"); //$NON-NLS-1$
		}

		String returnString = "action=INIT&state=" + page + "_INIT";
		if (logger.isDebugEnabled()) {
			logger.debug("getDefaultParams(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}
}
