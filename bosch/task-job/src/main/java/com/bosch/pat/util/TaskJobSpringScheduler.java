package com.bosch.pat.util;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TaskJobSpringScheduler 
{
	@Autowired 
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("taskJob")
	private Job job;

	public void runTaskCrationJob() throws Throwable
	{
		System.out.println("Starting job at " + System.currentTimeMillis());

		//Set up the job parameters...
		JobParameters jobParameters = new JobParametersBuilder()
									  .addLong("time",System.currentTimeMillis()).toJobParameters();

		//Create a Job Execution...
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);

		//Monitor the Job status...
		BatchStatus batchStatus = jobExecution.getStatus();
		while(batchStatus.isRunning()) {
			System.out.println( "Still running...");
			Thread.sleep( 2 * 1000 ); // 2 seconds
		}

		System.out.println("JobExecution finished, exit code: " +
				jobExecution.getExitStatus().getExitCode());
	}

	//@Scheduled(fixedDelay = 1000 * 10) //Can be configured through XML or through annotation. 
	public void runTaskCreationJobOnASchedule() throws Throwable {
		runTaskCrationJob();
	}

	public static void main(String[] args) throws Throwable 
	{
		//Initialize a application context XML..
		ApplicationContext context =
				new ClassPathXmlApplicationContext("config/spring-scheduled-batch.xml","config/applicationContext.xml");
		System.out.println(context);
	}


}
