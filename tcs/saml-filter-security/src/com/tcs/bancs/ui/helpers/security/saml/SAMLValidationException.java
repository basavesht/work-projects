package com.tcs.bancs.ui.helpers.security.saml;

import org.apache.log4j.Logger;

public class SAMLValidationException extends Exception
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SAMLValidationException.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	
	private String miscInfo = null;

	private SAMLValidationException(String msg)
	{
		super(msg);
	}
	
	private SAMLValidationException(String msg, Throwable cause)
	{
		super(msg,cause);
	}

	private SAMLValidationException(String msg, String info, Throwable cause)
	{
		super(msg,cause);
		miscInfo = info;
	}

	private SAMLValidationException(Throwable cause)
	{
		super(cause);
	}
	
	public static SAMLValidationException createInstance(String msg)
	{
		if (logger.isDebugEnabled()) {
			logger.debug("createInstance(String) - start"); //$NON-NLS-1$
		}

		SAMLValidationException returnSAMLValidationException = new SAMLValidationException(msg);
		if (logger.isDebugEnabled()) {
			logger.debug("createInstance(String) - end"); //$NON-NLS-1$
		}
		return returnSAMLValidationException;
	}
	public static SAMLValidationException createInstance(String msg, Throwable cause)
	{
		if (logger.isDebugEnabled()) {
			logger.debug("createInstance(String, Throwable) - start"); //$NON-NLS-1$
		}

		SAMLValidationException returnSAMLValidationException = new SAMLValidationException(msg, cause);
		if (logger.isDebugEnabled()) {
			logger.debug("createInstance(String, Throwable) - end"); //$NON-NLS-1$
		}
		return returnSAMLValidationException;
	}
	public static SAMLValidationException createInstance(String msg, String info, Throwable cause)
	{
		if (logger.isDebugEnabled()) {
			logger.debug("createInstance(String, Throwable) - start"); //$NON-NLS-1$
		}

		SAMLValidationException returnSAMLValidationException = new SAMLValidationException(msg, info, cause);
		if (logger.isDebugEnabled()) {
			logger.debug("createInstance(String, Throwable) - end"); //$NON-NLS-1$
		}
		return returnSAMLValidationException;
	}
	public static SAMLValidationException createInstance(Throwable cause)
	{
		if (logger.isDebugEnabled()) {
			logger.debug("createInstance(Throwable) - start"); //$NON-NLS-1$
		}

		SAMLValidationException returnSAMLValidationException = new SAMLValidationException(cause);
		if (logger.isDebugEnabled()) {
			logger.debug("createInstance(Throwable) - end"); //$NON-NLS-1$
		}
		return returnSAMLValidationException;
	}

	public String getMiscInfo() {
		return miscInfo;
	}
}
