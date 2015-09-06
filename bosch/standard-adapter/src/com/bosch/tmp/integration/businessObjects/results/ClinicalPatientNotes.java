package com.bosch.tmp.integration.businessObjects.results;

import java.util.ArrayList;
import java.util.List;

import com.bosch.th.cia.Note;

public class ClinicalPatientNotes {

	private String patientId ;
	private List<Note> notes;

	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public List<Note> getNotes() {
		if(notes == null){
			notes = new ArrayList<Note>();
		}
		return this.notes;
	}
}
