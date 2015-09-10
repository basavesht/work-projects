package com.bosch.pat.util;

public enum EmailTemplateAttributes {

	JOB_NAME("job_name"),
	JOB_STATUS("job_status"),
	JOB_STEP_STATUS("job_step_status"),
	JOB_READ_COUNT("job_read_count"),
	JOB_START_TIME("job_start_time"),
	JOB_END_TIME("job_end_time"),
	JOB_COMMIT_COUNT("job_commit_count"),
	JOB_FAILURE_EXCEPTIONS("job_failure_exceptions"),
	JOB_PARAMS("job_parameters"),
	JOB_SKIP_COUNT("job_skip_count"),
	JOB_RUN_ID("job_run_id");
	
	String attribute = null;
	private EmailTemplateAttributes(String attribute) {
		this.attribute = attribute;
	}
	
	public String getAttribute(){
		return this.attribute;
	}
}
