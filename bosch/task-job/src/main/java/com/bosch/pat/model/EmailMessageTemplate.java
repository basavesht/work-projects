package com.bosch.pat.model;

import java.io.File;
import java.util.Map;

public class EmailMessageTemplate 
{
	private String from ;
	private String to ;
	private String subject ;
	private String body;
	private File attachment;
	private Map<String, Object> modal;
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public File getAttachment() {
		return attachment;
	}
	public void setAttachment(File attachment) {
		this.attachment = attachment;
	}
	public Map<String, Object> getModal() {
		return modal;
	}
	public void setModal(Map<String, Object> modal) {
		this.modal = modal;
	}
}
