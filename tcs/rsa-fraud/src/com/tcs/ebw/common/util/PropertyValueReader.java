package com.tcs.ebw.common.util;

import java.util.HashMap;

public class PropertyValueReader
{
	static HashMap data = new HashMap();
	static String value;
	public PropertyValueReader()
	{
	}
	
	public static String getValue(String key)
	{
		if(data.get(key)!=null)
		{
			value=(String)data.get(key);
		}
		else
		{
			try
			{
				value=PropertyFileReader.getProperty(key);
				data.put(key,value);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		return value;
	}
	
	public static void main(String args[])
	{
		System.out.println(PropertyValueReader.getValue("activity_logging"));
		System.out.println(PropertyValueReader.getValue("activity_logging"));
		System.out.println(PropertyValueReader.getValue("Prepopulator_drivername"));
		System.out.println(PropertyValueReader.getValue("Prepopulator_drivername"));
		
	}
}