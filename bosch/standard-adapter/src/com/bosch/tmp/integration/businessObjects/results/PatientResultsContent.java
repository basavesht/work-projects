/**
 *
 */
package com.bosch.tmp.integration.businessObjects.results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bosch.th.integrationpatient.IntegrationPatientType;
import com.bosch.th.patientrecord.MeterMeasurement;
import com.bosch.th.patientrecord.SurveyResponse;

/**
 * PatientResultBean implementation class..
 * @author BEA2KOR
 */
public class PatientResultsContent
{
	private List<SurveyResponse> surveyResponse;
	private List<MeterMeasurement> meterMeasurement;
	private ClinicalPatientNotes patientNotes;
	private Map<Long, ClinicalSurveyNotes> surveyNotes;
	private IntegrationPatientType integrationPatient;

	public List<SurveyResponse> getSurveyResponse() {
		if(surveyResponse == null){
			surveyResponse = new ArrayList<SurveyResponse>();
		}
		return this.surveyResponse;
	}

	public ClinicalPatientNotes getPatientNotes() {
		if(patientNotes == null){
			patientNotes = new ClinicalPatientNotes();
		}
		return this.patientNotes;
	}

	public Map<Long, ClinicalSurveyNotes> getSurveyNotes() {
		if(surveyNotes == null){
			surveyNotes = new HashMap<Long, ClinicalSurveyNotes>();
		}
		return this.surveyNotes;
	}

	public List<MeterMeasurement> getMeterMeasurement() {
		if(meterMeasurement == null){
			meterMeasurement = new ArrayList<MeterMeasurement>();
		}
		return this.meterMeasurement;
	}

	public IntegrationPatientType getIntegrationPatient() {
		return integrationPatient;
	}
	public void setIntegrationPatient(IntegrationPatientType integrationPatient) {
		this.integrationPatient = integrationPatient;
	}
}
