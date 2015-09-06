package com.bosch.tmp.integration.util;

import com.bosch.tmp.integration.validation.ConfigurationLoader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.hl7.v2xml.TS1CONTENT;

/**
 * This class provides general utility methods used across the application.
 * @author gtk1pal
 * modified by tis1pal
 */
public class Utils
{

    public static final Logger logger = LoggerFactory.getLogger(Utils.class);

    /**
     * This method converts a string value to a boolean value.
     * @param stringValue string to be converted to boolean
     * @return boolean form of a string
     */
    public static boolean convertStringToBoolean(String stringValue)
    {
        boolean booleanValue = Boolean.parseBoolean(stringValue);
        return booleanValue;
    }

    /**
     * This method converts a string value to an integer value.
     * @param stringValue string to be converted to integer
     * @return integer value of the input string
     * @throws NumberFormatException NPE when string cannot be converted to int
     */
    public static int convertStringToInt(String stringValue) throws NumberFormatException
    {
        int intValue = Integer.parseInt(stringValue);
        return intValue;
    }

    /**
     * This method converts String to Date.
     * @param dateStr The date in String format.
     * @param formatStr The format string to format the date.
     * @return Date Date value of converted string.
     */
    public static Date convertStringToDate(String dateStr, String formatStr) throws Exception
    {
        if (formatStr == null)
        {
            formatStr = "yyyyMMddHmsS";
        }
        DateFormat df = new SimpleDateFormat(formatStr);
        try
        {
            return df.parse(dateStr);
        }
        catch (ParseException ex)
        {
            return null;
        }
    }

    /**
     * This method converts inputstream read from an xml file to string
     * @param in - inputstream
     * @return - a string
     */
    public static String convertInputStreamToString(InputStream in) throws Exception
    {
        InputStreamReader is = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(is);
        String read = null;
        StringBuffer sb = new StringBuffer();
        while ((read = br.readLine()) != null)
        {
            sb.append(read);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * This method returns current timestamp in string based on the specified format.
     * @return string value of current date
     */
    public static String createCurrentTimestamp(String formatStr)
    {
        if ("".equalsIgnoreCase(formatStr)|| formatStr == null || formatStr.length() == 0)
        {
            formatStr = "yyyy-MM-dd H:m:s.S";
        }
        DateFormat df = new SimpleDateFormat(formatStr);

        //create a java calendar instance
        Calendar calendar = Calendar.getInstance();
        //get a java.util.Date from the calendar instance
        java.util.Date now = calendar.getTime();
        //get a string representation of the date
        String currentTime = df.format(now);
        return currentTime;
    }

    /**
     * This method returns current time stamp in HL7 TS1CONTENT format
     * @param timestamp
     * @return TS1CONTENT object
     */
    public static TS1CONTENT createCurrentTimestamp(final Date timestamp)
    {
        TS1CONTENT ts1 = new TS1CONTENT();
        String dateFormat = "yyyyMMddHHmmssZ";
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        String timeDisplay = df.format(timestamp);
        ts1.setValue(timeDisplay);
        return ts1;
    }

    /**
     * Method to convert date from string to XMLGregorianCalendar
     * @param str - date string
     * @return XMLGregorianCalendar
     * @throws Exception
     */
    public static XMLGregorianCalendar convertStringToXMLGregorianCalendar(String str) throws Exception
    {
        if (str == null)
        {
            return null;
        }
        XMLGregorianCalendar cal = null;
        DateFormat formatter = new SimpleDateFormat(ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.DATE_FORMAT.toString()));
        Date date = (Date) formatter.parse(str);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        DatatypeFactory df = DatatypeFactory.newInstance();
        cal = df.newXMLGregorianCalendar((GregorianCalendar) calendar);
        return cal;
    }

     public static XMLGregorianCalendar convertStringToXMLGregorianCalendar(String str,String Dateformat) throws Exception
    {
        if (str == null)
        {
            return null;
        }
        XMLGregorianCalendar cal = null;
        DateFormat formatter = new SimpleDateFormat(Dateformat);
        Date date = (Date) formatter.parse(str);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        DatatypeFactory df = DatatypeFactory.newInstance();
        cal = df.newXMLGregorianCalendar((GregorianCalendar) calendar);
        return cal;
    }
    /**
     * Method to convert Date from XMLGregorianCalendar to String
     * @param XMLGregorianCalendar
     * @return String
     */
    public static String convertXMLGregorianCalendarToString(XMLGregorianCalendar cal)
    {
        if (cal == null)
        {
            return null;
        }
        Date currentDate = cal.toGregorianCalendar().getTime();
        String dateFormat = ConfigurationLoader.getApplicationConfigValueById(ConfigKeyEnum.DATE_FORMAT.toString());
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(currentDate);
    }
    /**
     * Method to convert Date from XMLGregorianCalendar to String
     * @param XMLGregorianCalendar
     * @return String
     */
    public static String convertXMLGregorianCalendarToString(XMLGregorianCalendar cal,String dobFormat)
    {
        if (cal == null)
        {
            return null;
        }
        Date dob = cal.toGregorianCalendar().getTime();        
        SimpleDateFormat sdf = new SimpleDateFormat(dobFormat);
        return sdf.format(dob);
    }

    /**
     * Method to convert Date from XMLGregorianCalendar to Date
     * @param XMLGregorianCalendar
     * @return String
     */
    public static Date convertXMLGregorianCalendarToDate(XMLGregorianCalendar cal)
    {
        if (cal == null)
        {
            return null;
        }
        Date dob = cal.toGregorianCalendar().getTime();    
        return dob;
    }

    /**
     * Method to convert date to XMLGregorianCalendar
     * @param date
     * @return XMLGregorianCalendar
     * @throws Exception
     */
    public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) throws DatatypeConfigurationException
    {
        if (date == null)
        {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        DatatypeFactory df = DatatypeFactory.newInstance();
        XMLGregorianCalendar cal = df.newXMLGregorianCalendar((GregorianCalendar) calendar);
        return cal;
    }

}
