package com.bosch.pat.autoReview;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.pat.dao.AutoSystemReviewTaskDao;
import com.bosch.pat.entity.BatchJobExecution;
import com.bosch.pat.model.AutoSystemReviewParams;
import com.bosch.pat.util.TaskConstants;
import com.bosch.pat.util.TaskUtil;

@Service
public class AutoSystemReviewTaskImpl implements AutoSystemReviewTask{
	public static final Logger LOGGER = Logger.getLogger(AutoSystemReviewTaskImpl.class);
	@Autowired
	private AutoSystemReviewTaskDao autoSystemReviewDao;
	
	private AutoSystemReviewParams params;
	
	public AutoSystemReviewParams getParams() {
		return params;
	}

	public void setParams(AutoSystemReviewParams params) {
		this.params = params;
	}

	public AutoSystemReviewParams getAutoSystemReviewParams(){
		AutoSystemReviewParams params = new AutoSystemReviewParams();
		this.setParams(params);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Calendar currentDayUTCS = TaskUtil.convertToOffsetTimeZone(cal, cal.getTimeZone().getDisplayName(), TaskConstants.DEF_TZ); 
		params.setReviewTimeUtcs(currentDayUTCS.getTimeInMillis());
		params.setDefReviewDays(DEF_REVIEW_DAYS);
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
	
	public void systemReviewTask(AutoSystemReviewParams params){
		if (params==null){
			LOGGER.error("systemReviewTask() received invalid input :"+params);
			return;
		}
		Calendar reviewTime = Calendar.getInstance();
		reviewTime.setTimeInMillis(params.getReviewTimeUtcs());
		Calendar previousExec = Calendar.getInstance();
		previousExec.setTimeInMillis(params.getReviewTimeUtcs());
		LOGGER.info("systemReviewTask() Calling stored procedure execution :"+params +" Review Time UTCS:"+reviewTime +" previousExec:"+previousExec);
		autoSystemReviewDao.systemReviewTask(params);
		LOGGER.info("systemReviewTask() stored procedure execution completed:"+params);
	}
	
}
