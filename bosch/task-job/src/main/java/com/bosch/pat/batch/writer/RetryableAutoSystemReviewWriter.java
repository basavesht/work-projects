package com.bosch.pat.batch.writer;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;

import com.bosch.pat.autoReview.AutoSystemReviewTask;
import com.bosch.pat.model.AutoSystemReviewParams;


public class RetryableAutoSystemReviewWriter implements ItemWriter<AutoSystemReviewParams> 
{
	public static final Logger logger = Logger.getLogger(RetryableAutoSystemReviewWriter.class);

	@Autowired
	private AutoSystemReviewTask autoSystemReviewService;
	@Autowired
	private RetryTemplate retryTemplate;

	/**
	 * Item Writer class..
	 */
	@Override
	public void write(final List<? extends AutoSystemReviewParams> taskList) throws Exception
	{
		logger.info("Inside the AutoSystemItemWriter  - Retryable");

		//Execute each task request in a loop with a in-built spring-batch-retry logic.
		for (final AutoSystemReviewParams task : taskList) {
			retryTemplate.execute(
					new RetryCallback<Object>() {
						public Object doWithRetry(RetryContext context) throws Exception {
							autoSystemReviewService.systemReviewTask(task);
							return null;
						}
					}
					);
		}		
	}

}
