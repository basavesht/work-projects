package com.bosch.tmp.integration.process.results;

/**
 * Exception processing during results workflow processing...
 * @author BEA2KOR
 *
 */
public class ResultsProcessException extends Exception
{
	/**
	 *
	 */
	private static final long serialVersionUID = -577906240512576922L;

	public ResultsProcessException(String message, Throwable cause){
		super(message, cause);
	}

	public ResultsProcessException(String message){
		super(message);
	}
}
