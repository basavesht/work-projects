
package com.bosch.tmp.integration.validation;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hl7.v2xml.CX5CONTENT;
import com.bosch.tmp.integration.util.ApplicationConstants;

/**
 * @author gaa2pal3
 * This Class checks if the Query Type entered is Valid.
 * Query Code is  Validated against the default SIA System Result Query Type(QT).
 * If Query Type does not match  error is thrown back.
 */
public class QueryTypeChecker extends DefaultChecker{

     public static final Logger logger = LoggerFactory.getLogger(QueryTypeChecker.class);
    
      /* validating the Query Code against the SIA System  Result Query Code*/
      public boolean isChecked(Object obj, Validation validation)
      {
        logger.debug("**********************************RESULT QUERY TYPE CHECKER******************************");
        if (validation == null)
        {
            return false;
        }
        // If object is null or not instance of QPD9CONTENT, do nothing
        if (obj == null)
        {
            logger.debug("CX5CONTENT is either null or not instance of CX5CONTENT");
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
            
            /* QueryType Check*/
            if(value == null){
                logger.debug("QueryType is null");
                return false;
            }
            for (ApplicationConstants.ResultQueryTypeForCX5 cx5: ApplicationConstants.ResultQueryTypeForCX5.values()){
                 if (cx5.toString().equals(value)){
                     return true;
                  }
            }
            return false;
        }
        catch (Exception e)
        {
            // if there is an exception while parsing the query type value, do nothing and return true.
            logger.error("Exception was raised while parsing the QPD9-CX5" + e);
            return false;
        }
        


    }


}
