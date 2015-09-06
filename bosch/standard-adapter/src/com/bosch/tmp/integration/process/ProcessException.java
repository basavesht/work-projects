package com.bosch.tmp.integration.process;

/**
 *
 * @author gtk1pal
 */
public class ProcessException extends Exception
{
    public ProcessException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ProcessException(String message)
    {
        super(message);
    }
}
