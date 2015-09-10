package com.bosch.pat.batch.processor;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;

import com.bosch.pat.model.AutoUserLockParams;

public class AutoUserLockProcessor implements ItemProcessor<AutoUserLockParams, AutoUserLockParams> 
{

	public static final Logger logger = Logger.getLogger(AutoUserLockProcessor.class);

	/**
	 * Item Processor class
	 */
	@Override
	public AutoUserLockParams process(AutoUserLockParams task) throws Exception {
		logger.info("Inside the AutoSystemReview Processor");
		return task;
	}

}
