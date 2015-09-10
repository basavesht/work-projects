package com.bosch.pat.autoUserLock;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.pat.dao.AutoSystemReviewTaskDao;
import com.bosch.pat.entity.BatchJobExecution;
import com.bosch.pat.model.AutoUserLockParams;
import com.bosch.pat.util.TaskConstants;
import com.bosch.pat.util.TaskUtil;

@Service
public class AutoUserLockTaskImpl implements AutoUserLockTask{
	public static final Logger LOGGER = Logger.getLogger(AutoUserLockTaskImpl.class);
	@Autowired
	private AutoSystemReviewTaskDao autoSystemReviewDao;
	
	private AutoUserLockParams params;
	
	public AutoUserLockParams getParams() {
		return params;
	}

	public void setParams(AutoUserLockParams params) {
		this.params = params;
	}

	public AutoUserLockParams getAutoUserLockParams(){
		AutoUserLockParams params = new AutoUserLockParams();
		this.setParams(params);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		params.setDefIdleDays(DEF_IDLE_DAYS);
		BatchJobExecution recentJob = autoSystemReviewDao.mostRecentSuccessJobExec(JOB_NAME);
		if (recentJob!=null){
			Date startDate = recentJob.getStartTime();
			cal.setTime(startDate);
			cal.add(Calendar.DATE, -1);
			cal = TaskUtil.convertToOffsetTimeZone(cal, cal.getTimeZone().getDisplayName(), TaskConstants.DEF_TZ); 
			params.setPrevExecDateUtcs(cal.getTimeInMillis());
		}
		return params;
	}
	
	public void userLockTask(AutoUserLockParams params){
		if (params==null){
			LOGGER.error("userLockTask() received invalid input :"+params);
			return;
		}
		Calendar reviewTime = Calendar.getInstance();
		LOGGER.info("userLockTask() Calling stored procedure execution :"+params +" Review Time UTCS:"+reviewTime );
		autoSystemReviewDao.autoUserLockTask(params);
		LOGGER.info("userLockTask() stored procedure execution completed:"+params);
	}
	
}
