package com.bosch.pat.dao.impl;

import org.springframework.stereotype.Repository;

import com.bosch.pat.dao.TaskLogDao;
import com.bosch.pat.entity.PatTaskLog;
import com.bosch.pat.exception.PATDataException;

@Repository
public class TaskLogDaoImpl extends GenericDaoImpl<Long, PatTaskLog> implements TaskLogDao
{
	@Override
	public void createTaskLog(PatTaskLog taskLog) throws PATDataException {
		persist(taskLog);
	} 
}
