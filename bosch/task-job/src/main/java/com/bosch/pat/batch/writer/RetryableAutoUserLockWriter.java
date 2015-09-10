package com.bosch.pat.batch.writer;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;

import com.bosch.pat.autoUserLock.AutoUserLockTask;
import com.bosch.pat.model.AutoUserLockParams;


public class RetryableAutoUserLockWriter implements ItemWriter<AutoUserLockParams> 
{
	public static final Logger logger = Logger.getLogger(RetryableAutoUserLockWriter.class);

	@Autowired
	private AutoUserLockTask autoUserLockTask;
	@Autowired
	private RetryTemplate retryTemplate;

	/**
	 * Item Writer class..
	 */
	@Override
	public void write(final List<? extends AutoUserLockParams> taskList) throws Exception
	{
		logger.info("Inside the AutoSystemItemWriter  - Retryable");

		//Execute each task request in a loop with a in-built spring-batch-retry logic.
		for (final AutoUserLockParams task : taskList) {
			retryTemplate.execute(
					new RetryCallback<Object>() {
						public Object doWithRetry(RetryContext context) throws Exception {
							autoUserLockTask.userLockTask(task);
							return null;
						}
					}
					);
		}		
	}

}
