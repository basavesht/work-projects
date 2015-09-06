package com.bosch.tmp.integration.validation;

import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.hl7.v2xml.MSH9CONTENT;

/**
 * This checker class checks if trigger event in the msg is supported by the incoming msg type
 * 
 * @author gtk1pal
 */
class UnsupportedTriggerChecker extends DefaultChecker
{
    
    public static final Logger logger = LoggerFactory.getLogger(UnsupportedTriggerChecker.class);

    private String faultId;

    public String getFaultId()
    {
        return faultId;
    }

    public void setFaultId(String faultId)
    {
        this.faultId = faultId;
    }
    public boolean isChecked(Object obj, Validation validation)
    {
        logger.debug("**********************************UNSUPPORTED_TRIGGER_CHECKER******************************");
        if (validation == null)
        {
            return true;
        }

        Object configValue = null;
        if (validation.getValue() == null)
        {
            configValue = validation.getValues().getValue();
        }
        else
        {
            configValue = validation.getValue();
        }
        
        // If config value is not available or not valid do nothing
        if ((configValue == null) || !(configValue instanceof String) ||
                !(configValue.toString().equalsIgnoreCase("true")))
        {
            logger.debug("No value set in configuration or the value is not set to true");
            return true;
        }

        if (obj == null)
        {
            return true;
        }
        //if object is not instance of MSH return true
        if (!(obj instanceof MSH9CONTENT))
        {
            return true;
        }

        MSH9CONTENT msh9content = (MSH9CONTENT) obj;
        String messageTrigger = null;
        if (msh9content != null && msh9content.getMSG2() != null)
        {
            messageTrigger = msh9content.getMSG2().getValue();
        }
        else
        {
            return false;
        }
        String messageType = null;
        if (msh9content != null && msh9content.getMSG1() != null)
        {
            messageType = msh9content.getMSG1().getValue();
        }
        else
        {
            return false;
        }
        String messageStructure = null;
        if (msh9content != null && msh9content.getMSG3() != null)
        {
            messageStructure = msh9content.getMSG3().getValue();
        }
        else
        {
            return false;
        }
        if (messageType != null && messageType.length() > 0)
        {
            if (messageTrigger != null && messageTrigger.length() > 0)
            {
                if (messageStructure != null && messageStructure.length() > 0)
                {
                    if (!(messageType.equalsIgnoreCase("ADT") || messageType.equalsIgnoreCase("QVR") || messageType.
                            equalsIgnoreCase("ACK")))
                    {
                        setFaultId("UNSUPPORTED_MESSAGE_CODE");
                        return false;
                    }
                    if (!(messageTrigger.equalsIgnoreCase("A04") ||
                            messageTrigger.equalsIgnoreCase("A08") ||
                            messageTrigger.equalsIgnoreCase("A31") ||
                            messageTrigger.equalsIgnoreCase("A03") ||
                            messageTrigger.equalsIgnoreCase("Q17")))

                    {
                        setFaultId("UNSUPPORTED_TRIGGER_EVENT");
                        return false;
                    }
                    if (!(messageStructure.equalsIgnoreCase("ADT_A01") ||                           
                            messageStructure.equalsIgnoreCase("ADT_A05") ||
                            messageStructure.equalsIgnoreCase("ADT_A03") ||
                            messageStructure.equalsIgnoreCase("QVR_Q17")))
                    {
                        setFaultId("UNSUPPORTED_MESSAGE_STRUCTURE");
                        return false;
                    }                    
                    if ((messageTrigger.equalsIgnoreCase("A04") && messageType.equalsIgnoreCase("ADT") && messageStructure.
                            equalsIgnoreCase("ADT_A01")) ||
                            (messageTrigger.equalsIgnoreCase("A08") && messageType.equalsIgnoreCase("ADT") && messageStructure.
                            equalsIgnoreCase("ADT_A01")) ||
                            (messageTrigger.equalsIgnoreCase("A31") && messageType.equalsIgnoreCase("ADT") && messageStructure.
                            equalsIgnoreCase("ADT_A05")) ||
                            (messageTrigger.equalsIgnoreCase("A03") && messageType.equalsIgnoreCase("ADT") && messageStructure.
                            equalsIgnoreCase("ADT_A03")) ||
                            (messageTrigger.equalsIgnoreCase("Q17") && messageType.equalsIgnoreCase("QVR") && messageStructure.
                            equalsIgnoreCase("QVR_Q17")) || messageType.equalsIgnoreCase("ACK"))
                    {
                        return true;
                    }
                    else
                    {
                        setFaultId("UNSUPPORTED_MESSAGE");
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}


