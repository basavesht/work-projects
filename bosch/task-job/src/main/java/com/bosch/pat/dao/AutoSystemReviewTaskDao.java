package com.bosch.pat.dao;

import com.bosch.pat.entity.BatchJobExecution;
import com.bosch.pat.model.AutoSystemReviewParams;
import com.bosch.pat.model.AutoUserLockParams;

public interface AutoSystemReviewTaskDao {

	void systemReviewTask(AutoSystemReviewParams params);
	
	BatchJobExecution mostRecentSuccessJobExec(String jobName);
	
	void autoUserLockTask(AutoUserLockParams params);
}
