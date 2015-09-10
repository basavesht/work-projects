package com.bosch.pat.batch.writer;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;

import com.bosch.pat.model.TaskRequest;
import com.bosch.pat.service.patient.task.TaskManager;


public class RetryableTaskItemWriter implements ItemWriter<TaskRequest> 
{
	public static final Logger logger = Logger.getLogger(RetryableTaskItemWriter.class);

	@Autowired
	private TaskManager taskManagerService;

	@Autowired
	private RetryTemplate retryTemplate;

	/**
	 * Item Writer class..
	 */
	@Override
	public void write(final List<? extends TaskRequest> taskList) throws Exception
	{
		logger.info("Inside the ItemTask Writer - Retryable");

		//Execute each task request in a loop with a in-built spring-batch-retry logic.
		for (final TaskRequest task : taskList) {
			retryTemplate.execute(
					new RetryCallback<Object>() {
						public Object doWithRetry(RetryContext context) throws Exception {
							taskManagerService.createTask(task);
							return null;
						}
					}
					);
		}		
	}

}
