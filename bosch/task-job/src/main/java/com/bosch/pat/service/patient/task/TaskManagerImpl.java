package com.bosch.pat.service.patient.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bosch.pat.dao.ConnectEraSummaryDao;
import com.bosch.pat.dao.RecurringTaskDao;
import com.bosch.pat.dao.TaskDao;
import com.bosch.pat.entity.ConnectEraSummary;
import com.bosch.pat.entity.PatTask;
import com.bosch.pat.entity.PatTaskRecurring;
import com.bosch.pat.exception.TaskProcessException;
import com.bosch.pat.exception.UserNotAuthorizedException;
import com.bosch.pat.model.TaskJobParameters;
import com.bosch.pat.model.TaskParams;
import com.bosch.pat.model.TaskParams.TaskOwner;
import com.bosch.pat.model.TaskParams.TaskRecurringParams;
import com.bosch.pat.model.TaskRequest;
import com.bosch.pat.util.TaskConstants;
import com.bosch.pat.util.TaskUtil;

@Service
public class TaskManagerImpl implements TaskManager 
{
	public static final Logger logger = Logger.getLogger(TaskManagerImpl.class);

	@Autowired
	private TaskDao taskRepository;

	@Autowired
	private RecurringTaskDao recurringTaskRepository;

	@Autowired
	private ConnectEraSummaryDao connectEraSumaryRepository;

	@Override
	@Transactional (rollbackFor = {TaskProcessException.class})
	public void createTask(TaskRequest request) throws UserNotAuthorizedException, TaskProcessException 
	{
		try 
		{
			//Verify request..
			if(request == null || request.getTasks() == null || request.getTasks().isEmpty() ) {
				throw new IllegalArgumentException("Task request is empty");
			}

			//Task attributes..
			TaskParams taskParams = null;
			taskParams = request.getTasks().get(0);
			Timestamp taskDueDateInUTCS = new Timestamp(taskParams.getDueDate().getTimeInMillis()) ;
			Calendar currentDateInUTCS = TaskUtil.getCurrentDateInInputTZ("UTC");

			//Task instance..
			PatTask task = new PatTask();
			task.setAcntId(taskParams.getAcntId());
			task.setPatId(taskParams.getPatientId());
			task.setDescription(taskParams.getDescription());
			task.setDueDate(taskDueDateInUTCS);
			task.setStatus(TaskConstants.TASK_INCOMPLETE_STATUS);
			task.setCreatedById(taskParams.getCreatedById());
			task.setCreatedByName(taskParams.getCreatedByName());
			task.setCreatedDate(currentDateInUTCS.getTime());
			task.setAssignedTo(taskParams.getAssignedTo());

			//Update the recurring task instance...
			TaskParams.TaskRecurringParams taskRecurringParams = taskParams.getRecurringParams();
			Timestamp nextExecutionDateInUTCS = null ;
			PatTaskRecurring recurringTaskInst = null;
			if(taskRecurringParams.isRecurring()) {
				recurringTaskInst = recurringTaskRepository.findTaskByRecurringTaskId(taskRecurringParams.getRecurringId()); 
				if(TaskUtil.parseDate(taskDueDateInUTCS).compareTo(TaskUtil.parseDate(currentDateInUTCS.getTime())) == 0) {
					nextExecutionDateInUTCS = TaskUtil.getTaskNextExecutionInUTCS(TaskUtil.getNextExecutionDate(taskRecurringParams.getFrequency(), taskParams.getDueDate().getTime()));
				}
				else {
					nextExecutionDateInUTCS = taskDueDateInUTCS;
				}
				recurringTaskInst.setNextExecutionDate(nextExecutionDateInUTCS); 

				//Update the task instance...
				task.setRecurringTaskParams(recurringTaskInst);
			}

			//Persist task..
			taskRepository.createTask(task);

			//Update recurring task..
			recurringTaskRepository.updateRecurringTask(recurringTaskInst);

			//Update MOST RECENT TASK DUE DATE in CONNECT_ERA_SUMMARY...
			updateMostRecentTaskDueDate(taskParams.getPatientId(),taskParams.getAcntId());
		} 
		catch (IllegalArgumentException e) {
			throw new TaskProcessException("IllegalArgumentException : ", e);
		}
		catch (DataAccessException e) {
			throw new TaskProcessException("DataAccessException : ", e);
		}
		catch (Exception e) {
			throw new TaskProcessException("Internal Error (Task-Create): ", e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<TaskParams> getScheduledDueTasks(TaskJobParameters request) throws UserNotAuthorizedException, TaskProcessException
	{
		List<TaskParams> response = new ArrayList<TaskParams>();
		try 
		{
			//Verify request..
			if(request == null || request.getStartTime() == null || request.getEndTime() == null) {
				throw new IllegalArgumentException("Task request is empty");
			}

			//Get the task job parameters (in UTCS)...
			Timestamp startTime = request.getStartTime();
			Timestamp endTime = request.getEndTime();

			//Retrieve task list..
			List<PatTaskRecurring> recurringTaskEntities = recurringTaskRepository.findTasksByExecutionDate(startTime, endTime);
			if(recurringTaskEntities!=null && !recurringTaskEntities.isEmpty()) {
				for(PatTaskRecurring recurringTask : recurringTaskEntities) 
				{
					//TaskDue date in UTC TZ..
					Date taskDueDateInUTCS = TaskUtil.getTaskNextExecutionInUTCS(TaskUtil.getNextExecutionDate(recurringTask.getFrequency(), recurringTask.getNextExecutionDate()));

					//Task Due date(Calendar) in UTC TZ..
					Calendar taskDueDateUTCSCal = Calendar.getInstance();
					taskDueDateUTCSCal.setTime(taskDueDateInUTCS);

					//Task parameters modal..
					TaskParams taskParams = new TaskParams();
					taskParams.setAcntId(recurringTask.getAcntId());
					taskParams.setPatientId(recurringTask.getPatId());
					taskParams.setDescription(recurringTask.getDescription());
					taskParams.setDueDate(taskDueDateUTCSCal);
					taskParams.setCreatedById(recurringTask.getCreatedById());
					taskParams.setCreatedByName(recurringTask.getCreatedByName());
					taskParams.setAssignedTo(recurringTask.getAssignedTo());

					//Set the recurring instance..
					TaskRecurringParams recurringParams = taskParams.new TaskRecurringParams();
					recurringParams.setRecurringId(recurringTask.getTaskRecurringId());
					recurringParams.setFrequency(recurringTask.getFrequency());
					recurringParams.setRecurring(true);
					taskParams.setRecurringParams(recurringParams);

					//Task Owner Details...
					TaskOwner owner = taskParams.new TaskOwner();
					owner.setId(recurringTask.getCreatedById());
					owner.setName(recurringTask.getCreatedByName());
					taskParams.setOwner(owner);

					//Add to the task list ...
					response.add(taskParams);
				}
			}
			else {
				logger.info("No tasks could be found for the defined Job Parameters..");
			}
		} 
		catch (IllegalArgumentException e) {
			throw new TaskProcessException("IllegalArgumentException : ", e);
		}
		catch (DataAccessException e) {
			throw new TaskProcessException("DataAccessException : ", e);
		}
		catch (Exception e) {
			throw new TaskProcessException("Internal Error (Task-Read): ", e);
		}
		return response;
	}

	/**
	 * Update the most recent task due date...
	 * @param request
	 * @throws TaskProcessException
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void updateMostRecentTaskDueDate(Long patientId, Integer acntId) throws TaskProcessException 
	{
		try 
		{ 
			//Compute NEXT POSSIBLE TASK DUE DATE for the current patient ...
			Timestamp mostRecentTaskDueDate = null;
			List<Object> taskList = taskRepository.findNextPossibleTaskDueDateByPatId(patientId,acntId,TaskConstants.TASK_INCOMPLETE_STATUS);
			if(taskList!=null && !taskList.isEmpty()){
				mostRecentTaskDueDate = (Timestamp)((Object[])taskList.get(0))[2]; //TASK_DUE_DATE
			}
			else {
				logger.info("No Task(s) are due currently for the selected patient");
			}

			//Update ConnectEraSummary instance with the MOST_RECENT_TASK_DUE_DATE...
			List<Object> currentConnectEraSummary = connectEraSumaryRepository.findByCurrentConnectEraIdNative(acntId,patientId);
			if (currentConnectEraSummary != null && !currentConnectEraSummary.isEmpty() && currentConnectEraSummary.size() == 1) {
				ConnectEraSummary connectEraSummary = ((ConnectEraSummary)currentConnectEraSummary.get(0));
				connectEraSummary.setMostRecentTaskDueDate(mostRecentTaskDueDate);
			}
			else {
				logger.info("Could not find current ConnectEraSummary for the patient :" + patientId);
			}
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw new TaskProcessException("DataAccessException : ", e);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new TaskProcessException("Internal Error (Task-Update (Most_Recent_Task_DueDate)): ", e);
		}
	}
}
