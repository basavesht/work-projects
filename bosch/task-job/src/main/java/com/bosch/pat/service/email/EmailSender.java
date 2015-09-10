package com.bosch.pat.service.email;

import com.bosch.pat.model.EmailMessageTemplate;

public interface EmailSender {

	/**
	 * Send an email regarding job run status without any attachments..
	 * @param isJobRunSuccess
	 * @param message
	 */
	public void sendJobRunStatus(boolean isJobRunSuccess,  EmailMessageTemplate message);

	/**
	 * Send an email regarding job run status with attachments..
	 * @param isJobRunSuccess
	 * @param message
	 */
	public void sendJobRunStatusWithAttachments(boolean isJobRunSuccess, EmailMessageTemplate message);

}
