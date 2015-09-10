package com.bosch.pat.service.email;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;

import com.bosch.pat.model.EmailMessageTemplate;

@Service
public class EmailSenderImpl implements EmailSender {

	@Autowired
	private MailSender mailSender;

	@Autowired
	private JavaMailSender mailSenderWithAttachments;

	@Autowired
	private VelocityEngine velocityEngine;

	@Override
	public void sendJobRunStatus(boolean isJobRunSuccess, EmailMessageTemplate emailTemplate) 
	{
		//Simple Email message instance..
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailTemplate.getFrom());
		message.setTo(emailTemplate.getTo());
		message.setSubject(emailTemplate.getSubject());
		message.setText(emailTemplate.getBody());

		//Send email..
		try{
			this.mailSender.send(message);
		}
		catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

	@Override
	public void sendJobRunStatusWithAttachments(boolean isJobRunSuccess, EmailMessageTemplate emailTemplate) 
	{
		final EmailMessageTemplate emailText = emailTemplate;

		//Email message instance allowing to attach attachments too..
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "ISO-8859-1");
				message.setFrom(emailText.getFrom());
				message.setTo(emailText.getTo());
				message.setSubject(emailText.getSubject());

				String body = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, "config/templates/jobStatusReport.vm", "ISO-8859-1", emailText.getModal());
				message.setText(body, true);

				if (emailText.getAttachment()!=null && !StringUtils.isEmpty(emailText.getAttachment().getPath())) {
					FileSystemResource file = new FileSystemResource(emailText.getAttachment().getPath());
					message.addAttachment("log", file);
				}
			}
		};

		//Send email..
		try {
			this.mailSenderWithAttachments.send(preparator);
		}
		catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

}
