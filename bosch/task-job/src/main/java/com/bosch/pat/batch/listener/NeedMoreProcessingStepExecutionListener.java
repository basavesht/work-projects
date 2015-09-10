package com.bosch.pat.batch.listener;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

public class NeedMoreProcessingStepExecutionListener extends StepExecutionListenerSupport
{
	public static final Logger logger = Logger.getLogger(NeedMoreProcessingStepExecutionListener.class);

	/**
	 * The listener to re-run a step within a job after
	 * the completion of the step execution. This means if we need 
	 * to re-check some data after the step execution is over and 
	 * want to trigger the step (it even might be the same step though)
	 * then we can do that through a listener. 
	 * Create a custom exit status ans based on the value, set the 
	 * following attribute on the batch element. 
	 * <batch:next on="NEEDS_MORE_PROCESSING" to="step1"/>
	 */
	public ExitStatus afterStep(StepExecution stepExecution) 
	{
		logger.info("Checking if another processing is required through NeedMoreProcessingStepExecutionListener");
		boolean needMoreProcessing = false;
		if(stepExecution!=null && stepExecution.getFailureExceptions()!=null && !stepExecution.getFailureExceptions().isEmpty()){
			logger.info("Exception encountered during step processing");
			return ExitStatus.FAILED;
		}
		else {
			if(needMoreProcessing) {
				//Make sure to set the status as defined in job configuration attribute...
				return new ExitStatus("NEEDS_MORE_PROCESSING");
			}
		}
		return null;
	}
}
