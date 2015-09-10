package com.bosch.pat.util;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AutoUserLockJobMainTest {

	public static void main(String[] args)
	{
		//Initialize applicationContext...
		ApplicationContext context = null;
		try 
		{
			context = new ClassPathXmlApplicationContext("config/applicationContext.xml");
			
			//Create a JobLauncher...
			JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
			
			//Get the Job...
			Job job = (Job) context.getBean("autoUserLock");
			
			//Set up the job parameters...
			JobParameters jobParameters = 
					  new JobParametersBuilder()
					  .addLong("time",System.currentTimeMillis()).toJobParameters();
			
			//Create a Job Execution...
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			
			//Monitor the Job status...
			BatchStatus batchStatus = jobExecution.getStatus();
			while(batchStatus.isRunning()) {
				System.out.println( "Still running...");
				Thread.sleep( 2 * 1000 ); // 2 seconds
			}
		} 
		catch (BeansException e) {
			e.printStackTrace();
		} catch (JobExecutionAlreadyRunningException e) {
			e.printStackTrace();
		} catch (JobRestartException e) {
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			e.printStackTrace();
		} catch (JobParametersInvalidException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
