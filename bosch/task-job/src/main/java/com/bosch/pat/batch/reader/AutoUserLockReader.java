package com.bosch.pat.batch.reader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import com.bosch.pat.autoUserLock.AutoUserLockTask;
import com.bosch.pat.model.AutoUserLockParams;


public class AutoUserLockReader implements ItemReader<AutoUserLockParams>
{
	public static final Logger logger = Logger.getLogger(AutoUserLockReader.class);

	@Autowired
	private AutoUserLockTask autoUserLockTask;

	private Collection<AutoUserLockParams> taskList = null;
	private Iterator<AutoUserLockParams> tasksItr = null;

	/**
	 * Item Reader class
	 * @return AutoUserLockRequest
	 */
	@Override
	public AutoUserLockParams read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException 
	{
		logger.info("Inside the ItemTask Reader");

		//Create a list of task request. (This needs to be executed only once)..
		if(taskList == null) {
			taskList = new ArrayList<AutoUserLockParams>();
			AutoUserLockParams params = autoUserLockTask.getAutoUserLockParams();
			//List<TaskParams> responseList = taskManagerService.getScheduledDueTasks(createJobReaderParams());
			logger.info("Job Read count for scheduled due tasks : " + params);
			if(params!=null ) {
				taskList.add(params);
			}
			else {
				logger.info("No task found matching Job Reader parameters");
			}
			tasksItr = taskList.iterator();
		}

		//Return each task one after the other..
		if (taskList!=null && taskList.size() >= 1) {
			if(tasksItr.hasNext()){
				return (AutoUserLockParams) tasksItr.next();
			}
		}
		return null;
	}
}
