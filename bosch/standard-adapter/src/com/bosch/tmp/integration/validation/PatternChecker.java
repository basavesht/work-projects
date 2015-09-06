package com.bosch.tmp.integration.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;

/**
 * Pattern checker class validates an identifier against configured pattern
 *
 * @author gtk1pal
 */
public class PatternChecker extends DefaultChecker
{

    public static final Logger logger = LoggerFactory.getLogger(PatternChecker.class);

    public boolean isChecked(Object obj, Validation validation)
    {
        logger.debug("**********************************PATTERN_CHECKER******************************");
        
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
        if ((configValue == null) || !(configValue instanceof String))
        {
            logger.debug("config value is missing or not string");
            return true;
        }else{
            logger.debug("config value :"+ configValue);
        }
        if (validation == null)
        {
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
            logger.debug("Exception raised while getting value using java reflexion");
        }
        if (value != null && value.toString().trim().length() >0)
        {
            boolean isValid = false;
            //Read reg ex from config file
            String expression = (String) configValue;
            CharSequence inputStr = value.toString().trim();
             //Make the comparison case-insensitive.
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches())
            {
                isValid = true;
            }
            return isValid;
        }
        // otherwise, do nothing and return true
        return true;
    }
}
