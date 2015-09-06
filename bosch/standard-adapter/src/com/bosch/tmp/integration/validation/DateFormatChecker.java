package com.bosch.tmp.integration.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hl7.v2xml.TS1CONTENT;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Responsibility of this checker class is to validate the incoming date against the configured date format.
 *
 * @author gtk1pal
 */
public class DateFormatChecker extends DefaultChecker
{

    public static final Logger logger = LoggerFactory.getLogger(DateFormatChecker.class);

    private  final String DATE_PATTERN_YYYYMMDD = "((19|20)\\d\\d)(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])";

    private  final String DATE_PATTERN_MMDDYYYY = "(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])((19|20)\\d\\d)";

    private  final String DATE_PATTERN_DDMMYYYY = "(0?[1-9]|[12][0-9]|3[01])(0?[1-9]|1[012])((19|20)\\d\\d)";


    private final String DATE_FORMAT_YYYYMMDD="YYYYMMDD";
    private final String DATE_FORMAT_MMDDYYYY="MMDDYYYY";
    private final String DATE_FORMAT_DDMMYYYY="DDMMYYYY";


    private Pattern pattern;
    private Matcher matcher;


   

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
            // format as configured and it will also make sure that it's a legal date
            sdf = new SimpleDateFormat((String) configValue);
            sdf.setLenient(false);
            TS1CONTENT msg = (TS1CONTENT) obj;
            String date = msg.getValue();
            if( date != null && !date.isEmpty()){
                Date testDate = sdf.parse(date); //check if Date is Valid as per the Format defined
                if(testDate != null){
                   return processDate(date,configValue.toString());
                }
                else{
                    return false;
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



    public boolean processDate(String date,String dateType){
        try
        {
            if(dateType.equalsIgnoreCase(DATE_FORMAT_YYYYMMDD)){
               return validateYYYYMMDDD(date);
            }else if(dateType.equalsIgnoreCase(DATE_FORMAT_MMDDYYYY)){
               return validateMMDDYYY(date);
            }else if(dateType.equalsIgnoreCase(DATE_FORMAT_DDMMYYYY)){
                return validateDDMMYYY(date);
            }
            return false;
        }
        // if the format of the string provided in configuration doesn't match the format
        // from the message, then the message will be rejected.
        catch (Exception e)
        {
            logger.error("The format of the string provided doesn't match the format from message" + e);
            return false;
        }
    }


    /**
   * Validate date format with regular expression
   * @param date date address for validation
   * @return true valid date format, false invalid date format
   */
    public boolean validateYYYYMMDDD(final String date)
    {
        pattern = Pattern.compile(DATE_PATTERN_YYYYMMDD);
        matcher = pattern.matcher(date);

        if (matcher.matches())
        {
            matcher.reset();
            if (matcher.find())
            {
                int year = Integer.parseInt(matcher.group(1));
                String month = matcher.group(2);
                String day = matcher.group(3);
                
                if (day.equals("31") &&
                        (month.equals("4") || month.equals("6") || month.equals("9") ||
                        month.equals("11") || month.equals("04") || month.equals("06") ||
                        month.equals("09")))
                {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                }
                else if (month.equals("2") || month.equals("02"))
                {
                    //leap year
                    if (year % 4 == 0)
                    {
                        if (day.equals("30") || day.equals("31"))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    else
                    {
                        if (day.equals("29") || day.equals("30") || day.equals("31"))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                }
                else
                {
                    return true;
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

    //Validate MMDDYYYY
     public boolean validateMMDDYYY(final String date)
    {
        pattern = Pattern.compile(DATE_PATTERN_MMDDYYYY);
        matcher = pattern.matcher(date);

        if (matcher.matches())
        {

            matcher.reset();

            if (matcher.find())
            {
                String month = matcher.group(1);
                String day = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));
                if (day.equals("31") &&
                        (month.equals("4") || month.equals("6") || month.equals("9") ||
                        month.equals("11") || month.equals("04") || month.equals("06") ||
                        month.equals("09")))
                {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                }
                else if (month.equals("2") || month.equals("02"))
                {
                    //leap year
                    if (year % 4 == 0)
                    {
                        if (day.equals("30") || day.equals("31"))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    else
                    {
                        if (day.equals("29") || day.equals("30") || day.equals("31"))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                }
                else
                {
                    return true;
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

     //Validate DDMMYYYY
     public boolean validateDDMMYYY(final String date)
    {
        pattern = Pattern.compile(DATE_PATTERN_DDMMYYYY);
        matcher = pattern.matcher(date);

        if (matcher.matches())
        {

            matcher.reset();

            if (matcher.find())
            {

                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));
                if (day.equals("31") &&
                        (month.equals("4") || month.equals("6") || month.equals("9") ||
                        month.equals("11") || month.equals("04") || month.equals("06") ||
                        month.equals("09")))
                {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                }
                else if (month.equals("2") || month.equals("02"))
                {
                    //leap year
                    if (year % 4 == 0)
                    {
                        if (day.equals("30") || day.equals("31"))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    else
                    {
                        if (day.equals("29") || day.equals("30") || day.equals("31"))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                }
                else
                {
                    return true;
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
