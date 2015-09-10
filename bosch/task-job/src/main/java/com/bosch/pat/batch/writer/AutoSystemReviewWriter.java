package com.bosch.pat.batch.writer;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.bosch.pat.autoReview.AutoSystemReviewTask;
import com.bosch.pat.model.AutoSystemReviewParams;


public class AutoSystemReviewWriter implements ItemWriter<AutoSystemReviewParams> 
{
	public static final Logger logger = Logger.getLogger(AutoSystemReviewWriter.class);

	@Autowired
	private AutoSystemReviewTask autoSystemReviewService;

	/**
	 * Item Writer class..
	 */
	@Override
	public void write(final List<? extends AutoSystemReviewParams> taskList) throws Exception
	{
		logger.info("Inside the Task Writer");
		
		//Execute each task request in a loop.
		for (final AutoSystemReviewParams task : taskList) {
			autoSystemReviewService.systemReviewTask(task);
		}		
	}

}
