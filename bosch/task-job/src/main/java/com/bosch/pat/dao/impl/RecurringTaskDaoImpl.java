package com.bosch.pat.dao.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bosch.pat.dao.RecurringTaskDao;
import com.bosch.pat.entity.PatTaskRecurring;
import com.bosch.pat.exception.PATDataException;

@Repository
public class RecurringTaskDaoImpl extends GenericDaoImpl<Long, PatTaskRecurring> implements RecurringTaskDao
{
	@Override
	public PatTaskRecurring updateRecurringTask(PatTaskRecurring task) throws PATDataException {
		return merge(task);
	}

	@Override
	public PatTaskRecurring findTaskByRecurringTaskId(Long recurringTaskId) throws PATDataException {
		return findById(recurringTaskId);
	}

	@Override
	public List<PatTaskRecurring> findTasksByExecutionDate(Timestamp startTime, Timestamp endTime) throws PATDataException {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("startTime",startTime);
		params.put("endTime",endTime);
		return (List<PatTaskRecurring>)findByNamedQueryAndNamedParams("PatTaskRecurring.findTasksByExecutionDate", params);
	}

}
