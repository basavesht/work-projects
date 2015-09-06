package com.bosch.tmp.integration.businessObjects.results;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.bosch.th.cia.Note;

public class ClinicalSurveyNotes {

	private Long surveyId;
	private XMLGregorianCalendar surveyResponseDate;
	private XMLGregorianCalendar surveyRecievedDate;
	private List<Note> notes;

	public Long getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}
	public XMLGregorianCalendar getSurveyResponseDate() {
		return surveyResponseDate;
	}
	public void setSurveyResponseDate(XMLGregorianCalendar surveyResponseDate) {
		this.surveyResponseDate = surveyResponseDate;
	}
	public XMLGregorianCalendar getSurveyRecievedDate() {
		return surveyRecievedDate;
	}
	public void setSurveyRecievedDate(XMLGregorianCalendar surveyRecievedDate) {
		this.surveyRecievedDate = surveyRecievedDate;
	}
	public List<Note> getNotes() {
		if(notes == null){
			notes = new ArrayList<Note>();
		}
		return this.notes;
	}
}
