package com.tcs.ebw.serverside.jaas.utils;

import com.tcs.ebw.common.util.EBWLogger;
import com.tcs.ebw.transferobject.EBWTransferObject;

/**
 * @author 231259
 * @year 2009
 */
public class ExecTimeLoggerHelper extends EBWTransferObject {
	
	private Double logId;
	private String userId;
	private String sessionId;
	private String serviceId;
	private String startTime;
	private String endTime;
	private String isExtLogged; 
	private String threadId;

	private static final long serialVersionUID = 1L;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Double getLogId() {
		return logId;
	}

	public void setLogId(Double logId) {
		this.logId = logId;
	}

	public String getIsExtLogged() {
		return isExtLogged;
	}

	public void setIsExtLogged(String isExtLogged) {
		this.isExtLogged = isExtLogged;
	}

	/**
	 * @return the threadId
	 */
	public String getThreadId() {
		return threadId;
	}

	/**
	 * @param threadId the threadId to set
	 */
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
}
