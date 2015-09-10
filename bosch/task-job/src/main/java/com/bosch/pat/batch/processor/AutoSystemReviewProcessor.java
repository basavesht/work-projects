package com.bosch.pat.batch.processor;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;

import com.bosch.pat.model.AutoSystemReviewParams;

public class AutoSystemReviewProcessor implements ItemProcessor<AutoSystemReviewParams, AutoSystemReviewParams> 
{

	public static final Logger logger = Logger.getLogger(AutoSystemReviewProcessor.class);

	/**
	 * Item Processor class
	 */
	@Override
	public AutoSystemReviewParams process(AutoSystemReviewParams task) throws Exception {
		logger.info("Inside the AutoSystemReview Processor");
		return task;
	}

}
