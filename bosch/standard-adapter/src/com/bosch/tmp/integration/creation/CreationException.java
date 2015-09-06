package com.bosch.tmp.integration.creation;

/**
 * This exception is thrown when there is a failure during creation of a message.
 *
 * @author gtk1pal
 */
public class CreationException extends Exception
{
    public CreationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CreationException(String message)
    {
        super(message);
    }
}
