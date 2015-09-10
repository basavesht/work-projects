package com.bosch.pat.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.CollectionUtils;

public class TaskJobQuartzScheduler extends QuartzJobBean 
{
	public static final Logger logger = Logger.getLogger(TaskJobQuartzScheduler.class);

	public static final String JOB_NAME_KEY = "jobName";

	private JobRegistry jobRegistry ;
	public void setJobRegistry(JobRegistry jobRegistry) {
		this.jobRegistry = jobRegistry;
	}

	private JobLauncher jobLauncher ;
	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	private JobExplorer jobExplorer;
	public void setJobExplorer(JobExplorer jobExplorer) {
		this.jobExplorer = jobExplorer;
	}

	private Timestamp startTime = null;
	private Timestamp endTime = null;

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException
	{
		try
		{
			Map<String, Object> jobDataMap = context.getMergedJobDataMap();
			String jobName = (String) jobDataMap.get(JOB_NAME_KEY);
			String noOfPastDays = (String) jobDataMap.get("noOfPastDays");

			createTaskJobReaderParams(jobName,Integer.parseInt(noOfPastDays));

			//Set up the job parameters...
			JobParametersBuilder jobParamsBuilder = new JobParametersBuilder();
			jobParamsBuilder.addParameter("executionTime", new JobParameter(System.currentTimeMillis()));
			jobParamsBuilder.addParameter("startTime", new JobParameter(startTime));
			jobParamsBuilder.addParameter("endTime", new JobParameter(endTime));
			JobParameters jobParameters = jobParamsBuilder.toJobParameters();

			//Launch Job
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

	/**
	 * Create the input parameters required to execute job reader...
	 * @return TaskJobParameters
	 * @throws  
	 */
	private void createTaskJobReaderParams(String jobName, Integer noOfPastDays) {
		try 
		{
			//Get last successfull job execution instance...
			JobExecution lastJobRunExec = getLastSuccessfulJobExecution(jobName);
			Date lastJobRunDate = null;
			if(lastJobRunExec!=null) {
				lastJobRunDate = lastJobRunExec.getLastUpdated();
				logger.debug("Last successful job (Task Job) run date " + lastJobRunDate.toString());
			}

			startTime = TaskUtil.getScheduledTasksDueStartTime(lastJobRunDate, noOfPastDays);
			endTime = TaskUtil.getScheduledTasksDueEndTime();

			logger.debug("Task Job Read Query Start Time : " + startTime.toString());
			logger.debug("Task Job Read Query End Time : " + endTime.toString());
		} 
		catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This will retrieve the latest or last job instance completed sucessfully...
	 * @param jobName
	 */
	private JobExecution getLastSuccessfulJobExecution (String jobName)
	{
		int startJobInstanceIndex = 0;
		boolean foundLastCompletedJobInstance = false; 
		while (!foundLastCompletedJobInstance) {
			List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, startJobInstanceIndex, 1);
			if (jobInstances!=null  && !CollectionUtils.isEmpty(jobInstances)) {
				List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstances.get(0));
				if (!CollectionUtils.isEmpty(jobExecutions)) {
					for (JobExecution jobExec : jobExecutions) {
						if(jobExec.getExitStatus().equals(ExitStatus.COMPLETED)) {
							foundLastCompletedJobInstance = true;
							return jobExec;
						}
					}
				}
			}
			else {
				//No More JobInstances avaliable..
				return null ;
			}
			startJobInstanceIndex ++ ;
		}
		return null;
	}
}
