package com.bosch.pat.util;

import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class AutoSystemReviewQuartzScheduler extends QuartzJobBean 
{

	public static final String JOB_NAME_KEY = "jobName";

	private JobRegistry jobRegistry ;
	public void setJobRegistry(JobRegistry jobRegistry) {
		this.jobRegistry = jobRegistry;
	}

	private JobLauncher jobLauncher ;
	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}


	@SuppressWarnings("unchecked")
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException
	{
		Map<String, Object> jobDataMap = context.getMergedJobDataMap();
		String jobName = (String) jobDataMap.get(JOB_NAME_KEY);

		//Set up the job parameters...
		JobParameters jobParameters = new JobParametersBuilder()
										  .addLong("time",System.currentTimeMillis()).toJobParameters();
		try
		{
			JobExecution jobExecution = jobLauncher.run(jobRegistry.getJob(jobName),jobParameters);

			//Monitor the Job status...
			BatchStatus batchStatus = jobExecution.getStatus();
			while(batchStatus.isRunning()) {
				System.out.println( "Still running...");
				Thread.sleep( 2 * 1000 ); // 2 seconds
			}

			System.out.println("JobExecution finished, exit code: " +
					jobExecution.getExitStatus().getExitCode());
		} 
		catch (Exception e){
			throw new JobExecutionException(e);
		}
	}
}
