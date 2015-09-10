package com.bosch.pat.entity;

import java.util.Date;

/**
 * The Class BatchJobExecution.
 */
public class BatchJobExecution {

	/** The job execution id. */
	private long jobExecutionId;
	
	/** The version. */
	private int version;
	
	/** The job instance id. */
	private long jobInstanceId;
	
	/** The start time. */
	private Date startTime;
	
	/** The end time. */
	private Date endTime;
	
	/** The status. */
	private String status;
	
	/** The exit code. */
	private String exitCode;
	
	/** The exit message. */
	private String exitMessage;
	
	/** The last updated. */
	private Date lastUpdated;
	
	/**
	 * Gets the job execution id.
	 *
	 * @return the job execution id
	 */
	public long getJobExecutionId() {
		return jobExecutionId;
	}

	/**
	 * Sets the job execution id.
	 *
	 * @param jobExecutionId the new job execution id
	 */
	public void setJobExecutionId(long jobExecutionId) {
		this.jobExecutionId = jobExecutionId;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 *
	 * @param version the new version
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * Gets the job instance id.
	 *
	 * @return the job instance id
	 */
	public long getJobInstanceId() {
		return jobInstanceId;
	}

	/**
	 * Sets the job instance id.
	 *
	 * @param jobInstanceId the new job instance id
	 */
	public void setJobInstanceId(long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime the new start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime the new end time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the exit code.
	 *
	 * @return the exit code
	 */
	public String getExitCode() {
		return exitCode;
	}

	/**
	 * Sets the exit code.
	 *
	 * @param exitCode the new exit code
	 */
	public void setExitCode(String exitCode) {
		this.exitCode = exitCode;
	}

	/**
	 * Gets the exit message.
	 *
	 * @return the exit message
	 */
	public String getExitMessage() {
		return exitMessage;
	}

	/**
	 * Sets the exit message.
	 *
	 * @param exitMessage the new exit message
	 */
	public void setExitMessage(String exitMessage) {
		this.exitMessage = exitMessage;
	}

	/**
	 * Gets the last updated.
	 *
	 * @return the last updated
	 */
	public Date getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * Sets the last updated.
	 *
	 * @param lastUpdated the new last updated
	 */
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BatchJobExecution [jobExecutionId=" + jobExecutionId
				+ ", version=" + version + ", jobInstanceId=" + jobInstanceId
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", status=" + status + ", exitCode=" + exitCode
				+ ", exitMessage=" + exitMessage + ", lastUpdated="
				+ lastUpdated + "]";
	}

}
