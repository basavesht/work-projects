package com.bosch.pat.autoUserLock;

import com.bosch.pat.model.AutoUserLockParams;


public interface AutoUserLockTask {
	public static final String JOB_NAME="autoIdleUserLock";
	public static final int DEF_IDLE_DAYS=30;

	public AutoUserLockParams getAutoUserLockParams();
	
	public void userLockTask(AutoUserLockParams params);
	
}
