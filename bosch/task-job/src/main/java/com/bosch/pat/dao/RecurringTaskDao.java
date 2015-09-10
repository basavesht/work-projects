package com.bosch.pat.dao;

import java.sql.Timestamp;
import java.util.List;

import com.bosch.pat.entity.PatTaskRecurring;
import com.bosch.pat.exception.PATDataException;

public interface RecurringTaskDao {
	public PatTaskRecurring updateRecurringTask (PatTaskRecurring task) throws PATDataException ;
	public PatTaskRecurring findTaskByRecurringTaskId (Long recurringTaskId) throws PATDataException ;
	public List<PatTaskRecurring> findTasksByExecutionDate (Timestamp startTime, Timestamp endTime) throws PATDataException ;
}
