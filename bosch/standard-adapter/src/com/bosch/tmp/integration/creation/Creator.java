package com.bosch.tmp.integration.creation;

/**
 * This is the base interface of a customer-specific message creator.
 * It defines operations to build messages of a variety of types (ack, result etc).
 *
 * @author gtk1pal
 */
public interface Creator
{
    public Object createMessage(String messageType, Object ... data) throws CreationException;    
}
