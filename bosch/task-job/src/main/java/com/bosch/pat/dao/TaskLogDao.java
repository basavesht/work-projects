package com.bosch.pat.dao;

import com.bosch.pat.entity.PatTaskLog;
import com.bosch.pat.exception.PATDataException;

public interface TaskLogDao {
	public void createTaskLog (PatTaskLog taskLog) throws PATDataException ;
}
