package com.bosch.pat.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bosch.pat.model.EmailMessageTemplate;
import com.bosch.pat.service.email.EmailSender;
import com.bosch.pat.service.email.EmailSenderImpl;

public class SendEmailTest {

	public static void main (String[] args) throws IOException{
		
		final Properties properties = new Properties();
		properties.load(SendEmailTest.class.getClassLoader().getResourceAsStream("config/email.properties"));
		
		//Initialize a application context XML and load the quartz scheduler properties..
		ApplicationContext context = new ClassPathXmlApplicationContext("config/applicationContext.xml");
		System.out.println(context);
		
		EmailMessageTemplate emailTemplate = new EmailMessageTemplate();
		emailTemplate.setFrom(properties.getProperty("email.from"));
		emailTemplate.setTo(properties.getProperty("email.to"));
		emailTemplate.setSubject("Hi");
		emailTemplate.setBody("Test email");
		
		EmailSender email = context.getBean(EmailSenderImpl.class);
		email.sendJobRunStatus(true, emailTemplate);
	}
 }
