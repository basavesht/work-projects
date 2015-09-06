package com.bosch.tmp.integration.creation;

/**
 * This is the base interface of a customer-specific and type-specific message builder.
 * It defines an operation to build a message given a list of arbritrary data.
 *
 * @author gtk1pal
 */
public interface Builder
{
    public Object buildMessage(Object ... data) throws CreationException;
}
