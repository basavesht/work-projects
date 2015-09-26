package com.tcs.ebw.common.util;

import java.util.Date;
import com.tcs.ebw.common.services.GetApplicationData;
import com.tcs.ebw.common.util.EBWLogger;

public class ApplicationUtil
{
	public ApplicationUtil()
	{
	}
	
	public static Date currentDate()
	{
		Date d=null;
		try
		{
			d=new Date(ConvertionUtil.convertToAppDateStr(GetApplicationData.serviceCall()));
			//d=new Date(GetApplicationData.serviceCall());
		}catch(Exception e){System.out.println(e);}
		//System.out.println(d.toString());
		return d;
	}
	
	public static String currentDateStr()
	{
		String d=null;
		try
		{
			d=ConvertionUtil.convertToAppDateStr(GetApplicationData.serviceCall());
		}catch(Exception e){System.out.println(e);}
		//System.out.println(d.toString());
		return d;
	}
	public static void main(String args[])
	{
		System.out.println((ApplicationUtil.currentDate().toString()));
	}
	
}