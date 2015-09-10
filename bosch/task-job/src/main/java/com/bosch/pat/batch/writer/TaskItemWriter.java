package com.bosch.pat.batch.writer;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.bosch.pat.model.TaskRequest;
import com.bosch.pat.service.patient.task.TaskManager;


public class TaskItemWriter implements ItemWriter<TaskRequest> 
{
	public static final Logger logger = Logger.getLogger(TaskItemWriter.class);

	@Autowired
	private TaskManager taskManagerService;

	/**
	 * Item Writer class..
	 */
	@Override
	public void write(final List<? extends TaskRequest> taskList) throws Exception
	{
		logger.info("Inside the Task Writer");
		
		//Execute each task request in a loop.
		for (final TaskRequest task : taskList) {
			taskManagerService.createTask(task);
		}		
	}

}
