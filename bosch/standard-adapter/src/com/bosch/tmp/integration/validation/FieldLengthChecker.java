package com.bosch.tmp.integration.validation;

import org.slf4j.Logger; import org.slf4j.LoggerFactory;

/**
 * Checking whether the incoming msg's field length exceeds configured value.
 *
 * @author gtk1pal
 */
public class FieldLengthChecker extends DefaultChecker
{

    public static final Logger logger = LoggerFactory.getLogger(FieldLengthChecker.class);

    public boolean isChecked(Object obj, Validation validation)
    {
        logger.debug("********************FIELD_LENGTH_CHECKER***************************");

        Object configValue = null;
        if (validation == null)
        {
            return true;
        }
        if (validation.getValue() == null)
        {
            configValue = validation.getValues().getValue();
        }
        else
        {
            configValue = validation.getValue();
        }

        // If config value is not available or not valid do nothing
        if ((configValue == null) || !(configValue instanceof String))
        {
            logger.debug("configValue is missing or not an instance of string");
            return true;
        }

        int configIntValue = 0;
        try
        {
            configIntValue = Integer.parseInt(configValue.toString());
        }
        catch (Exception ex)
        {
            // in case cannot parse the config value to integer, do nothing.
            logger.debug("configValue length is not an integer value");
            return true;
        }
        if (obj == null)
        {
            return true;
        }
        // Using Java reflection to get the value for that specified object.
        Object value = null;
        try
        {
            value = obj.getClass().getDeclaredMethod("getValue").invoke(obj);
        }
        catch (Exception ex)
        {
            // leave value as null if 'getValue' can't be invoked by 'obj' or if 'obj' is null.
            logger.error("Error while getting the field value from incoming message through reflection for field length validation");
        }

        int length = 0;
        if (value != null)
        {
            length = value.toString().length();
        }
        if (length <= configIntValue)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
