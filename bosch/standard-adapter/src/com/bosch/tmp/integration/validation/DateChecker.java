
package com.bosch.tmp.integration.validation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.hl7.v2xml.TS1CONTENT;
import java.util.Calendar;

/**
 * @author gaa2pal3
 * This Class checks if the Date entered is less then the System Date
 */
public class DateChecker extends DefaultChecker{

     public static final Logger logger = LoggerFactory.getLogger(DateChecker.class);

      public boolean isChecked(Object obj, Validation validation)
      {
        logger.debug("**********************************DATE_FORMAT_CHECKER******************************");
        boolean correctDate=false;
        Object configValue;
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

        // If object is null or not instance of TS1CONTENT, do nothing
        if ((obj == null) || !(obj instanceof TS1CONTENT))
        {
            logger.debug("Object is either null or not instance of TS1CONTENT");
            return true;
        }

        // If config value is not available or not valid do nothing
        if ((configValue == null) || !(configValue instanceof String))
        {
            logger.debug("config value is null or not valid");
            return true;
        }

        //format the Config Date
        SimpleDateFormat sdf;
        try
        {
            // Date validation using SimpleDateFormat. It will take a string and make sure it's in the proper
            // format as configured and it will also make sure that date entered is less then System Date
            //sdf = new SimpleDateFormat((String) configValue);
            //sdf.setLenient(false);
            TS1CONTENT msg = (TS1CONTENT) obj;
            String valueDate = msg.getValue();
            if( valueDate != null && !valueDate.isEmpty()){
               /* Date testDate = sdf.parse(valueDate); // format checek is done in DateForamt checker
                //Date currentDate = sdf.parse((new Date().toString()));
                Date currentDate = sdf.parse(sdf.format(new Date()));
                logger.debug("Current Date:-"+ currentDate.toString());

                logger.debug("Test Date:-"+ testDate.toString());
                if(testDate != null && currentDate  != null){
                    logger.debug("comparing ....");
                    int num = testDate.compareTo(currentDate);
                    logger.debug("resukt ...."+ num);


                    if(testDate.compareTo(currentDate)< 0){
                        return true;
                    }else{
                        return false;
                    }
                }*/
            Date toDate = new SimpleDateFormat((String) configValue).parse(valueDate);
            long toDateAsTimestamp = toDate.getTime();
            long currentTimestamp = System.currentTimeMillis();
            long getRidOfTime = 1000 * 60 * 60 * 24;
            long toDateAsTimestampWithoutTime = toDateAsTimestamp / getRidOfTime;
            long currentTimestampWithoutTime = currentTimestamp / getRidOfTime;

            if (toDateAsTimestampWithoutTime >= currentTimestampWithoutTime)
            {
                return false;
            }
            else
            {
                return true;
            }
           }else{
                return true;  // where date is optional
           }
        }
        catch (Exception e)
        {
            // if there is an exception while parsing the config value, do nothing and return true.
            logger.error("Exception was raised while parsing the configured date" + e);
            return false;
        }
        


    }


}
