package com.bosch.pat.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PatJobSchedulingManager {

	public static void main(String[] args) throws Throwable 
	{
		try {
			//Initialize a application context XML and load the quartz scheduler properties..
			ApplicationContext context = new ClassPathXmlApplicationContext("config/quartz-scheduled-batch.xml","config/applicationContext.xml");
			System.out.println(context);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}


}
