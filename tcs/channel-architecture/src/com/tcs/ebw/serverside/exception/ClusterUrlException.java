package com.tcs.ebw.serverside.exception;

import com.tcs.ebw.exception.EbwException;
/**
 *  This class is used in Database connection and EJB connection classes to catch exeption
 *  occured in configuration of IP and port for clustered environment.
 */

public class ClusterUrlException extends EbwException
{
		
	public ClusterUrlException()
	{
		super("SYS0017");
	}
	
	public ClusterUrlException(Object appObj, Object expObj) 
	{
		super(appObj, expObj);
	}


}
