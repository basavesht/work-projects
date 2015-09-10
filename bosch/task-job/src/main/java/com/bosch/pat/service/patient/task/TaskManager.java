package com.bosch.pat.service.patient.task;

import java.util.List;

import com.bosch.pat.exception.TaskProcessException;
import com.bosch.pat.exception.UserNotAuthorizedException;
import com.bosch.pat.model.TaskJobParameters;
import com.bosch.pat.model.TaskParams;
import com.bosch.pat.model.TaskRequest;

public interface TaskManager {

	/**
	 * Create task for a care manager associated to a particular patient.
	 * @param request
	 * @throws UserNotAuthorizedException
	 * @throws TaskCreateException
	 */
	public void createTask(TaskRequest request) throws UserNotAuthorizedException, TaskProcessException ;

	/**
	 * Get the list of all assigned task for the care manager of an associated patient.
	 * @param request
	 * @return
	 * @throws UserNotAuthorizedException
	 * @throws TaskReadException
	 */
	public List<TaskParams> getScheduledDueTasks(TaskJobParameters request) throws UserNotAuthorizedException, TaskProcessException ;
}
