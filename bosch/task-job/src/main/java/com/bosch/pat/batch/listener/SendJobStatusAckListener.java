package com.bosch.pat.batch.listener;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameter.ParameterType;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bosch.pat.model.EmailMessageTemplate;
import com.bosch.pat.service.email.EmailSender;
import com.bosch.pat.util.EmailTemplateAttributes;
import com.bosch.pat.util.TaskUtil;

@Component
public class SendJobStatusAckListener 
{
	public static final Logger logger = Logger.getLogger(SendJobStatusAckListener.class);

	@Autowired
	private EmailSender emailSender;

	private String from;
	private String to;

	@Value("${email.from}")
	public String getFromEmail(String from) {
		return this.from = from;
	}

	@Value("${email.to}")
	public String getToEmail(String to) {
		return this.to = to;
	}

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		logger.debug("Calling BEFORE job execution listener method..");
	}

	@AfterJob
	public void afterJob(JobExecution jobExecution) 
	{
		logger.debug("Calling AFTER job execution listener method for sending email acknowledgments..");
		EmailMessageTemplate emailTemplate = new EmailMessageTemplate();
		String subject = null;

		//Create email template modal attributes...
		Map<String, Object> emailTemplateModalMap = new HashMap<String, Object> ();
		emailTemplateModalMap.put(EmailTemplateAttributes.JOB_STATUS.getAttribute(),jobExecution.getExitStatus().getExitCode());
		if(jobExecution.getJobInstance()!=null ){
			subject = "PATBatch Job Run Notification : " + StringUtils.capitalize(jobExecution.getJobInstance().getJobName()) + " (" + new Date().toString() + ") "
					+ jobExecution.getExitStatus().getExitCode();
			emailTemplateModalMap.put(EmailTemplateAttributes.JOB_NAME.getAttribute(),StringUtils.capitalize(jobExecution.getJobInstance().getJobName()));
		}
		if(jobExecution.getStepExecutions()!=null ){
			Iterator<StepExecution> it = jobExecution.getStepExecutions().iterator();
			while (it.hasNext()) {
				StepExecution stepExec = it.next();
				if(stepExec!=null) {
					emailTemplateModalMap.put(EmailTemplateAttributes.JOB_RUN_ID.getAttribute(),stepExec.getId());
					emailTemplateModalMap.put(EmailTemplateAttributes.JOB_START_TIME.getAttribute(),stepExec.getStartTime());
					emailTemplateModalMap.put(EmailTemplateAttributes.JOB_END_TIME.getAttribute(),stepExec.getEndTime());
					emailTemplateModalMap.put(EmailTemplateAttributes.JOB_STEP_STATUS.getAttribute(),stepExec.getExitStatus().getExitCode());
					emailTemplateModalMap.put(EmailTemplateAttributes.JOB_READ_COUNT.getAttribute(),stepExec.getReadCount());
					emailTemplateModalMap.put(EmailTemplateAttributes.JOB_SKIP_COUNT.getAttribute(),stepExec.getSkipCount());

					//Extracting the job parameters map...
					if(stepExec.getJobParameters()!=null  && !stepExec.getJobParameters().isEmpty()) {
						Map<String, JobParameter> jobParamsMap = stepExec.getJobParameters().getParameters();
						StringBuilder jobParams = new StringBuilder();
						for(Entry<String,JobParameter> params : jobParamsMap.entrySet()) {
							if(params!=null && params.getValue()!=null){
								if(params.getValue().getType().compareTo(ParameterType.DATE) == 0) {
									jobParams.append(StringUtils.capitalize(params.getKey()) + " = " +  TaskUtil.formatDate((Date)params.getValue().getValue()) + " UTC");
									jobParams.append("<br/>");
								}
								else {
									jobParams.append(StringUtils.capitalize(params.getKey()) + " = " +  params.getValue().getValue().toString());
									jobParams.append("<br/>");
								}

							}
						}
						emailTemplateModalMap.put(EmailTemplateAttributes.JOB_PARAMS.getAttribute(), jobParams.toString());
					}

					//Extracting the failure exceptions..
					if(stepExec.getFailureExceptions()!=null && !stepExec.getFailureExceptions().isEmpty()){
						StringBuilder jobStepFailureReasons = new StringBuilder();
						for(Throwable th : stepExec.getFailureExceptions()) {
							if(th.getCause()!=null){
								jobStepFailureReasons.append("<b> Exception : </b>");
								jobStepFailureReasons.append(th.getCause().toString() + "<br/>"+ th.getCause().getStackTrace()[0]);
								jobStepFailureReasons.append("<br/>");
							}
						}
						emailTemplateModalMap.put(EmailTemplateAttributes.JOB_FAILURE_EXCEPTIONS.getAttribute(),jobStepFailureReasons.toString());
					}
					else {
						emailTemplateModalMap.put(EmailTemplateAttributes.JOB_FAILURE_EXCEPTIONS.getAttribute(),"NONE");
					}
				}
			}
		}
		emailTemplate.setFrom(from);
		emailTemplate.setTo(to);
		emailTemplate.setSubject(subject);
		emailTemplate.setModal(emailTemplateModalMap);

		//Send email...
		logger.debug("Sending email notifications from : " + this.from + " to :" + this.to);
		emailSender.sendJobRunStatusWithAttachments(true, emailTemplate);
	}
}
