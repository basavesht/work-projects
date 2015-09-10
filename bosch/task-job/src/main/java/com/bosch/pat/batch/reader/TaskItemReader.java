package com.bosch.pat.batch.reader;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.bosch.pat.model.TaskJobParameters;
import com.bosch.pat.model.TaskParams;
import com.bosch.pat.model.TaskRequest;
import com.bosch.pat.service.patient.task.TaskManager;


public class TaskItemReader implements ItemReader<TaskRequest>
{
	public static final Logger logger = Logger.getLogger(TaskItemReader.class);

	@Autowired
	private TaskManager taskManagerService;

	private Collection<TaskRequest> taskList = null;
	private Iterator<TaskRequest> tasksItr = null;
	private Timestamp startTime = null;
	private Timestamp endTime = null;

	@Value("#{jobParameters['startTime']}")
	public void getStartTime(Timestamp startTime){
		this.startTime = startTime;  
	}

	@Value("#{jobParameters['endTime']}")
	public void getEndTime(Timestamp endTime){
		this.endTime = endTime;  
	}

	/**
	 * Item Reader class
	 * @return TaskRequest
	 */
	@Override
	public TaskRequest read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException 
	{
		logger.info("Inside the ItemTask Reader");

		//Create a list of task request. (This needs to be executed only once)..
		if(taskList == null) 
		{
			taskList = new ArrayList<TaskRequest>();

			//Create input request from the job parameters ...
			TaskJobParameters taskJobParams = new TaskJobParameters();
			taskJobParams.setStartTime(startTime);
			taskJobParams.setEndTime(endTime);

			//Invoke service...
			List<TaskParams> responseList = taskManagerService.getScheduledDueTasks(taskJobParams);

			logger.info("Job Read count for scheduled due tasks : " + responseList.size());
			if(responseList!=null && !responseList.isEmpty()) {
				for(TaskParams taskRequest : responseList) {
					TaskRequest request = new TaskRequest();
					request.getTasks().add(taskRequest);
					taskList.add(request);
				}
			}
			else {
				logger.info("No task found matching Job Reader parameters");
			}
			tasksItr = taskList.iterator();
		}

		//Return each task one after the other..
		if (taskList!=null && taskList.size() >= 1) {
			if(tasksItr.hasNext()){
				return (TaskRequest) tasksItr.next();
			}
		}
		return null;
	}

}
