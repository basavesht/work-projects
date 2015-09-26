package com.tcs.ebw.taglib;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tcs.ebw.common.util.PropertyFileReader;

public class CompareRecords implements Comparable 
{ 
	 String numericalDataType = "integer,float,number,numeric,double,BigDecimal,amount,''";
	 String otherDataType = "date,Date,DateTime,Timestamp,timestamp,datetime,TimeStamp";
	 String otherStringType = "varchar";
	  //private  String dbDateFormat;
	  //private  String appDateFormat;
	  //private  String appDateTimeFormat;
	  private  String dbOutDateFormat;
	  //private  String dbDateTimeFormat;
	  private  String dbOutDateTimeFormat;
	  
	 
	 String dataType;
    Object obj = null; 
    public CompareRecords(Object o,String dataType) 
    { 
        obj = o;
        this.dataType=dataType;
        
    } 
    public int compareTo(Object o)  { 
            int flag = 0; 
     try{
            Object o1 = obj; 
            Object o2 = o; 
            if (o1 == null && o2 == null) {
                return 0; 
            }
            else if (o1 == null) { // Define null less than everything. 
                return -1; 
            } 
            else if (o2 == null) { 
                return 1; 
            }
            if(dataType!=null){
             if (numericalDataType.indexOf(dataType)>-1)
            {
       		 
       		 //System.out.println("inside numer");
       		 Number n1 = (Number)new java.math.BigDecimal(o1.toString());
                double d1 = n1.doubleValue();
                Number n2 = (Number)new java.math.BigDecimal(o2.toString());
                double d2 = n2.doubleValue();
       		 
                if (d1 < d2)
                    return -1;
                else if (d1 > d2)
                    return 1;
                else
                    return 0;
            }
            else if (otherDataType.indexOf(dataType)>-1)
            {
       	 try
            {	
       		 //dbDateTimeFormat = PropertyFileReader.getProperty("DB_DATETIME_FORMAT");
             //dbDateFormat = PropertyFileReader.getProperty("DB_DATE_FORMAT");
             //appDateFormat = PropertyFileReader.getProperty("APP_DATE_FORMAT");
             dbOutDateFormat = PropertyFileReader.getProperty("DB_OUT_DATE_FORMAT");
             dbOutDateTimeFormat = PropertyFileReader.getProperty("DB_OUT_DATETIME_FORMAT");
             //appDateTimeFormat = PropertyFileReader.getProperty("APP_DATETIME_FORMAT");
               
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
            
       	 SimpleDateFormat simpledateformat = new SimpleDateFormat();
       	 if(dataType.equalsIgnoreCase("date") || dataType.equalsIgnoreCase("Date"))
       		 simpledateformat.applyPattern(dbOutDateFormat);
       	 else if(dataType.equalsIgnoreCase("dateTime") || dataType.equalsIgnoreCase("DateTime"))
       		 simpledateformat.applyPattern(dbOutDateTimeFormat);
            
                  
       	// System.out.println("inside date");
       	
                Date d1 = (Date)simpledateformat.parse(o1.toString());
                long n1 = d1.getTime();
                Date d2 = (Date)simpledateformat.parse(o2.toString());
                long n2 = d2.getTime();
               // System.out.println("n1="+n1+" n2="+n2);
                if (n1 < n2)
                    return -1;
                else if (n1 > n2)
                    return 1;
                else return 0;
            
            }
        else if (otherStringType.indexOf(dataType)>-1)
            {
       //	 System.out.println("inside varchar");
                String s1 = (String)o1;
                String s2    = (String)o2;
                int result = s1.compareTo(s2);

                if (result < 0)
                    return -1;
                else if (result > 0)
                    return 1;
                else return 0;
            }
       
        else
            {
     //  	 System.out.println("inside else");
                Object v1 = o1;
                String s1 = v1.toString();
                Object v2 = o1;
                String s2 = v2.toString();
                int result = s1.compareTo(s2);
               // System.out.println("inside else result="+result);
                if (result < 0)
                    return -1;
                else if (result > 0)
                    return 1;
                else return 0;
            }
     }
     }
     catch(Exception e)
     {e.printStackTrace();}
      //  } 
        return flag; 
    } 
    
    public boolean equals(Object o)  {
    	if(obj!=null)
    		return obj.equals(o);
    	else
    		return false;
    	
    }
    
    public int hashCode() {
    	  assert false : "hashCode not designed";
    	  return 42; // any arbitrary constant will do 
    	  }
  
    
  
} 
