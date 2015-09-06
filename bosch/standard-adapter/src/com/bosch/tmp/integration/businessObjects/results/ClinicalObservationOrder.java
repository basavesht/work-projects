package com.bosch.tmp.integration.businessObjects.results;

import java.util.ArrayList;
import java.util.List;

import org.hl7.v2xml.NTECONTENT;
import org.hl7.v2xml.OBRCONTENT;
import org.hl7.v2xml.OBXCONTENT;

/**
 * Defines the ORDER per session...
 * @author BEA2KOR
 *
 */
public class ClinicalObservationOrder 
{
	private List<OBXCONTENT> clinicalObservations = null;
	private List<NTECONTENT> clinicalNotes = null;
	private OBRCONTENT observationRequest = null;
	private String sessionId = null;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public List<OBXCONTENT> getClinicalObservations() {
		if(clinicalObservations == null){
			clinicalObservations = new ArrayList<OBXCONTENT>();
		}
		return this.clinicalObservations;
	}

	public List<NTECONTENT> getClinicalNotes() {
		if(clinicalNotes == null){
			clinicalNotes = new ArrayList<NTECONTENT>();
		}
		return this.clinicalNotes;
	}

	public OBRCONTENT getObservationRequest() {
		return observationRequest;
	}
	public void setObservationRequest(OBRCONTENT observationRequest) {
		this.observationRequest = observationRequest;
	}
}
