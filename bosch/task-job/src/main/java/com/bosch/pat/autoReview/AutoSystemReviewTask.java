package com.bosch.pat.autoReview;

import com.bosch.pat.model.AutoSystemReviewParams;


public interface AutoSystemReviewTask {
	public static final String JOB_NAME="autoSystemReview";
	public static final int DEF_REVIEW_DAYS=30;

	public AutoSystemReviewParams getAutoSystemReviewParams();
	
	public void systemReviewTask(AutoSystemReviewParams params);
	
}
