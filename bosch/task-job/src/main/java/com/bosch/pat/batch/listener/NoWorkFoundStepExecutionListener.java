package com.bosch.pat.batch.listener;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class NoWorkFoundStepExecutionListener extends StepExecutionListenerSupport {

	public static final Logger logger = Logger.getLogger(NoWorkFoundStepExecutionListener.class);

	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.info("Checking the read count through NoWorkFoundStepExecutionListener");
		if(!stepExecution.getFailureExceptions().isEmpty()){
			logger.info("Exception encountered during step processing");
			return ExitStatus.FAILED;
		}
		else {
			if (stepExecution.getReadCount() == 0) {
				logger.info("No records exit for the specified parameters. Returning JOB status as completed.");
				return new ExitStatus("NO_RECORDS_FOUND");
			}
		}

		return null;
	}

}
