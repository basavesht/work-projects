package com.tcs.ebw.common.util;



import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.apache.struts.upload.FormFile;

import java.util.ResourceBundle;

/**
 * <p>
 * NumberUtil class has Utility functions for Number Formatting.
 * All the functions are static functions. It can be called without
 * class instance. 
 * <p>
 * 
 * @author TCS
 */
public class ConvertionUtil {

	/**
	 * variable used for DB Input Date Format
	 */
	private static String dbDateFormat;

	/**
	 * variable used for Application Date Format
	 */
	private static String appDateFormat;

	private static String appLongDateFormat;
	private static String appDateTimeFormat;

	/**
	 * variable used for DB Output Date Format
	 */
	private static String dbOutDateFormat;

	private static String dbDateTimeFormat;
	private static String dbOutDateTimeFormat;

	private static String decFormat=null;
	private static String inFormat=null;
	private static String seperator=null;
	private static String cDecimal=null;
	private static String aDecimal=null;
	private static int dec=0;
	private static int adec=0;
	private static int informat=0;
	private static char cDec;
	private static char sep;
	private static String cyFmt="";//added for Currency Format

	private static String negativeIndicatorValue="DefaultIndValue";


	static 
	{
		try
		{	dbDateTimeFormat = PropertyFileReader.getProperty("DB_DATETIME_FORMAT");
		dbDateFormat = PropertyFileReader.getProperty("DB_DATE_FORMAT");
		appDateFormat = PropertyFileReader.getProperty("APP_DATE_FORMAT");
		dbOutDateFormat = PropertyFileReader.getProperty("DB_OUT_DATE_FORMAT");
		dbOutDateTimeFormat = PropertyFileReader.getProperty("DB_OUT_DATETIME_FORMAT");
		appDateTimeFormat = PropertyFileReader.getProperty("APP_DATETIME_FORMAT");
		appLongDateFormat = PropertyFileReader.getProperty("APP_LONGDATE_FORMAT");
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}


		// Start for Amount Format in EbwTable
		ResourceBundle ebwTableRb = null;
		try
		{
			ebwTableRb = ResourceBundle.getBundle("com.tcs.ebw.taglib.EbwTableResource");
			decFormat = ebwTableRb.getString("decFormat");



			inFormat = ebwTableRb.getString("inFormat");
			seperator = ebwTableRb.getString("seperator");
			cDecimal = ebwTableRb.getString("cDecimal");
			aDecimal = ebwTableRb.getString("amtDecFormat");
			cyFmt = ebwTableRb.getString("currencyFormat");
			dec = Integer.parseInt(decFormat);
			adec = Integer.parseInt(aDecimal);
			informat = Integer.parseInt(inFormat);
			sep = seperator.charAt(0);
			cDec = cDecimal.charAt(0);
			negativeIndicatorValue = PropertyFileReader.getProperty("negativeIndicatorValue");
		}
		catch(Exception exc)
		{
			EBWLogger.logDebug("ConvertionUtil:NumberFormat", "ResourceBundle key not found :" + exc);
		}
		//End for Amount Format in EbwTable


	}    

	/**
	 * Constructor of ConversionUtil     
	 */
	public ConvertionUtil()
	{

	}


	/**
	 * This method is used to convert to Database date format
	 * @param s input String as parameter
	 * @return String
	 */
	public static String convertToDBDateStr(String s)
	{
		Object obj = null;
		StringBuffer stringbuffer = new StringBuffer();
		try
		{
			SimpleDateFormat simpledateformat = new SimpleDateFormat();
			simpledateformat.applyPattern(appDateFormat);
			java.util.Date date = simpledateformat.parse(s);
			simpledateformat.applyPattern(dbDateFormat);
			simpledateformat.format(date, stringbuffer, new FieldPosition(0));
		}
		catch(Exception exception)
		{
			exception.printStackTrace();

		}
		return stringbuffer.toString();
	}


	//added fro pagination 
	//This will include even the hour, minute and sec.
	public static String convertToDBDateTimeStr(String s)
	{

		Object obj = null;
		StringBuffer stringbuffer = new StringBuffer();
		try
		{
			SimpleDateFormat simpledateformat = new SimpleDateFormat();
			simpledateformat.applyPattern(appDateTimeFormat);
			java.util.Date date = simpledateformat.parse(s);
			simpledateformat.applyPattern(dbDateTimeFormat);
			simpledateformat.format(date, stringbuffer, new FieldPosition(0));
		}
		catch(Exception exception)
		{
			exception.printStackTrace();

		}

		return stringbuffer.toString();
	}
	/**
	 * This function is used to convert to Application Date Format
	 * @param s String as input parameter
	 * @return String 
	 */
	public static String convertToAppDateStr(String s)
	{
		EBWLogger.trace("ConvertionUtil :","into convertToAppDateStr");
		Object obj = null;
		StringBuffer stringbuffer = new StringBuffer();
		try
		{
			SimpleDateFormat simpledateformat = new SimpleDateFormat();
			simpledateformat.applyPattern(dbOutDateFormat);
			java.util.Date date = simpledateformat.parse(s);

			EBWLogger.logDebug("ConvertionUtil :","Got Date object");
			simpledateformat.applyPattern(appDateFormat);
			simpledateformat.format(date, stringbuffer, new FieldPosition(0));
			EBWLogger.logDebug("ConvertionUtil :","formatted SimpledateFormat");

		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil","Finished convertToAppDateStr");
		return stringbuffer.toString();
	}

	/**
	 * Added by Basavesh T for the MS Implementation on 02092009
	 * This function is used to take object(Date or timestamp object) as parameter & convert it into Application Date format (Application Date format) 
	 * @param obj Object as input parameter
	 * @return String 
	 */

	public static String convertToAppDateStr(Object obj)
	{
		//EBWLogger.trace("ConversionUtil :","Starting Method convertToString with input parameter :"+obj);
		String s = null;
		if(obj != null)
		{
			if(obj instanceof java.util.Date || obj instanceof java.sql.Timestamp)
			{
				SimpleDateFormat simpledateformat =null;
				simpledateformat = new SimpleDateFormat(appDateFormat);
				s = simpledateformat.format(obj);
			}
		} else
		{
			s = null;
		}
		//EBWLogger.trace("ConversionUtil :", "Exit from Method convertToString with result :"+s);
		return s;
	}

	/**
	 * This function is used to convert to Application Date Format
	 * @param s String as input parameter
	 * @return String 
	 */
	public static String convertToAppDateTimeStr(String s)
	{
		EBWLogger.trace("ConvertionUtil :","into convertToAppDateStr");
		Object obj = null;
		StringBuffer stringbuffer = new StringBuffer();
		try
		{
			SimpleDateFormat simpledateformat = new SimpleDateFormat();

			simpledateformat.applyPattern(dbOutDateTimeFormat);
			java.util.Date date = simpledateformat.parse(s);

			EBWLogger.logDebug("ConvertionUtil :","Got Date object");
			simpledateformat.applyPattern(appDateTimeFormat);
			simpledateformat.format(date, stringbuffer, new FieldPosition(0));
			EBWLogger.logDebug("ConvertionUtil :","formatted SimpledateFormat");

		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil","Finished convertToAppDateStr");
		return stringbuffer.toString();
	}



	/**
	 * Takes String as a parameter and converts into 
	 * a Database specific Date object.
	 * 
	 * @param strDate
	 * @return Date object.
	 */
	public static Date convertToDBDate(java.util.Date date)
	{
		EBWLogger.trace("ConvertionUtil :", "Before objDate creation");
		Date date1 = null;
		try
		{            
			date1 = new Date(date.getTime());         
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil :", "After objDate creation");
		return date1;
	}
	/**
	 * Takes String as a parameter and converts into 
	 * a Double object.
	 * 
	 * @param strDouble
	 * @return Double object.
	 */
	public static Double convertToDouble(String strDouble) {
		EBWLogger.trace("ConversionUtil :", "Starting Method convertToDouble with input parameter :"+strDouble);
		Double objDouble = null;
		try {
			if (strDouble!=null && strDouble.length() > 0)  {
				DecimalFormat objDecimalFormat = new DecimalFormat();
				objDecimalFormat.applyPattern("###,###,###,###,###,###,###.###");
				if (strDouble!=null && strDouble.indexOf(".") < 0) 
					strDouble = strDouble + ".0";
				if (strDouble!=null && strDouble.indexOf("$")!=-1) 
					strDouble = strDouble.substring(1);

				objDouble = new Double(objDecimalFormat.parse(strDouble).doubleValue());
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		EBWLogger.trace("ConversionUtil :","Exit From d convertToDouble with result :"+objDouble);
		return objDouble;
	}   

	/**
	 * This function is used to take String as parameter & convert it into Date format
	 * @param s  String as input parameter
	 * @return Date
	 */
	public static java.util.Date convertToDate(String s)
	{
		EBWLogger.trace("ConversionUtil :", (new StringBuilder("Starting Method convertToDate with input parameter :")).append(s).toString());
		java.util.Date date = null;
		if(s != null && s.length() > 0 && !s.equalsIgnoreCase("MM/DD/YYYY"))
			try
		{
				SimpleDateFormat simpledateformat = new SimpleDateFormat();
				if(s.indexOf("day") != -1)
					simpledateformat.applyPattern(appLongDateFormat);
				else
					simpledateformat.applyPattern(appDateFormat);
				date = simpledateformat.parse(s);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConversionUtil :", (new StringBuilder("Exit from Method convertToDate with result :")).append(date).toString());
		return date;
	}

	/**
	 * This function is used to take object as parameter & convert it into String 
	 * @param obj Object as input parameter
	 * @return String 
	 */

	public static String convertToString(Object obj)
	{
		//EBWLogger.trace("ConversionUtil :","Starting Method convertToString with input parameter :"+obj);
		String s = null;
		if(obj != null)
		{
			if(obj instanceof java.util.Date)
			{
				SimpleDateFormat simpledateformat =null;
				if(obj instanceof java.sql.Timestamp){

					simpledateformat = new SimpleDateFormat(dbOutDateTimeFormat);
				}
				else
					simpledateformat = new SimpleDateFormat(appDateFormat);

				s = simpledateformat.format(obj);

			} else
				if(obj instanceof Double)
				{
					DecimalFormat decimalformat = new DecimalFormat();
					decimalformat.applyPattern("###############.########");
					s = decimalformat.format(Double.parseDouble(obj.toString()));
				} else
					if(obj.toString().length() > 0)
						s = obj.toString();
		} else
		{
			s = null;
		}
		//EBWLogger.trace("ConversionUtil :", "Exit from Method convertToString with result :"+s);
		return s;
	}

	/**
	 * This function is used to take Object Array as parameter &  convert it into String Array
	 * @param aobj Object Array
	 * @return  String Array
	 */

	public static String[] convertToStringArray(Object aobj[])
	{
		//EBWLogger.trace("ConversionUtil :", "Starting Method convertToStringArray with input parameter :"+aobj);
		String as[] = (String[])null;
		if(aobj != null)
		{
			as = new String[aobj.length];
			for(int i = 0; i < aobj.length; i++)
				if(aobj[i] instanceof java.util.Date)
				{
					SimpleDateFormat simpledateformat = new SimpleDateFormat(appDateFormat);
					as[i] = simpledateformat.format(aobj[i]);
				} else
					if(aobj[i] instanceof Double)
					{
						DecimalFormat decimalformat = new DecimalFormat();
						decimalformat.applyPattern("###############.########");
						as[i] = decimalformat.format(Double.parseDouble(aobj[i].toString()));
					} else
						if(aobj[i].toString().length() > 0)
						{
							as[i] = aobj[i].toString();
							EBWLogger.logDebug("ConversionUtil :", "Values from the String[] are" + as[i]);
						}

		} else
		{
			as = (String[])null;
		}
		//EBWLogger.trace("ConversionUtil :", "Exit from Method convertToStringArray with result :"+as);
		return as;
	}

	/**
	 * This function is used to convert object to String
	 * @param obj Object as inout parameter
	 * @return String 
	 */
	public static String convertToUIString(Object obj)
	{
		String s = convertToString(obj);
		if(s == null || s != null && s.equals("null"))
			s = "";
		return s;
	}

	/**
	 * This function is used to take two String parameters and convert it into HashMap
	 * @param s String as input parameter
	 * @param s1 String as input parameter
	 * @return HashMap
	 */
	public static HashMap convertToMap(String s, String s1)
	{
		EBWLogger.trace("ConversionUtil :","Starting Method  convertToMap with input parameters  :"+s+" and "+s1);
		HashMap hashmap = new HashMap();
		if(s != null && s.length() > 0)
		{
			String as[] = s.split(s1);
			for(int i = 0; i < as.length; i++)
			{
				String s2 = as[i].substring(0, as[i].indexOf("=")).toUpperCase();
				String s3 = as[i].substring(as[i].indexOf("=") + 1);
				hashmap.put(s2, s3);
			}

		}
		EBWLogger.trace("ConversionUtil :", "Exit from Method convertToMap with result :"+hashmap);
		return hashmap;
	}

	/**
	 * This function is used to convert String[] to String
	 * @param as String Array as input parameter
	 * @return String
	 */
	public static String convertToCSV(String as[])
	{
		EBWLogger.trace("ConversionUtil :", "Starting Method convertToCSV with input parameter :");
		if(as.length == 1 && as[0].equals(""))
			return null;
		StringBuffer stringbuffer = new StringBuffer();
		for(int i = 0; i < as.length; i++)
		{
			EBWLogger.logDebug("ConversionUtil :","Values inside foorloop is :" + as[i]);
			stringbuffer.append(as[i]);
			stringbuffer.append(",");
		}      
		stringbuffer.deleteCharAt(stringbuffer.toString().length() - 1);   
		EBWLogger.trace("ConversionUtil :", "Exit from Method convertToCSV with result :"+stringbuffer.toString());
		return stringbuffer.toString();
	}

	/**
	 * This function is used to convert String to TimeStamp
	 * @param s String as input parameter
	 * @return TimeStamp
	 */
	public static Timestamp convertToTimestamp(String s)
	{
		EBWLogger.trace("ConversionUtil :", "Starting Method convertToDate with input parameter :"+s);
		java.util.Date date = null;
		long time=0;
		if(s != null && s.length() > 0 && !s.equalsIgnoreCase("MM/DD/YYYY"))
		{
			try
			{
				SimpleDateFormat simpledateformat = new SimpleDateFormat(); 
				if(s.indexOf("day")!=-1)
					simpledateformat.applyPattern(appLongDateFormat); // added for long date format
				else
					simpledateformat.applyPattern(appDateFormat);
				java.util.Date d = simpledateformat.parse(s); 

				time = d.getTime();            	
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
			EBWLogger.trace("ConversionUtil :", "Exit from Method convertToDate with result :"+date);
		}
		else
		{
			return null;
		}
		return(new Timestamp(time)); 
	}

	/**
	 * This function is used to Convert Date to TimeStamp
	 * @param dtDate java.util.Date 
	 * @return String 
	 */

	public static Timestamp convertToTimestamp(java.util.Date dtDate) {
		EBWLogger.trace("ConversionUtil :", "Starting Method convertToTimestamp with input parameter :"+dtDate);
		java.sql.Timestamp objTimestamp = null;
		String strRet=null;
		try {                      
			if(dtDate==null)
				objTimestamp=null;
			else{
				objTimestamp = new java.sql.Timestamp(  (dtDate).getTime());
				EBWLogger.logDebug("ConversionUtil :","Enter into TimeStamp");

			}           

		} catch (Exception exc) {
			exc.printStackTrace();
		}
		EBWLogger.trace("ConversionUtil :", "Exit from Method convertToTimestamp with result :"+objTimestamp);
		return objTimestamp;
	} 

	/**
	 * This function is used to take int & date as parameter and returns Date
	 * @param i  integer as parameter
	 * @param date input Date 
	 * @return Output Date
	 */
	public static java.util.Date toRequiredDate(int i, java.util.Date date)
	{
		EBWLogger.logDebug("ConversionUtil :","value of toRequiredDate is :"+i);
		long l = (long)i * 24L * 60L * 60L * 1000L;
		java.util.Date date1 = new java.util.Date(date.getTime() + l);
		return date1;
	}

	public static String getApplicationDateFormat() {
		return appDateFormat;
	}

	public static String getDAOutDateFormat() {
		return dbOutDateFormat;
	}

	public static String ConvertToUserNumberFormat(String value,int dec,char sep,char cDec,int informat){
		Locale locale1;
		String[] stdecarray= new String[2];
		String result;
		String fracValue=null;
		String fraction = null;
		String resultValue;
		if (cDec == ',')
			locale1 = Locale.GERMANY;
		else
			locale1 = Locale.US;

		String sign = "";
		if(value!=null && value.length()>0){
			if (value.indexOf("-")>-1){
				sign="-";
				value = value.substring(value.indexOf("-")+"-".length(), value.length());
			}
			if (value.indexOf(".")>-1){
				fracValue=value.substring(value.indexOf(".")+ 1,value.length());
				if(fracValue.length()>dec)
					fracValue=fracValue.substring(0,dec);
			}
			boolean nonZeroFract = false;
			fraction=fracValue;
			String frac=fraction;
			if(frac!=null && frac.length()>0){
				int f=frac.length();
				while(frac.length()>=1){
					if(frac.startsWith("0"))
						frac = frac.substring(1, frac.length());
					else{
						nonZeroFract=true;
						break;
					}
				}
			}
			String[] starray = value.split("\\.");
			NumberFormat formatter = NumberFormat.getInstance(locale1);
			BigDecimal bd = new BigDecimal(value);
			try{

				formatter.setMaximumFractionDigits(dec);
				formatter.setMinimumFractionDigits(dec);
				result = formatter.format(bd).toString();
				stdecarray = result.split("\\"+cDec);


			}catch(Exception e){
				System.out.println("Excep:"+e);
			}
			String stnum = starray[0];

			int F1 = 3;
			String strdec="0";

			EBWLogger.logDebug("ConvertionUtil:","decFormat is :" + dec);

			for(int j=0;j<dec-1;j++){
				strdec="0" + strdec;
			}
			if(stnum.length() <= 3){
				String  st = null;
				if(stdecarray[1]!=null && stdecarray[1].length()>0){
					if(stdecarray[1].equalsIgnoreCase(strdec))
						st =  stnum;
					else
						st =  (stnum+cDec+stdecarray[1]);

				} 
				if(sign!=null && sign.length()>0)
					st=sign+st;
				if(st.indexOf(".")>-1 ){
					resultValue=st.substring(st.indexOf(".")+ 1,st.length());
					resultValue=resultValue.substring(0,fracValue.length());
					st=st.substring(0,st.indexOf("."))+"."+ resultValue;
				}
				//System.out.println("nonZeroFract if is :"+nonZeroFract);
				if(!nonZeroFract && (fraction!=null && fraction.length()>0)){
					st=st+"."+fraction;
				}
				return st;
			}else{
				String stnum1 =stnum.substring(stnum.length()-F1,stnum.length());
				stnum = stnum.substring(0,stnum.length()-F1);

				F1 = informat;

				for (int i = 0; i < Math.floor((stnum.length()-(1+i))/F1); i++){
					stnum = stnum.substring(0,stnum.length()-((F1+1)*i+F1))+sep+stnum.substring(stnum.length()-((F1+1)*i+F1));
				}
				String  st = null;
				if(stdecarray[1]!=null && stdecarray[1].length()>0){
					if(stdecarray[1].equalsIgnoreCase(strdec))
						st =  stnum+sep+stnum1;
					else					
						st =  (stnum+sep+stnum1+cDec+stdecarray[1]);

				} 
				if(sign!=null && sign.length()>0)
					st=sign+st;
				//System.out.println("St is :"+  st);
				if(st.indexOf(".")>-1){
					resultValue=st.substring(st.indexOf(".")+ 1,st.length());
					resultValue=resultValue.substring(0,fracValue.length());
					//System.out.println("resultValue 2:"+ resultValue);
					st=st.substring(0,st.indexOf("."))+"."+ resultValue;

				}
				//System.out.println("nonZeroFract else is :"+nonZeroFract);
				if(!nonZeroFract && (fraction!=null && fraction.length()>0)){
					st=st+"."+fraction;
				}
				return st;
			}
		}else
			return "";
	}

	public static String convertToNumericFormat(String value,int dec,char sep,char cDec,int informat){
		String stsym = "";
		if(value.charAt(0) == '-')
		{		
			value = value.substring(1,value.length());		
			stsym ="-";		
		}

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(dec, BigDecimal.ROUND_HALF_UP);		
		String stvalue = bd.toString();
		String[] starray = stvalue.split("\\.");
		String stnum = starray[0];
		int F1 = 3;		
		if(stnum.length() <= 3){
			if(dec>0){
				return(stsym+stnum+cDec+starray[1]);
			}
			else{
				return(stsym+stnum);
			}
		}
		else{		
			String stnum1 =stnum.substring(stnum.length()-F1,stnum.length());		
			stnum = stnum.substring(0,stnum.length()-F1);

			F1 = informat;

			for (int i = 0; i < Math.floor((stnum.length()-(1+i))/F1); i++){
				stnum = stnum.substring(0,stnum.length()-((F1+1)*i+F1))+sep+stnum.substring(stnum.length()-((F1+1)*i+F1));
			}
			if(dec>0){
				return (stsym+stnum+sep+stnum1+cDec+starray[1]);
			}
			else{
				return (stsym+stnum+sep+stnum1);
			}
		}
	}

//	Convertion Util Methods Added For WMP
	public static StringBuffer convertToStringBuffer(String val)
	{
		EBWLogger.trace("ConvertionUtil :", "Before StringBuffer creation");
		StringBuffer str=null;
		try
		{            
			str=new StringBuffer(val);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil :", "After StringBuffer creation");
		return str;
	}

	public static java.math.BigDecimal convertToBigDecimal(String val,char separator)
	{
		EBWLogger.trace("ConvertionUtil :", "Before BigDecimal creation");
		if(val.indexOf(separator)>-1)
			val=removeSeparator(val,separator);
		java.math.BigDecimal str=null;
		try
		{            
			str=new java.math.BigDecimal(val);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil :", "After BigDecimal creation");
		return str;
	}

	public static java.math.BigDecimal convertToBigDecimal(String val)
	{
		EBWLogger.trace("ConvertionUtil :", "Before BigDecimal creation");    	
		BigDecimal str=null;
		try
		{     
			if(val!=null && !val.equalsIgnoreCase(""))
			{
				if(val.indexOf("$")!=-1){
					val = val.replace("$", "");
				}
				if(val.indexOf(",")!=-1){
					val = val.replaceAll(",", "");
				}
				str=new BigDecimal(val);
			}
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil :", "After BigDecimal creation");
		return str;
	}

	public static int convertToint(String val,char separator)
	{
		EBWLogger.trace("ConvertionUtil :", "Before int creation");
		if(val.indexOf(separator)>-1)
			val=removeSeparator(val,separator);
		Integer intval=null;
		int value=0;
		try
		{            
			intval=new Integer(val);
			value=intval.intValue();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil :", "After int creation");
		return value;
	}

	public static int convertToint(String val)
	{
		EBWLogger.trace("ConvertionUtil :", "Before int creation");
		Integer intval=null;
		int value=0;
		try
		{   
			if(val!=null && !val.equalsIgnoreCase("")){         
				intval=new Integer(val);
				value=intval.intValue();
			}
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil :", "After int creation");
		return value;
	}

	public static double convertTodouble(String val,char separator)
	{
		EBWLogger.trace("ConvertionUtil :", "Before double creation");
		if(val.indexOf(separator)>-1)
			val=removeSeparator(val,separator);
		Double intval=null;
		double value=0.0;
		try
		{            
			intval=new Double(val);
			value=intval.doubleValue();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil :", "After double creation");
		return value;
	}

	public static double convertTodouble(String val)
	{
		EBWLogger.trace("ConvertionUtil :", "Before double creation");    	
		Double intval=null;
		double value=0.0;
		try
		{            
			intval=new Double(val);
			value=intval.doubleValue();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil :", "After double creation");
		return value;
	}

	public static long convertTolong(String val,char separator)
	{
		EBWLogger.trace("ConvertionUtil :", "Before long creation");
		if(val.indexOf(separator)>-1)
			val=removeSeparator(val,separator);
		Long intval=null;
		long value=0;
		try
		{            
			intval=new Long(val);
			value=intval.longValue();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil :", "After long creation");
		return value;
	}

	public static long convertTolong(String val)
	{
		EBWLogger.trace("ConvertionUtil :", "Before long creation");    	
		Long intval=null;
		long value=0;
		try
		{            
			intval=new Long(val);
			value=intval.longValue();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil :", "After long creation");
		return value;
	}

	public static char convertTochar(String val)
	{
		EBWLogger.trace("ConvertionUtil :", "Before long creation");
		Character inputval=null;
		char value=0;
		try
		{            
			inputval=new Character(val.charAt(0));
			value=inputval.charValue();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil :", "After long creation");
		return value;
	}

	public static com.tcs.mastercraft.mctype.MasterCraftDate convertToMasterCraftDate(String val,String userDateFormat)
	{
		EBWLogger.trace("ConvertionUtil :", "Before MasterCraftDate creation");
		com.tcs.mastercraft.mctype.MasterCraftDate dateval=null;
		SimpleDateFormat dateFormat=null;
		try
		{
			if(userDateFormat==null ||userDateFormat.length()==0)
				dateFormat=new SimpleDateFormat("dd-MMM-yyyy");
			else
				dateFormat=new SimpleDateFormat(userDateFormat);
			java.util.Date dtVal=dateFormat.parse(val);
			dateval=new com.tcs.mastercraft.mctype.MasterCraftDate(dtVal);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		EBWLogger.trace("ConvertionUtil :", "After MasterCraftDate creation");
		return dateval;
	}


	public static String convertToDateFormat(Object val,String userDateFormat){		
		String dateresult="";    	
		SimpleDateFormat formatterdate;
		if(userDateFormat==null ||userDateFormat.length()==0)
			formatterdate=new SimpleDateFormat(appDateFormat);
		else
			formatterdate=new SimpleDateFormat(userDateFormat);
		try
		{
			dateresult = formatterdate.format(val);		
		}catch(Exception e){
			System.out.println("Excep:"+e);
		}
		return dateresult;
	}

	public static String convertToDisplayFormat(int val,int dec,String sep,String cDec,int informat){
		BigDecimal value=new BigDecimal(val);
		String returnval=convertToNumericFormat(value.toPlainString(),dec,sep.charAt(0),cDec.charAt(0),informat);
		return returnval;
	}

	public static String convertToDisplayFormat(long val,int dec,String sep,String cDec,int informat){
		BigDecimal value=new BigDecimal(val);
		String returnval=convertToNumericFormat(value.toPlainString(),dec,sep.charAt(0),cDec.charAt(0),informat);
		return returnval;
	}

	public static String convertToDisplayFormat(double val,int dec,String sep,String cDec,int informat){
		BigDecimal value=new BigDecimal(val);
		String returnval=convertToNumericFormat(value.toPlainString(),dec,sep.charAt(0),cDec.charAt(0),informat);
		return returnval;
	}

	public static String convertToDisplayFormat(BigDecimal val,int dec,String sep,String cDec,int informat){
		String returnval=convertToNumericFormat(val.toPlainString(),dec,sep.charAt(0),cDec.charAt(0),informat);
		return returnval;
	}




	public static com.tcs.mastercraft.mctype.MasterCraftBString convertToMasterCraftBString(FormFile objFile)
	{
		com.tcs.mastercraft.mctype.MasterCraftBString  objMasterCraftBString=null;
		try
		{
			int fileSize =objFile.getFileSize();

			byte[] strBuffer = new byte[fileSize];

			objFile.getInputStream().read(strBuffer);

			objMasterCraftBString= new com.tcs.mastercraft.mctype.MasterCraftBString (strBuffer,objFile.getFileSize());
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		return objMasterCraftBString;
	}

	public static String removeSeparator(String stdbcurr,char sep) {
		String result = "";
		for (int i = 0; i < stdbcurr.length(); i ++) {
			if (stdbcurr.charAt(i) != sep) result += stdbcurr.charAt(i);
		}
		if (sep == '.'){
			result = result.replace(',','.');	
		}
		return result;
	}

	public static String ConvertToUserNumberFormat(String value,int dec,char sep,char cDec,int informat,String datatype){
		System.out.println("decFormat="+decFormat+",inFormat="+inFormat+",seperator="+seperator+",cDecimal="+cDec+",aDecimal="+aDecimal);


		cDec=cDecimal.charAt(0);
		Locale locale1;
		String[] stdecarray= new String[2];
		String result;
		String fracValue=null;
		String fraction = null;
		String resultValue;
		informat=Integer.parseInt(inFormat);
		System.out.println("informatis="+informat);
		if (cDec == ',')
			locale1 = Locale.GERMANY;
		else
			locale1 = Locale.US;

		String sign = "";
		/**Checking whether value is null or not.Then it will check for indexOf(-).
		 * Calculate the fraction Value.
		 * Doing the Formatting on the value before decimal. 
		 */
		if(value!=null && value.length()>0){
			if(!datatype.equals("SignedAmount1")){
				if (value.indexOf("-")>-1){
					sign="-";
					value = value.substring(value.indexOf("-")+"-".length(), value.length());

				}
			}
			if (value.indexOf(".")>-1){
				fracValue=value.substring(value.indexOf(".")+ 1,value.length());

				if(fracValue.length()>dec)
					fracValue=fracValue.substring(0,dec);
			}

			String[] starray = value.split("\\.");
			NumberFormat formatter = NumberFormat.getInstance(locale1);
			BigDecimal bd = new BigDecimal(value);

			try{	        	
				formatter.setMaximumFractionDigits(dec);
				formatter.setMinimumFractionDigits(dec);
				result = formatter.format(bd).toString();
				stdecarray = result.split("\\"+cDec);

			}catch(Exception e){
				System.out.println("Excep:"+e);
			}
			String stnum = starray[0];

			EBWLogger.logDebug("ConvertionUtil:","decFormat is :" + dec);
			int F1=informat;

			String strdec="0";			
			for(int j=0;j<dec-1;j++){
				strdec="0" + strdec;
			}


			/* If the length of Number is less than informat then show the number before decimal without seperator
			 * and after decimal till dec variable
			 * Else condition is checking if the fracValue is there then it will show the number with fracValue
			 * If decimal is not there in number then it will append the strdec with the number for Amount datatype
			 * If length of fracValue is less than dec then it will append 0 in that. 
			 */

			if(stnum.length() <= F1){
				String  st = null;
				if(!datatype.equals("Amount") && !datatype.equals("SignedAmount") && !datatype.equals("CurrencyAmount") && !datatype.equals("SignedCurrencyAmount")){
					System.out.println("decFormat="+cDec);
					if(stdecarray[1]!=null && stdecarray[1].length()>0){
						if(stdecarray[1].equalsIgnoreCase(strdec))
							st =  stnum;
						else
							st =  (stnum+cDec+stdecarray[1]);
					} 	

					if(sign!=null && sign.length()>0)
						st=sign+st;



					if(st.indexOf(".")>-1 ){
						resultValue=st.substring(st.indexOf(".")+ 1,st.length());			
						st=st.substring(0,st.indexOf("."))+"."+ resultValue;
					}

				}else{

					if(fracValue!=null && fracValue.length()>=1){
						if(fracValue.length()<dec){
							for(int k=fracValue.length();k<dec;k++){
								fracValue=fracValue+"0";
							}					
						}	
						st=stnum+ "." + fracValue;
					} else 
						st=stnum+ "." + strdec;
					if(sign!=null && sign.length()>0)
						st=sign+st;
				}				

				if(datatype.equals("Amount")){
					st=cyFmt+st;//added for Currency Format
				}				

				return st;
			}else{
				String stnum1 =stnum.substring(stnum.length()-F1,stnum.length());
				stnum = stnum.substring(0,stnum.length()-F1);

				for (int i = 0; i < Math.floor((stnum.length()-(1+i))/F1); i++){
					stnum = stnum.substring(0,stnum.length()-((F1+1)*i+F1))+sep+stnum.substring(stnum.length()-((F1+1)*i+F1));
				}				

				String  st = null;
				if(stdecarray[1]!=null && stdecarray[1].length()>0){
					if(stdecarray[1].equalsIgnoreCase(strdec))
						st =  stnum+sep+stnum1;
					else					
						st =  (stnum+sep+stnum1+cDec+stdecarray[1]);			
				} 

				if(sign!=null && sign.length()>0)
					st=sign+st;

				if(!datatype.equals("Amount") && !datatype.equals("SignedAmount") && !datatype.equals("CurrencyAmount") && !datatype.equals("SignedCurrencyAmount")){				
					if(st.indexOf(".")>-1){
						resultValue=st.substring(st.indexOf(".")+ 1,st.length());
						resultValue=resultValue.substring(0,fracValue.length());
						st=st.substring(0,st.indexOf("."))+"."+ resultValue;							
					}
				}else{
					if(st.indexOf(".")<0)
						st=st+"."+strdec;
				}				
				if(datatype.equals("Amount")){
					st=cyFmt+st;//added for Currency Format
				}

				return st;

			}

		}else
			return "";

	}

	/**
	 * This function is used to return String based on first Argument
	 * @param obj Object as input parameter
	 * @return String 
	 */
	public static String convertToReqCol(String obj,String dataType){
		EBWLogger.logDebug("Inside convertToReqCol Method: ","value is :" + obj);

		String value=obj;
		String strOutput=null;
		String[] strValue=new String[2];
		String[] strDiffIndicator=new String[2];

		EBWLogger.logDebug("convertToReqCol Method :", "negativeIndicatorValueis :" + negativeIndicatorValue);
		if(negativeIndicatorValue!=null){
			if(negativeIndicatorValue.indexOf(",")>-1)
				strDiffIndicator=negativeIndicatorValue.split(",");
			else{
				strDiffIndicator[0]=negativeIndicatorValue;
			}
		}

		if(dataType.equals("SignedCurrencyAmount") || dataType.equals("SignedQuantity") || dataType.equals("SignedAmount") || dataType.equals("CurrencyAmount")){

			strValue=value.split("-");
			if(strValue.length>1){
				for(int i=0;i<strDiffIndicator.length;i++){
					if(strValue[0].equals(strDiffIndicator[i])){
						value="-" + strValue[1];
						break;
					}else 
						value=strValue[1];	
				}
			}


			if(dataType.equals("SignedQuantity")){
				strOutput=ConvertToUserNumberFormat(value,dec,sep,cDec,informat,dataType);
			}else {
				strOutput=ConvertToUserNumberFormat(value,adec,sep,cDec,informat,dataType);
			}



			if(dataType.equals("SignedCurrencyAmount")){
				if(strValue.length>2)
					strValue[0]=strValue[2];	    	
				else
					strValue[0]="";
			}

			if(dataType.equals("CurrencyAmount") || dataType.equals("SignedCurrencyAmount")){	    		
				if(!strValue[0].equals(""))
					strOutput=strValue[0] +"~" + strOutput;
				else
					strOutput=strOutput;
			} 

		} else {
			if(dataType.equals("Amount"))
				strOutput=ConvertToUserNumberFormat(value,adec,sep,cDec,informat,dataType);
			else
				strOutput=ConvertToUserNumberFormat(value,dec,sep,cDec,informat,dataType);    		
		}

		return strOutput;
	}

	/**
	 * This function is used to get the Current System time 
	 * @param obj Object as input parameter
	 * @return String 
	 */
	public static String getCurrentTime(){

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(appDateFormat);
		return objSimpleDateFormat.format(cal.getTime());
	}



	public static void main(String args[])
	{
		/*System.out.println("convertToDBDateStr " + convertToMap("exchng=NSE~markettype=Normal Cash ~trdqnty=150", "~"));
        System.out.println("convertToDBDateStr " + convertToDBDateStr("12/02/2006"));
        System.out.println("Convert to datesTRING :" + convertToString(convertToDate("27/24/2006")));
        System.out.println("Convert to date :" + convertToDate("27/24/2006"));
        System.out.println("Convert to Timestamp :" + convertToTimestamp("27/04/2006"));*/

		//String dtstr="02/12/2007 12:05:06";
		//System.out.println("convertToAppDateTimeStr "+ConvertionUtil.convertToAppDateTimeStr(dtstr));
		//System.out.println("Date :"+new java.util.Date(dtstr));
		//System.out.println(new java.util.Date(107,11,7).toString());
		//System.out.println("Db date :"+convertToDBDate(new java.util.Date(107,11,7)).toString());
		//System.out.println("convertToAppDateStr "+ConvertionUtil.convertToString((Object)(convertToDBDate(new java.util.Date(107,11,7)))));
		/*String s = "welcome( to my city";
        String s1 = "bangalore";
        System.out.println((new StringBuffer(s)).replace(0, s.length(), s1));*/
		String value = "1538.02";
		System.out.println("input :"+ value);
		int dec = 6;
		char sep = ',';
		char cDec = '.';
		int informat = 3;
		System.out.println("formatusercurr()  :"+ ConvertToUserNumberFormat(value,dec,sep,cDec,informat));

		String str = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

		java.util.Date date = new java.util.Date();
		System.out.println("date:"+new java.util.Date());
		System.out.println(ConvertionUtil.convertToDBDateStr(str));
		System.out.println(ConvertionUtil.convertToAppDateStr(ConvertionUtil.convertToDBDateStr("23/12/1979")));
	}   
}