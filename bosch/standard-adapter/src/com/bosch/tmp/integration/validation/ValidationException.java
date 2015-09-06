package com.bosch.tmp.integration.validation;

/**
 *
 * @author gtk1pal
 */
public class ValidationException extends Exception
{
    public ValidationException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ValidationException(String message)
    {
        super(message);
    }   
}
