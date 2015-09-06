package com.bosch.tmp.integration.process;

import com.bosch.tmp.integration.util.AckTypeEnum;
import com.bosch.tmp.integration.util.Error;

/**
 *
 * @author gtk1pal
 */
public interface Processor
{
    public Object processMessage(String methodName,Object message) throws ProcessException; 
    public void setAckType(AckTypeEnum ackType);   
    public void setErrorObject(Error error);
}
