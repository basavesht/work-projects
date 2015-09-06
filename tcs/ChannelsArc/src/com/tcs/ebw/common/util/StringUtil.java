/*
 * Created on Oct 24, 2005
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.common.util;

import java.util.Date;
import java.util.Enumeration;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * StringUtil class has Utility functions for String manipulation.
 * All the functions are static functions. It can be called without
 * class instance. 
 * <p>
 * 
 * @author TCS
 */
public class StringUtil {
    /**
     * <p>
     * Converts First character as Caps.
     * <pre>
     * 		StringUtil.initCaps("hello world");
     * 		// output is Hello world
     * </pre>
     * @param input - input string
     * @return String
     */
    public static String initCaps(String input)  {
        if (input==null || input.length() == 0) return "";
        return input.substring(0, 1).toUpperCase().concat(input.substring(1, input.length()));
    }
    
    /**
     * <p>Converts all the words in a string as Caps.
     * It splits based on space.
     * <pre>
     * 		StringUtil.allInitCaps("hello world");
     * 		// output is Hello World
     * </pre>
     * @param input
     * @return
     */
    public static String allInitCaps(String input)  {
        if (input==null || input.length() == 0) return "";
        String strArr[]=input.split(" ");
        StringBuffer strB = new StringBuffer();
        for (int i=0; i < strArr.length; i++) {
            strB.append (initCaps(strArr[i]));
            if (i < strArr.length-1) strB.append (" ");
        }
        return strB.toString();
    }
    
    /**
     * <p>
     * Converts First character as Lower case.
     * <pre>
     * 		StringUtil.initLower("Hello World");
     * 		// output is hello world
     * </pre>
     * @param input - input string
     * @return String
     */
    public static String initLower(String input)  {
        if (input==null || input.length() == 0) return "";
        return input.substring(0, 1).toLowerCase().concat(input.substring(1, input.length()));
    }
    
    /**
     * <p>Converts Double objects into formatted String object.
     * This removes 0 value in decimal place.
     * <pre>
     * 		StringUtil.convertToIntegerStr("123.0");
     * 		// output is 123
     * 
     * 		StringUtil.convertToIntegerStr("123.40");
     * 		// output is 123.4
     * </pre>
     * 
     * @param obj
     * @return
     */
    public static String convertToIntegerStr(String obj) {
        String str="";
        try {
            if (obj != null && obj.trim().length() > 0) {
		        DecimalFormat dformat = new DecimalFormat();
		        dformat.applyPattern("###############.########");
		        str = dformat.format(Double.parseDouble(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
    
    /**
     * <p>Converts Double objects into formatted String object.
     * This removes 0 value in decimal place.
     * <pre>
     * 		StringUtil.convertToDateStr(new Date());
     * 		// output is 03/01/2006
     * </pre>
     * 
     * @param obj Date is passed as a parameter.
     * @return returns String
     */
    public static String convertToDateStr(Date obj) {
        String str="";
        try {
            if (obj != null) {
                SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy");
		        str = dtFormat.format(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
    
    /**
     * <p>Converts object into String.
     * If the Object is null, then it returns empty.
     * 
     * <pre>
     * 		StringUtil.convertToStr(new TransferObject());
     * 		//output is transfer objects toString method will be called.
     * </pre>
     * @param obj
     * @return
     */
    public static String convertToStr(Object obj) {
        String strRet="";
        if (obj!=null && obj.toString().length() >0) {
            strRet = obj.toString().trim();
        }
        return strRet;
    }

    /**
     * <p>
     * printConsole is used instead of System.out.println.
     * This takes log Object. If the log object is enumeration,
     * it reads the enumeration and prints each element.
     * <p>
     * <pre>
     * StringUtil.printConsole("SellOrderEntry.doForward", request);
     * </pre>
     * 
     * @param sourceMethod - will have Class Name and Method Name
     * 					ClassName.MethodName should be passed.
     * @param objLog - can be any object.
     */
    public static void printConsole (String sourceMethod, Object objLog) {
        if (objLog instanceof Enumeration) {
            Enumeration values = (Enumeration) objLog;
            while (values.hasMoreElements()) {
                System.out.println (sourceMethod + " - Enum : " + values.nextElement());
            }
        } else if (objLog instanceof HttpServletRequest) {
            HttpServletRequest objRequest = (HttpServletRequest) objLog; 
            Enumeration attributeName = objRequest.getAttributeNames();
            String key;
            while (attributeName.hasMoreElements()) {
                key = attributeName.nextElement().toString();
                System.out.println (sourceMethod + " - Key : " + key + " \tValue : " + objRequest.getAttribute(key));
            }
        } else {
            System.out.println (sourceMethod + " - " + objLog);
        }
    }
    
    /**
     * <p>removeUnderscore method is used for removing underscore from
     * the given input string and creates the field name in a 
     * Java convenstional manner.
     * <p>
     * <code>
     * 		removeUnderscore("EXCHANGE_RATE");
     * 		// output is "exchangeRate"
     * </code> 
     * @param strInput
     * @return
     */
    public static String removeUnderscore(String strInput) {
        StringBuffer strB = new StringBuffer();
        strInput = StringUtil.initLower(strInput);
        
        //if (strInput.indexOf("_") > -1) {
            strInput = strInput.toLowerCase();
        //} 
        
        String strArr[] = strInput.split("_");
        strB.append(strArr[0]);
        
        for (int i=1; i < strArr.length; i++) {
            strB.append(StringUtil.initCaps(strArr[i]));
        }
        return strB.toString();
    }    
    
    public static void main (String str[]) {
     /*   String s = "<a href=\"SellOrderEntry.htm\" target=\"new\">~VARIABLE</a> : value : LSE";
        System.out.println ("Replace : " + s.replaceAll("~VARIABLE", "LSE"));
        
        System.out.println (StringUtil.convertToIntegerStr("234.220"));
        String onclick="onclick='javascript:alert(\"Hello\");'";
	    if (onclick != null && onclick.length() > 0 && 
	            onclick.indexOf(":") > 0 && onclick.lastIndexOf("'") > 0) {
	        System.out.println ("str : " + onclick.substring(onclick.indexOf(":")+1, onclick.lastIndexOf("'")));
	    }
	    */
        
        String query = " insert into result";
        query = query.replaceAll("result","output");
        System.out.println(query);
    }
}
