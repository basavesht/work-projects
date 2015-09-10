package com.bosch.pat.batch.writer;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.bosch.pat.autoUserLock.AutoUserLockTask;
import com.bosch.pat.model.AutoUserLockParams;


public class AutoUserLockWriter implements ItemWriter<AutoUserLockParams> 
{
	public static final Logger logger = Logger.getLogger(AutoUserLockWriter.class);

	@Autowired
	private AutoUserLockTask autoUserLockTask;

	/**
	 * Item Writer class..
	 */
	@Override
	public void write(final List<? extends AutoUserLockParams> taskList) throws Exception
	{
		logger.info("Inside the Task Writer");
		
		//Execute each task request in a loop.
		for (final AutoUserLockParams task : taskList) {
			autoUserLockTask.userLockTask(task);
		}		
	}

}
