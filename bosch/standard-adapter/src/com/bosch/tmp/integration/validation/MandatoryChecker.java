package com.bosch.tmp.integration.validation;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsibility of this class is to check if the input object is null or not.
 *
 * @author tis1pal
 */
public class MandatoryChecker extends DefaultChecker
{

    public static final Logger logger = LoggerFactory.getLogger(MandatoryChecker.class);

    public boolean isChecked(Object obj, Validation validation)
    {
        if (validation == null)
        {
            logger.info("validation is not enabled");
            return true;
        }
        Object configValue;
        if (validation.getValue() == null)
        {
            configValue = validation.getValues().getValue();
        }
        else
        {
            configValue = validation.getValue();
        }
        // If config value is not available or not valid do nothing
        if ((configValue == null) ||
                !(configValue instanceof String) ||
                (!configValue.toString().equalsIgnoreCase("true") && !configValue.toString().equalsIgnoreCase("false")))
        {
            logger.debug("configvalue is not available or not true or false");
            return true;
        }
        // If this check is required, do the check
        if (configValue.toString().equalsIgnoreCase("true"))
        {
            logger.debug("********************MANDATORY_CHECKER***************************");
            if (obj != null)
            {
                if (obj instanceof List)
                {
                    List msg = (List) obj;
                    if (msg.size() == 0)
                    {
                        return false;
                    }
                }
                // Using Java reflection to get the value for that specified object.
                Object value = null;
                try
                {
                    value = obj.getClass().getDeclaredMethod("getValue").invoke(obj);
                    if (value != null && value.toString().trim().length()!= 0 && !value.equals("\"\""))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                catch (Exception ex)
                {
                    // leave value as null if 'getValue' can't be invoked by 'obj' or if 'obj' is null.
                    logger.info(
                            "Error while getting the field value from incoming message through reflection for mandatory validation");

                }
            }
            else
            {
                return false;
            }
        }
        // otherwise, do nothing and return true
        return true;
    }
}
