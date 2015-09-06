//Source File Name:   GetApplicationData.java

package com.tcs.ebw.common.services;

import com.tcs.ebw.serverside.factory.EBWServiceFactory;
import com.tcs.ebw.serverside.factory.IEBWService;
import com.tcs.ebw.common.util.ConvertionUtil;
import java.util.ArrayList;
import java.util.HashMap;

public  class GetApplicationData
{

 public static String serviceCall(String column)
     throws Exception
 {
     Object objParams[]={column, new Boolean(false)};
     Object objOutput = null;
     Class clsParamTypes[]={java.lang.Object.class, java.lang.Boolean.class};
     try
     {
         IEBWService objService = EBWServiceFactory.create("getSystemInfo");
         objOutput = objService.execute(clsParamTypes, objParams);
     }
     catch(Exception e)
     {
         System.out.println("ApplicationData  Service Error ");
         e.printStackTrace();
     }
     ArrayList test = (ArrayList)objOutput;
     return ((ArrayList)test.get(1)).get(0).toString();
 }

 public static String serviceCall()
     throws Exception
 {
		return getSystemDate();		
 }


 //String service;
 static HashMap column=new HashMap();
	
 //UserPrincipal objUserPrincipal;
 private static String sysDate;

	private static String getSystemDate()
	{
		column.put("dateFormat",strDBOutDate);
		Object objParams[]={column, new Boolean(false)};
		Class clsParamTypes[]= {java.lang.Object.class, java.lang.Boolean.class	};
		Object objOutput = null;
		try
		{	
			if(objOutput==null){
				IEBWService objService = EBWServiceFactory.create("getSystemInfo");
				objOutput = objService.execute(clsParamTypes, objParams);
			}
		}
		catch(Exception e)
		{
			System.out.println("ApplicationData  Service Error ");
			e.printStackTrace();
		}
		ArrayList test = (ArrayList)objOutput;
		System.out.println("From Service Call :"+strDBOutDate+((ArrayList)test.get(1)).get(0).toString());
		return ((ArrayList)test.get(1)).get(0).toString();

	}
	static String strAppDate=ConvertionUtil.getApplicationDateFormat();
	static String strDBOutDate=ConvertionUtil.getDAOutDateFormat();

	/*static {
		sysDate = getSystemDate();
		column.put("dateFormat",strAppDate);
	}*/

}