
package com.bosch.tmp.integration.validation;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hl7.v2xml.QPD9CONTENT;
import org.hl7.v2xml.CX1CONTENT;
import com.bosch.tmp.integration.util.ApplicationConstants;

/**
 * @author gaa2pal3
 * This Class checks if the Query Code entered is Valid.
 * Query Code is  Validated against the default SIA System Result Query Code.
 * If Query Code does not match  error is thrown back.
 */
public class QueryCodeChecker extends DefaultChecker{

     public static final Logger logger = LoggerFactory.getLogger(QueryCodeChecker.class);

      /* validating the Query Code against the SIA System  Result Query Code*/
      public boolean isChecked(Object obj, Validation validation)
      {
        logger.debug("**********************************RESULT QUERY CODE CHECKER******************************");

        if (validation == null)
        {
            return false;
        }
        boolean queryCodeCheck = false;

        // If object is null or not instance of CX1CONTENT, do nothing
        if (obj == null)
        {
            logger.debug("CX1CONTENT is either null or not instance of CX1CONTENT");
            return false;
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
        try
        {

            for (ApplicationConstants.ResultQueryCodeForCX1 cx1: ApplicationConstants.ResultQueryCodeForCX1.values()){
                 if (cx1.toString().equals(value)){
                     return true;
                 }
            }
            return false;
        }
        catch (Exception e)
        {
            // if there is an exception while parsing the config value, do nothing and return true.
            logger.error("Exception was raised while parsing the QPD9-CX1" + e);
            return false;
        }



    }


}
