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

import com.bosch.pat.autoReview.AutoSystemReviewTask;
import com.bosch.pat.model.AutoSystemReviewParams;


public class AutoSystemReviewReader implements ItemReader<AutoSystemReviewParams>
{
	public static final Logger logger = Logger.getLogger(AutoSystemReviewReader.class);

	@Autowired
	private AutoSystemReviewTask autoSystemReviewService;

	private Collection<AutoSystemReviewParams> taskList = null;
	private Iterator<AutoSystemReviewParams> tasksItr = null;

	/**
	 * Item Reader class
	 * @return AutoSystemReviewRequest
	 */
	@Override
	public AutoSystemReviewParams read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException 
	{
		logger.info("Inside the ItemTask Reader");

		//Create a list of task request. (This needs to be executed only once)..
		if(taskList == null) {
			taskList = new ArrayList<AutoSystemReviewParams>();
			AutoSystemReviewParams params = autoSystemReviewService.getAutoSystemReviewParams();
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
				return (AutoSystemReviewParams) tasksItr.next();
			}
		}
		return null;
	}
}
