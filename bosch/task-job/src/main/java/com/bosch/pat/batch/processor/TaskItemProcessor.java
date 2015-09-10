package com.bosch.pat.batch.processor;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;

import com.bosch.pat.model.TaskRequest;

public class TaskItemProcessor implements ItemProcessor<TaskRequest, TaskRequest> 
{

	public static final Logger logger = Logger.getLogger(TaskItemProcessor.class);

	/**
	 * Item Processor class
	 */
	@Override
	public TaskRequest process(TaskRequest task) throws Exception {
		logger.info("Inside the ItemTask Processor");
		return task;
	}

}
