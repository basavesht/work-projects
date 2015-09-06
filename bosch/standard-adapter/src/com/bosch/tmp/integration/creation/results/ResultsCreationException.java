package com.bosch.tmp.integration.creation.results;

import com.bosch.tmp.integration.creation.CreationException;

public class ResultsCreationException extends CreationException 
{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = -3257592053196584432L;

	public ResultsCreationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ResultsCreationException(String message)
	{
		super(message);
	}
}
